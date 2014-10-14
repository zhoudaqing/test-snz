package io.terminus.snz.user.dao;

import io.terminus.snz.user.model.PurchaserAuthority;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class PurchaserAuthorityDaoTest extends TestBaseDao {

    @Autowired
    private PurchaserAuthorityDao purchaserAuthorityDao;

    PurchaserAuthority getAuthority(Long userId, Integer type, String content, String richContent, Integer position, String role) {
        PurchaserAuthority one = new PurchaserAuthority();
        one.setUserId(userId);
        one.setType(type);
        one.setContent(content);
        one.setRichContent(richContent);
        one.setPosition(position);
        one.setRole(role);
        return one;
    }

    @Test
    public void testCreate() throws Exception {
        PurchaserAuthority neo = getAuthority(1L, 1, "123", "", 1, "resource");
        assertTrue(purchaserAuthorityDao.create(neo) > 0);

        PurchaserAuthority exist = purchaserAuthorityDao.findById(neo.getId());
        assertNotNull(exist);
        assertThat(exist.getUserId(), is(1L));
        assertThat(exist.getType(), is(1));
        assertThat(exist.getContent(), is("123"));
        assertThat(exist.getRichContent(), is(""));
        assertThat(exist.getRole(), is("resource"));
    }

    @Test
    public void testDelete() {
        PurchaserAuthority neo = getAuthority(1L, 1, "123", "", 1, "resource");
        assertTrue(purchaserAuthorityDao.create(neo) > 0);

        PurchaserAuthority exist = purchaserAuthorityDao.findById(neo.getId());
        assertNotNull(exist);

        purchaserAuthorityDao.delete(neo.getId());
        exist = purchaserAuthorityDao.findById(neo.getId());
        assertNull(exist);
    }

    @Test
    public void testUpdate() throws Exception {
        PurchaserAuthority neo = getAuthority(1L, 1, "123", "", 1, "resource");
        assertTrue(purchaserAuthorityDao.create(neo) > 0);

        PurchaserAuthority updateOne = getAuthority(2L, 2, "321", ",.", 1, "big_data");
        updateOne.setId(neo.getId());
        assertTrue(purchaserAuthorityDao.update(updateOne));

        PurchaserAuthority exist = purchaserAuthorityDao.findById(neo.getId());
        assertNotNull(exist);
        assertThat(exist.getUserId(), is(2L));
        assertThat(exist.getType(), is(2));
        assertThat(exist.getContent(), is("321"));
        assertThat(exist.getRichContent(), is(",."));
        assertThat(exist.getRole(), is("big_data"));
    }

    @Test
    public void testFindBy() throws Exception {
        PurchaserAuthority neo = getAuthority(1L, 1, "123", "", 1, "resource");
        PurchaserAuthority neo2 = getAuthority(2L, 2, "321", ",.", 1, "resource");
        assertTrue(purchaserAuthorityDao.create(neo) > 0);
        assertTrue(purchaserAuthorityDao.create(neo2) > 0);

        PurchaserAuthority params = new PurchaserAuthority();
        params.setRole("resource");
        List<PurchaserAuthority> exists = purchaserAuthorityDao.findBy(params);
        assertNotNull(exists);
        assertFalse(exists.isEmpty());
        assertTrue(exists.size() == 2);
        assertThat(exists.get(0).getContent(), is("123"));
        assertThat(exists.get(1).getContent(), is("321"));
    }

    @Test
    public void testFindByUserId() throws Exception {
        PurchaserAuthority neo = getAuthority(1L, 1, "123", "", 1, "resource");
        PurchaserAuthority neo2 = getAuthority(1L, 2, "321", ",.", 1, "resource");
        assertTrue(purchaserAuthorityDao.create(neo) > 0);
        assertTrue(purchaserAuthorityDao.create(neo2) > 0);

        PurchaserAuthority params = new PurchaserAuthority();
        params.setUserId(1L);
        List<PurchaserAuthority> exists = purchaserAuthorityDao.findBy(params);
        assertNotNull(exists);
        assertFalse(exists.isEmpty());
        assertTrue(exists.size() == 2);
        assertThat(exists.get(0).getContent(), is("123"));
        assertThat(exists.get(1).getContent(), is("321"));
    }
}