package io.terminus.snz.category.manager;

import com.google.common.collect.Lists;
import io.terminus.snz.category.dao.FrontendCategoryDao;
import io.terminus.snz.category.model.FrontendCategory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 8/4/14
 */
public class FrontendCategoryManagerTest {

    @InjectMocks
    private FrontendCategoryManager frontendCategoryManager;

    @Mock
    private FrontendCategoryDao frontendCategoryDao;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    private FrontendCategory getFc(Long id, Long parentId, String name, Integer level, boolean hasChildren) {
        FrontendCategory fc = new FrontendCategory();
        fc.setId(id);
        fc.setName(name);
        fc.setParentId(parentId);
        fc.setLevel(level);
        fc.setHasChildren(hasChildren);
        return fc;
    }

    @Test
    public void testCRUD() throws Exception {
        FrontendCategory fc = new FrontendCategory();
        fc.setId(1L);
        fc.setName("前台类目");
        fc.setLevel(1);
        fc.setParentId(0L);

        /*
        create
         */
        when(frontendCategoryDao.create(fc)).thenReturn(1L);
        assertThat(frontendCategoryManager.create(fc), is(1L));

        /*
        update
         */
        when(frontendCategoryDao.update(fc)).thenReturn(true);
        when(frontendCategoryDao.findById(1L)).thenReturn(fc);
        assertThat(frontendCategoryManager.update(fc), is(true));

        /*
        delete
         */
        when(frontendCategoryDao.delete(1L)).thenReturn(true);
//        assertThat(frontendCategoryManager.delete(1L), is(true));
        // TODO: to be completed

        /*
        findById
         */
        when(frontendCategoryDao.findById(1L)).thenReturn(fc);
        assertThat(frontendCategoryManager.findById(1L).getLevel(), is(fc.getLevel()));

        /*
        findByIds
         */
        when(frontendCategoryDao.findByIds(Lists.newArrayList(1L))).thenReturn(Lists.newArrayList(fc));
        assertThat(frontendCategoryManager.findByIds(Lists.newArrayList(1L)).size(), is(1));

        /*
        findChildrenOf
         */
        when(frontendCategoryDao.findChildrenOf(1L)).thenReturn(Lists.newArrayList(fc));
//        assertThat(frontendCategoryManager.findChildrenOf(1L).size(), is(1));
        // TODO: to be completed

        /*
        findByLevels
         */
        when(frontendCategoryDao.findByLevels(1)).thenReturn(Lists.newArrayList(fc));
        assertThat(frontendCategoryManager.findByLevels(1).size(), is(1));

        /*
        findByLevelAndName
         */
        when(frontendCategoryDao.findByLevelAndName(1, "前台类目")).thenReturn(fc);
        assertThat(frontendCategoryManager.findByLevelAndName(1, "前台类目").getLevel(), is(fc.getLevel()));
    }

    @Test
    public void testFindAllLoop() throws Exception {
        // 一级
        FrontendCategory fc1 = getFc(1L, 0L, "一级类目", 1, true);
        List<FrontendCategory> root = Lists.newArrayList(fc1);

        // 二级
        FrontendCategory fc2 = getFc(2L, 1L, "二级类目1", 2, true);
        FrontendCategory fc3 = getFc(3L, 1L, "二级类目2", 2, true);
        List<FrontendCategory> childrenOfOne = Lists.newArrayList(fc2, fc3); // fc1的孩子

        // 三级
        FrontendCategory fc4 = getFc(4L, 2L, "三级类目1", 3, false);
        FrontendCategory fc5 = getFc(5L, 2L, "三级类目2", 3, false);
        List<FrontendCategory> childrenOfTwo = Lists.newArrayList(fc4, fc5); // fc2的孩子

        FrontendCategory fc6 = getFc(6L, 3L, "三级类目3", 3, false);
        FrontendCategory fc7 = getFc(7L, 3L, "三级类目4", 3, false);
        List<FrontendCategory> childrenOfThree = Lists.newArrayList(fc6, fc7); // fc3的孩子

        when(frontendCategoryDao.findChildrenOf(0L)).thenReturn(root);
        when(frontendCategoryDao.findChildrenOf(1L)).thenReturn(childrenOfOne);
        when(frontendCategoryDao.findChildrenOf(2L)).thenReturn(childrenOfTwo);
        when(frontendCategoryDao.findChildrenOf(3L)).thenReturn(childrenOfThree);

        List<FrontendCategory> result = frontendCategoryManager.findAllLoop(0L);
        assertThat(result.size(), is(7));
    }
}