package io.terminus.snz.user.tool;

/**
 * Author:Guo Chaopeng
 * Created on 14-8-26.
 */
public class StashSupplierInfoKeys {

    public static String company(Long userId) {
        return "stash-company-user:" + userId;
    }

    public static String paperwork(Long userId) {
        return "stash-paperwork-user:" + userId;
    }

    public static String contactInfo(Long userId) {
        return "stash-contact-info-user:" + userId;
    }

    public static String companyRank(Long userId) {
        return "stash-company-rank-user:" + userId;
    }

    public static String finance(Long userId) {
        return "stash-finance-user:" + userId;
    }

    public static String RD(Long userId) {
        return "stash-rd-user:" + userId;
    }

    public static String quality(Long userId) {
        return "stash-quality-user:" + userId;
    }

    public static String response(Long userId) {
        return "stash-response-user:" + userId;
    }

    public static String delivery(Long userId) {
        return "stash-delivery-user:" + userId;
    }

}
