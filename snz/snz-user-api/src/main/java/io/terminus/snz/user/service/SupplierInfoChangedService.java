package io.terminus.snz.user.service;

import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.dto.*;
import io.terminus.snz.user.model.*;

import java.util.List;


/**
 * Author:Guo Chaopeng
 * Created on 14-8-15.
 */
public interface SupplierInfoChangedService {

    /**
     * 是否需要检查信息修改情况
     *
     * @param approveStatus 用户的审核状态
     * @return 需要返回true，否则false
     */
    public boolean needCheckChanged(Integer approveStatus);

    /**
     * 检查公司基本信息更改情况
     *@param userId 用户编号
     * @param updatedCompanyDto 要更新的公司基本信息
     * @param oldCompany        原有的公司信息
     * @param oldMainBusinesses 原有的主营业务
     * @param oldSupplyParks    原有的可供货园区
     * @return 检查结果
     */
    public SupplierInfoChangedDto baseCompanyChanged(Long userId,CompanyDto updatedCompanyDto, Company oldCompany, List<CompanyMainBusiness> oldMainBusinesses, List<CompanySupplyPark> oldSupplyParks);

    /**
     * 获取新的公司基本信息和字段修改之前的值
     *
     * @param userId            用户编号
     * @param oldCompany        原公司信息
     * @param oldMainBusinesses 原主营业务
     * @param oldSupplyParks    原可供货园区
     * @return 新的公司基本信息和字段修改之前的值
     */
    public SupplierUpdatedInfoDto<CompanyDto> getNewBaseCompany(Long userId, Company oldCompany, List<CompanyMainBusiness> oldMainBusinesses, List<CompanySupplyPark> oldSupplyParks);

    /**
     * 检查三证修改情况
     *
     * @param userId           用户编号
     * @param updatedPaperwork 待更新的三证信息
     * @param oldPaperwork     原有的三证信息
     * @return 检查结果
     */
    public SupplierInfoChangedDto paperworkChanged(Long userId, PaperworkDto updatedPaperwork, Company oldPaperwork);

    /**
     * 获取新的三证信息和字段修改之前的值
     *
     * @param userId       用户编号
     * @param oldPaperwork 原有的三证信息
     * @return 新的三证信息和字段修改之前的值
     */
    public SupplierUpdatedInfoDto<PaperworkDto> getNewPaperwork(Long userId, Company oldPaperwork);

    /**
     * 检查联系人信息更改情况
     *@param userId 用户编号
     * @param updatedContactInfo 待更新的联系人信息
     * @param oldContactInfo     原有的联系人信息
     * @return 检查结果
     */
    public SupplierInfoChangedDto contactInfoChanged(Long userId,ContactInfo updatedContactInfo, ContactInfo oldContactInfo);

    /**
     * 获取新的联系人信息和字段修改之前的值
     *
     * @param userId         用户编号
     * @param oldContactInfo 原来的联系人信息
     * @return 新的联系人信息和字段修改之前的值
     */
    public SupplierUpdatedInfoDto<ContactInfo> getNewContactInfo(Long userId, ContactInfo oldContactInfo);

    /**
     * 检查质量保证信息更改情况
     *
     * @param userId         用户编号
     * @param updatedQuality 待更新的质量保证信息
     * @param oldQuality     原有的质量保证信息
     * @return 检查结果
     */
    public SupplierInfoChangedDto qualityChanged(Long userId, CompanyExtraQuality updatedQuality, CompanyExtraQuality oldQuality);

    /**
     * 获取信息的质量保证信息和字段修改之前的值
     *
     * @param userId                 用户编号
     * @param oldCompanyExtraQuality 原来的质量保证信息
     * @return 信息的质量保证信息和字段修改之前的值
     */
    public SupplierUpdatedInfoDto<CompanyExtraQuality> getNewCompanyExtraQuality(Long userId, CompanyExtraQuality oldCompanyExtraQuality);

    /**
     * 检查财务更改情况
     *
     * @param userId         用户编号
     * @param updatedFinance 待更新的财务信息
     * @param oldFinance     原财务信息
     * @return 检查结果
     */
    public SupplierInfoChangedDto financeChanged(Long userId, Finance updatedFinance, Finance oldFinance);

    /**
     * 获取新的财务信息和字段修改之前的值
     *
     * @param userId     用户编号
     * @param oldFinance 原财务信息
     * @return 新的财务信息和字段修改之前的值
     */
    public SupplierUpdatedInfoDto<Finance> getNewFinance(Long userId, Finance oldFinance);

    /**
     * 将供应商更新为修改信息待审核状态
     *
     * @param userId        user id
     * @param approveStatus 供应商当前的审核状态
     */
    public void updateModifyInfoWaitingForApprove(Long userId, Integer approveStatus);

    /**
     * 检查供应商各项信息是否修改
     *
     * @param userId user id
     * @return 检查结果
     */
    @Export(paramNames = {"userId"})
    public Response<SupplierInfoChanged> checkSupplierInfoChanged(Long userId);

}
