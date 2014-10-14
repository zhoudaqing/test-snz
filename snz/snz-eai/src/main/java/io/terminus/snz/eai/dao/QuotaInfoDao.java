package io.terminus.snz.eai.dao;


import com.google.common.collect.Maps;
import io.terminus.snz.eai.model.OutPriceInfo;
import io.terminus.snz.eai.model.OutQuotaInfo;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Desc: 衍生品接配额信息
 * author: luyzh
 * Date: 14-9-4
 */
@Repository
public class QuotaInfoDao extends SqlSessionDaoSupport {

     /**
     * 根据 WERKS,factory,MATNR查询
     * @param WERKS   供应商
     * @param factory 工厂
     * @param MATNR   物料号
     * @return 结果
     */
    public OutQuotaInfo findbymatnrall(String MATNR,String factory, String WERKS) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("MATNR", MATNR);
        param.put("WERKS", WERKS);
        param.put("FACTORY", factory);
        return getSqlSession().selectOne("OutQuotaInfo.findbymatnrall",param);
    }

    /**
     * 插入配额信息
     * @param newQuota   配额信息列表
     * @return 插入老品信息的id
     */
    public Long create(OutQuotaInfo newQuota) {
        getSqlSession().insert("OutQuotaInfo.create", newQuota);
        return newQuota.getId();
    }

    /**
     * 更新配额信息
     * @param ups   每个物料号工厂的配额信息
     * @return Boolean
     * 返回更新是否成功
     */
    public Boolean update(OutQuotaInfo ups){
        return  getSqlSession().update("OutQuotaInfo.update",ups)==1;
    }

    /**
     * 根据 WERKS,MATNR查询
     * @param WERKS   工厂
     * @param MATNR   物料号
     * @return 结果
     */
    public OutPriceInfo findbymatnr(String MATNR, String WERKS){
        Map<String, Object> param = Maps.newHashMap();
        param.put("MATNR", MATNR);
        param.put("WERKS", WERKS);
        return getSqlSession().selectOne("OutPriceInfo.findbymatnrORwerks",param);
    }

    /**
     * 插入价格信息
     * @param newQuota   价格信息列表
     * @return 插入老品信息的id
     */
    public Long createother(OutPriceInfo newQuota) {
        getSqlSession().insert("OutPriceInfo.createother", newQuota);
        return newQuota.getId();
    }

    /**
     * 更新价格信息
     * @param ups   每个物料号工厂的价格信息
     * @return Boolean
     * 返回更新是否成功
     */
    public Boolean updateother(OutPriceInfo ups){
        return  getSqlSession().update("OutPriceInfo.updateother",ups)==1;
    }

     /**
     * 根据id查找一个老品信息
     * @param id    指定的老品信息id
     * @return 一个老品信息
     */
    public OutQuotaInfo findById(Long id) {
        return getSqlSession().selectOne("OutQuotaInfo.findById", id);
    }
    public OutPriceInfo findByIdother(Long id) {
        return getSqlSession().selectOne("OutPriceInfo.findById", id);
    }

    /**
     * 根据 modulenum查找一个老品信息
     * @param modulenum    指定物料号
     * @return 配额价格信息
     */
    public List<OutQuotaInfo> findbymodulenum(String modulenum) {
        return getSqlSession().selectList("OutQuotaInfo.findBymodulenum", modulenum);
    }
    public List<OutPriceInfo> findbymodulenumother(String modulenum) {
        return getSqlSession().selectList("OutPriceInfo.findBymodulenum", modulenum);
    }

}
