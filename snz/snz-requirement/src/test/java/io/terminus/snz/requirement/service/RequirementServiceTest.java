package io.terminus.snz.requirement.service;

import com.google.common.collect.Lists;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.dto.RequirementDto;
import io.terminus.snz.requirement.model.Requirement;
import io.terminus.snz.requirement.model.RequirementTeam;
import io.terminus.snz.requirement.model.RequirementTime;
import io.terminus.snz.user.model.User;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertNotNull;

public class RequirementServiceTest extends BasicService {
    @Autowired
    private RequirementService requirementService;

    BaseUser user = null;

    @Before
    public void init(){
        user = new User();
        user.setId(1l);
        user.setName("Michale");
        user.setMobile("18657327206");
    }

    @Test
    public void testCreate() throws Exception {
        user.setId(1l);
        user.setName("Michael");
        Response<Long> response = requirementService.create(createMock() , user);
        assertNotNull(response);
    }

    @Test
    public void testExistName() throws Exception {
        assertNotNull(requirementService.existName(null, "Michael"));
        assertNotNull(requirementService.existName(1l, null));
        assertNotNull(requirementService.existName(1l, "Michael"));
    }

    @Test
    public void testUpdate() throws Exception {
        RequirementDto requirementDto = createMock();
        //测试更新倒入需求编号
        Requirement requirement = requirementDto.getRequirement();
        requirement.setId(1l);

        Long teamId = 1l;
        for(RequirementTeam team : requirementDto.getTeamList()){
            team.setRequirementId(1l);
            team.setId(teamId++ == 1l ? 1l : null);
        }

        Long timeId = 1l;
        for(RequirementTime time : requirementDto.getTimeList()){
            time.setRequirementId(1l);
            time.setId(timeId++ == 1l ? 1l : null);
        }

        Response<Boolean> response = requirementService.update(requirementDto);
        assertNotNull(response.getResult());
    }

    @Test
    public void testDeleteTeam() throws Exception {
        assertNotNull(requirementService.deleteTeam(null));
        assertNotNull(requirementService.deleteTeam(1l));
    }

    @Test
    public void testDelete() throws Exception {
        assertNotNull(requirementService.delete(null));
        assertNotNull(requirementService.delete(1l));
    }

    @Test
    public void testFindById() throws Exception {
        assertNotNull(requirementService.findById(null));
        assertNotNull(requirementService.findById(1l));
    }

    @Test
    public void testFindDetailById() throws Exception {
        assertNotNull(requirementService.findDetailById(null , user));
        assertNotNull(requirementService.findDetailById(1l , null));
        assertNotNull(requirementService.findDetailById(1l , user));
    }

    @Test
    public void testFindByPurchaser() throws Exception {
        assertNotNull(requirementService.findByPurchaser(null, 1, 1, null, null, null, null, null));
        assertNotNull(requirementService.findByPurchaser(user, null, 1, null, null, null, null, null));
        assertNotNull(requirementService.findByPurchaser(user, 1, null, null, null, null, null, null));
        assertNotNull(requirementService.findByPurchaser(user, 1, 1, null, null, null, null, null));
    }

    @Test
    public void testFindByParams() throws Exception {
        assertNotNull(requirementService.findByParams(null, 1, null, null));
        assertNotNull(requirementService.findByParams(1l, null, null, null));
        assertNotNull(requirementService.findByParams(1l, 1, null, null));
    }

    @Test
    public void testAskAudit() throws Exception {
        assertNotNull(requirementService.askAudit(null , user));
        assertNotNull(requirementService.askAudit(1l , null));

        assertNotNull(requirementService.askAudit(1l , user));
        //再次审核
        assertNotNull(requirementService.askAudit(2l , user));
    }

    @Test
    public void testAuditRequirement() throws Exception {
        assertNotNull(requirementService.auditRequirement(null , Requirement.CheckResult.FAILED.value(), user));

        //审核不通过
        assertNotNull(requirementService.auditRequirement(1l , Requirement.CheckResult.FAILED.value(), user));
        //审核通过
        assertNotNull(requirementService.auditRequirement(1l , Requirement.CheckResult.SUCCESS.value(), user));
    }

    @Test
    public void testTransitionStatus() throws Exception {
        assertNotNull(requirementService.transitionStatus(null , user));
        assertNotNull(requirementService.transitionStatus(1l , null));
        //测试所有阶段
        for(long i=1; i<=10; i++){
            assertNotNull(requirementService.transitionStatus(i , user));
        }
    }

    @Test
    public void testFindTopicPeople() throws Exception {
        assertNotNull(requirementService.findTopicPeople(null));
        assertNotNull(requirementService.findTopicPeople(1l));
    }

    @Test
    public void testTransitionExpire() throws Exception {
        requirementService.transitionExpire();
    }

    @Test
    public void testWarningExpire() throws Exception {
        requirementService.warningExpire(2);
    }

    private RequirementDto createMock(){
        RequirementDto requirementDto = new RequirementDto();

        //需求
        Requirement requirement = new Requirement();
        requirement.setName("test");
        requirement.setPurchaserId(1l);
        requirement.setPurchaserName("purchaserName");
        requirement.setSeriesIds("{sids:[{id:1,name:冰箱把手},{id:21,name:冰箱门}]}");
        requirement.setCoinType(1);
        requirement.setMaterielType(1l);
        requirement.setModuleType(1);
        requirement.setDeliveryAddress("{ad:[{pa:101,fa:10},{pa:101,fa:20}]}");
        requirement.setDescription("describe");
        requirement.setAccessories("{file:[url1,url2]}");
        requirement.setSelectNum(3);
        requirement.setReplaceNum(2);
        requirement.setCompanyScope("[{id:10,name:AGH}]");
        requirement.setTacticsId(1);
        requirement.setHeadDrop("引领点");
        requirement.setModuleNum(10);
        requirement.setModuleTotal(10000);
        requirement.setCheckResult(0);
        requirement.setCreatorId(1l);
        requirement.setCreatorName("Michael");
        requirement.setCreatorPhone("18657327206");
        requirement.setCreatorEmail("MichaelZhaoZero@gmail.com");
        requirementDto.setRequirement(requirement);

        //团队创建
        RequirementTeam requirementTeam = new RequirementTeam();
        requirementTeam.setRequirementId(1l);
        requirementTeam.setRequirementName("requirementName");
        requirementTeam.setType(1);
        requirementTeam.setUserId(1l);
        requirementTeam.setUserName("Michael");
        requirementTeam.setUserNumber("102310");
        requirementTeam.setUserPhone("18657327206");
        List<RequirementTeam> teamList = Lists.newArrayList();
        teamList.add(requirementTeam);
        requirementDto.setTeamList(teamList);

        //时间创建
        List<RequirementTime> timeList = Lists.newArrayList();
        for(int i=0; i<5; i++){
            RequirementTime requirementTime = mockTime();
            requirementTime.setType(i+1);
            timeList.add(requirementTime);
        }
        requirementDto.setTimeList(timeList);

        return requirementDto;
    }

    private RequirementTime mockTime(){
        RequirementTime requirementTime = new RequirementTime();
        requirementTime.setRequirementId(1l);
        requirementTime.setPredictStart(DateTime.now().toDate());
        requirementTime.setPredictEnd(DateTime.now().toDate());
        requirementTime.setUserId(1l);
        requirementTime.setUserName("Michael");

        return requirementTime;
    }
}