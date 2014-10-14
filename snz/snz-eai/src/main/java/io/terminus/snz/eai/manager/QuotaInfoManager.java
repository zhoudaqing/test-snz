package io.terminus.snz.eai.manager;

import com.google.common.collect.Lists;
import com.haier.GetQuotaInfoFromGVSToEBS.ZSTRPRICEINFOType;
import com.haier.GetQuotaInfoFromGVSToEBS.ZSTRQUOINFOType;
import io.terminus.snz.eai.dao.QuotaInfoDao;
import io.terminus.snz.eai.model.OutPriceInfo;
import io.terminus.snz.eai.model.OutQuotaInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Desc: 衍生品接配额信息
 * author: luyzh
 * Date: 14-9-4
 */
@Slf4j
@Component
public class QuotaInfoManager {
    @Autowired
    private QuotaInfoDao quotaInfoDao;

    @Transactional
    public void batchDerivativeQuota(List<ZSTRQUOINFOType> outquoData ,List<ZSTRPRICEINFOType>  outpriceData){
        log.info("batchDerivativeQuota start");
        BigDecimal b=new BigDecimal("100");
        //配额信息的处理
        List<OutQuotaInfo> tempQuota = Lists.newArrayList();
        for(ZSTRQUOINFOType tempquota : outquoData){
            OutQuotaInfo quotaInfo = new OutQuotaInfo();
            quotaInfo.setModulenum(tempquota.getMATNR());
            quotaInfo.setModulename(tempquota.getMAKTX());
            quotaInfo.setFactorynum(tempquota.getWERKS());
            quotaInfo.setMatkl(tempquota.getMATKL());
            quotaInfo.setSupplierid(tempquota.getLIFNR());
            quotaInfo.setSuppliername(tempquota.getNAME1());
            quotaInfo.setQuantity(tempquota.getQUOTE().longValue());
            tempQuota.add(quotaInfo);
        }
        //遍历数组
        for(OutQuotaInfo quoData: tempQuota) {
            //物料，工厂，供应商确认一条配额数据
            OutQuotaInfo quotatemp = quotaInfoDao.findbymatnrall(quoData.getModulenum(),quoData.getFactorynum(),quoData.getSupplierid());
            if(quotatemp==null){
                quotaInfoDao.create(quoData);
            }else{
                quotatemp.setModulename(quoData.getModulename());
                quotatemp.setMatkl(quoData.getMatkl());
                quotatemp.setSuppliername(quoData.getSuppliername());
                quotatemp.setQuantity(quoData.getQuantity());
                quotaInfoDao.update(quotatemp);
            }
        }

        //处理价格信息
        List<OutPriceInfo> tempPrice = Lists.newArrayList();
        for(ZSTRPRICEINFOType tempprice : outpriceData){
            OutPriceInfo priceInfo = new OutPriceInfo();
            priceInfo.setModulenum(tempprice.getMATNR());
            priceInfo.setModulename(tempprice.getMAKTX());
            priceInfo.setMatkl(tempprice.getMATKL());
            priceInfo.setSupplierid(tempprice.getLIFNR());
            priceInfo.setSuppliername(tempprice.getNAME1());
            priceInfo.setPurchaseorg(tempprice.getEKORG());
            priceInfo.setPurchasetype(tempprice.getESOKZ());
            //价格拿到元为单位，需转换为分单位
            priceInfo.setScale(tempprice.getKBETR().multiply(b).setScale(2, BigDecimal.ROUND_HALF_EVEN).longValue());
            priceInfo.setFeeunit(tempprice.getKPEIN().longValue());
            priceInfo.setPurchaseunit(tempprice.getKMEIN());
            priceInfo.setCointype(tempprice.getKONWA());
            tempPrice.add(priceInfo);
        }
        //遍历数组－新增还是修改
        for(OutPriceInfo priData: tempPrice) {
            OutPriceInfo pricetemp = quotaInfoDao.findbymatnr(priData.getModulenum(),priData.getSupplierid());
            if(pricetemp==null){
                quotaInfoDao.createother(priData);
            }else{
                pricetemp.setModulename(priData.getModulename());
                pricetemp.setMatkl(priData.getMatkl());
                pricetemp.setSuppliername(priData.getSuppliername());
                pricetemp.setPurchaseorg(priData.getPurchaseorg());
                pricetemp.setPurchasetype(priData.getPurchasetype());
                pricetemp.setScale(priData.getScale());
                pricetemp.setFeeunit(priData.getFeeunit());
                pricetemp.setPurchaseunit(priData.getPurchaseunit());
                pricetemp.setCointype(priData.getCointype());
                quotaInfoDao.updateother(pricetemp);
            }
        }
        log.info("batchDerivativeQuota end");
    }
}
