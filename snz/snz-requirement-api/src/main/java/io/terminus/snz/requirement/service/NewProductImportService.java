package io.terminus.snz.requirement.service;

import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.dto.NewProductStepsDto;
import io.terminus.snz.requirement.model.NewProductImport;

import java.util.List;


/**
 * 新品导入信息
 *
 * @author wanggen
 */
public interface NewProductImportService {

    /**
     * 新增
     *
     * @param newProductImport add bean
     * @return 新增后的自增序列号
     */
    @Export(paramNames = {"newProductImport"})
    public Response<Long> create(NewProductImport newProductImport);


    /**
     * 根据ID查询但条记录
     *
     * @return 返回的 newProductImport
     */
    public Response<NewProductImport> findById(Long id);


    /**
     * 根据 id 列表查询多个结果集
     *
     * @param ids 多个id
     * @return NewProductImport 列表
     */
    public Response<List<NewProductImport>> findByIds(List<Long> ids);


    /**
     * 根据 moduleNum 查询 NewProductImport 列表
     *
     * @param moduleNum 模块编号
     * @return 结果列
     */
    @Export(paramNames = {"moduleNum"})
    Response<List<NewProductImport>> findByModuleNum(String moduleNum);


    /**
     * 根据 moduleNum 查询新品进度流程信息<BR>
     * 每个供应商对该模块的有多个流程节点
     *
     * @param moduleNum 模块编号
     * @return 行式数据，适合表格显示
     */
    @Export(paramNames = {"moduleNum"})
    Response<List<NewProductStepsDto>> findNPIStepsByModuleNum(String moduleNum);


    /**
     * 更新操作
     *
     * @param newProductImport 更新操作参数
     * @return 影响行数
     */
    public Response<Integer> update(NewProductImport newProductImport);


    /**
     * 根据序列 id 删除记录
     *
     * @param ids 序列 id 列表
     * @return 删除行数
     */
    public Response<Integer> deleteByIds(List<Long> ids);


}
