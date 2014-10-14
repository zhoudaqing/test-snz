package io.terminus.snz.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * Author:Guo Chaopeng
 * Created on 14-8-26.
 */
public class SupplierUpdatedInfoDto<T> implements Serializable {

    private static final long serialVersionUID = 3447647424178071028L;

    @Setter
    @Getter
    private T supplierInfo;                //供应商信息（包括待审核的信息）

    @Setter
    @Getter
    private Map<String, Object> oldValues; //原信息

}
