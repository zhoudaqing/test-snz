package io.terminus.snz.eai.dao;

import com.google.common.collect.Maps;
import io.terminus.snz.eai.model.MDMBankView;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Date: 8/5/14
 * Time: 11:40
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */
public class MDMBankViewDaoTest extends TestBaseDao {

    @Autowired
    MDMBankViewDao mdmBankViewDao;

    private MDMBankView init = genNew();

    private MDMBankView genNew() {
        MDMBankView view = new MDMBankView();
        view.setName("xx");
        view.setCode("123");
        view.setCountry("CN");
        return view;
    }

    @Before
    public void init() {
        mdmBankViewDao.create(init);
        assertNotNull(init.getId());
    }

    @Test
    public void shouldFindBy() {
        MDMBankView param = new MDMBankView();
        param.setId(init.getId());
        assertNotNull(mdmBankViewDao.findBy(param));
    }

    @Test
    public void shouldFindById() {
        assertNotNull(mdmBankViewDao.findById(init.getId()));
    }

    @Test
    public void shouldUpdate() {
        MDMBankView stub = genNew();
        mdmBankViewDao.create(stub);
        assertNotNull(stub.getId());

        stub.setName("hehe");
        mdmBankViewDao.update(stub);
        MDMBankView found = mdmBankViewDao.findById(stub.getId());
        assertEquals("hehe", found.getName());
    }
    @Test
    public void shouldFindByFuzzy() {
        Map params = Maps.newHashMap();

        params.put("offset", 0);
        params.put("size", 20);
        assertNotNull(mdmBankViewDao.pagingBankFindByFuzzyName(params));

    }

    @Test
    public void shouldfindBankByName() {
        assertNull(mdmBankViewDao.findBankByName("中国农业银行"));
    }
}
