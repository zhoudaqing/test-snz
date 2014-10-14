package io.terminus.snz.requirement.service.mock;

import com.google.common.base.Optional;
import io.terminus.pampas.common.Response;
import io.terminus.snz.eai.dto.MDMVendorCodeApply;
import io.terminus.snz.eai.service.MdmVendorCodeService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Desc:
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-30.
 */
@Service
public class MdmVendorCodeServiceMock implements MdmVendorCodeService {
    @Override
    public Response<List<Optional<String>>> applyVendorCode(MDMVendorCodeApply apply) {
        return null;
    }
}
