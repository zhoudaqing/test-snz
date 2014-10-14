package io.terminus.snz.requirement.dao;

import com.google.common.collect.Lists;
import io.terminus.snz.requirement.model.DetailQuota;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

/**
 * Desc:详细的配额信息测试
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-25.
 */
public class DetailQuotaDaoTest extends BasicTest {
    @Autowired
    private DetailQuotaDao detailQuotaDao;

    @Test
    public void testCreate(){
        detailQuotaDao.create(mock());
    }

    @Test
    public void testCreateBatch(){
        List<DetailQuota> quotaList = Lists.newArrayList();

        for(int i=0; i<5; i++){
            DetailQuota quota = mock();
            quota.setModuleId(new Long(i));
            quotaList.add(quota);
        }
        detailQuotaDao.createBatch(quotaList);
    }

    @Test
    public void testUpdate(){
        detailQuotaDao.update(mock());
    }

    @Test
    public void testFindByParams(){
        detailQuotaDao.findByParams(1l , new HashMap<String, Object>());
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
