package io.terminus.snz.message;

import org.junit.Before;
import org.mockito.MockitoAnnotations;

public class BaseRedisDaoTest {
    @Before
    public void init(){
        // NOTE: necessary
        MockitoAnnotations.initMocks(this);
    }
}
