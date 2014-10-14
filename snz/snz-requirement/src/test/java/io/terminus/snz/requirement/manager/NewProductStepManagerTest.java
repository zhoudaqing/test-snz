package io.terminus.snz.requirement.manager;

import com.google.common.collect.Lists;
import io.terminus.snz.requirement.dao.NewProductStepDao;
import io.terminus.snz.requirement.model.NewProductStep;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class NewProductStepManagerTest {

    @InjectMocks
    private NewProductStepManager newProductStepManager;

    @Mock
    private NewProductStepDao newProductStepDao;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPost() throws Exception {

        NewProductStep newProductStep = new NewProductStep();
        newProductStep.setId(1l); //自增主键
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

        when(newProductStepDao.findByMouldNumberAndSupplierNameAndStep(anyString(), anyString(), anyInt()))
                .thenReturn(newProductStep);
        newProductStepManager.post(Lists.newArrayList(newProductStep));

        when(newProductStepDao.findByMouldNumberAndSupplierNameAndStep(anyString(), anyString(), anyInt()))
                .thenReturn(null);
        newProductStepManager.post(Lists.newArrayList(newProductStep));

    }
}