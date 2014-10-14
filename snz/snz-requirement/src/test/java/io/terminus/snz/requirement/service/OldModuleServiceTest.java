package io.terminus.snz.requirement.service;

import com.google.common.collect.Lists;
import io.terminus.common.model.Paging;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.dao.OldModuleDao;
import io.terminus.snz.requirement.dao.RequirementDao;
import io.terminus.snz.requirement.model.OldModule;
import io.terminus.snz.requirement.model.Requirement;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Desc:
 * Mail: houly@terminus.io
 * author: Hou Luyao
 * Date: 14-8-3.
 */
public class OldModuleServiceTest {

    @Mock
    private OldModuleDao oldModuleDao;

    @Mock
    private RequirementDao requirementDao;

    @InjectMocks
    private OldModuleServiceImpl oldModuleServiceImpl;

    @Before
    public void init(){
        // NOTE: necessary
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByFilters(){
        when(oldModuleDao.findByFilters(anyMap())).thenReturn(createOldModulePaging());
        Response<Paging<OldModule>> response = oldModuleServiceImpl.findByFilters("1", 1, 2, "名称");
        assertTrue(response.isSuccess());
    }
    @Test
    public void testFindByFiltersException(){
        when(oldModuleDao.findByFilters(anyMap())).thenThrow(Exception.class);
        Response<Paging<OldModule>> response = oldModuleServiceImpl.findByFilters("1", 1, 2, "名称");
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"oldModule.find.failed");
    }

    @Test
    public void testFindByModuleNum(){
        when(oldModuleDao.findByModuleNum(anyString())).thenReturn(createOldModule());
        Response<OldModule> response = oldModuleServiceImpl.findByModuleNum("1");
        assertTrue(response.isSuccess());
    }

    @Test
    public void testFindByModuleNumException(){
        when(oldModuleDao.findByModuleNum(anyString())).thenThrow(Exception.class);
        Response<OldModule> response = oldModuleServiceImpl.findByModuleNum("1");
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"oldModule.find.failed");
    }

    @Test
    public void testDelete(){
        when(oldModuleDao.delete(anyLong())).thenReturn(true);
        Response<Boolean> response = oldModuleServiceImpl.delete(1L);
        assertTrue(response.isSuccess());
        assertTrue(response.getResult());
    }

    @Test
    public void testDeleteException(){
        when(oldModuleDao.delete(anyLong())).thenThrow(Exception.class);
        Response<Boolean> response = oldModuleServiceImpl.delete(1L);
        assertFalse(response.isSuccess());
    }

    @Test
    public void testFindById(){
        when(oldModuleDao.findById(anyLong())).thenReturn(createOldModule());
        Response<OldModule> response = oldModuleServiceImpl.findById(1L);
        assertTrue(response.isSuccess());
    }

    @Test
    public void testFindByIdException(){
        when(oldModuleDao.findById(anyLong())).thenThrow(Exception.class);
        Response<OldModule> response = oldModuleServiceImpl.findById(1L);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"oldModule.find.failed");
    }

    @Test
    public void testUpdate(){
        when(oldModuleDao.update(any(OldModule.class))).thenReturn(true);
        Response<Boolean> response = oldModuleServiceImpl.update(createOldModule());
        assertTrue(response.isSuccess());
        assertTrue(response.getResult());

        response = oldModuleServiceImpl.update(null);
        assertFalse(response.isSuccess());

        when(oldModuleDao.update(any(OldModule.class))).thenThrow(Exception.class);
        response = oldModuleServiceImpl.update(createOldModule());
        assertFalse(response.isSuccess());
    }

    @Test
    public void testCreate(){
        OldModule oldModule = createOldModule();
        oldModule.setRequirementId(null);
        Response<Boolean> response = oldModuleServiceImpl.create(oldModule);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"oldModule.requirementId.null");

        oldModule = createOldModule();
        oldModule.setModuleNum(null);
        response = oldModuleServiceImpl.create(oldModule);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"oldModule.moduleNum.null");

        oldModule = createOldModule();
        oldModule.setPrioritySelect(null);
        response = oldModuleServiceImpl.create(oldModule);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"oldModule.prioritySelect.null");

        oldModule = createOldModule();
        oldModule.setModuleName(null);
        response = oldModuleServiceImpl.create(oldModule);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"oldModule.moduleName.null");

        oldModule = createOldModule();
        oldModule.setCost(null);
        response = oldModuleServiceImpl.create(oldModule);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"oldModule.cost.null");

        oldModule = createOldModule();
        oldModule.setResourceNum(null);
        response = oldModuleServiceImpl.create(oldModule);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"oldModule.resourceNum.null");

        oldModule = createOldModule();
        oldModule.setTotal(null);
        response = oldModuleServiceImpl.create(oldModule);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"oldModule.total.null");

        oldModule = createOldModule();
        oldModule.setSeriesId(null);
        response = oldModuleServiceImpl.create(oldModule);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"oldModule.seriesId.null");

        oldModule = createOldModule();
        oldModule.setSeriesName(null);
        response = oldModuleServiceImpl.create(oldModule);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"oldModule.seriesName.null");

        oldModule = createOldModule();
        oldModule.setSupplyAt(null);
        response = oldModuleServiceImpl.create(oldModule);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"oldModule.supplyAt.null");

        when(requirementDao.findById(anyLong())).thenReturn(null);
        oldModule = createOldModule();
        response = oldModuleServiceImpl.create(oldModule);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"requirement.not.exist");

        Requirement requirement = createRequirement();
        requirement.setStatus(4);
        when(requirementDao.findById(anyLong())).thenReturn(requirement);
        oldModule = createOldModule();
        response = oldModuleServiceImpl.create(oldModule);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"requirement.lock.existed");

        when(requirementDao.findById(anyLong())).thenReturn(createRequirement());
        when(oldModuleDao.create(any(OldModule.class))).thenReturn(null);
        oldModule = createOldModule();
        response = oldModuleServiceImpl.create(oldModule);
        assertTrue(response.isSuccess());

        when(requirementDao.findById(anyLong())).thenReturn(createRequirement());
        when(oldModuleDao.create(any(OldModule.class))).thenThrow(Exception.class);
        oldModule = createOldModule();
        response = oldModuleServiceImpl.create(oldModule);
        assertFalse(response.isSuccess());

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
        return oldModule;
    }

    private Paging<OldModule> createOldModulePaging(){
        Paging<OldModule> oldModulePaging = new Paging<OldModule>(1L, Lists.newArrayList(createOldModule()));
        return oldModulePaging;
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
