package io.terminus.snz.related.daos;

import com.google.common.collect.Lists;
import io.terminus.snz.related.BaseDaoTest;
import io.terminus.snz.related.models.Compensation;
import io.terminus.snz.related.models.CompensationDetail;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Author:  wenhaoli
 * Date: 2014-08-12
 */
public class CompensationDetailDaoTest extends BaseDaoTest {

    @Autowired
    private CompensationDetailDao compensationDetailDao;

    @Test
    public void testCreates() {
        List<CompensationDetail> test = mockCompensationDetails(23);
        assertEquals(new Integer(test.size()), compensationDetailDao.creates(test));
    }

    @Test
    public void testfind() {
        List<CompensationDetail> test = mockCompensationDetails(10);
        for(CompensationDetail te : test){
            compensationDetailDao.create(te);
        }
        assertNotNull(compensationDetailDao.find(test.get(0).getId()));
        assertNull(compensationDetailDao.find(1000l));
    }

    @Test
    public void testfindDetail(){
        List<CompensationDetail> test = mockCompensationDetails(10);
        compensationDetailDao.creates(test);
        List<CompensationDetail> result = compensationDetailDao.findDetail(1L);
        assertFalse(result.isEmpty());
        System.out.print(result);
    }

    @Test
    public void testFindByCompensation(){
        List<CompensationDetail> details = compensationDetailDao.findByCompensation(new Compensation());
        assertEquals(0, details.size());
    }

    private CompensationDetail mockCompensationDetail(){
        CompensationDetail c = new CompensationDetail();
        c.setListId(1L);
        c.setFactory("oo");
        c.setListNum("XXXXXX");
        c.setCurrentDay(new Date());
        return c;
    }

    private List<CompensationDetail> mockCompensationDetails(int count){
        List<CompensationDetail> cs = Lists.newArrayList();
        for (int i=0; i<count; i++){
            cs.add(mockCompensationDetail());
        }
        return cs;
    }
}
