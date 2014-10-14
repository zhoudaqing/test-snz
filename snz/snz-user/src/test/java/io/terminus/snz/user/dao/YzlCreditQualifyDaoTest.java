package io.terminus.snz.user.dao;

import com.google.common.collect.Lists;
import io.terminus.snz.user.model.YzlCreditQualify;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
/**
 * Created by dream on 14-10-14.
 */
public class YzlCreditQualifyDaoTest extends TestBaseDao {

    @Autowired
    YzlCreditQualifyDao yzlCreditQualifyDao;

    private YzlCreditQualify yzlCreditQualify;

    @Before
    public void setUp() {
        yzlCreditQualify = new YzlCreditQualify();
        yzlCreditQualify.setUserId(1l);
        yzlCreditQualify.setSupplierId(1l);
        yzlCreditQualify.setStatus(1);
        yzlCreditQualify.setMessage("message");
        yzlCreditQualifyDao.create(yzlCreditQualify);
        assertNotNull(yzlCreditQualify.getId());
    }

    @Test
    public void shouleSelectByStatus() {
        Integer status = 1;
        List<YzlCreditQualify> yzlCreditQualifies = yzlCreditQualifyDao.selectByStatus(status);
        assertNotNull(yzlCreditQualifies);
    }
}
