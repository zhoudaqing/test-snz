/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
 * Author: jlchen
 * Date: 2013-01-18
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnAuthorize401Exception extends RuntimeException {
    private static final long serialVersionUID = -5179634070581556579L;

    public UnAuthorize401Exception() {
    }

    public UnAuthorize401Exception(String message) {
        super(message);
    }

    public UnAuthorize401Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public UnAuthorize401Exception(Throwable cause) {
        super(cause);
    }
}
