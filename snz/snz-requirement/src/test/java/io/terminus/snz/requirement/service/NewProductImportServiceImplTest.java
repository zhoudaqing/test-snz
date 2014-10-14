package io.terminus.snz.requirement.service;

import com.google.common.collect.Lists;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.dao.NewProductImportDao;
import io.terminus.snz.requirement.dao.NewProductStepDao;
import io.terminus.snz.requirement.dto.NewProductStepsDto;
import io.terminus.snz.requirement.manager.NewProductImportManager;
import io.terminus.snz.requirement.model.NewProductImport;
import io.terminus.snz.requirement.model.NewProductStep;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class NewProductImportServiceImplTest{

    @InjectMocks
    private NewProductImportServiceImpl newProductImportService;

    @Mock
    private NewProductImportManager newProductImportManager;

    @Mock
    private NewProductImportDao newProductImportDao;

    @Mock
    private NewProductStepDao stepDao;

    @Mock
    private NewProductImport newProductImport;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        newProductImport = new NewProductImport();
        newProductImport.setId(1l); //自增主键
        newProductImport.setDocumentName("1"); //文件
        newProductImport.setModuleId(1l);   //设置模块内部编号
        newProductImport.setModuleNum("1"); //设置模块海尔内部编号
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
    }

    @Test
    public void testCreate() throws Exception {

        Response<Long> response = null;

        when(newProductImportManager.importNPI(Matchers.<NewProductImport>anyObject())).thenReturn(1l);

        newProductImport = new NewProductImport();
        newProductImport.setSupplierCode("SP1");
        newProductImport.setSupplierName("SN1");
        newProductImport.setDocumentName("1"); //文件
        newProductImport.setModuleId(1l);
        newProductImport.setModuleNum("1"); //？？？编号
        newProductImport.setOriginalCreationDate(DateTime.now().toDate()); //原件创建时间
        newProductImport.setPurchaseConfirmer("小李"); //采购确认人
        newProductImport.setAssemblyConclusion("1"); //装配结论
        newProductImport.setOutTester("1"); //外检员
        response = newProductImportService.create(newProductImport);

        newProductImport = new NewProductImport();
        newProductImport.setSupplierCode("SP1");
        newProductImport.setSupplierName("SN1");
        newProductImport.setDocumentName("1"); //文件
        newProductImport.setModuleNum("1"); //？？？编号
        newProductImport.setOriginalCreationDate(DateTime.now().toDate()); //原件创建时间
        newProductImport.setPrototypeSendDate(DateTime.now().plusDays(10).toDate()); //原件发布时间
        newProductImport.setPurchaseConfirmer("小李"); //采购确认人
        newProductImport.setOutTester("1"); //外检员
        newProductImport.setTestSampleReceiver("1"); //检测收样人
        response = newProductImportService.create(newProductImport);

        newProductImport = new NewProductImport();
        newProductImport.setSupplierCode("SP1");
        newProductImport.setSupplierName("SN1");
        newProductImport.setDocumentName("1"); //文件
        newProductImport.setModuleNum("1"); //？？？编号
        newProductImport.setOriginalCreationDate(DateTime.now().toDate()); //原件创建时间
        newProductImport.setPrototypeSendDate(DateTime.now().toDate()); //原件发布时间
        newProductImport.setPurchaseConfirmer("小李"); //采购确认人
        newProductImport.setPurchaseConfirmDate(null); //采购确认时间
        newProductImport.setSampleFinishDate(DateTime.now().plusDays(20).toDate()); //样机完成时间
        newProductImport.setOutTester("1"); //外检员
        response = newProductImportService.create(newProductImport);

        newProductImport = new NewProductImport();
        newProductImport.setSupplierCode("SP1");
        newProductImport.setSupplierName("SN1");
        newProductImport.setDocumentName("1"); //文件
        newProductImport.setModuleNum("1"); //？？？编号
        newProductImport.setOriginalCreationDate(DateTime.now().toDate()); //原件创建时间
        newProductImport.setPrototypeSendDate(DateTime.now().toDate()); //原件发布时间
        newProductImport.setPurchaseConfirmer("小李"); //采购确认人
        newProductImport.setPurchaseConfirmDate(null); //采购确认时间
        newProductImport.setSampleFinishDate(DateTime.now().toDate()); //样机完成时间
        newProductImport.setAssemblyFinishDate(DateTime.now().toDate()); //装配完成时间
        newProductImport.setAssemblyConclusion("1"); //装配结论
        newProductImport.setOutTester("1"); //外检员
        newProductImport.setSampleReceiveDate(DateTime.now().plusDays(30).toDate()); //收样确认时间
        response = newProductImportService.create(newProductImport);

        newProductImport = new NewProductImport();
        newProductImport.setSupplierCode("SP1");
        newProductImport.setSupplierName("SN1");
        newProductImport.setDocumentName("1"); //文件
        newProductImport.setModuleNum("1"); //？？？编号
        newProductImport.setOriginalCreationDate(DateTime.now().toDate()); //原件创建时间
        newProductImport.setPrototypeSendDate(DateTime.now().toDate()); //原件发布时间
        newProductImport.setPurchaseConfirmer("小李"); //采购确认人
        newProductImport.setPurchaseConfirmDate(DateTime.now().toDate()); //采购确认时间
        newProductImport.setSampleFinishDate(DateTime.now().toDate()); //样机完成时间
        newProductImport.setAssemblyFinishDate(DateTime.now().toDate()); //装配完成时间
        newProductImport.setAssemblyConclusion("1"); //装配结论
        newProductImport.setOutTester("1"); //外检员
        newProductImport.setSampleReceiveDate(DateTime.now().plusDays(40).toDate()); //收样确认时间
        response = newProductImportService.create(newProductImport);

//        when(newProductImportDao.create(newProductImport)).thenReturn(1L);
//        Assert.assertTrue("The new created id must be:[1]", response.getResult()==1);
    }


    @Test
    public void testFindById() throws Exception {
        when(newProductImportDao.findById(1L)).thenReturn(newProductImport);
        Response<NewProductImport> findByIdResp = newProductImportService.findById(1L);
        Assert.assertTrue("The found object's id must be 1" ,
                          findByIdResp.getResult().getId()==1);
    }


    @Test
    public void testFindByIds() throws Exception {
        when(newProductImportDao.findByIds(Lists.newArrayList(1l)))
            .thenReturn(Lists.newArrayList(newProductImport));
        Response<List<NewProductImport>> findByIdsResp = newProductImportService.findByIds(Lists.newArrayList(1l));
        Assert.assertTrue("The found list must by at least one record",
                findByIdsResp.getResult().size()==1 &&
                findByIdsResp.getResult().get(0).getId()==1);

    }


    @Test
    public void testFindByModuleNumber(){
        List<NewProductImport> newProductImports = Lists.newArrayList(newProductImport);
        when(newProductImportDao.findByModuleNum(anyString())).thenReturn(newProductImports);
        Response<List<NewProductImport>> byMouldNumberResp = newProductImportService.findByModuleNum(anyString());

    }


    @Test
    public void findNPIStepsByMouldNumberTest(){

        List<NewProductStep> steps = Lists.newArrayList();

        NewProductStep step = new NewProductStep();
        step.setStep(1);
        step.setModuleNum("1");
        step.setSupplierName("SN1");
        step.setSupplierCode("SC1");
        step.setDatetime(DateTime.now().toDate());
        step.setRealDatetime(DateTime.now().toDate());
        step.setDuration(8);
        step.setStatus(0);
        step.setInCharge("小李");
        steps.add(step);

        when(stepDao.findByModuleNum(anyString())).thenReturn(steps);
        Response<List<NewProductStepsDto>> npiStepsByMouldNumberResp = newProductImportService.findNPIStepsByModuleNum("1");
        System.err.println(npiStepsByMouldNumberResp.getResult());

    }


    @Test
    public void testUpdate() throws Exception {
        when(newProductImportDao.update(newProductImport)).thenReturn(1);
        newProductImport.setId(1l); //自增主键
        newProductImport.setDocumentName("1"); //文件
        newProductImport.setModuleNum("1"); //？？？编号
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
        Response<Integer> updateResp = newProductImportService.update(newProductImport);
        Assert.assertTrue("At least one record must be updated", updateResp.getResult()==1);
    }

    @Test
    public void testDeleteByIds() throws Exception {
        when(newProductImportDao.deleteByIds(Lists.newArrayList(1l))).thenReturn(1);
        Response<Integer> deleteResponse = newProductImportService.deleteByIds(Lists.newArrayList(1l));
        Assert.assertTrue("At least one record must be deleted", deleteResponse.getResult()==1);
    }
}
