package io.terminus.snz.statistic.dao;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Desc:基础测试类
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-02.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/redis-context-test.xml"})
public abstract class BasicTest {
}
