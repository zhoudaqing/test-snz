package io.terminus.snz.requirement.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Date: 8/15/14
 * Time: 15:58
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */

@ToString
@EqualsAndHashCode
public class SceneBadRecord implements Serializable {
    private static final long serialVersionUID = 1767770930604880391L;

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String wId;

    @Getter
    @Setter
    private String moduleNum;

    @Getter
    @Setter
    private String vCode;

    @Getter
    @Setter
    private Integer wCount;

    @Getter
    @Setter
    private Date sendAt;

    @Getter
    @Setter
    private String depart;

    @Getter
    @Setter
    private String departName;

    @Getter
    @Setter
    private String loadBatch;

    @Getter
    @Setter
    private Integer money;

    @Getter
    @Setter
    private Date createdAt;

    @Getter
    @Setter
    private Date updatedAt;
}
