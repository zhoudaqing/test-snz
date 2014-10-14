package io.terminus.snz.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;


/**
 * Description：公司排名信息
 * Author：Guo Chaopeng
 * Created on 14-5-3-下午12:40
 */
@ToString
public class CompanyRank implements Serializable {

    private static final long serialVersionUID = -6765572297602855676L;

    @Getter
    @Setter
    private Long id;                           //自增主键

    @Getter
    @Setter
    private Long userId;                       //用户编号

    @Getter
    @Setter
    private Long companyId;                    //企业编号

    @Getter
    @Setter
    private Integer inRank;                    //国内排名

    @Getter
    @Setter
    private String inRankOrg;                  //国内排名机构

    @Getter
    @Setter
    private String inRankFile;                 //国内排名证明材料

    @Getter
    @Setter
    private String inRankFileName;             //国内排名证明材料名称

    @Getter
    @Setter
    private String inRankRemark;               //国内排名备注

    @Getter
    @Setter
    private Integer outRank;                   //国际排名

    @Getter
    @Setter
    private String outRankOrg;                 //国际排名机构

    @Getter
    @Setter
    private String outRankFile;                //国际排名证明材料

    @Getter
    @Setter
    private String outRankFileName;            //国际排名证明材料名称

    @Getter
    @Setter
    private String outRankRemark;              //国际排名备注

    @Getter
    @Setter
    private Date createdAt;                    //创建日期

    @Getter
    @Setter
    private Date updatedAt;                    //修改日期

}