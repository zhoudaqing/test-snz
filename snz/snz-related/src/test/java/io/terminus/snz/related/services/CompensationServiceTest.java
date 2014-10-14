package io.terminus.snz.related.services;


import io.terminus.snz.related.BaseTest;
import io.terminus.snz.related.daos.CompensationDao;
import io.terminus.snz.related.models.Compensation;
import io.terminus.snz.user.service.CompanyService;
import io.terminus.snz.user.service.SupplierTQRDCInfoTmpService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.junit.Assert.assertFalse;

/**
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-8-4
 */
public class CompensationServiceTest extends BaseTest{

    @InjectMocks
    private CompensationServiceImpl compensationService;

    @Mock
    private CompensationDao compensationDao;

    @Mock
    private FactoryProductionDirectorService factoryProductionDirectorService;

    @Mock
    private CompanyService companyService;

    @Mock
    private SupplierTQRDCInfoTmpService supplierTQRDCInfoTmpService;

    @Test
    public void testPaging(){
        // user not login
        assertFalse(compensationService.paging(null, 1, 1, null, null, null).isSuccess());
        compensationService.paging(loginer, 1, 1, null, null, null);
    }

    @Test
    public void testBatchAdd(){
        assertFalse(compensationService.batchAdd(Collections.<Compensation>emptyList()).isSuccess());
        compensationService.batchAdd(Arrays.asList(mockCompensation()));
    }

    @Test
    public void testUpdateStatus(){
        assertFalse(compensationService.updateStatus(null, 1L, Compensation.Status.PASS.value()).isSuccess());
        compensationService.updateStatus(loginer, 1L, Compensation.Status.PASS.value());
    }

    private Compensation mockCompensation() {
        Compensation c = new Compensation();
        c.setEnteredAt(new Date());
        c.setPark("xx");
        c.setFactory("9010");
        c.setProductLine("冰箱");
        return c;
    }
}
