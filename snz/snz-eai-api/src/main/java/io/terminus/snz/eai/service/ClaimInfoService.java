package io.terminus.snz.eai.service;

import io.terminus.pampas.common.Response;
import org.example.receiveclaiminfofromgvs.ZSPJHINPUT2;
import org.example.sendclaiminfotogvs.ZSPJHOUTPUT;

import java.util.List;

/**
 * 通过EAI操作SAP索赔信息
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-8-8
 */
public interface ClaimInfoService {

    /**
     * 获取索赔信息列表, 按天或按月查询
     * @param day 按天查询 yyyy-MM-dd
     * @param month 按月查询 yyyy-MM-dd
     * @return 获取信息列表对象
     */
    Response<List<ZSPJHOUTPUT>> getClaimInfos(String day, String month);

    /**
     * 写入索赔信息(采购商审核后)
     * @param zspjhinput2s 索赔信息列表对象
     * ZSPJHINPUT2对象类似为:
     *     inputParam.setLIFNR("V98507");       //供应商或债权人账号
           inputParam.setWERKS("9220");         //工厂
           inputParam.setMATNR("0010807698");   //物料号
           inputParam.setZRETSTA("申诉失败");    //申诉结果
           inputParam.setZDATE("2014-06-26");   //凭证入账日期
     * @return 写入成功返回true, 反之false
     */
    Response<Boolean> setCompensations(List<ZSPJHINPUT2> zspjhinput2s);
}
