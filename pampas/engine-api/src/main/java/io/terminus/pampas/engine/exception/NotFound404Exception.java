/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
 * Author: jlchen
 * Date: 2013-01-17
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFound404Exception extends RuntimeException {
    private static final long serialVersionUID = -5775653511116823817L;

    public NotFound404Exception() {
    }

    public NotFound404Exception(Throwable cause) {
        super(cause);
    }

    public NotFound404Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFound404Exception(String message) {
        super(message);
    }
}
