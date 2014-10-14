package io.terminus.snz.requirement.dao;

import com.google.common.collect.Lists;
import io.terminus.snz.requirement.model.ReqPredictTime;
import io.terminus.snz.requirement.model.RequirementStatus;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertNotNull;

public class ReqPredictTimeDaoTest extends BasicTest {

    @Autowired
    private ReqPredictTimeDao reqPredictTimeDao;

    @Test
    public void testCreate() throws Exception {
        assertNotNull(reqPredictTimeDao.create(mock()));
    }

    @Test
    public void testCreateBatch() throws Exception {
        List<ReqPredictTime> reqPredictTimeList = Lists.newArrayList();

        for(int i=0; i<10; i++){
            reqPredictTimeList.add(mock());
        }

        assertNotNull(reqPredictTimeDao.createBatch(reqPredictTimeList));
    }

    @Test
    public void testUpdate() throws Exception {
        assertNotNull(reqPredictTimeDao.update(mock()));
    }

    @Test
    public void testFindById() throws Exception {
        assertNotNull(reqPredictTimeDao.findById(1l));
    }

    @Test
    public void testFindByStatus() throws Exception {
        assertNotNull(reqPredictTimeDao.findByStatus(1l , RequirementStatus.RES_INTERACTIVE.value()));
    }

    @Test
    public void testFindByStatues() throws Exception {
        assertNotNull(reqPredictTimeDao.findByStatus(1l , 1));
    }

    @Test
    public void testFindByRequirementId() throws Exception {
        assertNotNull(reqPredictTimeDao.findByRequirementId(1l));
    }

    @Test
    public void testDelete() throws Exception {
        assertNotNull(reqPredictTimeDao.delete(1l));
    }

    private ReqPredictTime mock(){
        ReqPredictTime reqPredictTime = new ReqPredictTime();
        reqPredictTime.setRequirementId(1l);
        reqPredictTime.setType(1);
        reqPredictTime.setPredictStart(DateTime.now().toDate());
        reqPredictTime.setPredictEnd(DateTime.now().toDate());

        return reqPredictTime;
    }
}