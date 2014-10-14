package io.terminus.snz.user.manager;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import io.terminus.snz.user.dao.CompanyDao;
import io.terminus.snz.user.dao.SupplierTQRDCInfoDao;
import io.terminus.snz.user.dao.SupplierTQRDCInfoTmpDao;
import io.terminus.snz.user.model.Company;
import io.terminus.snz.user.model.SupplierTQRDCInfoTmp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Random;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

/**
 * @author wanggen
 */
@RunWith(JUnit4.class)
public class SupplierTQRDCInfoManagerTest {

    @InjectMocks
    private SupplierTQRDCInfoManager supplierTQRDCInfoManager;

    @Mock
    private SupplierTQRDCInfoDao supplierTQRDCInfoDao;

    @Mock
    private SupplierTQRDCInfoTmpDao supplierTQRDCInfoTmpDao;

    @Mock
    private CompanyDao companyDao;

    @org.junit.Before
    public void before(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testBatchImportTQRDCInfos() throws Exception {

        List<SupplierTQRDCInfoTmp> supplierTQRDCInfoTmps = Lists.newArrayList();
        SupplierTQRDCInfoTmp tmp;

        tmp = new SupplierTQRDCInfoTmp();
        tmp.setModule("钢板");
//        tmp.setSupplierCode();
        randomeScores(tmp);
        supplierTQRDCInfoTmps.add(tmp);

        tmp = new SupplierTQRDCInfoTmp();
        tmp.setModule("钢板");
        randomeScores(tmp);
        supplierTQRDCInfoTmps.add(tmp);

        tmp = new SupplierTQRDCInfoTmp();
        tmp.setModule("钢板");
        randomeScores(tmp);
        supplierTQRDCInfoTmps.add(tmp);

        tmp = new SupplierTQRDCInfoTmp();
        tmp.setModule("钢板");
        randomeScores(tmp);
        supplierTQRDCInfoTmps.add(tmp);

        tmp = new SupplierTQRDCInfoTmp();
        tmp.setModule("钢板");
        randomeScores(tmp);
        supplierTQRDCInfoTmps.add(tmp);

        tmp = new SupplierTQRDCInfoTmp();
        tmp.setModule("钢板");
        randomeScores(tmp);
        supplierTQRDCInfoTmps.add(tmp);

        tmp = new SupplierTQRDCInfoTmp();
        tmp.setModule("钢板");
        randomeScores(tmp);
        supplierTQRDCInfoTmps.add(tmp);


        tmp = new SupplierTQRDCInfoTmp();
        tmp.setModule("门");
        randomeScores(tmp);
        supplierTQRDCInfoTmps.add(tmp);

        tmp = new SupplierTQRDCInfoTmp();
        tmp.setModule("门");
        randomeScores(tmp);
        supplierTQRDCInfoTmps.add(tmp);

        tmp = new SupplierTQRDCInfoTmp();
        tmp.setModule("门");
        randomeScores(tmp);
        supplierTQRDCInfoTmps.add(tmp);

        when(companyDao.findBySupplierCode(anyString())).thenReturn(new Company());
        when(supplierTQRDCInfoTmpDao.findByDate(anyString())).thenReturn(supplierTQRDCInfoTmps);
        supplierTQRDCInfoManager.batchImportTQRDCInfos(ImmutableMap.<String, Object>of("date", "20140101"));


    }

    private void randomeScores(SupplierTQRDCInfoTmp tmp) {
        int score;
        tmp.setCompositeScore((score=random.nextInt(100))<50 ? (score+50) : score);
        tmp.setTechScore((score=random.nextInt(100))<50 ? (score+50) : score);
        tmp.setQualityScore((score=random.nextInt(100))<50 ? (score+50) : score);
        tmp.setRespScore((score=random.nextInt(100))<50 ? (score+50) : score);
        tmp.setDeliverScore((score=random.nextInt(100))<50 ? (score+50) : score);
        tmp.setCostScore((score=random.nextInt(100))<50 ? (score+50) : score);
        tmp.setSupplierCode("V"+random.nextInt(10000));
    }
    private Random random = new Random();
}