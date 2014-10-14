package io.terminus.snz.user.model;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.Map;

/**
 * TQRDC 中间表
 * <p/>
 * Created by wanggen
 * <PRE>
 * id : Long  //主键
 * userId : Long  //用户id
 * companyId : Long  //对应企业信息
 * specialNumber : String  //专用号
 * supplierCode : String  //供应商代码
 * supplierName : String  //供应商名称
 * module : String  //模块
 * productLineId : Integer  //产品线ID
 * productLine : String  //产品线
 * location : String  //区域
 * rank : String  //排名
 * date : String  //日期
 * compositeScore : Integer  //综合绩效
 * supplierStatus : Integer  //供应商状态(0:正常|-1:警告|-2:整改|-3:停新品)
 * techScore : Integer  //技术得分
 * delayDays : Integer  //拖期天数
 * newProductPass : Integer  //新品合格率
 * qualityScore : Integer  //质量得分
 * liveBad : Integer  //现场不良率
 * marketBad : Integer  //市场不良率
 * respScore : Integer  //响应得分
 * programPromisedRate : Integer  //方案承诺率
 * programSelectedRate : Integer  //方案选中率
 * programAdoptedRate : Integer  //方案落地率
 * requirementResp : Integer  //需求响应度
 * deliverScore : Integer  //交付得分
 * deliverDiff : Integer  //交付差异
 * costScore : Integer  //成本得分
 * increment : Integer  //增值
 * techScoreRank : Integer  //技术得分排序
 * qualityScoreRank : Integer  //质量得分排序
 * respScoreRank : Integer  //响应得分排序
 * deliveryScoreRank : Integer  //交付得分排序
 * affectDeliveryCount : Integer  //影响 T-1 交付次数
 * costScoreRank : Integer  //成本得分排序
 * priceReductionAmount : Integer  //降价额
 * priceReductionRange : Integer  //降幅
 * createdAt : Date  //
 * updatedAt : Date  //
 * </PRE>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class SupplierTQRDCInfoTmp implements Serializable {
    private static final long serialVersionUID = 4196465882076061874L;

    private Long id;    //主键

    private Long userId;    //用户id

    private Long companyId;    //对应企业信息

    private String specialNumber;    //专用号

    private String supplierCode;    //供应商代码

    private String supplierName;    //供应商名称

    private String module;    //模块

    private Integer productLineId;    //产品线ID

    private String productLine;    //产品线

    private String location;    //区域

    private String rank;    //排名

    private String date;    //日期

    private Integer compositeScore;    //综合绩效

    private Integer supplierStatus;    //供应商状态(0:正常|-1:警告|-2:整改|-3:停新品)

    private Integer techScore;    //技术得分

    private Integer delayDays;    //拖期天数

    private Integer newProductPass;    //新品合格率

    private Integer qualityScore;    //质量得分

    private Integer liveBad;    //现场不良率

    private Integer marketBad;    //市场不良率

    private Integer respScore;    //响应得分

    private Integer programPromisedRate;    //方案承诺率

    private Integer programSelectedRate;    //方案选中率

    private Integer programAdoptedRate;    //方案落地率

    private Integer requirementResp;    //需求响应度

    private Integer deliverScore;    //交付得分

    private Integer deliverDiff;    //交付差异

    private Integer costScore;    //成本得分

    private Integer increment;    //增值

    private Integer techScoreRank;    //技术得分排序

    private Integer qualityScoreRank;    //质量得分排序

    private Integer respScoreRank;    //响应得分排序

    private Integer deliveryScoreRank;    //交付得分排序

    private Integer affectDeliveryCount;    //影响 T-1 交付次数

    private Integer costScoreRank;    //成本得分排序

    private Integer priceReductionAmount;    //降价额

    private Integer priceReductionRange;    //降幅

    private Date createdAt;    //

    private Date updatedAt;    //


    private final static Map<Field, Field> fromToFieldMap = Maps.newConcurrentMap();
    public void mapTo(SupplierTQRDCInfo tqrdc) {
        try {
            if (fromToFieldMap.isEmpty()) {
                Class<SupplierTQRDCInfoTmp> fromClass = SupplierTQRDCInfoTmp.class;
                Class<SupplierTQRDCInfo> toClass = SupplierTQRDCInfo.class;
                for (Field fromField : fromClass.getDeclaredFields()) {
                    try {
                        Field toField = toClass.getDeclaredField(fromField.getName());
                        if (fromField.getType().equals(toField.getType())) {
                            if (Modifier.isFinal(toField.getModifiers()))
                                continue;
                            fromToFieldMap.put(fromField, toField);
                            fromField.setAccessible(true);
                            toField.setAccessible(true);
                        }
                    } catch (Exception e) {
                        log.error("Failed map field:[{}] Caused By:{}", fromField, e.toString());
                    }
                }
            }
            if (!fromToFieldMap.isEmpty()) {
                for (Map.Entry<Field, Field> entry : fromToFieldMap.entrySet()) {
                    Object val = entry.getKey().get(this);
                    if (val != null) {
                        entry.getValue().set(tqrdc, val);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failed to copy data from SupplierTQRDCInfoTmp to SupplierTQRDCInfo", e);
        }

    }
}
