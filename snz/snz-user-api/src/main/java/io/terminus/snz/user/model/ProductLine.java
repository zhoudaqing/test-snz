package io.terminus.snz.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Author:Guo Chaopeng
 * Created on 14-7-2.
 */
@ToString
public class ProductLine implements Serializable {

    private static final long serialVersionUID = 3089686390178848049L;
    @Setter
    @Getter
    private Long id;       //所属产品线编号

    @Setter
    @Getter
    private String name;   //所属产品线名称

}
