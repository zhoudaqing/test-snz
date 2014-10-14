package io.terminus.snz.statistic.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import io.terminus.pampas.common.BaseUser;
import io.terminus.snz.statistic.dao.SupplierSolutionCountDao;
import io.terminus.snz.statistic.model.SupplierSolutionCount;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class SolutionCountServiceTest {

    BaseUser user = null;

    @Before
    public void init(){
        user = new BaseUser();
        user.setId(1l);
        user.setName("Michale");
        user.setMobile("18657327206");

        MockitoAnnotations.initMocks(this);
    }

    @Mock
    private SupplierSolutionCountDao supplierSolutionCountDao;

    @InjectMocks
    private SolutionCountServiceImpl solutionCountService;

    @Test
    public void testSetSupCount() throws Exception {
        supplierSolutionCountDao.setSupCount(any(SupplierSolutionCount.class));
        assertNotNull(solutionCountService.setSupCount(mock()));
    }

    @Test
    public void testUpdateBatchCounts() throws Exception {
        supplierSolutionCountDao.updateBatchCounts(Lists.newArrayList(1l , 2l), 1, 2);
        assertNotNull(solutionCountService.updateBatchCounts(Lists.newArrayList(1l , 2l), 1, 2));
    }

    @Test
    public void testFindSupplierCount() throws Exception {
        when(supplierSolutionCountDao.findSupplierCount(1l)).thenReturn(mock());
        assertNotNull(solutionCountService.findSupplierCount(null));
        assertNotNull(solutionCountService.findSupplierCount(user));
    }

    public SupplierSolutionCount mock(){
        SupplierSolutionCount supplierSolutionCount = new SupplierSolutionCount();
        supplierSolutionCount.setUserId(1l);
        supplierSolutionCount.setUserName("supplierName");
        supplierSolutionCount.setStatusCounts(ImmutableMap.of(1, 1, 2, 3));

        return supplierSolutionCount;
    }
}