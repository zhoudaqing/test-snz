package io.terminus.snz.statistic.dao;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import io.terminus.common.redis.utils.JedisTemplate;
import io.terminus.snz.statistic.model.SupplierSolutionCount;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class SupplierSolutionCountDaoTest {
    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Mock
    private JedisTemplate jedisTemplate;

    @InjectMocks
    private SupplierSolutionCountDao supplierSolutionCountDao;

    @Test
    public void testSetSupCount() throws Exception {
        jedisTemplate.execute(any(JedisTemplate.JedisActionNoResult.class));
        supplierSolutionCountDao.setSupCount(mock());
    }

    @Test
    public void testUpdateBatchCounts() throws Exception {
        jedisTemplate.execute(any(JedisTemplate.JedisActionNoResult.class));
        supplierSolutionCountDao.updateBatchCounts(Lists.newArrayList(1l , 2l) , 1, 2);
    }

    @Test
    public void testFindSupplierCount() throws Exception {
        when(jedisTemplate.execute(any(JedisTemplate.JedisAction.class))).thenReturn(mock());
        assertNotNull(supplierSolutionCountDao.findSupplierCount(1l));
    }

    public SupplierSolutionCount mock(){
        SupplierSolutionCount supplierSolutionCount = new SupplierSolutionCount();
        supplierSolutionCount.setUserId(1l);
        supplierSolutionCount.setUserName("supplierName");
        supplierSolutionCount.setStatusCounts(ImmutableMap.of(1, 1, 2, 3));

        return supplierSolutionCount;
    }
}