package io.terminus.snz.eai.service;

import com.google.common.base.Strings;
import io.terminus.pampas.common.Response;
import io.terminus.snz.eai.dao.MDMOldModuleInfoDao;
import io.terminus.snz.eai.model.MDMOldModuleInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Date: 8/31/14
 * Time: 22:03
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */

@Slf4j
@Service
public class MdmOldModuleInfoServiceImpl implements MdmOldModuleInfoService {

    @Autowired
    MDMOldModuleInfoDao mdmOldModuleInfoDao;

    @Override
    public Response<MDMOldModuleInfo> findByModuleNum(String moduleNum) {
        Response<MDMOldModuleInfo> result = new Response<MDMOldModuleInfo>();

        try {
            checkArgument(!Strings.isNullOrEmpty(moduleNum));

            MDMOldModuleInfo oldModuleInfo = mdmOldModuleInfoDao.findByModuleNum(moduleNum);
            result.setResult(oldModuleInfo);

        } catch (IllegalArgumentException e) {
            log.error("`findByModuleNum` invoke fail, with wrong argument moduleNum:{}, e:{}", moduleNum, e);
            result.setError("illegal.argument");
            return result;
        } catch (Exception e) {
            log.error("`findByModuleNum` invoke fail. with module num :{}, e:{}", moduleNum, e);
            result.setError("oldModule.info.find.fail");
            return result;
        }
        return result;
    }
}
