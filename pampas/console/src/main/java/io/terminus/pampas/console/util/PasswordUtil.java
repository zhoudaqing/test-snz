/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.console.util;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.util.UUID;

/**
 * Author:  <a href="mailto:i@terminus.io">jlchen</a>
 * Date: 2014-07-25
 */
public class PasswordUtil {

    private final static HashFunction sha512 = Hashing.sha512();

    private final static Splitter splitter = Splitter.on('@').trimResults();

    private final static Joiner joiner = Joiner.on('@').skipNulls();

    private final static HashFunction md5 = Hashing.md5();

    /**
     * 对密码进行加密
     *
     * @param password 原密码
     * @return 加密后的密码
     */
    public static String encryptPassword(String password) {
        String salt = md5.newHasher().putString(UUID.randomUUID().toString(), Charsets.UTF_8).putLong(System.currentTimeMillis()).hash()
                .toString().substring(0, 4);
        String realPassword = sha512.hashString(password + salt, Charsets.UTF_8).toString().substring(0, 20);
        return joiner.join(salt, realPassword);
    }

    /**
     * 密码匹配
     *
     * @param password          明文密码
     * @param encryptedPassword 加密后的密码
     * @return 匹配是否成功
     */
    public static boolean passwordMatch(String password, String encryptedPassword) {
        Iterable<String> parts = splitter.split(encryptedPassword);
        String salt = Iterables.get(parts, 0);
        String realPassword = Iterables.get(parts, 1);
        return Objects.equal(sha512.hashString(password + salt, Charsets.UTF_8).toString().substring(0, 20), realPassword);
    }

}
