package io.terminus.snz.web.controllers;

import io.terminus.pampas.common.Response;
import io.terminus.pampas.engine.MessageSources;
import io.terminus.snz.requirement.model.NewProductImport;
import io.terminus.snz.requirement.service.NewProductImportService;
import io.terminus.snz.web.BaseTest;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

/**
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-8-5
 */
public class NewProductImportControllerTest extends BaseTest{

    @Mock
    private MessageSources messageSources;

    @Mock
    private NewProductImportService newProductImportService;

//    @InjectMocks
//    private NewProductImportController newProductImportController;

    @Test
    public void testImportNewProduct(){
        Response<Long> mockResp = new Response<Long>();
        mockResp.setResult(1L);
        when(newProductImportService.create(any(NewProductImport.class))).thenReturn(mockResp);
        //assertTrue(newProductImportController.importNewProduct(request));
    }
}
