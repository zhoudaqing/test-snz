package io.terminus.snz.user.dao;

import com.google.common.collect.Lists;
import io.terminus.common.model.PageInfo;
import io.terminus.common.model.Paging;
import io.terminus.common.utils.JsonMapper;
import io.terminus.snz.user.dto.RichSupplierDto;
import io.terminus.snz.user.model.Company;
import io.terminus.snz.user.model.CompanyMainBusiness;
import io.terminus.snz.user.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Description：
 * Author：Guo Chaopeng
 * Created on 14-5-3-下午5:46
 */
public class CompanyDaoTest extends TestBaseDao {

    private Company company;

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private CompanyMainBusinessDao companyMainBusinessDao;

    private void mock() {
        company = new Company();
        company.setUserId(2L);
        company.setIsComplete(0);
        company.setIncludeKeywords("大华好");
        company.setProductLine("1,2");
        company.setBusinessLicense("/img/123.jpg");
        company.setBlDate(new Date());
        company.setCorporation("娃哈哈牛");
        company.setCorpAddr("杭州");
        company.setGroupName("哈哈");
        company.setCorpAddr("北京");
        company.setDesc("我公司很强");
        company.setFixedAssets(45000000L);
        company.setFaCoinType(Company.CoinType.GBP.value());
        company.setFoundAt(new Date());
        company.setInitAgent("端点网络");
        company.setNature(1);
        company.setListedStatus(0);
        company.setListedRegion("香港");
        company.setTicker("35352");
        company.setOrgCert("/img/435.jpg");
        company.setOcDate(new Date());
        company.setTaxNo("/img/434.jpg");
        company.setTnDate(new Date());
        company.setZipcode("323535");
        company.setRegCapital(4545767L);
        company.setRcCoinType(Company.CoinType.EUR.value());
        company.setRegCountry(3);
        company.setRegProvince(43);
        company.setRegCity(54);
        company.setPersonScale("1000 人以上");
        company.setCustomers("女性");
        company.setWorldTop(0);
        company.setOfficialWebsite("www.baidu.com");
        company.setSupplierCode("v12334");
        company.setActingBrand("兔兔");
        company.setBusinessLicenseId("3434");
        company.setOrgCertId("3343");
        company.setTaxNoId("4545");
        company.setFactories("ewew");
        company.setParticipateCount(0);
        company.setResourceType(Company.ResourceType.NORMAL.value());
        company.setCompetitors("百度,阿里巴巴");

    }

    @Before
    public void setUp() {
        mock();
        companyDao.create(company);
    }

    @Test
    public void testCreate() {
        assertNotNull(company.getId());
    }

    @Test
    public void testFindById() {
        Company model = companyDao.findById(company.getId());
        Assert.assertTrue(model.getId() == company.getId());
    }

    @Test
    public void testFindByUserId() {
        Company model = companyDao.findByUserId(company.getUserId());
        Assert.assertTrue(model.getId() == company.getId());
    }

    @Test
    public void testDelete() {
        companyDao.delete(company.getId());
        Company model = companyDao.findById(company.getId());
        Assert.assertNull(model);
    }

    @Test
    public void testUpdate() {
        Company updatedModel = new Company();
        updatedModel.setId(company.getId());
        updatedModel.setInitAgent("阿里巴巴");
        updatedModel.setCustomers("男性");
        updatedModel.setOcDate(new Date());
        updatedModel.setRcCoinType(Company.CoinType.JPY.value());
        updatedModel.setFixedAssets(35L);
        updatedModel.setFaCoinType(Company.CoinType.GBP.value());
        updatedModel.setActingBrand("aa");
        updatedModel.setSupplierCode("de3455");
        updatedModel.setNature(1);
        updatedModel.setIsComplete(1);
        updatedModel.setProductLine("1,3");
        updatedModel.setBusinessLicenseId("434");
        updatedModel.setOrgCertId("454");
        updatedModel.setTaxNoId("5545");
        updatedModel.setFactories("rrr");
        updatedModel.setIncludeKeywords("bb");
        updatedModel.setResourceType(Company.ResourceType.BENCH_MARK.value());
        updatedModel.setCompetitors("腾讯");

        companyDao.update(updatedModel);

        Company model = companyDao.findById(company.getId());
        assertEquals(model.getRcCoinType().intValue(), updatedModel.getRcCoinType().intValue());
        assertEquals(model.getIsComplete().intValue(), updatedModel.getIsComplete().intValue());
        assertEquals(model.getIncludeKeywords(), updatedModel.getIncludeKeywords());
        assertEquals(model.getProductLine(), updatedModel.getProductLine());
        assertEquals(model.getFixedAssets(), model.getFixedAssets());
        assertEquals(model.getFaCoinType().intValue(), updatedModel.getFaCoinType().intValue());
        assertEquals(model.getActingBrand(), updatedModel.getActingBrand());
        assertEquals(model.getSupplierCode(), updatedModel.getSupplierCode());
        assertEquals(model.getBusinessLicenseId(), updatedModel.getBusinessLicenseId());
        assertEquals(model.getOrgCertId(), updatedModel.getOrgCertId());
        assertEquals(model.getTaxNoId(), updatedModel.getTaxNoId());
        assertEquals(model.getFactories(), updatedModel.getFactories());
        assertEquals(model.getResourceType(), updatedModel.getResourceType());
        assertEquals(model.getCompetitors(), updatedModel.getCompetitors());
    }

    @Test
    public void testUpdateParticipateCount() {
        companyDao.updateParticipateCount(company.getId());
        Company model = companyDao.findById(company.getId());
        assertEquals(1, model.getParticipateCount().intValue());

        companyDao.updateParticipateCount(company.getId());
        Company model2 = companyDao.findById(company.getId());
        assertEquals(2, model2.getParticipateCount().intValue());
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = new ArrayList<Long>();
        ids.add(company.getId() + 1);
        ids.add(company.getId() + 2);

        List<Company> companies = companyDao.findByIds(ids);

        assertEquals(0, companies.size());

    }

    @Test
    public void testFindByCorporation() {

        List<Company> companies = companyDao.findByCorporation(company.getCorporation());

        assertEquals(1, companies.size());

    }

    @Test
    public void test() {

        RichSupplierDto richSupplierDto = new RichSupplierDto();

        User u = new User();
        u.setNick("jack");
        u.setEncryptedPassword("123456");
        u.setMobile("18768177783");
        u.setEmail("g11@qq.com");

        Company company = new Company();
        company.setCorporation("海尔");
        company.setInitAgent("张三");
        company.setRegCountry(1);
        company.setRegCapital(1000L);
        company.setFoundAt(new Date());
        company.setCustomers("年轻女性");
        company.setBusinessLicense("http://file.snz.com/w5ete.jpg");
        company.setBlDate(new Date());
        company.setOrgCert("http://file.snz.com/dgds.jpg");
        company.setOcDate(new Date());
        company.setTaxNo("http://file.snz.com/gsfwe.jpg");
        company.setTnDate(new Date());

        List<CompanyMainBusiness> companyMainBusinesses = new ArrayList<CompanyMainBusiness>();
        CompanyMainBusiness companyMainBusiness1 = new CompanyMainBusiness();
        companyMainBusiness1.setMainBusinessId(1L);
        companyMainBusiness1.setName("冰箱");

        CompanyMainBusiness companyMainBusiness2 = new CompanyMainBusiness();
        companyMainBusiness2.setMainBusinessId(2L);
        companyMainBusiness2.setName("风扇");

        companyMainBusinesses.add(companyMainBusiness1);
        companyMainBusinesses.add(companyMainBusiness2);

        richSupplierDto.setUser(u);
        richSupplierDto.setCompany(company);
        richSupplierDto.setCompanyMainBusinesses(companyMainBusinesses);

        String json = JsonMapper.nonEmptyMapper().toJson(richSupplierDto);

        System.out.println("=============================================");

        System.out.println(json);

        System.out.println("=============================================");
    }

    @Test
    public void testCountSupplier() {
        Long count = companyDao.countSupplier();
        assertEquals(count.intValue(), 1);
    }

    @Test
    public void testCountParticipatedSupplier() {
        Long count = companyDao.countParticipatedSupplier();
        assertEquals(0, count.intValue());
    }

    @Test
    public void testCountSupplierHasSupplierCode() {
        company.setSupplierCode("vvv32");
        Long count = companyDao.countSupplierHasSupplierCode();
        assertEquals(1, count.intValue());
    }

    @Test
    public void testloadAllUserIdAndSupplierCode() {
        Company mapList = companyDao.findBySupplierCode("v12334");
        assertNotNull(mapList);
    }


    @Test
    public void testFindCompanyBySupplierName() {
        List<Company> companies = companyDao.findCompanyBySupplierName("司");
        System.out.println(companies);
    }

    @Test
    public void shouldCountSupplierHasSupplierCode() {
        Paging page = companyDao.pagingCompanyHasSupplierCode("公司", 0, 20);
        assertNotNull(page);
    }

    @Test
    public void shouldFindCompanyHasVCode() {
        PageInfo page = new PageInfo(null, null);
        List found = companyDao.findCompanyHasVCode(page.toMap());
        assertFalse(found.isEmpty());
    }

    @Test
    public void testFindUserIdsByFuzzyCorporation() {
        List<Long> userIds = companyDao.findUserIdsByFuzzyCorporation("娃哈", Arrays.asList(company.getUserId()));
        Assert.assertEquals(1, userIds.size());
    }
}
