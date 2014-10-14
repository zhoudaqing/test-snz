package io.terminus.snz.requirement.eai;

import com.google.common.collect.Lists;
import com.haier.QuerySupplierBalanceFromGVS.QuerySupplierBalanceFromGVS;
import com.haier.QuerySupplierBalanceFromGVS.QuerySupplierBalanceFromGVS_Service;
import com.haier.QuerySupplierBalanceFromGVS.ZFIINTLIFNRYEIN;
import com.haier.QuerySupplierBalanceFromGVS.ZFIINTLIFNRYEOUT;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.when;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 7/24/14
 */
public class QuerySupplierBalanceFromGVSTest {

    @Mock
    private QuerySupplierBalanceFromGVS_Service service;

    @Mock
    private QuerySupplierBalanceFromGVS soap;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test1() {
//        QuerySupplierBalanceFromGVS_Service service = new QuerySupplierBalanceFromGVS_Service();
//        QuerySupplierBalanceFromGVS soap = service.getQuerySupplierBalanceFromGVSSOAP();

        List<ZFIINTLIFNRYEIN> inputs = Lists.newArrayList();

        for (String str : new String[]{"V12359"}) {
            ZFIINTLIFNRYEIN input = new ZFIINTLIFNRYEIN();
            input.setLIFNR(str);
            inputs.add(input);
        }

        List<ZFIINTLIFNRYEOUT> outputs = Lists.newArrayList(new ZFIINTLIFNRYEOUT());

        when(soap.querySupplierBalanceFromGVS(inputs)).thenReturn(outputs);
    }
}
