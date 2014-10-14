/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.common;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-5-5
 */
public class BaseUser implements Serializable {
    private static final long serialVersionUID = 5193232588743907086L;
    @Getter
    @Setter
    protected Long id;  //用户id

    @Getter
    @Setter
    protected String name;

    @Getter
    @Setter
    protected String nickName;

    @Getter
    protected Integer type; //类型, 端点公司内部保留使用, 如果需要设置权限, 请使用roles字段

    @Getter
    @Setter
    protected List<String> roles; //角色列表

    public void setType(Integer type) {
        this.type = type;
        this.enumType = TYPE.fromNumber(type);
    }

    private TYPE enumType;

    @Getter
    @Setter
    private String tags; //用户标志


    @Getter
    @Setter
    protected Long parent; //parent id

    @Getter
    @Setter
    protected String mobile;

    public static enum TYPE {
        ADMIN(0, "管理员"),
        BUYER(1, "买家"),
        SELLER(2, "卖家"),
        SITE_OWNER(3, "站点拥有者"),
        DESIGNER(4, "设计师"),
        SUB_ACCOUNT(5, "子账号"),
        WHOLESALER(6, "批发商"),
        OTHER(7, "其他"),
        AGENT(8, "代理商"),            // crm 中使用
        FINANCE(9, "财务"),
        PURCHASER(10, "采购商"),       // 690 中使用
        APPLIER(11, "供应商"),         // 690 中使用
        ALL(-99, "全部");             // 用于权限校验，不是真实TYPE

        private final int value;

        private final String display;

        private TYPE(int number, String display) {
            this.value = number;
            this.display = display;
        }

        public static TYPE fromName(String name) {
            for (TYPE type : TYPE.values()) {
                if (type.name().equalsIgnoreCase(name)) {
                    return type;
                }
            }
            return null;
        }

        public static TYPE fromNumber(Integer number) {
            if (number == null) {
                return null;
            }
            for (TYPE type : TYPE.values()) {
                if (type.value == number) {
                    return type;
                }
            }
            return null;
        }

        public int toNumber() {
            return value;
        }

        public String toName() {
            return display;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }


    public BaseUser() {
        this.roles = new ArrayList<String>();
    }

    public BaseUser(Long id, String name, Integer type) {
        this();
        this.id = id;
        this.name = name;
        this.setType(type);
    }

    public BaseUser(Long id, String name, TYPE type) {
        this();
        this.id = id;
        this.name = name;
        this.setTypeEnum(type);
    }

    /***********************************************
     * all method after this comment are alias
     ***********************************************/

    /**
     * alias of {@link #setParent(Long)}
     */
    public void setParentId(Long parentId) {
        this.parent = parentId;
    }

    /**
     * alias of {@link #getParent()}
     */
    public Long getParentId() {
        return parent;
    }

    /**
     * alias of {@link #setName(String)}
     */
    public void setPrincipal(String principal) {
        this.name = principal;
    }

    /**
     * alias of {@link #getName()}
     */
    public String getPrincipal() {
        return name;
    }

    /**
     * ********************************************
     * all method before this comment are alias
     * *********************************************
     */

    public TYPE getTypeEnum() {
        return enumType;
    }

    public void setTypeEnum(TYPE type) {
        this.type = type.toNumber();
        this.enumType = type;
    }

    /**
     * 判断用户是否是某一类型
     * @param t 指定类型
     * @return 若是该类型返回true, 反之false
     */
    public boolean isType(TYPE t){
        return type == t.toNumber();
    }
}
