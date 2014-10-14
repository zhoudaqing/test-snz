package io.terminus.snz.user.dao;

import com.google.common.collect.Lists;
import io.terminus.common.model.Paging;
import io.terminus.snz.user.model.SupplierCreditQualify;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static io.terminus.snz.user.model.SupplierCreditQualify.STATUS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Date: 7/31/14
 * Time: 13:36
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */
public class SupplierCreditQualifyDaoTest extends TestBaseDao {

    @Autowired
    SupplierCreditQualifyDao supplierCreditQualifyDao;

    private SupplierCreditQualify init;

    @Before
    public void setup() {
        init = new SupplierCreditQualify();
        init.setUserId(1l);
        init.setSupplierId(1l);
        init.setStatus(1);
        supplierCreditQualifyDao.create(init);
        assertNotNull(init.getId());
    }

    @Test
    public void shouldFindBy() {
        SupplierCreditQualify found = supplierCreditQualifyDao.findById(init.getId());
        assertNotNull(found);

        SupplierCreditQualify param = new SupplierCreditQualify();
        param.setUserId(1l);
        found = supplierCreditQualifyDao.findBy(param);
        assertNotNull(found);
    }

    @Test
    public void testFindByUserIds(){
        List<Long> userIds = Lists.newArrayList(1l , 2l, 3l);

        List<SupplierCreditQualify> qualifyList = supplierCreditQualifyDao.findByUserIds(userIds);
        assertNotNull(qualifyList);
    }

    @Test
    public void shouldPagingGood() {
        SupplierCreditQualify stub = new SupplierCreditQualify();
        stub.setUserId(1l);
        stub.setSupplierId(1l);
        stub.setStatusEnum(STATUS.APPLYING);
        supplierCreditQualifyDao.create(stub);

        Paging page = supplierCreditQualifyDao.pagingForQualify(null, 0, 20);
        assertEquals(1L, (long)page.getTotal());
    }

    @Test
    public void shouldFindByUserId() {
        SupplierCreditQualify found = supplierCreditQualifyDao.findByUserId(init.getUserId());

        assertNotNull(found);
    }

    @Test
    public void testShouldPagingDelayed() {
        DateTime now = DateTime.now();

        Paging<SupplierCreditQualify> page = supplierCreditQualifyDao.upCommingIn(
                now.minusDays(2).withTimeAtStartOfDay().toDate(),
                now.minusDays(1).withTimeAtStartOfDay().toDate(),
                0, 20);
        assertEquals(1L, (long)page.getTotal());
        assertFalse(page.getData().isEmpty());
    }
}
