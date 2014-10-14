package io.terminus.snz.eai.dao;

import com.google.common.collect.Lists;
import io.terminus.snz.eai.model.MDMOldModuleInfo;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Date: 8/13/14
 * Time: 18:27
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */
public class MDMOldModuleDaoTest extends TestBaseDao {

    @Autowired
    MDMOldModuleInfoDao mwOldModuleInfoDao;

    private MDMOldModuleInfo init = genNew();

    private MDMOldModuleInfo genNew() {
        MDMOldModuleInfo i = new MDMOldModuleInfo();
        i.setModuleNum("xx");
        i.setModuleName("xx");
        i.setSeriesId(1l);
        i.setUnit("y");

        return i;
    }

    @Before
    public void init() {
        mwOldModuleInfoDao.create(init);
        assertNotNull(init.getId());

        MDMOldModuleInfo param = new MDMOldModuleInfo();
        param.setModuleNum(init.getModuleNum());
        assertNotNull(mwOldModuleInfoDao.findBy(param));
    }

    @Test
    public void shouldUpdate() {
        MDMOldModuleInfo update = genNew();
        mwOldModuleInfoDao.create(update);
        update.setSeriesId(2333l);
        mwOldModuleInfoDao.update(update);

        MDMOldModuleInfo found = mwOldModuleInfoDao.findById(update.getId());
        assertEquals(2333l, (long)found.getSeriesId());
    }

    @Test
    public void shouldFindForDump() {
        MDMOldModuleInfo stub = genNew();
        mwOldModuleInfoDao.create(stub);
        stub.setId(null);
        mwOldModuleInfoDao.create(stub);

        Long maxId = mwOldModuleInfoDao.maxId();
        assertNotNull(maxId);

        List<MDMOldModuleInfo> found = mwOldModuleInfoDao.forDump(maxId, 2);
        assertEquals(2, found.size());
    }

    @Test
    public void shouldDeleteInIds() {
        List<Long> ids = Lists.newArrayList();
        MDMOldModuleInfo stub = genNew();
        mwOldModuleInfoDao.create(stub);
        ids.add(stub.getId());
        stub.setId(null);
        mwOldModuleInfoDao.create(stub);
        ids.add(stub.getId());

        mwOldModuleInfoDao.deleteInIds(ids);

        assertNull(mwOldModuleInfoDao.findById(ids.get(0)));
        assertNull(mwOldModuleInfoDao.findById(ids.get(1)));
    }

    @Test
    public void shouldFindByModuleNum() {
        assertNotNull(mwOldModuleInfoDao.findByModuleNum("xx"));
    }
}
