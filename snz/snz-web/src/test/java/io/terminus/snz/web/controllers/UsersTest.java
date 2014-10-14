package io.terminus.snz.web.controllers;

import io.terminus.pampas.common.Response;
import io.terminus.pampas.engine.MessageSources;
import io.terminus.snz.user.dto.UserDto;
import io.terminus.snz.user.model.User;
import io.terminus.snz.user.service.AccountService;
import io.terminus.snz.user.service.CompanyService;
import io.terminus.snz.web.BaseTest;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-8-5
 */
public class UsersTest extends BaseTest{

    @Mock
    private MessageSources messageSources;

    @Mock
    private AccountService<User> accountService;

    @Mock
    private CompanyService companyService;

    @InjectMocks
    private Users users;

    @Test
    public void testLogin(){
        Response<UserDto> mockResp = new Response<UserDto>();
        mockResp.setResult(mockUserDto());
        when(accountService.login(anyString(), anyString())).thenReturn(mockResp);
        when(request.getSession()).thenReturn(session);
        assertEquals("/", users.login("abc", "def", "", request));
    }

    @Test
    public void testDirtyLogin(){
        Response<UserDto> mockResp = new Response<UserDto>();
        mockResp.setResult(mockUserDto());
        when(accountService.login(anyString(), anyString())).thenReturn(mockResp);
        when(request.getSession()).thenReturn(session);
        assertEquals("/", users.login("abc", "def", "", request));
    }

    @Test
    public void testLogout(){
        when(request.getSession(anyBoolean())).thenReturn(session);
        when(session.getAttribute(anyString())).thenReturn(new String("session_user_id"));
        assertEquals("redirect:/", users.logout(request));
    }
}
