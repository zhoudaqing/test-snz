package io.terminus.snz.requirement.dao;

import com.google.common.collect.Maps;
import io.terminus.snz.requirement.model.RequirementSolution;
import org.joda.time.DateTime;
import org.junit.Test;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Desc:需求方案Dao测试类
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-03.
 */
public class RequirementSolutionDaoTest extends BasicTest {
    @Autowired
    private RequirementSolutionDao requirementSolutionDao;

    @Test
    public void createTest(){
        requirementSolutionDao.create(mock());
    }

    @Test
    public void updateTest(){
        RequirementSolution requirementSolution = mock();
        requirementSolution.setId(1l);

        requirementSolutionDao.update(requirementSolution);
    }

    @Test
    public void testDelete(){
        requirementSolutionDao.delete(1l);
    }

    @Test
    public void findByIdTest(){
        assertThat(requirementSolutionDao.findById(1l), notNullValue());
    }

    @Test
    public void testFindByRequirementId(){
        assertThat(requirementSolutionDao.findByRequirementId(1l , 1l), notNullValue());
    }

    @Test
    public void testFindByUserId(){
        requirementSolutionDao.findByUserId(1l , 1l);
    }

    @Test
    public void testFindAllSolution(){
        requirementSolutionDao.findAllSolution(1l);
    }

    @Test
    public void testFindSolutionEnds(){
        requirementSolutionDao.findSolutionEnds(1l , null, 5, null);
    }

    @Test
    public void testFindByParams(){
        Map<String , Object> params = Maps.newHashMap();
        params.put("offset" , 0);
        params.put("size" , 20);
        System.out.println(requirementSolutionDao.findByParams(1l , params).getData());
    }

    @Test
    public void testFindSolutionsByQualifyAndSupplier() {
        RequirementSolution exist = mock();
        exist.setStatus(3); // 无法修改
        requirementSolutionDao.create(exist);
        List<RequirementSolution> solutions = requirementSolutionDao.findSolutionsByQualifyAndSupplier(1L, -1);
        RequirementSolution solution = solutions.get(0);
    }

    @Test
    public void testUpdateSolutionFile() throws Exception{
        final RequirementSolution mock = mock();
        requirementSolutionDao.create(mock);

        requirementSolutionDao.updateSolutionFile(mock.getId(), "{\"name\":\"EBS二期微调项目.zip\",\"url\":\"http://l.ihaier.com/assets/2014/07/03/19997_76fd5fcd9b2acd4c45777b58c2f7b472.zip\"}");

        System.out.println("woohoo"+requirementSolutionDao.findById(mock.getId()));


    }

    @Test(expected = MyBatisSystemException.class)
    public void testFindSolutionBySupplier() {
        RequirementSolution anotherSolution = new RequirementSolution();
        anotherSolution.setRequirementId(1L);
        anotherSolution.setRequirementName("req");
        anotherSolution.setUserId(1L);
        anotherSolution.setSupplierId(2L);
        anotherSolution.setSupplierName("sup");
        requirementSolutionDao.create(anotherSolution);
        RequirementSolution solution = requirementSolutionDao.findByUserId(1L, 1L);
    }

    private RequirementSolution mock(){
        RequirementSolution requirementSolution = new RequirementSolution();
        requirementSolution.setUserId(1l);
        requirementSolution.setRequirementId(1l);
        requirementSolution.setRequirementName("requirementName");
        requirementSolution.setSupplierId(1l);
        requirementSolution.setSupplierName("Michael");
        requirementSolution.setTechnology(92);
        requirementSolution.setQuality(1000);
        requirementSolution.setReaction(DateTime.now().toDate());
        requirementSolution.setDelivery(100);
        requirementSolution.setCost(100);
        requirementSolution.setStatus(0);
        requirementSolution.setSolutionFile("[{\"name\":\"关于我们.xlsx\"," +
                "\"url\":\"http://l.ihaier.com/assets/2014/07/28/20052_c6baf511255d97cf88a0bdce344a3a66.xlsx\"," +
                "\"version\":\"2014-07-28 22:14:06\"}]");

        return requirementSolution;
    }
}
