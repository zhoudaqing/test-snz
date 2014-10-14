package io.terminus.snz.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 供应商整体区域信息
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-7-19
 */
public class SupplierTQRDCInfos implements Serializable {

    private static final long serialVersionUID = -2768954753474124884L;

    @Getter
    @Setter
    private List<Integer> locations;            //最近月区域计数信息, 依此为: 红区, 黄区, 蓝区, 绿区

    @Getter
    @Setter
    private List<List<Integer>> yearLocations;  //年内每月份区域计数信息

    @Setter
    @Getter
    private String lastMonth;                   //最新月份

}
