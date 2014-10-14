package io.terminus.snz.requirement.service;

import com.google.common.collect.Lists;
import io.terminus.common.utils.JsonMapper;
import io.terminus.snz.requirement.model.DetailQuota;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertNotNull;

public class QuotaSapServiceTest extends BasicService {
    private static final JsonMapper JSON_MAPPER = JsonMapper.JSON_NON_DEFAULT_MAPPER;

    @Autowired
    private QuotaSapService quotaSapService;

    @Test
    public void testUpdateQuota() throws Exception {
        assertNotNull(quotaSapService.updateQuota(mock()));
    }

    @Test
    public void testUpdateQuotaBatch() throws Exception {
        List<DetailQuota> quotaList = Lists.newArrayList();

        for(int i=0; i<5; i++){
            quotaList.add(mock());
        }

        assertNotNull(quotaSapService.updateQuotaBatch(JSON_MAPPER.toJson(quotaList)));
    }

    @Test
    public void testFindQuotaWithSap() throws Exception {
        assertNotNull(quotaSapService.findQuotaWithSap(null , 1, null, null));
        assertNotNull(quotaSapService.findQuotaWithSap(1l , null, null, null));
        assertNotNull(quotaSapService.findQuotaWithSap(1l , 1, null, null));
    }

    @Test
    public void testSetQuotaInfoToSAP() throws Exception {
        assertNotNull(quotaSapService.setQuotaInfoToSAP(null));
        assertNotNull(quotaSapService.setQuotaInfoToSAP(1l));
    }

    private DetailQuota mock(){
        DetailQuota quota = new DetailQuota();
        quota.setRequirementId(1l);
        quota.setRequirementName("requirementName");
        quota.setSolutionId(1l);
        quota.setModuleId(1l);
        quota.setModuleNum("000001");
        quota.setModuleName("moduleName");
        quota.setFactoryNum("9878");
        quota.setSupplierId(1l);
        quota.setSupplierVCode("00001V");
        quota.setSupplierName("supplierName");
        quota.setPurchaseType("zh");
        quota.setQuantity(10000);
        quota.setScale(10);
        quota.setOriginalCost(100);
        quota.setActualCost(89);
        quota.setAgencyFee(1);
        quota.setFeeUnit(12);
        quota.setPurchaseUnit("zh");
        quota.setCoinType("RMB");
        quota.setTaxCode("tax");

        return quota;
    }
}