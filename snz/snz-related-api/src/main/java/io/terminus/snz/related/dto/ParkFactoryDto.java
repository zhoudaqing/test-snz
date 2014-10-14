package io.terminus.snz.related.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Desc:园区和工厂的数据集合
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-08-14.
 */
@ToString
@EqualsAndHashCode
public class ParkFactoryDto implements Serializable {
    private static final long serialVersionUID = 4548997642749606643L;

    @Getter
    @Setter
    private Long parkId;        //园区编号

    @Getter
    @Setter
    private String parkName;    //园区名称

    @Getter
    @Setter
    private Long factoryId;     //工厂id

    @Getter
    @Setter
    private String factoryNum;  //工厂编号

    @Getter
    @Setter
    private String factoryName; //工厂名称
}
