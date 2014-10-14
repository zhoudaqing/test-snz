package io.terminus.snz.requirement.ignore;

import io.terminus.snz.requirement.model.Module;
import io.terminus.snz.requirement.service.BasicService;
import io.terminus.snz.requirement.service.ModuleService;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;

public class ModuleServiceTest extends BasicService {
    @Autowired
    private ModuleService moduleService;

    @Test
    public void testCreate() throws Exception {
        //assertNotNull(moduleService.create(mock()));
    }

    @Test
    public void testUpdate() throws Exception {
        //assertNotNull(moduleService.update(mock()));
    }

    @Test
    public void testDelete() throws Exception {
        assertNotNull(moduleService.delete(null));
        assertNotNull(moduleService.delete(1l));
    }

    @Test
    public void testFindAllIdentify() throws Exception {
        assertNotNull(moduleService.findAllIdentify());
    }

    @Test
    public void testFindByParams() throws Exception {
        assertNotNull(moduleService.findByParams(null , 1, 20));
        assertNotNull(moduleService.findByParams(1l , 1, 20));
    }

    @Test
    public void testFindByRequirementId() throws Exception {
        assertNotNull(moduleService.findByRequirementId(null , 1l, 1, 20));
        assertNotNull(moduleService.findByRequirementId(1l , null, 1, 20));
        assertNotNull(moduleService.findByRequirementId(1l , 1l, 1, 20));
    }

    @Test
    public void testSetModuleNum() throws Exception {
        assertNotNull(moduleService.setModuleNum());
    }

    @Test
    public void testPagingByPurchaserId() throws Exception {
        assertNotNull(moduleService.pagingByPurchaserId(null, 1, 20));
        assertNotNull(moduleService.pagingByPurchaserId(1l, 1, 20));
        assertNotNull(moduleService.pagingByPurchaserId(0l, 1, 20));
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
}