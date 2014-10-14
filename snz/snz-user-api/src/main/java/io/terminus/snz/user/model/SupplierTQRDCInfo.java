package io.terminus.snz.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Author:Guo Chaopeng
 * Created on 14-6-20.
 */
@ToString
public class SupplierTQRDCInfo implements Serializable {

    private static final long serialVersionUID = -3885663187391137575L;

    @Getter
    @Setter
    private Long id;                       //自增主键

    @Getter
    @Setter
    private Long userId;                  //用户编号

    @Getter
    @Setter
    private Long companyId;              //企业编号

    @Getter
    @Setter
    private String specialNumber;          //专用号

    @Getter
    @Setter
    private String supplierCode;          //供应商代码

    @Getter
    @Setter
    private String supplierName;          //供应商名称

    @Getter
    @Setter
    private String module;                //模块

    @Getter
    @Setter
    private Integer productLineId;        //产品线ID

    @Getter @Setter
    private String productLine;           //产品线

    @Setter
    @Getter
    private String location;              //区域

    @Setter
    @Getter
    private Integer rank;                 //排序

    @Getter
    @Setter
    private String month;                 //月份

    @Getter
    @Setter
    private String date;                    //日期

    @Getter
    @Setter
    private Integer compositeScore;         //综合得分

    @Getter
    @Setter
    private Integer supplierStatus;         //供应商状态

    @Getter
    @Setter
    private Integer techScore;             //技术得分

    @Getter
    @Setter
    private Integer delayDays;             //拖期天数

    @Getter
    @Setter
    private Integer newProductPass;        //新品合格率

    @Getter
    @Setter
    private Integer qualityScore;          //质量得分

    @Getter
    @Setter
    private String shortComing;             //短板

    @Getter
    @Setter
    private Integer liveBad;               //现场不良率

    @Getter
    @Setter
    private Integer marketBad;             //市场不良率

    @Getter
    @Setter
    private Integer respScore;             //响应得分


    @Getter
    @Setter
    private Integer programPromisedRate;    //方案承诺率

    @Getter
    @Setter
    private Integer programSelectedRate;    //方案选中率

    @Getter
    @Setter
    private Integer programAdoptedRate;     //方案落地率

    @Getter
    @Setter
    private Integer requirementResp;       //需求响应度

    @Getter
    @Setter
    private Integer deliverScore;          //交付得分

    @Getter
    @Setter
    private Integer deliverDiff;           //交付差异

    @Getter
    @Setter
    private Integer costScore;             //成本得分

    @Getter
    @Setter
    private Integer increment;             //增值

    @Getter
    @Setter
    private Integer techScoreRank;         //技术得分排序

    @Getter
    @Setter
    private String techLocation;           //技术得分区位

    @Getter
    @Setter
    private Integer qualityScoreRank;      //质量得分排序

    @Getter
    @Setter
    private String qualityLocation;        //质量得分区位

    @Getter
    @Setter
    private Integer respScoreRank;         //响应得分排序

    @Getter
    @Setter
    private String respLocation;           //响应得分区位

    @Getter
    @Setter
    private Integer deliveryScoreRank;      //交付得分排序

    @Getter
    @Setter
    private String deliveryLocation;        //交付得分区位

    @Getter
    @Setter
    private Integer affectDeliveryCount;    //影响 T-1 交付次数

    @Getter
    @Setter
    private Integer costScoreRank;         //成本得分排序

    @Getter
    @Setter
    private String costLocation;            //成本得分区位

    @Getter
    @Setter
    private Integer priceReductionAmount;   //降价额度

    @Getter
    @Setter
    private Integer priceReductionRange;    //降价幅度

    @Getter
    @Setter
    private Date createdAt;                //创建时间

    @Getter
    @Setter
    private Date updatedAt;                //修改时间


    private static enum TQRDC{
        T(1, "技术"),
        Q(2, "质量"),
        R(3, "响应"),
        D(4, "交付"),
        C(5, "成本");

        public int val;
        public String display;

        private TQRDC(int val, String display){
            this.val = val;
            this.display = display;
        }
    }


    /** 0:正常 | -1:警告 | -2:整改  | -3:停新品 **/
    public static enum SupplierStatus {
        normal(0, "正常"),
        warn(-1, "警告"),
        adjusting(-2, "整改"),
        stop_supply(-3, "停止供应");

        private final int value;
        private final String description;

        private SupplierStatus(int value, String description) {
            this.value = value;
            this.description = description;
        }
        public int value() {
            return this.value;
        }
        @Override
        public String toString() {
            return description;
        }
    }

    /**
     * 计算短板, 即t q r d c中分数最小的
     */
    public void shortComing(){
        Integer worst = techScore;
        shortComing = TQRDC.T.display;
        if (worst > qualityScore){
            worst = qualityScore;
            shortComing = TQRDC.Q.display;
        }
        if (worst > respScore){
            worst = respScore;
            shortComing = TQRDC.R.display;
        }
        if (worst > deliverScore){
            worst = deliverScore;
            shortComing = TQRDC.D.display;
        }
        if (worst > costScore){
            worst = costScore;
            shortComing = TQRDC.C.display;
        }
    }
}
