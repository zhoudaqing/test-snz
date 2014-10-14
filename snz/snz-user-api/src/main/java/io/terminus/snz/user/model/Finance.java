package io.terminus.snz.user.model;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


/**
 * Description：财务信息
 * Author：Guo Chaopeng
 * Created on 14-5-3-下午13:50
 */
@ToString
public class Finance implements Serializable {

    private static final long serialVersionUID = -4882889071744335630L;

    @Getter
    @Setter
    private Long id;                     //自增主键

    @Getter
    @Setter
    private Long companyId;              //企业编号

    @Getter
    @Setter
    private Long userId;                 //用户编号

    @Getter
    @Setter
    @NotNull
    private Integer country;             //国家

    @Getter
    @Setter
    @NotBlank
    private String openingBank;          //开户行

    @Getter
    @Setter
    @NotBlank
    private String openLicense;          //开户许可证

    @Getter
    @Setter
    @NotBlank
    private String bankCode;             //银行码

    @Getter
    @Setter
    @NotBlank
    private String bankAccount;          //银行账号

    @Getter
    @Setter
    @NotNull
    private Integer coinType;            //结算币种

    public static enum CoinType {
        RMB(1, "RMB"),
        GBP(2, "英镑"),
        USD(3, "美元"),
        EUR(4, "欧元"),
        JPY(5, "日元");

        private final Integer value;

        private final String description;

        private CoinType(Integer value, String description) {
            this.value = value;

            this.description = description;
        }

        public static CoinType from(Integer value) {
            for (CoinType coinType : CoinType.values()) {
                if (Objects.equal(value, coinType.value)) {
                    return coinType;
                }
            }

            return null;
        }

        public Integer value() {
            return value;
        }

        @Override
        public String toString() {
            return description;
        }
    }


    @Getter
    @Setter
    @NotBlank
    private String recentFinance;        //近三年销售额和净利润（json存储）

    @Getter
    @Setter
    private Date createdAt;              //创建时间

    @Getter
    @Setter
    private Date updatedAt;              //修改时间

}