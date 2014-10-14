package io.terminus.snz.requirement.manager;

import com.google.common.collect.Lists;
import io.terminus.snz.requirement.dao.MWOldModuleInfoDao;
import io.terminus.snz.requirement.dao.ModuleDao;
import io.terminus.snz.requirement.model.MWOldModuleInfo;
import io.terminus.snz.requirement.model.Module;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

/**
 * Date: 8/14/14
 * Time: 0:00
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */
public class ModuleManagerTest {

    @InjectMocks
    ModuleManager moduleManager;

    @Mock
    ModuleDao moduleDao;

    @Mock
    MWOldModuleInfoDao mwOldModuleInfoDao;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldWriteOldModule() {
        when(moduleDao.findOneBy(any(Module.class))).thenReturn(mock());
        when(moduleDao.update(any(Module.class))).thenReturn(true);
        when(mwOldModuleInfoDao.maxId()).thenReturn(1000l);
        MWOldModuleInfo moduleInfoMock = new MWOldModuleInfo();
        moduleInfoMock.setModuleName("a");
        moduleInfoMock.setModuleNum("a");
        moduleInfoMock.setSeriesId(1l);
        moduleInfoMock.setUnit("EA");
        when(mwOldModuleInfoDao.forDump(anyLong(), anyInt()))
                .thenReturn(Lists.newArrayList(moduleInfoMock))
                .thenReturn(Lists.<MWOldModuleInfo>newArrayList());
        when(mwOldModuleInfoDao.deleteInIds(anyList())).thenReturn(10);

        moduleManager.writeOldModule();
    }

    private Module mock(){
        Module module = new Module();
        module.setRequirementId(1l);
        module.setType(1);
        module.setModuleName("spu");
        module.setTotal(3000);
        module.setQuality(100);
        module.setCost(98);
        module.setDelivery(100);
        module.setAttestations("[{id:10,name:'EU认证'}, {id:10,name:'EU认证'}]");
        module.setSupplyAt(DateTime.now().toDate());

        return module;
    }
}
