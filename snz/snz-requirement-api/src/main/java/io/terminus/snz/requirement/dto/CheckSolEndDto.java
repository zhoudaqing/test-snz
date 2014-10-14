package io.terminus.snz.requirement.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Desc:校验供应商提交方案的结果处理
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-09-22.
 */
@ToString
@EqualsAndHashCode
public class CheckSolEndDto implements Serializable {
    private static final long serialVersionUID = 9209045975624193534L;

    @Setter
    @Getter
    private Boolean costResult;     //价格的校验是否通过

    @Setter
    @Getter
    private Integer paidResult;     //返回保证金状态-> 0：未提交，1：提交成功，-1：提交失败，2：交易成功，-2：交易失败
}
