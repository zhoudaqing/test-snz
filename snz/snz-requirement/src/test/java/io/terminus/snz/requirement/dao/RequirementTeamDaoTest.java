package io.terminus.snz.requirement.dao;

import com.google.common.collect.Lists;
import io.terminus.snz.requirement.model.RequirementTeam;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Desc:需求团队信息测试类
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-02.
 */
public class RequirementTeamDaoTest extends BasicTest {
    @Autowired
    private RequirementTeamDao requirementTeamDao;

    @Test
    public void createTest(){
        requirementTeamDao.create(mock());
    }

    @Test
    public void createBatchTest(){
        List<RequirementTeam> teamList = Lists.newArrayList();
        teamList.add(mock());
        teamList.add(mock());
        teamList.add(mock());
        requirementTeamDao.createBatch(teamList);
    }

    @Test
    public void updateTest(){
        //模拟审核操作
        RequirementTeam requirementTeam = mock();
        requirementTeam.setId(1l);
        requirementTeam.setUserNumber("2012");

        requirementTeamDao.update(requirementTeam);
    }

    @Test
    public void findByIdTest(){
        assertThat(requirementTeamDao.findById(1l), notNullValue());
    }

    @Test
    public void findByRequirementId(){
        System.out.println(requirementTeamDao.findByRequirementId(1l).size());
    }

    @Test
    public void testFindByIdType(){
        requirementTeamDao.findByIdType(1l , RequirementTeam.Type.C.value());
    }

    @Test
    public void deleteTest(){
        requirementTeamDao.delete(1l);
    }

    private RequirementTeam mock(){
        RequirementTeam requirementTeam = new RequirementTeam();
        requirementTeam.setRequirementId(1l);
        requirementTeam.setRequirementName("requirementName");
        requirementTeam.setType(1);
        requirementTeam.setUserId(1l);
        requirementTeam.setUserName("Michael");
        requirementTeam.setUserNumber("102310");
        requirementTeam.setUserPhone("18657327206");

        return requirementTeam;
    }
}
