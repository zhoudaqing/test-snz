/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.console;

import io.terminus.pampas.engine.utils.DubboHelper;
import io.terminus.pampas.engine.utils.ZkPaths;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-5-13
 */
@Component
public class ServiceHelper {
    @Autowired
    private ZkClient zkClient;
    @Autowired
    private DubboHelper dubboHelper;

    public <T> T getService(Class<T> clazz, String group) {
        // group is version
        return dubboHelper.getReference(clazz, group);
    }

    public <T> T getService(Class<T> clazz, String group, String cell) {
        // cell name is ip
//        if (MoreObjects.equal(localIp, cell)) {
//            return applicationContext.getBean(clazz);
//        } IP 一样不代表肯定在同一个 JVM 里，所以这是错的
        Map<String, Object> cellInfo = zkClient.readData(ZkPaths.cellPath(group, cell));
        return dubboHelper.getReference(clazz, group, cell, (Integer) cellInfo.get("port"));
    }
}
