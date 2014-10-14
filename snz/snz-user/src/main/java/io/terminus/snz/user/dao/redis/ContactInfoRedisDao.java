package io.terminus.snz.user.dao.redis;

import io.terminus.common.redis.utils.JedisTemplate;
import io.terminus.snz.user.model.ContactInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Author:Guo Chaopeng
 * Created on 14-8-26.
 */
@Repository
public class ContactInfoRedisDao extends SupplierBaseRedisDao<ContactInfo> {

    @Autowired
    public ContactInfoRedisDao(JedisTemplate jedisTemplate) {
        super(jedisTemplate);
    }

}
