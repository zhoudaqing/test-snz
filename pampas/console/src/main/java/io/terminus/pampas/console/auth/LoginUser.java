/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.console.auth;

import io.terminus.common.utils.BeanMapper;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.console.model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14/8/11
 */
public class LoginUser extends BaseUser {
    private static final long serialVersionUID = 737890915963599643L;

    @Getter
    @Setter
    private Integer status;   //状态 1:正常 -1: 禁用

    @Getter
    @Setter
    private String email; //email

    @Getter
    @Setter
    private String realName;  //真实名称

    @Getter
    @Setter
    private Date loginAt;     //上次登陆时间

    @Getter
    @Setter
    private Date createdAt;    //创建时间

    @Getter
    @Setter
    private Date updatedAt;     //更新时间

    public static LoginUser fromUser(User user) {
        if (user == null) {
            return null;
        }
        LoginUser loginUser = new LoginUser();
        BeanMapper.copy(user, loginUser);
        return loginUser;
    }
}
