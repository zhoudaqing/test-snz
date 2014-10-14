package io.terminus.snz.eai.service;


import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.Response;
import io.terminus.snz.eai.dto.OutmoduleDto;


/**∂
 * Date: 8/29/14
 * Time: 11:38
 * Author: 2014年 luyzh
 */
public interface QuotaInfoService {
    /**
     * 从GVS同步衍生号信息并写入数据库
     * werks	工厂
     * matnr	物料号
     * @return 操作是否成功
     */
    @Export(paramNames = {"matnr", "werks"})
    public Response<Boolean> applyQuotaInfo(String matnr, String werks);

    /**
     * 查询出中间表的老品信息
     * @param modulenum 老品号
     * @return 结果
     */
    @Export(paramNames = {"modulenum"})
    public OutmoduleDto findListfrom(String modulenum);
}
