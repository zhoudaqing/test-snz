package io.terminus.snz.requirement.service;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.terminus.common.model.PageInfo;
import io.terminus.common.utils.Arguments;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.dao.MarketBadRecordDao;
import io.terminus.snz.requirement.dao.SceneBadRecordDao;
import io.terminus.snz.requirement.dao.TmoneBadRecordDao;
import io.terminus.snz.requirement.dto.SupplierReparationDailyDto;
import io.terminus.snz.requirement.dto.SupplierReparationDetailDtoExport;
import io.terminus.snz.requirement.dto.SupplierReparationsDto;
import io.terminus.snz.requirement.manager.ReparationManager;
import io.terminus.snz.requirement.model.MarketBadRecord;
import io.terminus.snz.requirement.model.SceneBadRecord;
import io.terminus.snz.requirement.model.TmoneBadRecord;
import io.terminus.snz.user.model.Company;
import io.terminus.snz.user.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkState;
import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Date: 8/11/14
 * Time: 11:12
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */

@Slf4j
@Service
public class ReparationServiceImpl implements ReparationService {

    private static final DateTimeFormatter DFT = DateTimeFormat.forPattern("yyyy-MM-dd");

    @Autowired
    private SceneBadRecordDao sceneBadRecordDao;

    @Autowired
    private MarketBadRecordDao marketBadRecordDao;

    @Autowired
    private TmoneBadRecordDao tmoneBadRecordDao;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ReparationManager reparationManager;

    @Override
    public Response<SupplierReparationsDto> findSupplierReparationBy(BaseUser user, Long userId, Integer range, Integer type,
                                                                     String startAt, String endAt, Integer pageNo, Integer size) {
        Response<SupplierReparationsDto> result = new Response<SupplierReparationsDto>();
        PageInfo page = new PageInfo(pageNo, 2);

        // 检查用户登录
        if (user==null || user.getId()==null) {
            result.setError("user.not.login");
            return result;
        }
        Long supplierUserId = Objects.firstNonNull(userId, user.getId());

        // 检查数据类型
        TYPE t = TYPE.fromValue(type);
        Integer newType;
        if (t!=null) {
            newType = t.getValue();
        } else {
            newType = null;
        }

        // 检查起止时间
        Map<String, DateTime> dates = getDateClosure(RANGE.fromValue(range).getRange(), startAt, endAt);
        DateTime startAtTemp = dates.get("startAt"), endAtTemp = dates.get("endAt");


        try {
            SupplierReparationsDto resultDto = new SupplierReparationsDto();

            // 得到一个供应商V码
            Response<Company> companyGet = companyService.findCompanyByUserId(supplierUserId);
            checkState(companyGet.isSuccess(), companyGet.getError());
            Company company = companyGet.getResult();
            checkState(!isNullOrEmpty(company.getSupplierCode()), "supplier.illegal.code");
            resultDto.setSupplierName(company.getCorporation());

            // 空位补全
            // 遍历，补足
            range = Days.daysBetween(startAtTemp, endAtTemp).getDays() + 1;

            // 遍历，补足
            List all = findDaysBetweenByType(null, company.getSupplierCode(), startAtTemp, endAtTemp, t);
            breakDownByDay(resultDto, all, startAtTemp, endAtTemp, t);

            // for highchart yAixs data point
            // 在这里分页
            List<SupplierReparationDailyDto> pageData = Lists.newArrayList();
            for (SupplierReparationDailyDto r: resultDto.getDetails()) {
                resultDto.getMoneyList().add(r.getMoney());
                resultDto.getQualities().add(r.getQuantity());
                resultDto.addMoney((int)(r.getMoney()*100));
                resultDto.addQuantity(r.getQuantity());

                if (!Arguments.isNullOrEmpty(r.getReparations())) {
                    pageData.add(r);
                }
            }

            resultDto.getCurrentPage().setTotal((long)pageData.size());       // set total here
            if (page.getOffset()>pageData.size()) {
                page.setOffset(pageData.size());
            }
            if ((page.getOffset() + page.getLimit())>pageData.size()) {
                page.setLimit(pageData.size() - page.getOffset());
            }
            resultDto.getCurrentPage().setData(pageData.subList(page.getOffset(), page.getOffset() + page.getLimit()));
            result.setResult(resultDto);
        } catch (IllegalStateException e) {
            log.error("`findSupplierReparationBy` invoke fail. with user:{}, range:{}, type:{}, startAt:{}, endAt{}, e:{}",
                    user, range, newType, startAt, endAt, e);
            result.setError(e.getMessage());
            return result;
        } catch (Exception e) {
            log.error("`findSupplierReparationBy` invoke fail. with user:{}, range:{}, type:{}, startAt:{}, endAt{}, e:{}",
                    user, range, newType, startAt, endAt, Throwables.getStackTraceAsString(e));
            result.setError("supplier.reparation.find.fail");
            return result;
        }

        return result;
    }

    /**
     * 将查询得到的所有索赔记录按照类型和日期按天分组插入结果dto集合
     *
     * @param resultDto      结果 DTO
     * @param all            查询到的所有索赔记录
     * @param startAt        开始时间
     * @param endAt          结束时间
     * @param type           索赔类型
     */
    private void breakDownByDay(SupplierReparationsDto resultDto, List all, DateTime startAt, DateTime endAt, TYPE type) {
        DateTime cursor = startAt;
        switch (type) {
            case VISIT:
                // pass for now
                break;
            case SCENE:
                while (!cursor.isAfter(endAt)) {
                    SupplierReparationDailyDto daily = new SupplierReparationDailyDto();
                    List<SceneBadRecord> sMatch = Lists.newArrayList();
                    for (Object sbr:all) {
                        if (Days.daysBetween(
                                cursor, new DateTime(((SceneBadRecord)sbr).getSendAt().getTime())).getDays()
                                == 0) {
                            sMatch.add((SceneBadRecord)sbr);
                        }
                    }
                    daily.setAllScene(sMatch);
                    daily.setCurrentDate(cursor.toDate());
                    resultDto.getDetails().add(daily);

                    cursor = cursor.plusDays(1);
                }
                break;
            case MARKET:
                while (!cursor.isAfter(endAt)) {
                    SupplierReparationDailyDto daily = new SupplierReparationDailyDto();
                    List<MarketBadRecord> mMatch = Lists.newArrayList();
                    for (Object mbr:all) {
                        if (Days.daysBetween(
                                cursor, new DateTime(((MarketBadRecord)mbr).getReportAt().getTime()))
                                .getDays() == 0) {
                            mMatch.add((MarketBadRecord) mbr);
                        }
                    }
                    daily.setAllMarket(mMatch);
                    daily.setCurrentDate(cursor.toDate());
                    resultDto.getDetails().add(daily);

                    cursor = cursor.plusDays(1);
                }
                break;
            case TMONE:
                List<TmoneBadRecord> tmones = (List<TmoneBadRecord>)all.get(0);
                List<Map<String, Object>> moneyList = (List<Map<String, Object>>)all.get(1);
                while (!cursor.isAfter(endAt)) {
                    SupplierReparationDailyDto daily = new SupplierReparationDailyDto();
                    List<TmoneBadRecord> tMatch = Lists.newArrayList();
                    for (Object tbr:tmones) {
                        if (Days.daysBetween(cursor, new DateTime(((TmoneBadRecord)tbr).getCurrentDay().getTime())).getDays() == 0) {
                            tMatch.add((TmoneBadRecord) tbr);
                        }
                    }
                    daily.setAllTmone(tMatch);
                    int money = 0;
                    for (Map<String, Object> moneyMap : moneyList) {
                        if(moneyMap.get("current") != null && moneyMap.get("money") != null){
                            if(Days.daysBetween(cursor, new DateTime(moneyMap.get("current").toString())).getDays() == 0 ){
                                money = Integer.parseInt(moneyMap.get("money").toString());
                            }
                        }
                    }
                    daily.setMoney((double)money/100);
                    daily.setCurrentDate(cursor.toDate());
                    resultDto.getDetails().add(daily);

                    cursor = cursor.plusDays(1);
                }
                break;
            default:break;
        }
    }
   

	@Override
    public Response<List<SupplierReparationDetailDtoExport>> findSupplierDailyReparationsBy(BaseUser user, Long userId, Integer range,
                                                                               Integer type, String startAt, String endAt) {
        Response<List<SupplierReparationDetailDtoExport>> result = new Response<List<SupplierReparationDetailDtoExport>>();
        // 检查用户登录
        if (user==null || user.getId()==null) {
            result.setError("user.not.login");
            return result;
        }
        Long supplierUserId = MoreObjects.firstNonNull(userId, user.getId());

        // 检查数据类型
        TYPE t = TYPE.fromValue(type);
        Integer newType;
        if (t!=null) {
            newType = t.getValue();
        } else {
            newType = null;
        }

        // 检查起止时间
        Map<String, DateTime> dates = getDateClosure(RANGE.fromValue(range).getRange(), startAt, endAt);
        DateTime startAtTemp = dates.get("startAt"), endAtTemp = dates.get("endAt");
        try {
            // 得到一个供应商V码
            Response<Company> companyGet = companyService.findCompanyByUserId(supplierUserId);
            checkState(companyGet.isSuccess(), companyGet.getError());
            Company company = companyGet.getResult();
            // todo: i18n
            checkState(!isNullOrEmpty(company.getSupplierCode()), "supplier.illegal.code");

            // 忽略空值
            List<SupplierReparationDetailDtoExport> dailyDtos = Lists.newArrayList();
            List resultSet = findDaysBetweenByType(null, company.getSupplierCode(), startAtTemp, endAtTemp, t);
            for (Object r:resultSet) {
                SupplierReparationDetailDtoExport dto = new SupplierReparationDetailDtoExport(r);
                dto.setType(t.getValue());
                dailyDtos.add(dto);
            }

            result.setResult(dailyDtos);
        } catch (IllegalStateException e) {
            log.error("`findSupplierReparationBy` invoke fail. with user:{}, range:{}, type:{}, startAt:{}, endAt{}, e:{}",
                    user, range, newType, startAt, endAt, e);
            result.setError(e.getMessage());
            return result;
        } catch (Exception e) {
            log.error("`pageSupplierReparationBy` invoke fail. with user:{}, range:{}, type:{}, startAt:{}, endAt{}, e:{}",
                    user, range, newType, startAt, endAt, e);
            result.setError("supplier.reparation.find.fail");
            return result;
        }

        return result;
    }

    /**
     * 根据时间范围、种类和供应商编码查找一个供应商绩效
     * @param daily    供应商每日明细
     * @param vcode    供应商编码
     * @param start    开始时间
     * @param end      截止时间，默认为空，则查找一天
     * @param t        查询不良的类型
     */
    private List findDaysBetweenByType(@Nullable SupplierReparationDailyDto daily, String vcode, DateTime start, DateTime end, TYPE t) {
        DateTime endAt = Objects.firstNonNull(end, start.plusDays(1));
        switch (t) {
            case MARKET:
                List<MarketBadRecord> markets = marketBadRecordDao.findBetween(vcode, start.toDate(), endAt.toDate());
                if (daily != null) {
                    daily.setAllMarket(markets);
                }
                return markets;
            case SCENE:
                List<SceneBadRecord> scenes = sceneBadRecordDao.findBetween(vcode, start.toDate(), endAt.toDate());
                if (daily != null) {
                    daily.setAllScene(scenes);
                }
                return scenes;
            case TMONE:
                List<TmoneBadRecord> tmones = tmoneBadRecordDao.findBetween(vcode, start.toDate(), endAt.toDate());
                List<Map<String, Object>> moneyList = tmoneBadRecordDao.findMoneyList(vcode, start.toDate(), endAt.toDate());
                List<Object> returnList = Lists.newArrayList();
                if (daily != null) {
                    daily.setAllTmone(tmones);
                }
                returnList.add(tmones);
                returnList.add(moneyList);
                return returnList;
            default:
                return Collections.emptyList();
        }
    }

    @Override
    public Response<Boolean> deltaDumpScene(String startAt, String endAt) {
        Response<Boolean> result = new Response<Boolean>();
        final Map<String, DateTime> dates = getDateClosure(60, startAt, endAt);

        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        reparationManager.deltaDumpScene(dates.get("startAt"), dates.get("endAt"));
                    } catch (Exception e) {
                        log.error("full dump scene bad record fail, e:{}", e);
                    }
                }
            }).start();
            result.setResult(true);
        } catch (Exception e) {
            log.error("`deltaDumpScene` invoke fail. e:{}", e);
            result.setError("reparation.sync.scene.data.fail");
            return result;
        }
        return result;
    }

    @Override
    public Response<Boolean> fullDumpMarketSummary() {
        Response<Boolean> result = new Response<Boolean>();

        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        reparationManager.fullDumpMarketSummary();
                    } catch (Exception e) {
                        log.error("`fullDumpMarketSummary` invoke fail. ");
                    }
                }
            }).start();
            result.setResult(true);
        } catch (Exception e) {
            log.error("`fullDumpMarketSummary` invoke fail. ");
            result.setError("reparation.market.full.dump.fail");
            return result;
        }
        return result;
    }
    
    @Override
    public Response<Boolean> deltaSyncMarketData(final String startAt, final String endAt) {
        Response<Boolean> result = new Response<Boolean>();

        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Map<String, DateTime> dates = getDateClosure(1, startAt, endAt);
                    reparationManager.deltaSyncMarketData(dates.get("startAt"), dates.get("endAt"));
                }
            });

            result.setResult(true);
        } catch (Exception e) {
            log.error("`deltaSyncMarketData` invoke fail. with start:{}, end:{}, e:{}", startAt, endAt, e);
            result.setError("reparation.sync.market.data.fail");
            return result;
        }
        return result;
    }

    /**
     * 获取一段时间范围
     * 1. 不存在开始时间，根据 range 得出结束时间
     * 2. 不存在结束时间，则取昨天
     * @param range      一段时间范围
     * @param startAt    开始时间
     * @param endAt      结束时间
     * @return k-v 形式的起止时间
     */
    private Map<String, DateTime> getDateClosure(Integer range, String startAt, String endAt) {
        DateTime startAtTemp;
        DateTime endAtTemp;
        if (!isNullOrEmpty(startAt)) {   //若为空则默认开始时间一周前
            startAtTemp = DFT.parseDateTime(startAt);
        } else {
            startAtTemp = DateTime.now().withTimeAtStartOfDay().minusDays(range - 1);
        }

        if (!isNullOrEmpty(endAt)) {   //若为空则默认开始时间为今日开始
            endAtTemp = DFT.parseDateTime(endAt);
            if (endAtTemp.withTimeAtStartOfDay().isEqual(startAtTemp)) {
                endAtTemp =  startAtTemp.plusDays(1).withTimeAtStartOfDay();
            } else if (endAtTemp.isBefore(startAtTemp)) {
                endAtTemp =  DateTime.now().withTimeAtStartOfDay();
            }
        } else {
            endAtTemp =  DateTime.now().withTimeAtStartOfDay();
        }

        Map<String, DateTime> params = Maps.newHashMap();
        params.put("startAt", startAtTemp);
        params.put("endAt", endAtTemp);
        return params;
    }

    private enum RANGE {
        WEEK(1, 7),
        MONTH(2, 30),
        YEAR(3, 365);

        private int value;
        private int range;

        private RANGE(int value, int range) {
            this.value = value;
            this.range = range;
        }

        public static RANGE fromValue(Integer s) {
            if (s == null) {
                return WEEK;
            }
            for (RANGE t : RANGE.values()) {
                if (t.value == s) {
                    return t;
                }
            }
            return WEEK;
        }

        public int getRange() {
            return range;
        }

        public int getValue() {
            return value;
        }
    }

    public static enum  TYPE {
        VISIT(1, "入厂索赔"),
        SCENE(2, "现场索赔"),
        TMONE(3, "T-1索赔"),
        MARKET(4, "市场索赔");

        private int value;
        private String display;

        private TYPE(int value, String display) {
            this.value = value;
            this.display = display;
        }

        public static TYPE fromValue(Integer s) {
            if (s == null) return MARKET;
            for (TYPE t : TYPE.values()) {
                if (t.value == s)
                    return t;
            }
            return MARKET;
        }

        public int getValue() {
            return value;
        }

        public String getDisplay() {
            return display;
        }
    }
}
