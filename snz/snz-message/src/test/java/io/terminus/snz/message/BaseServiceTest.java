package io.terminus.snz.message;

import io.terminus.pampas.common.BaseUser;
import org.junit.Before;
import org.mockito.MockitoAnnotations;

/**
 * 基础服务测试
 */
public abstract class BaseServiceTest {

    protected BaseUser loginer;

    public BaseServiceTest(){
        loginer = new BaseUser();
        loginer.setId(1L);
        loginer.setName("xxx");
        loginer.setNickName("ooo");
        loginer.setMobile("xxoo");
    }

    @Before
    public void init(){
        // NOTE: necessary
        MockitoAnnotations.initMocks(this);
    }
}
