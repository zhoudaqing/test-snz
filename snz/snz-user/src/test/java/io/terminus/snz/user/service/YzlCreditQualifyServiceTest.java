package io.terminus.snz.user.service;

import com.google.common.collect.Lists;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.dao.YzlCreditQualifyDao;
import io.terminus.snz.user.model.YzlCreditQualify;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;
/**
 * Created by dream on 14-10-14.
 */
public class YzlCreditQualifyServiceTest extends BaseServiceTest{

    @InjectMocks
    YzlCreditQualifyServiceImpl yzlCreditQualifyService;

    @Mock
    YzlCreditQualifyDao yzlCreditQualifyDao;

    @Mock
    YzlCreditQualify yzlCreditQualify;

    @Test
    public void shouldFindByStatus() {
        Integer status = 4;
        Response<List<YzlCreditQualify>> resp;
        List<YzlCreditQualify> yzlCreditQualifies = Lists.newArrayList(yzlCreditQualify);

        when(yzlCreditQualifyDao.selectByStatus(status)).thenReturn(yzlCreditQualifies);
        resp = yzlCreditQualifyService.findByStatus(status);
        Assert.assertTrue("must", resp.getResult().size() >= 1);

        when(yzlCreditQualifyDao.selectByStatus(anyInt())).thenThrow(new RuntimeException("Interruption"));
        resp = yzlCreditQualifyService.findByStatus(status);
        Assert.assertTrue("Should be interrupted", resp.getError()!=null);
    }
}
