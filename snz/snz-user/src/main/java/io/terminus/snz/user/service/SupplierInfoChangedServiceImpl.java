package io.terminus.snz.user.service;

import com.fasterxml.jackson.databind.JavaType;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.terminus.common.utils.BeanMapper;
import io.terminus.common.utils.JsonMapper;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.dao.CompanyDao;
import io.terminus.snz.user.dao.UserDao;
import io.terminus.snz.user.dao.redis.SupplierChangedInfoRedisDao;
import io.terminus.snz.user.dto.*;
import io.terminus.snz.user.model.*;
import io.terminus.snz.user.tool.ChangedInfoKeys;
import io.terminus.snz.user.tool.CountryCertificate;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Author:Guo Chaopeng
 * Created on 14-8-15.
 */
@Service
@Slf4j
public class SupplierInfoChangedServiceImpl implements SupplierInfoChangedService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd");

    @Autowired
    private UserDao userDao;

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private SupplierChangedInfoRedisDao supplierChangedInfoRedisDao;

    /**
     * 是否需要检查信息是否修改了
     */
    public boolean needCheckChanged(Integer approveStatus) {
        if (Objects.equal(approveStatus, User.ApproveStatus.ENTER_WAITING_FOR_APPROVE.value()) ||
                Objects.equal(approveStatus, User.ApproveStatus.INIT.value())) {
            return false;
        }
        return true;
    }

    @Override
    public SupplierInfoChangedDto baseCompanyChanged(Long userId, CompanyDto updatedCompanyDto, Company oldCompany, List<CompanyMainBusiness> oldMainBusinesses, List<CompanySupplyPark> oldSupplyParks) {
        SupplierInfoChangedDto supplierInfoChangedDto = new SupplierInfoChangedDto();

        Company updatedCompany = updatedCompanyDto.getCompany();
        List<CompanyMainBusiness> updatedMainBusinesses = updatedCompanyDto.getCompanyMainBusinesses();
        List<CompanySupplyPark> updatedSupplyParks = updatedCompanyDto.getCompanySupplyParks();

        //取出新的信息
        CompanyDto newCompanyDto = getNewBaseCompany(userId, oldCompany, oldMainBusinesses, oldSupplyParks).getSupplierInfo();
        Company newCompany = newCompanyDto.getCompany();
        List<CompanyMainBusiness> newMainBusinesses = newCompanyDto.getCompanyMainBusinesses();
        List<CompanySupplyPark> newSupplyParks = newCompanyDto.getCompanySupplyParks();

        Map<String, String> changedInfo = Maps.newHashMap();

        if (!Objects.equal(updatedCompany.getCorporation(), newCompany.getCorporation())) {
            changedInfo.put(ChangedInfoKeys.companyCorporation(), updatedCompany.getCorporation());
            updatedCompany.setCorporation(null);
        }

        if (!Objects.equal(updatedCompany.getInitAgent(), newCompany.getInitAgent())) {
            changedInfo.put(ChangedInfoKeys.companyInitAgent(), updatedCompany.getInitAgent());
            updatedCompany.setInitAgent(null);
        }

        if (!Objects.equal(updatedCompany.getDesc(), newCompany.getDesc())) {
            changedInfo.put(ChangedInfoKeys.companyDesc(), updatedCompany.getDesc());
            updatedCompany.setDesc(null);
        }

        if (!Objects.equal(updatedCompany.getRegCountry(), newCompany.getRegCountry())) {
            changedInfo.put(ChangedInfoKeys.companyRegCountry(), String.valueOf(updatedCompany.getRegCountry()));
            updatedCompany.setRegCountry(null);
        }

//        if (!Objects.equal(company.getCustomers(), newCompany.getCustomers())) {
//            changedInfo.put("", newCompany.getCustomers());
//        }
//
        if (!Objects.equal(updatedCompany.getProductLine(), newCompany.getProductLine())) {
            changedInfo.put(ChangedInfoKeys.companyProductLine(), updatedCompany.getProductLine());
        }

        if (mainBusinessChanged(updatedMainBusinesses, newMainBusinesses)) {
            changedInfo.put(ChangedInfoKeys.companyMainBusiness(), JsonMapper.nonEmptyMapper().toJson(updatedMainBusinesses));
        }

        if (supplyParkChanged(updatedSupplyParks, newSupplyParks)) {
            changedInfo.put(ChangedInfoKeys.companySupplyPark(), JsonMapper.nonEmptyMapper().toJson(updatedSupplyParks));
        }

        //如果有V码，则继续检查合同信息
        if (!Strings.isNullOrEmpty(newCompany.getSupplierCode())) {

            if (!Objects.equal(updatedCompany.getGroupName(), newCompany.getGroupName())) {
                changedInfo.put(ChangedInfoKeys.companyGroupName(), updatedCompany.getGroupName());
                updatedCompany.setGroupName(null);
            }
            if (!Objects.equal(updatedCompany.getGroupAddr(), newCompany.getGroupAddr())) {
                changedInfo.put(ChangedInfoKeys.companyGroupAddr(), updatedCompany.getGroupAddr());
                updatedCompany.setGroupAddr(null);
            }
            if (!Objects.equal(updatedCompany.getRegCapital(), newCompany.getRegCapital())) {
                changedInfo.put(ChangedInfoKeys.companyRegCapical(), String.valueOf(updatedCompany.getRegCapital()));
                updatedCompany.setRegCapital(null);
            }
            if (!Objects.equal(updatedCompany.getRcCoinType(), newCompany.getRcCoinType())) {
                changedInfo.put(ChangedInfoKeys.companyRcCoinType(), String.valueOf(updatedCompany.getRcCoinType()));
                updatedCompany.setRcCoinType(null);
            }
            if (!Objects.equal(updatedCompany.getPersonScale(), newCompany.getPersonScale())) {
                changedInfo.put(ChangedInfoKeys.companyPersonScale(), updatedCompany.getPersonScale());
                updatedCompany.setPersonScale(null);
            }
            if (!Objects.equal(updatedCompany.getRegProvince(), newCompany.getRegProvince())) {
                changedInfo.put(ChangedInfoKeys.companyRegProvince(), String.valueOf(updatedCompany.getRegProvince()));
                updatedCompany.setRegProvince(null);
            }
            if (!Objects.equal(updatedCompany.getRegCity(), newCompany.getRegCity())) {
                changedInfo.put(ChangedInfoKeys.companyRegCity(), String.valueOf(updatedCompany.getRegCity()));
                updatedCompany.setRegCity(null);
            }
            if (!Objects.equal(updatedCompany.getFixedAssets(), newCompany.getFixedAssets())) {
                changedInfo.put(ChangedInfoKeys.companyFixedAssets(), String.valueOf(updatedCompany.getFixedAssets()));
                updatedCompany.setFixedAssets(null);
            }
            if (!Objects.equal(updatedCompany.getFaCoinType(), newCompany.getFaCoinType())) {
                changedInfo.put(ChangedInfoKeys.companyFaCoinType(), String.valueOf(updatedCompany.getFaCoinType()));
                updatedCompany.setFaCoinType(null);
            }

            Date updatedFoundAt = updatedCompany.getFoundAt();
            Date oldFoundAt = newCompany.getFoundAt();
            if (updatedFoundAt != null) {
                DateTime updated = new DateTime(updatedFoundAt);
                if (oldFoundAt == null) {
                    changedInfo.put(ChangedInfoKeys.companyFoundAt(), updated.toString(FORMATTER));
                    updatedCompany.setFoundAt(null);
                } else {
                    DateTime old = new DateTime(oldFoundAt);
                    if (!updated.equals(old)) {
                        changedInfo.put(ChangedInfoKeys.companyFoundAt(), updated.toString(FORMATTER));
                        updatedCompany.setFoundAt(null);
                    }
                }
            }

            if (!Objects.equal(updatedCompany.getOfficialWebsite(), newCompany.getOfficialWebsite())) {
                changedInfo.put(ChangedInfoKeys.companyOfficialWebSite(), updatedCompany.getOfficialWebsite());
                updatedCompany.setOfficialWebsite(null);
            }
            if (!Objects.equal(updatedCompany.getNature(), newCompany.getNature())) {
                changedInfo.put(ChangedInfoKeys.companyNature(), String.valueOf(updatedCompany.getNature()));
                updatedCompany.setNature(null);
            }
            if (!Objects.equal(updatedCompany.getWorldTop(), newCompany.getWorldTop())) {
                changedInfo.put(ChangedInfoKeys.companyWorldTop(), String.valueOf(updatedCompany.getWorldTop()));
                updatedCompany.setWorldTop(null);
            }
            if (!Objects.equal(updatedCompany.getListedStatus(), newCompany.getListedStatus())) {
                changedInfo.put(ChangedInfoKeys.companyListedStatus(), String.valueOf(updatedCompany.getListedStatus()));
                updatedCompany.setListedStatus(null);
            }
            if (!Objects.equal(updatedCompany.getListedRegion(), newCompany.getListedRegion())) {
                changedInfo.put(ChangedInfoKeys.companyListedRegion(), updatedCompany.getListedRegion());
                updatedCompany.setListedRegion(null);
            }
            if (!Objects.equal(updatedCompany.getTicker(), newCompany.getTicker())) {
                changedInfo.put(ChangedInfoKeys.companyTicker(), updatedCompany.getTicker());
                updatedCompany.setTicker(null);
            }

        }

        if (!changedInfo.isEmpty()) {
            supplierInfoChangedDto.setChanged(true);
            supplierInfoChangedDto.setChangedInfo(changedInfo);
        }

        return supplierInfoChangedDto;
    }

    @Override
    public SupplierUpdatedInfoDto<CompanyDto> getNewBaseCompany(Long userId, Company oldCompany, List<CompanyMainBusiness> oldMainBusinesses, List<CompanySupplyPark> oldSupplyParks) {
        SupplierUpdatedInfoDto<CompanyDto> supplierUpdatedInfoDto = new SupplierUpdatedInfoDto<CompanyDto>();
        CompanyDto companyDto = new CompanyDto();
        Map<String, Object> oldValues = Maps.newHashMap();

        boolean baseCompanyInfoChanged = supplierChangedInfoRedisDao.tabInfoChanged(userId, ChangedInfoKeys.companyTab());
        if (!baseCompanyInfoChanged) {
            companyDto.setCompany(oldCompany);
            companyDto.setCompanyMainBusinesses(oldMainBusinesses);
            companyDto.setCompanySupplyParks(oldSupplyParks);

            supplierUpdatedInfoDto.setSupplierInfo(companyDto);
            supplierUpdatedInfoDto.setOldValues(oldValues);
            return supplierUpdatedInfoDto;
        }

        Map<String, String> changedInfo = supplierChangedInfoRedisDao.getChangedInfos(userId);
        Company newCompany = new Company();
        BeanMapper.copy(oldCompany, newCompany);

        if (changedInfo.containsKey(ChangedInfoKeys.companyCorporation())) {
            newCompany.setCorporation(changedInfo.get(ChangedInfoKeys.companyCorporation()));
            oldValues.put(ChangedInfoKeys.companyCorporation(), oldCompany.getCorporation());
        }
        if (changedInfo.containsKey(ChangedInfoKeys.companyInitAgent())) {
            newCompany.setInitAgent(changedInfo.get(ChangedInfoKeys.companyInitAgent()));
            oldValues.put(ChangedInfoKeys.companyInitAgent(), oldCompany.getInitAgent());
        }
        if (changedInfo.containsKey(ChangedInfoKeys.companyRegCountry())) {
            String regCountry = changedInfo.get(ChangedInfoKeys.companyRegCountry());
            newCompany.setRegCountry("null".equals(regCountry) ? null : Integer.parseInt(regCountry));
            oldValues.put(ChangedInfoKeys.companyRegCountry(), oldCompany.getRegCountry());
        }
        if (changedInfo.containsKey(ChangedInfoKeys.companyDesc())) {
            newCompany.setDesc(changedInfo.get(ChangedInfoKeys.companyDesc()));
            oldValues.put(ChangedInfoKeys.companyDesc(), oldCompany.getDesc());
        }

        //如果有V码，则检查是否有待更新的合同信息
        if (!Strings.isNullOrEmpty(oldCompany.getSupplierCode())) {

            if (changedInfo.containsKey(ChangedInfoKeys.companyGroupName())) {
                newCompany.setGroupName(changedInfo.get(ChangedInfoKeys.companyGroupName()));
                oldValues.put(ChangedInfoKeys.companyGroupName(), oldCompany.getGroupName());
            }

            if (changedInfo.containsKey(ChangedInfoKeys.companyGroupAddr())) {
                newCompany.setGroupAddr(changedInfo.get(ChangedInfoKeys.companyGroupAddr()));
                oldValues.put(ChangedInfoKeys.companyGroupAddr(), oldCompany.getGroupAddr());
            }

            if (changedInfo.containsKey(ChangedInfoKeys.companyRegCapical())) {
                String regCapital = changedInfo.get(ChangedInfoKeys.companyRegCapical());
                newCompany.setRegCapital("null".equals(regCapital) ? null : Long.parseLong(regCapital));
                oldValues.put(ChangedInfoKeys.companyRegCapical(), oldCompany.getRegCapital());
            }

            if (changedInfo.containsKey(ChangedInfoKeys.companyRcCoinType())) {
                String reCoinType = changedInfo.get(ChangedInfoKeys.companyRcCoinType());
                newCompany.setRcCoinType("null".equals(reCoinType) ? null : Integer.parseInt(reCoinType));
                oldValues.put(ChangedInfoKeys.companyRcCoinType(), oldCompany.getRcCoinType());
            }

            if (changedInfo.containsKey(ChangedInfoKeys.companyPersonScale())) {
                newCompany.setPersonScale(changedInfo.get(ChangedInfoKeys.companyPersonScale()));
                oldValues.put(ChangedInfoKeys.companyPersonScale(), oldCompany.getPersonScale());
            }

            if (changedInfo.containsKey(ChangedInfoKeys.companyRegProvince())) {
                String regProvince = changedInfo.get(ChangedInfoKeys.companyRegProvince());
                newCompany.setRegProvince("null".equals(regProvince) ? null : Integer.parseInt(regProvince));
                oldValues.put(ChangedInfoKeys.companyRegProvince(), oldCompany.getRegProvince());
            }

            if (changedInfo.containsKey(ChangedInfoKeys.companyRegCity())) {
                String regCity = changedInfo.get(ChangedInfoKeys.companyRegCity());
                newCompany.setRegCity("null".equals(regCity) ? null : Integer.parseInt(regCity));
                oldValues.put(ChangedInfoKeys.companyRegCity(), oldCompany.getRegCity());
            }

            if (changedInfo.containsKey(ChangedInfoKeys.companyFixedAssets())) {
                String fixedAssets = changedInfo.get(ChangedInfoKeys.companyFixedAssets());
                newCompany.setFixedAssets("null".equals(fixedAssets) ? null : Long.parseLong(fixedAssets));
                oldValues.put(ChangedInfoKeys.companyFixedAssets(), oldCompany.getFixedAssets());
            }

            if (changedInfo.containsKey(ChangedInfoKeys.companyFaCoinType())) {
                String faCoinType = changedInfo.get(ChangedInfoKeys.companyFaCoinType());
                newCompany.setFaCoinType("null".equals(faCoinType) ? null : Integer.parseInt(faCoinType));
                oldValues.put(ChangedInfoKeys.companyFaCoinType(), oldCompany.getFaCoinType());
            }

            if (changedInfo.containsKey(ChangedInfoKeys.companyFoundAt())) {
                String foundAt = changedInfo.get(ChangedInfoKeys.companyFoundAt());
                newCompany.setFoundAt(DateTime.parse(foundAt, FORMATTER).toDate());
                oldValues.put(ChangedInfoKeys.companyFoundAt(), oldCompany.getFoundAt());
            }

            if (changedInfo.containsKey(ChangedInfoKeys.companyOfficialWebSite())) {
                newCompany.setOfficialWebsite(changedInfo.get(ChangedInfoKeys.companyOfficialWebSite()));
                oldValues.put(ChangedInfoKeys.companyOfficialWebSite(), oldCompany.getOfficialWebsite());
            }

            if (changedInfo.containsKey(ChangedInfoKeys.companyNature())) {
                String nature = changedInfo.get(ChangedInfoKeys.companyNature());
                newCompany.setNature("null".equals(nature) ? null : Integer.parseInt(nature));
                oldValues.put(ChangedInfoKeys.companyNature(), oldCompany.getNature());
            }

            if (changedInfo.containsKey(ChangedInfoKeys.companyWorldTop())) {
                String worldTop = changedInfo.get(ChangedInfoKeys.companyWorldTop());
                newCompany.setWorldTop("null".equals(worldTop) ? null : Integer.parseInt(worldTop));
                oldValues.put(ChangedInfoKeys.companyWorldTop(), oldCompany.getWorldTop());
            }

            if (changedInfo.containsKey(ChangedInfoKeys.companyListedStatus())) {
                String listedStatus = changedInfo.get(ChangedInfoKeys.companyListedStatus());
                newCompany.setListedStatus("null".equals(listedStatus) ? null : Integer.parseInt(listedStatus));
                oldValues.put(ChangedInfoKeys.companyListedStatus(), oldCompany.getListedStatus());
            }

            if (changedInfo.containsKey(ChangedInfoKeys.companyListedRegion())) {
                newCompany.setListedRegion(changedInfo.get(ChangedInfoKeys.companyListedRegion()));
                oldValues.put(ChangedInfoKeys.companyListedRegion(), oldCompany.getListedRegion());
            }

            if (changedInfo.containsKey(ChangedInfoKeys.companyTicker())) {
                newCompany.setTicker(changedInfo.get(ChangedInfoKeys.companyTicker()));
                oldValues.put(ChangedInfoKeys.companyTicker(), oldCompany.getTicker());
            }

        }

        companyDto.setCompany(newCompany);

        if (changedInfo.containsKey(ChangedInfoKeys.companyMainBusiness())) {
            String mainBusinessesJson = changedInfo.get(ChangedInfoKeys.companyMainBusiness());
            JavaType javaType = JsonMapper.JSON_NON_EMPTY_MAPPER.createCollectionType(List.class, CompanyMainBusiness.class);
            List<CompanyMainBusiness> updatedMainBusinesses = JsonMapper.JSON_NON_EMPTY_MAPPER.fromJson(mainBusinessesJson, javaType);
            companyDto.setCompanyMainBusinesses(updatedMainBusinesses);

            oldValues.put(ChangedInfoKeys.companyMainBusiness(), oldMainBusinesses);
        } else {
            companyDto.setCompanyMainBusinesses(oldMainBusinesses);
        }

        if (changedInfo.containsKey(ChangedInfoKeys.companySupplyPark())) {
            String supplyParkJson = changedInfo.get(ChangedInfoKeys.companySupplyPark());
            JavaType javaType = JsonMapper.JSON_NON_EMPTY_MAPPER.createCollectionType(List.class, CompanySupplyPark.class);
            List<CompanySupplyPark> updatedSupplyParks = JsonMapper.JSON_NON_EMPTY_MAPPER.fromJson(supplyParkJson, javaType);
            companyDto.setCompanySupplyParks(updatedSupplyParks);

            oldValues.put(ChangedInfoKeys.companySupplyPark(), oldSupplyParks);
        } else {
            companyDto.setCompanySupplyParks(oldSupplyParks);
        }

        supplierUpdatedInfoDto.setSupplierInfo(companyDto);
        supplierUpdatedInfoDto.setOldValues(oldValues);
        return supplierUpdatedInfoDto;

    }

    @Override
    public SupplierInfoChangedDto paperworkChanged(Long userId, PaperworkDto updatedPaperwork, Company oldPaperwork) {
        SupplierInfoChangedDto supplierInfoChangedDto = new SupplierInfoChangedDto();

        Map<String, String> changedInfo = Maps.newHashMap();

        int type = CountryCertificate.check(oldPaperwork.getRegCountry());

        PaperworkDto newPaperworkDto = getNewPaperwork(userId, oldPaperwork).getSupplierInfo();

        switch (type) {
            case CountryCertificate.NO_CERTIFICATE:
                supplierInfoChangedDto.setChanged(false);
                return supplierInfoChangedDto;
            case CountryCertificate.ONLY_NEED_BUSINESS_LICENSE:
                checkBusinessLicense(changedInfo, updatedPaperwork, newPaperworkDto);
                break;
            case CountryCertificate.ONLY_NEED_BUSINESS_LICENSE_AND_TAX_NO:
                checkBusinessLicense(changedInfo, updatedPaperwork, newPaperworkDto);
                checkTaxNo(changedInfo, updatedPaperwork, newPaperworkDto);
                break;
            default:
                checkBusinessLicense(changedInfo, updatedPaperwork, newPaperworkDto);
                checkTaxNo(changedInfo, updatedPaperwork, newPaperworkDto);
                checkOrgCert(changedInfo, updatedPaperwork, newPaperworkDto);
        }

        if (!changedInfo.isEmpty()) {
            supplierInfoChangedDto.setChanged(true);
            supplierInfoChangedDto.setChangedInfo(changedInfo);
        }

        return supplierInfoChangedDto;
    }

    @Override
    public SupplierUpdatedInfoDto<PaperworkDto> getNewPaperwork(Long userId, Company oldPaperwork) {
        SupplierUpdatedInfoDto<PaperworkDto> supplierUpdatedInfoDto = new SupplierUpdatedInfoDto<PaperworkDto>();
        PaperworkDto paperworkDto = new PaperworkDto();
        Map<String, Object> oldValues = Maps.newHashMap();

        BeanMapper.copy(oldPaperwork, paperworkDto);

        boolean paperworkChanged = supplierChangedInfoRedisDao.tabInfoChanged(userId, ChangedInfoKeys.paperworkTab());
        if (!paperworkChanged) {
            supplierUpdatedInfoDto.setSupplierInfo(paperworkDto);
            supplierUpdatedInfoDto.setOldValues(oldValues);
            return supplierUpdatedInfoDto;
        }

        Map<String, String> changedInfo = supplierChangedInfoRedisDao.getChangedInfos(userId);

        if (changedInfo.containsKey(ChangedInfoKeys.companyBusinessLicense())) {
            paperworkDto.setBusinessLicense(changedInfo.get(ChangedInfoKeys.companyBusinessLicense()));
            oldValues.put(ChangedInfoKeys.companyBusinessLicense(), oldPaperwork.getBusinessLicense());
        }
        if (changedInfo.containsKey(ChangedInfoKeys.companyBusinessLicenseId())) {
            paperworkDto.setBusinessLicenseId(changedInfo.get(ChangedInfoKeys.companyBusinessLicenseId()));
            oldValues.put(ChangedInfoKeys.companyBusinessLicenseId(), oldPaperwork.getBusinessLicenseId());
        }
        if (changedInfo.containsKey(ChangedInfoKeys.companyBusinessLicenseDate())) {
            String blDate = changedInfo.get(ChangedInfoKeys.companyBusinessLicenseDate());
            paperworkDto.setBlDate(DateTime.parse(blDate, FORMATTER).toDate());
            oldValues.put(ChangedInfoKeys.companyBusinessLicenseDate(), oldPaperwork.getBlDate());
        }

        if (changedInfo.containsKey(ChangedInfoKeys.companyTaxNo())) {
            paperworkDto.setTaxNo(changedInfo.get(ChangedInfoKeys.companyTaxNo()));
            oldValues.put(ChangedInfoKeys.companyTaxNo(), oldPaperwork.getTaxNo());
        }
        if (changedInfo.containsKey(ChangedInfoKeys.companyTaxNoId())) {
            paperworkDto.setTaxNoId(changedInfo.get(ChangedInfoKeys.companyTaxNoId()));
            oldValues.put(ChangedInfoKeys.companyTaxNoId(), oldPaperwork.getTaxNoId());
        }

        if (changedInfo.containsKey(ChangedInfoKeys.companyOrgCert())) {
            paperworkDto.setOrgCert(changedInfo.get(ChangedInfoKeys.companyOrgCert()));
            oldValues.put(ChangedInfoKeys.companyOrgCert(), oldPaperwork.getOrgCert());
        }
        if (changedInfo.containsKey(ChangedInfoKeys.companyOrgCertId())) {
            paperworkDto.setOrgCertId(changedInfo.get(ChangedInfoKeys.companyOrgCertId()));
            oldValues.put(ChangedInfoKeys.companyOrgCertId(), oldPaperwork.getOrgCertId());
        }
        if (changedInfo.containsKey(ChangedInfoKeys.companyOrgCertDate())) {
            String ocDate = changedInfo.get(ChangedInfoKeys.companyOrgCertDate());
            paperworkDto.setOcDate(DateTime.parse(ocDate, FORMATTER).toDate());
            oldValues.put(ChangedInfoKeys.companyOrgCertDate(), oldPaperwork.getOcDate());
        }

        supplierUpdatedInfoDto.setSupplierInfo(paperworkDto);
        supplierUpdatedInfoDto.setOldValues(oldValues);
        return supplierUpdatedInfoDto;

    }

    public SupplierInfoChangedDto contactInfoChanged(Long userId, ContactInfo updatedContactInfo, ContactInfo oldContactInfo) {
        SupplierInfoChangedDto supplierInfoChangedDto = new SupplierInfoChangedDto();

        ContactInfo newContactInfo = getNewContactInfo(userId, oldContactInfo).getSupplierInfo();

        Map<String, String> changedInfo = Maps.newHashMap();

        if (!Objects.equal(updatedContactInfo.getName(), newContactInfo.getName())) {
            changedInfo.put(ChangedInfoKeys.contactInfoName(), updatedContactInfo.getName());
            updatedContactInfo.setName(null);
        }

        if (!Objects.equal(updatedContactInfo.getEmail(), newContactInfo.getEmail())) {
            changedInfo.put(ChangedInfoKeys.contactInfoEmail(), updatedContactInfo.getEmail());
            updatedContactInfo.setEmail(null);
        }

        if (!Objects.equal(updatedContactInfo.getMobile(), newContactInfo.getMobile())) {
            changedInfo.put(ChangedInfoKeys.contactInfoMobile(), updatedContactInfo.getMobile());
            updatedContactInfo.setMobile(null);
        }

        Company company = companyDao.findByUserId(userId);
        if (!Strings.isNullOrEmpty(company.getSupplierCode())) {
            if (!Objects.equal(updatedContactInfo.getOfficePhone(), newContactInfo.getOfficePhone())) {
                changedInfo.put(ChangedInfoKeys.contactInfoPhone(), updatedContactInfo.getOfficePhone());
                updatedContactInfo.setOfficePhone(null);
            }
        }

        if (!changedInfo.isEmpty()) {
            supplierInfoChangedDto.setChanged(true);
            supplierInfoChangedDto.setChangedInfo(changedInfo);
        }
        return supplierInfoChangedDto;
    }

    @Override
    public SupplierUpdatedInfoDto<ContactInfo> getNewContactInfo(Long userId, ContactInfo oldContactInfo) {

        SupplierUpdatedInfoDto<ContactInfo> supplierUpdatedInfoDto = new SupplierUpdatedInfoDto<ContactInfo>();
        Map<String, Object> oldValues = Maps.newHashMap();

        boolean contactInfoChanged = supplierChangedInfoRedisDao.tabInfoChanged(userId, ChangedInfoKeys.contactInfoTab());
        if (!contactInfoChanged) {
            supplierUpdatedInfoDto.setSupplierInfo(oldContactInfo);
            supplierUpdatedInfoDto.setOldValues(oldValues);
            return supplierUpdatedInfoDto;
        }

        Map<String, String> changedInfo = supplierChangedInfoRedisDao.getChangedInfos(userId);

        ContactInfo newContactInfo = new ContactInfo();
        BeanMapper.copy(oldContactInfo, newContactInfo);

        if (changedInfo.containsKey(ChangedInfoKeys.contactInfoName())) {
            newContactInfo.setName(changedInfo.get(ChangedInfoKeys.contactInfoName()));
            oldValues.put(ChangedInfoKeys.contactInfoName(), oldContactInfo.getName());
        }

        if (changedInfo.containsKey(ChangedInfoKeys.contactInfoEmail())) {
            newContactInfo.setEmail(changedInfo.get(ChangedInfoKeys.contactInfoEmail()));
            oldValues.put(ChangedInfoKeys.contactInfoEmail(), oldContactInfo.getEmail());
        }

        if (changedInfo.containsKey(ChangedInfoKeys.contactInfoMobile())) {
            newContactInfo.setMobile(changedInfo.get(ChangedInfoKeys.contactInfoMobile()));
            oldValues.put(ChangedInfoKeys.contactInfoMobile(), oldContactInfo.getMobile());
        }

        if (changedInfo.containsKey(ChangedInfoKeys.contactInfoPhone())) {
            newContactInfo.setOfficePhone(changedInfo.get(ChangedInfoKeys.contactInfoPhone()));
            oldValues.put(ChangedInfoKeys.contactInfoPhone(), oldContactInfo.getOfficePhone());
        }

        supplierUpdatedInfoDto.setSupplierInfo(newContactInfo);
        supplierUpdatedInfoDto.setOldValues(oldValues);
        return supplierUpdatedInfoDto;
    }

    @Override
    public SupplierInfoChangedDto qualityChanged(Long userId, CompanyExtraQuality updatedQuality, CompanyExtraQuality oldQuality) {
        SupplierInfoChangedDto supplierInfoChangedDto = new SupplierInfoChangedDto();

        CompanyExtraQuality newQuality = getNewCompanyExtraQuality(userId, oldQuality).getSupplierInfo();

        Map<String, String> changedInfo = Maps.newHashMap();

        if (!Objects.equal(updatedQuality.getRohsId(), newQuality.getRohsId())) {
            changedInfo.put(ChangedInfoKeys.qualityRohsId(), updatedQuality.getRohsId());
            updatedQuality.setRohsId(null);
        }
        if (!Objects.equal(updatedQuality.getRohsAttachUrl(), newQuality.getRohsAttachUrl())) {
            changedInfo.put(ChangedInfoKeys.qualityRohsAttachUrl(), updatedQuality.getRohsAttachUrl());
            updatedQuality.setRohsAttachUrl(null);
        }

        if (updatedQuality.getRohsValidDate() != null) {
            DateTime updatedRohsValidDate = new DateTime(updatedQuality.getRohsValidDate());
            if (newQuality.getRohsValidDate() == null) {
                changedInfo.put(ChangedInfoKeys.qualityRohsValidDate(), updatedRohsValidDate.toString(FORMATTER));
                updatedQuality.setRohsValidDate(null);
            } else {
                DateTime oldRohsValidDate = new DateTime(newQuality.getRohsValidDate());
                if (!updatedRohsValidDate.equals(oldRohsValidDate)) {
                    changedInfo.put(ChangedInfoKeys.qualityRohsValidDate(), updatedRohsValidDate.toString(FORMATTER));
                    updatedQuality.setRohsValidDate(null);
                }
            }
        }

        if (!Objects.equal(updatedQuality.getIso9001Id(), newQuality.getIso9001Id())) {
            changedInfo.put(ChangedInfoKeys.qualityISO9001Id(), updatedQuality.getIso9001Id());
            updatedQuality.setIso9001Id(null);
        }
        if (!Objects.equal(updatedQuality.getIso9001AttachUrl(), newQuality.getIso9001AttachUrl())) {
            changedInfo.put(ChangedInfoKeys.qualityISO9001AttachUrl(), updatedQuality.getIso9001AttachUrl());
            updatedQuality.setIso9001AttachUrl(null);
        }

        if (updatedQuality.getIso9001ValidDate() != null) {
            DateTime updatedIso9001ValidDate = new DateTime(updatedQuality.getIso9001ValidDate());
            if (newQuality.getIso9001ValidDate() == null) {
                changedInfo.put(ChangedInfoKeys.qualityISO9001ValidDate(), updatedIso9001ValidDate.toString(FORMATTER));
                updatedQuality.setIso9001ValidDate(null);
            } else {
                DateTime oldIso9001ValidDate = new DateTime(newQuality.getIso9001ValidDate());
                if (!updatedIso9001ValidDate.equals(oldIso9001ValidDate)) {
                    changedInfo.put(ChangedInfoKeys.qualityISO9001ValidDate(), updatedIso9001ValidDate.toString(FORMATTER));
                    updatedQuality.setIso9001ValidDate(null);
                }
            }
        }

        if (!Objects.equal(updatedQuality.getIso14001Id(), newQuality.getIso14001Id())) {
            changedInfo.put(ChangedInfoKeys.qualityISO14001Id(), updatedQuality.getIso14001Id());
            updatedQuality.setIso14001Id(null);
        }
        if (!Objects.equal(updatedQuality.getIso14001AttachUrl(), newQuality.getIso14001AttachUrl())) {
            changedInfo.put(ChangedInfoKeys.qualityISO14001AttachUrl(), updatedQuality.getIso14001AttachUrl());
            updatedQuality.setIso14001AttachUrl(null);
        }

        if (updatedQuality.getIso14001ValidDate() != null) {
            DateTime updatedIso14001ValidDate = new DateTime(updatedQuality.getIso14001ValidDate());
            if (newQuality.getIso14001ValidDate() == null) {
                changedInfo.put(ChangedInfoKeys.qualityISO14001ValidDate(), updatedIso14001ValidDate.toString(FORMATTER));
                updatedQuality.setIso14001ValidDate(null);
            } else {
                DateTime oldIso14001ValidDate = new DateTime(newQuality.getIso14001ValidDate());
                if (!updatedIso14001ValidDate.equals(oldIso14001ValidDate)) {
                    changedInfo.put(ChangedInfoKeys.qualityISO14001ValidDate(), updatedIso14001ValidDate.toString(FORMATTER));
                    updatedQuality.setIso14001ValidDate(null);
                }
            }
        }

        if (!Objects.equal(updatedQuality.getTs16949Id(), newQuality.getTs16949Id())) {
            changedInfo.put(ChangedInfoKeys.qualityTS16949Id(), updatedQuality.getTs16949Id());
            updatedQuality.setTs16949Id(null);
        }
        if (!Objects.equal(updatedQuality.getTs16949AttachUrl(), newQuality.getTs16949AttachUrl())) {
            changedInfo.put(ChangedInfoKeys.qualityTS16949AttachUrl(), updatedQuality.getTs16949AttachUrl());
            updatedQuality.setTs16949AttachUrl(null);
        }

        if (updatedQuality.getTs16949ValidDate() != null) {
            DateTime updatedTs16949ValidDate = new DateTime(updatedQuality.getTs16949ValidDate());
            if (newQuality.getTs16949ValidDate() == null) {
                changedInfo.put(ChangedInfoKeys.qualityTS16949ValidDate(), updatedTs16949ValidDate.toString(FORMATTER));
                updatedQuality.setTs16949ValidDate(null);
            } else {
                DateTime oldTs16949ValidDate = new DateTime(newQuality.getTs16949ValidDate());
                if (!updatedTs16949ValidDate.equals(oldTs16949ValidDate)) {
                    changedInfo.put(ChangedInfoKeys.qualityTS16949ValidDate(), updatedTs16949ValidDate.toString(FORMATTER));
                    updatedQuality.setTs16949ValidDate(null);
                }
            }
        }

        if (!changedInfo.isEmpty()) {
            supplierInfoChangedDto.setChanged(true);
            supplierInfoChangedDto.setChangedInfo(changedInfo);
        }
        return supplierInfoChangedDto;
    }

    @Override
    public SupplierUpdatedInfoDto<CompanyExtraQuality> getNewCompanyExtraQuality(Long userId, CompanyExtraQuality oldCompanyExtraQuality) {

        SupplierUpdatedInfoDto<CompanyExtraQuality> supplierUpdatedInfoDto = new SupplierUpdatedInfoDto<CompanyExtraQuality>();
        Map<String, Object> oldValues = Maps.newHashMap();

        boolean qualityChanged = supplierChangedInfoRedisDao.tabInfoChanged(userId, ChangedInfoKeys.qualityTab());
        if (!qualityChanged) {
            supplierUpdatedInfoDto.setSupplierInfo(oldCompanyExtraQuality);
            supplierUpdatedInfoDto.setOldValues(oldValues);
            return supplierUpdatedInfoDto;
        }

        Map<String, String> changedInfo = supplierChangedInfoRedisDao.getChangedInfos(userId);

        CompanyExtraQuality newCompanyExtraQuality = new CompanyExtraQuality();
        BeanMapper.copy(oldCompanyExtraQuality, newCompanyExtraQuality);

        if (changedInfo.containsKey(ChangedInfoKeys.qualityRohsId())) {
            newCompanyExtraQuality.setRohsId(changedInfo.get(ChangedInfoKeys.qualityRohsId()));
            oldValues.put(ChangedInfoKeys.qualityRohsId(), oldCompanyExtraQuality.getRohsId());
        }
        if (changedInfo.containsKey(ChangedInfoKeys.qualityRohsAttachUrl())) {
            newCompanyExtraQuality.setRohsAttachUrl(changedInfo.get(ChangedInfoKeys.qualityRohsAttachUrl()));
            oldValues.put(ChangedInfoKeys.qualityRohsAttachUrl(), oldCompanyExtraQuality.getRohsAttachUrl());
        }
        if (changedInfo.containsKey(ChangedInfoKeys.qualityRohsValidDate())) {
            String rohsValidDate = changedInfo.get(ChangedInfoKeys.qualityRohsValidDate());
            newCompanyExtraQuality.setRohsValidDate(DateTime.parse(rohsValidDate, FORMATTER).toDate());
            oldValues.put(ChangedInfoKeys.qualityRohsValidDate(), oldCompanyExtraQuality.getRohsValidDate());
        }

        if (changedInfo.containsKey(ChangedInfoKeys.qualityISO9001Id())) {
            newCompanyExtraQuality.setIso9001Id(changedInfo.get(ChangedInfoKeys.qualityISO9001Id()));
            oldValues.put(ChangedInfoKeys.qualityISO9001Id(), oldCompanyExtraQuality.getIso9001Id());
        }
        if (changedInfo.containsKey(ChangedInfoKeys.qualityISO9001AttachUrl())) {
            newCompanyExtraQuality.setIso9001AttachUrl(changedInfo.get(ChangedInfoKeys.qualityISO9001AttachUrl()));
            oldValues.put(ChangedInfoKeys.qualityISO9001AttachUrl(), oldCompanyExtraQuality.getIso9001AttachUrl());
        }
        if (changedInfo.containsKey(ChangedInfoKeys.qualityISO9001ValidDate())) {
            String iso900ValidDate = changedInfo.get(ChangedInfoKeys.qualityISO9001ValidDate());
            newCompanyExtraQuality.setIso9001ValidDate(DateTime.parse(iso900ValidDate, FORMATTER).toDate());
            oldValues.put(ChangedInfoKeys.qualityISO9001ValidDate(), oldCompanyExtraQuality.getIso9001ValidDate());
        }

        if (changedInfo.containsKey(ChangedInfoKeys.qualityISO14001Id())) {
            newCompanyExtraQuality.setIso14001Id(changedInfo.get(ChangedInfoKeys.qualityISO14001Id()));
            oldValues.put(ChangedInfoKeys.qualityISO14001Id(), oldCompanyExtraQuality.getIso14001Id());
        }
        if (changedInfo.containsKey(ChangedInfoKeys.qualityISO14001AttachUrl())) {
            newCompanyExtraQuality.setIso14001AttachUrl(changedInfo.get(ChangedInfoKeys.qualityISO14001AttachUrl()));
            oldValues.put(ChangedInfoKeys.qualityISO14001AttachUrl(), oldCompanyExtraQuality.getIso14001AttachUrl());
        }
        if (changedInfo.containsKey(ChangedInfoKeys.qualityISO14001ValidDate())) {
            String iso1400ValidDate = changedInfo.get(ChangedInfoKeys.qualityISO14001ValidDate());
            newCompanyExtraQuality.setIso14001ValidDate(DateTime.parse(iso1400ValidDate, FORMATTER).toDate());
            oldValues.put(ChangedInfoKeys.qualityISO14001ValidDate(), oldCompanyExtraQuality.getIso14001ValidDate());
        }

        if (changedInfo.containsKey(ChangedInfoKeys.qualityTS16949Id())) {
            newCompanyExtraQuality.setTs16949Id(changedInfo.get(ChangedInfoKeys.qualityTS16949Id()));
            oldValues.put(ChangedInfoKeys.qualityTS16949Id(), oldCompanyExtraQuality.getTs16949Id());
        }
        if (changedInfo.containsKey(ChangedInfoKeys.qualityTS16949AttachUrl())) {
            newCompanyExtraQuality.setTs16949AttachUrl(changedInfo.get(ChangedInfoKeys.qualityTS16949AttachUrl()));
            oldValues.put(ChangedInfoKeys.qualityTS16949AttachUrl(), oldCompanyExtraQuality.getTs16949AttachUrl());
        }
        if (changedInfo.containsKey(ChangedInfoKeys.qualityTS16949ValidDate())) {
            String ts16949ValidDate = changedInfo.get(ChangedInfoKeys.qualityTS16949ValidDate());
            newCompanyExtraQuality.setTs16949ValidDate(DateTime.parse(ts16949ValidDate, FORMATTER).toDate());
            oldValues.put(ChangedInfoKeys.qualityTS16949ValidDate(), oldCompanyExtraQuality.getTs16949ValidDate());
        }

        supplierUpdatedInfoDto.setSupplierInfo(newCompanyExtraQuality);
        supplierUpdatedInfoDto.setOldValues(oldValues);
        return supplierUpdatedInfoDto;
    }

    @Override
    public SupplierInfoChangedDto financeChanged(Long userId, Finance updatedFinance, Finance oldFinance) {
        SupplierInfoChangedDto supplierInfoChangedDto = new SupplierInfoChangedDto();

        Finance newFinance = getNewFinance(userId, oldFinance).getSupplierInfo();

        Map<String, String> changedInfo = Maps.newHashMap();

        if (!Objects.equal(updatedFinance.getCountry(), newFinance.getCountry())) {
            changedInfo.put(ChangedInfoKeys.financeCountry(), String.valueOf(updatedFinance.getCountry()));
            updatedFinance.setCountry(null);
        }
        if (!Objects.equal(updatedFinance.getBankAccount(), newFinance.getBankAccount())) {
            changedInfo.put(ChangedInfoKeys.financeBankAccount(), updatedFinance.getBankAccount());
            updatedFinance.setBankAccount(null);
        }
        if (!Objects.equal(updatedFinance.getBankCode(), newFinance.getBankCode())) {
            changedInfo.put(ChangedInfoKeys.financeBankCode(), updatedFinance.getBankCode());
            updatedFinance.setBankCode(null);
        }
        if (!Objects.equal(updatedFinance.getCoinType(), newFinance.getCoinType())) {
            changedInfo.put(ChangedInfoKeys.financeCoinType(), String.valueOf(updatedFinance.getCoinType()));
            updatedFinance.setCoinType(null);
        }
        if (!Objects.equal(updatedFinance.getOpenLicense(), newFinance.getOpenLicense())) {
            changedInfo.put(ChangedInfoKeys.financeOpenLicense(), updatedFinance.getOpenLicense());
            updatedFinance.setOpenLicense(null);
        }
        if (!Objects.equal(updatedFinance.getRecentFinance(), newFinance.getRecentFinance())) {
            changedInfo.put(ChangedInfoKeys.financeRecentFinance(), updatedFinance.getRecentFinance());
            updatedFinance.setRecentFinance(null);
        }
        if (!Objects.equal(updatedFinance.getOpeningBank(), newFinance.getOpeningBank())) {
            changedInfo.put(ChangedInfoKeys.financeOpeningBank(), updatedFinance.getOpeningBank());
            updatedFinance.setOpeningBank(null);
        }

        if (!changedInfo.isEmpty()) {
            supplierInfoChangedDto.setChanged(true);
            supplierInfoChangedDto.setChangedInfo(changedInfo);
        }

        return supplierInfoChangedDto;
    }

    @Override
    public SupplierUpdatedInfoDto<Finance> getNewFinance(Long userId, Finance oldFinance) {

        SupplierUpdatedInfoDto<Finance> supplierUpdatedInfoDto = new SupplierUpdatedInfoDto<Finance>();
        Map<String, Object> oldValues = Maps.newHashMap();

        boolean financeChanged = supplierChangedInfoRedisDao.tabInfoChanged(userId, ChangedInfoKeys.financeTab());
        if (!financeChanged) {
            supplierUpdatedInfoDto.setSupplierInfo(oldFinance);
            supplierUpdatedInfoDto.setOldValues(oldValues);
            return supplierUpdatedInfoDto;
        }

        Map<String, String> changedInfo = supplierChangedInfoRedisDao.getChangedInfos(userId);

        Finance newFinance = new Finance();
        BeanMapper.copy(oldFinance, newFinance);

        if (changedInfo.containsKey(ChangedInfoKeys.financeOpeningBank())) {
            newFinance.setOpeningBank(changedInfo.get(ChangedInfoKeys.financeOpeningBank()));
            oldValues.put(ChangedInfoKeys.financeOpeningBank(), oldFinance.getOpeningBank());
        }
        if (changedInfo.containsKey(ChangedInfoKeys.financeOpenLicense())) {
            newFinance.setOpenLicense(changedInfo.get(ChangedInfoKeys.financeOpenLicense()));
            oldValues.put(ChangedInfoKeys.financeOpenLicense(), oldFinance.getOpenLicense());
        }
        if (changedInfo.containsKey(ChangedInfoKeys.financeRecentFinance())) {
            newFinance.setRecentFinance(changedInfo.get(ChangedInfoKeys.financeRecentFinance()));
            oldValues.put(ChangedInfoKeys.financeRecentFinance(), oldFinance.getRecentFinance());
        }
        if (changedInfo.containsKey(ChangedInfoKeys.financeCoinType())) {
            String coinType = changedInfo.get(ChangedInfoKeys.financeCoinType());
            newFinance.setCoinType("null".equals(coinType) ? null : Integer.parseInt(coinType));
            oldValues.put(ChangedInfoKeys.financeCoinType(), oldFinance.getCoinType());
        }
        if (changedInfo.containsKey(ChangedInfoKeys.financeBankCode())) {
            newFinance.setBankCode(changedInfo.get(ChangedInfoKeys.financeBankCode()));
            oldValues.put(ChangedInfoKeys.financeBankCode(), oldFinance.getBankCode());
        }
        if (changedInfo.containsKey(ChangedInfoKeys.financeBankAccount())) {
            newFinance.setBankAccount(changedInfo.get(ChangedInfoKeys.financeBankAccount()));
            oldValues.put(ChangedInfoKeys.financeBankAccount(), oldFinance.getBankAccount());
        }
        if (changedInfo.containsKey(ChangedInfoKeys.financeCountry())) {
            String country = changedInfo.get(ChangedInfoKeys.financeCountry());
            newFinance.setCountry("null".equals(country) ? null : Integer.parseInt(country));
            oldValues.put(ChangedInfoKeys.financeCountry(), oldFinance.getCountry());
        }

        supplierUpdatedInfoDto.setSupplierInfo(newFinance);
        supplierUpdatedInfoDto.setOldValues(oldValues);
        return supplierUpdatedInfoDto;
    }

    @Override
    public void updateModifyInfoWaitingForApprove(Long userId, Integer approveStatus) {

        if (Objects.equal(approveStatus, User.ApproveStatus.OK.value())) {
            User updatedUser = new User();
            updatedUser.setId(userId);
            updatedUser.setApproveStatus(User.ApproveStatus.MODIFY_INFO_WAITING_FOR_APPROVE.value());
            updatedUser.setLastSubmitApprovalAt(new Date());
            userDao.update(updatedUser);
        }
    }

    @Override
    public Response<SupplierInfoChanged> checkSupplierInfoChanged(Long userId) {
        Response<SupplierInfoChanged> result = new Response<SupplierInfoChanged>();
        if (userId == null) {
            log.error("use id can not be null");
            result.setError("user.id.not.null.fail");
            return result;
        }

        try {

            SupplierInfoChanged supplierInfoChanged = new SupplierInfoChanged();
            supplierInfoChanged.setCompanyChanged(supplierChangedInfoRedisDao.tabInfoChanged(userId, ChangedInfoKeys.companyTab()));
            supplierInfoChanged.setPaperworkChanged(supplierChangedInfoRedisDao.tabInfoChanged(userId, ChangedInfoKeys.paperworkTab()));
            supplierInfoChanged.setContactInfoChanged(supplierChangedInfoRedisDao.tabInfoChanged(userId, ChangedInfoKeys.contactInfoTab()));
            supplierInfoChanged.setFinanceChanged(supplierChangedInfoRedisDao.tabInfoChanged(userId, ChangedInfoKeys.financeTab()));
            supplierInfoChanged.setQualityChanged(supplierChangedInfoRedisDao.tabInfoChanged(userId, ChangedInfoKeys.qualityTab()));

            result.setResult(supplierInfoChanged);
        } catch (Exception e) {
            log.error("fail to check supplier info changed where userId={},cause:{}", userId, Throwables.getStackTraceAsString(e));
            result.setError("check.supplier.info.changed.fail");
        }

        return result;
    }

    private boolean mainBusinessChanged(List<CompanyMainBusiness> updatedMainBusinesses, List<CompanyMainBusiness> oldMainBusinesses) {

        if (updatedMainBusinesses != null && oldMainBusinesses != null) {

            if (!Objects.equal(updatedMainBusinesses.size(), oldMainBusinesses.size())) {
                return true;
            }

            List<Long> newMainBusinessIds = Lists.transform(updatedMainBusinesses, new Function<CompanyMainBusiness, Long>() {
                @Override
                public Long apply(CompanyMainBusiness companyMainBusiness) {
                    return companyMainBusiness.getMainBusinessId();
                }
            });

            List<Long> oldMainBusinessIds = Lists.transform(oldMainBusinesses, new Function<CompanyMainBusiness, Long>() {
                @Override
                public Long apply(CompanyMainBusiness companyMainBusiness) {
                    return companyMainBusiness.getMainBusinessId();
                }
            });

            if (!oldMainBusinessIds.containsAll(newMainBusinessIds)) {
                return true;
            }
        }

        return false;
    }

    private boolean supplyParkChanged(List<CompanySupplyPark> updatedSupplyParks, List<CompanySupplyPark> oldSupplyParks) {

        if (updatedSupplyParks != null && oldSupplyParks != null) {

            if (!Objects.equal(updatedSupplyParks.size(), oldSupplyParks.size())) {
                return true;
            }

            List<Long> newSupplyParkIds = Lists.transform(updatedSupplyParks, new Function<CompanySupplyPark, Long>() {
                @Override
                public Long apply(CompanySupplyPark companySupplyPark) {
                    return companySupplyPark.getSupplyParkId();
                }
            });

            List<Long> oldSupplyParkIds = Lists.transform(oldSupplyParks, new Function<CompanySupplyPark, Long>() {
                @Override
                public Long apply(CompanySupplyPark companySupplyPark) {
                    return companySupplyPark.getSupplyParkId();
                }
            });

            if (!oldSupplyParkIds.containsAll(newSupplyParkIds)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 检查营业执照修改情况
     */
    private void checkBusinessLicense(Map<String, String> changedInfo, PaperworkDto updatedPaperwork, PaperworkDto oldPaperwork) {

        if (!Objects.equal(updatedPaperwork.getBusinessLicense(), oldPaperwork.getBusinessLicense())) {
            changedInfo.put(ChangedInfoKeys.companyBusinessLicense(), updatedPaperwork.getBusinessLicense());
        }
        if (!Objects.equal(updatedPaperwork.getBusinessLicenseId(), oldPaperwork.getBusinessLicenseId())) {
            changedInfo.put(ChangedInfoKeys.companyBusinessLicenseId(), updatedPaperwork.getBusinessLicenseId());
        }

        DateTime updatedBlDate = new DateTime(updatedPaperwork.getBlDate());
        if (oldPaperwork.getBlDate() == null) {
            changedInfo.put(ChangedInfoKeys.companyBusinessLicenseDate(), updatedBlDate.toString(FORMATTER));
        } else {
            DateTime oldBlDate = new DateTime(oldPaperwork.getBlDate());
            if (!updatedBlDate.isEqual(oldBlDate)) {
                changedInfo.put(ChangedInfoKeys.companyBusinessLicenseDate(), updatedBlDate.toString(FORMATTER));
            }
        }
    }

    /**
     * 检查税务证修改情况
     */
    private void checkTaxNo(Map<String, String> changedInfo, PaperworkDto updatedPaperwork, PaperworkDto oldPaperwork) {

        if (!Objects.equal(updatedPaperwork.getTaxNo(), oldPaperwork.getTaxNo())) {
            changedInfo.put(ChangedInfoKeys.companyTaxNo(), updatedPaperwork.getTaxNo());
        }
        if (!Objects.equal(updatedPaperwork.getTaxNoId(), oldPaperwork.getTaxNoId())) {
            changedInfo.put(ChangedInfoKeys.companyTaxNoId(), updatedPaperwork.getTaxNoId());
        }

    }

    /**
     * 检查组织机构修改情况
     */
    private void checkOrgCert(Map<String, String> changedInfo, PaperworkDto updatedPaperwork, PaperworkDto oldPaperwork) {

        if (!Objects.equal(updatedPaperwork.getOrgCert(), oldPaperwork.getOrgCert())) {
            changedInfo.put(ChangedInfoKeys.companyOrgCert(), updatedPaperwork.getOrgCert());
        }
        if (!Objects.equal(updatedPaperwork.getOrgCertId(), oldPaperwork.getOrgCertId())) {
            changedInfo.put(ChangedInfoKeys.companyOrgCertId(), updatedPaperwork.getOrgCertId());
        }

        DateTime updatedOcDate = new DateTime(updatedPaperwork.getOcDate());
        if (oldPaperwork.getOcDate() == null) {
            changedInfo.put(ChangedInfoKeys.companyOrgCertDate(), updatedOcDate.toString(FORMATTER));
        } else {
            DateTime oldOcDate = new DateTime(oldPaperwork.getOcDate());
            if (!updatedOcDate.isEqual(oldOcDate)) {
                changedInfo.put(ChangedInfoKeys.companyOrgCertDate(), updatedOcDate.toString(FORMATTER));
            }
        }
    }

}
