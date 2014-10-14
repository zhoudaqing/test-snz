package io.terminus.snz.user.manager;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import io.terminus.common.exception.ServiceException;
import io.terminus.common.utils.BeanMapper;
import io.terminus.pampas.common.Response;
import io.terminus.snz.category.model.FrontendCategory;
import io.terminus.snz.category.service.FrontendCategoryService;
import io.terminus.snz.user.dao.*;
import io.terminus.snz.user.dao.redis.*;
import io.terminus.snz.user.dto.*;
import io.terminus.snz.user.event.ApproveEvent;
import io.terminus.snz.user.model.*;
import io.terminus.snz.user.service.*;
import io.terminus.snz.user.tool.ChangedInfoKeys;
import io.terminus.snz.user.tool.HaierCasHttpUtil;
import io.terminus.snz.user.tool.PasswordUtil;
import io.terminus.snz.user.tool.StashSupplierInfoKeys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkState;
import static io.terminus.common.utils.Arguments.isNull;

/**
 * 用户管理
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-7-5
 */
@Slf4j
public class AccountManager {
    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CompanyMainBusinessDao companyMainBusinessDao;

    @Autowired
    private FinanceDao financeDao;

    @Autowired
    private AdditionalDocDao additionalDocDao;

    @Autowired
    private SupplierTQRDCInfoDao supplierTQRDCInfoDao;

    @Autowired
    private ContactInfoDao contactInfoDao;

    @Autowired
    private CompanyRankDao companyRankDao;

    @Autowired
    private CompanySupplyParkDao companySupplyParkDao;

    @Autowired
    private PurchaserExtraDao purchaserExtraDao;

    @Autowired
    private TagService tagService;

    @Autowired
    private MainBusinessApproverDao mainBusinessApproverDao;

    @Autowired
    private FrontendCategoryService frontendCategoryService;

    @Autowired
    private SupplierApproveLogDao supplierApproveLogDao;

    @Autowired
    private SupplierChangedInfoRedisDao supplierChangedInfoRedisDao;

    @Autowired
    private SupplierInfoChangedService supplierInfoChangedService;

    @Autowired
    private SupplierCreditQualifyService supplierCreditQualifyService;

    @Autowired
    private SupplierResourceMaterialService supplierResourceMaterialService;

    @Autowired
    private CompanyExtraQualityDao companyExtraQualityDao;

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
    private CompanyExtraRDRedisDao companyExtraRDRedisDao;

    @Autowired
    private CompanyExtraResponseRedisDao companyExtraResponseRedisDao;

    @Autowired
    private CompanyExtraQualityRedisDao companyExtraQualityRedisDao;

    @Autowired
    private CompanyExtraRDDao companyExtraRDDao;

    @Autowired
    private CompanyExtraResponseDao companyExtraResponseDao;

    @Autowired
    private CompanyExtraDeliveryDao companyExtraDeliveryDao;

    @Autowired
    private CompanyExtraDeliveryRedisDao companyExtraDeliveryRedisDao;

    /**
     * 供应商入驻
     *
     * @param richSupplierDto 供应商信息
     * @return 生成的用户编号
     */
    @Transactional
    public Long createSupplier(RichSupplierDto richSupplierDto) {
        User user = richSupplierDto.getUser();
        user.setName(user.getNick());
        Company company = richSupplierDto.getCompany();
        ContactInfo contactInfo = richSupplierDto.getContactInfo();
        List<CompanyMainBusiness> companyMainBusinesses = richSupplierDto.getCompanyMainBusinesses();
        List<CompanySupplyPark> companySupplyParks = richSupplierDto.getCompanySupplyParks();

        List<Long> supplyParkIds = Lists.newArrayList();

        //加密密码
        user.setEncryptedPassword(PasswordUtil.encryptPassword(user.getEncryptedPassword()));
        //创建用户
        Long userId = userDao.create(user);

        //创建企业基本信息
        company.setUserId(userId);
        Long companyId = companyDao.create(company);

        //创建联系人信息
        if (contactInfo != null) {
            contactInfo.setUserId(userId);
            contactInfo.setCompanyId(companyId);
            contactInfo.setEmail(user.getEmail());
            contactInfo.setMobile(user.getMobile());
            contactInfoDao.create(contactInfo);
        }

        //创建主营业务信息
        if (companyMainBusinesses != null && !companyMainBusinesses.isEmpty()) {
            for (CompanyMainBusiness companyMainBusiness : companyMainBusinesses) {
                companyMainBusiness.setUserId(userId);
                companyMainBusiness.setCompanyId(companyId);
                companyMainBusinessDao.create(companyMainBusiness);
            }
        }

        //创建可供货园区信息
        if (companySupplyParks != null && !companySupplyParks.isEmpty()) {
            for (CompanySupplyPark companySupplyPark : companySupplyParks) {
                companySupplyPark.setUserId(userId);
                companySupplyPark.setCompanyId(companyId);
                companySupplyParkDao.create(companySupplyPark);

                supplyParkIds.add(companySupplyPark.getSupplyParkId());
            }
        }

        return userId;

    }

    /**
     * 更新企业基本信息
     *
     * @param companyDto 要更新的信息
     */
    @Transactional
    public void updateCompany(CompanyDto companyDto, Company existedCompany) {

        Long userId = existedCompany.getUserId();
        Company company = companyDto.getCompany();

        User user = userDao.findById(userId);

        boolean isChanged = false;
        Map<String, String> changedInfo = null;

        boolean needCheckChanged = supplierInfoChangedService.needCheckChanged(user.getApproveStatus());
        if (needCheckChanged) {

            List<CompanyMainBusiness> existedMainBusinesses = companyMainBusinessDao.findByUserId(userId);
            List<CompanySupplyPark> existedSupplyParks = companySupplyParkDao.findByUserId(userId);

            SupplierInfoChangedDto companyChangedDto = supplierInfoChangedService.baseCompanyChanged(userId, companyDto, existedCompany, existedMainBusinesses, existedSupplyParks);

            if (companyChangedDto.isChanged()) {
                isChanged = true;
                changedInfo = companyChangedDto.getChangedInfo();
                supplierInfoChangedService.updateModifyInfoWaitingForApprove(userId, user.getApproveStatus());

                //如果修改了主营业务，则放到临时表，供审核查询时使用
                if (companyChangedDto.getChangedInfo().containsKey(ChangedInfoKeys.companyMainBusiness())) {

                    companyMainBusinessTmpDao.deleteByUserId(userId);
                    List<CompanyMainBusiness> companyMainBusinesses = companyDto.getCompanyMainBusinesses();
                    for (CompanyMainBusiness companyMainBusiness : companyMainBusinesses) {
                        CompanyMainBusinessTmp companyMainBusinessTmp = new CompanyMainBusinessTmp();
                        BeanMapper.copy(companyMainBusiness, companyMainBusinessTmp);
                        companyMainBusinessTmp.setUserId(userId);
                        companyMainBusinessTmp.setCompanyId(company.getId());
                        companyMainBusinessTmpDao.create(companyMainBusinessTmp);
                    }
                }
            }

        } else {
            List<CompanyMainBusiness> companyMainBusinesses = companyDto.getCompanyMainBusinesses();

            if (companyMainBusinesses != null && !companyMainBusinesses.isEmpty()) {
                companyMainBusinessDao.deleteByUserId(userId);
                for (CompanyMainBusiness companyMainBusiness : companyMainBusinesses) {
                    companyMainBusiness.setUserId(userId);
                    companyMainBusiness.setCompanyId(company.getId());
                    companyMainBusinessDao.create(companyMainBusiness);
                }
            }

            List<CompanySupplyPark> companySupplyParks = companyDto.getCompanySupplyParks();
            if (companySupplyParks != null && !companySupplyParks.isEmpty()) {
                companySupplyParkDao.deleteByUserId(userId);
                for (CompanySupplyPark companySupplyPark : companySupplyParks) {
                    companySupplyPark.setUserId(userId);
                    companySupplyPark.setCompanyId(company.getId());
                    companySupplyParkDao.create(companySupplyPark);
                }
            }
        }

        companyDao.update(company);

        //更新标签
        tagService.updateTagsByCompany(userId, company);

        if (isChanged) {
            supplierChangedInfoRedisDao.addChangedTab(userId, ChangedInfoKeys.companyTab());
            supplierChangedInfoRedisDao.addChangedInfos(userId, changedInfo);
        }

        //删除暂存信息
        companyRedisDao.removeBaseCompanyInfo(userId);

    }

    @Transactional
    public void updateContactInfo(Long userId, ContactInfo updatedContactInfo, ContactInfo oldContactInfo) {

        User user = userDao.findById(userId);

        boolean isChanged = false;
        Map<String, String> changedInfo = null;

        boolean needCheckChanged = supplierInfoChangedService.needCheckChanged(user.getApproveStatus());
        if (needCheckChanged) {

            SupplierInfoChangedDto supplierInfoChangedDto = supplierInfoChangedService.contactInfoChanged(userId, updatedContactInfo, oldContactInfo);
            if (supplierInfoChangedDto.isChanged()) {
                isChanged = true;
                changedInfo = supplierInfoChangedDto.getChangedInfo();
                supplierInfoChangedService.updateModifyInfoWaitingForApprove(userId, user.getApproveStatus());
            }
        }

        updatedContactInfo.setId(oldContactInfo.getId());
        contactInfoDao.update(updatedContactInfo);

        if (isChanged) {
            supplierChangedInfoRedisDao.addChangedTab(userId, ChangedInfoKeys.contactInfoTab());
            supplierChangedInfoRedisDao.addChangedInfos(userId, changedInfo);
        }

        //删除暂存信息
        contactInfoRedisDao.remove(StashSupplierInfoKeys.contactInfo(userId));

    }

    @Transactional
    public ApproveEvent approveSupplier(SupplierApproveDto supplierApproveDto) {
        ApproveEvent approveEvent = doApproveSupplier(supplierApproveDto);

        Long userId = supplierApproveDto.getUserId();

        switch (approveEvent.getApproveResult()) {
            case MODIFY_INFO_OK: //修改信息审核通过，更新供应商信息
                updateSupplierInfo(userId);
                removeChangedInfo(userId);
                break;
            case MODIFY_INFO_FAIL:
                removeChangedInfo(userId);
                break;
            default:
                return approveEvent;

        }

        return approveEvent;
    }

    private ApproveEvent doApproveSupplier(SupplierApproveDto supplierApproveDto) {

        Long userId = supplierApproveDto.getUserId();
        Integer operation = supplierApproveDto.getOperation();
        Integer approveStatus = supplierApproveDto.getApproveStatus();

        User updateUser = new User();
        updateUser.setId(userId);

        SupplierApproveLog supplierApproveLog = new SupplierApproveLog();
        supplierApproveLog.setUserId(userId);
        supplierApproveLog.setApproverId(supplierApproveDto.getApproverId());
        supplierApproveLog.setApproverName(supplierApproveDto.getApproverName());
        supplierApproveLog.setApprovedAt(new Date());
        supplierApproveLog.setDescription(supplierApproveDto.getDescription());

        ApproveEvent approveEvent = new ApproveEvent();

        if (Objects.equal(operation, 1)) {//审核通过

            updateUser.setApproveStatus(User.ApproveStatus.OK.value());
            supplierApproveLog.setApproveResult(SupplierApproveLog.ApproveResult.OK.value());

            if (Objects.equal(approveStatus, User.ApproveStatus.ENTER_WAITING_FOR_APPROVE.value())) {//入驻审核
                updateUser.setEnterPassAt(new Date());
                approveEvent.setApproveResult(ApproveEvent.ApproveResult.ENTER_OK);

                tagService.addSupplierStatusTag(userId, User.SupplierTag.REGISTER_SUPPLIER);

                //一流资源确认
                Company company = companyDao.findByUserId(userId);
                doConfirmResource(supplierApproveDto.getApproverId(), userId, company.getId(), supplierApproveDto.getResourceType(), supplierApproveDto.getCompetitors());

            } else {//修改信息审核
                approveEvent.setApproveResult(ApproveEvent.ApproveResult.MODIFY_INFO_OK);
            }

        } else if (Objects.equal(operation, 2)) {//审核不通过

            updateUser.setRefuseStatus(User.RefuseStatus.IS_REFUSED.value());
            supplierApproveLog.setApproveResult(SupplierApproveLog.ApproveResult.FAIL.value());

            if (Objects.equal(approveStatus, User.ApproveStatus.ENTER_WAITING_FOR_APPROVE.value())) {//入驻审核
                updateUser.setApproveStatus(User.ApproveStatus.ENTER_FAIL.value());
                approveEvent.setApproveResult(ApproveEvent.ApproveResult.ENTER_FAIL);
            } else {//修改信息审核
                updateUser.setApproveStatus(User.ApproveStatus.MODIFY_INFO_FAIL.value());
                approveEvent.setApproveResult(ApproveEvent.ApproveResult.MODIFY_INFO_FAIL);
            }

        } else {//驳回7天审核通过的供应商
            updateUser.setApproveStatus(User.ApproveStatus.ENTER_FAIL.value());
            supplierApproveLog.setApproveResult(SupplierApproveLog.ApproveResult.FAIL.value());
        }


        if (Objects.equal(approveStatus, User.ApproveStatus.ENTER_WAITING_FOR_APPROVE.value()) || Objects.equal(operation, 3)) {
            supplierApproveLog.setApproveType(SupplierApproveLog.ApproveType.ENTER.value());
        } else {
            supplierApproveLog.setApproveType(SupplierApproveLog.ApproveType.MODIFY_INFO.value());
        }

        //更改审核状态
        userDao.update(updateUser);

        //添加审核记录
        supplierApproveLogDao.create(supplierApproveLog);

        return approveEvent;
    }

    @Transactional
    public void confirmResource(Long approverId, Long userId, Long companyId, Integer resourceType, String competitors) {
        doConfirmResource(approverId, userId, companyId, resourceType, competitors);
    }

    private void doConfirmResource(Long approverId, Long userId, Long companyId, Integer resourceType, String competitors) {

        //500强、标杆企业直接通过资质验证和信息
        if (Objects.equal(resourceType, Company.ResourceType.WORLD_TOP.value())
                || Objects.equal(resourceType, Company.ResourceType.BENCH_MARK.value())) {

            Response<Boolean> resourceMaterialRes = supplierResourceMaterialService.forceApproveAll(approverId, companyId);
            if (!resourceMaterialRes.isSuccess()) {
                log.error("fail to force approve all aptitude where user id={}", userId);
                throw new ServiceException(resourceMaterialRes.getError());
            }

            Response<SupplierCreditQualify> creditQualifyRes = supplierCreditQualifyService.findCreditQualifyByUserId(userId);
            if (creditQualifyRes.isSuccess()) {
                SupplierCreditQualify supplierCreditQualify = creditQualifyRes.getResult();
                if (!supplierCreditQualify.isCreditQualified()) {
                    Response<Boolean> sysCreditQualifyRes = supplierCreditQualifyService.systemApplyCreditQualify(userId, null, SupplierCreditQualify.STATUS.AUTO.toValue());
                    if (!sysCreditQualifyRes.isSuccess()) {
                        log.error("fail to system apply credit qualify where user id={}", userId);
                        throw new ServiceException(sysCreditQualifyRes.getError());
                    }
                }
            }

        }

        Company updatedCompany = new Company();
        updatedCompany.setId(companyId);
        updatedCompany.setResourceType(resourceType);

        if (Objects.equal(resourceType, Company.ResourceType.BENCH_MARK.value())) {
            updatedCompany.setCompetitors(competitors);
        }

        companyDao.update(updatedCompany);
    }

    @Transactional
    public void addSupplierCodeAndTag(Long userId, Long companyId, String supplierCode) {
        Company updated = new Company();
        updated.setId(companyId);
        updated.setSupplierCode(supplierCode);
        companyDao.update(updated);

        tagService.addSupplierStatusTag(userId, User.SupplierTag.ALTERNATIVE);
    }

    /**
     * 更新三证
     *
     * @param userId           用户编号
     * @param updatedPaperwork 待更新的三证信息
     * @param oldPaperwork     原三证信息
     */
    @Transactional
    public void updatePaperwork(Long userId, PaperworkDto updatedPaperwork, Company oldPaperwork) {

        User user = userDao.findById(userId);

        if (Objects.equal(user.getApproveStatus(), User.ApproveStatus.INIT.value())
                || Objects.equal(user.getApproveStatus(), User.ApproveStatus.ENTER_WAITING_FOR_APPROVE.value())
                || Objects.equal(user.getApproveStatus(), User.ApproveStatus.ENTER_FAIL.value())) {

            oldPaperwork.setBusinessLicense(updatedPaperwork.getBusinessLicense());
            oldPaperwork.setBusinessLicenseId(updatedPaperwork.getBusinessLicenseId());
            oldPaperwork.setBlDate(updatedPaperwork.getBlDate());
            oldPaperwork.setOrgCert(updatedPaperwork.getOrgCert());
            oldPaperwork.setOrgCertId(updatedPaperwork.getOrgCertId());
            oldPaperwork.setOcDate(updatedPaperwork.getOcDate());
            oldPaperwork.setTaxNo(updatedPaperwork.getTaxNo());
            oldPaperwork.setTaxNoId(updatedPaperwork.getTaxNoId());

            companyDao.update(oldPaperwork);

            //触发入驻审核
            if (Objects.equal(user.getApproveStatus(), User.ApproveStatus.INIT.value())) {
                User updatedUser = new User();
                updatedUser.setId(userId);
                updatedUser.setApproveStatus(User.ApproveStatus.ENTER_WAITING_FOR_APPROVE.value());
                updatedUser.setLastSubmitApprovalAt(new Date());
                userDao.update(updatedUser);
            }

            return;
        }

        SupplierInfoChangedDto supplierInfoChangedDto = supplierInfoChangedService.paperworkChanged(userId, updatedPaperwork, oldPaperwork);

        if (supplierInfoChangedDto.isChanged()) {
            supplierInfoChangedService.updateModifyInfoWaitingForApprove(userId, user.getApproveStatus());
            supplierChangedInfoRedisDao.addChangedTab(userId, ChangedInfoKeys.paperworkTab());
            supplierChangedInfoRedisDao.addChangedInfos(userId, supplierInfoChangedDto.getChangedInfo());
        }

        //删除暂存信息
        paperworkRedisDao.remove(StashSupplierInfoKeys.paperwork(userId));
    }

    /**
     * 创建或更新质量保证信息
     *
     * @param userId         用户编号
     * @param updatedQuality 待更新的质量保证信息
     */
    @Transactional
    public void createOrUpdateCompanyExtraQuality(Long userId, CompanyExtraQuality updatedQuality) {

        CompanyExtraQuality oldQuality = companyExtraQualityDao.findByUserId(userId);
        if (oldQuality == null) {
            companyExtraQualityDao.create(updatedQuality);
            companyExtraQualityRedisDao.remove(StashSupplierInfoKeys.quality(userId));
            return;
        }

        boolean isChanged = false;
        Map<String, String> changedInfo = null;

        Company company = companyDao.findByUserId(userId);

        //如果有V码，则检查合同信息是否修改
        if (!Strings.isNullOrEmpty(company.getSupplierCode())) {
            SupplierInfoChangedDto supplierInfoChangedDto = supplierInfoChangedService.qualityChanged(userId, updatedQuality, oldQuality);
            if (supplierInfoChangedDto.isChanged()) {
                isChanged = true;
                changedInfo = supplierInfoChangedDto.getChangedInfo();
                User user = userDao.findById(userId);
                supplierInfoChangedService.updateModifyInfoWaitingForApprove(userId, user.getApproveStatus());
            }
        }
        companyExtraQualityDao.update(updatedQuality);

        if (isChanged) {
            supplierChangedInfoRedisDao.addChangedTab(userId, ChangedInfoKeys.qualityTab());
            supplierChangedInfoRedisDao.addChangedInfos(userId, changedInfo);
        }

        companyExtraQualityRedisDao.remove(StashSupplierInfoKeys.quality(userId));
    }

    /**
     * 创建或更新公司财务信息
     *
     * @param financeDto 财务信息
     */
    @Transactional
    public void createOrUpdateFinance(FinanceDto financeDto) {

        Long userId = financeDto.getFinance().getUserId();
        Finance existed = financeDao.findByUserId(userId);

        boolean isChanged = false;
        Map<String, String> changedInfo = null;

        if (existed != null) {
            Finance updatedFinance = financeDto.getFinance();
            updatedFinance.setId(existed.getId());

            Company company = companyDao.findByUserId(userId);
            //如果有V码，则检查合同修改是否修改
            if (!Strings.isNullOrEmpty(company.getSupplierCode())) {
                SupplierInfoChangedDto supplierInfoChangedDto = supplierInfoChangedService.financeChanged(userId, updatedFinance, existed);
                if (supplierInfoChangedDto.isChanged()) {
                    isChanged = true;
                    changedInfo = supplierInfoChangedDto.getChangedInfo();
                    User user = userDao.findById(userId);
                    supplierInfoChangedService.updateModifyInfoWaitingForApprove(userId, user.getApproveStatus());
                }
            }

            financeDao.update(updatedFinance);
        } else {
            Company company = companyDao.findByUserId(financeDto.getFinance().getUserId());
            financeDto.getFinance().setCompanyId(company.getId());
            financeDao.create(financeDto.getFinance());
        }

        if (isChanged) {
            supplierChangedInfoRedisDao.addChangedTab(userId, ChangedInfoKeys.financeTab());
            supplierChangedInfoRedisDao.addChangedInfos(userId, changedInfo);
        }

        //删除暂存信息
        financeRedisDao.remove(StashSupplierInfoKeys.finance(userId));
    }

    @Transactional
    public void createOrUpdateCompanyRank(Long userId, CompanyRank companyRank) {

        CompanyRank existed = companyRankDao.findByUserId(userId);

        if (existed != null) {
            companyRank.setId(existed.getId());
            companyRankDao.update(companyRank);
        } else {
            Company company = companyDao.findByUserId(userId);

            companyRank.setUserId(userId);
            companyRank.setCompanyId(company.getId());
            companyRankDao.create(companyRank);
        }

        //删除暂存信息
        companyRankRedisDao.remove(StashSupplierInfoKeys.companyRank(userId));
    }

    @Transactional
    public void createOrUpdateRD(Long userId, CompanyExtraRD companyExtraRD) {
        if (companyExtraRDDao.findByUserId(userId) == null) {
            companyExtraRDDao.create(companyExtraRD);
        } else {
            companyExtraRDDao.update(companyExtraRD);
        }

        //删除暂存信息
        companyExtraRDRedisDao.remove(StashSupplierInfoKeys.RD(userId));
    }

    @Transactional
    public void createOrUpdateResponse(Long userId, CompanyExtraResponse companyExtraResponse) {
        if (companyExtraResponseDao.findByUserId(userId) == null) {
            companyExtraResponseDao.create(companyExtraResponse);
        } else {
            companyExtraResponseDao.update(companyExtraResponse);
        }

        //删除暂存信息
        companyExtraResponseRedisDao.remove(StashSupplierInfoKeys.response(userId));
    }

    @Transactional
    public void createOrUpdateDelivery(Long userId, CompanyExtraDelivery companyExtraDelivery) {
        if (companyExtraDeliveryDao.findByUserId(userId) == null) {
            companyExtraDeliveryDao.create(companyExtraDelivery);
        } else {
            companyExtraDeliveryDao.update(companyExtraDelivery);
        }

        //删除暂存信息
        companyExtraDeliveryRedisDao.remove(StashSupplierInfoKeys.delivery(userId));
    }

    /**
     * 批量插入供应商的TQRDC信息
     *
     * @param supplierTQRDCInfos 供应商的TQRDC信息
     */
    @Transactional
    public void bulkCreateSupplierQTRDCInfo(List<SupplierTQRDCInfo> supplierTQRDCInfos) {
        if (supplierTQRDCInfos == null) {
            return;
        }
        for (SupplierTQRDCInfo supplierTQRDCInfo : supplierTQRDCInfos) {
            setUserIdAndCompanyId(supplierTQRDCInfo);//关联user表,冗余company id

            if (supplierTQRDCInfo.getUserId() == null) {
                continue;
            }

            supplierTQRDCInfoDao.create(supplierTQRDCInfo);

            //如果数据库中匹配到用户，则为用户打tqrdc tag(这里应该是为的最新月份的数据)
            if ("2014-04".equals(supplierTQRDCInfo.getMonth())) {
                tagService.addTQRDCTag(supplierTQRDCInfo.getUserId(), supplierTQRDCInfo.getCompositeScore());
            }


        }

    }

    /**
     * 批量更新供应商的TQRDC信息
     *
     * @param supplierTQRDCInfos 供应商的TQRDC信息
     */
    @Transactional
    public void bulkUpdateSupplierQTRDCInfo(List<SupplierTQRDCInfo> supplierTQRDCInfos) {
        if (supplierTQRDCInfos == null) {
            return;
        }
        for (SupplierTQRDCInfo supplierTQRDCInfo : supplierTQRDCInfos) {
            supplierTQRDCInfoDao.update(supplierTQRDCInfo);
        }
    }

    private void setUserIdAndCompanyId(SupplierTQRDCInfo supplierTQRDCInfo) {

        List<Company> companies = companyDao.findByCorporation(supplierTQRDCInfo.getSupplierName());
        if (companies != null && !companies.isEmpty()) {
            Company company = companies.get(0);
            supplierTQRDCInfo.setUserId(company.getUserId());
            supplierTQRDCInfo.setCompanyId(company.getId());
        }

    }

    /**
     * 批量插入供应商
     *
     * @param supplierImportDtos 供应商
     * @return 插入的供应商数量
     */
    @Transactional
    public Integer bulkCreateSupplier(List<SupplierImportDto> supplierImportDtos) {
        if (supplierImportDtos == null) {
            return 0;
        }

        int count = 0;

        //这里预先为供应商插入一条主营业务信息(暂时写死)
        CompanyMainBusiness companyMainBusiness = new CompanyMainBusiness();
        companyMainBusiness.setFirstLevelId(1L);
        companyMainBusiness.setMainBusinessId(463L);
        companyMainBusiness.setName("冰压");

        for (SupplierImportDto supplierImportDto : supplierImportDtos) {
            User user = supplierImportDto.getUser();
            Company company = supplierImportDto.getCompany();
            CompanyRank companyRank = supplierImportDto.getCompanyRank();
            ContactInfo contactInfo = supplierImportDto.getContactInfo();

            if (userExisted(user.getNick())) {
                log.warn("user existed where nick={},", user.getNick());
                continue;
            }

            if (Strings.isNullOrEmpty(user.getEmail())) {
                log.warn("email is empty where nick={},", user.getNick());
                continue;
            }

            user.setEncryptedPassword(PasswordUtil.encryptPassword(user.getEncryptedPassword()));
            Long userId = userDao.create(user);

            company.setUserId(userId);
            Long companyId = companyDao.create(company);

            companyRank.setUserId(userId);
            companyRank.setCompanyId(companyId);
            companyRankDao.create(companyRank);

            contactInfo.setUserId(userId);
            contactInfo.setCompanyId(companyId);
            contactInfoDao.create(contactInfo);

            companyMainBusiness.setUserId(userId);
            companyMainBusiness.setCompanyId(companyId);
            companyMainBusinessDao.create(companyMainBusiness);

            //插入相关标签
            tagService.updateTagsByCompany(userId, company);

            HaierCasHttpUtil.register(user.getNick(), user.getEmail(), "123456");

            count++;

        }
        return count;
    }

    @Transactional
    public Integer bulkCreateMainBusinessApprover(List<MainBusinessApprover> mainBusinessApprovers) {
        if (mainBusinessApprovers == null || mainBusinessApprovers.isEmpty()) {
            return 0;
        }

        int count = 0;

        for (MainBusinessApprover mainBusinessApprover : mainBusinessApprovers) {
            Response<FrontendCategory> frontendCategoryRes = frontendCategoryService.findFrontendCategoryByLevelAndName(3, mainBusinessApprover.getMainBusinessName());
            if (!frontendCategoryRes.isSuccess()) {
                log.error("main business not found where name={}", mainBusinessApprover.getMainBusinessName());
                continue;
            }
            mainBusinessApprover.setMainBusinessId(frontendCategoryRes.getResult().getId());
            mainBusinessApproverDao.create(mainBusinessApprover);

            count++;
        }

        return count;

    }

    @Transactional
    public void createPurcharser(PurchaserDto purchaserDto) {
        bulkCreatePurchaser(Arrays.asList(purchaserDto));
    }

    /**
     * 批量插入采购商
     *
     * @param purchaserDtos 采购商信息
     */
    @Transactional
    public void bulkCreatePurchaser(List<PurchaserDto> purchaserDtos) {
        if (purchaserDtos == null) {
            return;
        }
        for (PurchaserDto purchaserDto : purchaserDtos) {
            Long userId = userDao.create(purchaserDto.getUser());
            purchaserDto.getPurchaserExtra().setUserId(userId);
            purchaserExtraDao.create(purchaserDto.getPurchaserExtra());
        }
    }

    private void removeChangedInfo(Long userId) {
        companyMainBusinessTmpDao.deleteByUserId(userId);
        supplierChangedInfoRedisDao.removeChangedInfos(userId);
        supplierChangedInfoRedisDao.removeChangedTabs(userId);
    }

    private void updateSupplierInfo(Long userId) {
        synChangedBaseCompanyInfo(userId);
        synPaperwork(userId);
        synContactInfo(userId);
        synCompanyExtraQuality(userId);
        synFinance(userId);
    }

    private void synChangedBaseCompanyInfo(Long userId) {

        boolean baseCompanyChanged = supplierChangedInfoRedisDao.tabInfoChanged(userId, ChangedInfoKeys.companyTab());

        if (baseCompanyChanged) {
            Company existedCompany = companyDao.findByUserId(userId);

            CompanyDto companyDto = supplierInfoChangedService.getNewBaseCompany(userId, existedCompany, null, null).getSupplierInfo();

            Company company = companyDto.getCompany();
            List<CompanyMainBusiness> companyMainBusinesses = companyDto.getCompanyMainBusinesses();
            List<CompanySupplyPark> companySupplyParks = companyDto.getCompanySupplyParks();

            companyDao.update(company);

            //更新标签
            tagService.updateTagsByCompany(userId, company);

            if (companyMainBusinesses != null && !companyMainBusinesses.isEmpty()) {
                companyMainBusinessDao.deleteByUserId(userId);
                for (CompanyMainBusiness companyMainBusiness : companyMainBusinesses) {
                    companyMainBusiness.setUserId(userId);
                    companyMainBusiness.setCompanyId(company.getId());
                    companyMainBusinessDao.create(companyMainBusiness);
                }
            }

            if (companySupplyParks != null && !companySupplyParks.isEmpty()) {
                companySupplyParkDao.deleteByUserId(userId);
                for (CompanySupplyPark companySupplyPark : companySupplyParks) {
                    companySupplyPark.setUserId(userId);
                    companySupplyPark.setCompanyId(company.getId());
                    companySupplyParkDao.create(companySupplyPark);
                }
            }

        }
    }

    private void synPaperwork(Long userId) {
        boolean paperworkChanged = supplierChangedInfoRedisDao.tabInfoChanged(userId, ChangedInfoKeys.paperworkTab());

        if (paperworkChanged) {
            Company existed = companyDao.findByUserId(userId);
            PaperworkDto newPaperwork = supplierInfoChangedService.getNewPaperwork(userId, existed).getSupplierInfo();
            BeanMapper.copy(newPaperwork, existed);
            companyDao.update(existed);
        }
    }

    private void synContactInfo(Long userId) {
        boolean contactInfoChanged = supplierChangedInfoRedisDao.tabInfoChanged(userId, ChangedInfoKeys.contactInfoTab());
        if (contactInfoChanged) {
            ContactInfo existedContactInfo = contactInfoDao.findByUserId(userId);
            ContactInfo newContactInfo = supplierInfoChangedService.getNewContactInfo(userId, existedContactInfo).getSupplierInfo();
            contactInfoDao.update(newContactInfo);
        }
    }

    private void synCompanyExtraQuality(Long userId) {
        boolean qualityChanged = supplierChangedInfoRedisDao.tabInfoChanged(userId, ChangedInfoKeys.qualityTab());
        if (qualityChanged) {
            CompanyExtraQuality companyExtraQuality = new CompanyExtraQuality();
            companyExtraQuality.setUserId(userId);
            CompanyExtraQuality newQuality = supplierInfoChangedService.getNewCompanyExtraQuality(userId, companyExtraQuality).getSupplierInfo();
            companyExtraQualityDao.update(newQuality);
        }
    }

    private void synFinance(Long userId) {
        boolean financeChanged = supplierChangedInfoRedisDao.tabInfoChanged(userId, ChangedInfoKeys.financeTab());
        if (financeChanged) {
            Finance existedFinance = financeDao.findByUserId(userId);
            Finance newFinance = supplierInfoChangedService.getNewFinance(userId, existedFinance).getSupplierInfo();
            financeDao.update(newFinance);
        }
    }

    /**
     * 检查用户是否已经存在
     *
     * @param nick 昵称
     * @return 是否存在
     */
    private boolean userExisted(String nick) {
        User user = userDao.findByNick(nick);
        return user != null;
    }

    @Transactional
    public void changePasswordForSupplier(User user, String oldPassword, String newPassword) {
        if (isNull(user)) {
            log.error("user not be null");
            throw new RuntimeException();
        }

        User updated = new User();
        updated.setId(user.getId());
        updated.setEncryptedPassword(PasswordUtil.encryptPassword(newPassword));

        userDao.update(updated);

        String casResult = HaierCasHttpUtil.changePassword(user.getNick(), oldPassword, newPassword);
        checkState(Objects.equal(casResult, "ok"), casResult);
    }

    @Transactional
    public void setInfoCompleteAndAddTag(Long userId, Long companyId) {
        Company updatedCompany = new Company();
        updatedCompany.setId(companyId);
        updatedCompany.setIsComplete(1);
        companyDao.update(updatedCompany);

        //打完善信息标签
        tagService.addSupplierStatusTag(userId, User.SupplierTag.COMPLETE_SUPPLIER);

    }

}
