package io.terminus.snz.related.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 索赔明细
 * Author:  wenhaoli
 * Date: 2014-08-11
 */
@ToString
public class CompensationDetail implements Serializable {

    private static final long serialVersionUID = -8489422917591566172L;

    //索赔情况

    @Getter
    @Setter
    private Long id;                    //明细id

    @Getter
    @Setter
    private Long listId;                //索赔记录id

    @Getter
    @Setter
    private String listNum;             //订单号

    @Getter
    @Setter
    private String factory;             //工厂编号

    @Getter
    @Setter
    private String numOfProof;          //采购凭证号

    @Getter
    @Setter
    private String numOfParam;          //采购凭证的项目编号

    @Getter
    @Setter
    private String supplierAccount;         //内外部供应商标志, 应该是V码

    @Getter
    @Setter
    private String materielNo;          //物料号

    @Getter
    @Setter
    private Date timeOfDelivery;        //应交货日期

    @Getter
    @Setter
    private Date rTimeOfDelivery;       //实际交货日期

    @Getter
    @Setter
    private Date yTimeOfDelivery;       //预约交货日期

    @Getter
    @Setter
    private Integer numOfList;          //采购订单数量

    @Getter
    @Setter
    private Integer numOfDelivery;      //实际收货数量

    @Getter
    @Setter
    private Integer numOfDifference;    //差异数量

    @Getter
    @Setter
    private String descOfDifference;    //交货差异原因

    @Getter
    @Setter
    private Date currentDay;            //当前日期

    @Getter
    @Setter
    private Date currentTime;           //当前时间

    @Getter
    @Setter
    private Date createdAt;             //创建时间

    @Getter
    @Setter
    private Date updatedAt;             //更新时间


    /**
     * 索赔状态
     */
    public static enum Status{
        INIT(0, "未申诉"),
        ING(1, "申诉中"),
        PASS(2, "申诉成功"),
        UNPASS(3, "申诉失败");

        private int val;
        private String desc;

        private Status(int val, String desc){
            this.val = val;
            this.desc = desc;
        }

        public int value(){
            return this.val;
        }

        public String desc(){
            return this.desc;
        }
    }

}
