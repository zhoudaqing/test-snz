package io.terminus.snz.related.daos;

import io.terminus.snz.related.models.Compensation;
import io.terminus.snz.related.models.CompensationDetail;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author:  wenhaoli
 * Date: 2014-08-11
 */
@Repository
public class CompensationDetailDao extends SqlSessionDaoSupport {

    /**
     *单个创建明细表
     * @param Detail 明细表对象
     * @return id 创建成功返回id
     * */
    public Long create(CompensationDetail Detail){
        getSqlSession().insert("CompensationDetail.createone", Detail);
        return Detail.getId();
    }

    /**
     * 根据明细id查找明细表
     * @param id
     * @return 明细页
     * */
    public CompensationDetail find(Long id) {
        return getSqlSession().selectOne("CompensationDetail.find", id);
    }

    /**
     * 通过列表记录信息返回明细页
     * @param listId 列表记录id
     * @return 明细页面
     * */
    public List<CompensationDetail> findDetail(Long listId) {

        return getSqlSession().selectList("CompensationDetail.findById", listId);
    }

    /**
     * 批量创建索赔明细
     * @param compensationDetails
     * @return Boolean
     * */
    public Integer creates(List<CompensationDetail> compensationDetails){

        getSqlSession().insert("CompensationDetail.creates", compensationDetails);
        return compensationDetails.size();
    }

    /**
     * 根据列表记录id更新状态信息
     * @param param 明细表
     * @return Boolean
     * */
    public Boolean update(CompensationDetail param){
        return getSqlSession().update("CompensationDetail.update", param) == 1;
    }

    /**
     * 由索赔记录查询其明细列表，咱不分页
     * @param compensation 索赔记录
     * @return 索赔明细列表
     */
    public List<CompensationDetail> findByCompensation(Compensation compensation) {
        return getSqlSession().selectList("CompensationDetail.findByCompensation", compensation);
    }
}
