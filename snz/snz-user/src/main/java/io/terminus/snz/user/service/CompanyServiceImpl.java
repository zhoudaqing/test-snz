package io.terminus.snz.user.service;

import com.google.common.base.*;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.terminus.common.model.PageInfo;
import io.terminus.common.model.Paging;
import io.terminus.common.utils.BeanMapper;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.dao.*;
import io.terminus.snz.user.dao.redis.*;
import io.terminus.snz.user.dto.*;
import io.terminus.snz.user.manager.AccountManager;
import io.terminus.snz.user.model.*;
import io.terminus.snz.user.tool.*;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Throwables.getStackTraceAsString;
import static io.terminus.common.utils.Arguments.isNull;
import static io.terminus.common.utils.Arguments.notNull;
import static io.terminus.snz.user.model.User.JobRole;

/**
 * Author:Guo Chaopeng
 * Created on 14-5-4.
 */
@Slf4j
@Service
public class CompanyServiceImpl implements CompanyService {

    /**
     * 空白供应商信息页
     */
    private static final Paging<SupplierDto> EMPTY_SUPPLIER_DTO_PAGE = new Paging<SupplierDto>(0L, Collections.<SupplierDto>emptyList());

    /**
     * 允许获取MDM对接供应商列表的用户角色列表
     */
    private static final List<String> ALLOWED = Lists.newArrayList(JobRole.RESOURCE.role(), JobRole.SHARING.role());

    private static final DateTimeFormatter MONTH_FORMAT = DateTimeFormat.forPattern("yyyy-MM");

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private CompanyRankDao companyRankDao;

    @Autowired
    private ContactInfoDao contactInfoDao;

    @Autowired
    private FinanceDao financeDao;

    @Autowired
    private AdditionalDocDao additionalDocDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CompanyMainBusinessDao companyMainBusinessDao;

    @Autowired
    private AccountManager accountManager;

    @Autowired
    private SupplierTQRDCInfoDao supplierTQRDCInfoDao;

    @Autowired
    private CompanyExtraRDDao companyExtraRDDao;

    @Autowired
    private CompanyExtraQualityDao companyExtraQualityDao;

    @Autowired
    private CompanyExtraDeliveryDao companyExtraDeliveryDao;

    @Autowired
    private ProductLineDao productLineDao;

    @Autowired
    private CompanySupplyParkDao companySupplyParkDao;

    @Autowired
    private Validator validator;

    @Autowired
    private SupplierCreditQualifyService supplierCreditQualifyService;

    @Autowired
    private MainBusinessApproverDao mainBusinessApproverDao;

    @Autowired
    private SupplierInfoChangedService supplierInfoChangedService;

    @Autowired
    private SupplierApproveLogDao supplierApproveLogDao;

    @Autowired
    private CompanyMainBusinessTmpDao companyMainBusinessTmpDao;

    @Autowired
    private CompanyRedisDao companyRedisDao;

    @Autowired
    private PaperworkRedisDao paperworkRedisDao;

    @Autowired
    private ContactInfoRedisDao contactInfoRedisDao;

    @Autowired
    private CompanyRankRedisDao companyRankRedisDao;

    @Autowired
    private FinanceRedisDao financeRedisDao;

    @Autowired
    private SupplierResourceMaterialService supplierResourceMaterialService;

    @Autowired
    SupplierModuleCountDao supplierModuleCountDao;

    @Autowired
    SupplierModuleDetailDao supplierModuleDetailDao;                //被淘汰的供应商物料明细

    private LoadingCache<Long, Optional<Company>> companyCache;

    /**
     * 当通过userId查询company时对company进行缓存
     */
    public CompanyServiceImpl() {
        companyCache = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).build(new CacheLoader<Long, Optional<Company>>() {
            @Override
            public Optional<Company> load(Long userId) throws Exception {
                return Optional.fromNullable(companyDao.findByUserId(userId));
            }
        });
    }

    @Override
    public Response<Boolean> updateCompany(BaseUser baseUser, CompanyDto companyDto) {
        Response<Boolean> result = new Response<Boolean>();

        Long userId = baseUser.getId();

        if (userId == null) {
            log.error("user id can not be null");
            result.setError("user.id.not.null.fail");
            return result;
        }

        Response<Boolean> companyRes = checkCompany(companyDto.getCompany());
        if (!companyRes.isSuccess()) {
            result.setError(companyRes.getError());
            return result;
        }

        Response<Boolean> mainBusinessRes = checkCompanyMainBusiness(companyDto.getCompanyMainBusinesses());
        if (!mainBusinessRes.isSuccess()) {
            result.setError(mainBusinessRes.getError());
            return result;
        }

        try {

            Company existedCompany = companyCache.getUnchecked(userId).get();

            if (existedCompany == null) {
                log.error("company not found where user id={}", userId);
                result.setError("company.not.found");
                return result;
            }

            companyDto.getCompany().setUserId(userId);
            companyDto.getCompany().setId(existedCompany.getId());

            accountManager.updateCompany(companyDto, existedCompany);
            companyCache.invalidate(userId);

            result.setResult(Boolean.TRUE);
            return result;

        } catch (Exception e) {
            log.error("fail to update company where user id={},cause:{}", userId, getStackTraceAsString(e));
            result.setError("update.company.fail");
            return result;
        }

    }

    /**
     * 检查企业基本信息
     *
     * @param company 企业基本信息
     * @return 是否合法
     */
    private Response<Boolean> checkCompany(Company company) {
        Response<Boolean> result = new Response<Boolean>();

        if (company == null) {
            log.error("company can not be null");
            result.setError("company.not.null");
            return result;
        }

        if (Strings.isNullOrEmpty(company.getCorporation())) {
            log.error("corporation can not be null");
            result.setError("corporation.not.null");
            return result;
        }

        if (Strings.isNullOrEmpty(company.getInitAgent())) {
            log.error("initAgent can not be null");
            result.setError("initAgent.not.null");
            return result;
        }

        if (Strings.isNullOrEmpty(company.getDesc())) {
            log.error("company desc can not be null");
            result.setError("company.desc.not.null");
            return result;
        }

        if (company.getRegCountry() == null) {
            log.error("regCountry can not be null");
            result.setError("regCountry.not.null");
            return result;
        }

        if (Strings.isNullOrEmpty(company.getCustomers())) {
            log.error("customers can not be null");
            result.setError("customers.not.null");
            return result;
        }

        result.setResult(Boolean.TRUE);
        return result;

    }

    /**
     * 检查主营业务信息
     *
     * @param companyMainBusinesses 主营业务
     * @return 是否合法
     */
    private Response<Boolean> checkCompanyMainBusiness(List<CompanyMainBusiness> companyMainBusinesses) {
        Response<Boolean> result = new Response<Boolean>();

        if (companyMainBusinesses == null || companyMainBusinesses.isEmpty()) {
            log.error("company main business can not be null");
            result.setError("company.main.business.not.null");
            return result;
        }

        for (CompanyMainBusiness companyMainBusiness : companyMainBusinesses) {

            if (companyMainBusiness == null) {
                log.error("company main business can not be null");
                result.setError("company.main.business.not.null");
                return result;
            }

            if (companyMainBusiness.getMainBusinessId() == null) {
                log.error("company main business id can not be null");
                result.setError("company.main.business.id.not.null");
                return result;
            }

            if (Strings.isNullOrEmpty(companyMainBusiness.getName())) {
                log.error("company main business name can not be null");
                result.setError("company.main.business.name.not.null");
                return result;
            }

        }
        result.setResult(Boolean.TRUE);
        return result;
    }

    @Override
    public Response<Boolean> updatePaperwork(BaseUser baseUser, PaperworkDto paperworkDto) {
        Response<Boolean> result = new Response<Boolean>();

        if (paperworkDto == null) {
            log.error("paperwork can not be null");
            result.setError("paperwork.not.null");
            return result;
        }

        try {

            Optional<Company> companyOptional = companyCache.getUnchecked(baseUser.getId());

            if (!companyOptional.isPresent()) {
                log.error("company not found where user id={}", baseUser.getId());
                result.setError("company.not.found");
                return result;
            }

            Company existed = companyOptional.get();

            if (!Objects.equal(baseUser.getId(), existed.getUserId())) {
                log.error("user don't have the right");
                result.setError("authority.fail");
                return result;
            }

            //根据国家编号检查所需证件是否完整
            Response<Boolean> certificateRes = checkPaperworkByCountryId(existed.getRegCountry(), paperworkDto);
            if (!Objects.equal(certificateRes.getResult(), Boolean.TRUE)) {
                result.setError(certificateRes.getError());
                return result;
            }

            accountManager.updatePaperwork(baseUser.getId(), paperworkDto, existed);
            companyCache.invalidate(baseUser.getId());
            result.setResult(Boolean.TRUE);
            return result;

        } catch (Exception e) {
            log.error("fail to update paperwork where user id={}", baseUser.getId());
            result.setError("update.paperwork.fail");
            return result;
        }

    }

    @Override
    public Response<PaperworkDto> findPaperworkByUser(BaseUser baseUser) {
        Response<PaperworkDto> result = new Response<PaperworkDto>();

        Long userId = baseUser.getId();
        if (userId == null) {
            log.error("company userId can not be null");
            result.setError("company.userId.not.null.fail");
            return result;
        }

        try {

            //尝试从暂存获取信息
            PaperworkDto stash = paperworkRedisDao.findByKey(StashSupplierInfoKeys.paperwork(userId));
            if (stash != null) {
                result.setResult(stash);
                return result;
            }

            Optional<Company> companyOptional = companyCache.getUnchecked(userId);
            if (!companyOptional.isPresent()) {
                log.error("company not found where user id={}", userId);
                result.setError("company.not.found");
                return result;
            }

            SupplierUpdatedInfoDto<PaperworkDto> supplierUpdatedInfoDto = supplierInfoChangedService.getNewPaperwork(userId, companyOptional.get());
            result.setResult(supplierUpdatedInfoDto.getSupplierInfo());
            return result;

        } catch (Exception e) {
            log.error("fail to query company where userId={},cause:{}", userId, getStackTraceAsString(e));
            result.setError("query.company.fail");
            return result;
        }
    }

    @Override
    public Response<SupplierUpdatedInfoDto<PaperworkDto>> queryPaperworkByUserId(Long userId) {
        Response<SupplierUpdatedInfoDto<PaperworkDto>> result = new Response<SupplierUpdatedInfoDto<PaperworkDto>>();

        if (userId == null) {
            log.error("company userId can not be null");
            result.setError("company.userId.not.null.fail");
            return result;
        }

        try {
            Optional<Company> companyOptional = companyCache.getUnchecked(userId);
            if (!companyOptional.isPresent()) {
                log.error("company not found where user id={}", userId);
                result.setError("company.not.found");
                return result;
            }

            SupplierUpdatedInfoDto<PaperworkDto> supplierUpdatedInfoDto = supplierInfoChangedService.getNewPaperwork(userId, companyOptional.get());
            result.setResult(supplierUpdatedInfoDto);
            return result;

        } catch (Exception e) {
            log.error("fail to query company where userId={},cause:{}", userId, getStackTraceAsString(e));
            result.setError("query.company.fail");
            return result;
        }
    }

    /**
     * 根据国家id检查三证信息
     *
     * @param countryId    国家id
     * @param paperworkDto 企业信息
     * @return 是否合法
     */
    private Response<Boolean> checkPaperworkByCountryId(Integer countryId, PaperworkDto paperworkDto) {
        Response<Boolean> result = new Response<Boolean>();

        if (paperworkDto == null) {
            log.error("paperwork can not be null");
            result.setError("paperwork.not.null");
            return result;
        }

        int type = CountryCertificate.check(countryId);

        switch (type) {
            case CountryCertificate.NO_CERTIFICATE:
                result.setResult(Boolean.TRUE);
                return result;
            case CountryCertificate.ONLY_NEED_BUSINESS_LICENSE:
                return checkBusinessLicense(paperworkDto);
            case CountryCertificate.ONLY_NEED_BUSINESS_LICENSE_AND_TAX_NO:
                return checkBusinessLicenseAndTaxNo(paperworkDto);
            case CountryCertificate.NEED_THREE_CERTIFICATES:
                return checkThreePaperwork(paperworkDto);
        }

        log.error("not match a type where country id={}", countryId);
        result.setResult(Boolean.FALSE);
        return result;
    }

    /**
     * 检查营业执照
     *
     * @param paperworkDto 三证信息
     * @return 是否合法
     */
    private Response<Boolean> checkBusinessLicense(PaperworkDto paperworkDto) {
        Response<Boolean> result = new Response<Boolean>();

        if (paperworkDto == null) {
            log.error("paperwork can not be null");
            result.setError("paperwork.not.null");
            return result;
        }

        if (Strings.isNullOrEmpty(paperworkDto.getBusinessLicense())) {
            log.error("businessLicense can not be null");
            result.setError("business.license.not.null");
            return result;
        }

        if (Strings.isNullOrEmpty(paperworkDto.getBusinessLicenseId())) {
            log.error("businessLicense id can not be null");
            result.setError("business.license.id.not.null");
            return result;
        }

        if (paperworkDto.getBlDate() == null) {
            log.error("business license date can not be null");
            result.setError("business.license.date.not.null");
            return result;
        }

        result.setResult(Boolean.TRUE);
        return result;
    }

    /**
     * 检查营业执照和税务登记证号
     *
     * @param paperworkDto 三证信息
     * @return 是否合法
     */
    private Response<Boolean> checkBusinessLicenseAndTaxNo(PaperworkDto paperworkDto) {
        Response<Boolean> result = new Response<Boolean>();

        if (paperworkDto == null) {
            log.error("paperwork can not be null");
            result.setError("paperwork.not.null");
            return result;
        }

        Response<Boolean> blRes = checkBusinessLicense(paperworkDto);
        if (!blRes.isSuccess()) {
            return blRes;
        }

        if (Strings.isNullOrEmpty(paperworkDto.getTaxNo())) {
            log.error("taxNo can not be null");
            result.setError("taxNo.not.null");
            return result;
        }

        if (Strings.isNullOrEmpty(paperworkDto.getTaxNoId())) {
            log.error("taxNo id can not be null");
            result.setError("taxNo.id.not.null");
            return result;
        }

        result.setResult(Boolean.TRUE);
        return result;
    }

    /**
     * 检查三证
     *
     * @param paperworkDto 三证信息
     * @return 是否合法
     */
    private Response<Boolean> checkThreePaperwork(PaperworkDto paperworkDto) {

        Response<Boolean> result = new Response<Boolean>();


        if (paperworkDto == null) {
            log.error("paperwork can not be null");
            result.setError("paperwork.not.null");
            return result;
        }

        Response<Boolean> blAndTnRes = checkBusinessLicenseAndTaxNo(paperworkDto);
        if (!blAndTnRes.isSuccess()) {
            return blAndTnRes;
        }

        if (Strings.isNullOrEmpty(paperworkDto.getOrgCert())) {
            log.error("orgCert can not be null");
            result.setError("orgCert.not.null");
            return result;
        }

        if (Strings.isNullOrEmpty(paperworkDto.getOrgCertId())) {
            log.error("orgCert id can not be null");
            result.setError("orgCert.id.not.null");
            return result;
        }

        if (paperworkDto.getOcDate() == null) {
            log.error("orgCert date can not be null");
            result.setError("orgCert.date.not.null");
            return result;
        }

        result.setResult(Boolean.TRUE);
        return result;

    }

    @Override
    public Response<CompanyDto> findBaseCompanyByUserId(BaseUser baseUser) {
        Response<CompanyDto> result = new Response<CompanyDto>();

        if (baseUser.getId() == null) {
            log.error("user id can not be null");
            result.setError("user.id.not.null.fail");
            return result;
        }

        try {

            //尝试从暂存获取信息
            CompanyDto stash = companyRedisDao.getBaseCompanyInfo(baseUser.getId());
            if (stash != null) {
                result.setResult(stash);
                return result;
            }

            Optional<Company> companyOptional = companyCache.getUnchecked(baseUser.getId());
            if (!companyOptional.isPresent()) {
                log.error("company not found where userId={}", baseUser.getId());
                result.setError("company.not.found");
                return result;
            }

            Company company = companyOptional.get();
            List<CompanyMainBusiness> companyMainBusinesses = companyMainBusinessDao.findByCompanyId(company.getId());
            List<CompanySupplyPark> companySupplyParks = companySupplyParkDao.findByCompanyId(company.getId());

            if (companyMainBusinesses == null) {
                companyMainBusinesses = Collections.emptyList();
            }

            if (companySupplyParks == null) {
                companySupplyParks = Collections.emptyList();
            }

            CompanyDto companyDto = supplierInfoChangedService.getNewBaseCompany(baseUser.getId(), company, companyMainBusinesses, companySupplyParks).getSupplierInfo();

            result.setResult(companyDto);
            return result;

        } catch (Exception e) {
            log.error("fail to query company where userId={},cause:{}", baseUser.getId(), getStackTraceAsString(e));
            result.setError("query.company.fail");
            return result;
        }

    }

    @Override
    public Response<SupplierUpdatedInfoDto<CompanyDto>> queryBaseCompanyByUserId(Long userId) {

        Response<SupplierUpdatedInfoDto<CompanyDto>> result = new Response<SupplierUpdatedInfoDto<CompanyDto>>();

        if (userId == null) {
            log.error("user id can not be null");
            result.setError("user.id.not.null.fail");
            return result;
        }

        try {
            Optional<Company> companyOptional = companyCache.getUnchecked(userId);
            if (!companyOptional.isPresent()) {
                log.error("company not found where userId={}", userId);
                result.setError("company.not.found");
                return result;
            }

            Company company = companyOptional.get();
            List<CompanyMainBusiness> companyMainBusinesses = companyMainBusinessDao.findByCompanyId(company.getId());
            List<CompanySupplyPark> companySupplyParks = companySupplyParkDao.findByCompanyId(company.getId());

            if (companyMainBusinesses == null) {
                companyMainBusinesses = Collections.emptyList();
            }

            if (companySupplyParks == null) {
                companySupplyParks = Collections.emptyList();
            }

            SupplierUpdatedInfoDto<CompanyDto> supplierUpdatedInfoDto = supplierInfoChangedService.getNewBaseCompany(userId, company, companyMainBusinesses, companySupplyParks);

            result.setResult(supplierUpdatedInfoDto);
            return result;

        } catch (Exception e) {
            log.error("fail to query company where userId={},cause:{}", userId, getStackTraceAsString(e));
            result.setError("query.company.fail");
            return result;
        }
    }

    @Override
    public Response<Company> findCompanyById(Long id) {
        Response<Company> result = new Response<Company>();

        if (id == null) {
            log.error("company id can not be null");
            result.setError("company.id.not.null.fail");
            return result;
        }

        try {

            Company company = companyDao.findById(id);

            if (company == null) {
                log.error("company not found where id={}", id);
                result.setError("company.not.found");
                return result;
            }

            result.setResult(company);
            return result;

        } catch (Exception e) {
            log.error("fail to query company where id={},cause:{}", id, getStackTraceAsString(e));
            result.setError("query.company.fail");
            return result;
        }
    }

    @Override
    public Response<Company> findCompanyByUserId(Long userId) {
        Response<Company> result = new Response<Company>();

        if (userId == null) {
            log.error("company userId can not be null");
            result.setError("company.userId.not.null.fail");
            return result;
        }

        try {
            Optional<Company> companyOptional = companyCache.getUnchecked(userId);
            if (!companyOptional.isPresent()) {
                log.error("company not found where user id={}", userId);
                result.setError("company.not.found");
                return result;
            }

            result.setResult(companyOptional.get());
            return result;

        } catch (Exception e) {
            log.error("fail to query company where userId={},cause:{}", userId, getStackTraceAsString(e));
            result.setError("query.company.fail");
            return result;
        }
    }

    @Override
    public Response<Company> findUnCacheCompanyByUserId(Long userId) {
        Response<Company> result = new Response<Company>();

        if (userId == null) {
            log.error("company userId can not be null");
            result.setError("company.userId.not.null.fail");
            return result;
        }

        try {
            Company company = companyDao.findByUserId(userId);
            if (company == null) {
                log.error("company not found where user id={}", userId);
                result.setError("company.not.found");
                return result;
            }

            result.setResult(company);
            return result;

        } catch (Exception e) {
            log.error("fail to query company where userId={},cause:{}", userId, getStackTraceAsString(e));
            result.setError("query.company.fail");
            return result;
        }
    }

    @Override
    public Response<List<CompanyMainBusiness>> findCompanyMainBusiness(Long mainBusinessId) {
        Response<List<CompanyMainBusiness>> result = new Response<List<CompanyMainBusiness>>();

        if (mainBusinessId == null) {
            log.error("company main business id can not be null");
            result.setError("company.main.business.id.not.null");
            return result;
        }

        try {

            List<CompanyMainBusiness> companyMainBusinesses = companyMainBusinessDao.findByMainBusinessId(mainBusinessId);
            result.setResult(companyMainBusinesses);
            return result;

        } catch (Exception e) {
            log.error("fail to query company main business where mainBusinessId={},cause:{}", mainBusinessId);
            result.setError("query.company.main.business.fail");
            return result;
        }

    }

    @Override
    public Response<List<Long>> findCompanyIdsByMainBusinessIds(List<Long> mainBusinessIds) {
        Response<List<Long>> resp = new Response<List<Long>>();

        try {
            if (Iterables.isEmpty(mainBusinessIds)) {
                resp.setResult(Collections.<Long>emptyList());
                return resp;
            }
            List<CompanyMainBusiness> cmbs =
                    companyMainBusinessDao.findCompanyIdsByMainBusinessIds(mainBusinessIds);
            if (Iterables.isEmpty(cmbs)) {
                resp.setResult(Collections.<Long>emptyList());
                return resp;
            }
            List<Long> cids = Lists.transform(cmbs, new Function<CompanyMainBusiness, Long>() {
                @Override
                public Long apply(CompanyMainBusiness cmb) {
                    return cmb.getUserId();
                }
            });
            resp.setResult(cids);
        } catch (Exception e) {
            log.error("failed to find company ids(main business ids={})", mainBusinessIds);
        }
        return resp;
    }

    @Override
    public Response<DetailSupplierDto> findDetailSupplierByUserId(Long userId) {

        Response<DetailSupplierDto> result = new Response<DetailSupplierDto>();

        if (userId == null) {
            log.error("user id can not be null");
            result.setError("user.id.not.null.fail");
            return result;
        }

        try {

            User user = userDao.findById(userId);
            if (user == null) {
                log.error("user not found where id={}", userId);
                result.setError("user.not.found");
                return result;
            }

            Company company = companyDao.findByUserId(userId);
            if (company == null) {
                log.error("company not found where user id={}", userId);
                result.setError("company.not.found");
                return result;
            }

            List<CompanyMainBusiness> companyMainBusinesses = companyMainBusinessDao.findByUserId(userId);
            if (companyMainBusinesses == null) {
                log.error("company main business not found where userId={}", userId);
                result.setError("company.main.business.not.found");
                return result;
            }

            List<CompanySupplyPark> companySupplyParks = companySupplyParkDao.findByUserId(userId);
            if (companySupplyParks == null) {
                companySupplyParks = Collections.emptyList();
            }

            CompanyDto companyDto = supplierInfoChangedService.getNewBaseCompany(userId, company, companyMainBusinesses, companySupplyParks).getSupplierInfo();
            PaperworkDto paperworkDto = supplierInfoChangedService.getNewPaperwork(user.getId(), company).getSupplierInfo();
            BeanMapper.copy(paperworkDto, companyDto.getCompany());

            DetailSupplierDto detailSupplierDto = new DetailSupplierDto();
            detailSupplierDto.setUser(user);
            detailSupplierDto.setCompany(companyDto.getCompany());
            detailSupplierDto.setCompanyMainBusinesses(companyDto.getCompanyMainBusinesses());
            detailSupplierDto.setCompanySupplyParks(companyDto.getCompanySupplyParks());

            if (Objects.equal(user.getApproveStatus(), User.ApproveStatus.ENTER_WAITING_FOR_APPROVE.value())) {//入驻审核
                detailSupplierDto.setApprovedType(SupplierDto.ApprovedType.ENTER.value());
            }

            if (Objects.equal(user.getApproveStatus(), User.ApproveStatus.MODIFY_INFO_WAITING_FOR_APPROVE.value())) {//修改审核
                detailSupplierDto.setApprovedType(SupplierDto.ApprovedType.MODIFY.value());
            }

            result.setResult(detailSupplierDto);
            return result;

        } catch (Exception e) {
            log.error("fail to query supplier where user id={}", userId);
            result.setError("query.supplier.fail");
            return result;
        }

    }

    @Override
    public Response<List<Company>> findCompaniesByIds(List<Long> ids) {
        Response<List<Company>> result = new Response<List<Company>>();

        if (ids == null) {
            log.error("ids can not be null");
            result.setError("company.id.not.null.fail");
            return result;
        }

        try {

            List<Company> companies = companyDao.findByIds(ids);

            if (companies == null) {
                log.error("company not found where ids:{}", ids);
                result.setError("company.not.found");
                return result;
            }

            result.setResult(companies);
            return result;

        } catch (Exception e) {
            log.error("fail to query company where ids:{},cause:{}", ids, getStackTraceAsString(e));
            result.setError("query.company.fail");
            return result;
        }
    }

    @Override
    public Response<Boolean> updateParticipateCount(Long companyId) {
        Response<Boolean> result = new Response<Boolean>();

        if (companyId == null) {
            log.error("company id can not be null");
            result.setError("company.id.not.null.fail");
            return result;
        }

        try {
            companyDao.updateParticipateCount(companyId);
            result.setResult(Boolean.TRUE);
            return result;
        } catch (Exception e) {
            log.error("fail to update participate count where company id={},cause:{}", companyId, getStackTraceAsString(e));
            result.setError("update.participate.count.fail");
            return result;
        }
    }

    @Override
    public Response<Boolean> createOrUpdateCompanyRank(BaseUser baseUser, CompanyRank companyRank) {
        Response<Boolean> result = new Response<Boolean>();

        if (companyRank == null) {
            log.error("companyRank can not be null");
            result.setError("company.rank.not.null");
            return result;
        }

        if (baseUser.getId() == null) {
            log.error("user id can not be null");
            result.setError("user.id.not.null.fail");
            return result;
        }

        try {
            accountManager.createOrUpdateCompanyRank(baseUser.getId(), companyRank);
            result.setResult(Boolean.TRUE);
            return result;
        } catch (Exception e) {
            log.error("fail to create or update company rank where user id={},cause:{}", baseUser.getId(), getStackTraceAsString(e));
            result.setError("create.or.update.company.rank.fail");
            return result;
        }
    }


    @Override
    public Response<CompanyRank> findCompanyRankByUserId(BaseUser baseUser) {
        Response<CompanyRank> result = new Response<CompanyRank>();

        if (baseUser.getId() == null) {
            log.error("user id can not be null");
            result.setError("user.id.not.null.fail");
            return result;
        }

        try {

            //尝试从暂存获取信息
            CompanyRank stash = companyRankRedisDao.findByKey(StashSupplierInfoKeys.companyRank(baseUser.getId()));
            if (stash != null) {
                result.setResult(stash);
                return result;
            }

            CompanyRank companyRank = companyRankDao.findByUserId(baseUser.getId());
            if (companyRank == null) {
                companyRank = new CompanyRank();
            }

            result.setResult(companyRank);
            return result;

        } catch (Exception e) {
            log.error("fail to query company rank where userId={},cause:{}", baseUser.getId(), getStackTraceAsString(e));
            result.setError("query.company.rank.fail");
            return result;
        }

    }

    @Override
    public Response<SupplierUpdatedInfoDto<CompanyRank>> queryCompanyRankByUserId(Long userId) {

        Response<SupplierUpdatedInfoDto<CompanyRank>> result = new Response<SupplierUpdatedInfoDto<CompanyRank>>();

        if (userId == null) {
            log.error("user id can not be null");
            result.setError("user.id.not.null.fail");
            return result;
        }

        try {

            CompanyRank companyRank = companyRankDao.findByUserId(userId);
            if (companyRank == null) {
                companyRank = new CompanyRank();
            }

            SupplierUpdatedInfoDto<CompanyRank> supplierUpdatedInfoDto = new SupplierUpdatedInfoDto<CompanyRank>();
            supplierUpdatedInfoDto.setSupplierInfo(companyRank);

            Map<String, Object> oldValues = Maps.newHashMap();
            supplierUpdatedInfoDto.setOldValues(oldValues);

            result.setResult(supplierUpdatedInfoDto);
            return result;

        } catch (Exception e) {
            log.error("fail to query company rank where userId={},cause:{}", userId, getStackTraceAsString(e));
            result.setError("query.company.rank.fail");
            return result;
        }

    }

    @Override
    public Response<Boolean> createOrUpdateContactInfo(BaseUser baseUser, ContactInfo contactInfo) {
        Response<Boolean> result = new Response<Boolean>();

        Long userId = baseUser.getId();
        if (contactInfo == null) {
            log.error("contactInfo can not be null");
            result.setError("contactInfo.not.null");
            return result;
        }

        if (userId == null) {
            log.error("user id can not be null");
            result.setError("user.id.not.null.fail");
            return result;
        }

        try {

            ContactInfo existed = contactInfoDao.findByUserId(userId);

            if (existed == null) {
                log.error("contactInfo not found where user id={}", userId);
                result.setError("contactInfo.not.found");
                return result;
            }

            accountManager.updateContactInfo(userId, contactInfo, existed);

            result.setResult(Boolean.TRUE);
            return result;
        } catch (Exception e) {
            log.error("fail to update contactInfo where user id={},cause:{}", userId, getStackTraceAsString(e));
            result.setError("update.contactInfo.fail");
            return result;
        }
    }

    @Override
    public Response<ContactInfo> findContactInfoByUserId(BaseUser baseUser) {
        Response<ContactInfo> result = new Response<ContactInfo>();

        if (baseUser.getId() == null) {
            log.error("user id can not be null");
            result.setError("user.id.not.null.fail");
            return result;
        }

        Long userId = baseUser.getId();

        try {

            //尝试从暂存获取信息
            ContactInfo stash = contactInfoRedisDao.findByKey(StashSupplierInfoKeys.contactInfo(userId));
            if (stash != null) {
                result.setResult(stash);
                return result;
            }

            ContactInfo contactInfo = contactInfoDao.findByUserId(userId);
            if (contactInfo == null) {
                log.error("contactInfo not found where user id={}", userId);
                result.setError("contactInfo.not.found");
                return result;
            }

            result.setResult(supplierInfoChangedService.getNewContactInfo(userId, contactInfo).getSupplierInfo());
            return result;

        } catch (Exception e) {
            log.error("fail to query contactInfo where userId={},cause:{}", baseUser.getId(), getStackTraceAsString(e));
            result.setError("query.contactInfo.fail");
            return result;
        }
    }

    @Override
    public Response<SupplierUpdatedInfoDto<ContactInfo>> queryContactInfoByUserId(Long userId) {
        Response<SupplierUpdatedInfoDto<ContactInfo>> result = new Response<SupplierUpdatedInfoDto<ContactInfo>>();

        if (userId == null) {
            log.error("user id can not be null");
            result.setError("user.id.not.null.fail");
            return result;
        }

        try {

            ContactInfo contactInfo = contactInfoDao.findByUserId(userId);
            if (contactInfo == null) {
                log.error("contactInfo not found where user id={}", userId);
                result.setError("contactInfo.not.found");
                return result;
            }

            SupplierUpdatedInfoDto<ContactInfo> supplierUpdatedInfoDto = supplierInfoChangedService.getNewContactInfo(userId, contactInfo);

            result.setResult(supplierUpdatedInfoDto);
            return result;

        } catch (Exception e) {
            log.error("fail to query contactInfo where userId={},cause:{}", userId, getStackTraceAsString(e));
            result.setError("query.contactInfo.fail");
            return result;
        }
    }

    @Override
    public Response<Boolean> createOrUpdateFinance(BaseUser baseUser, FinanceDto financeDto) {
        Response<Boolean> result = new Response<Boolean>();

        if (baseUser.getId() == null) {
            log.error("user id can not be null");
            result.setError("user.id.not.null.fail");
            return result;
        }

        Finance finance = financeDto.getFinance();

        if (Strings.isNullOrEmpty(finance.getOpenLicense())) {
            log.error("open license can not be null");
            result.setError("finance.open.license.not.null");
            return result;
        }

        if (Strings.isNullOrEmpty(finance.getRecentFinance())) {
            log.error("finance recent sold and net can not be null");
            result.setError("finance.recent.sold.and.net.not.null");
            return result;
        }

        try {
            financeDto.getFinance().setUserId(baseUser.getId());
            accountManager.createOrUpdateFinance(financeDto);
            result.setResult(Boolean.TRUE);
            return result;
        } catch (Exception e) {
            log.error("fail to create or update finance where userId={},cause:{}", baseUser.getId(), getStackTraceAsString(e));
            result.setError("create.or.update.finance.fail");
            return result;
        }
    }

    @Override
    public Response<FinanceDto> findFinanceByUserId(BaseUser baseUser) {
        Response<FinanceDto> result = new Response<FinanceDto>();

        if (baseUser.getId() == null) {
            log.error("user id can not be null");
            result.setError("user.id.not.null.fail");
            return result;
        }

        try {

            FinanceDto financeDto = new FinanceDto();

            //尝试从暂存获取信息
            Finance stash = financeRedisDao.findByKey(StashSupplierInfoKeys.finance(baseUser.getId()));
            if (stash != null) {
                financeDto.setFinance(stash);
                result.setResult(financeDto);
                return result;
            }

            Finance finance = financeDao.findByUserId(baseUser.getId());

            if (finance == null) {
                result.setResult(financeDto);
                return result;
            }

            financeDto.setFinance(supplierInfoChangedService.getNewFinance(baseUser.getId(), finance).getSupplierInfo());

            result.setResult(financeDto);
            return result;

        } catch (Exception e) {
            log.error("fail to query finance where userId={},cause:{}", baseUser.getId(), getStackTraceAsString(e));
            result.setError("query.finance.fail");
            return result;
        }
    }

    @Override
    public Response<SupplierUpdatedInfoDto<FinanceDto>> queryFinanceByUserId(Long userId) {

        Response<SupplierUpdatedInfoDto<FinanceDto>> result = new Response<SupplierUpdatedInfoDto<FinanceDto>>();

        if (userId == null) {
            log.error("user id can not be null");
            result.setError("user.id.not.null.fail");
            return result;
        }

        try {

            SupplierUpdatedInfoDto<FinanceDto> supplierUpdatedInfoDto = new SupplierUpdatedInfoDto<FinanceDto>();
            Finance finance = financeDao.findByUserId(userId);

            if (finance == null) {
                Map<String, Object> oldValues = Maps.newHashMap();
                supplierUpdatedInfoDto.setSupplierInfo(new FinanceDto());
                supplierUpdatedInfoDto.setOldValues(oldValues);
                result.setResult(supplierUpdatedInfoDto);
                return result;
            }

            SupplierUpdatedInfoDto<Finance> financeUpdatedDto = supplierInfoChangedService.getNewFinance(userId, finance);

            FinanceDto financeDto = new FinanceDto();
            financeDto.setFinance(financeUpdatedDto.getSupplierInfo());

            supplierUpdatedInfoDto.setSupplierInfo(financeDto);
            supplierUpdatedInfoDto.setOldValues(financeUpdatedDto.getOldValues());

            result.setResult(supplierUpdatedInfoDto);
            return result;

        } catch (Exception e) {
            log.error("fail to query finance where userId={},cause:{}", userId, getStackTraceAsString(e));
            result.setError("query.finance.fail");
            return result;
        }
    }

    @Override
    public Response<Boolean> deleteAdditionalDoc(Long additionalDocId, BaseUser baseUser) {
        Response<Boolean> result = new Response<Boolean>();

        if (additionalDocId == null) {
            log.error("additionalDoc id can not be null");
            result.setError("additionalDoc.id.not.null");
            return result;
        }

        if (baseUser.getId() == null) {
            log.error("user id can not be null");
            result.setError("user.id.not.null.fail");
            return result;
        }

        try {
            AdditionalDoc additionalDoc = additionalDocDao.findById(additionalDocId);

            if (additionalDoc == null) {
                log.error("additionalDoc not found where additionalDocId={}", additionalDocId);
                result.setError("additionalDoc.not.found");
                return result;
            }

            if (!Objects.equal(baseUser.getId(), additionalDoc.getUserId())) {
                log.error("user don't have the right");
                result.setError("authority.fail");
                return result;
            }

            boolean isSuccess = additionalDocDao.delete(additionalDocId);

            if (!isSuccess) {
                log.error("fail to delete additionalDoc where additionalDocId={}", additionalDocId);
                result.setError("delete.additionalDoc.fail");
                return result;
            }

            result.setResult(Boolean.TRUE);
            return result;

        } catch (Exception e) {
            log.error("fail to delete additionalDoc where additionalDocId={},cause:{}", additionalDocId, getStackTraceAsString(e));
            result.setError("delete.additionalDoc.fail");
            return result;
        }
    }

    @Override
    public Response<Paging<SupplierDto>> findSupplierForMDMQualify(BaseUser user, String supplierName,
                                                                   Integer pageNo, Integer size) {
        PageInfo page = new PageInfo(pageNo, size);
        Response<Paging<SupplierDto>> result = new Response<Paging<SupplierDto>>();

        try {
            checkArgument(notNull(user) && notNull(user.getId()), "supplier.find.not.login");

            List<String> roles = user.getRoles();
            if (Collections.disjoint(ALLOWED, roles)) {
                result.setResult(EMPTY_SUPPLIER_DTO_PAGE);
                return result;
            }
            Integer stage = roles.contains(JobRole.SHARING.role()) ? 2 : 1;

            Paging<Company> found = companyDao.findSuppliersForQualify(supplierName, stage, page.getOffset(), page.getLimit());
            if (found.getTotal() == 0l) {
                result.setResult(EMPTY_SUPPLIER_DTO_PAGE);
                return result;
            }

            // 根据供应商名字来查找
            List<SupplierDto> suppliers = Lists.newArrayList();
            for (Company com : found.getData()) {
                SupplierDto dto = new SupplierDto(com);
                // User should never be null!
                User sup = userDao.findById(com.getUserId());
                dto.setNick(sup.getNick());

                ContactInfo contact = contactInfoDao.findByUserId(com.getUserId());
                if (contact != null) {
                    dto.setOfficePhone(contact.getOfficePhone());
                }
                suppliers.add(dto);
            }
            Paging<SupplierDto> supplierDtoPaging =
                    new Paging<SupplierDto>(found.getTotal(), suppliers);
            result.setResult(supplierDtoPaging);

        } catch (IllegalArgumentException e) {
            log.error("`findSupplierForMDMQualify` invoke with illegal arguments, user:{}, supplierName:{}",
                    user, supplierName);
            result.setError(e.getMessage());
            return result;
        } catch (Exception e) {
            log.error("`findSupplierForMDMQualify` invoke fail. with user:{}. supplierName:{}, e:{}",
                    user, supplierName, getStackTraceAsString(e));
            result.setError("supplier.find.fail");
            return result;
        }

        return result;
    }

    @Override
    public Response<Paging<SupplierDto>> findSupplierBy(BaseUser baseUser, Integer type, String supplierName, Integer pageNo, Integer size) {

        Response<Paging<SupplierDto>> result = new Response<Paging<SupplierDto>>();

        try {

            List<Long> userIds;
            boolean isQueryApproverNames = false;//是否查询供应商的审核人

            if (Objects.equal(baseUser.getType(), User.Type.ADMIN.value())) {
                userIds = null;
                isQueryApproverNames = true;
            } else {

                String employId = baseUser.getNickName();

                if (Strings.isNullOrEmpty(employId)) {
                    log.error("employee id not found");
                    result.setError("authority.fail");
                    return result;
                }

                List<String> userRoles = baseUser.getRoles();
                if (userRoles.contains(JobRole.RESOURCE.role())) {//资源小薇

                    if (Objects.equal(type, 3)) {
                        if (!userRoles.contains(JobRole.MICRO_MASTER.role())) {
                            log.warn("user don't have the right");
                            result.setResult(EMPTY_SUPPLIER_DTO_PAGE);
                            return result;
                        }
                    }

                    //查询该登录用户审核的主营业务编号
                    List<Long> mainBusinessIds = mainBusinessApproverDao.findMainBusinessIdsByMemberIdOrLeaderId(employId);
                    if (mainBusinessIds == null || mainBusinessIds.isEmpty()) {
                        log.error("main business not found where employ id={}", employId);
                        result.setError("approved.main.business.not.found");
                        return result;
                    }

                    //查询属于该登录用户审核的所有供应商编号
                    userIds = companyMainBusinessDao.findUserIdsByMainBusinessIds(mainBusinessIds);
                    if (userIds == null || userIds.isEmpty()) {
                        log.warn("no supplier select main business(ids:{})", mainBusinessIds);
                        result.setResult(EMPTY_SUPPLIER_DTO_PAGE);
                        return result;
                    }

                    //去除修改了主营业务的供应商编号
                    List<CompanyMainBusinessTmp> companyMainBusinessTmps = companyMainBusinessTmpDao.findByUserIds(userIds);
                    if (companyMainBusinessTmps != null && !companyMainBusinessTmps.isEmpty()) {
                        List<Long> updatedMainBusinessUserIds = Lists.transform(companyMainBusinessTmps, new Function<CompanyMainBusinessTmp, Long>() {
                            public Long apply(CompanyMainBusinessTmp companyMainBusinessTmp) {
                                return companyMainBusinessTmp.getUserId();
                            }
                        });
                        userIds.removeAll(updatedMainBusinessUserIds);
                    }

                    //从修改了主营业务的供应商中查询出属于改登录用户审核的供应商编号
                    List<Long> tmpUserIds = companyMainBusinessTmpDao.findUserIdsByMainBusinessIds(mainBusinessIds);
                    if (tmpUserIds != null && !tmpUserIds.isEmpty()) {
                        userIds.addAll(tmpUserIds);
                    }

                } else {
                    log.warn("unknown role when query approval list");
                    result.setResult(EMPTY_SUPPLIER_DTO_PAGE);
                    return result;
                }
            }

            if (!Strings.isNullOrEmpty(supplierName)) {
                userIds = companyDao.findUserIdsByFuzzyCorporation(supplierName, userIds);
                if (userIds == null || userIds.isEmpty()) {
                    result.setResult(EMPTY_SUPPLIER_DTO_PAGE);
                    return result;
                }
            }

            Paging<User> paging;
            PageInfo pageInfo = new PageInfo(pageNo, size);

            Integer select = (type == null ? 0 : type);

            switch (select) {
                case 1://入驻待审核
                    paging = userDao.findByApproveAndUserIds(User.Type.SUPPLIER.value(), User.ApproveStatus.ENTER_WAITING_FOR_APPROVE.value(), null, userIds, pageInfo.getOffset(), pageInfo.getLimit());
                    break;
                case 2://修改注册信息待审核
                    paging = userDao.findByApproveAndUserIds(User.Type.SUPPLIER.value(), User.ApproveStatus.MODIFY_INFO_WAITING_FOR_APPROVE.value(), null, userIds, pageInfo.getOffset(), pageInfo.getLimit());
                    break;
                case 3://最近7天入驻审核通过的供应商(只有上级能查看)
                    paging = userDao.findLastEnterPassByUserIds(7, null, userIds, pageInfo.getOffset(), pageInfo.getLimit());
                    break;
                default://入驻待审核和修改注册信息待审核
                    paging = userDao.findApprovingSupplierByUserIds(null, userIds, pageInfo.getOffset(), pageInfo.getLimit());
            }

            if (paging.getTotal() == 0) {
                result.setResult(EMPTY_SUPPLIER_DTO_PAGE);
                return result;
            }

            List<User> users = paging.getData();
            result.setResult(new Paging<SupplierDto>(paging.getTotal(), getSupplierDtoByUsers(users, isQueryApproverNames)));
            return result;

        } catch (Exception e) {
            log.error("fail to find supplier where type={},cause:{}", type, getStackTraceAsString(e));
            result.setError("query.supplier.fail");
            return result;
        }

    }

    private List<SupplierDto> getSupplierDtoByUsers(List<User> users, boolean isQueryApprovers) {

        List<SupplierDto> supplierDtos = Lists.newArrayList();

        for (User user : users) {

            Company oldCompany = companyDao.findByUserId(user.getId());
            if (oldCompany == null) {
                log.warn("company not found where user id={}", user.getId());
                continue;
            }

            List<CompanyMainBusiness> oldCompanyMainBusinesses = companyMainBusinessDao.findByUserId(user.getId());
            if (oldCompanyMainBusinesses == null) {
                log.warn("company main business not found where user id={}", user.getId());
                continue;
            }

            ContactInfo oldContactInfo = contactInfoDao.findByUserId(user.getId());
            if (oldContactInfo == null) {
                log.warn("contact info not found where user id={}", user.getId());
                continue;
            }

            CompanyDto companyDto = supplierInfoChangedService.getNewBaseCompany(user.getId(), oldCompany, oldCompanyMainBusinesses, null).getSupplierInfo();
            ContactInfo newContactInfo = supplierInfoChangedService.getNewContactInfo(user.getId(), oldContactInfo).getSupplierInfo();
            PaperworkDto paperworkDto = supplierInfoChangedService.getNewPaperwork(user.getId(), oldCompany).getSupplierInfo();
            BeanMapper.copy(paperworkDto, companyDto.getCompany());

            SupplierDto supplierDto = new SupplierDto(companyDto.getCompany());

            supplierDto.setNick(user.getNick());

            List<String> mainBusinessNames = getCompanyBusinessNames(companyDto.getCompanyMainBusinesses());
            supplierDto.setMainBusinessNames(mainBusinessNames);

            //需要查询该供应商的审核人
            if (isQueryApprovers) {
                List<String> approverNames = getSupplierApproverNames(companyDto.getCompanyMainBusinesses());
                supplierDto.setApproverNames(approverNames);
            }

            supplierDto.setOfficePhone(newContactInfo.getOfficePhone());
            supplierDto.setMobile(newContactInfo.getMobile());

            if (Objects.equal(user.getApproveStatus(), User.ApproveStatus.ENTER_WAITING_FOR_APPROVE.value())) {//入驻审核
                supplierDto.setApprovedType(SupplierDto.ApprovedType.ENTER.value());
            }

            if (Objects.equal(user.getApproveStatus(), User.ApproveStatus.MODIFY_INFO_WAITING_FOR_APPROVE.value())) {//修改审核
                supplierDto.setApprovedType(SupplierDto.ApprovedType.MODIFY.value());
            }

            supplierDtos.add(supplierDto);
        }

        return supplierDtos;
    }

    @Override
    public Response<Paging<SupplierDto>> findSupplierQualifyBy(Integer status, String nick, Integer pageNo, Integer size) {
        Response<Paging<SupplierDto>> result = new Response<Paging<SupplierDto>>();
        PageInfo pageInfo = new PageInfo(pageNo, size);
        try {
            Paging<User> paging = userDao.findQualifyingBy(status, nick, pageInfo.getOffset(), pageInfo.getLimit());
            if (paging.getTotal() == 0) {
                result.setResult(EMPTY_SUPPLIER_DTO_PAGE);
                return result;
            }

            List<User> users = paging.getData();
            List<SupplierDto> supplierDtos = Lists.newArrayList();

            for (User user : users) {

                Optional<Company> companyOptional = companyCache.getUnchecked(user.getId());

                if (!companyOptional.isPresent()) {
                    log.warn("company not found where user id={}", user.getId());
                    continue;
                }

                Company company = companyOptional.get();

                SupplierDto supplierDto = new SupplierDto(company);
                supplierDto.setNick(user.getNick());

                List<CompanyMainBusiness> companyMainBusinesses = companyMainBusinessDao.findByCompanyId(company.getId());
                if (companyMainBusinesses == null) {
                    log.warn("company main business not found where company id={}", company.getId());
                    continue;
                }

                List<String> mainBusinessNames = getCompanyBusinessNames(companyMainBusinesses);
                supplierDto.setMainBusinessNames(mainBusinessNames);

                ContactInfo contactInfo = contactInfoDao.findByUserId(user.getId());

                if (contactInfo != null) {
                    supplierDto.setOfficePhone(contactInfo.getOfficePhone());
                }

                supplierDtos.add(supplierDto);

            }

            result.setResult(new Paging<SupplierDto>(paging.getTotal(), supplierDtos));

        } catch (Exception e) {
            log.error("find supplier(status={}, nick={}) failed, error code:{}", status, nick, getStackTraceAsString(e));
            result.setError("query.supplier.fail");
        }
        return result;
    }

    @Override
    public Response<Boolean> certificateExpired(BaseUser baseUser) {
        Response<Boolean> result = new Response<Boolean>();

        if (baseUser.getId() == null) {
            log.error("user id can not be null");
            result.setError("user.id.not.null.fail");
            return result;
        }

        try {

            Optional<Company> companyOptional = companyCache.getUnchecked(baseUser.getId());
            if (!companyOptional.isPresent()) {
                log.error("company not found where user id:{}", baseUser.getId());
                result.setError("company.not.found");
                return result;
            }

            Company company = companyOptional.get();
            PaperworkDto paperworkDto = getPaperwork(company);

            //根据国家检查三证信息是否完整
            Response<Boolean> checkPaperworkRes = checkPaperworkByCountryId(company.getRegCountry(), paperworkDto);
            if (!Objects.equal(checkPaperworkRes.getResult(), Boolean.TRUE)) {
                result.setError(checkPaperworkRes.getError());
                return result;
            }

            int type = CountryCertificate.check(company.getRegCountry());

            if (type == CountryCertificate.NO_CERTIFICATE) {
                result.setResult(Boolean.FALSE);
                return result;
            }

            Date now = new Date(System.currentTimeMillis());

            if (type == CountryCertificate.ONLY_NEED_BUSINESS_LICENSE) {
                boolean expired = now.after(company.getBlDate());
                result.setResult(expired);
                return result;
            }

            if (type == CountryCertificate.ONLY_NEED_BUSINESS_LICENSE_AND_TAX_NO) {
                boolean expired = now.after(company.getBlDate());
                result.setResult(expired);
                return result;
            }

            if (type == CountryCertificate.NEED_THREE_CERTIFICATES) {
                boolean expired = now.after(company.getBlDate()) || now.after(company.getOcDate());
                result.setResult(expired);
                return result;
            }

            log.error("not match a type when check company certificate expire where user id={}", baseUser.getId());
            result.setResult(Boolean.FALSE);
            return result;


        } catch (Exception e) {
            log.error("fail to check company certificate if expired where user id={}", baseUser.getId());
            result.setError("check.company.certificate.expired.fail");
            return result;
        }

    }

    private PaperworkDto getPaperwork(Company company) {
        PaperworkDto paperworkDto = new PaperworkDto();

        if (company == null) {
            return paperworkDto;
        }

        paperworkDto.setBusinessLicense(company.getBusinessLicense());
        paperworkDto.setBusinessLicenseId(company.getBusinessLicenseId());
        paperworkDto.setBlDate(company.getBlDate());
        paperworkDto.setOrgCert(company.getOrgCert());
        paperworkDto.setOrgCertId(company.getOrgCertId());
        paperworkDto.setOcDate(company.getOcDate());
        paperworkDto.setTaxNo(company.getTaxNo());
        paperworkDto.setTaxNoId(company.getTaxNoId());

        return paperworkDto;
    }

    @Override
    public Response<Paging<SupplierTQRDCInfo>> findSupplierTQRDCInfoBy(String supplierName, String supplierCode, String month, String module, String orderBy, Integer compositeScoreStart, Integer compositeScoreEnd, Integer pageNo, Integer size) {

        Response<Paging<SupplierTQRDCInfo>> result = new Response<Paging<SupplierTQRDCInfo>>();

        try {

            if (Strings.isNullOrEmpty(supplierName) && Strings.isNullOrEmpty(supplierCode) && Strings.isNullOrEmpty(month) && Strings.isNullOrEmpty(orderBy) && compositeScoreStart == null && compositeScoreEnd == null) {
                result.setResult(new Paging<SupplierTQRDCInfo>());
                return result;
            }
            String queryMonth = null;

            //如果没有选择月份则查询最新月份的信息
            if (Strings.isNullOrEmpty(month)) {
                queryMonth = supplierTQRDCInfoDao.findMaxMonth();
            } else {
                queryMonth = month;
            }

            PageInfo pageInfo = new PageInfo(pageNo, size);
            Paging<SupplierTQRDCInfo> paging = supplierTQRDCInfoDao.findBy(supplierName, supplierCode, queryMonth, module, orderBy, compositeScoreStart, compositeScoreEnd, pageInfo.getOffset(), pageInfo.getLimit());
            result.setResult(paging);
            return result;

        } catch (Exception e) {
            log.error("fail to find supplier TQRDC information,cause:{}", getStackTraceAsString(e));
            result.setError("query.supplier.tqrdc.information.fail");
            return result;
        }

    }

    @Override
    public Response<CompanyPerformance> findDetailSupplierTQRDCInfo(String supplierCode) {
        Response<CompanyPerformance> result = new Response<CompanyPerformance>();

        if (Strings.isNullOrEmpty(supplierCode)) {
            log.error("supplier name can not be null");
            result.setError("supplier.code.not.null");
            return result;
        }

        try {
            String monthStart = DateUtil.getCurrentYearFirstMonth();
            String monthEnd = DateUtil.getCurrentYearLastMonth();

            //获取供应商今年各月份的tqrdc信息
            List<SupplierTQRDCInfo> supplierTQRDCInfos = supplierTQRDCInfoDao.findBySupplierCode(supplierCode, monthStart, monthEnd);

            //封装数据，方便前端显示各种图表信息
            CompanyPerformance companyPerformance = getCompanyPerformance(supplierTQRDCInfos);

            result.setResult(companyPerformance);
            return result;

        } catch (Exception e) {
            log.error("fail to query detail supplier tqrdc information,cause:{}", Throwables.getStackTraceAsString(e));
            result.setError("query.detail.supplier.tqrdc.fail");
            return result;
        }

    }

    @Override
    public Response<CompanyPerformance> findDetailSupplierTQRDCInfoByUserId(Long userId) {
        Response<CompanyPerformance> result = new Response<CompanyPerformance>();

        if (userId == null) {
            log.error("userId name can not be null");
            result.setError("user.id.not.null.fail");
            return result;
        }

        try {
            String monthStart = DateUtil.getCurrentYearFirstMonth();
            String monthEnd = DateUtil.getCurrentYearLastMonth();

            //获取供应商今年各月份的tqrdc信息
            List<SupplierTQRDCInfo> supplierTQRDCInfos = supplierTQRDCInfoDao.findByUserId(userId, monthStart, monthEnd);

            //封装数据，方便前端显示各种图表信息
            CompanyPerformance companyPerformance = getCompanyPerformance(supplierTQRDCInfos);

            result.setResult(companyPerformance);
            return result;

        } catch (Exception e) {
            log.error("fail to query detail supplier tqrdc information with param:[{}],cause:{}", userId, Throwables.getStackTraceAsString(e));
            result.setError("query.detail.supplier.tqrdc.fail");
            return result;
        }

    }

    private CompanyPerformance getCompanyPerformance(List<SupplierTQRDCInfo> supplierTQRDCInfos) {
        CompanyPerformance companyPerformance = new CompanyPerformance();
        if (supplierTQRDCInfos == null) {
            return companyPerformance;
        }

        List<Double> locations = Lists.newArrayListWithCapacity(12);       //每月的区位
        List<Integer> modules = Lists.newArrayListWithCapacity(12);        //模块走势
        List<Integer> ranks = Lists.newArrayListWithCapacity(12);          //模块排序
        List<Integer> tScore = Lists.newArrayListWithCapacity(12);         //每月的技术得分
        List<Integer> tRanks = Lists.newArrayListWithCapacity(12);         //技术得分排名
        List<Integer> qScore = Lists.newArrayListWithCapacity(12);         //每月的质量得分
        List<Integer> qRanks = Lists.newArrayListWithCapacity(12);         //技术质量排名
        List<Integer> rScore = Lists.newArrayListWithCapacity(12);         //每月的响应得分
        List<Integer> rRanks = Lists.newArrayListWithCapacity(12);         //响应得分排名
        List<Integer> dScore = Lists.newArrayListWithCapacity(12);              //每月的交付得分
        List<Integer> dRanks = Lists.newArrayListWithCapacity(12);         //交付得分排名
        List<Integer> cScore = Lists.newArrayListWithCapacity(12);              //每月的成本得分
        List<Integer> cRanks = Lists.newArrayListWithCapacity(12);         //成本得分排名

        //以最新月份作为当前月(因为结果是按照月份升序排序的，所以集合最后一个就是最新的月份)
        SupplierTQRDCInfo lastMonth = supplierTQRDCInfos.get(supplierTQRDCInfos.size() - 1);
        lastMonth.shortComing(); //计算短板
        companyPerformance.setSupplierTQRDCInfo(lastMonth);

        //设置每月的各项指标的得分和排名
        Integer redLocations = 0;
        DateTime month = MONTH_FORMAT.parseDateTime(DateUtil.getCurrentYearFirstMonth());

        for (int i = 1; i <= 12; i++) {

            boolean existed = false;
            for (SupplierTQRDCInfo supplierTQRDCInfo : supplierTQRDCInfos) {

                if (Objects.equal(supplierTQRDCInfo.getMonth(), MONTH_FORMAT.print(month))) {

                    locations.add(getLocationValue(supplierTQRDCInfo.getLocation()));
                    modules.add(supplierTQRDCInfo.getCompositeScore());
                    ranks.add(supplierTQRDCInfo.getRank());
                    tScore.add(supplierTQRDCInfo.getTechScore());
                    tRanks.add(supplierTQRDCInfo.getTechScoreRank());
                    qScore.add(supplierTQRDCInfo.getQualityScore());
                    qRanks.add(supplierTQRDCInfo.getQualityScoreRank());
                    rScore.add(supplierTQRDCInfo.getRespScore());
                    rRanks.add(supplierTQRDCInfo.getRespScoreRank());
                    dScore.add(supplierTQRDCInfo.getDeliverScore());
                    dRanks.add(supplierTQRDCInfo.getDeliveryScoreRank());
                    cScore.add(supplierTQRDCInfo.getCostScore());
                    cRanks.add(supplierTQRDCInfo.getCostScoreRank());

                    if (Objects.equal("红区", supplierTQRDCInfo.getLocation())) {
                        ++redLocations;
                    }

                    existed = true;
                    break;
                }

            }

            //没有数据的月份用null填充
            if (!existed) {
                locations.add(null);
                modules.add(null);
                ranks.add(null);
                tScore.add(null);
                tRanks.add(null);
                qScore.add(null);
                qRanks.add(null);
                rScore.add(null);
                rRanks.add(null);
                dScore.add(null);
                dRanks.add(null);
                cScore.add(null);
                cRanks.add(null);
            }

            month = month.plusMonths(1);

        }

        companyPerformance.setRedLocations(redLocations);
        companyPerformance.setLocations(locations);
        companyPerformance.setModules(modules);
        companyPerformance.modulesTrend(); //计算模块趋势
        companyPerformance.setRanks(ranks);
        companyPerformance.setT(tScore);
        companyPerformance.setTranks(tRanks);
        companyPerformance.setQ(qScore);
        companyPerformance.setQranks(qRanks);
        companyPerformance.setR(rScore);
        companyPerformance.setRranks(rRanks);
        companyPerformance.setD(dScore);
        companyPerformance.setDranks(dRanks);
        companyPerformance.setC(cScore);
        companyPerformance.setCranks(cRanks);

        companyPerformance.computeTQRDCTrend(); //计算tqrdc各种趋势

        SupplierModuleCount moduleCount = supplierModuleCountDao.findByModuleName(lastMonth.getModule());
        companyPerformance.setModuleCount(moduleCount);

        Integer count = supplierModuleDetailDao.countBySupplierCode(lastMonth.getSupplierCode());
        companyPerformance.setEliminated(count>0);

        return companyPerformance;
    }

    private Double getLocationValue(String location) {
        if ("绿区".equals(location)) {
            return 12.5;
        }
        if ("蓝区".equals(location)) {
            return 37.5;
        }
        if ("黄区".equals(location)) {
            return 62.5;
        }
        if ("红区".equals(location)) {
            return 87.5;
        }
        return 0.0;
    }

    @Override
    public Response<SupplierTQRDCInfo> findSupplierLastTQRDCInfoByUserId(Long userId) {
        Response<SupplierTQRDCInfo> result = new Response<SupplierTQRDCInfo>();

        if (userId == null) {
            log.error("user id can not be null");
            result.setError("user.id.not.null.fail");
            return result;
        }

        try {

            SupplierTQRDCInfo supplierTQRDCInfo = supplierTQRDCInfoDao.findLastByUserId(userId);
            if (supplierTQRDCInfo == null) {
                log.warn("supplier tqrdc information not found where user id={}", userId);
                result.setResult(new SupplierTQRDCInfo());
                return result;
            }

            result.setResult(supplierTQRDCInfo);
            return result;

        } catch (Exception e) {
            log.error("fail to find supplier tqrdc information where user id={},cause:{}", userId, getStackTraceAsString(e));
            result.setError("query.supplier.tqrdc.information.fail");
            return result;
        }

    }

    @Override
    public Response<SupplierTQRDCInfos> findSupplierTQRDCInfos() {
        Response<SupplierTQRDCInfos> resp = new Response<SupplierTQRDCInfos>();
        try {
            List<SupplierLocationInfo> supplierLocationInfos = supplierTQRDCInfoDao.groupByMonthAndLocation();
            SupplierTQRDCInfos supplierTQRDCInfos = new SupplierTQRDCInfos();
            statisticsLocations(supplierTQRDCInfos, supplierLocationInfos);
            statisticsYearLocations(supplierTQRDCInfos, supplierLocationInfos);

            resp.setResult(supplierTQRDCInfos);
        } catch (Exception e) {
            log.error("failed to find supplierTQRDCInfos, cause: {}", getStackTraceAsString(e));
            resp.setError("company.findsuppliers.fail");
        }
        return resp;
    }

    /**
     * 统计出最近月的区域计数信息
     *
     * @param supplierTQRDCInfos
     * @param supplierLocationInfos
     */
    private void statisticsLocations(SupplierTQRDCInfos supplierTQRDCInfos, List<SupplierLocationInfo> supplierLocationInfos) {
        List<Integer> latestMonthLocations = Lists.newArrayListWithCapacity(4);
        String latestMonth = supplierLocationInfos.get(supplierLocationInfos.size() - 1).getMonth();
        SupplierLocationInfo locationInfo = null;

        for (int i = supplierLocationInfos.size() - 1; i >= 0; i--) {
            locationInfo = supplierLocationInfos.get(i);
            if (!Objects.equal(latestMonth, locationInfo.getMonth())) { //不是最近月
                break;
            }
            latestMonthLocations.add(locationInfo.getCount());
        }
        // 填充空的数据
        int paddingNum = 4 - latestMonthLocations.size();
        for (int i = 0; i < paddingNum; i++) {
            latestMonthLocations.add(null);
        }
        supplierTQRDCInfos.setLocations(latestMonthLocations);
        supplierTQRDCInfos.setLastMonth(latestMonth);
    }

    /**
     * 统计年内区域计数信息
     *
     * @param supplierTQRDCInfos
     * @param supplierLocationInfos
     */
    private void statisticsYearLocations(SupplierTQRDCInfos supplierTQRDCInfos, List<SupplierLocationInfo> supplierLocationInfos) {
        List<List<Integer>> yearLocations = Lists.newArrayListWithCapacity(12);
        String curMonth = null;
        List<Integer> curMonthLocations = Lists.newArrayList(4);
        for (SupplierLocationInfo locationInfo : supplierLocationInfos) {
            if (curMonth != null && !Objects.equal(curMonth, locationInfo.getMonth())) {
                for (int i = 0; i < 4 - curMonthLocations.size(); i++) {
                    curMonthLocations.add(null);
                }
                yearLocations.add(curMonthLocations);
            }
            curMonthLocations.add(locationInfo.getCount());
            curMonth = locationInfo.getMonth();

        }
        // 填充年内月份的区域计数
        for (int i = 0; i < 12 - yearLocations.size(); i++) {
            yearLocations.add(null);
        }
        supplierTQRDCInfos.setYearLocations(yearLocations);
    }

    @Override
    public Response<Boolean> setTQRDCRank() {
        Response<Boolean> result = new Response<Boolean>();

        try {

            log.info("start to update tqrdc rank");

            List<SupplierTQRDCInfo> month1 = supplierTQRDCInfoDao.findByMonth("2014-01");
            sortTQRDC(month1);
            accountManager.bulkUpdateSupplierQTRDCInfo(month1);

            List<SupplierTQRDCInfo> month2 = supplierTQRDCInfoDao.findByMonth("2014-02");
            sortTQRDC(month2);
            accountManager.bulkUpdateSupplierQTRDCInfo(month2);

            List<SupplierTQRDCInfo> month3 = supplierTQRDCInfoDao.findByMonth("2014-03");
            sortTQRDC(month3);
            accountManager.bulkUpdateSupplierQTRDCInfo(month3);

            List<SupplierTQRDCInfo> month4 = supplierTQRDCInfoDao.findByMonth("2014-04");
            sortTQRDC(month4);
            accountManager.bulkUpdateSupplierQTRDCInfo(month4);

            log.info("end to update tqrdc rank");
            result.setResult(Boolean.TRUE);
            return result;

        } catch (Exception e) {
            log.error("fail to update tqrdc rank,cause:{}", getStackTraceAsString(e));
            result.setError("update.tqrdc.rank.fail");
            return result;
        }
    }

    @Override
    public Response<Boolean> isComplete(Long userId) {
        Response<Boolean> result = new Response<Boolean>();

        if (userId == null) {
            log.error("user id can not be null");
            result.setError("user.id.not.null.fail");
            return result;
        }

        try {
            result.setResult(completeInfo(userId));
            return result;
        } catch (Exception e) {
            log.error("fail to check supplier if have a complete information");
            result.setError("check.supplier.complete.information.fail");
            return result;
        }

    }

    /**
     * 获取供应商信息是否完整的检查结果
     */
    private InfoCompleteCheckResult getInfoCompleteCheckResult(Long userId) {

        InfoCompleteCheckResult infoCompleteCheckResult = new InfoCompleteCheckResult();

        Optional<Company> companyOptional = companyCache.getUnchecked(userId);

        if (!companyOptional.isPresent()) {
            log.error("company not found where user id={}", userId);
            infoCompleteCheckResult.setIsComplete(Boolean.FALSE);
            infoCompleteCheckResult.setInfoCompletePercent(0L);
            return infoCompleteCheckResult;
        }

        Company company = companyOptional.get();

        if (company.isCompleteInfo()) {
            infoCompleteCheckResult.setIsComplete(Boolean.TRUE);
            infoCompleteCheckResult.setInfoCompletePercent(100L);
            return infoCompleteCheckResult;
        }

        //没有填写的字段数量
        int missCount = 0;

        //检查公司基本信息
        missCount += checkConstraint(company);

        //检查主营业务
        List<CompanyMainBusiness> companyMainBusinesses = companyMainBusinessDao.findByUserId(userId);
        if (companyMainBusinesses == null || companyMainBusinesses.isEmpty()) {
            missCount++;
        }

        //检查可供货园区
        List<CompanySupplyPark> companySupplyParks = companySupplyParkDao.findByUserId(userId);
        if (companySupplyParks == null || companySupplyParks.isEmpty()) {
            missCount++;
        }

        //检查联系人信息
        ContactInfo contactInfo = contactInfoDao.findByUserId(userId);
        missCount += (contactInfo == null ? SupplierColumnCount.CONTACT_INFO_COLUMN_COUNT : checkConstraint(contactInfo));

        //检查财务信息
        Finance finance = financeDao.findByUserId(userId);
        missCount += (finance == null ? SupplierColumnCount.FINANCE_COLUMN_COUNT : checkConstraint(finance));

        CompanyExtraDelivery delivery = companyExtraDeliveryDao.findByUserId(userId);
        missCount += (delivery == null ? SupplierColumnCount.EXTRA_DELIVERY_COLUMN_COUNT : checkConstraint(delivery));

        CompanyExtraQuality quality = companyExtraQualityDao.findByUserId(userId);
        missCount += (quality == null ? SupplierColumnCount.EXTRA_QUALITY_COLUMN_COUNT : checkConstraint(quality));

        CompanyExtraRD rd = companyExtraRDDao.findByUserId(userId);
        missCount += (rd == null ? SupplierColumnCount.EXTRA_RD_COLUMN_COUNT : checkConstraint(rd));

        if (missCount == 0) {

            //所有信息已经完善，更新company表的标志字段和打标签
            accountManager.setInfoCompleteAndAddTag(userId, company.getId());
            companyCache.invalidate(company.getUserId());

            infoCompleteCheckResult.setIsComplete(Boolean.TRUE);
            infoCompleteCheckResult.setInfoCompletePercent(100L);

        } else {
            long percent = Math.round((SupplierColumnCount.COLUMN_TOTAL - missCount) * 100.0 / SupplierColumnCount.COLUMN_TOTAL);
            infoCompleteCheckResult.setIsComplete(Boolean.FALSE);
            infoCompleteCheckResult.setInfoCompletePercent(percent);
        }

        return infoCompleteCheckResult;

    }


    /**
     * 检查供应商的信息是否完整
     */
    private boolean completeInfo(Long userId) {

        Optional<Company> companyOptional = companyCache.getUnchecked(userId);

        if (!companyOptional.isPresent()) {
            log.error("company not found where user id={}", userId);
            return false;
        }

        Company company = companyOptional.get();

        if (company.isCompleteInfo()) {
            return true;
        }

        //检查公司基本信息
        if (checkConstraint(company) != 0) {
            return false;
        }

        //检查主营业务
        List<CompanyMainBusiness> companyMainBusinesses = companyMainBusinessDao.findByUserId(userId);
        if (companyMainBusinesses == null || companyMainBusinesses.isEmpty()) {
            return false;
        }

        //检查可供货园区
        List<CompanySupplyPark> companySupplyParks = companySupplyParkDao.findByUserId(userId);
        if (companySupplyParks == null || companySupplyParks.isEmpty()) {
            return false;
        }

        //检查联系人信息
        ContactInfo contactInfo = contactInfoDao.findByUserId(userId);
        if (contactInfo == null || checkConstraint(contactInfo) != 0) {
            return false;
        }

        //检查财务信息
        Finance finance = financeDao.findByUserId(userId);
        if (finance == null || checkConstraint(finance) != 0) {
            return false;
        }

        CompanyExtraDelivery delivery = companyExtraDeliveryDao.findByUserId(userId);
        if (delivery == null || checkConstraint(delivery) != 0) {
            return false;
        }

        CompanyExtraQuality quality = companyExtraQualityDao.findByUserId(userId);
        if (quality == null || checkConstraint(quality) != 0) {
            return false;
        }

        CompanyExtraRD rd = companyExtraRDDao.findByUserId(userId);
        if (rd == null || checkConstraint(rd) != 0) {
            return false;
        }

        return true;

    }

    private <T> int checkConstraint(T t) {
        Set<ConstraintViolation<T>> violations = validator.validate(t);
        return violations.size();
    }

    @Override
    public Response<List<ProductLine>> findAllProductLine() {
        Response<List<ProductLine>> result = new Response<List<ProductLine>>();

        try {
            List<ProductLine> productLines = productLineDao.findAll();
            if (productLines == null) {
                productLines = Collections.emptyList();
            }
            result.setResult(productLines);
            return result;
        } catch (Exception e) {
            log.error("fail to find all product line,cause:{}", getStackTraceAsString(e));
            result.setError("query.product.line.fail");
            return result;
        }
    }

    @Override
    public Response<List<ProductLine>> findProductLineByIds(List<Long> ids) {
        Response<List<ProductLine>> result = new Response<List<ProductLine>>();

        try {
            List<ProductLine> productLines = productLineDao.findByIds(ids);
            if (productLines == null) {
                productLines = Collections.emptyList();
            }
            result.setResult(productLines);
            return result;
        } catch (Exception e) {
            log.error("fail to find product line where ids={},cause:{}", ids, getStackTraceAsString(e));
            result.setError("query.product.line.fail");
            return result;
        }
    }

    @Override
    public Response<SupplierStepDto> getSupplierStepBySelf(BaseUser baseUser) {
        if (isNull(baseUser)) {
            log.error("must login first");
            Response<SupplierStepDto> result = new Response<SupplierStepDto>();
            result.setError("user.not.login");
            return result;
        }
        return getSupplierStepRemain(baseUser.getId());
    }

    @Override
    public Response<SupplierStepDto> getSupplierStep(BaseUser baseUser, Long userId) {
        // TODO: baseUser's role checking
        return getSupplierStepRemain(userId);
    }

    private Response<SupplierStepDto> getSupplierStepRemain(Long userId) {
        Response<SupplierStepDto> result = new Response<SupplierStepDto>();
        if (isNull(userId)) {
            log.error("must login first");
            result.setError("user.not.login");
            return result;
        }
        try {

            Optional<Company> companyOptional = companyCache.getUnchecked(userId);

            if (!companyOptional.isPresent()) {
                log.error("company not found where user id={}", userId);
                result.setError("company.not.found");
                return result;
            }

            SupplierStepDto supplierStepDto = new SupplierStepDto();

            Company company = companyOptional.get();
            User user = userDao.findById(userId);

            //返回审核状态和审核意见
            if (Objects.equal(user.getApproveStatus(), User.ApproveStatus.ENTER_FAIL.value())) {
                supplierStepDto.setApproveStatus(SupplierStepDto.ApproveStatus.ENTER_FAIL.value());
            } else if (Objects.equal(user.getApproveStatus(), User.ApproveStatus.MODIFY_INFO_FAIL.value())) {
                supplierStepDto.setApproveStatus(SupplierStepDto.ApproveStatus.MODIFY_INFO_FAIL.value());
            } else {
                supplierStepDto.setApproveStatus(SupplierStepDto.ApproveStatus.NORMAL.value());
            }
            SupplierApproveLog supplierApproveLog = supplierApproveLogDao.findLastByUserIdAndApproveType(userId, null);
            if (supplierApproveLog != null) {
                supplierStepDto.setApproveDescription(supplierApproveLog.getDescription());
            }

            //审核失败的用户停留在上传三证阶段
            if (Objects.equal(user.getApproveStatus(), User.ApproveStatus.ENTER_FAIL.value())) {
                supplierStepDto.setStep(SupplierStep.UPLOAD_PAPERWORK.value());
                result.setResult(supplierStepDto);
                return result;
            }

            //导入的供应商不需要检查三证和审核
            if (Objects.equal(user.getOrigin(), User.Origin.NORMAL.value())) {

                Response<Boolean> checkPaperwork = checkPaperworkByCountryId(company.getRegCountry(), getPaperwork(company));
                if (!Objects.equal(checkPaperwork.getResult(), Boolean.TRUE)) {
                    supplierStepDto.setStep(SupplierStep.UPLOAD_PAPERWORK.value());
                    result.setResult(supplierStepDto);//上传三证阶段
                    return result;
                }

                if (Objects.equal(user.getApproveStatus(), User.ApproveStatus.ENTER_WAITING_FOR_APPROVE.value())) {
                    supplierStepDto.setStep(SupplierStep.WAIT_FOR_APPROVE.value());
                    result.setResult(supplierStepDto);//等待激活阶段
                    return result;
                }

            }

            InfoCompleteCheckResult infoCompleteCheckResult = getInfoCompleteCheckResult(userId);
            if (!infoCompleteCheckResult.getIsComplete()) {
                supplierStepDto.setStep(SupplierStep.COMPLETE_INFO.value());
                supplierStepDto.setInfoCompletePercent(infoCompleteCheckResult.getInfoCompletePercent());
                result.setResult(supplierStepDto);//完善信息阶段
                return result;
            }

            // 资质交互状态
            Response<SupplierResourceMaterialInfo> supplierQualifyResp = supplierResourceMaterialService.getInfoByUserId(userId);
            if (!supplierQualifyResp.isSuccess()) {
                result.setError(supplierQualifyResp.getError());
                return result;
            }
            SupplierResourceMaterialInfo supplierResourceMaterialInfo = supplierQualifyResp.getResult();

            // 信用等级状态
            Response<SupplierCreditQualify> supplierCreditResp = supplierCreditQualifyService.findCreditQualifyByUserId(userId);
            if (!supplierCreditResp.isSuccess()) {
                result.setError(supplierCreditResp.getError());
                return result;
            }
            SupplierCreditQualify supplierCreditQualify = supplierCreditResp.getResult();

            if (Strings.isNullOrEmpty(supplierResourceMaterialInfo.getApprovedModuleIds()) || !supplierCreditQualify.isCreditQualified()) {
                supplierStepDto.setStep(SupplierStep.QUALIFY.value());
                supplierStepDto.setAptitudeQualified(false);
                supplierStepDto.setCreditQualified(supplierCreditQualify.isCreditQualified());
                supplierStepDto.setMessage(supplierCreditQualify.getMessage());
                result.setResult(supplierStepDto);//资质和等级验证阶段
                return result;
            }

            supplierStepDto.setStep(SupplierStep.STANDARD_SUPPLIER.value());
            result.setResult(supplierStepDto);//合格供应商
            return result;

        } catch (Exception e) {
            log.error("fail to get supplier step where user id={},cause:{}", userId, getStackTraceAsString(e));
            result.setError("get.supplier.step.fail");
            return result;
        }

    }

    @Override
    public Response<Paging<SupplierDto>> findRefusedSuppliers(String nick, Integer pageNo, Integer size) {

        Response<Paging<SupplierDto>> result = new Response<Paging<SupplierDto>>();

        try {

            Map<String, Object> criteria = Maps.newHashMap();
            criteria.put("type", null);
            criteria.put("approveStatus", null);
            criteria.put("refuseStatus", User.RefuseStatus.IS_REFUSED.value());
            criteria.put("nick", Strings.emptyToNull(nick));

            PageInfo pageInfo = new PageInfo(pageNo, size);
            Paging<User> paging = userDao.findBy(criteria, pageInfo.getOffset(), pageInfo.getLimit());

            if (paging.getTotal() == 0) {
                result.setResult(EMPTY_SUPPLIER_DTO_PAGE);
                return result;
            }

            List<User> users = paging.getData();
            List<SupplierDto> supplierDtos = Lists.newArrayList();

            for (User user : users) {

                Optional<Company> companyOptional = companyCache.getUnchecked(user.getId());

                if (!companyOptional.isPresent()) {
                    log.warn("company not found where user id={}", user.getId());
                    continue;
                }

                Company company = companyOptional.get();

                SupplierDto supplierDto = new SupplierDto(company);
                supplierDto.setNick(user.getNick());

                List<CompanyMainBusiness> companyMainBusinesses = companyMainBusinessDao.findByCompanyId(company.getId());
                if (companyMainBusinesses == null) {
                    log.warn("company main business not found where company id={}", company.getId());
                    continue;
                }

                List<String> mainBusinessNames = getCompanyBusinessNames(companyMainBusinesses);
                supplierDto.setMainBusinessNames(mainBusinessNames);

                ContactInfo contactInfo = contactInfoDao.findByUserId(user.getId());

                if (contactInfo != null) {
                    supplierDto.setOfficePhone(contactInfo.getOfficePhone());
                }

                supplierDtos.add(supplierDto);

            }

            result.setResult(new Paging<SupplierDto>(paging.getTotal(), supplierDtos));
            return result;

        } catch (Exception e) {
            log.error("fail to find refused supplier,cause:{}", getStackTraceAsString(e));
            result.setError("query.supplier.fail");
            return result;
        }

    }

    @Override
    public Response<Boolean> addSupplierCode(Long companyId, String supplierCode) {
        Response<Boolean> result = new Response<Boolean>();

        if (companyId == null) {
            log.error("company id can not be null");
            result.setError("company.id.not.null.fail");
            return result;
        }

        if (Strings.isNullOrEmpty(supplierCode)) {
            log.error("supplier code can not be null");
            result.setError("supplier.code.not.null.fail");
            return result;
        }

        try {

            Company company = companyDao.findById(companyId);
            if (company == null) {
                log.error("company not found where company id={}", companyId);
                result.setError("company.not.found");
                return result;
            }

            accountManager.addSupplierCodeAndTag(company.getUserId(), companyId, supplierCode);
            companyCache.invalidate(company.getUserId());

            result.setResult(Boolean.TRUE);

        } catch (Exception e) {
            log.error("fail to add supplier code where company id={},supplierCode={},cause:{}", companyId, supplierCode, Throwables.getStackTraceAsString(e));
            result.setError("add.supplier.code.fail");
        }

        return result;

    }

    @Override
    public Response<Map<Long, ContactInfo>> findContactInfoByUserIds(List<Long> ids) {
        Response<Map<Long, ContactInfo>> resp = new Response<Map<Long, ContactInfo>>();
        try {
            if (Iterables.isEmpty(ids)) {
                resp.setResult(Maps.<Long, ContactInfo>newHashMap());
                return resp;
            }

            List<ContactInfo> contactInfos = contactInfoDao.findByUserIds(ids);
            Map<Long, ContactInfo> mapResult = Maps.newHashMapWithExpectedSize(contactInfos.size());
            for (ContactInfo ci : contactInfos) {
                mapResult.put(ci.getUserId(), ci);
            }
            resp.setResult(mapResult);
        } catch (Exception e) {
            log.error("failed to find contactinfo of users(ids={}), cause: {}",
                    ids, Throwables.getStackTraceAsString(e));
            resp.setError("contactinfo.find.fail");
        }
        return resp;
    }

    @Override
    public Response<Map<Long, List<CompanyMainBusiness>>> findMainBussinessByUserIds(List<Long> ids) {
        Response<Map<Long, List<CompanyMainBusiness>>> resp = new Response<Map<Long, List<CompanyMainBusiness>>>();
        try {
            if (Iterables.isEmpty(ids)) {
                resp.setResult(Maps.<Long, List<CompanyMainBusiness>>newHashMap());
                return resp;
            }
            List<CompanyMainBusiness> companyMainBusinesses = companyMainBusinessDao.findByUserIds(ids);
            Map<Long, List<CompanyMainBusiness>> mapResult = Maps.newHashMap();
            Long userId;
            for (CompanyMainBusiness mainBusiness : companyMainBusinesses) {
                userId = mainBusiness.getUserId();
                if (mapResult.containsKey(userId)) {
                    mapResult.get(userId).add(mainBusiness);
                } else {
                    List<CompanyMainBusiness> cmbs = Lists.newArrayList();
                    cmbs.add(mainBusiness);
                    mapResult.put(userId, cmbs);
                }
            }
            resp.setResult(mapResult);
        } catch (Exception e) {
            log.error("failed to find CompanyMainBusiness(userIds={}), cause: {}",
                    ids, Throwables.getStackTraceAsString(e));
            resp.setError("mainbusiness.find.fail");
        }
        return resp;
    }

    @Override
    public Response<List<CompanyMainBusiness>> findMainBussinessByUserId(BaseUser user) {
        Response<List<CompanyMainBusiness>> resp = new Response<List<CompanyMainBusiness>>();
        try {
            if (user == null) {
                log.error("user isn't login");
                resp.setError("user.not.login");
                return resp;
            }
            List<CompanyMainBusiness> result = companyMainBusinessDao.findByUserId(user.getId());
            resp.setResult(result);
        } catch (Exception e) {
            log.error("failed to find company main business, cause: {}", user, Throwables.getStackTraceAsString(e));
            resp.setError("companymainbusiness.find.fail");
        }
        return resp;
    }

    @Override
    public Response<List<CompanyMainBusiness>> findMainBusinessByCompanyId(Long companyId) {
        Response<List<CompanyMainBusiness>> rusult = new Response<List<CompanyMainBusiness>>();
        try {
            if (companyId == null) {
                log.error("company id can not be null");
                rusult.setError("company.id.not.null.fail");
                return rusult;
            }
            List<CompanyMainBusiness> companyMainBusinesses = companyMainBusinessDao.findByCompanyId(companyId);
            rusult.setResult(companyMainBusinesses);
        } catch (Exception e) {
            log.error("failed to find company main business where company id={}, cause: {}", companyId, Throwables.getStackTraceAsString(e));
            rusult.setError("query.company.main.business.fail");
        }
        return rusult;
    }

    @Override
    public Response<Company> findCompanyBySupplierCode(String supplierCode) {
        Response<Company> resp = new Response<Company>();
        try {
            Company companyBySupplierCode = companyDao.findBySupplierCode(supplierCode);
            resp.setResult(companyBySupplierCode);
            return resp;
        } catch (Exception e) {
            resp.setError("query.company.fail");
            log.error("Faild to select from `snz_companies` with supplierCode:{}", supplierCode, e);
            return resp;
        }
    }

    @Override
    public Response<List<Company>> findCompaniesByFuzzySupplierName(String supplierName) {
        Response<List<Company>> resp = new Response<List<Company>>();
        try {
            List<Company> companyBySupplierName = companyDao.findCompanyBySupplierName(supplierName);
            resp.setResult(companyBySupplierName);
            return resp;
        } catch (Exception e) {
            resp.setError("query.company.fail");
            log.error("Faild to select from `snz_companies` with supplierName:{}", supplierName, e);
            return resp;
        }
    }

    @Override
    public Response<Paging<SupplierDto>> findEnterPassSuppliers(BaseUser baseUser, String nick, Integer pageNo, Integer size) {

        Response<Paging<SupplierDto>> result = new Response<Paging<SupplierDto>>();

        String employId = baseUser.getNickName();

        if (Strings.isNullOrEmpty(employId)) {
            log.error("employee id not found");
            result.setError("authority.fail");
            return result;
        }

        try {

            List<String> userRoles = baseUser.getRoles();
            if (userRoles.contains(JobRole.RESOURCE.role())) {//资源小薇

                //查询该登录用户审核的主营业务编号
                List<Long> mainBusinessIds = mainBusinessApproverDao.findMainBusinessIdsByMemberIdOrLeaderId(employId);
                if (mainBusinessIds == null || mainBusinessIds.isEmpty()) {
                    log.error("main business not found where employ id={}", employId);
                    result.setError("approved.main.business.not.found");
                    return result;
                }

                //查询属于该登录用户审核的所有供应商编号
                List<Long> userIds = companyMainBusinessDao.findUserIdsByMainBusinessIds(mainBusinessIds);
                if (userIds == null || userIds.isEmpty()) {
                    log.warn("no supplier select main business(ids:{})", mainBusinessIds);
                    result.setResult(EMPTY_SUPPLIER_DTO_PAGE);
                    return result;
                }

                if (!Strings.isNullOrEmpty(nick)) {
                    userIds = companyDao.findUserIdsByFuzzyCorporation(nick, userIds);
                    if (userIds == null || userIds.isEmpty()) {
                        result.setResult(EMPTY_SUPPLIER_DTO_PAGE);
                        return result;
                    }
                }

                PageInfo pageInfo = new PageInfo(pageNo, size);
                Paging<User> paging = userDao.findEnterPassSupplier(null, userIds, pageInfo.getOffset(), pageInfo.getLimit());

                if (paging.getTotal() == 0) {
                    result.setResult(EMPTY_SUPPLIER_DTO_PAGE);
                    return result;
                }

                List<User> users = paging.getData();
                result.setResult(new Paging<SupplierDto>(paging.getTotal(), getSupplierDtoByUsers(users, false)));
                return result;

            } else {
                log.warn("unknown role when query approve list");
                result.setResult(EMPTY_SUPPLIER_DTO_PAGE);
                return result;
            }

        } catch (Exception e) {
            log.error("fail to find enter pass supplier,cause:{}", getStackTraceAsString(e));
            result.setError("query.supplier.fail");
            return result;
        }

    }

    @Override
    public Response<Paging<SupplierDto>> pagingCompanyHasSupplierCode(BaseUser user, String companyName, Integer pageNo, Integer size) {
        Response<Paging<SupplierDto>> result = new Response<Paging<SupplierDto>>();
        PageInfo page = new PageInfo(pageNo, size);
        // notice that when validate user privileges later, ensure other
        // service referenced this api update  user too
        if (user == null) {
            result.setResult(EMPTY_SUPPLIER_DTO_PAGE);
            return result;
        }

        try {
            Paging<Company> companyPaging = companyDao.pagingCompanyHasSupplierCode(companyName, page.getOffset(), page.getLimit());
            if (companyPaging.getTotal() == 0l) {
                result.setResult(EMPTY_SUPPLIER_DTO_PAGE);
                return result;
            }

            // find user by user ids
            List<Long> userIds = Lists.newArrayList();
            for (Company c : companyPaging.getData()) {
                userIds.add(c.getUserId());
            }

            // 这里返回的用户根据用户id排序
            if (userIds.isEmpty()) {
                result.setResult(EMPTY_SUPPLIER_DTO_PAGE);
                return result;
            }
            final List<User> usersGet = userDao.findByIds(userIds);
            if (usersGet.isEmpty()) {
                result.setResult(EMPTY_SUPPLIER_DTO_PAGE);
                return result;
            }

            // 将供应商按照供应商编码排序的顺序对应到用户
            Map<Long, User> userOrderMap = Maps.newLinkedHashMap();
            for (Long id : userIds) {
                for (User u : usersGet) {
                    if (Objects.equal(u.getId(), id)) {
                        userOrderMap.put(id, u);
                        break;
                    }
                }
            }

            List<User> users = Lists.newArrayList(userOrderMap.values());

            result.setResult(new Paging<SupplierDto>(companyPaging.getTotal(), getSupplierDtoByUsers(users, false)));
        } catch (Exception e) {
            log.error("`pagingCompanyHasSupplierCode` invoke fail. with user:{}, pageNo:{}, size:{}, e:{}",
                    user, pageNo, size);
            result.setError("query.supplier.fail");
            return result;
        }

        return result;
    }

    @Override
    public Response<Boolean> confirmResource(BaseUser baseUser, Long userId, Integer resourceType, String competitors) {
        Response<Boolean> result = new Response<Boolean>();

        try {

            if (userId == null) {
                log.error("user id can not be null");
                result.setError("user.id.not.null.fail");
                return result;
            }

            if (resourceType == null) {
                log.error("resource type can not be null");
                result.setError("resource.type.not.null.fail");
                return result;
            }

            Optional<Company> companyOptional = companyCache.getUnchecked(userId);
            if (!companyOptional.isPresent()) {
                log.error("company not found where user id={}", userId);
                result.setError("company.not.found");
                return result;
            }

            accountManager.confirmResource(baseUser.getId(), userId, companyOptional.get().getId(), resourceType, competitors);
            result.setResult(Boolean.TRUE);

        } catch (Exception e) {
            log.error("fail to confirm resource where user id={} with resource type={}", userId, resourceType);
            result.setError("confirm.resource.fail");
        }

        return result;
    }

    @Override
    public Response<Map<Long, String>> companyHasVcode(Integer pageNp, Integer size) {
        Response<Map<Long, String>> result = new Response<Map<Long, String>>();
        PageInfo page = new PageInfo(pageNp, size);

        try {
            List<Company> companyList = companyDao.findCompanyHasVCode(page.toMap());
            Map<Long, String> vcodes = Maps.newConcurrentMap();

            for (Company c : companyList) {
                vcodes.put(c.getId(), c.getSupplierCode());
            }

            result.setResult(vcodes);
        } catch (Exception e) {
            log.error("`companyHasVcode` invoke fail. with page no:{}, page size:{}, e:{}", pageNp, size, e);
            result.setError("company.find.fail");
            return result;
        }

        return result;
    }

    private void sortTQRDC(List<SupplierTQRDCInfo> supplierTQRDCInfoList) {

        //set tech score rank
        sortT(supplierTQRDCInfoList);

        //set quality score rank
        sortQ(supplierTQRDCInfoList);

        //set resp score rank
        sortR(supplierTQRDCInfoList);

        //set delivery rank
        sortD(supplierTQRDCInfoList);

        //set cost rank
        sortC(supplierTQRDCInfoList);

    }

    private void sortT(List<SupplierTQRDCInfo> supplierTQRDCInfoList) {
        Collections.sort(supplierTQRDCInfoList, new Comparator<SupplierTQRDCInfo>() {
            @Override
            public int compare(SupplierTQRDCInfo supplierTQRDCInfo, SupplierTQRDCInfo supplierTQRDCInfo2) {
                return supplierTQRDCInfo.getTechScore() - supplierTQRDCInfo2.getTechScore();
            }
        });
        for (int i = 0; i < supplierTQRDCInfoList.size(); i++) {
            supplierTQRDCInfoList.get(i).setTechScoreRank(i + 1);
        }
    }

    private void sortQ(List<SupplierTQRDCInfo> supplierTQRDCInfoList) {
        Collections.sort(supplierTQRDCInfoList, new Comparator<SupplierTQRDCInfo>() {
            @Override
            public int compare(SupplierTQRDCInfo supplierTQRDCInfo, SupplierTQRDCInfo supplierTQRDCInfo2) {
                return supplierTQRDCInfo.getQualityScore() - supplierTQRDCInfo2.getQualityScore();
            }
        });
        for (int i = 0; i < supplierTQRDCInfoList.size(); i++) {
            supplierTQRDCInfoList.get(i).setQualityScoreRank(i + 1);
        }
    }

    private void sortR(List<SupplierTQRDCInfo> supplierTQRDCInfoList) {
        Collections.sort(supplierTQRDCInfoList, new Comparator<SupplierTQRDCInfo>() {
            @Override
            public int compare(SupplierTQRDCInfo supplierTQRDCInfo, SupplierTQRDCInfo supplierTQRDCInfo2) {
                return supplierTQRDCInfo.getRespScore() - supplierTQRDCInfo2.getRespScore();
            }
        });
        for (int i = 0; i < supplierTQRDCInfoList.size(); i++) {
            supplierTQRDCInfoList.get(i).setRespScoreRank(i + 1);
        }
    }

    private void sortD(List<SupplierTQRDCInfo> supplierTQRDCInfoList) {
        Collections.sort(supplierTQRDCInfoList, new Comparator<SupplierTQRDCInfo>() {
            @Override
            public int compare(SupplierTQRDCInfo supplierTQRDCInfo, SupplierTQRDCInfo supplierTQRDCInfo2) {
                return supplierTQRDCInfo.getDeliverScore() - supplierTQRDCInfo2.getDeliverScore();
            }
        });
        for (int i = 0; i < supplierTQRDCInfoList.size(); i++) {
            supplierTQRDCInfoList.get(i).setDeliveryScoreRank(i + 1);
        }
    }

    private void sortC(List<SupplierTQRDCInfo> supplierTQRDCInfoList) {
        Collections.sort(supplierTQRDCInfoList, new Comparator<SupplierTQRDCInfo>() {
            @Override
            public int compare(SupplierTQRDCInfo supplierTQRDCInfo, SupplierTQRDCInfo supplierTQRDCInfo2) {
                return supplierTQRDCInfo.getCostScore() - supplierTQRDCInfo2.getCostScore();
            }
        });
        for (int i = 0; i < supplierTQRDCInfoList.size(); i++) {
            supplierTQRDCInfoList.get(i).setCostScoreRank(i + 1);
        }
    }

    private List<String> getCompanyBusinessNames(List<CompanyMainBusiness> companyMainBusinesses) {

        if (companyMainBusinesses == null || companyMainBusinesses.isEmpty()) {
            return Collections.emptyList();
        }

        return Lists.transform(companyMainBusinesses, new Function<CompanyMainBusiness, String>() {
            @Override
            public String apply(CompanyMainBusiness cmb) {
                return cmb.getName();
            }
        });

    }

    private List<String> getSupplierApproverNames(List<CompanyMainBusiness> companyMainBusinesses) {
        if (companyMainBusinesses == null || companyMainBusinesses.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> mainBusinessIds = Lists.transform(companyMainBusinesses, new Function<CompanyMainBusiness, Long>() {
            @Override
            public Long apply(CompanyMainBusiness cmb) {
                return cmb.getMainBusinessId();
            }
        });

        List<MainBusinessApprover> mainBusinessApprovers = mainBusinessApproverDao.findByMainBusinessIds(mainBusinessIds);
        if (mainBusinessApprovers == null || mainBusinessApprovers.isEmpty()) {
            return Collections.emptyList();
        }

        Set<String> approverNames = Sets.newHashSet();
        for (MainBusinessApprover mainBusinessApprover : mainBusinessApprovers) {
            approverNames.add(mainBusinessApprover.getMemberName());
            approverNames.add(mainBusinessApprover.getLeaderName());
        }

        return new ArrayList<String>(approverNames);
    }

}

