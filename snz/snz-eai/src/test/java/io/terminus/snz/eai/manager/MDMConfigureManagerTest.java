package io.terminus.snz.eai.manager;

import com.google.common.collect.Lists;
import io.terminus.snz.eai.dao.MDMConfigureDao;
import io.terminus.snz.eai.model.MDMConfigure;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Date: 8/4/14
 * Time: 2:11
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */
public class MDMConfigureManagerTest extends BaseManagerTest {

    @InjectMocks
    MDMSelectInfoManager mdmConfigureManager;

    @Mock
    MDMConfigureDao mdmConfigureDao;

    @Test
    public void shouldBulkInsertOrUpdate() {
        List<MDMConfigure> configureList = Lists.<MDMConfigure>newArrayList();
        for (int i = 0; i < 20; i++) {
            configureList.add(genNew(String.valueOf(i), "name"+i, MDMConfigure.TYPE.NATION.toValue()));
        }

        when(mdmConfigureDao.create(any(MDMConfigure.class))).thenReturn(1L);
        when(mdmConfigureDao.findListBy(any(MDMConfigure.class))).thenReturn(configureList);
        mdmConfigureManager.bulkInsertOrUpdate(configureList);
        List<MDMConfigure> found = mdmConfigureDao.findListBy(new MDMConfigure());
        assertFalse(found.isEmpty());
        assertEquals(20, found.size());
    }

    private MDMConfigure genNew(String code, String name, Integer integer) {
        MDMConfigure c = new MDMConfigure();
        c.setCode(code);
        c.setName(name);
        c.setType(integer);
        return c;
    }
}
