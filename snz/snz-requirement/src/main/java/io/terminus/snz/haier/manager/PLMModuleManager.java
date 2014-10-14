package io.terminus.snz.haier.manager;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.terminus.pampas.common.Response;
import io.terminus.snz.haier.dao.PLMModuleDao;
import io.terminus.snz.haier.model.PLMModule;
import io.terminus.snz.requirement.dao.ModuleDao;
import io.terminus.snz.requirement.dao.OldModuleDao;
import io.terminus.snz.requirement.dao.RequirementDao;
import io.terminus.snz.requirement.dao.RequirementSendDao;
import io.terminus.snz.requirement.model.*;
import io.terminus.snz.user.model.Company;
import io.terminus.snz.user.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Desc:海尔模块中间表操作
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-08.
 */
@Slf4j
@Component
public class PLMModuleManager {
    //产品部代码信息
    public static final Map<String , String> PLM_PRODUCTS = new HashMap<String, String>(){
        private static final long serialVersionUID = -560887276503396089L;
        {
            put("冰箱" , "06-冰箱");
            put("全自动洗衣机" , "03-波轮洗衣机");
            put("双桶洗衣机" , "03-波轮洗衣机");
            put("滚筒洗衣机" , "02-滚筒洗衣机");
            put("洗碗机" , "12-洗碗机");
            put("家用空调" , "01-家用空调");
            put("商用空调" , "15-商用空调");
            put("油烟机" , "23-吸油烟机");
            put("冷柜" , "07-冷柜");
            put("医疗冷柜" , "27-医用冷柜");
            put("热水器" , "04-热水器");
        }
    };

    public static final DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");

    @Autowired
    private PLMModuleDao plmModuleDao;              //海尔的模块中间表数据

    @Autowired
    private ModuleDao moduleDao;                    //交互平台模块操作对象

    @Autowired
    private OldModuleDao oldModuleDao;              //老品数据操作

    @Autowired
    private CompanyService companyService;          //供应商信息管理

    @Autowired
    private RequirementDao requirementDao;          //需求编号

    @Autowired
    private RequirementSendDao requirementSendDao;  //需求状态切换

    /**
     * 根据需求获取需求下的所有模块，将模块信息写入到plm中间表
     * @param requirement   需求信息
     */
    public void sendToPLM(final Requirement requirement){
        //获取全部需要写入的模块数据信息
        List<Module> moduleList = moduleDao.findModules(requirement.getId());

        if(moduleList.isEmpty()){
            log.debug("send module info to plm table failed, module info is empty");
            return;
        }

        //过滤已存在老品编号的模块
        List<PLMModule> plmModules = Lists.newArrayList();
        for(Module module : moduleList){
            if(module.getModuleNum() == null){
                PLMModule plmModule = new PLMModule();

                plmModule.setBuNo(PLM_PRODUCTS.get(requirement.getProductName()));
                plmModule.setDemandId(requirement.getId().toString());
                plmModule.setDemandName(requirement.getName());
                plmModule.setDemandRelPerson(requirement.getCreatorName());
                plmModule.setDemandRelTime(dateFormat.print(new DateTime(requirement.getSendTime())));
                plmModule.setModuleExampleId(module.getId().toString());
                plmModule.setModuleExampleName(module.getModuleName());

                plmModules.add(plmModule);
            }
        }

        Integer sendNum = plmModuleDao.createBatch(plmModules);

        //模块数据信息已完全提交到plm表
        RequirementSend requirementSend = new RequirementSend();
        requirementSend.setRequirementId(requirement.getId());
        requirementSend.setSendPLM(Objects.equal(moduleList.size() , sendNum) ? RequirementSend.Type.COMMIT.value() : RequirementSend.Type.UN_COMMIT.value());

        requirementSendDao.update(requirementSend);
        log.debug("send module info success.");
    }

    /**
     * 批量实现向模块数据，写入海尔plm系统生成的模块编号
     */
    public void setModuleNum(){
        //获取全部未写入模块专用号的模块信息
        List<Module> moduleList = moduleDao.findNoModuleNum(null);

        //做需求的cache
        Map<Long , Requirement> reqCache = new HashMap<Long, Requirement>();

        if(moduleList.isEmpty()){
            log.debug("find moduleList is empty.");
            return;
        }

        //获取模块编号
        List<String> moduleIds = Lists.transform(moduleList , new Function<Module, String>() {
            @Nullable
            @Override
            public String apply(Module module) {
                return module.getId().toString();
            }
        });

        //获取haier的模块编号
        Map<Long , String> moduleMap = plmModuleDao.findAllModule(moduleIds);
        Module module;
        //使用entry遍历，它们的执行效率大概为1.5:1与keySet对比
        for(Map.Entry<Long , String> entry : moduleMap.entrySet()){
            module = new Module();
            module.setId(entry.getKey());
            module.setModuleNum(entry.getValue());

            moduleDao.update(module);
        }

        //写入老品数据
        OldModule oldModule;
        Requirement requirement;
        for(Module newModule : moduleList){
            requirement = queryRequirement(reqCache , newModule.getRequirementId());
            oldModule = new OldModule();

            oldModule.setRequirementId(newModule.getRequirementId());
            oldModule.setRequirementName(requirement.getName());
            oldModule.setPurchaserId(0l);
            oldModule.setPurchaserName("海尔");
            oldModule.setModuleId(newModule.getId());
            oldModule.setModuleName(newModule.getModuleName());
            oldModule.setModuleNum(moduleMap.get(newModule.getId()));
            oldModule.setTotal(newModule.getTotal());
            oldModule.setSeriesId(newModule.getSeriesId());
            oldModule.setSeriesName(newModule.getSeriesName());

            oldModuleDao.update(oldModule);
        }

        //记录需求的状态信息
        RequirementSend requirementSend;
        for(Long requirementId : reqCache.keySet()){
            //查询对应的需求下是否还有未提交到plm的数据
            if(moduleDao.findNoModuleNum(requirementId).isEmpty()){
                requirementSend = new RequirementSend();
                requirementSend.setRequirementId(requirementId);
                requirementSend.setSendPLM(RequirementSend.Type.COMMIT.value());

                //更新plm的数据信息
                requirementSendDao.update(requirementSend);
            }
        }
    }

    /**
     * 根据需求的配额情况更新plm中间表的供应商V码信息
     * @param quotaList 配额信息列表
     * @return Integer
     * 返回更新的数据条数
     */
    public Integer updateSupplierV(List<ModuleQuota> quotaList){
        Map<Long , List<ModuleQuota>> quotaMap = Maps.newHashMap();
        //将配额信息分配到对应的moduleId下
        for(ModuleQuota moduleQuota : quotaList){
            List<ModuleQuota> moduleQuotas = quotaMap.get(moduleQuota.getModuleId());
            if(moduleQuotas == null){
                moduleQuotas = Lists.newArrayList(moduleQuota);
                quotaMap.put(moduleQuota.getModuleId() , moduleQuotas);
            }else{
                moduleQuotas.add(moduleQuota);
            }
        }

        //供应商V码获取
        Map<Long , String> supplierVCode = Maps.newHashMap();

        //根据moduleId更新&插入数据
        List<PLMModule> updateList = Lists.newArrayList();
        List<PLMModule> insertList = Lists.newArrayList();
        for(Map.Entry<Long , List<ModuleQuota>> entry : quotaMap.entrySet()){
            List<PLMModule> plmModules = plmModuleDao.findByModuleId(entry.getKey().toString());
            if(!plmModules.isEmpty()){
                PLMModule plmModule = plmModules.get(0);

                //模块配额数据
                ModuleQuota quota = entry.getValue().get(0);

                //写入供应商V码
                if(supplierVCode.get(quota.getSupplierId()) == null){
                    Response<Company> companyRes = companyService.findCompanyById(quota.getSupplierId());
                    if(!companyRes.isSuccess()){
                        log.error("find company v code filed , companyId={} error code={}", quota.getSupplierId(), companyRes.getError());
                        break;
                    }
                    supplierVCode.put(quota.getSupplierId() , companyRes.getResult().getSupplierCode());
                }
                plmModule.setSupplierVId(supplierVCode.get(quota.getSupplierId()));
                plmModule.setSupplierName(quota.getSupplierName());
                updateList.add(plmModule);


                //插入新的PLM数据信息
                PLMModule newModule;
                for(int i=1; i<entry.getValue().size(); i++){
                    newModule = new PLMModule();
                    newModule.setBuNo(plmModule.getBuNo());
                    newModule.setDemandId(plmModule.getDemandId());
                    newModule.setDemandName(plmModule.getDemandName());
                    newModule.setDemandRelPerson(plmModule.getDemandRelPerson());
                    newModule.setDemandRelTime(plmModule.getDemandRelTime());
                    newModule.setModuleExampleId(plmModule.getModuleExampleId());
                    newModule.setModuleExampleName(plmModule.getModuleExampleName());

                    //写入供应商V码
                    if(supplierVCode.get(quota.getSupplierId()) == null){
                        Response<Company> companyRes = companyService.findCompanyById(quota.getSupplierId());
                        if(!companyRes.isSuccess()){
                            log.error("find company v code filed , companyId={} error code={}", quota.getSupplierId(), companyRes.getError());
                            break;
                        }
                        supplierVCode.put(quota.getSupplierId() , companyRes.getResult().getSupplierCode());
                    }
                    plmModule.setSupplierVId(supplierVCode.get(quota.getSupplierId()));
                    plmModule.setSupplierName(quota.getSupplierName());

                    insertList.add(newModule);
                }
            }
        }

        //批量更新数据
        Integer updateLength = plmModuleDao.updateSupplierVId(updateList);

        //批量创建数据
        plmModuleDao.createBatch(insertList);

        return updateLength;
    }

    /**
     * 根据需求编号获取详细的需求信息
     * @param requirementMap    需求的cache
     * @param requirementId     需求编号
     * @return  Requirement
     * 返回需求详细信息
     */
    private Requirement queryRequirement(Map<Long , Requirement> requirementMap , Long requirementId){
        if(requirementMap.get(requirementId) == null){
            requirementMap.put(requirementId , requirementDao.findById(requirementId));
        }

        return requirementMap.get(requirementId);
    }
}
