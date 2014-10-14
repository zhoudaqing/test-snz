package io.terminus.snz.user.tool;

import org.junit.Assert;
import org.junit.Test;

/**
 * Author:Guo Chaopeng
 * Created on 14-8-4.
 */
public class CountryCertificateTest {

    @Test
    public void testCheck() {
        Integer type = CountryCertificate.check(1);
        Assert.assertEquals(type.intValue(), 3);
    }

}
