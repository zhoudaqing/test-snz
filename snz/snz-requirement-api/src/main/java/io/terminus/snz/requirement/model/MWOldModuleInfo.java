package io.terminus.snz.requirement.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * middle ware old module info table
 *
 * Date: 8/13/14
 * Time: 18:02
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */

@ToString
@EqualsAndHashCode
public class MWOldModuleInfo implements Serializable {
    private static final long serialVersionUID = -4758959525518457398L;
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String moduleNum;

    @Getter
    @Setter
    private String moduleName;

    @Getter
    @Setter
    private String unit;

    @Getter
    @Setter
    private Long seriesId;

    @Getter
    @Setter
    private Date createdAt;

    @Getter
    @Setter
    private Date updatedAt;
}
