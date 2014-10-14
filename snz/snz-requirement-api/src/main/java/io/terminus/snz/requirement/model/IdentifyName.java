package io.terminus.snz.requirement.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Desc:模块认证信息
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-14.
 */
public class IdentifyName implements Serializable {
    private static final long serialVersionUID = -3599380530987101038L;

    @Setter
    @Getter
    private Integer id;             //认证编号

    @Setter
    @Getter
    private String name;            //认证名称

    @Getter
    @Setter
    private Date createdAt;         //创建时间

    @Getter
    @Setter
    private Date updatedAt;         //修改时间
}
