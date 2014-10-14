package io.terminus.snz.related;

import io.terminus.pampas.common.BaseUser;
import org.junit.Before;
import org.mockito.MockitoAnnotations;

/**
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-8-4
 */
public class BaseTest {

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    protected BaseUser loginer;

    public BaseTest(){
        loginer = new BaseUser();
        loginer.setId(1L);
        loginer.setName("xxx");
        loginer.setNickName("ooo");
        loginer.setMobile("xxoo");
    }
}
