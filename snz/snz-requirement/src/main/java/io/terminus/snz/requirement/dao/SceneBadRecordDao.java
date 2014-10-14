package io.terminus.snz.requirement.dao;

import com.google.common.collect.Maps;
import io.terminus.snz.requirement.model.MWSceneBadRecord;
import io.terminus.snz.requirement.model.SceneBadRecord;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Date: 8/15/14
 * Time: 16:10
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */

@Repository
public class SceneBadRecordDao extends SqlSessionDaoSupport {
    public Long create(SceneBadRecord insert) {
        getSqlSession().insert("SceneBadRecord.create", insert);
        return insert.getId();
    }

    public Boolean update(SceneBadRecord update) {
        return getSqlSession().update("SceneBadRecord.update", update) == 1;
    }

    public SceneBadRecord findById(Long id) {
        return getSqlSession().selectOne("SceneBadRecord.findById", id);
    }

    public SceneBadRecord findBy(SceneBadRecord params) {
        return getSqlSession().selectOne("SceneBadRecord.findBy", params);
    }

    public List<SceneBadRecord> findBetween(String vCode, Date startAt, Date endAt) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("vCode", vCode);
        param.put("startAt", startAt);
        param.put("endAt", endAt);

        return getSqlSession().selectList("SceneBadRecord.findBetween", param);
    }

    public SceneBadRecord findByWId(String wId) {
        return getSqlSession().selectOne("SceneBadRecord.findByWId", wId);
    }

    public int deleteMWByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }
        return getSqlSession().delete("MWSceneBadRecord.deleteByIds", ids);
    }

    public Long maxMWId() {
        return getSqlSession().selectOne("MWSceneBadRecord.maxId");
    }

    public List<MWSceneBadRecord> forMWDump(Long maxId, Integer offset, Integer limit, Date startAt, Date endAt) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("id", maxId);
        param.put("limit", limit);
        param.put("offset", offset);
        param.put("startAt", startAt);
        param.put("endAt", endAt);

        return getSqlSession().selectList("MWSceneBadRecord.forDump", param);
    }
}
