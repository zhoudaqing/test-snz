package io.terminus.snz.category.service;

import com.google.common.collect.Lists;
import io.terminus.common.utils.JsonMapper;
import io.terminus.pampas.common.Response;
import io.terminus.snz.category.dao.CategoryBindingDao;
import io.terminus.snz.category.dto.CategoryPair;
import io.terminus.snz.category.manager.BackendCategoryManager;
import io.terminus.snz.category.manager.FrontendCategoryManager;
import io.terminus.snz.category.model.CategoryBinding;
import io.terminus.snz.category.model.FrontendCategory;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.LinkedHashSet;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 8/3/14
 */
public class FrontendCategoryServiceImplTest extends BaseServiceTest {

    @InjectMocks
    private FrontendCategoryServiceImpl frontendCategoryService;

    @InjectMocks
    private BackendCategoryServiceImpl backendCategoryService;

    @Mock
    private CategoryBindingDao categoryBindingDao;

    @Mock
    private BackendCategoryManager backendCategoryManager;

    @Mock
    private FrontendCategoryManager frontendCategoryManager;

    private FrontendCategory getFc(String name, Integer level, Long parentId) {
        FrontendCategory fc = new FrontendCategory();
        fc.setName(name);
        fc.setLevel(level);
        fc.setParentId(parentId);
        return fc;
    }

    @Test
    public void testFrontendCRUD() throws Exception {
        FrontendCategory fc = getFc("前台类目", 1, 0L);

        CategoryPair cp = new CategoryPair();
        cp.setId(1L);
        cp.setPath("path");

        CategoryBinding cb = new CategoryBinding();
        cb.setFrontId(1L);
        cb.setBcs(JsonMapper.nonDefaultMapper().toJson(Lists.newArrayList(cp)));

        /*
        fc create
         */
        when(frontendCategoryManager.create(fc)).thenReturn(1L);
        Response<Long> fcCreateRes = frontendCategoryService.create(fc);
        assertTrue(fcCreateRes.isSuccess());
        assertThat(fcCreateRes.getResult(), is(1L));

        /*
        fc update
         */
        when(frontendCategoryManager.update(fc)).thenReturn(true);
        Response<Boolean> fcUpdateRes = frontendCategoryService.update(fc);
        assertTrue(fcUpdateRes.isSuccess());
        assertThat(fcUpdateRes.getResult(), is(true));

        /*
        fc delete
         */
        when(frontendCategoryManager.delete(1L)).thenReturn(true);
        Response<Boolean> fcDeleteRes = frontendCategoryService.delete(1L);
        assertTrue(fcDeleteRes.isSuccess());
        assertThat(fcDeleteRes.getResult(), is(true));

        /*
        fc 通过id查询
         */
        when(frontendCategoryManager.findById(1L)).thenReturn(fc);
        Response<FrontendCategory> fcRes = frontendCategoryService.findById(1L);
        assertTrue(fcRes.isSuccess());
        assertThat(fcRes.getResult().getLevel(), is(1));
        // 查不到
//        assertFalse(frontendCategoryService.findById(0L).isSuccess()); // TODO: 要改成这样，查不到报错
        fcRes = frontendCategoryService.findById(0L);
        assertTrue(fcRes.isSuccess());
        assertNull(fcRes.getResult().getLevel());
        // 参数错误
        assertFalse(frontendCategoryService.findById(null).isSuccess());

        /*
        fc 批量id查询
         */
        when(frontendCategoryManager.findByIds(Lists.newArrayList(1L))).thenReturn(Lists.newArrayList(fc));
        Response<List<FrontendCategory>> fcsRes = frontendCategoryService.findByIds(Lists.newArrayList(1L));
        assertTrue(fcsRes.isSuccess());
        assertThat(fcsRes.getResult().size(), is(1));
        // 查不到
        fcsRes = frontendCategoryService.findByIds(Lists.newArrayList(0L));
        assertTrue(fcsRes.isSuccess());
        assertThat(fcsRes.getResult().size(), is(0));
        // 参数错误
        assertFalse(frontendCategoryService.findByIds(null).isSuccess());

        /*
        fc 查询孩子节点列表
         */
        when(frontendCategoryManager.findChildrenOf(1L)).thenReturn(Lists.newArrayList(fc));
        fcsRes = frontendCategoryService.findChildrenOf(1L);
        assertTrue(fcsRes.isSuccess());
        assertThat(fcsRes.getResult().size(), is(1));
        // 查不到
        fcsRes = frontendCategoryService.findChildrenOf(0L);
        assertTrue(fcsRes.isSuccess());
        assertThat(fcsRes.getResult().size(), is(0));
        // 参数错误
        assertFalse(frontendCategoryService.findChildrenOf(null).isSuccess());

        /*
        fc 通过level查询列表
         */
        when(frontendCategoryManager.findByLevels(1)).thenReturn(Lists.newArrayList(fc));
        fcsRes = frontendCategoryService.findByLevels(1);
        assertTrue(fcsRes.isSuccess());
        assertThat(fcsRes.getResult().size(), is(1));
        // 查不到
        fcsRes = frontendCategoryService.findByLevels(0);
        assertTrue(fcsRes.isSuccess());
        assertThat(fcsRes.getResult().size(), is(0));
        // 参数错误
        fcsRes = frontendCategoryService.findByLevels(null);
        assertFalse(fcsRes.isSuccess());

        /*
        fc 通过level和name查询单个
         */
        when(frontendCategoryManager.findByLevelAndName(1, "前台类目")).thenReturn(fc);
        fcRes = frontendCategoryService.findFrontendCategoryByLevelAndName(1, "前台类目");
        assertTrue(fcRes.isSuccess());
        assertThat(fcRes.getResult().getLevel(), is(1));
        // 查不到或参数错误
        assertFalse(frontendCategoryService.findFrontendCategoryByLevelAndName(2, "前台类目").isSuccess());
        assertFalse(frontendCategoryService.findFrontendCategoryByLevelAndName(null, "前台类目").isSuccess());
        assertFalse(frontendCategoryService.findFrontendCategoryByLevelAndName(1, null).isSuccess());
        assertFalse(frontendCategoryService.findFrontendCategoryByLevelAndName(null, null).isSuccess());

        /*
        通过后台类目列表批量查询前台类目
         */
        // TODO: to be completed
        // fcsRes = frontendCategoryService.findFrontendByIds("dummy list");

        /*
        通过前台类目id查询绑定
         */
        when(categoryBindingDao.findByFcid(1L)).thenReturn(Lists.newArrayList(cp));
        Response<List<CategoryPair>> cpsRes = frontendCategoryService.findBindingsOf(1L);
        assertTrue(cpsRes.isSuccess());
        assertThat(cpsRes.getResult().size(), is(1));
        // 查不到
        cpsRes = frontendCategoryService.findBindingsOf(0L);
        assertTrue(cpsRes.isSuccess());
        assertThat(cpsRes.getResult().size(), is(0));
        // 参数错误
        assertFalse(frontendCategoryService.findBindingsOf(null).isSuccess());

        /*
        根据后台类目id寻找绑定的前台类目
         */
        when(categoryBindingDao.findAll()).thenReturn(Lists.newArrayList(cb));
        Response<LinkedHashSet<Long>> idsRes = frontendCategoryService.findFcIdsByBcId(1L);
        assertTrue(idsRes.isSuccess());
        assertThat(idsRes.getResult().size(), is(1));
        // TODO: 完善错误参数

        /*
        同上，批量参数
         */
        when(categoryBindingDao.findAll()).thenReturn(Lists.newArrayList(cb));
        idsRes = frontendCategoryService.findFcIdsByBcIds(Lists.newArrayList(1L));
        assertTrue(idsRes.isSuccess());
        assertThat(idsRes.getResult().size(), is(1));
        // TODO: 完善错误参数

        /*
        绑定后台类目
        TODO: to be completed
         */
        assertTrue(frontendCategoryService.bind(1L, cb.getBcs()).isSuccess());

        /*
        unbind, TODO: to be completed
         */
        assertTrue(frontendCategoryService.unbind(1L).isSuccess());

        /*
        查询祖先节点（路径）TODO: to be completed
         */
        when(frontendCategoryManager.findById(anyLong())).thenReturn(fc);
        assertTrue(frontendCategoryService.ancestorsOf(1L).isSuccess());

        /*
        findAll TODO: to be completed
         */
        assertTrue(frontendCategoryService.findAll().isSuccess());
    }

    @Test
    public void testFindAllTotally() throws Exception{
        when(frontendCategoryManager.findAllLoop(0L)).thenReturn(Lists.newArrayList(new FrontendCategory()));
        Response<List<FrontendCategory>> findAllRes = frontendCategoryService.findAllTotally();
        assertTrue(findAllRes.isSuccess());
        assertThat(findAllRes.getResult().size(), is(1));
    }
}