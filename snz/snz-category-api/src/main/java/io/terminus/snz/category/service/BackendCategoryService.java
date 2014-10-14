package io.terminus.snz.category.service;

import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.Response;
import io.terminus.snz.category.model.BackendCategory;
import io.terminus.snz.category.model.BackendCategoryProperty;

import java.util.List;

/**
 * Author:  <a href="mailto:i@terminus.io">jlchen</a>
 * Date: 2014-06-11
 */
public interface BackendCategoryService {

    /**
     * 创建后台类目
     * @param bc  待创建的后台类目
     * @return  新创建类目的id
     */
    @Export(paramNames = {"bc"})
    Response<Long>  create(BackendCategory bc);


    /**
     * 更新后台类目
     * @param bc  待更新的后台类目
     * @return  是否更新成功
     */
    @Export(paramNames = {"bc"})
    Response<Boolean> update(BackendCategory bc);

    /**
     * 删除后台类目
     * @param id 待删除的后台类目id
     * @return 是否删除成功
     */
    @Export(paramNames = {"id"})
    Response<Boolean> delete(Long id);

    /**
     * 根据id寻找后台类目
     * @param id  后台类目id
     * @return  对应的后台类目
     */
    @Export(paramNames = {"id"})
    Response<BackendCategory> findById(Long id);

    /**
     * 根据ids批量查找后台类目
     * @param ids    后台类目id列表
     * @return 所要查找的后台类目列表
     */
    @Export(paramNames = {"ids"})
    Response<List<BackendCategory>> findByIds(List<Long> ids);

    /**
     * 根据id寻找下级的后台类目
     * @param id  后台类目parent id
     * @return  对应的后台类目孩子列表
     */
    @Export(paramNames = {"id"})
    Response<List<BackendCategory>> findChildrenOf(Long id);

    /**
     * 根据级别编号获取该级别的下的所有类目
     * @param level 级别编号
     * @return  List
     * 返回级别类目列表
     */
    @Export(paramNames = {"level"})
    Response<List<BackendCategory>> findByLevels(Integer level);


    /**
     * 根据 parentId、level 查询后台二级类目
     * @param parentId 父级ID
     * @param level    等级
     * @return         后台类目
     */
    @Export(paramNames = {"parentId", "level"})
    Response<List<BackendCategory>> findByParentIdAndLevel(Integer parentId, Integer level);


    /**
     * 创建后台类目属性
     * @param property 后台类目属性
     * @return 后台类目属性id
     */
    @Export(paramNames = {"property"})
    Response<Long> createProperty(BackendCategoryProperty property);

    /**
     * 更新后台类目属性
     * @param id 后台类目属性id
     * @param property 后台类目属性
     * @return 是否更新成功
     */
    @Export(paramNames = {"id", "property"})
    Response<Boolean> updateProperty(Long id, BackendCategoryProperty property);

    /**
     * 删除后台类目属性
     * @param id 后台类目属性id
     * @return 是否删除成功
     */
    @Export(paramNames = {"id"})
    Response<Boolean> deleteProperty(Long id);

    /**
     * 根据后台类目属性id查找对应的后台类目属性
     * @param id 后台类目属性id
     * @return 所要搜寻的后台类目属性
     */
    @Export(paramNames = {"id"})
    Response<BackendCategoryProperty> findPropertyById(Long id);

    /**
     * 根据后台类目id查找其属性列表
     * @param bcId 后台类目id
     * @return 后台类目对应的属性列表
     */
    @Export(paramNames = {"bcId"})
    Response<List<BackendCategoryProperty>> findPropertiesByBcId(Long bcId);

    /**
     * 根据后台类目id编号查询类目属性信息
     * @param bcIds 后台类目ids
     * @return  List
     * 返回后台类目属性列表
     */
    Response<List<BackendCategoryProperty>> findPropertiesByBcIds(List<Long> bcIds);

    /**
     * 根据后台类目id编号和工厂编号列表查询类目属性信息（查找多个，传参为逗号分隔的String）
     * @param bcId          后台类目id
     * @param factoryNums   工厂编号（多个，逗号分隔）
     * @return 后台类目属性List
     */
    @Export(paramNames = {"bcId", "factoryNums"})
    Response<List<BackendCategoryProperty>> findPropertiesByBcIdWithFactoryNums(Long bcId, String factoryNums);
}
