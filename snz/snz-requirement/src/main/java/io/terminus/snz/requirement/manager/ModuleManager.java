package io.terminus.snz.requirement.manager;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import io.terminus.common.utils.JsonMapper;
import io.terminus.snz.requirement.dao.DerivativeDiffDao;
import io.terminus.snz.requirement.dao.MWOldModuleInfoDao;
import io.terminus.snz.requirement.dao.ModuleDao;
import io.terminus.pampas.common.Response;
import io.terminus.snz.category.model.BackendCategoryProperty;
import io.terminus.snz.category.service.BackendCategoryService;
import io.terminus.snz.requirement.dao.ModuleFactoryDao;
import io.terminus.snz.requirement.dao.RequirementDao;
import io.terminus.snz.requirement.model.MWOldModuleInfo;
import io.terminus.snz.requirement.model.Module;
import io.terminus.snz.requirement.model.ModuleFactory;
import io.terminus.snz.requirement.model.Requirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.terminus.snz.requirement.model.Module.Unit;

/**
 * 模块的事物类
 *
 * Date: 8/13/14
 * Time: 17:53
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */

@Slf4j
@Component
public class ModuleManager {

    private static final Integer PAGE_SIZE = 500;

    private static final JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();

    @Autowired
    private MWOldModuleInfoDao mwOldModuleInfoDao;

    @Autowired
    private RequirementDao requirementDao;

    @Autowired
    private ModuleDao moduleDao;                            //模块

    @Autowired
    private ModuleFactoryDao moduleFactoryDao;              //模块工厂管理

    @Autowired
    private BackendCategoryService backendCategoryService;  //后台类目管理

    @Autowired
    DerivativeDiffDao derivativeDiffDao;

    /**
     * 从中间表 mw_old_module_infos 读取数据，写入老品表
     * @return 写入后的老品的id列表
     */
    @Transactional
    public List<Long> writeOldModule() {
        Long maxId = mwOldModuleInfoDao.maxId();
        List<Long> ids = Lists.newArrayList();

        if (maxId==null) {
            return ids;
        }

        List<MWOldModuleInfo> forDump = mwOldModuleInfoDao.forDump(maxId, PAGE_SIZE);
        Module moduleParam = new Module();
        while (!forDump.isEmpty()) {
            for (MWOldModuleInfo old:forDump) {
                // if not exists, warn and skip
                moduleParam.setModuleNum(old.getModuleNum());
                Module module = moduleDao.findOneBy(moduleParam);
                if (module==null) {
                    log.warn("find module by module number fail, module number:{}", old.getModuleNum());
                    continue;
                }

                // set series, module name
                module.setModuleName(old.getModuleName());
                module.setSeriesId(old.getSeriesId());
                Unit units = jsonMapper.fromJson(module.getUnits(), Unit.class);
                if (units == null) {
                    units = new Unit();
                    units.setCost(new Unit());
                    units.setDelivery(new Unit());
                }

                // set units
                units.getCost().setSalesId("1");
                units.getCost().setSalesName(old.getUnit());
                units.getCost().setQuantityId("1");
                units.getCost().setQuantityName(1);
                units.getDelivery().setSalesId("1");
                units.getDelivery().setSalesName(old.getUnit());
                module.setUnits(jsonMapper.toJson(units));

                // update module
                if (moduleDao.update(module)) {
                    ids.add(module.getId());
                }
            }

            // next page
            maxId = forDump.get(forDump.size() - 1).getId();
            forDump = mwOldModuleInfoDao.forDump(maxId, PAGE_SIZE);
        }

        // clean up
        mwOldModuleInfoDao.deleteInIds(ids);
        return ids;
    }

    /**
     * 统计需求的正选供应商&备选供应商数量写入requirement(同时计算出每个模块下的工厂对应的供应商数量)
     * @param requirementId   需求编号
     */
    public void countSelectNum(Long requirementId){
        //做一个方法级的属性信息的cache
        Map<Long , Long[]> propertyCache = new HashMap<Long, Long[]>();

        //获取全部模块信息
        List<Module> moduleList = moduleDao.findModules(requirementId);
        int maxSelect = 1;
        ModuleFactory newModuleFactory;
        Long[] property;

        //写入每个模块下的工厂的正选供应商数量
        List<ModuleFactory> factoryList;
        for(Module module : moduleList){

            //获取对应模块的工厂数据信息
            factoryList = moduleFactoryDao.findByModuleId(module.getId());

            for(ModuleFactory moduleFactory : factoryList){
                //处理未设定数量的工厂数据信息
                if(moduleFactory.getSelectNum() == null || moduleFactory.getSelectNum() == 0){
                    newModuleFactory = new ModuleFactory();
                    newModuleFactory.setId(moduleFactory.getId());

                    //获取对应于工厂的模块正选人数
                    property = querySourceProperty(propertyCache , moduleFactory.getPropertyId());
                    if(property == null){
                        continue;
                    }

                    //写入模块的供应商数量选择
                    if(module.getTotal() > property[0] && module.getTotal() < property[1]){
                        newModuleFactory.setSelectNum(2);
                        maxSelect = maxSelect == 3 ? 3 : 2;
                    }else if(module.getTotal() > property[1]){
                        //需求的最大支持3个正选供应商
                        newModuleFactory.setSelectNum(3);
                        maxSelect = 3;
                    }else{
                        newModuleFactory.setSelectNum(1);
                    }

                    //更新模块的工厂配额信息
                    moduleFactoryDao.update(newModuleFactory);
                }else{
                    //获取需求量的最大供应商选取数量
                    maxSelect = maxSelect < moduleFactory.getSelectNum() ? moduleFactory.getSelectNum() : maxSelect;
                }
            }
        }

        //更新正选&备选供应商数量
        Requirement newReq = new Requirement();
        newReq.setId(requirementId);
        newReq.setSelectNum(maxSelect);
        newReq.setReplaceNum(1);
        requirementDao.update(newReq);
    }

    /**
     * 根据属性编号获取类目属性信息(数组－》0:资源量1，1:资源量2)
     * @param propertyMap   类目属性列表
     * @param propertyId    属性编号
     * @return  Long[]
     * 返回类目属性信息
     */
    private Long[] querySourceProperty(Map<Long , Long[]> propertyMap, Long propertyId){
        try{
            //写入类目属性信息
            if(propertyMap.get(propertyId) == null){
                Response<BackendCategoryProperty> propertyRes = backendCategoryService.findPropertyById(propertyId);
                if(!propertyRes.isSuccess()){
                    log.error("find backend category failed, error code={}", propertyRes.getError());
                    return null;
                }
                BackendCategoryProperty property = propertyRes.getResult();
                //写入资源量信息
                propertyMap.put(propertyId , new Long[]{Long.parseLong(property.getValue2()), Long.parseLong(property.getValue3())});
            }

            return propertyMap.get(propertyId);
        }catch(Exception e){
            log.error("query source property failed, error code={}", Throwables.getStackTraceAsString(e));
        }

        return null;
    }
}
