package io.terminus.snz.sns.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/*
 * 话题的回复
 * MADE IN IntelliJ IDEA
 * AUTHOR: haolin
 * AT 30/4/14.
 */
@ToString @EqualsAndHashCode(of={"id"})
public class Reply implements Serializable{

    private static final long serialVersionUID = 7275770228208289417L;

    @Getter
    @Setter
    private Long id;            //主健

    @Getter
    @Setter
    private Long userId;        //用户id

    @Getter
    @Setter
    private Long receiverId;    //被回复的用户id, 若为顶级回复该值为0

    @Getter
    @Setter
    private String receiverName; //被回复者用户名称

    @Getter
    @Setter
    private Long reqId;         //对应需求id

    @Getter
    @Setter
    private Long topicId;       //对应话题id

    @Getter
    @Setter
    private Long tid = 0L;      //顶级回复id, 若为顶级回复, 该值为0

    @Getter
    @Setter
    private Long pid = 0L;      //父级回复id, 若为顶级回复, 该值为0

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

    @Getter
    @Setter
    protected List<Reply> followReplies;        // 回复当前信息的下一级回复

}
