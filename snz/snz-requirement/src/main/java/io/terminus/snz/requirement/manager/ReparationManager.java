package io.terminus.snz.requirement.manager;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.terminus.common.model.Paging;
import io.terminus.common.utils.Joiners;
import io.terminus.common.utils.Splitters;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.dao.MWDepartNameDao;
import io.terminus.snz.requirement.dao.MarketBadRecordDao;
import io.terminus.snz.requirement.dao.SceneBadRecordDao;
import io.terminus.snz.requirement.model.MWMarketBadRecord;
import io.terminus.snz.requirement.model.MWSceneBadRecord;
import io.terminus.snz.requirement.model.MarketBadRecord;
import io.terminus.snz.requirement.model.SceneBadRecord;
import io.terminus.snz.user.dto.SupplierDto;
import io.terminus.snz.user.model.SupplierReparationSumaries;
import io.terminus.snz.user.service.CompanyService;
import io.terminus.snz.user.service.SupplierSummaryService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.MoreObjects.firstNonNull;
import static com.google.common.base.Strings.emptyToNull;
import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Date: 8/19/14
 * Time: 10:43
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */

@Slf4j
@Component
public class ReparationManager {

    private static final Integer PAGE_SIZE = 500;

    private static final DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyyMMdd");

    @Autowired
    private SceneBadRecordDao sceneBadRecordDao;

    @Autowired
    private MarketBadRecordDao marketBadRecordDao;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private MWDepartNameDao mwDepartNameDao;

    @Autowired
    private SupplierSummaryService supplierSummaryService;

    // 手动维护的关系表
    private LoadingCache<String, Optional<String>> departName;

    private LoadingCache<String, Optional<String>> getVCode;

    private LoadingCache<String, Optional<Long>> existsScene;

    private LoadingCache<String, List<String>> codeList;



    public ReparationManager() {
        departName = CacheBuilder.newBuilder()
                .expireAfterAccess(2L, TimeUnit.DAYS)
                .build(new CacheLoader<String, Optional<String>>() {
                    @Override
                    public Optional<String> load(String key) throws Exception {
                        return Optional.fromNullable(mwDepartNameDao.getDepartName(key));
                    }
                });

        getVCode = CacheBuilder.newBuilder()
                .expireAfterAccess(2L, TimeUnit.DAYS)
                .build(new CacheLoader<String, Optional<String>>() {
                    @Override
                    public Optional<String> load(String key) throws Exception {
                        List<String> param = Lists.newArrayList(Splitters.COMMA.split(key));
                        if (param.size() < 2) {
                            return Optional.absent();
                        } else {
                            return Optional.fromNullable(mwDepartNameDao.getVCode(param.get(0), param.get(1)));
                        }
                    }
                });

        codeList = CacheBuilder.newBuilder()
                .expireAfterWrite(2l, TimeUnit.DAYS)
                .build(new CacheLoader<String, List<String>>() {
                    @Override
                    public List<String> load(String key) throws Exception {
                        return mwDepartNameDao.getDepartCode(key);
                    }
                });

        existsScene = CacheBuilder.newBuilder()
                .expireAfterWrite(1l, TimeUnit.DAYS)
                .build(new CacheLoader<String, Optional<Long>>() {
                    @Override
                    public Optional<Long> load(String key) throws Exception {
                        SceneBadRecord sbr = sceneBadRecordDao.findByWId(key);
                        if (sbr == null) {
                            return Optional.absent();
                        }
                        return Optional.fromNullable(sbr.getId());
                    }
                });
    }

    @Transactional
    public void deltaDumpScene(DateTime start, DateTime end) throws Exception {
        // get all vcode
        Map<Long, String> vCodes = Maps.newHashMap();
        Date startAt = start.toDate(), endAt = end.toDate();
        Response<Map<Long, String>> vcodesGet = companyService.companyHasVcode(0, PAGE_SIZE);
        Integer handled = 0;
        while (vcodesGet.isSuccess() && !vcodesGet.getResult().isEmpty()) {
            vCodes.putAll(vcodesGet.getResult());

            handled += vcodesGet.getResult().size();
            vcodesGet = companyService.companyHasVcode(handled, PAGE_SIZE);
        }
        if (vCodes.isEmpty()) {
            return;
        }


        // get mmax id for dump
        Long maxId = sceneBadRecordDao.maxMWId();
        if (maxId == null) {
            return;
        }


        List<MWSceneBadRecord> mwList =  sceneBadRecordDao.forMWDump(maxId, 0, PAGE_SIZE, startAt, endAt);
        while (!mwList.isEmpty()) {
            handled = 0;
            List<Long> ids = Lists.newArrayList();
            while (!mwList.isEmpty()) {
                for (MWSceneBadRecord mw : mwList) {
                    // 没有对应的 Company 略过
                    if (!vCodes.containsValue(mw.getSupplycode())) {
                        ids.add(mw.getId());
                        continue;
                    }

                    // skip money is 0
                    SceneBadRecord sbr = fromMWSceneBadRecord(mw);
                    if (sbr.getMoney() == 0) {
                        ids.add(mw.getId());
                        continue;
                    }

                    // skip if exists
                    if (existsScene.getUnchecked(mw.getWasterid()).isPresent()) {
                        ids.add(mw.getId());
                        continue;
                    }
                    Optional<String> name = departName.getUnchecked(mw.getCareerdepart());
                    if (name.isPresent()) {
                        sbr.setDepartName(name.get());
                    }
                    sceneBadRecordDao.create(sbr);
                    existsScene.put(sbr.getVCode(), Optional.of(sbr.getId()));
                    ids.add(mw.getId());
                }
                maxId = mwList.get(mwList.size() - 1).getId();
                handled += mwList.size();
                if (handled > 10000) {
                    break;
                }
                mwList = sceneBadRecordDao.forMWDump(maxId, handled, PAGE_SIZE, startAt, endAt);
            }

            // clean and next page
            handled = 0;
            Integer size = 100;
            while (!ids.isEmpty() && handled < ids.size()) {
                try {
                    if (handled + 100 >= ids.size()) {
                        size = ids.size() - handled;
                    }
                    sceneBadRecordDao.deleteMWByIds(ids.subList(handled, handled + size));
                } catch (Exception e) {
                    log.error("delete scene bad record fail, e:{}", e);
                } finally {
                    handled += 100;
                }
            }
            mwList = sceneBadRecordDao.forMWDump(maxId, handled, PAGE_SIZE, startAt, endAt);
        }
    }

    private SceneBadRecord fromMWSceneBadRecord(MWSceneBadRecord mw) {
        SceneBadRecord sbr = new SceneBadRecord();
        sbr.setVCode(mw.getSupplycode());
        sbr.setModuleNum(mw.getSpecialtiesno());
        sbr.setWId(mw.getWasterid());
        sbr.setWCount(Integer.valueOf(mw.getWastercount()));
        sbr.setLoadBatch(mw.getLoad_batch());
        sbr.setMoney((int) (Double.valueOf(mw.getPenalty()) * 100));
        sbr.setDepart(mw.getCareerdepart());
        sbr.setSendAt(dtf.parseDateTime(mw.getSendday()).toDate());

        return sbr;
    }

    public void deltaSyncMarketData(DateTime start, DateTime end) {
        Date startAt = start.toDate(), endAt = end.toDate();

        // get max id
        Long maxId = marketBadRecordDao.maxMWIdIn(startAt, endAt);
        if (maxId == null) {
            return;
        }

        List<MWMarketBadRecord> markets = marketBadRecordDao.forMWDumpIn(maxId, null, 0, PAGE_SIZE, startAt, endAt);
        Integer handled = 0;
        List<Long> ids = Lists.newArrayList();
        while (!markets.isEmpty()) {
            for (MWMarketBadRecord mmbr : markets) {
                // not if the module num is empty
                if ( isNullOrEmpty(mmbr.getModuleNum()) ) {
                    ids.add(mmbr.getId());
                    continue;
                }

                // money unit to RMB fen
                // not if both fee and price is empty
                Integer price = (int) (Float.valueOf(firstNonNull(emptyToNull(mmbr.getPrice()), "0")) * 100);
                Integer fee = (int) (Float.valueOf(firstNonNull(emptyToNull(mmbr.getFee()), "0")) * 100);
                if ( (price + fee) == 0) {
                    ids.add(mmbr.getId());
                    continue;
                }

                // not if no v code
                List<String> codes = codeList.getUnchecked(mmbr.getBusiness());
                String vCode = null;
                for (String code:codes) {
                    Optional<String> v = getVCode.getUnchecked(Joiners.COMMA.join(code, mmbr.getModuleNum()));
                    if (v.isPresent()) {
                        vCode = v.get();
                        break;
                    }
                }
                if (Strings.isNullOrEmpty(vCode)) {
                    ids.add(mmbr.getId());
                    continue;
                }

                MarketBadRecord mbr = MarketBadRecord.from(mmbr);
                mbr.setPrice(price);
                mbr.setFee(fee);
                marketBadRecordDao.create(mbr);
            }

            // clean and next page
            maxId = markets.get(markets.size()-1).getId();
            handled += markets.size();
            markets = marketBadRecordDao.forMWDumpIn(maxId, null, handled, PAGE_SIZE, startAt, endAt);
        }

        // clean and next page
        handled = 0;
        Integer size = 100;
        while ( !ids.isEmpty() && handled < ids.size() ) {
            try {
                if (handled + 100 >= ids.size()) {
                    size = ids.size() - handled;
                }
                sceneBadRecordDao.deleteMWByIds(ids.subList(handled, handled + size));
            } catch (Exception e) {
                log.error("delete scene bad record fail, e:{}", e);
            } finally {
                handled += 100;
            }
        }
    }

//    @Transactional
    public void fullDumpMarketSummary() throws Exception {
        // with start time of day like '2014-09-16 00:00:00'
        DateTime todayDT = DateTime.now().withTimeAtStartOfDay();
        Date today = todayDT.toDate();
        Date yesterday = todayDT.minusDays(1).toDate();

        // like '2014-01-01 00:00:00'
        Date lastYear = DateTime.parse(String.valueOf(todayDT.getYear())).toDate();

        // with start day of month like '2014-09-01 00:00:00'
        Date thisMonth = todayDT.minusDays(todayDT.getDayOfMonth()-1).toDate();

        // start with monday of the week
        Date thisWeek = todayDT.minusDays(todayDT.getDayOfWeek()-1).toDate();

        // get  find first 500 suppliers
        BaseUser user = new BaseUser();
        Response<Paging<SupplierDto>> getSupplierDtos = companyService.pagingCompanyHasSupplierCode(user, null, 0, PAGE_SIZE);
        if (!getSupplierDtos.isSuccess()) {
            log.error("`fullDumpMarketSummary` find supplier dto page fail, e:{}", getSupplierDtos.getError());
            throw new Exception(getSupplierDtos.getError());
        }
        Paging<SupplierDto> supplierPage = getSupplierDtos.getResult();

        // start process
        Integer handled = 0;
        while (supplierPage.getTotal()!=0l && !supplierPage.getData().isEmpty()) {
            for (SupplierDto sd : supplierPage.getData()) {
                SupplierReparationSumaries srs = new SupplierReparationSumaries();
                srs.setSupplierUid(sd.getUserId());

                List<MarketBadRecord> dumpList = marketBadRecordDao.forDumpIn(null, sd.getVCode(), 0, PAGE_SIZE, lastYear, today);
                Integer summed = 0;
                if (dumpList.isEmpty()) {
                    continue;
                }
                while (!dumpList.isEmpty()) {
                    for (MarketBadRecord mbr : dumpList) {
                        // yearly
                        srs.increaseYearlyAmount(mbr.getPrice() + mbr.getFee());

                        // monthly
                        if (!mbr.getReportAt().before(thisMonth)) {
                            srs.increaseMonthlyAmount(mbr.getPrice() + mbr.getFee());

                            // weekly
                            if (!mbr.getReportAt().before(thisWeek)) {
                                srs.increaseWeeklyAmount(mbr.getPrice() + mbr.getFee());

                                // daily
                                if (!mbr.getReportAt().before(yesterday)) {
                                    srs.increaseDailyAmount(mbr.getPrice() + mbr.getFee());
                                }
                            }
                        }
                    } // for loop end
                    summed += dumpList.size();
                    dumpList = marketBadRecordDao.forDumpIn(null, sd.getVCode(), summed, PAGE_SIZE, lastYear, today);
                } // while loop end

                srs.setStartAt(lastYear);
                srs.setEndAt(today);
                Response<Long> tryUpdate = supplierSummaryService.insertOrUpdateReparationSummary(srs);
                if (!tryUpdate.isSuccess()) {
                    // we don't care update error, just go on
                    log.error("insert or update reparation summary fail, summary:{}", srs);
                }
            } // for loop end

            // get next page
            handled += supplierPage.getData().size();
            getSupplierDtos = companyService.pagingCompanyHasSupplierCode(user, null, handled, PAGE_SIZE);
            if (!getSupplierDtos.isSuccess()) {
                log.error("`fullDumpMarketSummary` find supplier dto page fail, e:{}", getSupplierDtos.getError());
                throw new Exception(getSupplierDtos.getError());
            }
            supplierPage = getSupplierDtos.getResult();
        } // while loop end
    }
}
