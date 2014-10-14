package io.terminus.snz.requirement.dto;

import io.terminus.snz.related.models.AddressFactory;
import io.terminus.snz.requirement.model.Requirement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * Desc:需求详细的信息包括工厂信息
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-08-14.
 */
@ToString
@EqualsAndHashCode
public class RequirementFactoryDto implements Serializable {
    private static final long serialVersionUID = -4527522401072357495L;

    @Setter
    @Getter
    private Requirement requirement;            //需求信息

    @Setter
    @Getter
    private List<AddressFactory> factoryList;   //需求的配置工厂
}
