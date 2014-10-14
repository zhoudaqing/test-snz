package io.terminus.snz.user.service;

import com.google.common.collect.Lists;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.dao.SupplierTQRDCInfoDao;
import io.terminus.snz.user.dto.SupplierLevelCountDto;
import io.terminus.snz.user.manager.SupplierTQRDCInfoManager;
import io.terminus.snz.user.model.SupplierTQRDCInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class SupplierTQRDCInfoServiceImplTest {


    @InjectMocks
    private SupplierTQRDCInfoServiceImpl supplierTQRDCInfoService;

    @Mock
    private SupplierTQRDCInfoDao supplierTQRDCInfoDao;

    @Mock
    private SupplierTQRDCInfo supplierTQRDCInfo;

    @Mock
    private SupplierTQRDCInfoManager supplierTQRDCInfoManager;


    private List<SupplierTQRDCInfo> supplierTQRDCInfos = Lists.newArrayList();


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        SupplierTQRDCInfo tqrdcInfo = new SupplierTQRDCInfo();
        tqrdcInfo.setProductLineId(1); tqrdcInfo.setCompositeScore(100);
        supplierTQRDCInfos.add(tqrdcInfo);

        tqrdcInfo = new SupplierTQRDCInfo();
        tqrdcInfo.setProductLineId(1);
        supplierTQRDCInfos.add(tqrdcInfo);

        tqrdcInfo = new SupplierTQRDCInfo();
        tqrdcInfo.setProductLineId(2); tqrdcInfo.setCompositeScore(89);
        supplierTQRDCInfos.add(tqrdcInfo);

        tqrdcInfo = new SupplierTQRDCInfo();
        tqrdcInfo.setProductLineId(2); tqrdcInfo.setCompositeScore(76);
        supplierTQRDCInfos.add(tqrdcInfo);

        tqrdcInfo = new SupplierTQRDCInfo();
        tqrdcInfo.setProductLineId(3); tqrdcInfo.setCompositeScore(100);
        supplierTQRDCInfos.add(tqrdcInfo);

        tqrdcInfo = new SupplierTQRDCInfo();
        tqrdcInfo.setProductLineId(3); tqrdcInfo.setCompositeScore(86);
        supplierTQRDCInfos.add(tqrdcInfo);

        tqrdcInfo = new SupplierTQRDCInfo();
        tqrdcInfo.setProductLineId(3); tqrdcInfo.setCompositeScore(66);
        supplierTQRDCInfos.add(tqrdcInfo);



    }

    @Test
    public void testCreate() throws Exception {

        Response<Boolean> resp = supplierTQRDCInfoService.create(supplierTQRDCInfos.get(0));
        Assert.assertTrue("Should be created correctly", resp.getResult());

    }

    @Test
    public void testcountCompositeScoreOfMonth(){

        when(supplierTQRDCInfoDao.findCompositeScoreOfMonth("2014-01"))
                .thenReturn(supplierTQRDCInfos);
        Response<SupplierLevelCountDto> response = supplierTQRDCInfoService.countCompositeScoreOfMonth("2014-01");
        SupplierLevelCountDto dto = response.getResult();

        Assert.assertTrue("Fault algorithmic with: SupplierTQRDCInfoService.countCompositeScoreOfMonth()",
                dto.getBestCount()==2 &&
                dto.getStandardCount()==2 &&
                dto.getLimitedCount()==1 &&
                dto.getBadCount()==1
        );

        try {
            when(supplierTQRDCInfoDao.findCompositeScoreOfMonth(Matchers.<String>anyObject())).thenThrow(new RuntimeException("故意异常"));
            response = supplierTQRDCInfoService.countCompositeScoreOfMonth("");
            Assert.assertTrue("This test must by error", !response.isSuccess());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void testCountCompositeScoreByProductLineOfMonth() throws Exception {

        when(supplierTQRDCInfoDao.findCompositeScoreOfMonth("2014-01")).thenReturn(supplierTQRDCInfos);
        Response<Map<Integer, SupplierLevelCountDto>> response = supplierTQRDCInfoService.countCompositeScoreByProductLineOfMonth("2014-01");

        SupplierLevelCountDto prd1=response.getResult().get(1);
        SupplierLevelCountDto prd2=response.getResult().get(2);
        SupplierLevelCountDto prd3=response.getResult().get(3);

        Assert.assertTrue("Fault algorithmic with: SupplierTQRDCInfoService.countCompositeScoreByProductLineOfMonth()",
            prd1.getBestCount()==1 &&
                prd2.getStandardCount()==1&&
                prd2.getLimitedCount()==1&&
                    prd3.getStandardCount()==1&&
                    prd3.getBadCount()==1
                );

        when(supplierTQRDCInfoDao.findCompositeScoreOfMonth(Matchers.<String>anyObject())).thenThrow(Exception.class);
        response = supplierTQRDCInfoService.countCompositeScoreByProductLineOfMonth("2014-01");
        Assert.assertTrue("This test must by error", !response.isSuccess());

    }

    @Test
    public void testexecTransfer() throws Exception {

        when(supplierTQRDCInfoManager.batchImportTQRDCInfos(anyMap())).thenReturn(1);
        Response<Integer> resp = supplierTQRDCInfoService.execTransfer(anyMap());
        Assert.assertTrue("Must by executed correctly", resp.getResult()!=null);

        when(supplierTQRDCInfoManager.batchImportTQRDCInfos(anyMap())).thenThrow(Exception.class);
        resp = supplierTQRDCInfoService.execTransfer(anyMap());
        Assert.assertTrue("Must by executed with error", resp.getError()!=null);

    }
}