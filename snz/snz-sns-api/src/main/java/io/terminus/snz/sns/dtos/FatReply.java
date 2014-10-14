package io.terminus.snz.sns.dtos;

import io.terminus.snz.sns.models.Reply;
import io.terminus.snz.user.model.ContactInfo;
import io.terminus.snz.user.model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 这个回复很胖
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-7-28
 */
public class FatReply extends Reply {

    @Getter @Setter
    private List<String> businesses;                                        //公司主营业务

    @Getter @Setter
    private List<String> tags;                                              //公司标签

    @Getter @Setter
    private ContactInfo contactInfo;                                        //联系人信息

    @Getter @Setter
    private Boolean isSupplier = Boolean.TRUE;                              //是否是供应商, 默认是

    @Getter @Setter
    private User user;                                                      //用户信息, 采购商的回复需要用

    public FatReply(Reply r){
        setId(r.getId());
        setUserId(r.getUserId());
        setCompanyName(r.getCompanyName());
        setContent(r.getContent());
        setReqId(r.getReqId());
        setTopicId(r.getTopicId());
        setTid(r.getTid());
        setPid(r.getPid());
        setReceiverId(r.getReceiverId());
        setReceiverName(r.getReceiverName());
        setFiles(r.getFiles());
        setCreatedAt(r.getCreatedAt());
        setUpdatedAt(r.getUpdatedAt());
        setFollowReplies(r.getFollowReplies());
    }
}
