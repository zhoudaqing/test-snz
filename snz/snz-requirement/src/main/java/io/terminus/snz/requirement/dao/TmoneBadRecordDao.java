package io.terminus.snz.requirement.dao;

import com.google.common.collect.Maps;
import io.terminus.snz.requirement.model.TmoneBadRecord;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by grancy on 14-9-24.
 */
@Repository
public class TmoneBadRecordDao extends SqlSessionDaoSupport{

    public List<TmoneBadRecord> findBetween(String vCode, Date startAt, Date endAt) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("vCode", vCode);
        param.put("startAt", startAt);
        param.put("endAt", endAt);

        return getSqlSession().selectList("TmoneBadRecord.findBetween", param);
    }

    public List<Map<String, Object>> findMoneyList(String vCode, Date startAt, Date endAt) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("vCode", vCode);
        param.put("startAt", startAt);
        param.put("endAt", endAt);

        return getSqlSession().selectList("TmoneBadRecord.findMoneyList", param);
    }
}
