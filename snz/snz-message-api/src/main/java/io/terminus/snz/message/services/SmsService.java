package io.terminus.snz.message.services;


import io.terminus.pampas.common.Response;

/**
 * Author:  <a href="mailto:jlchen.cn@gmail.com">jlchen</a>
 * Date: 2013-05-23
 */
public interface SmsService {

    /**
     * 发送单条信息
     *
     * @param from    发送方
     * @param to      接收方手机号码
     * @param message 消息体
     */
    Response<Boolean> sendSingle(String from, String to, String message);

    /**
     * 群发信息
     *
     * @param from    接收方
     * @param to      接受方手机号码列表
     * @param message 消息体
     */
    Response<Boolean> sendGroup(String from, Iterable<String> to, String message);


    /**
     * 通过短信模板发送短信
     *
     * @param from         发送方
     * @param to           接收方
     * @param templateFile 模板文件
     * @param context      模板数据上下文
     * @return 发送结果
     */
    Response<Boolean> sendWithTemplate(String from, String to, String templateFile, Object context);


    /**
     * 查询剩余短信条数
     *
     * @return 剩余短信条数
     */
    Integer available();

}
