package io.terminus.snz.requirement.dao;

import com.google.common.collect.Lists;
import io.terminus.snz.requirement.model.ModuleSolution;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Desc:模块方案测试
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-03.
 */
public class ModuleSolutionDaoTest extends BasicTest {
    @Autowired
    private ModuleSolutionDao moduleSolutionDao;

    @Test
    public void createTest(){
        moduleSolutionDao.create(mock());
    }

    @Test
    public void testCreateBatch(){
        List<ModuleSolution> solutions = Lists.newArrayList();
        solutions.add(mock());
        solutions.add(mock());
        solutions.add(mock());
        moduleSolutionDao.createBatch(solutions);
    }

    @Test
    public void updateTest(){
        ModuleSolution moduleSolution = mock();
        moduleSolution.setId(1l);

        moduleSolutionDao.update(moduleSolution);
    }

    @Test
    public void findByIdTest(){
        assertThat(moduleSolutionDao.findById(1l), notNullValue());
    }

    @Test
    public void testCountBySolutionId(){
        System.out.println(moduleSolutionDao.countBySolutionId(1l));
    }

    @Test
    public void testDeleteBySolutionId(){
        moduleSolutionDao.deleteBySolutionId(1l);
    }

    @Test
    public void testFindExist(){
        moduleSolutionDao.findExist(1l , 1l);
    }

    @Test
    public void testFindAllSolutions(){
        moduleSolutionDao.findAllSolutions(1l);
    }

    private ModuleSolution mock(){
        ModuleSolution moduleSolution = new ModuleSolution();
        moduleSolution.setSolutionId(1l);
        moduleSolution.setModuleId(1l);
        moduleSolution.setModuleName("moduleName");
        moduleSolution.setTechnology("technology");
        moduleSolution.setQuality(100);
        moduleSolution.setReaction(DateTime.now().toDate());
        moduleSolution.setDelivery(10);
        moduleSolution.setCost(100);

        return moduleSolution;
    }
}
