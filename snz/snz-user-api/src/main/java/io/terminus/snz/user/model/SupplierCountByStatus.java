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
public class SupplierCountByStatus implements Serializable {

    private static final long serialVersionUID = 672291488944828693L;

    @Setter
    @Getter
    private Long id;

    @Setter
    @Getter
    private Date date;

    @Setter
    @Getter
    private Integer status;

    public static enum Status {
        REGISTERED(1, "注册"),
        PARTICIPATED(2, "参与交互"),
        STANDARD(3, "入围"),
        PARTNER(4, "合作");

        private final int value;

        private final String display;

        private Status(int value, String display) {
            this.value = value;
            this.display = display;
        }

        public static Status from(Integer value) {
            for (Status status : Status.values()) {
                if (Objects.equal(status.value, value)) {
                    return status;
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
