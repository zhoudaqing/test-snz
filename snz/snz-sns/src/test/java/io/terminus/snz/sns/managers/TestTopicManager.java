package io.terminus.snz.sns.managers;

import io.terminus.snz.requirement.model.CoinType;
import io.terminus.snz.requirement.model.Requirement;
import io.terminus.snz.requirement.model.RequirementStatus;
import io.terminus.snz.sns.BaseManagerTest;
import io.terminus.snz.sns.daos.mysql.TopicDao;
import io.terminus.snz.sns.daos.mysql.TopicUserDao;
import io.terminus.snz.sns.models.Topic;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

/**
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-8-4
 */
public class TestTopicManager extends BaseManagerTest {

    @Mock
    private TopicDao topicDao;

    @Mock
    private TopicUserDao topicUserDao;

    @InjectMocks
    private TopicManager topicManager = new TopicManager();

    @Test
    public void testCreateNonPublic(){
        when(topicDao.create(any(Topic.class))).thenReturn(1);
        Topic mockTopic = mockTopic(1L);
        assertEquals(1, topicManager.createNonPublic(mockTopic, Arrays.asList(1L, 2L)));
    }

    private Topic mockTopic(Long id){
        return mockTopic(id, Topic.Scope.PUBLIC, mockRequirement(1L));
    }

    private Topic mockTopic(Long id, Topic.Scope scope, Requirement req){
        Topic t = new Topic();
        t.setId(id);
        t.setReqId(req.getId());
        t.setReqName(req.getName());
        t.setReqStatus(req.getStatus());
        t.setScope(scope.value());
        t.setCompanyName("xxx公司");
        t.setTitle("ooxx");
        t.setContent("xxoo");
        t.setUserId(req.getCreatorId());
        t.setUserName("xxxooo");
        return t;
    }

    private Requirement mockRequirement(Long id){
        Requirement r = new Requirement();
        r.setId(id);
        r.setCreatorId(id);
        r.setCheckId(id);
        r.setCheckName("me");
        r.setAccessories("");
        r.setAnswerSu(10);
        r.setCheckResult(Requirement.CheckResult.SUCCESS.value());
        r.setCheckTime(new Date());
        r.setCoinType(CoinType.CNY.value());
        r.setCompanyScope("");
        r.setDescription("");
        r.setStatus(RequirementStatus.SOL_END.value());
        return r;
    }
}
