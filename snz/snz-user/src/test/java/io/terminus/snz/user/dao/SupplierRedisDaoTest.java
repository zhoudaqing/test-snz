package io.terminus.snz.user.dao;

import com.google.common.collect.Lists;
import io.terminus.common.redis.utils.JedisTemplate;
import io.terminus.common.utils.JsonMapper;
import io.terminus.snz.user.dao.redis.CompanyRedisDao;
import io.terminus.snz.user.dto.CompanyDto;
import io.terminus.snz.user.model.Company;
import io.terminus.snz.user.model.CompanyMainBusiness;
import io.terminus.snz.user.model.CompanySupplyPark;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Author:Guo Chaopeng
 * Created on 14-7-15.
 */
public class SupplierRedisDaoTest {

    @Mock
    private JedisTemplate jedisTemplate;

    @InjectMocks
    private CompanyRedisDao companyRedisDao;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSaveSupplierOldInfo() {

        Company company = new Company();
        company.setSupplierCode("rer323");
        company.setInitAgent("443");
        company.setIsComplete(1);

        List<CompanyMainBusiness> companyMainBusinesses = Lists.newArrayList();

        CompanyMainBusiness companyMainBusiness = new CompanyMainBusiness();
        companyMainBusiness.setUserId(2L);
        companyMainBusiness.setName("re");

        CompanyMainBusiness companyMainBusiness2 = new CompanyMainBusiness();
        companyMainBusiness2.setUserId(7L);
        companyMainBusiness2.setName("fre");

        companyMainBusinesses.add(companyMainBusiness);
        companyMainBusinesses.add(companyMainBusiness2);

        List<CompanySupplyPark> companySupplyParks = Lists.newArrayList();

        CompanySupplyPark companySupplyPark = new CompanySupplyPark();
        companySupplyPark.setCompanyId(1L);
        companySupplyPark.setName("fr");

        companySupplyParks.add(companySupplyPark);

        CompanyDto companyDto = new CompanyDto();
        companyDto.setCompany(company);
        companyDto.setCompanyMainBusinesses(companyMainBusinesses);
        companyDto.setCompanySupplyParks(companySupplyParks);

        String baseCompanyInfoJson = JsonMapper.JSON_NON_EMPTY_MAPPER.toJson(companyDto);
        when(jedisTemplate.execute(any(JedisTemplate.JedisAction.class))).thenReturn(baseCompanyInfoJson);

        CompanyDto dto = companyRedisDao.getBaseCompanyInfo(1L);

        Assert.assertEquals(company.getSupplierCode(), dto.getCompany().getSupplierCode());
        Assert.assertEquals(companyMainBusinesses.get(0).getName(), dto.getCompanyMainBusinesses().get(0).getName());
        Assert.assertEquals(companySupplyParks.get(0).getName(), dto.getCompanySupplyParks().get(0).getName());

    }
}
