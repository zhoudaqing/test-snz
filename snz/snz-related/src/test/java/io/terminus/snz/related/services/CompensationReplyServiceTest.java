package io.terminus.snz.related.services;

import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.related.BaseTest;
import io.terminus.snz.related.daos.CompensationReplyDao;
import io.terminus.snz.related.models.CompensationReply;
import io.terminus.snz.user.model.Company;
import io.terminus.snz.user.service.CompanyService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Author:  wenhaoli
 * Date: 2014-08-14
 */
public class CompensationReplyServiceTest extends BaseTest {

    @InjectMocks
    private CompensationReplyServiceImpl compensationReplyService;

    @Mock
    private CompensationReplyDao compensationReplyDao;

    @Mock
    private CompanyService companyService;

    @Test
    public void testCreate() {

        BaseUser user = new BaseUser();
        user.setType(2);
        user.setName("abc");
        user.setId(1L);
        Company company = new Company();
        company.setUserId(1L);
        company.setId(1L);
        Response<Company> companyRes = new Response<Company>();
        companyRes.setResult(company);
        when(companyService.findCompanyByUserId(1L)).thenReturn(companyRes);
        Long id = 1L;
        when(compensationReplyDao.create(any(CompensationReply.class))).thenReturn(id);
        CompensationReply compensationReply = new CompensationReply();
        when(compensationReplyDao.findById(1L)).thenReturn(compensationReply);
        CompensationReply reply = new CompensationReply();
        reply.setContent("哼哼哈嘿！");
        Response<CompensationReply> test = compensationReplyService.create(user, reply, 1L);
        assertNotNull(test.getResult());

    }
}
