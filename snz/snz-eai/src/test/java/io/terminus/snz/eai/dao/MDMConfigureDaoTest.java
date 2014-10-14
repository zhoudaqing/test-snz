package io.terminus.snz.eai.dao;

import com.google.common.collect.Lists;
import io.terminus.snz.eai.model.MDMConfigure;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static io.terminus.snz.eai.model.MDMConfigure.TYPE;
import static org.junit.Assert.*;

/**
 * Date: 7/25/14
 * Time: 11:09
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */
public class MDMConfigureDaoTest extends TestBaseDao {

    @Autowired
    MDMConfigureDao mdmConfigureDao;

    private final MDMConfigure init = genNew();

    private MDMConfigure genNew() {
        MDMConfigure gen = new MDMConfigure();
        gen.setCode("10010");
        gen.setName("hehe");
        gen.setTypeEnum(TYPE.METHOD);
        return gen;
    }

    @Before
    public void setup() {
        mdmConfigureDao.create(init);

        assertNotNull(init.getId());
    }

    @Test
    public void shouldFindListBy() {
        MDMConfigure param = new MDMConfigure();
        param.setTypeEnum(TYPE.METHOD);

        List<MDMConfigure> found = mdmConfigureDao.findListBy(param);
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
    }

    @Test
    public void shouldFindBy() {
        MDMConfigure param = new MDMConfigure();
        param.setId(init.getId());
        assertNotNull(mdmConfigureDao.findBy(param));
    }

    @Test
    public void shouldFindById() {
        assertNotNull(mdmConfigureDao.findById(init.getId()));
    }

    @Test
    public void shouldUpdate() {
        MDMConfigure update = genNew();
        mdmConfigureDao.create(update);

        update.setName("xxx");
        mdmConfigureDao.update(update);
        assertEquals("xxx", mdmConfigureDao.findById(update.getId()).getName());
    }

    @Test
    public void shouldFindListByTypes() {
        MDMConfigure insert = genNew();
        insert.setTypeEnum(TYPE.TERM);
        mdmConfigureDao.create(insert);
        insert = genNew();
        insert.setTypeEnum(TYPE.CODE);
        mdmConfigureDao.create(insert);

        List<Integer> types = Lists.newArrayList(TYPE.CODE.toValue(), TYPE.TERM.toValue());
        List found = mdmConfigureDao.findListByTypes(types);
        assertFalse(found.isEmpty());
        assertEquals(2, found.size());
    }
}
