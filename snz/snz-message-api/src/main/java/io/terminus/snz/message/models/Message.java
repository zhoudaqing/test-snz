package io.terminus.snz.message.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息类
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-6-7
 */
public class Message implements Serializable{
    private static final long serialVersionUID = 85676985629968050L;

    @Getter
    @Setter
    private Long id;            //主健

    @Getter
    @Setter
    private Long userId;        //创建者

    @Getter
    @Setter
    private String content;     //内容

    @Getter
    @Setter
    private Integer mtype;       //消息类型, 如下

    @Getter
    @Setter
    private Date createdAt;     //创建时间

    @Getter
    @Setter
    private Date updatedAt;     //更新时间

    /**
     * 消息类型枚举
     */
    public static enum Type{
        APPLIER_PASS(0, "供应商入驻通过"),
        APPLIER_UNPASS(1, "供应商入驻不通过"),
        APPLIER_INFO_PASS(2, "供应商信息审核通过"),
        APPLIER_INFO_UNPASS(3, "供应商信息审核未通过"),
        REQUIREMENT_APPLY(4, "需求申请"),
        REQUIREMENT_APPROVE_PASS(5, "需求审批通过"),
        REQUIREMENT_APPROVE_UNPASS(6, "需求审批未通过"),
        REQUIREMENT_PUBLISH(7, "需求发布"),
        REQUIREMENT_LOCK(13, "需求锁定"),
        REQUIREMENT_INFO_EMPTY(14, "需求信息未创建完整"),
        REQUIREMENT_CREATE(15, "需求信息创建"),
        REQUIREMENT_WARNING(16, "需求预警"),
        REQUIREMENT_SOLUTION_END(8, "方案终投"),
        REQUIREMENT_SOLUTION_SELECTED(9, "方案选定"),
        SOLUTION_SUBMIT(10, "方案提交"),
        TOPIC_CREATE(11, "话题创建"),
        REPLY_CREATE(12, "话题回复"),
        SUPPLIER_QUALIFY_DEADLINE(20, "供应商资质交互即将到期"),
        SUPPLIER_APPOINTED_UNPASS(30, "甲指供应商信息审核未通过");

        private int val;
        private String desc;

        private Type(int val, String desc){
            this.val = val;
            this.desc = desc;
        }

        public int value(){
            return this.val;
        }

        /**
         * 由消息值返回对应的消息类型
         * @param val 消息值
         * @return 消息类型
         */
        public static Type from(int val){
            for (Type p: Type.values()){
                if (val == p.value()){
                    return p;
                }
            }
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;

        Message message = (Message) o;

        if (!id.equals(message.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /**
     * Redis中保存用户未读消息的Key
     * @param uid 用户id
     * @return 用户未读消息的Key
     */
    public static String keyOfNewMsg(Long uid){
        return "users:" + uid + ":msgs:new";
    }

    /**
     * Redis中保存用户所有消息的Key
     * @param uid 用户id
     * @return 用户所有消息的Key
     */
    public static String keyOfAllMsg(Long uid){
        return "users:" + uid + ":msgs:all";
    }
}
