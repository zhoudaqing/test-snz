package io.terminus.snz.user.dto;

import io.terminus.snz.user.model.CompanyExtraQuality;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 7/7/14
 */
public class CompanyExtraQualityDto implements Serializable{

    private static final long serialVersionUID = -8503272496715632723L;

    @Getter
    @Setter
    private CompanyExtraQuality companyExtraQuality;

    @Getter
    @Setter
    private List<CtqCpkDto> ctqCpkList;
}
