package io.terminus.snz.user.service;

import com.google.common.collect.Lists;
import io.terminus.pampas.common.Response;
import io.terminus.snz.category.model.FrontendCategory;
import io.terminus.snz.category.service.FrontendCategoryService;
import io.terminus.snz.user.dao.CompanyDao;
import io.terminus.snz.user.dao.CompanyMainBusinessDao;
import io.terminus.snz.user.model.Company;
import io.terminus.snz.user.model.CompanyMainBusiness;
import io.terminus.snz.user.model.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

/**
 * Created by yangzefeng on 14-8-4
 */
public class RichSupplierTest {

    @InjectMocks
    private RichSuppliers richSuppliers;

    @Mock
    private CompanyDao companyDao;

    @Mock
    private FrontendCategoryService frontendCategoryService;

    @Mock
    private CompanyMainBusinessDao companyMainBusinessDao;

    private Company company;

    private List<CompanyMainBusiness> businessList;

    private Response<List<FrontendCategory>> response = new Response<List<FrontendCategory>>();

    private User user;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        company = new Company();
        company.setId(1l);
        company.setNature(1);
        company.setCorpAddr("test");

        businessList = Lists.newArrayList();

        List<FrontendCategory> frontCategories = Lists.newArrayList();

        response.setResult(frontCategories);

        user = new User();
        user.setId(1l);
        user.setNick("test");
    }

    @Test
    public void testRichSuppliers() {
        when(companyDao.findByUserId(anyLong())).thenReturn(company);

        when(companyMainBusinessDao.findByCompanyId(anyLong())).thenReturn(businessList);

        when(frontendCategoryService.findByIds(anyList())).thenReturn(response);

        assertNotNull(richSuppliers.make(user));
    }
}
