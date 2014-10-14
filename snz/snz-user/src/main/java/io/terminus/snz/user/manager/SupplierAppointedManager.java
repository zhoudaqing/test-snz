package io.terminus.snz.user.manager;

import com.google.common.base.Strings;
import io.terminus.snz.user.dao.SupplierAppointedDao;
import io.terminus.snz.user.model.SupplierAppointed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Author:Grancy Guo
 * Created on 14-9-19.
 */
@Slf4j
@Component
public class SupplierAppointedManager {
    @Autowired
    private SupplierAppointedDao supplierAppointedDao;

    @Transactional
    public boolean batchSupplierAppointed(SupplierAppointed supplierAppointed){
        //公司名称和需求ID非空
        if(Strings.isNullOrEmpty(supplierAppointed.getCorporation())){
            log.error("Corporation cannot be null");
            return false;
        }
        if(supplierAppointed.getRequirementId() == null){
            log.error("RequirementId cannot be null");
            return false;
        }
        //根据需求ID查询是否存在该需求下的甲指供应商
        SupplierAppointed check = supplierAppointedDao.findCompanyByReq(supplierAppointed.getRequirementId());

        if(check == null){
            //如果不存在则插入
            supplierAppointed.setStatus(SupplierAppointed.Status.SUBMITTED.value());
            supplierAppointedDao.create(supplierAppointed);
        }else{
            //如果存在，则先判断审核状态，如果已经审核则不修改
            //如果未审核，则执行先删后插
            if( (check.getStatus().intValue() != SupplierAppointed.Status.FIRSTPASS.value() )
              && check.getStatus().intValue() != SupplierAppointed.Status.LASTPASS.value() ){
                //如果是可修改状态，那么更新数据
                supplierAppointed.setStatus(SupplierAppointed.Status.SUBMITTED.value());
                supplierAppointedDao.deleteCompanyByReq(supplierAppointed.getRequirementId());
                supplierAppointedDao.create(supplierAppointed);
            }
        }
        return true;
    }
}
