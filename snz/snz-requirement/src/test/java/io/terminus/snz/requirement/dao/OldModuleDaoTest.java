package io.terminus.snz.requirement.dao;

import com.google.common.collect.Maps;
import io.terminus.snz.requirement.model.OldModule;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by jiaoyuan on 14-7-5.
 */
public class OldModuleDaoTest extends BasicTest {
    @Autowired
    private OldModuleDao oldModuleDao;


    public OldModule mock(){
        OldModule oldModule = new OldModule();
        oldModule.setAttestation("5");
        oldModule.setCost(5);
        oldModule.setDelivery(5);
        oldModule.setHeadDrop("5");
        oldModule.setInteractionStarts(5);
        oldModule.setModuleId(5l);
        oldModule.setModuleName("5");
        oldModule.setNpiStatus(5);
        oldModule.setRequirementId(1l);
        oldModule.setRequirementName("1");
        oldModule.setNpiStatus(5);
        oldModule.setPrioritySelect(5);
        oldModule.setPurchaserId(5l);
        oldModule.setPurchaserName("5");
        oldModule.setQuality(5);
        oldModule.setResourceCount(5);
        oldModule.setSeriesId(5l);
        oldModule.setTotal(5);
        oldModule.setTimeTotal(5);
        oldModule.setModuleNum("5");
        oldModule.setOverTime(new Date());
        oldModule.setResourceNum("5");
        oldModule.setSeriesName("5");
        oldModule.setQuotaIds("[1,3,4]");
        oldModule.setSupplyAt(DateTime.now().toDate());
        oldModule.setTacticsId(5);
        return oldModule;

    }
    @Test
    public void getByIdTest(){

        assertThat(oldModuleDao.findById(1l),notNullValue());
    }
    @Test
    public  void insertTest(){
       oldModuleDao.create(mock());

    }
    @Test
    public void updateTest(){
        OldModule oldModule = mock();
        oldModule.setAttestation("att");
        oldModuleDao.update(oldModule);
    }
    @Test
    public  void deleteTest(){
        oldModuleDao.delete(1l);
    }
    @Test
    public void findByIds() {
        List<Long> ids = new ArrayList<Long>();
        ids.add(1l);
        ids.add(2l);
        ids.add(8l);

        oldModuleDao.findByIds(ids);

    }
    @Test
    public  void findByModuleNumTest(){
        oldModuleDao.findByModuleNum("modulenum");
    }
    @Test
    public  void findByFiltersTest(){
        Map params = Maps.newHashMap();

        params.put("offset", 0);
        params.put("size", 20);
        oldModuleDao.findByFilters(params);
    }
    @Test
    public void createTest(){
        oldModuleDao.create(mock());

    }
}
