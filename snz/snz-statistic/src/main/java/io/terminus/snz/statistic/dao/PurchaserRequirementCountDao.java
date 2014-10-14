package io.terminus.snz.statistic.dao;

import com.google.common.collect.Maps;
import io.terminus.common.annotation.RedisLock;
import io.terminus.common.redis.utils.JedisTemplate;
import io.terminus.snz.statistic.manager.STKeyUtil;
import io.terminus.snz.statistic.model.PurchaserRequirementCount;
import io.terminus.snz.statistic.model.RequirementStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

import java.util.Map;

/**
 * Desc:采购商的需求统计信息(使用redis做采购商需求统计数据的持久层信息)
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-10.
 */
@Repository
public class PurchaserRequirementCountDao {
    //最长等待时间(1s)
    public static final long MAX_WAIT = 1000;

    //锁的最长持有时间(1min)
    public static final int EXPIRED_TIME = 1000*60;

    public static final String PU_REQ_COUNT_LOCK = "pu-req-count-lock";

    @Autowired
    private JedisTemplate jedisTemplate;

    /**
     * 写入采购商的需求统计数据信息
     * @param requirementCount 用户的需求统计数据信息
     */
    @RedisLock(keyName = PU_REQ_COUNT_LOCK, maxWait = MAX_WAIT, expiredTime = EXPIRED_TIME)
    public void setPurCount(final PurchaserRequirementCount requirementCount){
        jedisTemplate.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                //采购商用户下的所有需求统计数据
                String reqKey = STKeyUtil.reqCountKey(requirementCount.getUserId());

                Map<Integer , Integer> countMap = requirementCount.getStatusCounts();
                for (Map.Entry<Integer, Integer> countStatus : countMap.entrySet()){
                    Integer status = countStatus.getKey();
                    Integer value = countStatus.getValue();
                    if (value != null){
                        String oldCount = jedis.hget(reqKey , status.toString());
                        Integer newCount = oldCount == null ? value : Integer.parseInt(oldCount) + value;
                        jedis.hset(reqKey, status.toString(), newCount.toString());
                    }
                }
            }
        });
    }

    /**
     * 实现的更改用户的需求数据的统计信息(这个是需求跳转时实现整体需求下的供应商统计数据的更改)
     * @param userId        采购商用户编号
     * @param oldStatus     旧的需求状态
     * @param newStatus     新的需求状态
     */
    @RedisLock(keyName = PU_REQ_COUNT_LOCK, maxWait = MAX_WAIT, expiredTime = EXPIRED_TIME)
    public void updateReqCount(final Long userId , final Integer oldStatus, final Integer newStatus){
        jedisTemplate.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                //更改用户的阶段需求数量
                String reqKey = STKeyUtil.reqCountKey(userId);

                String oldCount = jedis.hget(reqKey , oldStatus.toString());
                if(oldCount != null && Integer.parseInt(oldCount) > 0){
                    //上一个阶段的需求数量
                    Integer countValue = Integer.parseInt(oldCount) - 1;
                    jedis.hset(reqKey , oldStatus.toString(), countValue.toString());
                }

                String newCount = jedis.hget(reqKey , newStatus.toString());
                //下一个阶段的需求数量
                Integer countValue = newCount == null ? 1 : Integer.parseInt(newCount)+1;
                jedis.hset(reqKey , newStatus.toString(), countValue.toString());
            }
        });
    }

    /**
     * 根据采购商用户编号获取不同阶段的需求统计数据
     * @param userId    采购商用户编号
     * @return  RequirementCount
     * 返回采购商用户统计数据信息
     */
    public PurchaserRequirementCount findReqCount(final Long userId){
        return jedisTemplate.execute(new JedisTemplate.JedisAction<PurchaserRequirementCount>() {
            @Override
            public PurchaserRequirementCount action(Jedis jedis) {
                PurchaserRequirementCount requirementCount = new PurchaserRequirementCount();
                requirementCount.setUserId(userId);

                //获取采购商的不同阶段的需求统计数据
                String reqKey = STKeyUtil.reqCountKey(requirementCount.getUserId());

                //查询所有阶段
                Map<Integer , Integer> countMap = Maps.newHashMap();
                String statusValue;
                for(RequirementStatus status : RequirementStatus.values()){
                    statusValue = jedis.hget(reqKey , status.toString());

                    //写入阶段需求统计数量
                    countMap.put(status.value(), statusValue == null ? 0 : Integer.parseInt(statusValue));
                }

                requirementCount.setStatusCounts(countMap);
                return requirementCount;
            }
        });
    }
}
