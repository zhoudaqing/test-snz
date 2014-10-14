package io.terminus.snz.requirement.dao;

import com.google.common.collect.Lists;
import io.terminus.snz.requirement.model.SceneBadRecord;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Date: 8/20/14
 * Time: 10:24
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */
public class SceneBadRecordDaoTest extends BasicTest {
    private SceneBadRecord init = genNew();

    private SceneBadRecord genNew() {
        SceneBadRecord n = new SceneBadRecord();
        n.setModuleNum("xxx");
        n.setVCode("sa");
        n.setWId("ola");

        return n;
    }

    @Autowired
    SceneBadRecordDao sceneBadRecordDao;

    @Before
    public void init() {
        sceneBadRecordDao.create(init);
        assertNotNull(init.getId());

        assertNotNull(sceneBadRecordDao.findById(init.getId()));
        assertNotNull(sceneBadRecordDao.findByWId(init.getWId()));
    }

    @Test
    public void shouldFindBetween() {
        DateTime now = DateTime.now().withTimeAtStartOfDay();
        SceneBadRecord create = genNew();
        create.setSendAt(now.minusDays(2).toDate());
        sceneBadRecordDao.create(create);

        create.setSendAt(now.minusDays(4).toDate());
        sceneBadRecordDao.create(create);

        assertFalse(sceneBadRecordDao.findBetween("sa", now.minusDays(2).toDate(), now.toDate()).isEmpty());
        assertEquals(2, sceneBadRecordDao.findBetween("sa", now.minusDays(4).toDate(), now.toDate()).size());
    }

    @Test
    public void shouldGetMaxId() {
        Long maxId = sceneBadRecordDao.maxMWId();
        assertNotNull(maxId);

        //assertEquals(2, sceneBadRecordDao.forMWDump(maxId, 2, null, null).size());
    }

    @Test
    public void shouldDeleteMWByIds() {
        List<Long> ids = Lists.newArrayList(1l, 3l, 5l);
        assertNotNull(sceneBadRecordDao.deleteMWByIds(ids));
    }
}
