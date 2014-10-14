package io.terminus.snz.user.tool;

/**
 * 供应商完善信息中相应表要填的字段数目
 * Author:Guo Chaopeng
 * Created on 14-8-5.
 */
public class SupplierColumnCount {

    public static final int COMPANY_COLUMN_COUNT = 8;            //公司

    public static final int CONTACT_INFO_COLUMN_COUNT = 4;       //联系人

    // public static final int COMPANY_RANK_COLUMN_COUNT = 8;    //公司排名

    public static final int FINANCE_COLUMN_COUNT = 7;             //财务

    public static final int EXTRA_RD_COLUMN_COUNT = 13;           //研发

    public static final int EXTRA_QUALITY_COLUMN_COUNT = 16;      //质量

    public static final int EXTRA_DELIVERY_COLUMN_COUNT = 4;      //交货

    public static final int COLUMN_TOTAL =
            COMPANY_COLUMN_COUNT +
                    CONTACT_INFO_COLUMN_COUNT +
                    // COMPANY_RANK_COLUMN_COUNT +
                    FINANCE_COLUMN_COUNT +
                    EXTRA_RD_COLUMN_COUNT +
                    EXTRA_QUALITY_COLUMN_COUNT +
                    EXTRA_DELIVERY_COLUMN_COUNT +
                    2;               //合计(加上主营业务和供货园区)

}
