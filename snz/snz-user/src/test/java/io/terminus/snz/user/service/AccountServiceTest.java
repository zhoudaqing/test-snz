package io.terminus.snz.user.service;

import com.google.common.base.Optional;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.dao.BlackListDao;
import io.terminus.snz.user.dao.PurchaserExtraDao;
import io.terminus.snz.user.dao.SupplierApproveLogDao;
import io.terminus.snz.user.dao.UserDao;
import io.terminus.snz.user.dto.*;
import io.terminus.snz.user.event.ApproveEvent;
import io.terminus.snz.user.event.SupplierEventBus;
import io.terminus.snz.user.manager.AccountManager;
import io.terminus.snz.user.model.*;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Desc:
 * Mail: houly@terminus.io
 * author: Hou Luyao
 * Date: 14-8-4.
 */
public class AccountServiceTest extends BaseServiceTest {

    @InjectMocks
    private AccountServiceImpl accountServiceImpl;

    @Mock
    private LoadingCache<Long, Optional<User>> userCache;

    @Mock
    private UserDao userDao;

    @Mock
    private SupplierApproveLogDao supplierApproveLogDao;

    @Mock
    private AccountManager accountManager;

    @Mock
    private SupplierIndexService supplierIndexService;

    @Mock
    private PurchaserExtraDao purchaserExtraDao;

    @Mock
    private SupplierEventBus eventBus;

    @Mock
    private BlackListDao blackListDao;

    @Test
    public void testFindUserById() {
        Response<User> response = accountServiceImpl.findUserById(null);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "user.id.not.null.fail");


        when(userDao.findById(1L)).thenReturn(createUser());
        response = accountServiceImpl.findUserById(2L);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "user.not.found");

        when(userDao.findById(anyLong())).thenReturn(createUser());
        response = accountServiceImpl.findUserById(1L);
        assertTrue(response.isSuccess());

    }

    @Test
    public void testFindUserByIds() {
        Response<List<User>> response = accountServiceImpl.findUserByIds(null);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "user.find.fail");

        when(userDao.findByIds(anyList())).thenReturn(Lists.<User>newArrayList());
        response = accountServiceImpl.findUserByIds(Lists.newArrayList(1L));
        assertTrue(response.isSuccess());

        when(userDao.findByIds(anyList())).thenThrow(Exception.class);
        response = accountServiceImpl.findUserByIds(Lists.newArrayList(1L));
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "user.find.fail");
    }

    @Test
    public void testFindUserByNick() {
        Response<User> response = accountServiceImpl.findUserByNick(null);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "user.nick.empty");

        when(userDao.findByNick(anyString())).thenReturn(createUser());
        response = accountServiceImpl.findUserByNick("nuck");
        assertTrue(response.isSuccess());

        when(userDao.findByNick(anyString())).thenReturn(null);
        response = accountServiceImpl.findUserByNick("nuck");
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "user.not.found");

        when(userDao.findByNick(anyString())).thenThrow(Exception.class);
        response = accountServiceImpl.findUserByNick("nuck");
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "user.query.fail");
    }

    @Test
    public void testFindUserByRoleStr() {
        // 传参为null
        Response<List<User>> resp = accountServiceImpl.findUserByRoleStr(null);
        assertFalse(resp.isSuccess());

        // 传参为空字符串
        resp = accountServiceImpl.findUserByRoleStr("");
        assertFalse(resp.isSuccess());

        // userDao查询异常
        when(userDao.findByRoleStr("hehe")).thenThrow(RuntimeException.class);
        resp = accountServiceImpl.findUserByRoleStr("hehe");
        assertFalse(resp.isSuccess());

        // 查到两条结果
        User user1 = new User();
        user1.setId(1L);
        user1.setRoleStr("hahaha");
        User user2 = new User();
        user2.setId(2L);
        user2.setRoleStr("bbhahahehe");
        when(userDao.findByRoleStr("haha")).thenReturn(Lists.newArrayList(user1, user2));
        resp = accountServiceImpl.findUserByRoleStr("haha");
        assertTrue(resp.isSuccess());
        assertThat(resp.getResult().size(), is(2));
        assertThat(resp.getResult().get(0).getId(), is(1L));
        assertThat(resp.getResult().get(0).getRoleStr(), is("hahaha"));
        assertThat(resp.getResult().get(1).getId(), is(2L));
        assertThat(resp.getResult().get(1).getRoleStr(), is("bbhahahehe"));
    }

    @Test
    public void testActiveUser() {
        Response<Boolean> response = accountServiceImpl.activeUser(null, null);
        assertTrue(response.isSuccess());
    }

    @Test
    public void testResendActiveCode() {
        Response<Boolean> response = accountServiceImpl.resendActiveCode(null);
        assertTrue(response.isSuccess());
    }

    @Test
    public void testChangePassword() {
        User user = createUser();
        user.setId(null);
        Response<Boolean> response = accountServiceImpl.changePassword(user, null, null, null);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "user.id.not.null.fail");

        user = createUser();
        response = accountServiceImpl.changePassword(user, null, null, null);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "user.password.empty");

        when(userDao.findById(anyLong())).thenReturn(null);
        response = accountServiceImpl.changePassword(user, "1234", "1234", "1234");
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "user.not.found");

        when(userDao.findById(anyLong())).thenReturn(createUser());
        response = accountServiceImpl.changePassword(user, "34", "1234", "1234");
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "user.password.incorrect");


        when(userDao.findById(anyLong())).thenReturn(createUser());
        when(userDao.update(any(User.class))).thenReturn(true);
        response = accountServiceImpl.changePassword(user, "1234", "1234", "1234");
        assertTrue(response.isSuccess());

        when(userDao.findById(anyLong())).thenThrow(Exception.class);
        when(userDao.update(any(User.class))).thenReturn(true);
        response = accountServiceImpl.changePassword(user, "1234", "1234", "1234");
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "user.change.password.fail");

    }

    @Test
    public void testFindSupplierApproveExtraByUserId() {
        Response<SupplierApproveExtra> response = accountServiceImpl.findSupplierApproveExtraByUserId(null);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "user.id.not.null.fail");

        when(supplierApproveLogDao.findLastByUserIdAndApproveType(anyLong(), anyInt())).thenReturn(new SupplierApproveLog());
        response = accountServiceImpl.findSupplierApproveExtraByUserId(1L);
        assertTrue(response.isSuccess());

        when(supplierApproveLogDao.findLastByUserIdAndApproveType(anyLong(),anyInt())).thenThrow(Exception.class);
        response = accountServiceImpl.findSupplierApproveExtraByUserId(1L);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "query.supplier.approve.extra.fail");
    }

    @Test
    public void testLogout() {
        Response<Boolean> response = accountServiceImpl.logout(null);
        assertTrue(response.isSuccess());
    }

    @Test
    public void testUpdateUser() {
        User user = createUser();
        user.setId(null);
        Response<Boolean> response = accountServiceImpl.updateUser(user);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "user.null.fail");

        when(userDao.update(any(User.class))).thenReturn(true);
        response = accountServiceImpl.updateUser(createUser());
        assertTrue(response.isSuccess());

        when(userDao.update(any(User.class))).thenThrow(Exception.class);
        response = accountServiceImpl.updateUser(createUser());
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "user.update.fail");

    }

    @Test
    public void testLogin() {
        Response<UserDto> response = accountServiceImpl.login(null, null);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "login.nick.empty");

        when(userDao.findByNick(anyString())).thenReturn(null);
        response = accountServiceImpl.login("nick", "1234");
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "user.not.found");

        User user = createUser();
        user.setStatus(-1);
        when(userDao.findByNick(anyString())).thenReturn(user);
        response = accountServiceImpl.login("nick", "1234");
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "user.account.frozen");

        when(userDao.findByNick(anyString())).thenReturn(createUser());
        response = accountServiceImpl.login("nick", "12345");
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "user.password.incorrect");

        when(userDao.findByNick(anyString())).thenReturn(createUser());
        response = accountServiceImpl.login("nick", "1234");
        assertTrue(response.isSuccess());

        when(userDao.findByNick(anyString())).thenThrow(Exception.class);
        response = accountServiceImpl.login("nick", "1234");
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "user.query.fail");

    }

    @Test
    public void testDirtyLogin() {
        Response<UserDto> response = accountServiceImpl.dirtyLogin(null, null);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "login.nick.empty");

        when(userDao.findByNick(anyString())).thenReturn(null);
        response = accountServiceImpl.dirtyLogin("nick", "1234");
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "user.not.found");

        User user = createUser();
        user.setStatus(-1);
        when(userDao.findByNick(anyString())).thenReturn(user);
        response = accountServiceImpl.dirtyLogin("nick", "1234");
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "user.account.frozen");

        when(userDao.findByNick(anyString())).thenReturn(createUser());
        response = accountServiceImpl.dirtyLogin("nick", "12345");
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "user.password.incorrect");

        when(userDao.findByNick(anyString())).thenReturn(createUser());
        response = accountServiceImpl.dirtyLogin("nick", "1234");
        assertTrue(response.isSuccess());

        when(userDao.findByNick(anyString())).thenThrow(Exception.class);
        response = accountServiceImpl.dirtyLogin("nick", "1234");
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "user.query.fail");

    }

    @Test
    public void testUserExists() {
        Response<Boolean> response = accountServiceImpl.userExists(null);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "login.nick.empty");

        when(userDao.findByNick(anyString())).thenReturn(createUser());
        response = accountServiceImpl.userExists("nick");
        assertTrue(response.isSuccess());

        when(userDao.findByNick(anyString())).thenThrow(Exception.class);
        response = accountServiceImpl.userExists("nick");
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "user.query.fail");

    }

    @Test
    public void testUpdateStatus() {
        Response<Boolean> response = accountServiceImpl.updateStatus(1L, 1);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "user.status.update.fail");

        response = accountServiceImpl.updateStatus(1L, 1);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "user.status.update.fail");

        when(userDao.findById(anyLong())).thenReturn(createUser());
        when(userDao.update(any(User.class))).thenReturn(true);
        Response<Boolean> r = new Response<Boolean>();
        r.setResult(true);
        when(supplierIndexService.realTimeIndex(anyList(), any(User.SearchStatus.class))).thenReturn(r);
        response = accountServiceImpl.updateStatus(1L, -1);
        assertTrue(response.isSuccess());

        when(userDao.findById(anyLong())).thenReturn(createUser());
        when(userDao.update(any(User.class))).thenReturn(true);
        r = new Response<Boolean>();
        r.setResult(true);
        when(supplierIndexService.realTimeIndex(anyList(), any(User.SearchStatus.class))).thenReturn(r);
        response = accountServiceImpl.updateStatus(1L, 0);
        assertTrue(response.isSuccess());

    }

    @Test
    public void testFindPurchaserById() {
        Response<PurchaserDto> response = accountServiceImpl.findPurchaserById(null);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "user.id.not.null.fail");

        when(userDao.findById(anyLong())).thenReturn(null);
        response = accountServiceImpl.findPurchaserById(1L);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "user.not.found");

        when(userDao.findById(anyLong())).thenReturn(createUser());
        when(purchaserExtraDao.findByUserId(anyLong())).thenReturn(createPurchaserExtra());
        response = accountServiceImpl.findPurchaserById(2L);
        assertTrue(response.isSuccess());

        when(userDao.findById(anyLong())).thenReturn(createUser());
        when(purchaserExtraDao.findByUserId(anyLong())).thenReturn(null);
        response = accountServiceImpl.findPurchaserById(2L);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "purchaser.extra.not.found");

        when(userDao.findById(anyLong())).thenReturn(createUser());
        when(userDao.findById(anyLong())).thenReturn(createUser());
        when(purchaserExtraDao.findByUserId(anyLong())).thenThrow(Exception.class);
        response = accountServiceImpl.findPurchaserById(2L);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "purchaser.query.fail");
    }

    @Test
    public void testApproveSupplier() {
        Response<Boolean> response = accountServiceImpl.approveSupplier(loginer,null);
        assertFalse(response.isSuccess());

        SupplierApproveDto supplierApproveDto=new SupplierApproveDto();
        response = accountServiceImpl.approveSupplier(loginer,supplierApproveDto);
        assertFalse(response.isSuccess());

        supplierApproveDto.setUserId(1L);
        response = accountServiceImpl.approveSupplier(loginer,supplierApproveDto);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "approve.operation.not.null");

        supplierApproveDto.setOperation(1);
        response = accountServiceImpl.approveSupplier(loginer,supplierApproveDto);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "user.not.found");

        when(accountManager.approveSupplier(supplierApproveDto)).thenReturn(createApproveResult());
        when(userDao.findById(anyLong())).thenReturn(createUser());
        when(supplierIndexService.realTimeIndex(anyList(), any(User.SearchStatus.class))).thenReturn(new Response<Boolean>());
        response = accountServiceImpl.approveSupplier(new BaseUser(), supplierApproveDto);
        assertTrue(response.isSuccess());

    }

    @Test
    public void testNeedCommitPaperwork() {
        Response<Boolean> response = accountServiceImpl.needCommitPaperwork(new BaseUser());
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "user.not.found");

        User user = createUser();
        when(userDao.findById(anyLong())).thenReturn(user);
        response = accountServiceImpl.needCommitPaperwork(new BaseUser());
        assertTrue(response.isSuccess());

        user = createUser();
        user.setStatus(User.Status.OK.value());
        user.setType(User.Type.SUPPLIER.value());
        user.setApproveStatus(User.ApproveStatus.INIT.value());
        when(userDao.findById(anyLong())).thenReturn(user);
        response = accountServiceImpl.needCommitPaperwork(new BaseUser());
        assertTrue(response.isSuccess());


        when(userDao.findById(anyLong())).thenThrow(Exception.class);
        response = accountServiceImpl.needCommitPaperwork(new BaseUser());
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "check.supplier.need.commit.paperwork.fail");

    }

    @Test
    public void testInSupplierConsole() {
        loginer.setId(null);
        Response<Boolean> response = accountServiceImpl.inSupplierConsole(loginer);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "user.id.not.null.fail");

        loginer.setId(1L);
        response = accountServiceImpl.inSupplierConsole(loginer);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "user.not.found");

        User user = createUser();
        loginer.setId(1L);
        user.setStatus(User.Status.FROZEN.value());
        when(userDao.findById(anyLong())).thenReturn(user);
        response = accountServiceImpl.inSupplierConsole(loginer);
        assertTrue(response.isSuccess());

        user = createUser();
        loginer.setId(1L);
        user.setStatus(User.Status.OK.value());
        when(userDao.findById(anyLong())).thenReturn(user);
        response = accountServiceImpl.inSupplierConsole(loginer);
        assertTrue(response.isSuccess());

        user = createUser();
        loginer.setId(1L);
        user.setStatus(User.Status.OK.value());
        when(userDao.findById(anyLong())).thenThrow(Exception.class);
        response = accountServiceImpl.inSupplierConsole(loginer);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "check.supplier.can.in.console.fail");
    }

    @Test
    public void testCreateSupplier() {
        RichSupplierDto richSupplierDto = createRichSupplierDto();
        richSupplierDto.getUser().setNick("nick");
        richSupplierDto.getUser().setEmail("12312");
        richSupplierDto.getCompany().setCorporation("1212");
        richSupplierDto.getCompany().setInitAgent("1212");
        richSupplierDto.getCompany().setDesc("1212");
        richSupplierDto.getCompany().setRegCountry(1);
        richSupplierDto.getCompany().setCustomers("1212");

        when(accountManager.createSupplier(any(RichSupplierDto.class))).thenReturn(1L);
        Response<Long> response = accountServiceImpl.createSupplier(richSupplierDto);
        assertTrue(response.isSuccess());

        when(accountManager.createSupplier(any(RichSupplierDto.class))).thenThrow(Exception.class);
        response = accountServiceImpl.createSupplier(richSupplierDto);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "create.supplier.fail");

    }

    @Test
    public void testCreateBizSupplier() {
        RichSupplierDto richSupplierDto = createRichSupplierDto();
        richSupplierDto.getUser().setNick("nick");
        richSupplierDto.getUser().setEmail("12312");
        richSupplierDto.getCompany().setCorporation("1212");
        richSupplierDto.getCompany().setInitAgent("1212");
        richSupplierDto.getCompany().setDesc("1212");
        richSupplierDto.getCompany().setRegCountry(1);
        richSupplierDto.getCompany().setCustomers("1212");

        when(accountManager.createSupplier(any(RichSupplierDto.class))).thenReturn(1L);
        Response<Long> response = accountServiceImpl.createBizSupplier(richSupplierDto);
        assertTrue(response.isSuccess());

        when(accountManager.createSupplier(any(RichSupplierDto.class))).thenThrow(Exception.class);
        response = accountServiceImpl.createBizSupplier(richSupplierDto);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "create.supplier.fail");
    }

    @Test
    public void testFindStaffByWorkNo() {
        Response<TeamMemeberDto> response = accountServiceImpl.findStaffByWorkNo("");
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "not.implements");

    }

    @Test
    public void testFindStaffByName() {
        Response<List<TeamMemeberDto>> response = accountServiceImpl.findStaffByName("");
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "not.implements");

    }

    private RichSupplierDto createRichSupplierDto() {
        RichSupplierDto richSupplierDto = new RichSupplierDto();
        richSupplierDto.setUser(createUser());
        richSupplierDto.getUser().setNick("nick");
        richSupplierDto.getUser().setEmail("12312");
        richSupplierDto.setCompany(createCompany());
        richSupplierDto.setCompanyMainBusinesses(getCompanyMainUsinesses());
        richSupplierDto.setCompanySupplyParks(getCompanySupplyParks());
        richSupplierDto.setContactInfo(getContactInfo());
        return richSupplierDto;
    }

    private Company createCompany() {
        Company company = new Company();
        company.setId(1L);
        company.setCorporation("1212");
        company.setInitAgent("1212");
        company.setDesc("1212");
        company.setRegCountry(1);
        company.setCustomers("1212");
        return company;
    }

    private List<CompanyMainBusiness> getCompanyMainUsinesses() {
        List<CompanyMainBusiness> companyMainBusinesses = Lists.newArrayList();
        CompanyMainBusiness companyMainBusiness = new CompanyMainBusiness();
        companyMainBusiness.setFirstLevelId(2L);
        companyMainBusiness.setMainBusinessId(1L);
        companyMainBusiness.setCompanyId(1L);
        companyMainBusiness.setUserId(loginer.getId());
        companyMainBusiness.setName("冰箱");

        companyMainBusinesses.add(companyMainBusiness);
        return companyMainBusinesses;

    }

    private List<CompanySupplyPark> getCompanySupplyParks() {
        List<CompanySupplyPark> companySupplyParks = Lists.newArrayList();
        CompanySupplyPark companySupplyPark = new CompanySupplyPark();
        companySupplyPark.setSupplyParkId(1L);
        companySupplyPark.setCompanyId(1L);
        companySupplyPark.setUserId(loginer.getId());
        companySupplyPark.setName("1121212");
        companySupplyParks.add(companySupplyPark);
        return companySupplyParks;
    }

    private ContactInfo getContactInfo() {
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setName("baby");
        contactInfo.setMobile("18969973090");
        contactInfo.setCompanyId(1L);
        contactInfo.setUserId(loginer.getId());
        contactInfo.setDepartment("development");
        contactInfo.setDuty("leader");
        contactInfo.setEmail("8g2@163.com");
        contactInfo.setOfficePhone("99120");
        return contactInfo;
    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setName("zhangsan");
        user.setNickName("jack");
        user.setMobile("18969971111");
        user.setEncryptedPassword("1127@a7dbc531dc91d8408c72");
        user.setType(2);
        return user;
    }

    private PurchaserExtra createPurchaserExtra() {
        PurchaserExtra purchaserExtra = new PurchaserExtra();
        purchaserExtra.setUserId(1L);
        purchaserExtra.setEmployeeId("235353");
        purchaserExtra.setDepartment("财务部");
        purchaserExtra.setLeader("刘德华");
        purchaserExtra.setPosition("经理");
        return purchaserExtra;
    }

    private ApproveEvent createApproveResult() {
        ApproveEvent approveEvent = new ApproveEvent();
        approveEvent.setApproveResult(ApproveEvent.ApproveResult.ENTER_FAIL);
        return approveEvent;
    }
}
