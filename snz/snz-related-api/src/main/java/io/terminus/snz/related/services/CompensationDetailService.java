package io.terminus.snz.related.services;

import io.terminus.pampas.common.Response;
import io.terminus.snz.related.models.CompensationDetail;

import java.util.List;

/**
 * 索赔明细交互service
 * Author:  wenhaoli
 * Date: 2014-08-11
 */
public interface CompensationDetailService {

    /**
     * 批量添加索赔明细
     * @param compensationDetails 索赔明细
     * @return 添加成功返回true
     * */
     Response<Boolean> batchAdd(List<CompensationDetail> compensationDetails);

  }
