package io.terminus.snz.requirement.event;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import io.terminus.common.utils.JsonMapper;
import io.terminus.pampas.common.Response;
import io.terminus.snz.category.model.FrontendCategory;
import io.terminus.snz.message.models.Mail;
import io.terminus.snz.message.models.Message;
import io.terminus.snz.message.services.MailService;
import io.terminus.snz.message.services.MessageService;
import io.terminus.snz.requirement.dao.ModuleDao;
import io.terminus.snz.requirement.dao.RequirementSolutionDao;
import io.terminus.snz.requirement.dto.RequirementDto;
import io.terminus.snz.requirement.manager.ModuleManager;
import io.terminus.snz.requirement.model.*;
import io.terminus.snz.requirement.tool.AnalyzeExcel;
import io.terminus.snz.statistic.model.RequirementCountType;
import io.terminus.snz.statistic.service.RequirementCountService;
import io.terminus.snz.statistic.service.SolutionCountService;
import io.terminus.snz.user.model.Company;
import io.terminus.snz.user.model.User;
import io.terminus.snz.user.service.AccountService;
import io.terminus.snz.user.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * Desc:需求事件处理
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-09.
 */
@Slf4j
@Component
public class RequirementEventHandler implements RequirementEvents {
    private static final JsonMapper JSON_MAPPER = JsonMapper.JSON_NON_EMPTY_MAPPER;
    @Autowired
    private RequirementEventBus eventBus;
    @Autowired
    private AnalyzeExcel analyzeExcel;                        //excel解析
    @Autowired
    private ModuleDao moduleDao;                              //模块处理
    @Autowired
    private RequirementSolutionDao solutionDao;               //方案管理
    @Autowired
    private MessageService messageService;                    //消息处理机制
    @Autowired
    private MailService mailService;                          //邮件
    @Autowired
    private CompanyService companyService;                    //供应商公司
    @Autowired
    private ModuleManager moduleManager;                      //模块管理
    @Autowired
    private RequirementCountService requirementCountService;  //需求统计数据
    @Autowired
    private SolutionCountService solutionCountService;        //供应商需求的统计数据
    @Autowired
    private AccountService<User> accountService;

    @PostConstruct
    public void init() {
        eventBus.register(this);
    }

    @Subscribe
    @Override
    public void analyzeEvent(RequirementDto requirementDto){

        log.debug("analyze requirement module excel file.");
        //解析excel信息到对象中
        List<Module> moduleList = analyzeExcel.analyzeURL(requirementDto.getModuleFileURL() , 0, 1, new AnalyzeExcel.AnalyzeAction<Module>() {
            @Override
            public Module transform(String[] info) {
                //这个无法判断解析处理的数据是否是我们想要的数据只能让海尔的根据一个excel模版填写数据信息
                Module module = new Module();
                try{

                    for (int i = 0; i < info.length; i++) {
                        //信息不能为空有一个为空就返回一个null
                        if(Strings.isNullOrEmpty(info[i])){
                            return null;
                        }

                        switch (i){
                            case 0:
                                module.setRequirementId(new Float(info[i]).longValue()); break;
                            case 1:
                                module.setModuleName(info[i]); break;
                            case 2:
                                module.setQuality(new Float(info[i]).intValue()); break;
                            case 3:
                                module.setCost(new Float(info[i]).intValue()); break;
                            case 4:
                                module.setDelivery(new Float(info[i]).intValue()); break;
                            case 5:
                                module.setAttestations(info[i]); break;
                            case 6:
                                module.setSupplyAt(new DateTime(info[i]).toDate()); break;
                            default:
                                break;
                        }
                    }
                }catch(NumberFormatException e){
                    log.error("transform excel info to object failed, error code={}", Throwables.getStackTraceAsString(e));
                }catch(Exception e){
                    log.error("transform excel info to object failed, error code={}", Throwables.getStackTraceAsString(e));
                }

                return module;
            }
        });
        log.debug("transform requirement module excel success.");

        //将model对象保存到数据库中
        moduleDao.createBatch(moduleList);
        log.debug("create requirement modules success.");
    }

    @Subscribe
    @Override
    public void transitionEvent(RequirementMessage requirementMessage){
        //此处的状态是当前的需求状态，还未跳转到下一个阶段
        switch(RequirementStatus.from(requirementMessage.getRequirementStatus())){
            case WAIT_SEND:         //等待发布
                //推送的供应商
                List<Long> userIds = pushScope(requirementMessage.getCompanyScope());
                //向供应商推送需求发布的消息
                messageService.push(Message.Type.REQUIREMENT_PUBLISH, requirementMessage.getSenderId(),
                                    userIds, ImmutableMap.of("name", requirementMessage.getRequirementName(), "id", requirementMessage.getRequirementId()));
                //记录推送的供应商数量
                requirementCountService.setReqCountInfo(requirementMessage.getRequirementId() , RequirementCountType.SEND_SU, userIds.size());
                //发些邮件
                Response<List<User>> usersResp = accountService.findUserByIds(userIds);
                if (usersResp.isSuccess()) {
                    List<Mail<?>> mails = Lists.newArrayList();
                    for (User user : usersResp.getResult()) {
                        Response<Company> companyR = companyService.findCompanyByUserId(user.getId());
                        if (!companyR.isSuccess()) {
                            continue;
                        }
                        Mail<Map<String, Object>> mail = new Mail<Map<String, Object>>();
                        mail.setType(Mail.Type.REQUIREMENT_INVITATION);
                        mail.setTo(user.getEmail());
                        mail.setData(ImmutableMap.<String, Object>of(
                                "companyName", companyR.getResult().getCorporation(),
                                "requirementName", requirementMessage.getRequirementName())
                        );
                        mails.add(mail);
                    }
                    mailService.batchSend(mails);
                }
                break;

            case RES_INTERACTIVE:   //需求交互
                break;

            case RES_LOCK:          //需求锁定
                messageService.push(Message.Type.REQUIREMENT_INFO_EMPTY, requirementMessage.getSenderId(),
                                    pushScope(requirementMessage.getCompanyScope()), ImmutableMap.of("name", requirementMessage.getRequirementName(), "id", requirementMessage.getRequirementId()));
                break;

            case SOL_INTERACTIVE:   //方案交互
                //向供应商推送需求即将进入终投状态（需要那些用户去确认）
                messageService.push(Message.Type.REQUIREMENT_SOLUTION_END, requirementMessage.getSenderId(),
                                    requirementMessage.getReceiverIds(), ImmutableMap.of("name", requirementMessage.getRequirementName(), "id", requirementMessage.getRequirementId()));
                break;

            case SOL_END:           //方案终投
                messageService.push(Message.Type.REQUIREMENT_SOLUTION_SELECTED , requirementMessage.getSenderId(),
                                    requirementMessage.getReceiverIds(), ImmutableMap.of("name", requirementMessage.getRequirementName(), "id", requirementMessage.getRequirementId()));
                break;
            default:
                break;
        }
        log.debug("requirement transition event do success.");
    }

    @Subscribe
    @Override
    public void auditEvent(AuditMessage auditMessage) {
        //此处的状态是处理完后的审核状态
        switch (Requirement.CheckResult.from(auditMessage.getStatus())){
            case WAIT_SUBMIT:   //审核通过后向团队成员发送信息
                messageService.push(Message.Type.REQUIREMENT_CREATE , auditMessage.getSenderId(),
                                    auditMessage.getReceiverIds(), ImmutableMap.of("name" , auditMessage.getRequirementName(), "id", auditMessage.getRequirementId()));
                break;

            case WAIT:          //待审核（已提交给审核人员）
                //向上级领导发送消息
                messageService.push(Message.Type.REQUIREMENT_APPLY , auditMessage.getSenderId(),
                                    auditMessage.getReceiverIds(), ImmutableMap.of("name" , auditMessage.getRequirementName(), "id", auditMessage.getRequirementId()));
                break;

            case SUCCESS:       //审核通过
                messageService.push(Message.Type.REQUIREMENT_APPROVE_PASS , auditMessage.getSenderId(),
                                    auditMessage.getReceiverIds(), ImmutableMap.of("name" , auditMessage.getRequirementName(), "id", auditMessage.getRequirementId()));
                break;

            case FAILED:        //审核失败
                messageService.push(Message.Type.REQUIREMENT_APPROVE_UNPASS , auditMessage.getSenderId(),
                                    auditMessage.getReceiverIds(), ImmutableMap.of("name" , auditMessage.getRequirementName(), "id", auditMessage.getRequirementId()));
                break;
        }

        log.debug("audit requirement event do success.");
    }

    @Subscribe
    @Override
    public void countSelectNum(Requirement requirement) {
        //根据当前的需求的模块数量计算出最多可以几位供应商参与需求
        //(同时计算出每个模块工厂的供应商数量)
        moduleManager.countSelectNum(requirement.getId());
    }

    @Subscribe
    @Override
    public void countEvent(CountMessage countMessage) {
        log.debug("count requirement info start");
        Requirement requirement = countMessage.getRequirement();

        //需求状态跳转实现采购商统计数据更改
        requirementCountService.updatePurchaserReqCount(requirement.getCreatorId() , countMessage.getOldStatus(), countMessage.getNewStatus());

        List<Long> supplierIds = Lists.transform(solutionDao.findAllSolution(requirement.getId()) , new Function<RequirementSolution, Long>() {
            @Nullable
            @Override
            public Long apply(RequirementSolution solution) {
                return solution.getSupplierId();
            }
        });
        //供应商的需求统计数据
        solutionCountService.updateBatchCounts(supplierIds , countMessage.getOldStatus(), countMessage.getNewStatus());
        log.debug("count requirement info end");
    }


    /**
     * 根据需求信息获取推送消息的供应商范围(这部分的逻辑比较费时将它提到eventBus里处理)
     * @param companyScope   供应商范围
     * @return  List
     * 返回供应商的推送范围
     */
    private List<Long> pushScope(String companyScope){
        List<Long> scope = Lists.newArrayList();

        //获取前台类目编号列表（供应商范围，前台类目）
        List<FrontendCategory> propertyList = JSON_MAPPER.fromJson(companyScope , JSON_MAPPER.createCollectionType(List.class, FrontendCategory.class));

        List<Long> frontendCategoryIds = Lists.transform(propertyList , new Function<FrontendCategory, Long>() {
            @Nullable
            @Override
            public Long apply(FrontendCategory input) {
                return input.getId();
            }
        });

        //查询二级类目下绑定的企业主营业务关系
        Response<List<Long>> companyMainRes = companyService.findCompanyIdsByMainBusinessIds(frontendCategoryIds);
        if(!companyMainRes.isSuccess()){
            log.debug("find company Main business by frontend category failed, frontendCategoryIds=({}), error code={}", frontendCategoryIds, companyMainRes.getError());
            return scope;
        }

        return companyMainRes.getResult();
    }
}
