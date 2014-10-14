package io.terminus.snz.user.service;

import com.google.common.base.Optional;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.manager.SupplierResourceMaterialManager;
import io.terminus.snz.user.model.Company;
import io.terminus.snz.user.model.SupplierResourceMaterialInfo;
import io.terminus.snz.user.model.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 9/3/14
 */
public class SupplierResourceMaterialServiceImplTest {

    @InjectMocks
    private SupplierResourceMaterialServiceImpl supplierResourceMaterialService;

    @Mock
    private SupplierResourceMaterialManager supplierResourceMaterialManager;

    @Mock
    private CompanyService companyService;

    private BaseUser loginer;

    private User user;

    private Company company;

    @Before
    public void setup() {
        user = new User();
        user.setId(1L);
        user.setNick("东海重工");

        loginer = new BaseUser();
        loginer.setId(user.getId());

        company = new Company();
        company.setId(1L);
        company.setUserId(user.getId());
        company.setCorporation("东海重工,. LTD");

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetInfo() {
        Response<Company> companyResp = new Response<Company>();
        companyResp.setResult(company);
        when(companyService.findCompanyByUserId(loginer.getId())).thenReturn(companyResp);

        when(supplierResourceMaterialManager.getInfo(company.getId()))
                .thenReturn(Optional.<SupplierResourceMaterialInfo>fromNullable(null));

        Response<SupplierResourceMaterialInfo> infoResp = supplierResourceMaterialService.getInfo(loginer);
        assertTrue(infoResp.isSuccess());
        assertThat(infoResp.getResult().getTimes(), is(0L));
    }
}