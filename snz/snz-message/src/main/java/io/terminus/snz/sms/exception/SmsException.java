package io.terminus.snz.sms.exception;

/**
 * Author:  <a href="mailto:jlchen.cn@gmail.com">jlchen</a>
 * Date: 2013-05-23
 */
public class SmsException extends RuntimeException {
    public SmsException() {
    }

    public SmsException(String s) {
        super(s);
    }

    public SmsException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public SmsException(Throwable throwable) {
        super(throwable);
    }
}
