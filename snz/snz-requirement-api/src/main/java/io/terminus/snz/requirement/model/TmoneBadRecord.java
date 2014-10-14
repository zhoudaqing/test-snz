package io.terminus.snz.requirement.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by grancy on 14-9-24.
 * T-1未准时交付明细
 */
public class TmoneBadRecord implements Serializable{

    private static final long serialVersionUID = 5829796798448753325L;

    @Getter
    @Setter
    private Long id;                                                    //自增id

    @Getter
    @Setter
    private Long listId;                                                //主表索赔id

    @Getter
    @Setter
    private String listNum;                                             //订单号

    @Getter
    @Setter
    private String factory;                                             //'工厂'

    @Getter
    @Setter
    private String numOfProof;                                          //'采购凭证号'

    @Getter
    @Setter
    private String numOfParam;                                          //'采购凭证的项目编号'

    @Getter
    @Setter
    private String supplierAccount;                                     //'供应商标识'

    @Getter
    @Setter
    private String materielNo;                                          //'物料号'

    @Getter
    @Setter
    private Date timeOfDelivery;                                        //'应交货日期'

    @Getter
    @Setter
    private Date rTimeOfDelivery;                                       //'实际交货日期'

    @Getter
    @Setter
    private Date yTimeOfDelivery;                                       //'预约交货日期'

    @Getter
    @Setter
    private Integer numOfList;                                          //'采购订单数量'

    @Getter
    @Setter
    private Integer numOfDelivery;                                      //'实际收货数量'

    @Getter
    @Setter
    private Integer numOfDifference;                                    //'差异数量'

    @Getter
    @Setter
    private String descOfDifference;                                    //'交易差异原因'

    @Getter
    @Setter
    private Date currentDay;                                            //'当前日期'

    @Getter
    @Setter
    private Date currentTime;                                           //'当前时间'

    @Getter
    @Setter
    private Date createdAt;                                             //'创建时间'

    @Getter
    @Setter
    private Date updatedAt;                                             //'更新时间'

}
