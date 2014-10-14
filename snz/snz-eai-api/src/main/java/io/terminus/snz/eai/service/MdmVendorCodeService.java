package io.terminus.snz.eai.service;

import com.google.common.base.Optional;
import io.terminus.pampas.common.Response;
import io.terminus.snz.eai.dto.MDMVendorCodeApply;

import java.util.List;

/**
 * Date: 7/29/14
 * Time: 11:38
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */
public interface MdmVendorCodeService {

    public Response<List<Optional<String>>> applyVendorCode(MDMVendorCodeApply apply);
}
