package io.terminus.snz.related.daos;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.terminus.common.model.Paging;
import io.terminus.common.mysql.dao.MyBatisDao;
import io.terminus.common.utils.Constants;
import io.terminus.common.utils.JsonMapper;
import io.terminus.snz.related.models.Compensation;
import io.terminus.snz.related.models.CompensationDetail;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 索赔信息Dao
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-7-10
 */
@Repository
public class CompensationDao extends MyBatisDao<Compensation> {

    /**
     * 分页查询索赔信息(供应商用)
     * @param offset 起始
     * @param limit 分页大小
     * @param criteria 查询条件
     * @param deductedStartAt 扣款起始时间
     * @param deductedEndAt 扣款结束时间
     * @return 索赔分页对象
     */
    public Paging<Compensation> paging(Integer offset, Integer limit, Compensation criteria,
                                             String deductedStartAt, String deductedEndAt){
        Map<String, Object> params = Maps.newHashMap();
        if (criteria != null) {    //查询条件不为空
            Map<String, Object> objMap = JsonMapper.nonEmptyMapper().getMapper().convertValue(criteria, Map.class);
            params.putAll(objMap);
        }
        if(!"".equals(deductedStartAt))
            params.put("deductedStartAt", deductedStartAt);
        if(!"".equals(deductedEndAt))
            params.put("deductedEndAt", deductedEndAt);
        // get total count
        Long total = getSqlSession().selectOne(sqlId(COUNT), params);
        if (total <= 0){
            return new Paging<Compensation>(0L, Collections.<Compensation>emptyList());
        }
        params.put(Constants.VAR_OFFSET, offset);
        params.put(Constants.VAR_LIMIT, limit);
        // get data
        List<Compensation> datas = getSqlSession().selectList(sqlId(PAGING), params);
        return new Paging<Compensation>(total, datas);
    }

    /**
     * 分页查询索赔信息(采购商用)
     * @param offset 起始
     * @param limit 分页大小
     * @param criteria 查询条件
     * @param deductedStartAt 扣款起始时间
     * @param deductedEndAt 扣款结束时间
     * @param factories 工厂编号列表
     * @return 索赔分页对象
     */
    public Paging<Compensation> pagingForPurchaser(Integer offset, Integer limit, Compensation criteria,
                                             String deductedStartAt, String deductedEndAt, List<String> factories){
        Map<String, Object> params = Maps.newHashMap();
        if (criteria != null) {    //查询条件不为空
            Map<String, Object> objMap = JsonMapper.nonEmptyMapper().getMapper().convertValue(criteria, Map.class);
            params.putAll(objMap);
        }
        params.put("deductedStartAt", deductedStartAt);
        params.put("deductedEndAt", deductedEndAt);
        params.put("factories", factories);
        // get total count
        Long total = getSqlSession().selectOne(sqlId(".countForPurchaser"), params);
        if (total <= 0){
            return new Paging<Compensation>(0L, Collections.<Compensation>emptyList());
        }
        params.put(Constants.VAR_OFFSET, offset);
        params.put(Constants.VAR_LIMIT, limit);
        // get data
        List<Compensation> datas = getSqlSession().selectList(sqlId(".pagingForPurchaser"), params);
        return new Paging<Compensation>(total, datas);
    }

    /**
     * 根据信息查找记录id
     * @param detail
     * @return id
     * */
    public Compensation findOneByInfo(CompensationDetail detail) {
        return getSqlSession().selectOne("Compensation.findOneByInfo", detail);
    }

    /**
     * 自动更新记录表状态
     * @param time 时间截点
     * @return 是否更新成功
     * */
    public Boolean autoUpdateStatus(DateTime time){
        Date date = time.toDate();
        getSqlSession().update("Compensation.autoUpdate",ImmutableMap.of("date", date));
        return Boolean.TRUE;
    }

    /**
     * 得到目前截止一个月内索赔次数
     * @param dateLine 当前时间
     * @return List<Compensation>
     * */
    public List<Compensation> getSumOfMonth(Date dateLine){
        return getSqlSession().selectList("Compensation.getSumOfMonth", ImmutableMap.of("dateLine", dateLine));
    }
  }
