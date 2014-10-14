/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */
package io.terminus.snz.user.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import io.terminus.common.model.Paging;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.dao.ComplaintChatDao;
import io.terminus.snz.user.model.ComplaintChat;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * 抱怨信息聊天记录 服务类测试<BR>
 * @author wanggen 2014-09-20 13:44:55
 */
public class ComplaintChatServiceImplTest {

    @InjectMocks
    private ComplaintChatServiceImpl complaintChatService;

    @Mock
    private ComplaintChatDao complaintChatDao;

    @Mock
    private ComplaintChat complaintChat;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        complaintChat = new ComplaintChat();
        complaintChat.setId(1l); //主健
        complaintChat.setParentId(1l); //抱怨信息ID
        complaintChat.setSenderId(1l); //消息发送者ID
        complaintChat.setSenderName("消息发送者姓名或nick-1"); //消息发送者姓名或nick
        complaintChat.setMessage("消息内容-1"); //消息内容
        complaintChat.setCreatedAt(DateTime.now().toDate()); //创建时间
        complaintChat.setUpdatedAt(DateTime.now().toDate()); //更新时间
    }

    @Test
    public void testCreate() throws Exception {

        // Should be successfully
        when(complaintChatDao.create(complaintChat)).thenReturn(1L);
        Response<Long> response = complaintChatService.create(complaintChat);
        Assert.assertTrue("The new created id must be:[1]", response.getResult()==1);

        // Should be interrupted
        when(complaintChatDao.create(Matchers.<ComplaintChat>anyObject())).thenThrow(new RuntimeException("Interruption"));
        response = complaintChatService.create(complaintChat);
        Assert.assertTrue("Should be interrupted", response.getError()!=null);

        // Should be fast failed
        response = complaintChatService.create(null);
        Assert.assertTrue("Should be fast failed", response.getError()!=null);

    }


    @Test
    public void testFindById() throws Exception {

        // Should be successfully
        when(complaintChatDao.findById(1L)).thenReturn(complaintChat);
        Response<ComplaintChat> findByIdResp = complaintChatService.findById(1L);
        Assert.assertTrue("The found object's id must be 1" ,
                          findByIdResp.getResult().getId()==1);

        // Should be interrupted
        when(complaintChatDao.findById(Matchers.<Long>anyObject())).thenThrow(new RuntimeException("Interruption"));
        findByIdResp = complaintChatService.findById(1L);
        Assert.assertTrue("Should be interrupted", findByIdResp.getError()!=null);

        // Should be fast failed
        findByIdResp = complaintChatService.findById(null);
        Assert.assertTrue("Should be fast failed", findByIdResp.getError()!=null);

    }


    @Test
    public void testFindByIds() throws Exception {

        // Should be successfully
        when(complaintChatDao.findByIds(Lists.newArrayList(1l)))
            .thenReturn(Lists.newArrayList(complaintChat));
        Response<List<ComplaintChat>> findByIdsResp = complaintChatService.findByIds(Lists.newArrayList(1l));
        Assert.assertTrue("The found list must by at least one record",
                findByIdsResp.getResult().size()==1 &&
                findByIdsResp.getResult().get(0).getId()==1);

        // Should be interrupted
        when(complaintChatDao.findByIds(Lists.newArrayList(Matchers.<Long>anyObject()))).thenThrow(new RuntimeException("Interruption"));
        findByIdsResp = complaintChatService.findByIds(Lists.newArrayList(1l));
        Assert.assertTrue("Should be interrupted", findByIdsResp.getError()!=null);

        // Should be fast failed
        findByIdsResp = complaintChatService.findByIds(Lists.<Long>newArrayList());
        Assert.assertTrue("Should be fast failed", findByIdsResp.getError()!=null);

    }


    @Test
    public void testFindByPaging() throws Exception {

        Paging<ComplaintChat> paging = new Paging<ComplaintChat>();
        paging.setTotal(1l);
        paging.setData(Lists.newArrayList(complaintChat));

        // Should be successfully
        when(complaintChatDao.findByPaging(anyMap())).thenReturn(paging);
        Response<Paging<ComplaintChat>> findByPagingResp = complaintChatService.findByPaging(
                new HashMap<String, Object>(){{
                    put("id", 1l);
                }}, 1, 1);
        Assert.assertTrue("The found records must be more than one", findByPagingResp.getResult().getData().size()>=1);

        // Should be interrupted
        when(complaintChatDao.findByPaging(anyMap())).thenThrow(new RuntimeException("Interruption"));
        findByPagingResp=complaintChatService.findByPaging(
                new HashMap<String, Object>(){{
                put("id",1l);
            }},1,1);
        Assert.assertTrue("Should be interrupted", findByPagingResp.getError()!=null);

        // Should be fast failed
        findByPagingResp=complaintChatService.findByPaging(null,1,1);
        Assert.assertTrue("Should be fast failed", findByPagingResp.getError()!=null);

    }


    @Test
    public void testFindAllBy() throws Exception {

        List<ComplaintChat> complaintChats = Lists.newArrayList(complaintChat);

        // Should be successfully
        when(complaintChatDao.findAllBy(anyMap())).thenReturn(complaintChats);
        Response<List<ComplaintChat>> allByResp = complaintChatService.findAllBy(ImmutableMap.<String, Object>
                of("id", 1l));
        Assert.assertTrue("The found records must be more than one",
                allByResp.getResult().size()>=1);

        // Should be interrupted
        when(complaintChatDao.findAllBy(anyMap())).thenThrow(new RuntimeException("Interruption"));
        allByResp = complaintChatService.findAllBy(ImmutableMap.<String, Object>
                of("id", 1l));
        Assert.assertTrue("Should be interrupted", allByResp.getError()!=null);

        // Should be fast failed
        allByResp = complaintChatService.findAllBy(null);
        Assert.assertTrue("Should be fast failed", allByResp.getError()!=null);

    }

    @Test
    public void testFindByParentId(){

        Long parentId = 1l; //抱怨信息ID

        Response<List<ComplaintChat>> resp;
        List<ComplaintChat> complaintChats = Lists.newArrayList(complaintChat);

        // Should be successfully
        when(complaintChatDao.findByParentId(parentId)).thenReturn(complaintChats);
        resp = complaintChatService.findByParentId(parentId);
        Assert.assertTrue("At least one record must be found", resp.getResult().size()>=1);

        // Should be interrupted
        when(complaintChatDao.findByParentId(anyLong())).thenThrow(new RuntimeException("Interruption"));
        resp = complaintChatService.findByParentId(parentId);
        Assert.assertTrue("Should be interrupted", resp.getError()!=null);

        // Should be fast failed
        //resp = complaintChatService.findByUserId(anyLong());
        //Assert.assertTrue("Should be fast failed", resp.getError()!=null);

    }


    @Test
    public void testUpdate() throws Exception {

        // Should be successfully
        when(complaintChatDao.update(complaintChat)).thenReturn(1);
        complaintChat.setId(1l); //主健
        complaintChat.setParentId(1l); //抱怨信息ID
        complaintChat.setSenderId(1l); //消息发送者ID
        complaintChat.setSenderName("消息发送者姓名或nick-1"); //消息发送者姓名或nick
        complaintChat.setMessage("消息内容-1"); //消息内容
        complaintChat.setCreatedAt(DateTime.now().toDate()); //创建时间
        complaintChat.setUpdatedAt(DateTime.now().toDate()); //更新时间
        Response<Integer> updateResp = complaintChatService.update(complaintChat);
        Assert.assertTrue("At least one record must be updated", updateResp.getResult()==1);

        // Should be interrupted
        when(complaintChatDao.update(Matchers.<ComplaintChat>anyObject())).thenThrow(new RuntimeException("Interruption"));
        updateResp = complaintChatService.update(complaintChat);
        Assert.assertTrue("Should be interrupted", updateResp.getError()!=null);

        // Should be fast failed
        updateResp = complaintChatService.update(null);
        Assert.assertTrue("Should be fast failed", updateResp.getError()!=null);

    }

    @Test
    public void testDeleteByIds() throws Exception {

        // Should be successfully
        when(complaintChatDao.deleteByIds(Lists.newArrayList(1l))).thenReturn(1);
        Response<Integer> deleteResponse = complaintChatService.deleteByIds(Lists.newArrayList(1l));
        Assert.assertTrue("At least one record must be deleted", deleteResponse.getResult()==1);

        // Should be interrupted
        when(complaintChatDao.deleteByIds(Matchers.<List>anyObject())).thenThrow(new RuntimeException("Interruption"));
        deleteResponse = complaintChatService.deleteByIds(Lists.newArrayList(1l));
        Assert.assertTrue("Should be interrupted", deleteResponse.getError()!=null);

        // Should be fast failed
        deleteResponse = complaintChatService.deleteByIds(null);
        Assert.assertTrue("Should be fast failed", deleteResponse.getError()!=null);

    }
}