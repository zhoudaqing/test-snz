package io.terminus.snz.requirement.manager;

import io.terminus.snz.requirement.dao.NewProductImportDao;
import io.terminus.snz.requirement.dao.NewProductStepDao;
import io.terminus.snz.requirement.model.NewProductImport;
import io.terminus.snz.requirement.model.NewProductStep;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wanggen on 14-8-7.
 */
@Slf4j
@Component
public class NewProductImportManager {

    @Autowired
    private NewProductImportDao newProductImportDao;

    @Autowired
    private NewProductStepDao newProductStepDao;


    @Transactional
    /**
     *  新品导入
     *  1. 该新品未导入过（根据模块号+供应商名称确认）
     *      1.1 插入一条该新品的步骤
     *  2. 该新品导入过
     *      2.1 该新品存在当前步骤信息，根据新数据更新一次步骤信息
     *      2.2 该新品不存在当前步骤信息，根据新数据创建一条步骤信息，然后新增到表
     */
    public Long importNPI(NewProductImport newProductImport) {
        // 1. 根据 模块号、供应商号 查询该新品导入是否已经存在
        List<NewProductImport> productImportExists = newProductImportDao.findByModuleNumAndSupplierName(
                newProductImport.getModuleNum(), newProductImport.getSupplierName());

        // 1.1 不存在，插入新品导入数据
        if(productImportExists==null || productImportExists.size()==0){
            newProductImportDao.create(newProductImport);
            syncHandleStepInfo(newProductImport);
        }else{
            newProductImport.setId(productImportExists.get(0).getId());
        // 1.2 若存在，用当前发来的数据更新数据库中记录
            newProductImportDao.update(newProductImport);
            syncHandleStepInfo(newProductImport);
        }
        return newProductImport.getId();
    }


    /**
     * 当导入新品时，同步处理该新品的步骤信息
     * @param newProductImport 新品导入进度数据
     */
    private void syncHandleStepInfo(NewProductImport newProductImport) {
        Preconditions.checkState(newProductImport!=null);
        // 根据新数据判断当前数据属于哪个流程节点- 根据时间流，取最大一个时间作为当前时间节点
        NewProductStep.Step currStep = newProductImport.detectStep();

        // 根据 ${parent_id},${step} 查询步骤信息 - 该步骤也可能不存在
        NewProductStep step =
            newProductStepDao.findByParentIdAndStep(newProductImport.getId(), currStep.value());

        // 若该步骤信息存在，更新，若不存在则新增。
        if(step==null){
            step = NewProductStep.createFrom(newProductImport, null);
            newProductStepDao.create(step);
        }else{
            step = NewProductStep.createFrom(newProductImport, step);
            newProductStepDao.update(step);
        }
    }

}
