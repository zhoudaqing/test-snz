package io.terminus.snz.sns.dtos;

import io.terminus.snz.sns.models.Reply;

import java.io.Serializable;
import java.util.List;

/**
 * 回复详情，包括从顶级回复及子回复
 * MADE IN IntelliJ IDEA
 * AUTHOR: haolin
 * AT 12/5/14.
 */
public class ReplyDetail implements Serializable{
    private static final long serialVersionUID = 5521439765767184013L;

    private Reply top;              //顶级回复

    private List<Reply> children;   //子回复列表

    public ReplyDetail(Reply top, List<Reply> children){
        this.top = top;
        this.children = children;
    }
}
