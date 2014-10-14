package io.terminus.snz.user.service;

import com.google.common.base.Throwables;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.dao.SupplierTQRDCInfoTmpDao;
import io.terminus.snz.user.model.SupplierTQRDCInfoTmp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Author:  wenhaoli
 * Date: 2014-08-16
 */
@Slf4j @Service
public class SupplierTQRDCInfoTmpServiceImpl implements SupplierTQRDCInfoTmpService  {

    @Autowired
    private SupplierTQRDCInfoTmpDao supplierTQRDCInfoTmpDao;

    @Override
    public Response<SupplierTQRDCInfoTmp> findByUserId(BaseUser user){
        Response<SupplierTQRDCInfoTmp> resp = new Response<SupplierTQRDCInfoTmp>();
        try{
            if (user == null){
                log.error("user isn't login");
                resp.setError("user.not.login");
                return resp;
            }
            SupplierTQRDCInfoTmp supplierTQRDCInfoTmp = supplierTQRDCInfoTmpDao.findByUserId(user.getId());
            resp.setResult(supplierTQRDCInfoTmp);
        } catch (Exception e){
            log.error("failed to find InfoTmp, causer: {}", user, Throwables.getStackTraceAsString(e));
            resp.setError("InfoTmp.find.fail");
        }
        return resp;
    }
}
