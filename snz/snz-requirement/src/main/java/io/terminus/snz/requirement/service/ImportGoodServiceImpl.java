package io.terminus.snz.requirement.service;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.dao.ImportGoodDao;
import io.terminus.snz.requirement.dao.ImportGoodRowDao;
import io.terminus.snz.requirement.dto.ImportGoodCurrentStageDto;
import io.terminus.snz.requirement.dto.ImportGoodDto;
import io.terminus.snz.requirement.model.ImportGood;
import io.terminus.snz.requirement.model.ImportGoodRow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.base.Throwables.getStackTraceAsString;
import static io.terminus.common.utils.Arguments.*;
import static io.terminus.snz.requirement.model.ImportGood.STAGE;

/**
 * Date: 7/11/14
 * Time: 15:09
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */

@Slf4j
@Service
public class ImportGoodServiceImpl implements ImportGoodService {

    @Autowired
    private ImportGoodDao importGoodDao;

    @Autowired
    private ImportGoodRowDao importGoodRowDao;

    @Override
    public Response<ImportGoodCurrentStageDto> findImportGoodAndCurrentRow(Long moduleId) {
        Response<ImportGoodCurrentStageDto> result = new Response<ImportGoodCurrentStageDto>();
        ImportGoodCurrentStageDto dto = new ImportGoodCurrentStageDto();

        try {
            checkArgument(positive(moduleId), "import.good.illegal.argument");
            ImportGood param = new ImportGood();
            param.setModuleId(moduleId);

            ImportGood found = importGoodDao.findOneBy(param);
            if (isNull(found)) {
                log.error("find import good by module id fail, moduleId:{}", moduleId);
                result.setError("import.good.find.fail");
                return result;
            }

            dto.setImportGood(found);
            if (Objects.equal(found.getStage(), STAGE.UNDEFINED.toNumber())) {
                result.setResult(dto);
                return result;
            }

            Long stageId = found.getCurrentRowID();
            checkState(notNull(stageId), "import.good.stage.undefined");
            ImportGoodRow row = importGoodRowDao.findById(stageId);
            checkState(notNull(row), "import.good.row.find.fail");
            dto.setImportGoodRow(row);
            result.setResult(dto);

        } catch (IllegalArgumentException e) {
            log.error("`findImportGoodAndCurrentRow` invoke with illegal moduleId:{}, e:{}",
                    moduleId, getStackTraceAsString(e));
            result.setError(e.getMessage());
            return result;
        }catch (IllegalStateException e) {
            log.error("`findImportGoodAndCurrentRow` invoke with illegal moduleId:{}, e:{}",
                    moduleId, getStackTraceAsString(e));
            result.setError(e.getMessage());
            return result;
        }catch (Exception e) {
            log.error("`findImportGoodAndCurrentRow` invoke fail, moduleId:{}, e:{}",
                    moduleId, getStackTraceAsString(e));
            result.setError("import.good.find.fail");
            return result;
        }

        return result;
    }

    @Override
    public Response<List<ImportGoodDto>> findByModuleId(Long moduleId) {
        Response<List<ImportGoodDto>> result = new Response<List<ImportGoodDto>>();

        try {
            checkArgument(notNull(moduleId), "Module id should be a positive number!");

            ImportGood param = new ImportGood();
            param.setModuleId(moduleId);
            List<ImportGood> importGoods = importGoodDao.findBy(param);
            if (isNullOrEmpty(importGoods)) {
                result.setResult(Collections.<ImportGoodDto>emptyList());
                return result;
            }

            List<ImportGoodDto> goodDtos = Lists.newArrayList();
            for (ImportGood row: importGoods) {
                List<Long> rowIds = row.getAvailableRowId();
                List<ImportGoodRow> rows = importGoodRowDao.findByIds(rowIds);

                // rows 为空也正常返回
                ImportGoodDto dto = new ImportGoodDto();
                dto.setImportGood(row);
                dto.setImportGoodRowList(rows);
                goodDtos.add(dto);
            }

            result.setResult(goodDtos);
        } catch (IllegalArgumentException e) {
            log.error("`findByModuleId` invoke fail with illegal argument, moduleId:{}, e:{}",
                    moduleId, getStackTraceAsString(e));
            result.setError("import.good.illegal.argument");
            return result;
        } catch (Exception e) {
            log.error("`findByModuleId` invoke fail, with module id:{}, e:{}",
                    moduleId, getStackTraceAsString(e));
            result.setError("import.good.find.fail");
            return result;
        }

        return result;
    }

    @Override
    public Response<Boolean> updateProgress(ImportGoodRow row, Long igid, Integer stage) {
        Response<Boolean> result = new Response<Boolean>();

        try {
            checkArgument(notNull(row), "import.good.update.illegal.arg");
            checkArgument(notNull(igid), "import.good.update.illegal.arg");

            ImportGood found = importGoodDao.findById(igid);
            checkState(notNull(found), "import.good.find.fail");

            Long rowId = found.getRowID(row.getStage());
            ImportGoodRow foundRow = importGoodRowDao.findById(rowId);
            checkState(notNull(foundRow), "import.good.row.not.exist");

            foundRow.setProgress(row.getProgress());
            if (!foundRow.isValid()) {
                result.setError("import.good.row.invalid");
                return result;
            }
            importGoodRowDao.updateForce(foundRow);
            result.setResult(true);

        } catch (IllegalArgumentException e) {
            log.error("`updateProgress` invoke with illegal argument, row:{}, moduleId:{}, e:{}",
                    row, igid, getStackTraceAsString(e));
            result.setError(e.getMessage());
            return result;
        } catch (IllegalStateException e) {
            log.error("`updateProgress` invoke with illegal state, row:{}, moduleId:{}, e:{}",
                    row, igid, getStackTraceAsString(e));
            result.setError(e.getMessage());
            return result;
        } catch (Exception e) {
            log.error("`updateProgress` invoke fail. row:{}, moduleId:{}, e:{}",
                    row, igid, getStackTraceAsString(e));
            result.setError("import.good.update.fail");
            return result;
        }

        return result;
    }
}
