package io.terminus.snz.requirement.manager;

import io.terminus.snz.requirement.dao.NewProductStepDao;
import io.terminus.snz.requirement.model.NewProductStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 提供对新品导入步骤事务管理方法
 *  1. 提交步骤
 *     1.1 检测该步骤是否存在
 *          若存在则更新已有数据
 *          若不存在则插入新数据
 *  2. 批量迭代提交步骤信息
 * @author wanggen on 14-8-8.
 */
@Component
public class NewProductStepManager {

    @Autowired
    private NewProductStepDao newProductStepDao;

    @Transactional
    public void post(List<NewProductStep> steps){
        for(NewProductStep step: steps) {
            doInsertOrUpdate(step);
        }
    }

    private void doInsertOrUpdate(NewProductStep step) {
        NewProductStep sameStep = newProductStepDao.findByMouldNumberAndSupplierNameAndStep(step.getModuleNum(), step.getSupplierCode(), step.getStep());
        if(sameStep==null){
            checkNotNull(step.getModuleNum(), "Module num of step can't be null");
            checkNotNull(step.getSupplierCode(), "Supplier code of step can't be null");
            newProductStepDao.create(step);
        }else{
            newProductStepDao.update(step);
        }
    }

}
