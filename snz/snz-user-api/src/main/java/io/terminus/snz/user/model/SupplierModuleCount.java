package io.terminus.snz.user.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Author:Guo Chaopeng
 * Created on 14-8-28.
 */
public class SupplierModuleCount implements Serializable {

    private static final long serialVersionUID = -6752080635348065505L;

    @Setter
    @Getter
    private Long id;

    @Setter
    @Getter
    private Long moduleId;          //模块编号

    @Setter
    @Getter
    private String moduleName;      //模块名称

    @Setter
    @Getter
    private int supplierCount;      //模块商数量

    @Setter
    @Getter
    private int bestCount;           //优选供应商数量

    @Setter
    @Getter
    private int standardCount;       //合格供应商数量

    @Setter
    @Getter
    private int limitedCount;        //受限制供应商数量

    @Setter
    @Getter
    private int badCount;             //淘汰供应商数量

    public void incrCountByScore(Integer score) {
        if (score == null || score < 0)
            return;
        if (score >= 90 && score <= 100)
            bestCount++;
        else if (score >= 80 && score <= 89)
            standardCount++;
        else if (score >= 60 && score <= 79)
            limitedCount++;
        else if (score < 60)
            badCount++;
    }


    @Getter
    @Setter
    private Date createdAt;                      //创建时间

    @Getter
    @Setter
    private Date updatedAt;                      //修改时间

}
