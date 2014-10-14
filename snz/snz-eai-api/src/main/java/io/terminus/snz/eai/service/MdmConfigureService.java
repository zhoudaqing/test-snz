package io.terminus.snz.eai.service;

import io.terminus.common.model.Paging;
import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.eai.model.MDMBankView;
import io.terminus.snz.eai.model.MDMConfigure;

import java.util.List;

/**
 * Date: 7/25/14
 * Time: 11:38
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */
public interface MdmConfigureService {

    /**
     * 根据部分配置信息查找含有相同信息的配置信息的列表
     *
     * @param param    部分配置信息
     * @return         含有和给出参数相同信息的配置信息列表
     */
    @Export(paramNames = {"param"})
    public Response<List<MDMConfigure>> findListBy(MDMConfigure param);

    /**
     * 根据部分配置信息查找含有相同信息的一个配置信息
     *
     * @param param    部分配置信息
     * @return         含有和给出参数相同信息的一个配置信息
     */
    public Response<MDMConfigure> findBy(MDMConfigure param);

    /**
     * 从MDM同步配置信息
     *
     * @return          操作是否成功
     */
    public Response<Boolean> syncConfigureDataFromMDM();

    /**
     * 根据银行名字模糊查找用户名，被MDM资质验证完善银行信息调用
     *
     * @param user      当前登录用户
     * @param name      银行名字
     * @param pageNo    当前分页
     * @param size      分页大小
     * @return  含有和给出参数相同信息的一个page
     */
    @Export(paramNames = {"user", "name", "pageNo", "size"})
    public Response<Paging<MDMBankView>> pagingBankFindByFuzzyName(BaseUser user, String name,
                                                                   Integer pageNo, Integer size);

    /**
     *
     * @param name
     * @return 含有和给出参数相同名字的银行信息
     * */
    @Export(paramNames = {"name"})
    public Response<MDMBankView> findBankByName(String name);

    /**
     * 定时任务，从MDM同步老品信息并写入数据库
     *
     * @return 操作是否成功
     */
    @Export(paramNames = {"startAt", "endAt"})
    public Response<Boolean> syncOldModuleFromMDM(String startAt, String endAt);
}
