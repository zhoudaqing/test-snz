package io.terminus.snz.requirement.service;

import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.dto.ImportGoodCurrentStageDto;
import io.terminus.snz.requirement.dto.ImportGoodDto;
import io.terminus.snz.requirement.model.ImportGoodRow;

import java.util.List;

/**
 * Date: 7/11/14
 * Time: 15:09
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */
public interface ImportGoodService {

    /**
     * 展现新品导入列表时使用这个接口提供列表每行所需要的信息。
     *
     * @param moduleId    新品对应模块的ID
     * @return            新品导入详情记录和当前进行的步骤信息
     */
    @Export(paramNames = {"moduleId"})
    public Response<ImportGoodCurrentStageDto> findImportGoodAndCurrentRow(Long moduleId);

    /**
     * 根据模块的ID查找新品导入的详情信息
     * 当从新品导入列表点击详情跳转到详情页时，可以使用这个方法提供数据。
     *
     * @param moduleId    新品导入详情的ID
     * @return            模块所有的新品导入详情
     */
    @Export(paramNames = {"moduleId"})
    public Response<List<ImportGoodDto>> findByModuleId(Long moduleId);


    /**
     * 更新现在导入的步骤
     *
     * @param row             当前操作的的详情
     * @param importGoodId    新品导入的ID
     * @param stage           当前步骤
     * @return                更新是否成功
     */
    @Export(paramNames = {"row", "importGoodId", "stage"})
    public Response<Boolean> updateProgress(ImportGoodRow row, Long importGoodId, Integer stage);
}
