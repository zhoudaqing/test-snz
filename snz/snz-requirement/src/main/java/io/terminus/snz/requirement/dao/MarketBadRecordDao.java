package io.terminus.snz.requirement.dao;

import com.google.common.collect.Maps;
import io.terminus.snz.requirement.model.MWMarketBadRecord;
import io.terminus.snz.requirement.model.MarketBadRecord;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Date: 8/15/14
 * Time: 13:39
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */

@Repository
public class MarketBadRecordDao extends SqlSessionDaoSupport {
    public Long create(MarketBadRecord insert) {
        getSqlSession().insert("MarketBadRecord.create", insert);
        return insert.getId();
    }

    public Boolean update(MarketBadRecord update) {
        return getSqlSession().update("MarketBadRecord.update", update) == 1;
    }

    public MarketBadRecord findById(Long id) {
        return getSqlSession().selectOne("MarketBadRecord.findById", id);
    }

    public MarketBadRecord findBy(MarketBadRecord params) {
        return getSqlSession().selectOne("MarketBadRecord.findBy", params);
    }

    public List<MarketBadRecord> findBetween(String vCode, Date startAt, Date endAt) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("vCode", vCode);
        param.put("startAt", startAt);
        param.put("endAt", endAt);

        return getSqlSession().selectList("MarketBadRecord.findBetween", param);
    }

    public Long maxIdIn(Date startAt, Date endAt) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("startAt", startAt);
        params.put("endAt", endAt);

        return getSqlSession().selectOne("MarketBadRecord.maxIdIn", params);
    }

    public List<MarketBadRecord> forDumpIn(Long maxId, String vCode, Integer offset, Integer limit, Date startAt, Date endAt) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("maxId", maxId);
        params.put("vCode", vCode);
        params.put("offset", offset);
        params.put("limit", limit);
        params.put("startAt", startAt);
        params.put("endAt", endAt);

        return getSqlSession().selectList("MarketBadRecord.forDumpIn", params);
    }

    public Long maxMWIdIn(Date startAt, Date endAt) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("startAt", startAt);
        params.put("endAt", endAt);

        return getSqlSession().selectOne("MWMarketBadRecord.maxIdIn", params);
    }

    public List<MWMarketBadRecord> forMWDumpIn(Long maxId, String vCode, Integer offset, Integer limit, Date startAt, Date endAt) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("maxId", maxId);
        params.put("vCode", vCode);
        params.put("offset", offset);
        params.put("limit", limit);
        params.put("startAt", startAt);
        params.put("endAt", endAt);

        return getSqlSession().selectList("MWMarketBadRecord.forDumpIn", params);
    }

    public void deleteMWByIds(List<Long> ids) {
        getSqlSession().delete("MWMarketBadRecord.deleteInIds", ids);
    }
}
