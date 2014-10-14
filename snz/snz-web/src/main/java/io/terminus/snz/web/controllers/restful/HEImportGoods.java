package io.terminus.snz.web.controllers.restful;

import io.terminus.pampas.common.Response;
import io.terminus.pampas.engine.MessageSources;
import io.terminus.snz.requirement.dto.ImportGoodDto;
import io.terminus.snz.requirement.model.ImportGoodRow;
import io.terminus.snz.requirement.service.ImportGoodListService;
import io.terminus.snz.requirement.service.ImportGoodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;

/**
 * About to discard warning!!!
 * 会在下周重构时移动到 snz-web
 *
 * Date: 7/15/14
 * Time: 14:27
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */

@Slf4j
@Controller
@RequestMapping("/extend")
public class HEImportGoods {

    @Autowired
    MessageSources messageSources;

    @Autowired
    ImportGoodService importGoodService;

    @Autowired
    ImportGoodListService importGoodListService;

    @ResponseBody
    @RequestMapping(value = "/import-good", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<Boolean> update(@RequestParam("progress") Date progress,
                                    @RequestParam("igid") Long importGoodID,
                                    @RequestParam("stage") Integer stage ) {

        Response<Boolean> result = new Response<Boolean>();

        try {
            ImportGoodRow param = new ImportGoodRow();
            param.setProgress(progress);
            Response<Boolean> tryUpdateIG = importGoodService.updateProgress(param, importGoodID, stage);
            checkState(tryUpdateIG.isSuccess(), tryUpdateIG.getError());

            result.setResult(true);
        } catch (IllegalArgumentException e) {
            log.error("`update` invoke with illegal argument, progress:{}, inCharge:{}, moduleId:{}",
                    progress, importGoodID);
            result.setError(messageSources.get("import.good.illegal.argument"));
            return result;
        } catch (Exception e) {
            log.error("`update` invoke fail. with row:{}, progress:{}, inCharge:{}, moduleId:{}",
                    progress, importGoodID);
            result.setError(messageSources.get("import.good.update.fail"));
            return result;
        }

        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/import-goods", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<ImportGoodDto>> pagingImportGoodList(@RequestParam("pid") Long purchaserId,
                                                              @RequestParam("igid") Long importGoodId,
                                                              @RequestParam("pageNo") Integer pageNo,
                                                              @RequestParam("size") Integer size) {
        Response<List<ImportGoodDto>> result = new Response<List<ImportGoodDto>>();

        try {
            importGoodListService.pagingByPurchaserId(purchaserId, pageNo, size);

        } catch (Exception e) {
            log.error("`pagingImportGoodList` invoke fail. ");
            result.setError("");
            return result;
        }
        return null;
    }
}
