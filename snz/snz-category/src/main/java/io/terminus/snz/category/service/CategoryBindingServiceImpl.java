package io.terminus.snz.category.service;

import com.google.common.base.Throwables;
import io.terminus.pampas.common.Response;
import io.terminus.snz.category.dao.CategoryBindingDao;
import io.terminus.snz.category.dto.CategoryPair;
import io.terminus.snz.category.dto.CategoryPairWithBcName;
import io.terminus.snz.category.model.BackendCategory;
import io.terminus.snz.category.model.CategoryBinding;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by yangzefeng on 14-6-26
 */
@Service @Slf4j
public class CategoryBindingServiceImpl implements CategoryBindingService {

    @Autowired
    private CategoryBindingDao categoryBindingDao;

    @Autowired
    private BackendCategoryService backendCategoryService;

    @Override
    public Response<Long> create(CategoryBinding categoryBinding) {
        Response<Long> result = new Response<Long>();

        if(categoryBinding == null) {
            log.error("params can not be null when create categoryBinding");
            result.setError("illegal.params");
            return result;
        }

        try {
            Long id = categoryBindingDao.create(categoryBinding);
            result.setResult(id);
            return result;
        }catch(Exception e) {
            log.error("fail to create categoryBinding {}, cause:{}",
                    categoryBinding, Throwables.getStackTraceAsString(e));
            result.setError("categoryBinding.create.fail");
            return result;
        }
    }

    @Override
    public Response<Boolean> update(CategoryBinding categoryBinding) {
        Response<Boolean> result = new Response<Boolean>();

        if(categoryBinding.getId() == null) {
            log.error("id can not be null when update categoryBinding");
            result.setError("illegal.params");
            return result;
        }

        try {
            categoryBindingDao.update(categoryBinding);
            result.setResult(Boolean.TRUE);
            return result;
        }catch (Exception e) {
            log.error("fail to update categoryBinding {}, cause:{}",
                    categoryBinding, Throwables.getStackTraceAsString(e));
            result.setError("categoryBinding.update.fail");
            return result;
        }
    }

    @Override
    public Response<Boolean> delete(Long id) {
        Response<Boolean> result = new Response<Boolean>();

        if(id == null) {
            log.error("id can not be null when delete categoryBinding");
            result.setError("illegal.params");
            return result;
        }

        try {
            categoryBindingDao.delete(id);
            result.setResult(Boolean.TRUE);
            return result;
        }catch (Exception e) {
            log.error("fail to delete categoryBinding id={}, cause:{}",
                    id, Throwables.getStackTraceAsString(e));
            result.setError("categoryBinding.delete.fail");
            return result;
        }
    }

    @Override
    public Response<List<CategoryPair>> findByFcid(Long fcid) {
        Response<List<CategoryPair>> result = new Response<List<CategoryPair>>();

        if(fcid == null) {
            log.error("fcid can not be null when find categoryPair");
            result.setError("illegal.params");
            return result;
        }

        try {
            List<CategoryPair> pairs = categoryBindingDao.findByFcid(fcid);
            if (pairs == null) {
                pairs = Collections.emptyList();
            }
            result.setResult(pairs);
            return result;
        }catch (Exception e) {
            log.error("fail to find categoryPair by fcid={}, cause:{}",
                    fcid, Throwables.getStackTraceAsString(e));
            result.setError("categoryPair.query.fail");
            return result;
        }
    }

    @Override
    public Response<List<CategoryPairWithBcName>> findBcNamesByFcid(Long fcid) {
        Response<List<CategoryPairWithBcName>> result = new Response<List<CategoryPairWithBcName>>();
        Response<List<CategoryPair>> categoryPairR = findByFcid(fcid);
        if (!categoryPairR.isSuccess()) {
            result.setError(categoryPairR.getError());
            return result;
        }
        try {
            // TODO: need optimize : backendCategoryService.findByIds
            List<CategoryPairWithBcName> categoryPairWithBcNames = new ArrayList<CategoryPairWithBcName>();
            for (CategoryPair categoryPair : categoryPairR.getResult()) {
                if (categoryPair == null) {
                    continue;
                }
                CategoryPairWithBcName categoryPairWithBcName = new CategoryPairWithBcName();
                categoryPairWithBcName.setCategoryPair(categoryPair);
                Response<BackendCategory> backR = backendCategoryService.findById(categoryPair.getId());
                if (!backR.isSuccess()) {
                    result.setError(backR.getError());
                    return result;
                }
                categoryPairWithBcName.setBcName(backR.getResult().getName());
                categoryPairWithBcNames.add(categoryPairWithBcName);
            }
            result.setResult(categoryPairWithBcNames);
        } catch (Exception e) {
            log.error("findBcNamesByFcid(fcid={}) failed, cause:{}", fcid, Throwables.getStackTraceAsString(e));
            result.setError("categoryPair.query.fail");
        }
        return result;
    }

    @Override
    public Response<CategoryBinding> findBy(Long fcid) {
        Response<CategoryBinding> result = new Response<CategoryBinding>();

        if(fcid == null) {
            log.error("fcid can not be null when find categoryBinding");
            result.setError("illegal.params");
            return result;
        }

        try {
            CategoryBinding binding = categoryBindingDao.findBy(fcid);
            if (binding == null) {
                binding = new CategoryBinding();
            }
            result.setResult(binding);
            return result;
        }catch (Exception e) {
            log.error("fail to find categoryBinding by fcid={}, cause:{}",
                    fcid, Throwables.getStackTraceAsString(e));
            result.setError("categoryBinding.query.fail");
            return result;
        }
    }

    @Override
    public Response<CategoryBinding> findById(Long id) {
        Response<CategoryBinding> result = new Response<CategoryBinding>();

        if(id == null) {
            log.error("id can not be null when find categoryBinding");
            result.setError("illegal.params");
            return result;
        }

        try {
            CategoryBinding binding = categoryBindingDao.findById(id);
            if (binding == null) {
                binding = new CategoryBinding();
            }
            result.setResult(binding);
            return result;
        }catch (Exception e) {
            log.error("fail to find categoryBinding by id={}, cause:{}",
                    id, Throwables.getStackTraceAsString(e));
            result.setError("categoryBinding.query.fail");
            return result;
        }
    }
}
