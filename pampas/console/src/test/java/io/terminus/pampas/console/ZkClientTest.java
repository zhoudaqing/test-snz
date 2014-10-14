/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.console;

import org.I0Itec.zkclient.ZkClient;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-5-13
 */
public class ZkClientTest {
    private ZkClient zkClient;

    @Before
    public void setUp() {
        zkClient = new ZkClient("localhost:2181");
    }

    @Test
    public void test() {
        System.out.println(zkClient.getChildren("/dubbo/com.aixforce.TestProvider/providers"));
    }
}
