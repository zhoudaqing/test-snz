package io.terminus.snz.requirement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 对应新品导入列表中的行记录
 * 表：snz_income_good_lists
 *
 * Date: 7/10/14
 * Time: 15:28
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */

@ToString
@EqualsAndHashCode
public class ImportGoodList implements Serializable {
    private static final long serialVersionUID = -4673786815735344352L;

    @Getter
    @Setter
    private Long id;

    // 冗余需求的名称，显示为项目名
    @Getter
    @Setter
    private String requirementName;

    // 需求的id，显示为项目ID
    @Getter
    @Setter
    private Long requirementId;

    // 二级类目，显示为模块的ID
    @Getter
    @Setter
    private Long seriesId;

    // 二级类目名，同上
    @Getter
    @Setter
    private String seriesName;

    // 所述生产线（PL）
    @Getter
    @Setter
    private String  productLine;

    // 模块序列号
    @Getter
    @Setter
    private String moduleNum;

    @Getter
    @Setter
    private Long moduleId;

    @Getter
    @Setter
    private String moduleName;

    // 当前进度的负责人
    @Getter
    @Setter
    private String inCharge;

    @Getter
    @Setter
    @JsonIgnore
    private Date createdAt;

    @Getter
    @Setter
    @JsonIgnore
    private Date updatedAt;
}
