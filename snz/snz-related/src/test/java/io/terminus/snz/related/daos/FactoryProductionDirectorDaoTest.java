package io.terminus.snz.related.daos;

import io.terminus.snz.related.BaseDaoTest;
import io.terminus.snz.related.models.FactoryProductionDirector;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class FactoryProductionDirectorDaoTest extends BaseDaoTest {

    @Autowired
    private FactoryProductionDirectorDao factoryProductionDirectorDao;

    @Test
    public void testFindBy() throws Exception {
        FactoryProductionDirector one = new FactoryProductionDirector();
        one.setDirectorId("00001234");
        one.setDirectorName("总监");
        one.setFactoryNum("9031");
        one.setDescription("工厂1");
        one.setProductLineId(3L);
        one.setProductLineName("产品线3");

        FactoryProductionDirector two = new FactoryProductionDirector();
        two.setDirectorId("00001234");
        two.setDirectorName("总监");
        two.setFactoryNum("9032");
        two.setDescription("工厂2");
        two.setProductLineId(4L);
        two.setProductLineName("产品线4");

        FactoryProductionDirector three = new FactoryProductionDirector();
        three.setDirectorId("00001234");
        three.setDirectorName("总监");
        three.setFactoryNum("9031");
        three.setDescription("工厂1");
        three.setProductLineId(3L);
        three.setProductLineName("产品线3");

        factoryProductionDirectorDao.create(one);
        factoryProductionDirectorDao.create(two);
        factoryProductionDirectorDao.create(three);

        // 1. 根据 factoryNum 和 productLineName 搜
        FactoryProductionDirector params = new FactoryProductionDirector();
        params.setFactoryNum("9031");
        params.setProductLineName("产品线3");
        List<FactoryProductionDirector> directors = factoryProductionDirectorDao.findBy(params);

        assertNotNull(directors);
        assertThat(directors.size(), is(2));

        // 2. 根据 directorId（工号）查找
        params = new FactoryProductionDirector();
        params.setDirectorId("00001234");
        directors = factoryProductionDirectorDao.findBy(params);

        assertNotNull(directors);
        assertThat(directors.size(), is(3));

        // 3. 找不到
        params.setDirectorId("no id");
        directors = factoryProductionDirectorDao.findBy(params);

        assertNotNull(directors);
        assertTrue(directors.isEmpty());
    }
}