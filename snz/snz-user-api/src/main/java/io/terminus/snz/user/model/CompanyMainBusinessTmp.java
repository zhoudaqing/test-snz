package io.terminus.snz.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Description：企业主营业务临时表
 * Author:Guo Chaopeng
 * Created on 14-8-26.
 */
@ToString
public class CompanyMainBusinessTmp implements Serializable {

    private static final long serialVersionUID = -3969788811710231648L;

    @Setter
    @Getter
    private Long id;                //自增主键

    @Setter
    @Getter
    private Long firstLevelId;     //一级类目编号

    @Setter
    @Getter
    private Long mainBusinessId;    //主营业务编号

    @Setter
    @Getter
    private String name;            //主营业务名称

    @Setter
    @Getter
    private Long companyId;         //企业编号

    @Setter
    @Getter
    private Long userId;            //用户编号

    @Setter
    @Getter
    private Date createdAt;         //创建时间

    @Setter
    @Getter
    private Date updatedAt;         //修改时间

}
