package io.terminus.snz.category.service;

import com.fasterxml.jackson.databind.JavaType;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Splitter;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import io.terminus.common.utils.JsonMapper;
import io.terminus.pampas.common.Response;
import io.terminus.snz.category.dao.CategoryBindingDao;
import io.terminus.snz.category.dto.CategoryPair;
import io.terminus.snz.category.dto.FrontEndCategoryNav;
import io.terminus.snz.category.manager.FrontendCategoryManager;
import io.terminus.snz.category.model.CategoryBinding;
import io.terminus.snz.category.model.FrontendCategory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author:  <a href="mailto:i@terminus.io">jlchen</a>
 * Date: 2014-06-11
 */
@Service @Slf4j
public class FrontendCategoryServiceImpl implements FrontendCategoryService {

    private static final Integer MAX_LEVEL = 3;

    private static final Splitter splitter = Splitter.on(",").omitEmptyStrings().trimResults();

    public static final JsonMapper JSON_MAPPER = JsonMapper.nonDefaultMapper();

    private static final JavaType JAVA_TYPE = JSON_MAPPER.createCollectionType(List.class, CategoryPair.class);

    //key:后台类目的Id
    private final LoadingCache<Long, LinkedHashSet<Long>> backToFrontCache = CacheBuilder.newBuilder()
            .expireAfterAccess(3, TimeUnit.MINUTES)
            .build(
                    new CacheLoader<Long, LinkedHashSet<Long>>() {
                        @Override
                        public LinkedHashSet<Long> load(Long key) throws Exception {
                            List<CategoryBinding> categoryBindings = categoryBindingDao.findAll();

                            List<Long> frontIds = Lists.newArrayList();
                            for (CategoryBinding categoryBinding : categoryBindings) {
                                List<CategoryPair> pairList = JSON_MAPPER.fromJson(categoryBinding.getBcs(), JAVA_TYPE);

                                for (CategoryPair categoryPair : pairList) {
                                    if(Objects.equal(categoryPair.getId() , key)){
                                        //查询到后台类目对象
                                        frontIds.add(categoryBinding.getFrontId());
                                    }
                                }
                            }

                            return new LinkedHashSet<Long>(frontIds);
                        }
                    });


    @Autowired
    private FrontendCategoryManager frontendCategoryManager;

    @Autowired
    private CategoryBindingDao categoryBindingDao;

    /**
     * 创建前台类目
     *
     * @param fc 待创建的前台类目
     * @return 新创建类目的id
     */
    @Override
    public Response<Long> create(FrontendCategory fc) {
        Response<Long> result = new Response<Long>();
        try {
            result.setResult(frontendCategoryManager.create(fc));
        } catch (Exception e) {
            log.error("create FrontendCategory({}) failed, error code={}", fc, Throwables.getStackTraceAsString(e));
            result.setError("category.create.fail");
        }
        return result;
    }

    /**
     * 更新前台类目
     *
     * @param fc 待更新的前台类目
     * @return 是否更新成功
     */
    @Override
    public Response<Boolean> update(FrontendCategory fc) {
        Response<Boolean> result = new Response<Boolean>();
        try {
            result.setResult(frontendCategoryManager.update(fc));
        } catch (Exception e) {
            log.error("update FrontendCategory({}) failed, error code={}", fc, Throwables.getStackTraceAsString(e));
            result.setError("category.update.fail");
        }
        return result;
    }

    @Override
    public Response<Boolean> delete(Long id) {
        Response<Boolean> result = new Response<Boolean>();
        try {
            result.setResult(frontendCategoryManager.delete(id));
        } catch (Exception e) {
            log.error("delete FrontendCategory(id={}) failed, error code={}", id, Throwables.getStackTraceAsString(e));
            result.setError("category.delete.fail");
        }
        return result;
    }

    /**
     * 根据id寻找前台类目
     *
     * @param id 前台类目id
     * @return 对应的前台类目
     */
    @Override
    public Response<FrontendCategory> findById(Long id) {
        Response<FrontendCategory> result = new Response<FrontendCategory>();
        try {
            checkNotNull(id);
            FrontendCategory fc = frontendCategoryManager.findById(id);
            if (fc == null) {
                fc = new FrontendCategory();
            }
            result.setResult(fc);
        } catch (Exception e) {
            log.error("find FrontendCategory(id={}) failed, error code={}", id, Throwables.getStackTraceAsString(e));
            result.setError("category.query.fail");
            return result;
        }
        return result;
    }

    /**
     * 根据id寻找下级的前台类目
     *
     * @param id 前台类目parent id
     * @return 对应的前台类目孩子列表
     */
    @Override
    public Response<List<FrontendCategory>> findChildrenOf(Long id) {
        Response<List<FrontendCategory>> result = new Response<List<FrontendCategory>>();
        try {
            checkNotNull(id);
            List<FrontendCategory> fcs = frontendCategoryManager.findChildrenOf(id);
            if (fcs == null) {
                fcs = Collections.emptyList();
            }
            result.setResult(fcs);
        } catch (Exception e) {
            log.error("find children of FrontendCategory(id={}) failed, error code={}", id, Throwables.getStackTraceAsString(e));
            result.setError("category.query.fail");
        }
        return result;
    }

    /**
     * 根据前台类目id寻找绑定的后台类目
     *
     * @param id 前台类目id
     * @return 绑定的后台叶子类目列表
     */
    @Override
    public Response<List<CategoryPair>> findBindingsOf(Long id) {
        Response<List<CategoryPair>> result = new Response<List<CategoryPair>>();
        try {
            checkNotNull(id);
            List<CategoryPair> cps = categoryBindingDao.findByFcid(id);
            if (cps == null) {
                cps = Collections.emptyList();
            }
            result.setResult(cps);
        } catch (Exception e) {
            log.error("find CategoryPairs by FrontendCategory(id={}) failed, error code={}", id, Throwables.getStackTraceAsString(e));
            result.setError("binding.query.fail");
        }
        return result;
    }

    @Override
    public Response<LinkedHashSet<Long>> findFcIdsByBcId(Long bcId) {
        Response<LinkedHashSet<Long>> result = new Response<LinkedHashSet<Long>>();
        if (bcId == null) {
            result.setError("category.id.null");
            return result;
        }
        try {
            result.setResult(backToFrontCache.get(bcId));
        } catch (Exception e) {
            log.error("findFcIdsByBcId(bcId={}) failed, error code=", bcId, Throwables.getStackTraceAsString(e));
            result.setError("category.query.fail");
        }
        return result;
    }

    @Override
    public Response<LinkedHashSet<Long>> findFcIdsByBcIds(List<Long> bcIds) {
        Response<LinkedHashSet<Long>> result = new Response<LinkedHashSet<Long>>();
        if (bcIds == null) {
            result.setError("category.id.null");
            return result;
        }

        try {
            LinkedHashSet<Long> frontendCategories = new LinkedHashSet<Long>();

            for(Long bcId : bcIds){
                frontendCategories.addAll(backToFrontCache.get(bcId));
            }

            result.setResult(frontendCategories);
        } catch (Exception e) {
            log.error("findFcIdsByBcId(bcIds={}) failed, error code=", bcIds, Throwables.getStackTraceAsString(e));
            result.setError("category.query.fail");
        }
        return result;
    }

    @Override
    public Response<List<FrontendCategory>> findFrontendByIds(String bcIds) {
        List<Long> bcIdList = JSON_MAPPER.fromJson(bcIds , JSON_MAPPER.createCollectionType(List.class , Long.class));

        Response<List<FrontendCategory>> result = new Response<List<FrontendCategory>>();

        if (bcIds == null) {
            log.error("find frontend category need bcIds.");
            result.setError("category.id.null");
            return result;
        }

        try{
            Response<LinkedHashSet<Long>> fcRes = findFcIdsByBcIds(bcIdList);
            if(!fcRes.isSuccess()){
                log.error("find frontend category id failed.");
                result.setError(fcRes.getError());
                return result;
            }

            if(fcRes.getResult().isEmpty()){
                log.debug("find frontend category by backend category is empty.");
                result.setError("category.find.empty");
                return result;
            }

            result.setResult(frontendCategoryManager.findByIds(Lists.newArrayList(fcRes.getResult())));
        }catch(Exception e){
            log.error("find frontend categories failed, bcIds={}, error code={}", bcIds, Throwables.getStackTraceAsString(e));
            result.setError("category.query.fail");
        }

        return result;
    }

    /**
     * 将一个前台叶子类目和多个后台叶子类目绑定, 后台叶子类目, List<CategoryPair> json格式
     * 这个接口有saveOrUpdate的语义
     *
     * @param fcid 前台叶子类目id
     * @param bcs  多个后台叶子类目, List<CategoryPair> json格式
     * @return 是否绑定成功
     */
    @Override
    public Response<Boolean> bind(Long fcid, String bcs) {
        Response<Boolean> result = new Response<Boolean>();
        try {
            CategoryBinding cb = categoryBindingDao.findBy(fcid);
            if (cb == null) {
                cb = new CategoryBinding();
                cb.setFrontId(fcid);
                cb.setBcs(bcs);
                categoryBindingDao.create(cb);
            } else {
                cb.setBcs(bcs);
                categoryBindingDao.update(cb);
            }
            List<CategoryPair> cps = JSON_MAPPER.fromJson(bcs, JAVA_TYPE);
            for (CategoryPair cp : cps) {
                backToFrontCache.refresh(cp.getId());
            }
            result.setResult(Boolean.TRUE);
        } catch (Exception e) {
            log.error("bind fcid={}, bcs={} failed, error code={}", fcid, bcs, Throwables.getStackTraceAsString(e));
            result.setError("category.bind.fail");
        }
        return result;
    }


    /**
     * 解除一个前台叶子类目到后台叶子类目的绑定
     *
     * @param fcid 待解绑的前台叶子类目id
     * @return 是否解绑成功
     */
    @Override
    public Response<Boolean> unbind(Long fcid) {
        Response<Boolean> result = new Response<Boolean>();
        try {
            CategoryBinding cb = categoryBindingDao.findBy(fcid);
            if (cb != null) {
                categoryBindingDao.delete(cb.getId());
            }
            result.setResult(Boolean.TRUE);
        } catch (Exception e) {
            log.error("unbind fcid={} failed, error code={}", fcid, Throwables.getStackTraceAsString(e));
            result.setError("category.unbind.fail");
        }
        return result;
    }

    @Override
    public Response<List<FrontendCategory>> ancestorsOf(Long id) {
        Response<List<FrontendCategory>> result = new Response<List<FrontendCategory>>();

        if(id == null) {
            log.error("id can not be null when find ancestors");
            result.setError("illegal.params");
            return result;
        }

        try {
            List<FrontendCategory> ancestors = Lists.newArrayListWithCapacity(MAX_LEVEL);
            Long currentId = id;
            while (currentId >= 1) {
                FrontendCategory frontendCategory = frontendCategoryManager.findById(currentId);
                currentId = frontendCategory.getParentId();
                ancestors.add(frontendCategory);
            }
            result.setResult(ancestors);
            return result;
        }catch (Exception e) {
            log.error("fail to find ancestor of frontendCategory id={}, cause:{}",
                    id, Throwables.getStackTraceAsString(e));
            result.setError("category.query.fail");
            return result;
        }
    }

    @Override
    public Response<List<FrontendCategory>> findByIds(List<Long> ids) {
        Response<List<FrontendCategory>> result = new Response<List<FrontendCategory>>();
        if(ids == null || ids.isEmpty()) {
            log.error("ids can not be null when find frontendCategory by ids");
            result.setError("illegal.params");
            return result;
        }
        try {
            List<FrontendCategory> frontendCategories = frontendCategoryManager.findByIds(ids);
            result.setResult(frontendCategories);
            return result;
        }catch (Exception e) {
            log.error("fail to find frontendCategory by ids={},cause:{}", ids, Throwables.getStackTraceAsString(e));
            result.setError("category.query.fail");
            return result;
        }
    }

    @Override
    public Response<List<FrontendCategory>> findByLevels(Integer level) {
        Response<List<FrontendCategory>> result = new Response<List<FrontendCategory>>();
        if (level == null) {
            result.setError("category.param.null");
            return result;
        }
        try {
            result.setResult(Objects.firstNonNull(frontendCategoryManager.findByLevels(level),
                    Collections.<FrontendCategory>emptyList()));
        } catch (Exception e) {
            log.error("find children of FrontendCategory(level={}) failed, error code={}", level, Throwables.getStackTraceAsString(e));
            result.setError("category.query.fail");
        }
        return result;
    }

    @Override
    public Response<List<FrontendCategory>> findByStringIds(String ids) {
        List<String> idStringList = splitter.splitToList(ids);
        List<Long>   idLongList = Lists.transform(idStringList, new Function<String, Long>() {
            @Nullable
            @Override
            public Long apply(@Nullable String input) {
                return Long.valueOf(input);
            }
        });
        return findByIds(idLongList);
    }

    @Override
    public Response<List<FrontEndCategoryNav>> findAll() {
        Response<List<FrontEndCategoryNav>> result = new Response<List<FrontEndCategoryNav>>();

        try {
            List<FrontendCategory> firstLevels = frontendCategoryManager.findChildrenOf(0l);
            List<FrontEndCategoryNav> navs = Lists.newArrayListWithCapacity(firstLevels.size());

            for(FrontendCategory fc : firstLevels) {
                FrontEndCategoryNav nav = new FrontEndCategoryNav();
                nav.setFirstLevel(fc);
                List<FrontendCategory> secondLevels = frontendCategoryManager.findChildrenOf(fc.getId());
                List<FrontendCategory> allThirdLevels = Lists.newArrayList();
                for (FrontendCategory sfc : secondLevels) {
                    List<FrontendCategory> thirdLevels = frontendCategoryManager.findChildrenOf(sfc.getId());
                    allThirdLevels.addAll(thirdLevels);
                }
                nav.setThirdLevel(allThirdLevels);
                navs.add(nav);
            }

            result.setResult(navs);
            return result;
        }catch (Exception e) {
            log.error("fail to find all frontendCategory, cause:{}",e);
            result.setError("category.query.fail");
            return result;
        }
    }

    @Override
    public Response<List<FrontendCategory>> findAllTotally() {
        Response<List<FrontendCategory>> result = new Response<List<FrontendCategory>>();
        try {
            result.setResult(frontendCategoryManager.findAllLoop(0L));
        } catch (Exception e) {
            log.error("findAllTotally() failed, cause:{}", Throwables.getStackTraceAsString(e));
            result.setError("category.query.fail");
        }
        return result;
    }

    @Override
    public Response<FrontendCategory> findFrontendCategoryByLevelAndName(Integer level, String name) {
        Response<FrontendCategory> result = new Response<FrontendCategory>();
        try {
            FrontendCategory fc = frontendCategoryManager.findByLevelAndName(level, name);
            checkNotNull(fc);
            result.setResult(fc);
        } catch (Exception e) {
            log.error("fail to find frontendCategory by level={} and name={}, cause:{}",
                    level, name, Throwables.getStackTraceAsString(e));
            result.setError("category.query.fail");
        }
        return result;
    }
}
