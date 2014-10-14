package io.terminus.snz.requirement.dao;

import io.terminus.common.model.Paging;
import io.terminus.snz.requirement.model.DetailQuota;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Desc:详细的配额信息进sap的数据
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-25.
 */
@Repository
public class DetailQuotaDao extends SqlSessionDaoSupport {
    /**
     * 根据详细的配额信息创建sap数据
     * @param detailQuota   详细的配额细腻
     * @return  Long
     * 返回创建的配额信息编号
     */
    public Long create(DetailQuota detailQuota){
        getSqlSession().insert("DetailQuota.create" , detailQuota);
        return detailQuota.getId();
    }

    /**
     * 实现批量创建配额信息
     * @param detailQuotaList   配额信息列表
     * @return  Integer
     * 返回创建的配额条数
     */
    public Integer createBatch(List<DetailQuota> detailQuotaList){
        return getSqlSession().insert("DetailQuota.createBatch" , detailQuotaList);
    }

    /**
     * 更新详细的配额信息
     * @param detailQuota   配额信息
     * @return  Boolean
     * 返回更新的配额信息
     */
    public Boolean update(DetailQuota detailQuota){
        return getSqlSession().update("DetailQuota.update" , detailQuota) > 0;
    }

    /**
     * 通过需求编号&一些参数查询配额信息信息
     * @param requirementId   需求编号
     * @param params          查询操作处理
     * @return  List
     * 返回查询得到的详细配额信息列表
     */
    public Paging<DetailQuota> findByParams(Long requirementId , Map<String , Object> params){
        params.put("requirementId" , requirementId);
        Paging<DetailQuota> paging;

        Long count = getSqlSession().selectOne("DetailQuota.findAllCount" , params);
        if(count == 0){
            paging = new Paging<DetailQuota>(0l , new ArrayList<DetailQuota>());
        }else{
            List<DetailQuota> detailQuotas = getSqlSession().selectList("DetailQuota.findByParams" , params);

            paging = new Paging<DetailQuota>(count , detailQuotas);
        }

        return paging;
    }
}
