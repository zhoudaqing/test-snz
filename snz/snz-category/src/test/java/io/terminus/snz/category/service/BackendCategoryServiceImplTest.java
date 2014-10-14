package io.terminus.snz.category.service;

import com.google.common.collect.Lists;
import io.terminus.pampas.common.Response;
import io.terminus.snz.category.dao.BackendCategoryPropertyDao;
import io.terminus.snz.category.manager.BackendCategoryManager;
import io.terminus.snz.category.manager.FrontendCategoryManager;
import io.terminus.snz.category.model.BackendCategory;
import io.terminus.snz.category.model.BackendCategoryProperty;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 8/3/14
 */
public class BackendCategoryServiceImplTest extends BaseServiceTest {

    @InjectMocks
    private BackendCategoryServiceImpl backendCategoryService;

    @Mock
    private BackendCategoryManager backendCategoryManager;

    @Mock
    private FrontendCategoryManager frontendCategoryManager;

    @Mock
    private BackendCategoryPropertyDao backendCategoryPropertyDao;

    private BackendCategory getBc(String name, Integer level) {
        BackendCategory bc = new BackendCategory();
        bc.setName(name);
        bc.setLevel(level);
        return bc;
    }

    private BackendCategoryProperty getBcp(String name, Long bcId, String value1, String value2, String value3) {
        BackendCategoryProperty bcp = new BackendCategoryProperty();
        bcp.setName(name);
        bcp.setBcId(bcId);
        bcp.setValue1(value1);
        bcp.setValue2(value2);
        bcp.setValue3(value3);
        return bcp;
    }

    @Test
    public void testCUD() throws Exception {
        BackendCategory bc = getBc("后台类目1", 1);
        BackendCategoryProperty bcp = getBcp("把手资源量", 1L, "1000", "2000", "3000");

        // create
        when(backendCategoryManager.create(bc)).thenReturn(1L);
        Response<Long> backCreateRes = backendCategoryService.create(bc);
        assertTrue(backCreateRes.isSuccess());
        assertThat(backCreateRes.getResult(), is(1L));
        // 错误参数测试
        backCreateRes = backendCategoryService.create(null);
        assertFalse(backCreateRes.isSuccess());

        when(backendCategoryPropertyDao.create(bcp)).thenReturn(1L);
        Response<Long> backProCreateRes = backendCategoryService.createProperty(bcp);
        assertTrue(backProCreateRes.isSuccess());
        assertThat(backProCreateRes.getResult(), is(1L));
        // 错误参数测试
        assertFalse(backendCategoryService.createProperty(null).isSuccess());

        // update
        when(backendCategoryManager.update(bc)).thenReturn(true);
        Response<Boolean> backUpdateRes = backendCategoryService.update(bc);
        assertTrue(backUpdateRes.isSuccess());
        assertThat(backUpdateRes.getResult(), is(true));
        // 错误参数：
        assertFalse(backendCategoryService.update(null).isSuccess());

        when(backendCategoryPropertyDao.update(any(BackendCategoryProperty.class))).thenReturn(true);
        Response<Boolean> backProUpdateRes = backendCategoryService.updateProperty(1L, bcp);
        assertTrue(backProUpdateRes.isSuccess());
        assertThat(backProUpdateRes.getResult(), is(true));
        // 错误参数
        assertFalse(backendCategoryService.updateProperty(1L, null).isSuccess());
        assertFalse(backendCategoryService.updateProperty(null, bcp).isSuccess());
        assertFalse(backendCategoryService.updateProperty(null, null).isSuccess());

        // delete
        when(backendCategoryManager.delete(1L)).thenReturn(true);
        Response<Boolean> backDeleteRes = backendCategoryService.delete(1L);
        assertTrue(backDeleteRes.isSuccess());
        assertThat(backDeleteRes.getResult(), is(true));
        // 错误参数
        assertFalse(backendCategoryService.delete(null).isSuccess());

        when(backendCategoryPropertyDao.delete(1L)).thenReturn(true);
        Response<Boolean> backProDeleteRes = backendCategoryService.deleteProperty(1L);
        assertTrue(backProDeleteRes.isSuccess());
        assertThat(backProDeleteRes.getResult(), is(true));

        // 错误参数
        assertFalse(backendCategoryService.deleteProperty(null).isSuccess());
    }

    @Test
    public void testForFind() throws Exception {
        BackendCategory bc = getBc("后台类目", 1);
        BackendCategoryProperty bcp = getBcp("资源量", 1L, "1000", "2000", "3000");

        /*
         后台类目findById
          */
        when(backendCategoryManager.findById(1L)).thenReturn(bc);
        Response<BackendCategory> backRes = backendCategoryService.findById(1L);
        assertTrue(backRes.isSuccess());
        assertThat(backRes.getResult().getLevel(), is(1));
        // 错误参数
        assertFalse(backendCategoryService.findById(null).isSuccess());

        /*
         后台类目批量查找
          */
        when(backendCategoryManager.findByIds(anyList())).thenReturn(Lists.newArrayList(bc));
        Response<List<BackendCategory>> backIdsRes = backendCategoryService.findByIds(Lists.newArrayList(1L));
        assertTrue(backIdsRes.isSuccess());
        assertThat(backIdsRes.getResult().size(), is(1));
        // 错误参数
        assertFalse(backendCategoryService.findByIds(null).isSuccess());

        /*
         后台类目通过level查询
          */
        when(backendCategoryManager.findByLevels(1)).thenReturn(Lists.newArrayList(bc));
        Response<List<BackendCategory>> bcsRes = backendCategoryService.findByLevels(1);
        assertTrue(bcsRes.isSuccess());
        assertThat(bcsRes.getResult().size(), is(1));
        // 查不到
        bcsRes = backendCategoryService.findByLevels(2);
        assertTrue(bcsRes.isSuccess());
        assertThat(bcsRes.getResult().size(), is(0));
        // 错误参数
        assertFalse(backendCategoryService.findByLevels(null).isSuccess());

        /*
        查找叶子节点
         */
        when(backendCategoryManager.findChildrenOf(1L)).thenReturn(Lists.newArrayList(bc));
        bcsRes = backendCategoryService.findChildrenOf(1L);
        assertTrue(bcsRes.isSuccess());
        assertThat(bcsRes.getResult().size(), is(1));
        // 查不到
        bcsRes = backendCategoryService.findChildrenOf(0L);
        assertTrue(bcsRes.isSuccess());
        assertThat(bcsRes.getResult().size(), is(0));
        // 参数错误
        assertFalse(backendCategoryService.findChildrenOf(null).isSuccess());

        /*
        根据后台类目属性id查
         */
        when(backendCategoryPropertyDao.findById(1L)).thenReturn(bcp);
        Response<BackendCategoryProperty> backProRes = backendCategoryService.findPropertyById(1L);
        assertTrue(backProRes.isSuccess());
        assertThat(backProRes.getResult().getValue1(), is("1000"));
        // 查不到
        // assertFalse(backendCategoryService.findPropertyById(0L).isSuccess()); // TODO: 需要改成查不到抛错
        backProRes = backendCategoryService.findPropertyById(0L);
        assertTrue(backProRes.isSuccess());
        assertNull(backProRes.getResult().getValue1());

        /*
        通过后台类目id查属性
         */
        when(backendCategoryPropertyDao.findByBcId(1L)).thenReturn(Lists.newArrayList(bcp));
        Response<List<BackendCategoryProperty>> bcpRes = backendCategoryService.findPropertiesByBcId(1L);
        assertTrue(bcpRes.isSuccess());
        assertThat(bcpRes.getResult().size(), is(1));
        // 查不到
        bcpRes = backendCategoryService.findPropertiesByBcId(0L);
        assertTrue(bcpRes.isSuccess());
        assertThat(bcpRes.getResult().size(), is(0));
        // 参数错误
        assertFalse(backendCategoryService.findPropertyById(null).isSuccess());

        /*
        类目属性批量查
         */
        when(backendCategoryPropertyDao.findByBcIds(Lists.newArrayList(1L))).thenReturn(Lists.newArrayList(bcp));
        bcpRes = backendCategoryService.findPropertiesByBcIds(Lists.newArrayList(1L));
        assertTrue(bcpRes.isSuccess());
        assertThat(bcpRes.getResult().size(), is(1));
        // 查不到
        bcpRes = backendCategoryService.findPropertiesByBcIds(Lists.newArrayList(0L));
        assertTrue(bcpRes.isSuccess());
        assertThat(bcpRes.getResult().size(), is(0));
        // 参数错误
        bcpRes = backendCategoryService.findPropertiesByBcIds(null);
        assertFalse(bcpRes.isSuccess());
    }

    @Test
    public void testFindPropertiesByBcIdWithFactoryNums() {
        BackendCategoryProperty property = new BackendCategoryProperty();
        property.setBcId(1L);
        property.setFactoryNum("工厂1");
        BackendCategoryProperty property2 = new BackendCategoryProperty();
        property2.setBcId(1L);
        property2.setFactoryNum("工厂2");
        when(backendCategoryPropertyDao.findByBcIdWithFactoryNums(eq(1L), anyList())).thenReturn(Lists.newArrayList(property, property2));
        Response<List<BackendCategoryProperty>> backProResp = backendCategoryService.findPropertiesByBcIdWithFactoryNums(1L, "工厂1,工厂2");
        assertTrue(backProResp.isSuccess());
        assertThat(backProResp.getResult().size(), is(2));

        // 异常
        when(backendCategoryPropertyDao.findByBcIdWithFactoryNums(eq(2L), anyList())).thenThrow(new RuntimeException());
        backProResp = backendCategoryService.findPropertiesByBcIdWithFactoryNums(2L, "工厂1");
        assertFalse(backProResp.isSuccess());

        // 为空
        when(backendCategoryPropertyDao.findByBcIdWithFactoryNums(eq(3L), anyList())).thenReturn(null);
        backProResp = backendCategoryService.findPropertiesByBcIdWithFactoryNums(3L, "工厂2");
        assertTrue(backProResp.isSuccess());
        assertTrue(backProResp.getResult().isEmpty());

        // bcId 为空
        backProResp = backendCategoryService.findPropertiesByBcIdWithFactoryNums(null, "工厂1,工厂2");
        assertFalse(backProResp.isSuccess());
        assertThat(backProResp.getError(), is("category.property.bcid.null"));
    }
}