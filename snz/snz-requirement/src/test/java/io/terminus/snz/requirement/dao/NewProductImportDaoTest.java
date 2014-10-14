package io.terminus.snz.requirement.dao;

import com.google.common.collect.ImmutableList;
import io.terminus.snz.requirement.model.NewProductImport;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class NewProductImportDaoTest extends BasicTest {

    @Autowired
    private NewProductImportDao newProductImportDao;

    private Long createdId = null;

    private NewProductImport newProductImport = null;

    @Before
    public void setUp() throws Exception {
        newProductImport = new NewProductImport();
        newProductImport.setId(1l); //自增主键
        newProductImport.setSupplierCode("供应商代码-1"); //供应商代码
        newProductImport.setSupplierName("供应商名称-1"); //供应商名称
        newProductImport.setDocumentName("1"); //文件
        newProductImport.setModuleId(1l); //模块系统自增编号
        newProductImport.setModuleNum("1l");//海尔内部物料编号
        newProductImport.setOriginalCreationDate(DateTime.now().toDate()); //原件创建时间
        newProductImport.setPrototypeSendDate(DateTime.now().toDate()); //原件发布时间
        newProductImport.setPurchaseConfirmer("1"); //采购确认人
        newProductImport.setPurchaseConfirmDate(DateTime.now().toDate()); //采购确认时间
        newProductImport.setSampleFinishDate(DateTime.now().toDate()); //样机完成时间
        newProductImport.setAssemblyFinishDate(DateTime.now().toDate()); //装配完成时间
        newProductImport.setAssemblyConclusion("1"); //装配结论
        newProductImport.setOutTester("1"); //外检员
        newProductImport.setSampleReceiveDate(DateTime.now().toDate()); //收样确认时间
        newProductImport.setTestSampleReceiveTime(DateTime.now().toDate()); //检测收样时间
        newProductImport.setTestSampleReceiver("1"); //检测收样人
        newProductImport.setTestStartDate(DateTime.now().toDate()); //检测开始时间
        newProductImport.setTestPlanedDate(DateTime.now().toDate()); //检测计划时间
        newProductImport.setTestEndDate(DateTime.now().toDate()); //检测完成时间
        newProductImport.setTestConclusion("1"); //检测结论
        newProductImport.setFinalConclusion("1"); //最终结论
        newProductImport.setCreatedAt(DateTime.now().toDate()); //创建时间
        newProductImport.setUpdatedAt(DateTime.now().toDate()); //修改时间
        createdId = newProductImportDao.create(newProductImport);
    }

    @Test
    public void testCreate() throws Exception {
        newProductImportDao.create(newProductImport);
        Assert.assertTrue("One record must be created", createdId>=1);
    }

    @Test
    public void testFindById() throws Exception {
        NewProductImport byId = newProductImportDao.findById(createdId);
        Assert.assertTrue("The requery result's id must equals:"+createdId, byId!=null && byId.getId().equals(createdId));
    }


    @Test
    public void testFindByIds() throws Exception {
        List<NewProductImport> byIds = newProductImportDao.findByIds(ImmutableList.of(createdId));
        Assert.assertTrue("One record must be selectd"+createdId, byIds!=null && byIds.size()==1);
    }


    @Test
    public void testFindByMouldNumber(){

        String mouldNumber = "1"; //模块编号

        List<NewProductImport> newProductImportList = newProductImportDao.findByModuleNum(mouldNumber);

        Assert.assertTrue("", newProductImportList!=null);

    }


    @Test
    public void testFindByMouldNumberAndSupplierCode(){

        String mouldNumber = "模块编号-1"; //模块编号
        String supplierCode = "供应商代码-1"; //供应商代码

        Integer offset=0, limit=1000;

        List<NewProductImport> newProductImportList = newProductImportDao.findByModuleNumAndSupplierName(mouldNumber, supplierCode);

        //Assert.assertTrue("", newProductImportList.getData()!=null);

    }


    @Test
    public void testUpdate() throws Exception {
        NewProductImport newProductImport = new NewProductImport();
        newProductImport.setId(createdId);
        newProductImport.setSupplierCode("供应商代码-1-new"); //供应商代码
        newProductImport.setSupplierName("供应商名称-1-new"); //供应商名称
        newProductImport.setDocumentName("11"); //文件
        newProductImport.setModuleId(1l);   //模块自增编号
        newProductImport.setModuleNum("11"); //海尔内部模块编号
        newProductImport.setOriginalCreationDate(DateTime.now().toDate()); //原件创建时间
        newProductImport.setPrototypeSendDate(DateTime.now().toDate()); //原件发布时间
        newProductImport.setPurchaseConfirmer("11"); //采购确认人
        newProductImport.setPurchaseConfirmDate(DateTime.now().toDate()); //采购确认时间
        newProductImport.setSampleFinishDate(DateTime.now().toDate()); //样机完成时间
        newProductImport.setAssemblyFinishDate(DateTime.now().toDate()); //装配完成时间
        newProductImport.setAssemblyConclusion("11"); //装配结论
        newProductImport.setOutTester("11"); //外检员
        newProductImport.setSampleReceiveDate(DateTime.now().toDate()); //收样确认时间
        newProductImport.setTestSampleReceiveTime(DateTime.now().toDate()); //检测收样时间
        newProductImport.setTestSampleReceiver("11"); //检测收样人
        newProductImport.setTestStartDate(DateTime.now().toDate()); //检测开始时间
        newProductImport.setTestPlanedDate(DateTime.now().toDate()); //检测计划时间
        newProductImport.setTestEndDate(DateTime.now().toDate()); //检测完成时间
        newProductImport.setTestConclusion("11"); //检测结论
        newProductImport.setFinalConclusion("11"); //最终结论
        newProductImport.setCreatedAt(DateTime.now().toDate()); //创建时间
        newProductImport.setUpdatedAt(DateTime.now().toDate()); //修改时间
        int updated = newProductImportDao.update(newProductImport);
        Assert.assertTrue("One record must be updated", updated==1);
    }


    @Test
    public void testDeleteByIds() throws Exception {
        int deleted = newProductImportDao.deleteByIds(ImmutableList.of(createdId));
        Assert.assertTrue("One record must be deleted", deleted==1);
    }

}