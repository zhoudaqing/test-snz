package io.terminus.snz.user.tool;

import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import io.terminus.snz.user.dao.AddressDao;
import io.terminus.snz.user.dto.SupplierImportDto;
import io.terminus.snz.user.model.*;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Author:Guo Chaopeng
 * Created on 14-6-21.
 */
@Slf4j
@Component
public class ExcelTransform {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd");

    @Autowired
    private AddressDao addressDao;

    @Autowired
    private AnalyzeExcelUtil analyzeExcelUtil;

    public List<SupplierTQRDCInfo> getSupplierTQRDCInfo(String excelPath) {

        log.debug("analyze excel file.");
        //解析excel信息到对象中
        List<SupplierTQRDCInfo> supplierTQRDCInfos = analyzeExcelUtil.analyzePath(
                excelPath,
                2,
                new AnalyzeExcelUtil.AnalyzeAction<SupplierTQRDCInfo>() {
                    @Override
                    public SupplierTQRDCInfo transform(String[] info) {
                        SupplierTQRDCInfo supplierTQRDCInfo = new SupplierTQRDCInfo();
                        try {

                            for (int i = 0; i < info.length; i++) {
                                switch (i) {
                                    case 0:
                                        supplierTQRDCInfo.setMonth(info[i]);
                                        break;
                                    case 1:
                                        supplierTQRDCInfo.setSupplierCode(info[i]);
                                        break;
                                    case 2:
                                        supplierTQRDCInfo.setSupplierName(info[i]);
                                        break;
                                    case 3:
                                        supplierTQRDCInfo.setModule(info[i]);
                                        break;
                                    /*
                                    case 4:
                                        supplierTQRDCInfo.setRank(Math.round(Float.parseFloat(info[i])));
                                        break;
                                    case 5:
                                        supplierTQRDCInfo.setLocation(info[i]);
                                        break;
                                    case 6:
                                        supplierTQRDCInfo.setCompositeScore(Math.round(Float.parseFloat(info[i])));
                                        break;
                                    case 7:
                                        supplierTQRDCInfo.setTechScore(Math.round(Float.parseFloat(info[i])));
                                        break;
                                    case 8:
                                        supplierTQRDCInfo.setDelayDays(Math.round(Float.parseFloat(info[i])));
                                        break;
                                    case 9:
                                        supplierTQRDCInfo.setNewProductPass(Math.round(Float.parseFloat(info[i])));
                                        break;
                                    case 10:
                                        supplierTQRDCInfo.setQualityScore(Math.round(Float.parseFloat(info[i])));
                                        break;
                                    case 11:
                                        supplierTQRDCInfo.setLiveBad(Math.round(Float.parseFloat(info[i])));
                                        break;
                                    case 12:
                                        supplierTQRDCInfo.setMarketBad(Math.round(Float.parseFloat(info[i])));
                                        break;
                                    case 13:
                                        supplierTQRDCInfo.setRespScore(Math.round(Float.parseFloat(info[i])));
                                        break;
                                    case 14:
                                        supplierTQRDCInfo.setRequirementResp(Math.round(Float.parseFloat(info[i]) * 100));
                                        break;
                                    case 15:
                                        supplierTQRDCInfo.setDeliverScore(Math.round(Float.parseFloat(info[i])));
                                        break;
                                    case 16:
                                        supplierTQRDCInfo.setDeliverDiff(Math.round(Float.parseFloat(info[i]) * 100));
                                        break;
                                    case 17:
                                        supplierTQRDCInfo.setCostScore(Math.round(Float.parseFloat(info[i])));
                                        break;
                                    case 18:
                                        supplierTQRDCInfo.setIncrement(Math.round(Float.parseFloat(info[i])));
                                        break;
                                        */
                                    default:
                                        log.warn("unknown column");
                                }
                            }
                        } catch (Exception e) {
                            log.error("transform excel info to object failed, error code={}", Throwables.getStackTraceAsString(e));
                            throw new RuntimeException(e);
                        }
                        return supplierTQRDCInfo;
                    }
                });
        log.debug("transform excel success.");
        return supplierTQRDCInfos;
    }

    public List<SupplierImportDto> getSupplier(String excelPath) {

        log.debug("analyze excel file.");

        final Map<String, Integer> countryMap = getCountryMap();
        final Integer otherCountryId = countryMap.get("其他");

        final Map<String, Integer> provinceMap = getProvinceMap();

        final Map<String, Integer> natureMap = getNatureMap();
        final Integer otherNatureId = natureMap.get("其他");

        //解析excel信息到对象中
        List<SupplierImportDto> supplierImportDtos = analyzeExcelUtil.analyzePath(
                excelPath,
                1,
                new AnalyzeExcelUtil.AnalyzeAction<SupplierImportDto>() {
                    @Override
                    public SupplierImportDto transform(String[] info) {

                        User user = new User();
                        Company company = new Company();
                        CompanyRank companyRank = new CompanyRank();
                        ContactInfo contactInfo = new ContactInfo();

                        try {

                            for (int i = 0; i < info.length; i++) {

                                switch (i) {
                                    /*
                                    case 2:
                                        company.setCorporation(info[i]);
                                        break;
                                    case 3:
                                        company.setCorpAddr(info[i]);
                                        break;
                                    case 4:
                                        company.setGroupName(info[i]);
                                        break;
                                    case 5:
                                        company.setGroupAddr(info[i]);
                                        break;
                                    case 6:
                                        company.setInitAgent(info[i]);
                                        break;
                                    case 7:
                                        company.setDesc(info[i]);
                                        break;
                                        */
                                    case 8:
                                        Integer countryId =
                                                countryMap.get(info[i]) == null ?
                                                        otherCountryId : countryMap.get(info[i]);
                                        company.setRegCountry(countryId);
                                        break;
                                    case 9:
                                        company.setRegProvince(provinceMap.get(info[i]));
                                        break;
                                    case 10:
                                        company.setPersonScale(info[i]);
                                        break;
                                    case 11:
                                        //主营业务
                                        break;
                                    case 12:
                                        company.setOfficialWebsite(info[i]);
                                        break;
                                    case 13:
                                        Integer natureId =
                                                natureMap.get(info[i]) == null ? otherNatureId : natureMap.get(info[i]);
                                        company.setNature(natureId);
                                        break;
                                    /*
                                    case 14:
                                        Integer worldTop =
                                                Strings.isNullOrEmpty(info[i]) ? null : Math.round(Float.parseFloat(info[i]));
                                        company.setWorldTop(worldTop);
                                        break;
                                    case 15:
                                        Integer listedStatus =
                                                Strings.isNullOrEmpty(info[i]) ? null : Math.round(Float.parseFloat(info[i]));
                                        company.setListedStatus(listedStatus);
                                        break;
                                    case 16:
                                        company.setListedRegion(info[i]);
                                        break;
                                    case 17:
                                        company.setTicker(info[i]);
                                        break;
                                    case 18:
                                        company.setBusinessLicenseId(info[i]);
                                        break;
                                    case 19:
                                        company.setBlDate(format(info[i]));
                                        break;
                                    case 20:
                                        company.setBusinessLicense(info[i]);
                                        break;
                                    case 22:
                                        company.setOrgCertId(info[i]);
                                        break;
                                        */
                                    case 23:
                                        company.setOcDate(format(info[i]));
                                        break;
                                    /*
                                    case 24:
                                        company.setOrgCert(info[i]);
                                        break;
                                    case 26:
                                        company.setTaxNoId(info[i]);
                                        break;
                                    case 27:
                                        company.setTaxNo(info[i]);//excel缺失税号有效期
                                        break;
                                    case 33:
                                        Integer inRank =
                                                Strings.isNullOrEmpty(info[i]) ?
                                                        null : Math.round(Float.parseFloat(info[i]));
                                        companyRank.setInRank(inRank);
                                        break;
                                    case 34:
                                        companyRank.setInRankOrg(info[i]);
                                        break;
                                    case 35:
                                        companyRank.setInRankFile(info[i]);
                                        break;
                                    case 36:
                                        companyRank.setInRankFileName(info[i]);
                                        break;
                                    case 37:
                                        companyRank.setInRankRemark(info[i]);
                                        break;
                                    case 38:
                                        Integer outRank =
                                                Strings.isNullOrEmpty(info[i]) ?
                                                        null : Math.round(Float.parseFloat(info[i]));
                                        companyRank.setOutRank(outRank);
                                        break;
                                    case 39:
                                        companyRank.setOutRankOrg(info[i]);
                                        break;
                                    case 40:
                                        companyRank.setOutRankFile(info[i]);
                                        break;
                                    case 41:
                                        companyRank.setOutRankFileName(info[i]);
                                        break;
                                    case 42:
                                        companyRank.setOutRankRemark(info[i]);
                                        break;
                                    case 43:
                                        //主要合作客户
                                        break;
                                    case 44:
                                        contactInfo.setDepartment(info[i]);
                                        break;
                                    case 45:
                                        contactInfo.setName(info[i]);
                                        break;
                                    case 46:
                                        contactInfo.setDuty(info[i]);
                                        break;
                                    case 47:
                                        contactInfo.setOfficePhone(info[i]);
                                        break;
                                    case 48:
                                        String mobile =
                                                info[i].length() > 11 ? info[i].substring(0, 11) : info[i];
                                        contactInfo.setMobile(mobile);
                                        break;
                                    case 49:
                                        contactInfo.setEmail(info[i]);
                                        break;
                                    case 50:
                                        user.setNick(info[i]);
                                        break;
                                    case 52:
                                        String supplierCode = "无".equals(info[i]) ? null : info[i];
                                        company.setSupplierCode(supplierCode);
                                        break;
                                        */
                                    default:
                                        log.warn("unknown column");

                                }
                            }
                        } catch (Exception e) {
                            log.error("transform excel info to object failed, error code={}", Throwables.getStackTraceAsString(e));
                            throw new RuntimeException(e);
                        }

                        SupplierImportDto supplierImportDto = new SupplierImportDto();

                        user.setType(User.Type.SUPPLIER.value());
                        user.setStatus(User.Status.OK.value());
                        user.setApproveStatus(User.ApproveStatus.OK.value());
                        user.setOrigin(User.Origin.CLOUD.value());
                        user.setEncryptedPassword("123456");
                        user.setRoleStr(User.Type.SUPPLIER.name());
                        user.setRefuseStatus(User.RefuseStatus.NOT_REFUSED.value());
                        user.setQualifyStatus(User.QualifyStatus.NO_SUBMISSION.value()); // 初始未提交申请
                        user.setMobile(Strings.nullToEmpty(contactInfo.getMobile()));
                        user.setEmail(Strings.nullToEmpty(contactInfo.getEmail()));

                        company.setCustomers("{\"appliance\":[{\"remark\":\"\",\"percentage\":\"\"," +
                                "\"passivePercentage\":\"\"}],\"car\":[{\"remark\":\"\",\"percentage\":\"\"," +
                                "\"passivePercentage\":\"\"}],\"other\":[{\"remark\":\"\",\"percentage\":\"\"," +
                                "\"passivePercentage\":\"\"}]}");
                        company.setParticipateCount(0);

                        supplierImportDto.setUser(user);
                        supplierImportDto.setCompany(company);
                        supplierImportDto.setCompanyRank(companyRank);
                        supplierImportDto.setContactInfo(contactInfo);

                        return supplierImportDto;
                    }
                });
        log.debug("transform excel success.");
        return supplierImportDtos;
    }

    public List<MainBusinessApprover> getMainBusinessApprover(String excelPath) {

        log.debug("analyze excel file.");
        //解析excel信息到对象中
        List<MainBusinessApprover> mainBusinessApprovers = analyzeExcelUtil.analyzePath(
                excelPath,
                2,
                new AnalyzeExcelUtil.AnalyzeAction<MainBusinessApprover>() {
                    @Override
                    public MainBusinessApprover transform(String[] info) {

                        MainBusinessApprover mainBusinessApprover = new MainBusinessApprover();

                        try {

                            for (int i = 0; i < info.length; i++) {

                                switch (i) {
                                    case 4:
                                        mainBusinessApprover.setMainBusinessName(info[i]);
                                        break;
                                    case 5:
                                        mainBusinessApprover.setMemberName(info[i]);
                                        break;
                                    case 6:
                                        mainBusinessApprover.setMemberId(info[i]);
                                        break;
                                    case 7:
                                        mainBusinessApprover.setLeaderName(info[i]);
                                        break;
                                    case 8:
                                        mainBusinessApprover.setLeaderId(info[i]);
                                        break;
                                    default:
                                        log.warn("unknown column");
                                }
                            }
                        } catch (Exception e) {
                            log.error("transform excel info to object failed, error code={}", Throwables.getStackTraceAsString(e));
                            throw new RuntimeException(e);
                        }

                        return mainBusinessApprover;
                    }
                });
        log.debug("transform excel success.");
        return mainBusinessApprovers;
    }


    private Date format(String date) throws ParseException {
        if (Strings.isNullOrEmpty(date)) {
            return null;
        }
        return DATE_FORMAT.parseDateTime(date.substring(0, 10)).toDate();
    }

    private Map<String, Integer> getCountryMap() {
        Map<String, Integer> map = Maps.newHashMap();

        List<Address> countries = addressDao.findByLevel(0);

        if (countries != null) {
            for (Address country : countries) {
                map.put(country.getName(), country.getId());
            }
        }
        return map;
    }

    private Map<String, Integer> getProvinceMap() {
        Map<String, Integer> map = Maps.newHashMap();

        List<Address> provinces = addressDao.findByLevel(1);

        if (provinces != null) {
            for (Address province : provinces) {
                map.put(province.getName(), province.getId());
            }
        }
        return map;
    }

    private Map<String, Integer> getNatureMap() {
        Map<String, Integer> map = Maps.newHashMap();

        map.put("国有", 1);
        map.put("集体所有制", 2);
        map.put("民营", 3);
        map.put("私营", 4);
        map.put("中外合资", 5);
        map.put("外商独资", 6);
        map.put("其他", 7);

        return map;
    }

}
