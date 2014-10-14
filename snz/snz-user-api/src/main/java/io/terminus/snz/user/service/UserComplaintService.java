package io.terminus.snz.user.service;

import io.terminus.common.model.Paging;
import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.model.UserComplaint;

import java.util.List;
import java.util.Map;


/**
 * 用户抱怨反馈信息<P>
 *
 * @author wanggen 2014-08-13 14:53:07
 */
public interface UserComplaintService {

    /**
     * 新增
     *
     * @param userComplaint add bean
     * @return 新增后的自增序列号
     */
    @Export(paramNames = {"userComplaint"})
    public Response<Long> create(UserComplaint userComplaint);


    /**
     * 根据ID查询但条记录
     *
     * @return 返回的 userComplaint
     */
    @Export(paramNames = {"id"})
    public Response<UserComplaint> findById(Long id);


    /**
     * 分页查询
     * <PRE>
     * 查询接口
     * userId                   Long        抱怨人ID
     * userName                 String      抱怨人
     * productLineId            Integer     产品线ID
     * productLineName          String      产品线名称:后台二级类目
     * frontendCategoryId       Long        模块:前台一级类目
     * frontendCategoryName     String      模块-前台一级类目
     * factoryNum               Integer     生产工厂编号
     * factoryName              String      工厂名称
     * productOwnerId           Long        产品负责人ID
     * productOwnerName         String      产品负责人
     * supplierCode             String      供应商CODE
     * supplierName             String      供应商名称
     * moduleId                 Long        模块号
     * moduleName               String      模块名称
     * </PRE>
     * @param param     可选查询参数
     * @param pageNo    页码
     * @param pageSize  每页数量
     * @return          分页查询结果
     */
    @Export(paramNames = {"user","param", "pageNo", "pageSize"})
    Response<Paging<UserComplaint>> findByPaging(BaseUser user, Map<String, Object> param, Integer pageNo, Integer pageSize);


    /**
     * 根据查询条件查询
     * <PRE>
     * 查询接口
     * userId                   Long        抱怨人ID
     * userName                 String      抱怨人
     * productLineId            Integer     产品线ID
     * productLineName          String      产品线名称:后台二级类目
     * frontendCategoryId       Long        模块:前台一级类目
     * frontendCategoryName     String      模块-前台一级类目
     * factoryNum               Integer     生产工厂编号
     * factoryName              String      工厂名称
     * productOwnerId           Long        产品负责人ID
     * productOwnerName         String      产品负责人
     * supplierCode             String      供应商CODE
     * supplierName             String      供应商名称
     * moduleId                 Long        模块号
     * moduleName               String      模块名称
     * </PRE>
     * @param param 可选查询参数
     * @return 分页查询结果
     */
    @Export(paramNames = {"user", "param"})
    Response<List<UserComplaint>> findAllBy(BaseUser user, Map<String, Object> param);


    /**
     * 根据 userId 查询 UserComplaint 列表
     *
     * @param userId   抱怨人ID
     * @return 结果列
     */
    @Export(paramNames = {"userId"})
    Response<List<UserComplaint>> findByUserId(Long userId);


    /**
     * 更新操作
     *
     * @param userComplaint 更新操作参数
     * @return 影响行数
     */
    public Response<Integer> update(UserComplaint userComplaint);


    /**
     * 根据序列 id 删除记录
     *
     * @param ids 序列 id 列表
     * @return 删除行数
     */
    public Response<Integer> deleteByIds(List<Long> ids);


}
