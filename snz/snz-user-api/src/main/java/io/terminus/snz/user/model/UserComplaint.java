package io.terminus.snz.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户抱怨反馈信息<P>
 *
 * Created by wanggen 2014-08-13 14:31:26
 * <PRE>
 * id                       Long        自增主键
 * userId                   Long        抱怨人ID
 * userName                 String      抱怨人
 * productLineId            Integer     产品线ID
 * productLineName          String      产品线名称:后台二级类目
 * frontendCategoryId       Long        模块:前台一级类目
 * frontendCategoryName     String      模块-前台一级类目
 * factoryNum               String      生产工厂编号
 * factoryName              String      工厂名称
 * productOwnerId           Long        产品负责人ID
 * productOwnerName         String      产品负责人
 * complaintTypes           String      抱怨类型(模块包装运输协议|(T-1,停产,欠产,拒单,延单))
 * supplierCode             String      供应商CODE
 * supplierName             String      供应商名称
 * moduleNum                String      模块号
 * moduleName               String      模块名称
 * complaintContent         String      抱怨回馈内容
 * complaintReason          String      抱怨原因
 * claimAmount              Long        索赔金额
 * claimDoc                 String      索赔证明材料
 * claimTotal               Long        索赔合计
 * scoreTotal               Long        积分合计
 * isSendMessage            Boolean     是否发送短信给供应商
 * createdAt                Date        创建时间
 * updatedAt                Date        更新时间
 * </PRE>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserComplaint implements Serializable {

    private static final long serialVersionUID = 6824617440258171437L;

    private Long id;                               //自增主键

    private Long userId;                           //抱怨人ID

    private String userName;                       //抱怨人

    private Integer productLineId;                 //产品线ID

    private String productLineName;                //产品线名称:后台二级类目

    private Long frontendCategoryId;               //模块:前台一级类目

    private String frontendCategoryName;           //模块-前台一级类目

    private String  factoryNum;                    //生产工厂编号

    private String factoryName;                    //工厂名称

    private Long productOwnerId;                   //产品负责人ID

    private String productOwnerName;               //产品负责人

    private String complaintTypes;                 //抱怨类型(模块包装运输协议|(T-1,停产,欠产,拒单,延单))

    private String supplierCode;                   //供应商CODE

    private String supplierName;                   //供应商名称

    private String moduleNum;                      //模块号

    private String moduleName;                     //模块名称

    private String complaintContent;               //抱怨回馈内容

    private String complaintReason;                //抱怨原因

    private Long claimAmount;                      //索赔金额

    private String claimDoc;                       //索赔证明材料

    private Long claimTotal;                       //索赔合计

    private Long scoreTotal;                       //积分合计

    private Boolean isSendMessage;                 //是否需要发送短信给供应商

    private Date createdAt;                        //创建时间

    private Date updatedAt;                        //更新时间


}
