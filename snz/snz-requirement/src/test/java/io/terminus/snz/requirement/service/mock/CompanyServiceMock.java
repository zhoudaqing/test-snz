package io.terminus.snz.requirement.service.mock;

import io.terminus.common.model.Paging;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.dto.*;
import io.terminus.snz.user.model.*;
import io.terminus.snz.user.service.CompanyService;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Desc:mock
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-30.
 */
@Service
public class CompanyServiceMock implements CompanyService {
    @Override
    public Response<Boolean> updateCompany(BaseUser baseUser, CompanyDto companyDto) {
        return null;
    }

    @Override
    public Response<Boolean> updatePaperwork(BaseUser baseUser, PaperworkDto paperworkDto) {
        return null;
    }

    @Override
    public Response<PaperworkDto> findPaperworkByUser(BaseUser baseUser) {
        return null;
    }

    @Override
    public Response<SupplierUpdatedInfoDto<PaperworkDto>> queryPaperworkByUserId(Long userId) {
        return null;
    }

    @Override
    public Response<CompanyDto> findBaseCompanyByUserId(BaseUser baseUser) {
        return null;
    }

    @Override
    public Response<SupplierUpdatedInfoDto<CompanyDto>> queryBaseCompanyByUserId(Long userId) {
        return null;
    }


    @Override
    public Response<Company> findCompanyById(Long id) {
        Response<Company> result = new Response<Company>();
        result.setResult(mock());

        return result;
    }

    @Override
    public Response<Company> findCompanyByUserId(Long userId) {
        Response<Company> result = new Response<Company>();
        result.setResult(mock());

        return result;
    }

    @Override
    public Response<Company> findUnCacheCompanyByUserId(Long userId) {
        return null;
    }

    @Override
    public Response<List<CompanyMainBusiness>> findCompanyMainBusiness(Long mainBusinessId) {
        return null;
    }

    @Override
    public Response<List<Long>> findCompanyIdsByMainBusinessIds(List<Long> mainBusinessIds) {
        return null;
    }

    @Override
    public Response<DetailSupplierDto> findDetailSupplierByUserId(Long userId) {
        return null;
    }

    @Override
    public Response<List<Company>> findCompaniesByIds(List<Long> ids) {
        return null;
    }

    @Override
    public Response<Boolean> updateParticipateCount(Long companyId) {
        return null;
    }

    @Override
    public Response<Boolean> createOrUpdateCompanyRank(BaseUser baseUser, CompanyRank companyRank) {
        return null;
    }

    @Override
    public Response<CompanyRank> findCompanyRankByUserId(BaseUser baseUser) {
        return null;
    }

    @Override
    public Response<SupplierUpdatedInfoDto<CompanyRank>> queryCompanyRankByUserId(Long userId) {
        return null;
    }

    @Override
    public Response<Boolean> createOrUpdateContactInfo(BaseUser baseUser, ContactInfo contactInfo) {
        return null;
    }

    @Override
    public Response<ContactInfo> findContactInfoByUserId(BaseUser baseUser) {
        return null;
    }

    @Override
    public Response<SupplierUpdatedInfoDto<ContactInfo>> queryContactInfoByUserId(Long userId) {
        return null;
    }

    @Override
    public Response<Boolean> createOrUpdateFinance(BaseUser baseUser, FinanceDto financeDto) {
        return null;
    }

    @Override
    public Response<FinanceDto> findFinanceByUserId(BaseUser baseUser) {
        return null;
    }

    @Override
    public Response<SupplierUpdatedInfoDto<FinanceDto>> queryFinanceByUserId(Long userId) {
        return null;
    }

    @Override
    public Response<Boolean> deleteAdditionalDoc(Long additionalDocId, BaseUser baseUser) {
        return null;
    }

    @Override
    public Response<Paging<SupplierDto>> findSupplierForMDMQualify(BaseUser user, @Nullable String supplierName, Integer pageNo, Integer size) {
        return null;
    }

    @Override
    public Response<Paging<SupplierDto>> findSupplierBy(BaseUser baseUser, Integer type, String nick, Integer pageNo, Integer size) {
        return null;
    }

    @Override
    public Response<Paging<SupplierDto>> findSupplierQualifyBy(Integer status, String nick, Integer pageNo, Integer size) {
        return null;
    }

    @Override
    public Response<Boolean> certificateExpired(BaseUser baseUser) {
        return null;
    }

    @Override
    public Response<Paging<SupplierTQRDCInfo>> findSupplierTQRDCInfoBy(String supplierName, String supplierCode, String month, String module, String orderBy, Integer compositeScoreStart, Integer compositeScoreEnd, Integer pageNo, Integer size) {
        return null;
    }

    @Override
    public Response<CompanyPerformance> findDetailSupplierTQRDCInfo(String supplierCode) {
        return null;
    }

    @Override
    public Response<CompanyPerformance> findDetailSupplierTQRDCInfoByUserId(Long userId) {
        return null;
    }

    @Override
    public Response<SupplierTQRDCInfo> findSupplierLastTQRDCInfoByUserId(Long userId) {
        return null;
    }

    @Override
    public Response<SupplierTQRDCInfos> findSupplierTQRDCInfos() {
        return null;
    }

    @Override
    public Response<Boolean> setTQRDCRank() {
        return null;
    }

    @Override
    public Response<Boolean> isComplete(Long userId) {
        return null;
    }

    @Override
    public Response<List<ProductLine>> findAllProductLine() {
        return null;
    }

    @Override
    public Response<List<ProductLine>> findProductLineByIds(List<Long> ids) {
        return null;
    }

    @Override
    public Response<SupplierStepDto> getSupplierStepBySelf(BaseUser baseUser) {
        return null;
    }

    @Override
    public Response<SupplierStepDto> getSupplierStep(BaseUser baseUser, Long userId) {
        return null;
    }

    @Override
    public Response<Paging<SupplierDto>> findRefusedSuppliers(String nick, Integer pageNo, Integer size) {
        return null;
    }

    @Override
    public Response<Boolean> addSupplierCode(Long companyId, String supplierCode) {
        return null;
    }

    @Override
    public Response<Map<Long, ContactInfo>> findContactInfoByUserIds(List<Long> ids) {
        return null;
    }

    @Override
    public Response<Map<Long, List<CompanyMainBusiness>>> findMainBussinessByUserIds(List<Long> ids) {
        return null;
    }

    @Override
    public Response<List<CompanyMainBusiness>> findMainBussinessByUserId(BaseUser user) {
        return null;
    }

    @Override
    public Response<List<CompanyMainBusiness>> findMainBusinessByCompanyId(Long companyId) {
        return null;
    }

    public Response<Company> findCompanyBySupplierCode(String supplierCode) {
        return null;
    }

    @Override
    public Response<List<Company>> findCompaniesByFuzzySupplierName(String supplierName) {
        return null;
    }

    @Override
    public Response<Paging<SupplierDto>> findEnterPassSuppliers(BaseUser baseUser, String nick, Integer pageNo, Integer size) {
        return null;
    }

    @Override
    public Response<Paging<SupplierDto>> pagingCompanyHasSupplierCode(BaseUser user, String companyName, Integer pageNo, Integer size) {
        return null;
    }

    @Override
    public Response<Boolean> confirmResource(BaseUser baseUser, Long userId, Integer resourceType, String competitors) {
        return null;
    }

    @Override
    public Response<Map<Long, String>> companyHasVcode(Integer page, Integer size) {
        return null;
    }


    private Company mock() {
        Company company = new Company();
        company.setId(1l);
        company.setUserId(1l);
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

        return company;
    }
}
