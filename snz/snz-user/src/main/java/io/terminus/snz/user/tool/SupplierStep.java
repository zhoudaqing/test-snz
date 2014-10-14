package io.terminus.snz.user.tool;

import com.google.common.base.Objects;

/**
 * Author:Guo Chaopeng
 * Created on 14-7-3.
 */
public enum SupplierStep {
    REGISTER(1, "注册"),
    UPLOAD_PAPERWORK(2, "上传三证"),
    WAIT_FOR_APPROVE(3, "等待审批"),
    ENTER_SUCCESS(4, "入驻成功"),
    COMPLETE_INFO(5, "完善信息"),
    QUALIFY(6, "验证"),
    STANDARD_SUPPLIER(7, "合格供应商");

    private final int value;

    private final String display;

    private SupplierStep(int value, String display) {
        this.value = value;
        this.display = display;
    }

    public static SupplierStep from(Integer value) {
        for (SupplierStep step : SupplierStep.values()) {
            if (Objects.equal(step.value, value)) {
                return step;
            }
        }
        return null;
    }

    public int value() {
        return value;
    }

    public String toString() {
        return display;
    }

}
