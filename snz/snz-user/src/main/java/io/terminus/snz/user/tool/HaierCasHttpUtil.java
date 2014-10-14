package io.terminus.snz.user.tool;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import io.terminus.common.utils.JsonMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 7/31/14
 */
@Slf4j
public class HaierCasHttpUtil {

    private static final String CAS_SERVICE = "http://user.ihaier.com";
//    private static final String CAS_SERVICE = "http://local.cas.com:8080/";
    private static final String CAS_PASSWORD = "sJiD9scV6C5Qaz7ZgJVV"; // 每次推送必须的加载密钥

    private static final JsonMapper JSON_MAPPER = JsonMapper.nonDefaultMapper();

    public static String request(String url, Map<String, String> params) {
        try {
            HttpRequest request = HttpRequest.get(url).form(params);
            log.info("haier cas request(url={}, params={})", url, params);
            return request.body();
        } catch (Exception e) {
            log.error("request(url={}, params={}) failed, error code={}",
                    url, params, Throwables.getStackTraceAsString(e));
            throw new RuntimeException(e);
        }
    }

    public static String register(String username, String email, String passwd) {
        log.debug("haier cas register(username={}, email={}, passwd={})", username, email, passwd);
        Map<String, String> params = Maps.newHashMap();
        params.put("username", username);
        params.put("email", email);
        params.put("passwd", passwd);
        params.put("caspd", CAS_PASSWORD);
        String result = request(CAS_SERVICE + "/registersso", params);
        log.debug("haier cas register result={}", result);

        ErrorCode map = JSON_MAPPER.fromJson(result, ErrorCode.class);
        if (map.getError() != 0) {
            log.error("haier cas register(username={}, email={}, passwd={}) failed, code:{}, msg:{}",
                    username, email, passwd, map.getCode(), map.getMsg());
            return map.getMsg();
        }
        return "ok";
    }

    public static String changePassword(String username, String oldPassword, String newPassword) {
        log.debug("haier cas changePassword(username={}, oldPassword={}, newPassword={})", username, oldPassword, newPassword);
        Map<String, String> params = Maps.newHashMap();
        params.put("username", username);
        params.put("opasswd", oldPassword);
        params.put("npasswd", newPassword);
        params.put("source", "CAS");
        params.put("force", "0");
        params.put("caspd", CAS_PASSWORD);
        String result = request(CAS_SERVICE + "/passportofname", params);

        ErrorCode map = JSON_MAPPER.fromJson(result, ErrorCode.class);
        if (map.getError() != 0) {
            log.error("haier cas changePassword(username={}, oldPassword={}, newPassword={}) failed, code:{}, msg:{}",
                    username, oldPassword, newPassword, map.getCode(), map.getMsg());
            return map.getMsg();
        }
        return "ok";
    }

    public static class ErrorCode {

        private Integer error;
        private Integer code;
        private String msg;

        public Integer getError() {
            return error;
        }

        public Integer getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }
}
