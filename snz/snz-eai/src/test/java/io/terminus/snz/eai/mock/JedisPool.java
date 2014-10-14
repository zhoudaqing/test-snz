package io.terminus.snz.eai.mock;

import org.mockito.Mockito;
import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

/**
 * Date: 8/6/14
 * Time: 10:30
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */
public class JedisPool<T extends Jedis> extends Pool<Jedis> {
    @Override
    public Jedis getResource() {
        return Mockito.mock(Jedis.class);
    }

    @Override
    public void returnResourceObject(Jedis resource) {
    }

    @Override
    public void returnBrokenResource(Jedis resource) {
    }

    @Override
    public void returnResource(Jedis resource) {
    }

    @Override
    public void destroy() {
    }

    @Override
    protected void returnBrokenResourceObject(Jedis resource) {
    }

    @Override
    protected void closeInternalPool() {
    }
}
