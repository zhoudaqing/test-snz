package io.terminus.snz.requirement.dao;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.terminus.snz.requirement.model.MWOldModuleInfo;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Date: 8/13/14
 * Time: 18:08
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */

@Repository
public class MWOldModuleInfoDao extends SqlSessionDaoSupport {

    /**
     * 插入一个老品信息
     * @param insert 插入的老品信息
     * @return 插入老品信息的id
     */
    public Long create(MWOldModuleInfo insert) {
        getSqlSession().insert("MWOldModuleInfo.create", insert);
        return insert.getId();
    }

    /**
     * 更新一个老品信息
     * @param update 更新数据
     * @return 更新是否成功
     */
    public Boolean update(MWOldModuleInfo update) {
        return getSqlSession().update("MWOldModuleInfo.update", update) == 1;
    }

    /**
     * 根据id查找一个老品信息
     * @param id 指定的老品信息id
     * @return 一个老品信息
     */
    public MWOldModuleInfo findById(Long id) {
        return getSqlSession().selectOne("MWOldModuleInfo.findById", id);
    }

    /**
     * 根据参数查找一个老品信息
     * @param params 老品参数
     * @return 一个老品信息
     */
    public MWOldModuleInfo findBy(MWOldModuleInfo params) {
        return getSqlSession().selectOne("MWOldModuleInfo.findBy", params);
    }

    /**
     * 获取当前表中最大的id
     * @return 当前最大id
     */
    public Long maxId() {
        return getSqlSession().selectOne("MWOldModuleInfo.maxId");
    }

    /**
     * 根据id降序排列后，获取一个给定大小的老品信息列表，用来导数据
     * @param maxId 操作时最大的id
     * @param limit 返回列表大小
     * @return 老品信息列表
     */
    public List<MWOldModuleInfo> forDump(Long maxId, Integer limit) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("maxId", maxId);
        params.put("limit", limit);

        return getSqlSession().selectList("MWOldModuleInfo.forDump", params);
    }

    /**
     * 如果老品信息的id在列表里面，就删除它
     * @param ids 给定id列表
     * @return 操作数量
     */
    public Integer deleteInIds(List<Long> ids) {
        return getSqlSession().delete("MWOldModuleInfo.deleteInIds", ids);
    }


    /**
     * 根据物料号或物料名称查询物料信息
     *
     * @param moduleNum  物料号
     * @param moduleName 物料名称
     */
    public List<MWOldModuleInfo> findByModuleNumOrModuleName(String moduleNum, String moduleName) {
        return getSqlSession().selectList("MWOldModuleInfo.findByModuleNumOrModuleName",
                ImmutableMap.of("moduleNum", moduleNum, "moduleName", moduleName));
    }

}
