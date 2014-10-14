package io.terminus.snz.requirement.dao;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.terminus.common.model.Paging;
import io.terminus.snz.requirement.model.OldModule;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jiaoyuan on 14-7-5.
 */
@Repository
public class OldModuleDao extends SqlSessionDaoSupport {
    /**
     * 创建老品具体信息
     * @param oldModule 老品具体信息
     * @return
     * 返回创建的老品具体信息的编号
     */
    public Long create(OldModule oldModule){
        getSqlSession().insert("OldModule.create" , oldModule);
        return oldModule.getId();
    }

    /**
     * 更新具体老品的信息
     * @param oldModule 老品具体信息
     * @return
     * 是否更新成功
     */
    public Boolean update(OldModule oldModule){
        return getSqlSession().update("OldModule.update" , oldModule) == 1;
    }

    /**
     * 通过具体的需求编号查询老品信息
     * @param requirementId 需求编号
     * @return
     * 返回该需求对应的老品信息
     */
    public OldModule findById(Long requirementId){
        return getSqlSession().selectOne("OldModule.findById", requirementId);
    }

    public Boolean delete(Long purchaserId){
       return getSqlSession().delete("OldModule.delete",purchaserId)==0;
    }
    /**
     * 通过模块专用号集合查询老品数据信息
     * @param moduleIds 老品id
     *
     * @return 老品集合
     * 返回老品集合
     */
    public List<OldModule> findByIds(List<Long> moduleIds) {
        return getSqlSession().selectList("OldModule.findByIds",moduleIds);
    }

    /**
     * 通过模块专用号查询老品信息
     * @param moduleNum
     * @return 老品信息
     */
    public  OldModule findByModuleNum(String moduleNum){
        return getSqlSession().selectOne("OldModule.findByModuleNum",moduleNum);
    }

    /**
     * 通过各个筛选条件查询老品信息
     * @param params
     * @return
     * 分页后的list
     */
    public Paging<OldModule> findByFilters(Map<String,Object> params){
        Paging<OldModule> paging;
        Long count = getSqlSession().selectOne("OldModule.findCountByFilters",params);
        if(count==0){
            paging = new Paging<OldModule>(0l,new ArrayList<OldModule>());
        }else {
            List<OldModule> oldModules = getSqlSession().selectList("OldModule.findByFilters", params);

            paging = new Paging<OldModule>(count, oldModules);
        }

        return  paging;
    }

    /**
     * 通过需求编号查询老品详细信息同时老品当中对应的衍生号id不能为空
     * @param requirementId 需求编号
     * @param params 分页数据
     * @return
     * 分页查询后的老品详细信息
     */
    public Paging<OldModule> findByRequirementId(Long requirementId,Map<String,Object> params){
        Map<String , Object> newParams = Maps.newHashMap();
        newParams.putAll(params);
        newParams.put("requirementId",requirementId);
        newParams.put("moduleIdNotNull","");

        Paging<OldModule> paging;
        Long count = getSqlSession().selectOne("OldModule.findCountByRequirementId",newParams);
        if(count == 0L){
            paging = new Paging<OldModule>(0L, Lists.<OldModule>newArrayList());
            return paging;
        }
        List<OldModule> oldModules = getSqlSession().selectList("OldModule.findByRequirementId",newParams);
        paging = new Paging<OldModule>(count,oldModules);
        return paging;
    }
}
