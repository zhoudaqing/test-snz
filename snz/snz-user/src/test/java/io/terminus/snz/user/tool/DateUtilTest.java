package io.terminus.snz.user.tool;

import org.junit.Assert;
import org.junit.Test;

/**
 * Author:Guo Chaopeng
 * Created on 14-8-4.
 */
public class DateUtilTest {

    @Test
    public void testGetCurrentYearFirstMonth() {
        Assert.assertNotNull(DateUtil.getCurrentYearFirstMonth());
    }

    @Test
    public void testGetCurrentYearLastMonth() {
        Assert.assertNotNull(DateUtil.getCurrentYearLastMonth());
    }

    @Test
    public void testGetYesterdayStart() {
        Assert.assertNotNull(DateUtil.getYesterdayStart());
    }

}
