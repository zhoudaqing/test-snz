package io.terminus.snz.requirement.dao;

import com.google.common.collect.Lists;
import io.terminus.snz.requirement.model.RequirementTime;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Desc:需求时间创建测试处理
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-02.
 */
public class RequirementTimeDaoTest extends BasicTest {
    @Autowired
    private RequirementTimeDao requirementTimeDao;

    @Test
    public void createTest(){
        requirementTimeDao.create(mock());
    }

    @Test
    public void createBatchTest(){
        List<RequirementTime> timeList = Lists.newArrayList();
        timeList.add(mock());
        timeList.add(mock());

        requirementTimeDao.createBatch(timeList);
    }

    @Test
    public void updateTest(){
        //模拟审核操作
        RequirementTime requirementTime = mock();
        requirementTime.setId(1l);
        requirementTime.setActualEnd(DateTime.now().toDate());

        requirementTimeDao.update(requirementTime);
    }

    @Test
    public void findByIdTest(){
        assertThat(requirementTimeDao.findById(1l), notNullValue());
    }

    @Test
    public void testFindByStatus(){
        requirementTimeDao.findByStatus(1l , 1);
    }

    @Test
    public void testFindByStatues(){
        requirementTimeDao.findByStatues(1l , 1, 2);
    }

    @Test
    public void findByRequirementIdTest(){
        requirementTimeDao.findByRequirementId(1l);
    }

    @Test
    public void deleteTest(){
        requirementTimeDao.delete(1l);
    }

    private RequirementTime mock(){
        RequirementTime requirementTime = new RequirementTime();
        requirementTime.setRequirementId(1l);
        requirementTime.setType(1);
        requirementTime.setPredictStart(DateTime.now().toDate());
        requirementTime.setPredictEnd(DateTime.now().toDate());
        requirementTime.setUserId(1l);
        requirementTime.setUserName("Michael");

        return requirementTime;
    }
}
