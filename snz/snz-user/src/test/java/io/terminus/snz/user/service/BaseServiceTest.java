package io.terminus.snz.user.service;

import io.terminus.pampas.common.BaseUser;
import org.junit.Before;
import org.mockito.MockitoAnnotations;

/**
 * Author:Guo Chaopeng
 * Created on 14-8-3.
 */
public abstract class BaseServiceTest {

    protected BaseUser loginer;

    public BaseServiceTest() {
        loginer = new BaseUser();
        loginer.setId(1L);
        loginer.setName("zhangsan");
        loginer.setNickName("jack");
        loginer.setMobile("18969971111");
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
}
