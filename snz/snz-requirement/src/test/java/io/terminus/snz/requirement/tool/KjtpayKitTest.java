package io.terminus.snz.requirement.tool;

import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class KjtpayKitTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testPayRequest() throws Exception {
        Map<String, String> params = KjtpayKit.buildPayRequest(
                "103021", 1L, KjtpayKit.dateFormat.parseDateTime("20140617020101").toDate(), "测试商品"
        );
        Map<String, String> nParams = KjtpayKit.sign(params);

        //String page = KjtpayKit.request(nParams);
        //System.out.print(page);
    }

    @Test
    public void testSign() throws Exception {
        Map<String, String> params = Maps.newTreeMap(String.CASE_INSENSITIVE_ORDER);

        // 错误时，应答的POST
        params.put("inputCharset", "1");
        params.put("payType", "02");
        params.put("bankCode", "");
        params.put("orderId", "103021");
        params.put("orderTime", "20140617020101");
        params.put("orderAmount", "1");
        params.put("dealId", "");
        params.put("dealTime", "20140619121337");
        params.put("ext1", "");
        params.put("ext2", "");
        params.put("payResult", "0001");
        params.put("errCode", "10014");

        String signed = KjtpayKit.sign(params).get("signMsg");
        //Assert.assertEquals("OGVlODYzODM4OWQ5NGI3MTZiYTk4ZGExYTM1YWJkMWY=", signed);
    }
}