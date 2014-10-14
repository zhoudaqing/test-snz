package io.terminus.snz.related.services;

import com.google.common.base.Objects;
import com.google.common.base.Throwables;
import com.google.common.collect.Iterables;
import io.terminus.pampas.common.Response;
import io.terminus.snz.related.daos.CompensationDao;
import io.terminus.snz.related.daos.CompensationDetailDao;
import io.terminus.snz.related.models.Compensation;
import io.terminus.snz.related.models.CompensationDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author:  wenhaoli
 * Date: 2014-08-11
 */
@Service
@Slf4j
public class CompensationDetailServiceImpl implements CompensationDetailService {

    @Autowired
    private CompensationDetailDao compensationDetailDao;

    @Autowired
    private CompensationDao compensationDao;

    @Override
    public Response<Boolean> batchAdd(List<CompensationDetail> compensationDetails) {
        Response<Boolean> resp = new Response<Boolean>();
        try {
            if(compensationDetails ==null || Iterables.isEmpty(compensationDetails)) {
                log.error("compensationDetails can't be empty when add batch.");
                resp.setError("compensationDetails.is.empty.");
                return resp;
            }
            for(CompensationDetail midDetail: compensationDetails){
                Compensation mid = compensationDao.findOneByInfo(midDetail);
                midDetail.setListId(mid.getId());
            }
            Integer added = compensationDetailDao.creates(compensationDetails);
            if(!Objects.equal(added,compensationDetails.size())){
                log.error("some compensationdetails hasn't been added.");
                resp.setError("compensationdetail.batchadd.miss");
                return resp;
            }
            resp.setSuccess(Boolean.TRUE);
        }catch (Exception e){
            log.error("failed to batch add compensationdetails, cause:{}", Throwables.getStackTraceAsString(e));
            resp.setError("compensationdetail.batchadd.failed");
        }
        return resp;
    }

}
