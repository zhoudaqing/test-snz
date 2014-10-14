/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.common;

/**
 * Date: 3/29/14
 * Time: 14:50
 * Author: 2014年 <a href="mailto:dong.worker@gmail.com">张成栋</a>
 */
public class UserUnauthorizedException extends RuntimeException {

    public UserUnauthorizedException() {
    }

    public UserUnauthorizedException(String message) {
        super(message);
    }

    public UserUnauthorizedException(Throwable cause) {
        super(cause);
    }

    public UserUnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
