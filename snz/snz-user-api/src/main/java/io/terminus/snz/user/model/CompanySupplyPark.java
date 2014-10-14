package io.terminus.snz.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Author:Guo Chaopeng
 * Created on 14-7-4.
 */
@ToString
public class CompanySupplyPark implements Serializable {

    private static final long serialVersionUID = 7023596549866763880L;

    @Setter
    @Getter
    private Long id;                //自增主键

    @Setter
    @Getter
    private Long companyId;         //企业编号

    @Setter
    @Getter
    private Long userId;            //用户编号

    @Setter
    @Getter
    private Long supplyParkId;      //供货园区编号

    @Setter
    @Getter
    private String name;            //供货园区名称

    @Setter
    @Getter
    private Date createdAt;         //创建时间

    @Setter
    @Getter
    private Date updatedAt;         //修改时间

}
