package io.terminus.snz.eai.service;

import io.terminus.snz.eai.dao.QuotaInfoDao;
import io.terminus.snz.eai.manager.QuotaInfoManager;
import io.terminus.snz.eai.model.OutPriceInfo;
import io.terminus.snz.eai.model.OutQuotaInfo;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * Created by luyuzhou on 14-9-9.
 */
public class QuotaInfoServiceTest extends BaseServiceTest {

    @InjectMocks
    QuotaInfoServiceImpl quotaInfoServiceImpl;

    @Mock
    QuotaInfoManager quotaInfoManager;

    @Mock
    QuotaInfoDao quotaInfoDao;

    private List<OutQuotaInfo> outQuotaInfo;

    private List<OutPriceInfo> outPriceInfo;

    @Test
    public void shouldapplyQuotaInfo(){
        doNothing().when(quotaInfoManager).batchDerivativeQuota(anyList(), anyList());

        assertNotNull(quotaInfoServiceImpl.applyQuotaInfo("", ""));
    }

    @Test
    public void shouldfindListfrom(){
        when(quotaInfoDao.findbymodulenum(anyString())).thenReturn(outQuotaInfo);
        when(quotaInfoDao.findbymodulenumother(anyString())).thenReturn(outPriceInfo);
        assertNotNull(quotaInfoServiceImpl.findListfrom("001"));
    }
}
