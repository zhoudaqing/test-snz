package io.terminus.snz.user.manager;

import io.terminus.snz.user.BaseTest;
import io.terminus.snz.user.dao.*;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;

/**
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-8-4
 */
public class SupplierSummaryManagerTest extends BaseTest{

    @Mock
    private CompanySupplyParkDao companySupplyParkDao;

    @Mock
    private SupplierCountBySupplyParkDao supplierCountBySupplyParkDao;

    @Mock
    private SupplierCountByStatusDao supplierCountByStatusDao;

    @Mock
    private SupplierCountByLevelDao supplierCountByLevelDao;

    @Mock
    private SupplierCountByIndustryDao supplierCountByIndustryDao;

    @InjectMocks
    private SupplierSummaryManager supplierSummaryManager;

    @Test
    public void testCreateSupplierSummaryBySupplyParkIds(){
        supplierSummaryManager.createSupplierSummaryBySupplyParkIds(Arrays.asList(1L, 2L));
    }

    @Test
    public void testCreateSupplierSummaryByStatus(){
        supplierSummaryManager.createSupplierSummaryByStatus(Arrays.asList(mockSupplierCountByStatus()));
    }

    @Test
    public void testCreateSupplierSummaryByLevel(){
        supplierSummaryManager.createSupplierSummaryByLevel(Arrays.asList(mockSupplierCountByLevel()));
    }

    @Test
    public void testCreateSupplierSummaryByIndustry(){
        supplierSummaryManager.createSupplierSummaryByIndustry(Arrays.asList(mockSupplierCountByIndustry()));
    }
}
