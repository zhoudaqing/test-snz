package io.terminus.snz.user.tool;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Author:Guo Chaopeng
 * Created on 14-6-16.
 */
public class CountryCertificate {

    /**
     * 无需三证
     */
    public static final int NO_CERTIFICATE = 0;

    /**
     * 只需要营业执照
     */
    public static final int ONLY_NEED_BUSINESS_LICENSE = 1;

    /**
     * 只需要营业执照和税务证号
     */
    public static final int ONLY_NEED_BUSINESS_LICENSE_AND_TAX_NO = 2;

    /**
     * 三证齐全
     */
    public static final int NEED_THREE_CERTIFICATES = 3;

    /**
     * 无需三证的国家id
     */
    private static final List<Integer> NON_CERTIFICATE_COUNTRY_IDS = Lists.newArrayList(15, 16);

    /**
     * 只需要营业执照的国家id
     */
    private static final List<Integer> ONLY_NEED_BUSINESS_LICENSE_COUNTRY_IDS = Lists.newArrayList(6, 10, 11, 14);

    /**
     * 只需要营业执照和税务证号的国家id
     */
    private static final List<Integer> ONLY_NEED_BUSINESS_LICENSE_AND_TAX_NO_COUNTRY_IDS = Lists.newArrayList(2, 12);

    /**
     * 根据国家id检查入驻需要哪些证件
     *
     * @param countryId 国家id
     * @return 需要的证件
     */
    public static int check(Integer countryId) {
        if (NON_CERTIFICATE_COUNTRY_IDS.contains(countryId)) {
            return NO_CERTIFICATE;
        }
        if (ONLY_NEED_BUSINESS_LICENSE_COUNTRY_IDS.contains(countryId)) {
            return ONLY_NEED_BUSINESS_LICENSE;
        }
        if (ONLY_NEED_BUSINESS_LICENSE_AND_TAX_NO_COUNTRY_IDS.contains(countryId)) {
            return ONLY_NEED_BUSINESS_LICENSE_AND_TAX_NO;
        }

        return NEED_THREE_CERTIFICATES;
    }

}
