package io.terminus.snz.user.service;

import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.dto.SupplierImportDto;
import io.terminus.snz.user.manager.AccountManager;
import io.terminus.snz.user.model.MainBusinessApprover;
import io.terminus.snz.user.model.SupplierTQRDCInfo;
import io.terminus.snz.user.tool.ExcelTransform;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author:Guo Chaopeng
 * Created on 14-6-21.
 */
@Slf4j
@Service
public class DataImportServiceImpl implements DataImportService {

    @Autowired
    private ExcelTransform excelTransform;

    @Autowired
    private AccountManager accountManager;

    @Override
    public Response<Boolean> bulkImportSupplierTQRDCInfo(String excelPath) {
        Response<Boolean> result = new Response<Boolean>();

        if (Strings.isNullOrEmpty(excelPath)) {
            log.error("excel path can not be null");
            result.setError("excel.path.not.null.fail");
            return result;
        }

        try {
            List<SupplierTQRDCInfo> supplierTQRDCInfos = excelTransform.getSupplierTQRDCInfo(excelPath);
            if (supplierTQRDCInfos == null) {
                log.error("supplier TQRDC information not found");
                result.setError("supplier.tqrdc.not.found");
                return result;
            }

            accountManager.bulkCreateSupplierQTRDCInfo(supplierTQRDCInfos);

            result.setResult(Boolean.TRUE);
            return result;
        } catch (Exception e) {
            log.error("fail to import supplier TQRDC information where excel url={},cause:{}", excelPath, Throwables.getStackTraceAsString(e));
            result.setError("import.supplier.tqrdc.info.fail");
            return result;
        }

    }

    @Override
    public Response<Integer> bulkImportSupplier(String excelPath) {

        Response<Integer> result = new Response<Integer>();

        if (Strings.isNullOrEmpty(excelPath)) {
            log.error("excel path can not be null");
            result.setError("excel.path.not.null.fail");
            return result;
        }

        try {
            List<SupplierImportDto> supplierImportDtos = excelTransform.getSupplier(excelPath);
            if (supplierImportDtos == null) {
                log.error("suppliers not found");
                result.setError("suppliers.not.found");
                return result;
            }

            Integer count = accountManager.bulkCreateSupplier(supplierImportDtos);

            result.setResult(count);
            return result;
        } catch (Exception e) {
            log.error("fail to import supplier where excel path={},cause:{}", excelPath, Throwables.getStackTraceAsString(e));
            result.setError("import.supplier.fail");
            return result;
        }

    }

    @Override
    public Response<Integer> bulkImportMainBusinessApprover(String excelPath) {

        Response<Integer> result = new Response<Integer>();

        if (Strings.isNullOrEmpty(excelPath)) {
            log.error("excel path can not be null");
            result.setError("excel.path.not.null.fail");
            return result;
        }

        try {
            List<MainBusinessApprover> mainBusinessApprovers = excelTransform.getMainBusinessApprover(excelPath);
            if (mainBusinessApprovers == null || mainBusinessApprovers.isEmpty()) {
                log.error("approvers not found");
                result.setError("approvers.not.found");
                return result;
            }

            Integer count = accountManager.bulkCreateMainBusinessApprover(mainBusinessApprovers);

            result.setResult(count);
            return result;
        } catch (Exception e) {
            log.error("fail to import approver where excel path={},cause:{}", excelPath, Throwables.getStackTraceAsString(e));
            result.setError("import.approver.fail");
            return result;
        }

    }

}
