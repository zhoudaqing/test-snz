package io.terminus.snz.eai.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 从MDM同步过来的银行数据
 *
 * Date: 8/5/14
 * Time: 11:22
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */

@NoArgsConstructor
@ToString
public class MDMBankView implements Serializable {
    private static final long serialVersionUID = 6089558806784697343L;

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String code;        // 银行编号

    @Getter
    @Setter
    private String name;        // 银行名字

    @Getter
    @Setter
    private String country;     // 银行国家
}
