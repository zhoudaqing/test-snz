package io.terminus.snz.requirement.service;

import io.terminus.common.model.Paging;
import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.dto.CheckSolEndDto;
import io.terminus.snz.requirement.dto.SupplierSolutionDto;
import io.terminus.snz.requirement.model.Requirement;
import io.terminus.snz.requirement.model.RequirementSolution;

import java.util.List;

/**
 * Desc:方案处理接口（包含整体需求&模块需求）
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-05.
 */
public interface RequirementSolutionService {
    /**
     * 供应商创建整体的需求方案
     * @param solution  需求方案
     * @param user      供应商用户
     * @return  Boolean
     * 返回整体需求方案创建的结果
     * （每一个供应商对于同一个需求只能提交一个需求）
     */
    @Export(paramNames = {"solution" , "user"})
    public Response<Boolean> createSolution(RequirementSolution solution , BaseUser user);

    /**
     * 通过需求编号以及供应商编号，查询供应商是否已经对改需求创建了方案
     * @param requirementId    需求编号
     * @param supplierId    供应商编号
     * @return RequirementSolution
     * 返回供应商对应的需求方案（唯一的方案）
     * （原则：一个供应商只能有一个方案）
     */
    @Export(paramNames = {"requirementId", "supplierId"})
    public Response<RequirementSolution> existSolution(Long requirementId , Long supplierId);

    /**
     * 供应商更改整体的需求方案信息
     * @param solution  需求方案
     * @return Boolean
     * 返回更新结果
     */
    @Export(paramNames = {"solution"})
    public Response<Boolean> updateSolution(RequirementSolution solution);

    /**
     * 用于保存用户的签订协议（当需求交互时就需要签订保密协议，当用户创建了需求就默认为已经签订保密协议了）
     * @param requirementId 需求编号
     * @param signType      签订状态(null:表示查询状态，1:确定同意状态)
     * @param user          操作用户对象
     * @return Boolean
     * 返回是否已签订协议
     */
    @Export(paramNames = {"requirementId" , "signType", "user"})
    public Response<Boolean> signSecrecy(Long requirementId , Integer signType, BaseUser user);

    /**
     * 向供应商对应的需求方案发送一个topic话题
     * @param solutionId    方案编号
     * @param topicId       话题编号
     * @return  Boolean
     * 返回创建topic是否成功
     */
    @Export(paramNames = {"solutionId" , "topicId", "user"})
    public Response<Boolean> topicWithSupplier(Long solutionId, Long topicId, BaseUser user);

    /**
     * 查询签订过保密协议的所有供应商名称
     * @param requirementId 需求编号
     * @param pageNo        当前页数（默认为0）
     * @param size          分页大小（默认为20）
     * @return  Paging
     * 返回签订过保密协议的所有需求方案
     */
    @Export(paramNames = {"requirementId" , "pageNo", "size"})
    public Response<Paging<SupplierSolutionDto>> findSignByParam(Long requirementId , Integer pageNo , Integer size);

    /**
     * 查询用户的详细信息
     * @param requirementId 需求编号
     * @param user          供应商用户信息
     * @return  SupplierSolutionDto
     * 返回供应商的详细信息
     */
    @Export(paramNames = {"requirementId" , "user"})
    public Response<SupplierSolutionDto> findSolutionSupplier(Long requirementId , BaseUser user);

    /**
     * 供应商更给整体的承诺信息
     * @param requirementId 需求编号
     * @param acceptInfo    承诺信息
     * @param user          用户信息
     * @return  Boolean
     * 返回更新结果
     */
    @Export(paramNames = {"requirementId" , "acceptInfo", "user"})
    public Response<Boolean> updateSolutionAccept(Long requirementId , String acceptInfo, BaseUser user);

    /**
     * 支持物理删除（无用的数据对于采购商而言）供应商提供的需求方案信息
     * @param solutionId 方案编号
     * @return  Boolean
     * 返回删除结果
     * （需要注意的，删除操作的前提－》当前的需求还未到达5:选定供应商&方案）
     */
    @Export(paramNames = {"solutionId"})
    public Response<Boolean> deleteSolution(Long solutionId);

    /**
     * 通过整体方案编号查询详细的方案信息
     * @param solutionId    方案编号
     * @return  RequirementSolution
     * 返回方案详细信息
     */
    @Export(paramNames = {"solutionId"})
    public Response<RequirementSolution> findById(Long solutionId);

    /**
     * 通过需求编号&用户信息查询详细的需求方案信息
     * @param requirementId 需求编号
     * @param user          用户信息
     * @return  RequirementSolution
     * 返回方案的详细信息
     */
    @Export(paramNames = {"requirementId" , "user"})
    public Response<RequirementSolution> findSolutionBySupplier(Long requirementId , BaseUser user);

    /**
     * 通过当前用户以及需求编号查询该供应商的提交了的承诺信息
     * @param requirementId     需求编号
     * @param user              用户信息
     * @return  String          （null:表示全部承诺，0:表示还未创建方案，not null:表示未全部承诺无法跳到下一个页面）
     * 返回承诺信息
     */
    @Export(paramNames = {"requirementId" , "user"})
    public Response<String> checkAcceptInfo(Long requirementId , BaseUser user);

    /**
     * 通过需求编号以及用户信息查询供应商是否满足提交需求方案的条件
     * @param requirementId 需求编号
     * @param user          用户信息
     * @return  Boolean
     * 返回验证是否通过
     */
    @Export(paramNames = {"requirementId" , "user"})
    public Response<Boolean> checkSupplierInfo(Long requirementId , BaseUser user);

    /**
     * 供应商的方案终投阶段提交最终方案信息
     * @param requirementId    需求编号
     * @param user             用户信息
     * @return  Integer
     * 返回保证金状态（0：未提交，1：提交成功，-1：提交失败，2：交易成功，-2：交易失败）
     */
    @Export(paramNames = {"requirementId" , "user"})
    public Response<CheckSolEndDto> solutionEnd(Long requirementId , BaseUser user);

    /**
     * 查询某个需求下的全部供应商提交的方案信息
     * @param requirementId 需求编号
     * @param status 当前需求所处的阶段
     * @return  List
     * 返回一个方案列表
     */
    @Export(paramNames = {"requirementId" , "status"})
    public Response<List<RequirementSolution>> findAllSolution(Long requirementId , Integer status);

    /**
     * 批量为供应商提供的方案的技术指标进行打分
     * @param solutions  供应商提供的方案列表
     * @return  Boolean
     * 返回对于供应商方案的打分是否成功
     */
    @Export(paramNames = {"solutions"})
    public Response<Boolean> updateBatchTechnology(String solutions);

    /**
     * 实现提交整体方案的文档信息（//todo 后期还会将这些数据处理转换成html格式）
     * @param requirementId 需求编号
     * @param solutionFile  上传的文档内容
     * @return  Boolean
     * 返回创建是否成功
     */
    @Export(paramNames = {"requirementId" , "solutionFile", "user"})
    public Response<Boolean> updateSolutionFile(Long requirementId , String solutionFile, BaseUser user);

    /**
     * 判断某个用户是否有参加过需求（就是判断是否已经同意过保密协议）
     * @param user  用户
     * @return  Boolean
     * 返回是否参加过需求
     */
    @Export(paramNames = {"user"})
    public Response<Boolean> existSolutions(BaseUser user);

    /**
     * 查询供应商的需求信息（需要分页处理）
     * @param user          采购商用户
     * @param qualifyStatus 资质校验状态（-2:资质检查不通过, －1:没有提交, 1:已提交，等待审核, 2:资质检查通过）
     * @param status        需求状态（null：显示除删除状态的全部数据,3:方案交互，4:方案综投，5:选定供应商与方案，6:招标结束）
     * @param reqName       需求名称的模糊查询
     * @param startAt       开始时间
     * @param endAt         结束时间
     * @param pageNo        当前页数（默认为0）
     * @param size          分页大小（默认为20）
     * @return Paging
     */
    @Export(paramNames = {"user", "qualifyStatus", "status", "reqName", "startAt", "endAt", "pageNo", "size"})
    public Response<Paging<Requirement>> findByParams(BaseUser user, Integer qualifyStatus, Integer status, String reqName,
                                                      String startAt, String endAt, Integer pageNo , Integer size);

    /**
     * 通过需求编号&参数查询(这个主要是提供给采购商查询)
     * @param requirementId 需求编号
     * @param statusArray   方案状态 0:签订保密协议 1:待承诺, 2:全部承诺, 3:部分无法承诺, 4:已上传文件, 5:最终提交(查询多个状态类型:1,2,3)
     * @param pageNo        当前页数（默认为0）
     * @param size          分页大小（默认为20）
     * @return  Paging
     * 返回查询到的分页数据信息
     */
    @Export(paramNames = {"requirementId", "statusArray", "pageNo","size"})
    public Response<Paging<RequirementSolution>> findByRequirementId(Long requirementId , String statusArray, Integer pageNo, Integer size);
}
