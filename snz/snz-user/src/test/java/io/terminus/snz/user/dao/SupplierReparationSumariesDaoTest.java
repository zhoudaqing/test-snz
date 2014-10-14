package io.terminus.snz.user.dao;

import io.terminus.snz.user.model.SupplierReparationSumaries;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Copyright (c) $today.year 杭州端点网络科技有限公司
 * Date: 9/16/14
 * Time: 18:17
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */
public class SupplierReparationSumariesDaoTest extends TestBaseDao {
    @Autowired
    SupplierReparationSumariesDao supplierReparationSumariesDao;

    private static SupplierReparationSumaries init;

    @Before
    public void init() {
        init = new SupplierReparationSumaries();
        init.increaseDailyAmount(199);
        init.increaseWeeklyAmount(100);
        init.increaseMonthlyAmount(100);
        init.increaseYearlyAmount(100);
        init.setSupplierUid(1l);

        supplierReparationSumariesDao.create(init);
    }

    @Test
    public void stub() {
        Assert.assertNotNull(supplierReparationSumariesDao.findSupplierReparationSumariesBySupplierUid(init.getSupplierUid()));
    }
}
