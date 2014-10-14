package io.terminus.snz.eai.service;

import com.google.common.base.Objects;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.haier.SelectInfoFromMDM.InType;
import com.haier.SelectInfoFromMDM.SelectInfoFromMDM;
import com.haier.SelectInfoFromMDM.SelectInfoFromMDMOPResponse;
import com.haier.SelectInfoFromMDM.SelectInfoFromMDM_Service;
import io.terminus.common.model.PageInfo;
import io.terminus.common.model.Paging;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.category.service.BackendCategoryService;
import io.terminus.snz.eai.dao.MDMBankViewDao;
import io.terminus.snz.eai.dao.MDMConfigureDao;
import io.terminus.snz.eai.dto.InfoSelectedFromMDM;
import io.terminus.snz.eai.dto.MDMConfigureRow;
import io.terminus.snz.eai.dto.MDMModuleInfoRow;
import io.terminus.snz.eai.manager.MDMSelectInfoManager;
import io.terminus.snz.eai.model.MDMBankView;
import io.terminus.snz.eai.model.MDMConfigure;
import io.terminus.snz.eai.util.MDMResultParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static io.terminus.common.utils.Arguments.notNull;
import static io.terminus.snz.eai.model.MDMConfigure.TYPE;

/**
 * Date: 7/25/14
 * Time: 11:37
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */

@Slf4j
@Service(value = "mdmConfigureServiceImpl")
public class MdmConfigureServiceImpl implements MdmConfigureService {

    @Autowired
    private MDMConfigureDao mdmConfigureDao;

    @Autowired
    private MDMBankViewDao mdmBankView;

    @Autowired
    private MDMSelectInfoManager mdmSelectInfoManager;

    @Autowired
    private BackendCategoryService backendCategoryService;

    private static final List<Integer> DISPLAY_TYPES =
            Lists.newArrayList(TYPE.SLAVE.toValue(), TYPE.CODE.toValue(),
                    TYPE.TERM.toValue(), TYPE.METHOD.toValue(), TYPE.FINGROUP.toValue());

    @Override
    public Response<List<MDMConfigure>> findListBy(MDMConfigure param) {
        Response<List<MDMConfigure>> result = new Response<List<MDMConfigure>>();

        try {
            List<MDMConfigure> mdmConfigures = mdmConfigureDao.findListByTypes(DISPLAY_TYPES);

            result.setResult(mdmConfigures);
        } catch (Exception e) {
            log.error("`findListBy` invoke fail. with param:{}", param);
            result.setError("mdm.configure.find.fail");
            return result;
        }
        return result;
    }

    @Override
    public Response<MDMConfigure> findBy(MDMConfigure param) {
        Response<MDMConfigure> result = new Response<MDMConfigure>();

        try {
            MDMConfigure found = mdmConfigureDao.findBy(param);
            checkState(notNull(found));

            result.setResult(found);
        } catch (Exception e) {
            log.error("`findBy` invoke fail. ");
            result.setError("mdm.configure.find.fail");
            return result;
        }

        return result;
    }

    @Override
    public Response<Boolean> syncConfigureDataFromMDM() {
        Response<Boolean> result = new Response<Boolean>();
        MDMResultParser parser = MDMResultParser.getInstance();

        try {
            // 从MDM获取所有数据
            SelectInfoFromMDM_Service service = new SelectInfoFromMDM_Service();
            SelectInfoFromMDM port = service.getSelectInfoFromMDMSOAP();

            InType inType = new InType();
            inType.setCurrentPage("1");
            inType.setStartTime("2011-01-01");
            inType.setEndTime("2014-07-19");
            inType.setDepartment("table_hm_value_set_JHPT");
            inType.setTableName("haiermdm.hm_value_set");
            SelectInfoFromMDMOPResponse.Output resp = port.selectInfoFromMDMOP(inType);

            String data = resp.getOut();
            Integer pageNo = Integer.parseInt(resp.getCurrentPage()),
                    pageCount = Integer.parseInt(resp.getPageCount());
            List<MDMConfigureRow> rows = parser.parse(data, MDMConfigureRow.class).getROWSET();
            while (pageNo < pageCount) {
                pageNo += 1;
                inType.setCurrentPage(pageNo+"");
                resp = port.selectInfoFromMDMOP(inType);
                rows.addAll(parser.parse(resp.getOut(), MDMConfigureRow.class).getROWSET());
            }
            if(rows.isEmpty()) {
                result.setResult(false);
                return result;
            }

            // 转换为 snz 配置信息
            List<MDMConfigure> configures = Lists.newArrayList();
            for (MDMConfigureRow row:rows) {
                String valueId = row.getVALUE_SET_ID();
                TYPE type = TYPE.fromValueID(valueId);
                if (type == null) {
                    continue;
                }

                MDMConfigure configure = new MDMConfigure();
                configure.setName(row.getVALUE_MEANING());
                configure.setCode(row.getVALUE());
                configure.setTypeEnum(type);
                configures.add(configure);
            }

            // 批量插入
            mdmSelectInfoManager.bulkInsertOrUpdate(configures);
            result.setResult(true);
        } catch (Exception e) {
            log.error("`syncConfigureDataFromMDM` invoke fail. e:{}", e);
            result.setError("mdm.sync.configure.fail");
            return result;
        }

        return result;
    }

    @Override
    public Response<Paging<MDMBankView>> pagingBankFindByFuzzyName(BaseUser user, String name, Integer pageNo, Integer size) {
        Response<Paging<MDMBankView>> paging = new Response<Paging<MDMBankView>>();
        PageInfo page = new PageInfo(pageNo, size);

        if (user == null){
            log.error("user can not be null when find MDMBandView");
            paging.setError("user.not.login");
            return paging;
        }

        Map<String,Object> params = Maps.newHashMap();
        params.put("user",user);
        params.put("name",name);
        params.put("offset",page.getOffset());
        params.put("size",page.getLimit());
        try {
            paging.setResult(mdmBankView.pagingBankFindByFuzzyName(params));
        } catch( Exception e){
            log.error("find MDMBankView failed,user={},name={},pageNo={},size={}, error code={}",user,name,pageNo,size,
                    Throwables.getStackTraceAsString(e));
            paging.setError("MDMBankView.find.failed");
        }
        return paging;
    }

    @Override
    public Response<MDMBankView> findBankByName(String name) {
        Response<MDMBankView> result = new Response<MDMBankView>();

        if(name == null) {
            log.error("name can not be null when find MDMBankView");
            result.setError("illegal.argument");
            return result;
        }

        try {
            MDMBankView set = mdmBankView.findBankByName(name);
            checkNotNull(set);
            result.setResult(set);
        } catch ( Exception e) {
            log.error("find MDMBankView failed ,name={}",name);
            result.setError("MDMBankView.find.failed");
        }
        return result;
    }

    @Override
    public Response<Boolean> syncOldModuleFromMDM(String startAt, String endAt) {
        Response<Boolean> result = new Response<Boolean>();
        String start = Objects.firstNonNull(startAt, "2014-01-01");
        String end = Objects.firstNonNull(endAt, "2014-09-01");

        try {
            SelectInfoFromMDM_Service service = new SelectInfoFromMDM_Service();
            SelectInfoFromMDM port =service.getSelectInfoFromMDMSOAP();

            InType inType = new InType();
            inType.setCurrentPage("1");
            inType.setStartTime(start);
            inType.setEndTime(end);
            inType.setDepartment("table_HREBSCN_HM_MTL_GENERAL_JHPT");
            inType.setTableName("haiermdm.HREBSCN_HM_MTL_GENERAL");
            SelectInfoFromMDMOPResponse.Output output = port.selectInfoFromMDMOP(inType);
            Integer pageCount = Integer.valueOf(output.getPageCount());
            Integer currentPage = Integer.valueOf(output.getCurrentPage());
            InfoSelectedFromMDM<MDMModuleInfoRow> dataset =
                    MDMResultParser.getInstance().parse(output.getOut(), MDMModuleInfoRow.class);

            while (currentPage <= pageCount) {
                if (dataset.getROWSET().isEmpty()) {
                    break;
                }

                mdmSelectInfoManager.bulkInsertOldModuleInfo(dataset.getROWSET());

                currentPage += 1;
                inType.setCurrentPage(currentPage+"");
                output = port.selectInfoFromMDMOP(inType);
                pageCount = Integer.valueOf(output.getPageCount());
                currentPage = Integer.valueOf(output.getCurrentPage());
                dataset = MDMResultParser.getInstance().parse(output.getOut(), MDMModuleInfoRow.class);
            }

            result.setResult(true);
        } catch (Exception e) {
            log.error("`syncOldModuleFromMDM` invoke fail. with startAt:{}, endAt:{}, e:{}", startAt, endAt, e);
            result.setError("sync.old.module.fail");
            return result;
        }

        return result;
    }
}
