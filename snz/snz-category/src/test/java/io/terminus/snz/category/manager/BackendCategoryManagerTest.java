package io.terminus.snz.category.manager;

import com.google.common.collect.Lists;
import io.terminus.snz.category.dao.BackendCategoryDao;
import io.terminus.snz.category.model.BackendCategory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 8/4/14
 */
public class BackendCategoryManagerTest {

    @InjectMocks
    private BackendCategoryManager backendCategoryManager;

    @Mock
    private BackendCategoryDao backendCategoryDao;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCRUD() throws Exception {
        BackendCategory bc = new BackendCategory();
        bc.setId(1L);
        bc.setName("后台类目");
        bc.setLevel(1);
        bc.setParentId(0L);

        /*
        create
         */
        when(backendCategoryDao.create(bc)).thenReturn(1L);
        assertThat(backendCategoryManager.create(bc), is(1L));

        /*
        update
         */
        when(backendCategoryDao.update(bc)).thenReturn(true);
        when(backendCategoryDao.findById(1L)).thenReturn(bc);
        assertThat(backendCategoryManager.update(bc), is(true));

        /*
        delete
         */
        when(backendCategoryDao.delete(1L)).thenReturn(true);
//        assertThat(backendCategoryManager.delete(1L), is(true));
        // TODO: to be completed

        /*
        findById
         */
        when(backendCategoryDao.findById(1L)).thenReturn(bc);
        assertThat(backendCategoryManager.findById(1L).getLevel(), is(bc.getLevel()));

        /*
        findByIds
         */
        when(backendCategoryDao.findByIds(Lists.newArrayList(1L))).thenReturn(Lists.newArrayList(bc));
        assertThat(backendCategoryManager.findByIds(Lists.newArrayList(1L)).size(), is(1));

        /*
        findChildrenOf
         */
        when(backendCategoryDao.findChildrenOf(1L)).thenReturn(Lists.newArrayList(bc));
//        assertThat(backendCategoryManager.findChildrenOf(1L).size(), is(1));
        // TODO: to be completed

        /*
        findByLevels
         */
        when(backendCategoryDao.findByLevels(1)).thenReturn(Lists.newArrayList(bc));
        assertThat(backendCategoryManager.findByLevels(1).size(), is(1));

        /*
        findByLevelAndName
         */
        when(backendCategoryDao.findByLevelAndName(1, "后台类目")).thenReturn(bc);
        assertThat(backendCategoryManager.findByLevelAndName(1, "后台类目").getLevel(), is(bc.getLevel()));
    }
}