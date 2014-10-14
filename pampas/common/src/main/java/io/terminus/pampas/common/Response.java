/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-6-22
 */
@ToString
public class Response<T> implements Serializable {
    private static final long serialVersionUID = 6138979130005367537L;

    @Getter
    @Setter
    private boolean success;
    @Getter
    private T result;
    @Getter
    private String error;

    public void setResult(T result) {
        this.success = true;
        this.result = result;
    }

    public void setError(String error) {
        this.success = false;
        this.error = error;
    }

    public static <T> Response<T> ok(T data) {
        Response<T> resp = new Response<T>();
        resp.setResult(data);
        return resp;
    }

    public static <T> Response<T> ok() {
        return Response.ok(null);
    }

    public static <T> Response<T> fail(String error) {
        Response<T> resp = new Response<T>();
        resp.setError(error);
        return resp;
    }
}
