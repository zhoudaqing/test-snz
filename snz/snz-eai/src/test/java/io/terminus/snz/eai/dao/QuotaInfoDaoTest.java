package io.terminus.snz.eai.dao;

import io.terminus.snz.eai.model.OutPriceInfo;
import io.terminus.snz.eai.model.OutQuotaInfo;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * Created by luyuzhou on 14-9-4.
 */
public class QuotaInfoDaoTest extends TestBaseDao{

    @Autowired
    QuotaInfoDao quotaInfoDao;

    private OutQuotaInfo init = newOne();

    private OutQuotaInfo newOne(){
        OutQuotaInfo i = new OutQuotaInfo();
        i.setModulenum("0001");
        i.setModulename("xxxxxx");
        i.setFactorynum("9120");
        i.setMatkl("110203");
        i.setSupplierid("V500465");
        i.setSuppliername("yyyyyy");
        i.setQuantity(100l);
        return i;
    }

    private OutPriceInfo init2 = newSecond();

    private OutPriceInfo newSecond(){
        OutPriceInfo j = new OutPriceInfo();
        j.setModulenum("0001");
        j.setModulename("xxxxxx");
        j.setMatkl("110203");
        j.setSupplierid("V500465");
        j.setSuppliername("yyyyyy");
        j.setPurchaseorg("9990");
        j.setPurchasetype("0");
        j.setScale(25l);
        j.setFeeunit(1l);
        j.setPurchaseunit("EA");
        j.setCointype("USD");
        return j;
    }

    @Before
    public void init() {
        quotaInfoDao.create(init);
        assertNotNull(init.getId());
        quotaInfoDao.createother(init2);
        assertNotNull(init2.getId());
    }

    @Test
    public void shouldFindbymatnrall(){
        assertNotNull(quotaInfoDao.findbymatnrall("0001","9120","V500465"));
    }

    @Test
    public void shouldFindbymatnr(){
        assertNotNull(quotaInfoDao.findbymatnr("0001", "V500465"));
    }

    @Test
    public void shouldUpdate(){
        OutQuotaInfo update = newOne();
        quotaInfoDao.create(update);
        update.setQuantity(200l);
        quotaInfoDao.update(update);

        OutQuotaInfo found = quotaInfoDao.findById(update.getId());
        assertEquals(200l, (long)found.getQuantity());
    }

    @Test
    public void shouldUpdateother(){
        OutPriceInfo update = newSecond();
        quotaInfoDao.createother(update);
        update.setScale(100l);
        quotaInfoDao.updateother(update);

        OutPriceInfo found = quotaInfoDao.findByIdother(update.getId());
        assertEquals(100l, (long)found.getScale());
    }


//    private QuotaInfo init = newGet();
//
//    private QuotaInfo newGet() {
//        QuotaInfo i = new QuotaInfo();
//        i.setModulenum("0001");
//        i.setModulename("xxxxxx");
//        i.setFactorynum("9120");
//        i.setMatkl("110203");
//        i.setSupplierid("V500465");
//        i.setSuppliername("yyyyyy");
//        i.setQuantity(100l);
//        i.setPurchaseorg("9990");
//        i.setPurchasetype("0");
//        i.setScale(25l);
//        i.setFeeunit(1l);
//        i.setPurchaseunit("EA");
//        i.setCointype("USD");
//        return i;
//    }
//
//    @Before
//    public void init() {
//        quotaInfoDao.create(init);
//        assertNotNull(init.getId());
//    }
//
//    @Test
//    public void shouldUpdate(){
//        QuotaInfo update = newGet();
//        quotaInfoDao.create(update);
//        update.setQuantity(200l);
//        quotaInfoDao.update(update);
//
//        QuotaInfo found = quotaInfoDao.findById(update.getId());
//        assertEquals(200l, (long)found.getQuantity());
//    }
//
//    @Test
//    public void shouldUpdateother(){
//        QuotaInfo update = newGet();
//        quotaInfoDao.createother(update);
//        update.setScale(100l);
//        quotaInfoDao.updateother(update);
//
//        QuotaInfo found = quotaInfoDao.findById(update.getId());
//        assertEquals(100l, (long)found.getScale());
//    }
//
//    @Test
//    public void shouldFindbymatnr(){
//        assertNotNull(quotaInfoDao.findbymatnr("0001", "V500465"));
//    }
//
//    @Test
//    public void shouldFindbymatnrall(){
//        assertNotNull(quotaInfoDao.findbymatnrall("0001","9120","V500465"));
//    }
}
