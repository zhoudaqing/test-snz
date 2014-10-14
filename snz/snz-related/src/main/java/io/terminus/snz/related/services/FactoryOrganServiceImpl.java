package io.terminus.snz.related.services;

import com.google.common.base.Throwables;
import io.terminus.pampas.common.Response;
import io.terminus.snz.related.daos.FactoryOrganDao;
import io.terminus.snz.related.models.FactoryOrgan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-7-25
 */
@Service @Slf4j
public class FactoryOrganServiceImpl implements FactoryOrganService {

    @Autowired
    private FactoryOrganDao factoryOrganDao;

    @Override
    public Response<Integer> batchAdd(List<FactoryOrgan> factoryOrgans) {
        Response<Integer> resp = new Response<Integer>();
        try {
            Integer inserted = factoryOrganDao.creates(factoryOrgans);
            resp.setResult(inserted);
        } catch(Exception e){
            log.error("failed to batch add factoryorgan, cause:{}", Throwables.getStackTraceAsString(e));
            resp.setError("factoryorgan.batchadd.fail");
        }
        return resp;
    }

    @Override
    public Response<List<String>> findOrganByFactory(String factory) {
        Response<List<String>> resp = new Response<List<String>>();
        try{
            resp.setResult(factoryOrganDao.findOrganByFactory(factory));
        } catch (Exception e){
            log.error("failed to find organ or factory({}), cause: {}",
                    factory, Throwables.getStackTraceAsString(e));
            resp.setError("factoryorgan.findorganbyfactory.fail");
        }
        return resp;
    }
}
