package io.terminus.snz.requirement.service.mock;

import io.terminus.pampas.common.Response;
import io.terminus.snz.user.model.Address;
import io.terminus.snz.user.service.AddressService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Desc:
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-30.
 */
@Service
public class AddressServiceMock implements AddressService {
    @Override
    public Response<List<Address>> countries() {
        return null;
    }

    @Override
    public Response<List<Address>> provinces() {
        return null;
    }

    @Override
    public Response<List<Address>> citiesOf(Integer provinceId) {
        return null;
    }

    @Override
    public Response<List<Address>> districtOf(Integer cityId) {
        return null;
    }

    @Override
    public Response<Address> findById(Integer id) {
        return null;
    }

    @Override
    public Response<List<Integer>> ancestorsOf(Integer anyId) {
        return null;
    }

    @Override
    public Response<List<Address>> ancestorOfAddresses(Integer anyId) {
        return null;
    }

    @Override
    public Response<List<Address>> getTreeOf(Integer parentId) {
        return null;
    }
}
