package io.terminus.snz.eai.service;

import com.google.common.base.*;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.primitives.Longs;
import com.haier.QuerySupplierBalanceFromGVS.QuerySupplierBalanceFromGVS;
import com.haier.QuerySupplierBalanceFromGVS.QuerySupplierBalanceFromGVS_Service;
import com.haier.QuerySupplierBalanceFromGVS.ZFIINTLIFNRYEIN;
import com.haier.QuerySupplierBalanceFromGVS.ZFIINTLIFNRYEOUT;
import io.terminus.pampas.common.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 7/25/14
 */
@Slf4j
@Service(value = "hgvsServiceImpl")
public class HGVSServiceImpl implements HGVSService {

    private QuerySupplierBalanceFromGVS soap;

    public HGVSServiceImpl(){
        QuerySupplierBalanceFromGVS_Service service = new QuerySupplierBalanceFromGVS_Service();
        soap = service.getQuerySupplierBalanceFromGVSSOAP();
    }
    @Override
    public Response<Long> getBalanceBySupplierCode(String vCode) {
        Response<Long> result = new Response<Long>();
        try {
            if (Strings.isNullOrEmpty(vCode)) {
                result.setResult(0L);
                return result;
            }
            List<ZFIINTLIFNRYEIN> inputs = Lists.newArrayList();
            ZFIINTLIFNRYEIN input = new ZFIINTLIFNRYEIN();
            input.setLIFNR(vCode);
            inputs.add(input);

            List<ZFIINTLIFNRYEOUT> outputs = soap.querySupplierBalanceFromGVS(inputs);

            long balance = 0L;
            for (ZFIINTLIFNRYEOUT output : outputs) {
                if (Objects.equal(output.getFLAG(), "F")) {
                    result.setError(output.getMESSAGE());
                    return result;
                }
                // TODO: to be refactored
                Long midBalance = Longs.tryParse(output.getUM01U().multiply(new BigDecimal(100L)).toBigInteger().toString());

                midBalance = - midBalance; // 余额本身为负数
                if (midBalance < 0) {
                    log.error("must positive");
                    result.setError("hgvs.query.exist.minus");
                    return result;
                }
                balance += midBalance;
            }

            result.setResult(balance);
        } catch (Exception e) {
            log.error("getBalanceBySupplierCode(vCode={}) failed, cause:{}", vCode, Throwables.getStackTraceAsString(e));
            result.setError("hgvs.query.fail");
        }
        return result;
    }

    @Override
    public Response<Map<String, Long>> bulkGetBalanceBySupplierCodes(List<String> vCodes) {
        Response<Map<String, Long>> result = new Response<Map<String, Long>>();
        try {
            // 去重，去非法
            Set<String> vCodeSet = FluentIterable.from(vCodes).filter(Predicates.not(new Predicate<String>() {
                @Override
                public boolean apply(String vCode) {
                    return Strings.isNullOrEmpty(vCode);
                }
            })).toSet();

            Map<String, Long> balanceMap = Maps.newHashMap();
            if (vCodeSet.isEmpty()) {
                result.setResult(balanceMap);
                return result;
            }

            // 批量查询
            List<ZFIINTLIFNRYEIN> inputs = Lists.newArrayList();
            for (String vCode : vCodeSet) {
                ZFIINTLIFNRYEIN input = new ZFIINTLIFNRYEIN();
                input.setLIFNR(vCode);
            }
            List<ZFIINTLIFNRYEOUT> outputs = soap.querySupplierBalanceFromGVS(inputs);

            // 求和
            for (ZFIINTLIFNRYEOUT output : outputs) {
                String vCode = output.getLIFNR();
                // TODO: need refactor
                Long balance = Longs.tryParse(output.getUM01U().multiply(new BigDecimal(100L)).toBigInteger().toString());
                if (!balanceMap.containsKey(vCode)) {
                    balanceMap.put(vCode, balance);
                } else {
                    balanceMap.put(vCode, balanceMap.get(vCode));
                }
            }
            result.setResult(balanceMap);
        } catch (Exception e) {
            log.error("bulkGetBalanceBySupplierCodes(vCodes={}) failed, casue:{}",
                    vCodes, Throwables.getStackTraceAsString(e));
            result.setError("hgvs.query.fail");
        }
        return result;
    }
}
