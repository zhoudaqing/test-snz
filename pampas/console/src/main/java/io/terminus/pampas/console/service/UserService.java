/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.console.service;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import io.terminus.common.model.PageInfo;
import io.terminus.common.model.Paging;
import io.terminus.common.utils.JsonMapper;
import io.terminus.pampas.client.Export;
import io.terminus.pampas.client.action.LoginAction;
import io.terminus.pampas.client.action.LogoutAction;
import io.terminus.pampas.common.Response;
import io.terminus.pampas.common.UserUtil;
import io.terminus.pampas.console.auth.AuthHelpers;
import io.terminus.pampas.console.dao.UserDao;
import io.terminus.pampas.console.dao.UserPermissionDao;
import io.terminus.pampas.console.model.User;
import io.terminus.pampas.console.model.UserPermission;
import io.terminus.pampas.console.util.AuthUtil;
import io.terminus.pampas.console.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author:  <a href="mailto:i@terminus.io">jlchen</a>
 * Date: 2014-07-25
 */
@Service
public class UserService {
    private final static Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserPermissionDao userPermissionDao;
    @Autowired
    private AuthHelpers authHelpers;

    @Export(paramNames = {"userId"})
    public User findById(Long userId) {
        return userDao.load(userId);
    }

    public User findByName(String name) {
        return userDao.findByName(name);
    }

    @Export(paramNames = {"name", "pageNo", "pageSize"})
    public Paging<User> paging(String name, Integer pageNo, Integer pageSize) {
        PageInfo pageInfo = new PageInfo(pageNo, pageSize);
        User criteria = new User();
        criteria.setName(name);
        return userDao.paging(pageInfo.getOffset(), pageInfo.getLimit(), criteria);
    }

    @Export(paramNames = {"user"})
    public Long create(User user) {
        authHelpers.checkPermission(AuthUtil.admin());
        user.setStatus(1);//默认已启用
        user.setPassword(PasswordUtil.encryptPassword(user.getPassword()));
        userDao.create(user);
        return user.getId();
    }

    /**
     * 用户登陆
     *
     * @param name     登陆名称
     * @param password 登陆密码
     * @return 登陆的用户, 如果失败, 则抛出 IllegalArgumentException
     */
    @Export(paramNames = {"name", "password"})
    public Response<LoginAction> login(String name, String password) {
        User user = userDao.findByName(name);
        if (user == null) {
            log.error("user(name={}) not exist", name);
            return Response.fail("user not exist");
        }
        if (PasswordUtil.passwordMatch(password, user.getPassword())) {
            //更新上次登陆时间
            User u = new User();
            u.setId(user.getId());
            u.setLoginAt(new Date());
            userDao.update(u);
            LoginAction loginAction = new LoginAction(user.getId(), null);
            loginAction.setMaxAge((int)TimeUnit.DAYS.toSeconds(7)); // 7 days
            return Response.ok(loginAction);
        } else {
            log.error("failed to login user(name={}), password mismatch ", name);
            return Response.fail("password mismatch");
        }
    }

    @Export
    public LogoutAction logout() {
        return new LogoutAction();
    }

    /**
     * 修改密码
     *
     * @param originPassword 原有的登陆密码
     * @param newPassword    新的登陆密码
     *                       <p/>
     *                       如果失败, 则抛出 IllegalArgumentException
     */
    @Export(paramNames = {"originPassword", "newPassword"})
    public Response<Void> updatePassword(String originPassword, String newPassword) {
        Long currentUserId = UserUtil.getUserId();
        User user = userDao.load(currentUserId);
        if (PasswordUtil.passwordMatch(originPassword, user.getPassword())) {
            userDao.updatePassword(user.getId(), PasswordUtil.encryptPassword(newPassword));
        } else {
            log.error("failed to update password of user(id={}), password mismatch ", currentUserId);
            return Response.fail("origin password mismatch");
        }
        return Response.ok();
    }

    /**
     * 更新用户信息, 管理员使用
     * @param user  待更新的用户
     */
    @Export(paramNames = {"user"})
    public void update(User user){
        if (!Strings.isNullOrEmpty(user.getPassword())) {
            user.setPassword(PasswordUtil.encryptPassword(user.getPassword()));
        }
        userDao.update(user);
    }

    /**
     * 用户自行修改用户信息
     * @param user  待更新的用户
     */
    @Export(paramNames = {"user"})
    public void updateProfile(User user){
        user.setId(UserUtil.getUserId());
        user.setPassword(null);
        userDao.update(user);
    }

    @Export(paramNames = {"userId"})
    @Transactional
    public void delete(Long userId) {
        authHelpers.checkPermission(AuthUtil.admin());
        userDao.delete(userId);
        userPermissionDao.deleteByUserId(userId);
    }

    /**
     * 给用户授权, 包括创建和更新
     * @return  返回对应的数据库中的授权记录id, 一个用户只有一条授权信息
     */
    @Export(paramNames = {"userId", "permissions"})
    public Long authorize(Long userId, Set<String> permissions){
        checkNotNull(userId);
        authHelpers.checkPermission(AuthUtil.admin());
        UserPermission userPermission = new UserPermission();
        userPermission.setUserId(userId);
        userPermission.setPermissions(JsonMapper.nonEmptyMapper().toJson(permissions));
        return userPermissionDao.saveOrUpdate(userPermission);
    }

    /**
     *  查找用户的授权信息
     * @param userId  用户id
     * @return   授权信息列表
     */
    public List<String> findPermissions(Long userId){
        return userPermissionDao.findByUserId(userId);
    }

    /**
     * get user info and user permissions
     * @param userId userId
     * @return {user: object, permission: list[string]}
     */
    @Export(paramNames = {"userId"})
    public Map<String, Object> findUserAndPermissions(Long userId) {
        Map<String, Object> result = Maps.newHashMap();
        result.put("user", userDao.load(userId));
        List<String> permissions = userPermissionDao.findByUserId(userId);
        if (permissions.contains(AuthUtil.admin())) {
            result.put("isAdmin", true);
            permissions.remove(AuthUtil.admin());
        }
        result.put("permissions", permissions);
        return result;
    }

    @Export
    public Map<String, Object> getCurrentUserPermissions() {
        Map<String, Object> result = Maps.newHashMap();
        List<String> permissions = userPermissionDao.findByUserId(UserUtil.getUserId());
        if (permissions.contains(AuthUtil.admin())) {
            result.put("isAdmin", true);
            permissions.remove(AuthUtil.admin());
        }
        result.put("permissions", permissions);
        return result;
    }
}
