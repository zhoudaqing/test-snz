package io.terminus.snz.user.service;

import com.google.common.collect.Lists;
import io.terminus.search.ESClient;
import io.terminus.snz.user.dao.UserDao;
import io.terminus.snz.user.model.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

/**
 * Created by yangzefeng on 14-8-4
 */
public class SupplierIndexServiceTest {

    @InjectMocks
    private SupplierIndexServiceImpl supplierIndexServiceImpl;

    @Mock
    ESClient esClient;

    @Mock
    UserDao userDao;

    private List<User> users;

    private User user;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        user = new User();
        user.setId(1l);
        user.setAccountType(1);
        user.setName("test");
        user.setApproveStatus(1);
        user.setEmail("email");
        user.setNick("nick");
        users = Lists.newArrayList();
        users.add(user);
    }

    @Test
    public void testFullDump() {
        when(userDao.maxSupplierId()).thenReturn(1l);
        when(userDao.forDump(2l,200)).thenReturn(users);
        assertNotNull(supplierIndexServiceImpl.fullDump());
    }

    @Test
    public void testDeltaDump() {
        when(userDao.maxSupplierId()).thenReturn(1l);
        when(userDao.forDeltaDump(anyLong(), anyString(), anyInt())).thenReturn(users);
        assertNotNull(supplierIndexServiceImpl.deltaDump(15));
    }

    @Test
    public void testRealTimeIndex() {
        when(userDao.findById(anyLong())).thenReturn(user);
        assertNotNull(supplierIndexServiceImpl.realTimeIndex(Lists.newArrayList(1l,2l), User.SearchStatus.INDEX));
    }
}
