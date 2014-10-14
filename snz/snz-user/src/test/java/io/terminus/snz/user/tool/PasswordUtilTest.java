package io.terminus.snz.user.tool;

import org.junit.Assert;
import org.junit.Test;

/**
 * Author:Guo Chaopeng
 * Created on 14-8-4.
 */
public class PasswordUtilTest {

    @Test
    public void testEncryptPassword() {
        String e = PasswordUtil.encryptPassword("123456");
        System.out.println(e);
        Assert.assertNotNull(e);
    }

    @Test
    public void testPasswordMatch() {
        String password = "123456";
        Assert.assertTrue(PasswordUtil.passwordMatch(password, PasswordUtil.encryptPassword(password)));
    }


}
