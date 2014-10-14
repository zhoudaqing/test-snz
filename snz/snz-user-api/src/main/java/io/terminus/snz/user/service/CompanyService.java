package io.terminus.snz.user.service;

import io.terminus.common.model.Paging;
import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.dto.*;
import io.terminus.snz.user.model.*;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * Author:Guo Chaopeng
 * Created on 14-5-4.
 */
public interface CompanyService {
    /**
     * 根据企业基本信息编号更新企业基本信息
     *
     * @param baseUser   当前用户
     * @param companyDto 要更新的信息
     * @return 更新是否成功
     */
    @Export(paramNames = {"baseUser", "companyDto"})
    public Response<Boolean> updateCompany(BaseUser baseUser, CompanyDto companyDto);

    /**
     * 修改三证
     *
     * @param baseUser     登录用户
     * @param paperworkDto 要修改的三证信息
     * @return 修改是否成功
     */
    @Export(paramNames = {"baseUser", "paperworkDto"})
    public Response<Boolean> updatePaperwork(BaseUser baseUser, PaperworkDto paperworkDto);

    /**
     * 根据用户查询三证信息（包含暂存和未审核通过的信息）
     *
     * @param baseUser 当前登录用户
     * @return 三证信息
     */
    @Export(paramNames = {"baseUser"})
    public Response<PaperworkDto> findPaperworkByUser(BaseUser baseUser);

    /**
     * 根据用户编号查询三证信息(包括未审核通过的信息)
     *
     * @param userId 用户编号
     * @return 三证信息
     */
    @Export(paramNames = {"userId"})
    public Response<SupplierUpdatedInfoDto<PaperworkDto>> queryPaperworkByUserId(Long userId);

    /**
     * 根据用户编号查询企业基本信息（包含暂存和未审核通过的信息）
     *
     * @param baseUser 当前用户
     * @return 企业基本信息
     */
    @Export(paramNames = {"baseUser"})
    public Response<CompanyDto> findBaseCompanyByUserId(BaseUser baseUser);

    /**
     * 根据用户编号查询企业基本信息（包含未审核通过的信息）
     *
     * @param userId 用户编号
     * @return 企业基本信息
     */
    @Export(paramNames = {"userId"})
    public Response<SupplierUpdatedInfoDto<CompanyDto>> queryBaseCompanyByUserId(Long userId);

    /**
     * 根据企业编号查询企业信息
     *
     * @param id 企业编号
     * @return 企业信息
     */
    @Export(paramNames = {"id"})
    public Response<Company> findCompanyById(Long id);

    /**
     * 根据用户编号查询企业信息
     *
     * @param userId 用户编号
     * @return 企业信息
     */
    @Export(paramNames = {"userId"})
    public Response<Company> findCompanyByUserId(Long userId);

    /**
     * 根据用户编号查询企业信息（不走缓存）
     *
     * @param userId 用户编号
     * @return 企业信息
     */
    @Export(paramNames = {"userId"})
    public Response<Company> findUnCacheCompanyByUserId(Long userId);

    /**
     * 根据主营业务编号查询主营业务信息
     *
     * @param mainBusinessId 主营业务编号（二级类目编号）
     * @return 主营业务信息
     */
    @Export(paramNames = {"mainBusinessId"})
    public Response<List<CompanyMainBusiness>> findCompanyMainBusiness(Long mainBusinessId);

    /**
     * 根据主营业务编号列表查询公司用户id列表
     *
     * @param mainBusinessIds 主营业务编号（二级类目编号）编号列表
     * @return 对应的公司用户id列表
     */
    @Export(paramNames = {"mainBusinessIds"})
    public Response<List<Long>> findCompanyIdsByMainBusinessIds(List<Long> mainBusinessIds);

    /**
     * 查询供应商的详细信息
     *
     * @param userId 供应商编号
     * @return 详细信息
     */
    @Export(paramNames = {"userId"})
    public Response<DetailSupplierDto> findDetailSupplierByUserId(Long userId);

    /**
     * 根据企业编号集合查询企业信息
     *
     * @param ids 企业编号集合
     * @return 企业信息
     */
    @Export(paramNames = {"ids"})
    public Response<List<Company>> findCompaniesByIds(List<Long> ids);

    /**
     * 根据企业编号更新交互参与数
     *
     * @param companyId 企业编号
     * @return 更新是否成功
     */
    @Export(paramNames = {"companyId"})
    public Response<Boolean> updateParticipateCount(Long companyId);

    /**
     * 创建或更新公司行业排名信息
     *
     * @param baseUser    当前用户
     * @param companyRank 排名信息
     * @return 是否成功
     */
    @Export(paramNames = {"baseUser", "companyRank"})
    public Response<Boolean> createOrUpdateCompanyRank(BaseUser baseUser, CompanyRank companyRank);

    /**
     * 根据用户编号查询企业行业排名信息（包含暂存的信息）
     *
     * @param baseUser 用户
     * @return 行业排名信息
     */
    @Export(paramNames = {"baseUser"})
    public Response<CompanyRank> findCompanyRankByUserId(BaseUser baseUser);

    /**
     * 根据用户编号查询企业行业排名信息
     *
     * @param userId 用户编号
     * @return 行业排名信息
     */
    @Export(paramNames = {"userId"})
    public Response<SupplierUpdatedInfoDto<CompanyRank>> queryCompanyRankByUserId(Long userId);

    /**
     * 创建或者更新企业联系人信息
     *
     * @param baseUser    当前用户
     * @param contactInfo 联系人信息
     * @return 是否成功
     */
    @Export(paramNames = {"baseUser", "contactInfo"})
    public Response<Boolean> createOrUpdateContactInfo(BaseUser baseUser, ContactInfo contactInfo);

    /**
     * 根据用户编号查询企业联系人信息（包含暂存和未审核通过的信息）
     *
     * @param baseUser 用户
     * @return 联系人信息
     */
    @Export(paramNames = {"baseUser"})
    public Response<ContactInfo> findContactInfoByUserId(BaseUser baseUser);

    /**
     * 根据用户编号查询企业联系人信息（包含未审核通过的信息）
     *
     * @param userId 用户编号
     * @return 联系人信息
     */
    @Export(paramNames = {"userId"})
    public Response<SupplierUpdatedInfoDto<ContactInfo>> queryContactInfoByUserId(Long userId);

    /**
     * 创建或更新公司财务信息
     *
     * @param baseUser   当前用户
     * @param financeDto 财务信息
     * @return 是否成功
     */
    @Export(paramNames = {"baseUser", "financeDto"})
    public Response<Boolean> createOrUpdateFinance(BaseUser baseUser, FinanceDto financeDto);

    /**
     * 根据用户编号查询公司财务信息（包含暂存和未审核通过的信息）
     *
     * @param baseUser 用户
     * @return 公司财务信息
     */
    @Export(paramNames = {"baseUser"})
    public Response<FinanceDto> findFinanceByUserId(BaseUser baseUser);

    /**
     * 根据用户编号查询公司财务信息（包含未审核通过的信息）
     *
     * @param userId 用户编号
     * @return 公司财务信息
     */
    @Export(paramNames = {"userId"})
    public Response<SupplierUpdatedInfoDto<FinanceDto>> queryFinanceByUserId(Long userId);

    /**
     * 根据鉴定材料信息编号删除鉴定材料信息
     *
     * @param additionalDocId 鉴定材料信息编号
     * @param baseUser        当前操作用户
     * @return 删除是否成功
     */
    @Export(paramNames = {"additionalDocId", "baseUser"})
    public Response<Boolean> deleteAdditionalDoc(Long additionalDocId, BaseUser baseUser);

    /**
     * 为采购经理或财务展示尚未申请V码的供应商列表
     *
     * @param user         当前登录用户
     * @param supplierName 供应商名字
     * @param pageNo       当前分页
     * @param size         分页大小
     * @return 分页后的供应商
     */
    public Response<Paging<SupplierDto>> findSupplierForMDMQualify(BaseUser user, @Nullable String supplierName,
                                                                   Integer pageNo, Integer size);

    /**
     * 根据条件查询供应商信息
     *
     * @param baseUser     当前登录用户
     * @param type         查询条件，1：入驻待审核，2：修改企业信息待审核，null：前两者
     * @param supplierName 供应商的名称(可选)
     * @param pageNo       页码
     * @param size         返回条数
     * @return 供应商信息
     */
    @Export(paramNames = {"baseUser", "type", "supplierName", "pageNo", "size"})
    public Response<Paging<SupplierDto>> findSupplierBy(BaseUser baseUser, Integer type, String supplierName, Integer pageNo, Integer size);

    /**
     * 根据资质验证状态查询供应商信息
     *
     * @param status 资质验证状态
     * @param nick   供应商昵称（可选）
     * @param pageNo 页码
     * @param size   返回条数
     * @return 供应商信息
     */
    @Export(paramNames = {"status", "nick", "pageNo", "size"})
    Response<Paging<SupplierDto>> findSupplierQualifyBy(Integer status, String nick, Integer pageNo, Integer size);

    /**
     * 检查三证是否过期
     *
     * @param baseUser 用户
     * @return 过期返回true，否则返回false
     */
    @Export(paramNames = {"baseUser"})
    public Response<Boolean> certificateExpired(BaseUser baseUser);

    /**
     * 查询供应商的tqrdc信息
     *
     * @param supplierName        供应商名称
     * @param supplierCode        供应商代码
     * @param month               月份，格式：2014-02
     * @param orderBy             排序字段(综合得分：compositeScore，技术得分：techScore，质量得分：qualityScore，响应得分：respScore，交付得分：deliverScore,成本得分：costScore)
     * @param compositeScoreStart 综合得分开始范围
     * @param compositeScoreEnd   综合得分结束范围
     * @param pageNo              页码
     * @param size                每页的的记录数
     * @return 供应商的tqrdc信息
     */
    @Export(paramNames = {"supplierName", "supplierCode", "month", "module", "orderBy", "compositeScoreStart", "compositeScoreEnd", "pageNo", "size"})
    public Response<Paging<SupplierTQRDCInfo>> findSupplierTQRDCInfoBy(String supplierName, String supplierCode, String month, String module, String orderBy, Integer compositeScoreStart, Integer compositeScoreEnd, Integer pageNo, Integer size);

    /**
     * 查询供应商的tqrdc详细信息
     *
     * @param supplierCode 供应商名称
     * @return 供应商的tqrdc详细信息
     */
    @Export(paramNames = {"supplierCode"})
    public Response<CompanyPerformance> findDetailSupplierTQRDCInfo(String supplierCode);

    /**
     * 查询供应商的tqrdc详细信息
     *
     * @param userId 用户名称
     * @return 供应商的tqrdc详细信息
     */
    @Export(paramNames = {"userId"})
    public Response<CompanyPerformance> findDetailSupplierTQRDCInfoByUserId(Long userId);

    /**
     * 根据供应商编号查询供应商最新月份的tqrdc信息
     *
     * @param userId 供应商编号
     * @return 供应商最新月份的tqrdc信息
     */
    public Response<SupplierTQRDCInfo> findSupplierLastTQRDCInfoByUserId(Long userId);

    /**
     * 获取供应商整体统计信息
     *
     * @return 供应商整体统计信息
     */
    @Export()
    public Response<SupplierTQRDCInfos> findSupplierTQRDCInfos();

    /**
     * 预处理供应商的tqrdc排序（临时使用，待完善）
     *
     * @return 是否成功
     */
    @Export
    public Response<Boolean> setTQRDCRank();

    /**
     * 检查供应商是否已经完善所需信息
     *
     * @param userId 供应商id
     * @return 已完善则返回true，否则返回false
     */
    @Export(paramNames = {"userId"})
    public Response<Boolean> isComplete(Long userId);

    /**
     * 查询所有产品线信息
     *
     * @return 产品线信息
     */
    @Export
    public Response<List<ProductLine>> findAllProductLine();

    /**
     * 根据产品线编号查询产品线信息
     *
     * @param ids 产品线编号
     * @return 产品线信息
     */
    @Export(paramNames = "ids")
    public Response<List<ProductLine>> findProductLineByIds(List<Long> ids);

    /**
     * 获取供应商的当前处于哪个阶段（供应商查询自身）
     *
     * @param baseUser 当前登录的供应商
     * @return 供应商阶段信息
     */
    @Export(paramNames = {"baseUser"})
    public Response<SupplierStepDto> getSupplierStepBySelf(BaseUser baseUser);

    /**
     * 获取供应商的当前处于哪个阶段
     *
     * @param baseUser 当前登录用户 TODO: 参数目前无用
     * @param userId   供应商用户id
     * @return 供应商阶段信息
     */
    @Export(paramNames = {"baseUser", "userId"})
    public Response<SupplierStepDto> getSupplierStep(BaseUser baseUser, Long userId);

    /**
     * 查询被驳回过的供应商信息
     *
     * @param nick   供应商的昵称(可选)
     * @param pageNo 页码
     * @param size   返回条数
     * @return 被驳回过的供应商信息
     */
    @Export(paramNames = {"nick", "pageNo", "size"})
    public Response<Paging<SupplierDto>> findRefusedSuppliers(String nick, Integer pageNo, Integer size);

    /**
     * 录入供应商代码
     *
     * @param companyId    公司编号
     * @param supplierCode 供应商代码
     * @return 是否成功
     */
    public Response<Boolean> addSupplierCode(Long companyId, String supplierCode);

    /**
     * 根据用户id列表获取联系人信息
     *
     * @param ids
     * @return 联系人信息
     */
    public Response<Map<Long, ContactInfo>> findContactInfoByUserIds(List<Long> ids);

    /**
     * 根据用户id列表获取主营业务信息
     *
     * @param ids 用户id列表
     * @return 主营业务信息
     */
    public Response<Map<Long, List<CompanyMainBusiness>>> findMainBussinessByUserIds(List<Long> ids);

    /**
     * 根据用户获取当前用户所对应的主营业务信息
     *
     * @param user 用户
     * @return 主营业务信息
     */
    public Response<List<CompanyMainBusiness>> findMainBussinessByUserId(BaseUser user);

    /**
     * 根据公司编号获取当前公司所对应的主营业务信息
     *
     * @param companyId 公司编号
     * @return 主营业务信息
     */
    public Response<List<CompanyMainBusiness>> findMainBusinessByCompanyId(Long companyId);


    /**
     * 根据供应商 V code 查询供应商信息
     *
     * @param supplierCode 供应商代码
     * @return 供应商公司信息
     */
    @Export(paramNames = {"supplierCode"})
    public Response<Company> findCompanyBySupplierCode(String supplierCode);

    /**
     * 根据供应商 名称模糊 查询供应商信息
     *
     * @param supplierName 供应商名称
     * @return 供应商公司信息
     */
    @Export(paramNames = {"supplierName"})
    public Response<List<Company>> findCompaniesByFuzzySupplierName(String supplierName);

    /**
     * 查询入驻审核通过的供应商信息
     *
     * @param nick     供应商昵称
     * @param baseUser 当前登录用户
     * @param pageNo   页码
     * @param size     返回条数
     * @return 供应商信息
     */
    @Export(paramNames = {"baseUser", "nick", "pageNo", "size"})
    public Response<Paging<SupplierDto>> findEnterPassSuppliers(BaseUser baseUser, String nick, Integer pageNo, Integer size);


    /**
     * // todo: 加入权限限制
     * 登录后的用户管理供应商的绩效
     *
     * @param user        登录后的用户 todo: 加入权限限制
     * @param companyName 按名字查询公司
     * @param pageNo      分页页码
     * @param size        分页的大小
     * @return 供应商分页的列表
     */
    @Export(paramNames = {"user", "companyName", "pageNo", "size"})
    public Response<Paging<SupplierDto>> pagingCompanyHasSupplierCode(BaseUser user, String companyName, Integer pageNo, Integer size);

    /**
     * 确认资源类型
     *
     * @param baseUser     当前登录用户
     * @param userId       供应商user id
     * @param resourceType 资源类型(0：普通资源，1：标杆企业，2：500强)
     * @param competitors  竞争对手(对采购商而言)（多个用逗号分隔)
     * @return 是否成功
     */
    @Export(paramNames = {"baseUser", "userId", "resourceType", "competitors"})
    public Response<Boolean> confirmResource(BaseUser baseUser, Long userId, Integer resourceType, String competitors);

    /**
     * 被定时任务调用，获取 id-vcode 形势的 map
     *
     * @param page 页数
     * @param size 每页大小
     * @return id，vcode 对应的 map
     */
    public Response<Map<Long, String>> companyHasVcode(Integer page, Integer size);
}
