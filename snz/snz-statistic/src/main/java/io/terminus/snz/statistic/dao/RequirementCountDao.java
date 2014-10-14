package io.terminus.snz.statistic.dao;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.terminus.common.annotation.RedisLock;
import io.terminus.common.redis.utils.JedisTemplate;
import io.terminus.snz.statistic.manager.STKeyUtil;
import io.terminus.snz.statistic.model.RequirementCountType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;

/**
 * Desc:对某个需求的各种数据的统计计算(使用redis做需求统计数据的cache层，当需求进入选定供应商阶段会将数据写入db。)
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-23.
 */
@Repository
public class RequirementCountDao {
    //最长等待时间(1s)
    public static final long MAX_WAIT = 1000;

    //锁的最长持有时间(1min)
    public static final int EXPIRED_TIME = 1000*60;

    public static final String REQ_COUNT_LOCK = "req-count-lock";

    public static final String REQ_CN_TOPIC_LOCK = "req-cn-topic-lock";

    @Autowired
    private JedisTemplate jedisTemplate;

    /**
     * 写入需求的统计数据信息
     */
    @RedisLock(keyName = REQ_COUNT_LOCK, maxWait = MAX_WAIT, expiredTime = EXPIRED_TIME)
    public void setReqCount(final Long requirementId , final RequirementCountType countType, final Integer addCount){
        jedisTemplate.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                //需求统计信息key
                String reqKey = STKeyUtil.reqInfoCountKey(requirementId);

                //获取所有的需求统计数据
                List<String> countValues = jedis.hmget(reqKey , countType.value());

                //获取已有的统计数据
                Integer countValue = countValues.get(0) == null ? null : Integer.parseInt(countValues.get(0));

                //将需求统计数据写入redis
                jedis.hmset(reqKey , ImmutableMap.of(countType.value(), countValue == null ? addCount.toString() : (addCount + countValue)+""));
            }
        });
    }

    /**
     * 通过需求编号以及需求统计参数显示需求详细数据
     * @param requirementId 需求编号
     * @param countTypes    统计参数
     * @return RequirementCount
     * 返回需求的统计数据信息
     */
    public Map<RequirementCountType , Integer> findReqCount(final Long requirementId, final String... countTypes){
        return jedisTemplate.execute(new JedisTemplate.JedisAction<Map<RequirementCountType , Integer>>() {
            @Override
            public Map<RequirementCountType , Integer> action(Jedis jedis) {
                Map<RequirementCountType , Integer> reqCountValues = Maps.newHashMap();

                //需求统计信息key
                String reqKey = STKeyUtil.reqInfoCountKey(requirementId);

                //获取所有的需求统计数据
                List<String> countValues = jedis.hmget(reqKey , countTypes);
                for(int i=0; i<countTypes.length; i++){
                    if(Objects.equal(RequirementCountType.from(countTypes[i]), RequirementCountType.ANSWER_SU)){
                        //需求下的供应商回复列表
                        String topicKey = STKeyUtil.reqTopicCountKey(requirementId);
                        //写入响应的供应商数量
                        reqCountValues.put(RequirementCountType.ANSWER_SU , jedis.hgetAll(topicKey).keySet().size());
                    }else {
                        reqCountValues.put(RequirementCountType.from(countTypes[i]), countValues.get(i) == null ? 0 : Integer.parseInt(countValues.get(i)));
                    }
                }

                return reqCountValues;
            }
        });
    }

    /**
     * 删除某个需求的统计数据信息
     * @param requirementId 需求编号
     */
    @RedisLock(keyName = REQ_COUNT_LOCK, maxWait = MAX_WAIT, expiredTime = EXPIRED_TIME)
    public void deleteReqCount(final Long requirementId){
        jedisTemplate.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                //需求统计信息key
                String reqKey = STKeyUtil.reqInfoCountKey(requirementId);
                jedis.del(reqKey);
            }
        });
    }

    /**
     * 实时写入回复话题的供应商编号（去重）
     * @param requirementId 需求编号
     * @param supplierId    供应商公司编号
     */
    @RedisLock(keyName = REQ_CN_TOPIC_LOCK, maxWait = MAX_WAIT, expiredTime = EXPIRED_TIME)
    public void incTopicSuppliers(final Long requirementId, final Long supplierId){
        jedisTemplate.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                //需求下的供应商回复列表
                String topicKey = STKeyUtil.reqTopicCountKey(requirementId);
                jedis.hset(topicKey , supplierId.toString() , "1");
            }
        });
    }

    /**
     * 删除某个需求的话题统计数据信息
     * @param requirementId 需求编号
     */
    @RedisLock(keyName = REQ_CN_TOPIC_LOCK, maxWait = MAX_WAIT, expiredTime = EXPIRED_TIME)
    public void deleteTopicCount(final Long requirementId){
        jedisTemplate.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                //需求下的供应商回复列表
                String topicKey = STKeyUtil.reqTopicCountKey(requirementId);
                jedis.del(topicKey);
            }
        });
    }
}
