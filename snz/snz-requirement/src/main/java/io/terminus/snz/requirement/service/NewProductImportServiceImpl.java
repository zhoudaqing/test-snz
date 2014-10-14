package io.terminus.snz.requirement.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.dao.NewProductImportDao;
import io.terminus.snz.requirement.dao.NewProductStepDao;
import io.terminus.snz.requirement.dto.NewProductStepsDto;
import io.terminus.snz.requirement.manager.NewProductImportManager;
import io.terminus.snz.requirement.model.NewProductImport;
import io.terminus.snz.requirement.model.NewProductStep;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * 新品导入信息
 *
 * @author wanggen
 */
@Service
@Slf4j
public class NewProductImportServiceImpl implements NewProductImportService {

    @Autowired
    private NewProductImportDao newProductImportDao;

    @Autowired
    private NewProductStepDao newProductStepDao;

    @Autowired
    private NewProductImportManager newProductImportManager;

    @Override
    public Response<Long> create(NewProductImport newProductImport) {
        Response<Long> resp = new Response<Long>();
        if (newProductImport == null) {
            log.error("param [newProductImport] can not be null");
            resp.setError("params.not.null");
            return resp;
        }
        try {
            Long id = newProductImportManager.importNPI(newProductImport);
            resp.setResult(id);
            return resp;
        } catch (Exception e) {
            resp.setError("snz_new_product_imports.insert.failed");
            log.error("Failed to insert into `snz_new_product_imports` with param:{}", newProductImport, e);
            return resp;
        }
    }

    @Override
    public Response<NewProductImport> findById(Long id) {
        Response<NewProductImport> resp = new Response<NewProductImport>();
        if (id == null) {
            log.error("param id can not be null when query");
            resp.setError("params.not.null");
            return resp;
        }
        try {
            NewProductImport newProductImport = newProductImportDao.findById(id);
            if (newProductImport == null) {
                log.warn("Failed to findById from `snz_new_product_imports` with param:{}", id);
                resp.setError("snz_new_product_imports.select.failed");
                return resp;
            }
            resp.setResult(newProductImport);
            return resp;
        } catch (Exception e) {
            resp.setError("snz_new_product_imports.select.failed");
            log.error("Failed to findById from `snz_new_product_imports` with param:{}", id, e);
            return resp;
        }
    }

    @Override
    public Response<List<NewProductImport>> findByIds(List<Long> ids) {
        Response<List<NewProductImport>> resp = new Response<List<NewProductImport>>();
        if (ids == null) {
            log.error("param ids can not be null when findByIds");
            resp.setError("params.not.null");
            return resp;
        }
        try {
            List<NewProductImport> newProductImports = newProductImportDao.findByIds(ids);
            if (newProductImports == null || newProductImports.size() < 1) {
                log.warn("Failed to findByIds from `snz_new_product_imports` with param:{}", ids);
                resp.setError("snz_new_product_imports.select.failed");
                return resp;
            }
            resp.setResult(newProductImports);
            return resp;
        } catch (Exception e) {
            resp.setError("snz_new_product_imports.select.failed");
            log.error("Failed to findByIds from `snz_new_product_imports` with param:{}", ids, e);
            return resp;
        }
    }


    @Override
    public Response<List<NewProductImport>> findByModuleNum(String moduleNum) {
        Response<List<NewProductImport>> resp = new Response<List<NewProductImport>>();
        try {
            List<NewProductImport> newProductImports = newProductImportDao.findByModuleNum(moduleNum);
            if (newProductImports.size() < 1) {
                log.warn("Failed to findByModuleNum from `snz_new_product_imports` with moduleNum:{}", moduleNum);
                resp.setError("snz_new_product_imports.select.failed");
                return resp;
            }
            resp.setResult(newProductImports);
            return resp;
        } catch (Exception e) {
            resp.setError("snz_new_product_imports.select.failed");
            log.error("Failed to findByModuleNum from `snz_new_product_imports` with moduleNum:{}", moduleNum, e);
            return resp;
        }
    }


    @Override
    public Response<List<NewProductStepsDto>> findNPIStepsByModuleNum(String moduleNum) {

        Response<List<NewProductStepsDto>> resp = new Response<List<NewProductStepsDto>>();
        try {
            List<NewProductStep> steps = newProductStepDao.findByModuleNum(moduleNum);
            if (steps == null || steps.size() <= 0) {
                resp.setResult(Collections.EMPTY_LIST);
                return resp;
            }
            Collections.sort(steps, new Comparator<NewProductStep>() {
                public int compare(NewProductStep o1, NewProductStep o2) {
                    return o1.getRealDatetime().compareTo(o2.getRealDatetime());
                }
            });
            Map<String, NewProductStepsDto> supplierSteps = Maps.newHashMap();
            NewProductStepsDto stepsDto;
            for (NewProductStep step : steps){
                stepsDto=supplierSteps.get(step.getSupplierName());
                if(stepsDto == null) {
                    stepsDto=NewProductStepsDto.create(step.getModuleNum(),step.getSupplierCode(), step.getSupplierName());
                    supplierSteps.put(step.getSupplierName(), stepsDto);
                }
                MethodUtils.invokeMethod(stepsDto, "setStep" + step.getStep(), step);
                stepsDto.setCurrStep(step.getStep());
            }
            resp.setResult(Lists.newArrayList(supplierSteps.values()));
        } catch (Exception e) {
            resp.setError("snz_new_product_imports.select.failed");
            log.error("Failed to find new product steps when moduleNum=[{}]", moduleNum, e);
        }
        return resp;
    }


    @Override
    public Response<Integer> update(NewProductImport newProductImport) {
        Response<Integer> resp = new Response<Integer>();
        if (newProductImport == null) {
            log.error("param newProductImport can not be null when update");
            resp.setError("params.not.null");
            return resp;
        }
        try {
            resp.setResult(newProductImportDao.update(newProductImport));
            return resp;
        } catch (Exception e) {
            resp.setError("snz_new_product_imports.update.failed");
            log.error("Failed to update table `snz_new_product_imports` with param:{}", newProductImport, e);
            return resp;
        }
    }

    @Override
    public Response<Integer> deleteByIds(List<Long> ids) {
        Response<Integer> resp = new Response<Integer>();
        if (ids == null) {
            log.error("param can not be null when deleteByIds");
            resp.setError("params.not.null");
            return resp;
        }
        try {
            resp.setResult(newProductImportDao.deleteByIds(ids));
            return resp;
        } catch (Exception e) {
            resp.setError("snz_new_product_imports.delete.failed");
            log.error("Failed to deleteByIds from `snz_new_product_imports` with param:{}", ids, e);
            return resp;
        }
    }
}
