package io.terminus.snz.requirement.service;

import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.dto.QuotationDetailDto;
import io.terminus.snz.requirement.model.ModuleQuotation;

/**
 * Desc:需求的谈判逻辑服务
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-09-05.
 */
public interface RequirementTransactService {
    /**
     * 采购商角度查询需求入选的所有供应商的模块的报价详细数据信息
     * @param requirementId 需求编号
     * @param queryType     状态标志位（1:已修改的谈判价格，2:未提交的谈判价格，3:显示全部）
     * @param pageNo        当前页数（默认为0）
     * @param size          分页大小（默认为10）
     * @return Paging
     * 返回入选的供应商的报价数据信息
     */
    @Export(paramNames = {"requirementId" , "queryType", "pageNo", "size"})
    public Response<QuotationDetailDto> findSupplierQuota(Long requirementId , Integer queryType, Integer pageNo, Integer size);

    /**
     * 这个是从供应商纬度去查询模块的谈判价格的内容
     * @param user          方案的创建者
     * @param requirementId 需求编号
     * @param status        状态标志位（1:已修改的谈判价格，2:未提交的谈判价格，3:显示全部）
     * @param pageNo        当前页数（默认为0）
     * @param size          分页大小（默认为10）
     * @return Paging
     * 返回封装好的分页数据
     */
    @Export(paramNames = {"user","requirementId","status","pageNo","size"})
    public Response<QuotationDetailDto> findTransactQuota(BaseUser user , Long requirementId, Integer status, Integer pageNo,Integer size);

    /**
     * 更新模块的谈判价格信息
     * @param moduleQuotation 模块谈判价格
     * @return Boolean
     * 返回谈判价格是否成功
     */
    @Export(paramNames = {"moduleQuotation"})
    public Response<Boolean> updateTransactPrice(ModuleQuotation moduleQuotation);

    /**
     * 批量更新模块的谈判价格信息
     * @param moduleQuotations 多个模块谈判价格
     * @return Boolean
     * 返回谈判价格是否成功
     */
    @Export(paramNames = {"moduleQuotations"})
    public Response<Boolean> updateBatchTransactPrice(String moduleQuotations);

    /**
     * 批量更新需求的谈判文件
     * @param requirementId 需求编号
     * @param negotiateFile 谈判文件
     * @param user          处理用户
     * @return  Boolean
     * 返回谈判文件上传是否成功
     */
    @Export(paramNames = {"requirementId" , "negotiateFile", "user"})
    public Response<Boolean> uploadNegotiateFile(Long requirementId , String negotiateFile, BaseUser user);

    /*
     * 当需求处于方案终投&属于谈判阶段的时候需要上传谈判文档
     * @param requirementId 需求编号
     * @param transactFile  上传文档
     * @param user 操作用户
     * @return  Boolean
     * 上传是否成功
     */
    @Export(paramNames = {"requirementId" , "transactFile", "user"})
    public Response<Boolean> uploadSolEndFile(Long requirementId , String transactFile, BaseUser user);
}
