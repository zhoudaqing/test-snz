package io.terminus.snz.user.service;

import io.terminus.pampas.common.Response;

import java.util.List;

/**
 * Desc: 细分到类目的权限
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 8/28/14
 */
public interface PurchaserAuthorityService {

    /**
     * 授予用户一条在后台类目上的某某职位某某职责的权限
     * @param userId 被授予者id（采购商）
     * @param bcId 后台类目id
     * @param position 职位（1. 小微主, 2. 小微成员）
     * @param role 职责, @see User.JobRole
     * @return 是否授予成功
     */
    Response<Boolean> grantAuthInBcId(Long userId, Long bcId, Integer position, String role);

    /**
     * 撤销用户在后台类目上某某职位某某职责的权限
     * @param userId 被撤销者id（采购商）
     * @param bcId 后台类目id
     * @param position 职位（1. 小微主, 2. 小微成员）
     * @param role 职责, @see User.JobRole
     * @return 是否撤销成功
     */
    Response<Boolean> revokeAuthInBcId(Long userId, Long bcId, Integer position, String role);

    /**
     * 获取有对应职位职责的后台类目id列表
     * @param userId 查询此用户的权限
     * @param position 职位（1. 小微主, 2. 小微成员）
     * @param role 职责, @see User.JobRole
     * @return 后台类目id列表
     */
    Response<List<Long>> getAuthorizedBcIds(Long userId, Integer position, String role);

    /**
     * 查询对应权限的所有供应商
     * @param bcId 后台类目id
     * @param position 职位（1. 小微主, 2. 小微成员）
     * @param role 职责, @see User.JobRole
     * @return 有权限的用户id列表
     */
    Response<List<Long>> getUserIdsHavingAuthInBcId(Long bcId, Integer position, String role);

    /**
     * 查询对应权限的所有采购商（类目集合）
     * @param bcIds 后台类目id列表
     * @param position 职位（1. 小微主, 2. 小微成员）
     * @param role 职责, @see User.JobRole
     * @return 有权限的用户id列表
     */
    Response<List<Long>> getUserIdsHavingAuthInBcIds(List<Long> bcIds, Integer position, String role);

    /**
     * 检查用户有对应类目是否有某某职位某某职责的权限
     * @param userId 查询此用户
     * @param bcId 后台类目id
     * @param position 职位（1. 小微主, 2. 小微成员）
     * @param role 职责, @see User.JobRole
     * @return 是否有此权限
     */
    Response<Boolean> checkAuthInBcId(Long userId, Long bcId, Integer position, String role);

    /**
     * 检查用户有对应类目id列表中任意一个有权限
     * @param userId 查询此用户
     * @param bcIds 后台类目id列表
     * @param position 职位（1. 小微主, 2. 小微成员）
     * @param role 职责, @see User.JobRole
     * @return 是否有权限
     */
    Response<Boolean> checkAuthInAnyBcIds(Long userId, List<Long> bcIds, Integer position, String role);

    /**
     * 检查用户对应类目id列表中所有都有权限
     * @param userId 查询此用户
     * @param bcIds 后台类目id列表
     * @param position 职位（1. 小微主, 2. 小微成员）
     * @param role 职责, @see User.JobRole
     * @return 是否有权限
     */
    Response<Boolean> checkAuthInAllBcIds(Long userId, List<Long> bcIds, Integer position, String role);
}
