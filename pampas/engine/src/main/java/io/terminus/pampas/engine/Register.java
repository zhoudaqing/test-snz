/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.ServiceBean;
import com.fasterxml.jackson.databind.JavaType;
import com.google.common.base.Charsets;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import io.terminus.common.utils.JsonMapper;
import io.terminus.pampas.engine.service.AppService;
import io.terminus.pampas.engine.service.ConfigService;
import io.terminus.pampas.engine.utils.DubboHelper;
import io.terminus.pampas.engine.utils.Protocol;
import io.terminus.pampas.engine.utils.ZkPaths;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-5-13
 */
@Configuration
@Slf4j
public class Register {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private DubboHelper dubboHelper;
    @Autowired
    private Setting setting;

    private ZkClient zkClient;
    private String groupName;
    private RegistryConfig registryConfig = null;

    @PostConstruct
    private void init() throws Exception {
        ApplicationConfig applicationConfig = applicationContext.getBean(ApplicationConfig.class);
        groupName = applicationConfig.getName();
        // 1. 获取信息并注册到 zk
        checkNotNull(groupName, "applicationConfig.getName() should not be null");
        final ProtocolConfig protocolConfig = applicationContext.getBean(ProtocolConfig.class);
        if (!Strings.isNullOrEmpty(protocolConfig.getName()) // 为空时默认是 dubbo
                && !Objects.equal(protocolConfig.getName().toLowerCase(), "dubbo")) {
            throw new IllegalArgumentException("only support dubbo protocol");
        }
        // 随机生成的 port 无法简单获取，所以报错
        if (protocolConfig.getPort() == null || protocolConfig.getPort() <= 0) {
            throw new IllegalArgumentException("only support clearly port definition in dubbo protocol");
        }
        prepareZkClient();
        String groupPath = ZkPaths.groupPath(groupName);
        if (!zkClient.exists(groupPath)) {
            zkClient.createPersistent(groupPath, true);
        }
        final String cellName = InetAddress.getLocalHost().getHostAddress();
        final String cellPath = ZkPaths.cellPath(groupName, cellName);
        IZkStateListener stateListener = new IZkStateListener() {
            @Override
            public void handleStateChanged(Watcher.Event.KeeperState state) throws Exception {}
            @Override
            public void handleNewSession() throws Exception {
                if (zkClient.exists(cellPath)) {
                    zkClient.delete(cellPath);
                }
                Map<String, Object> cellInfo = Maps.newHashMap();
                cellInfo.put("port", protocolConfig.getPort());
                zkClient.create(cellPath, cellInfo, CreateMode.EPHEMERAL);
            }
        };
        zkClient.subscribeStateChanges(stateListener);
        // run first time
        stateListener.handleNewSession();
    }

    public <T> ServiceBean<T> createServiceBean(Class<T> clazz) {
        ServiceBean<T> serviceBean = new ServiceBean<T>();
        if (registryConfig != null) {
            serviceBean.setRegistry(registryConfig);
        }
        serviceBean.setInterface(clazz);
        serviceBean.setVersion(groupName);
        serviceBean.setRef(applicationContext.getBean(clazz));
        return serviceBean;
    }

    @Bean
    public ServiceBean<ConfigService> dubboConfigService() {
        return createServiceBean(ConfigService.class);
    }

    @Bean
    public ServiceBean<AppService> dubboAppService() {
        return createServiceBean(AppService.class);
    }

    private void prepareZkClient() {
        Map<String, RegistryConfig> registryConfigs = applicationContext.getBeansOfType(RegistryConfig.class);
        if (registryConfigs.size() == 1) {
            registryConfig = registryConfigs.values().iterator().next();
        } else {
            if (Strings.isNullOrEmpty(setting.getRegistryId())) {
                throw new IllegalArgumentException("registryId in [Setting] should not be empty when multi dubbo registry configs");
            }
            if (!registryConfigs.containsKey(setting.getRegistryId())) {
                throw new IllegalArgumentException("no specific dubbo registry found for id: " + setting.getRegistryId());
            }
            registryConfig = registryConfigs.get(setting.getRegistryId());
        }
        String zkUrl;
        if (Protocol.analyze(registryConfig.getAddress()) == Protocol.ZK) {
            zkUrl = Protocol.removeProtocol(registryConfig.getAddress(), Protocol.ZK);
        } else if ("zookeeper".equalsIgnoreCase(registryConfig.getProtocol())) {
            zkUrl = registryConfig.getAddress();
        } else {
            throw new IllegalArgumentException("only supported zookeeper dubbo registry protocol");
        }
        zkClient = new ZkClient(zkUrl);
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
    }
}
