package io.terminus.snz.requirement.ignore;

import io.terminus.common.model.Paging;
import io.terminus.common.utils.JsonMapper;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.dto.DerivativeDto;
import io.terminus.snz.requirement.model.DerivativeDiff;
import io.terminus.snz.requirement.service.DerivativeService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Desc: 提供衍生品信息的相关操作测试
 * Mail: houly@terminus.io
 * author: Hou Luyao
 * Date: 14-7-25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/requirement-service-test-forOldModule.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class DerivativeServiceTest {
    private static final JsonMapper JSON_MAPPER = JsonMapper.JSON_NON_DEFAULT_MAPPER;

    @Autowired
    private DerivativeService derivativeService;

    @Test
    public void testFindByRequirementId(){
        Response<Paging<DerivativeDto>> response = derivativeService.findByRequirementId(1L, 1, 20);
        Assert.assertTrue(response.isSuccess());
    }

    @Test
    public void testBatchDerivative(){
        Response<Boolean> response = derivativeService.batchDerivative(mockString());
        Assert.assertTrue(response.isSuccess());
    }

    @Test
    public void testFindDiffByRequirementId(){
        Response<Paging<DerivativeDiff>> response = derivativeService.findDiffByRequirementId(1L,1,20);
        Assert.assertTrue(response.isSuccess());
    }

    private String mockString(){
        List<DerivativeDiff> derivativeDiffs = new ArrayList<DerivativeDiff>();

        DerivativeDiff derivativeDiff = new DerivativeDiff();
        derivativeDiff.setId(1L);
        derivativeDiff.setModuleName("modulename");
        derivativeDiff.setModuleId(1L);
        derivativeDiff.setRequirementId(1L);
        derivativeDiff.setBomModule(1);
        derivativeDiff.setCreatedAt(new Date());
        derivativeDiff.setDrive(1);
        derivativeDiff.setHostChange(1);
        derivativeDiff.setMaterial(1);
        derivativeDiff.setOverseasParts(1);
        derivativeDiff.setMatrix(1);
        derivativeDiff.setPrinting(1);
        derivativeDiff.setSoftwareParam(1);
        derivativeDiff.setSurfaceTreatment(1);
        derivativeDiff.setUpdatedAt(new Date());
        derivativeDiff.setStructure(1);

        DerivativeDiff derivativeDiff1 = new DerivativeDiff();
        derivativeDiff1.setId(null);
        derivativeDiff1.setModuleName("modulename");
        derivativeDiff1.setModuleId(1L);
        derivativeDiff1.setRequirementId(1L);
        derivativeDiff1.setBomModule(1);
        derivativeDiff1.setCreatedAt(new Date());
        derivativeDiff1.setDrive(1);
        derivativeDiff1.setHostChange(1);
        derivativeDiff1.setMaterial(1);
        derivativeDiff1.setOverseasParts(1);
        derivativeDiff1.setMatrix(1);
        derivativeDiff1.setPrinting(1);
        derivativeDiff1.setSoftwareParam(1);
        derivativeDiff1.setSurfaceTreatment(1);
        derivativeDiff1.setUpdatedAt(new Date());
        derivativeDiff1.setStructure(1);

        derivativeDiffs.add(derivativeDiff);
        derivativeDiffs.add(derivativeDiff1);

        return JSON_MAPPER.toJson(derivativeDiffs);
    }
}
