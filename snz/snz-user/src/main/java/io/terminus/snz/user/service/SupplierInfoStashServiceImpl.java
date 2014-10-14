package io.terminus.snz.user.service;

import com.google.common.base.Throwables;
import io.terminus.common.utils.JsonMapper;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.dao.redis.*;
import io.terminus.snz.user.dto.CompanyDto;
import io.terminus.snz.user.dto.CompanyExtraQualityDto;
import io.terminus.snz.user.dto.FinanceDto;
import io.terminus.snz.user.dto.PaperworkDto;
import io.terminus.snz.user.model.*;
import io.terminus.snz.user.tool.StashSupplierInfoKeys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Author:Guo Chaopeng
 * Created on 14-8-26.
 */
@Service
@Slf4j
public class SupplierInfoStashServiceImpl implements SupplierInfoStashService {

    @Autowired
    private CompanyRedisDao companyRedisDao;

    @Autowired
    private PaperworkRedisDao paperworkRedisDao;

    @Autowired
    private ContactInfoRedisDao contactInfoRedisDao;

    @Autowired
    private FinanceRedisDao financeRedisDao;

    @Autowired
    private CompanyRankRedisDao companyRankRedisDao;

    @Autowired
    private CompanyExtraRDRedisDao companyExtraRDRedisDao;

    @Autowired
    private CompanyExtraQualityRedisDao companyExtraQualityRedisDao;

    @Autowired
    private CompanyExtraDeliveryRedisDao companyExtraDeliveryRedisDao;

    @Override
    public Response<Boolean> stashCompany(BaseUser baseUser, CompanyDto companyDto) {
        Response<Boolean> result = new Response<Boolean>();

        try {
            companyRedisDao.saveBaseCompanyInfo(baseUser.getId(), companyDto);
            result.setResult(Boolean.TRUE);
        } catch (Exception e) {
            log.error("fail to stash company where user id={},cause:{}", baseUser.getId(), Throwables.getStackTraceAsString(e));
            result.setError("stash.company.fail");
        }
        return result;
    }

    @Override
    public Response<Boolean> stashPaperwork(BaseUser baseUser, PaperworkDto paperworkDto) {
        Response<Boolean> result = new Response<Boolean>();

        try {
            paperworkRedisDao.save(StashSupplierInfoKeys.paperwork(baseUser.getId()), paperworkDto);
            result.setResult(Boolean.TRUE);
        } catch (Exception e) {
            log.error("fail to stash paperwork where user id={},cause:{}", baseUser.getId(), Throwables.getStackTraceAsString(e));
            result.setError("stash.paperwork.fail");
        }
        return result;
    }

    @Override
    public Response<Boolean> stashContactInfo(BaseUser baseUser, ContactInfo contactInfo) {
        Response<Boolean> result = new Response<Boolean>();

        try {
            contactInfoRedisDao.save(StashSupplierInfoKeys.contactInfo(baseUser.getId()), contactInfo);
            result.setResult(Boolean.TRUE);
        } catch (Exception e) {
            log.error("fail to stash contact info where user id={},cause:{}", baseUser.getId(), Throwables.getStackTraceAsString(e));
            result.setError("stash.contact.info.fail");
        }
        return result;
    }

    @Override
    public Response<Boolean> stashCompanyRank(BaseUser baseUser, CompanyRank companyRank) {
        Response<Boolean> result = new Response<Boolean>();

        try {
            companyRankRedisDao.save(StashSupplierInfoKeys.companyRank(baseUser.getId()), companyRank);
            result.setResult(Boolean.TRUE);
        } catch (Exception e) {
            log.error("fail to stash company rank where user id={},cause:{}", baseUser.getId(), Throwables.getStackTraceAsString(e));
            result.setError("stash.company.rank.info.fail");
        }
        return result;
    }

    @Override
    public Response<Boolean> stashFinance(BaseUser baseUser, FinanceDto financeDto) {
        Response<Boolean> result = new Response<Boolean>();

        try {
            financeRedisDao.save(StashSupplierInfoKeys.finance(baseUser.getId()), financeDto.getFinance());
            result.setResult(Boolean.TRUE);
        } catch (Exception e) {
            log.error("fail to stash finance where user id={},cause:{}", baseUser.getId(), Throwables.getStackTraceAsString(e));
            result.setError("stash.finance.fail");
        }
        return result;
    }

    @Override
    public Response<Boolean> stashRD(BaseUser baseUser, CompanyExtraRD companyExtraRD) {
        Response<Boolean> result = new Response<Boolean>();

        try {
            companyExtraRDRedisDao.save(StashSupplierInfoKeys.RD(baseUser.getId()), companyExtraRD);
            result.setResult(Boolean.TRUE);
        } catch (Exception e) {
            log.error("fail to stash research and develop where user id={},cause:{}", baseUser.getId(), Throwables.getStackTraceAsString(e));
            result.setError("stash.rd.fail");
        }
        return result;
    }

    @Override
    public Response<Boolean> stashQuality(BaseUser baseUser, CompanyExtraQualityDto companyExtraQualityDto) {
        Response<Boolean> result = new Response<Boolean>();

        try {
            CompanyExtraQuality companyExtraQuality = companyExtraQualityDto.getCompanyExtraQuality();
            companyExtraQuality.setCtqCpk(JsonMapper.nonEmptyMapper().toJson(companyExtraQualityDto.getCtqCpkList()));
            companyExtraQualityRedisDao.save(StashSupplierInfoKeys.quality(baseUser.getId()), companyExtraQuality);
            result.setResult(Boolean.TRUE);
        } catch (Exception e) {
            log.error("fail to stash quality where user id={},cause:{}", baseUser.getId(), Throwables.getStackTraceAsString(e));
            result.setError("stash.quality.fail");
        }
        return result;
    }

    @Override
    public Response<Boolean> stashResponse(BaseUser baseUser, CompanyExtraDelivery companyExtraDelivery) {
        Response<Boolean> result = new Response<Boolean>();

        try {
            companyExtraDeliveryRedisDao.save(StashSupplierInfoKeys.delivery(baseUser.getId()), companyExtraDelivery);
            result.setResult(Boolean.TRUE);
        } catch (Exception e) {
            log.error("fail to stash response where user id={},cause:{}", baseUser.getId(), Throwables.getStackTraceAsString(e));
            result.setError("stash.response.fail");
        }
        return result;
    }
}
