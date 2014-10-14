/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.mapping;

import com.google.common.base.Objects;
import io.terminus.common.redis.dao.RedisBaseDao;
import io.terminus.pampas.engine.model.App;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-6-27
 */
public class TypeArgumentsTest {
    public void testMethod(List<RedisBaseDao<App>> param) {
    }

    @Test
    public void test() throws NoSuchMethodException {
        Method[] methods = TypeArgumentsTest.class.getDeclaredMethods();
        for (Method method : methods) {
            if (Objects.equal(method.getName(), "testMethod")) {
                for (Type type : method.getGenericParameterTypes()) {
                    if (type instanceof ParameterizedType) {
                        Type firstType = ((ParameterizedType) type).getActualTypeArguments()[0];
                        System.out.println(firstType);
                        if (firstType instanceof ParameterizedType) {
                            System.out.println(((ParameterizedType) firstType).getActualTypeArguments()[0]);
                            System.out.println(((ParameterizedType) firstType).getRawType());
                        }
                    }
                }
            }
        }
    }
}
