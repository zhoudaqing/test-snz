package io.terminus.snz.requirement.model;

import com.google.common.base.Objects;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Desc:供应商对于采购商的需求提供的整体的方案
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-03.
 */
@ToString
@EqualsAndHashCode
public class RequirementSolution implements Serializable {
    private static final long serialVersionUID = -4617944916397813223L;

    @Setter
    @Getter
    private Long id;                //自增主键

    @Setter
    @Getter
    private Long requirementId;     //需求编号

    @Setter
    @Getter
    private String requirementName; //需求名称

    @Setter
    @Getter
    private Long userId;            //用户编号（用于话题圈定范围）

    @Setter
    @Getter
    private Long supplierId;        //供应商编号

    @Setter
    @Getter
    private String supplierName;    //供应商名称

    @Setter
    @Getter
    private Integer technology;     //技术(由采购商对方案的技术进行评分)

    @Setter
    @Getter
    private Integer quality;        //质量（这些数据都是后期系统自动加权计算出来的）

    @Setter
    @Getter
    private Date reaction;          //互动

    @Setter
    @Getter
    private Integer delivery;       //产能

    @Setter
    @Getter
    private Integer cost;           //成本

    @Setter
    @Getter
    private String notAccept;       //无法承诺信息(NULL:承诺,填写信息表示无法承诺)

    @Setter
    @Getter
    private String solutionFile;    //上传的详细的方案文档

    @Setter
    @Getter
    private String quotationFile;   //报价阶段上传的文档信息

    @Setter
    @Getter
    private Long topicId;           //对应供应商的唯一的topicId为了方便交谈（完全就是一个扯淡的交互）

    @Setter
    @Getter
    private Integer status;         //标记方案终投阶段只能投递一次：0:签订保密协议 1:待承诺, 2:全部承诺, 3:部分无法承诺, 4:已上传文件, 5:最终提交

    @Setter
    @Getter
    private Date createdAt;         //创建时间

    @Setter
    @Getter
    private Date updatedAt;         //修改时间

    public enum Status{
        SIGN_CONF(0 , "签订保密协议"),
        WAIT_ACCEPT(1 , "待承诺"),
        ALL_ACCEPT(2, "全部承诺"),
        LITTLE_ACCEPT(3 , "部分无法承诺"),
        SEND_FILE(4 , "已上传文件"),
        SEND_END(5 , "最终提交");

        private final Integer value;

        private final String description;

        private Status(Integer value , String description){
            this.value = value;

            this.description = description;
        }

        public static Status from(Integer value){
            for(Status status : Status.values()){
                if(Objects.equal(value, status.value)){
                    return status;
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
