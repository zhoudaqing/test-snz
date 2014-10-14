package io.terminus.snz.requirement.service;

import com.google.common.collect.Lists;
import io.terminus.common.model.Paging;
import io.terminus.pampas.common.Response;
import io.terminus.snz.category.model.BackendCategoryProperty;
import io.terminus.snz.category.service.BackendCategoryService;
import io.terminus.snz.haier.manager.PLMModuleManager;
import io.terminus.snz.requirement.dao.IdentifyNameDao;
import io.terminus.snz.requirement.dao.ModuleDao;
import io.terminus.snz.requirement.dao.RequirementDao;
import io.terminus.snz.requirement.dto.DerivativeDto;
import io.terminus.snz.requirement.dto.ModuleInfoDto;
import io.terminus.snz.requirement.dto.ModulesDto;
import io.terminus.snz.requirement.manager.CountManager;
import io.terminus.snz.requirement.manager.ModuleManager;
import io.terminus.snz.requirement.model.*;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Desc:
 * Mail: houly@terminus.io
 * author: Hou Luyao
 * Date: 14-8-5.
 */
public class ModuleServiceTest{

    @Mock
    private RequirementDao requirementDao;

    @Mock
    private ModuleDao moduleDao;

    @Mock
    private ModuleManager moduleManager;

    @Mock
    private IdentifyNameDao identifyNameDao;

    @Mock
    private CountManager countManager;

    @Mock
    private PLMModuleManager plmModuleManager;

    @Mock
    private BackendCategoryService backendCategoryService;

    @InjectMocks
    private ModuleServiceImpl moduleServiceImpl;

    @Before
    public void init(){
        // NOTE: necessary
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreate() throws Exception {
        ModuleInfoDto module = createModule();
        module.getModule().setRequirementId(null);
        Response<Boolean> response = moduleServiceImpl.createModule(module);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"requirement.id.null");

        module = createModule();
        module.getModule().setModuleName(null);
        response = moduleServiceImpl.createModule(module);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"module.moduleName.null");

        module = createModule();
        response = moduleServiceImpl.createModule(module);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"module.resourceNum.null");

        module = createModule();
        module.getModule().setTotal(null);
        response = moduleServiceImpl.createModule(module);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"module.total.null");

        module = createModule();
        module.getModule().setPropertyId(null);
        response = moduleServiceImpl.createModule(module);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"module.property.null");

        module = createModule();
        when(backendCategoryService.findPropertiesByBcId(anyLong())).thenReturn(new Response<List<BackendCategoryProperty>>());
        response = moduleServiceImpl.createModule(module);
        assertFalse(response.isSuccess());
        assertNull(response.getError());

        module = createModule();
        Response<List<BackendCategoryProperty>> r = new Response<List<BackendCategoryProperty>>();
        r.setResult(Lists.<BackendCategoryProperty>newArrayList());
        when(backendCategoryService.findPropertiesByBcId(anyLong())).thenReturn(r);
        response = moduleServiceImpl.createModule(module);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"module.property.null");

        module = createModule();
        r = new Response<List<BackendCategoryProperty>>();
        List<BackendCategoryProperty> list = Lists.newArrayList();
        list.add(createBackendCategoryProperty());
        r.setResult(list);
        when(backendCategoryService.findPropertiesByBcId(anyLong())).thenReturn(r);
        response = moduleServiceImpl.createModule(module);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"module.total.small");

        module = createModule();
        r = new Response<List<BackendCategoryProperty>>();
        list = Lists.newArrayList();
        BackendCategoryProperty b = createBackendCategoryProperty();
        b.setValue1("1");
        list.add(b);
        r.setResult(list);
        when(backendCategoryService.findPropertiesByBcId(anyLong())).thenReturn(r);
        when(requirementDao.findById(anyLong())).thenReturn(createRequirement());
        response = moduleServiceImpl.createModule(module);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"requirement.lock.existed");

        module = createModule();
        module.getModule().setType(null);
        r = new Response<List<BackendCategoryProperty>>();
        list = Lists.newArrayList();
        b = createBackendCategoryProperty();
        b.setValue1("1");
        list.add(b);
        r.setResult(list);
        when(backendCategoryService.findPropertiesByBcId(anyLong())).thenReturn(r);
        when(requirementDao.findById(anyLong())).thenReturn(null);
        response = moduleServiceImpl.createModule(module);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"module.create.failed");

        module = createModule();
        module.getModule().setType(3);
        module.getModule().setOldModuleId(null);
        r = new Response<List<BackendCategoryProperty>>();
        list = Lists.newArrayList();
        b = createBackendCategoryProperty();
        b.setValue1("1");
        list.add(b);
        r.setResult(list);
        when(backendCategoryService.findPropertiesByBcId(anyLong())).thenReturn(r);
        when(requirementDao.findById(anyLong())).thenReturn(new Requirement());
        response = moduleServiceImpl.createModule(module);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"module.oldModuleId.null");

        module = createModule();
        module.getModule().setType(3);
        module.getModule().setModuleNum(null);
        r = new Response<List<BackendCategoryProperty>>();
        list = Lists.newArrayList();
        b = createBackendCategoryProperty();
        b.setValue1("1");
        list.add(b);
        r.setResult(list);
        when(backendCategoryService.findPropertiesByBcId(anyLong())).thenReturn(r);
        when(requirementDao.findById(anyLong())).thenReturn(new Requirement());
        response = moduleServiceImpl.createModule(module);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"module.moduleNum.null");

        module = createModule();
        module.getModule().setType(3);
        List<Module> modules = Lists.newArrayList(new Module());
        when(moduleDao.findDerive(anyString(),anyInt())).thenReturn(modules);
        r = new Response<List<BackendCategoryProperty>>();
        list = Lists.newArrayList();
        b = createBackendCategoryProperty();
        b.setValue1("1");
        list.add(b);
        r.setResult(list);
        when(backendCategoryService.findPropertiesByBcId(anyLong())).thenReturn(r);
        when(requirementDao.findById(anyLong())).thenReturn(new Requirement());
        response = moduleServiceImpl.createModule(module);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"module.derive.exist");

        module = createModule();
        module.getModule().setType(3);
        modules = Lists.newArrayList();
        when(moduleDao.findDerive(anyString(),anyInt())).thenReturn(modules);
        r = new Response<List<BackendCategoryProperty>>();
        list = Lists.newArrayList();
        b = createBackendCategoryProperty();
        b.setValue1("1");
        list.add(b);
        r.setResult(list);
        when(backendCategoryService.findPropertiesByBcId(anyLong())).thenReturn(r);
        when(requirementDao.findById(anyLong())).thenReturn(new Requirement());
        countManager.createMEvent(anyLong(), any(Module.class));
        when(moduleDao.create(any(Module.class))).thenReturn(null);
        response = moduleServiceImpl.createModule(module);
        assertTrue(response.isSuccess());
    }

    @Test
    public void update() {
        ModuleInfoDto module = createModule();
        Response<Boolean> response = moduleServiceImpl.update(module);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"module.id.null");

        module = createModule();
        when(requirementDao.findById(anyLong())).thenReturn(createRequirement());
        response = moduleServiceImpl.update(module);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"requirement.lock.existed");

        module = createModule();
        when(requirementDao.findById(anyLong())).thenReturn(new Requirement());
        when(moduleDao.findById(anyLong())).thenReturn(new Module());
        countManager.updateMEvent(anyLong(), any(Module.class), any(Module.class));
        when(moduleDao.update(any(Module.class))).thenReturn(true);
        response = moduleServiceImpl.update(module);
        assertTrue(response.isSuccess());

        module = createModule();
        when(requirementDao.findById(anyLong())).thenReturn(new Requirement());
        when(moduleDao.findById(anyLong())).thenReturn(new Module());
        countManager.updateMEvent(anyLong(), any(Module.class), any(Module.class));
        when(moduleDao.update(any(Module.class))).thenThrow(Exception.class);
        response = moduleServiceImpl.update(module);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"module.update.failed");
    }

    @Test
    public void delete() {
        Response<Boolean> response = moduleServiceImpl.delete(null);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"module.id.null");

        Module module = createModuleInfo();
        when(moduleDao.findById(anyLong())).thenReturn(module);
        when(requirementDao.findById(anyLong())).thenReturn(createRequirement());
        response = moduleServiceImpl.delete(1L);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"requirement.lock.existed");

        module = createModuleInfo();
        when(requirementDao.findById(anyLong())).thenReturn(new Requirement());
        when(moduleDao.findById(anyLong())).thenReturn(module);
        countManager.updateMEvent(anyLong(),any(Module.class),any(Module.class));
        when(moduleDao.delete(anyLong())).thenReturn(true);
        response = moduleServiceImpl.delete(1L);
        assertTrue(response.isSuccess());

        module = createModuleInfo();
        when(requirementDao.findById(anyLong())).thenReturn(new Requirement());
        when(moduleDao.findById(anyLong())).thenReturn(module);
        countManager.updateMEvent(anyLong(), any(Module.class), any(Module.class));
        when(moduleDao.delete(anyLong())).thenThrow(Exception.class);
        response = moduleServiceImpl.delete(1L);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"module.delete.failed");
    }

    @Test
    public void findAllIdentify() {
        when(identifyNameDao.findAllName()).thenReturn(Lists.<IdentifyName>newArrayList());
        Response<List<IdentifyName>> response = moduleServiceImpl.findAllIdentify();
        assertTrue(response.isSuccess());

        when(identifyNameDao.findAllName()).thenThrow(Exception.class);
        response = moduleServiceImpl.findAllIdentify();
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "find.identify.failed");
    }

    @Test
    public void findByParams() {
        Response<ModulesDto> response = moduleServiceImpl.findByParams(null,null,null);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"requirement.id.null");

        when(requirementDao.findById(anyLong())).thenReturn(createRequirement());
        when(moduleDao.findByParams(anyLong(),anyMap())).thenReturn(new Paging<Module>(1L,Lists.newArrayList(createModuleInfo())));
        response = moduleServiceImpl.findByParams(1L,null,null);
        assertTrue(response.isSuccess());

        when(requirementDao.findById(anyLong())).thenReturn(createRequirement());
        when(moduleDao.findByParams(anyLong(),anyMap())).thenThrow(Exception.class);
        response = moduleServiceImpl.findByParams(1L,null,null);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"module.find.failed");
    }

    @Test
    public void findByRequirementId() {
        Response<Paging<Module>> response = moduleServiceImpl.findByRequirementId(null,null,null,null);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"requirement.id.null");

        response = moduleServiceImpl.findByRequirementId(1L,null,null,null);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"requirement.creatorId.null");

        when(requirementDao.findById(anyLong())).thenReturn(createRequirement());
        when(moduleDao.findByParams(anyLong(),anyMap())).thenReturn(new Paging<Module>(1L,Lists.newArrayList(createModuleInfo())));
        response = moduleServiceImpl.findByRequirementId(1L,2L,null,null);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"requirement.creatorId.noPower");

        when(requirementDao.findById(anyLong())).thenReturn(createRequirement());
        when(moduleDao.findByParams(anyLong(),anyMap())).thenReturn(new Paging<Module>(1L,Lists.newArrayList(createModuleInfo())));
        response = moduleServiceImpl.findByRequirementId(1L,1L,null,null);
        assertTrue(response.isSuccess());

        when(requirementDao.findById(anyLong())).thenReturn(createRequirement());
        when(moduleDao.findByParams(anyLong(),anyMap())).thenThrow(Exception.class);
        response = moduleServiceImpl.findByRequirementId(1L,1L,null,null);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"module.find.failed");
        
    }

    @Test
    public void setModuleNum() {
        Boolean b = moduleServiceImpl.setModuleNum();
        assertTrue(b);
    }

    @Test
    public void pagingByPurchaserId() {
        when(moduleDao.findByParams(anyLong(),anyMap())).thenReturn(new Paging<Module>(1L,Lists.newArrayList(createModuleInfo())));
        when(moduleDao.countByPurchaser(anyLong(),anyMap())).thenReturn(1L);
        when(requirementDao.findByParams(anyLong(),anyMap())).thenReturn(new Paging<Requirement>(1L,Lists.newArrayList(createRequirement())));
        Response<Paging<ModulesDto>> response = moduleServiceImpl.pagingByPurchaserId(0L,1,20);
        assertTrue(response.isSuccess());
    }

    private DerivativeDto createDerive(){
        DerivativeDto derivativeDto = new DerivativeDto();

        Module module = new Module();
        module.setId(1L);
        module.setRequirementId(1l);
        module.setType(1);
        module.setModuleName("spu");
        module.setTotal(3000);
        module.setQuality(100);
        module.setCost(98);
        module.setDelivery(100);
        module.setAttestations("attestation");
        module.setSupplyAt(DateTime.now().toDate());
        module.setOldModuleId(1L);
        module.setSelectNum(1);
        module.setSeriesId(1L);
        module.setModuleNum("1");
        derivativeDto.setModule(module);

        OldModule oldModule = new OldModule();
        oldModule.setModuleId(1l);
        oldModule.setTotal(20000);
        oldModule.setModuleName("test1");
        oldModule.setCost(100);
        oldModule.setModuleNum("A0001");
        oldModule.setPurchaserId(0l);
        oldModule.setPurchaserName("海尔");
        oldModule.setRequirementId(1l);
        derivativeDto.setOldModule(oldModule);


        return derivativeDto;
    }

    private ModuleInfoDto createModule(){
        ModuleInfoDto moduleInfoDto = new ModuleInfoDto();

        Module module = new Module();
        module.setId(1L);
        module.setRequirementId(1l);
        module.setType(1);
        module.setModuleName("spu");
        module.setTotal(3000);
        module.setQuality(100);
        module.setCost(98);
        module.setDelivery(100);
        module.setAttestations("[{id:10,name:'EU认证'}, {id:10,name:'EU认证'}]");
        module.setSupplyAt(DateTime.now().toDate());
        module.setOldModuleId(1L);
        module.setSelectNum(1);
        module.setSeriesId(1L);
        module.setModuleNum("1");

        moduleInfoDto.setModule(module);
        moduleInfoDto.setFactoryList(Lists.newArrayList(mock() , mock(), mock()));
        return moduleInfoDto;
    }

    private Module createModuleInfo(){
        Module module = new Module();
        module.setId(1L);
        module.setRequirementId(1l);
        module.setType(1);
        module.setModuleName("spu");
        module.setTotal(3000);
        module.setQuality(100);
        module.setCost(98);
        module.setDelivery(100);
        module.setAttestations("[{id:10,name:'EU认证'}, {id:10,name:'EU认证'}]");
        module.setSupplyAt(DateTime.now().toDate());
        module.setOldModuleId(1L);
        module.setSelectNum(1);
        module.setSeriesId(1L);
        module.setModuleNum("1");

        return module;
    }

    private BackendCategoryProperty createBackendCategoryProperty(){
        BackendCategoryProperty bcp = new BackendCategoryProperty();
        bcp.setName("name");
        bcp.setBcId(1L);
        bcp.setValue1("3001");
        bcp.setValue2("2");
        bcp.setValue3("3");
        return bcp;
    }

    private ModuleFactory mock(){
        ModuleFactory moduleFactory = new ModuleFactory();
        moduleFactory.setId(1l);
        moduleFactory.setFactoryName("factoryName");
        moduleFactory.setFactoryNum("9889");
        moduleFactory.setModuleId(1l);
        moduleFactory.setPropertyId(1l);
        moduleFactory.setResourceNum(1000);
        moduleFactory.setSalesId(1);
        moduleFactory.setSalesName("saleName");
        moduleFactory.setSelectNum(3);

        return moduleFactory;
    }

    private Requirement createRequirement(){
        Requirement requirement = new Requirement();
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
        requirement.setStatus(3);
        return requirement;
    }

    @Test
    public void queryOldModule() {
        Response<DerivativeDto> response = moduleServiceImpl.queryOldModule(null);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"oldmodule.find.failed");

        response = moduleServiceImpl.queryOldModule("A01");
        assertTrue(response.isSuccess());
    }

    @Test
    public void createDeriveModule(){
        DerivativeDto derivativeDto = createDerive();
        Response<Boolean> result = moduleServiceImpl.createDeriveModule(derivativeDto);
        assertTrue(result.isSuccess());

        derivativeDto.getModule().setRequirementId(null);
        result = moduleServiceImpl.createDeriveModule(derivativeDto);
        assertFalse(result.isSuccess());
        assertEquals(result.getError(),"requirement.id.null");

        derivativeDto.getModule().setModuleName(null);
        derivativeDto.getModule().setRequirementId(1l);
        result = moduleServiceImpl.createDeriveModule(derivativeDto);
        assertFalse(result.isSuccess());
        assertEquals(result.getError(),"module.moduleName.null");

        derivativeDto.getOldModule().setTotal(null);
        derivativeDto.getModule().setModuleName("test1");
        result = moduleServiceImpl.createDeriveModule(derivativeDto);
        assertFalse(result.isSuccess());
        assertEquals(result.getError(),"module.total.null");
    }

}
