package io.terminus.snz.sns.services;

import com.google.common.base.Objects;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
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
import io.terminus.snz.requirement.model.Requirement;
import io.terminus.snz.requirement.service.RequirementService;
import io.terminus.snz.sns.daos.mysql.ReplyDao;
import io.terminus.snz.sns.daos.mysql.TopicDao;
import io.terminus.snz.sns.daos.mysql.TopicUserDao;
import io.terminus.snz.sns.dtos.FatReply;
import io.terminus.snz.sns.dtos.TopicDetail;
import io.terminus.snz.sns.managers.ReplyManager;
import io.terminus.snz.sns.models.Reply;
import io.terminus.snz.sns.models.Topic;
import io.terminus.snz.user.model.Company;
import io.terminus.snz.user.model.CompanyMainBusiness;
import io.terminus.snz.user.model.ContactInfo;
import io.terminus.snz.user.model.User;
import io.terminus.snz.user.service.AccountService;
import io.terminus.snz.user.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * MADE IN IntelliJ IDEA
 * AUTHOR: haolin
 * AT 4/5/14.
 */
@Service
@Slf4j
public class ReplyServiceImpl implements ReplyService {
    @Autowired
    private TopicDao topicDao;

    @Autowired
    private ReplyDao replyDao;

    @Autowired
    private TopicService topicService;

    @Autowired
    private TopicUserDao topicUserDao;

    @Autowired
    private ReplyManager replyManager;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private AccountService<User> accountService;

    @Autowired
    private RequirementService requirementService;

    @Autowired
    private MessageService messageService;

    private Splitter commaSplitter = Splitter.on(",").trimResults().omitEmptyStrings();

    @Override
    public Response<Reply> create(BaseUser user, Reply r) {
        Response<Reply> resp = new Response<Reply>();
        try {
            if (user == null) {
                log.error("user isn't login");
                resp.setError("user.not.login");
                return resp;
            }
            Long userId = user.getId();
            if (User.is(user, User.Type.PURCHASER)) {    //采购商，现系统就只有海尔
                r.setCompanyName(user.getName());   //就用用户名
            } else {
                Response<Company> rc = companyService.findCompanyByUserId(userId);
                Company company = rc.getResult();
                r.setCompanyName(company.getCorporation());
            }
            r.setUserId(userId);

            if (r.getReceiverId() != null) {
                Reply _receiver = replyDao.findOneByCreatorId(r.getReceiverId());
                if (_receiver != null) {
                    r.setReceiverName(_receiver.getCompanyName());
                }
            }

            // 话题权限验证
            Response<TopicDetail> tR = topicService.findById(user, r.getTopicId(), Boolean.FALSE);
            if (!tR.isSuccess()) {
                log.error("fail to find topic(id={})", r.getTopicId());
                resp.setError("topic.find.fail");
                return resp;
            }
            Topic t = tR.getResult();
            if (t == null) {
                log.error("fail to create reply, topic doesn't exist");
                resp.setError("topic.not.exist");
                return resp;
            }
            // 话题已关闭
            if (t.getClosed() == 1) {
                log.error("fail to reply topic, topic(id={}) not exist", t.getId());
                resp.setError("topic.closed");
                return resp;
            }

            int inserted;
            boolean isCircle = t.getScope() != Topic.Scope.PUBLIC.value();
            if (isCircle) {    //非公共话题，验证用户是否在话题圈内
                boolean isOwner = Objects.equal(user.getId(), t.getUserId());
                if (!isOwner && !topicUserDao.exist(r.getUserId(), r.getTopicId())) {
                    log.error("reply topic(id={}) is denied", r.getTopicId());
                    resp.setError("topic.reply.deny");
                    return resp;
                }
            }
            // 验证需求和话题的状态是否一致
            if (!validReqTopicStatus(t)) {
                throw new ServiceException("requirement.topic.status.isnt.same");
            }
            r.setReqId(t.getReqId());
            // escape
            r.setContent(HtmlEscapers.htmlEscaper().escape(r.getContent()));
            // 增加需求下的参与供应商(以是否回复过该需求下任意话题为标准)
            if (User.is(user, User.Type.SUPPLIER)) {
                //将供应商回复写入需求的统计数据
                inserted = replyManager.create(r);
            } else {
                inserted = replyDao.create(r);
            }

            if (isCircle && inserted == 1) {
                // 查询改圈子下的用户id列表
                List<Long> joinerIds = topicUserDao.findUserIdsByTopicId(r.getTopicId());
                // 推送消息
                messageService.push(Message.Type.REPLY_CREATE, r.getUserId(), joinerIds, ImmutableMap.of("companyName", r.getCompanyName(), "t", t));
            }
            resp.setResult(r);
            // 更新topic回复数
            topicService.replied(r.getTopicId());
        } catch (ServiceException e) {
            resp.setError(e.getMessage());
        } catch (Exception e) {
            log.error("fail to create reply{}, cause:{}", r, Throwables.getStackTraceAsString(e));
            resp.setError("topic.reply.fail");
        }
        return resp;
    }

    /**
     * 验证需求和话题状态是否一致
     *
     * @param t 话题对象
     */
    public boolean validReqTopicStatus(Topic t) {
        Long reqId = t.getReqId();
        Response<RequirementDto> resp = requirementService.findById(reqId);
        if (!resp.isSuccess()) {
            log.error("failed to find requiremnt(id={}).", reqId);
            throw new ServiceException("requirement.find.fail");
        }
        if (resp.getResult() == null) {
            log.error("the requirement(id={}) isn't existed.", reqId);
            throw new ServiceException("requirement.not.exsit");
        }
        Requirement req = resp.getResult().getRequirement();
        if (!Objects.equal(t.getReqStatus(), req.getStatus())) {
            log.error("current topic(id={})'s reqStatus isn't equal to requirement(id={})'s status", t.getId(), reqId);
            //throw new ServiceException("requirement.topic.status.isnt.same");
            return false;
        }
        return true;
    }

    @Override
    public Response<Paging<FatReply>> pagingSimple(BaseUser user, Long topicId, Integer pageNo, Integer pageSize) {
        Response<Paging<FatReply>> resp = new Response<Paging<FatReply>>();
        try {
            if (user == null) {
                log.error("user isn't login");
                resp.setError("user.not.login");
                return resp;
            }
            Topic t = topicDao.findById(topicId);
            if (t == null) {
                log.error("topic(id={}) isn't exsit.");
                resp.setError("topic.not.exsit");
                return resp;
            }
            if (t.getScope() != Topic.Scope.PUBLIC.value()) {
                // 验证当前用户是否在圈子内
                boolean isOwner = Objects.equal(user.getId(), t.getUserId());
                if (!isOwner && !isInCircle(user.getId(), t.getId())) {
                    resp.setError("user.notin.circle");
                    return resp;
                }
            }
            PageInfo pi = new PageInfo(pageNo, pageSize);
            Paging<Reply> replyPaging;

            // 对于圈内私密话题(scope=3)的回复消息,当前用户是该话题的创建者、或者参与过该话题的回复，都可以看到一级回复
            if (Objects.equal(t.getScope(), Topic.Scope.CIRCLE_PRIVATE.value()) && !Objects.equal(t.getUserId(), user.getId())) {
                replyPaging = replyDao.pagingForPrivate(topicId, t.getUserId(), user.getId(), pi.getOffset(), pi.getLimit());
            }
            // 公共话题或圈子对回复无可见性限制，当前用户可以看到 scope=1,2 的话题的所有一级回复
            else {
                replyPaging = replyDao.pagingForAll(topicId, pi.getOffset(), pi.getLimit());
            }
            Paging<FatReply> res = render2FatReply(user, t, replyPaging);
            resp.setResult(res);
        } catch (ServiceException e) {
            resp.setError(e.getMessage());
        } catch (Exception e) {
            log.error("failed to paging reply of topic(id={}),pageNo={},pageSize={}, cause:{}", topicId, pageNo, pageSize, Throwables.getStackTraceAsString(e));
            resp.setError("topic.reply.paging.fail");
        }
        return resp;
    }

    /**
     * 将回复转换为FatReply
     *
     * @param user        当前操作用户
     * @param topic       当前话题
     * @param replyPaging Reply分页对象  @return FatReply分页对象
     */
    private Paging<FatReply> render2FatReply(BaseUser user, Topic topic, Paging<Reply> replyPaging) {
        if (Iterables.isEmpty(replyPaging.getData())) {
            return new Paging<FatReply>(0L, Collections.<FatReply>emptyList());
        }

        List<FatReply> fatReplies = Lists.newArrayListWithCapacity(replyPaging.getData().size());
        FatReply fatReply;
        for (Reply firstReply : replyPaging.getData()) {
            firstReply.setFollowReplies(replyDao.findByPid(firstReply.getId(), topic.getScope(), topic.getUserId(), firstReply.getUserId(), user.getId()));
            fatReply = new FatReply(firstReply);
            fatReplies.add(fatReply);
        }

        List<Long> userIds = Lists.newArrayList();
        for (Reply r : replyPaging.getData()) {
            userIds.add(r.getUserId());
        }

        Response<List<User>> usersResp = accountService.findUserByIds(userIds);
        if (!usersResp.isSuccess()) {
            log.error("failed to find users by ids={}, cause: {}", userIds, usersResp.getError());
            throw new ServiceException(usersResp.getError());
        }

        List<Long> supplierIds = Lists.newArrayList();
        List<User> suppliers = Lists.newArrayList();
        List<User> purchasers = Lists.newArrayList();
        for (User u : usersResp.getResult()) {
            if (User.is(u, User.Type.SUPPLIER)) {
                supplierIds.add(u.getId());
                suppliers.add(u);
            } else if (User.is(u, User.Type.PURCHASER)) {
                purchasers.add(u);
            }
        }

        // 填充采购商附加信息
        fillUserInfos(purchasers, fatReplies);

        // 填充供应商附加信息
        // 填充主营业务
        fillMainBussinesses(supplierIds, fatReplies);

        // 填充公司标签
        fillTags(suppliers, fatReplies);

        // 填充联系人信息
        fillContactInfos(supplierIds, fatReplies);

        return new Paging<FatReply>(replyPaging.getTotal(), fatReplies);

    }


    /**
     * 填充采购商用户信息
     *
     * @param purchasers 采购商用户
     * @param fatReplies 回复对象列表
     */
    private void fillUserInfos(List<User> purchasers, List<FatReply> fatReplies) {
        for (User p : purchasers) {
            for (FatReply fatReply : fatReplies) {
                if (Objects.equal(p.getId(), fatReply.getUserId())) {
                    fatReply.setIsSupplier(Boolean.FALSE);
                    fatReply.setUser(p);
                }
            }
        }
    }

    /**
     * 填充回复主营业务
     *
     * @param userIds    用户id列表
     * @param fatReplies FatReply对象
     */
    private void fillMainBussinesses(List<Long> userIds, List<FatReply> fatReplies) {
        Response<Map<Long, List<CompanyMainBusiness>>> resp = companyService.findMainBussinessByUserIds(userIds);
        if (!resp.isSuccess()) {
            throw new ServiceException(resp.getError());
        }
        Map<Long, List<CompanyMainBusiness>> data = resp.getResult();
        if (data != null && data.size() > 0) {
            List<CompanyMainBusiness> businesses;
            List<String> businessNames;
            for (FatReply fatReply : fatReplies) {
                businesses = data.get(fatReply.getUserId());
                if (businesses != null && businesses.size() > 0) {
                    businessNames = Lists.newArrayList();
                    for (CompanyMainBusiness mainBusiness : businesses) {
                        businessNames.add(mainBusiness.getName());
                    }
                    fatReply.setBusinesses(businessNames);
                }
            }
        }
    }

    /**
     * 填充回复标签
     *
     * @param users      用户列表
     * @param fatReplies FatReply对象
     */
    private void fillTags(List<User> users, List<FatReply> fatReplies) {
        if (!Iterables.isEmpty(users)) {
            Map<Long, User> mapUsers = toMap(users);
            User u;
            List<String> tags = null;
            for (FatReply fatReply : fatReplies) {
                u = mapUsers.get(fatReply.getUserId());
                if (u != null && !Strings.isNullOrEmpty(u.getTags())) {
                    tags = commaSplitter.splitToList(u.getTags());
                    fatReply.setTags(tags);
                }
            }
        }
    }

    /**
     * user对象列表转换为map对象, <userId, user>
     *
     * @param users 用户列表对象
     * @return 用户map对象
     */
    private Map<Long, User> toMap(List<User> users) {
        Map<Long, User> mapUsers = Maps.newHashMapWithExpectedSize(users.size());
        for (User u : users) {
            mapUsers.put(u.getId(), u);
        }
        return mapUsers;
    }

    /**
     * 填充联系人信息
     *
     * @param userIds    用户id列表
     * @param fatReplies FatReply对象
     */
    private void fillContactInfos(List<Long> userIds, List<FatReply> fatReplies) {
        Response<Map<Long, ContactInfo>> resp = companyService.findContactInfoByUserIds(userIds);
        if (!resp.isSuccess()) {
            throw new ServiceException(resp.getError());
        }
        Map<Long, ContactInfo> data = resp.getResult();
        if (data != null && data.size() > 0) {
            ContactInfo info;
            for (FatReply fatReply : fatReplies) {
                info = data.get(fatReply.getUserId());
                fatReply.setContactInfo(info);
            }
        }
    }

    /**
     * 认证当前用户是否在圈子里
     *
     * @param tid 话题 id
     * @return 在返回true, 反之false
     */
    private boolean isInCircle(Long userId, Long tid) {
        if (!topicUserDao.exist(userId, tid)) {
            return false;
        }
        return true;
    }
}
