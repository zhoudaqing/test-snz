package io.terminus.snz.requirement.manager;

import com.google.common.collect.Lists;
import io.terminus.snz.requirement.dao.DerivativeDiffDao;
import io.terminus.snz.requirement.dao.ModuleDao;
import io.terminus.snz.requirement.model.DerivativeDiff;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Desc: 衍生品差异处理模块
 * Mail: houly@terminus.io
 * author: Hou Luyao
 * Date: 14-7-25.
 */
@Slf4j
@Component
public class DerivativeDiffManager {
    @Autowired
    private DerivativeDiffDao derivativeDiffDao;

    @Autowired
    private ModuleDao moduleDao;

    @Transactional
    public Boolean batchDerivativeDiffs(List<DerivativeDiff> derivativeDiffs){
        log.info("batchDerivativeDiffs start");
        List<DerivativeDiff> diffListUpdate = Lists.newArrayList();
        List<DerivativeDiff> diffListAdd = Lists.newArrayList();

        for(DerivativeDiff derivativeDiff : derivativeDiffs){
            //查询本需求本模块的差异分析信息
            List<DerivativeDiff> checkList = derivativeDiffDao.checkDiffExists(derivativeDiff.getRequirementId(),derivativeDiff.getModuleId());
            if(checkList.isEmpty() || null == checkList){
                diffListAdd.add(derivativeDiff);
            }
            else{
                diffListUpdate.add(derivativeDiff);
            }

        }
        //如果新增List为空则不新增，不为空则执行新增
        if(!diffListAdd.isEmpty() && null != diffListAdd){
            log.info("batchDerivativeDiffs,add num {},update num {}",diffListAdd.size(),diffListUpdate.size());
            log.info("begin to add diffs");
            Boolean result = derivativeDiffDao.createBash(diffListAdd);
            log.info("add diffs result,{}",result);
        }
        //如果更新List为空则不更新，不为空则执行更新
        if(!diffListUpdate.isEmpty() && null != diffListUpdate){
            log.info("begin to update diffs");
            for (DerivativeDiff diff : diffListUpdate){
                derivativeDiffDao.update(diff);
            }
            log.info("update diffs successfull");
            log.info("batchDerivativeDiffs end");
        }

        return true;
    }

    /**
     * 执行衍生号删除操作
     * 先删除衍生号差异分析表相关数据，再删除该衍生号数据
     * 后续还要最先删除成本分析数据，此处预留
     */
    @Transactional
    public Boolean deleteDeriveModule(Long requirementId, Long moduleId){
        log.info("begin to delete DeriveDiff...");
        Boolean result = derivativeDiffDao.delete(requirementId, moduleId);
        if(!result){
            log.error("cannot delete derivativeDiff,caused by requirementId={},moduleId={}",requirementId,moduleId);
            return false;
        }
        log.info("begin to delete dervieModule...");
        result = moduleDao.deleteByParams(requirementId, moduleId);
        if(!result){
            log.error("cannot delete deriveModule,caused by requirementId={},moduleId={}",requirementId,moduleId);
            return false;
        }
        return result;
    }
}
