package io.terminus.snz.user.model;

import com.fasterxml.jackson.databind.JavaType;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import io.terminus.common.utils.JsonMapper;
import lombok.*;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Date: 7/31/14
 * Time: 10:57
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */

@ToString
@EqualsAndHashCode
public class SupplierCreditQualify implements Serializable {
    private static final long serialVersionUID = 8213445927389932083L;

    private static final JsonMapper JSON_MAPPER = JsonMapper.JSON_NON_EMPTY_MAPPER;

    private static final JavaType MSG_LIST = JSON_MAPPER.createCollectionType(ArrayList.class, Message.class);

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private Long userId;

    @Getter
    @Setter
    private Long supplierId;

    @Getter
    @Setter
    private Integer status; // -4 EJECT, -3 OTHERS, -2 C, -1 B, 1 BBB, 2 A, 3 AA, 4 AAA, 5 AUTO, 11 APPLYING,

    @Getter
    @Setter
    private String message;

    @Getter
    @Setter
    private String appealMsg;

    @Getter
    @Setter
    private Long reviewer;

    @Getter
    @Setter
    private String reviewerName;

    @Getter
    @Setter
    private Date createdAt;

    @Getter
    @Setter
    private Date updatedAt;

    // 判断当前处于那个阶段
    public TYPE whichType() {
        return TYPE.fromStatus(getStatusEnum());
    }

    // 当前供应商信用等级验证是否通过
    public Boolean isCreditQualified() {
        if (status == null) return false;
        STATUS t = STATUS.fromValue(status);
        return t != null && t.toValue() > 0 && t.toValue() < 10;
    }

    // 当前供应商是否在申请信用等级验证
    public Boolean isApplying() {
        return Objects.equal(STATUS.APPLYING.toValue(), status);
    }

    // 直接通过一个状态的枚举类型复制
    public void setStatusEnum(STATUS s) {
        this.status = s==null?null:s.toValue();
    }

    // 得到一个状态的枚举类型
    public STATUS getStatusEnum() {
        return status==null ? null : STATUS.fromValue(status);
    }

    public void appendMessage(String message, Long uid, String name) {
        ArrayList<Message> msgList = (ArrayList<Message>) MoreObjects.firstNonNull(
                JSON_MAPPER.fromJson(this.message, MSG_LIST),
                Lists.newArrayList());
        msgList.add(new Message(new Date().getTime(), message, uid, name));
        this.message = JSON_MAPPER.toJson(msgList);
    }

    @Nullable
    public List<Message> getMsgList() {
        return JSON_MAPPER.fromJson(message, MSG_LIST);
    }

    public void appendRepealMsg(String message, Long id, String name) {
        ArrayList<Message> msgList = (ArrayList<Message>) MoreObjects.firstNonNull(
                JSON_MAPPER.fromJson(this.appealMsg, MSG_LIST),
                Lists.newArrayList());
        msgList.add(new Message(new Date().getTime(), message, id, name));
        this.appealMsg = JSON_MAPPER.toJson(msgList);
    }

    @Nullable
    public List<Message> getAppealMsgList() {
        return JSON_MAPPER.fromJson(appealMsg, MSG_LIST);
    }

    /**
     * 在数据库中没有对应的 column，作为查询的条件
     */
    public static enum TYPE {
        // status 为 appeal
        APPEAL(0, "申诉中"),
        // status 的 value 为 1 ~ 4
        APPLIED(1, "审核通过"),
        // status 的 value 为 -1 ~ -4
        REJECT(2, "审核未通过"),
        // status 的 value 为 11
        APPLYING(3, "审核中");

        private int value;
        private String display;

        private TYPE(int value, String display) {
            this.value = value;
            this.display = display;
        }

        public static TYPE fromValue(Integer s) {
            if (s == null) return APPLYING;
            for (TYPE t : TYPE.values()) {
                if (t.value == s)
                    return t;
            }
            return APPLYING;
        }

        public int getValue() {
            return value;
        }

        public String getDisplay() {
            return display;
        }

        // 默认返回正在申诉中
        public static TYPE fromStatus(STATUS s) {
            if (s==null) {
                return APPLYING;
            }
            if (s.toValue() < 0) {
                return REJECT;
            }
            if (s.toValue() > 0 && s.toValue() <= 10) {
                return APPLIED;
            }
            if (s.toValue() == 11) {
                return APPLYING;
            }
            if (s.toValue() == 12) {
                return APPEAL;
            }
            return APPLYING;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public static enum STATUS {
        REJECT(-4, "REJECT"),
        OTHERS(-3, "OTHERS"),
        C(-2, "C"),
        B(-1, "B"),
        BBB(1, "BBB"),
        A(2, "A"),
        AA(3, "AA"),
        AAA(4, "AAA"),
        AUTO(5, "AUTO"),
        APPLYING(11, "APPLYING"),
        APPEAL(12, "APPEAL");
        private int value;
        private String display;

        private STATUS(int value, String display) {
            this.value = value;
            this.display = display;
        }

        public static STATUS fromValue(Integer s) {
            if (s == null) return  null;
            for (STATUS t: STATUS.values()) {
                if (t.toValue()==s)
                    return t;
            }
            return null;
        }

        public Integer toValue() {
            return this.value;
        }

        public String toDisplay() {
            return this.display;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    @ToString
    @NoArgsConstructor
    public static class Message implements Serializable{
        private static final long serialVersionUID = 641111139590391063L;

        public Message(Long time, String msg, Long uid, String name) {
            timestamp = time;
            message = msg;
            fromUid = uid;
            fromUName = name;
        }

        @Getter
        @Setter
        private Long timestamp;

        @Getter
        @Setter
        private String message;

        @Getter
        @Setter
        private Long fromUid;

        @Getter
        @Setter
        private String fromUName;
    }
}
