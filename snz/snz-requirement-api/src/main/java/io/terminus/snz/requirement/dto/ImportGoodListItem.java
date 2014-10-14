package io.terminus.snz.requirement.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Date: 7/17/14
 * Time: 10:58
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */
public class ImportGoodListItem implements Serializable {
    private static final long serialVersionUID = 7434794893959703558L;


    @Getter
    @Setter
    private Long extraInfoId;

    // start: Data fetching from Module
    @Getter
    @Setter
    private Long requirement_id;

    @Getter
    @Setter
    private String requirement_name;

    @Getter
    @Setter
    private Long series_id;

    @Getter
    @Setter
    private String series_name;

    @Getter
    @Setter
    private String module_num;

    @Getter
    @Setter
    private String module_name;

    @Getter
    @Setter
    private Long module_id;
    // end: Data fetching from Module

    @Getter
    @Setter
    private String product_line;

    @Getter
    @Setter
    private String in_charge;

}
