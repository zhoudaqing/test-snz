package io.terminus.snz.sns.dtos;

import io.terminus.snz.sns.models.Topic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 话题详情，用于前台话题详情展示
 * MADE IN IntelliJ IDEA
 * AUTHOR: haolin
 * AT 4/5/14.
 */
@ToString
public class TopicDetail extends Topic{

    @Getter @Setter
    private Boolean canReply;                           //是否可以回复

    @Getter @Setter
    private List<UserReplyCount> supplierReplies;       //回复过该话题的所有供应商

    @Getter @Setter
    private List<UserReplyCount> purchaserReplies;      //回复过该话题的所有采购商

    @Getter @Setter
    private List<TopicFriend> friends;                  //若该话题是圈内话题，可指定朋友圈

    public TopicDetail(Topic t){
        setId(t.getId());
        setTitle(t.getTitle());
        setContent(t.getContent());
        setUserId(t.getUserId());
        setReqId(t.getReqId());
        setReqStatus(t.getReqStatus());
        setReqName(t.getReqName());
        setScope(t.getScope());
        setCompanyName(t.getCompanyName());
        setTotalViews(t.getTotalViews());
        setTotalReplies(t.getTotalReplies());
        setFiles(t.getFiles());
        setClosed(t.getClosed());
        setLastReplierId(t.getLastReplierId());
        setLastReplierName(t.getLastReplierName());
        setCreatedAt(t.getCreatedAt());
    }
}

