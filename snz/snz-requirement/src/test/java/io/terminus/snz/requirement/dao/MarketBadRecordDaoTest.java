package io.terminus.snz.requirement.dao;

import io.terminus.snz.requirement.model.MarketBadRecord;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Copyright (c) $today.year 杭州端点网络科技有限公司
 * Date: 9/16/14
 * Time: 10:40
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */
public class MarketBadRecordDaoTest extends BasicTest {
    @Autowired
    private MarketBadRecordDao marketBadRecordDao;

    private static MarketBadRecord init;

    @Before
    public void init() {
        init = new MarketBadRecord();
        init.setOid("xxx1");
        init.setPrice(100);
        init.setReportAt(DateTime.now().withTimeAtStartOfDay().toDate());

        marketBadRecordDao.create(init);
        assertNotNull(init.getId());
    }

    @Test
    public void shouldGetMaxId() {
        Long mid = marketBadRecordDao.maxIdIn(null ,null);
        assertNotNull(mid);
    }

    @Test
    public void shouldFindForDump() {
        DateTime today = DateTime.now().withTimeAtStartOfDay();
        List found = marketBadRecordDao.forDumpIn(null, null, 0, 20, today.minusDays(1).toDate(), today.plusDays(1).toDate());
        assertFalse(found.isEmpty());
    }
}
