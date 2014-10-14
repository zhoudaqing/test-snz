package io.terminus.snz.requirement.manager;

import io.terminus.snz.requirement.dao.ModuleSolutionDao;
import io.terminus.snz.requirement.dao.RequirementSolutionDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Desc:方案处理模块
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-06.
 */
@Slf4j
@Component
public class SolutionManager {
    @Autowired
    private RequirementSolutionDao requirementSolutionDao;

    @Autowired
    private ModuleSolutionDao moduleSolutionDao;

    /**
     * 通过整体需求方案编号删除供应商的方案信息
     * @param solutionId    方案编号
     * （使用事务管理）
     */
    @Transactional
    public void deleteSolution(Long solutionId){
        //删除整体方案信息
        requirementSolutionDao.delete(solutionId);
        log.error("delete requirement solution success.");

        //删除具体模块方案信息
        moduleSolutionDao.deleteBySolutionId(solutionId);
        log.error("delete module solutions success.");
    }
}
