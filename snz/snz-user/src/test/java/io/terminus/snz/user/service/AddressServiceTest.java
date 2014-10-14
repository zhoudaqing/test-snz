package io.terminus.snz.user.service;

import com.google.common.collect.Lists;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.dao.AddressDao;
import io.terminus.snz.user.model.Address;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

/**
 * Desc:
 * Mail: houly@terminus.io
 * author: Hou Luyao
 * Date: 14-8-4.
 */
public class AddressServiceTest extends BaseServiceTest {

    @Mock
    private AddressDao addressDao;

    @InjectMocks
    private AddressServiceImpl addressServiceImpl;

    @Test
    public void testFindById(){
        Response<Address> response = addressServiceImpl.findById(null);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"params.not.null");

        response = addressServiceImpl.findById(1);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"address.query.fail");

        when(addressDao.findById(anyInt())).thenReturn(createAdress());
        response = addressServiceImpl.findById(1);
        assertTrue(response.isSuccess());
    }

    @Test
    public void testCountries(){
        when(addressDao.findByLevel(0)).thenReturn(Lists.newArrayList(createAdress()));
        Response<List<Address>> response = addressServiceImpl.countries();
        assertTrue(response.isSuccess());
        when(addressDao.findByLevel(0)).thenThrow(Exception.class);
        response = addressServiceImpl.countries();
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"province.query.fail");
    }

    @Test
    public void testProvinces(){
        when(addressDao.findByLevel(1)).thenReturn(Lists.newArrayList(createAdress()));
        Response<List<Address>> response = addressServiceImpl.provinces();
        assertTrue(response.isSuccess());
    }

    @Test
    public void testCitiesOf(){
        Response<List<Address>> response = addressServiceImpl.citiesOf(null);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"params.not.null");

        when(addressDao.findByParentId(anyInt())).thenReturn(Lists.newArrayList(createAdress()));
        response = addressServiceImpl.citiesOf(1);
        assertTrue(response.isSuccess());
    }

    @Test
    public void testDistrictOf(){
        Response<List<Address>> response = addressServiceImpl.districtOf(null);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"params.not.null");

        when(addressDao.findByParentId(anyInt())).thenReturn(Lists.newArrayList(createAdress()));
        response = addressServiceImpl.districtOf(1);
        assertTrue(response.isSuccess());
    }

    @Test
    public void testAncestorsOf(){
        Response<List<Integer>> response = addressServiceImpl.ancestorsOf(null);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"params.not.null");

        when(addressDao.findByParentId(1)).thenReturn(Lists.newArrayList(createAdress()));
        response = addressServiceImpl.ancestorsOf(1);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"ancestor.query.fail");

        when(addressDao.findByParentId(1)).thenReturn(Lists.newArrayList(createAdress()));
        when(addressDao.findById(1)).thenReturn(createAdress());
        response = addressServiceImpl.ancestorsOf(1);
        assertTrue(response.isSuccess());
    }

    @Test
    public void testAncestorOfAddresses(){
        Response<List<Address>> response = addressServiceImpl.ancestorOfAddresses(null);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"params.not.null");

        when(addressDao.findByParentId(1)).thenReturn(Lists.newArrayList(createAdress()));
        response = addressServiceImpl.ancestorOfAddresses(1);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"ancestor.query.fail");

        when(addressDao.findByParentId(1)).thenReturn(Lists.newArrayList(createAdress()));
        when(addressDao.findById(1)).thenReturn(createAdress());
        response = addressServiceImpl.ancestorOfAddresses(1);
        assertTrue(response.isSuccess());
    }

    @Test
    public void testGetTreeOf(){
        Response<List<Address>> response = addressServiceImpl.getTreeOf(null);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"params.not.null");

        Address c = createAdress();
        c.setLevel(3);
        when(addressDao.findByParentId(1)).thenReturn(Lists.newArrayList(c));
        when(addressDao.findById(1)).thenReturn(createAdress());
        response = addressServiceImpl.getTreeOf(1);
        assertTrue(response.isSuccess());

    }

    private Address createAdress(){
        Address address = new Address();
        address.setName("测试地址");
        address.setId(1);
        address.setLevel(1);
        address.setParentId(0);
        return address;
    }
}
