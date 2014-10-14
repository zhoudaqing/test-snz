package io.terminus.snz.sms.haier;


import com.github.kevinsawicki.http.HttpRequest;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import io.terminus.pampas.common.Response;
import io.terminus.snz.message.models.SmsRequest;
import io.terminus.snz.message.models.SmsRequestBody;
import io.terminus.snz.message.models.SmsRequestMessage;
import io.terminus.snz.message.models.SmsResponse;
import io.terminus.snz.message.services.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

import static com.google.common.base.Preconditions.*;

/**
 * Author:  <a href="mailto:remindxiao@gmail.com">xiao</a>
 * Date: 2013-12-18
 */
@Service
public class HaierSmsServiceImpl implements SmsService {
    private final static Logger log = LoggerFactory.getLogger(HaierSmsServiceImpl.class);

    //短信长度阈值
    private static final int MSG_MAX_LEN = 600;

    private static final String DEFAULT_SMS_TYPE = "";

    private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"GBK\"?>\n";

    @Value("#{app.operId}")
    private String operId;
    @Value("#{app.operPass}")
    private String operPass;
    @Value("#{app.smsRequestUrl}")
    private String smsRequestUrl;

    public HaierSmsServiceImpl() {
    }

    //constructor for mock
    public HaierSmsServiceImpl(String operId, String operPass, String smsRequestUrl) {
        this.operId = operId;
        this.operPass = operPass;
        this.smsRequestUrl = smsRequestUrl;
    }

    /**
     * 发送单条信息: <br/>
     * <p>
     * 普通短信与长短信是两种提交模式。<br/>
     * 普通短信目前长度阀值为67个字，超过该字数长度将被系统拆分，但发送到手机上仍然显示为一条完整短信；<br/>
     * 短信内容的最大长度无限制，但考虑到短信接收成功率问题，<br/>
     * 短信总长度按照长短信拆分后不要超过10条(长短信按67个字进行拆分)<br/>
     * 短信编码方式采用GBK
     * </p>
     *
     * @param from    发送方手机
     * @param to      接收方手机号码
     * @param message 消息体
     * @throws io.terminus.snz.sms.exception.SmsException 异常
     */
    @Override
    public Response<Boolean> sendSingle(String from, String to, String message) {
        Response<Boolean> response = new Response<Boolean>();
        response.setSuccess(false);
        response.setResult(false);
        try {
            //检查入参
            checkNotNull(message, "message can not be null");
            checkNotNull(from, "message sender can not be null");
            checkNotNull(to, "message receiver can not be null");


            int messageLength = message.length();
            checkArgument(messageLength <= MSG_MAX_LEN, "message too long: %s", messageLength);

            String messageType = getMessageType(message);

            if (log.isDebugEnabled()) {
                log.debug("sender:{},receiver:{},message:{}", from, to, message);
            }

            //发送短信
            SmsResponse smsResponse = sendMessage(from, to, message, messageType);

            checkState(smsResponse != null, "sms.ack.empty");
            checkState(smsResponse.getBody() != null, "sms.ack.empty");

            String code = smsResponse.getBody().getCode() == null ? "NULL" : smsResponse.getBody().getCode();
            checkState("0".equals(smsResponse.getBody().getCode()), "sms.ack.error:" + code);

            response.setSuccess(Boolean.TRUE);
            response.setResult(Boolean.TRUE);

        } catch (IllegalStateException e) {
            log.error("sms sendSingle raise exception {}", e.getMessage());
            response.setError(e.getMessage());
        } catch (Exception e) {
            log.error("sms sendSingle raise exception {}", e);
            response.setError("sms.send.fail");
        }

        return response;
    }


    /**
     * 群发信息:<br/>
     * <p>
     * 群发短信单次请求无数量上限，但考虑到效率问题，建议群发短信单次请求数不要超过200条
     * </p>
     *
     * @param from    发送方手机
     * @param to      接受方手机号码列表
     * @param message 消息体
     */
    @Override
    public Response<Boolean> sendGroup(String from, Iterable<String> to, String message) {
        Response<Boolean> response = new Response<Boolean>();
        response.setSuccess(false);
        response.setResult(true);

        try {
            //检查入参
            checkNotNull(message, "message can not be null");
            checkNotNull(from, "message sender can not be null");
            checkNotNull(to, "message receiver can not be null");


            String messageType = getMessageType(message);

            if (log.isDebugEnabled()) {
                log.debug("receiver:{},message:{}", to, message);
            }

            //发送短信
            String receiver = Joiner.on(",").skipNulls().join(to);
            SmsResponse smsResponse = sendMessage(from, receiver, message, messageType);

            checkState(smsResponse != null, "sms.ack.empty");
            checkState(smsResponse.getBody() != null, "sms.ack.empty");

            String code = smsResponse.getBody().getCode() == null ? "NULL" : smsResponse.getBody().getCode();
            checkState("0".equals(smsResponse.getBody().getCode()), "sms.ack.error:" + code);

            response.setSuccess(Boolean.TRUE);
            response.setResult(Boolean.TRUE);

        } catch (IllegalStateException e) {
            log.error("sms sendSingle raise exception {}", e.getMessage());
            response.setError(e.getMessage());
        } catch (Exception e) {
            log.error("sms sendSingle raise exception {}", e);
            response.setError("sms.send.fail");
        }

        return response;
    }

    /**
     * 通过短信模板发送短信
     *
     * @param from    发送方
     * @param to      接收方
     * @param context 模板数据上下文
     * @return 发送结果
     */
    @Override
    public Response<Boolean> sendWithTemplate(String from, String to, String templateFile, Object context) {
        Response<Boolean> resp = new Response<Boolean>();
        try {
            String message = SmsTemplates.build(templateFile, context);
            return sendSingle(from, to, message);
        } catch (Exception e) {
            resp.setError("短信发送失败:" + e.getMessage());
            return resp;
        }
    }


    //根据当前消息长度判断当前消息类型
    //大于67小于200为长消息，小于67为普通消息，否则为非法消息
    private String getMessageType(String message) {
        int messageLength = message.length();
        checkArgument(messageLength <= MSG_MAX_LEN, "message too long: %s", messageLength);

        //默认以普通短信发送,海尔短信平台会自动根据长度调整消息类型
        return DEFAULT_SMS_TYPE;
    }


    //发送短信
    private SmsResponse sendMessage(String from, String to, String message, String messageType) throws UnsupportedEncodingException {

        //拼装短信报文
        SmsRequestMessage smsMsg = new SmsRequestMessage(to, message, messageType);

        SmsRequestBody smsBody = new SmsRequestBody("", Lists.newArrayList(smsMsg));
        SmsRequest request = new SmsRequest(operId, operPass, smsBody);
        String requestXml = XmlTranslator.toXML(request);

        requestXml = XML_HEADER + requestXml;
        byte[] content = requestXml.getBytes("GBK");

        log.debug("sms request: {}", requestXml);
        //发送短信
        HttpRequest httpRequest = HttpRequest.post(smsRequestUrl, false).send(content).connectTimeout(10000).readTimeout(10000);
        String responseXml = httpRequest.body();
        log.debug("sms ack: {}", responseXml);


        //接收应答请求
        SmsResponse response = XmlTranslator.fromXML(responseXml, SmsResponse.class);
        checkNotNull(response, "response is null");
        checkNotNull(response.getBody(), "response message body is null");

        if (!Objects.equal(response.getBody().getCode(), "0")) {
            log.error("failed to send message from {} to {},response:{}", from, to, responseXml);
        }
        return response;
    }


    /**
     * 查询剩余短信条数
     *
     * @return 剩余短信条数
     */
    @Override
    public Integer available() {
        return null;
    }
}
