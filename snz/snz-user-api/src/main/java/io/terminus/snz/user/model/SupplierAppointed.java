package io.terminus.snz.user.model;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;

/**
 * Desc:甲指库信息
 * Mail:guork@terminus.io
 * author Grancy Guo
 * Date:2014-09-15.
 */
public class SupplierAppointed implements Serializable {

    private static final long serialVersionUID = 6595431078038122774L;

    @Getter
    @Setter
    private Long id;                              //自增主键

    @Getter
    @Setter
    private String corporation;                   //法人公司名称

    @Getter
    @Setter
    private Long requirementId;                  //需求id

    @Getter
    @Setter
    private String requirementName;              //需求name

    @Getter
    @Setter
    private String seriesIds;                    //系列编号(后台三级类目)

    @Getter
    @Setter
    private Long creatorId;                      //创建需求的人员编号

    @Getter
    @Setter
    private Long companyId;                       //供应商公司表id

    @Getter
    @Setter
    private String advice;                        //审核意见

    @Getter
    @Setter
    private Integer status;                       //审核状态，0 初始，1 已提交，2 初审通过，3 初审未通过，4 终审通过，5终审未通过

    public static enum Status {
        INIT(0, "初始"),
        SUBMITTED(1, "已提交"),
        FIRSTPASS(2, "初审通过"),
        FIRSTNOTPASS(3, "初审未通过"),
        LASTPASS(4, "终审通过"),
        LASTNOTPASS(5, "终审未通过");

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

    @Getter
    @Setter
    private Date createdAt;                      //创建时间

    @Getter
    @Setter
    private Date updatedAt;                      //修改时间
}
