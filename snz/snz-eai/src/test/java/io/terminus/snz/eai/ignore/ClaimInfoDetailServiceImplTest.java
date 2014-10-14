package io.terminus.snz.eai.ignore;

import org.example.transstandardcredittosrm.TransStandardCreditToSRM;
import org.example.transstandardcredittosrm.TransStandardCreditToSRM_Service;
import org.example.transstandardcredittosrm.ZDSP12LOG20INPUT;
import org.example.transstandardcredittosrm.ZINTZDSP12LOG20SENDING;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import javax.xml.ws.Holder;
import java.util.ArrayList;
import java.util.List;

/**
 * 索赔明细信息获取
 */
public class ClaimInfoDetailServiceImplTest {

    TransStandardCreditToSRM transStandardCreditToSRM;

    /**
     * 索赔明细数据 web Service 查询测试
     *
     * @throws Exception
     */
    @Test
    public void testGetClaimInfoDetails() throws Exception {

        DateTime dateTime = DateTime.now().withDate(2014, 9, 8);
        DateTimeFormatter FMT = DateTimeFormat.forPattern("YYYY-MM-dd");


        TransStandardCreditToSRM_Service transStandardCreditToSRM_service = new TransStandardCreditToSRM_Service();
        transStandardCreditToSRM = transStandardCreditToSRM_service.getTransStandardCreditToSRMSOAP();

        for (int i = 0; i <= 20; i++) {

            List<ZDSP12LOG20INPUT> currents = new ArrayList<ZDSP12LOG20INPUT>();
            ZDSP12LOG20INPUT currentZ = new ZDSP12LOG20INPUT();
            currentZ.setZDATE(FMT.print(dateTime));
            currents.add(currentZ);

            Holder<List<ZINTZDSP12LOG20SENDING>> outData = new Holder<List<ZINTZDSP12LOG20SENDING>>();
            Holder<String> eaiFlag = new Holder<String>();
            Holder<String> eaiMessage = new Holder<String>();
            Holder<String> message = new Holder<String>();
            transStandardCreditToSRM.transStandardCreditToSRM(currents, message, outData, eaiFlag, eaiMessage);

            System.out.println(FMT.print(dateTime) + " 响应: " + eaiMessage.value);
            System.out.println(FMT.print(dateTime) + " 标识: " + eaiFlag.value);
            System.out.println(FMT.print(dateTime) + " 消息: " + message.value);

            for(ZINTZDSP12LOG20SENDING data: outData.value)
                System.out.println(FMT.print(dateTime) + "       数据: " + data);
            System.out.println();

            dateTime = dateTime.plusDays(1);

        }

    }
}