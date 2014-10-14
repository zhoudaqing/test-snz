package io.terminus.snz.related.services;

import com.google.common.collect.Lists;
import io.terminus.snz.related.BaseTest;
import io.terminus.snz.related.daos.AddressFactoryDao;
import io.terminus.snz.related.daos.AddressParkDao;
import io.terminus.snz.related.daos.CategoryFactoryDao;
import io.terminus.snz.related.models.AddressPark;
import io.terminus.snz.related.models.CategoryFactory;
import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

public class DeliveryServiceITest extends BaseTest {

    @Mock
    private AddressParkDao addressParkDao;

    @Mock
    private AddressFactoryDao addressFactoryDao;

    @Mock
    private CategoryFactoryDao categoryFactoryDao;

    @InjectMocks
    private DeliveryServiceImpl deliveryService;

    @Test
    public void testFindAllPark() throws Exception {
        when(addressParkDao.findAllPark()).thenReturn(Lists.newArrayList(mock()));
        assertNotNull(deliveryService.findAllPark());
    }

    @Test
    public void testFindParkByProductId() throws Exception {
        when(addressParkDao.findParkByProductId(anyLong())).thenReturn(Lists.newArrayList(mock()));
        assertNotNull(deliveryService.findParkByProductId(null));
        assertNotNull(deliveryService.findParkByProductId(1l));
    }

    @Test
    public void testFindFactories() throws Exception {
        when(categoryFactoryDao.findByProductId(1l , 1l)).thenReturn(Lists.newArrayList(mockCategory()));
        assertNotNull(deliveryService.findFactories(null , 1l));
        assertNotNull(deliveryService.findFactories(1l , null));
        assertNotNull(deliveryService.findFactories(1l , 1l));
    }

    @Test
    public void testFindParksByIds() throws Exception {
        when(addressParkDao.findByIds(anyList())).thenReturn(Lists.newArrayList(mock()));
        assertNotNull(deliveryService.findParksByIds(Lists.newArrayList(1l , 2l)));
    }

    private AddressPark mock(){
        AddressPark addressPark = new AddressPark();
        addressPark.setId(1l);
        addressPark.setParkName("name");
        addressPark.setCreatedAt(DateTime.now().toDate());
        addressPark.setUpdatedAt(DateTime.now().toDate());

        return addressPark;
    }

    private CategoryFactory mockCategory(){
        CategoryFactory categoryFactory = new CategoryFactory();
        categoryFactory.setId(1l);
        categoryFactory.setFactoryId(1l);
        categoryFactory.setProductId(1l);
        categoryFactory.setCreatedAt(DateTime.now().toDate());
        categoryFactory.setUpdatedAt(DateTime.now().toDate());

        return categoryFactory;
    }
}