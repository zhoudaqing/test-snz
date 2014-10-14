package io.terminus.snz.requirement.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

import static io.terminus.common.utils.Arguments.*;

/**
 * 新品导入详情中的具体预算时间和完成情况
 * 表：snz_income_good_rows
 *
 * Date: 7/9/14
 * Time: 11:37
 * Author: 2014年 <a href="mailto:dong.worker@gmail.com">张成栋</a>
 */

@ToString
@EqualsAndHashCode
public class ImportGoodRow implements Serializable {
    private static final long serialVersionUID = 3926604136579340155L;

    private static long MILL_SEC_OF_DAY = 1000*60*60*24;

    @Getter
    @Setter
    private Long id;

    // 预算时间，其实是开始时间
    @Getter
    @Setter
    private Date timeline;

    // 预算周期，其实是预计持续时间
    @Getter
    @Setter
    private Integer duration;

    // 进度，其实是实际完成时间，由海尔调用API录入
    @Getter
    @Setter
    private Date progress;

    // 状态：-2 未定义，-1 未开始，1-提前，0-普通，2-延迟
    @Setter
    private Integer status;

    // 负责人，来自海尔的系统
    @Getter
    @Setter
    private String inCharge;

    @Getter
    @Setter
    private Integer stage;

    @Getter
    @Setter
    private Date createdAt;

    @Getter
    @Setter
    private Date updatedAt;

    public Boolean isValid() {
        return positive(duration)&&notNull(progress)&&notNull(timeline)&&timeline.before(progress);
    }

    public Integer getStatus() {
        if (isNull(progress) && isNull(timeline)) {
            return STATUS.UNDEFINED.toNumber();
        }

        if (isNull(progress) || isNull(timeline)) {
            status = STATUS.SUSPEND.toNumber();
        } else {
            long actualDuration = (long) Math.ceil((progress.getTime() - timeline.getTime()) / MILL_SEC_OF_DAY);

            if (actualDuration > duration) {
                status = STATUS.LATE.toNumber();
            } else if (actualDuration < duration) {
                status = STATUS.EARLY.toNumber();
            } else {
                status = STATUS.NORMAL.toNumber();
            }
        }

        return this.status;
    }

    public static enum STATUS {
        UNDEFINED(-2, "未定义"),
        SUSPEND(-1, "未启动"),
        NORMAL(0, "正常"),
        EARLY(1, "提前"),
        LATE(2, "推迟");

        private final int value;
        private final String desc;

        private STATUS(int i, String str) {
            this.value = i;
            this.desc = str;
        }

        public static STATUS fromNumber(int value) {
            for (STATUS s: STATUS.values()) {
                if(s.value == value) {
                    return s;
                }
            }
            return null;
        }

        public int toNumber() {return value;}

        public String toName() {return desc;}

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }
}
