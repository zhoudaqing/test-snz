package io.terminus.snz.requirement.service;

import com.google.common.collect.Lists;
import io.terminus.common.model.Paging;
import io.terminus.common.utils.JsonMapper;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.dao.DerivativeDiffDao;
import io.terminus.snz.requirement.dao.ModuleDao;
import io.terminus.snz.requirement.dao.ModuleQuotaDao;
import io.terminus.snz.requirement.dao.OldModuleDao;
import io.terminus.snz.requirement.dto.DerivativeDto;
import io.terminus.snz.requirement.manager.DerivativeDiffManager;
import io.terminus.snz.requirement.model.*;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Desc:
 * Mail: houly@terminus.io
 * author: Hou Luyao
 * Date: 14-8-3.
 */
public class DerivativeServiceTest {
    private static final JsonMapper JSON_MAPPER = JsonMapper.JSON_NON_DEFAULT_MAPPER;

    @Mock
    private DerivativeDiffDao derivativeDiffDao;

    @Mock
    private OldModuleDao oldModuleDao;

    @Mock
    private ModuleDao moduleDao;

    @Mock
    private ModuleQuotaDao moduleQuotaDao;

    @Mock
    private DerivativeDiffManager derivativeDiffManager;

    @InjectMocks
    private DerivativeServiceImpl derivativeServiceImpl;

    @Before
    public void init(){
        // NOTE: necessary
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByRequirementId(){
        Response<Paging<DerivativeDto>> response = derivativeServiceImpl.findByRequirementId(null, null, null);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"derivative.requirementId.null");

        Paging<Module> moduleList = new Paging<Module>(0L, Lists.<Module>newArrayList());
        when(moduleDao.findByParams(anyLong(),anyMap())).thenReturn(moduleList);
        response = derivativeServiceImpl.findByRequirementId(1L, 1, 20);
        assertTrue(response.isSuccess());

        when(moduleDao.findByParams(anyLong(),anyMap())).thenReturn(createModuleList());
        when(oldModuleDao.findByIds(anyList())).thenReturn(createOldModuleList());
        when(moduleQuotaDao.findByIds(any(String[].class))).thenReturn(createModuleQuotaList());
        response = derivativeServiceImpl.findByRequirementId(1L, 1, 20);
        assertTrue(response.isSuccess());

        when(moduleQuotaDao.findByIds(any(String[].class))).thenThrow(Exception.class);
        response = derivativeServiceImpl.findByRequirementId(1L, 1, 20);
        assertFalse(response.isSuccess());

    }

    @Test
    public void testCreate(){
        DerivativeDiff derivativeDiff = createDerivativeDiff();
        derivativeDiff.setRequirementId(null);
        Response<Boolean> response = derivativeServiceImpl.create(derivativeDiff);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"derivativeDiff.requirementId.null");

        derivativeDiff = createDerivativeDiff();
        derivativeDiff.setModuleId(null);
        response = derivativeServiceImpl.create(derivativeDiff);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"derivativeDiff.moduleId.null");

        when(derivativeDiffDao.create(any(DerivativeDiff.class))).thenReturn(null);
        derivativeDiff = createDerivativeDiff();
        response = derivativeServiceImpl.create(derivativeDiff);
        assertTrue(response.isSuccess());

        when(derivativeDiffDao.create(any(DerivativeDiff.class))).thenThrow(Exception.class);
        derivativeDiff = createDerivativeDiff();
        response = derivativeServiceImpl.create(derivativeDiff);
        assertFalse(response.isSuccess());

    }

    @Test
    public void testUpdate(){
        when(derivativeDiffDao.update(any(DerivativeDiff.class))).thenReturn(true);
        Response<Boolean> response = derivativeServiceImpl.update(createDerivativeDiff());
        assertTrue(response.isSuccess());

        when(derivativeDiffDao.update(any(DerivativeDiff.class))).thenThrow(Exception.class);
        response = derivativeServiceImpl.update(createDerivativeDiff());
        assertFalse(response.isSuccess());

    }

    @Test
    public void testFindDiffByRequirementId(){
        Response<Paging<DerivativeDiff>> response = derivativeServiceImpl.findDiffByRequirementId(null, null, null);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"derivativeDiff.requirementId.null");

        Paging<OldModule> moduleList = new Paging<OldModule>(0L, Lists.<OldModule>newArrayList());
        when(oldModuleDao.findByRequirementId(anyLong(), anyMap())).thenReturn(moduleList);
        response = derivativeServiceImpl.findDiffByRequirementId(1L, 1, 20);
        assertTrue(response.isSuccess());

        when(oldModuleDao.findByRequirementId(anyLong(), anyMap())).thenReturn(new Paging<OldModule>(1L,createOldModuleList()));
        when(derivativeDiffDao.findByRequirementId(anyLong())).thenReturn(Lists.newArrayList(createDerivativeDiff()));
        response = derivativeServiceImpl.findDiffByRequirementId(1L, 1, 20);
        assertTrue(response.isSuccess());

        when(derivativeDiffDao.findByRequirementId(anyLong())).thenThrow(Exception.class);
        response = derivativeServiceImpl.findDiffByRequirementId(1L, 1, 20);
        assertFalse(response.isSuccess());
    }

    @Test
    public void testFindByModuleNum(){
        Response<OldModule> response = derivativeServiceImpl.findByModuleNum(null);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"oldModule.moduleNum.null");

        when(oldModuleDao.findByModuleNum(anyString())).thenReturn(createOldModule());
        response = derivativeServiceImpl.findByModuleNum("1");
        assertTrue(response.isSuccess());

        when(oldModuleDao.findByModuleNum(anyString())).thenThrow(Exception.class);
        response = derivativeServiceImpl.findByModuleNum("1");
        assertFalse(response.isSuccess());

    }

    @Test
    public void testBatchDerivative(){
        Response<Boolean> response = derivativeServiceImpl.batchDerivative(null);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"derivativeDiff.bash.null");

        when(derivativeDiffManager.batchDerivativeDiffs(anyList())).thenReturn(true);
        response = derivativeServiceImpl.batchDerivative("[]");
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"derivativeDiff.bash.empty");

        when(derivativeDiffManager.batchDerivativeDiffs(anyList())).thenReturn(true);
        response = derivativeServiceImpl.batchDerivative(mockString());
        assertTrue(response.isSuccess());

        when(derivativeDiffManager.batchDerivativeDiffs(anyList())).thenThrow(Exception.class);
        response = derivativeServiceImpl.batchDerivative(mockString());
        assertFalse(response.isSuccess());
    }

    private Paging<Module> createModuleList(){
        Paging<Module> modulePaging = new Paging<Module>(1L,Lists.newArrayList(createModule()));
        return modulePaging;
    }

    private Module createModule(){
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

    private OldModule createOldModule(){
        OldModule oldModule = new OldModule();
        oldModule.setRequirementId(3L);
        oldModule.setModuleNum("wuliaohao");
        oldModule.setPrioritySelect(0);
        oldModule.setModuleName("wuliaomiaoshu");
        oldModule.setCost(10);
        oldModule.setTotal(100);
        oldModule.setSeriesId(1l);
        oldModule.setSeriesName("seriesname");
        oldModule.setSupplyAt(new Date());
        oldModule.setResourceNum("[{\"faId\":42,\"faNum\":8640,\"faName\":\"重庆零部件家用空调PL工厂\",\"num\":\"1000\"}]");
        oldModule.setQuotaIds("1,2");
        return oldModule;
    }

    private List<OldModule> createOldModuleList(){
        return Lists.newArrayList(createOldModule());
    }

    private List<ModuleQuota> createModuleQuotaList(){
        return Lists.newArrayList(createModuleQuota());
    }

    private ModuleQuota createModuleQuota(){
        ModuleQuota moduleQuota = new ModuleQuota();
        return moduleQuota;
    }

    private DerivativeDiff createDerivativeDiff(){
        DerivativeDiff derivativeDiff = new DerivativeDiff();
        derivativeDiff.setId(1L);
        derivativeDiff.setModuleName("modulename");
        derivativeDiff.setModuleId(1L);
        derivativeDiff.setRequirementId(1L);
        derivativeDiff.setBomModule(1);
        derivativeDiff.setCreatedAt(new Date());
        derivativeDiff.setDrive(1);
        derivativeDiff.setHostChange(1);
        derivativeDiff.setMaterial(1);
        derivativeDiff.setOverseasParts(1);
        derivativeDiff.setMatrix(1);
        derivativeDiff.setPrinting(1);
        derivativeDiff.setSoftwareParam(1);
        derivativeDiff.setSurfaceTreatment(1);
        derivativeDiff.setUpdatedAt(new Date());
        derivativeDiff.setStructure(1);
        createRequirement();
        return derivativeDiff;
    }

    private String mockString(){
        List<DerivativeDiff> derivativeDiffs = new ArrayList<DerivativeDiff>();

        DerivativeDiff derivativeDiff = new DerivativeDiff();
        derivativeDiff.setId(1L);
        derivativeDiff.setModuleName("modulename");
        derivativeDiff.setModuleId(1L);
        derivativeDiff.setRequirementId(1L);
        derivativeDiff.setBomModule(1);
        derivativeDiff.setCreatedAt(new Date());
        derivativeDiff.setDrive(1);
        derivativeDiff.setHostChange(1);
        derivativeDiff.setMaterial(1);
        derivativeDiff.setOverseasParts(1);
        derivativeDiff.setMatrix(1);
        derivativeDiff.setPrinting(1);
        derivativeDiff.setSoftwareParam(1);
        derivativeDiff.setSurfaceTreatment(1);
        derivativeDiff.setUpdatedAt(new Date());
        derivativeDiff.setStructure(1);

        DerivativeDiff derivativeDiff1 = new DerivativeDiff();
        derivativeDiff1.setId(null);
        derivativeDiff1.setModuleName("modulename");
        derivativeDiff1.setModuleId(1L);
        derivativeDiff1.setRequirementId(1L);
        derivativeDiff1.setBomModule(1);
        derivativeDiff1.setCreatedAt(new Date());
        derivativeDiff1.setDrive(1);
        derivativeDiff1.setHostChange(1);
        derivativeDiff1.setMaterial(1);
        derivativeDiff1.setOverseasParts(1);
        derivativeDiff1.setMatrix(1);
        derivativeDiff1.setPrinting(1);
        derivativeDiff1.setSoftwareParam(1);
        derivativeDiff1.setSurfaceTreatment(1);
        derivativeDiff1.setUpdatedAt(new Date());
        derivativeDiff1.setStructure(1);

        derivativeDiffs.add(derivativeDiff);
        derivativeDiffs.add(derivativeDiff1);

        return JSON_MAPPER.toJson(derivativeDiffs);
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
        return requirement;
    }
}
