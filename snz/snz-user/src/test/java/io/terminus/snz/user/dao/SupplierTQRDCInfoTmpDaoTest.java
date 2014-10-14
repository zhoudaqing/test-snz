package io.terminus.snz.user.dao;

import com.google.common.collect.ImmutableList;
import io.terminus.snz.user.model.SupplierTQRDCInfoTmp;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author wanggen
 */
public class SupplierTQRDCInfoTmpDaoTest extends TestBaseDao{

    @Autowired
    private SupplierTQRDCInfoTmpDao supplierTQRDCInfoTmpDao;

    private Long createdId = null;

    private SupplierTQRDCInfoTmp supplierTQRDCInfoTmp = null;

    @Before
    public void setUp() throws Exception {
        supplierTQRDCInfoTmp = new SupplierTQRDCInfoTmp();
        supplierTQRDCInfoTmp.setId(1l); //主键
        supplierTQRDCInfoTmp.setUserId(1l); //用户id
        supplierTQRDCInfoTmp.setCompanyId(1l); //对应企业信息
        supplierTQRDCInfoTmp.setSpecialNumber("专用号-1"); //专用号
        supplierTQRDCInfoTmp.setSupplierCode("供应商代码-1"); //供应商代码
        supplierTQRDCInfoTmp.setSupplierName("供应商名称-1"); //供应商名称
        supplierTQRDCInfoTmp.setModule("模块-1"); //模块
        supplierTQRDCInfoTmp.setProductLineId(1); //产品线ID
        supplierTQRDCInfoTmp.setProductLine("产品线-1"); //产品线
        supplierTQRDCInfoTmp.setLocation("区域-1"); //区域
        supplierTQRDCInfoTmp.setRank("排名-1"); //排名
        supplierTQRDCInfoTmp.setDate("日期-1"); //日期
        supplierTQRDCInfoTmp.setCompositeScore(1); //综合绩效
        supplierTQRDCInfoTmp.setSupplierStatus(1); //供应商状态(0:正常|-1:警告|-2:整改|-3:停新品)
        supplierTQRDCInfoTmp.setTechScore(1); //技术得分
        supplierTQRDCInfoTmp.setDelayDays(1); //拖期天数
        supplierTQRDCInfoTmp.setNewProductPass(1); //新品合格率
        supplierTQRDCInfoTmp.setQualityScore(1); //质量得分
        supplierTQRDCInfoTmp.setLiveBad(1); //现场不良率
        supplierTQRDCInfoTmp.setMarketBad(1); //市场不良率
        supplierTQRDCInfoTmp.setRespScore(1); //响应得分
        supplierTQRDCInfoTmp.setProgramPromisedRate(1); //方案承诺率
        supplierTQRDCInfoTmp.setProgramSelectedRate(1); //方案选中率
        supplierTQRDCInfoTmp.setProgramAdoptedRate(1); //方案落地率
        supplierTQRDCInfoTmp.setRequirementResp(1); //需求响应度
        supplierTQRDCInfoTmp.setDeliverScore(1); //交付得分
        supplierTQRDCInfoTmp.setDeliverDiff(1); //交付差异
        supplierTQRDCInfoTmp.setCostScore(1); //成本得分
        supplierTQRDCInfoTmp.setIncrement(1); //增值
        supplierTQRDCInfoTmp.setTechScoreRank(1); //技术得分排序
        supplierTQRDCInfoTmp.setQualityScoreRank(1); //质量得分排序
        supplierTQRDCInfoTmp.setRespScoreRank(1); //响应得分排序
        supplierTQRDCInfoTmp.setDeliveryScoreRank(1); //交付得分排序
        supplierTQRDCInfoTmp.setAffectDeliveryCount(1); //影响 T-1 交付次数
        supplierTQRDCInfoTmp.setCostScoreRank(1); //成本得分排序
        supplierTQRDCInfoTmp.setPriceReductionAmount(1); //降价额
        supplierTQRDCInfoTmp.setPriceReductionRange(1); //降幅
        supplierTQRDCInfoTmp.setCreatedAt(DateTime.now().toDate()); //
        supplierTQRDCInfoTmp.setUpdatedAt(DateTime.now().toDate()); //
        createdId = supplierTQRDCInfoTmpDao.create(supplierTQRDCInfoTmp);
    }

    @Test
    public void testCreate() throws Exception {
        createdId = supplierTQRDCInfoTmpDao.create(supplierTQRDCInfoTmp);
        Assert.assertTrue("One record must be created", createdId>=1);
    }

    @Test
    public void testFindById() throws Exception {
        SupplierTQRDCInfoTmp byId = supplierTQRDCInfoTmpDao.findById(createdId);
        Assert.assertTrue("The requery result's id must equals:"+createdId, byId!=null && byId.getId().equals(createdId));
    }


    @Test
    public void testFindByIds() throws Exception {
        List<SupplierTQRDCInfoTmp> byIds = supplierTQRDCInfoTmpDao.findByIds(ImmutableList.of(createdId));
        Assert.assertTrue("One record must be selectd"+createdId, byIds!=null && byIds.size()==1);
    }


    @Test
    public void testFindByDate(){

        String date = "日期-1"; //日期

        List<SupplierTQRDCInfoTmp> supplierTQRDCInfoTmpList = supplierTQRDCInfoTmpDao.findByDate(date);
        Assert.assertTrue("At least one record be found", supplierTQRDCInfoTmpList!=null);

    }


    @Test
    public void testUpdate() throws Exception {
        SupplierTQRDCInfoTmp supplierTQRDCInfoTmp = new SupplierTQRDCInfoTmp();
        supplierTQRDCInfoTmp.setId(createdId);
        supplierTQRDCInfoTmp.setUserId(1001l); //用户id
        supplierTQRDCInfoTmp.setCompanyId(1001l); //对应企业信息
        supplierTQRDCInfoTmp.setSpecialNumber("专用号-1-new"); //专用号
        supplierTQRDCInfoTmp.setSupplierCode("供应商代码-1-new"); //供应商代码
        supplierTQRDCInfoTmp.setSupplierName("供应商名称-1-new"); //供应商名称
        supplierTQRDCInfoTmp.setModule("模块-1-new"); //模块
        supplierTQRDCInfoTmp.setProductLineId(1001); //产品线ID
        supplierTQRDCInfoTmp.setProductLine("产品线-1-new"); //产品线
        supplierTQRDCInfoTmp.setLocation("区域-1-new"); //区域
        supplierTQRDCInfoTmp.setRank("排名-1-new"); //排名
        supplierTQRDCInfoTmp.setDate("日期-1-new"); //日期
        supplierTQRDCInfoTmp.setCompositeScore(1001); //综合绩效
        supplierTQRDCInfoTmp.setSupplierStatus(1001); //供应商状态(0:正常|-1:警告|-2:整改|-3:停新品)
        supplierTQRDCInfoTmp.setTechScore(1001); //技术得分
        supplierTQRDCInfoTmp.setDelayDays(1001); //拖期天数
        supplierTQRDCInfoTmp.setNewProductPass(1001); //新品合格率
        supplierTQRDCInfoTmp.setQualityScore(1001); //质量得分
        supplierTQRDCInfoTmp.setLiveBad(1001); //现场不良率
        supplierTQRDCInfoTmp.setMarketBad(1001); //市场不良率
        supplierTQRDCInfoTmp.setRespScore(1001); //响应得分
        supplierTQRDCInfoTmp.setProgramPromisedRate(1001); //方案承诺率
        supplierTQRDCInfoTmp.setProgramSelectedRate(1001); //方案选中率
        supplierTQRDCInfoTmp.setProgramAdoptedRate(1001); //方案落地率
        supplierTQRDCInfoTmp.setRequirementResp(1001); //需求响应度
        supplierTQRDCInfoTmp.setDeliverScore(1001); //交付得分
        supplierTQRDCInfoTmp.setDeliverDiff(1001); //交付差异
        supplierTQRDCInfoTmp.setCostScore(1001); //成本得分
        supplierTQRDCInfoTmp.setIncrement(1001); //增值
        supplierTQRDCInfoTmp.setTechScoreRank(1001); //技术得分排序
        supplierTQRDCInfoTmp.setQualityScoreRank(1001); //质量得分排序
        supplierTQRDCInfoTmp.setRespScoreRank(1001); //响应得分排序
        supplierTQRDCInfoTmp.setDeliveryScoreRank(1001); //交付得分排序
        supplierTQRDCInfoTmp.setAffectDeliveryCount(1001); //影响 T-1 交付次数
        supplierTQRDCInfoTmp.setCostScoreRank(1001); //成本得分排序
        supplierTQRDCInfoTmp.setPriceReductionAmount(1001); //降价额
        supplierTQRDCInfoTmp.setPriceReductionRange(1001); //降幅
        supplierTQRDCInfoTmp.setCreatedAt(DateTime.now().toDate()); //
        supplierTQRDCInfoTmp.setUpdatedAt(DateTime.now().toDate()); //
        int updated = supplierTQRDCInfoTmpDao.update(supplierTQRDCInfoTmp);
        Assert.assertTrue("One record must be updated", updated==1);
    }


    @Test
    public void testDeleteByIds() throws Exception {
        int deleted = supplierTQRDCInfoTmpDao.deleteByIds(ImmutableList.of(createdId));
        Assert.assertTrue("One record must be deleted", deleted==1);
    }

}