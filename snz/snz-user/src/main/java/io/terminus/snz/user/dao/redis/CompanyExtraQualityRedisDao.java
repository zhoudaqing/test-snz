package io.terminus.snz.user.dao.redis;

import io.terminus.common.redis.utils.JedisTemplate;
import io.terminus.snz.user.model.CompanyExtraQuality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Author:Guo Chaopeng
 * Created on 14-8-26.
 */
@Repository
public class CompanyExtraQualityRedisDao extends SupplierBaseRedisDao<CompanyExtraQuality> {

    @Autowired
    public CompanyExtraQualityRedisDao(JedisTemplate jedisTemplate) {
        super(jedisTemplate);
    }

}
