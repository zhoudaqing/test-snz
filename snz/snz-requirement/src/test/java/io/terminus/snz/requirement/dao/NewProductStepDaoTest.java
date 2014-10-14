package io.terminus.snz.requirement.dao;

import com.google.common.collect.ImmutableList;
import io.terminus.snz.requirement.model.NewProductStep;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class NewProductStepDaoTest extends  BasicTest{

    @Autowired
    private NewProductStepDao newProductStepDao;

    private Long createdId = null;

    private NewProductStep newProductStep = null;

    @Before
    public void setUp() throws Exception {
        newProductStep = new NewProductStep();
        newProductStep.setId(1l); //自增主键
        newProductStep.setParentId(1l); //父ID
        newProductStep.setModuleNum("模块号-1"); //模块号
        newProductStep.setSupplierCode("供应商代码-1"); //供应商代码
        newProductStep.setSupplierName("供应商名称-1"); //供应商名称
        newProductStep.setStep(1); //流程节点(1:原件创建 | 2:原件创建 | 3:装配完成 | 4:测试样品 | 5:检测开始 | 6:检测计划时间 | 7:检测完成)
        newProductStep.setDatetime(DateTime.now().toDate()); //计划时间或日期
        newProductStep.setDuration(1); //周期
        newProductStep.setRealDatetime(DateTime.now().toDate()); //时间进度时间
        newProductStep.setStatus(1); //状态(-1:延期 | 0:正常 | 1:超前)
        newProductStep.setCreatedAt(DateTime.now().toDate()); //创建时间
        newProductStep.setUpdatedAt(DateTime.now().toDate()); //
        createdId = newProductStepDao.create(newProductStep);
    }

    @Test
    public void testCreate() throws Exception {
        createdId = newProductStepDao.create(newProductStep);
        Assert.assertTrue("One record must be created", createdId>=1);
    }

    @Test
    public void testFindById() throws Exception {
        NewProductStep byId = newProductStepDao.findById(createdId);
        Assert.assertTrue("The requery result's id must equals:"+createdId, byId!=null && byId.getId().equals(createdId));
    }


    @Test
    public void testFindByIds() throws Exception {
        List<NewProductStep> byIds = newProductStepDao.findByIds(ImmutableList.of(createdId));
        Assert.assertTrue("One record must be selectd"+createdId, byIds!=null && byIds.size()==1);
    }


    @Test
    public void testFindByMouldNumberAndSupplierName(){

        String moduleNum = "模块号-1"; //模块号
        String supplierName = "供应商名称-1"; //供应商名称

        Integer offset=0, limit=1000;

        List<NewProductStep> newProductStepList = newProductStepDao.findByModuleNumAndSupplierName(moduleNum, supplierName);

        //Assert.assertTrue("", newProductStepList.getData()!=null);

    }


    @Test
    public void testFindByMouldNumberAndSupplierCodeAndStep(){

        String mouldNumber = "模块号-1"; //模块号
        String supplierCode = "供应商代码-1"; //供应商代码
        Integer step = 1;
        NewProductStep newProductStepList = newProductStepDao.findByMouldNumberAndSupplierNameAndStep(mouldNumber, supplierCode, step);

        //Assert.assertTrue("", newProductStepList.getData()!=null);
    }


    @Test
    public void testUpdate() throws Exception {
        NewProductStep newProductStep = new NewProductStep();
        newProductStep.setId(createdId);
        newProductStep.setParentId(1l); //父ID
        newProductStep.setModuleNum("模块号-1-new"); //模块号
        newProductStep.setSupplierCode("供应商代码-1-new"); //供应商代码
        newProductStep.setSupplierName("供应商名称-1-new"); //供应商名称
        newProductStep.setStep(1001); //流程节点(1:原件创建 | 2:原件创建 | 3:装配完成 | 4:测试样品 | 5:检测开始 | 6:检测计划时间 | 7:检测完成)
        newProductStep.setDatetime(DateTime.now().toDate()); //计划时间或日期
        newProductStep.setDuration(1001); //周期
        newProductStep.setRealDatetime(DateTime.now().toDate()); //时间进度时间
        newProductStep.setStatus(1001); //状态(-1:延期 | 0:正常 | 1:超前)
        newProductStep.setCreatedAt(DateTime.now().toDate()); //创建时间
        newProductStep.setUpdatedAt(DateTime.now().toDate()); //
        boolean updated = newProductStepDao.update(newProductStep);
        Assert.assertTrue("One record must be updated", updated);
    }


    @Test
    public void testDeleteByIds() throws Exception {
        int deleted = newProductStepDao.deleteByIds(ImmutableList.of(createdId));
        Assert.assertTrue("One record must be deleted", deleted==1);
    }

    /**
     *
     * Method: findByParentIdAndStep(Long parentId, Integer step)
     *
     */
    @Test
    public void testFindByParentIdAndStep() throws Exception {
        newProductStepDao.findByParentIdAndStep(1l, 1);
    }


}