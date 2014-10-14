package io.terminus.snz.user.dao;

import io.terminus.snz.user.model.CompanyRank;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Description：
 * Author：Guo Chaopeng
 * Created on 14-5-3-下午8:27
 */
public class CompanyRankTest extends TestBaseDao {

    private CompanyRank companyRank;

    @Autowired
    private CompanyRankDao companyRankDao;

    private void mock() {
        companyRank = new CompanyRank();
        companyRank.setUserId(1L);
        companyRank.setCompanyId(1L);
        companyRank.setInRank(10);
        companyRank.setInRankFile("/file/ds32.doc");
        companyRank.setInRankFileName("aa.doc");
        companyRank.setInRankOrg("国家商务部");
        companyRank.setOutRank(17);
        companyRank.setOutRankFile("/file/dff.doc");
        companyRank.setOutRankFileName("bb.doc");
        companyRank.setInRankOrg("国际商务部");
    }

    @Before
    public void setUp() {
        mock();
        companyRankDao.create(companyRank);
    }

    @Test
    public void testCreate() {
        Assert.assertNotNull(companyRank.getId());
    }

    @Test
    public void testFindById() {
        CompanyRank model = companyRankDao.findById(companyRank.getId());
        Assert.assertEquals(model.getId(), companyRank.getId());
    }

    @Test
    public void testFindByUserId() {
        CompanyRank model = companyRankDao.findByUserId(companyRank.getUserId());
        Assert.assertEquals(model.getId(), companyRank.getId());
    }

    @Test
    public void testDelete() {
        companyRankDao.delete(companyRank.getId());
        CompanyRank model = companyRankDao.findById(companyRank.getId());
        Assert.assertNull(model);
    }

    @Test
    public void testUpdate() {
        CompanyRank updatedModel = new CompanyRank();
        updatedModel.setId(companyRank.getId());
        updatedModel.setInRank(3);
        updatedModel.setInRankFileName("dd.doc");
        updatedModel.setOutRankFileName("ee.doc");
        companyRankDao.update(updatedModel);

        CompanyRank model = companyRankDao.findById(companyRank.getId());
        Assert.assertEquals(model.getInRank(), updatedModel.getInRank());
        Assert.assertEquals(model.getInRankFileName(), updatedModel.getInRankFileName());
        Assert.assertEquals(model.getOutRankFileName(), updatedModel.getOutRankFileName());


    }

}
