package io.terminus.snz.user.dto;

import java.io.Serializable;

/**
 * 约束检查结果
 * Author:Guo Chaopeng
 * Created on 14-8-5.
 */
public class ConstraintCheckResult implements Serializable {

    private static final long serialVersionUID = -6674919647527767408L;

    private Boolean isSuccess;      //是否成功

    private Integer failCount;      //约束检查失败字段数量

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;

        if (isSuccess) {
            this.failCount = 0;
        }
    }

    public Integer getFailCount() {
        return failCount;
    }

    public void setFailCount(Integer failCount) {
        this.failCount = failCount;
        this.isSuccess = Boolean.FALSE;
    }
}
