package io.terminus.snz.requirement.service.mock;

import io.terminus.pampas.common.Response;
import io.terminus.snz.related.dto.ParkFactoryDto;
import io.terminus.snz.related.models.AddressFactory;
import io.terminus.snz.related.models.AddressPark;
import io.terminus.snz.related.services.DeliveryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Desc:
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-09-15.
 */
@Service
public class DeliveryServiceMock implements DeliveryService {
    @Override
    public Response<List<AddressPark>> findAllPark() {
        return null;
    }

    @Override
    public Response<List<AddressPark>> findParkByProductId(Long productId) {
        return null;
    }

    @Override
    public Response<List<AddressFactory>> findFactories(Long productId, Long parkId) {
        return null;
    }

    @Override
    public Response<List<AddressFactory>> findFactoryByIds(List<Long> factoryIds) {
        return null;
    }

    @Override
    public Response<List<AddressPark>> findParksByIds(List<Long> ids) {
        return null;
    }

    @Override
    public Response<List<ParkFactoryDto>> findDetailFactories(Long productId, List<Long> factoryIds) {
        return null;
    }
}
