package io.terminus.snz.user.model;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import io.terminus.common.utils.Joiners;
import io.terminus.common.utils.Splitters;
import io.terminus.pampas.common.BaseUser;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description：用户
 * Author：Guo Chaopeng
 * Created on 14-5-3-下午12:27
 */
@ToString
public class User extends BaseUser {

    @Getter
    @Setter
    private Long id;                           //自增主键

    @Getter
    @Setter
    private Long outerId;                     //外部id, 比如海尔portal用户的id

    @Getter
    @Setter
    private String name;                       //真实姓名

    @Getter
    @Setter
    private String nick;                       //昵称

    @Getter
    @Setter
    private String encryptedPassword;          //加密后的密码

    @Getter
    @Setter
    private String phone;                      //固定电话

    @Getter
    @Setter
    private String email;                      //电子邮箱

    @Setter
    @Getter
    private String roleStr;                    //角色名(多个角色以逗号隔开)

    public enum JobRole {

        RESOURCE("resource", "资源小微"),
        BIG_DATA("big_data", "供应商大数据"),
        SHARING("sharing", "共享财务"),
        SHARING_SB("share_boss", "共享财务上级"),
        COMPETITIVE("competitive", "模块竞争力小微（商务小微）"),
        COMMON_RESEARCH("common_research", "利共体研发"),
        COMMON_REPUTATION("common_reputation", "利共体口碑"),
        COMMON_CHAIN("common_chain", "利共体供应链"),
        COMMON_MANUFACTURE("common_manufacture", "利共体制造（制造小微）"),
        PRODUCT_CHIEF("product_chief", "生产总监"),
        MICRO_MASTER("micro_master", "小微主（二级审核员）");

        private final String role, description;

        private JobRole(String role, String description) {
            this.role = role;
            this.description = description;
        }

        public static JobRole from(String role) {
            for (JobRole job : JobRole.values()) {
                if (Objects.equal(role, job.role)) {
                    return job;
                }
            }
            return null;
        }

        public String role() {
            return role;
        }

        @Override
        public String toString() {
            return description;
        }
    }

    @Getter
    @Setter
    private Integer status;                    //状态

    public static enum Status {
        FROZEN(-1, "已冻结"),
        OK(0, "正常");

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

    public static enum Type {
        ADMIN(1, "管理员"),
        SUPPLIER(2, "供应商"),
        PURCHASER(3, "采购商");

        private final int value;

        private final String display;

        private Type(int value, String display) {
            this.value = value;
            this.display = display;
        }

        public static Type from(Integer value) {
            for (Type type : Type.values()) {
                if (Objects.equal(type.value, value)) {
                    return type;
                }
            }
            return null;
        }

        public static Type fromName(String name) {
            for (Type type : Type.values()) {
                if (Objects.equal(type.name(), name.toUpperCase())) {
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
    private Integer approveStatus;       //审核状态：-2：修改信息审核不通过，-1:入驻审核不通过，0：未触发审核，1:入驻待审核，2：审核通过，3：修改信息待审核

    public static enum ApproveStatus {
        MODIFY_INFO_FAIL(-2, "修改信息审核不通过"),
        ENTER_FAIL(-1, "入驻审核不通过"),
        INIT(0, "未触发审核"),
        ENTER_WAITING_FOR_APPROVE(1, "入驻待审核"),
        OK(2, "审核通过"),
        MODIFY_INFO_WAITING_FOR_APPROVE(3, "修改信息待审核");

        private final int value;

        private final String display;

        private ApproveStatus(int value, String display) {
            this.value = value;
            this.display = display;
        }

        public static ApproveStatus from(Integer value) {
            for (ApproveStatus status : ApproveStatus.values()) {
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
    private Date enterPassAt;  //入驻审核通过时间

    public static enum SearchStatus {
        INDEX(1, "索引"),
        DELETE(2, "删除");

        private final int value;

        private final String display;

        private SearchStatus(int value, String display) {
            this.value = value;
            this.display = display;
        }

        public static SearchStatus from(int value) {
            for (SearchStatus ss : SearchStatus.values()) {
                if (Objects.equal(ss.value, value)) {
                    return ss;
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
    private Integer accountType;                   //账号类型（账号类型, 1:制造商，2：渠道商）

    public static enum AccountType {
        MANUFACTURER(1, "制造商"),
        CHANNEL(2, "渠道商");

        private final int value;

        private final String display;

        private AccountType(int value, String display) {
            this.value = value;
            this.display = display;
        }

        public static AccountType from(Integer value) {
            for (AccountType accountType : AccountType.values()) {
                if (Objects.equal(accountType.value, value)) {
                    return accountType;
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


    private Integer origin;        //用户来源

    public Integer getOrigin() {
        return origin;
    }

    public void setOrigin(Integer origin) {
        this.origin = origin;
    }

    public static enum Origin {
        NORMAL(0, "正常入驻"),
        BIZ(1, "百卓接入"),
        CLOUD(2, "云平台"),
        HAIER_PORTAL(3, "海尔Portal");

        private final int value;

        private final String display;

        private Origin(int value, String display) {
            this.value = value;
            this.display = display;
        }

        public static Origin from(Integer value) {
            for (Origin origin : Origin.values()) {
                if (Objects.equal(origin.value, value)) {
                    return origin;
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
    private Integer refuseStatus;      //审核拒绝状态,0：没有被拒绝过，-1:被拒绝过

    public static enum RefuseStatus {
        IS_REFUSED(-1, "被拒绝过"),
        NOT_REFUSED(0, "没有被拒绝过");

        private final int value;

        private final String display;

        private RefuseStatus(int value, String display) {
            this.value = value;
            this.display = display;
        }

        public static RefuseStatus from(Integer value) {
            for (RefuseStatus status : RefuseStatus.values()) {
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
    @Deprecated
    private Integer qualifyStatus;      //资质校验状态

    @Deprecated
    public enum QualifyStatus {
        SUBMITTED(1, "已提交，等待审核"), NO_SUBMISSION(-1, "没有提交（或提交失败）"),
        QUALIFIED(2, "资质检查通过"), QUALIFY_FAILED(-2, "资质检查不通过");

        private final Integer value;
        private final String description;

        private QualifyStatus(Integer value, String description) {
            this.value = value;

            this.description = description;
        }

        public static QualifyStatus from(Integer value) {
            for (QualifyStatus qualifyStatus : QualifyStatus.values()) {
                if (Objects.equal(value, qualifyStatus.value)) {
                    return qualifyStatus;
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

    @Setter
    @Getter
    private String tags;      //用户标签

    public List<String> buildTags() {
        List<String> modified = Lists.newArrayList();
        if (!Strings.isNullOrEmpty(tags)) {
            modified.addAll(Splitters.COMMA.splitToList(tags));
        }
        return modified;
    }

    public void setTagsFromList(List<String> tagList) {
        if (tagList != null) {
            tags = Joiners.COMMA.join(tagList);
        }
    }

    public static enum SupplierTag {
        /* 状态机 注册->完善信息->入围->备选->(合作->)淘汰 */
        REGISTER_SUPPLIER(1, "注册"),
        COMPLETE_SUPPLIER(2, "完善信息"),
        STANDARD_SUPPLIER(3, "入围"),
        ALTERNATIVE(4, "备选"),
        IN_SUPPLIER(5, "合作"),
        DIE_OUT(6, "淘汰"),
        WORLD_TOP_SUPPLIER(7, "500强"),
        PERFORMANCE(8, "绩效"),
        CREDIT(9, "信用等级评价"),
        RESOURCE_MATERIAL(10, "资质交互");

        private final int value;

        private final String display;

        private SupplierTag(int value, String display) {
            this.value = value;
            this.display = display;
        }

        public static SupplierTag from(Integer value) {
            for (SupplierTag tag : SupplierTag.values()) {
                if (Objects.equal(tag.value, value)) {
                    return tag;
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
    private Integer step; //供应商当前所处阶段：注册->完善信息->入围->备选->(合作->)淘汰

    public static enum Step {
        /* 所处阶段 注册->完善信息->入围->备选->(合作->)淘汰 */
        REGISTER_SUPPLIER(1, "注册"),
        COMPLETE_SUPPLIER(2, "完善信息"),
        STANDARD_SUPPLIER(3, "入围"),
        ALTERNATIVE(4, "备选"),
        IN_SUPPLIER(5, "合作"),
        DIE_OUT(6, "淘汰");

        private final int value;

        private final String display;

        private Step(int value, String display) {
            this.value = value;
            this.display = display;
        }

        public static Step from(Integer value) {
            for (Step step : Step.values()) {
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

    /**
     * 判断当前用户是否某类型
     *
     * @param t 用户类型
     * @return 若是返回true, 反之false
     */
    public static boolean is(BaseUser user, Type t) {
        return t.value() == user.getType();
    }

    /**
     * 由roleStr分割成role列表
     *
     * @return role列表
     */
    public List<String> buildRoles() {
        return Splitter.on(",").splitToList(this.roleStr);
    }

    public String rolesToString(List<String> roles) {
        return Joiner.on(",").skipNulls().join(roles);
    }

    public void addRole(String role) {
        List<String> roles = new ArrayList<String>(buildRoles());
        roles.add(role);
        this.roleStr = rolesToString(roles);
    }

    public void delRole(String role) {
        List<String> roles = new ArrayList<String>(buildRoles());
        roles.remove(role);
        this.roleStr = rolesToString(roles);
    }

    public boolean hasRole(String role) {
        return buildRoles().contains(role);
    }

    @Setter
    @Getter
    public Date lastSubmitApprovalAt;   //最近提交审核(入驻审核和修改信息审核)时间

    @Getter
    @Setter
    private Date lastLoginAt;

    @Getter
    @Setter
    private Date createdAt;

    @Getter
    @Setter
    private Date updatedAt;

}
