package io.terminus.snz.requirement.manager;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import io.terminus.common.annotation.RedisLock;
import io.terminus.common.redis.utils.JedisTemplate;
import io.terminus.snz.requirement.model.Module;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;

/**
 * Desc:用于管理各种统计数据使用redis来处理
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-06-29.
 */
@Slf4j
@Component
public class CountManager {
    //最长等待时间(1s)
    public static final long MAX_WAIT = 1000;

    //锁的最长持有时间(1min)
    public static final int EXPIRED_TIME = 1000*60;

    public static final String REQUIREMENT_COUNT_LOCK = "REQUIREMENT_COUNT_LOCK";

    public static final String SOLUTION_SIGN_LOCK = "SOLUTION_SIGN_LOCK";

    @Autowired
    private JedisTemplate jedisTemplate;

    /**
     * 当创建模块时触发更改模块数量信息
     * @param requirementId 需求编号
     * @param module        模块信息
     */
    @RedisLock(keyName = REQUIREMENT_COUNT_LOCK , maxWait = MAX_WAIT, expiredTime = EXPIRED_TIME)
    public void createMEvent(final Long requirementId , final Module module){
        jedisTemplate.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                //数量的统计
                String moduleNumKey = REKeyUtil.moduleNumKey(requirementId);
                String value0 = jedis.get(moduleNumKey);
                Integer numValue = Integer.parseInt(value0 == null ? "0" : value0) + 1;
                jedis.set(moduleNumKey , numValue.toString());

                //总数的统计
                String moduleTotalKey = REKeyUtil.moduleTotalKey(requirementId);
                String value1 = jedis.get(moduleTotalKey);
                Integer totalValue = Integer.parseInt(value1 == null ? "0" : value1)+module.getTotal();
                jedis.set(moduleTotalKey , totalValue.toString());
            }
        });
    }

    /**
     * 当更改模块信息时触发更改整体需求的模块统计信息
     * @param requirementId 需求编号
     * @param newModule     新的模块信息
     * @param oldModule     久的模块信息
     */
    @RedisLock(keyName = REQUIREMENT_COUNT_LOCK , maxWait = MAX_WAIT, expiredTime = EXPIRED_TIME)
    public void updateMEvent(final Long requirementId , final Module newModule, final Module oldModule){
        jedisTemplate.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                //总数的统计
                String moduleTotalKey = REKeyUtil.moduleTotalKey(requirementId);
                String value = jedis.get(moduleTotalKey);
                Integer countValue = Integer.parseInt(value == null ? "0" : value) + (newModule.getTotal() - oldModule.getTotal());

                jedis.set(moduleTotalKey, countValue.toString());
            }
        });
    }

    /**
     * 当删除模块信息时触发更改整体需求的模块统计信息
     * @param requirementId 需求编号
     * @param module        删除的模块信息
     */

    @RedisLock(keyName = REQUIREMENT_COUNT_LOCK , maxWait = MAX_WAIT, expiredTime = EXPIRED_TIME)
    public void deleteMEvent(final Long requirementId, final Module module){
        jedisTemplate.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                //数量的统计
                String moduleNumKey = REKeyUtil.moduleNumKey(requirementId);
                String value0 = jedis.get(moduleNumKey);
                Integer numValue = Integer.parseInt(value0 == null ? "0" : value0) - 1;
                jedis.set(moduleNumKey , numValue.toString());

                //总数的统计
                String moduleTotalKey = REKeyUtil.moduleTotalKey(requirementId);
                String value1 = jedis.get(moduleTotalKey);
                Integer totalValue = Integer.parseInt(value1 == null ? "0" : value1) - module.getTotal();
                jedis.set(moduleTotalKey , totalValue.toString());
            }
        });
    }

    /**
     * 写入需求的模块数量统计信息
     * @param requirementId 需求编号
     * @param moduleNum     模块数量
     * @param moduleTotal   模块的全部数量
     */
    @RedisLock(keyName = REQUIREMENT_COUNT_LOCK , maxWait = MAX_WAIT, expiredTime = EXPIRED_TIME)
    public void setRCount(final Long requirementId , final Integer moduleNum, final Integer moduleTotal){
        jedisTemplate.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                //数量的统计
                String moduleNumKey = REKeyUtil.moduleNumKey(requirementId);
                jedis.set(moduleNumKey , moduleNum.toString());

                //总数的统计
                String moduleTotalKey = REKeyUtil.moduleTotalKey(requirementId);
                jedis.set(moduleTotalKey , moduleTotal.toString());
            }
        });
    }

    /**
     * 当同意了需求的保密协议后将记录数据库信息
     * @param requirementId 需求保密协议
     * @param userId        用户编号
     */
    @RedisLock(keyName = SOLUTION_SIGN_LOCK , maxWait = MAX_WAIT, expiredTime = EXPIRED_TIME)
    public void signSecrecyEvent(final Long requirementId, final Long userId){
        jedisTemplate.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                //保密协议签订
                String signKey = REKeyUtil.signKey(requirementId);

                jedis.hmset(signKey , ImmutableMap.of(userId.toString() , "1"));
            }
        });
    }

    /**
     * 通过需求编号获取需求模块的数量信息
     * @param requirementId 需求编号
     * @return  Integer
     * 返回模块数量信息
     */
    public Integer getModuleNum(Long requirementId){
        return getIntValue(REKeyUtil.moduleNumKey(requirementId));
    }

    /**
     * 通过需求编号获取需求模块的总体数量信息
     * @param requirementId 需求编号
     * @return  Integer
     * 返回需求的模块总数信息
     */
    public Integer getModuleTotal(Long requirementId){
        return getIntValue(REKeyUtil.moduleTotalKey(requirementId));
    }

    /**
     * 返回用户是否已对需求协议签名
     * @param requirementId 需求编号
     * @param userId        用户编号
     * @return  Boolean
     * 返回用户是否已对需求签名
     */
    public Boolean existSign(Long requirementId , Long userId){
        return getSignValue(REKeyUtil.signKey(requirementId) , userId.toString());
    }

    /**
     * 同过需求编号获取需求下的所有点击过协议的供应商编号
     * @param requirementId  需求编号
     * @return List
     * 返回供应商编号
     */
    public List<Long> findAllSign(final Long requirementId){
        return jedisTemplate.execute(new JedisTemplate.JedisAction<List<Long>>() {
            @Override
            public List<Long> action(Jedis jedis) {
                String signKey = REKeyUtil.signKey(requirementId);

                Map<String , String> signValues = jedis.hgetAll(signKey);

                List<Long> signSuppliers = Lists.newArrayList();
                for(Map.Entry<String , String> entry : signValues.entrySet()){
                    if(entry.getValue() != null && Objects.equal(entry.getValue() ,"1")){
                        //记录供应商的编号
                        signSuppliers.add(Long.parseLong(entry.getKey()));
                    }
                }

                return signSuppliers;
            }
        });
    }

    /**
     * 将用户从需求的队列中去除出去(当队列为空时删除key)
     * @param requirementId 需求编号
     * @param userId        用户编号
     */
    @RedisLock(keyName = SOLUTION_SIGN_LOCK , maxWait = MAX_WAIT, expiredTime = EXPIRED_TIME)
    public void removeSign(final Long requirementId , final Long userId){
        jedisTemplate.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                //删除属性信息
                String signKey = REKeyUtil.signKey(requirementId);
                jedis.hdel(signKey, userId + "");

                //当签名为空时删除这个key
                if (jedis.hgetAll(signKey).isEmpty()) {
                    jedis.del(signKey);
                }
            }
        });
    }

    /**
     * 根据redis的key获取模块的数据信息
     * @param countKey  统计数据的key
     * @return  Integer
     * 返回redis中的统计数据
     */
    private Integer getIntValue(final String countKey){
        return jedisTemplate.execute(new JedisTemplate.JedisAction<Integer>() {
            @Override
            public Integer action(Jedis jedis) {
                String value = jedis.get(countKey);
                if(!Strings.isNullOrEmpty(value)){
                    return Integer.valueOf(value);
                }
                return -1;
            }
        });
    }

    /**
     * 根据redis的Key获取redis数据信息
     * @param signKey   签名数据的key
     * @param userId    签名用户
     * @return  Boolean 返回用户是否已经签名
     * 返回redis中的
     */
    private Boolean getSignValue(final String signKey , final String userId){
        return jedisTemplate.execute(new JedisTemplate.JedisAction<Boolean>() {
            @Override
            public Boolean action(Jedis jedis) {
                List<String> signValue = jedis.hmget(signKey , userId);

                if(signValue.isEmpty() || !Objects.equal(signValue.get(0),"1")){
                    return false;
                }

                return true;
            }
        });
    }

    /**
     * 通过key删除redis的对应数据
     * @param countKey  统计数据的key
     */
    public void deleteKey(final String countKey){
        jedisTemplate.execute(new JedisTemplate.JedisActionNoResult(){
            @Override
            public void action(Jedis jedis) {
                jedis.del(countKey);
            }
        });
    }
}
