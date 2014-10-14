package io.terminus.snz.category.manager;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import io.terminus.common.exception.ServiceException;
import io.terminus.snz.category.dao.BaseCategoryDao;
import io.terminus.snz.category.model.CategoryBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Author:  <a href="mailto:i@terminus.io">jlchen</a>
 * Date: 2014-06-11
 */
public abstract class BaseCategoryManager<T extends CategoryBase> {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public abstract BaseCategoryDao<T> getCategoryDao();

    private final LoadingCache<Long, List<T>> findChildrenOfCache = CacheBuilder.newBuilder()
            .expireAfterAccess(20, TimeUnit.MINUTES)
            .build(
                    new CacheLoader<Long, List<T>>() {
                        @Override
                        public List<T> load(Long id) throws Exception {
                            return getCategoryDao().findChildrenOf(id);
                        }
                    }
            );

    /**
     * 创建前台或者后台类目
     *
     * @param category 待创建的类目
     * @return  新创建类目的id
     */
    @Transactional
    public Long create(T category) {
        if (category.getParentId() == null) {//表示一级类目
            category.setParentId(0L);
        }
        category.setHasChildren(false);//新创建的类目是没有children的

        int categoryLevel = 1; // 默认是一级类目
        if (category.getParentId() > 0L) {//不是一级类目
            T parent = findById(category.getParentId());
            if(parent == null){
                log.error("category(id={}) is not found", category.getParentId());
                throw new ServiceException("category.not.found");
            }
            if(!parent.getHasChildren()) { //将parent 的hasChildren设置为true
                parent.setHasChildren(Boolean.TRUE);
                update(parent);
            }
            categoryLevel = parent.getLevel()+1;//将层级设为parent level的下一级
        }

        category.setLevel(categoryLevel);//设置层级
        Long id = getCategoryDao().create(category);
        findChildrenOfCache.refresh(id);
        findChildrenOfCache.refresh(category.getParentId());
        return id;
    }

    @Transactional
    public boolean update(T category){
        boolean success = getCategoryDao().update(category);
        findChildrenOfCache.refresh(findById(category.getId()).getParentId());
        return success;
    }

    @Transactional
    public boolean delete(Long id) {
        BaseCategoryDao<T> dao = getCategoryDao();
        Long parentId = dao.findById(id).getParentId();
        T parent = dao.findById(parentId);
        List<T> children = dao.findChildrenOf(parentId);
        if (children.size() <= 1) {
            parent.setHasChildren(false);
            this.update(parent);
        }
        boolean success = dao.delete(id);
        findChildrenOfCache.refresh(parentId);
        return success;
    }

    public T findById(Long id){
        return getCategoryDao().findById(id);
    }

    public List<T> findChildrenOf(Long id){
        try {
            return findChildrenOfCache.get(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<T> findAllLoop(Long id) {
        List<T> children = findChildrenOf(id);
        List<T> result = Lists.newArrayList(children);
        for (T child : children) {
            if (child.getHasChildren()) {
                result.addAll(findAllLoop(child.getId()));
            }
        }
        return result;
    }

    public List<T> findByLevels(Integer level){
        return getCategoryDao().findByLevels(level);
    }

    public List<T> findByParentIdAndLevel(Integer parentId, Integer level){
        return getCategoryDao().findByParentIdAndLevel(parentId, level);
    }

    public List<T> findByIds(List<Long> ids) {
        return getCategoryDao().findByIds(ids);
    }

    public T findByLevelAndName(Integer level, String name) {
        return getCategoryDao().findByLevelAndName(level, name);
    }
}
