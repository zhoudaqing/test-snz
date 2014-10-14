package io.terminus.snz.requirement.service;

import io.terminus.common.model.Paging;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.dto.RequirementBIZDto;

import java.util.Date;

/**
 * Desc:需求模块对外的提供的接口
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-09-03.
 */
public interface RequirementOutService {
    /**
     * 查询需求的详细数据到百卓系统
     * @param pageNo    当前的页码
     * @param size      分页数量
     * @param startAt 开始时间（查询时间段）
     * @param endAt   结束时间
     * @return  Paging
     * 返回需求的详细信息
     */
    public Response<Paging<RequirementBIZDto>> sendToBizInfo(Integer pageNo, Integer size, String startAt, String endAt);
}
