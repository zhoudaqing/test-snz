package io.terminus.snz.requirement.service;

import com.google.common.collect.Lists;
import io.terminus.common.model.Paging;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.dto.ModulesDto;
import io.terminus.snz.requirement.dto.RequirementFactoryDto;
import io.terminus.snz.requirement.model.ImportGoodList;
import io.terminus.snz.requirement.model.Module;
import io.terminus.snz.requirement.model.Requirement;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

/**
 * Date: 8/5/14
 * Time: 10:28
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */
public class ImportGoodListServiceTest {

    @InjectMocks
    ImportGoodListServiceImpl importGoodListService;

    @Mock
    ModuleService moduleService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldPagingBy() {
        Response<Paging<ModulesDto>> modulesGetMock = new Response<Paging<ModulesDto>>();
        List<ModulesDto> modulesDtoMock = Lists.newArrayList();
        Paging<ModulesDto> modulesPagingMock = new Paging<ModulesDto>(1l, modulesDtoMock);
        modulesGetMock.setResult(modulesPagingMock);

        ModulesDto dtoMock = new ModulesDto();
        Requirement reqMock = new Requirement();
        reqMock.setId(1l);
        reqMock.setName("xxx");
        RequirementFactoryDto requirementFactoryDto = new RequirementFactoryDto();
        requirementFactoryDto.setRequirement(reqMock);
        dtoMock.setRequirementFactoryDto(requirementFactoryDto);
        dtoMock.setModuleList(Lists.<Module>newArrayList());
        Module moduleMock = new Module();
        moduleMock.setId(1l);
        moduleMock.setModuleName("xxx");
        moduleMock.setModuleNum("xxx");
        moduleMock.setSeriesId(1l);
        moduleMock.setSeriesName("xxx");
        dtoMock.getModuleList().add(moduleMock);
        modulesDtoMock.add(dtoMock);

        when(moduleService.pagingByPurchaserId(anyLong(), anyInt(), anyInt())).thenReturn(modulesGetMock);

        Response<Paging<ImportGoodList>> pageGet = importGoodListService.pagingBy(1l, null, null, 0, 20);
        assertTrue(pageGet.isSuccess());
    }

    @Test
    public void shouldPagingByPurchaser() {
        assertNotNull(importGoodListService.pagingByPurchaserId(1l, 0, 20));
    }
}
