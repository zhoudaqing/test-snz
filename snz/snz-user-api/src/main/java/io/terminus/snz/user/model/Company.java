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
 * Description：企业基本信息
 * Author：Guo Chaopeng
 * Created on 14-5-3-下午12:00
 */
@ToString
public class Company implements Serializable {

    private static final long serialVersionUID = -4249000408474061145L;

    @Getter
    @Setter
    private Long id;                             //自增主键

    @Getter
    @Setter
    private Long userId;                         //用户编号

    @Setter
    @Getter
    private Integer resourceType;                //资源类型(0：普通资源，1：标杆企业，2：500强)

    public static enum ResourceType {
        NORMAL(0, "普通资源"),
        BENCH_MARK(1, "标杆企业"),
        WORLD_TOP(2, "500强");

        private final int value;

        private final String display;

        private ResourceType(int value, String display) {
            this.value = value;
            this.display = display;
        }

        public static ResourceType from(Integer value) {
            for (ResourceType type : ResourceType.values()) {
                if (Objects.equal(type.value, value)) {
                    return type;
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
    private String competitors;                  //竞争对手(对采购商而言)（多个用逗号分隔)

    @Setter
    @Getter
    private Integer isComplete;                  //供应商信息是否完整，0/null：不完整，1：完整

    @Setter
    @Getter
    private String includeKeywords;             //公司名称包含的黑名单关键字

    @Setter
    @Getter
    @NotBlank
    private String productLine;                  //所属产品线(json)

    @Getter
    @Setter
    @NotBlank
    private String corporation;                   //法人公司名称

    @Getter
    @Setter
    private String supplierCode;                  //供应商代码

    @Getter
    @Setter
    private String actingBrand;                   //代理的品牌

    @Getter
    @Setter
    private String corpAddr;                      //法人公司地址

    @Getter
    @Setter
    private String zipcode;                       //邮政编号

    @Getter
    @Setter
    private String groupName;                     //集团公司名称

    @Getter
    @Setter
    private String groupAddr;                     //集团公司地址

    @Getter
    @Setter
    @NotBlank
    private String initAgent;                     //法人代表

    @Getter
    @Setter
    private Long fixedAssets;                     //公司固定资产

    @Getter
    @Setter
    private Integer faCoinType;                   //公司固定资产币种

    @Getter
    @Setter
    private Long regCapital;                      //注册资金

    @Getter
    @Setter
    private Integer rcCoinType;                  //注册资金币种

    @Getter
    @Setter
    @NotNull
    private Integer regCountry;                   //注册国家代号

    @Getter
    @Setter
    @NotNull
    private Integer regProvince;                  //隶属省份代号

    @Getter
    @Setter
    @NotNull
    private Integer regCity;                     //隶属城市代号

    @Getter
    @Setter
    private Integer worldTop;                     //是否世界500强（0：否，1：是）

    @Getter
    @Setter
    private String officialWebsite;              //公司网站

    @Getter
    @Setter
    private String personScale;                   //规模人数

    @Getter
    @Setter
    private Date foundAt;                         //成立时间

    @Getter
    @Setter
    @NotBlank
    private String desc;                          //公司简介

    @Getter
    @Setter
    private Integer nature;                        //企业性质

    public static enum Nature {
        STATEOWNED(1, "国有"),
        COLLECTIVE(2, "集体"),
        PRIVATELYOPERATED(3, "民营"),
        PRIVATELYOWNED(4, "私营"),
        JOINTVENTURE(5, "合资"),
        FOREIGNINVESTMENT(6, "外资"),
        OTHERS(7, "其他");
        private final int value;

        private final String display;

        private Nature(int value, String display) {
            this.value = value;
            this.display = display;
        }

        public static Nature from(Integer value) {
            for (Nature status : Nature.values()) {
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
    private Integer listedStatus;                 //上市状态（0：没有上市，1：已上市）

    public static enum ListedStatus {
        NOLISTEDSTATUS(0, "没有上市"),
        LISTEDSTATUS(1, "已上市");

        private final int value;

        private final String display;

        private ListedStatus(int value, String display) {
            this.value = value;
            this.display = display;
        }

        public static ListedStatus from(Integer value) {
            for (ListedStatus status : ListedStatus.values()) {
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
    private String listedRegion;                   //上市地区

    @Getter
    @Setter
    private String ticker;                        //股票代码

    @Getter
    @Setter
    private String businessLicense;               //营业执照路径

    @Getter
    @Setter
    private String businessLicenseId;            //营业执照号

    @Getter
    @Setter
    private Date blDate;                          //营业执照有效期

    @Getter
    @Setter
    private String orgCert;                       //组织机构证路径

    @Getter
    @Setter
    private String orgCertId;                     //组织机构证号

    @Getter
    @Setter
    private Date ocDate;                          //组织机构证号有效期

    @Getter
    @Setter
    private String taxNo;                         //税务登记证路径

    @Getter
    @Setter
    private String taxNoId;                      //税务登记证号

    @Getter
    @Setter
    private Date tnDate;                          //税务登记证号有效期

    @Getter
    @Setter
    private Integer participateCount;            //交互参与数

    @Getter
    @Setter
    @NotBlank
    private String customers;                    //客户群（json存储）

    @Getter
    @Setter
    private String factories;                   //工厂信息（json存储）

    public static enum WorldTop {
        NO_WORLD_TOP_500(0, "非世界五百强"),
        IS_WORLD_TOP_500(1, "世界五百强");

        private final Integer value;

        private final String description;

        private WorldTop(Integer value, String description) {
            this.value = value;

            this.description = description;
        }

        public static WorldTop from(Integer value) {
            for (WorldTop worldTop : WorldTop.values()) {
                if (Objects.equal(value, worldTop.value)) {
                    return worldTop;
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

    /**
     * 供应商信息是否完整
     */
    public boolean isCompleteInfo() {
        return Objects.equal(isComplete, 1);
    }

    @Getter
    @Setter
    private Date createdAt;                      //创建时间

    @Getter
    @Setter
    private Date updatedAt;                      //修改时间


}