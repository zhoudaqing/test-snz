package io.terminus.snz.related.services;

import io.terminus.common.model.Paging;
import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.related.dto.ArchFileDto;
import io.terminus.snz.related.dto.DetailAndReply;
import io.terminus.snz.related.models.Compensation;
import io.terminus.snz.related.models.CompensationDetail;
import io.terminus.snz.related.models.CompensationReply;

import java.util.List;

/**
 * 索赔服务接口
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-7-10
 */
public interface CompensationService {

    /**
     * 分页查询索赔信息
     * @param pageNo 页号
     * @param pageSize 分页大小
     * @param criteria 筛选条件
     * @param deductedStartAt 扣款起始时间
     * @param deductedEndAt 扣款结束时间
     * @return 索赔信息分页对象
     */
    @Export(paramNames = {"user", "pageNo", "pageSize", "criteria", "deductedStartAt", "deductedEndAt"})
    Response<Paging<Compensation>> paging(BaseUser user,
                Integer pageNo, Integer pageSize, Compensation criteria,
                String deductedStartAt, String deductedEndAt);

    /**
     * 批量添加索赔信息
     * @param compensations
     * @return 添加成功返回true
     */
    Response<Boolean> batchAdd(List<Compensation> compensations);

    /**
     * 更新索赔信息状态
     * @param user 当前用户
     * @param cid 索赔信息PK
     * @param status 状态
     * @return 更新成功返回true
     */
    @Export(paramNames = {"user", "cid", "status"})
    Response<Boolean> updateStatus(BaseUser user, Long cid, Integer status);

    /**
     * 查看索赔明细
     *
     * @param user 当前用户
     * @param listId 索赔列表信息id
     * @return 索赔信息明细
     * */
    @Export(paramNames = {"user","listId","pageNo", "pageSize"})
    Response<DetailAndReply> findDetail(BaseUser user, Long listId, Integer pageNo, Integer pageSize);

    /**
     * 查看索赔明细信息
     * @param user 当前用户
     * @param listId 索赔列表信息id
     * @return 索赔明细列表
     * */
    @Export(paramNames = {"user","listId"})
    Response<List<CompensationDetail>> findDetailList(BaseUser user, Long listId);

    /**
     * 查找会话paging
     * @param user 当前用户
     * @param listId 索赔列表信息id
     * @param pageNo 起始偏移
     * @param pageSize 分页大小
     * @return 索赔会话paging
     * */
    @Export(paramNames = {"user","listId","pageNo","pageSize"})
    Response<Paging<CompensationReply>> findReplyPage(BaseUser user, Long listId, Integer pageNo, Integer pageSize);

    /**
     * 获取档案信息
     * @param user 当前用户
     * @param listId 索赔列表信息id
     * */
    @Export(paramNames = {"user","listId"})
    Response<ArchFileDto> findFileInfo(BaseUser user, Long listId);
}
