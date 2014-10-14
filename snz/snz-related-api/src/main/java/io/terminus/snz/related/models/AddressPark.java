package io.terminus.snz.related.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Desc:地址的园区信息
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-06-26.
 */
public class AddressPark implements Serializable {
    private static final long serialVersionUID = -2403944711906956966L;

    @Setter
    @Getter
    private Long id;        //园区编号

    @Setter
    @Getter
    private String parkName;      //园区名称

    @Getter
    @Setter
    private Date createdAt;         //创建时间

    @Getter
    @Setter
    private Date updatedAt;         //修改时间
}
