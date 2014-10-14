package io.terminus.snz.category.dao;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import io.terminus.snz.category.model.BackendCategory;
import io.terminus.snz.category.model.BackendCategoryProperty;
import io.terminus.snz.category.model.FrontendCategory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class CategoryDaoTest extends BasicTest {

    private FrontendCategory fc;

    private BackendCategory bc;

    private BackendCategoryProperty bcp1, bcp2;

    @Autowired
    private FrontendCategoryDao fcDao;

    @Autowired
    private BackendCategoryDao bcDao;

    @Autowired
    private BackendCategoryPropertyDao bcpDao;

    @Before
    public void setUp() throws Exception {
        fc = newFrontCategory("fc1", 0L, 1, true);
        bc = newBackCategory("bc1",0L,1, true);
        fcDao.create(fc);
        bcDao.create(bc);

        bcp1 = new BackendCategoryProperty();
        bcp2 = new BackendCategoryProperty();

        bcp1.setBcId(bc.getId());
        bcp1.setType("type1");
        bcp1.setFactoryNum("工厂1");
        bcp1.setValue1("1000");
        bcp1.setValue2("10000");
        bcp1.setValue3("100000");

        bcp2.setBcId(bc.getId());
        bcp2.setType("type2");
        bcp2.setFactoryNum("工厂2");
        bcp2.setValue1("2000");
        bcp2.setValue2("20000");
        bcp2.setValue3("200000");

        bcpDao.create(bcp1);
        bcpDao.create(bcp2);
    }

    private FrontendCategory newFrontCategory(String name, Long parentId, Integer level, Boolean hasChildren) {
        FrontendCategory f = new FrontendCategory();
        f.setName(name);
        f.setParentId(parentId);
        f.setLevel(level);
        f.setHasChildren(hasChildren);
        return f;
    }

    private BackendCategory newBackCategory(String name, Long parentId, Integer level, Boolean hasChildren) {
        BackendCategory b = new BackendCategory();
        b.setName(name);
        b.setParentId(parentId);
        b.setLevel(level);
        b.setHasChildren(hasChildren);
        return b;
    }



    @Test
    public void testUpdate() throws Exception {
        FrontendCategory ufc = newFrontCategory("ufc1", null,null ,false);
        ufc.setId(fc.getId());
        BackendCategory ubc = newBackCategory("ubc1", null, null, false);
        ubc.setId(bc.getId());
        fcDao.update(ufc);
        bcDao.update(ubc);

        FrontendCategory actualFc = fcDao.findById(fc.getId());
        BackendCategory actualBc = bcDao.findById(bc.getId());

        assertThat(actualFc.getName(), is(ufc.getName()));
        assertThat(actualBc.getName(), is(ubc.getName()));
    }

    @Test
    public void testDelete() throws Exception {
        Long fcid = fc.getId(), bcid = bc.getId();
        fcDao.delete(fcid);
        bcDao.delete(bcid);
        assertThat(fcDao.findById(fcid), nullValue());
        assertThat(bcDao.findById(bcid), nullValue());
    }

    @Test
    public void testFindById() throws Exception {
        FrontendCategory actualFc = fcDao.findById(fc.getId());
        BackendCategory actualBc = bcDao.findById(bc.getId());
        assertThat(actualFc, is(fc));
        assertThat(actualBc, is(bc));
    }

    @Test
    public void testFindByIds() throws Exception {
        FrontendCategory fc2 = newFrontCategory("fc2", fc.getId(),fc.getLevel()+1,false);
        BackendCategory bc2 = newBackCategory("bc2", bc.getId(), bc.getLevel()+1, false);
        fcDao.create(fc2);
        bcDao.create(bc2);

        List<FrontendCategory> fcs = fcDao.findByIds(ImmutableList.of(fc.getId(), fc2.getId()));
        List<BackendCategory>  bcs = bcDao.findByIds(ImmutableList.of(bc.getId(), bc2.getId()));

        assertThat(fcs, contains(fc, fc2));
        assertThat(bcs, contains(bc, bc2));
    }

    @Test
    public void testFindByLevels() throws Exception {
        List<FrontendCategory> fcs = fcDao.findByLevels(1);
        List<BackendCategory> bcs = bcDao.findByLevels(1);
        assertNotNull(fcs);
        assertNotNull(bcs);
        assertThat(fcs.size(), is(1));
        assertThat(bcs.size(), is(1));
    }

    @Test
    public void testFindByLevelAndName() throws Exception {
        FrontendCategory fcln = fcDao.findByLevelAndName(1, "fc1");
        BackendCategory bcln = bcDao.findByLevelAndName(1, "bc1");
        assertNotNull(fcln);
        assertNotNull(bcln);
    }

    @Test
    public void testFindChildrenOf() throws Exception {
        FrontendCategory fc2 = newFrontCategory("fc2", fc.getId(), fc.getLevel() + 1, false);
        FrontendCategory fc3 = newFrontCategory("fc3", fc.getId(),fc.getLevel()+1,false);
        BackendCategory bc2 = newBackCategory("bc2", bc.getId(), bc.getLevel() + 1, false);
        BackendCategory bc3 = newBackCategory("bc3", bc.getId(), bc.getLevel()+1, false);

        fcDao.create(fc2);
        fcDao.create(fc3);

        bcDao.create(bc2);
        bcDao.create(bc3);

        List<FrontendCategory> fcs = fcDao.findChildrenOf(fc.getId());
        List<BackendCategory> bcs = bcDao.findChildrenOf(bc.getId());

        assertThat(fcs, contains(fc2, fc3));
        assertThat(bcs, contains(bc2, bc3));
    }

    @Test
    public void testPropertyUpdate() {
        BackendCategoryProperty bcp3 = new BackendCategoryProperty();
        bcp3.setId(bcp1.getId());
        bcp3.setType("type3");
        bcp3.setFactoryNum("工厂修改！！");
        bcp3.setValue1("3400");
        bcpDao.update(bcp3);
        assertThat(bcpDao.findById(bcp1.getId()).getValue1(), is("3400"));
        assertThat(bcpDao.findById(bcp1.getId()).getType(), is("type3"));
        assertThat(bcpDao.findById(bcp1.getId()).getFactoryNum(), is("工厂修改！！"));
    }

    @Test
    public void testPropertyDelete() {
        bcpDao.delete(bcp1.getId());
        assertNull(bcpDao.findById(bcp1.getId()));
    }

    @Test
    public void testPropertyFindByBcId() {
        List<BackendCategoryProperty> bcpl = bcpDao.findByBcId(bc.getId());
        assertThat(bcpl.size(), is(2));
    }

    @Test
    public void testPropertyFindByBcIds() {
        List<Long> ids = Lists.newArrayList();
        ids.add(bc.getId());
        List<BackendCategoryProperty> bcpl = bcpDao.findByBcIds(ids);
        assertNotNull(bcpl);
        assertThat(bcpl.size(), is(2));
    }

    @Test
    public void testPropertyFindByBcIdWithFactoryNums() {
        // 全部取出
        List<BackendCategoryProperty> bcpl = bcpDao.findByBcIdWithFactoryNums(bc.getId(), Lists.newArrayList("工厂1", "工厂2"));
        assertNotNull(bcpl);
        assertThat(bcpl.size(), is(2));

        // 只取 "工厂1"
        bcpl = bcpDao.findByBcIdWithFactoryNums(bc.getId(), Lists.newArrayList("工厂1"));
        assertNotNull(bcpl);
        assertThat(bcpl.size(), is(1));
        assertThat(bcpl.get(0).getValue1(), is("1000"));

        // 只取 "工厂2"
        bcpl = bcpDao.findByBcIdWithFactoryNums(bc.getId(), Lists.newArrayList("工厂2"));
        assertNotNull(bcpl);
        assertThat(bcpl.size(), is(1));
        assertThat(bcpl.get(0).getValue1(), is("2000"));
    }
}