package io.terminus.snz.user.service;

import io.terminus.common.model.Paging;
import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.category.model.FrontendCategory;
import io.terminus.snz.user.dto.SupplierCountByDimension;
import io.terminus.snz.user.dto.SupplierReportExportDto;
import io.terminus.snz.user.dto.SupplierReportQueryCriteria;

import java.util.List;

/**
 * Author:Guo Chaopeng
 * Created on 14-9-3.
 */
public interface SupplierReportService {

    /**
     * 从不同纬度统计供应商数据
     *
     * @return是否成功
     */
    public Response<Boolean> countSupplierByDimensions();

    /**
     * 获取供应商不同纬度的统计数据
     *
     * @return 统计结果
     */
    @Export
    public Response<SupplierCountByDimension> getSupplierCountByDimensions();

    /**
     * 报表查询
     *
     * @param baseUser 当前登录用户
     * @param criteria 查询条件
     * @param pageNo   页面
     * @param size     大小
     * @return 报表信息
     */
    @Export(paramNames = {"baseUser", "criteria", "pageNo", "size"})
    public Response<Paging<SupplierReportExportDto>> queryReport(BaseUser baseUser, SupplierReportQueryCriteria criteria, Integer pageNo, Integer size);

    /**
     * 查询详细的报表信息
     *
     * @param baseUser 当前登录用户
     * @param criteria 查询条件
     * @return 详细的报表信息
     */
    public Response<List<SupplierReportExportDto>> findDetailReport(BaseUser baseUser, SupplierReportQueryCriteria criteria);

    /**
     * 查询当前登录用户负责的主营业务
     *
     * @param baseUser 当前登录用户
     * @return 主营业务信息
     */
    @Export(paramNames = {"baseUser"})
    public Response<List<FrontendCategory>> findOwnMainBusinesses(BaseUser baseUser);

}
