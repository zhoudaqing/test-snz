package io.terminus.snz.eai.ignore;

import com.haier.OuterSysVendorInfoToMDM.OuterSysVendorInfoToMDM;
import com.haier.OuterSysVendorInfoToMDM.OuterSysVendorInfoToMDM_Service;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-8-8
 */
@Ignore
public class MdmVendorCodeServiceTest {

    private OuterSysVendorInfoToMDM outerSysVendorInfoToMDM;

    @Before
    public void init(){
        OuterSysVendorInfoToMDM_Service outerSysVendorInfoToMDM_service = new OuterSysVendorInfoToMDM_Service();
        outerSysVendorInfoToMDM = outerSysVendorInfoToMDM_service.getOuterSysVendorInfoToMDMSOAP();
    }

    @Test
    public void testOuterSysVendorInfoToMDM(){
        outerSysVendorInfoToMDM.outerSysVendorInfoToMDM(
                "123", "123", "123", "123", "321", "456", "123",
                "456", "321", null, null, null, null, null, null,
                null, null, null, null, null);
    }
}
