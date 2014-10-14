package io.terminus.snz.requirement.dto;

import com.google.common.base.Throwables;
import io.terminus.common.utils.JsonMapper;
import io.terminus.snz.requirement.model.Module;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Date;

/**
 * Desc:返回给百卓的模块数据
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-09-20.
 */
@Slf4j
@ToString
@EqualsAndHashCode
public class ModuleBIZDto implements Serializable {
    private static final long serialVersionUID = 7364672594419871708L;

    private static final JsonMapper JSON_MAPPER = JsonMapper.JSON_NON_EMPTY_MAPPER;

    @Getter
    @Setter
    private Long id;                //自增主键

    @Getter
    @Setter
    private String moduleFlowNum;   //模块流水码

    @Getter
    @Setter
    private Long requirementId;     //需求编号

    @Getter
    @Setter
    private Integer type;           //模块类型（1:老品，2:新品，3:衍生号，4:甲指，5:专利）

    @Getter
    @Setter
    private String moduleName;      //模块名称系列名称

    @Setter
    @Getter
    private Long oldModuleId;       //老品模块Id

    @Getter
    @Setter
    private String moduleNum;       //模块专用号(海尔的系统生成的编号)

    @Getter
    @Setter
    private Long seriesId;          //系列编号

    @Getter
    @Setter
    private String seriesName;      //系列名称

    @Getter
    @Setter
    private Long propertyId;        //类目属性编号（针对于整体的模块类目属性）

    @Getter
    @Setter
    private String total;          //模块需求的整体数量统计

    @Getter
    @Setter
    private String quality;        //质量目标（可以有创建者or团队人员填写）

    @Getter
    @Setter
    private String cost;           //成本目标

    @Getter
    @Setter
    private String delivery;       //产能要求

    @Getter
    @Setter
    private String attestations;    //认证要求[{id:10,name:'EU认证'}, {id:10,name:'EU认证'}]

    @Getter
    @Setter
    private Date supplyAt;          //批量供货时间

    @Getter
    @Setter
    private Integer selectNum;      //每个模块选取的供应商数量

    @Getter
    @Setter
    private Date createdAt;         //创建时间

    @Getter
    @Setter
    private Date updatedAt;         //修改时间

    public ModuleBIZDto(){}

    //创建百卓需要的数据
    public ModuleBIZDto(Module module){

        try{
            this.setId(module.getId());
            this.setModuleFlowNum(module.getModuleFlowNum());
            this.setRequirementId(module.getRequirementId());
            this.setType(module.getType());
            this.setModuleName(module.getModuleName());
            this.setOldModuleId(module.getOldModuleId());
            this.setModuleNum(module.getModuleNum());
            this.setSeriesId(module.getSeriesId());
            this.setSeriesName(module.getSeriesName());
            this.setPropertyId(module.getPropertyId());
            this.setTotal(module.getTotal()+"");

            //解析模块单位信息
            ModuleSale moduleSale = JSON_MAPPER.fromJson(module.getUnits() ,  ModuleSale.class);
            CostSale costSale = moduleSale.getCost();
            DeliverySale deliverySale = moduleSale.getDelivery();

            this.setQuality(module.getQuality()+"");
            this.setCost(module.getCost()+" 元"+costSale.getQuantityName()+costSale.getSalesName());
            this.setDelivery(module.getDelivery()+" "+deliverySale.getSalesName());
            this.setAttestations(module.getAttestations());
            this.setSupplyAt(module.getSupplyAt());
            this.setSelectNum(module.getSelectNum());
            this.setCreatedAt(module.getCreatedAt());
            this.setUpdatedAt(module.getUpdatedAt());
        }catch(Exception e){
            log.error("analyze module sale failed,(module={}) error code={}", module, Throwables.getStackTraceAsString(e));
        }
    }
}
