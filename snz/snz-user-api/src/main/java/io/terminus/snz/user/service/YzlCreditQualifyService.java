package io.terminus.snz.user.service;

import io.terminus.common.model.Paging;
import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.model.YzlCreditQualify;

import java.util.List;
import java.util.Map;

/**
 * Created by dream on 14-10-11.
 */
public interface YzlCreditQualifyService {

    /**
     * 根据状态查询
     * @param status
     * @return  返回信用列表
     */
    @Export(paramNames = "status")
    public Response<List<YzlCreditQualify>> findByStatus(Integer status);

    /**
     * 根据id删除一条记录
     * @param id
     * @return
     */
    @Export(paramNames = "id")
    public Response<Boolean> deleteById(Integer id);

    /**
     * 单条插入
     * @param yzlCreditQualify
     * @return
     */
    @Export(paramNames = "yzlCreditQualify")
    public Response<Integer> create(YzlCreditQualify yzlCreditQualify);

    /**
     * 更新
     * @param yzlCreditQualify
     * @return
     */
    @Export(paramNames = "yzlCreditQualify")
    public Response<Integer> update(YzlCreditQualify yzlCreditQualify);

    /**
     * 根据list里的id批量删除
     * @param ids
     * @return
     */
    @Export(paramNames = "ids")
    public Response<Integer> deleteByIds(List<Integer> ids);

    /**
     * 批量插入数据
     * @param yzlCreditQualifies
     * @return
     */
    @Export(paramNames = "yzlCreditQualifies")
    public Response<Integer> creates(List<YzlCreditQualify> yzlCreditQualifies);

    /**
     * 分页查询
     * @param status
     * @param message
     * @param pageNo
     * @param size
     * @return
     */
    @Export(paramNames = {"status","message", "pageNo", "size"})
    public Response<Paging<YzlCreditQualify>> findByPage(Integer status, String message, Integer pageNo, Integer size);
}
