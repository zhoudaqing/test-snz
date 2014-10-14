package io.terminus.snz.related.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Desc:地址的工厂信息
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-06-26.
 */
@ToString
@EqualsAndHashCode
public class AddressFactory implements Serializable {
    private static final long serialVersionUID = -4872353283963109864L;

    @Setter
    @Getter
    private Long id;                //自增编号

    @Setter
    @Getter
    private String factoryNum;      //工厂编号

    @Setter
    @Getter
    private String factoryName;     //工厂名称

    @Getter
    @Setter
    private Date createdAt;         //创建时间

    @Getter
    @Setter
    private Date updatedAt;         //修改时间
}
