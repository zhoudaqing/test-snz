package io.terminus.snz.related.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Desc:类目与工厂的关系
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-08-07.
 */
@ToString
@EqualsAndHashCode
public class CategoryFactory implements Serializable {
    private static final long serialVersionUID = 3954493490818426645L;

    @Getter
    @Setter
    private Long id;                //自增编号

    @Getter
    @Setter
    private Long productId;         //类目编号

    @Getter
    @Setter
    private Long parkId;            //园区编号

    @Getter
    @Setter
    private Long factoryId;         //工厂编号

    @Getter
    @Setter
    private Date createdAt;         //创建时间

    @Getter
    @Setter
    private Date updatedAt;         //修改时间
}
