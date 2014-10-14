package io.terminus.snz.requirement.service;

import io.terminus.pampas.common.BaseUser;
import io.terminus.snz.user.service.CompanyService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Date: 8/12/14
 * Time: 12:29
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */
public class ReparationServiceTest {

    @InjectMocks
    ReparationServiceImpl reparationService;

    @Mock
    CompanyService companyService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldFindSupplierReparations() {
        BaseUser user = new BaseUser();
        user.setId(1l);
        reparationService.findSupplierReparationBy(user, 7L, null, null, "2014-08-01", "2014-08-07", 0, 20);
    }
}
