package io.terminus.snz.requirement.dao;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.terminus.common.model.Paging;
import io.terminus.common.utils.JsonMapper;
import io.terminus.snz.requirement.dto.SolutionFileDto;
import io.terminus.snz.requirement.model.RequirementSolution;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Desc:供应商对于采购商的需求提供的整体的方案处理
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-03.
 */
@Repository
public class RequirementSolutionDao extends SqlSessionDaoSupport {

    public static final com.fasterxml.jackson.databind.JavaType JSON_TYPE =
            JsonMapper.JSON_NON_DEFAULT_MAPPER.createCollectionType(List.class, SolutionFileDto.class);
    private static final DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 创建需求的整体的方案
     * @param requirementSolution  整体的方案
     * @return Long
     * 返回创建的整体的方案信息编号
     */
    public Long create(RequirementSolution requirementSolution){
        getSqlSession().insert("RequirementSolution.create" , requirementSolution);
        return requirementSolution.getId();
    }

    /**
     * 更新需求整体的方案信息
     * @param requirementSolution  整体的方案
     * @return Boolean
     * 返回更新是否成功
     */
    public Boolean update(RequirementSolution requirementSolution){
        return getSqlSession().update("RequirementSolution.update" , requirementSolution) == 1;
    }


    /**
     * 更新需求整体的方案附件信息
     * @param solutionId 方案编号
     * @param newFile 新附件, SolutionFileDto 的json表示形式
     * @return Boolean
     * 返回更新是否成功
     */
    public Boolean updateSolutionFile(Long solutionId, String newFile){
        RequirementSolution requirementSolution = findById(solutionId);

        SolutionFileDto sfd = JsonMapper.JSON_NON_DEFAULT_MAPPER.fromJson(newFile, SolutionFileDto.class);
        sfd.setVersion(dtf.print(DateTime.now()));
        List<SolutionFileDto> solutionFiles;
        if(Strings.isNullOrEmpty(requirementSolution.getSolutionFile())){
            solutionFiles = ImmutableList.of(sfd);
        }else{
            solutionFiles =  JsonMapper.nonEmptyMapper().fromJson(
                    requirementSolution.getSolutionFile(),
                    JSON_TYPE
                    );
            solutionFiles.add(0, sfd);
        }

        //标记为已上传方案文档
        requirementSolution.setSolutionFile(JsonMapper.JSON_NON_EMPTY_MAPPER.toJson(solutionFiles));
        requirementSolution.setStatus(RequirementSolution.Status.SEND_FILE.value());
        return getSqlSession().update("RequirementSolution.update" , requirementSolution) == 1;
    }

    /**
     * 根据方案编号删除方案
     * @param solutionId 方案编号
     * @return  Boolean
     * 删除是否成功
     */
    public Boolean delete(Long solutionId){
        return getSqlSession().delete("RequirementSolution.delete" , solutionId) == 1;
    }

    /**
     * 通过整体的方案信息编号查询信息
     * @param requirementSolutionId   整体的方案信息编号
     * @return  RequirementSolution
     * 返回方案信息
     */
    public RequirementSolution findById(Long requirementSolutionId){
        return getSqlSession().selectOne("RequirementSolution.findById" , requirementSolutionId);
    }

    /**
     * 供应商通过编号以及需求编号查询方案信息
     * @param requirementId 需求编号
     * @param supplierId    供应商编号
     * @return  RequirementSolution
     * 返回整体方案信息
     */
    public RequirementSolution findByRequirementId(Long requirementId, Long supplierId){
        return getSqlSession().selectOne("RequirementSolution.findByRequirementId" ,
                ImmutableMap.of("requirementId" , requirementId , "supplierId", supplierId));
    }

    /**
     * 通过需求方案的创建者以及需求编号查询方案信息
     * @param requirementId 需求编号
     * @param userId        方案创建者
     * @return  RequirementSolution
     * 返回整体的方案信息
     */
    public RequirementSolution findByUserId(Long requirementId, Long userId){
        return getSqlSession().selectOne("RequirementSolution.findByUserId" ,
                ImmutableMap.of("requirementId" , requirementId, "userId", userId));
    }

    /**
     * 通过需求编号查询该需求下的所有方案信息
     * @param requirementId 需求编号
     * @return  List
     * 返回需求方案信息
     */
    public List<RequirementSolution> findAllSolution(Long requirementId){
        return getSqlSession().selectList("RequirementSolution.findAllSolution" , requirementId);
    }

    /**
     * 查询可以进入需求的方案终投阶段的全部供应商方案
     * @param requirementId 需求编号
     * @param supplierIds   供应商列表(null:显示全部)
     * @param sortType 方案的排序方式(1:技术,2:质量,3:互动,4:产能,5:成本。)
     * @param status    需求方案状态（null：显示全部）
     * @return  List
     * 返回需求方案信息列表
     */
    public List<RequirementSolution> findSolutionEnds(Long requirementId , List<Long> supplierIds, Integer sortType, Integer status){
        Map<String , Object> params = Maps.newHashMap();
        params.put("requirementId" , requirementId);
        params.put("supplierIds" , supplierIds);
        params.put("sortType" , sortType);
        params.put("status" , status);
        return getSqlSession().selectList("RequirementSolution.findSolutionEnds" , params);
    }

    /**
     * 通过需求编号&一些参数查询需求信息
     * @param requirementId   需求编号（null:查询全部的方案信息）
     * @param params        查询操作处理
     * @return  List
     * 返回查询得到的详细信息列表
     */
    public Paging<RequirementSolution> findByParams(Long requirementId , Map<String , Object> params){
        params.put("requirementId" , requirementId);
        Paging<RequirementSolution> paging;

        Long count = getSqlSession().selectOne("RequirementSolution.findSolutionCount" , params);
        if(count == 0){
            paging = new Paging<RequirementSolution>(0l , new ArrayList<RequirementSolution>());
        }else{
            List<RequirementSolution> solutionList = getSqlSession().selectList("RequirementSolution.findByParams" , params);
            paging = new Paging<RequirementSolution>(count , solutionList);
        }

        return paging;
    }

    /**
     * 通过参数查询需求信息
     * @param params    参数
     * @return Paging List
     */
    public Paging<RequirementSolution> findSolutionsByParams(Map<String, Object> params) {
        Paging<RequirementSolution> paging;
        Long count = getSqlSession().selectOne("RequirementSolution.findSolutionCount" , params);
        if(count == 0){
            paging = new Paging<RequirementSolution>(0l , new ArrayList<RequirementSolution>());
        }else{
            List<RequirementSolution> solutionList = getSqlSession().selectList("RequirementSolution.findByParams" , params);
            paging = new Paging<RequirementSolution>(count , solutionList);
        }

        return paging;
    }

    /**
     * 通过资质验证状态和供应商id查询
     * @param supplierId       供应商id
     * @param qualifyStatus    资质验证状态
     * @return List
     */
    public List<RequirementSolution> findSolutionsByQualifyAndSupplier(Long supplierId, Integer qualifyStatus) {
        return getSqlSession().selectList("RequirementSolution.findByParamsWithNoLimit", ImmutableMap.of(
                "supplierId", supplierId, "qualifyStatus", qualifyStatus
        ));
    }
}
