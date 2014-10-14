package io.terminus.snz.requirement.dao;

import com.google.common.collect.Lists;
import io.terminus.snz.requirement.model.ModuleFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertNotNull;

public class ModuleFactoryDaoTest extends BasicTest {

    @Autowired
    private ModuleFactoryDao moduleFactoryDao;

    @Test
    public void testCreate() throws Exception {
        assertNotNull(moduleFactoryDao.create(mock()));
    }

    @Test
    public void testCreateBatch() throws Exception {
        List<ModuleFactory> factoryList = Lists.newArrayList();

        for(int i=0; i<5; i++){
            factoryList.add(mock());
        }

        assertNotNull(moduleFactoryDao.createBatch(factoryList));
    }

    @Test
    public void testUpdate() throws Exception {
        assertNotNull(moduleFactoryDao.update(mock()));
    }

    @Test
    public void testDelete() throws Exception {
        assertNotNull(moduleFactoryDao.delete(1l));
    }

    @Test
    public void testDeleteByReqId(){
        assertNotNull(moduleFactoryDao.delete(1l));
    }

    @Test
    public void testFindById() throws Exception {
        assertNotNull(moduleFactoryDao.findById(1l));
    }

    @Test
    public void testFindByIds() throws Exception {
        assertNotNull(moduleFactoryDao.findByIds(Lists.newArrayList(1l , 2l)));
    }

    @Test
    public void testFindByModuleId() throws Exception {
        assertNotNull(moduleFactoryDao.findByModuleId(1l));
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
}