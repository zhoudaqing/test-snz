/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.mapping;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import io.terminus.pampas.client.Agent;
import io.terminus.pampas.client.ParamUtil;
import io.terminus.pampas.client.WrapResp;
import io.terminus.pampas.common.UserUtil;
import io.terminus.pampas.engine.config.model.Service;
import io.terminus.pampas.engine.utils.DubboHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-4-17
 */
@Component
public class DubboExecutor extends Executor<Object> {
    @Autowired
    private DubboHelper dubboHelper;

    private LoadingCache<Service, LinkedHashMap<String, Class>> methodInfoCache;

    @PostConstruct
    private void init() {
        methodInfoCache = CacheBuilder.newBuilder().build(new CacheLoader<Service, LinkedHashMap<String, Class>>() {
            @Override
            public LinkedHashMap<String, Class> load(Service service) throws Exception {
                Agent agent = getAgent(service.getApp());
                LinkedHashMap<String, String> paramsInfo = agent.getParamsInfo(service.getUri());
                LinkedHashMap<String, Class> params = Maps.newLinkedHashMap();
                for (String name : paramsInfo.keySet()) {
                    String className = paramsInfo.get(name);
                    Class paramClass = ParamUtil.getPrimitiveClass(className);
                    if (paramClass == null) {
                        try {
                            paramClass = Class.forName(className);
                        } catch (ClassNotFoundException e) {
                            paramClass = ParamConverter.UnKnowClass.class;
                        }
                    }
                    params.put(name, paramClass);
                }
                return params;
            }
        });
    }

    @Override
    public Object exec(Service service, Map<String, Object> params) {
        LinkedHashMap<String, Class> methodParams = methodInfoCache.getUnchecked(service);
        Map<String, Object> args = Maps.newHashMap();
        boolean needContext = false;
        for (String paramName : methodParams.keySet()) {
            Class paramClass = methodParams.get(paramName);
            Object arg = ParamConverter.convertParam(paramName, paramClass, params);
            if (arg != null) {
                args.put(paramName, arg);
            } else {
                // 因为 BaseUser 和 InnerCookie 肯定不会为空
                // 如果转换完的 arg 为 null ，而参数类型又不是基本类型就认为需要整个上下文到 Agent 那里转对象
                if (!ParamUtil.isPrimitive(paramClass)) {
                    needContext = true;
                }
            }
        }
        Agent agent = getAgent(service.getApp());
        WrapResp wrapResp = agent.call(service.getUri(), args, needContext ? params : null);
        // 因为是远端调用 所以这里返回的 InnerCookie 是经过序列化和反序列化后的复制品，需要 merge 起来
        if (wrapResp.getCookie() != null) {
            UserUtil.getInnerCookie().merge(wrapResp.getCookie());
        }
        return unwrapResponse(wrapResp.getResult());
    }

    private Agent getAgent(String app) {
        return dubboHelper.getReference(Agent.class, app);
    }
}
