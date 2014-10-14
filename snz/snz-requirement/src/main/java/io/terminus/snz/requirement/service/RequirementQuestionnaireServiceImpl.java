package io.terminus.snz.requirement.service;

import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import io.terminus.common.model.PageInfo;
import io.terminus.common.model.Paging;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.dao.RequirementQuestionnaireDao;
import io.terminus.snz.requirement.model.RequirementQuestionnaire;
import io.terminus.snz.user.model.Company;
import io.terminus.snz.user.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Author:Guo Chaopeng
 * Created on 14-9-22.
 */
@Slf4j
@Service
public class RequirementQuestionnaireServiceImpl implements RequirementQuestionnaireService {

    @Autowired
    private RequirementQuestionnaireDao requirementQuestionnaireDao;

    @Autowired
    private CompanyService companyService;

    @Override
    public Response<Boolean> createQuestionnaire(BaseUser baseUser, RequirementQuestionnaire requirementQuestionnaire) {
        Response<Boolean> result = new Response<Boolean>();

        try {

            if (baseUser == null) {
                log.error("user not login");
                result.setError("user.not.login");
                return result;
            }

            if (requirementQuestionnaire == null) {
                log.error("requirement questionnaire can not be null");
                result.setError("requirement.questionnaire.not.null.fail");
                return result;
            }

            if (requirementQuestionnaire.getRequirementId() == null) {
                log.error("requirement id can not be null");
                result.setError("requirement.id.null");
                return result;
            }

            if (Strings.isNullOrEmpty(requirementQuestionnaire.getRequirementName())) {
                log.error("requirement name can not be null");
                result.setError("requirement.name.null");
                return result;
            }

            Long userId = baseUser.getId();

            RequirementQuestionnaire existed = requirementQuestionnaireDao.findByUserIdAndRequirementId(userId, requirementQuestionnaire.getRequirementId());
            if (existed != null) {
                log.error("requirement questionnaire duplicated where user id={},requirement id={}", userId, requirementQuestionnaire.getRequirementId());
                result.setError("requirement.questionnaire.duplicated");
                return result;
            }

            Response<Company> companyResponse = companyService.findCompanyByUserId(userId);
            if (!companyResponse.isSuccess()) {
                log.error("company not found where user id={}", userId);
                result.setError("company.not.found");
                return result;
            }

            requirementQuestionnaire.setUserId(userId);
            requirementQuestionnaire.setCompanyId(companyResponse.getResult().getId());
            requirementQuestionnaire.setCorporation(companyResponse.getResult().getCorporation());

            requirementQuestionnaireDao.create(requirementQuestionnaire);
            result.setResult(Boolean.TRUE);

        } catch (Exception e) {
            log.error("fail to create requirement questionnaire where user id={},cause:{}", baseUser.getId(), Throwables.getStackTraceAsString(e));
            result.setError("create.requirement.questionnaire.fail");
        }
        return result;
    }

    @Override
    public Response<Paging<RequirementQuestionnaire>> findQuestionnairesByPaging(String corporation, String requirementName, Integer pageNo, Integer size) {
        Response<Paging<RequirementQuestionnaire>> result = new Response<Paging<RequirementQuestionnaire>>();

        try {

            PageInfo page = new PageInfo(pageNo, size);
            Paging<RequirementQuestionnaire> paging = requirementQuestionnaireDao.findByPaging(corporation, requirementName, page.getOffset(), page.getLimit());
            result.setResult(paging);

        } catch (Exception e) {
            log.error("fail to find requirement questionnaire,cause{}", Throwables.getStackTraceAsString(e));
            result.setError("find.requirement.questionnaire.fail");
            return result;
        }

        return result;
    }

    @Override
    public Response<RequirementQuestionnaire> findQuestionnaireById(Long id) {
        Response<RequirementQuestionnaire> result = new Response<RequirementQuestionnaire>();

        try {

            RequirementQuestionnaire requirementQuestionnaire = requirementQuestionnaireDao.findById(id);
            if (requirementQuestionnaire == null) {
                log.error("requirement questionnaire not found where id={}", id);
                result.setError("requirement.questionnaire.not.found");
                return result;
            }

            result.setResult(requirementQuestionnaire);

        } catch (Exception e) {
            log.error("fail to find requirement questionnaire where id={},cause:{}", id, Throwables.getStackTraceAsString(e));
            result.setError("find.requirement.questionnaire.fail");
        }

        return result;
    }

}
