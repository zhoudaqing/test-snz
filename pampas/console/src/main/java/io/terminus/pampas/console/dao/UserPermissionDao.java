/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.console.dao;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import io.terminus.common.mysql.dao.MyBatisDao;
import io.terminus.common.utils.JsonMapper;
import io.terminus.pampas.console.model.UserPermission;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * Author:  <a href="mailto:i@terminus.io">jlchen</a>
 * Date: 2014-07-25
 */
@Repository
public class UserPermissionDao extends MyBatisDao<UserPermission> {

    public List<String> findByUserId(Long userId){
        UserPermission userPermission = getSqlSession().selectOne("UserPermission.findByUserId", userId);

        if(userPermission!=null && !Strings.isNullOrEmpty(userPermission.getPermissions())){
            return JsonMapper.nonEmptyMapper().fromJson(userPermission.getPermissions(),
                    JsonMapper.nonEmptyMapper().createCollectionType(List.class,String.class));
        }
        return Collections.emptyList();
    }

    public boolean deleteByUserId(Long userId){
        return getSqlSession().delete("UserPermission.deleteByUserId", userId)==1;
    }

    public Long saveOrUpdate(UserPermission up){
        UserPermission userPermission = getSqlSession().selectOne("UserPermission.findByUserId", up.getUserId());
        if(userPermission == null){
            create(up);
            return up.getId();
        } else{
            updateByUserId(up.getUserId(), up.getPermissions());
            return userPermission.getId();
        }
    }

    private boolean updateByUserId(Long userId, String permissions){
        return getSqlSession().update("UserPermission.updateByUserId",
                ImmutableMap.of("userId", userId, "permissions", permissions))
                == 1;
    }
}
