package io.terminus.snz.requirement.dto;

import com.google.common.collect.Lists;
import io.terminus.common.model.Paging;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Date: 8/11/14
 * Time: 11:17
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */
public class SupplierReparationsDto implements Serializable {
    private static final long serialVersionUID = -6121269011783321350L;

    @Setter
    private List<SupplierReparationDailyDto> details;

    @Setter
    private Paging<SupplierReparationDailyDto> currentPage;

    @Getter
    @Setter
    private List<Integer> qualities = new ArrayList<Integer>();

    @Getter
    @Setter
    private List<Double> moneyList = new ArrayList<Double>();

    @Getter
    @Setter
    private Integer money = 0;

    @Getter
    @Setter
    private Integer quantity = 0;

    @Getter
    @Setter
    private String supplierName;

    public void addMoney(Integer money) {
        this.money += money;
    }

    public void addQuantity(Integer quantity) {
        this.quantity += quantity;
    }


    public List<SupplierReparationDailyDto> getDetails() {
        if (details==null) {
            details = Lists.newArrayList();
        }

        return details;
    }

    public Paging<SupplierReparationDailyDto> getCurrentPage() {
        if(currentPage == null) {
            currentPage = new Paging<SupplierReparationDailyDto>(0l, Lists.<SupplierReparationDailyDto>newArrayList());
        }
        return currentPage;
    }
}
