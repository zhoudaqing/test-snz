package io.terminus.snz.requirement.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * Desc:Excel数据校验error
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-08-28.
 */
public class ExcelErrorDto implements Serializable {
    private static final long serialVersionUID = 7790530947834341891L;

    @Setter
    @Getter
    private Boolean error;                      //是否存在错误

    @Setter
    @Getter
    private Boolean warning;                    //是否存在警告信息

    @Setter
    @Getter
    private Integer lineNum;                    //错误数据在第几行

    @Setter
    @Getter
    private Map<String , String> errorView;     //错误数据信息（错误的属性名称:错误信息）

    @Setter
    @Getter
    private Map<String , String> warningView;   //警告信息（模块的专用号未找到）
}
