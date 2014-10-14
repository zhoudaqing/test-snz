package io.terminus.snz.requirement.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 对应中间表 mw_supplier_scene_bad_records
 * 包含现场不良信息
 *
 * Date: 8/20/14
 * Time: 10:01
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */
public class MWSceneBadRecord implements Serializable {

    private static final long serialVersionUID = -113689612129685538L;

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String wasterid;

    @Getter
    @Setter
    private String specialtiesno;

    @Getter
    @Setter
    private String supplycode;

    @Getter
    @Setter
    private String wastercount;

    @Getter
    @Setter
    private String sendday;

    @Getter
    @Setter
    private String careerdepart;

    @Getter
    @Setter
    private String created_date;

    @Getter
    @Setter
    private String last_modified_date;

    @Getter
    @Setter
    private String load_batch;

    @Getter
    @Setter
    private String penalty;
}
