package io.terminus.snz.requirement.dao;

import com.google.common.collect.Lists;
import io.terminus.snz.requirement.model.ImportGoodRow;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.List;

import static io.terminus.common.utils.Arguments.isNull;
import static io.terminus.common.utils.Arguments.isNullOrEmpty;
import static io.terminus.snz.requirement.model.ImportGoodRow.STATUS;
import static org.junit.Assert.*;

/**
 * Date: 7/9/14
 * Time: 16:04
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */
public class ImportGoodRowTest extends BasicTest {

    @Autowired
    ImportGoodRowDao importGoodRowDao;

    private final ImportGoodRow init = genNew(null, null);

    private ImportGoodRow genNew(@Nullable Date timeline, @Nullable Date progress) {
        ImportGoodRow igr = new ImportGoodRow();
        igr.setInCharge("John Doe");
        igr.setStage(1);

        igr.setTimeline(isNull(timeline) ? DateTime.now().minusDays(1).toDate() : timeline);
        igr.setDuration(1);
        igr.setProgress(isNull(progress) ? DateTime.now().toDate() : progress);

        return igr;
    }

    @Before
    public void setup() {
        init.setInCharge("whoever");
        importGoodRowDao.create(init);
        assertNotNull(init.getId());
    }

    @Test
    public void shouldHaveRightStatus() {
        ImportGoodRow row = genNew(null, null);
        row.setDuration(2);

        assertEquals(STATUS.EARLY, STATUS.fromNumber(row.getStatus()));
        assertEquals(STATUS.NORMAL, STATUS.fromNumber(init.getStatus()));
    }

    @Test
    public void shouldFindById() {
        ImportGoodRow test = genNew(null, null);
        test.setInCharge("anywhere");
        importGoodRowDao.create(test);

        ImportGoodRow found = importGoodRowDao.findById(test.getId());

        assertEquals("anywhere", found.getInCharge());
    }

    @Test
    public void shouldFindByParams() {
        ImportGoodRow params = new ImportGoodRow();
        params.setInCharge("whoever");
        ImportGoodRow found = importGoodRowDao.findBy(params);
    }

    @Test
    public void shouldUpdateRecord() {
        ImportGoodRow toUpdate = genNew(null, null);
        toUpdate.setInCharge("xixi");
        importGoodRowDao.create(toUpdate);

        toUpdate.setInCharge("hehehe");
        importGoodRowDao.update(toUpdate);
        ImportGoodRow found = importGoodRowDao.findById(toUpdate.getId());
        assertEquals("hehehe", found.getInCharge());
    }

    @Test
    public void shouldFindByIds() {
        List<Long> ids = Lists.newArrayList();
        ids.add(init.getId());

        ImportGoodRow row = genNew(null, null);
        importGoodRowDao.create(row);
        ids.add(row.getId());

        List<ImportGoodRow> rowList = importGoodRowDao.findByIds(ids);
        assertFalse("Should not be empty", isNullOrEmpty(rowList));
        assertEquals(2, rowList.size());
    }

    @Test
    public void shouldUpdateForce() {
        ImportGoodRow row = genNew(null, null);
        importGoodRowDao.create(row);
        row.setProgress(null);
        importGoodRowDao.updateForce(row);

        ImportGoodRow found = importGoodRowDao.findById(row.getId());
        assertNull(found.getProgress());
        assertEquals(STATUS.SUSPEND.toNumber(), (int)found.getStatus());
    }
}
