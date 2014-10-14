package io.terminus.snz.requirement.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Copyright (c) $today.year 杭州端点网络科技有限公司
 * Date: 9/19/14
 * Time: 15:10
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */

@ToString
@EqualsAndHashCode
public class MWMarketBadRecord implements Serializable {
    private static final long serialVersionUID = 7439884436159235226L;
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String oid;

    @Getter
    @Setter
    private Date reportAt;

    @Getter
    @Setter
    private String port;

    @Getter
    @Setter
    private String pl;

    @Getter
    @Setter
    private String business;

    @Getter
    @Setter
    private String branch;

    @Getter
    @Setter
    private String productId;

    @Getter
    @Setter
    private String productSid;

    @Getter
    @Setter
    private Date assembledAt;

    @Getter
    @Setter
    private Date logAt;

    @Getter
    @Setter
    private String complain;

    @Getter
    @Setter
    private String maintType;

    @Getter
    @Setter
    private String serviceType;

    @Getter
    @Setter
    private String describe;

    @Getter
    @Setter
    private String cause;

    @Getter
    @Setter
    private String object;

    @Getter
    @Setter
    private String action;

    @Getter
    @Setter
    private String moduleNum;

    @Getter
    @Setter
    private String vCode;

    @Setter
    @Getter
    private String price;

    @Setter
    @Getter
    private String fee;

    @Getter
    @Setter
    private String moduleName;

    @Getter
    @Setter
    private String status;
}
