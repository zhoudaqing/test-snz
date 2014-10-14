/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
 * Author: jlchen
 * Date: 2013-01-18
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
@Slf4j
public class Server500Exception extends RuntimeException {
    private static final long serialVersionUID = -6799208467426832752L;

    public Server500Exception() {
    }

    public Server500Exception(String message) {
        super(message);
    }

    public Server500Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public Server500Exception(Throwable cause) {
        super(cause);
    }
}
