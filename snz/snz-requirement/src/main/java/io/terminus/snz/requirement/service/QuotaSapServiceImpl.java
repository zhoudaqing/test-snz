package io.terminus.snz.requirement.service;

import com.google.common.base.Objects;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import io.terminus.common.model.PageInfo;
import io.terminus.common.model.Paging;
import io.terminus.common.utils.JsonMapper;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.dao.DetailQuotaDao;
import io.terminus.snz.requirement.dao.RequirementSendDao;
import io.terminus.snz.requirement.model.DetailQuota;
import io.terminus.snz.requirement.model.RequirementSend;
import lombok.extern.slf4j.Slf4j;
import org.example.transpricegoodsinfotohgvs.OutType;
import org.example.transpricegoodsinfotohgvs.TransPriceGoodsInfoToHGVS;
import org.example.transpricegoodsinfotohgvs.TransPriceGoodsInfoToHGVSService;
import org.example.transpricegoodsinfotohgvs.ZDWHJHPT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * Desc:配额信息进sap的管理
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-25.
 */
@Slf4j
@Service
public class QuotaSapServiceImpl implements QuotaSapService {
    private static Date date = new Date();

    private static int addNum = 0;

    //999999个数据足够满足在1s内的同步数据唯一
    private static final int MAX_NUM = 999999;

    private static final int MAX_SET = 500; //最大写入条数

    private static final JsonMapper JSON_MAPPER = JsonMapper.JSON_NON_DEFAULT_MAPPER;

    //数据类型及对应验证规则
    private static final Map<String , String> dataMaps = new HashMap<String, String>(){
        private static final long serialVersionUID = 1395819460915706261L;
        {
            put("050910" , "01020304050615293738");
            put("0509" , "010203040615293738");
            put("07" , "01030407152425273738");
            put("10" , "010203");
        }
    };

    //sap系统对接服务
    private static final TransPriceGoodsInfoToHGVS sapService = new TransPriceGoodsInfoToHGVSService().getTransPriceGoodsInfoToHGVSSOAP();

    @Autowired
    private DetailQuotaDao detailQuotaDao;

    @Autowired
    private RequirementSendDao requirementSendDao;

    @Override
    public Response<Boolean> updateQuota(DetailQuota detailQuota) {
        Response<Boolean> result = new Response<Boolean>();

        if(detailQuota.getId() == null){
            log.error("update detail quota info need detailQuota id");
            result.setError("quota.id.null");
            return result;
        }

        try{
            detailQuota.setStatus(DetailQuota.Status.SUBMIT.value());
            detailQuotaDao.update(detailQuota);
            result.setResult(true);
        }catch(Exception e){
            log.error("update detail quota filed, error code={}", Throwables.getStackTraceAsString(e));
            result.setError("quota.update.failed");
        }

        return result;
    }

    @Override
    public Response<Boolean> updateQuotaBatch(String detailQuotas) {
        Response<Boolean> result = new Response<Boolean>();

        try{
            //获取详细的配额信息列表
            List<DetailQuota> quotaList = JSON_MAPPER.fromJson(detailQuotas , JSON_MAPPER.createCollectionType(List.class , DetailQuota.class));

            //批量更新详细配额数据
            for(DetailQuota detailQuota : quotaList){
                detailQuota.setStatus(DetailQuota.Status.SUBMIT.value());
                detailQuotaDao.update(detailQuota);
            }
            result.setResult(true);
        }catch(Exception e){
            log.error("update detail quota filed, error code={}", Throwables.getStackTraceAsString(e));
            result.setError("quota.update.failed");
        }

        return result;
    }

    @Override
    public Response<Paging<DetailQuota>> findQuotaWithSap(Long requirementId, Integer status, Integer pageNo, Integer size) {
        Response<Paging<DetailQuota>> result = new Response<Paging<DetailQuota>>();

        if(requirementId == null){
            log.error("find detail quota info need requirement id.");
            result.setError("requirement.id.null");
            return result;
        }

        try{
            Map<String , Object> params = Maps.newHashMap();
            //默认显示10条数据
            PageInfo pageInfo = new PageInfo(pageNo , Objects.firstNonNull(size , 10));
            params.putAll(pageInfo.toMap());
            params.put("status", status);

            result.setResult(detailQuotaDao.findByParams(requirementId , params));
        }catch(Exception e){
            log.error("find detail quota info failed, requirementId={}, error code={}", requirementId, Throwables.getStackTraceAsString(e));
            result.setError("quota.fina.failed");
        }

        return result;
    }

    @Override
    public Response<Boolean> setQuotaInfoToSAP(Long requirementId) {
        Response<Boolean> result = new Response<Boolean>();

        if(requirementId == null){
            log.error("set detail quota info to sap system need requirement id.");
            result.setError("requirement.id.null");
            return result;
        }

        try{
            Integer pageNo = 1;
            Map<String , Object> params = Maps.newHashMap();
            //查询未写的数据
            PageInfo pageInfo = new PageInfo(pageNo , MAX_SET);
            params.putAll(pageInfo.toMap());
            params.put("status" , DetailQuota.Status.WAIT_SUBMIT.value());

            Paging<DetailQuota> noSubmit = detailQuotaDao.findByParams(requirementId , params);
            if(noSubmit.getTotal() > 0){
                //还未填写完整详细的数据
                log.error("need set detail info for quota.");
                result.setError("quota.detail.failed");
            }else{
                //写入数据到sap(防止一次传输的数据量太大导致链接超时的情况，使用每次传输500条数据)
                params.putAll(pageInfo.toMap((pageNo++).toString() , MAX_SET+""));
                params.put("status" , DetailQuota.Status.SUBMIT.value());

                Paging<DetailQuota> firstPaging = detailQuotaDao.findByParams(requirementId , params);
                sendToSap(firstPaging);
                //获取所有的分页数量
                int allPage = (int)(firstPaging.getTotal()%MAX_SET == 0 ? firstPaging.getTotal()/MAX_SET : firstPaging.getTotal()/MAX_SET+1);

                Paging<DetailQuota> sendPaging;
                while(pageNo <= allPage){
                    params.putAll(pageInfo.toMap((pageNo++).toString() , MAX_SET+""));
                    sendPaging = detailQuotaDao.findByParams(requirementId , params);
                    //写入数据到sap系统
                    sendToSap(sendPaging);
                }

                //标记需求的配额数据已发送到sap
                RequirementSend requirementSend = new RequirementSend();
                requirementSend.setRequirementId(requirementId);
                requirementSend.setSendSAP(RequirementSend.Type.COMMIT.value());
                requirementSendDao.update(requirementSend);

                result.setResult(true);
            }
        }catch(Exception e){
            log.error("set detail quota info to sap system failed, requirementId={}, error code={}", requirementId, Throwables.getStackTraceAsString(e));
            result.setError("quota.setSap.failed");
        }

        return result;
    }

    /**
     * 向sap系统发送详细的配额信息内容
     * @param sendPaging    配额信息
     * @return  Boolean
     * 返回配额信息发送是否成功
     */
    private Boolean sendToSap(Paging<DetailQuota> sendPaging){
        Boolean result = false;

        try{
            List<ZDWHJHPT> sapList = new ArrayList<ZDWHJHPT>();
            ZDWHJHPT data;

            for(DetailQuota detailQuota : sendPaging.getData()){
                data = new ZDWHJHPT();

                data.setIDNO(uniqueCode());			                                //数据ID: 可按规则生成: 如日期+id
                data.setMATNR(detailQuota.getModuleNum());			                //物料号
                data.setWERKS(detailQuota.getFactoryNum());					        //工厂
                data.setLIFNR(detailQuota.getSupplierVCode());				        //供应商账号
                data.setEKORG(detailQuota.getPurchaseOrg());					    //采购组织
                data.setDATAT(detailQuota.getDataType()); 				            //数据类型
                data.setCHECKR(dataMaps.get(detailQuota.getDataType()));            //验证规则
                data.setKPEIN(new BigDecimal(detailQuota.getFeeUnit()));		    //采购价格单位
                data.setKMEIN(detailQuota.getPurchaseUnit());					    //采购单位
                data.setZOA1(new BigDecimal(detailQuota.getDuty()/100));		    //关税
                data.setAGENTFEE(new BigDecimal(detailQuota.getAgencyFee()/100));   //代理费
                data.setWAERS(detailQuota.getCoinType());					        //采购货币
                data.setEKGRP(detailQuota.getPurchaseTeam());					    //采购组
                data.setMEINS(detailQuota.getBasicUnit());					        //基本计量单位
                data.setTAX(detailQuota.getTaxCode());						        //税码
                data.setNETPR(new BigDecimal(detailQuota.getActualCost()/100));	    //净价
                data.setQUOTE(new BigDecimal(detailQuota.getQuantity()));	        //配额
                data.setAPLFZ(new BigDecimal(detailQuota.getPurchaseDay()));		//采购期
                data.setZAF1(new BigDecimal(detailQuota.getOtherFee()/100));		//其他费用
                data.setTYPE(detailQuota.getPurchaseType());						//采购类型

                sapList.add(data);
            }

            //将数据抛送到sap
            OutType outType = sapService.transPriceGoodsInfoToHGVS(sapList);
            //数据是否发送成功
            result = outType.getUNDO() == 0;

            //写入成功发送状态到详细配额列表
            if(result){
                DetailQuota updateQuota;
                for(DetailQuota detailQuota : sendPaging.getData()){
                    updateQuota = new DetailQuota();
                    updateQuota.setId(detailQuota.getId());
                    updateQuota.setStatus(DetailQuota.Status.SEND_SAP.value());

                    detailQuotaDao.update(detailQuota);
                }
            }
        }catch(Exception e){
            log.error("send detail quota info to sap failed, error code={}", Throwables.getStackTraceAsString(e));
        }

        return result;
    }

    /**
     * 获取唯一的需求系统流水号(使用同步保证在并发情况下的数据的唯一性)
     * @return String
     * 返回一个唯一的系统流水编号
     */
    private static synchronized String uniqueCode(){
        //获取增量数据
        addNum = addNum > MAX_NUM ? 0 : addNum;

        date.setTime(System.currentTimeMillis());

        return String.format("%1$tY%1$tm%1$td%1$tk%1$tM%1$tS%2$06d", date, addNum++);
    }
}
