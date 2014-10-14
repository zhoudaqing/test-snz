/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.exception;

/*
 * Author: jlchen
 * Date: 2013-01-17
 */
public class RenderException extends RuntimeException {
    private static final long serialVersionUID = 3581303840451244869L;

    public RenderException() {
    }

    public RenderException(String message) {
        super(message);
    }

    public RenderException(String message, Throwable cause) {
        super(message, cause);
    }

    public RenderException(Throwable cause) {
        super(cause);
    }
}
