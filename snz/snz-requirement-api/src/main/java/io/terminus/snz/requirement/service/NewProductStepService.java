package io.terminus.snz.requirement.service;

import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.model.NewProductStep;

import java.util.List;

/**
 * 更新新品导入数据步骤信息
 *      若表中在按 模块号+供应商code+步骤编号 更新
 *      否则插入新数据
 * @author wanggen on 14-8-8.
 */
public interface NewProductStepService {

    /**
     * 更新步骤信息
     * @param step 步骤信息
     * @return 更新是否成功
     */
    Response<Boolean> post(NewProductStep step);

    /**
     * 批量更新步骤信息
     * @param steps 步骤信息列表
     * @return 更新是否成功
     */
    Response<Boolean> batchPost(List<NewProductStep> steps);


}
