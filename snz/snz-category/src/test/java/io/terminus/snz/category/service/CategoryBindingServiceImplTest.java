package io.terminus.snz.category.service;

import com.google.common.collect.Lists;
import io.terminus.common.utils.JsonMapper;
import io.terminus.pampas.common.Response;
import io.terminus.snz.category.dao.CategoryBindingDao;
import io.terminus.snz.category.dto.CategoryPair;
import io.terminus.snz.category.dto.CategoryPairWithBcName;
import io.terminus.snz.category.model.CategoryBinding;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 8/4/14
 */
public class CategoryBindingServiceImplTest extends BaseServiceTest {

    @InjectMocks
    private CategoryBindingServiceImpl categoryBindingService;

    @Mock
    private BackendCategoryService backendCategoryService;

    @Mock
    private CategoryBindingDao categoryBindingDao;

    private CategoryBinding getCb(Long id, Long fcId, String bcs) {
        CategoryBinding cb = new CategoryBinding();
        cb.setId(id);
        cb.setFrontId(fcId);
        cb.setBcs(bcs);
        return cb;
    }

    @Test
    public void testCRUD() throws Exception {
        CategoryPair cp = new CategoryPair();
        cp.setId(1L);
        cp.setPath("path");

        CategoryBinding cb = getCb(1L, 1L, JsonMapper.nonDefaultMapper().toJson(Lists.newArrayList(cp)));

        /*
        create
         */
        when(categoryBindingDao.create(cb)).thenReturn(1L);
        Response<Long> createRes = categoryBindingService.create(cb);
        assertTrue(createRes.isSuccess());
        assertThat(createRes.getResult(), is(1L));
        // 参数错误
        assertFalse(categoryBindingService.create(null).isSuccess());
        // dao 异常
        when(categoryBindingDao.create(any(CategoryBinding.class))).thenThrow(new RuntimeException());
        assertFalse(categoryBindingService.create(cb).isSuccess());

        /*
        update
         */
        when(categoryBindingDao.update(cb)).thenReturn(true);
        Response<Boolean> updateRes = categoryBindingService.update(cb);
        assertTrue(updateRes.isSuccess());
        assertThat(updateRes.getResult(), is(true));
        // 参数错误
        assertFalse(categoryBindingService.update(new CategoryBinding()).isSuccess());
        // dao 异常
        when(categoryBindingDao.update(any(CategoryBinding.class))).thenThrow(new RuntimeException());
        assertFalse(categoryBindingService.update(cb).isSuccess());

        /*
        delete
         */
        when(categoryBindingDao.delete(1L)).thenReturn(true);
        Response<Boolean> deleteRes = categoryBindingService.delete(1L);
        assertTrue(deleteRes.isSuccess());
        assertThat(deleteRes.getResult(), is(true));
        // 参数错误
        assertFalse(categoryBindingService.delete(null).isSuccess());
        // dao 异常
        when(categoryBindingDao.delete(1L)).thenThrow(new RuntimeException());
        assertFalse(categoryBindingService.delete(1L).isSuccess());

        /*
        findByFcId
         */
        when(categoryBindingDao.findByFcid(1L)).thenReturn(Lists.newArrayList(cp));
        Response<List<CategoryPair>> cpsRes = categoryBindingService.findByFcid(1L);
        assertTrue(cpsRes.isSuccess());
        assertThat(cpsRes.getResult().size(), is(1));
        // 查不到
        cpsRes = categoryBindingService.findByFcid(0L);
        assertTrue(cpsRes.isSuccess());
        assertThat(cpsRes.getResult().size(), is(0));
        // 参数错误
        assertFalse(categoryBindingService.findByFcid(null).isSuccess());

        /*
        findBcNamesByFcid
         */
        Response<List<CategoryPairWithBcName>> cpbcsRes = categoryBindingService.findBcNamesByFcid(1L);
//        assertTrue(cpbcsRes.isSuccess());
        // TODO: to be completed

        /*
        findBy
         */
        when(categoryBindingDao.findBy(1L)).thenReturn(cb);
        Response<CategoryBinding> cbRes = categoryBindingService.findBy(1L);
        assertTrue(cbRes.isSuccess());
        assertThat(cbRes.getResult().getBcs(), is(cb.getBcs()));
        // 错误参数
        assertFalse(categoryBindingService.findBy(null).isSuccess());
        // dao 异常
        when(categoryBindingDao.findBy(1L)).thenThrow(new RuntimeException());
        assertFalse(categoryBindingService.findBy(1L).isSuccess());

        /*
        findById
         */
        when(categoryBindingDao.findById(1L)).thenReturn(cb);
        cbRes = categoryBindingService.findById(1L);
        assertTrue(cbRes.isSuccess());
        assertThat(cbRes.getResult().getBcs(), is(cb.getBcs()));
        // 错误参数
        assertFalse(categoryBindingService.findById(null).isSuccess());
        // dao 异常
        when(categoryBindingDao.findById(anyLong())).thenThrow(new RuntimeException());
        assertFalse(categoryBindingService.findBy(1L).isSuccess());
    }
}