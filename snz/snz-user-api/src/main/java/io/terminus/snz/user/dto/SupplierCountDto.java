package io.terminus.snz.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Author:Guo Chaopeng
 * Created on 14-7-24.
 */
public class SupplierCountDto implements Serializable {

    private static final long serialVersionUID = 3648717193999507844L;
    @Setter
    @Getter
    private List<Date> dates;        //日期

    @Setter
    @Getter
    private List<String> months;     //月份（用于按层次统计）

    @Setter
    @Getter
    private List<Long> result1;      //条件1查询结果

    @Setter
    @Getter
    private List<Long> result2;      //条件2查询结果

}
