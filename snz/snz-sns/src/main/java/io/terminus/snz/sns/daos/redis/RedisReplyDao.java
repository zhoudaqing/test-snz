package io.terminus.snz.sns.daos.redis;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import io.terminus.common.redis.utils.JedisTemplate;
import io.terminus.common.redis.utils.RedisClient;
import io.terminus.snz.requirement.model.Requirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-7-24
 */
@Repository
public class RedisReplyDao {
    @Autowired
    private JedisTemplate jedisTemplate;

    /**
     * 增加供应商id到对应的需求下
     * @param rid 需求id
     * @param uid 用户id
     * @return 添加记录数
     */
    public Long incrRequirementSuppliers(Long rid, Long uid){
        String key = Requirement.keyOfSuppliers(rid);
        return RedisClient.setAdd(jedisTemplate, key, String.valueOf(uid));
    }

    /**
     * 获取多个需求的参与供应商数
     * @param rids 需求id列表
     * @return 对应的需求列表的参与供应商数
     */
    public List<Long> countRequirementSuppliers(List<Long> rids){
        List<String> keys = Lists.transform(rids, new Function<Long, String>() {
            @Override
            public String apply(Long rid) {
                return Requirement.keyOfSuppliers(rid);
            }
        });
        return RedisClient.setCounts(jedisTemplate, keys);
    }

}
