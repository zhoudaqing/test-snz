package io.terminus.snz.user.dto;

import io.terminus.snz.user.model.User;
import lombok.Getter;
import lombok.Setter;

/**
 * 查询团队成员时用的DTO
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-8-20
 */
public class TeamMemeberDto extends User {

    @Getter @Setter
    private String organ;       //部门信息

    public TeamMemeberDto(User u){
        setId(u.getId());
        setName(u.getName());
        setNick(u.getNick());
        setEmail(u.getEmail());
        setPhone(u.getPhone());
        setMobile(u.getMobile());
        setStatus(u.getStatus());
        setType(u.getType());
        setPrincipal(u.getPrincipal());
        setRoles(u.getRoles());
        setAccountType(u.getAccountType());
        setTags(u.getTags());
    }
}
