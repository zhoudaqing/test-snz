package io.terminus.snz.requirement.service;

import com.google.common.base.Throwables;
import io.terminus.common.utils.JsonMapper;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.dao.ReqPredictTimeDao;
import io.terminus.snz.requirement.dao.RequirementTimeDao;
import io.terminus.snz.requirement.dto.RequirementTimeDto;
import io.terminus.snz.requirement.model.RequirementTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Desc:需求时间处理
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-08-19.
 */
@Slf4j
@Service
public class RequirementTimeServiceImpl implements RequirementTimeService {

    private static final JsonMapper JSON_MAPPER = JsonMapper.JSON_NON_DEFAULT_MAPPER;

    @Autowired
    private RequirementTimeDao requirementTimeDao;

    @Autowired
    private ReqPredictTimeDao reqPredictTimeDao;

    @Override
    public Response<Boolean> updateRequirementTimes(String requirementTimes, BaseUser user) {
        Response<Boolean> result = new Response<Boolean>();

        List<RequirementTime> timeList = JSON_MAPPER.fromJson(requirementTimes , JSON_MAPPER.createCollectionType(List.class , RequirementTime.class));

        if(user == null){
            log.error("update requirement times need user login.");
            result.setError("user.not.login");
            return  result;
        }

        if(timeList == null || timeList.isEmpty()){
            log.error("update requirement times need requirement time.");
            result.setError("time.type.null");
            return result;
        }

        try{
            //todo 后期增加对于更改操作时的时间规则的验证(哪些用户可以更改还未确定)
            for(RequirementTime requirementTime : timeList){
                requirementTimeDao.update(requirementTime);
            }

            result.setResult(true);
        }catch(Exception e){
            log.error("update requirement failed, error code={}", Throwables.getStackTraceAsString(e));
            result.setError("requirement.update.failed");
        }
        return result;
    }

    @Override
    public Response<RequirementTimeDto> findRequirementTimes(Long requirementId) {
        Response<RequirementTimeDto> result = new Response<RequirementTimeDto>();

        if(requirementId == null){
            log.error("find requirement time need requirement id.");
            result.setError("requirement.id.null");
            return result;
        }

        try{
            RequirementTimeDto requirementTimeDto = new RequirementTimeDto();

            requirementTimeDto.setReqPredictTimes(reqPredictTimeDao.findByRequirementId(requirementId));
            requirementTimeDto.setRequirementTimes(requirementTimeDao.findByRequirementId(requirementId));

            result.setResult(requirementTimeDto);
        }catch(Exception e){
            log.error("find requirement times failed, requirementId={} error code={}", requirementId, Throwables.getStackTraceAsString(e));
            result.setError("time.find.failed");
        }

        return result;
    }
}
