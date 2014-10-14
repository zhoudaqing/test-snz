/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.console.dao;

import com.google.common.collect.ImmutableMap;
import io.terminus.common.mysql.dao.MyBatisDao;
import io.terminus.pampas.console.model.User;
import org.springframework.stereotype.Repository;

/**
 * Author:  <a href="mailto:i@terminus.io">jlchen</a>
 * Date: 2014-07-25
 */
@Repository
public class UserDao extends MyBatisDao<User> {

    public User findByName(String name){
        return getSqlSession().selectOne("User.findByName", name);
    }

    public boolean updatePassword(Long id, String password) {
        return getSqlSession().update("User.updatePassword", ImmutableMap.of("id", id, "password", password)) == 1;
    }
}
