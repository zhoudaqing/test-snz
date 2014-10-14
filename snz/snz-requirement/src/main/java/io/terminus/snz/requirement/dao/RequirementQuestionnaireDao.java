package io.terminus.snz.requirement.dao;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.terminus.common.model.Paging;
import io.terminus.snz.requirement.model.RequirementQuestionnaire;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Author:Guo Chaopeng
 * Created on 14-9-22.
 */
@Repository
public class RequirementQuestionnaireDao extends SqlSessionDaoSupport {

    public Long create(RequirementQuestionnaire requirementQuestionnaire) {
        getSqlSession().insert("RequirementQuestionnaire.insert", requirementQuestionnaire);
        return requirementQuestionnaire.getId();
    }

    public RequirementQuestionnaire findByUserIdAndRequirementId(Long userId, Long requirementId) {
        return getSqlSession().selectOne("RequirementQuestionnaire.findByUserIdAndRequirementId", ImmutableMap.of("userId", userId, "requirementId", requirementId));
    }

    public RequirementQuestionnaire findById(Long id) {
        return getSqlSession().selectOne("RequirementQuestionnaire.findById", id);
    }

    public Paging<RequirementQuestionnaire> findByPaging(String corporation, String requirementName, Integer offset, Integer limit) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("corporation", Strings.emptyToNull(corporation));
        params.put("requirementName", Strings.emptyToNull(requirementName));

        Long count = getSqlSession().selectOne("RequirementQuestionnaire.count", params);
        if (count == 0) {
            return new Paging<RequirementQuestionnaire>(0L, Collections.<RequirementQuestionnaire>emptyList());
        }

        params.put("offset", offset);
        params.put("limit", limit);
        List<RequirementQuestionnaire> requirementQuestionnaires = getSqlSession().selectList("RequirementQuestionnaire.findByPaging", params);

        return new Paging<RequirementQuestionnaire>(count, requirementQuestionnaires);
    }

}
