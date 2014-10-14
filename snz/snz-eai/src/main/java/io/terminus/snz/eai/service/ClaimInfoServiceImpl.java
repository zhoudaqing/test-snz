package io.terminus.snz.eai.service;

import com.google.common.base.Throwables;
import io.terminus.pampas.common.Response;
import lombok.extern.slf4j.Slf4j;
import org.example.receiveclaiminfofromgvs.OutType;
import org.example.receiveclaiminfofromgvs.ReceiveClaimInfoFromGVS;
import org.example.receiveclaiminfofromgvs.ReceiveClaimInfoFromGVS_Service;
import org.example.receiveclaiminfofromgvs.ZSPJHINPUT2;
import org.example.sendclaiminfotogvs.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * EAI索赔信息实现
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-8-8
 */
@Service @Slf4j
public class ClaimInfoServiceImpl implements ClaimInfoService {

    /**
     * 获取索赔信息的接口
     */
    private SendClaimInfoToGVS sendClaimInfoToGVS;

    /**
     * 发送索赔信息申诉结果的接口
     */
    private ReceiveClaimInfoFromGVS receiveClaimInfoFromGVS;

    public ClaimInfoServiceImpl(){
        SendClaimInfoToGVS_Service sendClaimInfoToGVSService = new SendClaimInfoToGVS_Service();
        sendClaimInfoToGVS = sendClaimInfoToGVSService.getSendClaimInfoToGVSSOAP();

        ReceiveClaimInfoFromGVS_Service receiveClaimInfoFromGVSService = new ReceiveClaimInfoFromGVS_Service();
        receiveClaimInfoFromGVS = receiveClaimInfoFromGVSService.getReceiveClaimInfoFromGVSSOAP();
    }

    @Override
    public Response<List<ZSPJHOUTPUT>> getClaimInfos(String day, String month) {
        Response<List<ZSPJHOUTPUT>> resp = new Response<List<ZSPJHOUTPUT>>();
        try {
            if (day == null && month == null){
                log.error("day and month can't be empty all");
                resp.setError("day.month.both.empty");
                return resp;
            }
            if (day != null && month != null){
                log.error("day and month only one param is existed.");
                resp.setError("day.month.both.existed");
                return resp;
            }

            List<ZSPJHINPUT> days = null;
            if (day != null){
                days = new ArrayList<ZSPJHINPUT>();
                ZSPJHINPUT dayZ = new ZSPJHINPUT();
                dayZ.setZDATE(day);
                days.add(dayZ);
            }
            List<ZSPJHINPUTMONTHLY> months = null;
            if (month != null){
                months = new ArrayList<ZSPJHINPUTMONTHLY>();
                ZSPJHINPUTMONTHLY monthZ = new ZSPJHINPUTMONTHLY();
                monthZ.setZDATE(month);
                months.add(monthZ);
            }

            List<ZSPJHOUTPUT> claimInfos = sendClaimInfoToGVS.sendClaimInfoToGVS(days, months);
            resp.setResult(claimInfos);
        } catch(Exception e){
            log.error("failed to get claiminfos from sap, cause: {}",
                    Throwables.getStackTraceAsString(e));
            resp.setError("claiminfo.get.fail");
        }
        return resp;
    }

    @Override
    public Response<Boolean> setCompensations(List<ZSPJHINPUT2> claimInfos) {
        Response<Boolean> resp = new Response<Boolean>();
        try {
            OutType result = receiveClaimInfoFromGVS.receiveClaimInfoFromGVS(claimInfos);
            if (!"0".equals(result.getEAIFlag())){
                log.error("failed to put claiminfo to sap through eai, cause: {}", result.getEAIMessage());
                resp.setError(result.getEAIMessage());
            }
            resp.setResult(Boolean.TRUE);
        } catch (Exception e){
            log.error("failed to put claiminfo to sap through eai, cause: {}",
                    Throwables.getStackTraceAsString(e));
            resp.setError("claiminfo.put.fail");
        }
        return resp;
    }
}
