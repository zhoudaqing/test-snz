package io.terminus.snz.requirement.dao;

import com.google.common.base.Strings;
import io.terminus.common.redis.utils.JedisTemplate;
import io.terminus.snz.requirement.manager.REKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.List;

/**
 * 根据中间表 depart id 获取一个 depart name
 *
 * Date: 8/20/14
 * Time: 11:44
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */

@Component
public class MWDepartNameDao {

    @Autowired
    private JedisTemplate jedisTemplate;

    public String getDepartName(final String code) {
        return jedisTemplate.execute(new JedisTemplate.JedisAction<String>() {
            @Override
            public String action(Jedis jedis) {
                if (Strings.isNullOrEmpty(code)) {
                    return null;
                }

                return jedis.get(REKeyUtil.mwDepartKey(code));
            }
        });
    }

    public List<String> getDepartCode(final String name) {
        return jedisTemplate.execute(new JedisTemplate.JedisAction<List<String>>() {
            @Override
            public List<String> action(Jedis jedis) {
                if (Strings.isNullOrEmpty(name)) {
                    return Collections.emptyList();
                }

                return jedis.lrange(REKeyUtil.mwBusinessKey(name), 0, -1);
            }
        });
    }

    public String getVCode(final String fac, final String num) {
        return jedisTemplate.execute(new JedisTemplate.JedisAction<String>() {
            @Override
            public String action(Jedis jedis) {
                if (Strings.isNullOrEmpty(fac) || Strings.isNullOrEmpty(num)) {
                    return null;
                }
                return jedis.get(REKeyUtil.mwFacModuleKey(fac, num));
            }
        });
    }
}
