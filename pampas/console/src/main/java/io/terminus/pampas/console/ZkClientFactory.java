/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.console;

import com.alibaba.dubbo.config.RegistryConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.google.common.base.Charsets;
import io.terminus.common.utils.JsonMapper;
import io.terminus.pampas.engine.utils.Protocol;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-5-13
 */
@Component
public class ZkClientFactory implements FactoryBean<ZkClient> {
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public ZkClient getObject() throws Exception {
        RegistryConfig registryConfig = applicationContext.getBean(RegistryConfig.class);
        String zkUrl;
        if (Protocol.analyze(registryConfig.getAddress()) == Protocol.ZK) {
            zkUrl = Protocol.removeProtocol(registryConfig.getAddress(), Protocol.ZK);
        } else if ("zookeeper".equalsIgnoreCase(registryConfig.getProtocol())) {
            zkUrl = registryConfig.getAddress();
        } else {
            throw new IllegalArgumentException("only supported zookeeper dubbo registry protocol");
        }
        ZkClient zkClient = new ZkClient(zkUrl);
        zkClient.setZkSerializer(new ZkSerializer() {
            private final JavaType mapType = JsonMapper.nonEmptyMapper().createCollectionType(Map.class, String.class, Object.class);
            @Override
            public byte[] serialize(Object data) throws ZkMarshallingError {
                return JsonMapper.nonEmptyMapper().toJson(data).getBytes(Charsets.UTF_8);
            }
            @Override
            public Object deserialize(byte[] bytes) throws ZkMarshallingError {
                return JsonMapper.nonEmptyMapper().fromJson(new String(bytes, Charsets.UTF_8), mapType);
            }
        });
        return zkClient;
    }

    @Override
    public Class<?> getObjectType() {
        return ZkClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
