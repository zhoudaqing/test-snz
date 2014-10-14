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
public class CompanyExtraDelivery implements Serializable {
    private static final long serialVersionUID = 2179400385244833524L;

    @Getter
    @Setter
    private Long id;                                    // 自增主键

    @Getter
    @Setter
    private Long userId;                                // 用户id

    // 生产保障能力
    @Getter
    @Setter
    @NotNull
    private String listOfEquipments;                 // 生产设备清单 设备名称，参数，数量

    @Getter
    @Setter
    @NotNull
    private Integer moduleProductionCapacity;           // 模块生产能力（单位：件/年）

    @Getter
    @Setter
    @NotBlank
    private String moduleProductionCapacityAttachUrl;   // 模块商年产量附件

    @Getter
    @Setter
    @NotNull
    private String listOfAutomationEquipment;        // 自动化设备清单 设备名称 参数 数量

    // 交货保障
    @Getter
    @Setter
    private String deliveryArea;                        // 供货区域（json列表）

    @Getter
    @Setter
    private Integer deliveryCycle;                      // 交货周期（天）

    // 自评
    @Getter
    @Setter
    private Integer openRate;                 // 开机率

    @Getter
    @Setter
    private Date createdAt;

    @Getter
    @Setter
    private Date updatedAt;
}
