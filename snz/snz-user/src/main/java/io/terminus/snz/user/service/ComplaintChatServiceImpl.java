/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */
package io.terminus.snz.user.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import io.terminus.common.model.PageInfo;
import io.terminus.common.model.Paging;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.dao.ComplaintChatDao;
import io.terminus.snz.user.model.ComplaintChat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 抱怨信息聊天记录 服务类<BR>
 *
 * @author wanggen 2014-09-20 13:44:55
 */
@Service
@Slf4j
public class ComplaintChatServiceImpl implements ComplaintChatService {

    @Autowired
    private ComplaintChatDao complaintChatDao;  //用户抱怨主题聊天消息DAO

    final Cache<Long, Long> maxChatCache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.HOURS).maximumSize(100).build();

    @Override
    public Response<Long> create(ComplaintChat complaintChat) {
        Response<Long> resp = new Response<Long>();
        if (complaintChat == null) {
            log.error("param [complaintChat] can not be null");
            resp.setError("params.not.null");
            return resp;
        }
        try {
            Long createdId = complaintChatDao.create(complaintChat);
            maxChatCache.put(complaintChat.getParentId(), createdId);
            resp.setResult(createdId);
            return resp;
        } catch (Exception e) {
            resp.setError("snz_complaint_chats.insert.failed");
            log.error("Failed to insert into `snz_complaint_chats` with param:{}", complaintChat, e);
            return resp;
        }
    }

    @Override
    public Response<ComplaintChat> findById(Long id) {
        Response<ComplaintChat> resp = new Response<ComplaintChat>();
        if (id == null) {
            log.error("param [id] can not be null when query");
            resp.setError("params.not.null");
            return resp;
        }
        try {
            ComplaintChat complaintChat = complaintChatDao.findById(id);
            if (complaintChat == null) {
                log.warn("No records found from `snz_complaint_chats` with param:{}", id);
                resp.setError("snz_complaint_chats.select.failed");
                return resp;
            }
            resp.setResult(complaintChat);
            return resp;
        } catch (Exception e) {
            resp.setError("snz_complaint_chats.select.failed");
            log.error("Failed to findById from `snz_complaint_chats` with param:{}", id, e);
            return resp;
        }
    }

    @Override
    public Response<List<ComplaintChat>> findByIds(List<Long> ids) {
        Response<List<ComplaintChat>> resp = new Response<List<ComplaintChat>>();
        if (ids == null) {
            log.error("param [ids] can not be null when findByIds");
            resp.setError("params.not.null");
            return resp;
        }
        try {
            List<ComplaintChat> complaintChats = complaintChatDao.findByIds(ids);
            if (complaintChats == null || complaintChats.size() < 1) {
                log.warn("No records found from `snz_complaint_chats` with param:{}", ids);
                resp.setError("snz_complaint_chats.select.failed");
                return resp;
            }
            resp.setResult(complaintChats);
            return resp;
        } catch (Exception e) {
            resp.setError("snz_complaint_chats.select.failed");
            log.error("Failed to findByIds from `snz_complaint_chats` with param:{}", ids, e);
            return resp;
        }
    }

    @Override
    public Response<Paging<ComplaintChat>> findByPaging(Map<String, Object> param, Integer pageNo, Integer pageSize) {
        Response<Paging<ComplaintChat>> resp = new Response<Paging<ComplaintChat>>();
        if (param == null) {
            param = Maps.newHashMap();
        }
        try {
            PageInfo pageInfo = new PageInfo(pageNo, pageSize);
            param.put("offset", pageInfo.getOffset());
            param.put("limit", pageInfo.getLimit());
            Paging<ComplaintChat> complaintChats = complaintChatDao.findByPaging(param);
            if (complaintChats.getTotal() < 1) {
                log.warn("No records found from `snz_complaint_chats` with param:{}", param);
                resp.setError("snz_complaint_chats.select.failed");
                return resp;
            }
            resp.setResult(complaintChats);
            return resp;
        } catch (Exception e) {
            resp.setError("snz_complaint_chats.select.failed");
            log.error("Failed to findByPaging from `snz_complaint_chats` with param:{}", param, e);
            return resp;
        }
    }

    @Override
    public Response<List<ComplaintChat>> findAllBy(Map<String, Object> param) {
        Response<List<ComplaintChat>> resp = new Response<List<ComplaintChat>>();
        if (param == null) {
            param = Maps.newHashMap();
        }
        if (param.containsKey("minId") && param.containsKey("parentId")) {
            Long parentId = null;
            Long minId = null;
            try {
                parentId = Long.valueOf((String) param.get("parentId"));
                minId = Long.valueOf((String) param.get("minId"));
                Long currMaxId = maxChatCache.getIfPresent(parentId);
                if (currMaxId != null && currMaxId <= minId || 99999999 == minId) {
                    resp.setResult(Collections.<ComplaintChat>emptyList());
                    return resp;
                }
            } catch (NumberFormatException e) {
                log.error("Failed to parse param minId:[{}], parentId:[{}]", minId, parentId, e);
            }
        }
        try {
            try {
                if (param.containsKey("offset")) {
                    param.put("offset", Integer.valueOf((String) param.get("offset")));
                }
                if (param.containsKey("limit")) {
                    param.put("limit", Integer.valueOf((String) param.get("limit")));
                }
            } catch (NumberFormatException e) {
                log.error("Illegal params offset:[{}], limit:[{}]", param.get("offset"), param.get("limit"), e);
            }
            List<ComplaintChat> complaintChats = complaintChatDao.findAllBy(param);
            if (complaintChats.size() > 0)
                maxChatCache.put(complaintChats.get(0).getParentId(), complaintChats.get(0).getId());
            resp.setResult(complaintChats);
            return resp;
        } catch (Exception e) {
            resp.setError("snz_complaint_chats.select.failed");
            log.error("Failed to findAllBy from `snz_complaint_chats` with param:{}", param, e);
            return resp;
        }
    }

    @Override
    public Response<List<ComplaintChat>> findByParentId(Long parentId) {
        Response<List<ComplaintChat>> resp = new Response<List<ComplaintChat>>();
        try {
            List<ComplaintChat> complaintChats = complaintChatDao.findByParentId(parentId);
            if (complaintChats == null || complaintChats.size() < 1) {
                log.warn("No records found  with parentId:{}", parentId);
                resp.setError("snz_complaint_chats.select.failed");
                return resp;
            }
            resp.setResult(complaintChats);
            return resp;
        } catch (Exception e) {
            resp.setError("snz_complaint_chats.select.failed");
            log.error("Failed to findByParentId from `snz_complaint_chats` with parentId:{}", parentId, e);
            return resp;
        }
    }


    @Override
    public Response<Integer> update(ComplaintChat complaintChat) {
        Response<Integer> resp = new Response<Integer>();
        if (complaintChat == null) {
            log.error("param complaintChat can not be null when update");
            resp.setError("params.not.null");
            return resp;
        }
        try {
            resp.setResult(complaintChatDao.update(complaintChat));
            return resp;
        } catch (Exception e) {
            resp.setError("snz_complaint_chats.update.failed");
            log.error("Failed to update table `snz_complaint_chats` with param:{}", complaintChat, e);
            return resp;
        }
    }

    @Override
    public Response<Integer> deleteByIds(List<Long> ids) {
        Response<Integer> resp = new Response<Integer>();
        if (ids == null) {
            log.error("param can not be null when deleteByIds");
            resp.setError("params.not.null");
            return resp;
        }
        try {
            resp.setResult(complaintChatDao.deleteByIds(ids));
            return resp;
        } catch (Exception e) {
            resp.setError("snz_complaint_chats.delete.failed");
            log.error("Failed to deleteByIds from `snz_complaint_chats` with param:{}", ids, e);
            return resp;
        }
    }
}
