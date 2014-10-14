package io.terminus.snz.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * Author:Guo Chaopeng
 * Created on 14-8-11.
 */
public class SupplierInfoChangedDto implements Serializable {

    private static final long serialVersionUID = 925720863194898660L;

    @Setter
    @Getter
    private boolean isChanged;                  //信息是否更改

    @Setter
    @Getter
    private Map<String, String> changedInfo;    //修改的字段

}
