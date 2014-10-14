package io.terminus.snz.user.dao.redis;

import io.terminus.common.redis.utils.JedisTemplate;
import io.terminus.snz.user.dto.SupplierCountByDimension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Author:Guo Chaopeng
 * Created on 14-9-5.
 */
@Repository
public class SupplierCountByDimensionRedisDao extends SupplierBaseRedisDao<SupplierCountByDimension> {

    @Autowired
    public SupplierCountByDimensionRedisDao(JedisTemplate jedisTemplate) {
        super(jedisTemplate);
    }

}
