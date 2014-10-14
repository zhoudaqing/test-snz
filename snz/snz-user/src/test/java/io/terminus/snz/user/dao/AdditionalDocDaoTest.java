package io.terminus.snz.user.dao;

import io.terminus.snz.user.model.AdditionalDoc;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Description：
 * Author：Guo Chaopeng
 * Created on 14-5-3-下午9:50
 */
public class AdditionalDocDaoTest extends TestBaseDao {

    private AdditionalDoc additionalDoc;

    @Autowired
    private AdditionalDocDao additionalDocDao;

    private void mock() {
        additionalDoc = new AdditionalDoc();
        additionalDoc.setUserId(2L);
        additionalDoc.setFinanceId(1L);
        additionalDoc.setComment("");
        additionalDoc.setName("财务报告");
        additionalDoc.setFiles("/file/dfs.doc");
    }

    @Before
    public void setUp() {
        mock();
        additionalDocDao.create(additionalDoc);
    }

    @Test
    public void testCreate() {
        Assert.assertNotNull(additionalDoc.getId());
    }

    @Test
    public void testFindById() {
        AdditionalDoc model = additionalDocDao.findById(additionalDoc.getId());
        Assert.assertEquals(model.getId(), additionalDoc.getId());

    }

    @Test
    public void testFindByFinanceId() {
        List<AdditionalDoc> additionalDocs = additionalDocDao.findByFinanceId(additionalDoc.getFinanceId());
        Assert.assertEquals(additionalDocs.get(0).getId(), additionalDoc.getId());
    }

    @Test
    public void testDelete() {
        additionalDocDao.delete(additionalDoc.getId());
        AdditionalDoc model = additionalDocDao.findById(additionalDoc.getId());
        Assert.assertNull(model);
    }

    @Test
    public void testUpdate() {
        AdditionalDoc updatedModel = new AdditionalDoc();
        updatedModel.setId(additionalDoc.getId());
        updatedModel.setName("财务总结");
        additionalDocDao.update(updatedModel);

        AdditionalDoc model = additionalDocDao.findById(additionalDoc.getId());
        Assert.assertEquals(model.getName(), updatedModel.getName());
    }

}
