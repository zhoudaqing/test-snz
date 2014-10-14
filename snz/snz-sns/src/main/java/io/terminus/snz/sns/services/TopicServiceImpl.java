package io.terminus.snz.sns.services;

import com.google.common.base.*;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.html.HtmlEscapers;
import io.terminus.common.exception.ServiceException;
import io.terminus.common.model.PageInfo;
import io.terminus.common.model.Paging;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.message.models.Message;
import io.terminus.snz.message.services.MessageService;
import io.terminus.snz.requirement.dto.RequirementDto;
import io.terminus.snz.requirement.dto.TopicUser;
import io.terminus.snz.requirement.service.RequirementService;
import io.terminus.snz.sns.daos.mysql.ReplyDao;
import io.terminus.snz.sns.daos.mysql.TopicDao;
import io.terminus.snz.sns.daos.mysql.TopicUserDao;
import io.terminus.snz.sns.daos.redis.RedisReplyDao;
import io.terminus.snz.sns.dtos.RequirementCount;
import io.terminus.snz.sns.dtos.TopicDetail;
import io.terminus.snz.sns.dtos.UserReplyCount;
import io.terminus.snz.sns.managers.TopicManager;
import io.terminus.snz.sns.models.Topic;
import io.terminus.snz.statistic.model.RequirementCountType;
import io.terminus.snz.statistic.service.RequirementCountService;
import io.terminus.snz.user.model.Company;
import io.terminus.snz.user.model.User;
import io.terminus.snz.user.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-5-3
 */
@Service
@Slf4j
public class TopicServiceImpl implements TopicService {

    @Autowired
    private TopicDao topicDao;

    @Autowired
    private TopicManager topicManager;

    @Autowired
    private ReplyDao replyDao;

    @Autowired
    private RedisReplyDao redisReplyDao;

    @Autowired
    private TopicUserDao topicUserDao;

    @Autowired
    private ReplyService replyService;

    @Autowired
    private RequirementService requirementService;

    @Autowired
    private RequirementCountService requirementCountService;    //需求话题统计数据信息

    @Autowired
    private CompanyService companyService;

    @Autowired
    private MessageService messageService;

    private Splitter commaSplitter = Splitter.on(",").omitEmptyStrings().trimResults();

    private Joiner commaJoiner = Joiner.on(",");

    @Override
    public Response<Integer> create(BaseUser user, Topic t, String joinerIds) {
        Response<Integer> resp = new Response<Integer>();
        try {
            if (user == null){
                log.error("user doesn't login");
                throw new ServiceException("user.not.login");
            }
            t.setUserId(user.getId());
            // escape
            t.setContent(HtmlEscapers.htmlEscaper().escape(t.getContent()));
            if (User.is(user, User.Type.PURCHASER)){
                //采购商保存姓名
                t.setUserName(user.getName());
            } else if (User.is(user, User.Type.SUPPLIER)){
                //供应商保存公司名
                Response<Company> companyResp = companyService.findCompanyByUserId(user.getId());
                if (!companyResp.isSuccess()){
                    resp.setError(companyResp.getError());
                    return resp;
                }
                Company company = companyResp.getResult();
                t.setUserName(company.getCorporation());
            }

            // 设置需求的冗余字段
            checkRequirement(t.getReqId());
            Response<RequirementDto> rrd = requirementService.findById(t.getReqId());
            if (!rrd.isSuccess()){
                log.error("failed to find requirement(id={}): {}", t.getReqId(), rrd.getError());
                resp.setError(rrd.getError());
                return resp;
            }
            RequirementDto rd = rrd.getResult();
            if (rd == null){
                log.error("requirement(id={}) isn't exist", t.getReqId());
                resp.setError("requirement.not.exsit");
                return resp;
            }
            t.setReqId(rd.getRequirement().getId());
            t.setReqStatus(rd.getRequirement().getStatus());
            t.setReqName(rd.getRequirement().getName());
            // 获取公司名
            // 若为供应商
            if (User.is(user, User.Type.SUPPLIER)){
                Response<Company> companyResp = companyService.findCompanyByUserId(user.getId());
                if (!companyResp.isSuccess()){
                    resp.setError(companyResp.getError());
                    return resp;
                }
                Company company = companyResp.getResult();
                if (company == null){
                    resp.setError("The user(id={})'s company isn't existed.");
                    return resp;
                }
                t.setCompanyName(company.getCorporation());
            } else {
                t.setCompanyName("海尔");
            }

            //公开话题
            if (t.getScope() == Topic.Scope.PUBLIC.value()){
                resp = createPublic(t);
            } else{
                if (Strings.isNullOrEmpty(joinerIds)){
                    log.error("circle topic's joinerIds can't be empty");
                    resp.setError("topic.joinerIds.empty");
                    return resp;
                }
                List<Long> jids = toListLong(joinerIds);
                resp = createNonPublic(t, jids);
            }

            //写入采购商发布的话题数量+1
            requirementCountService.setReqCountInfo(t.getReqId() , RequirementCountType.TOPIC_NUM, 1);
        } catch (ServiceException e){
            resp.setError(e.getMessage());
        } catch (Exception e){
            log.error("failed to create topic({}), cause: {}",
                            t, Throwables.getStackTraceAsString(e));
            resp.setError("topic.create.fail");
        }
        resp.setResult(t.getId()==null ? 1 : t.getId().intValue());
        return resp;
    }

    /**
     * 检查需求信息
     * @param reqId 需求id
     */
    private void checkRequirement(Long reqId) {
        if (reqId == null || reqId < 0L){
            log.error("requirement id is invalid.");
            throw new ServiceException("requirement.id.invalid");
        }

    }

    @Override
    public Response<Integer> createDefault(BaseUser user, Topic t) {
        t.setTitle(Topic.DEFAULT.getTitle());
        t.setContent(Topic.DEFAULT.getContent());
        t.setScope(Topic.DEFAULT.getScope());
        return create(user, t, null);
    }

    private List<Long> toListLong(String joinerIds) {
        List<String> jidsStr = commaSplitter.splitToList(joinerIds);
        return Lists.transform(jidsStr, new Function<String, Long>() {
            @Override
            public Long apply(String idStr) {
                return Long.valueOf(idStr);
            }
        });
    }

    /**
     * 创建公共话题
     * @param t 话题对象
     * @return 创建记录数
     */
    private Response<Integer> createPublic(Topic t) {
        Response<Integer> resp = new Response<Integer>();
        try {
            paramsValidate(resp, t, "createPublic");
            if (!resp.isSuccess()){
                return resp;
            }
            int inserted = topicDao.create(t);
            resp.setResult(inserted);
        }catch (Exception e) {
            log.error("fail to createPublic topic{}, cause:{}", t, Throwables.getStackTraceAsString(e));
            resp.setError("topic.createPublic.fail");
        }
        return resp;
    }

    /**
     * 创建非公开话题
     * @param t 话题对象
     * @param joinerIds 参与者id列表
     * @return 创建记录数
     */
    private Response<Integer> createNonPublic(Topic t, List<Long> joinerIds) {
        Response<Integer> resp = new Response<Integer>();

        try {
            paramsValidate(resp, t, "createNonPublic");
            if (!resp.isSuccess()){
                return resp;
            }

            // 判断用户是否在可选圈子用户内, 可选圈子用户为提交过方案的供应商和需求的团队成员
            usersValid(t, joinerIds);

            t.setJoiners(joinerIds.size());
            int inserted = topicManager.createNonPublic(t, joinerIds);
            resp.setResult(inserted);
            if (inserted == 1){
                //推送消息
                messageService.push(Message.Type.TOPIC_CREATE, t.getUserId(), joinerIds, t);
            }
        } catch (ServiceException e){
          resp.setError(e.getMessage());
        } catch (Exception e) {
            log.error("fail to createNonPublic topic{}, cause:{}", t, Throwables.getStackTraceAsString(e));
            resp.setError("topic.createNonPublic.fail");
        }
        return resp;
    }

    /**
     * 验证是否所选的圈子用户合法
     */
    private void usersValid(Topic t, List<Long> joinerIds) {
        Response<List<TopicUser>> resp =  requirementService.findTopicPeople(t.getReqId());

        if (!resp.isSuccess()){
            log.error("failed to find the requirement(id={}) topic circle people", t.getReqId());
            throw new ServiceException("requirement.topicusers.find.fail");
        }
        List<TopicUser> tus = resp.getResult();

        //没有可选的圈子用户
        if (Iterables.isEmpty(tus)){
            log.error("topic circle people is empty, requirement id={}.", t.getReqId());
            throw new ServiceException("requirement.circlepeople.empty");
        }
        // 获取解决方案对应的公司id
        List<Long> userIds = Lists.transform(tus, new Function<TopicUser, Long>() {
            @Override
            public Long apply(TopicUser tu) {
                return tu.getUserId();
            }
        });

        // 有用户不在圈子内
        if (!userIds.containsAll(joinerIds)){
            log.error("some joiners({}) aren't in this requirement(id={}) circle(ids=({})).",
                    joinerIds, t.getReqId(), userIds);
            throw new ServiceException("requirement.circle.exclude");
        }
    }

    @Override
    public Response<TopicDetail> findById(BaseUser user, Long id, Boolean isView) {
        //默认增加浏览量
        Boolean viewed = isView;
        if (viewed == null){
            viewed = Boolean.TRUE;
        }
        Response<TopicDetail> resp = new Response<TopicDetail>();
        try{
            if (id == null || id <= 0){
                log.error("topic(id={}) must > 0 when find topic", id);
                resp.setError("topic.id.invalid");
                return resp;
            }
            Topic t = topicDao.findById(id);
            if (t == null){
                log.error("topic(id={}) isn't existed", id);
                resp.setError("topic.not.exist");
                return resp;
            }
            if (t.getScope() != Topic.Scope.PUBLIC.value()){
                boolean isOwner = Objects.equal(user.getId(), t.getUserId());
                // 既不是创建话题的用户，也不在话题圈子内
                if (!isOwner && !isInCircle(user.getId(), t.getId())){
                    resp.setError("topic.notin.circle");
                    return resp;
                }
            }
            TopicDetail td = new TopicDetail(t);
            try{
                td.setCanReply(replyService.validReqTopicStatus(t));
            } catch (Exception e){
                td.setCanReply(Boolean.FALSE);
            }

            if(!t.getScope().equals(Topic.Scope.PUBLIC)){
                td.setFriends(topicUserDao.findFriends(t));
            }

            // 查询回复过话题的所有供应商列表
            List<User> suppliers = replyDao.listTopicSuppliers(id);
            // 查询各供应商的回复数目
            List<UserReplyCount> userReplies1 = usersReplyCounts(t.getId(), suppliers);
            td.setSupplierReplies(userReplies1);

            // 查询回复过话题的所有采购商列表
            List<User> purchasers = replyDao.listTopicPurchasers(id);
            // 查询各采购商的回复数目
            List<UserReplyCount> userReplies2 = usersReplyCounts(t.getId(), purchasers);
            td.setPurchaserReplies(userReplies2);

            resp.setResult(td);
            if (viewed) {
                //浏览量+1
                viewed(id);
            }
        } catch(Exception e){
            log.error("fail to find topic(id={}), cause:{}", id, Throwables.getStackTraceAsString(e));
            resp.setError("topic.find.fail");
        }
        return resp;
    }

    /**
     * 查询用户(供应商、采购商)对应话题的回复数
     * @param tid 话题id
     * @param users 供应商列表
     * @return 用户回复统计对象列表
     */
    private List<UserReplyCount> usersReplyCounts(Long tid, List<User> users) {
        if (Iterables.isEmpty(users)){
            return Collections.emptyList();
        }

        List<Long> userIds = Lists.transform(users, new Function<User, Long>() {
            @Override
            public Long apply(User u) {
                return u.getId();
            }
        });

        StringBuilder userIdsStr = new StringBuilder(commaJoiner.join(userIds));
        userIdsStr.insert(0, ",").append(",");

        List<Long> userReplyCounts =
                replyDao.usersReplyCounts(tid, userIds, userIdsStr.toString());

        List<UserReplyCount> userReplies = Lists.newArrayListWithCapacity(users.size());
        for (int i=0; i<userIds.size(); i++){
            if(User.Type.SUPPLIER.value() == users.get(i).getType()){
                Response<Company> company = companyService.findCompanyByUserId(users.get(i).getId());
                if(company.getResult()!=null){
                    users.get(i).setName(company.getResult().getCorporation());
                }
            }
            userReplies.add(new UserReplyCount(users.get(i), userReplyCounts.get(i)));
        }
        return userReplies;
    }

    private Response<Paging<Topic>> paging(BaseUser user, Map<String, Object> criteria, Integer pageNo, Integer pageSize) {
        Response<Paging<Topic>> result = new Response<Paging<Topic>>();
        try{
            if (user == null){
                log.error("user doesn't login.");
                result.setError("user.not.login");
                return result;
            }
            criteria.put("userId", user.getId());
            PageInfo page = new PageInfo(pageNo, pageSize);
            Paging<Topic> topicPaging = topicDao.pagingOfReq(criteria, page.getOffset(), page.getLimit());
            result.setResult(topicPaging);
        } catch(Exception e){
            log.error("failed to paging topic(pageNo={}, pageSize={})",
                    pageNo, pageSize, Throwables.getStackTraceAsString(e));
            result.setError("topic.paging.fail");
        }
        return result;
    }

    @Override
    public Response<Paging<Topic>> pagingPublics(BaseUser user, Long reqId, String reqStatus, Integer pageNo, Integer pageSize) {
        Map<String, Object> criteria = Maps.newHashMap();
        criteria.put("reqId", reqId);
        if(!Strings.isNullOrEmpty(reqStatus)){
            try {
                criteria.put("reqStatus", commaSplitter.splitToList(reqStatus));
            } catch (Exception e) {
                log.error("Illegal param:reqStatus, as value:[{}]", reqStatus,e);
            }
        }
        return paging(user, criteria, pageNo, pageSize);
    }

    @Override
    public Response<Paging<Topic>> pagingByJoinerId(BaseUser user, Long joinderId, Integer pageNo, Integer pageSize) {
        PageInfo page = new PageInfo(pageNo, pageSize);
        Response<Paging<Topic>> result = new Response<Paging<Topic>>();
        try{
            if (user == null){
                log.error("user doesn't login.");
                result.setError("user.not.login");
                return result;
            }
            Paging<Topic> sbSet = topicDao.pagingByJoinerId(joinderId, page.getOffset(), page.getLimit());
            result.setResult(sbSet);
        } catch(Exception e){
            log.error("failed to paging topic by joiner id (joinerId={}, pageNo={}, pageSize={})",
                    joinderId, pageNo, pageSize, Throwables.getStackTraceAsString(e));
            result.setError("topic.pagingbyjoinerid.fail");
        }
        return result;
    }

    @Override
    public Response<Boolean> viewed(Long id) {
        Response<Boolean> resp = new Response<Boolean>();
        try{
            topicDao.viewed(id);
            resp.setResult(Boolean.TRUE);
        } catch (Exception e){
            log.error("failed to update topic(id={})'s total views", id);
            resp.setError("topic.viewed.fail");
        }
        return resp;
    }

    @Override
    public Response<Boolean> replied(Long id) {
        Response<Boolean> resp = new Response<Boolean>();
        try{
            topicDao.replied(id);
            resp.setResult(Boolean.TRUE);
        } catch (Exception e){
            log.error("failed to update topic(id={})'s total replies", id);
            resp.setError("topic.replied.fail");
        }
        return resp;
    }

    @Override
    public Response<List<RequirementCount>> getRequirementsCountInfo(String ids) {
        Response<List<RequirementCount>> resp = new Response<List<RequirementCount>>();
        try{
            if (Strings.isNullOrEmpty(ids)){
                log.error("topic ids({}) can not empty when delete topic", ids);
                resp.setError("topic.ids.invalid");
                return resp;
            }
            List<Long> idsLong = toListLong(ids);
            // 1.获取需求对应的话题数
            List<Long> topics = topicDao.countRequirementTopics(idsLong);
            // 2.获取需求参与的供应商数
            List<Long> suppliers = redisReplyDao.countRequirementSuppliers(idsLong);

            List<RequirementCount> requirementCounts = buildReqCounts(idsLong, topics, suppliers);
            resp.setResult(requirementCounts);
        } catch (Exception e){
            log.error("failed to get requirements(ids={})'s count info, cause: {}",
                    ids, Throwables.getStackTraceAsString(e));
            resp.setError("topic.getreqcountinfo.fail");
        }
        return resp;
    }

    private List<RequirementCount> buildReqCounts(List<Long> idsLong, List<Long> topics, List<Long> suppliers) {
        List<RequirementCount> requirementCounts = Lists.newArrayListWithCapacity(idsLong.size());
        RequirementCount rc;
        for(int i=0; i<idsLong.size(); i++){
            rc = new RequirementCount(idsLong.get(i), topics.get(i), suppliers.get(i));
            requirementCounts.add(rc);
        }
        return requirementCounts;
    }

    /**
     * 认证当前用户是否在圈子里
     * @param tid 话题 id
     * @return 在返回true, 反之false
     */
    private boolean isInCircle(Long userId, Long tid) {
        return topicUserDao.exist(userId, tid);
    }

    /**
     * 参数验证
     */
    private void paramsValidate(Response<Integer> resp, Topic t, String op) {
        if(t == null) {
            log.error("topic can not be null when {} topic", op);
            resp.setError("topic.id.invalid");
            return;
        }

        if (t.getUserId() == null||t.getUserId()<=0){
            log.error("user id must > 0 when {} topic", op);
            resp.setError("user.id.invalid");
            return;
        }

        if(Strings.isNullOrEmpty(t.getTitle())) {
            log.error("topic title can not be empty when {} topic", op);
            resp.setError("topic.title.empty");
            return;
        }

        if (Strings.isNullOrEmpty(t.getCompanyName())){
            log.error("topic company name can not be empty when {} topic", op);
            resp.setError("topic.companyname.empty");
            return;
        }

        if (Strings.isNullOrEmpty(t.getContent())){
            log.error("topic content can not be empty when {} topic", op);
            resp.setError("topic.content.empty");
            return;
        }

        // 对应的需求id
        if (t.getReqId() == null || t.getReqId() <= 0){
            log.error("requirement id must > 0 when {} topic", op);
            resp.setError("topic.reqid.invalid");
            return;
        }

        // 对应的需求状态限制-- 暂时取消，不限制需求状态
        /*
        if (t.getReqStatus() == null || t.getReqStatus() <= 0){
            log.error("requirement status can't empty when {} topic", op);
            resp.setError("topic.reqstatus.invalid");
            return;
        }
        */

        //参数验证通过
        resp.setSuccess(Boolean.TRUE);
    }
}
