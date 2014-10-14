package io.terminus.snz.requirement.service;

import com.google.common.base.Objects;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.terminus.common.model.PageInfo;
import io.terminus.common.model.Paging;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.dao.ModuleDao;
import io.terminus.snz.requirement.dao.ModuleFactoryDao;
import io.terminus.snz.requirement.dao.RequirementDao;
import io.terminus.snz.requirement.dto.ModuleBIZDto;
import io.terminus.snz.requirement.dto.ModuleDetailBIZDto;
import io.terminus.snz.requirement.dto.RequirementBIZDto;
import io.terminus.snz.requirement.model.Module;
import io.terminus.snz.requirement.model.Requirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Desc:
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-09-03.
 */
@Slf4j
@Service
public class RequirementOutServiceImpl implements RequirementOutService {

    @Autowired
    private RequirementDao requirementDao;

    @Autowired
    private ModuleDao moduleDao;

    @Autowired
    private ModuleFactoryDao moduleFactoryDao;

    @Override
    public Response<Paging<RequirementBIZDto>> sendToBizInfo(Integer pageNo, Integer size, String startAt, String endAt) {
        Response<Paging<RequirementBIZDto>> result = new Response<Paging<RequirementBIZDto>>();

        try{
            Map<String , Object> params = Maps.newHashMap();

            //默认查询100行数据
            PageInfo pageInfo = new PageInfo(pageNo , Objects.firstNonNull(size, 100));
            params.putAll(pageInfo.toMap());
            params.put("upStartAt" , startAt);
            params.put("upEndAt" , endAt);

            //获取全部的需求数据信息
            Paging<Requirement> reqPaging = requirementDao.findByParams(null , params);

            List<RequirementBIZDto> bizReqList = Lists.newArrayList();
            RequirementBIZDto bizDto;
            for(Requirement requirement : reqPaging.getData()){
                bizDto = new RequirementBIZDto();
                //写入需求数据
                bizDto.setRequirementId(requirement.getId());
                bizDto.setRequirementName(requirement.getName() == null ? "null" : requirement.getName());
                bizDto.setMaterielType(requirement.getMaterielType() == null ? 0 : requirement.getMaterielType());
                bizDto.setMaterielName(requirement.getMaterielName() == null ? "null" : requirement.getMaterielName());
                bizDto.setProductId(requirement.getProductId() == null ? 0 : requirement.getProductId());
                bizDto.setProductName(requirement.getProductName() == null ? "null" : requirement.getProductName());
                bizDto.setSeriesIds(requirement.getSeriesIds() == null ? "null" : requirement.getSeriesIds());
                bizDto.setCreateAt(requirement.getCreatedAt());
                bizDto.setUpdateAt(requirement.getUpdatedAt());

                //写入模块的详细数据
                List<Module> moduleList = moduleDao.findModules(requirement.getId());

                List<ModuleDetailBIZDto> moduleInfoDtoList = Lists.newArrayList();
                ModuleDetailBIZDto moduleDetailBIZDto;
                for(Module module : moduleList){
                    moduleDetailBIZDto = new ModuleDetailBIZDto();
                    moduleDetailBIZDto.setModule(new ModuleBIZDto(module));
                    moduleDetailBIZDto.setFactoryList(moduleFactoryDao.findByModuleId(module.getId()));
                    moduleInfoDtoList.add(moduleDetailBIZDto);
                }

                bizDto.setModuleInfoDtoList(moduleInfoDtoList);
                bizReqList.add(bizDto);
            }

            result.setResult(new Paging<RequirementBIZDto>(reqPaging.getTotal() , bizReqList));
        }catch(Exception e){
            log.error("find requirement & module info to send biz failed, error code={}", Throwables.getStackTraceAsString(e));
            result.setError("requirement.find.failed");
        }

        return result;
    }
}
