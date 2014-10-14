package io.terminus.snz.user.dao.redis;

import com.google.common.base.Strings;
import io.terminus.common.redis.utils.JedisTemplate;
import io.terminus.common.utils.JsonMapper;
import io.terminus.snz.user.dto.CompanyDto;
import io.terminus.snz.user.tool.StashSupplierInfoKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

/**
 * Author:Guo Chaopeng
 * Created on 14-8-26.
 */
@Repository
public class CompanyRedisDao {

    @Autowired
    private JedisTemplate jedisTemplate;

    public void saveBaseCompanyInfo(final Long userId, CompanyDto companyDto) {
        final String baseCompanyJson = JsonMapper.JSON_NON_EMPTY_MAPPER.toJson(companyDto);
        jedisTemplate.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                jedis.set(StashSupplierInfoKeys.company(userId), baseCompanyJson);
            }
        });
    }

    public CompanyDto getBaseCompanyInfo(final Long userId) {
        String baseCompanyJson = jedisTemplate.execute(new JedisTemplate.JedisAction<String>() {
            @Override
            public String action(Jedis jedis) {
                return jedis.get(StashSupplierInfoKeys.company(userId));
            }
        });

        if (Strings.isNullOrEmpty(baseCompanyJson)) {
            return null;
        }

        CompanyDto companyDto = JsonMapper.JSON_NON_EMPTY_MAPPER.fromJson(baseCompanyJson, CompanyDto.class);
        return companyDto;
    }

    public void removeBaseCompanyInfo(final Long userId) {
        jedisTemplate.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                jedis.del(StashSupplierInfoKeys.company(userId));
            }
        });
    }


}
