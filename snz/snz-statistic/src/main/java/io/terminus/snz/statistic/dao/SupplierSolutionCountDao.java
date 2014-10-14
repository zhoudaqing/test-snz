package io.terminus.snz.statistic.dao;

import com.google.common.collect.Maps;
import io.terminus.common.annotation.RedisLock;
import io.terminus.common.redis.utils.JedisTemplate;
import io.terminus.snz.statistic.manager.STKeyUtil;
import io.terminus.snz.statistic.model.RequirementStatus;
import io.terminus.snz.statistic.model.SupplierSolutionCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;

/**
 * Desc:供应商的方案统计信息(使用redis做供应商方案统计数据的持久层信息)
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-10.
 */
@Repository
public class SupplierSolutionCountDao {
    //最长等待时间(1s)
    public static final long MAX_WAIT = 1000;

    //锁的最长持有时间(1min)
    public static final int EXPIRED_TIME = 1000*60;

    public static final String SU_SOL_COUNT_LOCK = "su-sol-count-lock";

    @Autowired
    private JedisTemplate jedisTemplate;

    /**
     * 创建供应商的统计数据
     * @param solutionCount 供应商的统计数据信息
     */
    @RedisLock(keyName = SU_SOL_COUNT_LOCK, maxWait = MAX_WAIT, expiredTime = EXPIRED_TIME)
    public void setSupCount(final SupplierSolutionCount solutionCount){
        jedisTemplate.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                //供应商用户下的所有需求统计数据
                String solKey = STKeyUtil.solCountKey(solutionCount.getUserId());

                Map<Integer , Integer> countMap = solutionCount.getStatusCounts();
                for (Map.Entry<Integer, Integer> countStatus : countMap.entrySet()){
                    Integer status = countStatus.getKey();
                    Integer value = countStatus.getValue();
                    if (value != null){
                        String oldCount = jedis.hget(solKey , status.toString());
                        //增量写入用户的需求数量信息
                        Integer newCount = oldCount == null ? value : Integer.parseInt(oldCount) + value;
                        jedis.hset(solKey, status.toString(), newCount.toString());
                    }
                }
            }
        });
    }

    /**
     * 实现批量的更改用户的需求方案数据的统计信息(这个是需求跳转时实现整体需求下的供应商统计数据的更改)
     * @param userIds       供应商用户Id列表
     * @param oldStatus     久的需求状态
     * @param newStatus     新的需求状态
     */
    @RedisLock(keyName = SU_SOL_COUNT_LOCK, maxWait = MAX_WAIT, expiredTime = EXPIRED_TIME)
    public void updateBatchCounts(final List<Long> userIds , final Integer oldStatus, final Integer newStatus){
        jedisTemplate.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                //更改供应商的阶段方案数量
                String solKey;
                String oldCount;
                String newCount;
                for(Long supplierId : userIds){
                    solKey  = STKeyUtil.solCountKey(supplierId);

                    oldCount = jedis.hget(solKey , oldStatus.toString());
                    if(oldCount != null && Integer.parseInt(oldCount) > 0){
                        //上一个阶段的需求数量
                        Integer countValue = Integer.parseInt(oldCount) - 1;
                        jedis.hset(solKey , oldStatus.toString(), countValue.toString());
                    }

                    newCount = jedis.hget(solKey , newStatus.toString());
                    //下一个阶段的需求数量
                    Integer countValue = newCount == null ? 1 : Integer.parseInt(newCount)+1;
                    jedis.hset(solKey , newStatus.toString(), countValue.toString());
                }
            }
        });
    }

    /**
     * 根据供应商编号获取供应商的不同阶段的方案统计数据
     * @param userId    供应商用户编号
     * @return  SolutionCount
     * 返回供应商统计数据信息
     */
    public SupplierSolutionCount findSupplierCount(final Long userId){
        return jedisTemplate.execute(new JedisTemplate.JedisAction<SupplierSolutionCount>() {
            @Override
            public SupplierSolutionCount action(Jedis jedis) {
                SupplierSolutionCount solutionCount = new SupplierSolutionCount();
                solutionCount.setUserId(userId);

                //获取供应商的不同阶段的方案统计数据
                String solKey = STKeyUtil.solCountKey(userId);

                //查询所有阶段
                Map<Integer , Integer> countMap = Maps.newHashMap();
                String statusValue;
                for(RequirementStatus status : RequirementStatus.values()){
                    statusValue = jedis.hget(solKey , status.value().toString());

                    //写入阶段需求统计数量
                    countMap.put(status.value(), statusValue == null ? 0 : Integer.parseInt(statusValue));
                }

                solutionCount.setStatusCounts(countMap);
                return solutionCount;
            }
        });
    }
}
