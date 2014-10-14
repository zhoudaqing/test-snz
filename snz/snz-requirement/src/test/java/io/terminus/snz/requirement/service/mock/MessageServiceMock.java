package io.terminus.snz.requirement.service.mock;

import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.message.dtos.MessagePagingView;
import io.terminus.snz.message.models.Message;
import io.terminus.snz.message.services.MessageService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Desc:mock对象
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-30.
 */
@Service
public class MessageServiceMock implements MessageService {
    @Override
    public Response<Long> newMsgCount(BaseUser user) {
        return null;
    }

    @Override
    public Response<Message> findById(BaseUser user, Long id) {
        return null;
    }

    @Override
    public Response<List<Message>> lastest(BaseUser user, Integer limit) {
        return null;
    }

    @Override
    public Response<Long> rmNewMsgId(BaseUser user, Long mid) {
        return null;
    }

    @Override
    public Response<Boolean> rmMsgs(BaseUser user, String ids) {
        return null;
    }

    @Override
    public Response<Boolean> read(BaseUser user, String ids) {
        return null;
    }

    @Override
    public Response<Integer> delete(BaseUser user, Long mid) {
        return null;
    }

    @Override
    public Response<MessagePagingView> paging(BaseUser user, Integer pageNo, Integer pageSize, Integer status) {
        return null;
    }

    @Override
    public Response<Boolean> push(Message.Type type, Long uid, Long receiverId, Object datas) {
        return null;
    }

    @Override
    public Response<Boolean> push(Message.Type type, Long uid, List<Long> receiverIds, Object datas) {
        return null;
    }
}
