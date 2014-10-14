package io.terminus.snz.requirement.service;

import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.dto.RequirementTimeDto;

/**
 * Desc:需求时间操作处理
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-08-19.
 */
public interface RequirementTimeService {
    /**
     * 对阶段时间的延期操作
     * @param requirementTimes 需求的更改时间信息
     * @param user          需求创建者
     * @return  Boolean
     * 返回需求时间延期是否成功
     */
    @Export(paramNames = {"requirementTimes" , "user"})
    public Response<Boolean> updateRequirementTimes(String requirementTimes, BaseUser user);

    /**
     * 查询需求的时间包括预计时间&实际时间和延期时间
     * @param requirementId 需求变好
     * @return  RequirementTimeDto
     * 返回需求时间信息
     */
    @Export(paramNames = {"requirementId"})
    public Response<RequirementTimeDto> findRequirementTimes(Long requirementId);
}
