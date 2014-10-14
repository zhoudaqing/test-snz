package io.terminus.snz.user.tool;

/**
 * Author:Guo Chaopeng
 * Created on 14-8-15.
 */
public class ChangedInfoKeys {

    public static String changedInfos(Long userId) {
        return "supplier-changedInfos-user:" + userId;
    }

    //================tab key=====================================

    public static String changedTabs(Long userId) {
        return "changed-tabs-user:" + userId;
    }

    public static String companyTab() {
        return "company-tab";
    }

    public static String paperworkTab() {
        return "paperwork-tab";
    }

    public static String contactInfoTab() {
        return "contact-info-tab";
    }

    public static String qualityTab() {
        return "quality-tab";
    }

    public static String financeTab() {
        return "finance-tab";
    }

    //================company========================================

    public static String companyCorporation() {
        return "company-corporation";
    }

    public static String companyProductLine() {
        return "company-product-line";
    }

    public static String companyGroupName() {
        return "company-group-name";
    }

    public static String companyGroupAddr() {
        return "company-group-addr";
    }

    public static String companyRegCapical() {
        return "company-reg-capital";
    }

    public static String companyRcCoinType() {
        return "company-rc-coin-type";
    }

    public static String companyPersonScale() {
        return "company-person-scale";
    }

    public static String companyRegProvince() {
        return "company-reg-province";
    }

    public static String companyRegCity() {
        return "company-reg-city";
    }

    public static String companyFixedAssets() {
        return "company-fixed-assets";
    }

    public static String companyFaCoinType() {
        return "company-fa-coin-type";
    }

    public static String companyFoundAt() {
        return "company-found-at";
    }

    public static String companyOfficialWebSite() {
        return "company-official-web-site";
    }

    public static String companyNature() {
        return "company-nature";
    }

    public static String companyWorldTop() {
        return "company-world-top";
    }

    public static String companyListedStatus() {
        return "company-listed-status";
    }

    public static String companyListedRegion() {
        return "company-listed-region";
    }

    public static String companyTicker() {
        return "company-ticker";
    }

    public static String companyInitAgent() {
        return "company-init-agent";
    }

    public static String companyDesc() {
        return "company-desc";
    }

    public static String companyRegCountry() {
        return "company-reg-country";
    }

    public static String companyMainBusiness() {
        return "company-main-business";
    }

    public static String companySupplyPark() {
        return "company-supply-park";
    }

    //================paperwork========================================

    public static String companyBusinessLicense() {
        return "company-business-license";
    }

    public static String companyBusinessLicenseId() {
        return "company-business-license-id";
    }

    public static String companyBusinessLicenseDate() {
        return "company-business-license-date";
    }

    public static String companyTaxNo() {
        return "company-tax-no";
    }

    public static String companyTaxNoId() {
        return "company-tax-no-id";
    }

    public static String companyTaxNoDate() {
        return "company-tax-no-date";
    }

    public static String companyOrgCert() {
        return "company-org-cert";
    }

    public static String companyOrgCertId() {
        return "company-org-cert-id";
    }

    public static String companyOrgCertDate() {
        return "company-org-cert-date";
    }

    //================contact info========================================

    public static String contactInfoName() {
        return "contact-info-name";
    }

    public static String contactInfoMobile() {
        return "contact-info-mobile";
    }

    public static String contactInfoPhone() {
        return "contact-info-phone";
    }

    public static String contactInfoEmail() {
        return "contact-info-email";
    }

    //================quality========================================

    public static String qualityRohsId() {
        return "quality-rohs-id";
    }

    public static String qualityRohsValidDate() {
        return "quality-rohs-valid-date";
    }

    public static String qualityRohsAttachUrl() {
        return "quality-rohs-attach-url";
    }

    public static String qualityISO9001Id() {
        return "quality-iso09001-id";
    }

    public static String qualityISO9001ValidDate() {
        return "quality-iso9001-valid-date";
    }

    public static String qualityISO9001AttachUrl() {
        return "quality-iso9001-attach-url";
    }

    public static String qualityISO14001Id() {
        return "quality-iso14001-id";
    }

    public static String qualityISO14001ValidDate() {
        return "quality-iso14001-valid-date";
    }

    public static String qualityISO14001AttachUrl() {
        return "quality-iso14001-attach-url";
    }

    public static String qualityTS16949Id() {
        return "quality-ts16949-id";
    }

    public static String qualityTS16949ValidDate() {
        return "quality-ts16949-valid-date";
    }

    public static String qualityTS16949AttachUrl() {
        return "quality-ts16949-attach-url";
    }

    //================finance========================================

    public static String financeCountry() {
        return "finance-country";
    }

    public static String financeOpeningBank() {
        return "finance-opening-bank";
    }

    public static String financeOpenLicense() {
        return "finance-open-license";
    }

    public static String financeBankCode() {
        return "finance-bank-code";
    }

    public static String financeBankAccount() {
        return "finance-bank-account";
    }

    public static String financeCoinType() {
        return "finance-coin-type";
    }

    public static String financeRecentFinance() {
        return "finance-recent-finance";
    }

}
