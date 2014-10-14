package io.terminus.snz.eai.ignore;

import org.example.transstandardcredittosrm.TransStandardCreditToSRM;
import org.example.transstandardcredittosrm.TransStandardCreditToSRM_Service;
import org.example.transstandardcredittosrm.ZDSP12LOG20INPUT;
import org.example.transstandardcredittosrm.ZINTZDSP12LOG20SENDING;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.xml.ws.Holder;
import java.util.ArrayList;
import java.util.List;

/**
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-8-8
 */
@Ignore
public class TransStandardCreditToSRMTest {

    TransStandardCreditToSRM service;
            
    @Before
    public void init(){
        TransStandardCreditToSRM_Service serviceSrc = new TransStandardCreditToSRM_Service();
        service = serviceSrc.getTransStandardCreditToSRMSOAP();
    }

    @Test
    public void testTransStandardCreditToSRM(){
        String date = "2014-08-08";
        Holder<String> message = new Holder<String>();

        List<ZDSP12LOG20INPUT> inDatas = new ArrayList<ZDSP12LOG20INPUT>();
        ZDSP12LOG20INPUT inData = new ZDSP12LOG20INPUT();
        inData.setZDATE(date);
        inDatas.add(inData);

        Holder<List<ZINTZDSP12LOG20SENDING>> outData = new Holder<List<ZINTZDSP12LOG20SENDING>>();
        Holder<String> eaiFlag = new Holder<String>();
        Holder<String> eaiMessage = new Holder<String>();
        service.transStandardCreditToSRM(inDatas, message, outData, eaiFlag, eaiMessage);

        System.out.println("outData: " + outData.value);
        System.out.println("eaiFlag: " + eaiFlag.value);
        System.out.println("eaiMessage: " + eaiMessage.value);
        List<ZINTZDSP12LOG20SENDING> datas = outData.value;
        print(datas);
        //System.out.println("data size: " + datas.size());
    }

    private void print(List<ZINTZDSP12LOG20SENDING> datas) {
        for (ZINTZDSP12LOG20SENDING data : datas){
            System.out.println("lifnr: " + data.getLIFNR());
            // .... 其他数据
        }
    }
}
