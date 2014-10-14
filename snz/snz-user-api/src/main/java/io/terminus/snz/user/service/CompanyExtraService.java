package io.terminus.snz.user.service;

import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.dto.CompanyExtraQualityDto;
import io.terminus.snz.user.dto.SupplierUpdatedInfoDto;
import io.terminus.snz.user.model.CompanyExtraDelivery;
import io.terminus.snz.user.model.CompanyExtraRD;
import io.terminus.snz.user.model.CompanyExtraResponse;
import io.terminus.snz.user.model.CompanyExtraScaleAndCost;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 6/23/14
 */
public interface CompanyExtraService {

    /**
     * 更新或创建公司研发能力信息
     * @param companyExtraRD 待更新的研发能力信息
     * @return 是否更新成功
     */
    @Export(paramNames = {"companyExtraRD"})
    Response<Boolean> updateOrCreateRD(CompanyExtraRD companyExtraRD);

    /**
     * 根据用户搜寻公司研发能力信息（包含暂存的信息）
     * @param user 公司所对应的用户（唯一）
     * @return 所要搜寻的信息
     */
    @Export(paramNames = {"user"})
    Response<CompanyExtraRD> findRDByUser(BaseUser user);

    /**
     * 根据用户id搜寻公司研发能力信息
     * @param userId    公司所对应的用户id
     * @return 所要搜寻的信息
     */
    @Export(paramNames = {"userId"})
    Response<SupplierUpdatedInfoDto<CompanyExtraRD>> findRDByUserId(Long userId);

    /**
     * 更新或创建公司质量能力信息
     * @param companyExtraQualityDto 待更新的质量能力信息
     * @return 是否更新成功
     */
    @Export(paramNames = {"companyExtraQuality"})
    Response<Boolean> updateOrCreateQuality(CompanyExtraQualityDto companyExtraQualityDto);

    /**
     * 根据用户搜寻公司质量能力信息（包含暂存和未审核通过的信息）
     * @param user 公司所对应的用户（唯一）
     * @return 所要搜寻的信息
     */
    @Export(paramNames = {"user"})
    Response<CompanyExtraQualityDto> findQualityByUser(BaseUser user);

    /**
     * 根据用户id搜寻公司质量能力信息（包含未审核通过的信息）
     * @param userId 公司所对应的用户id
     * @return 所要搜寻的信息
     */
    @Export(paramNames = {"userId"})
    Response<SupplierUpdatedInfoDto<CompanyExtraQualityDto>> findQualityByUserId(Long userId);

    /**
     * 更新或创建公司响应信息
     * @param companyExtraResponse 待更新的信息
     * @return 是否更新成功
     */
    @Export(paramNames = {"companyExtraResponse"})
    Response<Boolean> updateOrCreateResponse(CompanyExtraResponse companyExtraResponse);

    /**
     * 根据用户搜寻公司响应信息
     * @param user 公司所对应的用户（唯一）
     * @return 所要搜寻的信息
     */
    @Export(paramNames = {"user"})
    Response<CompanyExtraResponse> findResponseByUser(BaseUser user);

    /**
     * 根据用户id搜寻公司响应信息
     * @param userId 公司所对应的用户id
     * @return 所要搜寻的信息
     */
    @Export(paramNames = {"userId"})
    Response<SupplierUpdatedInfoDto<CompanyExtraResponse>> findResponseByUserId(Long userId);

    /**
     * 更新或创建公司交货信息
     * @param companyExtraDelivery 所要创建的信息
     * @return 自增id
     */
    @Export(paramNames = {"companyExtraDelivery"})
    Response<Boolean> updateOrCreateDelivery(CompanyExtraDelivery companyExtraDelivery);

    /**
     * 根据用户搜寻公司交货信息（包含暂存的信息）
     * @param user 公司所对应的用户（唯一）
     * @return 所要搜寻的信息
     */
    @Export(paramNames = {"user"})
    Response<CompanyExtraDelivery> findDeliveryByUser(BaseUser user);

    /**
     * 根据用户id搜寻公司交货信息
     * @param userId 公司所对应的用户id
     * @return 所要搜寻的信息
     */
    @Export(paramNames = {"userId"})
    Response<CompanyExtraDelivery> findDeliveryByUserId(Long userId);

    /**
     * 更新或创建公司规模和成本信息
     * @param companyExtraScaleAndCost 待更新的信息
     * @return 是否更新成功
     */
    @Export(paramNames = {"companyExtraScaleAndCost"})
    Response<Boolean> updateOrCreateScaleAndCost(CompanyExtraScaleAndCost companyExtraScaleAndCost);

    /**
     * 根据用户搜寻公司规模和成本信息
     * @param user 公司所对应的用户（唯一）
     * @return 所要搜寻的信息
     */
    @Export(paramNames = {"user"})
    Response<CompanyExtraScaleAndCost> findScaleAndCostByUser(BaseUser user);

    /**
     * 根据用户id搜寻公司规模和成本信息
     * @param userId 公司所对应的用户id
     * @return 所要搜寻的信息
     */
    @Export(paramNames = {"userId"})
    Response<CompanyExtraScaleAndCost> findScaleAndCostByUserId(Long userId);
}
