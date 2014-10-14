package io.terminus.snz.eai.ignore;

import org.example.receiveclaiminfofromgvs.OutType;
import org.example.receiveclaiminfofromgvs.ReceiveClaimInfoFromGVS;
import org.example.receiveclaiminfofromgvs.ReceiveClaimInfoFromGVS_Service;
import org.example.receiveclaiminfofromgvs.ZSPJHINPUT2;
import org.example.sendclaiminfotogvs.*;
import org.junit.Ignore;
import org.junit.Test;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * eai索赔信息测试
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-7-16
 */
@Ignore
public class ClaimInfoTest {
    @Test
    public void testReceiveClaimInfoOK() throws MalformedURLException {
        ReceiveClaimInfoFromGVS_Service serviceSrc = new ReceiveClaimInfoFromGVS_Service();

        ReceiveClaimInfoFromGVS service = serviceSrc.getReceiveClaimInfoFromGVSSOAP();


        List<ZSPJHINPUT2> inputParams = new ArrayList<ZSPJHINPUT2>();
        ZSPJHINPUT2 inputParam = new ZSPJHINPUT2();
        /*
        园区:
        产业线:
        供应商或债权人账号: V12619
        供应商名称: 江苏惠利隆塑业集团有限公司
        内外部供应商: N
        工厂: 9220
        工厂名称:
        物料号: 0030203602
        物料描述: ROHS-线屑过滤器
        采购组: X20
        采购组描述: 陆卓钦
        扣分: 1.00
        扣款金额: 2000.00
        扣款日期:
        当前日期: 2014-06-26
        当前时间: 10:37:25
        需要扣款的标识:
        扣款状态:
        扣款结果: 供应商余额不足，暂不扣款！
        凭证入账日期: 2014-07-14
        EAIFlag: null
        EAIMessage: null
        EAIDetail: null
        */
        inputParam.setLIFNR("V12619");
        inputParam.setWERKS("9220");
        inputParam.setMATNR("0030203602");
        inputParam.setZRETSTA("申诉成功");
        inputParam.setZDATE("2014-06-26");
        inputParams.add(inputParam);
        OutType result = service.receiveClaimInfoFromGVS(inputParams);

        System.out.println("Z_RET: " + result.getZRET());
        System.out.println("EAIFlag: " + result.getEAIFlag());
        System.out.println("EAIMessage: " + result.getEAIMessage());
        System.out.println("EAIDetail: " + result.getEAIDetail());

    }

    @Test
    public void testReceiveClaimInfoFail() throws MalformedURLException {

        ReceiveClaimInfoFromGVS_Service serviceSrc = new ReceiveClaimInfoFromGVS_Service();

        ReceiveClaimInfoFromGVS service = serviceSrc.getReceiveClaimInfoFromGVSSOAP();


        List<ZSPJHINPUT2> inputParams = new ArrayList<ZSPJHINPUT2>();
        ZSPJHINPUT2 inputParam = new ZSPJHINPUT2();
        /*
        园区:
        产业线:
        供应商或债权人账号: V98507
        供应商名称: 青岛家电工艺装备研究所
        内外部供应商: N
        工厂: 9220
        工厂名称:
        物料号: 0010807698
        物料描述: ROHS-骨架总成(KFRD-35GW/Z3)
        采购组:
        采购组描述:
        扣分: 1.00
        扣款金额: 2000.00
        扣款日期:
        当前日期: 2014-06-26
        当前时间: 10:37:25
        需要扣款的标识:
        扣款状态:
        扣款结果:
        凭证入账日期: 2014-07-13
        EAIFlag: null
        EAIMessage: null
        EAIDetail: null
        */
        inputParam.setLIFNR("V98507");
        inputParam.setWERKS("9220");
        inputParam.setMATNR("0010807698");
        inputParam.setZRETSTA("申诉失败");
        inputParam.setZDATE("2014-06-26");
        inputParams.add(inputParam);
        OutType result = service.receiveClaimInfoFromGVS(inputParams);

        System.out.println("Z_RET: " + result.getZRET());
        System.out.println("EAIFlag: " + result.getEAIFlag());
        System.out.println("EAIMessage: " + result.getEAIMessage());
        System.out.println("EAIDetail: " + result.getEAIDetail());

    }

    /**
     * 根据日期获取索赔信息
     */
    @Test
    public void testSendClaimInfoByDate(){
        SendClaimInfoToGVS_Service serviceSrc = new SendClaimInfoToGVS_Service();
        SendClaimInfoToGVS service = serviceSrc.getSendClaimInfoToGVSSOAP();

        List<ZSPJHINPUT> dates = new ArrayList<ZSPJHINPUT>();
        ZSPJHINPUT date1 = new ZSPJHINPUT();
        date1.setZDATE("2014-09-11");
        dates.add(date1);

        List<ZSPJHOUTPUT> result = service.sendClaimInfoToGVS(dates, null);

        System.out.println(result.size());

        printResults(result);
    }

    /**
     * 通过月份获取索赔信息
     */
    @Test
    public void testSendClaimInfoByMonth(){
        SendClaimInfoToGVS_Service serviceSrc = new SendClaimInfoToGVS_Service();
        SendClaimInfoToGVS service = serviceSrc.getSendClaimInfoToGVSSOAP();

        List<ZSPJHINPUTMONTHLY> monthes = new ArrayList<ZSPJHINPUTMONTHLY>();
        ZSPJHINPUTMONTHLY month1 = new ZSPJHINPUTMONTHLY();
        month1.setZDATE("2014-09");
        monthes.add(month1);
        List<ZSPJHOUTPUT> result =
                service.sendClaimInfoToGVS(null, monthes);

        System.out.println(result.size());

        printResults(result);
    }

    private void printResults(List<ZSPJHOUTPUT> result) {
        for (ZSPJHOUTPUT res : result){
            System.out.println("园区: " + res.getZYY());
            System.out.println("产业线: " + res.getZCYX());
            System.out.println("供应商或债权人账号: " + res.getLIFNR());
            System.out.println("供应商名称: " + res.getNAME1());
            System.out.println("内外部供应商: " + res.getEXINFLAG());
            System.out.println("工厂: " + res.getWERKS());
            System.out.println("工厂名称: " );
            System.out.println("物料号: " + res.getMATNR());
            System.out.println("物料描述: " + res.getMAKTX());
            System.out.println("采购组: " + res.getEKGRP());
            System.out.println("采购组描述: " + res.getEKNAM());
            System.out.println("扣分: " + res.getKOUFEN());
            System.out.println("扣款金额: " + res.getZKKJE());
            System.out.println("扣款日期: ");
            System.out.println("当前日期: " + res.getZDATE());
            System.out.println("当前时间: " + res.getZTIME());
            System.out.println("需要扣款的标识: ");
            System.out.println("扣款状态: ");
            System.out.println("扣款结果: " + res.getZFMAS());
            System.out.println("凭证入账日期: " + res.getZFDAT());
            System.out.println("EAIFlag: " + res.getEAIFlag());
            System.out.println("EAIMessage: " + res.getEAIMessage());
            System.out.println("EAIDetail: " + res.getEAIDetail());
            System.out.println("-----------------------------------------");
        }
    }
}
