package io.terminus.snz.requirement.service;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.haier.OuterSysVendorInfoToMDM.RSPVENDORBANKTYPE;
import com.haier.OuterSysVendorInfoToMDM.RSPVENDORCOMPANYTYPE;
import com.haier.OuterSysVendorInfoToMDM.RSPVENDORPURTYPE;
import io.terminus.common.model.PageInfo;
import io.terminus.common.model.Paging;
import io.terminus.common.utils.Arguments;
import io.terminus.common.utils.BeanMapper;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.eai.dto.MDMVendorCodeApply;
import io.terminus.snz.eai.model.MDMConfigure;
import io.terminus.snz.eai.service.MdmConfigureService;
import io.terminus.snz.eai.service.MdmVendorCodeService;
import io.terminus.snz.requirement.dao.SupplierQualificationDao;
import io.terminus.snz.requirement.dto.MDMBaseCompanyDto;
import io.terminus.snz.requirement.model.Requirement;
import io.terminus.snz.requirement.model.RequirementRank;
import io.terminus.snz.requirement.model.RequirementStatus;
import io.terminus.snz.requirement.model.SupplierQualification;
import io.terminus.snz.user.dto.FinanceDto;
import io.terminus.snz.user.model.*;
import io.terminus.snz.user.service.AccountService;
import io.terminus.snz.user.service.AddressService;
import io.terminus.snz.user.service.CompanyService;
import io.terminus.snz.user.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.google.common.base.Objects.equal;
import static com.google.common.base.Objects.firstNonNull;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.base.Throwables.getStackTraceAsString;
import static io.terminus.common.utils.Arguments.*;
import static io.terminus.snz.requirement.service.RequirementService.QueryType;

/**
 * Date: 7/22/14
 * Time: 16:28
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */

@Slf4j
@Service(value = "mdmSupplierServiceImpl")
public class MDMSupplierServiceImpl implements MDMSupplierService {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private SupplierQualificationDao supplierQualificationDao;

    @Autowired
    private AddressService addressService;

    @Autowired
    private MdmConfigureService mdmConfigureService;

    @Autowired
    private RequirementService requirementService;

    @Autowired
    private AccountService<BaseUser> accountService;

    @Autowired
    private RequirementQuotaService requirementQuotaService;

    @Autowired
    private MdmVendorCodeService mdmVendorCodeService;

    @Autowired
    private TagService tagService;

    private ExecutorService executor = Executors.newFixedThreadPool(5);

    private static final Integer PAGE_SIZE = 500;

    /**
     * 允许获取MDM对接供应商列表的用户角色列表
     */
    private static final List<String> ALLOWED = Lists.newArrayList(User.JobRole.RESOURCE.role(), User.JobRole.SHARING.role());

    private static final Paging EMPTY_PAGE = new Paging<MDMBaseCompanyDto>(0L, Collections.<MDMBaseCompanyDto>emptyList());

    @Override
    public Response<Paging<MDMBaseCompanyDto>> findBaseSupplierInfoBy(BaseUser currentUser, String supplierName,
                                                                      Integer pageNo, Integer size) {
        PageInfo page = new PageInfo(pageNo, size);
        Response<Paging<MDMBaseCompanyDto>> result = new Response<Paging<MDMBaseCompanyDto>>();
        if (isNull(currentUser) || isNull(currentUser.getId())) {
            result.setResult(EMPTY_PAGE);
            return result;
        }

        // 获取当前用户查看步骤
        List<String> roles = currentUser.getRoles();
        if (Collections.disjoint(ALLOWED, roles)) {
            result.setError("user.not.authorized");
            return result;
        }

        try {
            Response<Paging<Requirement>> regGet = requirementService.findByPurchaser(currentUser, QueryType.MY_JOIN.value(),
                    RequirementStatus.TENDER_END.value(), supplierName, null, null, 0, PAGE_SIZE);
            checkState(regGet.isSuccess(), regGet.getError());
            Paging<Requirement> requirementPage = regGet.getResult();

            Integer handled = 0;
            Integer realOffSet, sectionLimit, limit=page.getLimit();
            Integer logicalOffset = page.getOffset();
            List<MDMBaseCompanyDto> baseCompanyDtos = Lists.newArrayList();
            while (!requirementPage.getData().isEmpty()) {
                // loop start
                // 获取实际偏移量和limit
                for (Requirement req:requirementPage.getData()) {
                    // 获取作为团队成员参与的需求，再通过需求获取供应商
                    Response<List<RequirementRank>> rrkGet = requirementQuotaService.findRequirementRanks(req.getId(), 3);   // find all
                    if (!rrkGet.isSuccess()) {
                        log.error("find requirement rank by requirement id fail, with requirement:{}, error code:{}",
                                req, rrkGet.getError());
                        continue;
                    }
                    List<RequirementRank> rrk = rrkGet.getResult();
                    List<Long> comIds = Lists.transform(rrk,
                            new Function<RequirementRank, Long>() {
                                @Override
                                public Long apply(RequirementRank rrk) {
                                    return rrk.getSupplierId();
                                }
                            }
                    );

                    // 根据用户的角色过滤对应的公司
                    List<Long> filletedIds = Lists.newArrayList();
                    for (Long cId: comIds) {
                        SupplierQualification sq = supplierQualificationDao.findBySupplierId(cId);
                        if (sq == null || equal(1, sq.getStage())) {
                            filletedIds.add(cId);
                        }
                        if (sq!=null && equal(2, sq.getStage())) {
                            filletedIds.add(cId);
                        }
                    }

                    // 通过公司列表获得基本公司信息dto。
                    Response<List<Company>> companiesGet = companyService.findCompaniesByIds(filletedIds);
                    checkState(companiesGet.isSuccess(), companiesGet.getError());

                    List<MDMBaseCompanyDto> baseCompanyDtosTmp = Lists.newArrayList();
                    for (Company com: companiesGet.getResult()) {
                        MDMBaseCompanyDto baseCompanyDto = companyToMDMBaseCompanyDto(com);
                        baseCompanyDto.setRequiremntName(req.getName());
                        baseCompanyDtosTmp.add(baseCompanyDto);
                    }


                    // 通过逻辑偏移量，计算实际偏移量
                    Integer count = baseCompanyDtosTmp.size();
                    if (count <= logicalOffset) {
                        logicalOffset -= count;
                        continue;
                    }
                    // 偏移量命中
                    realOffSet = logicalOffset;
                    sectionLimit = count - logicalOffset;
                    if (limit >= sectionLimit) {
                        limit -= sectionLimit;
                    } else {
                        sectionLimit = limit;
                        limit = 0;
                    }
                    logicalOffset = 0;

                    baseCompanyDtos.addAll(baseCompanyDtosTmp.subList(realOffSet, sectionLimit));

                    // List<ModuleDto> 里面的 module 记录数量已经达到上限
                    if (limit == 0) {
                        break;
                    }
                }
                handled += requirementPage.getData().size(); // offset step forward
                // loop end

                // next page
                regGet = requirementService.findByPurchaser(currentUser, QueryType.MY_JOIN.value(),
                        RequirementStatus.TENDER_END.value(), supplierName, null, null, handled, PAGE_SIZE);
                checkState(regGet.isSuccess(), regGet.getError());
                requirementPage = regGet.getResult();
            }

            result.setResult(new Paging<MDMBaseCompanyDto>(0l, baseCompanyDtos));
        } catch (IllegalStateException e) {
            log.error("`findBaseSupplierInfoBy` invoke fail. with currentUser:{}, supplierName:{}, pageNO:{}, size:{}. e:{}",
                    currentUser, supplierName, page, size, e);
            result.setError(e.getMessage());
            return result;
        } catch (Exception e) {
            log.error("`findBaseSupplierInfoBy` invoke fail. with currentUser:{}, supplierName:{}, pageNO:{}, size:{}, e:{}",
                    currentUser, supplierName, page, size, e);
            // todo: fill
            result.setError("supplier.base.find.fail");
            return result;
        }

        return result;
    }



    @Override
    public Response<SupplierQualification> findQualificationBySupplierId(Long supplierId) {
        Response<SupplierQualification> result = new Response<SupplierQualification>();

        try {
            checkArgument(positive(supplierId), "supplier.qualification.illegal.arg");


            SupplierQualification found = supplierQualificationDao.findBySupplierId(supplierId);
            checkState(notNull(found), "supplier.qualification.find.fail");

            result.setResult(found);
        } catch (IllegalArgumentException e) {
            log.error("`findQualificationBySupplierId` invoke with illegal supplier id:{}, e:{}",
                    supplierId, e);
            result.setError(e.getMessage());
            return result;
        } catch (Exception e) {
            log.error("`findQualificationBySupplierId` invoke fail. with supplier id:{}",
                    supplierId, e);
            result.setError("supplier.qualification.find.fail");
            return result;
        }

        return result;
    }

    @Override
    public Response<MDMBaseCompanyDto> findBaseSupplierInfoById(Long supplierId) {
        Response<MDMBaseCompanyDto> result = new Response<MDMBaseCompanyDto>();

        try {
            checkArgument(positive(supplierId), "supplier.find.illegal.argument");

            Response<Company> companyGet = companyService.findCompanyById(supplierId);
            checkState(companyGet.isSuccess());
            Company company = companyGet.getResult();
            MDMBaseCompanyDto dto = companyToMDMBaseCompanyDto(company);

            // 获取 MDM 资质验证
            SupplierQualification sq = supplierQualificationDao.findBySupplierId(company.getId());
            if (notNull(sq)) {
                sq.setFinGroup("1100");
                dto.setSupplierQualification(sq);
            }

            // 设置银行信息和其他信息
            BaseUser user = new BaseUser();
            user.setId(company.getUserId());
            Response<FinanceDto> financeGet = companyService.findFinanceByUserId(user);
            checkState(financeGet.isSuccess(), financeGet.getError());
            FinanceDto financeDto = financeGet.getResult();
            dto.setFinanceDto(financeDto);

            result.setResult(dto);
        } catch (IllegalArgumentException e) {
            log.error("`findBaseSupplierInfoById` invoke with illegal argument, supplier:{}, e:{}",
                    supplierId, e);
            result.setError(e.getMessage());
            return result;
        } catch (Exception e) {
            log.error("`findBaseSupplierInfoById` invoke fail. with supplierId:{}, e:{}",
                    supplierId, e);
            result.setError("supplier.find.base.by.id.fail");
            return result;
        }

        return result;
    }

    @Override
    public Response<Boolean> updateSupplierInfoById(BaseUser currentUser, Long supplierId,
                                                    final SupplierQualification supplierQualification) {
        Response<Boolean> result = new Response<Boolean>();
        Boolean isExtend = false;

        try {
            checkArgument(notNull(currentUser), "user.not.login");
            checkArgument(positive(supplierId) && notNull(supplierQualification), "illegal.argument");

            List<String> roles = currentUser.getRoles();
            if (Collections.disjoint(ALLOWED, roles)) {
                result.setError("user.not.authorized");
                return result;
            }
            Integer stage = roles.contains(User.JobRole.SHARING.role())?2:1;

            // 获取公司信息
            Response<Company> companyGet = companyService.findCompanyById(supplierId);
            checkState(companyGet.isSuccess());
            final Company company = companyGet.getResult();

            // 尝试获取一个已有的
            SupplierQualification found = supplierQualificationDao.findBySupplierId(company.getId());
            if (found!=null) {
                if (!equal(stage, found.getStage())) {
                    result.setError("user.not.authorized");
                    return result;
                }
                isExtend = true;
            }
            supplierQualification.setStage(stage);
            supplierQualification.maskByStage(stage);

            // 获取国家代码，如果失败设置为空
            Response<Address> addressGet = addressService.findById(company.getRegCountry());
            if (addressGet.isSuccess()) {
                MDMConfigure param = new MDMConfigure();
                param.setName(addressGet.getResult().getName());
                param.setTypeEnum(MDMConfigure.TYPE.NATION);
                Response<MDMConfigure> configureGet = mdmConfigureService.findBy(param);
                if (configureGet.isSuccess()) {
                    supplierQualification.setCountry(configureGet.getResult().getCode());
                }
            } else {
                supplierQualification.setCountry("");
            }

            // 设置银行信息和其他信息
            BaseUser user = new BaseUser();
            user.setId(company.getUserId());
            Response<FinanceDto> financeGet = companyService.findFinanceByUserId(user);
            checkState(financeGet.isSuccess(), financeGet.getError());
            FinanceDto financeDto = financeGet.getResult();
            Finance finance = financeDto.getFinance();
            if (finance!=null) {
                supplierQualification.setBankNum(finance.getBankAccount());
                supplierQualification.setBankCode(finance.getBankCode());
            }
            supplierQualification.setBankOwner("1");
            // TODO: get nation code later!

            supplierQualification.setBankNation("CN");
            supplierQualification.setPiPartner("");     // default empty, or supplier himself

            // 设置当前步骤
            supplierQualification.setId(null);
            if (supplierQualification.validateByStage(null)) {
                supplierQualification.setStage(2);    // step forward only on stage one
            }
            if (isExtend) {
                supplierQualification.setId(found.getId());
                BeanMapper.copy(supplierQualification, found);
                supplierQualificationDao.update(found);
            } else {
                supplierQualificationDao.create(supplierQualification);
            }

            // 当审核信息完成时，提交到 MDM
            if (equal(supplierQualification.getStage(), 2) && supplierQualification.validateByStage(2)) {
                executor.submit(new MDMVendorHelper(company));
            }
            result.setResult(true);
        } catch (Exception e) {
            log.error("`updateSupplierInfoById` invoke fail. with user:{}, supplierId:{}, qualification:{}, e:{}",
                    currentUser, supplierId, supplierQualification, e);
            result.setError("supplier.qualification.update.fail");
            return result;
        }

        return result;
    }

    private MDMBaseCompanyDto companyToMDMBaseCompanyDto(Company company) throws Exception {
        MDMBaseCompanyDto dto = new MDMBaseCompanyDto(company);

        // 获取办公电话、昵称
        Response<BaseUser> userGet = accountService.findUserById(company.getUserId());
        checkState(userGet.isSuccess(), "user.not.fond");
        BaseUser user = userGet.getResult();
        dto.setNick(user.getNickName());

        Response<ContactInfo> contactInfo = companyService.findContactInfoByUserId(user);
        if (contactInfo.isSuccess()) {
            dto.setOfficePhone(contactInfo.getResult().getOfficePhone());
        }

        // 获取国家代码，失败设置为空
        Response<Address> addressGet = addressService.findById(company.getRegCountry());
        if (addressGet.isSuccess()) {
            MDMConfigure param = new MDMConfigure();
            param.setName(addressGet.getResult().getName());
            param.setTypeEnum(MDMConfigure.TYPE.NATION);
            Response<MDMConfigure> configureGet = mdmConfigureService.findBy(param);
            if (configureGet.isSuccess()) {
                dto.setContryCode(configureGet.getResult().getCode());
            }
        } else {
            dto.setContryCode("");
        }

        return dto;
    }

    private void updateSQMsg(Long sid, String code, String msg) {
        SupplierQualification sqMsg = new SupplierQualification();
        sqMsg.setOpCode(firstNonNull(code, "").toUpperCase());
        sqMsg.setOpMsg(msg);
        sqMsg.setId(sid);
        supplierQualificationDao.update(sqMsg);
    }

    // private helper class for mdm vendor code apply.
    private class MDMVendorHelper implements Runnable {
        private MDMVendorCodeApply apply;
        private Company company;
        private SupplierQualification sq;

        public MDMVendorHelper(Company company) {
            apply = new MDMVendorCodeApply();
            this.company = company;
            this.sq = supplierQualificationDao.findBySupplierId(company.getId());
        }

        private void fill() {
            List<RSPVENDORBANKTYPE> rspvendorbanktype = apply.getTBank().getTVENDORBANKITEM();
            RSPVENDORBANKTYPE tBankType = new RSPVENDORBANKTYPE();
            tBankType.setBANKCOUNTRY(sq.getBankNation());
            tBankType.setACCOUNTHOLDERNAME(sq.getBankOwner());
            tBankType.setBANKBRANCHCODE(sq.getBankCode());
            tBankType.setBANKACCOUNTNUM(sq.getBankNum());
            rspvendorbanktype.add(tBankType);

            List<RSPVENDORPURTYPE> rspvendorpurtypes = apply.getTPru().getTVENDORPURITEM();
            RSPVENDORPURTYPE tPruType = new RSPVENDORPURTYPE();
            tPruType.setPARTNERPI(sq.getPiPartner());
            tPruType.setORDERCURRENCY(sq.getCurrency());
            tPruType.setPURCHASEGRPCODE(sq.getPurchOrg());
            rspvendorpurtypes.add(tPruType);

            List<RSPVENDORCOMPANYTYPE> rspvendorcompanytypes = apply.getTCompany().getTVENDORCOMPANYITEM();
            RSPVENDORCOMPANYTYPE tCompanyType = new RSPVENDORCOMPANYTYPE();
            tCompanyType.setSEQUENCENO(sq.getOrder());
            tCompanyType.setCOMPANYCODE(sq.getComCode());
            tCompanyType.setRECONCILEACCOUNT(sq.getSlave());
            tCompanyType.setPAYMENTTERM(sq.getPayTerm());
            tCompanyType.setPAYMENTMETHOD(sq.getPayMethod());
            rspvendorcompanytypes.add(tCompanyType);

            BaseUser user = new BaseUser();
            user.setId(company.getUserId());
            Response<ContactInfo> contactGet = companyService.findContactInfoByUserId(user);
            if (!contactGet.isSuccess()) {
                log.error("can't find contact info for mdm vendor apply, by company:{}", company);
                // todo: i18n string later
                updateSQMsg(sq.getId(), "E", "未找到公司联系信息");
                throw new RuntimeException("can't find contact info for mdm vendor apply, by company:" + company);
            }

            ContactInfo contactInfo = contactGet.getResult();
            apply.setSupplierName(company.getCorporation());
            apply.setAccountGroup("1100");
            apply.setTaxCode(company.getTaxNoId());

            Response<Address> addressGet = addressService.findById(company.getRegCity());
            if (addressGet.isSuccess()) {
                log.error("apply supplier vendor fail, find company registry city fail, with error code:{}",
                        addressGet.getError());
                // todo: i18n string later
                updateSQMsg(sq.getId(), "E", "查找公司注册城市");
                return;
            }
            apply.setStreetRoom(addressGet.getResult().getName());
            apply.setCountry(sq.getCountry());
            apply.setRegion("120100");
            apply.setPhone(contactInfo.getMobile());
        }

        @Override
        public void run() {
            fill();
            Response<List<Optional<String>>> result = mdmVendorCodeService.applyVendorCode(apply);

            // 检查服务调用是否成功
            if (!result.isSuccess() || Arguments.isNullOrEmpty(result.getResult())) {
                log.error("apply supplier vendor code fail, with error code:{}", result.getError());
                updateSQMsg(sq.getId(), "E", result.getError());
                return;
            }

            // 检查调用 MDM 是否成功
            List<Optional<String>> holders = result.getResult();
            if(equal("E", firstNonNull(holders.get(0).get(), "").toUpperCase())) {
                log.error("apply supplier vendor code fail, with error code:{}", result.getError());
                // when fail, leave message and return
                updateSQMsg(sq.getId(), "E", firstNonNull(holders.get(1).get(), ""));
            } else if (equal("S", firstNonNull(holders.get(0).get(), "").toUpperCase())) {
                // 检查 v 码是否有效
                String vCode = holders.get(3).get();
                if (Strings.isNullOrEmpty(vCode)) {
                    // when fail, leave message and return
                    log.error("apply supplier vendor code fail, with error code:{}", result.getError());
                    updateSQMsg(sq.getId(), "E", "获取V码为空");
                    return;
                }

                try {
                    // success and clean up
                    SupplierQualification update = new SupplierQualification();
                    update.setId(this.sq.getId());
                    update.setVCode(vCode);
                    update.setStage(3);
                    supplierQualificationDao.update(update);
                    Response<Boolean> tryAdd = companyService.addSupplierCode(company.getId(), vCode);
                    tagService.addSupplierStatusTag(company.getUserId(), User.SupplierTag.ALTERNATIVE);
                    checkState(tryAdd.isSuccess(), tryAdd.getError());
                } catch (Exception e) {
                    log.error("fail when updating vendor code to supplier, e:{}", getStackTraceAsString(e));
                }
            }

        }
    }
}
