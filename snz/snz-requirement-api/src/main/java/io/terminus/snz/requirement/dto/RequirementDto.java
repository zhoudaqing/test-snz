package io.terminus.snz.requirement.dto;

import io.terminus.snz.related.dto.ParkFactoryDto;
import io.terminus.snz.requirement.model.Requirement;
import io.terminus.snz.requirement.model.RequirementTeam;
import io.terminus.snz.requirement.model.RequirementTime;
import io.terminus.snz.user.model.SupplierAppointed;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Desc:详细需求信息
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-04.
 */
public class RequirementDto implements Serializable {
    private static final long serialVersionUID = 2034745266423603483L;

    @Setter
    @Getter
    private Requirement requirement;                    //需求信息

    @Setter
    @Getter
    private String moduleFileURL;                       //当使用导入excel文档进行模块信息导入时需要传回一个file的url

    @Setter
    @Getter
    private List<RequirementTeam> teamList;             //需求团队信息

    @Setter
    @Getter
    private List<RequirementTime> timeList;             //需求阶段时间

    @Setter
    @Getter
    private List<ParkFactoryDto> parkFactoryDtoList;    //工厂园区信息

    @Getter
    @Setter
    private SupplierAppointed supplierAppointed;        //甲指供应商信息
}
