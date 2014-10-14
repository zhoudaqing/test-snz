package io.terminus.snz.web.controllers.restful;

import io.terminus.common.model.Paging;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.dto.ImportGoodDto;
import io.terminus.snz.requirement.model.ImportGoodList;
import io.terminus.snz.requirement.service.ImportGoodListService;
import io.terminus.snz.requirement.service.ImportGoodService;
import io.terminus.snz.requirement.service.ModuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * About to discard warning!!!
 * 会在下周重构时移动到 snz-web
 *
 * Date: 7/15/14
 * Time: 16:54
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */

@Slf4j
@Controller
@RequestMapping("/api/extend")
public class HEImportGoodLists {

    @Autowired
    ImportGoodListService importGoodListService;

    @Autowired
    ImportGoodService importGoodService;

    @Autowired
    ModuleService moduleService;


    @ResponseBody
    @RequestMapping(value = "/import-good-lists", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<Paging<ImportGoodList>> pagingBy(@RequestParam("pid") Long purchaserId,
                                                     @RequestParam(value = "seriesId", required = false) Long seriesId,
                                                     @RequestParam(value = "moduleNum", required = false) String moduleNum,
                                                     @RequestParam(value = "pageNo", required = false) Integer pageNo,
                                                     @RequestParam(value = "size", required = false) Integer size) {


        return importGoodListService.pagingBy(purchaserId, seriesId, moduleNum, pageNo, size);
    }

    @ResponseBody
    @RequestMapping(value = "/import-goods", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<ImportGoodDto>> findImportGoodBy(Long moduleId,
                                                          Integer pageNo, Integer size) {

        return importGoodService.findByModuleId(moduleId);
    }
}
