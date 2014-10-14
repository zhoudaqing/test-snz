package io.terminus.snz.related.services;

import io.terminus.pampas.common.Response;
import io.terminus.snz.related.models.FactoryProductionDirector;

import java.util.List;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 8/15/14
 */
public interface FactoryProductionDirectorService {

    /**
     * 根据工厂代号和产业线名称查询
     * @param factoryNum         工厂代号
     * @param productLineName    产品线名称
     * @return 所要查询的列表
     */
    Response<List<FactoryProductionDirector>> findByFactoryNumAndProductLineName(String factoryNum, String productLineName);

    /**
     * 通过用户登录名查询
     * @param nick    用户登录名
     * @return 所要查询的列表
     */
    Response<List<FactoryProductionDirector>> findByUserNick(String nick);
}
