package io.terminus.snz.web.helpers;

import io.terminus.pampas.engine.handlebars.HandlebarEngine;
import io.terminus.snz.web.BaseTest;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

/**
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-8-5
 */
public class SnzHandlebarHelpersTest extends BaseTest{

    @Mock
    private HandlebarEngine handlebarEngine;

    @InjectMocks
    private SnzHandlebarHelpers snzHandlebarHelpers;

    @Test
    public void testInit(){
        snzHandlebarHelpers.init();
    }
}
