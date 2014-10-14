/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.mapping;

import com.google.common.base.MoreObjects;
import io.terminus.common.utils.JsonMapper;
import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-6-12
 */
public class JsonTest {
    public static class TestObj {
        @Getter
        @Setter
        private Date date;

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("date", date)
                    .toString();
        }
    }

    @Test
    public void testString2Date() {
        String json = "{\"date\": \"2012-01-01\"}";
        TestObj testObj = JsonMapper.nonEmptyMapper().fromJson(json, TestObj.class);
        System.out.println(testObj);
    }

    @Test
    public void testDate2String() {
        TestObj testObj = new TestObj();
        testObj.setDate(new Date());
        System.out.println(JsonMapper.nonEmptyMapper().toJson(testObj));
    }
}
