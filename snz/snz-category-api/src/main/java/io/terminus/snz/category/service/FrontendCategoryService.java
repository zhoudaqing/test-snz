package io.terminus.snz.category.service;

import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.Response;
import io.terminus.snz.category.dto.CategoryPair;
import io.terminus.snz.category.dto.FrontEndCategoryNav;
import io.terminus.snz.category.model.FrontendCategory;

import java.util.LinkedHashSet;
import java.util.List;

/**
 * Author:  <a href="mailto:i@terminus.io">jlchen</a>
 * Date: 2014-06-11
 */
public interface FrontendCategoryService {

    /**
     * 创建前台类目
     * @param fc  待创建的前台类目
     * @return  新创建类目的id
     */
    @Export(paramNames = {"fc"})
    Response<Long>  create(FrontendCategory fc);


    /**
     * 更新前台类目
     * @param fc  待更新的前台类目
     * @return  是否更新成功
     */
    @Export(paramNames = {"fc"})
    Response<Boolean> update(FrontendCategory fc);

    /**
     * 删除前台类目
     * @param id 待删除的前台类目id
     * @return 是否删除成功
     */
    @Export(paramNames = {"id"})
    Response<Boolean> delete(Long id);

    /**
     * 根据id寻找前台类目
     * @param id  前台类目id
     * @return  对应的前台类目
     */
    @Export(paramNames = {"id"})
    Response<FrontendCategory> findById(Long id);

    /**
     * 根据id寻找下级的前台类目
     * @param id  前台类目parent id
     * @return  对应的前台类目孩子列表
     */
    @Export(paramNames = {"id"})
    Response<List<FrontendCategory>> findChildrenOf(Long id);


    /**
     * 根据前台类目id寻找绑定的后台类目
     * @param id  前台类目id
     * @return  绑定的后台叶子类目列表
     */
    @Export(paramNames = {"id"})
    Response<List<CategoryPair>> findBindingsOf(Long id);

    /**
     * 根据后台类目id寻找绑定的前台类目
     * @param bcId 后台类目id
     * @return 绑定的前台类目列表
     */
    @Export(paramNames = {"bcId"})
    Response<LinkedHashSet<Long>> findFcIdsByBcId(Long bcId);

    /**
     * 根据后台的类目Id列表查询对应的绑定的全部前台类目
     * @param bcIds 后台类目编号
     * @return  LinkedHashSet
     * 返回后台类目绑定的前台类目
     */
    Response<LinkedHashSet<Long>> findFcIdsByBcIds(List<Long> bcIds);

    /**
     * 根据后台类目Id列表获取前台类目的信息
     * @param bcIds 后台类目编号
     * @return  List
     * 返回一个前台类目信息
     */
    @Export(paramNames = {"bcIds"})
    Response<List<FrontendCategory>> findFrontendByIds(String bcIds);

    /**
     * 将一个前台叶子类目和多个后台叶子类目绑定, 后台叶子类目, List<CategoryPair> json格式
     * 这个接口有saveOrUpdate的语义
     * @param fcid   前台叶子类目id
     * @param bcs  多个后台叶子类目, List<CategoryPair> json格式
     * @return   是否绑定成功
     */
    @Export(paramNames = {"fcid", "bcs"})
    Response<Boolean> bind(Long fcid, String bcs);


    /**
     * 解除一个前台叶子类目到后台叶子类目的绑定
     * @param fcid   待解绑的前台叶子类目id
     * @return  是否解绑成功
     */
    @Export(paramNames = {"fcid"})
    Response<Boolean> unbind(Long fcid);

    /**
     * 找一个前台类目的祖先，不包括根节点（root）
     * @param id 前台类目id
     * @return   类目树
     */
    @Export(paramNames = {"id"})
    Response<List<FrontendCategory>> ancestorsOf(Long id);

    /**
     * 根据多个id批量查询
     * @param ids    id列表
     * @return 前台类目列表
     */
    Response<List<FrontendCategory>> findByIds(List<Long> ids);

    /**
     * 根据前台类目level查询
     * @param level    level
     * @return 前台类目列表
     */
    @Export(paramNames = {"level"})
    Response<List<FrontendCategory>> findByLevels(Integer level);

    /**
     * 前台调用，方法内直接调用findByIds方法
     * @param ids string类型的id列表
     */
    @Export(paramNames = {"ids"})
    Response<List<FrontendCategory>> findByStringIds(String ids);

    /**
     * 查询所有前台类目（只有一级和三级对应）
     * @return 前台类目列表
     */
    @Export
    Response<List<FrontEndCategoryNav>> findAll();

    /**
     * 所有前台类目列表
     * @return 所要的列表
     */
    Response<List<FrontendCategory>> findAllTotally();

    /**
     * 通过level和名称查找前台类目
     * @param level    level
     * @param name     name
     * @return 前台类目
     */
    Response<FrontendCategory> findFrontendCategoryByLevelAndName(Integer level, String name);
}
