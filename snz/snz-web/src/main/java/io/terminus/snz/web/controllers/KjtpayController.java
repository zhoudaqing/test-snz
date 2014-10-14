package io.terminus.snz.web.controllers;

import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import io.terminus.pampas.common.Response;
import io.terminus.pampas.common.UserUtil;
import io.terminus.snz.requirement.dto.KjtTransDto;
import io.terminus.snz.requirement.service.DepositService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static com.google.common.base.Preconditions.checkState;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 6/19/14
 */
@Slf4j
@Controller
@RequestMapping("/api/kjtpay")
public class KjtpayController {

    @Autowired
    private DepositService depositService;

    @RequestMapping(value = "/notify/pay/test", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String kjtpayNotifyForPayTest(HttpServletRequest request) {
        Long requirementId = Long.parseLong(request.getParameter("requirementId"));
        Long amount = Long.parseLong(request.getParameter("amount"));
        KjtTransDto kjtTransDto = new KjtTransDto();
        return depositService.submit(UserUtil.getCurrentUser(), requirementId, amount, kjtTransDto).getResult();
    }

    /**
     * 获得快捷通交易结果后更新保证金信息
     * @param request 交易结果通知（POST）
     * @return 快捷通要求的返回码（0000为收到通知）
     */
    @RequestMapping(value = "/notify/pay", method = RequestMethod.POST)
    @ResponseBody
    public String kjtpayNotifyForPay(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("text/plain");
            Map<String, String> params = transformToPayMap(request);
            log.warn("received a notify for pay from kjt, params={}", params);
            Response<Boolean> dealR = depositService.dealResponse(params);
            checkState(dealR.isSuccess());
            return "0000";
        } catch (Exception e) {
            log.error("notify for pay get an error, request={}, cause:{}",
                    request.getParameterMap(), Throwables.getStackTraceAsString(e));
        }
        return "FAIL";
    }

    @RequestMapping(value = "/notify/trans", method = RequestMethod.POST)
    @ResponseBody
    public String kjtpayNotifyForTrans(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("text/plain");
            Map<String, String> params = transformToTransMap(request);
            log.warn("received a notify for trans from kjt, params={}", params);
            Response<Boolean> dealR = depositService.dealTransResponse(params);
            checkState(dealR.isSuccess());
            return "0000";
        } catch (Exception e) {
            log.error("notify for trans get an error, request={}, cause:{}",
                    request.getParameterMap(), Throwables.getStackTraceAsString(e));
        }
        return "FAIL";
    }

    private Map<String, String> transformToMap(HttpServletRequest request, String[] keys) {
        Map<String, String> params = Maps.newTreeMap(String.CASE_INSENSITIVE_ORDER);
        for (String key : keys) {
            params.put(key, Strings.nullToEmpty(request.getParameter(key)));
        }
        return params;
    }

    private Map<String, String> transformToPayMap(HttpServletRequest request) {
        String[] keys = {
                "inputCharset", "payType", "bankCode",
                "orderId", "orderTime", "orderAmount",
                "dealId", "dealTime",
                "ext1", "ext2",
                "payResult", "errCode",
                "signType", "signMsg"
        };
        return transformToMap(request, keys);
    }

    private Map<String, String> transformToTransMap(HttpServletRequest request) {
        String[] keys = {
                "inputCharset", "pid", "paytype", "version",
                "orderid", "account_type", "peername", "amount",
                "bankname", "cardnum", "province", "city",
                "peerbankname", "remark", "signMsg"
        };
        return transformToMap(request, keys);
    }
}
