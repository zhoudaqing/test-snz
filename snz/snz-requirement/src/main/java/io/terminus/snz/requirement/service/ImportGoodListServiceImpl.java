package io.terminus.snz.requirement.service;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import io.terminus.common.model.Paging;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.dto.ModulesDto;
import io.terminus.snz.requirement.model.ImportGoodList;
import io.terminus.snz.requirement.model.Module;
import io.terminus.snz.requirement.model.Requirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.base.Throwables.getStackTraceAsString;
import static io.terminus.common.utils.Arguments.isNullOrEmpty;
import static io.terminus.common.utils.Arguments.positive;

/**
 * Date: 7/11/14
 * Time: 15:01
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */

@Slf4j
@Service
public class ImportGoodListServiceImpl implements ImportGoodListService {

    @Autowired
    private ModuleService moduleService;

    @Override
    public Response<Paging<ImportGoodList>> pagingByPurchaserId(Long purchaserId, Integer pageNo, Integer size) {
        Response<Paging<ImportGoodList>> result = new Response<Paging<ImportGoodList>>();
        result.setResult(new Paging<ImportGoodList>(0l, Collections.<ImportGoodList>emptyList()));
        return result;
    }

    @Override
    public Response<Paging<ImportGoodList>> pagingBy(@Nullable Long purchaserId,
                                                    @Nullable Long seriesId, @Nullable String moduleNo,
                                                    @Nullable Integer pageNo, @Nullable Integer size) {

        Response<Paging<ImportGoodList>> result = new Response<Paging<ImportGoodList>>();

        try {
            checkArgument(Objects.equal(purchaserId, 0L)||positive(purchaserId),
                    "Purchaser's id must present and be a positive number!");

            Response<Paging<ModulesDto>> modulesGet = moduleService.pagingByPurchaserId(purchaserId, pageNo, size);
            checkState(modulesGet.isSuccess(), modulesGet.getError());
            List<ModulesDto> modules = modulesGet.getResult().getData();
            if (isNullOrEmpty(modules)) {
                result.setResult(new Paging<ImportGoodList>(0l, Collections.<ImportGoodList>emptyList()));
                return result;
            }

            List<ImportGoodList> importGoodListsDto = modulesDtoToImportGoodLists(modules);
            result.setResult(new Paging<ImportGoodList>(modulesGet.getResult().getTotal(), importGoodListsDto));
        } catch (IllegalStateException e) {
            log.error("`findBy` invoke fail, cause:{}", getStackTraceAsString(e));
            result.setError(e.getMessage());
            return result;
        } catch (IllegalArgumentException e) {
            log.error("`findBy` invoke with illegal argument: purchaserId:{}, seriesId:{}," +
                            "moduleNo:{}, pageNo:{}, size:{}, error:{}",
                    purchaserId, seriesId, moduleNo, pageNo, size, getStackTraceAsString(e));
            result.setError("import.good.list.illegal.argument");
            return result;
        } catch (Exception e) {
            log.error("`findBy` invoke fail with purchaserId:{}, seriesId:{}," +
                    "moduleNo:{}, pageNo:{}, size:{}, error:{}",
                    purchaserId, seriesId, moduleNo, pageNo, size, getStackTraceAsString(e));
            result.setError("import.good.list.find.fail");
            return result;
        }

        return result;
    }

    /**
     * 将 ModulesDto 初步处理为 ImportGoodList， 返回的结果
     * 不包括 productLine, progress, status, inCharge。
     *
     * @param modules    一般为从 ModuleService 得到的 paged ModulesDto
     * @return           初步处理的 ImportGoodList，
     */
    private List<ImportGoodList> modulesDtoToImportGoodLists(List<ModulesDto> modules) {
        List<ImportGoodList> listItems = Lists.newArrayList();

        for (ModulesDto dto : modules) {
            final Requirement r = dto.getRequirementFactoryDto().getRequirement();
            List<Module> list = dto.getModuleList();
            // skip empty hole
            if (isNullOrEmpty(list)) {
                continue;
            }

            // transform return un-serializable list, and function invoke lazy,
            // won't access db in function, compile section later.
            List<ImportGoodList> section = Lists.transform(list, new Function<Module, ImportGoodList>() {
                @Nullable
                @Override
                public ImportGoodList apply(Module m) {
                    ImportGoodList item = new ImportGoodList();
                    item.setRequirementId(r.getId());
                    item.setRequirementName(r.getName());
                    item.setModuleId(m.getId());
                    item.setModuleName(m.getModuleName());
                    item.setModuleNum(m.getModuleNum());
                    item.setSeriesId(m.getSeriesId());
                    item.setSeriesName(m.getSeriesName());

                    return item;
                }
            });

            // should be serializable
            listItems.addAll(ImmutableList.copyOf(section));
        }

        return listItems;
    }
}
