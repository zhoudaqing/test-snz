package io.terminus.snz.sns.managers;

import com.google.common.collect.Lists;
import io.terminus.snz.sns.daos.mysql.TopicDao;
import io.terminus.snz.sns.daos.mysql.TopicUserDao;
import io.terminus.snz.sns.models.Topic;
import io.terminus.snz.sns.models.TopicUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 话题管理器
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-5-3
 */
@Component
public class TopicManager {

    @Autowired
    private TopicDao topicDao;

    @Autowired
    private TopicUserDao topicUserDao;

    @Transactional
    public int createNonPublic(Topic t, List<Long> joinerIds){
        int created = topicDao.create(t);
        if (created > 0 && !joinerIds.isEmpty()){
            //关联用户与话题
            List<TopicUser> tus = Lists.newArrayListWithCapacity(joinerIds.size());
            TopicUser tu = null;
            for (Long joinerId : joinerIds){
                tu = new TopicUser();
                tu.setUserId(joinerId);
                tu.setTopicId(t.getId());
                tus.add(tu);
            }
            topicUserDao.createBatch(tus);

            // 发送话题创建的消息
//            TopicMessage tm = new TopicMessage();
//            tm.setTopicId(t.getId());
//            tm.setUserId(t.getUserId());
//            tm.setMtype(TopicMessage.Type.TOPIC.value());
//            tm.setCompanyName(t.getCompanyName());
//            tm.setContent(MessageTemplate.buildTopicCreateMsgSave(tm.getCompanyName(), t.getTitle()));
//            if (topicMessageDao.create(tm) > 0){
//                // 向圈子内用户，发送话题创建的消息
//                topicMessageCenter.publish(
//                        new TopicMessageTransfer(tm.getId(),joinerIds));
//            }
        }
        return created;
    }
}
