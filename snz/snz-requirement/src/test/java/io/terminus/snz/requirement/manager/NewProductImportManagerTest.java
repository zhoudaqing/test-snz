package io.terminus.snz.requirement.manager;

import com.google.common.collect.Lists;
import io.terminus.snz.requirement.dao.NewProductImportDao;
import io.terminus.snz.requirement.dao.NewProductStepDao;
import io.terminus.snz.requirement.model.NewProductImport;
import io.terminus.snz.requirement.model.NewProductStep;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class NewProductImportManagerTest {

    @InjectMocks
    private NewProductImportManager newProductImportManager;

    @Mock
    private NewProductImportDao newProductImportDao;

    @Mock
    private NewProductStepDao newProductStepDao;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testImportNPI() throws Exception {

        Long importNPI=null;

        NewProductImport newProductImport = new NewProductImport();
        newProductImport = new NewProductImport();
        newProductImport.setId(1001l);
        newProductImport.setSupplierCode("SP1");
        newProductImport.setSupplierName("SN1");
        newProductImport.setDocumentName("1"); //文件
        newProductImport.setModuleNum("1"); //？？？编号
        newProductImport.setOriginalCreationDate(DateTime.now().toDate()); //原件创建时间
        newProductImport.setPurchaseConfirmer("小李"); //采购确认人
        newProductImport.setAssemblyConclusion("1"); //装配结论
        newProductImport.setOutTester("1"); //外检员

        NewProductStep step = NewProductStep.createFrom(newProductImport, null);

        when(newProductStepDao.findByMouldNumberAndSupplierNameAndStep(anyString(), anyString(), anyInt()))
                .thenReturn(step);
        when(newProductImportDao.findByModuleNumAndSupplierName(anyString(), anyString()))
                .thenReturn(Lists.newArrayList(newProductImport));
        importNPI = newProductImportManager.importNPI(newProductImport);

        when(newProductStepDao.findByMouldNumberAndSupplierNameAndStep(anyString(), anyString(), anyInt()))
                .thenReturn(null);
        when(newProductImportDao.findByModuleNumAndSupplierName(anyString(), anyString()))
                .thenReturn(null);
        importNPI = newProductImportManager.importNPI(newProductImport);

        when(newProductStepDao.findByMouldNumberAndSupplierNameAndStep(anyString(), anyString(), anyInt()))
                .thenReturn(step);
        when(newProductImportDao.findByModuleNumAndSupplierName(anyString(), anyString()))
                .thenReturn(null);
        importNPI = newProductImportManager.importNPI(newProductImport);

        when(newProductStepDao.findByMouldNumberAndSupplierNameAndStep(anyString(), anyString(), anyInt()))
                .thenReturn(null);
        when(newProductImportDao.findByModuleNumAndSupplierName(anyString(), anyString()))
                .thenReturn(Lists.newArrayList(newProductImport));
        importNPI = newProductImportManager.importNPI(newProductImport);

    }
}