package io.terminus.snz.user.dao;

import com.google.common.collect.Lists;
import io.terminus.common.model.Paging;
import io.terminus.snz.user.model.SupplierBaseInfo;
import io.terminus.snz.user.model.SupplierTQRDCInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;


/**
 * Author:Guo Chaopeng
 * Created on 14-6-20.
 */
public class SupplierTQRDCInfoDaoTest extends TestBaseDao {

    private SupplierTQRDCInfo supplierTQRDCInfo;

    @Autowired
    private SupplierTQRDCInfoDao supplierTQRDCInfoDao;

    private void mock() {
        supplierTQRDCInfo = new SupplierTQRDCInfo();
        supplierTQRDCInfo.setCompositeScore(89);
        supplierTQRDCInfo.setCostScore(45);
        supplierTQRDCInfo.setDelayDays(67);
        supplierTQRDCInfo.setDeliverDiff(66);
        supplierTQRDCInfo.setIncrement(67);
        supplierTQRDCInfo.setLiveBad(67);
        supplierTQRDCInfo.setMarketBad(45);
        supplierTQRDCInfo.setDeliverScore(56);
        supplierTQRDCInfo.setMonth("2014-04");
        supplierTQRDCInfo.setNewProductPass(89);
        supplierTQRDCInfo.setSupplierName("海尔");
        supplierTQRDCInfo.setQualityScore(34);
        supplierTQRDCInfo.setSupplierCode("d434");
        supplierTQRDCInfo.setTechScore(89);
        supplierTQRDCInfo.setRequirementResp(54);
        supplierTQRDCInfo.setRespScore(99);
        supplierTQRDCInfo.setModule("aa");
        supplierTQRDCInfo.setProductLineId(1);
        supplierTQRDCInfo.setProductLine("洗衣机");
        supplierTQRDCInfo.setLocation("黄区");
        supplierTQRDCInfo.setRank(3);
        supplierTQRDCInfo.setUserId(1L);
        supplierTQRDCInfo.setCompanyId(2L);
        supplierTQRDCInfo.setDate("2014-4-5");

        supplierTQRDCInfo.setSupplierStatus(0);
        supplierTQRDCInfo.setProgramPromisedRate(80);
        supplierTQRDCInfo.setProgramSelectedRate(60);
        supplierTQRDCInfo.setProgramAdoptedRate(40);
        supplierTQRDCInfo.setAffectDeliveryCount(3);
        supplierTQRDCInfo.setPriceReductionAmount(300);
        supplierTQRDCInfo.setPriceReductionRange(28);

    }

    @Before
    public void setUp() {
        mock();
        supplierTQRDCInfoDao.create(supplierTQRDCInfo);

    }

    @Test
    public void testCreate() {
        Assert.assertNotNull(supplierTQRDCInfo.getId());
    }

    @Test
    public void testFindById() {
        SupplierTQRDCInfo model = supplierTQRDCInfoDao.findById(supplierTQRDCInfo.getId());
        System.err.println("Result of 【" + supplierTQRDCInfo.getId() + "】:" + model);
        Assert.assertNotNull(model);
    }

    @Test
    public void testFindLastByUserId() {
        SupplierTQRDCInfo model = supplierTQRDCInfoDao.findLastByUserId(supplierTQRDCInfo.getUserId());
        Assert.assertNotNull(model);
    }

    @Test
    public void testUpdate() {
        SupplierTQRDCInfo updated = new SupplierTQRDCInfo();
        updated.setId(supplierTQRDCInfo.getId());
        updated.setTechScoreRank(1);
        updated.setTechLocation("红区");
        updated.setQualityScoreRank(2);
        updated.setQualityLocation("绿区");
        updated.setRespScoreRank(3);
        updated.setRespLocation("蓝区");
        updated.setDeliveryScoreRank(4);
        updated.setDeliveryLocation("黄区");
        updated.setCostScoreRank(5);
        updated.setCostLocation("绿区");
        System.out.println(updated);
        supplierTQRDCInfoDao.update(updated);

        SupplierTQRDCInfo model = supplierTQRDCInfoDao.findById(supplierTQRDCInfo.getId());
        Assert.assertEquals(model.getTechScoreRank().intValue(), 1);
        Assert.assertEquals(model.getQualityScoreRank().intValue(), 2);
        Assert.assertEquals(model.getRespScoreRank().intValue(), 3);
        Assert.assertEquals(model.getDeliveryScoreRank().intValue(), 4);
        Assert.assertEquals(model.getCostScoreRank().intValue(), 5);
    }

    @Test
    public void testFindSupplierTQRDCInfoBy() {

        for (int i = 1; i <= 5; i++) {
            supplierTQRDCInfoDao.create(supplierTQRDCInfo);
        }

        String supplierName = null;
        String month = "2014-04";
        String orderBy = "compositeScore";
        String module = "aa";
        Integer compositeScoreStart = 60;
        Integer compositeScoreEnd = 89;
        Integer offset = 0;
        Integer limit = 20;

        Paging<SupplierTQRDCInfo> paging = supplierTQRDCInfoDao.findBy(supplierName, supplierTQRDCInfo.getSupplierCode(), month, module, orderBy, compositeScoreStart, compositeScoreEnd, offset, limit);

        Assert.assertTrue(paging.getTotal().intValue() == 6);
        Assert.assertTrue(paging.getData().size() == 6);

    }

    @Test
    public void testFindBySupplierName() {

        for (int i = 1; i <= 5; i++) {
            supplierTQRDCInfoDao.create(supplierTQRDCInfo);
        }

        String supplierName = "海尔";
        String monthStart = "2014-01";
        String monthEnd = "2014-03";
        List<SupplierTQRDCInfo> supplierTQRDCInfos = supplierTQRDCInfoDao.findBySupplierName(supplierName, monthStart, monthEnd);
        Assert.assertTrue(supplierTQRDCInfos.size() == 0);

    }

    @Test
    public void testFindBySupplierCode() {

        for (int i = 1; i <= 5; i++) {
            supplierTQRDCInfoDao.create(supplierTQRDCInfo);
        }

        String monthStart = "2014-01";
        String monthEnd = "2014-12";
        List<SupplierTQRDCInfo> supplierTQRDCInfos = supplierTQRDCInfoDao.findBySupplierCode(supplierTQRDCInfo.getSupplierCode(), monthStart, monthEnd);
        Assert.assertTrue(supplierTQRDCInfos.size() == 6);

    }


    @Test
    public void testFindByMonth() {

        for (int i = 1; i <= 5; i++) {
            supplierTQRDCInfoDao.create(supplierTQRDCInfo);
        }

        List<SupplierTQRDCInfo> supplierTQRDCInfos = supplierTQRDCInfoDao.findByMonth(supplierTQRDCInfo.getMonth());
        Assert.assertTrue(supplierTQRDCInfos.size() == 6);

    }

    @Test
    public void testFindMaxMonth() {

        for (int i = 1; i <= 9; i++) {
            String month = "2014-0" + i;
            supplierTQRDCInfo.setMonth(month);
            supplierTQRDCInfoDao.create(supplierTQRDCInfo);
        }

        supplierTQRDCInfo.setMonth("2014-11");
        supplierTQRDCInfoDao.create(supplierTQRDCInfo);

        String maxMonth = supplierTQRDCInfoDao.findMaxMonth();
        Assert.assertEquals(maxMonth, "2014-11");
    }

    @Test
    public void testGroupByMonthAndLocation() {
        Assert.assertEquals(1, supplierTQRDCInfoDao.groupByMonthAndLocation().size());
    }

    @Test
    public void testFindByMonthAndUserIds() {
        List<SupplierTQRDCInfo> supplierTQRDCInfos = supplierTQRDCInfoDao.findByMonthAndUserIds(supplierTQRDCInfo.getMonth(), Lists.newArrayList(supplierTQRDCInfo.getUserId()));
        Assert.assertTrue(supplierTQRDCInfos.size() == 1);
    }

    @Test
    public void testFindByMonthAndModule() {
        List<SupplierTQRDCInfo> supplierTQRDCInfos = supplierTQRDCInfoDao.findByMonthAndModule(supplierTQRDCInfo.getMonth(), supplierTQRDCInfo.getModule());
        Assert.assertTrue(supplierTQRDCInfos.size() == 1);
    }

    @Test
    public void testLoadBaseSupplierInfos() {
        try {
            List<SupplierBaseInfo> supplierBaseInfos = supplierTQRDCInfoDao.loadSupplierBaseInfos();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBatchCreate() {
        Integer createSize = supplierTQRDCInfoDao.batchCreate(Lists.newArrayList(supplierTQRDCInfo, supplierTQRDCInfo));
    }

    @Test
    public void testFindUserIdsByMonthAndStrategy() {
        List<Long> userIds = supplierTQRDCInfoDao.findUserIdsByMonthAndStrategy(supplierTQRDCInfo.getMonth(), 3, Arrays.asList(supplierTQRDCInfo.getUserId()));
        Assert.assertEquals(1, userIds.size());
    }

}
