package io.terminus.snz.user.service;

import com.google.common.collect.Lists;
import com.haier.openplatform.hac.dto.HacUserDTO;
import com.haier.openplatform.hac.dto.UserIDSDTO;
import com.haier.openplatform.hac.dto.UserMergeDTO;
import com.haier.openplatform.hac.service.HacUserServiceCli;
import com.haier.openplatform.hac.service.agent.HacUserServiceClient;
import com.haier.openplatform.util.ExecuteResult;
import io.terminus.snz.user.BaseTest;
import io.terminus.snz.user.dao.BlackListDao;
import io.terminus.snz.user.dao.PurchaserExtraDao;
import io.terminus.snz.user.dao.UserDao;
import io.terminus.snz.user.dto.RichSupplierDto;
import io.terminus.snz.user.manager.AccountManagerHac;
import io.terminus.snz.user.model.User;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-8-5
 */
public class HacUserServiceTest extends BaseTest{

    @Mock
    private HacUserServiceClient hacUserServiceClient;

    @Mock
    private HacUserServiceCli hacUserServiceCli;

    @Mock
    private UserDao userDao;

    @Mock
    private BlackListDao blackListDao;

    @Mock
    private PurchaserExtraDao purchaserExtraDao;

    @Mock
    private AccountManagerHac accountManagerHac;

    @InjectMocks
    private HacUserServiceImpl hacUserService;

    @Test
    public void testLogin(){
        ExecuteResult<UserMergeDTO> mockResult = new ExecuteResult<UserMergeDTO>();
        mockResult.setResult(mockUserMergeDTO());
        mockResult.setSuccessMessage("yes");
        // mock user existed
        when(hacUserServiceCli.mergeUserLogin(any(UserMergeDTO.class))).thenReturn(mockResult);
        when(userDao.findByNick(anyString())).thenReturn(mockPurchaser(1L));
        when(purchaserExtraDao.findByUserId(anyLong())).thenReturn(mockPurchaserExtra());
        assertTrue(hacUserService.login("xxx", "ooo").isSuccess());

        // mock user not exist
        when(userDao.findByNick(anyString())).thenReturn(null);
        assertTrue(hacUserService.login("xxx", "ooo").isSuccess());
    }

    @Test
    public void testLogout(){
        ExecuteResult<String> mockResult = new ExecuteResult<String>();
        mockResult.setSuccessMessage("yes");
        mockResult.setResult("success");
        when(hacUserServiceClient.userLogout(any(UserIDSDTO.class))).thenReturn(mockResult);
        assertTrue(hacUserService.logout("7D02CD5B2F0E75FDDDE6FCC5527768D6-10.135.106.115").isSuccess());
    }

    @Test
    public void testFindStaffByWorkNo(){
        ExecuteResult<List<HacUserDTO>> mockResult = new ExecuteResult<List<HacUserDTO>>();
        mockResult.setResult(Arrays.asList(mockHacUserDTO()));
        mockResult.setSuccessMessage("ok");
        when(hacUserServiceCli.searchIDMUser(anyString(), anyString(), anyString())).thenReturn(mockResult);
        when(userDao.findByNick(anyString())).thenReturn(null);
        assertNotNull(hacUserService.findStaffByWorkNo("xxoo").getResult());
    }

    @Test
    public void testFindStaffByName(){
        ExecuteResult<List<HacUserDTO>> mockResult = new ExecuteResult<List<HacUserDTO>>();
        mockResult.setResult(Arrays.asList(mockHacUserDTO(), mockHacUserDTO()));
        mockResult.setSuccessMessage("ok");
        when(hacUserServiceCli.searchIDMUser(anyString(), anyString(), anyString())).thenReturn(mockResult);
        when(userDao.findByNicks(anyList())).thenReturn(Lists.<User>newArrayList());
        assertEquals(2, hacUserService.findStaffByName("ooxx").getResult().size());
    }

    @Test
    public void testCreateSupplier(){
        when(accountManagerHac.createSupplier(any(RichSupplierDto.class))).thenReturn(1L);
        when(blackListDao.findByName(anyString())).thenReturn(null);
        when(blackListDao.findAll()).thenReturn(Arrays.asList(mockBlackList(), mockBlackList()));
        assertEquals(1, hacUserService.createSupplier(mockRichSupplierDto(1L)).getResult().intValue());
    }

    @Test
    public void testActiveUser(){
        ExecuteResult<String> mockResult = new ExecuteResult<String>();
        mockResult.setResult("xxx");
        mockResult.setSuccessMessage("ok");
        when(hacUserServiceClient.activeUser(any(UserIDSDTO.class))).thenReturn(mockResult);
        assertTrue(hacUserService.activeUser(loginer, "123123").getResult());
    }

    @Test
    public void testUpdateUser(){
        assertTrue(hacUserService.updateUser(mockPurchaser(1L)).getResult());
    }

    @Test
    public void testChangePassword(){
        ExecuteResult<UserMergeDTO> mockResult = new ExecuteResult<UserMergeDTO>();
        mockResult.setResult(mockUserMergeDTO());
        mockResult.setSuccessMessage("yes");
        when(hacUserServiceCli.mergeUserLogin(any(UserMergeDTO.class))).thenReturn(mockResult);
        when(userDao.findByNick(anyString())).thenReturn(mockPurchaser(1L));
        when(userDao.findById(loginer.getId())).thenReturn(new User());
        assertTrue(hacUserService.changePassword(loginer, "xxx", "yyy", "yyy").getResult());
    }

    @Test
    public void testResendActiveCode(){
        ExecuteResult<String> mockResult = new ExecuteResult<String>();
        mockResult.setResult("xxx");
        mockResult.setSuccessMessage("ok");
        when(hacUserServiceClient.resendActiveCode(any(UserIDSDTO.class))).thenReturn(mockResult);
        assertTrue(hacUserService.resendActiveCode(loginer).getResult());
    }

    private HacUserDTO mockHacUserDTO(){
        HacUserDTO hacUserDTO = new HacUserDTO();
        hacUserDTO.setId(1L);
        hacUserDTO.setName("xxx");
        hacUserDTO.setNickName("ooo");
        return hacUserDTO;
    }

    private UserMergeDTO mockUserMergeDTO(){
        UserMergeDTO umd = new UserMergeDTO();
        umd.setId(123123L);
        umd.setNickName("xxx");
        umd.setUserName("ooo");
        umd.setEmaile("123124@aa.com");
        umd.setMobile("123123123");
        return umd;
    }

}
