package io.terminus.snz.related.services;

import com.google.common.base.Throwables;
import io.terminus.pampas.common.Response;
import io.terminus.snz.related.daos.FactoryProductionDirectorDao;
import io.terminus.snz.related.models.FactoryProductionDirector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 8/15/14
 */
@Slf4j
@Service
public class FactoryProductionDirectorServiceImpl implements FactoryProductionDirectorService {

    @Autowired
    private FactoryProductionDirectorDao factoryProductionDirectorDao;

    @Override
    public Response<List<FactoryProductionDirector>> findByFactoryNumAndProductLineName(String factoryNum, String productLineName) {
        Response<List<FactoryProductionDirector>> result = new Response<List<FactoryProductionDirector>>();
        try {
            FactoryProductionDirector params = new FactoryProductionDirector();
            params.setFactoryNum(checkNotNull(factoryNum));
            params.setProductLineName(checkNotNull(productLineName));
            result.setResult(factoryProductionDirectorDao.findBy(params));
        } catch (Exception e) {
            log.error("findByFactoryNumAndProductLineName(factoryNum={}, productLineName={}) failed, cause:{}",
                    factoryNum, productLineName, Throwables.getStackTraceAsString(e));
            result.setError("factory.production.director.query.fail");
        }
        return result;
    }

    @Override
    public Response<List<FactoryProductionDirector>> findByUserNick(String nick) {
        Response<List<FactoryProductionDirector>> result = new Response<List<FactoryProductionDirector>>();
        try {
            FactoryProductionDirector params = new FactoryProductionDirector();
            params.setDirectorId(checkNotNull(nick));
            result.setResult(factoryProductionDirectorDao.findBy(params));
        } catch (Exception e) {
            log.error("findByUserNick(nick={}) failed, cause:{}",
                    nick, Throwables.getStackTraceAsString(e));
            result.setError("factory.production.director.query.fail");
        }
        return result;
    }
}
