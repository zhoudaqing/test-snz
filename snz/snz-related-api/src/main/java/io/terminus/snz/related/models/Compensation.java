package io.terminus.snz.related.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 索赔信息
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-7-10
 */
@ToString
public class Compensation implements Serializable{

    private static final long serialVersionUID = 4701932982348530371L;

    @Getter @Setter
    private Long id;                    //PK

    @Getter @Setter
    private String park;                //园区

    @Getter @Setter
    private String productLine;         //产业线

    @Getter @Setter
    private String supplierAccount;     //供应商或债权人的账号, 应该是V码

    @Getter @Setter
    private String supplierName;        //供应商名称

    @Getter @Setter
    private Integer isInternal;         //内外部供应商标志

    @Getter @Setter
    private String factory;             //工厂编码

    @Getter @Setter
    private String factoryName;         //工厂名称

    @Getter @Setter
    private String materielNo;          //物料号

    @Getter @Setter
    private String materielDesc;        //物料描述, 短文本

    @Getter @Setter
    private String purchaserGroup;      //采购组

    @Getter @Setter
    private String purchaserGroupDesc;  //采购组描述

    @Getter @Setter
    private String score;               //扣分

    @Getter @Setter
    private Integer isMoney;            //需要扣款标志, 0不需要, 1需要

    @Getter @Setter
    private String money;              //扣除金额

    @Getter @Setter
    private Date deductedAt;            //扣除日期

    @Getter @Setter
    private Date current;               //当前日期

    @Getter @Setter
    private Integer status;             //申诉状态

    @Getter @Setter
    private String result;              //扣款结果

    @Getter @Setter
    private Date enteredAt;             //凭证入账日期

    @Getter @Setter
    private Date createdAt;             //违约时间

    @Getter @Setter
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
    }
}
