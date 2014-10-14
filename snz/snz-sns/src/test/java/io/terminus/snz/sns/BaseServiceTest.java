package io.terminus.snz.sns;

import io.terminus.pampas.common.BaseUser;
import io.terminus.snz.user.model.User;
import org.junit.Before;
import org.mockito.MockitoAnnotations;

public abstract class BaseServiceTest {

    protected BaseUser loginer;
    protected BaseUser supplier;
    protected BaseUser purchaser;

    public BaseServiceTest(){
        loginer = new BaseUser();
        loginer.setId(1L);
        loginer.setName("xxx");
        loginer.setNickName("ooo");
        loginer.setMobile("xxoo");

        supplier = new User();
        supplier.setId(2L);
        supplier.setName("张三");
        supplier.setNickName("zhangsan");
        supplier.setType(User.Type.SUPPLIER.value());

        purchaser = new User();
        purchaser.setId(3L);
        purchaser.setName("李四");
        purchaser.setNickName("lisi");
        purchaser.setType(User.Type.PURCHASER.value());
    }

    @Before
    public void init(){
        // NOTE: necessary
        MockitoAnnotations.initMocks(this);
    }

    protected User mockPurchaser(long id) {
        User u = new User();
        u.setId(id);
        u.setNick("xxx");
        u.setType(User.Type.PURCHASER.value());
        return u;
    }

    protected User mockSupplier(long id) {
        User u = new User();
        u.setId(id);
        u.setNick("xxx");
        u.setType(User.Type.SUPPLIER.value());
        u.setTags("xx标签");
        return u;
    }

    protected User mockAdmin(long id) {
        User u = new User();
        u.setId(id);
        u.setNick("xxx");
        u.setType(User.Type.ADMIN.value());
        return u;
    }
}
