package io.terminus.snz.requirement.dao;

import com.google.common.collect.Lists;
import io.terminus.snz.requirement.model.MWOldModuleInfo;
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
public class MWOldModuleDaoTest extends BasicTest {

    private MWOldModuleInfo init = genNew();

    private MWOldModuleInfo genNew() {
        MWOldModuleInfo i = new MWOldModuleInfo();
        i.setModuleNum("xx");
        i.setModuleName("xx");
        i.setSeriesId(1l);
        i.setUnit("y");

        return i;
    }


    @Autowired
    MWOldModuleInfoDao mwOldModuleInfoDao;

    @Before
    public void init() {
        mwOldModuleInfoDao.create(init);
        assertNotNull(init.getId());

        MWOldModuleInfo param = new MWOldModuleInfo();
        param.setModuleNum(init.getModuleNum());
        assertNotNull(mwOldModuleInfoDao.findBy(param));
    }

    @Test
    public void shouldUpdate() {
        MWOldModuleInfo update = genNew();
        mwOldModuleInfoDao.create(update);
        update.setSeriesId(2333l);
        mwOldModuleInfoDao.update(update);

        MWOldModuleInfo found = mwOldModuleInfoDao.findById(update.getId());
        assertEquals(2333l, (long)found.getSeriesId());
    }

    @Test
    public void shouldFindForDump() {
        MWOldModuleInfo stub = genNew();
        mwOldModuleInfoDao.create(stub);
        stub.setId(null);
        mwOldModuleInfoDao.create(stub);

        Long maxId = mwOldModuleInfoDao.maxId();
        assertNotNull(maxId);

        List<MWOldModuleInfo> found = mwOldModuleInfoDao.forDump(maxId, 2);
        assertEquals(2, found.size());
    }

    @Test
    public void shouldDeleteInIds() {
        List<Long> ids = Lists.newArrayList();
        MWOldModuleInfo stub = genNew();
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
    public void findByModuleNumOrModuleNameTest(){
        List<MWOldModuleInfo> byModuleNumOrModuleName = mwOldModuleInfoDao.findByModuleNumOrModuleName("x", "x");
    }
}
