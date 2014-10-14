package io.terminus.snz.related.services;

import io.terminus.snz.related.BaseTest;
import io.terminus.snz.related.daos.FactoryOrganDao;
import io.terminus.snz.related.models.FactoryOrgan;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

/**
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-8-4
 */
public class FactoryOrganServiceTest extends BaseTest {

    @Mock
    private FactoryOrganDao factoryOrganDao;

    @InjectMocks
    private FactoryOrganServiceImpl factoryOrganService;

    @Test
    public void testBatchAdd(){
        List<FactoryOrgan> factoryOrgans = Arrays.asList(mockFactoryOrgan(), mockFactoryOrgan());
        factoryOrganService.batchAdd(factoryOrgans);
    }

    @Test
    public void testFindOrganByFactory(){
        when(factoryOrganDao.findOrganByFactory(anyString())).thenReturn(Arrays.asList(""));
        assertEquals(1, factoryOrganService.findOrganByFactory("xxx").getResult().size());
    }

    private FactoryOrgan mockFactoryOrgan(){
        FactoryOrgan fo = new FactoryOrgan();
        fo.setFactory("xxx");
        fo.setOrgan("ooo");
        return fo;
    }
}
