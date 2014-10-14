package io.terminus.snz.requirement.model;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Date: 8/15/14
 * Time: 13:09
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */
public class MarketBadRecord implements Serializable {

    private static final long serialVersionUID = -408782743380334033L;

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
    private Integer price;

    public Integer getPrice() {
        return Objects.firstNonNull(price, 0);
    }

    @Setter
    private Integer fee;

    public Integer getFee() {
        return Objects.firstNonNull(fee, 0);
    }

    @Getter
    @Setter
    private String moduleName;

    @Getter
    @Setter
    private String status;

    public static MarketBadRecord from(MWMarketBadRecord mmbr) {
        MarketBadRecord mbr = new MarketBadRecord();
        mbr.setId(mmbr.getId());
        mbr.setOid(mmbr.getOid());
        mbr.setReportAt(mmbr.getReportAt());
        mbr.setPort(mmbr.getPort());
        mbr.setPl(mmbr.getPl());
        mbr.setBusiness(mmbr.getBusiness());
        mbr.setBranch(mmbr.getBranch());
        mbr.setProductId(mmbr.getProductId());
        mbr.setProductSid(mmbr.getProductSid());
        mbr.setAssembledAt(mmbr.getAssembledAt());
        mbr.setLogAt(mmbr.getLogAt());
        mbr.setComplain(mmbr.getComplain());
        mbr.setMaintType(mmbr.getMaintType());
        mbr.setServiceType(mmbr.getServiceType());
        mbr.setDescribe(mmbr.getDescribe());
        mbr.setCause(mmbr.getCause());
        mbr.setObject(mmbr.getObject());
        mbr.setAction(mmbr.getAction());
        mbr.setModuleNum(mmbr.getModuleNum());
        mbr.setVCode(mmbr.getVCode());
        mbr.setModuleName(mmbr.getModuleName());
        mbr.setStatus(mmbr.getStatus());

        return mbr;
    }
}
