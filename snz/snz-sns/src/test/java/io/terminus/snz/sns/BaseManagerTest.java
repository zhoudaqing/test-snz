package io.terminus.snz.sns;

import org.junit.Before;
import org.mockito.MockitoAnnotations;

public class BaseManagerTest {
    @Before
    public void init(){
        // NOTE: necessary
        MockitoAnnotations.initMocks(this);
    }
}
