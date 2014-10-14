package io.terminus.snz.related.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Author:  wenhaoli
 * Date: 2014-08-11
 */

@ToString
@EqualsAndHashCode(of={"id"})
public class CompensationReply implements Serializable {

    private static final long serialVersionUID = 6089661634242540800L;

    @Getter
    @Setter
    private Long id;            //主健

    @Getter
    @Setter
    private Long userId;        //用户id

    @Getter
    @Setter
    private Long listId;           //索赔记录id

    @Getter
    @Setter
    private String content;     //内容

    @Getter
    @Setter
    private String companyName; //公司名

    @Getter
    @Setter
    private String files;       //附件url列表

    @Getter
    @Setter
    private Date createdAt;     //创建时间

    @Getter
    @Setter
    private Date updatedAt;     //更新时间

}
