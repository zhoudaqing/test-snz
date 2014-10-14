/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */
package io.terminus.snz.requirement.service;

import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.dao.MWOldModuleInfoDao;
import io.terminus.snz.requirement.model.MWOldModuleInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 老品信息 服务类<BR>
 *
 * @author wanggen 2014-09-02 13:12:17
 */
@Service(value = "mWOldModuleInfoServiceImpl")
@Slf4j
public class MWOldModuleInfoServiceImpl implements MWOldModuleInfoService {

    @Autowired
    private MWOldModuleInfoDao mWOldModuleInfoDao;

    @Override
    public Response<List<MWOldModuleInfo>> findByModuleNumOrModuleName(String moduleNum, String moduleName){
        Response<List<MWOldModuleInfo>> resp = new Response<List<MWOldModuleInfo>>();
        try{
            List<MWOldModuleInfo> mWOldModuleInfos = mWOldModuleInfoDao.findByModuleNumOrModuleName(moduleNum, moduleName);
            resp.setResult(mWOldModuleInfos);
            return resp;
        }catch (Exception e){
            resp.setError("mw_old_module_infos.select.failed");
            log.error("Failed to findByModuleNumOrModuleName from `mw_old_module_infos` with moduleNum:{}, moduleName:{}", moduleNum, moduleName, e);
            return resp;
        }
    }

}
