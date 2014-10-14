package io.terminus.snz.requirement.dao;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.terminus.snz.requirement.model.DerivativeDiff;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by jiaoyuan on 14-7-7.
 */
public class DerivativeDiffDaoTest extends  BasicTest{
    @Autowired
    private  DerivativeDiffDao derivativeDiffDao;
    @Test
    public void  createTest(){
        derivativeDiffDao.getSqlSession().insert("DerivativeDiff.create",mock());
    }
    @Test
    public void updateTest(){
        DerivativeDiff derivativeDiff = mock();
        derivativeDiff.setPrinting(2);
        derivativeDiff.setStructure(3);
        derivativeDiff.setMatrix(1);
        derivativeDiffDao.update(derivativeDiff);
    }
    public DerivativeDiff mock(){
        DerivativeDiff derivativeDiff = new DerivativeDiff();
        derivativeDiff.setBomModule(1);
        derivativeDiff.setModuleId(1L);
        derivativeDiff.setRequirementId(1L);
        derivativeDiff.setDrive(0);
        derivativeDiff.setHostChange(1);
        derivativeDiff.setMaterial(0);
        derivativeDiff.setOverseasParts(1);
        derivativeDiff.setMatrix(0);
        derivativeDiff.setStructure(1);
        derivativeDiff.setPrinting(2);
        derivativeDiff.setSoftwareParam(1);
        derivativeDiff.setSurfaceTreatment(1);
        derivativeDiff.setModuleId(1l);
        derivativeDiff.setModuleName("name2");
        return derivativeDiff;
    }
    

    @Test
    public void findByParamsTest(){
        Map<String , Object> params = Maps.newHashMap();
        params.put("offset" , 0);
        params.put("size" , 20);

        assertThat(derivativeDiffDao.findByParams(1L , params), notNullValue());
    }

    @Test
    public void createBashTest(){

        List<DerivativeDiff> derivativeDiffs = Lists.newArrayList();
        derivativeDiffs.add(mock());
        derivativeDiffs.add(mock());
        derivativeDiffs.add(mock());
        derivativeDiffs.add(mock());
        derivativeDiffDao.createBash(derivativeDiffs);

    }

    @Test
    public void checkDiffExists(){
        assertThat(derivativeDiffDao.checkDiffExists(1L , 1L), notNullValue());
    }

    @Test
    public  void deleteTest(){
        assertTrue(derivativeDiffDao.delete(1l,1l));
    }
}
