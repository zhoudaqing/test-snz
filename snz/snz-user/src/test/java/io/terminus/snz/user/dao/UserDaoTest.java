package io.terminus.snz.user.dao;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.terminus.common.model.Paging;
import io.terminus.common.utils.JsonMapper;
import io.terminus.snz.user.dto.BizCreateSupplierDto;
import io.terminus.snz.user.model.User;
import io.terminus.snz.user.tool.PasswordUtil;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Description：
 * Author：Guo Chaopeng
 * Created on 14-5-3-下午2:44
 */
public class UserDaoTest extends TestBaseDao {

    private User user;

    @Autowired
    private UserDao userDao;

    private void mock() {
        user = new User();
        user.setOrigin(User.Origin.NORMAL.value());
        user.setEmail("123@qq.com");
        user.setEncryptedPassword("ewfdf543646");
        user.setMobile("18969973054");
        user.setPhone("110");
        user.setName("jack");
        user.setNick("aaa");
        user.setRoleStr("supplier_init");
        user.setType(User.Type.SUPPLIER.value());
        //user.setAccountType(User.AccountType.ENTERPRISE.value());
        user.setStatus(User.Status.OK.value());
        user.setApproveStatus(User.ApproveStatus.ENTER_WAITING_FOR_APPROVE.value());
        user.setRefuseStatus(User.RefuseStatus.NOT_REFUSED.value());
        user.setTags("fsds");
        user.setQualifyStatus(User.QualifyStatus.QUALIFIED.value());
        user.setEnterPassAt(new Date());
        user.setStep(User.Step.COMPLETE_SUPPLIER.value());
        user.setCreatedAt(DateTime.now().minusDays(1).toDate());
        user.setLastSubmitApprovalAt(new Date());
    }

    private User mockUser() {
        User user = new User();
        user.setOrigin(User.Origin.NORMAL.value());
        user.setEmail("123@qq.com");
        user.setEncryptedPassword("ewfdf543646");
        user.setMobile("18969973054");
        user.setPhone("110");
        user.setName("jack");
        user.setNick("aaa");
        user.setRoleStr("supplier_init");
        user.setType(User.Type.SUPPLIER.value());
        //user.setAccountType(User.AccountType.ENTERPRISE.value());
        user.setStatus(User.Status.OK.value());
        user.setApproveStatus(User.ApproveStatus.ENTER_WAITING_FOR_APPROVE.value());
        user.setRefuseStatus(User.RefuseStatus.NOT_REFUSED.value());
        user.setTags("fsds");
        user.setQualifyStatus(User.QualifyStatus.QUALIFIED.value());
        return user;
    }

    @Before
    public void setUp() {
        mock();
        userDao.create(user);
    }

    @Test
    public void testCreate() {
        Assert.assertNotNull(user.getId());
    }

    @Test
    public void testFindById() {
        User model = userDao.findById(user.getId());
        Assert.assertTrue(model.getId() == user.getId());
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = new ArrayList<Long>() {
            private static final long serialVersionUID = 8767617103719222762L;

            {
                this.add(1l);
                this.add(2l);
            }
        };
        Assert.assertNotNull(userDao.findByIds(ids));
    }

    @Test
    public void testFindByRoleStr() {
        // roleStr: supplier_init
        String role = "supplier_init";
        for (int i=0; i<role.length(); ++i) {
            for (int j=i+1; j<=role.length(); ++j) {
                // System.out.println(role.substring(i, j));
                Assert.assertFalse(userDao.findByRoleStr(role.substring(i, j))
                        .isEmpty());
            }
        }

        Assert.assertTrue(userDao.findByRoleStr("spplier_ini").isEmpty());
    }

    @Test
    public void testFindBy() {
        Map<String, Object> params = Maps.newHashMap();
        params.put("type", null);
        params.put("approveStatus", User.ApproveStatus.ENTER_WAITING_FOR_APPROVE.value());
        params.put("refuseStatus", user.getRefuseStatus());
        params.put("nick", user.getNick());
        List<User> users = userDao.findBy(params, 0, 20).getData();
        Assert.assertEquals(users.size(), 1);
    }

    @Test
    public void testCountBy() {
        Long count = userDao.countBy(user.getType(), null, null, null);
        Assert.assertEquals(count.intValue(), 1);
    }

    @Test
    public void testFindQualifyingBy() {
        User u = mockUser();
        u.setNick("abcdefgfff");
        u.setQualifyStatus(1);
        u.setStatus(1);
        userDao.create(u);
        List<User> users = userDao.findQualifyingBy(1, u.getNick(), 0, 20).getData();
        Assert.assertEquals(1, users.size());
    }

    @Test
    public void testDelete() {
        userDao.delete(user.getId());
        User model = userDao.findById(user.getId());
        Assert.assertNull(model);
    }

    @Test
    public void testUpdate() {
        User updatedUser = new User();
        updatedUser.setId(user.getId());
        updatedUser.setOrigin(User.Origin.BIZ.value());
        updatedUser.setName("guochao");
        //updatedUser.setAccountType(User.AccountType.INDIVIDUAL.value());
        updatedUser.setType(User.Type.ADMIN.value());
        updatedUser.setApproveStatus(User.ApproveStatus.OK.value());
        updatedUser.setRefuseStatus(User.RefuseStatus.IS_REFUSED.value());
        updatedUser.setQualifyStatus(User.QualifyStatus.QUALIFY_FAILED.value());
        updatedUser.setTags("ttt");
        updatedUser.setEnterPassAt(new Date());
        updatedUser.setStep(User.Step.DIE_OUT.value());
        updatedUser.setLastSubmitApprovalAt(new Date());
        userDao.update(updatedUser);

        User model = userDao.findById(user.getId());
        Assert.assertTrue(model.getName().equals(updatedUser.getName()));
        Assert.assertEquals(model.getOrigin(), updatedUser.getOrigin());
        Assert.assertEquals(model.getTags(), updatedUser.getTags());
        Assert.assertTrue(model.getApproveStatus().intValue() == updatedUser.getApproveStatus().intValue());
        Assert.assertTrue(model.getRefuseStatus().intValue() == updatedUser.getRefuseStatus().intValue());
        Assert.assertTrue(model.getQualifyStatus().intValue() == updatedUser.getQualifyStatus().intValue());
        Assert.assertTrue(model.getRefuseStatus().intValue() == updatedUser.getRefuseStatus().intValue());
        Assert.assertTrue(model.getStep().intValue() == updatedUser.getStep().intValue());

    }

    @Test
    public void encryptedPassword() {
        String password = "123456";

        System.out.println(PasswordUtil.encryptPassword(password));
    }

    @Test
    public void mockBizSupplier() {
        BizCreateSupplierDto bizCreateSupplierDto = new BizCreateSupplierDto();
        bizCreateSupplierDto.setAccountType(1);
        bizCreateSupplierDto.setNick("百卓测试");
        bizCreateSupplierDto.setPassword("123456");
        bizCreateSupplierDto.setName("张三");
        bizCreateSupplierDto.setEmail("e@qq.com");
        bizCreateSupplierDto.setMobile("18969973054");
        bizCreateSupplierDto.setPhone("34456");
        bizCreateSupplierDto.setDepartment("研发");
        bizCreateSupplierDto.setDuty("经理");
        bizCreateSupplierDto.setCorporation("海尔");
        bizCreateSupplierDto.setInitAgent("李四");
        bizCreateSupplierDto.setDesc("hahaa");
        bizCreateSupplierDto.setRegCountry(1);
        //bizCreateSupplierDto.setProductLine(2);
        bizCreateSupplierDto.setCustomers("tt");
        bizCreateSupplierDto.setBusinessLicense("/img/bl.jpg");
        bizCreateSupplierDto.setBusinessLicenseId("123");
        //bizCreateSupplierDto.setBlDate(new Date());
        bizCreateSupplierDto.setOrgCert("/img/oc.jpg");
        bizCreateSupplierDto.setOrgCertId("3445");
        //bizCreateSupplierDto.setOcDate(new Date());
        bizCreateSupplierDto.setTaxNo("/img/tn.jpg");
        bizCreateSupplierDto.setTaxNoId("567");
        //bizCreateSupplierDto.setTnDate(new Date());
        bizCreateSupplierDto.setMainBusinessIds("1,2,3");
        bizCreateSupplierDto.setSupplyParkIds("1,2");

        System.out.println(JsonMapper.nonEmptyMapper().toJson(bizCreateSupplierDto));
    }

    @Test
    public void testCountQualifiedSupplier() {
        Long count = userDao.countQualifiedSupplier();
        Assert.assertEquals(1, count.intValue());
    }

    @Test
    public void testCountByApproveAndUserIds() {
        Long count = userDao.countByApproveAndUserIds(user.getType(), user.getApproveStatus(), user.getNick(), Lists.newArrayList(user.getId()));
        Assert.assertEquals(1, count.intValue());
    }

    @Test
    public void testFindByApproveAndUserIds() {
        Paging<User> paging = userDao.findByApproveAndUserIds(user.getType(), user.getApproveStatus(), user.getNick(), Lists.newArrayList(user.getId()), 0, 30);
        Assert.assertEquals(1, paging.getTotal().intValue());
        Assert.assertEquals(paging.getData().get(0).getNick(), user.getNick());
    }

    @Test
    public void testCountEnterAndModifyRegisterInfoByUserIds() {
        Long count = userDao.countApprovingSupplierByUserIds(user.getNick(), Lists.newArrayList(user.getId()));
        Assert.assertEquals(1, count.intValue());
    }

    @Test
    public void testFindEnterAndModifyRegisterInfoByUserIds() {
        Paging<User> paging = userDao.findApprovingSupplierByUserIds(user.getNick(), Lists.newArrayList(user.getId()), 0, 30);
        Assert.assertEquals(1, paging.getTotal().intValue());
        Assert.assertEquals(paging.getData().get(0).getNick(), user.getNick());
    }

    @Test
    public void testCountLastEnterPassByUserIds() {
        Long count = userDao.countLastEnterPassByUserIds(7, user.getNick(), Lists.newArrayList(user.getId()));
        Assert.assertEquals(1, count.intValue());
    }

    @Test
    public void testFindLastEnterPassByUserIds() {
        Paging<User> paging = userDao.findLastEnterPassByUserIds(7, user.getNick(), Lists.newArrayList(user.getId()), 0, 30);
        Assert.assertEquals(1, paging.getTotal().intValue());
        Assert.assertEquals(paging.getData().get(0).getNick(), user.getNick());
    }

    @Test
    public void test() {
        String s = "2014-04";
        System.out.println(s);
        System.out.println(DateTimeFormat.forPattern("yyyy-MM").parseDateTime(s).toDate());
    }

    @Test
    public void testCountEnterPassSupplier() {
        Long count = userDao.countEnterPassSupplier(user.getNick(), null);
        Assert.assertEquals(0, count.intValue());
    }

    @Test
    public void testFindEnterPassSupplier() {
        List<User> users = userDao.findEnterPassSupplier(user.getNick(), Lists.newArrayList(user.getId()), 0, 30).getData();
        Assert.assertEquals(0, users.size());
    }

    @Test
    public void testCountSupplierByCreatedAtAndStepAndIds() {
        Long count = userDao.countSupplierByCreatedAtAndStepAndIds(DateTime.now().minusDays(2).toDate(), new Date(), user.getStep(), Arrays.asList(user.getId()));
        Assert.assertEquals(1, count.intValue());
    }

    @Test
    public void testFindSupplierByCreatedAtAndStepAndIds() {
        List<User> users = userDao.findSupplierByCreatedAtAndStepAndIds(DateTime.now().minusDays(2).toDate(), new Date(), user.getStep(), Arrays.asList(user.getId()), 0, 30).getData();
        Assert.assertEquals(1, users.size());
    }

    @Test
    public void testFindApprovingUserIdsWithSubmitAt() {
        List<Long> userIds = userDao.findApprovingUserIdsWithSubmitAt(new DateTime().plusDays(1).toDate());
        Assert.assertEquals(1, userIds.size());
    }

}
