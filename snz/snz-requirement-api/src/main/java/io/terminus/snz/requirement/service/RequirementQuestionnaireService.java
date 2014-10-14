package io.terminus.snz.requirement.service;

import io.terminus.common.model.Paging;
import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.model.RequirementQuestionnaire;

/**
 * Author:Guo Chaopeng
 * Created on 14-9-22.
 */
public interface RequirementQuestionnaireService {

    /**
     * 创建调查问卷信息
     *
     * @param baseUser                 登录用户
     * @param requirementQuestionnaire 调查问卷
     * @return 是否成功
     */
    @Export(paramNames = {"baseUser", "requirementQuestionnaire"})
    public Response<Boolean> createQuestionnaire(BaseUser baseUser, RequirementQuestionnaire requirementQuestionnaire);

    /**
     * 分页查询问卷信息
     *
     * @param corporation     供应商名称
     * @param requirementName 需求名称
     * @param pageNo          页码
     * @param size            每页大小
     * @return 问卷信息
     */
    @Export(paramNames = {"corporation", "requirementName", "pageNo", "size"})
    public Response<Paging<RequirementQuestionnaire>> findQuestionnairesByPaging(String corporation, String requirementName, Integer pageNo, Integer size);

    /**
     * 根据id查询调查问卷
     *
     * @param id 调查问卷编号
     * @return 调查问卷信息
     */
    @Export(paramNames = {"id"})
    public Response<RequirementQuestionnaire> findQuestionnaireById(Long id);

}
