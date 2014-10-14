package io.terminus.snz.user.util;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.hash.Hashing;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Author:Guo Chaopeng
 * Created on 14-7-8.
 */
public class Signatures {

    public static String sign(String toVerify, int deep) {
        if (deep == 0) {
            return toVerify;
        }
        String expect = Hashing.md5().newHasher().putString(toVerify, Charsets.UTF_8).hash().toString();
        return sign(expect, deep - 1);
    }

    /**
     * 验证签名
     *
     * @param params  请求参数
     * @param restKey 密钥
     * @return 校验通过
     */
    public static boolean verify(Map<String, String> params, String restKey) {

        Map<String, String> sortParams = Maps.newTreeMap();

        //对参数进行升序排序
        for (Map.Entry<String, String> param : params.entrySet()) {
            //去掉空值和签名
            if (isValueEmptyOrSignRelatedKey(param.getKey(), param.getValue())) {
                continue;
            }
            sortParams.put(param.getKey(), param.getValue());
        }

        String toVerify = Joiner.on('&').withKeyValueSeparator("=").join(sortParams);

        String expect = Hashing.md5().newHasher()
                .putString(toVerify, Charsets.UTF_8)
                .putString(restKey, Charsets.UTF_8).hash().toString();

        String sign = params.get("sign");

        return Objects.equal(expect, sign);
    }

    private static boolean isValueEmptyOrSignRelatedKey(String key, String value) {
        return Strings.isNullOrEmpty(value) || StringUtils.equalsIgnoreCase(key, "sign");
    }

}
