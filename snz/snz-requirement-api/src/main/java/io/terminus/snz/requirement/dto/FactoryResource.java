package io.terminus.snz.requirement.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Desc:工厂配额信息
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-25.
 */
@ToString
@EqualsAndHashCode
public class FactoryResource implements Serializable {
    private static final long serialVersionUID = -327268731855365020L;

    @Getter
    @Setter
    private Long faId;          //工厂编号

    @Getter
    @Setter
    private String faNum;       //海尔的工程代码

    @Getter
    @Setter
    private String faName;      //工厂名称

    @Getter
    @Setter
    private Integer num;        //工厂资源量

    @Getter
    @Setter
    private Integer salesId;    //采购单位编号

    @Getter
    @Setter
    private String salesName;   //采购单位名称
}
