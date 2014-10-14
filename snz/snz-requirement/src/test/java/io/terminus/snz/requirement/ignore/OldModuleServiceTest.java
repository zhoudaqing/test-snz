package io.terminus.snz.requirement.ignore;

import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.model.OldModule;
import io.terminus.snz.requirement.service.OldModuleService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
/**
 * Desc: 老品业务测试
 * Mail: houly@terminus.io
 * author: Hou Luyao
 * Date: 14-7-24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/requirement-service-test-forOldModule.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class OldModuleServiceTest {
    @Autowired
    private OldModuleService oldModuleService;

    @Test
    public void testCreate(){
        Response<Boolean> response = oldModuleService.create(mockOldModule());
        Assert.assertTrue(response.isSuccess());
        Assert.assertTrue(response.getResult());
    }

    private OldModule mockOldModule(){
        OldModule oldModule = new OldModule();
        oldModule.setRequirementId(3L);
        oldModule.setModuleNum("wuliaohao");
        oldModule.setPrioritySelect(0);
        oldModule.setModuleName("wuliaomiaoshu");
        oldModule.setCost(10);
        oldModule.setTotal(100);
        oldModule.setSeriesId(1l);
        oldModule.setSeriesName("seriesname");
        oldModule.setSupplyAt(new Date());
        oldModule.setResourceNum("[{\"faId\":42,\"faNum\":8640,\"faName\":\"重庆零部件家用空调PL工厂\",\"num\":\"1000\"}]");
        return oldModule;
    }

}
