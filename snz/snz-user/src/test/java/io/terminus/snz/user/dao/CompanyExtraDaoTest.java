package io.terminus.snz.user.dao;

import io.terminus.snz.user.model.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 6/23/14
 */
public class CompanyExtraDaoTest extends TestBaseDao {

    @Autowired
    private CompanyExtraRDDao companyExtraRDDao;

    @Autowired
    private CompanyExtraQualityDao companyExtraQualityDao;

    @Autowired
    private CompanyExtraResponseDao companyExtraResponseDao;

    @Autowired
    private CompanyExtraDeliveryDao companyExtraDeliveryDao;

    @Autowired
    private CompanyExtraScaleAndCostDao companyExtraScaleAndCostDao;

    private CompanyExtraRD companyExtraRD;
    private CompanyExtraQuality companyExtraQuality;
    private CompanyExtraResponse companyExtraResponse;
    private CompanyExtraDelivery companyExtraDelivery;
    private CompanyExtraScaleAndCost companyExtraScaleAndCost;

    private CompanyExtraRD mockRD(Long userId) {
        CompanyExtraRD rd = new CompanyExtraRD();
        rd.setUserId(userId);
        return rd;
    }

    private CompanyExtraQuality mockQuality(Long userId) {
        CompanyExtraQuality qua = new CompanyExtraQuality();
        qua.setUserId(userId);
        return qua;
    }

    private CompanyExtraResponse mockResponse(Long userId) {
        CompanyExtraResponse res = new CompanyExtraResponse();
        res.setUserId(userId);
        return res;
    }

    private CompanyExtraDelivery mockDelivery(Long userId) {
        CompanyExtraDelivery del = new CompanyExtraDelivery();
        del.setUserId(userId);
        return del;
    }

    private CompanyExtraScaleAndCost mockScaleAndCost(Long userId) {
        CompanyExtraScaleAndCost sc = new CompanyExtraScaleAndCost();
        sc.setUserId(userId);
        return sc;
    }

    @Before
    public void setUp() {
        companyExtraRD = mockRD(1L);
        companyExtraQuality = mockQuality(1L);
        companyExtraDelivery = mockDelivery(1L);

        companyExtraRDDao.create(companyExtraRD);
        companyExtraQualityDao.create(companyExtraQuality);
        companyExtraDeliveryDao.create(companyExtraDelivery);
    }

    @Test
    public void testUpdate() {
        CompanyExtraRD rd1 = mockRD(1L);
        rd1.setInvestmentBeforeLastYear(200L);
        companyExtraRDDao.update(rd1);

        assertThat(companyExtraRDDao.findByUserId(1L).getInvestmentBeforeLastYear(), is(200L));

        CompanyExtraQuality qua1 = mockQuality(1L);
        qua1.setIso14001Id("iso11111");
        qua1.setAuditorType(1);
        companyExtraQualityDao.update(qua1);

        assertThat(companyExtraQualityDao.findByUserId(1L).getIso14001Id(), is("iso11111"));
        assertThat(companyExtraQualityDao.findByUserId(1L).getAuditorType(), is(1));

        CompanyExtraDelivery del1 = mockDelivery(1L);
        del1.setDeliveryArea("heh");
        companyExtraDeliveryDao.update(del1);

        assertThat(companyExtraDeliveryDao.findByUserId(1L).getDeliveryArea(), is("heh"));

    }
}
