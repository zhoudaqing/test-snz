package io.terminus.snz.requirement.service.mock;

import io.terminus.common.model.Paging;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.eai.model.MDMBankView;
import io.terminus.snz.eai.model.MDMConfigure;
import io.terminus.snz.eai.service.MdmConfigureService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Desc:
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-30.
 */
@Service
public class MdmConfigureServiceMock implements MdmConfigureService {
    @Override
    public Response<List<MDMConfigure>> findListBy(MDMConfigure param) {
        return null;
    }

    @Override
    public Response<MDMConfigure> findBy(MDMConfigure param) {
        return null;
    }

    @Override
    public Response<Boolean> syncConfigureDataFromMDM() {
        return null;
    }

    @Override
    public Response<Paging<MDMBankView>> pagingBankFindByFuzzyName(BaseUser user, String name, Integer pageNo, Integer size) {
        return null;
    }

    @Override
    public Response<MDMBankView> findBankByName(String name) {
        return null;
    }

    @Override
    public Response<Boolean> syncOldModuleFromMDM(String startAt, String endAt) {
        return null;
    }
}
