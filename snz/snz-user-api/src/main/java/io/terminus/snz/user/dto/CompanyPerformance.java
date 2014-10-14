package io.terminus.snz.user.dto;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.terminus.snz.user.model.SupplierModuleCount;
import io.terminus.snz.user.model.SupplierTQRDCInfo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Author:Guo Chaopeng
 * Created on 14-6-25.
 */
public class CompanyPerformance implements Serializable {

    private static final long serialVersionUID = 7825561837507114878L;
    @Getter
    @Setter
    private SupplierTQRDCInfo supplierTQRDCInfo;  //当月的信息


    //=======================================================

    @Getter
    @Setter
    private List<Double> locations;                  //每月的区位值

    @Getter
    @Setter
    private Integer redLocations;                   //年内红区次数

    @Getter
    @Setter
    private List<Integer> modules;                    //模块走势

    @Getter
    @Setter
    private String modulesTrend;                      //模块趋势: 上升, 下降, 波动

    @Getter
    @Setter
    private List<Integer> ranks;                      //模块排序

    //=======================================================

    @Getter
    @Setter
    private List<Integer> T;                          //每月的技术得分

    @Getter
    @Setter
    private String tTrend;                            //每月技术得分趋势

    @Getter
    @Setter
    private List<Integer> Tranks;                     //技术得分排名

    @Getter
    @Setter
    private String tRanksTrend;                       //技术得分排名趋势

    //=======================================================

    @Getter
    @Setter
    private List<Integer> Q;                          //每月的质量得分

    @Getter
    @Setter
    private String qTrend;                            //每月的质量得分趋势

    @Getter
    @Setter
    private List<Integer> Qranks;                    //技术质量排名

    @Getter
    @Setter
    private String qRanksTrend;                      //质量排名趋势

    //=======================================================

    @Getter
    @Setter
    private List<Integer> R;                          //每月的响应得分

    @Getter
    @Setter
    private String rTrend;                            //每月的响应得分趋势

    @Getter
    @Setter
    private List<Integer> Rranks;                     //响应得分排名

    @Getter
    @Setter
    private String rRanksTrend;                       //每月的响应得分趋势


    //=======================================================

    @Getter
    @Setter
    private List<Integer> D;                          //每月的交付得分

    @Getter
    @Setter
    private String dTrend;                            //每月的交付得分趋势

    @Getter
    @Setter
    private List<Integer> Dranks;                     //交付得分排名

    @Getter
    @Setter
    private String dRanksTrend;                       //交付得分排名趋势

    //=======================================================

    @Getter
    @Setter
    private List<Integer> C;                          //每月的成本得分

    @Getter
    @Setter
    private String cTrend;                            //每月的成本得分趋势

    @Getter
    @Setter
    private List<Integer> Cranks;                     //成本得分排名

    @Getter
    @Setter
    private String cRanksTrend;                       //每月的成本得分趋势

    @Getter
    @Setter
    private boolean isEliminated=false;               //该供应商是否被申明淘汰，判断依据是，该供应商在 `snz_supplier_module_details`中

    @Getter
    @Setter
    private SupplierModuleCount moduleCount;         //本模块中供应商数量统计

    /**
     * 根据模块走势modules, 得出模块趋势: 上升, 下降, 波动
     */
    public void modulesTrend() {
        modulesTrend = computeTrend(modules);
    }

    /**
     * 计算TQRDC各项趋势
     */
    public void computeTQRDCTrend() {
        tTrend = computeTrend(T);
        tRanksTrend = computeTrend(Tranks);
        //排名要取反, 数值越小, 越好
        tRanksTrend = notTrend(tRanksTrend);

        qTrend = computeTrend(Q);
        qRanksTrend = computeTrend(Qranks);
        qRanksTrend = notTrend(qRanksTrend);

        rTrend = computeTrend(R);
        rRanksTrend = computeTrend(Rranks);
        rRanksTrend = notTrend(rRanksTrend);

        dTrend = computeTrend(D);
        dRanksTrend = computeTrend(Dranks);
        dRanksTrend = notTrend(dRanksTrend);

        cTrend = computeTrend(C);
        cRanksTrend = computeTrend(Cranks);
        cRanksTrend = notTrend(cRanksTrend);
    }

    /**
     * 趋势取反
     *
     * @param trend 原趋势, 上升->恶化, 恶化->上升
     * @return
     */
    private String notTrend(String trend) {
        if (Objects.equal(Trend.U.display, trend)) {
            return Trend.D.display;
        } else if (Objects.equal(Trend.D.display, trend)) {
            return Trend.U.display;
        }
        return trend;
    }

    private static enum Trend {
        U(1, "上升"),
        D(2, "恶化"),
        S(3, "平稳"),
        V(4, "波动");

        public int val;
        public String display;

        private Trend(int val, String display) {
            this.val = val;
            this.display = display;
        }
    }

    /**
     * 根据List<Integer>计算其趋势: 上升, 恶化, 波动, 平稳
     *
     * @param results
     * @return
     */
    private String computeTrend(List<Integer> results) {
        if (results == null) {
            return Trend.S.display; //平稳
        }

        List<Integer> vals = Lists.newArrayList();
        for (Integer result : results) {
            if (result != null) {
                vals.add(result);
            }
        }
        if (vals.size() >= 2) {
            Set<Integer> sVals = Sets.newHashSet(vals);
            if (sVals.size() == 1) {
                return Trend.S.display; //平稳
            }

            int first = vals.get(0);
            int second = vals.get(1);

            boolean curIsUp = first < second ? true : false;
            boolean beforeIsUp;
            int before = second;
            for (int i = 2; i < vals.size(); i++) {
                if (vals.get(i) == null) break;
                beforeIsUp = curIsUp;
                curIsUp = vals.get(i) > before ? true : false;
                if (beforeIsUp != curIsUp) {
                    return Trend.V.display; //波动
                }
                before = vals.get(i);
            }
            return curIsUp ? Trend.U.display : Trend.D.display; // 上升 : 平稳
        }
        return Trend.S.display; //平稳
    }
}
