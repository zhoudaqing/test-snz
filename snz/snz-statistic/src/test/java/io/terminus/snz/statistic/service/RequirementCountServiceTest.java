package io.terminus.snz.statistic.service;

import com.google.common.collect.ImmutableMap;
import io.terminus.pampas.common.BaseUser;
import io.terminus.snz.statistic.dao.PurchaserRequirementCountDao;
import io.terminus.snz.statistic.dao.RequirementCountDao;
import io.terminus.snz.statistic.model.PurchaserRequirementCount;
import io.terminus.snz.statistic.model.RequirementCountType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class RequirementCountServiceTest {
    BaseUser user = null;

    @Before
    public void init(){
        user = new BaseUser();
        user.setId(1l);
        user.setName("Michale");
        user.setMobile("18657327206");

        MockitoAnnotations.initMocks(this);
    }

    @Mock
    private RequirementCountDao requirementCountDao;

    @Mock
    private PurchaserRequirementCountDao purchaserRequirementCountDao;

    @InjectMocks
    private RequirementCountServiceImpl requirementCountService;

    @Test
    public void testSetReqCountInfo() throws Exception {
        requirementCountDao.setReqCount(1l, RequirementCountType.ANSWER_SU, 1);
        assertNotNull(requirementCountService.setReqCountInfo(null, RequirementCountType.ANSWER_SU, 1));
        assertNotNull(requirementCountService.setReqCountInfo(1l, RequirementCountType.ANSWER_SU, 1));
    }

    @Test
    public void testIncTopicSuppliers() throws Exception {
        requirementCountDao.incTopicSuppliers(1l, 1l);
        assertNotNull(requirementCountService.incTopicSuppliers(null, 1l));
        assertNotNull(requirementCountService.incTopicSuppliers(1l , null));
        assertNotNull(requirementCountService.incTopicSuppliers(1l , 1l));
    }

    @Test
    public void testFindReqCount() throws Exception {
        requirementCountDao.findReqCount(1l, new String[]{"send_so", "answer_su"});
        assertNotNull(requirementCountService.findReqCount(1l, new RequirementCountType[]{RequirementCountType.SEND_SO, RequirementCountType.ANSWER_SU}));
    }

    @Test
    public void testDeleteReqCount() throws Exception {
        requirementCountDao.deleteReqCount(1l);
        assertNotNull(requirementCountService.deleteReqCount(null));
        assertNotNull(requirementCountService.deleteReqCount(1l));
    }

    @Test
    public void testSetPurchaserReqCount() throws Exception {
        purchaserRequirementCountDao.setPurCount(mock());
        assertNotNull(requirementCountService.setPurchaserReqCount(mock()));
    }

    @Test
    public void testUpdatePurchaserReqCount() throws Exception {
        purchaserRequirementCountDao.updateReqCount(1l, 1, 2);
        assertNotNull(requirementCountService.updatePurchaserReqCount(null , 1, 2));
        assertNotNull(requirementCountService.updatePurchaserReqCount(1l , 1, 2));
    }

    @Test
    public void testFindPurchaserReqCount() throws Exception {
        when(purchaserRequirementCountDao.findReqCount(1l)).thenReturn(mock());
        assertNotNull(requirementCountService.findPurchaserReqCount(null));
        assertNotNull(requirementCountService.findPurchaserReqCount(user));
    }

    public PurchaserRequirementCount mock(){
        PurchaserRequirementCount purchaserRequirementCount = new PurchaserRequirementCount();
        purchaserRequirementCount.setUserId(1l);
        purchaserRequirementCount.setUserName("name");
        purchaserRequirementCount.setStatusCounts(ImmutableMap.of(1, 1, 2, 1));

        return purchaserRequirementCount;
    }
}