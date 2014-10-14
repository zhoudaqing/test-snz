package io.terminus.snz.user.dao.redis;

import io.terminus.common.redis.utils.JedisTemplate;
import io.terminus.snz.user.model.CompanyExtraDelivery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Author:Guo Chaopeng
 * Created on 14-8-29.
 */
@Repository
public class CompanyExtraDeliveryRedisDao extends SupplierBaseRedisDao<CompanyExtraDelivery> {

    @Autowired
    public CompanyExtraDeliveryRedisDao(JedisTemplate jedisTemplate) {
        super(jedisTemplate);
    }
}
