package io.terminus.snz.requirement.manager;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import io.terminus.common.utils.JsonMapper;
import io.terminus.pampas.common.Response;
import io.terminus.snz.message.models.Message;
import io.terminus.snz.message.services.MessageService;
import io.terminus.snz.requirement.dao.*;
import io.terminus.snz.requirement.dto.ModuleSale;
import io.terminus.snz.requirement.dto.SolutionQuotaDto;
import io.terminus.snz.requirement.model.*;
import io.terminus.snz.statistic.model.SolutionCountType;
import io.terminus.snz.statistic.service.SolutionCountService;
import io.terminus.snz.user.model.Company;
import io.terminus.snz.user.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Desc:需求&模块配额数据处理
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-07.
 */
@Slf4j
@Component
public class QuotaManager {
    //默认税码的工厂
    private static final String TAX_DEFAULT = "9123";

    private static final JsonMapper JSON_MAPPER = JsonMapper.JSON_NON_EMPTY_MAPPER;

    @Autowired
    private RequirementSolutionDao requirementSolutionDao;  //需求方案处理

    @Autowired
    private ModuleDao moduleDao;                            //模块处理

    @Autowired
    private DetailQuotaDao detailQuotaDao;                  //详细模块配额处理

    @Autowired
    private ModuleSolutionDao moduleSolutionDao;            //模块方案处理

    @Autowired
    private ModuleQuotationDao moduleQuotationDao;          //模块报价处理

    @Autowired
    private ModuleQuotaDao moduleQuotaDao;                  //模块配额处理

    @Autowired
    private ModuleFactoryDao moduleFactoryDao;              //模块工厂信息

    @Autowired
    private RequirementRankDao requirementRankDao;          //需求排名管理

    @Autowired
    private RequirementDao requirementDao;                  //需求处理

    @Autowired
    private CompanyService companyService;                  //供应商管理

    @Autowired
    private MessageService messageService;                  //消息推送处理

    @Autowired
    private SolutionCountService solutionCountService;      //方案的统计数据信息

    /**
     * 通过方案的名次数据信息进行整体&模块的配额创建（这个是针对于T技术指标来排序得到的名次的配额创建）
     * @param requirement 需求信息
     * @param selectNum 正选的模块数量
     * @param rankList 名次数据信息
     * (在创建名次的同时还需要，未每个正选供应商创建模块配额数据，然后在创建记录到整体配额数据信息中)
     */
    @Transactional
    public void createQuotaWithT(Requirement requirement , Integer selectNum, List<RequirementRank> rankList){
        //todo 这边后期在用EventBus来异步处理一把
        //写入供应商的方案统计数据信息
        setSupSolCount(rankList);

        //批量创建需求的排名信息（包括：正选&备选）
        requirementRankDao.createBatch(rankList);

        //需求编号
        Long requirementId = requirement.getId();

        //创建正选的配额信息
        List<ModuleQuota> quotaList = Lists.newArrayList();
        //记录排名编号
        int rankNum = 1;
        //最优的方案编号
        Long solutionId = null;
        Long supplierId = null;
        List<ModuleQuotation> quotationList;

        //模块数据信息
        Map<Long , Module> moduleMap = queryModules(requirementId);
        Map<Long , ModuleQuotation> quotationMap = Maps.newHashMap();
        for(RequirementRank rank : rankList.subList(0 , selectNum)){
            //获取供应商的方案信息
            RequirementSolution solution = requirementSolutionDao.findByRequirementId(rank.getRequirementId() , rank.getSupplierId());

            //作为需求的默认方案
            if(rankNum == 1){
                solutionId = solution.getId();  //作为最优方案编号
                supplierId = solution.getSupplierId();//供应商编号
                quotationList = moduleQuotationDao.findAllQuotations(solution.getId());
                quotationMap = queryQuotations(quotationList);
            }else{
                quotationList = moduleQuotationDao.findAllQuotations(solution.getId());
            }

            ModuleQuota moduleQuota;
            List<ModuleFactory> moduleFactories;
            ModuleQuotation moduleQuotation;
            //为供应商对每个模块的配额
            for(ModuleQuotation quotation : quotationList){
                moduleFactories = moduleFactoryDao.findByModuleId(quotation.getModuleId());

                //针对模块的每个工厂分配模块资源量信息
                for(ModuleFactory moduleFactory : moduleFactories){
                    Integer scale = countScale(rankNum, moduleFactory.getSelectNum(), selectNum);
                    if(scale == null || scale == 0){
                        continue;
                    }
                    //第一名的报价
                    moduleQuotation = quotationMap.get(quotation.getModuleId());

                    moduleQuota = new ModuleQuota();
                    moduleQuota.setRequirementId(solution.getRequirementId());
                    moduleQuota.setSolutionId(solutionId);
                    moduleQuota.setModuleId(quotation.getModuleId());
                    moduleQuota.setModuleNum(moduleMap.get(quotation.getModuleId()).getModuleNum());
                    moduleQuota.setModuleName(quotation.getModuleName());
                    //写入模块配额的工厂编号
                    moduleQuota.setModuleFactoryId(moduleFactory.getId());
                    moduleQuota.setFactoryNum(moduleFactory.getFactoryNum());
                    moduleQuota.setFactoryName(moduleFactory.getFactoryName());
                    moduleQuota.setSupplierId(solution.getSupplierId());
                    moduleQuota.setSupplierName(solution.getSupplierName());

                    //写入工厂的配额数据信息
                    moduleQuota.setQuantity(moduleFactory.getResourceNum() * scale / 100);
                    moduleQuota.setScale(scale);
                    moduleQuota.setOriginalCost(quotation.getPrice());
                    moduleQuota.setActualCost(moduleQuotation.getPrice());
                    moduleQuota.setCostUnit(moduleMap.get(quotation.getModuleId()).getUnits());

                    //按照T来计算排序的直接默认同意(第一名的情况下)
                    moduleQuota.setStatus(Objects.equal(quotation.getSupplierId(), supplierId) ? ModuleQuota.Status.ACCEPT.value() : null);
                    quotaList.add(moduleQuota);
                }
            }

            rankNum++;
        }

        //在需求中确认最终的需求方案
        Requirement updateInfo = new Requirement();
        updateInfo.setId(requirementId);
        updateInfo.setSolutionId(solutionId);  //设置为第一名的方案作为最终方案（按照T来排序）
        requirementDao.update(updateInfo);

        //批量创建配额信息
        moduleQuotaDao.createBatch(quotaList);

        //向用户推送消息
        sendMessage(requirement , quotaList);
    }

    /**
     * 这个是根据各个供应商模块方案的C（报价）来进行排序以及模块分配配额(需要比较多个供应商的模块报价方案，查找出最优的报价方案)
     * @param requirement 需求信息
     * @param selectNum 正选的模块数量
     * @param rankList  名次数据信息
     */
    @Transactional
    public void createQuotaWithC(Requirement requirement , Integer selectNum, List<RequirementRank> rankList){
        //写入供应商的方案统计数据信息
        setSupSolCount(rankList);

        //批量创建需求的排名信息（包括：正选&备选）
        requirementRankDao.createBatch(rankList);

        //模块配额信息列表
        List<ModuleQuota> quotaList = Lists.newArrayList();

        //需求编号
        Long requirementId = requirement.getId();

        //模块数据信息
        Map<Long , Module> moduleMap = queryModules(requirementId);

        //只有一位供应商中标的情况
        if(selectNum == 1){
            RequirementRank rank = rankList.get(0);
            RequirementSolution solution = requirementSolutionDao.findByRequirementId(rank.getRequirementId() , rank.getSupplierId());

            List<ModuleQuotation> quotationList = moduleQuotationDao.findAllQuotations(solution.getId());

            ModuleQuota moduleQuota;
            List<ModuleFactory> moduleFactories;
            //为供应商对每个模块的配额
            for(ModuleQuotation quotation : quotationList){
                moduleFactories = moduleFactoryDao.findByModuleId(quotation.getModuleId());

                //针对模块的每个工厂分配模块资源量信息
                for(ModuleFactory moduleFactory : moduleFactories) {
                    Integer scale = countScale(1, moduleFactory.getSelectNum(), selectNum);
                    if (scale == null || scale == 0) {
                        continue;
                    }

                    moduleQuota = new ModuleQuota();
                    moduleQuota.setRequirementId(solution.getRequirementId());
                    moduleQuota.setSolutionId(solution.getId());
                    moduleQuota.setModuleId(quotation.getModuleId());
                    moduleQuota.setModuleNum(moduleMap.get(quotation.getModuleId()).getModuleNum());
                    moduleQuota.setModuleName(quotation.getModuleName());
                    moduleQuota.setModuleFactoryId(moduleFactory.getId());
                    moduleQuota.setFactoryNum(moduleFactory.getFactoryNum());
                    moduleQuota.setFactoryName(moduleFactory.getFactoryName());
                    moduleQuota.setSupplierId(solution.getSupplierId());
                    moduleQuota.setSupplierName(solution.getSupplierName());
                    moduleQuota.setQuantity(moduleFactory.getResourceNum() * scale / 100);
                    moduleQuota.setScale(scale);
                    moduleQuota.setOriginalCost(quotation.getPrice());
                    moduleQuota.setActualCost(quotation.getPrice());
                    moduleQuota.setCostUnit(moduleMap.get(quotation.getModuleId()).getUnits());

                    //只有一个用户默认为接受
                    moduleQuota.setStatus(ModuleQuota.Status.ACCEPT.value());
                    quotaList.add(moduleQuota);
                }
            }

            //在需求中确认最终的需求方案
            Requirement updateInfo = new Requirement();
            updateInfo.setId(requirementId);
            updateInfo.setSolutionId(0l);
            requirementDao.update(updateInfo);
        }else{
            //当存在多个供应商时需要对模块的报价进行查优
            //所有的模块报价信息列表
            List<List<ModuleQuotation>> moduleQuotations = Lists.newArrayList();
            for(RequirementRank rank : rankList.subList(0 , selectNum)){
                //获取供应商的方案信息
                RequirementSolution solution = requirementSolutionDao.findByRequirementId(rank.getRequirementId() , rank.getSupplierId());

                //查询到所有的模块报价方案
                moduleQuotations.add(moduleQuotationDao.findAllQuotations(solution.getId()));
            }

            //这个模块信息长度
            int length = moduleQuotations.get(0).size();
            int nowNum = 0;
            //遍历全部的模块报价查询最优模块报价
            while(nowNum < length){
                //获取供应商的模块报价方案
                List<ModuleQuotation> sortQuotations = Lists.newArrayList();
                for(List<ModuleQuotation> quotations : moduleQuotations){
                    sortQuotations.add(quotations.get(nowNum));
                }

                //按照模块报价排序
                sortQuotations = sortByPrice(sortQuotations);

                //获取最优模块报价
                ModuleQuotation bestQuotation = sortQuotations.get(0);

                //对供应商按照报价分配配额
                ModuleQuota moduleQuota;
                List<ModuleFactory> moduleFactories;
                int priceRank = 1;
                for(ModuleQuotation quotation : sortQuotations){
                    moduleFactories = moduleFactoryDao.findByModuleId(quotation.getModuleId());

                    //针对模块的每个工厂分配模块资源量信息
                    for(ModuleFactory moduleFactory : moduleFactories) {
                        Integer scale = countScale(priceRank, moduleFactory.getSelectNum(), selectNum);
                        if (scale == null || scale == 0) {
                            continue;
                        }
                        moduleQuota = new ModuleQuota();

                        moduleQuota.setRequirementId(requirementId);
                        //标记是按照C分配的
                        moduleQuota.setSolutionId(0l);
                        moduleQuota.setModuleId(quotation.getModuleId());
                        moduleQuota.setModuleNum(moduleMap.get(quotation.getModuleId()).getModuleNum());
                        moduleQuota.setModuleName(quotation.getModuleName());
                        //工厂编号
                        moduleQuota.setModuleFactoryId(moduleFactory.getId());
                        moduleQuota.setFactoryNum(moduleFactory.getFactoryNum());
                        moduleQuota.setFactoryName(moduleFactory.getFactoryName());
                        moduleQuota.setSupplierId(quotation.getSupplierId());
                        moduleQuota.setSupplierName(quotation.getSupplierName());
                        moduleQuota.setQuantity(moduleFactory.getResourceNum() * scale / 100);
                        moduleQuota.setScale(scale);
                        //记录原始&实际的价格信息
                        moduleQuota.setOriginalCost(quotation.getPrice());
                        moduleQuota.setActualCost(bestQuotation.getPrice());
                        moduleQuota.setCostUnit(moduleMap.get(quotation.getModuleId()).getUnits());

                        //是否是方案最低价格
                        moduleQuota.setStatus(Objects.equal(bestQuotation.getPrice(), quotation.getPrice()) ? ModuleQuota.Status.ACCEPT.value() : null);
                        quotaList.add(moduleQuota);
                    }
                    priceRank++;
                }

                nowNum++;
            }

            //在需求中确认最终的需求方案(按照C来计算就没有最优方案 )
            Requirement updateInfo = new Requirement();
            updateInfo.setId(requirementId);
            updateInfo.setSolutionId(0l);
            requirementDao.update(updateInfo);
        }

        //创建配额
        moduleQuotaDao.createBatch(quotaList);

        //向用户推送消息
        sendMessage(requirement , quotaList);
    }

    /**
     * 更新模块的报价数据信息
     * @param quotaCache 模块的报价数据
     */
    @Transactional
    public void autoUpdateQuota(Map<Long , List<ModuleQuota>> quotaCache){
        //判断是否需要对某个模块的配额进行重新配置
        Boolean exChange;
        List<ModuleQuota> quotaList;
        ModuleQuota newQuota;
        for (Map.Entry<Long, List<ModuleQuota>> entry : quotaCache.entrySet()) {
            quotaList = Lists.newArrayList();
            exChange = false;
            for (ModuleQuota moduleQuota : entry.getValue()) {
                //还未处理跟标操作（则默认为不跟标）
                if (moduleQuota.getStatus() == null) {
                    newQuota = new ModuleQuota();
                    newQuota.setId(moduleQuota.getId());
                    newQuota.setScale(0);
                    newQuota.setQuantity(0);
                    newQuota.setStatus(ModuleQuota.Status.DISAGREE.value());
                    moduleQuotaDao.update(newQuota);
                    exChange = true;
                } else {
                    quotaList.add(moduleQuota);
                }
            }

            //是否需求重新配额生成
            if (exChange) {
                updateQuotas(quotaList);
            }
        }
    }

    /**
     * 根据需要更改的配额的信息更改配额的比例&数量
     * @param quotaList 需要更改的配额信息
     */
    public void updateQuotas(List<ModuleQuota> quotaList){
        switch (quotaList.size()) {
            case 1:     //当还存在一个用户时的处理
                ModuleQuota moduleQuota = new ModuleQuota();
                moduleQuota.setId(quotaList.get(0).getId());
                moduleQuota.setScale(100);
                moduleQuota.setQuantity(quotaList.get(0).getQuantity() / quotaList.get(0).getScale() * 100);

                moduleQuotaDao.update(moduleQuota);
                break;

            case 2:     //当还存在二个用户的配额分配处理
                Integer total = quotaList.get(0).getQuantity() / quotaList.get(0).getScale();
                ModuleQuota moduleQuota1 = new ModuleQuota();
                moduleQuota1.setId(quotaList.get(0).getId());
                moduleQuota1.setQuantity(total * 70);
                moduleQuota1.setScale(70);

                ModuleQuota moduleQuota2 = new ModuleQuota();
                moduleQuota2.setId(quotaList.get(1).getId());
                moduleQuota2.setQuantity(total * 30);
                moduleQuota2.setScale(30);

                moduleQuotaDao.update(moduleQuota1);
                moduleQuotaDao.update(moduleQuota2);
                break;
            default:
                break;
        }
    }

    /**
     * 通过需求编号查询整体的解决方案信息
     * @param requirementId 需求编号
     * @return  List
     * 返回需求方案配额标准数据信息
     */
    public List<SolutionQuotaDto> findDetailSolution(Long requirementId){
        List<SolutionQuotaDto> actualSolutions = Lists.newArrayList();

        //获取需求详细的模块数量
        Integer actualModuleNum = moduleDao.countById(requirementId);

        //获取全部的需求方案信息
        List<RequirementSolution> solutionList = requirementSolutionDao.findAllSolution(requirementId);
        //过滤得到数据完整的方案信息
        for(RequirementSolution solution : solutionList){
            //已填写了完整的方案信息
            if(Objects.equal(moduleSolutionDao.countBySolutionId(solution.getId()) , actualModuleNum)){
                actualSolutions.add(new SolutionQuotaDto(solution));
            }
        }

        return actualSolutions;
    }

    /**
     * 根据需求信息&模块配额信息创建详细的需求配额数据(这个是每天晚上定时去计算的)
     * @param requirement   需求信息
     * @param moduleQuotas  模块配额数据
     */
    public void createDetailQuota(Requirement requirement, List<ModuleQuota> moduleQuotas){
        List<DetailQuota> detailQuotaList = Lists.newArrayList();

        Map<Long , Company> companyMap = Maps.newHashMap();
        Map<Long , Module> moduleMap = Maps.newHashMap();
        DetailQuota detailQuota;
        Module moduleInfo;
        for(ModuleQuota quota : moduleQuotas){

            //写入供应商信息
            if(companyMap.get(quota.getSupplierId()) == null){
                Response<Company> companyRes = companyService.findCompanyById(quota.getSupplierId());
                if(!companyRes.isSuccess()){
                    log.error("find company info filed , companyId={} error code={}", quota.getSupplierId(), companyRes.getError());
                    break;
                }
                companyMap.put(quota.getSupplierId() , companyRes.getResult());
            }

            //写入模块详细数据(这边不验证是否存在数据，此次数据必须完整)
            if(moduleMap.get(quota.getModuleId()) == null){
                moduleMap.put(quota.getModuleId() , moduleDao.findById(quota.getModuleId()));
            }
            moduleInfo = moduleMap.get(quota.getModuleId());

            //解析模块单位信息
            ModuleSale moduleSale = JSON_MAPPER.fromJson(moduleInfo.getUnits() ,  ModuleSale.class);

            ModuleFactory moduleFactory = moduleFactoryDao.findById(quota.getModuleFactoryId());

            detailQuota = new DetailQuota();
            detailQuota.setRequirementId(requirement.getId());
            detailQuota.setRequirementName(requirement.getName());
            detailQuota.setSolutionId(quota.getSolutionId());
            detailQuota.setModuleId(quota.getModuleId());
            detailQuota.setModuleNum(moduleInfo.getModuleNum());
            detailQuota.setModuleName(moduleInfo.getModuleName());
            detailQuota.setPurchaseOrg("采购组织");
            detailQuota.setFactoryNum(moduleFactory.getFactoryNum());
            detailQuota.setSupplierId(quota.getSupplierId());
            detailQuota.setSupplierVCode(companyMap.get(quota.getSupplierId()).getSupplierCode());
            detailQuota.setSupplierName(quota.getSupplierName());

            //所占工厂的配额比例
            detailQuota.setQuantity(quota.getQuantity());
            detailQuota.setScale(quota.getScale());
            detailQuota.setOriginalCost(quota.getOriginalCost());
            detailQuota.setActualCost(quota.getActualCost());
            detailQuota.setFeeUnit(moduleSale.getCost().getSalesId());
            detailQuota.setPurchaseUnit(moduleSale.getCost().getQuantityName());
            detailQuota.setCoinType(CoinType.from(requirement.getCoinType()).toString());
            detailQuota.setPurchaseTeam("采购组");
            detailQuota.setBasicUnit(moduleFactory.getSalesName());
            detailQuota.setTaxCode(Objects.equal(quota.getFactoryNum(), TAX_DEFAULT) ? "JO" : "JB");
            detailQuota.setStatus(DetailQuota.Status.WAIT_SUBMIT.value());

            detailQuotaList.add(detailQuota);
        }

        //批量创建详细配额信息
        detailQuotaDao.createBatch(detailQuotaList);
    }

    /**
     * 根据需求编号获取全部的模块数据信息（方便索引module信息）
     * @param requirementId 需求编号
     * @return  Map
     * 返回模块数据信息
     */
    private Map<Long , Module> queryModules(Long requirementId){
        Map<Long , Module> moduleMap = Maps.newHashMap();
        List<Module> moduleList = moduleDao.findModules(requirementId);

        for(Module module : moduleList){
            moduleMap.put(module.getId() , module);
        }

        return moduleMap;
    }

    /**
     * 用Map封装全部的模块方案数据信息(为了方便数据索引)
     * @param quotationList 模块报价信息
     * @return  Map
     * 返回模块报价数据信息
     */
    private Map<Long , ModuleQuotation> queryQuotations(List<ModuleQuotation> quotationList){
        Map<Long , ModuleQuotation> quotationMap = Maps.newHashMap();
        for(ModuleQuotation quotation : quotationList){
            quotationMap.put(quotation.getModuleId() , quotation);
        }

        return quotationMap;
    }

    /**
     * 实现将模块的报价信息按照报价价格进行排序
     * @param quotationList 模块的报价价格
     * @return  List
     * 返回排序好的模块报价信息
     */
    private List<ModuleQuotation> sortByPrice(List<ModuleQuotation> quotationList){
        //排序方式
        Ordering<ModuleQuotation> byPrice = new Ordering<ModuleQuotation>() {
            @Override
            public int compare( ModuleQuotation left, ModuleQuotation right) {
                return left.getPrice() - right.getPrice();
            }
        };

        return byPrice.immutableSortedCopy(quotationList);
    }

    /**
     * 通过当前排名信息&模块选定的正选供应商数量计算出最后的用户的配额情况(返回的是配额的比例)
     * @param rankNum       名次（T：的情况指整体的名次，C：的情况指针对于某一个模块的名次）
     * @param selectNum     工厂级别选取的供应商数量
     * @param reqSelectNum  需求级别的供应商选取数量(实际存在的供应商数量)
     * @return Integer
     * 返回模块的配额计算情况
     */
    private Integer countScale(int rankNum, int selectNum, int reqSelectNum){
        Integer scale = 0;

        //选取供应商数量
        int selectNumber = reqSelectNum < selectNum ? reqSelectNum : reqSelectNum;
        if(selectNumber == 1){
            //小于数量规格，就只分配给一个供应商
            scale = rankNum == 1 ? 100 : 0;
        }else if(selectNumber == 2){
            //数量级超过第一个数量级时的处理
            scale = rankNum == 1 ? 70 : (rankNum == 2 ? 30 : 0);
        }else if(selectNumber == 3){
            //数量级超过第二个数量级时的处理
            //根据具体的选定的供应商数量分配每个供应商的模块配额
            scale = rankNum == 1 ? 50 : (rankNum == 2 ? 30 : 20);
        }

        return scale;
    }

    /**
     * 根据模块的配额分配向供应商用户发送
     * @param requirement  需求对象信息
     * @param quotaList    模块分配信息
     */
    private void sendMessage(Requirement requirement , List<ModuleQuota> quotaList){
        for(ModuleQuota quotation : quotaList){
            //查询公司信息
            Response<Company> companyRes = companyService.findCompanyById(quotation.getSupplierId());

            //推送信息
            messageService.push(Message.Type.REQUIREMENT_SOLUTION_SELECTED , requirement.getCreatorId(),
                    companyRes.getResult().getUserId(), ImmutableMap.of("name" , requirement.getName(), "id", requirement.getId()));
        }
    }

    /**
     * 写入供应商的需求的方案的统计数据
     * @param rankList  方案的统计数据
     */
    private void setSupSolCount(List<RequirementRank> rankList){
        Response<Company> companyRes;

        //增加正选供应商&备选供应供应商的方案统计数据
        for(RequirementRank requirementRank : rankList){
            companyRes = companyService.findCompanyById(requirementRank.getId());
            if(companyRes.isSuccess()){
                if(Objects.equal(RequirementRank.Type.from(requirementRank.getType()), RequirementRank.Type.OFFICIAL)){
                    //正选
                    solutionCountService.setSolCountInfo(companyRes.getResult().getUserId() , SolutionCountType.SELECT_SOL, 1);
                }else {
                    //备选
                    solutionCountService.setSolCountInfo(companyRes.getResult().getUserId() , SolutionCountType.ALTERNATE_SOL , 1);
                }
            }else{
                log.error("find company failed, companyId={} error code={}", requirementRank.getSupplierId(), companyRes.getError());
            }
        }
    }
}
