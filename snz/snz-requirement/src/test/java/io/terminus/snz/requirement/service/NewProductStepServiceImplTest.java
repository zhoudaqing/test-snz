package io.terminus.snz.requirement.service;

import com.google.common.collect.Lists;
import io.terminus.snz.requirement.manager.NewProductStepManager;
import io.terminus.snz.requirement.model.NewProductStep;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class NewProductStepServiceImplTest {

    @InjectMocks
    NewProductStepServiceImpl newProductStepService;

    @Mock
    NewProductStepManager newProductStepManager;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPost() throws Exception {}

    @Test
    public void testBatchPost() throws Exception {
        NewProductStep step = new NewProductStep();
        newProductStepService.batchPost(Lists.newArrayList(step));
    }
}