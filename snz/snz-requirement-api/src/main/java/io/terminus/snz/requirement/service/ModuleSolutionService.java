package io.terminus.snz.requirement.service;

import io.terminus.common.model.Paging;
import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.dto.ModuleQuotationDto;
import io.terminus.snz.requirement.dto.ModuleSolutionDto;
import io.terminus.snz.requirement.model.ModuleQuotation;
import io.terminus.snz.requirement.model.ModuleSolution;

/**
 * Desc:需求中的具体模块的方案信息(包含模块的报价方案信息)
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-05.
 */
public interface ModuleSolutionService {
    /**
     * 创建模块的方案信息
     * @param moduleSolution 模块方案信息
     * @return Boolean
     * 返回模块创建结果
     */
    @Export(paramNames = {"solution"})
    public Response<Boolean> createSolution(ModuleSolution moduleSolution);

    /**
     * 实现批量的创建模块方案
     * @param solutionList  模块方案列表
     * @return  Boolean
     * 返回批量模块方案创建结果
     */
    @Export(paramNames = {"solutionList"})
    public Response<Boolean> batchSolution(String solutionList);

    /**
     * 通过整体的需求方案编号&具体模块需求编号查询是否已创建具体的模块方案
     * @param solutionId    整体方案编号
     * @param moduleId      具体模块要求编号
     * @return  Boolean
     * 返回是否已存在该方案
     */
    @Export(paramNames = {"solutionId", "moduleId"})
    public Response<Boolean> existSolution(Long solutionId , Long moduleId);

    /**
     * 供应商更改模块方案信息
     * @param solution  模块方案
     * @return Boolean
     * 返回更新结果
     */
    @Export(paramNames = {"solution"})
    public Response<Boolean> updateSolution(ModuleSolution solution);

    /**
     * 实现批量的更新模块的方案信息
     * @param moduleSolutions  模块方案信息
     * @return  Boolean
     * 返回更新结果
     */
    @Export(paramNames = {"moduleSolutions"})
    public Response<Boolean> batchUpdateSolution(String moduleSolutions);

    /**
     * 通过模块方案编号查询详细的方案信息
     * @param id    模块方案编号
     * @return  ModuleSolution
     * 返回模块方案详细信息
     */
    @Export(paramNames = {"id"})
    public Response<ModuleSolution> findById(Long id);

    /**
     * 通过整体方案编号查询需求下的所有模块信息(这个是从供应商纬度去查询提交的模块信息内容)
     * @param user          方案的创建者
     * @param requirementId 需求编号
     * @param status        状态标志位（1:显示全部, 2:未提交的模块方案，3:已提交的模块方案，）
     * @param pageNo        当前页数（默认为0）
     * @param size          分页大小（默认为10）
     * @return Paging
     * 返回封装好的分页数据
     */
    @Export(paramNames = {"user","requirementId","status","pageNo","size"})
    public Response<Paging<ModuleSolutionDto>> findModules(BaseUser user , Long requirementId, Integer status, Integer pageNo,Integer size);

    /**
     * 通过方案编号显示模块方案的信息（添加分页操作）
     * @param solutionId    方案编号
     * @param pageNo        当前页数（默认为0）
     * @param size          分页大小（默认为10）
     * @return  Paging
     * 返回封装好的分页数据
     */
    @Export(paramNames = {"solutionId" , "pageNo", "size"})
    public Response<Paging<ModuleSolution>> findSolutionByPurchaser(Long solutionId , Integer pageNo, Integer size);



    /*****************  模块的报价处理  **************/
    /**
     * 创建模块的报价方案信息
     * @param moduleQuotation 模块报价方案信息
     * @return Boolean
     * 返回模块报价方案创建结果
     */
    @Export(paramNames = {"moduleQuotation"})
    public Response<Boolean> createQuotation(ModuleQuotation moduleQuotation);

    /**
     * 实现批量创建的模块的报价方案
     * @param quotationList  模块报价方案列表
     * @param user           用户信息
     * @return  Boolean
     * 返回批量模块报价方案创建结果
     */
    @Export(paramNames = {"quotationList" , "user"})
    public Response<Boolean> batchQuotation(String quotationList , BaseUser user);

    /**
     * 通过整体的需求方案编号&具体模块需求编号查询是否已创建具体的模块报价方案
     * @param solutionId    整体方案编号
     * @param moduleId      具体模块要求编号
     * @return  Boolean
     * 返回是否已存在该模块报价方案
     */
    @Export(paramNames = {"solutionId","moduleId"})
    public Response<Boolean> existQuotation(Long solutionId , Long moduleId);

    /**
     * 供应商更改模块报价方案信息
     * @param moduleQuotation  模块报价方案
     * @return Boolean
     * 返回更新结果
     */
    @Export(paramNames = {"moduleQuotation"})
    public Response<Boolean> updateQuotation(ModuleQuotation moduleQuotation);

    /**
     * 批量更新模块的报价方案信息
     * @param moduleQuotations  模块报价方案信息
     * @return  Boolean
     * 返回更新结果
     */
    @Export(paramNames = {"moduleQuotations"})
    public Response<Boolean> batchUpdateQuotation(String moduleQuotations);

    /**
     * 通过模块报价方案编号查询详细的方案信息
     * @param quotationId    模块报价方案编号
     * @return  ModuleQuotation
     * 返回模块报价方案详细信息
     */
    @Export(paramNames = {"quotationId"})
    public Response<ModuleQuotation> findByQuotationId(Long quotationId);

    /**
     * 通过整体方案编号查询需求下的所有模块信息(这个是从供应商纬度去查询提交的模块信息内容)
     * @param user          方案的创建者
     * @param requirementId 需求编号
     * @param status     状态标志位（1:显示全部 2:未提交的模块报价方案, 3:已提交的模块报价方案）
     * @param pageNo     当前页数（默认为0）
     * @param size       分页大小（默认为10）
     * @return Paging
     * 返回封装好的分页数据
     */
    @Export(paramNames = {"user", "requirementId","status","pageNo", "size"})
    public Response<Paging<ModuleQuotationDto>> findQuotations(BaseUser user , Long requirementId, Integer status, Integer pageNo, Integer size);

    /**
     * 通过方案编号显示模块报价方案的信息（添加分页操作）
     * @param solutionId    方案编号
     * @param pageNo        当前页数（默认为0）
     * @param size          分页大小（默认为10）
     * @return  Paging
     * 返回封装好的分页数据
     */
    @Export(paramNames = {"solutionId" , "pageNo", "size"})
    public Response<Paging<ModuleQuotation>> findQuotationByPurchaser(Long solutionId , Integer pageNo, Integer size);

    /**
     * 根据用户信息以及需求编号更新用户的报价单文档信息
     * @param user          登入用户信息
     * @param requirementId 需求编号
     * @param quotationFile 报价单文档
     * @return Boolean
     * 返回更新报价单信息是否成功
     */
    @Export(paramNames = {"user", "requirementId", "quotationFile"})
    public Response<Boolean> updateQuotationFile(BaseUser user , Long requirementId, String quotationFile);
}
