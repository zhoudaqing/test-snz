package io.terminus.snz.user.dto;

import io.terminus.snz.user.model.PurchaserExtra;
import io.terminus.snz.user.model.User;
import lombok.*;

import java.io.Serializable;

/**
 * Author:Guo Chaopeng
 * Created on 14-6-6.
 */
@NoArgsConstructor @AllArgsConstructor @ToString
public class PurchaserDto implements Serializable {

    private static final long serialVersionUID = 2473370441945981285L;
    @Setter
    @Getter
    private User user;

    @Setter
    @Getter
    private PurchaserExtra purchaserExtra;

}
