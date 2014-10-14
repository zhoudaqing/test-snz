package io.terminus.snz.requirement.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Desc:向百卓抛送的需求的数据信息
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-09-03.
 */
public class RequirementBIZDto implements Serializable {
    private static final long serialVersionUID = 6167906676632838376L;

    @Setter
    @Getter
    private Long requirementId;                         //需求编号

    @Setter
    @Getter
    private String requirementName;                     //需求名称

    @Setter
    @Getter
    private Long materielType;                          //一级类目编号

    @Setter
    @Getter
    private String materielName;                        //一级类目名称

    @Setter
    @Getter
    private Long productId;                             //二级类目编号

    @Setter
    @Getter
    private String productName;                         //二级类目名称

    @Setter
    @Getter
    private String seriesIds;                           //多个三级类目json信息

    @Setter
    @Getter
    private Date createAt;                              //需求的创建时间

    @Setter
    @Getter
    private Date updateAt;                              //需求的更新时间

    @Setter
    @Getter
    private List<ModuleDetailBIZDto> moduleInfoDtoList;      //模块的详细信息列表
}
