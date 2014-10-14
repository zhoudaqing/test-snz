package io.terminus.snz.category.dao;

import io.terminus.snz.category.dto.CategoryPair;
import io.terminus.snz.category.model.CategoryBinding;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Author: Effet
 * Email: ef@terminus.io
 * Date: 6/11/14
 */
public class CategoryBindingDaoTest extends BasicTest {

    private CategoryBinding cb1, cb2, cb3;

    @Autowired
    private CategoryBindingDao categoryBindingDao;

    private CategoryBinding mock(Long id, Long fcid, String bcs) {
        CategoryBinding cb = new CategoryBinding();
        cb.setId(id);
        cb.setFrontId(fcid);
        cb.setBcs(bcs);
        return cb;
    }

    @Before
    public void setUp() throws Exception {
        cb1 = mock(5L, 10L, "[{\"id\":\"101\",\"path\":\"hehe\"},{\"id\":\"102\",\"path\":\"haha\"},{\"id\":\"103\",\"path\":\"hehehe\"}]");
        cb2 = mock(7L, 17L, "[{\"id\":\"101\",\"path\":\"1000\"},{\"id\":\"102\",\"path\":\"2000\"}]");
        cb3 = mock(10L, 77L, "[{\"id\":9,\"path\":\"test11 > 2而\"},{\"id\":3,\"path\":\"hahah\"},{\"id\":4,\"path\":\"xixi\"}]");
        cb1.setId(categoryBindingDao.create(cb1));
        cb2.setId(categoryBindingDao.create(cb2));
        cb3.setId(categoryBindingDao.create(cb3));
    }

    @Test
    public void testCreate() {
        CategoryBinding cb = mock(null, 30L, "hehe"), cbb;
        Long id = categoryBindingDao.create(cb);
        cbb = categoryBindingDao.findById(id);
        assertThat(cb.getFrontId(), is(cbb.getFrontId()));
        assertThat(cb.getBcs(), is(cbb.getBcs()));
    }

    @Test
    public void testUpdate() {
        CategoryBinding cb = mock(null, 30L, "hehe");
        cb.setId(cb1.getId());
        categoryBindingDao.update(cb);
        cb1 = categoryBindingDao.findById(cb1.getId());
        assertThat(cb.getFrontId(), is(cb1.getFrontId()));
        assertThat(cb.getBcs(), is(cb1.getBcs()));
    }

    @Test
    public void testDelete() {
        categoryBindingDao.delete(cb1.getId());
        assertThat(categoryBindingDao.findById(cb1.getId()), nullValue());
    }

    @Test
    public void testFindById() {
        CategoryBinding cb = categoryBindingDao.findById(cb1.getId());
        assertThat(cb.getFrontId(), is(cb1.getFrontId()));
        assertThat(cb.getBcs(), is(cb1.getBcs()));
    }

    @Test
    public void testFindBy() {
        CategoryBinding cb = categoryBindingDao.findBy(cb1.getFrontId());
        assertThat(cb.getId(), is(cb1.getId()));
        assertThat(cb.getBcs(), is(cb1.getBcs()));
    }

    @Test
    public void testFindByFcid() {
        List<CategoryPair> cps = categoryBindingDao.findByFcid(cb1.getFrontId());
        List<CategoryPair> cpz = categoryBindingDao.findByFcid(cb2.getFrontId());
        List<CategoryPair> cpp = categoryBindingDao.findByFcid(cb3.getFrontId());
        assertThat(cps, notNullValue());
        assertThat(cpz, notNullValue());
        assertThat(cps.size(), is(3));
        assertThat(cpz.size(), is(2));

        List<CategoryPair> cpEmpty = categoryBindingDao.findByFcid(1000L);
        //assertThat(cpEmpty.size(), is(0));
        assertNotNull(cpEmpty);
    }

    @Test
    public void testFindAll() {
        List<CategoryBinding> cbs = categoryBindingDao.findAll();
        assertNotNull(cbs);
        assertThat(cbs.size(), is(3));
    }
}
