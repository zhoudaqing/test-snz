package io.terminus.snz.sns.services;

import com.google.common.collect.Lists;
import io.terminus.common.model.Paging;
import io.terminus.pampas.common.Response;
import io.terminus.snz.message.services.MessageService;
import io.terminus.snz.requirement.dto.RequirementDto;
import io.terminus.snz.requirement.dto.TopicUser;
import io.terminus.snz.requirement.model.CoinType;
import io.terminus.snz.requirement.model.Requirement;
import io.terminus.snz.requirement.model.RequirementStatus;
import io.terminus.snz.requirement.service.RequirementService;
import io.terminus.snz.sns.BaseServiceTest;
import io.terminus.snz.sns.daos.mysql.ReplyDao;
import io.terminus.snz.sns.daos.mysql.TopicDao;
import io.terminus.snz.sns.daos.mysql.TopicUserDao;
import io.terminus.snz.sns.daos.redis.RedisReplyDao;
import io.terminus.snz.sns.managers.TopicManager;
import io.terminus.snz.sns.models.Topic;
import io.terminus.snz.statistic.model.RequirementCountType;
import io.terminus.snz.statistic.service.RequirementCountService;
import io.terminus.snz.user.model.Company;
import io.terminus.snz.user.model.User;
import io.terminus.snz.user.service.CompanyService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-5-4
 */
public class TopicServiceTest extends BaseServiceTest {
    @Mock
    private TopicUserDao topicUserDao;

    @Mock
    private TopicDao topicDao;

    @Mock
    private TopicManager topicManager;

    @Mock
    private ReplyDao replyDao;

    @Mock
    private RedisReplyDao redisReplyDao;

    @Mock
    private ReplyService replyService;

    @Mock
    private RequirementService requirementService;

    @Mock
    private RequirementCountService requirementCountService;    //需求话题统计数据信息

    @Mock
    private CompanyService companyService;

    @Mock
    private MessageService messageService;

    @InjectMocks
    private TopicServiceImpl topicService = new TopicServiceImpl();

    @Test
    public void testCreate(){
        Response<RequirementDto> reqResp = mockRequiremntResp(1L);
        Response<Company> companyResponse = mockCompanyResp(1L);
        when(requirementService.findById(anyLong())).thenReturn(reqResp);
        when(requirementCountService.setReqCountInfo(anyLong(),
                any(RequirementCountType.class), anyInt())).thenReturn(Boolean.TRUE);
        when(topicDao.create(any(Topic.class))).thenReturn(1);
        when(companyService.findCompanyByUserId(anyLong())).thenReturn(companyResponse);

        Response<Integer> createResp;
        // create public
        Topic publicTopic = mockTopic(1L, Topic.Scope.PUBLIC, reqResp.getResult().getRequirement());
        createResp = topicService.create(supplier, publicTopic, null);  //供应商创建
        assertEquals(1, createResp.getResult().intValue());
        createResp = topicService.create(purchaser, publicTopic, null); //采购商创建
        assertEquals(1, createResp.getResult().intValue());

        // create non public
        Topic privateTopic = mockTopic(1L, Topic.Scope.CIRCLE_PRIVATE, reqResp.getResult().getRequirement());
        String joinerIds = "22,33,44";
        Response<List<TopicUser>> topicPeopleResp = mockTopicPeopleResp(Arrays.asList(22L, 33L, 44L, 55L));
        when(requirementService.findTopicPeople(anyLong())).thenReturn(topicPeopleResp);
        when(topicManager.createNonPublic(any(Topic.class), anyList())).thenReturn(1);
        createResp = topicService.create(supplier, privateTopic, joinerIds);  //供应商创建
        assertEquals(1, createResp.getResult().intValue());

        // user not login
        assertFalse(topicService.create(null, privateTopic, joinerIds).isSuccess());

        // find requirement failed:
        reqResp.setError("error");
        when(requirementService.findById(anyLong())).thenReturn(reqResp);
        assertFalse(topicService.create(loginer, privateTopic, joinerIds).isSuccess());

        // find requirement null
        reqResp.setResult(null);
        when(requirementService.findById(anyLong())).thenReturn(reqResp);
        assertFalse(topicService.create(loginer, privateTopic, joinerIds).isSuccess());
    }

    @Test
    public void testCreateDefault(){
        Response<RequirementDto> reqResp = mockRequiremntResp(1L);
        Response<Company> companyResponse = mockCompanyResp(1L);
        when(requirementService.findById(anyLong())).thenReturn(reqResp);
        when(requirementCountService.setReqCountInfo(anyLong(),
                any(RequirementCountType.class), anyInt())).thenReturn(Boolean.TRUE);
        when(topicDao.create(any(Topic.class))).thenReturn(1);
        when(companyService.findCompanyByUserId(anyLong())).thenReturn(companyResponse);

        Topic defaultTopic = new Topic();
        defaultTopic.setTitle(Topic.DEFAULT.getTitle());
        defaultTopic.setContent(Topic.DEFAULT.getContent());
        defaultTopic.setScope(Topic.DEFAULT.getScope());
        defaultTopic.setReqId(1L);
        Response<Integer> createResp = topicService.createDefault(supplier, defaultTopic);  //供应商创建
        assertEquals(1, createResp.getResult().intValue());
    }

    @Test
    public void testFindById(){
        Response<RequirementDto> reqResp = mockRequiremntResp(1L);
        Topic privateTopic = mockTopic(1L, Topic.Scope.CIRCLE_PRIVATE, reqResp.getResult().getRequirement());
        when(topicDao.findById(anyLong())).thenReturn(privateTopic);
        when(replyService.validReqTopicStatus(any(Topic.class))).thenReturn(Boolean.TRUE);
        User supplier1 = mockSupplier(1L);
        when(replyDao.listTopicSuppliers(anyLong())).thenReturn(Arrays.asList(supplier1));
        when(replyDao.usersReplyCounts(anyLong(), anyList(), anyString())).thenReturn(Arrays.asList(1L));
        assertNotNull(topicService.findById(loginer, 1L, false).getResult());

        // not valid topic id
        assertFalse(topicService.findById(loginer, -1L, false).isSuccess());
        // topic not exist
        when(topicDao.findById(anyLong())).thenReturn(null);
        assertFalse(topicService.findById(loginer, 1L, false).isSuccess());
    }

    @Test
    public void testPaging(){
        Topic mockTopic = mockTopic(1L);
        Paging<Topic> mockPaging = new Paging<Topic>(10L, Arrays.asList(mockTopic));
        when(topicDao.paging(anyMap(), anyInt(), anyInt())).thenReturn(mockPaging);
        Paging<Topic> mockResult = topicService.pagingPublics(loginer, 1L, "1,2,3,4,5,6", 0, 10).getResult();
        assertEquals(10, mockResult.getTotal().intValue());
        assertEquals(1, mockResult.getData().size());

        //user not login
        Response<Paging<Topic>> resp = topicService.pagingPublics(null, 1L, "1,2,3,4,5,6", 0, 10);
        assertFalse(resp.isSuccess());
    }

    @Test
    public void testPagingByJoinerId(){
        Topic mockTopic = mockTopic(1L);
        Paging<Topic> mockPaging = new Paging<Topic>(10L, Arrays.asList(mockTopic));
        when(topicDao.pagingByJoinerId(anyLong(), anyInt(), anyInt())).thenReturn(mockPaging);
        Paging<Topic> mockResult = topicService.pagingByJoinerId(loginer, 1L,  0, 10).getResult();
        assertEquals(10, mockResult.getTotal().intValue());
        assertEquals(1, mockResult.getData().size());

        // user not login
        Response<Paging<Topic>> resp = topicService.pagingByJoinerId(null, 1L,  0, 10);
        assertFalse(resp.isSuccess());
    }

    @Test
    public void testViewed(){
        assertTrue(topicService.viewed(1L).getResult());
    }

    @Test
    public void testReplied(){
        assertTrue(topicService.replied(1L).getResult());
    }

    @Test
    public void testGetRequirementsCountInfo(){
        when(topicDao.countRequirementTopics(anyList())).thenReturn(Arrays.asList(1L));
        when(redisReplyDao.countRequirementSuppliers(anyList())).thenReturn(Arrays.asList(1L));
        assertEquals(1, topicService.getRequirementsCountInfo("1").getResult().size());
        // ids is null
        assertFalse(topicService.getRequirementsCountInfo(null).isSuccess());
    }

    private Response<List<TopicUser>> mockTopicPeopleResp(List<Long> userIds) {
        Response<List<TopicUser>> resp = new Response<List<TopicUser>>();
        List<TopicUser> tus = Lists.newArrayList();
        for (Long userId : userIds){
            TopicUser tu = new TopicUser();
            tu.setUserId(userId);
            tus.add(tu);
        }
        resp.setResult(tus);
        return resp;
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
