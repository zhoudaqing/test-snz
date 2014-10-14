package io.terminus.snz.eai.ignore;

import io.terminus.pampas.common.Response;
import io.terminus.snz.eai.service.BaseServiceTest;
import io.terminus.snz.eai.service.HGVSServiceImpl;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 7/25/14
 */
@Ignore
public class HGVSServiceImplTest extends BaseServiceTest {

    @InjectMocks
    private HGVSServiceImpl hgvsService;

    @Test
    public void testGetBalanceBySupplierCode() throws Exception {
        Response<Long> balanceRes = hgvsService.getBalanceBySupplierCode("V12359");
        assertTrue(balanceRes.isSuccess());
        assertThat(balanceRes.getResult(), is(-40660837L));
    }
}
