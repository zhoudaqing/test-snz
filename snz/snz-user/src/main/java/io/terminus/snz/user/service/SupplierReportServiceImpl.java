package io.terminus.snz.user.service;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import io.terminus.common.model.PageInfo;
import io.terminus.common.model.Paging;
import io.terminus.common.utils.Joiners;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.category.model.BackendCategory;
import io.terminus.snz.category.model.FrontendCategory;
import io.terminus.snz.category.service.BackendCategoryService;
import io.terminus.snz.category.service.FrontendCategoryService;
import io.terminus.snz.statistic.model.SolutionCountType;
import io.terminus.snz.statistic.service.SolutionCountService;
import io.terminus.snz.user.dao.*;
import io.terminus.snz.user.dao.redis.SupplierCountByDimensionRedisDao;
import io.terminus.snz.user.dto.SupplierCountByDimension;
import io.terminus.snz.user.dto.SupplierReportExportDto;
import io.terminus.snz.user.dto.SupplierReportQueryCriteria;
import io.terminus.snz.user.model.*;
import io.terminus.snz.user.tool.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Author:Guo Chaopeng
 * Created on 14-9-3.
 */
@Slf4j
@Service
public class SupplierReportServiceImpl implements SupplierReportService {

    @Autowired
    private MainBusinessApproverDao mainBusinessApproverDao;

    @Autowired
    private CompanyMainBusinessDao companyMainBusinessDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private ContactInfoDao contactInfoDao;

    @Autowired
    private FinanceDao financeDao;

    @Autowired
    private CompanyExtraRDDao companyExtraRDDao;

    @Autowired
    private CompanySupplyParkDao companySupplyParkDao;

    @Autowired
    private SupplierCreditQualifyDao supplierCreditQualifyDao;

    @Autowired
    private SupplierCountByDimensionRedisDao supplierCountByDimensionRedisDao;

    @Autowired
    private SolutionCountService solutionCountService;

    @Autowired
    private FrontendCategoryService frontendCategoryService;

    @Autowired
    private SupplierTQRDCInfoDao supplierTQRDCInfoDao;

    @Autowired
    private AddressService addressService;

    @Autowired
    private SupplierResourceMaterialService supplierResourceMaterialService;

    @Autowired
    private BackendCategoryService backendCategoryService;

    private static final Paging<SupplierReportExportDto> EMPTY_SUPPLIER_REPORT_EXPORT_DTO_PAGE = new Paging<SupplierReportExportDto>(0L, Collections.<SupplierReportExportDto>emptyList());

    private static final List<SupplierReportExportDto> EMPTY_SUPPLIER_REPORT_EXPORT_DTO_LIST = Collections.emptyList();

    @Override
    public Response<Boolean> countSupplierByDimensions() {

        Response<Boolean> result = new Response<Boolean>();

        try {

            Long registeredCount = userDao.countSupplierByCreatedAtAndStepAndIds(null, null, User.Step.REGISTER_SUPPLIER.value(), null);
            Long completedCount = userDao.countSupplierByCreatedAtAndStepAndIds(null, null, User.Step.COMPLETE_SUPPLIER.value(), null);
            Long standardCount = userDao.countSupplierByCreatedAtAndStepAndIds(null, null, User.Step.STANDARD_SUPPLIER.value(), null);
            Long inCount = userDao.countSupplierByCreatedAtAndStepAndIds(null, null, User.Step.IN_SUPPLIER.value(), null);
            Long alternativeCount = userDao.countSupplierByCreatedAtAndStepAndIds(null, null, User.Step.ALTERNATIVE.value(), null);
            Long dieOutCount = userDao.countSupplierByCreatedAtAndStepAndIds(null, null, User.Step.DIE_OUT.value(), null);

            SupplierCountByDimension supplierCountByDimension = new SupplierCountByDimension();
            supplierCountByDimension.setRegisteredCount(registeredCount);
            supplierCountByDimension.setCompletedCount(completedCount);
            supplierCountByDimension.setStandardCount(standardCount);
            supplierCountByDimension.setInCount(inCount);
            supplierCountByDimension.setAlternativeCount(alternativeCount);
            supplierCountByDimension.setDieOutCount(dieOutCount);
            supplierCountByDimension.summary();

            supplierCountByDimensionRedisDao.remove(RedisKeyUtil.countSupplierByDimension());
            supplierCountByDimensionRedisDao.save(RedisKeyUtil.countSupplierByDimension(), supplierCountByDimension);

            result.setResult(Boolean.TRUE);
            return result;

        } catch (Exception e) {
            log.error("fail to count supplier by dimensions, cause:{]", e);
            result.setError("count.supplier.by.dimensions.fail");
            return result;
        }
    }

    @Override
    public Response<SupplierCountByDimension> getSupplierCountByDimensions() {
        Response<SupplierCountByDimension> result = new Response<SupplierCountByDimension>();

        try {

            SupplierCountByDimension supplierCountByDimension = supplierCountByDimensionRedisDao.findByKey(RedisKeyUtil.countSupplierByDimension());
            if (supplierCountByDimension == null) {
                supplierCountByDimension = new SupplierCountByDimension();
            }

            result.setResult(supplierCountByDimension);

        } catch (Exception e) {
            log.error("fail to get supplier count by dimensions,cause:{}", Throwables.getStackTraceAsString(e));
            result.setError("get.supplier.count.by.dimensions.fail");
        }

        return result;
    }

    @Override
    public Response<Paging<SupplierReportExportDto>> queryReport(BaseUser baseUser, SupplierReportQueryCriteria criteria, Integer pageNo, Integer size) {
        Response<Paging<SupplierReportExportDto>> result = new Response<Paging<SupplierReportExportDto>>();

        try {

            String employId = baseUser.getNickName();

            List<Long> mainBusinessIds = mainBusinessApproverDao.findMainBusinessIdsByMemberIdOrLeaderId(employId);
            if (mainBusinessIds == null || mainBusinessIds.isEmpty()) {
                log.error("main business not found where employ id={}", employId);
                result.setError("approved.main.business.not.found");
                return result;
            }

            if (criteria == null) {
                criteria = new SupplierReportQueryCriteria();
            }

            if (criteria.getMainBusinessId() != null) {
                if (!mainBusinessIds.contains(criteria.getMainBusinessId())) {
                    log.warn("the main business({}) does not belong to user where nick({})", criteria.getMainBusinessId(), employId);
                    result.setResult(EMPTY_SUPPLIER_REPORT_EXPORT_DTO_PAGE);
                    return result;
                }
            }

            //查询属于该登录用户有权限查看的所有供应商编号
            List<Long> userIds = companyMainBusinessDao.findUserIdsByFcIds(criteria.getModuleId(), criteria.getMainBusinessId(), mainBusinessIds);
            if (userIds == null || userIds.isEmpty()) {
                log.warn("no supplier select main business(ids:{})", mainBusinessIds);
                result.setResult(EMPTY_SUPPLIER_REPORT_EXPORT_DTO_PAGE);
                return result;
            }

            //可供货园区筛选
            if (criteria.getSupplyParkId() != null) {
                List<Long> filterBySupplyPark = companySupplyParkDao.findUserIdsBySupplyParkId(criteria.getSupplyParkId(), userIds);
                if (filterBySupplyPark == null || filterBySupplyPark.isEmpty()) {
                    result.setResult(EMPTY_SUPPLIER_REPORT_EXPORT_DTO_PAGE);
                    return result;
                }
                userIds = filterBySupplyPark;
            }

            //策略筛选
            if (criteria.getStrategy() != null) {
                String lastMonth = supplierTQRDCInfoDao.findMaxMonth();
                List<Long> filterByStrategy = supplierTQRDCInfoDao.findUserIdsByMonthAndStrategy(lastMonth, criteria.getStrategy(), userIds);
                if (filterByStrategy == null || filterByStrategy.isEmpty()) {
                    result.setResult(EMPTY_SUPPLIER_REPORT_EXPORT_DTO_PAGE);
                    return result;
                }
                userIds = filterByStrategy;
            }

            PageInfo pageInfo = new PageInfo(pageNo, size);
            Paging<User> userPaging = userDao.findSupplierByCreatedAtAndStepAndIds(criteria.getRegisteredAtStart(), criteria.getRegisteredAtEnd(), criteria.getStep(), userIds, pageInfo.getOffset(), pageInfo.getLimit());

            if (userPaging.getTotal() == 0) {
                result.setResult(EMPTY_SUPPLIER_REPORT_EXPORT_DTO_PAGE);
                return result;
            }

            List<SupplierReportExportDto> supplierReportExportDtos = getSupplierReportExportDtoList(userPaging.getData());

            result.setResult(new Paging<SupplierReportExportDto>(userPaging.getTotal(), supplierReportExportDtos));

        } catch (Exception e) {
            log.error("fail to find detail report where criteria({}),cause:{}", criteria, Throwables.getStackTraceAsString(e));
            result.setError("find.detail.report.fail");
        }

        return result;
    }

    @Override
    public Response<List<SupplierReportExportDto>> findDetailReport(BaseUser baseUser, SupplierReportQueryCriteria criteria) {
        Response<List<SupplierReportExportDto>> result = new Response<List<SupplierReportExportDto>>();

        try {

            String employId = baseUser.getNickName();

            List<Long> mainBusinessIds = mainBusinessApproverDao.findMainBusinessIdsByMemberIdOrLeaderId(employId);
            if (mainBusinessIds == null || mainBusinessIds.isEmpty()) {
                log.error("main business not found where employ id={}", employId);
                result.setError("approved.main.business.not.found");
                return result;
            }

            if (criteria == null) {
                criteria = new SupplierReportQueryCriteria();
            }

            if (criteria.getMainBusinessId() != null) {
                if (!mainBusinessIds.contains(criteria.getMainBusinessId())) {
                    log.warn("the main business({}) does not belong to user where nick({})", criteria.getMainBusinessId(), employId);
                    result.setResult(EMPTY_SUPPLIER_REPORT_EXPORT_DTO_LIST);
                    return result;
                }
            }

            //查询属于该登录用户有权限查看的所有供应商编号
            List<Long> userIds = companyMainBusinessDao.findUserIdsByFcIds(criteria.getModuleId(), criteria.getMainBusinessId(), mainBusinessIds);
            if (userIds == null || userIds.isEmpty()) {
                log.warn("no supplier select main business(ids:{})", mainBusinessIds);
                result.setResult(EMPTY_SUPPLIER_REPORT_EXPORT_DTO_LIST);
                return result;
            }

            //可供货园区筛选
            if (criteria.getSupplyParkId() != null) {
                List<Long> filterBySupplyPark = companySupplyParkDao.findUserIdsBySupplyParkId(criteria.getSupplyParkId(), userIds);
                if (filterBySupplyPark == null || filterBySupplyPark.isEmpty()) {
                    result.setResult(EMPTY_SUPPLIER_REPORT_EXPORT_DTO_LIST);
                    return result;
                }
                userIds = filterBySupplyPark;
            }

            //策略筛选
            if (criteria.getStrategy() != null) {
                String lastMonth = supplierTQRDCInfoDao.findMaxMonth();
                List<Long> filterByStrategy = supplierTQRDCInfoDao.findUserIdsByMonthAndStrategy(lastMonth, criteria.getStrategy(), userIds);
                if (filterByStrategy == null || filterByStrategy.isEmpty()) {
                    result.setResult(EMPTY_SUPPLIER_REPORT_EXPORT_DTO_LIST);
                    return result;
                }
                userIds = filterByStrategy;
            }

            Paging<User> userPaging = userDao.findSupplierByCreatedAtAndStepAndIds(criteria.getRegisteredAtStart(), criteria.getRegisteredAtEnd(), criteria.getStep(), userIds, null, null);

            if (userPaging.getTotal() == 0) {
                result.setResult(EMPTY_SUPPLIER_REPORT_EXPORT_DTO_LIST);
                return result;
            }

            List<SupplierReportExportDto> supplierReportExportDtos = getSupplierReportExportDtoList(userPaging.getData());

            result.setResult(supplierReportExportDtos);

        } catch (Exception e) {
            log.error("fail to find detail report where criteria({}),cause:{}", criteria, Throwables.getStackTraceAsString(e));
            result.setError("find.detail.report.fail");
        }

        return result;
    }

    @Override
    public Response<List<FrontendCategory>> findOwnMainBusinesses(BaseUser baseUser) {
        Response<List<FrontendCategory>> result = new Response<List<FrontendCategory>>();

        try {

            String employId = baseUser.getNickName();

            List<Long> mainBusinessIds = mainBusinessApproverDao.findMainBusinessIdsByMemberIdOrLeaderId(employId);
            if (mainBusinessIds == null || mainBusinessIds.isEmpty()) {
                log.warn("main business not found where employ id={}", employId);
                result.setResult(Collections.<FrontendCategory>emptyList());
                return result;
            }

            Response<List<FrontendCategory>> fcRes = frontendCategoryService.findByIds(mainBusinessIds);
            if (!fcRes.isSuccess()) {
                log.error("fail to find frontend category where id={}", mainBusinessIds);
                result.setError("find.frontend.category.fail");
                return result;
            }

            result.setResult(fcRes.getResult());

        } catch (Exception e) {
            log.error("fail to find own modules where user id={}", baseUser.getId());
            result.setError("find.own.modules.fail");
        }

        return result;
    }

    private List<SupplierReportExportDto> getSupplierReportExportDtoList(List<User> users) {
        List<SupplierReportExportDto> supplierReportExportDtoList = Lists.newArrayList();

        for (User user : users) {

            Long userId = user.getId();

            Company company = companyDao.findByUserId(userId);
            if (company == null) {
                log.warn("company not found where user id={}", userId);
                continue;
            }

            ContactInfo contactInfo = contactInfoDao.findByUserId(userId);
            Finance finance = financeDao.findByUserId(userId);
            CompanyExtraRD companyExtraRD = companyExtraRDDao.findByUserId(userId);
            List<CompanySupplyPark> companySupplyParks = companySupplyParkDao.findByUserId(userId);
            Integer solutionCount = solutionCountService.findSupSolCount(user.getId(), SolutionCountType.SELECT_SOL).get(SolutionCountType.SELECT_SOL);

            SupplierReportExportDto supplierReportExportDto = new SupplierReportExportDto();

            supplierReportExportDto.setCreditQualifyResult(getCreditQualityResult(userId));
            supplierReportExportDto.setAptitudeQualifyResult(getAptitudeQualifyResult(company.getId()));
            supplierReportExportDto.setSelectedSolutionCount(solutionCount);

            if (company.getRegCountry() != null) {
                Response<Address> regCountryRes = addressService.findById(company.getRegCountry());
                if (regCountryRes.isSuccess()) {
                    supplierReportExportDto.setRegCountryName(regCountryRes.getResult().getName());
                }
            }
            supplierReportExportDto.setRegRegion(getRegRegion(company.getRegProvince(), company.getRegCity()));

            supplierReportExportDto.setUser(user);
            supplierReportExportDto.setCompany(company);
            supplierReportExportDto.setContactInfo(contactInfo != null ? contactInfo : new ContactInfo());
            supplierReportExportDto.setFinance(finance != null ? finance : new Finance());
            supplierReportExportDto.setCompanyExtraRD(companyExtraRD != null ? companyExtraRD : new CompanyExtraRD());
            supplierReportExportDto.setCompanySupplyParks(companySupplyParks != null ? companySupplyParks : Collections.<CompanySupplyPark>emptyList());

            supplierReportExportDtoList.add(supplierReportExportDto);
        }

        return supplierReportExportDtoList;
    }

    /**
     * 获取等级验证结果
     */
    private String getCreditQualityResult(Long userId) {
        SupplierCreditQualify param = new SupplierCreditQualify();
        param.setUserId(userId);
        SupplierCreditQualify supplierCreditQualify = supplierCreditQualifyDao.findBy(param);

        if (supplierCreditQualify == null || supplierCreditQualify.isApplying()) {
            return "未进行";
        }
        return supplierCreditQualify.isCreditQualified() ? "通过" : "未通过";
    }

    /**
     * 获取资质验证结果（审核通过的类目，多个类目之间逗号分割）
     */
    private String getAptitudeQualifyResult(Long companyId) {
        Response<List<Long>> resourceMaterialRes = supplierResourceMaterialService.getApprovedBcIds(companyId);

        List<BackendCategory> approvedBcs = null;
        if (resourceMaterialRes.isSuccess()) {
            List<Long> approvedBcIds = resourceMaterialRes.getResult();
            if (approvedBcIds != null && !approvedBcIds.isEmpty()) {
                Response<List<BackendCategory>> bcRes = backendCategoryService.findByIds(approvedBcIds);
                if (bcRes.isSuccess()) {
                    approvedBcs = bcRes.getResult();
                }
            }
        }

        List<String> approvedBcNames = null;
        if (approvedBcs != null) {
            approvedBcNames = Lists.transform(approvedBcs, new Function<BackendCategory, String>() {
                public String apply(BackendCategory backendCategory) {
                    return backendCategory.getName();
                }
            });
        }

        return approvedBcNames == null ? "" : Joiners.COMMA.join(approvedBcNames);
    }

    /**
     * 获取隶属区域（所在省+所在市）
     *
     * @param provinceId 省份编号
     * @param cityId     市编号
     * @return 隶属区域（所在省+所在市）
     */
    private String getRegRegion(Integer provinceId, Integer cityId) {
        String regProvinceName = null;
        if (provinceId != null) {
            Response<Address> regProvinceRes = addressService.findById(provinceId);
            if (regProvinceRes.isSuccess()) {
                regProvinceName = regProvinceRes.getResult().getName();
            }
        }

        String regCityName = null;
        if (cityId != null) {
            Response<Address> regCityRes = addressService.findById(cityId);
            if (regCityRes.isSuccess()) {
                regCityName = regCityRes.getResult().getName();
            }
        }

        if (!Strings.isNullOrEmpty(regProvinceName) && !Strings.isNullOrEmpty(regCityName)) {
            return (regProvinceName + " " + regCityName);
        }

        return "";
    }

}
