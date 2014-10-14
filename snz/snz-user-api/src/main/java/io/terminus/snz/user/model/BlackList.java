package io.terminus.snz.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Description：公司黑名单
 * Author：Guo Chaopeng
 * Created on 14-6-23
 */
@ToString
public class BlackList implements Serializable {

    private static final long serialVersionUID = 3433403304657213495L;

    @Getter
    @Setter
    private Long id;                           //自增主键

    @Getter
    @Setter
    private String name;                       //公司名

    @Getter
    @Setter
    private String keywords;                   //关键字

    @Setter
    @Getter
    private String initAgent;                  //法人

    @Getter
    @Setter
    private Date createdAt;

    @Getter
    @Setter
    private Date updatedAt;

}