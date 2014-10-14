package io.terminus.snz.web.jobs;

import io.terminus.pampas.common.Response;
import io.terminus.snz.related.models.AddressPark;
import io.terminus.snz.related.services.DeliveryService;
import io.terminus.snz.user.service.SupplierSummaryService;
import io.terminus.snz.web.BaseTest;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-8-5
 */
public class SupplierSummaryTest extends BaseTest{

    @Mock
    private DeliveryService deliveryService;

    @Mock
    private SupplierSummaryService supplierSummaryService;

    @InjectMocks
    private SupplierSummary supplierSummary;

    @Test
    public void testCountSupplier(){
        Response<List<AddressPark>> mockResp = new Response<List<AddressPark>>();
        mockResp.setResult(Arrays.asList(mockAddressPark()));
        when(deliveryService.findAllPark()).thenReturn(mockResp);
        supplierSummary.countSupplier();
    }

    @Test
    public void testCountSupplierByLevel(){
        supplierSummary.countSupplierByLevel();
    }
}
