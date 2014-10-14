package io.terminus.snz.requirement.service;

import com.google.common.collect.Lists;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.dao.ImportGoodDao;
import io.terminus.snz.requirement.dao.ImportGoodRowDao;
import io.terminus.snz.requirement.model.ImportGood;
import io.terminus.snz.requirement.model.ImportGoodRow;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static io.terminus.snz.requirement.model.ImportGood.STAGE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

/**
 * Date: 8/5/14
 * Time: 10:28
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */
public class ImportGoodServiceTest {

    @InjectMocks
    ImportGoodServiceImpl importGoodService;

    @Mock
    ImportGoodDao importGoodDao;

    @Mock
    ImportGoodRowDao importGoodRowDao;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldFindImportGoodAndCurrentRow() {
        // mock import good
        ImportGood importGoodMock = new ImportGood();
        importGoodMock.setStage(STAGE.CONTRACT.toNumber());
        importGoodMock.setEstimateContract(1l);
        when(importGoodDao.findOneBy(any(ImportGood.class))).thenReturn(importGoodMock);

        // mock import good row
        when(importGoodRowDao.findById(anyLong())).thenReturn(new ImportGoodRow());


        Response tryGet = importGoodService.findImportGoodAndCurrentRow(1l);
        assertTrue(tryGet.isSuccess());

        assertFalse(importGoodService.findImportGoodAndCurrentRow(-1l).isSuccess());

        importGoodMock = new ImportGood();
        importGoodMock.setStage(STAGE.UNDEFINED.toNumber());
        when(importGoodDao.findOneBy(any(ImportGood.class))).thenReturn(importGoodMock);
        assertTrue(importGoodService.findImportGoodAndCurrentRow(1l).isSuccess());

        importGoodMock = new ImportGood();
        importGoodMock.setStage(STAGE.DESIGN.toNumber());
        when(importGoodDao.findOneBy(any(ImportGood.class))).thenReturn(importGoodMock);
        assertFalse(importGoodService.findImportGoodAndCurrentRow(1l).isSuccess());
    }

    @Test
    public void shouldFindByModuleId() {
        List<ImportGood> goodListMock = Lists.newArrayList();
        ImportGood good = new ImportGood();
        good.setEstimateContract(1l);
        goodListMock.add(good);
        when(importGoodDao.findBy(any(ImportGood.class))).thenReturn(goodListMock);

        when(importGoodRowDao.findByIds(anyList())).thenReturn(Lists.<ImportGoodRow>newArrayList());

        Response tryGet = importGoodService.findByModuleId(1l);
        assertTrue(tryGet.isSuccess());

        assertFalse(importGoodService.findByModuleId(null).isSuccess());
    }

    @Test
    public void shouldUpdateProgress() {
        // mock imported good
        ImportGood goodMock = new ImportGood();
        goodMock.setStage(STAGE.CONTRACT.toNumber());
        goodMock.setEstimateContract(1l);
        when(importGoodDao.findById(anyLong())).thenReturn(goodMock);

        // mock imported row
        ImportGoodRow rowMock = new ImportGoodRow();
        rowMock.setDuration(1);
        rowMock.setTimeline(DateTime.now().minusDays(2).toDate());
        when(importGoodRowDao.findById(anyLong())).thenReturn(rowMock);

        // mock update row
        when(importGoodRowDao.updateForce(any(ImportGoodRow.class))).thenReturn(true);

        // all pass test
        ImportGoodRow param = new ImportGoodRow();
        param.setDuration(1);
        param.setStage(STAGE.CONTRACT.toNumber());
        param.setProgress(DateTime.now().minusDays(3).toDate());
        Response tryUpdate = importGoodService.updateProgress(param, 1l, 1);
        assertFalse(tryUpdate.isSuccess());

        param.setStage(STAGE.CONTRACT.toNumber());
        param.setProgress(DateTime.now().toDate());
        tryUpdate = importGoodService.updateProgress(param, 1l, 1);
        assertTrue(tryUpdate.isSuccess());

        assertFalse(importGoodService.updateProgress(null, null, null).isSuccess());

        when(importGoodRowDao.findById(anyLong())).thenReturn(null);
        assertFalse(importGoodService.updateProgress(param, 1l, 1).isSuccess());
    }
}
