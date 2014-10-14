package io.terminus.snz.web.controllers;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.terminus.common.exception.ServiceException;
import io.terminus.common.utils.Joiners;
import io.terminus.common.utils.JsonMapper;
import io.terminus.common.utils.NumberUtils;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.pampas.common.UserUtil;
import io.terminus.pampas.engine.MessageSources;
import io.terminus.snz.requirement.dto.SupplierReparationDetailDtoExport;
import io.terminus.snz.requirement.service.ReparationService;
import io.terminus.snz.user.dto.SupplierReportExportDto;
import io.terminus.snz.user.dto.SupplierReportQueryCriteria;
import io.terminus.snz.user.model.Company;
import io.terminus.snz.user.model.CompanySupplyPark;
import io.terminus.snz.user.model.User;
import io.terminus.snz.user.service.SupplierReportService;
import io.terminus.snz.web.util.ExportExcel;
import io.terminus.snz.web.util.HttpHeaderUtil;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Desc:
 * Mail: houly@terminus.io
 * author: Hou Luyao
 * Date: 14-8-26.
 */
@Slf4j
@Controller
@RequestMapping("/api/export")
public class ExcelExportController {

    private final static DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");

    @Autowired
    private ReparationService reparationService;

    @Autowired
    private SupplierReportService supplierReportService;

    @Autowired
    private MessageSources messageSources;

    private static HashBiMap<String,String> colsMap = HashBiMap.create();
    private static Set<String> colsNeedRichText = Sets.newHashSet();
    static{
//        colsMap.put("index","序号"); //导出的时候不需要导出序号
        colsMap.put("step","状态");
        colsMap.put("supplierCode","供应商编码");
        colsMap.put("corporation_supplyName","供应商名称");
        colsMap.put("accountType","类型(制造/渠道)");
        colsMap.put("creditQualifyResult","信用等级评价结果");
        colsMap.put("participateCount","响应交互数量");
        colsMap.put("selectedSolutionCount","方案中选数量");
        colsMap.put("listedStatus","是否上市公司");
        colsMap.put("personScale_employeeNum","员工人数");
        colsMap.put("customers","主要客户");
        colsMap.put("aptitudeQualifyResult","资质交互结果");
        colsMap.put("initAgent_legalPerson","法人");
        colsMap.put("supplierCode_code","供应商代码");
        colsMap.put("corporation_legalPerson","法人公司名称");
        colsMap.put("groupName","集团公司名称");
        colsMap.put("groupAddr","集团公司地址");
        colsMap.put("regCapital","注册资金(万)");
        colsMap.put("personScale_company","公司规模人数");
        colsMap.put("foundAt","公司成立时间");
        colsMap.put("regCountryName","公司注册国家");
        colsMap.put("regRegion","隶属地区");
        colsMap.put("fixedAssets","固定资产(万)");
        colsMap.put("productLine","所属产品线");
        colsMap.put("officialWebsite","公司网站");
        colsMap.put("nature","企业性质");
        colsMap.put("worldTop","是否世界500强");
        colsMap.put("listedRegion","上市地区");
        colsMap.put("ticker","股票代码");
        colsMap.put("companySupplyParks","可供货园区");
        colsMap.put("factories_area","工厂面积(平方米)");
        colsMap.put("factories_num","人数(人)");
        colsMap.put("factories_assets","固定资产(万元)");
        colsMap.put("name","联系人");
        colsMap.put("mobile","联系人手机");
        colsMap.put("officePhone","联系人办公电话");
        colsMap.put("email","联系人邮箱");
        colsMap.put("recentFinance_sold","销售额(万元)");
        colsMap.put("recentFinance_net","净利润(万元)");
        colsMap.put("assets","研发资产(万)");
        colsMap.put("labArea","试验室面积(㎡)");
        colsMap.put("numberOfPatents","专利数量");

        colsNeedRichText.add("customers");
        colsNeedRichText.add("factories_area");
        colsNeedRichText.add("factories_num");
        colsNeedRichText.add("factories_assets");
        colsNeedRichText.add("recentFinance_sold");
        colsNeedRichText.add("recentFinance_net");
    }
    /**
     * 索赔界面详情导出
     * @param context
     * @param request
     * @param response
     */
    @RequestMapping(value = "/reparation", method = RequestMethod.GET)
    public void exportSupplierReparation(@RequestParam Map<String,String> context,HttpServletRequest request,HttpServletResponse response){
        BaseUser user = UserUtil.getCurrentUser();
        if (user == null) {
            throw new ServiceException(messageSources.get("user.not.login"));
        }
        Response<List<SupplierReparationDetailDtoExport>> res = reparationService.findSupplierDailyReparationsBy(user, parseLong(context.get("userId")), parseInteger(context.get("range")), parseInteger(context.get("type")), context.get("startAt"), context.get("endAt"));
        if(!res.isSuccess()){
            throw new ServiceException(messageSources.get(res.getError()));
        }
        try {

            Integer type = parseInteger(context.get("type"));
            String[][] titles =  null;
            String fileName = null;
            String sheetName = null;
            switch (type){
                case 4:
                    fileName = "市场不良明细.xlsx";
                    sheetName = "市场不良明细";
                    String[][] marketTitles = {{"日期","产品大类","事业部","工单号","产品型号","产品序列号","生产日期","保修日期","维修网点","用户抱怨","故障现象","对象","维修措施","备件专用号","备件成本","售后费用","备件名称"}};
                    titles = marketTitles;
                    break;
                case 2:
                    fileName = "现场信息明细.xlsx";
                    sheetName = "现场信息明细";
                    String[][] onSceneTitles = {{"日期","事业部","退次号","专用号","不良数量","索赔金额","扣款原因描述","报表时间"}};
                    titles = onSceneTitles;
                    break;
                case 3:
                    ;
            }
            HttpHeaderUtil.setDowloadHeader(request, response, fileName);
            ExportExcel.getInstance().doExport(
                    Lists.newArrayList(ExportExcel.ExcelSheet.<SupplierReparationDetailDtoExport>of().buildSheetName(sheetName).buildData(res.getResult()).buildTitles(titles)),
                    fileName,
                    new ExportExcel.ExportAction<SupplierReparationDetailDtoExport>() {
                        @Override
                        public String[] transform(SupplierReparationDetailDtoExport supplierReparationDailyDto) {

                            switch (supplierReparationDailyDto.getType()){
                                case 4:
                                    return new String[]{
                                            parseDate(supplierReparationDailyDto.getDate()),
                                            parseString(supplierReparationDailyDto.getMarket().getPl()),
                                            parseString(supplierReparationDailyDto.getMarket().getBusiness()),
                                            parseString(supplierReparationDailyDto.getMarket().getOid()),
                                            parseString(supplierReparationDailyDto.getMarket().getProductId()),
                                            parseString(supplierReparationDailyDto.getMarket().getProductSid()),

                                            parseDate(supplierReparationDailyDto.getMarket().getAssembledAt()),
                                            parseDate(supplierReparationDailyDto.getMarket().getLogAt()),
                                            parseString(supplierReparationDailyDto.getMarket().getBranch()),
                                            parseString(supplierReparationDailyDto.getMarket().getComplain()),
                                            parseString(supplierReparationDailyDto.getMarket().getDescribe()),
                                            parseString(supplierReparationDailyDto.getMarket().getObject()),
                                            parseString(supplierReparationDailyDto.getMarket().getAction()),
                                            parseString(supplierReparationDailyDto.getMarket().getModuleNum()),
                                            parsePrice(supplierReparationDailyDto.getMarket().getPrice()),
                                            parsePrice(supplierReparationDailyDto.getMarket().getFee()),
                                            parseString(supplierReparationDailyDto.getMarket().getModuleName())
                                    };
                                case 2:
                                    return new String[]{
                                            parseDate(supplierReparationDailyDto.getDate()),
                                            parseString(supplierReparationDailyDto.getOnScene().getDepartName()),
                                            parseString(supplierReparationDailyDto.getOnScene().getWId()),
                                            parseString(supplierReparationDailyDto.getOnScene().getModuleNum()),
                                            parseInt(supplierReparationDailyDto.getOnScene().getWCount()),
                                            parsePrice(supplierReparationDailyDto.getOnScene().getMoney()),
                                            "---",
                                            parseDate(supplierReparationDailyDto.getOnScene().getSendAt())
                                    };
                            }
                            return new String[0];
                        }
                    },
                    response.getOutputStream()
            );
        } catch (IOException e) {
            log.error("export reparation exception fail.cause {}", Throwables.getStackTraceAsString(e));
            throw new ServiceException(messageSources.get("excel.export.fail"));
        }
    }

    /**
     * 供应商报表导出
     * @param context
     * @param request
     * @param response
     */
    @RequestMapping(value = "/supplier/report", method = RequestMethod.GET)
    public void exportSupplierReport(@RequestParam Map<String,String> context, HttpServletRequest request, HttpServletResponse response) {
        BaseUser user = UserUtil.getCurrentUser();
        if (user == null) {
            throw new ServiceException(messageSources.get("user.not.login"));
        }
        SupplierReportQueryCriteria criteria = new SupplierReportQueryCriteria(parseStringToDate(context.get("registeredAtStart")), parseStringToDate(context.get("registeredAtEnd")), parseLong(context.get("moduleId")), parseLong(context.get("mainBusinessId")), parseInteger(context.get("step")), parseLong(context.get("supplyParkId")), parseInteger(context.get("strategy")));
        Response<List<SupplierReportExportDto>> res = supplierReportService.findDetailReport(user, criteria);
        if(!res.isSuccess()){
            throw new ServiceException(messageSources.get(res.getError()));
        }

        String selectCol = context.get("selectCol");
        if(Strings.isNullOrEmpty(selectCol)){
            throw new ServiceException(messageSources.get("excel.export.notitle"));
        }
        try {
            final String[] cols = selectCol.split(",");
            String[] title = new String[cols.length];
            Set<Integer> colRichText = Sets.newHashSet();
            for(int i=0; i<cols.length; i++){
                title[i] = colsMap.get(cols[i]);
                if(colsNeedRichText.contains(cols[i])){
                    colRichText.add(i);
                }
            }
            String[][] titles = {title};
            String fileName = "供应商报表.xlsx";
            String sheetName = "供应商报表";

            HttpHeaderUtil.setDowloadHeader(request, response, fileName);
            ExportExcel.getInstance().doExport(
                    Lists.newArrayList(ExportExcel.ExcelSheet.<SupplierReportExportDto>of().buildSheetName(sheetName).buildData(res.getResult()).buildTitles(titles).buildColReachText(colRichText)),
                    fileName,
                    new ExportExcel.ExportAction<SupplierReportExportDto>() {
                        @Override
                        public String[] transform(SupplierReportExportDto supplierReportExportDto) {
                            String[] result = new String[cols.length];
                            for(int i=0; i<cols.length; i++){
                                result[i] = transformSupplierReportExportDto(cols[i], supplierReportExportDto);
                            }
                            return result;
                        }
                    },
                    response.getOutputStream()
            );
        } catch (Exception e) {
            log.error("fail to export supplier report,cause {}", Throwables.getStackTraceAsString(e));
            throw new ServiceException(messageSources.get("excel.export.fail"));
        }
    }

    /**
     * String 转换成Date
     * @param value
     * @return
     */
    private Date parseStringToDate(String value){
        if (Strings.isNullOrEmpty(value)){
            return null;
        }
        return dtf.parseDateTime(value).toDate();
    }
    /**
     * String 转换成Integer
     * @param value
     * @return
     */
    private Integer parseInteger(String value){
        if (Strings.isNullOrEmpty(value)){
            return null;
        }
        return Integer.valueOf(value);
    }

    /**
     * String 转换成Long
     * @param value
     * @return
     */
    private Long parseLong(String value){
        if (Strings.isNullOrEmpty(value)){
            return null;
        }
        return Long.valueOf(value);
    }

    /**
     * Int 类型转换成String
     * @param integer
     * @return
     */
    private String parseInt(Integer integer){
        return integer == null?"":integer.toString();
    }

    /**
     * Date转换成String
     * @param date
     * @return
     */
    private String parseDate(Date date){
       return date==null?"":dtf.print(new DateTime(date));
    }

    /**
     * 格式化金额 分到元
     * @param price
     * @return
     */
    private String parsePrice(Number price){
        return NumberUtils.formatPrice(price);
    }

    /**
     * 格式化金额 分到萬元
     * @param price
     * @return
     */
    private String parsePriceW(Number price){
        if(price == null){
            return "";
        }
        int divisor = 1000000;
        return getDecimalFormat(2).format(price.doubleValue() / divisor);
    }

    /**
     * 转换String
     * @param string
     * @return
     */
    private String parseString(String string){
        return Strings.isNullOrEmpty(string)?"":string;
    }

    private DecimalFormat getDecimalFormat(Integer digits){
        DecimalFormat decimalFormat = new DecimalFormat();
        Integer fractionDigits = digits == null? 2:digits;
        decimalFormat.setMaximumFractionDigits(fractionDigits);
        decimalFormat.setMinimumFractionDigits(fractionDigits);
        return decimalFormat;
    }

    private String transformSupplierReportExportDto(String title, SupplierReportExportDto supplierReportExportDto){
        if(Strings.isNullOrEmpty(title) || supplierReportExportDto == null){
            return "";
        }

        if("step".equals(title)){
            if(supplierReportExportDto.getUser() != null){
                User.Step step = User.Step.from(supplierReportExportDto.getUser().getStep());
                return step == null? "": step.toString();
            }
        }
        else if("supplierCode".equals(title)){
            if(supplierReportExportDto.getCompany() != null)
                return parseString(supplierReportExportDto.getCompany().getSupplierCode());
        }
        else if("corporation_supplyName".equals(title)){
            if(supplierReportExportDto.getCompany() != null)
                return parseString(supplierReportExportDto.getCompany().getCorporation());
        }
        else if("accountType".equals(title)){
            if(supplierReportExportDto.getUser() != null){
                User.AccountType accountType = User.AccountType.from(supplierReportExportDto.getUser().getAccountType());
                return accountType == null? "": accountType.toString();
            }
        }
        else if("creditQualifyResult".equals(title)){
            return parseString(supplierReportExportDto.getCreditQualifyResult());
        }
        else if("participateCount".equals(title)){
            if(supplierReportExportDto.getCompany() != null)
                return parseInt(supplierReportExportDto.getCompany().getParticipateCount());
        }
        else if("selectedSolutionCount".equals(title)){
            return parseInt(supplierReportExportDto.getSelectedSolutionCount());
        }
        else if("listedStatus".equals(title)){
            if(supplierReportExportDto.getCompany() != null){
                Company.ListedStatus listedStatus = Company.ListedStatus.from(supplierReportExportDto.getCompany().getListedStatus());
                return listedStatus == null? "": listedStatus.toString();
            }
        }
        else if("personScale_employeeNum".equals(title)){
            if(supplierReportExportDto.getCompany() != null)
                return parseString(supplierReportExportDto.getCompany().getPersonScale());
        }
        else if("customers".equals(title)){
            //rich text
            if(supplierReportExportDto.getCompany() != null && !Strings.isNullOrEmpty(supplierReportExportDto.getCompany().getCustomers())){
                String json = supplierReportExportDto.getCompany().getCustomers();
                Map<String, List<Map<String,String>>> o = JsonMapper.nonDefaultMapper().fromJson(json, Map.class);
                List<Map<String,String>> list = Lists.newArrayList();
                for(List<Map<String,String>> l : o.values()){
                    list.addAll(l);
                }
                List<String> result = Lists.transform(list, new Function<Map<String, String>, String>() {
                    @Nullable
                    @Override
                    public String apply(Map<String, String> stringStringMap) {
                        return parseString(stringStringMap.get("remark"));
                    }
                });
                while (result.contains("")){
                    result.remove("");
                }

                while (result.contains("null")){
                    result.remove("null");
                }
                return Joiners.COMMA.join(result);
            }
        }
        else if("aptitudeQualifyResult".equals(title)){
            return parseString(supplierReportExportDto.getAptitudeQualifyResult());
        }
        else if("initAgent_legalPerson".equals(title)){
            if(supplierReportExportDto.getCompany() != null)
                return parseString(supplierReportExportDto.getCompany().getInitAgent());
        }
        else if("supplierCode_code".equals(title)){
            if(supplierReportExportDto.getCompany() != null)
                return parseString(supplierReportExportDto.getCompany().getSupplierCode());
        }
        else if("corporation_legalPerson".equals(title)){
            if(supplierReportExportDto.getCompany() != null)
                return parseString(supplierReportExportDto.getCompany().getCorporation());
        }
        else if("groupName".equals(title)){
            if(supplierReportExportDto.getCompany() != null)
                return parseString(supplierReportExportDto.getCompany().getGroupName());
        }
        else if("groupAddr".equals(title)){
            if(supplierReportExportDto.getCompany() != null)
                return parseString(supplierReportExportDto.getCompany().getGroupAddr());
        }
        else if("regCapital".equals(title)){
            if(supplierReportExportDto.getCompany() != null)
                return parseString(parsePriceW(supplierReportExportDto.getCompany().getRegCapital()));
        }
        else if("personScale_company".equals(title)){
            if(supplierReportExportDto.getCompany() != null)
                return parseString(supplierReportExportDto.getCompany().getPersonScale());
        }
        else if("foundAt".equals(title)){
            if(supplierReportExportDto.getCompany() != null)
                return parseDate(supplierReportExportDto.getCompany().getFoundAt());
        }
        else if("regCountryName".equals(title)){
            return parseString(supplierReportExportDto.getRegCountryName());

        }
        else if("regRegion".equals(title)){
            return parseString(supplierReportExportDto.getRegRegion());
        }
        else if("fixedAssets".equals(title)){
            if(supplierReportExportDto.getCompany() != null)
                return parseString(parsePriceW(supplierReportExportDto.getCompany().getFixedAssets()));
        }
        else if("productLine".equals(title)){
            if(supplierReportExportDto.getCompany() != null)
                return parseString(supplierReportExportDto.getCompany().getProductLine());
        }
        else if("officialWebsite".equals(title)){
            if(supplierReportExportDto.getCompany() != null)
                return parseString(supplierReportExportDto.getCompany().getOfficialWebsite());
        }
        else if("nature".equals(title)){
            if(supplierReportExportDto.getCompany() != null){
                Company.Nature nature = Company.Nature.from(supplierReportExportDto.getCompany().getNature());
                return nature == null? "": nature.toString();
            }
        }
        else if("worldTop".equals(title)){
            if(supplierReportExportDto.getCompany() != null){
                Company.WorldTop worldTop = Company.WorldTop.from(supplierReportExportDto.getCompany().getWorldTop());
                return worldTop == null? "": worldTop.toString();
            }
        }
        else if("listedRegion".equals(title)){
            if(supplierReportExportDto.getCompany() != null)
                return parseString(supplierReportExportDto.getCompany().getListedRegion());
        }
        else if("ticker".equals(title)){
            if(supplierReportExportDto.getCompany() != null)
                return parseString(supplierReportExportDto.getCompany().getTicker());
        }
        else if("companySupplyParks".equals(title)){
            if(supplierReportExportDto.getCompanySupplyParks()!=null && supplierReportExportDto.getCompanySupplyParks().size()>0){
                List<String> list = Lists.transform(supplierReportExportDto.getCompanySupplyParks(), new Function<CompanySupplyPark, String>() {
                    @Nullable
                    @Override
                    public String apply(@Nullable CompanySupplyPark companySupplyPark) {
                        return companySupplyPark.getName();
                    }
                });
                return Joiners.COMMA.join(list);
            }
        }
        else if("factories_area".equals(title)){
            if(supplierReportExportDto.getCompany()!=null && !Strings.isNullOrEmpty(supplierReportExportDto.getCompany().getFactories())){
                String json = supplierReportExportDto.getCompany().getFactories();
                List<Map<String,String>> list = JsonMapper.nonDefaultMapper().fromJson(json, List.class);
                List<String> result = Lists.transform(list, new Function<Map<String, String>, String>() {
                    @Nullable
                    @Override
                    public String apply(@Nullable Map<String, String> stringStringMap) {
                        return stringStringMap.get("size");
                    }
                });
                Joiner.on(" \r\n ").join(result);
            }
        }
        else if("factories_num".equals(title)){
            //richtext
            if(supplierReportExportDto.getCompany()!=null && !Strings.isNullOrEmpty(supplierReportExportDto.getCompany().getFactories())){
                String json = supplierReportExportDto.getCompany().getFactories();
                List<Map<String,String>> list = JsonMapper.nonDefaultMapper().fromJson(json, List.class);
                List<String> result = Lists.transform(list, new Function<Map<String, String>, String>() {
                    @Nullable
                    @Override
                    public String apply(@Nullable Map<String, String> stringStringMap) {
                        return stringStringMap.get("employee");
                    }
                });
                Joiner.on(" \r\n ").join(result);
            }
        }
        else if("factories_assets".equals(title)){
            //richtext
            if(supplierReportExportDto.getCompany()!=null && !Strings.isNullOrEmpty(supplierReportExportDto.getCompany().getFactories())){
                String json = supplierReportExportDto.getCompany().getFactories();
                List<Map<String,String>> list = JsonMapper.nonDefaultMapper().fromJson(json, List.class);
                List<String> result = Lists.transform(list, new Function<Map<String, String>, String>() {
                    @Nullable
                    @Override
                    public String apply(@Nullable Map<String, String> stringStringMap) {
                        return stringStringMap.get("factoryFixedAssets");
                    }
                });
                Joiner.on(" \r\n ").join(result);
            }
        }
        else if("name".equals(title)){
            if(supplierReportExportDto.getContactInfo() != null)
                return parseString(supplierReportExportDto.getContactInfo().getName());
        }
        else if("mobile".equals(title)){
            if(supplierReportExportDto.getContactInfo() != null)
                return parseString(supplierReportExportDto.getContactInfo().getMobile());
        }
        else if("officePhone".equals(title)){
            if(supplierReportExportDto.getContactInfo() != null)
                return parseString(supplierReportExportDto.getContactInfo().getOfficePhone());
        }
        else if("email".equals(title)){
            if(supplierReportExportDto.getContactInfo() != null)
                return parseString(supplierReportExportDto.getContactInfo().getEmail());
        }
        else if("recentFinance_sold".equals(title)){
            //richtext
            if(supplierReportExportDto.getFinance()!=null && !Strings.isNullOrEmpty(supplierReportExportDto.getFinance().getRecentFinance())){
                String json = supplierReportExportDto.getFinance().getRecentFinance();
                Map<String,String> map = JsonMapper.nonDefaultMapper().fromJson(json, Map.class);
                return map.get("year1")+"年:"+map.get("sold1")+" \r\n "
                        + map.get("year2")+"年:"+map.get("sold2")+" \r\n "
                        + map.get("year3")+"年:"+map.get("sold3")+" \r\n ";
            }
        }
        else if("recentFinance_net".equals(title)){
            //richtext
            if(supplierReportExportDto.getFinance()!=null && !Strings.isNullOrEmpty(supplierReportExportDto.getFinance().getRecentFinance())){
                String json = supplierReportExportDto.getFinance().getRecentFinance();
                Map<String,String> map = JsonMapper.nonDefaultMapper().fromJson(json, Map.class);
                return map.get("year1")+"年:"+map.get("net1")+" \r\n "
                        + map.get("year2")+"年:"+map.get("net2")+" \r\n "
                        + map.get("year3")+"年:"+map.get("net3")+" \r\n ";
            }
        }
        else if("assets".equals(title)){
            if(supplierReportExportDto.getCompanyExtraRD() != null)
                return parseString(parsePriceW(supplierReportExportDto.getCompanyExtraRD().getAssets()));
        }
        else if("labArea".equals(title)){
            if(supplierReportExportDto.getCompanyExtraRD() != null)
                return parseInt(supplierReportExportDto.getCompanyExtraRD().getLabArea());
        }
        else if("numberOfPatents".equals(title)){
            if(supplierReportExportDto.getCompanyExtraRD() != null)
                return parseInt(supplierReportExportDto.getCompanyExtraRD().getNumberOfPatents());
        }
        else{
            return "";
        }

        return "";

    }
}
