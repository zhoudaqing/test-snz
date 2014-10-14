package io.terminus.snz.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

import static com.google.common.base.MoreObjects.firstNonNull;

/**
 * Desc: 供应商索赔损失信息统计表
 * Mail: houly@terminus.io
 * author: Hou Luyao
 * Date: 14-9-1.
 */
@ToString
public class SupplierReparationSumaries implements Serializable {
    private static final long serialVersionUID = 3646726429809166747L;

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private Long supplierUid;

    @Getter
    @Setter
    private Integer dailyCount;

    @Getter
    @Setter
    private Integer weeklyCount;

    @Getter
    @Setter
    private Integer monthlyCount;

    @Getter
    @Setter
    private Integer yearlyCount;

    @Getter
    @Setter
    private Long dailyAmount;

    @Getter
    @Setter
    private Long weeklyAmount;

    @Getter
    @Setter
    private Long monthlyAmount;

    @Getter
    @Setter
    private Long yearlyAmount;

    @Getter
    @Setter
    private Date startAt;

    @Getter
    @Setter
    private Date endAt;

    @Getter
    @Setter
    private Date createdAt;

    @Getter
    @Setter
    private Date updatedAt;

    public void increaseDailyAmount(Integer money) {
        this.dailyCount = firstNonNull(this.dailyCount, 0) + 1;
        this.dailyAmount = firstNonNull(this.dailyAmount, 0l) + (long)firstNonNull(money, 0);
    }

    public void increaseWeeklyAmount(Integer money) {
        this.weeklyCount = firstNonNull(this.weeklyCount, 0) + 1;
        this.weeklyAmount = firstNonNull(this.weeklyAmount, 0l) + (long)firstNonNull(money, 0);
    }

    public void increaseMonthlyAmount(Integer money) {
        this.monthlyCount = firstNonNull(this.monthlyCount, 0) + 1;
        this.monthlyAmount = firstNonNull(this.monthlyAmount, 0l) + (long)firstNonNull(money, 0);
    }

    public void increaseYearlyAmount(Integer money) {
        this.yearlyCount = firstNonNull(this.yearlyCount, 0) + 1;
        this.yearlyAmount = firstNonNull(this.yearlyAmount, 0l) + (long)firstNonNull(money, 0);
    }
}
