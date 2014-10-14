package io.terminus.snz.user.tool;

import com.github.kevinsawicki.http.HttpRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.DriverManager;

import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.when;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 8/5/14
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(DriverManager.class)
public class HaierCasHttpUtilTest {

    @Mock
    private HttpRequest request;

    @Test
    public void testRequest() throws Exception {
//        PowerMockito.mockStatic(HaierCasHttpUtil.class);
//        BDDMockito.given(HaierCasHttpUtil.request(anyString(), anyMap())).willReturn("{error:0}");

        //Map<String, String> params = Maps.newHashMap();
        //HaierCasHttpUtil.request("www.baidu.com", params);
        //assertThat(HaierCasHttpUtil.request("www.google.com", params), is("ok"));

        PowerMockito.mockStatic(HttpRequest.class);

//        BDDMockito.when(HttpRequest.get(anyString())).thenReturn(request);
        when(request.form(anyMap())).thenReturn(request);
        when(request.body()).thenReturn("{error:0}");


//        boolean isSuccess = HaierCasHttpUtil.register("username", "email", "password");
//        assertTrue(isSuccess);
    }

    @Test
    public void testRegister() throws Exception {

    }
}