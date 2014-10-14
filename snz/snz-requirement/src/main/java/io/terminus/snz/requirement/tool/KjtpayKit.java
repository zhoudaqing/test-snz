package io.terminus.snz.requirement.tool;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.common.base.*;
import com.google.common.collect.Maps;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;
import io.terminus.snz.requirement.dto.KjtTransDto;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 6/16/14
 */
@Slf4j
public class KjtpayKit {

    private static final String VERSION = "v1.0";

//    private static final String GATEWAY_URL = "http://testgw.kjtpay.com/netbankpay/req/gatewayTransRequest";

//    真实环境
    private static final String GATEWAY_URL = "http://b2bwy.kjtpay.com/netbankpay/req/gatewayTransRequest"; // B2B
//    private static final String GATEWAY_URL = "https://b2cwy.kjtpay.com/netbankpay/req/gatewayTransRequest"; // B2C
    private static final String TRANS_GATEWAY_URL = "http://b2cwy.kjtpay.com/netbankpay/req/gatewayTransRequest";

    // 线上环境
    private static final String NOTIFY_URL = "http://l.ihaier.com/api/kjtpay/notify/pay";
    private static final String RETURN_URL = "http://l.ihaier.com/pay_error";
    private static final String NOTIFY_TRANS_URL = "http//l.ihaier.com/api/kjtpay/notify/trans";

//    private static final String NOTIFY_URL = "http://requestb.in/1f865mc1";
//    private static final String RETURN_URL = "http://localhost:8080/pay_error";
//    private static final String NOTIFY_TRANS_URL = "http//localhost:8080/api/kjtpay/notify/trans";

//    private static final String SELLER_PID = "1120140106145014976";
//    private static final String SECURITY_KEY = "73I86TK42W76AP02VDTMO6SRBWPJ3K8S";
    private static final String SELLER_PID = "1120140630182333682"; // 真实账户
    private static final String SECURITY_KEY = "7EACSGLOK9HV3QRLK4HH59HYLSCIL8S1";

    public static final DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyyMMddHHmmss");

    public static final String PAY_SUCCESS_CODE = "0000";
    public static final String REFUND_SUCCESS_CODE = "99";

    public enum ErrCode {
        UNKNOWN("00000", "未知错误"),
        ENCODING_ERROR("10001", "不支持的字符编码格式,系统支持的字符编码格式为1.UTF-8,2.GBK,3.GB2312"),
        NOTIFY_URL_ERROR("10002", "页面返回地址和后台返回地址不能同时为空,请使用符合URL规则的http或者https地址"),
        GW_VERSION_ERROR("10003", "不支持的网关接口版本号,目前系统支持的版本号为v1.0"),
        PID_ERROR("10004", "合作商户号错误"),
        CONTACT_TYPE_ERROR("10005", "不支持的顾客联系方式,系统支持的联系方式为1.电子邮件,2.电话.当联系内容不为空时联系方式 不能为空."),
        CONTACT_ERROR("10006", "顾客的联系方式不正确,请输入合法的联系方式"),
        ORDER_ID_ERROR("10007", "订单号不正确,系统只支持以字母,数字组合的订单号,最大长度不能超过30"),
        ORDER_AMOUNT_ERROR("10008", "订单金额不正确,请输入以分为单位的金额"),
        ORDER_TIME_ERROR("10009", "订单提交时间不正确,请输入以yyyyMMddhhmmss格式的时间字符串"),
        PAY_TYPE_ERROR("10010", "指定的支付方式不正确"),
        BANK_CODE_ERROR("10011", "指定的银行ID不正确"),
        SIGN_TYPE_ERROR("10012", "不支持的签名类型,系统支持的签名类型为1.MD5"),
        SIGN_MSG_EMPTY("10013", "商户签名数据不能为空"),
        SIGN_ERROR("10014", "签名错误"),
        PAY_FAIL("10015", "银行支付失败"),
        ORDER_AMOUNT_LIMIT("10016", "商户交易金额已超过限制"),
        BANK_CARD_ERROR("10017", "卡类型错误"),
        PAY_AMOUNT_ERROR("10018", "支付金额必须大于费率值"),
        ALREADY_PAY_SUCCESS("20001", "该订单已支付成功,请勿重复支付");

        private final String value;
        private final String description;

        private ErrCode(String value, String description) {
            this.value = value;
            this.description = description;
        }

        public static ErrCode from(String value) {
            for(ErrCode errCode : ErrCode.values()){
                if(Objects.equal(errCode.value, value)) {
                    return errCode;
                }
            }
            return null;
        }

        public String value() {
            return value;
        }

        @Override
        public String toString() {
            return description;
        }
    }

    /**
     * 对参数进行签名
     * @param params 待签名的参数列表
     * @return 已签名的参数列表
     */
    public static Map<String, String> sign(Map<String, String> params) {
        Map<String, String> neoParams = new TreeMap<String, String>(params);
        try {
            String toSign = Joiner.on("&").withKeyValueSeparator("=").join(params);
            String signedStr = Hashing.md5().newHasher()
                    .putString(toSign, Charsets.UTF_8)
                            // append md5 of security key
                    .putString("&" + Hashing.md5().hashString(SECURITY_KEY, Charsets.UTF_8).toString()
                            , Charsets.UTF_8)
                    .hash().toString();

            String base64Str = BaseEncoding.base64().encode(signedStr.getBytes(Charsets.UTF_8));
            neoParams.put("signType", "MD5");
            neoParams.put("signMsg", base64Str);
        } catch (Exception e) {
            log.error("sign(params={}) failed, error code={}", params, Throwables.getStackTraceAsString(e));
            throw new RuntimeException(e);
        }
        return neoParams;
    }

    /**
     * 对参数签名进行验证
     * @param params 待验证的参数列表
     * @return 签名是否正确
     */
    public static boolean verify(Map<String, String> params) {
        try {
            String signMsg = params.get("signMsg");
            Map<String, String> paramsNonMsg = Maps.newTreeMap(String.CASE_INSENSITIVE_ORDER);
            for (Map.Entry<String, String> param : params.entrySet()){
                String paramKey = param.getKey();
                String paramVal = param.getValue();
                if (!"signType".equals(paramKey) && !"signMsg".equals(paramKey)){
                    paramsNonMsg.put(paramKey, paramVal);
                }
            }
            return Objects.equal(sign(paramsNonMsg).get("signMsg"), signMsg);
        } catch (Exception e) {
            log.error("verify(params={}) failed, error code={}", params, Throwables.getStackTraceAsString(e));
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建交易请求的表单
     * @param orderId 订单号（保证金id, String）
     * @param orderAmount 订单金额（保证金金额）
     * @param orderTime 订单生成时间（YYYYmmDDHHMMSS）
     * @param productName 产品名称
     * @return 创建的表单（待签名）
     */
    public static Map<String, String> buildPayRequest(String orderId, Long orderAmount, Date orderTime, String productName) {
        Map<String, String> params = Maps.newTreeMap(String.CASE_INSENSITIVE_ORDER);
        try {
            params.put("inputCharset", "1"); // UTF-8
            params.put("pid", SELLER_PID);
            params.put("returnUrl", RETURN_URL);
            params.put("notifyUrl", NOTIFY_URL);
            params.put("version", VERSION);
            params.put("bankCode", "");
            params.put("cardType", "0");
            params.put("payerName", "模块商");
            params.put("payerContactType", "");
            params.put("payerContact", "");
            params.put("payerIp", "");
            params.put("orderId", orderId);
            params.put("orderAmount", orderAmount.toString());
            params.put("orderTime", dateFormat.print(new DateTime(orderTime)));
            params.put("productName", productName);
            params.put("payType", "02");    // B2B
//            params.put("payType", "00");
            params.put("ext1", "");
            params.put("ext2", "");
        } catch (Exception e) {
            log.error("buildPayRequest(orderId={}, orderAmount={}, orderTime{}, productName{}, prefix={}) failed, error code={}",
                    orderId, orderAmount, orderTime, productName, Throwables.getStackTraceAsString(e));
            throw new RuntimeException(e);
        }
        return params;
    }

    /**
     * 创建退款交易请求表单
     * @param orderId         订单号（String）
     * @param amount          退款金额
     * @param kjtTransDto     退款银行卡信息
     * @return 创建的表单（待签名）
     */
    public static Map<String, String> buildTransRequest(String orderId, Long amount, KjtTransDto kjtTransDto) {
        Map<String, String> params = Maps.newTreeMap(String.CASE_INSENSITIVE_ORDER);
        try {
            // 协议参数
            params.put("inputCharset", "1"); // UTF-8
            params.put("pid", SELLER_PID);
            params.put("version", VERSION);
            // 业务参数
            params.put("account_type", kjtTransDto.getAccountType().toString());
            params.put("orderid", orderId);
            params.put("peername", kjtTransDto.getPeerName());
            params.put("amount", amount.toString());
            params.put("bankname", kjtTransDto.getBankName());
            params.put("cardnum", kjtTransDto.getCardNum());
            params.put("province", kjtTransDto.getProvince());
            params.put("city", kjtTransDto.getCity());
            params.put("peerbankname", kjtTransDto.getPeerBankName());
            params.put("remark", Strings.nullToEmpty(kjtTransDto.getRemark()));
            params.put("notifyurl", NOTIFY_TRANS_URL);
            params.put("overaduit", "1"); // 1: 跳过审核, 0: 不跳过
        } catch (Exception e) {
            log.error("buildTransRequest(orderId={}, amount={}, kjtTransDto={}) failed, error code={}",
                    orderId, amount, kjtTransDto, Throwables.getStackTraceAsString(e));
            throw new RuntimeException(e);
        }
        return params;
    }

    public static KjtTransDto buildTransResponse(Map<String, String> params) {
//        String orderId = params.get("orderid");
//        Integer amount = Integer.parseInt(params.get("amount"));

        KjtTransDto kjtTransDto = new KjtTransDto();

        kjtTransDto.setAccountType(Integer.parseInt(params.get("account_type")));
        kjtTransDto.setPeerName(params.get("peername"));
        kjtTransDto.setBankName(params.get("bankname"));
        kjtTransDto.setCardNum(params.get("cardnum"));
        kjtTransDto.setProvince(params.get("province"));
        kjtTransDto.setCity(params.get("city"));
        kjtTransDto.setPeerBankName(params.get("peerbankname"));
        kjtTransDto.setRemark(params.get("remark"));

        return kjtTransDto;
    }

    /**
     * 提交交易请求
     * @param params POST给快捷通支付网关的交易表单
     * @return 交易网址（须跳转到快捷通的支付网页进行支付）
     */
    public static String request(Map<String, String> params) {
        try {
            log.warn("request to kjt for pay, url={}, params={}", GATEWAY_URL, params);
            HttpRequest request = HttpRequest.post(GATEWAY_URL).form(params);
//            if (request.ok()) {
//                return request.url().toString();
            if (request.code() == 302) {
                return request.header("Location");
            } else {
                throw new Exception("Kjtpay request post failed");
            }
        } catch (Exception e) {
            log.error("pay request(params={}) failed, error code={}", params, Throwables.getStackTraceAsString(e));
            throw new RuntimeException(e);
        }
    }

    public static boolean requestTrans(Map<String, String> params) {
        try {
            log.warn("request to kjt for trans, url={}, params={}", GATEWAY_URL, params);
            HttpRequest request = HttpRequest.post(TRANS_GATEWAY_URL).form(params);
            return request.ok();
        } catch (Exception e) {
            log.error("trans request(params={}) failed, error code={}", params, Throwables.getStackTraceAsString(e));
            throw new RuntimeException(e);
        }
    }
}
