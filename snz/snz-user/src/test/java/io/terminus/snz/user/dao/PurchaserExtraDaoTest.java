package io.terminus.snz.user.dao;

import io.terminus.snz.user.model.PurchaserExtra;
import io.terminus.snz.user.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Description：
 * Author：Guo Chaopeng
 * Created on 14-6-6
 */
public class PurchaserExtraDaoTest extends TestBaseDao {

    private PurchaserExtra purchaserExtra;

    @Autowired
    private PurchaserExtraDao purchaserExtraDao;

    @Autowired
    private UserDao userDao;

    private PurchaserExtra mockExtra() {
        PurchaserExtra purchaserExtra = new PurchaserExtra();
        purchaserExtra.setEmployeeId("235353");
        purchaserExtra.setDepartment("财务部");
        purchaserExtra.setLeader("刘德华");
        purchaserExtra.setPosition("经理");
        return purchaserExtra;
    }

    private void mock() {
        purchaserExtra = new PurchaserExtra();
        purchaserExtra.setUserId(1L);
        purchaserExtra.setEmployeeId("235353");
        purchaserExtra.setDepartment("财务部");
        purchaserExtra.setLeader("刘德华");
        purchaserExtra.setPosition("经理");
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
        user.setEnterPassAt(new Date());
        return user;
    }

    @Before
    public void setUp() {
        mock();
        purchaserExtraDao.create(purchaserExtra);
    }

    @Test
    public void testCreate() {
        Assert.assertNotNull(purchaserExtra.getId());
    }

    @Test
    public void testFindById() {
        PurchaserExtra model = purchaserExtraDao.findById(purchaserExtra.getId());
        Assert.assertTrue(model.getId() == purchaserExtra.getId());
    }

    @Test
    public void testFindByUserId() {
        PurchaserExtra model = purchaserExtraDao.findByUserId(purchaserExtra.getUserId());
        Assert.assertTrue(model.getId() == purchaserExtra.getId());
    }

    @Test
    public void testUpdateByUserId() {
        User user = mockUser();
        userDao.create(user);

        PurchaserExtra extra = mockExtra();
        extra.setUserId(user.getId());
        purchaserExtraDao.create(extra);

        extra.setPosition("abcde");
        purchaserExtraDao.updateByUserId(extra);
    }
}
