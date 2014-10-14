/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */
package io.terminus.snz.user.dao;

import io.terminus.common.model.Paging;
import io.terminus.snz.user.model.ComplaintChat;
import org.joda.time.DateTime;
import com.google.common.collect.ImmutableList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对 `snz_complaint_chats` CRUD 测试<BR>
 * created by wanggen 2014-09-20 13:44:55
 */
public class ComplaintChatDaoTest extends TestBaseDao {

    @Autowired
    private ComplaintChatDao complaintChatDao;

    private Long createdId = null;

    private ComplaintChat complaintChat = null;

    @Before
    public void setUp() throws Exception {
        complaintChat = new ComplaintChat();
        complaintChat.setId(1l); //主健
        complaintChat.setParentId(1l); //抱怨信息ID
        complaintChat.setSenderId(1l); //消息发送者ID
        complaintChat.setSenderName("消息发送者姓名或nick-1"); //消息发送者姓名或nick
        complaintChat.setMessage("消息内容-1"); //消息内容
        complaintChat.setCreatedAt(DateTime.now().toDate()); //创建时间
        complaintChat.setUpdatedAt(DateTime.now().toDate()); //更新时间
        createdId = complaintChatDao.create(complaintChat);
    }

    @Test
    public void testCreate() throws Exception {
        createdId = complaintChatDao.create(complaintChat);
        Assert.assertTrue("One record must be created", createdId>=1);
    }

    @Test
    public void testFindById() throws Exception {
        ComplaintChat byId = complaintChatDao.findById(createdId);
        Assert.assertTrue("The requery result's id must equals:"+createdId, byId!=null && byId.getId().equals(createdId));
    }


    @Test
    public void testFindByIds() throws Exception {
        List<ComplaintChat> byIds = complaintChatDao.findByIds(ImmutableList.of(createdId));
        Assert.assertTrue("One record must be selectd"+createdId, byIds!=null && byIds.size()==1);
    }



    @Test
    public void testFindByPaging() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>(){
            {
                put("id", 1l); //主健
                put("parentId", 1l); //抱怨信息ID
                put("senderId", 1l); //消息发送者ID
                put("senderName", "消息发送者姓名或nick-1"); //消息发送者姓名或nick
                put("message", "消息内容-1"); //消息内容
                put("createdAt", DateTime.now().toDate()); //创建时间
                put("updatedAt", DateTime.now().toDate()); //更新时间
                put("offset",0);
                put("limit",10000);
            }
        };
        Paging<ComplaintChat> byPaging = complaintChatDao.findByPaging(param);
        Assert.assertTrue("At least one record must be found",
                byPaging.getData()!=null && byPaging.getData().size()>=1);
    }

    @Test
    public void testFindAllBy() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>(){
            {
                put("id", 1l); //主健
                put("parentId", 1l); //抱怨信息ID
                put("senderId", 1l); //消息发送者ID
                put("senderName", "消息发送者姓名或nick-1"); //消息发送者姓名或nick
                put("message", "消息内容-1"); //消息内容
                put("createdAt", DateTime.now().toDate()); //创建时间
                put("updatedAt", DateTime.now().toDate()); //更新时间
            }
        };
        List<ComplaintChat> allBy = complaintChatDao.findAllBy(param);
        Assert.assertTrue("At least one record must be found", allBy.size()>=0);
    }

    @Test
    public void testFindByParentId(){

        Long parentId = 1l; //抱怨信息ID

        List<ComplaintChat> complaintChats = complaintChatDao.findByParentId(parentId);

        Assert.assertTrue("At least one record must be found", complaintChats.size()>=1);

    }


    @Test
    public void testUpdate() throws Exception {
        ComplaintChat complaintChat = new ComplaintChat();
        complaintChat.setId(createdId);
        
        complaintChat.setParentId(1001l); //抱怨信息ID
        complaintChat.setSenderId(1001l); //消息发送者ID
        complaintChat.setSenderName("消息发送者姓名或nick-1-new"); //消息发送者姓名或nick
        complaintChat.setMessage("消息内容-1-new"); //消息内容
        complaintChat.setCreatedAt(DateTime.now().toDate()); //创建时间
        complaintChat.setUpdatedAt(DateTime.now().toDate()); //更新时间
        int updated = complaintChatDao.update(complaintChat);
        Assert.assertTrue("One record must be updated", updated==1);
    }


    @Test
    public void testDeleteByIds() throws Exception {
        int deleted = complaintChatDao.deleteByIds(ImmutableList.of(createdId));
        Assert.assertTrue("One record must be deleted", deleted==1);
    }

}