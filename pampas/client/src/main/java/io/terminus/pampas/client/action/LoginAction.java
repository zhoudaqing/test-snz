/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.client.action;

import io.terminus.pampas.client.Action;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-5-11
 */
public class LoginAction extends Action {
    private static final long serialVersionUID = 6245111191588573507L;

    @Getter
    private Long userId;
    @Getter
    @Setter
    private Integer maxAge = 0;

    public LoginAction(Long userId, Object data) {
        this.userId = userId;
        super.setData(data);
    }
}
