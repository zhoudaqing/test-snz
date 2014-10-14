/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.common;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-5-5
 */
public final class UserUtil {

    private static ThreadLocal<BaseUser> user = new ThreadLocal<BaseUser>();

    private static ThreadLocal<InnerCookie> cookies = new ThreadLocal<InnerCookie>();

    public static void putCurrentUser(BaseUser baseUser) {
        user.set(baseUser);
    }

    public static BaseUser getCurrentUser() {
        //noinspection unchecked
        return user.get();
    }

    public static void clearCurrentUser() {
        user.remove();
    }

    public static Long getUserId() {
        BaseUser baseUser = getCurrentUser();
        return baseUser != null ? baseUser.getId() : null;
    }

    /**
     * 对于子帐号,返回其主帐号的userId
     *
     * @return userId
     */
    public static Long getMasterId() {
        BaseUser baseUser = getCurrentUser();
        if (baseUser == null) {
            return null;
        }
        Long parentId = baseUser.getParentId();
        if (parentId == null || parentId < 0) { //如果主账号为空,或者主帐号是无效的负值,返回本身的id
            return baseUser.getId();
        }
        return parentId;
    }

    public static void putInnerCookie(InnerCookie innerCookie) {
        cookies.set(innerCookie);
    }

    public static InnerCookie getInnerCookie() {
        return cookies.get();
    }

    public static void clearInnerCookie() {
        cookies.remove();
    }
}
