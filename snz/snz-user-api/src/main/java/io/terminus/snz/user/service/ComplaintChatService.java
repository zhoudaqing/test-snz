/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */
package io.terminus.snz.user.service;

import io.terminus.common.model.Paging;
import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.model.ComplaintChat;

import java.util.List;
import java.util.Map;


/**
 * 抱怨信息聊天记录 服务类<BR>
 *
 * @author wanggen 2014-09-20 13:44:55
 */
public interface ComplaintChatService {

    /**
     * 新增
     *
     * @param complaintChat add bean
     * @return 新增后的自增序列号
     */
    @Export(paramNames = {"complaintChat"})
    public Response<Long> create(ComplaintChat complaintChat);


    /**
     * 根据ID查询单条记录
     *
     * @return 返回的 complaintChat
     */
    @Export(paramNames = {"id"})
    public Response<ComplaintChat> findById(Long id);


    /**
     * 根据 id 列表查询多个结果集
     * @param ids 多个id
     * @return ComplaintChat 列表
     */
    @Export(paramNames = {"ids"})
    public Response<List<ComplaintChat>> findByIds(List<Long> ids);


    /**
     * 分页查询
     * <PRE>
     * 查询接口
     * id                       Long        主健
     * parentId                 Long        抱怨信息ID
     * senderId                 Long        消息发送者ID
     * senderName               String      消息发送者姓名或nick
     * </PRE>
     * @param param     可选查询参数
     * @param pageNo    页码
     * @param pageSize  每页数量
     * @return          分页查询结果
     */
    @Export(paramNames = {"param", "pageNo", "pageSize"})
    Response<Paging<ComplaintChat>> findByPaging(Map<String, Object> param, Integer pageNo, Integer pageSize);


    /**
     * 根据查询条件查询
     * <PRE>
     * 查询接口
     * id                       Long        主健
     * parentId                 Long        抱怨信息ID
     * senderId                 Long        消息发送者ID
     * senderName               String      消息发送者姓名或nick
     * </PRE>
     * @param param 可选查询参数
     * @return 分页查询结果
     */
    @Export(paramNames = {"param"})
    Response<List<ComplaintChat>> findAllBy(Map<String, Object> param);




    /**
     * 根据 parentId 查询 ComplaintChat 列表
     *
     * @param parentId   抱怨信息ID
     * @return 结果列
     */
    @Export(paramNames = {"parentId"})
    Response<List<ComplaintChat>> findByParentId(Long parentId);



    /**
     * 更新操作
     *
     * @param complaintChat 更新操作参数
     * @return 影响行数
     */
    @Export(paramNames = {"complaintChat"})
    public Response<Integer> update(ComplaintChat complaintChat);


    /**
     * 根据序列 id 删除记录
     *
     * @param ids 序列 id 列表
     * @return 删除行数
     */
    @Export(paramNames = {"ids"})
    public Response<Integer> deleteByIds(List<Long> ids);


}
