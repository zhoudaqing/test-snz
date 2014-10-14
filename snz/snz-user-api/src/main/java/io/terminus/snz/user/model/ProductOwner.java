package io.terminus.snz.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 产品线负责人及相关工厂信息<P>
 *
 * Created by wanggen 2014-08-13 16:05:00
 * <PRE>
 * id                       Long        自增主键
 * factoryNum               Integer     工厂ID
 * factoryName              String      工程名称
 * productLineId            Integer     产品线ID
 * productLineName          String      产品线名称
 * ownerName                String      产品负责人
 * ownerId                  Long        产品负责人ID
 * createdAt                Date        创建时间
 * updatedAt                Date        更新时间
 * </PRE>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductOwner implements Serializable {
    private static final long serialVersionUID = 955359839129048140L;

    private Long id;                               //自增主键

    private String factoryNum;                     //工厂ID

    private String factoryName;                    //工程名称

    private Integer productLineId;                 //产品线ID

    private String productLineName;                //产品线名称

    private String ownerName;                      //产品负责人

    private Long ownerId;                          //产品负责人ID

    private Date createdAt;                        //创建时间

    private Date updatedAt;                        //更新时间


}
