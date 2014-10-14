package io.terminus.snz.user.service;

import com.fasterxml.jackson.databind.JavaType;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import io.terminus.common.utils.JsonMapper;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.dao.*;
import io.terminus.snz.user.dao.redis.CompanyExtraDeliveryRedisDao;
import io.terminus.snz.user.dao.redis.CompanyExtraQualityRedisDao;
import io.terminus.snz.user.dao.redis.CompanyExtraRDRedisDao;
import io.terminus.snz.user.dao.redis.CompanyExtraResponseRedisDao;
import io.terminus.snz.user.dto.CompanyExtraQualityDto;
import io.terminus.snz.user.dto.CtqCpkDto;
import io.terminus.snz.user.dto.SupplierUpdatedInfoDto;
import io.terminus.snz.user.manager.AccountManager;
import io.terminus.snz.user.model.*;
import io.terminus.snz.user.tool.StashSupplierInfoKeys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 6/23/14
 */
@Slf4j
@Service
public class CompanyExtraServiceImpl implements CompanyExtraService {

    @Autowired
    private CompanyExtraRDDao companyExtraRDDao;

    @Autowired
    private CompanyExtraQualityDao companyExtraQualityDao;

    @Autowired
    private CompanyExtraResponseDao companyExtraResponseDao;

    @Autowired
    private CompanyExtraDeliveryDao companyExtraDeliveryDao;

    @Autowired
    private CompanyExtraScaleAndCostDao companyExtraScaleAndCostDao;

    @Autowired
    private AccountManager accountManager;

    @Autowired
    private SupplierInfoChangedService supplierInfoChangedService;

    @Autowired
    private CompanyExtraQualityRedisDao companyExtraQualityRedisDao;

    @Autowired
    private CompanyExtraRDRedisDao companyExtraRDRedisDao;

    @Autowired
    private CompanyExtraResponseRedisDao companyExtraResponseRedisDao;

    @Autowired
    private CompanyExtraDeliveryRedisDao companyExtraDeliveryRedisDao;

    public static final JsonMapper JSON_MAPPER = JsonMapper.nonDefaultMapper();

    private static final JavaType JAVA_TYPE = JSON_MAPPER.createCollectionType(List.class, CtqCpkDto.class);


    @Override
    public Response<Boolean> updateOrCreateRD(CompanyExtraRD companyExtraRD) {
        Response<Boolean> result = new Response<Boolean>();
        Long userId = companyExtraRD.getUserId();
        if (userId == null) {
            result.setError("company.extra.no.user");
            return result;
        }
        try {
            accountManager.createOrUpdateRD(userId, companyExtraRD);
            result.setResult(Boolean.TRUE);
        } catch (Exception e) {
            log.error("updateOrCreate CompanyExtraRD({}) failed, error code={}", companyExtraRD, Throwables.getStackTraceAsString(e));
            result.setError("company.extra.update.fail");
        }
        return result;
    }

    @Override
    public Response<CompanyExtraRD> findRDByUser(BaseUser user) {
        Response<CompanyExtraRD> result = new Response<CompanyExtraRD>();
        try {

            //尝试从暂存获取信息
            CompanyExtraRD stash = companyExtraRDRedisDao.findByKey(StashSupplierInfoKeys.RD(user.getId()));
            if (stash != null) {
                result.setResult(stash);
                return result;
            }

            CompanyExtraRD rd = companyExtraRDDao.findByUserId(user.getId());
            if (rd == null) {
                rd = new CompanyExtraRD();
            }
            result.setResult(rd);
        } catch (Exception e) {
            log.error("find CompanyExtraRD by user={} failed, error code={}", user, Throwables.getStackTraceAsString(e));
            result.setError("company.extra.query.fail");
        }
        return result;
    }

    @Override
    public Response<SupplierUpdatedInfoDto<CompanyExtraRD>> findRDByUserId(Long userId) {
        Response<SupplierUpdatedInfoDto<CompanyExtraRD>> result = new Response<SupplierUpdatedInfoDto<CompanyExtraRD>>();
        try {
            CompanyExtraRD rd = companyExtraRDDao.findByUserId(userId);
            if (rd == null) {
                rd = new CompanyExtraRD();
            }

            SupplierUpdatedInfoDto<CompanyExtraRD> supplierUpdatedInfoDto = new SupplierUpdatedInfoDto<CompanyExtraRD>();
            supplierUpdatedInfoDto.setSupplierInfo(rd);

            Map<String, Object> oldValues = Maps.newHashMap();
            supplierUpdatedInfoDto.setOldValues(oldValues);

            result.setResult(supplierUpdatedInfoDto);
        } catch (Exception e) {
            log.error("find CompanyExtraRD by user id={} failed, error code={}", userId, Throwables.getStackTraceAsString(e));
            result.setError("company.extra.query.fail");
        }
        return result;
    }

    @Override
    public Response<Boolean> updateOrCreateQuality(CompanyExtraQualityDto companyExtraQualityDto) {
        Response<Boolean> result = new Response<Boolean>();
        try {
            CompanyExtraQuality companyExtraQuality = companyExtraQualityDto.getCompanyExtraQuality();
            companyExtraQuality.setCtqCpk(JSON_MAPPER.toJson(companyExtraQualityDto.getCtqCpkList()));
            Long userId = companyExtraQuality.getUserId();
            if (userId == null) {
                result.setError("company.extra.no.user");
                return result;
            }

            accountManager.createOrUpdateCompanyExtraQuality(userId, companyExtraQuality);
            result.setResult(Boolean.TRUE);

        } catch (Exception e) {
            log.error("updateOrCreate CompanyExtraQualityDto({}) failed, error code={}", companyExtraQualityDto, Throwables.getStackTraceAsString(e));
            result.setError("company.extra.update.fail");
        }
        return result;
    }

    @Override
    public Response<CompanyExtraQualityDto> findQualityByUser(BaseUser user) {
        Response<CompanyExtraQualityDto> result = new Response<CompanyExtraQualityDto>();
        try {

            CompanyExtraQualityDto companyExtraQualityDto = new CompanyExtraQualityDto();

            //尝试从暂存获取信息
            CompanyExtraQuality stash = companyExtraQualityRedisDao.findByKey(StashSupplierInfoKeys.quality(user.getId()));
            if (stash != null) {
                List<CtqCpkDto> ctqCpkDtos = JSON_MAPPER.fromJson(stash.getCtqCpk(), JAVA_TYPE);
                if (ctqCpkDtos == null) {
                    ctqCpkDtos = Collections.emptyList();
                }
                companyExtraQualityDto.setCompanyExtraQuality(stash);
                companyExtraQualityDto.setCtqCpkList(ctqCpkDtos);
                result.setResult(companyExtraQualityDto);
                return result;
            }

            CompanyExtraQuality qua = companyExtraQualityDao.findByUserId(user.getId());
            if (qua == null) {
                qua = new CompanyExtraQuality();
            }

            List<CtqCpkDto> ctqCpkDtos = JSON_MAPPER.fromJson(qua.getCtqCpk(), JAVA_TYPE);
            if (ctqCpkDtos == null) {
                ctqCpkDtos = Collections.emptyList();
            }

            companyExtraQualityDto.setCompanyExtraQuality(supplierInfoChangedService.getNewCompanyExtraQuality(user.getId(), qua).getSupplierInfo());
            companyExtraQualityDto.setCtqCpkList(ctqCpkDtos);
            result.setResult(companyExtraQualityDto);
        } catch (Exception e) {
            log.error("find CompanyExtraQuality by user={} failed, error code={}", user, Throwables.getStackTraceAsString(e));
            result.setError("company.extra.query.fail");
        }
        return result;
    }

    @Override
    public Response<SupplierUpdatedInfoDto<CompanyExtraQualityDto>> findQualityByUserId(Long userId) {
        Response<SupplierUpdatedInfoDto<CompanyExtraQualityDto>> result = new Response<SupplierUpdatedInfoDto<CompanyExtraQualityDto>>();
        try {
            CompanyExtraQuality qua = companyExtraQualityDao.findByUserId(userId);
            if (qua == null) {
                qua = new CompanyExtraQuality();
            }

            List<CtqCpkDto> ctqCpkDtos = JSON_MAPPER.fromJson(qua.getCtqCpk(), JAVA_TYPE);
            if (ctqCpkDtos == null) {
                ctqCpkDtos = Collections.emptyList();
            }

            SupplierUpdatedInfoDto<CompanyExtraQuality> qualityUpdatedInfoDto = supplierInfoChangedService.getNewCompanyExtraQuality(userId, qua);

            CompanyExtraQualityDto companyExtraQualityDto = new CompanyExtraQualityDto();
            companyExtraQualityDto.setCompanyExtraQuality(qualityUpdatedInfoDto.getSupplierInfo());
            companyExtraQualityDto.setCtqCpkList(ctqCpkDtos);

            SupplierUpdatedInfoDto<CompanyExtraQualityDto> supplierUpdatedInfoDto = new SupplierUpdatedInfoDto<CompanyExtraQualityDto>();
            supplierUpdatedInfoDto.setSupplierInfo(companyExtraQualityDto);
            supplierUpdatedInfoDto.setOldValues(qualityUpdatedInfoDto.getOldValues());

            result.setResult(supplierUpdatedInfoDto);
        } catch (Exception e) {
            log.error("find CompanyExtraQuality by user id={} failed, error code={}", userId, Throwables.getStackTraceAsString(e));
            result.setError("company.extra.query.fail");
        }
        return result;
    }

    @Override
    public Response<Boolean> updateOrCreateResponse(CompanyExtraResponse companyExtraResponse) {
        Response<Boolean> result = new Response<Boolean>();
        Long userId = companyExtraResponse.getUserId();
        if (userId == null) {
            result.setError("company.extra.no.user");
            return result;
        }
        try {
            accountManager.createOrUpdateResponse(userId, companyExtraResponse);
            result.setResult(Boolean.TRUE);
        } catch (Exception e) {
            log.error("updateOrCreate CompanyExtraResponse({}) failed, error code={}", companyExtraResponse, Throwables.getStackTraceAsString(e));
            result.setError("company.extra.update.fail");
        }
        return result;
    }

    @Override
    public Response<CompanyExtraResponse> findResponseByUser(BaseUser user) {
        Response<CompanyExtraResponse> result = new Response<CompanyExtraResponse>();
        try {
            //尝试从暂存获取信息
            CompanyExtraResponse stash = companyExtraResponseRedisDao.findByKey(StashSupplierInfoKeys.response(user.getId()));
            if (stash != null) {
                result.setResult(stash);
                return result;
            }

            CompanyExtraResponse res = companyExtraResponseDao.findByUserId(user.getId());
            if (res == null) {
                res = new CompanyExtraResponse();
            }
            result.setResult(res);
        } catch (Exception e) {
            log.error("find CompanyExtraResponse by user={} failed, error code={}", user, Throwables.getStackTraceAsString(e));
            result.setError("company.extra.query.fail");
        }
        return result;
    }

    @Override
    public Response<SupplierUpdatedInfoDto<CompanyExtraResponse>> findResponseByUserId(Long userId) {
        Response<SupplierUpdatedInfoDto<CompanyExtraResponse>> result = new Response<SupplierUpdatedInfoDto<CompanyExtraResponse>>();
        try {
            CompanyExtraResponse res = companyExtraResponseDao.findByUserId(userId);
            if (res == null) {
                res = new CompanyExtraResponse();
            }

            SupplierUpdatedInfoDto<CompanyExtraResponse> supplierUpdatedInfoDto = new SupplierUpdatedInfoDto<CompanyExtraResponse>();
            supplierUpdatedInfoDto.setSupplierInfo(res);

            Map<String, Object> oldValues = Maps.newHashMap();
            supplierUpdatedInfoDto.setOldValues(oldValues);

            result.setResult(supplierUpdatedInfoDto);
        } catch (Exception e) {
            log.error("find CompanyExtraResponse by user id={} failed, error code={}", userId, Throwables.getStackTraceAsString(e));
            result.setError("company.extra.query.fail");
        }
        return result;
    }

    @Override
    public Response<Boolean> updateOrCreateDelivery(CompanyExtraDelivery companyExtraDelivery) {
        Response<Boolean> result = new Response<Boolean>();
        Long userId = companyExtraDelivery.getUserId();
        if (userId == null) {
            result.setError("company.extra.no.user");
            return result;
        }
        try {
            accountManager.createOrUpdateDelivery(userId, companyExtraDelivery);
            result.setResult(Boolean.TRUE);

        } catch (Exception e) {
            log.error("updateOrCreate CompanyExtraDelivery({}) failed, error code={}", companyExtraDelivery, Throwables.getStackTraceAsString(e));
            result.setError("company.extra.update.fail");
        }
        return result;
    }

    @Override
    public Response<CompanyExtraDelivery> findDeliveryByUser(BaseUser user) {
        Response<CompanyExtraDelivery> result = new Response<CompanyExtraDelivery>();
        try {
            //尝试从暂存获取信息
            CompanyExtraDelivery stash = companyExtraDeliveryRedisDao.findByKey(StashSupplierInfoKeys.delivery(user.getId()));
            if (stash != null) {
                result.setResult(stash);
                return result;
            }

            CompanyExtraDelivery del = companyExtraDeliveryDao.findByUserId(user.getId());
            if (del == null) {
                del = new CompanyExtraDelivery();
            }
            result.setResult(del);
        } catch (Exception e) {
            log.error("find CompanyExtraDelivery by user={} failed, error code={}", user, Throwables.getStackTraceAsString(e));
            result.setError("company.extra.query.fail");
        }
        return result;
    }

    @Override
    public Response<CompanyExtraDelivery> findDeliveryByUserId(Long userId) {
        BaseUser baseUser = new BaseUser();
        baseUser.setId(userId);
        return findDeliveryByUser(baseUser);
    }

    @Override
    public Response<Boolean> updateOrCreateScaleAndCost(CompanyExtraScaleAndCost companyExtraScaleAndCost) {
        Response<Boolean> result = new Response<Boolean>();
        Long userId = companyExtraScaleAndCost.getUserId();
        if (userId == null) {
            result.setError("company.extra.no.user");
            return result;
        }
        try {
            if (companyExtraScaleAndCostDao.findByUserId(userId) == null) {
                companyExtraScaleAndCostDao.create(companyExtraScaleAndCost);
                result.setResult(companyExtraScaleAndCostDao.findByUserId(userId) != null);
            } else {
                result.setResult(companyExtraScaleAndCostDao.update(companyExtraScaleAndCost));
            }
        } catch (Exception e) {
            log.error("updateOrCreate CompanyExtraScaleAndCost({}) failed, error code={}", companyExtraScaleAndCost, Throwables.getStackTraceAsString(e));
            result.setError("company.extra.update.fail");
        }
        return result;
    }

    @Override
    public Response<CompanyExtraScaleAndCost> findScaleAndCostByUser(BaseUser user) {
        Response<CompanyExtraScaleAndCost> result = new Response<CompanyExtraScaleAndCost>();
        try {
            CompanyExtraScaleAndCost sc = companyExtraScaleAndCostDao.findByUserId(user.getId());
            if (sc == null) {
                sc = new CompanyExtraScaleAndCost();
            }
            result.setResult(sc);
        } catch (Exception e) {
            log.error("find CompanyExtraScaleAndCost by user={} failed, error code={}", user, Throwables.getStackTraceAsString(e));
            result.setError("company.extra.query.fail");
        }
        return result;
    }

    @Override
    public Response<CompanyExtraScaleAndCost> findScaleAndCostByUserId(Long userId) {
        BaseUser baseUser = new BaseUser();
        baseUser.setId(userId);
        return findScaleAndCostByUser(baseUser);
    }
}
