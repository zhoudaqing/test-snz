package io.terminus.snz.requirement.dao;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.terminus.common.model.Paging;
import io.terminus.snz.requirement.manager.CountManager;
import io.terminus.snz.requirement.model.Requirement;
import io.terminus.snz.requirement.model.RequirementStatus;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Desc:需求信息处理
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-01.
 */
@Repository
public class RequirementDao extends SqlSessionDaoSupport {

    @Autowired
    private CountManager countManager;

   /**
    * 创建需求信息
    * @param requirement  需求信息
    * @return Long
    * 返回创建的需求信息编号
    */
    public Long create(Requirement requirement){
        getSqlSession().insert("Requirement.create" , requirement);
        return requirement.getId();
    }

    /**
     * 更新需求信息
     * @param requirement  需求信息
     * @return Boolean
     * 返回更新是否成功
     */
    public Boolean update(Requirement requirement){
        return getSqlSession().update("Requirement.update" , requirement) == 1;
    }

    /**
     * 获取全部需求的统计数据
     * @return Long
     * 返回全部需求的统计数据
     */
    public Long findRequirementCount(){
        return getSqlSession().selectOne("Requirement.findRequirementCount");
    }

    /**
     * 通过需求信息编号查询信息
     * @param requirementId   需求信息编号
     * @return  Requirement
     * 返回需求信息
     */
    public Requirement findById(Long requirementId){
        Requirement requirement = getSqlSession().selectOne("Requirement.findById", requirementId);
        Integer moduleTotal = countManager.getModuleTotal(requirementId);
        if(moduleTotal != -1){
            requirement.setModuleTotal(moduleTotal);
        }

        Integer moduleNum = countManager.getModuleNum(requirementId);
        if(moduleNum != -1){
            requirement.setModuleNum(moduleNum);
        }

        return requirement;
    }

    /**
     * 通过采购商编号&采购商需求名称查询需求信息
     * @param purchaserId   采购商编号
     * @param name          采购商需求名称
     * @return Requirement
     * 返回详细需求信息
     */
    public Requirement findByName(Long purchaserId , String name){
        return getSqlSession().selectOne("Requirement.findByName" , ImmutableMap.of("purchaserId", purchaserId, "name", name));
    }

    /**
     * 通过采购商编号&一些参数查询需求信息
     * @param purchaserId   采购商编号(null－》表示没有限制用户)
     * @param params        查询操作处理
     * @return  List
     * 返回查询得到的详细信息列表
     */
    public Paging<Requirement> findByParams(Long purchaserId , Map<String , Object> params){
        params.put("purchaserId" , purchaserId);
        Paging<Requirement> paging;

        Long count = getSqlSession().selectOne("Requirement.findRequirementCount" , params);
        if(count == 0){
            paging = new Paging<Requirement>(0l , new ArrayList<Requirement>());
        }else{
            List<Requirement> requirements = getSqlSession().selectList("Requirement.findByParams" , params);
            //写入实时的redis的统计数据
            setModuleCount(requirements);
            paging = new Paging<Requirement>(count , requirements);
        }

        return paging;
    }

    /**
     * 通过采购商编号&一些参数查询需求信息(参与的团队的需求信息)
     * @param purchaserId   采购商编号(null－》表示没有限制用户)
     * @param params        查询操作处理
     * @return  List
     * 返回查询得到的详细信息列表
     */
    public Paging<Requirement> findReqByTeam(Long purchaserId , Map<String , Object> params){
        params.put("purchaserId" , purchaserId);
        Paging<Requirement> paging;

        Long count = getSqlSession().selectOne("Requirement.findReqCountByTeam" , params);
        if(count == 0){
            paging = new Paging<Requirement>(0l , new ArrayList<Requirement>());
        }else{
            List<Requirement> requirements = getSqlSession().selectList("Requirement.findReqByTeam" , params);
            //写入实时的redis的统计数据
            setModuleCount(requirements);
            paging = new Paging<Requirement>(count , requirements);
        }

        return paging;
    }

    /**
     * 通过供应商编号&一些参数查询需求信息
     * @param supplierId    供应商编号
     * @param params        查询操作处理
     * @return  List
     * 返回查询得到的详细信息列表
     */
    public Paging<Requirement> findBySupplier(Long supplierId , Map<String , Object> params){
        params.put("supplierId" , supplierId);
        Paging<Requirement> paging;

        Long count = getSqlSession().selectOne("Requirement.findCountBySupplier" , params);
        if(count == 0){
            paging = new Paging<Requirement>(0l , new ArrayList<Requirement>());
        }else{
            List<Requirement> requirements = getSqlSession().selectList("Requirement.findBySupplier" , params);
            setModuleCount(requirements);
            paging = new Paging<Requirement>(count , requirements);
        }

        return paging;
    }

    /**
     * 查询到全部的需求信息
     * @param toStatus 需求的阶段小于或等于某个阶段(null:显示全部需求还未锁定的需求)
     * @param checkResult   需求的审核结果(null:全部的审核结果)
     * @return List
     * 返回查询得到的详细信息列表
     */
    public List<Requirement> findAllRequirements(Integer toStatus , Integer checkResult){
        Map<String , Object> params = Maps.newHashMap();
        params.put("toStatus" , toStatus);
        params.put("checkResult" , checkResult);
        return getSqlSession().selectList("Requirement.findAllRequirements" , params);
    }

    /**
     * 查询全部卓越运营场景的&阶段为承诺底线的所有需求信息
     * @return  List
     * 返回查询的到的详细信息列表
     */
    public List<Requirement> findAllExcellence(){
        return getSqlSession().selectList("Requirement.findAllExcellence");
    }

    /**
     * 根据需求状态查询某个状态下的全部需求
     * @param status    需求状态
     * @return  List
     * 返回所有的需求
     */
    public List<Requirement> findReqByStatus(Integer status){
        return getSqlSession().selectList("Requirement.findReqByStatus" , status);
    }

    /**
     * 获取最后创建的需求编号
     * @return  Long
     * 返回最后的需求编号
     */
    public Long maxId() {
        return getSqlSession().selectOne("Requirement.maxId");
    }

    /**
     * 定时的dump需求的数据信息（只dump审核通过的需求）
     * @param lastId    偏移量
     * @param limit     获取数据条数
     * @return  List
     * 返回需求信息
     */
    public List<Requirement> forDump(Long lastId, Integer limit) {
        List<Requirement> requirements = getSqlSession().selectList("Requirement.forDump", ImmutableMap.of("lastId", lastId, "limit", limit));
        setModuleCount(requirements);
        return requirements;
    }

    /**
     * 定时的dump需求的数据信息（只dump审核通过的需求）
     * @param lastId    偏移量
     * @param compared  比较数据
     * @param limit     获取数据条数
     * @return  List
     * 返回需求信息
     */
    public List<Requirement> forDeltaDump(Long lastId, String compared, Integer limit) {
        List<Requirement> requirements = getSqlSession().selectList("Requirement.forDeltaDump", ImmutableMap.of("lastId", lastId,
                "compared", compared, "limit", limit));
        setModuleCount(requirements);
        return requirements;
    }

    /**
     * 将模块统计数据写入需求中
     * @param requirementList   需求列表
     */
    private void setModuleCount(List<Requirement> requirementList){
        //写入实时的redis的统计数据
        Integer moduleTotal, moduleNum;
        for(Requirement requirement : requirementList){
            //需求小于需求审核阶段从redis中获取数据
            if(requirement.getStatus() == null || requirement.getStatus() <= RequirementStatus.RES_LOCK.value()){
                moduleTotal = countManager.getModuleTotal(requirement.getId());
                if(moduleTotal != -1){
                    requirement.setModuleTotal(moduleTotal);
                }

                moduleNum = countManager.getModuleNum(requirement.getId());
                if(moduleNum != -1){
                    requirement.setModuleNum(moduleNum);
                }
            }
        }
    }
}
