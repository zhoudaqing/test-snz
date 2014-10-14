package io.terminus.snz.sns.models;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 需求的话题
 * MADE IN IntelliJ IDEA
 * AUTHOR: haolin
 * AT 30/4/14.
 */
@ToString @NoArgsConstructor
public class Topic implements Serializable{

    private static final long serialVersionUID = -5982762071330768459L;

    public static final Topic DEFAULT = new Topic("供应商点击参与交互", "公共讨论区域", Scope.PUBLIC);

    public Topic(String title, String content){
        this.title = title;
        this.content = content;
    }

    public Topic(String title, String content, Scope s){
        this.title = title;
        this.content = content;
        this.scope = s.value();
    }

    @Getter
    @Setter
    private Long id;                    //主键

    @Getter
    @Setter
    private Long userId;                //创建话题的用户id

    @Getter
    @Setter
    private String userName;            //创建话题的用户名

    @Getter
    @Setter
    private Long reqId;                 //对应需求id

    @Getter
    @Setter
    private Integer reqStatus;          //对应的需求状态

    @Getter
    @Setter
    private String reqName;             //对应的需求名称

    @Getter
    @Setter
    private String title;               //标题

    @Getter
    @Setter
    private String content;             //内容

    @Getter
    @Setter
    private Integer scope;              //话题范围

    @Getter
    @Setter
    private Integer joiners;            //圈子人数, 仅话题为圈子话题时才有值

    @Getter
    @Setter
    private String companyName;         //发起话题人的公司名,冗余

    @Getter
    @Setter
    private String files;               //附件json数组列表

    @Getter
    @Setter
    private Long lastReplierId;         //最后回复用户id

    @Getter
    @Setter
    private String lastReplierName;     //最后回复用户名

    @Getter
    @Setter
    private Long totalViews = 0L;        //总浏览数

    @Getter
    @Setter
    private Long totalReplies = 0L;      //总回复数

    @Getter
    @Setter
    private Integer deleted = 0;        //删除标志, 0未删除, 1删除

    @Getter
    @Setter
    private Integer closed = 0;         //关闭标志, 0未关闭, 1关闭

    @Getter
    @Setter
    private Date createdAt;             //创建时间

    @Getter
    @Setter
    private Date updatedAt;             //更新时间

    /**
     * 话题范围枚举
     */
    public static enum Scope {
        PUBLIC(1, "公开讨论"),                 // 公开讨论, 可在系统前台展示, 所有登录用户都可以回复该话题
        CIRCLE_PUBLIC(2, "当前圈子公开"),      //  圈子内公开, 话题圈子内的用户可回复该话题，及可以看见话题下的所有回复信息
        CIRCLE_PRIVATE(3, "当前圈子私密");     //  私密圈子，针对圈子内某些人，此时发起者可以看到所有回复者信息，回复者只能看到发起者与自己的回复信息，即回复者彼此不可看见对方的信息

        private int val;
        private String display;

        private Scope(int val, String display){
            this.val = val;
            this.display = display;
        }

        public int value(){
            return this.val;
        }

        public String display(){
            return this.display;
        }

        public Scope toScope(int val){
            for (Scope r: Scope.values()){
                if (Objects.equal(val, r.val)){
                    return r;
                }
            }
            return null;
        }
    }
}
