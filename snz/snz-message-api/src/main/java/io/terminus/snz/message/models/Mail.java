package io.terminus.snz.message.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Copyright (c) 2014 杭州端点网络科技有限公司
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14/8/28
 */
@ToString
public class Mail<T> implements Serializable {
    private static final long serialVersionUID = 5735034063337100512L;

    @Getter @Setter
    private Type type;

    @Getter @Setter
    private String to;

    @Getter @Setter
    private T data;

    public static enum Type {
        CREDIT_QUALIFY_UPCOMING("供应商信用等级评价即将到期"),
        CREDIT_QUALIFY_DELAYED("供应商信用等级评价即将过期"),
        REQUIREMENT_INVITATION("海尔需求邀请函"),
        REQUIREMENT_WARNING("需求预警－供应商数量不足"),
        SUPPLIER_QUALIFY_DEADLINE("供应商资质交互即将到期"),
        SUPPLIER_APPROVE_DEADLINE("供应商审核即将到期");

        @Getter
        private String title;

        private Type(String title) {
            this.title = title;
        }
    }
}
