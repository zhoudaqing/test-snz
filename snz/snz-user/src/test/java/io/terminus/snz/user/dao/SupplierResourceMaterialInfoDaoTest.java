package io.terminus.snz.user.dao;

import io.terminus.common.model.Paging;
import io.terminus.snz.user.model.SupplierResourceMaterialInfo;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 8/25/14
 */
public class SupplierResourceMaterialInfoDaoTest extends TestBaseDao {

    @Autowired
    private SupplierResourceMaterialInfoDao supplierResourceMaterialInfoDao;

    SupplierResourceMaterialInfo getNewInfo(Long supplierId, String supplierName, String approvedModuleIds, String notApprovedModuleIds, Long times, Date lastSubmitTime, Integer status) {
        SupplierResourceMaterialInfo info1 = new SupplierResourceMaterialInfo();
        info1.setSupplierId(supplierId);
        info1.setSupplierName(supplierName);
        info1.setApprovedModuleIds(approvedModuleIds);
        info1.setNotApprovedModuleIds(notApprovedModuleIds);
        info1.setTimes(times);
        info1.setLastSubmitTime(lastSubmitTime);
        info1.setStatus(status);
        return info1;
    }

    @Test
    public void testCreate() throws Exception {
        SupplierResourceMaterialInfo info1 = getNewInfo(1L, "公司1", "1,3,5", "7,4,2", 0L, DateTime.now().toDate(), SupplierResourceMaterialInfo.Status.NO_SUBMISSION.value());
        assertTrue(supplierResourceMaterialInfoDao.create(info1) > 0);
        SupplierResourceMaterialInfo exist = supplierResourceMaterialInfoDao.findById(info1.getId());
        assertNotNull(exist);
        assertThat(exist.getApprovedModuleIds(), is("1,3,5"));
        assertThat(exist.getNotApprovedModuleIds(), is("7,4,2"));
        assertThat(exist.getSupplierName(), is("公司1"));
    }

    @Test
    public void testUpdate() throws Exception {
        SupplierResourceMaterialInfo info1 = getNewInfo(1L, "公司1", "1,3,5", "7,4,2", 0L, DateTime.now().toDate(), SupplierResourceMaterialInfo.Status.NO_SUBMISSION.value());
        assertTrue(supplierResourceMaterialInfoDao.create(info1) > 0);
        info1.setSupplierName("公司1，改！");
        info1.setApprovedModuleIds("1,2,3");
        assertTrue(supplierResourceMaterialInfoDao.update(info1));
        SupplierResourceMaterialInfo exist = supplierResourceMaterialInfoDao.findById(info1.getId());
        assertNotNull(exist);
        assertThat(exist.getApprovedModuleIds(), is("1,2,3"));
        assertThat(exist.getNotApprovedModuleIds(), is("7,4,2"));
        assertThat(exist.getSupplierName(), is("公司1，改！"));
    }

    @Test
    public void testFindOneBySupplierId() throws Exception {
        SupplierResourceMaterialInfo info1 = getNewInfo(1L, "公司1", "1,3,5", "7,4,2", 0L, DateTime.now().toDate(), SupplierResourceMaterialInfo.Status.NO_SUBMISSION.value());
        assertTrue(supplierResourceMaterialInfoDao.create(info1) > 0);

        SupplierResourceMaterialInfo exist = supplierResourceMaterialInfoDao.findOneBySupplierId(1L);
        assertNotNull(exist);
        assertThat(exist.getApprovedModuleIds(), is("1,3,5"));
        assertThat(exist.getNotApprovedModuleIds(), is("7,4,2"));

        assertNull(supplierResourceMaterialInfoDao.findOneBySupplierId(0L));
    }

    @Test
    public void testFindBy() throws Exception {
        // 8天前,未提交
        assertTrue(0 < supplierResourceMaterialInfoDao.create(getNewInfo(
                1L, "公司1", "1,3,5", "7,4,2", 0L, DateTime.now().minusDays(8).toDate(),
                SupplierResourceMaterialInfo.Status.NO_SUBMISSION.value())));
        // 8天前
        assertTrue(0 < supplierResourceMaterialInfoDao.create(getNewInfo(
                1L, "公司1", "1,3,5", "7,4,2", 0L, DateTime.now().minusDays(8).toDate(),
                SupplierResourceMaterialInfo.Status.SUBMITTED.value())));
        // 9天前
        assertTrue(0 < supplierResourceMaterialInfoDao.create(getNewInfo(
                2L, "公司2", "1,2,3", "7,4,2", 0L, DateTime.now().minusDays(9).toDate(),
                SupplierResourceMaterialInfo.Status.SUBMITTED.value()
        )));
        // 10天前
        assertTrue(0 < supplierResourceMaterialInfoDao.create(getNewInfo(
                3L, "公司3", "2,4,5", "7,4,2", 0L, DateTime.now().minusDays(10).toDate(),
                SupplierResourceMaterialInfo.Status.SUBMITTED.value()
        )));

        List<SupplierResourceMaterialInfo> infos = supplierResourceMaterialInfoDao.findBy(
                SupplierResourceMaterialInfo.Status.NO_SUBMISSION.value(),
                DateTime.now().minusDays(10).toDate(), DateTime.now().minusDays(8).toDate());

        assertFalse(infos.isEmpty());

        infos = supplierResourceMaterialInfoDao.findBy(
                SupplierResourceMaterialInfo.Status.SUBMITTED.value(),
                DateTime.now().minusDays(10).toDate(), DateTime.now().minusDays(8).toDate());
        assertThat(infos.size(), is(2));

        infos = supplierResourceMaterialInfoDao.findBy(
                SupplierResourceMaterialInfo.Status.SUBMITTED.value(),
                DateTime.now().minusDays(10).minusSeconds(1).toDate(), DateTime.now().minusDays(8).toDate());
        assertThat(infos.size(), is(3));
    }

    @Test
    public void testPagingBy() throws Exception {
        // 不显示
        assertTrue(0 < supplierResourceMaterialInfoDao.create(getNewInfo(
                1L, "公司1", "1,3,5", "7,4,2", 0L, DateTime.now().toDate(),
                SupplierResourceMaterialInfo.Status.NO_SUBMISSION.value())));
        // 显示
        assertTrue(0 < supplierResourceMaterialInfoDao.create(getNewInfo(
                2L, "公司2", "1,2,3", "7,4,2", 0L, DateTime.now().toDate(),
                SupplierResourceMaterialInfo.Status.SUBMITTED.value()
        )));
        // 显示
        assertTrue(0 < supplierResourceMaterialInfoDao.create(getNewInfo(
                3L, "公司3", "2,4,5", "7,4,2", 0L, DateTime.now().toDate(),
                SupplierResourceMaterialInfo.Status.SUBMITTED.value()
        )));

        SupplierResourceMaterialInfo criteria = new SupplierResourceMaterialInfo();
        criteria.setSupplierName("公司");
        criteria.setStatus(SupplierResourceMaterialInfo.Status.SUBMITTED.value());

        Paging<SupplierResourceMaterialInfo> paging = supplierResourceMaterialInfoDao.pagingBy(criteria, 0, 10);
        assertTrue(paging.getTotal() == 2);
        assertThat(paging.getData().get(0).getSupplierName(), is("公司2"));
        assertThat(paging.getData().get(1).getSupplierName(), is("公司3"));
    }
}