package io.terminus.snz.user.service;

import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.Response;

/**
 * Author:Guo Chaopeng
 * Created on 14-6-21.
 */
public interface DataImportService {

    /**
     * 批量导入供应商的TQRDC信息
     *
     * @param excelPath excel文件的路径
     * @return 导入是否成功
     */
    @Export(paramNames = {"excelPath"})
    public Response<Boolean> bulkImportSupplierTQRDCInfo(String excelPath);

    /**
     * 批量导入供应商
     *
     * @param excelPath 包含供应商信息的excel文件的路径
     * @return 导入的供应商数量
     */
    @Export(paramNames = {"excelPath"})
    public Response<Integer> bulkImportSupplier(String excelPath);

    /**
     * 批量导入每个主营业务的审核人
     *
     * @param excelPath excel文件的路径
     * @return 导入的数量
     */
    @Export(paramNames = {"excelPath"})
    public Response<Integer> bulkImportMainBusinessApprover(String excelPath);


}
