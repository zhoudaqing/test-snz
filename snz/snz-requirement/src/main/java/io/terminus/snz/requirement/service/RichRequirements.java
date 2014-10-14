package io.terminus.snz.requirement.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.terminus.common.utils.BeanMapper;
import io.terminus.common.utils.JsonMapper;
import io.terminus.common.utils.Splitters;
import io.terminus.pampas.common.Response;
import io.terminus.snz.category.model.FrontendCategory;
import io.terminus.snz.category.service.FrontendCategoryService;
import io.terminus.snz.related.dto.ParkFactoryDto;
import io.terminus.snz.related.services.DeliveryService;
import io.terminus.snz.requirement.dao.RequirementTimeDao;
import io.terminus.snz.requirement.dto.RichRequirement;
import io.terminus.snz.requirement.model.Requirement;
import io.terminus.snz.requirement.model.RequirementTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yangzefeng on 14-6-20
 */
@Component @Slf4j
public class RichRequirements {

    @Autowired
    private RequirementTimeDao requirementTimeDao;

    @Autowired
    private FrontendCategoryService frontendCategoryService;

    @Autowired
    private DeliveryService deliveryService;    //工厂园区信息


    private static final JsonMapper JSON_MAPPER = JsonMapper.nonDefaultMapper();

    public RichRequirement make(Requirement requirement) {
        RichRequirement richRequirement = BeanMapper.map(requirement, RichRequirement.class);
        //因为需求的园区信息数据更改了，此处需要自行在查找一把
        List<String> deliveryAddresses = Splitters.COMMA.splitToList(requirement.getDeliveryAddress());
        List<Long> parsingAddresses = new ArrayList<Long>();
        for(String address : deliveryAddresses) {
            parsingAddresses.add(Long.parseLong(address));
        }
        Response<List<ParkFactoryDto>> result = deliveryService.findDetailFactories(requirement.getProductId(), parsingAddresses);

        if(!result.isSuccess()){
            log.error("find park factory info failed, error code={}", result.getError());
        }

        //将园区数据获取出来拼接成->青岛,杭州
        List<ParkFactoryDto> parkFactories = result.getResult();
        Map<Long , String> factory = Maps.newHashMap();

        StringBuilder builder = new StringBuilder();
        if(!parkFactories.isEmpty()){
            ParkFactoryDto parkFactoryDto;
            //拼接园区信息
            for(int i=0; i<parkFactories.size(); i++){
                parkFactoryDto = parkFactories.get(i);
                if(factory.get(parkFactoryDto.getParkId()) == null){
                    factory.put(parkFactoryDto.getParkId() , parkFactoryDto.getParkName());

                    builder.append(factory.get(parkFactoryDto.getParkId()));
                    builder.append(i == parkFactories.size()-1 ? "" : ",");
                }
            }
        }

        richRequirement.setDeliveryAddress(builder.toString());
        RequirementTime requirementTime = requirementTimeDao.findByStatus(requirement.getId(), requirement.getStatus());
        richRequirement.setPredictEnd(requirementTime.getPredictEnd());

        //采购总量
        richRequirement.setPurchaseNum(requirement.getModuleTotal());

        //三级前台类目
        List<FrontendCategory> thirdLevels = JSON_MAPPER.fromJson(
                requirement.getCompanyScope() , JSON_MAPPER.createCollectionType(List.class, FrontendCategory.class));
        List<Long> thirdLevelIds = Lists.newArrayListWithCapacity(thirdLevels.size());

        List<Long> secondLevelIds = Lists.newArrayListWithCapacity(thirdLevels.size());

        List<Long> firstLevelIds = Lists.newArrayListWithCapacity(thirdLevels.size());

        List<String> firstLevelNames = Lists.newArrayListWithCapacity(thirdLevelIds.size());

        for(FrontendCategory fc : thirdLevels) {
            thirdLevelIds.add(fc.getId());

            Response<List<FrontendCategory>> ancestorsR = frontendCategoryService.ancestorsOf(fc.getId());
            if(!ancestorsR.isSuccess()) {
                log.error("fail to find ancestor of frontendCategory id={}, error code={}",
                        fc.getId(), ancestorsR.getError());
            }
            List<FrontendCategory> ancestors = ancestorsR.getResult();
            secondLevelIds.add(ancestors.get(1).getId());

            firstLevelIds.add(ancestors.get(2).getId());

            if(!firstLevelNames.contains(ancestors.get(2).getName())) {
                firstLevelNames.add(ancestors.get(2).getName());
            }
        }
        richRequirement.setFirstLevelIds(firstLevelIds);

        richRequirement.setSecondLevelIds(secondLevelIds);

        richRequirement.setThirdLevelIds(thirdLevelIds);

        richRequirement.setFirstLevelNames(firstLevelNames);

        return richRequirement;
    }
}
