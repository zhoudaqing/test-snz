package io.terminus.snz.requirement.service;

import com.google.common.collect.Lists;
import io.terminus.snz.requirement.dao.RequirementDao;
import io.terminus.snz.requirement.model.Requirement;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

/**
 * Created by yangzefeng on 14-8-5
 */
public class RequirementIndexServiceTest {

    @InjectMocks
    private RequirementIndexServiceImpl requirementIndexServiceImpl;

    @Mock
    private RequirementDao requirementDao;

    private List<Requirement> requirements;

    private Requirement r;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        requirements = Lists.newArrayList();
        r = new Requirement();
        r.setId(1l);
        r.setName("test");
        requirements.add(r);
    }


    @Test
    public void testRequirementIndex() {
        when(requirementDao.maxId()).thenReturn(1l);
        when(requirementDao.forDump(anyLong(),anyInt())).thenReturn(requirements);
        assertNotNull(requirementIndexServiceImpl.fullDump());
    }

    @Test
    public void testRequirementDeltaIndex() {
        when(requirementDao.maxId()).thenReturn(1l);
        when(requirementDao.forDeltaDump(anyLong(),anyString(), anyInt())).thenReturn(requirements);
        assertNotNull(requirementIndexServiceImpl.deltaDump(15));
    }


    @Test
    public void testRealTimeIndex() {
        when(requirementDao.findById(anyLong())).thenReturn(r);
        assertNotNull(requirementIndexServiceImpl.realTimeIndex(anyLong(), Requirement.SearchStatus.INDEX));
    }
}
