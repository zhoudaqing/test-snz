package io.terminus.snz.eai.service;

import io.terminus.pampas.common.Response;
import org.example.transstandardcredittosrm.ZINTZDSP12LOG20SENDING;

import javax.xml.ws.Holder;
import java.util.List;

/**
 * 通过sap获取索赔明细信息
 * Author:  wenhaoli
 * Date: 2014-08-12
 */
public interface ClaimInfoDetailService {

    /**
     *获取索赔明细列表，按当前日期查询
     *@param ZDATE 当前日期
     *@return 获取信息明细列表
     * */
    Response<Holder<List<ZINTZDSP12LOG20SENDING>>> getClaimInfoDetails(String ZDATE);

}
