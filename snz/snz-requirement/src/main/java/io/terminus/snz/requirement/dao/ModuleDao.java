package io.terminus.snz.requirement.dao;

import com.google.common.collect.ImmutableMap;
import io.terminus.common.model.Paging;
import io.terminus.snz.requirement.model.Module;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Desc:需求具体模块信息处理
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-02.
 */
@Repository
public class ModuleDao extends SqlSessionDaoSupport {
    /**
     * 创建需求具体模块信息
     * @param module  需求具体模块信息
     * @return Long
     * 返回创建的需求具体模块信息编号
     */
    public Long create(Module module){
        getSqlSession().insert("Module.create" , module);
        return module.getId();
    }

    /**
     * 批量创建需求的具体模块信息
     * @param moduleList    具体模块信息列表
     * @return Boolean
     * 返回创建是否成功
     */
    public Boolean createBatch(List<Module> moduleList){
        return getSqlSession().insert("Module.createBatch" , moduleList) > 0;
    }

    /**
     * 更新需求具体模块信息
     * @param module  需求具体模块信息
     * @return Boolean
     * 返回更新是否成功
     */
    public Boolean update(Module module){
        return getSqlSession().update("Module.update", module) == 1;
    }

    /**
     * 通过需求具体模块信息编号查询信息
     * @param moduleId   需求具体模块信息编号
     * @return  Module
     * 返回需求信息
     */
    public Module findById(Long moduleId){
        return getSqlSession().selectOne("Module.findById" , moduleId);
    }

    /**
     * 通过需求编号查询需求下的具体模块的需求个数
     * @param requirementId 需求编号
     * @return Integer
     * 返回需求中的模块需求的个数
     */
    public Integer countById(Long requirementId){
        return getSqlSession().selectOne("Module.countById" , requirementId);
    }

    /**
     * 通过模块需求编号列表批量查询
     * @param moduleIds 模块需求编号
     * @return  List
     * 返回具体的模块需求信息
     */
    public List<Module> findByIds(List<Long> moduleIds){
        return getSqlSession().selectList("Module.findByIds" , moduleIds);
    }

    /**
     * 通过需求编号查询全部的需求下的模块信息
     * @param requirementId 需求编号
     * @return List
     * 返回需求下的全部模块信息
     */
    public List<Module> findModules(Long requirementId){
        return getSqlSession().selectList("Module.findModules" , requirementId);
    }

    /**
     * 获取全部未写入模块专用号的模块信息
     * @param requirementId 需求编号（null：显示全部）
     * @return  List
     * 返回模块信息列表
     */
    public List<Module> findNoModuleNum(Long requirementId){
        return getSqlSession().selectList("Module.findNoModuleNum" , requirementId);
    }

    /**
     * 通过参数查询对于需求的详细模块数据信息
     * @param requirementId 需求编号
     * @param params        分页参数（包括pageNo,size...）
     * @return Paging
     * 返回需求详细的模块数据列表
     */
    public Paging<Module> findByParams(Long requirementId , Map<String , Object> params){
        Paging<Module> paging;
        if(requirementId!=null) {
            params.put("requirementId", requirementId);
        }

        Long count = getSqlSession().selectOne("Module.findModuleCount" , params);
        if(count == 0){
            paging = new Paging<Module>(0l , new ArrayList<Module>());
        }else{
            List<Module> moduleList = getSqlSession().selectList("Module.findByParams" , params);
            paging = new Paging<Module>(count , moduleList);
        }

        return paging;
    }


    public Long countByPurchaser(Long purchaserId, Map<String, Object> params) {
        params.put("purchaserId", purchaserId);
        return getSqlSession().selectOne("Module.countByPurchaserId", params);
    }

    /**
     * 通过需求编号查询过滤后的模块信息
     * @param requirementId 需求编号
     * @param filterIds     需要过滤的模块编号
     * @param params        分页参数（包括pageNo,size...）
     * @return Paging
     * 返回需求详细的模块数据列表
     */
    public Paging<Module> findFilterModule(Long requirementId , List<Long> filterIds, Map<String , Object> params){
        Paging<Module> paging;
        params.put("requirementId" , requirementId);
        params.put("filterIds" , filterIds.isEmpty() ? null : filterIds);

        Long count = getSqlSession().selectOne("Module.findFilterCount" , params);
        if(count == 0){
            paging = new Paging<Module>(0l , new ArrayList<Module>());
        }else{
            List<Module> moduleList = getSqlSession().selectList("Module.findFilter" , params);
            paging = new Paging<Module>(count , moduleList);
        }

        return paging;
    }

    /**
     * 通过需求具体模块信息编号删除对象
     * @param moduleId   需求具体模块信息编号
     * @return  Boolean
     * 返回删除结果
     */
    public Boolean delete(Long moduleId){
        return getSqlSession().delete("Module.delete" , moduleId) == 1;
    }



    /**
     * 根据需求编号删除需求下的所有的模块信息
     * @param requirementId 需求编号
     * @return  Integer
     * 返回删除的条数
     */
    public Integer deleteByReqId(Long requirementId){
        return getSqlSession().delete("Module.deleteByReqId" , requirementId);
    }

    /**
     * 获取衍生号的信息
     * @param moduleNum     老品模块的编号
     * @param moduleType    老品模块的类型
     * @return  List
     * 返回全部衍生号信息
     */
    public List<Module> findDerive(String moduleNum, Integer moduleType){
        return getSqlSession().selectList("Module.findDerive" , ImmutableMap.of("moduleNum" , moduleNum, "moduleType", moduleType));
    }

    /**
     * 获取一个符合参数的
     * @param module    查找参数
     * @return 模块列表
     */
    public Module findOneBy(Module module) {
        return getSqlSession().selectOne("Module.findOne", module);
    }

    /**
     * 根据物料号查询老品基本信息
     * @param moduleNum 老品物料号
     * @return 老品基本信息
     */
    public Module findOldModuleByNum(String moduleNum) {
        return getSqlSession().selectOne("Module.findOldModuleById", moduleNum);
    }

    /**
     * 通过需求编号和需求具体模块信息编号删除对象
     * @param  requirementId 需求编号
     * @param  moduleId   需求具体模块信息编号
     * @return Boolean
     * 返回删除结果
     */
    public Boolean deleteByParams(Long requirementId, Long moduleId){
        return getSqlSession().delete("Module.deleteByParams" ,ImmutableMap.of("requirementId",requirementId, "moduleId",moduleId)) <= 1;
    }

}
