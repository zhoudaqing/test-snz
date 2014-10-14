package io.terminus.snz.related.components;

import com.google.common.collect.Lists;
import io.terminus.pampas.common.Response;
import io.terminus.snz.related.BaseTest;
import io.terminus.snz.related.services.CompensationService;
import org.example.sendclaiminfotogvs.SendClaimInfoToGVS;
import org.example.sendclaiminfotogvs.ZSPJHOUTPUT;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.when;

/**
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-8-4
 */
public class CompensationInfoSyncerTest extends BaseTest{

    @Mock
    private SendClaimInfoToGVS sendClaimInfoToGVS;

    @Mock
    private CompensationService compensationService;

    @InjectMocks
    private CompensationInfoSyncer compensationInfoSyncer;

    @Test
    public void testSyncByDay(){
        Response<Boolean> resp = new Response<Boolean>();
        resp.setResult(Boolean.TRUE);
        when(compensationService.batchAdd(anyList())).thenReturn(resp);
        List<ZSPJHOUTPUT> zspjhoutputs = Lists.newArrayList();
        when(sendClaimInfoToGVS.sendClaimInfoToGVS(anyList(), anyList())).thenReturn(zspjhoutputs);
        compensationInfoSyncer.syncByDay();

        // batch add failed
        resp.setResult(Boolean.FALSE);
        when(compensationService.batchAdd(anyList())).thenReturn(resp);
        compensationInfoSyncer.syncByDay();
    }

    @Test
    public void testSyncByMonth(){
        Response<Boolean> resp = new Response<Boolean>();
        resp.setResult(Boolean.TRUE);
        when(compensationService.batchAdd(anyList())).thenReturn(resp);
        List<ZSPJHOUTPUT> zspjhoutputs = Lists.newArrayList();
        zspjhoutputs.add(mockZSPJHOUTPUT());
        when(sendClaimInfoToGVS.sendClaimInfoToGVS(anyList(), anyList())).thenReturn(zspjhoutputs);
        compensationInfoSyncer.syncByMonth();

        // batch add failed
        resp.setResult(Boolean.FALSE);
        when(compensationService.batchAdd(anyList())).thenReturn(resp);
        compensationInfoSyncer.syncByMonth();

    }

    private ZSPJHOUTPUT mockZSPJHOUTPUT() {
        ZSPJHOUTPUT mock = new ZSPJHOUTPUT();
        mock.setEAIDetail("ok");
        mock.setEAIFlag("ok");
        mock.setEKGRP("BI");
        mock.setEKNAM("BE");
        mock.setLIFNR("1281");
        mock.setKOUFEN(new BigDecimal("12"));
        mock.setZKKJE(new BigDecimal("20"));
        mock.setZDATE("2014-08-06");
        mock.setZFMAS("2014-08-06");
        mock.setZFDAT("2014-08-06");
        return mock;
    }
}
