package io.terminus.snz.user.service;

import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.dto.CompanyDto;
import io.terminus.snz.user.dto.CompanyExtraQualityDto;
import io.terminus.snz.user.dto.FinanceDto;
import io.terminus.snz.user.dto.PaperworkDto;
import io.terminus.snz.user.model.CompanyExtraDelivery;
import io.terminus.snz.user.model.CompanyExtraRD;
import io.terminus.snz.user.model.CompanyRank;
import io.terminus.snz.user.model.ContactInfo;

/**
 * Author:Guo Chaopeng
 * Created on 14-8-26.
 */
public interface SupplierInfoStashService {

    /**
     * 暂存公司信息
     *
     * @param baseUser   当前登录用户
     * @param companyDto 公司信息
     * @return 是否成功
     */
    @Export(paramNames = {"baseUser", "companyDto"})
    public Response<Boolean> stashCompany(BaseUser baseUser, CompanyDto companyDto);

    /**
     * 暂存三证信息
     *
     * @param baseUser     当前登录用户
     * @param paperworkDto 三证信息
     * @return 是否成功
     */
    @Export(paramNames = {"baseUser", "paperworkDto"})
    public Response<Boolean> stashPaperwork(BaseUser baseUser, PaperworkDto paperworkDto);

    /**
     * 暂存联系人信息
     *
     * @param baseUser    当前登录用户
     * @param contactInfo 联系人信息
     * @return 是否成功
     */
    @Export(paramNames = {"baseUser", "contactInfo"})
    public Response<Boolean> stashContactInfo(BaseUser baseUser, ContactInfo contactInfo);

    /**
     * 暂存排名信息
     *
     * @param baseUser    当前登录用户
     * @param companyRank 公司排名信息
     * @return 是否成功
     */
    @Export(paramNames = {"baseUser", "companyRank"})
    public Response<Boolean> stashCompanyRank(BaseUser baseUser, CompanyRank companyRank);

    /**
     * 暂存财务信息
     *
     * @param baseUser   当前登录用户
     * @param financeDto 财务信息
     * @return 是否成功
     */
    @Export(paramNames = {"baseUser", "financeDto"})
    public Response<Boolean> stashFinance(BaseUser baseUser, FinanceDto financeDto);

    /**
     * 暂存研发信息
     *
     * @param baseUser       当前登录用户
     * @param companyExtraRD 研发信息
     * @return 是否成功
     */
    @Export(paramNames = {"baseUser", "companyExtraRD"})
    public Response<Boolean> stashRD(BaseUser baseUser, CompanyExtraRD companyExtraRD);

    /**
     * 暂存质量保证信息
     *
     * @param baseUser               当前登录用户
     * @param companyExtraQualityDto 质量保证信息
     * @return 是否成功
     */
    @Export(paramNames = {"baseUser", "companyExtraQuality"})
    public Response<Boolean> stashQuality(BaseUser baseUser, CompanyExtraQualityDto companyExtraQualityDto);

    /**
     * 暂存响应信息
     *
     * @param baseUser             当前登录用户
     * @param companyExtraDelivery 响应信息
     * @return 是否成功
     */
    @Export(paramNames = {"baseUser", "companyExtraDelivery"})
    public Response<Boolean> stashResponse(BaseUser baseUser, CompanyExtraDelivery companyExtraDelivery);

}
