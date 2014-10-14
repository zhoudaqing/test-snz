package io.terminus.snz.user.dto;

import io.terminus.snz.user.model.AdditionalDoc;
import io.terminus.snz.user.model.Finance;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Author:Guo Chaopeng
 * Created on 14-6-4.
 */
public class FinanceDto implements Serializable {

    private static final long serialVersionUID = 5452773456075310848L;

    @Setter
    @Getter
    private Finance finance;                        //财务信息

    @Setter
    @Getter
    private List<AdditionalDoc> financialDocs;      //财务证明

}
