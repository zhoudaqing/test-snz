package io.terminus.snz.requirement.dao;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.terminus.snz.requirement.model.ModuleQuota;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Desc:
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-03.
 */
public class ModuleQuotaDaoTest extends BasicTest {
    @Autowired
    private ModuleQuotaDao moduleQuotaDao;


    @Test
    public void createTest(){
        moduleQuotaDao.create(mock());
    }

    @Test
    public void createBatch(){
        List<ModuleQuota> quotaList = Lists.newArrayList();
        quotaList.add(mock());
        quotaList.add(mock());
        quotaList.add(mock());
        moduleQuotaDao.createBatch(quotaList);
    }

    @Test
    public void updateTest(){
        ModuleQuota moduleQuota = mock();
        moduleQuota.setId(1l);
        moduleQuota.setSupplierName("zero");

        moduleQuotaDao.update(moduleQuota);
    }

    @Test
    public void testFindByIds(){
        moduleQuotaDao.findByIds(new String[]{"1" , "2"});
    }

    @Test
    public void testFindByRequirementId(){
        moduleQuotaDao.findByRequirementId(1l , null);
    }

    @Test
    public void testFindByParams(){
        Map<String , Object> params = Maps.newHashMap();
        params.put("offset" , 0);
        params.put("size" , 20);
        params.put("supplierId" , 1l);
        moduleQuotaDao.findByParams(1l , params);
    }

    @Test
    public void testFindOtherQuota(){
        List<ModuleQuota> quotaList = moduleQuotaDao.findOtherQuota(1l , 1l, 1l);
    }

    @Test
    public void findByIdTest(){
        assertThat(moduleQuotaDao.findById(1l), notNullValue());
    }

    private ModuleQuota mock(){
        ModuleQuota moduleQuota = new ModuleQuota();
        moduleQuota.setRequirementId(1l);
        moduleQuota.setSolutionId(1l);
        moduleQuota.setModuleNum("00000a1");
        moduleQuota.setModuleId(1l);
        moduleQuota.setModuleName("moduleName");
        moduleQuota.setSupplierId(1l);
        moduleQuota.setSupplierName("Michael");
        moduleQuota.setQuantity(50);
        moduleQuota.setScale(70);

        return moduleQuota;
    }
}
