package io.terminus.snz.user.service;

import io.terminus.pampas.common.Response;
import io.terminus.snz.user.model.Company;
import io.terminus.snz.user.model.User;

/**
 * Author:Guo Chaopeng
 * Created on 14-7-28.
 */
public interface TagService {

    /**
     * 为供应商添加标签（适用于一般的标签，特殊标签使用特定的接口）
     *
     * @param userId 用户编号
     * @param tag    标签
     * @return 是否成功
     */
    public Response<Boolean> addTag(Long userId, User.SupplierTag tag);

    /**
     * 删除标签
     * @param userId 用户id
     * @param tag 标签
     * @return 是否成功
     */
    public Response<Boolean> delTag(Long userId, User.SupplierTag tag);

    /**
     * 为供应商添加tqrdc绩效标签
     *
     * @param userId 用户编号
     * @param score  绩效得分
     * @return 是否成功
     */
    public Response<Boolean> addTQRDCTag(Long userId, Integer score);

    /**
     * 根据公司信息更新标签
     *
     * @param userId  用户编号
     * @param company 公司信息
     * @return 是否成功
     */
    public Response<Boolean> updateTagsByCompany(Long userId, Company company);

    /**
     * 打商供应商状态标签
     * @param userId 用户id
     * @param tag 标签（可接受：注册，完善信息，入围，备选，合作，淘汰）
     * @return 是否打标签成功
     */
    public Response<Boolean> addSupplierStatusTag(Long userId, User.SupplierTag tag);
}
