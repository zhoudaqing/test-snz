package io.terminus.snz.requirement.dao;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.terminus.snz.requirement.model.Module;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;

/**
 * Desc:需求具体模块信息操作测试
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-02.
 */
public class ModuleDaoTest extends BasicTest {
    @Autowired
    private ModuleDao moduleDao;

    @Test
    public void createTest(){
        moduleDao.create(mock());
    }

    @Test
    public void createBatchTest(){
        List<Module> moduleList = Lists.newArrayList();
        moduleList.add(mock());
        moduleList.add(mock());
        moduleList.add(mock());
        moduleDao.createBatch(moduleList);
    }

    @Test
    public void updateTest(){
        Module module = mock();
        module.setId(1l);
        module.setModuleName("update name");
        moduleDao.update(module);
    }

    @Test
    public void findByIdTest(){
        assertThat(moduleDao.findById(1l), notNullValue());
    }

    @Test
    public void testCountById(){
        System.out.println(moduleDao.countById(1l));
    }

    @Test
    public void testFindNoModuleNum(){
        System.out.println(moduleDao.findNoModuleNum(null));
    }

    @Test
    public void testFindByIds(){
        List<Long> ids = new ArrayList<Long>();
        ids.add(1l);
        ids.add(2l);
        ids.add(100l);
        moduleDao.findByIds(ids);
    }

    @Test
    public void testFindModules(){
        List<Module> moduleList = moduleDao.findModules(1l);
    }

    @Test
    public void findByParams(){
        Map<String , Object> params = Maps.newHashMap();
        params.put("offset" , 0);
        params.put("size" , 20);
        params.put("type" , 3);
        moduleDao.findByParams(1l , params);
    }

    @Test
    public void testFindFilterModule(){
        List<Long> filterIds = Lists.newArrayList();
        filterIds.add(1l);
        Map<String , Object> params = Maps.newHashMap();
        params.put("pageNo" , 1);
        params.put("size" , 20);
        moduleDao.findFilterModule(1l, filterIds, params);
    }

    @Test
    public void deleteTest(){
        moduleDao.delete(1l);
    }

    @Test
    public void testDeleteByReqId(){
        moduleDao.deleteByReqId(1l);
    }

    @Test
    public void testFindDerive(){
        moduleDao.findDerive("0000001A" , 3);
    }

    @Test
    public void testShouldFindOnBy() {
        Module mock = mock();
        mock.setModuleNum("23333");
        moduleDao.create(mock);

        Module param = new Module();
        param.setModuleNum(mock.getModuleNum());
        assertNotNull(param);
    }

    private Module mock(){
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

    @Test
    public void findOldModuleByNumTest(){
        assertThat(moduleDao.findOldModuleByNum("A01"),notNullValue());
    }

    @Test
    public void deleteByParamsTest(){
        assertTrue(moduleDao.deleteByParams(1l,1l));
        assertFalse(moduleDao.deleteByParams(2l,1l));
    }

}
