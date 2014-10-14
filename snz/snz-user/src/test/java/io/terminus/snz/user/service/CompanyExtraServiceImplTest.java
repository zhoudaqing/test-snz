package io.terminus.snz.user.service;

import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.dao.*;
import io.terminus.snz.user.dto.CompanyExtraQualityDto;
import io.terminus.snz.user.manager.AccountManager;
import io.terminus.snz.user.model.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 8/3/14
 */
public class CompanyExtraServiceImplTest extends BaseServiceTest {

    @InjectMocks
    private CompanyExtraServiceImpl companyExtraService;

    @Mock
    private CompanyExtraRDDao companyExtraRDDao;

    @Mock
    private CompanyExtraQualityDao companyExtraQualityDao;

    @Mock
    private CompanyExtraDeliveryDao companyExtraDeliveryDao;

    @Mock
    private CompanyExtraResponseDao companyExtraResponseDao;

    @Mock
    private CompanyExtraScaleAndCostDao companyExtraScaleAndCostDao;

    @Mock
    private AccountManager accountManager;

    private BaseUser getUser(Long id) {
        return new BaseUser(id, "user", 1);
    }

    private CompanyExtraRD getRD(Long userId) {
        CompanyExtraRD rd = new CompanyExtraRD();
        rd.setUserId(userId);
        return rd;
    }

    private CompanyExtraQualityDto getQualityDto(Long userId) {
        CompanyExtraQuality quality = new CompanyExtraQuality();
        quality.setUserId(userId);
        CompanyExtraQualityDto dto = new CompanyExtraQualityDto();
        dto.setCompanyExtraQuality(quality);
        return dto;
    }

    private CompanyExtraDelivery getDelivery(Long userId) {
        CompanyExtraDelivery delivery = new CompanyExtraDelivery();
        delivery.setUserId(userId);
        return delivery;
    }

    private CompanyExtraResponse getResponse(Long userId) {
        CompanyExtraResponse response = new CompanyExtraResponse();
        response.setUserId(userId);
        return response;
    }

    private CompanyExtraScaleAndCost getScaleAndCost(Long userId) {
        CompanyExtraScaleAndCost scaleAndCost = new CompanyExtraScaleAndCost();
        scaleAndCost.setUserId(userId);
        return scaleAndCost;
    }

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testUpdateOrCreateRD() throws Exception {
        Response<Boolean> result = companyExtraService.updateOrCreateRD(getRD(1L));
        //Response<CompanyExtraRD> rdRes = companyExtraService.findRDByUser(getUser(1L));
        assertTrue(result.isSuccess());
        //  assertNotNull(rdRes.getResult());
        //   System.out.println(rdRes.getResult().getId());
//        assertThat(rdRes.getResult().getUserId(), is(1L));
    }

    @Test
    public void testFindRDByUser() throws Exception {
        companyExtraService.updateOrCreateRD(getRD(1L));
    }

    @Test
    public void testUpdateOrCreateQuality() throws Exception {
        Response<Boolean> result = companyExtraService.updateOrCreateQuality(getQualityDto(1L));
        // Response<CompanyExtraQualityDto> qualityDtoRes = companyExtraService.findQualityByUser(getUser(1L));
        assertTrue(result.isSuccess());
        //assertNotNull(qualityDtoRes.getResult());
//        assertThat(qualityDtoRes.getResult().getCompanyExtraQuality().getUserId(), is(1L));
    }

    @Test
    public void testFindQualityByUser() throws Exception {
        companyExtraService.updateOrCreateQuality(getQualityDto(1L));
        companyExtraService.findQualityByUser(getUser(1L));
    }

    @Test
    public void testUpdateOrCreateResponse() throws Exception {
        Response<Boolean> result = companyExtraService.updateOrCreateResponse(getResponse(1L));
        // Response<CompanyExtraResponse> responseRes = companyExtraService.findResponseByUser(getUser(1L));
        assertTrue(result.isSuccess());
        //  assertNotNull(responseRes.getResult());
//        assertThat(responseRes.getResult().getUserId(), is(1L));
    }

    @Test
    public void testFindResponseByUser() throws Exception {
        companyExtraService.updateOrCreateResponse(getResponse(1L));
        companyExtraService.findResponseByUser(getUser(1L));
    }

    @Test
    public void testUpdateOrCreateDelivery() throws Exception {
        Response<Boolean> result = companyExtraService.updateOrCreateDelivery(getDelivery(1L));
        //       Response<CompanyExtraDelivery> deliveryResponse = companyExtraService.findDeliveryByUser(getUser(1L));
        assertTrue(result.isSuccess());
//        assertThat(deliveryResponse.getResult().getUserId(), is(1L));
    }

    @Test
    public void testFindDeliveryByUser() throws Exception {
        companyExtraService.updateOrCreateDelivery(getDelivery(1L));
        companyExtraService.findDeliveryByUser(getUser(1L));
    }

    @Test
    public void testUpdateOrCreateScaleAndCost() throws Exception {
        companyExtraService.updateOrCreateScaleAndCost(getScaleAndCost(1L));
        Response<CompanyExtraScaleAndCost> scaleAndCostResponse = companyExtraService.findScaleAndCostByUser(getUser(1L));
        assertTrue(scaleAndCostResponse.isSuccess());
        assertNotNull(scaleAndCostResponse.getResult());
//        assertThat(scaleAndCostResponse.getResult().getUserId(), is(1L));
    }

    @Test
    public void testFindScaleAndCostByUser() throws Exception {
        companyExtraService.updateOrCreateScaleAndCost(getScaleAndCost(1L));
        companyExtraService.findScaleAndCostByUser(getUser(1L));
    }
}