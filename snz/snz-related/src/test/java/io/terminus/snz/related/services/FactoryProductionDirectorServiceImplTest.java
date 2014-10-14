package io.terminus.snz.related.services;

import com.google.common.collect.Lists;
import io.terminus.pampas.common.Response;
import io.terminus.snz.related.daos.FactoryProductionDirectorDao;
import io.terminus.snz.related.models.FactoryProductionDirector;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class FactoryProductionDirectorServiceImplTest {

    @InjectMocks
    private FactoryProductionDirectorServiceImpl factoryProductionDirectorService;

    @Mock
    private FactoryProductionDirectorDao factoryProductionDirectorDao;

    private FactoryProductionDirector one = null;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        one = new FactoryProductionDirector();
        one.setDirectorName("总监");
    }

    @Test
    public void testFindByFactoryNumAndProductLineName() throws Exception {
        // 测试查到结果
        when(factoryProductionDirectorDao.findBy(any(FactoryProductionDirector.class)))
                .thenReturn(Lists.newArrayList(one));

        Response<List<FactoryProductionDirector>> directorRes = factoryProductionDirectorService.findByFactoryNumAndProductLineName("num1", "name1");
        assertTrue(directorRes.isSuccess());
        assertThat(directorRes.getResult().size(), is(1));

        // 测试查不到
        when(factoryProductionDirectorDao.findBy(any(FactoryProductionDirector.class)))
                .thenReturn(Collections.<FactoryProductionDirector>emptyList());

        directorRes = factoryProductionDirectorService.findByFactoryNumAndProductLineName("num1", "name1");
        assertTrue(directorRes.isSuccess());
        assertTrue(directorRes.getResult().isEmpty());

        // Exception
        when(factoryProductionDirectorDao.findBy(any(FactoryProductionDirector.class)))
                .thenThrow(new RuntimeException());

        directorRes = factoryProductionDirectorService.findByFactoryNumAndProductLineName("num1", "name2");
        assertFalse(directorRes.isSuccess());
    }

    @Test
    public void testFindByUserNick() throws Exception {
        // 测试查到结果
        when(factoryProductionDirectorDao.findBy(any(FactoryProductionDirector.class)))
                .thenReturn(Lists.newArrayList(one));

        Response<List<FactoryProductionDirector>> directorRes = factoryProductionDirectorService.findByUserNick("nick");
        assertTrue(directorRes.isSuccess());
        assertThat(directorRes.getResult().size(), is(1));

        // 测试查不到
        when(factoryProductionDirectorDao.findBy(any(FactoryProductionDirector.class)))
                .thenReturn(Collections.<FactoryProductionDirector>emptyList());

        directorRes = factoryProductionDirectorService.findByUserNick("nick");
        assertTrue(directorRes.isSuccess());
        assertTrue(directorRes.getResult().isEmpty());

        // Exception
        when(factoryProductionDirectorDao.findBy(any(FactoryProductionDirector.class)))
                .thenThrow(new RuntimeException());

        directorRes = factoryProductionDirectorService.findByUserNick("nick");
        assertFalse(directorRes.isSuccess());
    }
}