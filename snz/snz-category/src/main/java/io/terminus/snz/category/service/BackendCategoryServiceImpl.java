package io.terminus.snz.category.service;

import com.google.common.base.Throwables;
import io.terminus.common.utils.Splitters;
import io.terminus.pampas.common.Response;
import io.terminus.snz.category.dao.BackendCategoryPropertyDao;
import io.terminus.snz.category.manager.BackendCategoryManager;
import io.terminus.snz.category.model.BackendCategory;
import io.terminus.snz.category.model.BackendCategoryProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static io.terminus.common.utils.Arguments.isNull;

/**
 * Author:  <a href="mailto:i@terminus.io">jlchen</a>
 * Date: 2014-06-11
 */
@Service
@Slf4j
public class BackendCategoryServiceImpl implements BackendCategoryService {

    @Autowired
    private BackendCategoryManager backendCategoryManager;

    @Autowired
    private BackendCategoryPropertyDao backendCategoryPropertyDao;

    /**
     * 创建后台类目
     * @param bc 待创建的后台类目
     * @return 新创建类目的id
     */
    @Override
    public Response<Long> create(BackendCategory bc) {
        Response<Long> result = new Response<Long>();
        if (bc == null) {
            result.setError("category.param.null");
            return result;
        }
        try {
            result.setResult(backendCategoryManager.create(bc));
        } catch (Exception e) {
            log.error("create BackendCategory({}) failed, error code={}", bc, Throwables.getStackTraceAsString(e));
            result.setError("category.create.fail");
        }
        return result;
    }

    /**
     * 更新后台类目
     *
     * @param bc 待更新的后台类目
     * @return 是否更新成功
     */
    @Override
    public Response<Boolean> update(BackendCategory bc) {
        Response<Boolean> result = new Response<Boolean>();
        if (bc == null) {
            result.setError("category.param.null");
            return result;
        }
        try {
            result.setResult(backendCategoryManager.update(bc));
        } catch (Exception e) {
            log.error("update BackendCategory({}) failed, error code={}", bc, Throwables.getStackTraceAsString(e));
            result.setError("category.update.fail");
        }
        return result;
    }

    @Override
    public Response<Boolean> delete(Long id) {
        Response<Boolean> result = new Response<Boolean>();
        if (id == null) {
            result.setError("category.param.null");
            return result;
        }
        try {
            result.setResult(backendCategoryManager.delete(id));
        } catch (Exception e) {
            log.error("delete BackendCategory(id={}) failed, error code={}", id, Throwables.getStackTraceAsString(e));
            result.setError("category.delete.fail");
        }
        return result;
    }

    /**
     * 根据id寻找后台类目
     *
     * @param id 后台类目id
     * @return 对应的后台类目
     */
    @Override
    public Response<BackendCategory> findById(Long id) {
        Response<BackendCategory> result = new Response<BackendCategory>();
        if (id == null) {
            result.setError("category.param.null");
            return result;
        }
        try {
            BackendCategory bc = backendCategoryManager.findById(id);
            if (bc == null) {
                bc = new BackendCategory();
            }
            result.setResult(bc);
        } catch (Exception e) {
            log.error("find BackendCategory(id={}) failed, error code={}", id, Throwables.getStackTraceAsString(e));
            result.setError("category.query.fail");
        }
        return result;
    }

    @Override
    public Response<List<BackendCategory>> findByIds(List<Long> ids) {
        Response<List<BackendCategory>> result = new Response<List<BackendCategory>>();
        if (ids == null || ids.isEmpty()) {
            result.setError("category.param.null");
            return result;
        }
        try {
            List<BackendCategory> bcs = backendCategoryManager.findByIds(ids);
            if (bcs == null) {
                bcs = Collections.emptyList();
            }
            result.setResult(bcs);
        } catch (Exception e) {
            log.error("find BackendCategories by ids={} failed, error code={}", ids, Throwables.getStackTraceAsString(e));
            result.setError("category.query.fail");
        }
        return result;
    }

    /**
     * 根据id寻找下级的后台类目
     *
     * @param id 后台类目parent id
     * @return 对应的后台类目孩子列表
     */
    @Override
    public Response<List<BackendCategory>> findChildrenOf(Long id) {
        Response<List<BackendCategory>> result = new Response<List<BackendCategory>>();
        if (id == null) {
            result.setError("category.param.null");
            return result;
        }
        try {
            List<BackendCategory> bcs = backendCategoryManager.findChildrenOf(id);
            if (bcs == null) {
                bcs = Collections.emptyList();
            }
            result.setResult(bcs);
        } catch (Exception e) {
            log.error("find children of BackendCategory(id={}) failed, error code={}", id, Throwables.getStackTraceAsString(e));
            result.setError("category.query.fail");
        }
        return result;
    }

    @Override
    public Response<List<BackendCategory>> findByLevels(Integer level) {
        Response<List<BackendCategory>> result = new Response<List<BackendCategory>>();
        if (level == null) {
            result.setError("category.param.null");
            return result;
        }
        try {
            List<BackendCategory> bcs = backendCategoryManager.findByLevels(level);
            if (bcs == null) {
                bcs = Collections.emptyList();
            }
            result.setResult(bcs);
        } catch (Exception e) {
            log.error("find children of BackendCategory(level={}) failed, error code={}", level, Throwables.getStackTraceAsString(e));
            result.setError("category.query.fail");
        }
        return result;
    }

    @Override
    public Response<List<BackendCategory>> findByParentIdAndLevel(Integer parentId, Integer level) {
        Response<List<BackendCategory>> result = new Response<List<BackendCategory>>();
        if (parentId == null || level == null) {
            result.setError("category.param.null");
            return result;
        }
        try {
            List<BackendCategory> bcs = backendCategoryManager.findByParentIdAndLevel(parentId, level);
            if (bcs == null) {
                bcs = Collections.emptyList();
            }
            result.setResult(bcs);
        } catch (Exception e) {
            log.error("find children of BackendCategory(parentId={}, level={}) failed, error code={}", parentId, level, Throwables.getStackTraceAsString(e));
            result.setError("category.query.fail");
        }
        return result;
    }

    @Override
    public Response<Long> createProperty(BackendCategoryProperty bcp) {
        Response<Long> result = new Response<Long>();
        if (bcp == null || bcp.getBcId() == null) {
            result.setError("category.property.bcid.null");
            return result;
        }
        try {
            result.setResult(backendCategoryPropertyDao.create(bcp));
        } catch (Exception e) {
            log.error("create BackendCategoryProperty({}) failed, error code={}", bcp, Throwables.getStackTraceAsString(e));
            result.setError("category.property.create.fail");
        }
        return result;
    }

    @Override
    public Response<Boolean> updateProperty(Long id, BackendCategoryProperty bcp) {
        Response<Boolean> result = new Response<Boolean>();
        if (bcp == null) {
            result.setError("category.param.null");
            return result;
        }
        if (id == null) {
            result.setError("category.property.id.null");
            return result;
        }
        bcp.setId(id);
        try {
            result.setResult(backendCategoryPropertyDao.update(bcp));
        } catch (Exception e) {
            log.error("update BackendCategoryProperty({}) failed, error code={}", bcp, Throwables.getStackTraceAsString(e));
            result.setError("category.property.update.fail");
        }
        return result;
    }

    @Override
    public Response<Boolean> deleteProperty(Long id) {
        Response<Boolean> result = new Response<Boolean>();
        if (id == null) {
            result.setError("category.property.id.null");
            return result;
        }
        try {
            result.setResult(backendCategoryPropertyDao.delete(id));
        } catch (Exception e) {
            log.error("delete BackendCategoryProperty(id={}) failed, error code={}", id, Throwables.getStackTraceAsString(e));
            result.setError("category.property.delete.fail");
        }
        return result;
    }

    @Override
    public Response<BackendCategoryProperty> findPropertyById(Long id) {
        Response<BackendCategoryProperty> result = new Response<BackendCategoryProperty>();
        if (id == null) {
            result.setError("category.property.id.null");
            return result;
        }
        try {
            BackendCategoryProperty bcp = backendCategoryPropertyDao.findById(id);
            if (bcp == null) {
                bcp = new BackendCategoryProperty();
            }
            result.setResult(bcp);
        } catch (Exception e) {
            log.error("find BackendCategoryProperty by id={} failed, error code={}", id, Throwables.getStackTraceAsString(e));
            result.setError("category.property.query.fail");
        }
        return result;
    }

    @Override
    public Response<List<BackendCategoryProperty>> findPropertiesByBcId(Long bcId) {
        Response<List<BackendCategoryProperty>> result = new Response<List<BackendCategoryProperty>>();
        if (bcId == null) {
            result.setError("category.property.bcid.null");
            return result;
        }
        try {
            List<BackendCategoryProperty> res = backendCategoryPropertyDao.findByBcId(bcId);
            if (res == null) {
                res = Collections.emptyList();
            }
            result.setResult(res);
        } catch (Exception e) {
            log.error("find BackendCategoryProperty by bcId={} failed, error code={}", bcId, Throwables.getStackTraceAsString(e));
            result.setError("category.property.query.fail");
        }
        return result;
    }

    @Override
    public Response<List<BackendCategoryProperty>> findPropertiesByBcIds(List<Long> bcIds) {
        Response<List<BackendCategoryProperty>> result = new Response<List<BackendCategoryProperty>>();

        if (bcIds == null || bcIds.isEmpty()) {
            log.error("find backend category property need backend category ids.");
            result.setError("category.property.bcid.null");
            return result;
        }
        try {
            List<BackendCategoryProperty> res = backendCategoryPropertyDao.findByBcIds(bcIds);
            if (res == null) {
                res = Collections.emptyList();
            }
            result.setResult(res);
        } catch (Exception e) {
            log.error("find BackendCategoryProperty by bcIds={} failed, error code={}", bcIds, Throwables.getStackTraceAsString(e));
            result.setError("category.property.query.fail");
        }

        return result;
    }

    @Override
    public Response<List<BackendCategoryProperty>> findPropertiesByBcIdWithFactoryNums(Long bcId, String factoryNums) {
        Response<List<BackendCategoryProperty>> result = new Response<List<BackendCategoryProperty>>();
        if (isNull(bcId)) {
            log.error("bcId must not null");
            result.setError("category.property.bcid.null");
            return result;
        }
        try {
            List<String> factories = Splitters.COMMA.splitToList(factoryNums);
            List<BackendCategoryProperty> res = backendCategoryPropertyDao.findByBcIdWithFactoryNums(bcId, factories);
            if (isNull(res)) {
                res = Collections.emptyList();
            }
            result.setResult(res);
        } catch (Exception e) {
            log.error("findPropertiesByBcIdWithFactoryNums(bcId={}, factoryNums={}) failed, cause:{}", bcId, factoryNums, Throwables.getStackTraceAsString(e));
            result.setError("category.property.query.fail");
        }
        return result;
    }

}
