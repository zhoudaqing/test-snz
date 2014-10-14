/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.design.service;

import io.terminus.common.model.Paging;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.design.model.Site;

import java.util.List;
import java.util.Map;

/**
 * Author:  <a href="mailto:jlchen.cn@gmail.com">jlchen</a>
 * Date: 2013-11-21
 */
public interface SiteService {

    /**
     * 创建模板站点，这个接口给运营后台用
     *
     * @param userId 用户id
     * @param site   模板站点
     * @return 新创建的站点id
     */
    Long createTemplateSite(Long userId, Site site);

    /**
     * 根据模板id创建店铺站点， 这个接口在商家入住时直接调用
     *
     * @param userId     卖家id
     * @param site       店铺站点
     * @param templateId 模板id
     * @return 新创建的站点id
     */
    Long createShopSite(Long userId, Site site, Long templateId);

    /**
     * 创建子站站点
     *
     * @param userId 站点管理员id
     * @param site   子站站点
     * @return 新创建的站点id
     */
    Long createSubSite(Long userId, Site site);


    /**
     * 站点使用模板进行装修,只有店铺站点才能使用模板
     *
     * @param siteId     店铺站点id
     * @param templateId 模板id
     * @return 使用该模版的装修实例id
     */
    Long useTemplate(Long siteId, Long templateId);

    /**
     * 重置一个站点的模板，会把自定义部分全都删除
     * @param siteId 站点id
     * @param templateId 店铺id
     */
    void resetTemplate(Long siteId, Long templateId);

    /**
     * 删除站点,如果删除的是模板站点,则只有未发布过的模板才可以删除
     *
     * @param userId 用户id
     * @param siteId 站点id
     * @return 是否删除成功
     */
    Boolean deleteSite(Long userId, Long siteId);


    /**
     * 更新站点信息,不允许更改站点的类型
     *
     * @param site 站点
     * @return 是否更新成功
     */
    Boolean update(Site site);

    /**
     * 发布站点实例,这里需要做站点实例的拷贝,并只保留一个发布后的实例
     *
     * @param siteId           站点id
     * @param designInstanceId 装修中站点实例id
     * @return 发布后站点实例id
     */
    Long release(Long siteId, Long designInstanceId);

    Long useAndRelease(Long siteId, Long templateId);

    /**
     * 根据domain查找site
     *
     * @param domain 域名
     * @return 站点信息
     */
    Site findByDomain(String domain);

    /**
     * 根据二级域名查找site
     *
     * @param subdomain 二级域名
     * @return 站点信息
     */
    Site findBySubdomain(String app, String subdomain);

    /**
     * 根据用户id查找所有的站点列表
     *
     * @param userId 用户id
     * @return 站点列表
     */
    List<Site> findByUserId(Long userId);

    Site findShopByUserId(Long userId);

    /**
     * 根据站点id查找站点
     *
     * @param siteId 站点id
     * @return 站点信息
     */
    Site findById(Long siteId);

    /**
     * 列出指定app下的所有sub-site
     * @param app app
     * @return site list
     */
    List<Site> listSubSites(String app);

    /**
     * 分页查询所有站点
     *
     * @param siteType 站点类型 1-模板,2-子站,3-店铺
     * @param pageNo   起始页码
     * @param size     每页返回条数
     * @return 分页结果
     */
    Paging<Site> pagination(String app, Integer siteType, Integer pageNo, Integer size);

    /**
     * 查询当前用户的店铺模板信息
     *
     * @param user 当前用户
     * @return sites -> 所有模板站点, usingTemplate -> 正在使用的模板站点的 id
     */
    Map<String, Object> listShopTemplates(BaseUser user, String app);
}
