package io.terminus.snz.user.service;

import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.model.Address;

import java.util.List;

/**
 * Description：
 * Author：Guo Chaopeng
 * Created on 14-6-6
 */
public interface AddressService {

    @Export
    Response<List<Address>> countries();

    @Export
    Response<List<Address>> provinces();

    @Export(paramNames = {"provinceId"})
    Response<List<Address>> citiesOf(Integer provinceId);

    @Export(paramNames = {"cityId"})
    Response<List<Address>> districtOf(Integer cityId);

    @Export(paramNames = {"id"})
    Response<Address> findById(Integer id);

    @Export(paramNames = {"anyId"})
    Response<List<Integer>> ancestorsOf(Integer anyId);

    @Export(paramNames = {"anyId"})
    Response<List<Address>> ancestorOfAddresses(Integer anyId);

    @Export(paramNames = {"tree"})
    Response<List<Address>> getTreeOf(Integer parentId);

}
