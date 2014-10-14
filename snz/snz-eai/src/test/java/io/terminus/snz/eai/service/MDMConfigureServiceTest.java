package io.terminus.snz.eai.service;

import com.google.common.collect.Lists;
import io.terminus.common.model.Paging;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.eai.dao.MDMBankViewDao;
import io.terminus.snz.eai.dao.MDMConfigureDao;
import io.terminus.snz.eai.manager.MDMSelectInfoManager;
import io.terminus.snz.eai.model.MDMBankView;
import io.terminus.snz.eai.model.MDMConfigure;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * Date: 8/4/14
 * Time: 2:46
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */
public class MDMConfigureServiceTest extends BaseServiceTest {

    @InjectMocks
    MdmConfigureServiceImpl mdmConfigureService;

    @Mock
    MDMConfigureDao mdmConfigureDao;

    @Mock
    MDMBankViewDao mdmBankViewDao;

    @Mock
    MDMSelectInfoManager mdmConfigureManager;

    private String data = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<OUTPUT>\n" +
            "    <ROWSET>\n" +
            "        <row>\n" +
            "            <RN>1.0</RN>\n" +
            "            <VALUE_ID>2008021800000000</VALUE_ID>\n" +
            "            <VALUE_SET_ID>CompanyCode</VALUE_SET_ID>\n" +
            "            <DESCRIPTION>公司代码</DESCRIPTION>\n" +
            "            <VALUE>6120</VALUE>\n" +
            "            <VALUE_MEANING>海尔集团电器产业有限公司</VALUE_MEANING>\n" +
            "            <VALUE_MEANING_EN>海尔集团电器产业有限公司</VALUE_MEANING_EN>\n" +
            "            <ACTIVE_FLAG>1</ACTIVE_FLAG>\n" +
            "            <CREATED>2008-02-28T18:43:00</CREATED>\n" +
            "            <LAST_UPD>2012-07-10T10:08:10</LAST_UPD>\n" +
            "            <DELETE_FLAG>0</DELETE_FLAG>\n" +
            "        </row>\n" +
            "        <row>\n" +
            "            <RN>2.0</RN>\n" +
            "            <VALUE_ID>2008021800000003</VALUE_ID>\n" +
            "            <VALUE_SET_ID>CompanyCode</VALUE_SET_ID>\n" +
            "            <DESCRIPTION>公司代码</DESCRIPTION>\n" +
            "            <VALUE>6740</VALUE>\n" +
            "            <VALUE_MEANING>海尔集团电器产业有限公司</VALUE_MEANING>\n" +
            "            <VALUE_MEANING_EN>海尔集团电器产业有限公司</VALUE_MEANING_EN>\n" +
            "            <ACTIVE_FLAG>0</ACTIVE_FLAG>\n" +
            "            <CREATED>2008-02-28T18:43:00</CREATED>\n" +
            "            <LAST_UPD>2011-06-10T14:00:11</LAST_UPD>\n" +
            "            <DELETE_FLAG>1</DELETE_FLAG>\n" +
            "        </row>\n" +
            "   </ROWSET>\n" +
            "</OUTPUT>\n";

    @Test
    public void shouldFindBy() {
        MDMConfigure result = new MDMConfigure();
        when(mdmConfigureDao.findBy(any(MDMConfigure.class))).thenReturn(result);

        MDMConfigure param = new MDMConfigure();
        param.setId(1l);
        Response<MDMConfigure> cfgGet = mdmConfigureService.findBy(param);
        assertTrue(cfgGet.isSuccess());
    }

    @Test
    public void shouldFindListBy() {
        MDMConfigure param = new MDMConfigure();
        param.setType(1);

        when(mdmConfigureDao.findListByTypes(anyList())).thenReturn(Lists.<MDMConfigure>newArrayList(param));

        Response<List<MDMConfigure>> cfgsGet = mdmConfigureService.findListBy(param);
        assertTrue(cfgsGet.isSuccess());
        assertFalse(cfgsGet.getResult().isEmpty());
    }

    @Test
    public void shouldSyncConfigureDataFromMDM() {
        doNothing().when(mdmConfigureManager).bulkInsertOrUpdate(anyList());

        assertNotNull(mdmConfigureService.syncConfigureDataFromMDM());
    }

    @Test
    public void shouldPagingBankFindByFuzzyName() {
        when(mdmBankViewDao.pagingBankFindByFuzzyName(anyMap()))
                .thenReturn(new Paging<MDMBankView>(0l, Collections.<MDMBankView>emptyList()));

        assertTrue(mdmConfigureService.pagingBankFindByFuzzyName(new BaseUser(), "xx", 0, 20).isSuccess());
    }

    @Test
    public void shouldfindBankByName() {
        when(mdmBankViewDao.findBankByName(anyString()))
                .thenReturn(null);
        assertFalse(mdmConfigureService.findBankByName("name").isSuccess());

        MDMBankView mdmBankView = new MDMBankView();
        mdmBankView.setId(1L);
        when(mdmBankViewDao.findBankByName(anyString()))
                .thenReturn(mdmBankView);

        Response<MDMBankView> mdmRes = mdmConfigureService.findBankByName("name");
        assertTrue(mdmRes.isSuccess());
        assertThat(mdmRes.getResult().getId(), is(1L));
    }
}
