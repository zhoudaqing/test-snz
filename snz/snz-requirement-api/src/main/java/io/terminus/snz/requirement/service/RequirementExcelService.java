package io.terminus.snz.requirement.service;

import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.dto.ExcelErrorDto;
import io.terminus.snz.requirement.dto.ModuleInfoDto;

import java.util.List;

/**
 * Desc:需求模块的excel解析数据处理操作
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-08-28.
 */
public interface RequirementExcelService {

    /**
     * 根据Excel解析后的数据写入操作
     * @param requirementId     需求编号
     * @param moduleInfoDtoList 需要写入的模块详细数据信息
     * @return  List
     * 返回excel数据错误信息(result==null:校验数据都正确, isSuccess==true:没有异常, result==null && isSuccess-->数据成功写入数据库)
     */
    public Response<List<ExcelErrorDto>> createModule(Long requirementId , List<ModuleInfoDto> moduleInfoDtoList);
}
