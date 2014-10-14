package io.terminus.snz.message.services;

import com.google.common.base.*;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import io.terminus.common.model.PageInfo;
import io.terminus.common.model.Paging;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.message.components.MessageCenter;
import io.terminus.snz.message.daos.mysql.MessageDao;
import io.terminus.snz.message.daos.redis.RedisMessageDao;
import io.terminus.snz.message.dtos.MessageDto;
import io.terminus.snz.message.dtos.MessageMark;
import io.terminus.snz.message.dtos.MessagePagingView;
import io.terminus.snz.message.managers.MessageManager;
import io.terminus.snz.message.models.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 消息服务实现
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-6-7
 */
@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private RedisMessageDao redisMessageDao;

    @Autowired
    private MessageManager messageManager;

    @Autowired
    private MessageCenter messageCenter;

    // messages' cache
    private LoadingCache<Long, Message> messageCache;

    private Splitter commaSplitter = Splitter.on(",").omitEmptyStrings().trimResults();

    public MessageServiceImpl() {
        messageCache = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).build(new CacheLoader<Long, Message>() {
            @Override
            public Message load(Long id) throws Exception {
                return messageDao.findById(id);
            }
        });
    }

    @Override
    public Response<Long> newMsgCount(BaseUser user) {
        Response<Long> resp = new Response<Long>();
        try {
            if (user == null) {
                resp.setError("user.not.login");
                return resp;
            }
            Long newMsgCount = redisMessageDao.getNewMsgsCount(user.getId());
            resp.setResult(newMsgCount == null ? 0 : newMsgCount);
        } catch (Exception e) {
            log.error("fail to get new messages count cause:{}", Throwables.getStackTraceAsString(e));
            resp.setError("topic.message.getnew.fail");
        }
        return resp;
    }

    @Override
    public Response<Message> findById(BaseUser user, Long id) {
        Response<Message> resp = new Response<Message>();
        try {
            if (!isUserLogin(user)) {
                resp.setError("user.not.login");
                return resp;
            }
            //TopicMessage tm = topicMessageDao.findById(id);
            Message m = messageCache.getUnchecked(id);
            if (m == null) {
                resp.setError("message.not.exist");
                return resp;
            }
            resp.setResult(m);
        } catch (Exception e) {
            log.error("fail to get message(id={}) casue:{}", id, Throwables.getStackTraceAsString(e));
            resp.setError("message.get.fail");
        }
        return resp;
    }

    @Override
    public Response<List<Message>> lastest(BaseUser user, Integer limit) {
        Response<List<Message>> resp = new Response<List<Message>>();
        try {
            if (user == null) {
                log.error("user not lgoin.");
                resp.setError("user.not.login");
                return resp;
            }
            Integer pageSize = 5;
            if (limit > 0){
                pageSize = limit;
            }
            List<Long> lastestIds =
                    redisMessageDao.getNewMsgIds(user.getId(), 0, pageSize);
            if (lastestIds == null || lastestIds.size() <= 0) {
                log.error("latest msg ids are empty.");
                resp.setError("message.latest.empty");
                return resp;
            }
            List<Message> lastestMsgs = messageDao.findByIds(lastestIds);
            if (!Objects.equal(lastestIds.size(), lastestMsgs.size())) {
                log.error("missing messages: lastestIds(ids={}), but latestMsgs({})", lastestIds, lastestMsgs);
                resp.setError("message.missing");
                return resp;
            }
            resp.setResult(lastestMsgs);
        } catch (Exception e) {
            log.error("fail to get latest new messages, cause:{}", Throwables.getStackTraceAsString(e));
            resp.setError("message.latest.fail");
        }
        return resp;
    }

    @Override
    public Response<Long> rmNewMsgId(BaseUser user, Long mid) {
        Response<Long> resp = new Response<Long>();
        try {
            if (user == null) {
                log.error("user not login");
                resp.setError("user.not.login");
                return resp;
            }
            if (mid == null || mid < 0L){
                log.error("message id is invalid");
                resp.setError("message.id.invalid");
                return resp;
            }
            resp.setResult(redisMessageDao.rmNewMsg(mid, user.getId()));
        } catch (Exception e) {
            log.error("fail to remove new message(id={}), cause:{}", mid, Throwables.getStackTraceAsString(e));
            resp.setError("message.remove.fail");
        }
        return resp;
    }

    @Override
    public Response<Boolean> rmMsgs(BaseUser user, final String ids) {
        Response<Boolean> resp = new Response<Boolean>();
        try{
            if (user == null) {
                log.error("user not login");
                resp.setError("user.not.login");
                return resp;
            }
            if (Strings.isNullOrEmpty(ids)){
                log.error("msg ids is empty");
                resp.setError("message.ids.empty");
                return resp;
            }
            List<Long> idsLong = Lists.transform(commaSplitter.splitToList(ids), new Function<String, Long>() {
                @Override
                public Long apply(String idStr) {
                    return Long.valueOf(idStr);
                }
            });
            redisMessageDao.rmMsgs(user.getId(), idsLong);
            resp.setResult(Boolean.TRUE);
        } catch (Exception e){
            log.error("fail to remove message(ids={}) from user({}), cause:{}",
                    ids, user, Throwables.getStackTraceAsString(e));
            resp.setError("message.remove.fail");
        }
        return resp;
    }

    @Override
    public Response<Boolean> read(BaseUser user, String ids) {
        Response<Boolean> resp = new Response<Boolean>();
        try{
            if (user == null) {
                resp.setError("user.not.login");
                return resp;
            }
            if (Strings.isNullOrEmpty(ids)){
                log.error("msg ids is empty");
                resp.setError("message.ids.empty");
                return resp;
            }
            List<Long> idsLong = Lists.transform(commaSplitter.splitToList(ids), new Function<String, Long>() {
                @Override
                public Long apply(String idStr) {
                    return Long.valueOf(idStr);
                }
            });
            redisMessageDao.rmNewMsgs(user.getId(), idsLong);
            resp.setResult(Boolean.TRUE);
        } catch (Exception e){
            log.error("fail to read message(ids={}) from user({}), cause:{}",
                    ids, user, Throwables.getStackTraceAsString(e));
            resp.setError("message.read.fail");
        }
        return resp;
    }

    @Override
    public Response<Integer> delete(BaseUser user, Long mid) {
        Response<Integer> resp = new Response<Integer>();
        try {
            if (user == null) {
                resp.setError("user.not.login");
                return resp;
            }
            if (mid == null || mid < 0L){
                log.error("message id is invalid");
                resp.setError("message.id.invalid");
                return resp;
            }
            Message tm = messageDao.findById(mid);
            if (tm == null) {
                log.error("message(id={}) doesn't exist", mid);
                resp.setError("topic.message.not.exist");
                return resp;
            }
            if (!Objects.equal(user.getId(), tm.getUserId())) {
                log.error("you(id={}) don't have permission to delete message(id={}, userId={})", user.getId(), mid, tm.getUserId());
                resp.setError("topic.message.delete.deny");
                return resp;
            }
            //物理删除了, 没有必要去移除用户的消息列表了
            resp.setResult(messageDao.delete(tm.getId()));
            messageCache.invalidate(mid);
        } catch (Exception e) {
            log.error("fail to delete message(id={}), cause: {}", mid, Throwables.getStackTraceAsString(e));
            resp.setError("topic.message.delete.fail");
        }
        return resp;
    }

    @Override
    public Response<MessagePagingView> paging(BaseUser user, Integer pageNo, Integer pageSize, Integer status) {
        Response<MessagePagingView> resp = new Response<MessagePagingView>();
        try {
            PageInfo page = new PageInfo(pageNo, pageSize);
            if (user == null ) {
                log.error("user not login");
                resp.setError("user.not.login");
                return resp;
            }
            Long userId = user.getId();
            MessagePagingView mpv = new MessagePagingView();
            Integer selectedStatus = status;
            if (status != null && status == 1){   //未读消息分页
                Paging<Message> pm = messageManager.pagingNewMsgs(userId, page.getOffset(), page.getLimit());
                if (pm.getTotal() > 0){
                    Long totalNum = redisMessageDao.getAllMsgsCount(userId);
                    mpv.setTotalNum(Long.valueOf(totalNum));
                    mpv.setNewNum(pm.getTotal());
                    mpv.setMsgs(pm);
                }
            } else {            //总消息分页
                selectedStatus = 0;
                Paging<Message> pm = messageManager.pagingAllMsgs(userId, page.getOffset(), page.getLimit());
                if (pm.getTotal() > 0){
                    mpv.setTotalNum(pm.getTotal());
                    List<Long> allNewMsgIds = redisMessageDao.getNewMsgIds(userId);
                    mpv.setNewNum(Long.valueOf(allNewMsgIds.size()));
                    List<MessageMark> lmm = doMark(allNewMsgIds, pm.getData());
                    mpv.setMsgs(new Paging<MessageMark>(pm.getTotal(), lmm));
                }
            }
            mpv.setStatus(selectedStatus);
            resp.setResult(mpv);
        } catch (Exception e) {
            log.error("fail to paging messages(pageNo={}, pageSize={}), cause:{}",
                    pageNo, pageSize, Throwables.getStackTraceAsString(e));
            resp.setError("message.paging.fail");
        }
        return resp;
    }

    /**
     * 标记消息，即标出未读和已读的消息
     * @param newMsgIds 未读消息id列表
     * @param msgs 消息列表
     * @return 返回带标记的消息列表
     */
    private List<MessageMark> doMark(List<Long> newMsgIds, List<Message> msgs) {
        List<MessageMark> markMsgs = Lists.newArrayListWithCapacity(msgs.size());
        MessageMark markMsg;
        for (int i=0; i < msgs.size(); i++){
            markMsg = new MessageMark(msgs.get(i));
            if (newMsgIds.contains(markMsg.getId())){
                markMsg.setIsRead(0);
            } else{
                markMsg.setIsRead(1);
            }
            markMsgs.add(markMsg);
        }
        return markMsgs;
    }

    @Override
    public Response<Boolean> push(Message.Type type, Long uid, Long receiverId, Object datas) {
        return push(type, uid, Arrays.asList(receiverId), datas);
    }

    @Override
    public Response<Boolean> push(Message.Type type, Long uid, List<Long> receiverIds, Object datas) {
        Response<Boolean> resp = new Response<Boolean>();
        try {
            MessageDto md = new MessageDto();
            Message m = new Message();
            m.setUserId(uid);
            m.setMtype(type.value());
            md.setMessage(m);
            md.setReceiverIds(receiverIds);
            md.setDatas(datas);
            messageCenter.publish(md);
            resp.setResult(Boolean.TRUE);
        } catch (Exception e) {
            log.error("failed to push message(type={}, uid={}, datas={}) to user(ids={})",
                    type, uid, receiverIds, datas);
            resp.setError("message.push.fail");
        }
        return resp;
    }

    /**
     * 用户是否登录
     *
     * @return 登录了返回true, 反之false
     */
    private boolean isUserLogin(BaseUser user) {
        Long userId = user.getId();
        if (userId == null || userId <= 0) {
            return false;
        }
        return true;
    }
}
