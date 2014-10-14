package io.terminus.snz.user.manager;

import io.terminus.snz.user.dao.*;
import io.terminus.snz.user.model.SupplierCountByIndustry;
import io.terminus.snz.user.model.SupplierCountByLevel;
import io.terminus.snz.user.model.SupplierCountByStatus;
import io.terminus.snz.user.model.SupplierCountBySupplyPark;
import io.terminus.snz.user.tool.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Author:Guo Chaopeng
 * Created on 14-7-23.
 */
@Component
public class SupplierSummaryManager {

    @Autowired
    private CompanySupplyParkDao companySupplyParkDao;

    @Autowired
    private SupplierCountBySupplyParkDao supplierCountBySupplyParkDao;

    @Autowired
    private SupplierCountByStatusDao supplierCountByStatusDao;

    @Autowired
    private SupplierCountByLevelDao supplierCountByLevelDao;

    @Autowired
    private SupplierCountByIndustryDao supplierCountByIndustryDao;

    @Transactional
    public void createSupplierSummaryBySupplyParkIds(List<Long> supplyParkIds) {

        Date date = DateUtil.getYesterdayStart();

        for (Long supplyParkId : supplyParkIds) {
            Long supplierCount = companySupplyParkDao.countBySupplyParkId(supplyParkId);

            SupplierCountBySupplyPark supplierCountBySupplyPark = new SupplierCountBySupplyPark();
            supplierCountBySupplyPark.setDate(date);
            supplierCountBySupplyPark.setSupplyParkId(supplyParkId);
            supplierCountBySupplyPark.setSupplierCount(supplierCount);

            supplierCountBySupplyParkDao.create(supplierCountBySupplyPark);
        }
    }

    @Transactional
    public void createSupplierSummaryByStatus(List<SupplierCountByStatus> supplierCountByStatuses) {
        for (SupplierCountByStatus supplierCountByStatus : supplierCountByStatuses) {
            supplierCountByStatusDao.create(supplierCountByStatus);
        }
    }

    @Transactional
    public void createSupplierSummaryByLevel(List<SupplierCountByLevel> supplierCountByLevels) {
        for (SupplierCountByLevel supplierCountByLevel : supplierCountByLevels) {
            supplierCountByLevelDao.create(supplierCountByLevel);
        }
    }

    @Transactional
    public void createSupplierSummaryByIndustry(List<SupplierCountByIndustry> supplierCountByIndustries) {
        for (SupplierCountByIndustry supplierCountByIndustry : supplierCountByIndustries) {
            supplierCountByIndustryDao.create(supplierCountByIndustry);
        }
    }

}
