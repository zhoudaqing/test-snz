package io.terminus.snz.user.dao;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import io.terminus.common.model.Paging;
import io.terminus.snz.user.model.SupplierBaseInfo;
import io.terminus.snz.user.model.SupplierLocationInfo;
import io.terminus.snz.user.model.SupplierTQRDCInfo;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Author:Guo Chaopeng
 * Created on 14-6-20.
 */
@Repository
public class SupplierTQRDCInfoDao extends SqlSessionDaoSupport {

    /**
     * 创建绩效
     */
    public Long create(SupplierTQRDCInfo supplierTQRDCInfo) {
        getSqlSession().insert("SupplierTQRDCInfo.insert", supplierTQRDCInfo);
        return supplierTQRDCInfo.getId();
    }

    public Integer batchCreate(List<SupplierTQRDCInfo> supplierTQRDCInfos) {
        getSqlSession().insert("SupplierTQRDCInfo.batchInsert", supplierTQRDCInfos);
        return supplierTQRDCInfos.size();
    }


    /**
     * 更新绩效
     */
    public boolean update(SupplierTQRDCInfo supplierTQRDCInfo) {
        return getSqlSession().update("SupplierTQRDCInfo.update", supplierTQRDCInfo) == 1;
    }

    /**
     * 根据id查询绩效
     */
    public SupplierTQRDCInfo findById(Long id) {
        return getSqlSession().selectOne("SupplierTQRDCInfo.findById", id);
    }

    /**
     * 根据user id查询最新的绩效
     */
    public SupplierTQRDCInfo findLastByUserId(Long userId) {
        return getSqlSession().selectOne("SupplierTQRDCInfo.findLastByUserId", userId);
    }

    /**
     * 查询最大月份
     */
    public String findMaxMonth() {
        return getSqlSession().selectOne("SupplierTQRDCInfo.findMaxMonth");
    }

    public List<SupplierTQRDCInfo> findBySupplierName(String supplierName, String monthStart, String monthEnd) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("supplierName", supplierName);
        params.put("monthStart", monthStart);
        params.put("monthEnd", monthEnd);
        return getSqlSession().selectList("SupplierTQRDCInfo.findBySupplierName", params);
    }

    public List<SupplierTQRDCInfo> findBySupplierCode(String supplierCode, String monthStart, String monthEnd) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("supplierCode", supplierCode);
        params.put("monthStart", monthStart);
        params.put("monthEnd", monthEnd);
        return getSqlSession().selectList("SupplierTQRDCInfo.findBySupplierCode", params);
    }

    /**
     * 根据用户ID查询绩效
     *
     * @param userId     用户ID
     * @param monthStart 开始月
     * @param monthEnd   结束月
     * @return 绩效数据列表
     */
    public List<SupplierTQRDCInfo> findByUserId(Long userId, String monthStart, String monthEnd) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("userId", userId);
        params.put("monthStart", monthStart);
        params.put("monthEnd", monthEnd);
        return getSqlSession().selectList("SupplierTQRDCInfo.findByUserId", params);
    }

    public List<SupplierTQRDCInfo> findByMonth(String month) {
        return getSqlSession().selectList("SupplierTQRDCInfo.findByMonth", month);
    }

    public Paging<SupplierTQRDCInfo> findBy(String supplierName, String supplierCode, String month, String module, String orderBy, Integer compositeScoreStart, Integer compositeScoreEnd, Integer offset, Integer limit) {

        Map<String, Object> params = Maps.newHashMap();
        params.put("supplierName", Strings.emptyToNull(supplierName));
        params.put("supplierCode", Strings.emptyToNull(supplierCode));
        params.put("month", Strings.emptyToNull(month));
        params.put("module", Strings.emptyToNull(module));
        params.put("compositeScoreStart", compositeScoreStart);
        params.put("compositeScoreEnd", compositeScoreEnd);

        Long count = getSqlSession().selectOne("SupplierTQRDCInfo.countBy", params);
        if (count == 0) {
            return new Paging<SupplierTQRDCInfo>(0L, Collections.<SupplierTQRDCInfo>emptyList());
        }

        params.put("orderBy", Strings.emptyToNull(orderBy));
        params.put("offset", offset);
        params.put("limit", limit);

        List<SupplierTQRDCInfo> users = getSqlSession().selectList("SupplierTQRDCInfo.findBy", params);

        return new Paging<SupplierTQRDCInfo>(count, users);
    }

    /**
     * 按月份和区域统计供应商区域信息
     *
     * @return 供应商区域信息
     */
    public List<SupplierLocationInfo> groupByMonthAndLocation() {
        return getSqlSession().selectList("SupplierTQRDCInfo.groupByMonthAndLocation");
    }

    /**
     * 查询供应商综合得分情况
     *
     * @param month 月份
     * @return 供应商得分
     */
    public List<SupplierTQRDCInfo> findCompositeScoreOfMonth(String month) {
        return getSqlSession().selectList("SupplierTQRDCInfo.findCompositeScoreOfMonth", month);
    }

    /**
     * 月底更新供应商状态
     *
     * @param month 修改条件
     * @return 更新结果
     */
    public Boolean updateSupplierStatusSmall(String month) {
        return getSqlSession().update("SupplierTQRDCInfo.updateSupplierStatusSmall", month) == 1;
    }

    public Boolean updateSupplierStatusBig(String month) {
        return getSqlSession().update("SupplierTQRDCInfo.updateSupplierStatusBig", month) == 1;
    }

    /**
     * 根据月份选出当月数据
     *
     * @param month 月份
     * @return 当月数据
     */
    public SupplierTQRDCInfo findByMonthOne(String month) {
        return getSqlSession().selectOne("SupplierTQRDCInfo.findByMonthOne", month);
    }

    public List<SupplierTQRDCInfo> findByMonthAndUserIds(String month, List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return Collections.emptyList();
        }
        Map<String, Object> params = Maps.newHashMap();
        params.put("month", month);
        params.put("list", userIds);
        return getSqlSession().selectList("SupplierTQRDCInfo.findByMonthAndUserIds", params);
    }


    public List<SupplierBaseInfo> loadSupplierBaseInfos() {
        return getSqlSession().selectList("loadSupplierBaseInfos");
    }

    public List<SupplierTQRDCInfo> findByMonthAndModule(String month, String module) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("month", month);
        params.put("module", module);
        return getSqlSession().selectList("SupplierTQRDCInfo.findByMonthAndModule", params);
    }

    public List<Long> findUserIdsByMonthAndStrategy(String month, Integer strategy, List<Long> userIds) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("month", month);
        params.put("strategy", strategy);
        params.put("list", userIds);

        return getSqlSession().selectList("SupplierTQRDCInfo.findUserIdsByMonthAndStrategy", params);
    }

    /**
     * 根据月份删除供应商绩效数据
     * @param month 月，示例:"2014-08"
     * @return	删除的行数
     */
    public int deleteByMonth(String month) {
        return getSqlSession().delete("SupplierTQRDCInfo.deleteByMonth", month);
    }
}
