package io.terminus.snz.related.components;

import com.google.common.base.Objects;
import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import io.terminus.common.annotation.RedisLock;
import io.terminus.pampas.common.Response;
import io.terminus.snz.eai.service.ClaimInfoDetailService;
import io.terminus.snz.eai.service.ClaimInfoService;
import io.terminus.snz.related.daos.CompensationDao;
import io.terminus.snz.related.models.Compensation;
import io.terminus.snz.related.models.CompensationDetail;
import io.terminus.snz.related.services.CompensationDetailService;
import io.terminus.snz.related.services.CompensationServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.example.sendclaiminfotogvs.ZSPJHOUTPUT;
import org.example.transstandardcredittosrm.ZINTZDSP12LOG20SENDING;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.ws.Holder;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 从SAP同步索赔信息
 * MADE IN IntelliJ IDEA
 * AUTHOR: haolin
 * AT 24/7/14.
 */
@Slf4j
public class CompensationInfoSyncer {

    @Autowired
    private CompensationServiceImpl compensationService;

    @Autowired
    private CompensationDetailService compensationDetailService;

    @Autowired
    private CompensationDao compensationDao;

    @Autowired
    private ClaimInfoService claimInfoService;

    @Autowired
    private ClaimInfoDetailService claimInfoDetailService;

    private static final DateTimeFormatter DEFAULT_DAY_FMT = DateTimeFormat.forPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DEFAULT_MONTH_FMT = DateTimeFormat.forPattern("yyyy-MM-30");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormat.forPattern("HH:mm:ss");


    /**
     * 数据同步整体调用
     */
    @RedisLock(keyName = "CompensationInfoSyncer:allSync", maxWait = 1, expiredTime = 1000*60*5)
    public void allSync(){
        syncByDay();
        syncdByDay();
        syncByMonth();
        syncdByMonth();
        changeStatus();
        log.info("update.success");
    }

    /**
     * 日同步, 每天子时
     */
    public void syncByDay(){
        log.info("Start sync compensation info from sap by day");
        Stopwatch watch = Stopwatch.createStarted();
        DateTime now = new DateTime();
        String today = now.toString(DEFAULT_DAY_FMT);
        try {
            Response<List<ZSPJHOUTPUT>> claimInfosResp = claimInfoService.getClaimInfos(today, null);
            if (!claimInfosResp.isSuccess()){
                log.error("failed to get claim infos from eai by day, cause: {}", claimInfosResp.getError());
                return;
            }
            List<Compensation> compensations = toCompensations(claimInfosResp.getResult());
            Response<Boolean> resp = compensationService.batchAdd(compensations);
            if (!resp.isSuccess()){
                log.error("failed to sync compensation info from say by day({}), cause: {}", today, resp.getError());
            }
        } catch (Exception e){
            log.error("failed to sync compensation info from say by day({}), cause: {}",
                    today, Throwables.getStackTraceAsString(e));
        }
        watch.stop();
        log.info("End sync compensation info from sap by day, cost time: {} ms", watch.elapsed(TimeUnit.MILLISECONDS));
    }

    /**
     * 日同步明细列表，每天子时
     * */

    public void syncdByDay(){
        log.info("Start sync compensation detailinfo from sap by day");
        Stopwatch watch = Stopwatch.createStarted();
        DateTime now = new DateTime();
        String today = now.toString(DEFAULT_DAY_FMT);
        try {
            Response<Holder<List<ZINTZDSP12LOG20SENDING>>> claimInfoDetailsResp = claimInfoDetailService.getClaimInfoDetails(today);
            if(!claimInfoDetailsResp.isSuccess()){
                log.error("failed to get claim detailinfos from eai by day,cause: {}", claimInfoDetailsResp.getError());
                return;
            }

            Holder<List<CompensationDetail>> compensationDetails = toCompensationDetails(claimInfoDetailsResp.getResult().value);
            Response<Boolean> resp = compensationDetailService.batchAdd(compensationDetails.value);
            if (!resp.isSuccess()){
                log.error("failed to sync compensation detailinfo from say by day({}), cause: {}", today, resp.getError());
            }
        } catch (Exception e){
            log.error("failed to sync compensation detailinfo from say by day({}), cause: {}",
                    today, Throwables.getStackTraceAsString(e));
        }
        watch.stop();
        log.info("End sync compensation detailinfo from sap by day, cost time: {} ms", watch.elapsed(TimeUnit.MILLISECONDS));
    }

     /**
     * 月同步, 每月最后一天子时
     */
    public void syncByMonth(){
        log.info("Start sync compensation info from sap by month");
        Stopwatch watch = Stopwatch.createStarted();
        String thisMonth = DateTime.now().toString(DEFAULT_MONTH_FMT);
        try {
            Response<List<ZSPJHOUTPUT>> claimInfosResp = claimInfoService.getClaimInfos(null, thisMonth);
            if (!claimInfosResp.isSuccess()){
                log.error("failed to get claim infos from eai by month, cause: {}", claimInfosResp.getError());
                return;
            }
            List<Compensation> compensations = toCompensations(claimInfosResp.getResult());
            Response<Boolean> resp = compensationService.batchAdd(compensations);
            if (!resp.isSuccess()){
                log.error("failed to sync compensation info from say by month({}), cause: {}",
                        thisMonth, resp.getError());
            }
        } catch (Exception e){
            log.error("failed to sync compensation info from say by month({}), cause: {}",
                    thisMonth, Throwables.getStackTraceAsString(e));
        }
        watch.stop();
        log.info("End sync compensation info from sap by month, cost time: {} ms", watch.elapsed(TimeUnit.MILLISECONDS));
    }

    /**
     * 月同步，每月最后一天子时
     * */
    public void syncdByMonth() {
        log.info("Start sync compensation detailinfo from sap by day");
        Stopwatch watch = Stopwatch.createStarted();
        DateTime now = new DateTime();
        String thisMonth = now.toString(DEFAULT_MONTH_FMT);
        try {
            Response<Holder<List<ZINTZDSP12LOG20SENDING>>> claimInfoDetailsResp = claimInfoDetailService.getClaimInfoDetails(thisMonth);
            if(!claimInfoDetailsResp.isSuccess()){
                log.error("failed to get claim detailinfos from eai by day,cause: {}", claimInfoDetailsResp.getError());
                return;
            }

            Holder<List<CompensationDetail>> compensationDetails = toCompensationDetails(claimInfoDetailsResp.getResult().value);
            Response<Boolean> resp = compensationDetailService.batchAdd(compensationDetails.value);
            if (!resp.isSuccess()){
                log.error("failed to sync compensation detailinfo from say by month({}), cause: {}", thisMonth, resp.getError());
            }
        } catch (Exception e){
            log.error("failed to sync compensation detailinfo from say by month({}), cause: {}",
                    thisMonth, Throwables.getStackTraceAsString(e));
        }
        watch.stop();
        log.info("End sync compensation detailinfo from sap by month, cost time: {} ms", watch.elapsed(TimeUnit.MILLISECONDS));
    }

    /**
     * 转换ZSPJHOUTPUT对象列表为Compensation对象列表
     */
    private List<Compensation> toCompensations(List<ZSPJHOUTPUT> claimInfos) {
        if (Iterables.isEmpty(claimInfos)){
            return Collections.emptyList();
        }
        List<Compensation> compensations = Lists.newArrayListWithCapacity(claimInfos.size());
        for (ZSPJHOUTPUT claiminfo : claimInfos){
            compensations.add(toCompensation(claiminfo));
        }
        return compensations;
    }

    /**
     * 转换ZINTZDSP12LOG20SENDING对象列表为CompensationDetail对象列表*/

    private Holder<List<CompensationDetail>> toCompensationDetails(List<ZINTZDSP12LOG20SENDING> claimDetailInfos) {
        if(Iterables.isEmpty(claimDetailInfos)) {
            return new Holder<List<CompensationDetail>>();
        }
        List<CompensationDetail> compensationDs = Lists.newArrayListWithCapacity(claimDetailInfos.size());
        for (ZINTZDSP12LOG20SENDING claimdinfo : claimDetailInfos){
            compensationDs.add(toCompensationDetail(claimdinfo));
        }
        return  new Holder<List<CompensationDetail>>(compensationDs);
    }

    /**
     * 转换ZSPJHOUTPUT对象为Compensation对象
     */
    private Compensation toCompensation(ZSPJHOUTPUT claiminfo) {
        Compensation c = new Compensation();
        c.setPark(claiminfo.getZYY());                                  //园区
        c.setProductLine(claiminfo.getZCYX());                          //产业线
        c.setSupplierAccount(claiminfo.getLIFNR());                     //供应商或债权人账号
        c.setSupplierName(claiminfo.getNAME1());                        //供应商名称
        Integer isInternal = Objects.equal("N", claiminfo.getEXINFLAG()) ? 0 : 1;
        c.setIsInternal(isInternal);                                    //是否是内部供应商
        c.setFactory(claiminfo.getWERKS());                             //工厂
        //c.setFactoryName();                                           //工厂名称 没提供该字段
        c.setMaterielNo(claiminfo.getMATNR());                          //物料号
        c.setMaterielDesc(claiminfo.getMAKTX());                        //物料描述
        c.setPurchaserGroup(claiminfo.getEKGRP());                      //采购组
        c.setPurchaserGroupDesc(claiminfo.getEKNAM());                  //采购组描述
        c.setScore(claiminfo.getKOUFEN().toString());                   //扣分
        c.setMoney(claiminfo.getZKKJE().toString());                    //扣款金额
        //c.setDeductedAt();                                            //扣款时间 妹提供该字段
        c.setCurrent(DateTime.parse(claiminfo.getZDATE()).toDate());    //当前时间
        c.setStatus(Compensation.Status.INIT.value());                  //扣款状态
        c.setResult(claiminfo.getZFMAS());                              //扣款结果
        if(claiminfo.getZFDAT()!=null && !"".equals(claiminfo.getZFDAT().trim()))
            c.setEnteredAt(DateTime.parse(claiminfo.getZFDAT()).toDate());  //凭证入账日期
        return c;
    }
    /**
     * 转换ZINTZDSP12LOG20SENDING对象为CompensationDetail对象
     * */
    private CompensationDetail toCompensationDetail(ZINTZDSP12LOG20SENDING claimdetailinfo) {
        CompensationDetail detail = new CompensationDetail();
        detail.setListNum(claimdetailinfo.getAUFNR());                                          //订单号
        detail.setFactory(claimdetailinfo.getWERKS());                                          //工厂
        detail.setNumOfProof(claimdetailinfo.getEBELN());                                       //采购凭证号
        detail.setNumOfParam(claimdetailinfo.getEBELP());                                       //采购凭证的项目编号
        detail.setSupplierAccount(claimdetailinfo.getLIFNR());                                  //供应商标识
        detail.setMaterielNo(claimdetailinfo.getMATNR());                                       //物料号
        Date today = DateTime.now().toDate();
        if(!Strings.isNullOrEmpty(claimdetailinfo.getXQDATE()))
            detail.setTimeOfDelivery(DateTime.parse(claimdetailinfo.getXQDATE()).toDate());         //应交货日期
        else
            detail.setTimeOfDelivery(today);
        if(!Strings.isNullOrEmpty(claimdetailinfo.getSHOUHUOTIME()))
            detail.setRTimeOfDelivery(DateTime.parse(claimdetailinfo.getSHOUHUOTIME()).toDate());   //实际交货日期
        else
            detail.setRTimeOfDelivery(today);
        if(!Strings.isNullOrEmpty(claimdetailinfo.getYYDATE()))
            detail.setYTimeOfDelivery(DateTime.parse(claimdetailinfo.getYYDATE()).toDate());        //预约交货日期
        else
            detail.setYTimeOfDelivery(today);
        detail.setNumOfList(claimdetailinfo.getNUM1().intValue());                              //采购订单数量
        detail.setNumOfDelivery(claimdetailinfo.getACTUALQTY().intValue());                     //实际收货数量
        if(!Strings.isNullOrEmpty(claimdetailinfo.getNUMCHA().toString()))
            detail.setNumOfDifference((int)Double.parseDouble(claimdetailinfo.getNUMCHA().toString()));    //差异数量
        else
            detail.setNumOfDifference(0);
        detail.setDescOfDifference(claimdetailinfo.getREASONS());                               //交货差异原因
        if(!Strings.isNullOrEmpty(claimdetailinfo.getZDATE()))
            detail.setCurrentDay(DateTime.parse(claimdetailinfo.getZDATE()).toDate());              //当前日期
        else
            detail.setCurrentDay(today);
        if(!Strings.isNullOrEmpty(claimdetailinfo.getZTIME()))
            detail.setCurrentTime(TIME_FMT.parseDateTime(claimdetailinfo.getZTIME()).toDate());             //当前时间
        else
            detail.setCurrentTime(today);
        return detail;
    }

    /**
     * 定时处理索赔订单状态job
     * */
    private void changeStatus() {
        log.info("Start change status of compensation");
        Stopwatch watch = Stopwatch.createStarted();
        DateTime time = DateTime.now().minusDays(7).toDateTime();
        try{
            compensationService.autoUpdateStatus(time);
        } catch (Exception e){
            log.error("failed to change status of compensation");
        }
        watch.stop();
        log.info("End change status of compensation, cost time : {} ms", watch.elapsed(TimeUnit.MILLISECONDS));
    }

}
