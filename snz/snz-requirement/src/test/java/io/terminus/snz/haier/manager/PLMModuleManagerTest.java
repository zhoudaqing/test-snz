package io.terminus.snz.haier.manager;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.terminus.snz.haier.dao.PLMModuleDao;
import io.terminus.snz.requirement.dao.ModuleDao;
import io.terminus.snz.requirement.dao.OldModuleDao;
import io.terminus.snz.requirement.dao.RequirementDao;
import io.terminus.snz.requirement.dao.RequirementSendDao;
import io.terminus.snz.requirement.model.Module;
import io.terminus.snz.requirement.model.ModuleQuota;
import io.terminus.snz.requirement.model.Requirement;
import io.terminus.snz.user.service.CompanyService;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

public class PLMModuleManagerTest{

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Mock
    private PLMModuleDao plmModuleDao;      //海尔的模块中间表数据

    @Mock
    private ModuleDao moduleDao;            //交互平台模块操作对象

    @Mock
    private CompanyService companyService;  //供应商信息管理

    @Mock
    private RequirementDao requirementDao;          //需求编号

    @Mock
    private RequirementSendDao requirementSendDao;  //需求状态切换

    @Mock
    private OldModuleDao oldModuleDao;

    @InjectMocks
    private PLMModuleManager plmModuleManager;

    @Test
    public void testSendToPLM() throws Exception {
        when(moduleDao.findModules(anyLong())).thenReturn(Arrays.asList(mockModule()));
        plmModuleManager.sendToPLM(mock());
    }

    @Test
    public void testSetModuleNum() throws Exception {
        when(moduleDao.findNoModuleNum(anyLong())).thenReturn(Arrays.asList(mockModule()));
        Map<Long, String> mockModuleMap = Maps.newHashMap();
        mockModuleMap.put(1L, "123");
        when(plmModuleDao.findAllModule(anyList())).thenReturn(mockModuleMap);
        when(requirementDao.findById(anyLong())).thenReturn(mock());
        plmModuleManager.setModuleNum();
    }

    @Test
    public void testUpdateSupplierV() throws Exception {
        List<ModuleQuota> quotaList = Lists.newArrayList();

        for(int i=0; i<5; i++){
            quotaList.add(mockQuota());
        }

        plmModuleManager.updateSupplierV(quotaList);
    }

    private Requirement mock(){
        Requirement requirement = new Requirement();
        requirement.setId(1l);
        requirement.setName("test");
        requirement.setPurchaserId(1l);
        requirement.setPurchaserName("purchaserName");
        requirement.setSeriesIds("{sids:[{id:1,name:冰箱把手},{id:21,name:冰箱门}]}");
        requirement.setCoinType(1);
        requirement.setMaterielType(1l);
        requirement.setModuleType(1);
        requirement.setDeliveryAddress("{ad:[{pa:101,fa:10},{pa:101,fa:20}]}");
        requirement.setDescription("describe");
        requirement.setAccessories("{file:[url1,url2]}");
        requirement.setSelectNum(3);
        requirement.setReplaceNum(2);
        requirement.setCompanyScope("[{id:10,name:AGH}]");
        requirement.setTacticsId(1);
        requirement.setHeadDrop("引领点");
        requirement.setModuleNum(10);
        requirement.setModuleTotal(10000);
        requirement.setCheckResult(0);
        requirement.setCreatorId(1l);
        requirement.setCreatorName("Michael");
        requirement.setCreatorPhone("18657327206");
        requirement.setCreatorEmail("MichaelZhaoZero@gmail.com");

        return requirement;
    }

    private ModuleQuota mockQuota(){
        ModuleQuota moduleQuota = new ModuleQuota();
        moduleQuota.setRequirementId(1l);
        moduleQuota.setSolutionId(1l);
        moduleQuota.setModuleId(1l);
        moduleQuota.setModuleName("moduleName");
        moduleQuota.setSupplierId(1l);
        moduleQuota.setSupplierName("Michael");
        moduleQuota.setQuantity(50);
        moduleQuota.setScale(70);

        return moduleQuota;
    }

    private Module mockModule(){
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