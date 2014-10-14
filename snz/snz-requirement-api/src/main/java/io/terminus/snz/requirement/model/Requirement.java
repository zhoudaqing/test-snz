package io.terminus.snz.requirement.model;

import com.google.common.base.Objects;
import io.terminus.common.model.Indexable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Desc:采购商的采购需求信息
 * Mail:v@terminus.io
 * author Michael Zhao
 * Date:2014-04-30.
 */
@ToString
@EqualsAndHashCode
public class Requirement implements Serializable,Indexable{

    private static final long serialVersionUID = 1012906476297470349L;

    @Getter
    @Setter
    private Long id;                //自增主键

    @Getter
    @Setter
    private String flowId;          //需求的流水码

    @Getter
    @Setter
    private String name;            //需求模版名称

    @Getter
    @Setter
    private Long purchaserId;       //采购商编号(指的是注册公司的编号)

    @Getter
    @Setter
    private String purchaserName;   //采购商名称

    @Getter
    @Setter
    private Long productId;         //产品类型（二级类目）

    @Getter
    @Setter
    private String productName;     //产品类型名称（二级类目）

    @Getter
    @Setter
    private String seriesIds;       //系列编号(后台三级类目)[{bcId:1,name:冰箱把手},{bcId:21,name:冰箱门}]

    @Getter
    @Setter
    private Long materielType;      //整体需求级别的物料类别（一级类目 1:SKD，2:模块，3:连接件，4:包装印刷辅料，5:二三级物料）

    @Getter
    @Setter
    private String materielName;    //整体需求级别的物料类别名称

    @Getter
    @Setter
    private Integer moduleType;     //整体需求级别的模块类型（1:新品,2:老品,3:衍生号）

    @Getter
    @Setter
    private Long moduleAmount;      //整体的模块总价格（总价格是保存为计算时0.2*100）

    @Getter
    @Setter
    private String deliveryAddress; //配送园区&厂商（1,2,3,4）

    @Getter
    @Setter
    private String description;     //需求描述信息

    @Getter
    @Setter
    private String accessories;     //上传附件内容（使用json保存->[url1,url2]）

    @Getter
    @Setter
    private Integer selectNum;      //选中的供应商数量

    @Getter
    @Setter
    private Integer replaceNum;     //备选方案的供应商数量

    @Getter
    @Setter
    private String companyScope;    //供应商范围定义（用于圈定供应商的范围，作为选取依据）

    @Getter
    @Setter
    private Integer tacticsId;      //模块策略编号(1:技术领先,2:差异化,3:卓越运营)

    @Getter
    @Setter
    private String headDrop;        //引领点

    @Getter
    @Setter
    private Integer coinType;       //需求级别的币种选定

    @Getter
    @Setter
    private Integer moduleNum;      //模块数量(这个数据先保存在后期在用Redis来处理，通过回写记录数据)

    @Getter
    @Setter
    private Integer moduleTotal;    //模块总数

    @Getter
    @Setter
    private Long creatorId;         //创建需求的人员编号（用于索引对应的主管）

    @Getter
    @Setter
    private String creatorName;     //需求创建人名称

    @Getter
    @Setter
    private String creatorPhone;    //需求创建人手机

    @Getter
    @Setter
    private String creatorEmail;    //需求创建人email

    @Getter
    @Setter
    private Integer checkResult;    //审核状态(0:未提交审核,1:待审核,2:审核通过,-1:审核不通过)

    @Getter
    @Setter
    private Long checkId;           //审核人员编号（主管）

    @Getter
    @Setter
    private String checkName;       //审核人员名称（主管名称）

    @Getter
    @Setter
    private Date checkTime;         //审核时间

    @Getter
    @Setter
    private Date sendTime;          //需求发布时间（为了方便查询）

    @Getter
    @Setter
    private Integer transactType;   //1:需求的谈判|2:竞标的确认(只有当需求的阶段处于需求4的阶段－》方案终投阶段)

    @Getter
    @Setter
    private String transactFile;    //谈判文件([{name:1.doc, url:url1},{name:2.doc, url:url2}])'

    @Getter
    @Setter
    private Integer sendSu;         //推送的供应商数量(各种需求的统计数据，当需求进入选定供应商阶段这些统计数据将从redis中回写到数据库)

    @Getter
    @Setter
    private Integer answerSu;       //响应的供应商数量

    @Getter
    @Setter
    private Integer sendSo;         //提交方案的供应商数量

    @Getter
    @Setter
    private Integer topicNum;       //话题数

    /**
     * 不同的场景会有不同的阶段名称（因为是后期追加的一些新的状态，对整体影响太大。所以直接使用多条件确认状态）
     * 场景一：模块属性－》新品、老品，模块策略－》技术领先、差异化
     * 需求状态：-1:删除状态，0:等待发布，1:需求交互，2:需求锁定，3:方案交互，4:方案终投，5:选定供应商与方案，6:招标结束
     *
     * 场景二：模块属性－》新品、老品、衍生号，模块策略－》卓越运营
     * 需求状态：-1:删除状态，0:等待发布，1:需求交互，2:需求锁定，3:承诺底线，4:谈判(transactType:1)｜竞标(transactType:2)，5:选定供应商与方案，6:招标结束
     * 谈判－》当该需求下的供应商提交的方案少于3个，
     * tacticsId:3          ----|
     *                          |
     * status:4             ----|---谈判状态（谈判状态当提交谈判文件后才能条状到下一个阶段）－》选定供应商（手动的分配配额）
     *                          |
     * transactType:1       ----|--else--|
     *                                   |---竞标状态（还是按照原来的新品流程）
     * transactType:2       -------------|
     */
    @Getter
    @Setter
    private Integer status;

    @Getter
    @Setter
    private Long solutionId;        //最终方案编号，当状态为5时确认状态（这个是由需求供应商方案中选取的一个）

    @Getter
    @Setter
    private Date createdAt;         //创建时间

    @Getter
    @Setter
    private Date updatedAt;         //修改时间


    public enum CheckResult{
        WAIT_SUBMIT(0 , "待提交审核"), WAIT(1 , "待审核"), SUCCESS(2 , "审核通过"), FAILED(-1, "审核未通过");

        private final Integer value;

        private final String description;

        private CheckResult(Integer value , String description){
            this.value = value;

            this.description = description;
        }

        public static CheckResult from(Integer value){
            for(CheckResult checkResult : CheckResult.values()){
                if(Objects.equal(value , checkResult.value)){
                    return checkResult;
                }
            }

            return null;
        }

        public Integer value(){
            return value;
        }

        @Override
        public String toString(){
            return description;
        }
    }

    public enum ModuleType{
        NEW_TYPE(1 , "新品"), OLD_TYPE(2, "老品"), DERIVE_TYPE(3 , "衍生号"), JIA_ZHI(4 , "甲指"), PATENT(5 , "专利");

        private final Integer value;

        private final String description;

        private ModuleType(Integer value , String description){
            this.value = value;

            this.description = description;
        }

        public static ModuleType from(Integer value){
            for(ModuleType moduleType : ModuleType.values()){
                if(Objects.equal(value , moduleType.value)){
                    return moduleType;
                }
            }

            return null;
        }

        public Integer value(){
            return value;
        }

        @Override
        public String toString(){
            return description;
        }
    }

    public enum TransactType{
        TRANSACT(1 , "谈判"), COMPETITIVE(2, "竞标");

        private final Integer value;

        private final String description;

        private TransactType(Integer value , String description){
            this.value = value;

            this.description = description;
        }

        public static TransactType from(Integer value){
            for(TransactType transactType : TransactType.values()){
                if(Objects.equal(value , transactType.value)){
                    return transactType;
                }
            }

            return null;
        }

        public Integer value(){
            return value;
        }

        @Override
        public String toString(){
            return description;
        }
    }

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
            for(SearchStatus ss : SearchStatus.values()) {
                if(Objects.equal(ss.value, value)) {
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

    /**
     * 参与过需求的供应商key(只要回复过需求下的任何话题即是)
     * @param rid 需求id
     * @return 参与过需求的供应商key
     */
    public static String keyOfSuppliers(Long rid){
        return "requirements:" + rid + ":suppliers";
    }
}
