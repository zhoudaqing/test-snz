package io.terminus.snz.requirement.service;

import io.terminus.common.model.Paging;
import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.dto.DerivativeDto;
import io.terminus.snz.requirement.dto.ModuleDetailDto;
import io.terminus.snz.requirement.dto.ModuleInfoDto;
import io.terminus.snz.requirement.dto.ModulesDto;
import io.terminus.snz.requirement.model.IdentifyName;
import io.terminus.snz.requirement.model.Module;
import io.terminus.snz.requirement.dto.DerivativeDto;

import java.util.List;

/**
 * Desc:提供具体需求的模块信息更改处理（考虑到效率问题，就直接将模块的信息更改提出来，实现异步的update）
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-05.
 */

public interface ModuleService {

    /**
     * 根据模块的详细信息创建模块信息(增加模块工厂配额信息)
     * @param moduleInfoDto 模块详细信息
     * @return  Boolean
     * 返回模块创建是否成
     */
    @Export(paramNames = {"moduleInfoDto"})
    public Response<Boolean> createModule(ModuleInfoDto moduleInfoDto);

    /**
     * 更新需求时->异步更新模块信息
     *
     * @param moduleInfoDto 模块信息
     * @return 返回更新结果
     */
    @Export(paramNames = {"moduleInfoDto"})
    public Response<Boolean> update(ModuleInfoDto moduleInfoDto);

    /**
     * 更新需求时->异步删除模块信息
     *
     * @param moduleId 具体模块需求编号
     * @return Boolean
     * 返回删除结果
     */
    @Export(paramNames = {"moduleId"})
    public Response<Boolean> delete(Long moduleId);

    /**
     * 获取所有的认证信息
     * @return  List
     * 返回认证信息
     */
    @Export
    public Response<List<IdentifyName>> findAllIdentify();

    /**
     * 统计每个模块的工厂的选取供应商的数量
     * @param requirementId 需求编号
     * @return  Boolean
     * 返回统计选取的供应商数量是否成功
     */
    @Export(paramNames = {"requirementId"})
    public Response<Boolean> countSelectNum(Long requirementId);

    /**
     * 通过需求编号查询需求下面的所有具体模块的信息（有分页）
     *
     * @param requirementId 需求编号
     * @param pageNo        当前页数（默认为0）
     * @param size          分页大小（默认为20）
     * @return Paging
     * 返回分页数据信息
     */
    @Export(paramNames = {"requirementId", "pageNo", "size"})
    public Response<ModulesDto> findByParams(Long requirementId, Integer pageNo, Integer size);

    /**
     * 通过需求编号获取详细的模块信息（有分页）
     *
     * @param requirementId 需求编号
     * @param pageNo        当前页数（默认为0）
     * @param size          分页大小（默认为20）
     * @return Paging
     * 返回分页数据信息
     */
    @Export(paramNames = {"requirementId", "moduleType", "moduleId", "pageNo", "size"})
    public Response<Paging<ModuleDetailDto>> findDetailByParams(Long requirementId, Integer moduleType, Long moduleId, Integer pageNo, Integer size);

    /**
     * 用于外部对接的处理（让外部系统通过http访问返回对方json数据，现在只有需求创建者才能调用）
     * //todo 后期可能会和haier的hr系统绑定id关系，到时候在处理
     * @param requirementId 需求编号
     * @param creatorId     用户编号（在我们系统上的用户编号）
     * @param pageNo        页面编号
     * @param size          分页数量
     * @return List
     * 返回模块数据列表
     */
    public Response<Paging<Module>> findByRequirementId(Long requirementId , Long creatorId, Integer pageNo, Integer size);

    /**
     * 向模块数据的写入模块专用号（定时操作）
     */
    public Boolean setModuleNum();

    /**
     * 分页查找采购商创建的需求下的模块列表，需求状态为结束投标。
     *
     * Paging 的逻辑示意如下：
     * 可以将一个 requirement 下面所有的 module 称为一个 section，
     * 进行如下分页时，逻辑上的偏移量为 b+1，实际上偏移量对 section a 来说为 2。
     * 在查找时，程序会先将偏移量转换为实际偏移量，再进行分页逻辑，这个过程对调用者透明。
     *
     *                    _ ...               ___ module b
     *                   /                   /
     *                  /_  requirement   a ----- module b+1 <-- logical_offset
     *                 /                     \___ ...
     *                /
     * purchaser 1 -------  requirement a+1 ----- module b+n <-- logical_limit
     *
     * @param  purchaserId    采购商的id
     * @param  pageNo         页面编号
     * @param  size           每页的数量
     * @return Paging<Module> 分页后的Module列表
     *
     */
    public Response<Paging<ModulesDto>> pagingByPurchaserId(Long purchaserId, Integer pageNo, Integer size);

    /**
     * 定时从 mw_old_module_infos 拿旧品数据
     * @return
     */
    public Response<Boolean> writeOldModule();


    /**
     * 根据模块号查询模块信息
     * @param moduleNum 模块号
     * @return 模块信息
     */
    @Export(paramNames = {"moduleNum"})
    public Response<Module> findModuleByModuleNum(String moduleNum);

    /**
     * 根据物料号查询老品基本信息
     * @param moduleNum  物料号
     * @return 模块信息列表
     */
    @Export(paramNames = {"moduleNum"})
    public Response<DerivativeDto> queryOldModule(String moduleNum);

    /**
     * 根据基准号(老品)信息生成衍生号信息
     * @param derivativeDto  新品老品数据
     * @return true or false
     */
    @Export(paramNames = {"derivativeDto"})
    public Response<Boolean> createDeriveModule(DerivativeDto derivativeDto);

}
