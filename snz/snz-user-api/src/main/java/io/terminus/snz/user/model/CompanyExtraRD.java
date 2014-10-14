package io.terminus.snz.user.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 6/23/14
 */
public class CompanyExtraRD implements Serializable {
    private static final long serialVersionUID = 7714141579149979101L;

    @Getter
    @Setter
    private Long id;                                    // 自增主键

    @Getter
    @Setter
    private Long userId;                                // 用户id

    //高新技术企业资质
    @Getter
    @Setter
    private Integer awardLevel;                         //所获证书等级

    // 技术团队
    @Getter
    @Setter
    private Integer numberOfPeople;                     // 研发人数

    @Getter
    @Setter
    private Integer numberOfIntermediateEngineer;       // 中级工程师人数

    @Getter
    @Setter
    private String otherNames;                             //其它职称

    @Getter
    @Setter
    private Integer sumNumberOfEngineer;                          //合计

    @Getter
    @Setter
    private Integer numberOfSeniorEngineer;             // 高级工程师人数

    // 研发实力
    @Getter
    @Setter
    private Long investmentThisYear;                    // 本年研发投入（单位分）

    @Getter
    @Setter
    private Long investmentLastYear;                    // 去年投入

    @Getter
    @Setter
    private Long investmentBeforeLastYear;              // 前年投入

    @Getter
    @Setter
    @NotNull
    private Long assets;                                // 研发资产（单位分）

    @Getter
    @Setter
    @NotBlank
    private String facilityAttachUrl;                   // 研发设施附件

    @Getter
    @Setter
    @NotNull
    private Integer labArea;                            // 试验室面积（单位平方米）

    @Getter
    @Setter
    @NotNull
    private Integer numberOfEquipments;                 // 研发设备数量（台）

    @Getter
    @Setter
    @NotNull
    private Integer labLevel;                           // 试验室级别（默认1位国家级）

    @Getter
    @Setter
    @NotBlank
    private String labAttachUrl;                        // 研发试验室附件

    // 研发成绩
    @Getter
    @Setter
    @NotNull
    private Integer numberOfPatents;                    // 专利数量

    @Getter
    @Setter
    @NotNull
    private Integer numberOfPatentsForInventions;       // 其中发明专利的数量

    @Getter
    @Setter
    @NotNull
    private Integer numberOfPatentsLastThreeYears;      // 近三年专利数量

    @Getter
    @Setter
    @NotBlank
    private String patentsAttachUrl;                    // 专利数量附件

    @Getter
    @Setter
    private String nationalTechnologyAwards;            // 获得国家级奖项

    @Getter
    @Setter
    private String provincialTechnologyAwards;          // 获得省级奖项

    @Getter
    @Setter
    @NotBlank
    private String successStories;                      // 成功案例

    @Getter
    @Setter
    @NotBlank
    private String successStoriesAttachUrl;             // 成功案例附件

    // 自评
    @Getter
    @Setter
    private String otherAbilityDescription;                  // 其它能力说明

    @Getter
    @Setter
    private Date createdAt;

    @Getter
    @Setter
    private Date updatedAt;
}
