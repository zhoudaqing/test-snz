package io.terminus.snz.user.model;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Author:Guo Chaopeng
 * Created on 14-7-23.
 */
public class SupplierCountByLevel implements Serializable {

    private static final long serialVersionUID = 672291488944828693L;

    @Setter
    @Getter
    private Long id;

    @Setter
    @Getter
    private String month;

    @Setter
    @Getter
    private Integer level;

    public static enum Level {
        BEST(1, "优选"),
        STANDARD(2, "合格"),
        LIMITED(3, "限制"),
        BAD(4, "淘汰");

        private final int value;

        private final String display;

        private Level(int value, String display) {
            this.value = value;
            this.display = display;
        }

        public static Level from(Integer value) {
            for (Level level : Level.values()) {
                if (Objects.equal(level.value, value)) {
                    return level;
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

    @Setter
    @Getter
    private Long supplierCount;

    @Setter
    @Getter
    private Date createdAt;

    @Setter
    @Getter
    private Date updatedAt;

}
