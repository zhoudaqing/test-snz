package io.terminus.snz.eai.service;

import com.haier.OuterSysVendorInfoToMDM.*;
import io.terminus.snz.eai.dto.MDMVendorCodeApply;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.xml.ws.Holder;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;

/**
 * Date: 8/5/14
 * Time: 16:30
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */
public class MdmVendorCodeServiceTest extends BaseServiceTest {

    @InjectMocks
    MdmVendorCodeServiceImpl mdmVendorCodeService;

    @Mock
    OuterSysVendorInfoToMDM_Service service;

    @Mock
    OuterSysVendorInfoToMDM port;

    @Test
    public void shouldApplyVendorCode() {
        doNothing().when(port).outerSysVendorInfoToMDM(anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(),
                any(RSPVENDORCOMPANYTABLE.class), any(RSPVENDORBANKTABLE.class), any(RSPVENDORPURTABLE.class),
                anyString(), anyString(), anyString(),
                any(Holder.class), any(Holder.class), any(Holder.class), any(Holder.class), any(Holder.class));

        // should through illegal argument
        assertNotNull(mdmVendorCodeService.applyVendorCode(null));

        MDMVendorCodeApply apply = new MDMVendorCodeApply();
        mdmVendorCodeService.applyVendorCode(apply);
    }
}
