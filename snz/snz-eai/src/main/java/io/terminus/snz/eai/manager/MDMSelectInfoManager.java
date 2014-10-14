package io.terminus.snz.eai.manager;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import io.terminus.snz.eai.dao.MDMConfigureDao;
import io.terminus.snz.eai.dao.MDMOldModuleInfoDao;
import io.terminus.snz.eai.dto.MDMModuleInfoRow;
import io.terminus.snz.eai.model.MDMConfigure;
import io.terminus.snz.eai.model.MDMOldModuleInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Date: 7/30/14
 * Time: 14:08
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */
@Slf4j
@Component
public class MDMSelectInfoManager {

    @Autowired
    private MDMConfigureDao mdmConfigureDao;

    @Autowired
    private MDMOldModuleInfoDao mdmOldModuleInfoDao;

    @Transactional
    public void bulkInsertOrUpdate(List<MDMConfigure> configures) {
        for (MDMConfigure configure: configures) {
            MDMConfigure param = new MDMConfigure();
            param.setCode(configure.getCode());
            param.setType(configure.getType());
            MDMConfigure found = mdmConfigureDao.findBy(param);

            if (found==null) {
                MDMConfigure create = new MDMConfigure();
                create.setCode(configure.getCode());
                create.setType(configure.getType());
                create.setName(configure.getName());
                mdmConfigureDao.create(create);
                continue;
            }

            MDMConfigure update = found;
            if (!Objects.equal(update.getName(), configure.getName())) {
                update.setName(configure.getName());
                mdmConfigureDao.update(update);
            }
        }
    }

    @Transactional
    public void bulkInsertOldModuleInfo(List<MDMModuleInfoRow> rows) {
        List<MDMOldModuleInfo> oldModuleInfos = Lists.newArrayList();
        for (MDMModuleInfoRow row : rows) {
            MDMOldModuleInfo oldModuleInfo = new MDMOldModuleInfo();
            oldModuleInfo.setUnit(row.getPRIMARY_UOM());
            oldModuleInfo.setModuleName(row.getMATERIAL_DESCRITION());
            oldModuleInfo.setModuleNum(row.getMATERIAL_CODE());
            oldModuleInfo.setSeriesName(row.getHRMODULARNAME());
            // MDM 给出的三级类目不带路径，只有一个三级类目名
            // 导致重名无法查找id
            // oldModuleInfo.setSeriesId();
            oldModuleInfos.add(oldModuleInfo);
        }

        for (MDMOldModuleInfo info : oldModuleInfos) {
            MDMOldModuleInfo found = mdmOldModuleInfoDao.findByModuleNum(info.getModuleNum());
            if (found == null) {
                mdmOldModuleInfoDao.create(info);
            } else {
                found.setModuleName(info.getModuleName());
                found.setUnit(info.getUnit());
                found.setSeriesId(info.getSeriesId());
                found.setSeriesName(info.getSeriesName());
                mdmOldModuleInfoDao.update(info);
            }
        }
    }
}
