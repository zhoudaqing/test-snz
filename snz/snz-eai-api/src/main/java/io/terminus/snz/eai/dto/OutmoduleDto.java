package io.terminus.snz.eai.dto;

import io.terminus.snz.eai.model.OutPriceInfo;
import io.terminus.snz.eai.model.OutQuotaInfo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by luyuzhou on 14-9-9.
 */
public class OutmoduleDto implements Serializable{
    @Getter
    @Setter
    private String modulnum;    //物料号

    @Getter
    @Setter
    private List<OutQuotaInfo> outQuotaInfo;    //配额信息

    @Getter
    @Setter
    private List<OutPriceInfo> outPriceInfo;    //价格信息


}
