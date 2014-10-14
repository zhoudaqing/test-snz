package io.terminus.snz.statistic.dao;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.terminus.common.annotation.RedisLock;
import io.terminus.common.redis.utils.JedisTemplate;
import io.terminus.snz.statistic.manager.STKeyUtil;
import io.terminus.snz.statistic.model.SolutionCountType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;

/**
 * Desc:统计供应商的方案数据统计信息
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-09-04.
 */
@Repository
public class SolutionCountDao {
    //最长等待时间(1s)
    public static final long MAX_WAIT = 1000;

    //锁的最长持有时间(1min)
    public static final int EXPIRED_TIME = 1000*60;

    public static final String SOLUTION_COUNT_LOCK = "solution-count-lock";

    @Autowired
    private JedisTemplate jedisTemplate;

    /**
     * 写入方案的统计数据信息
     * @param userId    用户编号
     * @param countType 统计数据的类型
     * @param addCount  增加的数据量
     */
    @RedisLock(keyName = SOLUTION_COUNT_LOCK, maxWait = MAX_WAIT, expiredTime = EXPIRED_TIME)
    public void setSolCount(final Long userId , final SolutionCountType countType, final Integer addCount){
        jedisTemplate.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                //需求供应商方案信息key
                String solKey = STKeyUtil.supSolCountKey(userId);

                //获取所有的方案状态的统计数据
                List<String> countValues = jedis.hmget(solKey , countType.value());

                //获取已有的统计数据
                Integer countValue = countValues.get(0) == null ? null : Integer.parseInt(countValues.get(0));

                //将统计数据写入redis
                jedis.hmset(solKey, ImmutableMap.of(countType.value(), countValue == null ? addCount.toString() : (addCount + countValue) + ""));
            }
        });
    }

    /**
     * 通过供应商的编号以及方案的统计参数显示方案的详细统计数据
     * @param userId        用户编号
     * @param countTypes    统计参数
     * @return SolutionCountType 返回方案状态对应的统计数据
     * 返回需求的统计数据信息
     */
    public Map<SolutionCountType , Integer> findSolCount(final Long userId, final String... countTypes){
        return jedisTemplate.execute(new JedisTemplate.JedisAction<Map<SolutionCountType , Integer>>() {
            @Override
            public Map<SolutionCountType , Integer> action(Jedis jedis) {
                Map<SolutionCountType , Integer> solCountMap = Maps.newHashMap();

                //需求供应商方案信息key
                String reqKey = STKeyUtil.supSolCountKey(userId);

                //获取所有的供应商方案的状态统计数据
                List<String> countValues = jedis.hmget(reqKey , countTypes);
                for(int i=0; i<countTypes.length; i++){
                    solCountMap.put(SolutionCountType.from(countTypes[i]), countValues.get(i) == null ? 0 : Integer.parseInt(countValues.get(i)));
                }

                return solCountMap;
            }
        });
    }

    /**
     * 删除某个供应商的方案的统计数据信息
     * @param userId 供应商用户编号
     */
    @RedisLock(keyName = SOLUTION_COUNT_LOCK, maxWait = MAX_WAIT, expiredTime = EXPIRED_TIME)
    public void deleteSolCount(final Long userId){
        jedisTemplate.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                //需求供应商方案信息key
                String reqKey = STKeyUtil.supSolCountKey(userId);
                jedis.del(reqKey);
            }
        });
    }
}
