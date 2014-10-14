package io.terminus.snz.requirement.ignore;

import com.google.common.collect.Lists;
import io.terminus.snz.haier.dao.PLMModuleDao;
import io.terminus.snz.haier.model.PLMModule;
import io.terminus.snz.haier.tool.DBDataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@Ignore
public class PLMModuleDaoTest {

    @Mock
    private DBDataSource dataSource;

    @InjectMocks
    private PLMModuleDao plmModuleDao;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreate() throws Exception {
        BasicDataSource basicDataSource = PowerMockito.mock(BasicDataSource.class);
        assertNotNull(plmModuleDao.create(mock()));
    }

    @Test
    public void testCreateBatch() throws Exception {
        when(dataSource.getSource(anyString())).thenReturn(new BasicDataSource());
        List<PLMModule> plmModuleList = Lists.newArrayList();

        for(int i=0; i<5; i++){
            plmModuleList.add(mock());
        }

        assertNotNull(plmModuleDao.createBatch(plmModuleList));
    }

    @Test
    public void testUpdateSupplierVId() throws Exception {
        when(dataSource.getSource(anyString())).thenReturn(new BasicDataSource());
        List<PLMModule> plmModuleList = Lists.newArrayList();

        for(int i=0; i<5; i++){
            plmModuleList.add(mock());
        }

        assertNotNull(plmModuleDao.updateSupplierVId(plmModuleList));
    }

    @Test
    public void testFindAllModule() throws Exception {
        when(dataSource.getSource(anyString())).thenReturn(new BasicDataSource());
        assertNotNull(plmModuleDao.findAllModule(Lists.newArrayList("1" , "2", "3")));
    }

    @Test
    public void testFindByModuleId() throws Exception {
        when(dataSource.getSource(anyString())).thenReturn(new BasicDataSource());
        assertNotNull(plmModuleDao.findByModuleId("1"));
    }

    private PLMModule mock(){
        PLMModule plmModule = new PLMModule();

        plmModule.setSupplierName("supplierName");
        plmModule.setSupplierVId("VID");
        plmModule.setModuleExampleName("exampleName");
        plmModule.setModuleExampleId("exampleId");
        plmModule.setDemandRelTime("demandId");
        plmModule.setDemandName("demandName");
        plmModule.setDemandRelPerson("person");
        plmModule.setPartNumber("number");
        plmModule.setReleaseDate("releaseDate");

        return plmModule;
    }
}