package io.terminus.snz.requirement.service;

import io.terminus.common.model.Paging;
import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.model.ImportGoodList;

import javax.annotation.Nullable;

/**
 * Date: 7/11/14
 * Time: 13:03
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */
public interface ImportGoodListService {


    @Export(paramNames = {"id", "p", "s"})
    public Response<Paging<ImportGoodList>> pagingByPurchaserId(Long id, Integer pageNo, Integer size);

    @Export(paramNames = {"id", "sid", "mn", "p", "s"})
    public Response<Paging<ImportGoodList>> pagingBy(@Nullable Long purchaserId,
                                                    @Nullable Long seriesId, @Nullable String moduleNo,
                                                    @Nullable Integer pageNo, @Nullable Integer size);
}
