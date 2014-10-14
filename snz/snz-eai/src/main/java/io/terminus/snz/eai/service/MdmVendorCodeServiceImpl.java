package io.terminus.snz.eai.service;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.haier.OuterSysVendorInfoToMDM.OuterSysVendorInfoToMDM;
import com.haier.OuterSysVendorInfoToMDM.OuterSysVendorInfoToMDM_Service;
import io.terminus.pampas.common.Response;
import io.terminus.snz.eai.dto.MDMVendorCodeApply;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.xml.ws.Holder;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static io.terminus.common.utils.Arguments.notNull;

/**
 * Date: 7/29/14
 * Time: 11:39
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */

@Slf4j
@Service("mdmVendorCodeServiceImpl")
public class MdmVendorCodeServiceImpl implements MdmVendorCodeService {

    @Override
    public Response<List<Optional<String>>> applyVendorCode(final MDMVendorCodeApply apply) {
        Response<List<Optional<String>>> result = new Response<List<Optional<String>>>();

        Holder<String> retCode = new Holder<String>();
        Holder<String> retMsg = new Holder<String>();
        Holder<String> outRowId = new Holder<String>();
        Holder<String> outVendorCode = new Holder<String>();
        Holder<String> outTaxCode = new Holder<String>();

        try {
            checkArgument(notNull(apply));
            OuterSysVendorInfoToMDM_Service service = new OuterSysVendorInfoToMDM_Service();
            OuterSysVendorInfoToMDM port = service.getOuterSysVendorInfoToMDMSOAP();

            port.outerSysVendorInfoToMDM("", apply.getSupplierName(), apply.getAccountGroup(), apply.getTaxCode(),
                    apply.getStreetRoom(), apply.getPostCode(), apply.getCountry(), apply.getRegion(), apply.getPhone(),
                    apply.getTCompany(), apply.getTBank(), apply.getTPru(),
                    "DDLPT", null, "CREATE",
                    retCode, retMsg, outRowId, outVendorCode, outTaxCode);


            Optional<String> rRetCode = Optional.of(retCode.value);
            Optional<String> rRetMsg = Optional.of(retMsg.value);
            Optional<String> rOutRowId = Optional.of(outRowId.value);
            Optional<String> rOutVendorCode = Optional.of(outVendorCode.value);
            Optional<String> routTaxCode = Optional.of(outTaxCode.value);

            result.setResult(Lists.newArrayList(rRetCode, rRetMsg, rOutRowId, rOutVendorCode, routTaxCode));
        } catch (IllegalArgumentException e) {
            log.error("`applyVendorCode` invoke with illegal argument, apply:{}, e:{}", apply, e);
            result.setError("supplier.vendor.code.illegal.arg");
            return result;
        } catch (Exception e) {
            log.error("`applyVendorCode` invoke fail. with apply:{}, retCode{}, retMsg:{}, e:{}",
                    apply, retCode, retMsg, e);
            result.setError("supplier.vendor.code.apply.fail");
            return result;
        }

        return result;
    }
}
