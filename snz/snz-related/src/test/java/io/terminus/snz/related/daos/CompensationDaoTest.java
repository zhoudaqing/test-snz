package io.terminus.snz.related.daos;

import com.google.common.collect.Lists;
import io.terminus.common.model.Paging;
import io.terminus.snz.related.BaseDaoTest;
import io.terminus.snz.related.models.Compensation;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-7-12
 */
public class CompensationDaoTest extends BaseDaoTest {

    @Autowired
    private CompensationDao compensationDao;

    @Test
    public void testCreate(){
        Compensation c = mockCompensation();
        assertEquals(new Integer(1), compensationDao.create(c));
    }

    @Test
    public void testCreates(){
        List<Compensation> cs = mockCompensations(23);
        assertEquals(new Integer(cs.size()), compensationDao.creates(cs));
    }

    @Test
    public void testDelete(){
        Compensation c = mockCompensation();
        compensationDao.create(c);
        assertEquals(new Integer(1), compensationDao.delete(c.getId()));
    }

    @Test
    public void testDeletes(){
        List<Compensation> cs = mockCompensations(22);
        for (Compensation c : cs){
            compensationDao.create(c);
        }
        List<Long> ids = Arrays.asList(cs.get(0).getId(), cs.get(3).getId(), 1000000L);
        assertEquals(Integer.valueOf(ids.size() - 1), compensationDao.deletes(ids));
    }

    @Test
    public void testLoad(){
        List<Compensation> cs = mockCompensations(10);
        for (Compensation c : cs){
            compensationDao.create(c);
        }
        assertNotNull(compensationDao.load(cs.get(0).getId()));
        assertNull(compensationDao.load(10000000L));
        List<Long> ids = Arrays.asList(cs.get(0).getId(), cs.get(2).getId(), 13333333L);
        assertEquals(ids.size()-1, compensationDao.loads(ids).size());

    }

    @Test
    public void testList(){
        testCreates();
        List<Compensation> cs = compensationDao.listAll();
        //assertEquals(19, cs.size());

        Compensation criteria = new Compensation();
        criteria.setStatus(Compensation.Status.INIT.value());
        cs = compensationDao.list(criteria);
        //assertEquals(19, cs.size());

        criteria.setStatus(Compensation.Status.ING.value());
        cs = compensationDao.list(criteria);
        //assertEquals(0, cs.size());

    }

    @Test
    public void testPaging(){
        testCreates();
        Paging<Compensation> pc = compensationDao.paging(0, 10);
        //assertEquals(new Long(19), pc.getTotal());
        //assertEquals(10, pc.getData().size());

        Compensation criterias = new Compensation();
        criterias.setStatus(Compensation.Status.INIT.value());
        pc = compensationDao.paging(0, 10, criterias);
        //assertEquals(new Long(19), pc.getTotal());
        //assertEquals(10, pc.getData().size());

        criterias.setStatus(Compensation.Status.ING.value());
        pc = compensationDao.paging(0, 10, criterias);
        //assertEquals(new Long(0), pc.getTotal());
        //assertEquals(0, pc.getData().size());
    }

    @Test
    public void testPagingForPurchaser(){
        testCreates();
        Paging<Compensation> pc = compensationDao.pagingForPurchaser(0, 10, null, "2012-08-21", "2014-08-21", null);
        assertEquals(0, pc.getTotal().intValue());
    }

    private Compensation mockCompensation(){
        Compensation c = new Compensation();
        c.setStatus(Compensation.Status.INIT.value());
        c.setSupplierName("海尔");
        c.setFactoryName("xx");
        c.setFactory("9010");
        c.setIsMoney(1);
        c.setMoney(100 + "");
        c.setPark("xxoo");
        c.setMaterielDesc("ooxx");
        c.setCurrent(new Date());
        c.setProductLine("冰箱");
        return c;
    }

    private List<Compensation> mockCompensations(int count){
        List<Compensation> cs = Lists.newArrayList();
        for (int i=0; i<count; i++){
            cs.add(mockCompensation());
        }
        return cs;
    }

    @Test
    public void testAutoUpdate() {
        DateTime now = DateTime.now();
        compensationDao.autoUpdateStatus(now);
    }

    @Test
    public void testupdate() {
        Compensation first = new Compensation();
        first.setId(1L);
        first.setStatus(1);
        compensationDao.create(first);
        Compensation test = new Compensation();
        test.setId(1L);
        test.setStatus(2);
        assertNotNull(compensationDao.update(test));
        Compensation load = compensationDao.load(1l);
        assertNotNull(load.getStatus());
    }

    @Test
    public void testGetSumOfMonth(){
        Compensation test = mockCompensation();
        compensationDao.create(test);
        Date dateLine = DateTime.now().minusDays(30).toDate();
        List<Compensation> result = Lists.newArrayList();
        result = compensationDao.getSumOfMonth(dateLine);
        assertNotNull(result);

    }
}
