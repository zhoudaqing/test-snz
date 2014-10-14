package io.terminus.snz.statistic.mananer;

import io.terminus.snz.statistic.manager.STKeyUtil;
import org.junit.Test;

/**
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-8-5
 */
public class STKeyUtilTest {

    @Test
    public void testAll(){
        STKeyUtil.reqInfoCountKey(1L);
        STKeyUtil.reqTopicCountKey(1L);
        STKeyUtil.solCountKey(2L);
        STKeyUtil.reqCountKey(22L);
    }
}
