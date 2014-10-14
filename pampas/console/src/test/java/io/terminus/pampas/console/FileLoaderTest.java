/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.console;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-5-12
 */
public class FileLoaderTest {
    @Test
    public void test() throws IOException {
        URL url = Resources.getResource("back_config.yaml");
        System.out.println(Resources.toString(url, Charsets.UTF_8));
    }
}
