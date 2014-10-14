package io.terminus.snz.user.dto;

import io.terminus.common.model.Indexable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by yangzefeng on 14-6-24
 */
@ToString @EqualsAndHashCode
public class RichSupplier implements Serializable,Indexable {
    private static final long serialVersionUID = -211696487768675091L;

    @Getter
    @Setter
    private Long id; //供应商id

    @Getter
    @Setter
    private String companyName; //供应商公司名称

    @Getter
    @Setter
    private List<Long> mainBusinessIds; //主营业务id，即前台3级类目id, 对应多个

    @Getter
    @Setter
    private List<Long> secondIds;   //前台2级类目id，对应多个

    @Getter
    @Setter
    private List<Long> firstIds;   //前台一级类目id,对应多个

    @Getter
    @Setter
    private String corpAddr; //公司地址

    @Getter
    @Setter
    private Long regCapital; //注册资金

    @Getter
    @Setter
    private Integer rcCoinType; //注册资金币种

    @Getter
    @Setter
    private Integer participateCount; //交互参与数

    @Getter
    @Setter
    private String mobile;     //联系电话

    @Getter
    @Setter
    private Iterable<String> tags;        //供应商标签

    @Getter
    @Setter
    private Integer type;     //type 0->游客, 1->管理员, 2->供应商, 3->采购商

    @Getter
    @Setter
    private Integer approveStatus; // -1->审核不通过, 0->未触发审核, 1->等待审核, 2->审核通过

    @Getter
    @Setter
    private Integer status;

    @Getter
    @Setter
    private Date createdAt;    //入驻时间
}
