package io.terminus.snz.requirement.service;

import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.dto.SupplierReparationDetailDtoExport;
import io.terminus.snz.requirement.dto.SupplierReparationsDto;

import java.util.List;

/**
 * Date: 8/11/14
 * Time: 11:12
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */
public interface ReparationService {

    /**
     * 查找一个用户的不良记录
     *
     * @param user     当前登录用户
     * @param userId   供应商用户的ID
     * @param range    赔偿的日期范围
     * @param type     赔偿的类型
     * @param startAt  自定义开始时间
     * @param endAt    自定义结束时间
     * @return 一个供应商一段时间内的不良记录
     */
    @Export(paramNames = {"user", "userId", "range", "type", "startAt", "endAt", "pageNo", "size"})
    public Response<SupplierReparationsDto> findSupplierReparationBy(BaseUser user, Long userId, Integer range, Integer type,
                                                                     String startAt, String endAt, Integer pageNo, Integer size);

    /**
     * 查找一个用户的不良记录，用来excel导出
     *
     * @param user     当前登录用户
     * @param userId   供应商用户的ID
     * @param range    赔偿的日期范围
     * @param type     赔偿的类型
     * @param startAt  自定义开始时间
     * @param endAt    自定义结束时间
     * @return 一个供应商一段时间内的不良记录
     */
    @Export(paramNames = {"user", "userId", "range", "type", "startAt", "endAt"})
    public Response<List<SupplierReparationDetailDtoExport>> findSupplierDailyReparationsBy(BaseUser user, Long userId, Integer range, Integer type,
                                                                               String startAt, String endAt);


    /**
     * 被定时任务执行，从中间表获取现场不良数据
     *
     * @return 操作是否成功
     */
    @Export(paramNames = {"startAt", "endAt"})
    public Response<Boolean> deltaDumpScene(String startAt, String endAt);

    /**
     * 全量统计市场不良的数据
     *
     * @return 操作是否成功
     */
    @Export
    public Response<Boolean> fullDumpMarketSummary();

    /**
     * 被定时任务执行，从中间包获取市场不良数据
     *
     * @param startAt    开始时间
     * @param endAt      结束时间
     * @return 操作是否成功
     */
    @Export(paramNames = {"startAt", "endAt"})
    public Response<Boolean> deltaSyncMarketData(String startAt, String endAt);
}
