package io.terminus.snz.user.tool;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.hash.Hashing;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * Author:Guo Chaopeng
 * Created on 14-8-4.
 */
public class SignaturesTest {

    @Test
    public void testSign() {
        Assert.assertNotNull(Signatures.sign("bh34524", 1));
    }

    @Test
    public void testVerify() {

        Map<String, String> params = Maps.newHashMap();
//        params.put("nick", "百卓测试5");
//        params.put("email", "e@qq.com");
//        params.put("mobile", "18969973054");
//        params.put("phone", "34456");
//        params.put("password", "123456");
//        params.put("accountType", "1");
//        params.put("contactName", "张三");
//        params.put("corporation", "海尔");
//        params.put("initAgent", "李四");
//        params.put("desc", "hahaa");
//        params.put("regCountry", "1");
//        params.put("productLine", "2");

        String restKey = "bh34524";
        buildParams(params, restKey);
        System.out.println(Hashing.md5().newHasher().putString(restKey,Charsets.UTF_8).hash().toString());

        Assert.assertTrue(Signatures.verify(params, restKey));

    }

    public void buildParams(Map<String, String> params, String restKey) {

        Map<String, String> sortParams = Maps.newTreeMap();

        //对参数进行升序排序
        for (String key : params.keySet()) {
            String value = params.get(key);
            //去掉空值和签名
            if (isValueEmptyOrSignRelatedKey(key, value)) {
                continue;
            }
            sortParams.put(key, value);
        }

        String toVerify = Joiner.on('&').withKeyValueSeparator("=").join(sortParams);

        String sign = Hashing.md5().newHasher()
                .putString(toVerify, Charsets.UTF_8)
                .putString(restKey, Charsets.UTF_8).hash().toString();

        params.put("sign", sign);

        System.out.println("the sign is:" + sign);

    }

    private static boolean isValueEmptyOrSignRelatedKey(String key, String value) {
        return Strings.isNullOrEmpty(value) || StringUtils.equalsIgnoreCase(key, "sign");
    }

}
