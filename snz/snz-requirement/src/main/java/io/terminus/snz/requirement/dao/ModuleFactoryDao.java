package io.terminus.snz.requirement.dao;

import io.terminus.snz.requirement.model.ModuleFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Desc:模块工程资源配额
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-08-05.
 */
@Repository
public class ModuleFactoryDao extends SqlSessionDaoSupport {

    /**
     * 创建模块的工厂资源配额
     * @param moduleFactory 工厂资源配额
     * @return  Long
     * 返回配额编号
     */
    public Long create(ModuleFactory moduleFactory){
        getSqlSession().insert("ModuleFactory.create" , moduleFactory);
        return moduleFactory.getModuleId();
    }

    /**
     * 批量创建模块的工厂资源两配额
     * @param moduleFactoryList 模块工厂的资源量配额信息
     * @return Boolean
     * 返回配额是否成功
     */
    public Boolean createBatch(List<ModuleFactory> moduleFactoryList){
        return getSqlSession().insert("ModuleFactory.createBatch" , moduleFactoryList) > 0;
    }

    /**
     * 更改模块的工厂资源配额信息
     * @param moduleFactory 模块工厂配额信息
     * @return  Boolean
     * 返回更新是否成功
     */
    public Boolean update(ModuleFactory moduleFactory){
        return getSqlSession().update("ModuleFactory.update" , moduleFactory) > 0;
    }

    /**
     * 删除模块的工厂资源配额
     * @param id    工厂资源配额Id
     * @return Boolean
     * 返回删除是否成功
     */
    public Boolean delete(Long id){
        return getSqlSession().delete("ModuleFactory.delete" , id) > 0;
    }

    /**
     * 根据模块编号删除模块的工厂数据信息
     * @param moduleId 模块编号
     * @return  Integer
     * 返回删除的条数
     */
    public Integer deleteByModuleId(Long moduleId){
        return getSqlSession().delete("ModuleFactory.deleteByModuleId" , moduleId);
    }

    /**
     * 通过编号查询工厂资源配额信息
     * @param id    编号
     * @return  ModuleFactory
     * 返回模块工厂资源配额信息
     */
    public ModuleFactory findById(Long id){
        return getSqlSession().selectOne("ModuleFactory.findById" , id);
    }

    /**
     * 通过编号列表获取模块的工厂资源配额信息
     * @param ids   编号列表
     * @return  List<ModuleFactory>
     * 返回模块的工厂资源配额信息
     */
    public List<ModuleFactory> findByIds(List<Long> ids){
        return getSqlSession().selectList("ModuleFactory.findByIds" , ids);
    }

    /**
     * 通过模块编号获取模块的工厂资源配额信息
     * @param moduleId  模块编号
     * @return  List<ModuleFactory>
     * 返回模块的工厂资源配额信息
     */
    public List<ModuleFactory> findByModuleId(Long moduleId){
        return getSqlSession().selectList("ModuleFactory.findByModuleId" , moduleId);
    }
}
