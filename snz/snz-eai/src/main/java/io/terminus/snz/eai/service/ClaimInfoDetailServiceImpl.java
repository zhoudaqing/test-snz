package io.terminus.snz.eai.service;

import com.google.common.base.Throwables;
import io.terminus.pampas.common.Response;
import lombok.extern.slf4j.Slf4j;
import org.example.transstandardcredittosrm.TransStandardCreditToSRM;
import org.example.transstandardcredittosrm.TransStandardCreditToSRM_Service;
import org.example.transstandardcredittosrm.ZDSP12LOG20INPUT;
import org.example.transstandardcredittosrm.ZINTZDSP12LOG20SENDING;
import org.springframework.stereotype.Service;

import javax.xml.ws.Holder;
import java.util.ArrayList;
import java.util.List;

/**
 * Author:  wenhaoli
 * Date: 2014-08-13
 */
@Service
@Slf4j
public class ClaimInfoDetailServiceImpl implements ClaimInfoDetailService {

    /**
     * 获取明细表的接口
     * */
    private TransStandardCreditToSRM transStandardCreditToSRM;

    public ClaimInfoDetailServiceImpl() {

        TransStandardCreditToSRM_Service transStandardCreditToSRM_service = new TransStandardCreditToSRM_Service();
        transStandardCreditToSRM = transStandardCreditToSRM_service.getTransStandardCreditToSRMSOAP();

    }

    @Override
    public Response<Holder<List<ZINTZDSP12LOG20SENDING>>> getClaimInfoDetails(String current) {
        Response<Holder<List<ZINTZDSP12LOG20SENDING>>> resp = new Response<Holder<List<ZINTZDSP12LOG20SENDING>>>();
        try{
            if(current == null){
                log.error("ZDATE can not be empty");
                resp.setError("ZDATE.empty");
                return resp;
            }
            List<ZDSP12LOG20INPUT> currents = new ArrayList<ZDSP12LOG20INPUT>();
            ZDSP12LOG20INPUT currentZ = new ZDSP12LOG20INPUT();
            currentZ.setZDATE(current);
            currents.add(currentZ);

            Holder<List<ZINTZDSP12LOG20SENDING>> outData = new Holder<List<ZINTZDSP12LOG20SENDING>>();
            Holder<String> eaiFlag = new Holder<String>();
            Holder<String> eaiMessage = new Holder<String>();
            Holder<String> message = new Holder<String>();
            transStandardCreditToSRM.transStandardCreditToSRM(currents, message, outData, eaiFlag, eaiMessage);
            resp.setResult(outData);
        }catch (Exception e){
            log.error("failed to get claiminfoDetails from sap, cause: {}",
                    Throwables.getStackTraceAsString(e));
            resp.setError("claiminfoDetails.get.fail");
        }
        return resp;
    }
}
