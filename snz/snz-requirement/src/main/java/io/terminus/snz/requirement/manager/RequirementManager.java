package io.terminus.snz.requirement.manager;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.terminus.common.utils.JsonMapper;
import io.terminus.common.utils.Splitters;
import io.terminus.pampas.common.Response;
import io.terminus.snz.haier.event.PLMEventBus;
import io.terminus.snz.message.models.Mail;
import io.terminus.snz.message.models.Message;
import io.terminus.snz.message.services.MailService;
import io.terminus.snz.message.services.MessageService;
import io.terminus.snz.related.dto.ParkFactoryDto;
import io.terminus.snz.related.services.DeliveryService;
import io.terminus.snz.requirement.dao.*;
import io.terminus.snz.requirement.dto.BackendJSON;
import io.terminus.snz.requirement.dto.RequirementDetailDto;
import io.terminus.snz.requirement.dto.RequirementDto;
import io.terminus.snz.requirement.dto.TopicUser;
import io.terminus.snz.requirement.event.RequirementEventBus;
import io.terminus.snz.requirement.model.*;
import io.terminus.snz.requirement.service.DepositService;
import io.terminus.snz.requirement.service.RequirementQuotaService;
import io.terminus.snz.requirement.tool.FlowNumber;
import io.terminus.snz.statistic.model.PurchaserRequirementCount;
import io.terminus.snz.statistic.model.RequirementCountType;
import io.terminus.snz.statistic.model.SolutionCountType;
import io.terminus.snz.statistic.service.RequirementCountService;
import io.terminus.snz.statistic.service.SolutionCountService;
import io.terminus.snz.user.model.User;
import io.terminus.snz.user.service.AccountService;
import io.terminus.snz.user.service.PurchaserAuthorityService;
import io.terminus.snz.user.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * Desc:需求管理整体处理
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-04.
 */
@Slf4j
@Component
public class RequirementManager {
    //方案的最小数量3个
    private static final int MIN_SOLUTION = 3;

    private static final JsonMapper JSON_MAPPER = JsonMapper.JSON_NON_DEFAULT_MAPPER;

    @Autowired
    private RequirementDao requirementDao;                  //整体需求管理

    @Autowired
    private ModuleDao moduleDao;                            //需求具体模块管理

    @Autowired
    private ModuleFactoryDao moduleFactoryDao;              //模块工厂数据管理

    @Autowired
    private RequirementTeamDao requirementTeamDao;          //需求团队管理

    @Autowired
    private RequirementTimeDao requirementTimeDao;          //需求时间管理

    @Autowired
    private ReqPredictTimeDao reqPredictTimeDao;            //预计需求的时间管理

    @Autowired
    private RequirementSolutionDao requirementSolutionDao;  //需求方案管理

    @Autowired
    private RequirementSendDao requirementSendDao;          //需求数据变迁的状态管理

    @Autowired
    private AccountService accountService;                  //用户管理

    @Autowired
    private RequirementEventBus reqEventBus;                //需求事件订阅

    @Autowired
    private CountManager countManager;                      //统计管理数据

    @Autowired
    private PLMEventBus plmEventBus;                        //plm模块的事件对象

    @Autowired
    private MessageService messageService;                  //消息中心管理

    @Autowired
    private MailService mailService;                        //邮件服务管理

    @Autowired
    private RequirementCountService requirementCountService;//需求统计数据

    @Autowired
    private DepositService depositService;                  //保证金管理

    @Autowired
    private DeliveryService deliveryService;                //工厂管理

    @Autowired
    private RequirementQuotaService requirementQuotaService;//需求配额逻辑

    @Autowired
    private PurchaserAuthorityService purchaserAuthorityService;//细分到类目的权限管理

    @Autowired
    private TagService tagService;                              //为供应商打标签

    @Autowired
    private SolutionCountService solutionCountService;          //方案统计管理

    /**
     * 创建具体的需求信息内容(使用事务管理处理)
     * @param requirementDto 需求对象
     */
    @Transactional
    public Long createRequirement(RequirementDto requirementDto){
        //创建需求详细信息
        Requirement requirement = requirementDto.getRequirement();
        //写入唯一的需求流水码
        requirement.setFlowId(FlowNumber.createReqFlowNum(requirement.getProductName(), requirement.getMaterielName(), requirement.getTacticsId()));
        Long requirementId = requirementDao.create(requirement);
        log.debug("create requirement info success.");

        //创建详细需求模块团队设定
        for(RequirementTeam team : requirementDto.getTeamList()){
            team.setRequirementId(requirementId);
            team.setRequirementName(requirement.getName());
        }
        requirementTeamDao.createBatch(requirementDto.getTeamList());
        log.debug("create requirement teams success.");

        //创建详细需求的阶段时间信息
        for(RequirementTime time : requirementDto.getTimeList()){
            time.setRequirementId(requirementId);
        }
        requirementTimeDao.createBatch(requirementDto.getTimeList());
        log.debug("create requirement times success.");

        //创建记录初始的阶段时间信息（用于显示）
        reqPredictTimeDao.createBatch(Lists.transform(requirementDto.getTimeList() , new Function<RequirementTime, ReqPredictTime>() {
            @Nullable
            @Override
            public ReqPredictTime apply(RequirementTime time) {
                ReqPredictTime reqPredictTime = new ReqPredictTime();
                reqPredictTime.setRequirementId(time.getRequirementId());
                reqPredictTime.setType(time.getType());
                reqPredictTime.setPredictStart(time.getPredictStart());
                reqPredictTime.setPredictEnd(time.getPredictEnd());

                return reqPredictTime;
            }
        }));
        log.debug("create requirement predict times success.");

        //当需求创建成功后向eventBus中post一个消息
        if(!Strings.isNullOrEmpty(requirementDto.getModuleFileURL())){
            //当使用excel导入操作时（通知解析excel）
            log.debug("analyze excel to create modules.");
            reqEventBus.post(requirementDto);
        }

        //写入供应商的需求统计数据
        PurchaserRequirementCount purchaserRequirementCount = new PurchaserRequirementCount();
        purchaserRequirementCount.setUserId(requirement.getCreatorId());
        purchaserRequirementCount.setUserName(requirement.getCreatorName());
        purchaserRequirementCount.setStatusCounts(ImmutableMap.of(RequirementStatus.WAIT_SEND.value(), 1));
        requirementCountService.setPurchaserReqCount(purchaserRequirementCount);

        //创建需求数据变迁的状态
        requirementSendDao.create(defaultSend(requirementId));

        return requirementId;
    }

    /**
     * 更新需求信息内容（使用事务处理）
     * @param requirementDto  需求对象
     * 在效率方面的考虑（模块的数据更新就直接都设置位controller异步处理了－》全部放在一起处理太变态了）
     */
    @Transactional
    public void updateRequirement(RequirementDto requirementDto){
        log.debug("update requirement start");

        Requirement requirement = requirementDto.getRequirement();

        //旧的的需求数据信息
        Requirement oldRequirement = requirementDao.findById(requirement.getId());

        if(!Objects.equal(oldRequirement.getDeliveryAddress() , requirement.getDeliveryAddress())
           || !Objects.equal(oldRequirement.getSeriesIds(), requirement.getSeriesIds())){
            List<Module> moduleList = moduleDao.findModules(requirement.getId());

            //判断原始的数据和新的数据对于已创建的模块是否存在影响
            moduleDao.deleteByReqId(requirement.getId());

            //删除模块的工厂数据信息
            for(Module module : moduleList) {
                moduleFactoryDao.deleteByModuleId(module.getId());
            }

            //除去redis中的数据
            countManager.deleteKey(REKeyUtil.moduleNumKey(requirement.getId()));
            countManager.deleteKey(REKeyUtil.moduleTotalKey(requirement.getId()));
        }

        //需求信息更新
        requirementDao.update(requirement);

        //更新团队设定
        if(requirementDto.getTeamList() != null){
            List<RequirementTeam> newTeams = Lists.newArrayList();
            for(RequirementTeam team : requirementDto.getTeamList()){
                //新添加的数据
                if(team.getRequirementId() == null){
                    team.setRequirementId(requirement.getId());
                    newTeams.add(team);
                }else{
                    //直接更新数据
                    team.setRequirementId(requirement.getId());
                    team.setRequirementName(requirement.getName());
                    requirementTeamDao.update(team);
                }
            }
            //添加新数据
            if(!newTeams.isEmpty()){
                requirementTeamDao.createBatch(newTeams);
            }
        }

        //更新时间数据信息
        if(requirementDto.getTimeList() != null){
            List<RequirementTime> newTimes = Lists.newArrayList();
            for(RequirementTime time : requirementDto.getTimeList()){
                if(time.getRequirementId() == null){
                    time.setRequirementId(requirement.getId());
                    newTimes.add(time);
                }else{
                    time.setRequirementId(requirement.getId());
                    requirementTimeDao.update(time);
                }
            }
            //添加新数据
            if(!newTimes.isEmpty()){
                requirementTimeDao.createBatch(newTimes);
            }
        }

        log.debug("update requirement end");
    }

    /**
     *  通过需求编号查询详细的信息
     * @param requirementId 需求编号
     * @return  RequirementDto
     * 返回需求详细信息
     */
    public RequirementDto findByRequirementId(Long requirementId){
        RequirementDto requirementDto = new RequirementDto();

        //查询需求信息
        Requirement requirement = requirementDao.findById(requirementId);
        requirementDto.setRequirement(requirement);

        //查询团队信息
        requirementDto.setTeamList(requirementTeamDao.findByRequirementId(requirementId));

        //查询需求的时间段信息
        requirementDto.setTimeList(requirementTimeDao.findByRequirementId(requirementId));

        //获取配送园区信息
        Response<List<ParkFactoryDto>> result = deliveryService.findDetailFactories(requirement.getProductId() ,
        Lists.transform(Splitters.COMMA.splitToList(requirement.getDeliveryAddress()) , new Function<String, Long>() {
            @Nullable
            @Override
            public Long apply(String input) {
                return Long.parseLong(input);
            }
        }));

        if(!result.isSuccess()){
            log.error("find park factory info failed, error code={}", result.getError());
        }
        requirementDto.setParkFactoryDtoList(result.getResult());

        return requirementDto;
    }

    /**
     * 通过需求编号查询详细的需求信息
     * @param requirementId 需求编号
     * @param supplierId    供应商编号（null：表示采购商）
     * @return RequirementDetailDto
     * 返回需求详细信息内容
     */
    public RequirementDetailDto findDetailRequirement(Long requirementId , Long supplierId){
        RequirementDetailDto detailDto = new RequirementDetailDto();

        //查询需求信息
        Requirement requirement = requirementDao.findById(requirementId);
        detailDto.setRequirement(requirement);

        //查询需求的时间段信息
        for(RequirementTime requirementTime : requirementTimeDao.findByRequirementId(requirementId)){
            if(Objects.equal(RequirementStatus.SOL_END , RequirementStatus.from(requirementTime.getType()))){
                //写入方案终投的实际开始时间
                detailDto.setSolEndTime(requirementTime.getActualStart());
            }

            if(Objects.equal(requirementTime.getType() , detailDto.getRequirement().getStatus())){
                detailDto.setFinishDay(requirementTime.getPredictEnd());
                //是否可以进行事件状态切换
                detailDto.setStatusType(Days.daysBetween(new DateTime(requirementTime.getPredictEnd()) , DateTime.now()).getDays() > 0);
            }
        }

        //获取配送园区信息
        Response<List<ParkFactoryDto>> result = deliveryService.findDetailFactories(requirement.getProductId() ,
        Lists.transform(Splitters.COMMA.splitToList(requirement.getDeliveryAddress()) , new Function<String, Long>() {
            @Nullable
            @Override
            public Long apply(String input) {
                return Long.parseLong(input);
            }
        }));

        if(!result.isSuccess()){
            log.error("find park factory info failed, error code={}", result.getError());
        }
        detailDto.setParkFactoryDtoList(result.getResult());

        //查询需求的时间段信息
        List<RequirementTime> requirementTimes = requirementTimeDao.findByRequirementId(requirementId);

        //计算延期天数
        List<ReqPredictTime> reqPredictTimes = reqPredictTimeDao.findByRequirementId(requirementId);
        Map<Integer , Integer> deferDays = Maps.newHashMap();
        Integer deferDay;
        for(RequirementTime reqTime : requirementTimes){
            for(ReqPredictTime predictTime : reqPredictTimes){
                if(Objects.equal(reqTime.getType() , predictTime.getType())){

                    if(reqTime.getActualEnd() == null){
                        break;
                    }else if(reqTime.getActualEnd().after(predictTime.getPredictEnd())){
                        deferDay = Days.daysBetween(new DateTime(predictTime.getPredictEnd()) , new DateTime(reqTime.getActualEnd())).getDays();
                        deferDays.put(reqTime.getType() , deferDay);
                        break;
                    }
                }
            }
        }
        detailDto.setDeferDays(deferDays);

        //获取对应的供应商的各种状态
        if(supplierId != null){
            RequirementSolution solution = requirementSolutionDao.findByRequirementId(requirementId , supplierId);
            detailDto.setSolutionStatus(solution == null ? null : solution.getStatus());

            Response<Integer> paidRes = depositService.checkPaid(requirementId , supplierId);
            //支付信息
            if(!paidRes.isSuccess()){
                log.error("find deposit failed, requirementId={}, supplierId={}", requirementId, supplierId);
                detailDto.setDepositStatus(Deposit.Status.INIT.value());
            }else{
                detailDto.setDepositStatus(paidRes.getResult());
            }
        }else{
            //采购商则获取团队成员数据信息（用于团队成员的数据校验）
            detailDto.setTeamList(requirementTeamDao.findByRequirementId(requirementId));
        }

        //写入需求其余标志位
        detailDto.setRequirementSend(requirementSendDao.findByRequirementId(requirementId));

        //写入统计数据(当阶段还未进入选定供应商与方案，统计数据都是存放在redis中)
        if(requirement.getStatus() == null || requirement.getStatus() < RequirementStatus.SUP_SOL.value()){
            Map<RequirementCountType , Integer> reqMaps = requirementCountService.findReqCount(requirement.getId() , RequirementCountType.values());
            requirement.setSendSu(reqMaps.get(RequirementCountType.SEND_SU));
            requirement.setAnswerSu(reqMaps.get(RequirementCountType.ANSWER_SU));
            requirement.setSendSo(reqMaps.get(RequirementCountType.SEND_SO));
            requirement.setTopicNum(reqMaps.get(RequirementCountType.TOPIC_NUM));
        }

        return detailDto;
    }

    /**
     * 查询一个需求下的所有可参与讨论的用户(包括供应商&采购商)
     * @param requirementId 需求编号
     * @return  List
     * 返回参与讨论的人员信息
     */
    public List<TopicUser> findTopicUsers(Long requirementId){
        List<TopicUser> topicUsers = Lists.newArrayList();

        Requirement requirement = requirementDao.findById(requirementId);
        //封装需求创建者信息
        RequirementTeam creator = new RequirementTeam();
        creator.setUserId(requirement.getCreatorId());
        creator.setUserName(requirement.getCreatorName());
        creator.setUserPhone(requirement.getCreatorPhone());

        //添加所有可参与讨论的用户编号
        List<Long> userIds = Lists.newArrayList();
        userIds.add(requirement.getCreatorId());
        //获取团队成员信息
        userIds.addAll(Lists.transform(requirementTeamDao.findByRequirementId(requirementId), new Function<RequirementTeam, Long>() {
            @Nullable
            @Override
            public Long apply(RequirementTeam input) {
                return input.getUserId();
            }
        }));

        //获取提交过方案的人员
        userIds.addAll(Lists.transform(requirementSolutionDao.findAllSolution(requirementId) , new Function<RequirementSolution, Long>() {
            @Nullable
            @Override
            public Long apply(RequirementSolution input) {
                return input.getUserId();
            }
        }));

        //获取全部的参与的人员
        if(!userIds.isEmpty()){
            Response<List<User>> resp = accountService.findUserByIds(userIds);
            if(!resp.isSuccess()){
                throw new RuntimeException(resp.getError());
            }
            topicUsers = Lists.transform(resp.getResult(), new Function<User, TopicUser>() {
                @Nullable
                @Override
                public TopicUser apply(User user) {
                    TopicUser topicUser = new TopicUser();
                    topicUser.setUserId(user.getId());
                    topicUser.setUserName(user.getName());
                    topicUser.setUserType(user.getType());

                    return topicUser;
                }
            });
        }


        return topicUsers;
    }

    /**
     * 对需求进行批量更改(根据阶段时间进行跳转)
     */
    public void transitionJob(){
        //获取全部还未完成的需求列表进行批量状态切换
        List<Requirement> requirementList = requirementDao.findAllRequirements(RequirementStatus.SUP_SOL.value() , Requirement.CheckResult.SUCCESS.value());

        log.debug("update batch requirement time status start.");
        for(Requirement requirement : requirementList){
            //获取需求的当前时间阶段及状态
            RequirementTime oldTime = requirementTimeDao.findByStatus(requirement.getId() , requirement.getStatus());
            if(oldTime != null) {
                //获取预定的时间阶段长度
                int timeLength = Days.daysBetween(new DateTime(oldTime.getPredictStart()) , new DateTime(oldTime.getPredictEnd())).getDays();

                //获取当前实际的阶段长度
                int actualLength = Days.daysBetween(new DateTime(oldTime.getActualStart()) , new DateTime()).getDays();

                //当达到预定时间自动处理
                if(timeLength <= actualLength){
                    //时间阶段切换
                    transitionTime(requirement , 0l, "自动处理", true);
                }
            }else{
                log.error("requirement status time is null, requirementId={}, statusId={}", requirement.getId(), requirement.getStatus());
            }
        }

        log.debug("update batch requirement time status end.");
    }

    /**
     * 定时的dump需求的所有模块的数量以及模块资源总量
     */
    public void dumpCountModule(){
        //获取全部还未进入需求锁定的需求数据
        List<Requirement> requirementList = requirementDao.findAllRequirements(null , null);

        Integer moduleNum;
        Integer moduleCount;
        for(Requirement requirement : requirementList){
            moduleNum = 0;
            moduleCount = 0;
            //获取全部的模块数据
            List<Module> moduleList = moduleDao.findModules(requirement.getId());
            for(Module module : moduleList){
                moduleNum++;
                moduleCount += module.getTotal();
            }

            //向redis写入同步的需求模块统计数据
            countManager.setRCount(requirement.getId() , moduleNum, moduleCount);
        }
    }

    /**
     * 当阶段处于->承诺底线时，提前WARNING_DAY天提醒当提交方案的供应商数量少于3个的情况下会发送消息给采购商
     * @param warningDay    提前多少天预警
     */
    public void warningJob(int warningDay){
        //获取全部承诺底线的阶段全部需求
        List<Requirement> requirementList = requirementDao.findAllExcellence();

        log.debug("query warning requirement status start.");
        for(Requirement requirement : requirementList){
            //获取需求的当前时间阶段及状态
            RequirementTime oldTime = requirementTimeDao.findByStatus(requirement.getId() , requirement.getStatus());

            //获取时间长度信息
            int timeLength = Days.daysBetween(DateTime.now() , new DateTime(oldTime.getPredictEnd())).getDays();

            //当到达预警时间时发送预警信息
            if(warningDay > timeLength){
                //获取需求的方案投递人数
                List<RequirementSolution> solutionList = requirementSolutionDao.findAllSolution(requirement.getId());

                //方案提交数量小于规定数值需要提醒供应商
                if(solutionList.size() < MIN_SOLUTION){
                    //获取后台三级类目信息
                    List<BackendJSON> categoryList = JSON_MAPPER.fromJson(requirement.getSeriesIds() , JSON_MAPPER.createCollectionType(List.class , BackendJSON.class));

                    List<Long> categoryIds = Lists.newArrayList();

                    for(BackendJSON category : categoryList){
                        categoryIds.add(category.getBcId());
                    }

                    //获取对应资资源小微
                    Response<List<Long>> userIdRes = purchaserAuthorityService.getUserIdsHavingAuthInBcIds(categoryIds , 2, User.JobRole.RESOURCE.role());

                    if(userIdRes.isSuccess()){
                        //获取全部要发送的用户信息
                        List<Long> userIds = Lists.newArrayList(requirement.getCreatorId());
                        userIds.addAll(userIdRes.getResult());
                        Response<List<User>> userRes = accountService.findUserByIds(userIds);

                        if (userRes.isSuccess()) {
                            List<Mail<?>> mails = Lists.newArrayList();
                            Mail<Map<String, Object>> mail;

                            for (User user : userRes.getResult()) {
                                //增加邮件发送
                                mail = new Mail<Map<String, Object>>();
                                mail.setType(Mail.Type.REQUIREMENT_INVITATION);
                                mail.setTo(user.getEmail());
                                mail.setData(ImmutableMap.<String, Object>of("userName", user.getName(), "requirementName", requirement.getName(), "pNum", solutionList.size()));
                                mails.add(mail);

                                //发送邮件
                                messageService.push(Message.Type.REQUIREMENT_WARNING, 0l, user.getId(),
                                        ImmutableMap.of("id", requirement.getId(), "name", requirement.getName(), "pNum", solutionList.size()));
                            }
                            mailService.batchSend(mails);
                        }
                    }else{
                        log.error("find resource user failed, categoryList={}" , categoryList);
                    }
                }
            }
        }

        log.debug("query warning requirement status end.");
    }

    /**
     * 记录用户提交时间状态切换的数据信息
     * @param requirement   需求对象
     * @param userId        操作者编号
     * @param userName      操作者名称
     * @param auto          用于区分状态切换是定时任务触发（or 人为的触发）
     * 返回操作结果
     */
    public void transitionTime(Requirement requirement, Long userId, String userName, Boolean auto){
        Integer status = requirement.getStatus();

        if(Objects.equal(RequirementStatus.from(status) , RequirementStatus.DELETE)){
            //当处于删除状态无法跳转
            return;
        }

        Long requirementId = requirement.getId();

        //具体的阶段执行具体的后台操作
        switch(RequirementStatus.from(status)){
            case WAIT_SEND:         //等待发布
                skipWaitSend(requirement , userId, status+1);
                break;

            case RES_INTERACTIVE:   //需求交互
                updateRequirementStatus(requirementId , status+1);
                break;

            case RES_LOCK:          //需求锁定
                skipResLock(requirement , userId, status+1, auto);
                break;

            case SOL_INTERACTIVE:   //方案交互|承诺底线
                skipSolInteractive(requirement, userId, status+1);
                break;

            case SOL_END:           //方案终投|谈判|竞标
                skipSolEnd(requirement, status+1);
                break;

            default:
                break;
        }

        //查询当前时间阶段&下一个时间阶段
        List<RequirementTime> timeList = requirementTimeDao.findByStatues(requirementId , status, status+1);
        for(RequirementTime time : timeList){
            if(Objects.equal(time.getType(), status)){
                //当前时间段的处理
                RequirementTime newR = new RequirementTime();
                newR.setId(time.getId());
                newR.setUserId(userId);
                newR.setUserName(userName);
                newR.setActualEnd(DateTime.now().toDate());

                requirementTimeDao.update(newR);
            }else if(Objects.equal(time.getType() , status+1)){
                //新的时间段的处理
                RequirementTime newR = new RequirementTime();
                newR.setId(time.getId());
                newR.setActualStart(DateTime.now().toDate());

                requirementTimeDao.update(newR);
            }
        }

        //统计数据的异步写入
        reqEventBus.post(new CountMessage(requirement , status, status+1));

        log.debug("update requirement time success.");
    }

    /**
     * 等待发布状态跳转时触发事件
     * @param requirement   需求信息
     * @param userId        状态跳转操作者
     * @param status        下一个需求状态
     */
    private void skipWaitSend(Requirement requirement, Long userId, Integer status){
        //切换到新的需求状态
        Requirement newR = new Requirement();

        newR.setSendTime(DateTime.now().toDate());
        newR.setId(requirement.getId());
        newR.setStatus(status);
        requirementDao.update(newR);

        //向供应商推送需求发布的消息
        reqEventBus.post(new RequirementMessage(requirement, userId, null));

        log.debug("skip WaitSend success.");
    }

    /**
     * 需求锁定状态跳转时触发事件
     * @param requirement   需求信息
     * @param userId        操作用户编号
     * @param status        下一个需求状态
     */
    private void skipResLock(Requirement requirement, Long userId, Integer status, Boolean auto){
        List<Module> moduleList = moduleDao.findModules(requirement.getId());
        //校验模块的数据是否填写完整
        if(auto) {
            for (Module module : moduleList) {
                if (!checkModule(module)) {
                    //通过消息中心向采购商推送需求消息(模块信息还未填写完整)
                    reqEventBus.post(new RequirementMessage(requirement , userId, Lists.newArrayList(requirement.getCreatorId())));
                    return;
                }
            }
        }

        //获取模块全部资源信息
        List<Module> modules = moduleDao.findModules(requirement.getId());
        Long moduleFee = 0l;
        for(Module module : modules){
            moduleFee += module.getTotal()*module.getCost();
        }

        Long requirementId = requirement.getId();
        //需求锁定后模块信息将无法在更改
        Requirement newR = new Requirement();
        newR.setId(requirementId);
        newR.setStatus(status);
        newR.setModuleNum(countManager.getModuleNum(requirementId));
        newR.setModuleTotal(countManager.getModuleTotal(requirementId));
        newR.setModuleAmount(moduleFee);
        requirementDao.update(newR);

        //除去redis中的数据
        countManager.deleteKey(REKeyUtil.moduleNumKey(requirementId));
        countManager.deleteKey(REKeyUtil.moduleTotalKey(requirementId));

        //计算需求的正选供应商数量&备选供应商数量（异步处理）
        reqEventBus.post(requirement);

        //当锁定的需求是新品时
        if(Objects.equal(Requirement.ModuleType.from(requirement.getModuleType()), Requirement.ModuleType.NEW_TYPE)){
            plmEventBus.post(requirement);
        }
        log.debug("skip ResLock success.");
    }

    /**
     * 方案交互状态跳转时触发事件
     * @param requirement   需求信息
     * @param userId        操作用户编号
     * @param status        下一个需求状态
     */
    private void skipSolInteractive(Requirement requirement, Long userId, Integer status){
        //在需求的方案交互和方案终投阶段的谈判文件的验证处理（单独查询供应商资质符合）
        checkTransact(requirement , status, RequirementQuotaService.QueryType.QUALIFY.value());

        //向供应商推送需求即将进入终投状态（需要那些用户去确认）
        reqEventBus.post(new RequirementMessage(requirement, userId, Lists.transform(requirementSolutionDao.findAllSolution(requirement.getId()) , new Function<RequirementSolution, Long>() {
            @Nullable
            @Override
            public Long apply(RequirementSolution solution) {
                return solution.getUserId();
            }
        })));

        log.debug("skip SolInteractive success.");
    }

    /**
     * 方案终投状态跳转时触发事件
     * @param requirement   需求信息
     * @param status        需求当前状态
     */
    private void skipSolEnd(Requirement requirement , Integer status){
        /*
         * 现在所有场景当不满足要求都会走谈判流程
         * 场景一：模块属性－》新品、老品，模块策略－》技术领先、差异化
         * 场景二：模块属性－》新品、老品、衍生号，模块策略－》卓越运营
         * 场景三：模块属性－》衍生号、甲指、专利等其他类型，模块策略－》卓越运营
         */
        if(Objects.equal(Requirement.TransactType.from(requirement.getTransactType()), Requirement.TransactType.TRANSACT)){
            //谈判阶段
            if(requirement.getTransactFile() != null){
                //入围供应商打标签
                setTagAndCount(requirement.getId());

                //已提交谈判文档(写入统计数据信息)
                updateRequirementCountAndStatus(requirement.getId() , status);

                log.debug("skip SolEnd success.");
            }else{
                //一直等待直到提交了谈判文档
                log.error("skip SolEnd filed, must send transact file");
            }
        }else if(Objects.equal(Requirement.TransactType.from(requirement.getTransactType()) , Requirement.TransactType.COMPETITIVE)){
            //校验一把是否符合谈判逻辑(查询全部数据是否符合)
            checkTransact(requirement , status, RequirementQuotaService.QueryType.ALL.value());

            //入围供应商打标签
            setTagAndCount(requirement.getId());

            //竞标状态直接跳转
            updateRequirementCountAndStatus(requirement.getId(), status);
        }
    }

    /**
     * 根据需求编号设置入围的供应商的标签以及供应商的方案统计数据信息
     * @param requirementId 需求编号
     */
    private void setTagAndCount(Long requirementId){
        Response<List<RequirementSolution>> solutionRes = requirementQuotaService.findTagSolutions(requirementId);
        if(solutionRes.isSuccess()){
            for(RequirementSolution solution : solutionRes.getResult()) {
                //打入围标签
                tagService.addSupplierStatusTag(solution.getUserId() , User.SupplierTag.STANDARD_SUPPLIER);

                //写入入围统计数据
                solutionCountService.setSolCountInfo(solution.getUserId() , SolutionCountType.ENTER_SOL, 1);
            }
        }
    }

    /**
     * 在需求的方案交互和方案终投阶段的谈判文件的验证处理
     * @param requirement   需求信息
     * @param status        需求的下一个跳转阶段
     * @param queryType     需求方案的查询范围处理
     */
    private void checkTransact(Requirement requirement, Integer status, Integer queryType){
        //切换到新的需求状态
        Requirement newR = new Requirement();

        newR.setId(requirement.getId());
        newR.setStatus(status);

        /*
         * 现在更改成新品和老品当小于三家供应商时会进入谈判阶段
         * 场景一：模块属性－》新品、老品，模块策略－》技术领先、差异化
         * 场景二：模块属性－》新品、老品、衍生号，模块策略－》卓越运营
         * 场景三：模块属性－》衍生号、甲指、专利等其他类型，模块策略－》卓越运营
         */
        if(Objects.equal(Tactics.from(requirement.getTacticsId()) , Tactics.EXCELLENCE) &&
                (Objects.equal(Requirement.ModuleType.from(requirement.getModuleType()) , Requirement.ModuleType.NEW_TYPE)
                        || Objects.equal(Requirement.ModuleType.from(requirement.getModuleType()) , Requirement.ModuleType.OLD_TYPE))){
            //场景二需要根据供应商投递的方案数量判断下一阶段的状态是（谈判｜竞标）
            //查询不同的需求方案范围
            Response<Integer> solutionNumRes = requirementQuotaService.findEndSolutionNum(requirement.getId() , queryType);
            if(!solutionNumRes.isSuccess() || solutionNumRes.getResult() < MIN_SOLUTION){
                //谈判阶段
                newR.setTransactType(Requirement.TransactType.TRANSACT.value());
            }else{
                //竞标阶段
                newR.setTransactType(Requirement.TransactType.COMPETITIVE.value());
            }
        }else{
            //竞标阶段(衍生号、甲指、专利不需要进行谈判)
            newR.setTransactType(Requirement.TransactType.COMPETITIVE.value());
        }
        requirementDao.update(newR);
    }

    /**
     * 更新需求当前的状态
     * @param requirementId 需求编号
     * @param status        需要更改的状态
     */
    private void updateRequirementStatus(Long requirementId, Integer status){
        Requirement newR = new Requirement();
        newR.setId(requirementId);
        newR.setStatus(status);
        requirementDao.update(newR);
    }

    /**
     * 实现更新需求信息&更新需求的统计数据写入数据库
     * @param requirementId 需求编号
     * @param status        状态
     */
    private void updateRequirementCountAndStatus(Long requirementId, Integer status){
        //已提交谈判文档
        Requirement newR = new Requirement();
        newR.setId(requirementId);
        newR.setStatus(status);

        //写入需求的统计数据到数据库&删除redis中的需求统计数据
        Map<RequirementCountType , Integer> reqMaps = requirementCountService.findReqCount(requirementId , RequirementCountType.values());
        newR.setSendSu(reqMaps.get(RequirementCountType.SEND_SU));
        newR.setAnswerSu(reqMaps.get(RequirementCountType.ANSWER_SU));
        newR.setSendSo(reqMaps.get(RequirementCountType.SEND_SO));
        newR.setTopicNum(reqMaps.get(RequirementCountType.TOPIC_NUM));
        requirementDao.update(newR);

        //删除redis数据
        requirementCountService.deleteReqCount(requirementId);
    }

    /**
     * 校验模块的信息是否完整
     * @param module 模块信息
     * @return Boolean
     * 返回模块校验结果
     */
    private Boolean checkModule(Module module){
        //质量目标
        if(module.getQuality() == null){
            log.error("create module need quality.");
            return false;
        }

        //成本目标
        if(module.getCost() == null){
            log.error("create module need cost.");
            return false;
        }

        //产能要求
        if(module.getDelivery() == null){
            log.error("create module need delivery.");
            return false;
        }

        //认证要求
        if(Strings.isNullOrEmpty(module.getAttestations())){
            log.error("create module need attestation.");
            return false;
        }

        //批量供货时间
        if(module.getSupplyAt() == null){
            log.error("create module need supply time.");
            return false;
        }

        return true;
    }

    /**
     * 构建一个需求的初始状态的数据位
     * @param requirementId 需求编号
     * @return  RequirementSend
     * 返回需求的状态位
     */
    private RequirementSend defaultSend(Long requirementId){
        RequirementSend requirementSend = new RequirementSend();
        requirementSend.setRequirementId(requirementId);
        requirementSend.setSendPLM(0);
        requirementSend.setReplyModuleNum(0);
        requirementSend.setBusinessNegotiate(0);
        requirementSend.setSupplierSign(0);
        requirementSend.setResultPublicity(0);
        requirementSend.setConfirmQuota(0);
        requirementSend.setSendVCode(0);
        requirementSend.setWriteDetailQuota(0);
        requirementSend.setSendSAP(0);

        return requirementSend;
    }
}
