package io.terminus.snz.requirement.service.mock;

import io.terminus.pampas.common.Response;
import io.terminus.snz.eai.service.HGVSService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Desc:
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-30.
 */
@Service
public class HGVSServiceMock implements HGVSService {
    @Override
    public Response<Long> getBalanceBySupplierCode(String vCode) {
        return null;
    }

    @Override
    public Response<Map<String, Long>> bulkGetBalanceBySupplierCodes(List<String> vCodes) {
        return null;
    }
}
