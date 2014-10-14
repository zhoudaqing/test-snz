package io.terminus.snz.requirement.tool;

import io.terminus.snz.requirement.model.Tactics;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Desc:创建唯一的系统对应编号
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-08-29.
 */
public class FlowNumber {
    private static Date date = new Date();

    private static int addNum = 0;

    //99999个数据足够满足在1s内的同步数据唯一
    private static final int MAX_NUM = 99999;

    //需求的模块策略
    private static final Map<String , String> tacticsMap = new HashMap<String, String>(){

        private static final long serialVersionUID = -5515344457039196733L;

        {
            this.put("卓越运营" , "ZY");
            this.put("差异化" , "CY");
            this.put("技术领先" , "JS");
        }
    };

    /**
     * 创建需求的唯一流水编号
     * @param productName   生产线名称
     * @param materielName
     * @param tacticsId
     * @return
     */
    public static String createReqFlowNum(String productName , String materielName, Integer tacticsId){
        String flowNum;

        String name1 = ChineseTransform.getFirstLetter(productName);
        String name2 = ChineseTransform.getFirstLetter(materielName);
        String name3 = tacticsMap.get(Tactics.from(tacticsId).toString());

        //唯一的需求流水编号
        //H-KT-LJ-ZY-201408181628400001
        flowNum = "H-"+name1+"-"+name2+"-"+name3+"-"+uniqueCode();

        return flowNum;
    }

    /**
     * 创建模块的唯一流水编号
     * @param productName   生产线名称（二级类目名称）
     * @param materielName  物料类别（一级类目名称）
     * @return  String
     * 返回模块的流水编号
     */
    public static String createModuleFlowNum(String productName , String materielName){
        String flowNum;

        String name1 = ChineseTransform.getFirstLetter(productName);
        String name2 = ChineseTransform.getFirstLetter(materielName);

        //唯一的模块流水编号
        //H-KT-LJ-2014082916284000001
        flowNum = "H-"+name1+"-"+name2+"-"+uniqueCode();

        return flowNum;
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

        return String.format("%1$tY%1$tm%1$td%1$tk%1$tM%1$tS%2$05d", date, addNum++);
    }
}
