package io.terminus.snz.sns.services;

import com.google.common.collect.Maps;
import io.terminus.common.exception.ServiceException;
import io.terminus.common.model.Paging;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.message.services.MessageService;
import io.terminus.snz.requirement.dto.RequirementDto;
import io.terminus.snz.requirement.model.CoinType;
import io.terminus.snz.requirement.model.Requirement;
import io.terminus.snz.requirement.model.RequirementStatus;
import io.terminus.snz.requirement.service.RequirementService;
import io.terminus.snz.sns.BaseServiceTest;
import io.terminus.snz.sns.daos.mysql.ReplyDao;
import io.terminus.snz.sns.daos.mysql.TopicDao;
import io.terminus.snz.sns.daos.mysql.TopicUserDao;
import io.terminus.snz.sns.dtos.FatReply;
import io.terminus.snz.sns.dtos.TopicDetail;
import io.terminus.snz.sns.managers.ReplyManager;
import io.terminus.snz.sns.models.Reply;
import io.terminus.snz.sns.models.Topic;
import io.terminus.snz.statistic.service.RequirementCountService;
import io.terminus.snz.user.model.Company;
import io.terminus.snz.user.model.CompanyMainBusiness;
import io.terminus.snz.user.model.ContactInfo;
import io.terminus.snz.user.model.User;
import io.terminus.snz.user.service.AccountService;
import io.terminus.snz.user.service.CompanyService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * MADE IN IntelliJ IDEA
 * AUTHOR: haolin
 * AT 4/5/14.
 */
public class ReplyServiceTest extends BaseServiceTest {

    @Mock
    private TopicDao topicDao;

    @Mock
    private ReplyDao replyDao;

    @Mock
    private TopicService topicService;

    @Mock
    private TopicUserDao topicUserDao;

    @Mock
    private ReplyManager replyManager;

    @Mock
    private CompanyService companyService;

    @Mock
    private AccountService<User> accountService;

    @Mock
    private RequirementService requirementService;

    @Mock
    private MessageService messageService;

    @Mock
    private RequirementCountService requirementCountService;

    @InjectMocks
    private ReplyServiceImpl replyService = new ReplyServiceImpl();

    @Test
    public void testCreate(){
        when(companyService.findCompanyByUserId(anyLong())).thenReturn(mockCompanyResp(1L));
        when(topicService.findById(any(BaseUser.class), anyLong(), anyBoolean())).thenReturn(mockTopicDetailResp(1L));
        when(requirementService.findById(anyLong())).thenReturn(mockRequiremntResp(1L));
        when(replyManager.create(any(Reply.class))).thenReturn(1);
        when(topicService.replied(anyLong())).thenReturn(null);
        assertNotNull(replyService.create(purchaser, mockReply(1L)));

        // user not login
        assertFalse(replyService.create(null, mockReply(1L)).isSuccess());

        // find topic failed
        Response<TopicDetail> failedFindTopicResp = new Response<TopicDetail>();
        when(topicService.findById(any(BaseUser.class), anyLong(), anyBoolean())).thenReturn(failedFindTopicResp);
        assertFalse(replyService.create(purchaser, mockReply(1L)).isSuccess());

        // topic not exist
        failedFindTopicResp.setResult(null);
        assertFalse(replyService.create(purchaser, mockReply(1L)).isSuccess());

        // user not in circle
        Topic privateTopic = mockTopic(2L, Topic.Scope.CIRCLE_PRIVATE, mockRequirement(2L));
        privateTopic.setUserId(100L);
        TopicDetail privateTopicDetail = new TopicDetail(privateTopic);
        Response<TopicDetail> privateTopicDetailResp = new Response<TopicDetail>();
        privateTopicDetailResp.setResult(privateTopicDetail);
        when(topicService.findById(any(BaseUser.class), anyLong(), anyBoolean())).thenReturn(privateTopicDetailResp);
        when(topicUserDao.exist(anyLong(), anyLong())).thenReturn(Boolean.FALSE);
        assertFalse(replyService.create(purchaser, mockReply(1L)).isSuccess());

    }

    @Test(expected = ServiceException.class)
    public void testValidReqTopicStatus(){
        when(requirementService.findById(anyLong())).thenReturn(mockRequiremntResp(1L));
        assertTrue(replyService.validReqTopicStatus(mockTopic(1L)));

        // find requiremnt failed
        Response<RequirementDto> failedResp = mockRequiremntResp(1L);
        failedResp.setSuccess(Boolean.FALSE);
        when(requirementService.findById(anyLong())).thenReturn(failedResp);
        assert(replyService.validReqTopicStatus(mockTopic(1L)));

        // requirement not exist
        failedResp.setResult(null);
        when(requirementService.findById(anyLong())).thenReturn(failedResp);
        assertFalse(replyService.validReqTopicStatus(mockTopic(1L)));

        // topic and requiremnt isn't same status
        failedResp = mockRequiremntResp(1L);
        Topic invalidStatusTopic = mockTopic(2L);
        invalidStatusTopic.setReqStatus(failedResp.getResult().getRequirement().getStatus() - 1);
        when(requirementService.findById(anyLong())).thenReturn(failedResp);
        assertFalse(replyService.validReqTopicStatus(invalidStatusTopic));
    }

    @Test
    public void testPagingSimple(){
        Topic privateTopic = mockTopic(1L, Topic.Scope.CIRCLE_PRIVATE, mockRequirement(1L));
        when(topicDao.findById(anyLong())).thenReturn(privateTopic);
        Paging<Reply> mockReplyPage = new Paging<Reply>(10L, Arrays.asList(mockReply(1L)));
        when(replyDao.pagingForPrivate(anyLong(), anyLong(), anyLong(), anyInt(), anyInt())).thenReturn(mockReplyPage);
        when(topicUserDao.exist(anyLong(), anyLong())).thenReturn(Boolean.TRUE);

        // mock main bussiness
        Map<Long, List<CompanyMainBusiness>> mockMainBussiess = Maps.newHashMap();
        mockMainBussiess.put(1L, Arrays.asList(mockMainBussiess(1L)));
        Response<Map<Long, List<CompanyMainBusiness>>> mockMainBussiessResp = new Response<Map<Long, List<CompanyMainBusiness>>>();
        mockMainBussiessResp.setResult(mockMainBussiess);
        when(companyService.findMainBussinessByUserIds(anyList())).thenReturn(mockMainBussiessResp);

        // mock tags
        Response<List<User>> mockUsersResp = new Response<List<User>>();
        mockUsersResp.setResult(Arrays.asList(mockSupplier(1L)));
        when(accountService.findUserByIds(anyList())).thenReturn(mockUsersResp);

        // mock contact info
        Response<Map<Long, ContactInfo>> mockContactInfoResp = new Response<Map<Long, ContactInfo>>();
        Map<Long, ContactInfo> mockContackInfo = Maps.newHashMap();
        mockContackInfo.put(1L, mockContackInfo(1L));
        mockContactInfoResp.setResult(mockContackInfo);
        when(companyService.findContactInfoByUserIds(anyList())).thenReturn(mockContactInfoResp);

        Response<Paging<FatReply>> resp = replyService.pagingSimple(purchaser, 1L, 0, 10);
        Paging<FatReply> mockResult = resp.getResult();

        assertEquals(10, mockResult.getTotal().intValue());
        assertEquals(1, mockResult.getData().size());

        // user not login
        assertFalse(replyService.pagingSimple(null,  1L, 0, 10).isSuccess());

        // topic not exist
        when(topicDao.findById(anyLong())).thenReturn(null);
        assertFalse(replyService.pagingSimple(purchaser,  1L, 0, 10).isSuccess());
    }

    private ContactInfo mockContackInfo(Long id) {
        ContactInfo info = new ContactInfo();
        info.setId(id);
        info.setCompanyId(id);
        info.setUserId(id);
        info.setMobile("1231123123");
        info.setEmail("11111@xx.com");
        return info;
    }

    private CompanyMainBusiness mockMainBussiess(Long id) {
        CompanyMainBusiness cmb = new CompanyMainBusiness();
        cmb.setId(id);
        cmb.setUserId(id);
        cmb.setCompanyId(id);
        cmb.setMainBusinessId(id);
        cmb.setName("xx主营业务");
        return cmb;
    }

    private Reply mockReply(Long id){
        Reply r = new Reply();
        r.setId(id);
        r.setTid(0L);
        r.setTopicId(1L);
        r.setPid(0L);
        r.setUserId(1L);
        return r;
    }
    
    private Response<Company> mockCompanyResp(Long id) {
        Response<Company> resp = new Response<Company>();
        Company company = new Company();
        company.setId(id);
        company.setUserId(loginer.getId());
        company.setActingBrand("");
        company.setCorporation("xxx公司");
        resp.setResult(company);
        return resp;
    }

    private Response<TopicDetail> mockTopicDetailResp(Long id){
        Topic mockTopic = mockTopic(id);
        TopicDetail topicDetail = new TopicDetail(mockTopic);
        Response<TopicDetail> resp = new Response<TopicDetail>();
        resp.setResult(topicDetail);
        return resp;
    }

    private Topic mockTopic(Long id){
        return mockTopic(id, Topic.Scope.PUBLIC, mockRequirement(1L));
    }

    private Topic mockTopic(Long id, Topic.Scope scope, Requirement req){
        Topic t = new Topic();
        t.setId(id);
        t.setReqId(req.getId());
        t.setReqName(req.getName());
        t.setReqStatus(req.getStatus());
        t.setScope(scope.value());
        t.setCompanyName("xxx公司");
        t.setTitle("ooxx");
        t.setContent("xxoo");
        t.setUserId(req.getCreatorId());
        t.setUserName("xxxooo");
        return t;
    }

    private Requirement mockRequirement(Long id){
        Requirement r = new Requirement();
        r.setId(id);
        r.setCreatorId(id);
        r.setCheckId(id);
        r.setCheckName("me");
        r.setAccessories("");
        r.setAnswerSu(10);
        r.setCheckResult(Requirement.CheckResult.SUCCESS.value());
        r.setCheckTime(new Date());
        r.setCoinType(CoinType.CNY.value());
        r.setCompanyScope("");
        r.setDescription("");
        r.setStatus(RequirementStatus.SOL_END.value());
        return r;
    }

    private RequirementDto mockRequirementDto(Requirement r){
        RequirementDto rd = new RequirementDto();
        rd.setRequirement(r);
        return rd;
    }

    private Response<RequirementDto> mockRequiremntResp(Long id){
        Response<RequirementDto> resp = new Response<RequirementDto>();
        resp.setResult(mockRequirementDto(mockRequirement(id)));
        return resp;
    }
}
