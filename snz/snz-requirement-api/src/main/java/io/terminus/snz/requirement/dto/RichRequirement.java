package io.terminus.snz.requirement.dto;

import io.terminus.common.model.Indexable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by yangzefeng on 14-6-20
 */
@EqualsAndHashCode @ToString
public class RichRequirement implements Serializable,Indexable {
    private static final long serialVersionUID = 2550903468252937152L;

    @Getter
    @Setter
    private Long id;            //需求id

    @Getter
    @Setter
    private String name;        //需求名称

    @Getter
    @Setter
    private List<Long> firstLevelIds;     //前台一级类目id列表

    @Getter
    @Setter
    private List<Long> secondLevelIds;      //前台二级类目id列表

    @Getter
    @Setter
    private List<Long> thirdLevelIds;  //前台三级类目id列表

    @Getter
    @Setter
    private List<String> firstLevelNames; //前台一级类目名称列表

    @Getter
    @Setter
    private Long purchaseId;        //需求方id

    @Getter
    @Setter
    private String purchaserName;   //需求方

    @Getter
    @Setter
    private Integer purchaseNum;    //采购数量

    @Getter
    @Setter
    private Integer moduleNum;      //模块数量

    @Getter
    @Setter
    private Integer moduleType;     //模块属性

    @Getter
    @Setter
    private Integer materielType;    //物料类别

    @Getter
    @Setter
    private String deliveryAddress;     //配套园区

    @Getter
    @Setter
    private Integer status;     //需求状态（1:需求交互，2:需求锁定，3:方案交互，4:方案综投，5:选定供应商与方案，招标结束）

    @Getter
    @Setter
    private Date predictEnd;    //预计结束时间

    @Getter
    @Setter
    private Date checkTime;     //需求发布时间
}
