package io.terminus.snz.user.tool;

import io.terminus.snz.user.dao.AddressDao;
import io.terminus.snz.user.dto.SupplierImportDto;
import io.terminus.snz.user.model.MainBusinessApprover;
import io.terminus.snz.user.model.SupplierTQRDCInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

/**
 * Author:Guo Chaopeng
 * Created on 14-8-5.
 */
public class ExcelTransformTest {

    @Mock
    private AddressDao addressDao;

    @Mock
    private AnalyzeExcelUtil analyzeExcelUtil;

    @InjectMocks
    private ExcelTransform excelTransform;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetSupplierTQRDCInfo() {
        String path = "";
        List<SupplierTQRDCInfo> supplierTQRDCInfos = excelTransform.getSupplierTQRDCInfo(path);
        Assert.assertNotNull(supplierTQRDCInfos);
    }

    @Test
    public void testGetMainBusinessApprover() {
        String path = "";
        List<MainBusinessApprover> mainBusinessApprovers = excelTransform.getMainBusinessApprover(path);
        Assert.assertNotNull(mainBusinessApprovers);
    }

    @Test
    public void testGetSupplier() {
        String path = "";
        List<SupplierImportDto> supplierImportDtos = excelTransform.getSupplier(path);
        Assert.assertNotNull(supplierImportDtos);
    }

}
