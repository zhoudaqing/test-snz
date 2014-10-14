/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.design.service;

import io.terminus.pampas.design.model.FullPage;
import io.terminus.pampas.design.model.Page;
import io.terminus.pampas.design.model.TemplatePage;

import java.util.List;

/**
 * Author:  <a href="mailto:jlchen.cn@gmail.com">jlchen</a>
 * Date: 2013-11-21
 */
public interface PageService {

    /**
     * 根据siteInstanceId和path来查找页面
     *
     * @param siteId     站点id
     * @param instanceId 站点实例id
     * @param path       访问路径
     * @return 页面信息
     */
    FullPage findBy(Long siteId, Long instanceId, String path);

    /**
     * 根据siteInstanceId和path来查找页面，忽略cache
     *
     * @param siteId     站点id
     * @param instanceId 站点实例id
     * @param path       访问路径
     * @return 页面信息
     */
    FullPage findByWithoutCache(Long siteId, Long instanceId, String path);

    /**
     * 根据 id 查询 page
     *
     * @param id id
     * @return page
     */
    Page findPageById(Long id);

    /**
     * 根据 id 查询 page
     *
     * @param id id
     * @return page
     */
    TemplatePage findTemplatePageById(Long id);

    /**
     * 根据站点实例id来查找所有的页面id
     *
     * @param siteId     站点id
     * @param instanceId 站点实例id
     * @return 页面id列表
     */
    List<Long> findPageIds(Long siteId, Long instanceId);


    /**
     * 根据站点实例id来查找所有的页面
     *
     * @param siteId     站点id
     * @param instanceId 站点实例id
     * @return 页面列表
     */
    List<? extends Page> findPages(Long siteId, Long instanceId);

    /**
     * 创建子站页面
     *
     * @param templatePage 子站页面
     * @return 新创建的子站页面id
     */
    Long createSubSitePage(TemplatePage templatePage);

    /**
     * 修改子站页面信息
     *
     * @param templatePage 子站页面
     */
    void updateSubSitePage(TemplatePage templatePage);

    /**
     * 删除子站页面
     *
     * @param pageId 子站页面id
     * @return 是否删除成功
     */
    void deleteSubSitePage(Long pageId);

    void updatePage(Page page);
}
