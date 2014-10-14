package io.terminus.snz.related.services;

import io.terminus.snz.related.BaseTest;
import io.terminus.snz.related.models.CompensationDetail;
import org.junit.Test;
import org.mockito.InjectMocks;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.junit.Assert.assertFalse;

/**
 * Author:  wenhaoli
 * Date: 2014-08-12
 */
public class CompensationDetailServiceTest extends BaseTest {

    @InjectMocks
    private CompensationDetailServiceImpl compensationDetailService;

    @InjectMocks
    private CompensationServiceImpl compensationService;

    @Test
    public void testbatchAdd(){
        assertFalse(compensationDetailService.batchAdd(Collections.<CompensationDetail>emptyList()).isSuccess());
        compensationDetailService.batchAdd(Arrays.asList(mockCompensation()));
    }

    private CompensationDetail mockCompensation() {
        CompensationDetail c = new CompensationDetail();
        c.setCurrentDay(new Date());
        c.setFactory("xx");
        return c;
    }
}
