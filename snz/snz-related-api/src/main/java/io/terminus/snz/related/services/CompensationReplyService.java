package io.terminus.snz.related.services;

import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.related.models.CompensationReply;

/**
 * Author:  wenhaoli
 * Date: 2014-08-12
 */
public interface CompensationReplyService {

    /**
     * 创建会话
     * @param re 会话对象
     * @param listId 列表记录id
     * @return 会话对象
     * */
     @Export(paramNames = {"user","re","listId"})
     Response<CompensationReply> create(BaseUser user, CompensationReply re, Long listId);
  }
