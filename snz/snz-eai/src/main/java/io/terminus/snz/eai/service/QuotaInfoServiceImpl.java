package io.terminus.snz.eai.service;

import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.haier.GetQuotaInfoFromGVSToEBS.*;
import io.terminus.pampas.common.Response;
import io.terminus.snz.eai.dao.QuotaInfoDao;
import io.terminus.snz.eai.dto.OutmoduleDto;
import io.terminus.snz.eai.manager.QuotaInfoManager;
import io.terminus.snz.eai.model.OutPriceInfo;
import io.terminus.snz.eai.model.OutQuotaInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.ws.Holder;
import java.util.ArrayList;
import java.util.List;

/**
 * Date: 8/29/14
 * Time: 11:38
 * Author: 2014年 luyzh
 */
@Slf4j
@Service()
public class QuotaInfoServiceImpl implements QuotaInfoService {

    @Autowired
    private QuotaInfoManager quotaInfoManager;

    @Autowired
    private QuotaInfoDao quotaInfoDao;

    @Override
    public Response<Boolean> applyQuotaInfo(String matnr,String werks) {
        Response<Boolean> result = new Response<Boolean>();
        //传入参数不能都为空
        if(Strings.isNullOrEmpty(matnr)&&Strings.isNullOrEmpty(werks)){
            log.error("Can't this WERKS is null and this MATNR is null");
            result.setError("derivative.all.null");
            return result;
        }
        //传入参数 不允许工厂有值，物料号没值
        if(Strings.isNullOrEmpty(matnr)&&!Strings.isNullOrEmpty(werks)){
            log.error("Can't this WERKS is not null and this MATNR is null");
            result.setError("derivative.merkormatnr.null");
            return result;
        }

        try{
            GetQuotaInfoFromGVSToJHPT_Service service = new GetQuotaInfoFromGVSToJHPT_Service();
            GetQuotaInfoFromGVSToJHPT soap =  service.getGetQuotaInfoFromGVSToJHPTSOAP();

            List<InputsType> ins = new ArrayList<InputsType>();
            InputsType inparam = new InputsType();
            inparam.setMATNR(matnr);
            inparam.setWERKS(werks);
            ins.add(inparam);

            Holder<List<ZSTRQUOINFOType>> outquoData= new Holder<List<ZSTRQUOINFOType>>();
            Holder<List<ZSTRPRICEINFOType>> outpriceData = new Holder<List<ZSTRPRICEINFOType>>();
            soap.getQuotaInfoFromGVSToJHPTOP(ins,outquoData,outpriceData);

            //业务处理
            quotaInfoManager.batchDerivativeQuota(outquoData.value, outpriceData.value);

            result.setResult(true);
        }catch (Exception e){
            log.error("failed to get quotainfos from sap, cause: {}",
                    Throwables.getStackTraceAsString(e));
            result.setError("quotainfos.get.fail");
        }

        return result;
    }


    @Override
    public OutmoduleDto findListfrom(String modulenum){
        OutmoduleDto outmoduleDto = new OutmoduleDto();
        List<OutQuotaInfo> outQuotaInfos = quotaInfoDao.findbymodulenum(modulenum);
        List<OutPriceInfo> outPriceInfos = quotaInfoDao.findbymodulenumother(modulenum);

        outmoduleDto.setModulnum(modulenum);
        outmoduleDto.setOutQuotaInfo(outQuotaInfos);
        outmoduleDto.setOutPriceInfo(outPriceInfos);
        return  outmoduleDto;
    }
}
