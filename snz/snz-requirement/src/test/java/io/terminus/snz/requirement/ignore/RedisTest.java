package io.terminus.snz.requirement.ignore;

import io.terminus.common.redis.utils.JedisTemplate;
import io.terminus.snz.requirement.manager.CountManager;
import io.terminus.snz.requirement.model.Module;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;

/**
 * Desc:
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-06-30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:spring/redis-context-test.xml"
})
public class RedisTest {
    @Autowired
    private JedisTemplate jedisTemplate;

    @Autowired
    private CountManager countManager;

    @Test
    public void Hello(){
        System.out.println("hello");
    }

    @Test
    public void destroy(){
        jedisTemplate.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                jedis.flushDB();
            }
        });
    }

    @Test
    public void testCreate(){
        Module module = new Module();
        module.setId(1l);
        module.setTotal(20000);
        countManager.createMEvent(1l , module);
    }

    @Test
    public void testUpdate(){
        Module newModule = new Module();
        newModule.setId(1l);
        newModule.setTotal(10000);

        Module oldModule = new Module();
        oldModule.setId(1l);
        oldModule.setTotal(15000);
        countManager.updateMEvent(1l , newModule, oldModule);
    }
}
