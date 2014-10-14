package io.terminus.snz.requirement.model;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Desc:标注需求的提交数据信息状态(用于标注哪些对接系统的写入操作已完成)
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-28.
 */
public class RequirementSend implements Serializable {
    private static final long serialVersionUID = -782321361987283456L;

    @Getter
    @Setter
    private Integer id;                 //自增编号

    @Getter
    @Setter
    private Long requirementId;         //需求编号

    @Getter
    @Setter
    private Integer sendPLM;            //是否已写入模块数据到plm中间表（需求锁定阶段跳转到方案交互抛送）

    @Getter
    @Setter
    private Integer replyModuleNum;     //从plm中间表回写模块专用号（确认某个需求下的所有模块是否都已经写入模块专用号）



    /** 配额已产生的情况下的处理状态 **/
    @Getter
    @Setter
    private Integer businessNegotiate;  //商务谈判状态（当状态为1时－》中选的供应商可以更改价格但是不能高于报价）

    @Setter
    @Getter
    private String negotiateFile;       //商务谈判文件

    @Getter
    @Setter
    private Integer supplierSign;       //供应商跟标（当状态为1时－》供应商界面显示是否跟标信息此时供应商才能看到个人的配额信息）

    @Getter
    @Setter
    private Integer resultPublicity;    //配额结果公示（当状态为1时－》用户在详细需求页面可以看到中标的用户的配额详细信息，供应商各自也能在本身后台查看配额信息）

    @Getter
    @Setter
    private Integer confirmQuota;       //是否选定的供应商已全部确认配额信息（在确认配额生成后）

    @Getter
    @Setter
    private Integer sendVCode;          //向plm中间表写入供应商V码信息（这个是最终确认配额后向plm抛送）

    @Getter
    @Setter
    private Integer writeDetailQuota;   //是否已写入详细的配额信息（采购商向详细的配额信息中写入必要的详细配额信息内容）

    @Getter
    @Setter
    private Integer sendSAP;            //是否已写入详细配额数据到sap（标记采购商是否已经向sap抛送全部的配额详细数据）

    @Getter
    @Setter
    private Date createdAt;             //创建时间

    @Getter
    @Setter
    private Date updatedAt;             //修改时间

    public enum Type{
        UN_COMMIT(0 , "未提交"), COMMIT(1 , "已全部提交");

        private final Integer value;

        private final String description;

        private Type(Integer value , String description){
            this.value = value;

            this.description = description;
        }

        public static Type from(Integer value){
            for(Type type : Type.values()){
                if(Objects.equal(value, type.value)){
                    return type;
                }
            }

            return null;
        }

        public Integer value(){
            return value;
        }

        @Override
        public String toString(){
            return description;
        }
    }
}
