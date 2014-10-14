package io.terminus.snz.message.daos.redis;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.terminus.common.redis.utils.JedisTemplate;
import io.terminus.common.redis.utils.RedisClient;
import io.terminus.snz.message.models.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Redis消息Dao
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-6-7
 */
@Repository
public class RedisMessageDao {
    @Autowired
    private JedisTemplate jedisTemplate;

    /**
     * 添加新消息给用户, 此时要向用户未读消息列表和总消息列表中都添加该消息id
     * @param mid 消息id
     * @param uid 用户id
     * @return 插入记录数
     */
    public Long addNewMsg(final Long mid, final Long uid){
        return addNewMsg(mid, Arrays.asList(uid));
    }

    /**
     * 添加新消息给用户列表, 此时要向用户未读消息列表和总消息列表中都添加该消息id
     * @param mid 消息id
     * @param uids 用户列表id
     * @return 添加记录数
     */
    public Long addNewMsg(final Long mid, final List<Long> uids){
        String keyNew;
        String keyAll;
        List<String> allKeys = Lists.newArrayList();
        for (Long uid : uids){
            keyNew = Message.keyOfNewMsg(uid);
            keyAll = Message.keyOfAllMsg(uid);
            allKeys.add(keyNew);
            allKeys.add(keyAll);
        }
        String val = String.valueOf(mid);
        return RedisClient.listAdd(jedisTemplate, allKeys, val);
    }

    /**
     * 从某用户新消息队列中移除新消息
     * @param mid 消息id
     * @param uid 用户id
     * @return 移除消息数
     */
    public Long rmNewMsg(final Long mid, final Long uid){
        String keyNew = Message.keyOfNewMsg(uid);
        return RedisClient.listRemOne(jedisTemplate, keyNew, mid);
    }

    /**
     * 获取用于未读消息数
     * @param uid 用户id
     * @return 用户未读消息数
     */
    public Long getNewMsgsCount(final Long uid){
        String key = Message.keyOfNewMsg(uid);
        return RedisClient.listLen(jedisTemplate, key);
    }

    /**
     * 获取用户未读消息id列表
     * @param uid 用户id
     * @param offset 起始偏移
     * @param limit 分页大小
     * @return 用户未读消息id列表
     */
    public List<Long> getNewMsgIds(final Long uid, final Integer offset, final Integer limit){
        String keyNewMsg = Message.keyOfNewMsg(uid);
        return RedisClient.listPaging2Long(jedisTemplate, keyNewMsg, offset,limit);
    }

    /**
     * 获取用户所有的未读消息id列表2014-08-08 15:20:17.895:WARN:oejs.ServletHandler:/api/msgs/newMsgCount
     io.terminus.pampas.common.UserNotLoginException: user not login.
     * @param uid 用户id
     * @return 用户所有的未读消息id列表
     */
    public List<Long> getNewMsgIds(final Long uid){
        String keyNewMsg = Message.keyOfNewMsg(uid);
        return RedisClient.listAll2Long(jedisTemplate, keyNewMsg);
    }

    /**
     * 获取用于总消息数
     * @param uid 用户id
     * @return 用户总消息数
     */
    public Long getAllMsgsCount(final Long uid){
        String key = Message.keyOfAllMsg(uid);
        return RedisClient.listLen(jedisTemplate, key);
    }

    /**
     * 获取用户总消息id列表
     * @param uid 用户id
     * @param offset 起始偏移
     * @param limit 分页大小
     * @return 用户总消息id列表
     */
    public List<Long> getAllMsgIds(final Long uid, final Integer offset, final Integer limit){
        String keyAllMsg = Message.keyOfAllMsg(uid);
        return RedisClient.listPaging2Long(jedisTemplate, keyAllMsg, offset,limit);
    }

    /**
     * 从用户的未读消息列表和总消息列表移除某消息id
     * @param userIds 用户id列表
     * @param id 消息id
     * @return 删除记录数
     */
    public Long rmNewAndAllMsg(final List<Long> userIds, final Long id) {
        List<String> allKeys = Lists.newArrayList();
        String newMsgKey;
        String allMsgKey;
        for (Long userId : userIds){
            newMsgKey = Message.keyOfNewMsg(userId);
            allMsgKey = Message.keyOfAllMsg(userId);
            allKeys.add(newMsgKey);
            allKeys.add(allMsgKey);
        }
        String mid = String.valueOf(id);
        return RedisClient.listRemOne(jedisTemplate, allKeys, mid);
    }

    /**
     * 批量移除用户的消息(包括未读消息列表和总消息列表)
     * @param uid 用户id
     * @param ids 消息id列表
     * @return 移除数
     */
    public Long rmMsgs(Long uid, List<Long> ids){
        List<String> keys = Lists.newArrayList();
        Map<String, List<?>> keyVals = Maps.newHashMap();
        // 未读列表
        String msgKey = Message.keyOfNewMsg(uid);
        keys.add(msgKey);
        keyVals.put(msgKey, ids);
        // 总列表
        msgKey = Message.keyOfAllMsg(uid);
        keys.add(msgKey);
        keyVals.put(msgKey, ids);
        return RedisClient.listRem(jedisTemplate, keys, keyVals);
    }

    /**
     * 移除用户的未读消息
     * @param uid 用户id
     * @param ids 消息id列表
     * @return 移除数
     */
    public Long rmNewMsgs(Long uid, List<Long> ids) {
        String key = Message.keyOfNewMsg(uid);
        return RedisClient.listRem(jedisTemplate, key, ids);
    }
}
