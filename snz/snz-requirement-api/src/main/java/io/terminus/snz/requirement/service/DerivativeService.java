package io.terminus.snz.requirement.service;

import io.terminus.common.model.Paging;
import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.dto.DerivativeDto;
import io.terminus.snz.requirement.model.DerivativeDiff;
import io.terminus.snz.requirement.model.OldModule;
import org.springframework.stereotype.Service;

/**
 * Desc:提供衍生品信息的相关操作
 * Created by jiaoyuan on 14-7-7.
 */
@Service
public interface DerivativeService {

    /**
     * 通过需求编号查询衍生号的数据信息（返回的数据显示有：衍生号信息&老品信息）
     * @param requirementId    需求编号
     * @param pageNo           当前页数（默认为0）
     * @param size             分页大小（默认为10）
     * @return  Paging
     * 返回封装好的分页数据
     */
    @Export(paramNames = {"requirementId","pageNo","size"})
    public Response<Paging<DerivativeDto>> findByRequirementId(Long requirementId , Integer pageNo, Integer size);

    /**
     * 创建场景差异信息
     * @param derivativeDiff  场景差异信息
     * @return  Boolean
     * 返回创建结果
     */
    @Export(paramNames = {"derivativeDiff"})
    public Response<Boolean> create(DerivativeDiff derivativeDiff);
    /**
     * 更改场景差异信息
     * @param derivativeDiff  场景差异信息
     * @return Boolean
     * 返回更新结果
     */
    @Export(paramNames = {"derivativeDiff"})
    public Response<Boolean> update(DerivativeDiff derivativeDiff);

    /**
     * 通过需求编号显示场景差异信息（添加分页操作）
     * @param requirementId    方案编号
     * @param pageNo        当前页数（默认为0）
     * @param size          分页大小（默认为20）
     * @return  Paging
     * 返回封装好的分页数据
     */
    @Export(paramNames = {"requirementId", "pageNo", "size"})
    public Response<Paging<DerivativeDiff>> findDiffByRequirementId(Long requirementId, Integer pageNo, Integer size);

    /**
     * 通过moduleNum查询老品信息
     * @param moduleNum
     * @return
     * 返回老品信息
     */
    @Export(paramNames = {"moduleNum"})
    public  Response<OldModule> findByModuleNum(String moduleNum);

    /**
     * 批量创建衍生号场景差异信息
     * @param derivativeList
     * @return
     * 返回是否创建成功
     */
    @Export(paramNames = {"derivativeList"})
    public Response<Boolean> batchDerivative(String derivativeList);

    /**
     * 根据需求id和衍生号id删除衍生号，同时清除差异分析表的数据
     * @param requirementId 需求id， ModuleId衍生号id
     * @return true or false
     */
    @Export(paramNames = {"requirementId","moduleId"})
    public Response<Boolean> deleteDeriveModule(Long requirementId, Long moduleId);
}
