package io.terminus.snz.user.tool;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.util.UUID;

/**
 * 密码工具类
 * Author:Guo Chaopeng
 * Created on 14-6-13.
 */
public class PasswordUtil {

    private static final HashFunction SHA512 = Hashing.sha512();

    private static final Splitter SPLITTER = Splitter.on('@').trimResults();

    private static final Joiner JOINER = Joiner.on('@').skipNulls();

    private static final HashFunction MD5 = Hashing.md5();

    /**
     * 对密码进行加密
     *
     * @param password 原密码
     * @return 加密后的密码
     */
    public static String encryptPassword(String password) {
        String salt = MD5.newHasher().putString(UUID.randomUUID().toString(), Charsets.UTF_8).putLong(System.currentTimeMillis()).hash()
                .toString().substring(0, 4);
        String realPassword = SHA512.hashString(password + salt, Charsets.UTF_8).toString().substring(0, 20);
        return JOINER.join(salt, realPassword);
    }

    /**
     * 密码匹配
     *
     * @param password          明文密码
     * @param encryptedPassword 加密后的密码
     * @return 匹配是否成功
     */
    public static boolean passwordMatch(String password, String encryptedPassword) {
        Iterable<String> parts = SPLITTER.split(encryptedPassword);
        String salt = Iterables.get(parts, 0);
        String realPassword = Iterables.get(parts, 1);
        return Objects.equal(SHA512.hashString(password + salt, Charsets.UTF_8).toString().substring(0, 20), realPassword);
    }

}
