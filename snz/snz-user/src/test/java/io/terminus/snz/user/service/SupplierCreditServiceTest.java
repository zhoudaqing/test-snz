package io.terminus.snz.user.service;

import com.google.common.collect.Lists;
import io.terminus.common.model.Paging;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.dao.CompanyDao;
import io.terminus.snz.user.dao.SupplierCreditQualifyDao;
import io.terminus.snz.user.dto.SupplierCreditQualifyDto;
import io.terminus.snz.user.model.Company;
import io.terminus.snz.user.model.SupplierCreditQualify;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

/**
 * Date: 8/4/14
 * Time: 9:45
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */
public class SupplierCreditServiceTest extends BaseServiceTest {

    @InjectMocks
    SupplierCreditQualifyServiceImpl supplierCreditQualifyService;

    @Mock
    SupplierCreditQualifyDao supplierCreditQualifyDao;

    @Mock
    CompanyService companyService;

    @Mock
    CompanyDao companyDao;

    @Test
    public void shouldPagingCreditQualify() {
        List<SupplierCreditQualify> mockList = Lists.newArrayList(new SupplierCreditQualify());
        Paging<SupplierCreditQualify> mockPage = new Paging<SupplierCreditQualify>(1l, mockList);
        when(supplierCreditQualifyDao.pagingForQualify(anyInt(), anyInt(), anyInt())).thenReturn(mockPage);
        when(companyDao.findById(anyLong())).thenReturn(new Company());

        Response<Paging<SupplierCreditQualifyDto>> pageGet = supplierCreditQualifyService.pagingCreditQualify(loginer, 1, 0, 20);
        assertTrue(pageGet.isSuccess());
        assertFalse(pageGet.getResult().getData().isEmpty());
        assertFalse(supplierCreditQualifyService.pagingCreditQualify(null, 1, 0, 20).isSuccess());
    }

    @Test
    public void shouldUpdateCreditQualify() {
        when(supplierCreditQualifyDao.findById(anyLong())).thenReturn(new SupplierCreditQualify());
        when(supplierCreditQualifyDao.update(Matchers.<SupplierCreditQualify>anyObject())).thenReturn(null);

        Response<Boolean> tryUpdate = supplierCreditQualifyService.updateCreditQualify(loginer, 1l, "", 2);
        assertTrue(tryUpdate.isSuccess());
        assertFalse(supplierCreditQualifyService.updateCreditQualify(null, -1l, "", 2).isSuccess());

        when(supplierCreditQualifyDao.findById(anyLong())).thenReturn(null);
        assertFalse(supplierCreditQualifyService.updateCreditQualify(loginer, 1l, "", 2).isSuccess());
    }

    @Test
    public void shouldApplyCreditQualify() {
        Response<Company> companyResult = new Response<Company>();
        Company company = new Company();
        company.setId(1l);
        companyResult.setResult(company);
        when(companyService.findCompanyByUserId(anyLong())).thenReturn(companyResult);
        when(supplierCreditQualifyDao.findBy(any(SupplierCreditQualify.class))).thenReturn(null);
        when(supplierCreditQualifyDao.create(Matchers.<SupplierCreditQualify>anyObject())).thenReturn(null);

        Response<Long> supApply = supplierCreditQualifyService.applyCreditQualify(loginer);
        assertTrue(supApply.isSuccess());

        // cover illegal argument
        assertFalse(supplierCreditQualifyService.applyCreditQualify(null).isSuccess());

        // if found one
        SupplierCreditQualify scqMock = new SupplierCreditQualify();
        scqMock.setUserId(1l);
        scqMock.setId(1l);
        when(supplierCreditQualifyDao.findBy(any(SupplierCreditQualify.class))).thenReturn(scqMock);
        when(supplierCreditQualifyDao.update(any(SupplierCreditQualify.class))).thenReturn(true);
        assertTrue(supplierCreditQualifyService.applyCreditQualify(loginer).isSuccess());

        Response companyGetMock = new Response();
        companyGetMock.setError("any werror");
        when(companyService.findCompanyByUserId(anyLong())).thenReturn(companyGetMock);
        assertFalse(supplierCreditQualifyService.applyCreditQualify(loginer).isSuccess());
    }

    @Test
    public void shouldFindCreditQualifyByUserIds() {
        when(supplierCreditQualifyDao.findByUserIds(anyListOf(Long.class)))
                .thenReturn(Lists.<SupplierCreditQualify>newArrayList());

        Response<List<SupplierCreditQualify>> supsGet =
                supplierCreditQualifyService.findCreditQualifyByUserIds(Lists.newArrayList(1l, 2l));

        assertTrue(supsGet.isSuccess());
    }

    @Test
    public void shouldFindCreditQualifyByUserId() {
        when(supplierCreditQualifyDao.findBy(any(SupplierCreditQualify.class))).thenReturn(null);

        Response<SupplierCreditQualify> supGet = supplierCreditQualifyService.findCreditQualifyByUserId(1l);
        assertTrue(supGet.isSuccess());
        assertNotNull(supGet.getResult());

        // should cover illegal argument exception
        assertFalse(supplierCreditQualifyService.findCreditQualifyByUserId(-1l).isSuccess());
    }
}
