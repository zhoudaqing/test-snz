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
public class CompanyExtraQuality implements Serializable {
    private static final long serialVersionUID = -4362355610564096919L;

    @Getter
    @Setter
    private Long id;                                    // 自增主键

    @Getter
    @Setter
    private Long userId;                                // 用户id

    // 质量管理体系
    @Getter
    @Setter
    private String rohsId;                              // RoHS(QC08000) 证书编号

    @Getter
    @Setter
    private Date rohsValidDate;                         // RoHS 证书有效期

    @Getter
    @Setter
    private String rohsAttachUrl;                       // RoHS 附件

    @Getter
    @Setter
    private String iso9001Id;                           // ISO9001 证书编号

    @Getter
    @Setter
    private Date iso9001ValidDate;                      // ISO9001 证书有效期

    @Getter
    @Setter
    private String iso9001AttachUrl;                    // ISO9001 附件

    @Getter
    @Setter
    private String iso14001Id;                          // ISO14001 证书编号

    @Getter
    @Setter
    private Date iso14001ValidDate;                     // ISO14001 证书有效期

    @Getter
    @Setter
    private String iso14001AttachUrl;                   // ISO14001 附件

    @Getter
    @Setter
    private String ts16949Id;                           // TS16949 证书编号

    @Getter
    @Setter
    private Date ts16949ValidDate;                      // TS16949 证书有效期

    @Getter
    @Setter
    private String ts16949AttachUrl;                    // TS16949 附件

    // 质量管理团队
    @Getter
    @Setter
    @NotNull
    private Integer numberForAdmissionTest;             // 入厂检验人数

    @Getter
    @Setter
    @NotNull
    private Integer numberForInspectionProcess;         // 过程巡检人数

    @Getter
    @Setter
    @NotNull
    private Integer numberForProductTesting;            // 成品检验人数

    @Getter
    @Setter
    @NotNull
    private Integer numberInLab;                        // 实验室人数

    @Getter
    @Setter
    @NotBlank
    private String numberForQualityAttachUrl;           // 质量管理人数附件

    @Getter
    @Setter
    private Integer auditorType;                        // 质量人员类型（1:资质审核员, 2:试验员, 3:绿带, 4:黑带)

    @Getter
    @Setter
    @NotNull
    private Integer examPeople;                         //取得资质审核员人数

    @Getter
    @Setter
    @NotNull
    private Integer testPeople;                         //取得资质试验员人数

    @Getter
    @Setter
    @NotNull
    private Integer greenLevel;                         //取得资质绿带人数

    @Getter
    @Setter
    @NotNull
    private Integer blackLevel;                         //取得资质黑带人数

    @Getter
    @Setter
    @NotBlank
    private String auditorAttachUrl;                    // 质量人员资质附件

    // 质量实力
    @Getter
    @Setter
    private Long investmentThisYear;                    // 今年投入（单位分）

    @Getter
    @Setter
    private Long investmentLastYear;                     // 去年投入

    @Getter
    @Setter
    private Long investmentBeforLastYear;                // 前年投入

    @Getter
    @Setter
    @NotNull
    private Integer numberOfEquipments;                 // 试验设配数量（台）

    @Getter
    @Setter
    private Integer labLevel;                           // 试验室级别（默认1为国家级）

    @Getter
    @Setter
    @NotBlank
    private String labAttachUrl;                        // 质量实验室附件

    // 质量控制
    @Getter
    @Setter
    @NotNull
    private Integer checksQualifiedRate;                // 现场合格率（单位万分之一）

    @Getter
    @Setter
    @NotNull
    private Integer marketQualifiedRate;                // 市场合格率

    @Getter
    @Setter
    @NotNull
    private Integer customerSatisfaction;               // 客户满意度

    @Getter
    @Setter
    @NotBlank
    private String qualifiedAttachUrl;                  // 质量绩效附件

    @Getter
    @Setter
    private String qualityTools;                        // 质量工具运用（SPC, MSA, ...）

    @Getter
    @Setter
    private String ctqCpk;                              // CTQ的CPK水平

    // 自评
    @Getter
    @Setter
    private String otherAbilityDescription;                     // 其它能力说明

    @Getter
    @Setter
    private Date createdAt;

    @Getter
    @Setter
    private Date updatedAt;
}
