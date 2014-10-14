package io.terminus.snz.requirement.tool;

import com.google.common.base.*;
import com.google.common.collect.Maps;
import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Desc:与海尔对接的服务组件
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-06-24.
 */
@Slf4j
public class HttpServiceUtil {
    private static final String[] DEFAULT_KEYS = new String[]{"signValue" , "signType"};    //默认的一些参数信息

    public static final String SING_WAY = "MD5";

    private static final String SECRET_KEY = "73I86TK42W76AP02MICHAEL6ZEROS3KSD8S";            //默认密钥

    /**
     * 实现对于参数的绑定以及参数加密机制(将secretKey先在海尔那边有一份&数据都返回给)
     * @param params    需要加密的数据对象
     * @param ifSign    是否加密
     * @param secretKey 密钥
     * @return Map
     * 返回加密后的数据
     */
    public static Map<String , String> buildParams(Map<String , String> params , Boolean ifSign, String secretKey){
        Map<String , String> newParams = Maps.newHashMap();

        try{
            //对params进行排序处理
            if (!(params instanceof SortedMap)) {
                newParams = new TreeMap<String, String>(params);
            }else{
                newParams = params;
            }

            //构建url参数(过滤空数据)
            String value = Joiner.on("&").withKeyValueSeparator("=").join(Maps.filterValues(newParams , new Predicate<Object>() {
                @Override
                public boolean apply(@Nullable Object input) {
                    return input != null;
                }
            }));

            //加密
            if(ifSign){
                //只签名一次
                newParams.put("signValue" , sign(value+Objects.firstNonNull(secretKey , SECRET_KEY) , 1));
                newParams.put("signType", "MD5");
            }else{
                newParams.put("signValue" , sign(value , 1));
            }
        }catch(Exception e){
            log.error("build params failed, params={}, error code={}.", newParams, Throwables.getStackTraceAsString(e));
        }

        return newParams;
    }

    /**
     * 通过外部的请求信息验证签名是否被更改
     * @param params    签名参数
     * @param signWay   检查验证方式是否相同
     * @return  Boolean
     * 返回签名是否相同
     */
    public static Boolean checkSign(Map<String , String> params, String signWay){
        //没有加密
        if(params.get("signType") == null){
            return true;
        }else {
            //验证签名方法是否相同
            if (!Objects.equal(params.get("signType"), signWay)) {
                log.error("sign way is different.");
                return false;
            }

            //获取签名前的参数
            Map<String , String> paramValues = Maps.newHashMap(params);
            for(String key : DEFAULT_KEYS){
                paramValues.remove(key);
            }

            //验证数据是否在传输中有被更改
            if(!Objects.equal(buildParams(paramValues , true, SECRET_KEY).get("signValue") , params.get("signValue"))){
                log.error("request params have been changed.");
                return false;
            }

            return true;
        }
    }

    /**
     * 签名函数(递归函数)
     * @param value 待签名的数据
     * @param deep  需要几成加密
     * @return  String
     * 返回签名后的数据
     */
    private static String sign(String value , int deep){
        int deepParam = deep;
        return deepParam-- == 0 ? value : sign(Hashing.md5().newHasher().putString(value , Charsets.UTF_8).hash().toString() , deepParam);
    }
}
