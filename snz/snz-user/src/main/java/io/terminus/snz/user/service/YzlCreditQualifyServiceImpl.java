package io.terminus.snz.user.service;

import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import io.terminus.common.model.PageInfo;
import io.terminus.common.model.Paging;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.dao.YzlCreditQualifyDao;
import io.terminus.snz.user.model.YzlCreditQualify;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by dream on 14-10-12.
 */
@Slf4j
@Service
public class YzlCreditQualifyServiceImpl implements YzlCreditQualifyService {

    @Autowired
    private YzlCreditQualifyDao yzlCreditQualifyDao;

    @Override
    public Response<List<YzlCreditQualify>> findByStatus(Integer status){
        Response<List<YzlCreditQualify>> result = new Response<List<YzlCreditQualify>>();

        if (status == null) {
            log.error("status is null");
            result.setError("status.can.not.be.null");
            return result;
        }

        try {
            List<YzlCreditQualify> yzlCreditQualifies = yzlCreditQualifyDao.selectByStatus(status);
            result.setResult(yzlCreditQualifies);
            return result;
        } catch (Exception e) {
            log.error("snz_credit_qualify select failed");
            result.setError("select.failed");
            return result;
        }
    }

    @Override
    public Response<Boolean> deleteById(Integer id) {
        Response<Boolean> result = new Response<Boolean>();
        if (id == null) {
            result.setError("id.can.not.be.null");
            return result;
        }
        try {
            result.setResult(yzlCreditQualifyDao.deleteById(id));
            return result;
        } catch (Exception e) {
            log.error("snz_credit_qualify select failed");
            result.setError("delete.failed");
            return result;
        }
    }

    @Override
    public Response<Integer> create(YzlCreditQualify yzlCreditQualify) {
        Response<Integer> result = new Response<Integer>();
        if (yzlCreditQualify == null) {
            log.error("yzlCreditQualify can not be null");
            result.setError("params.can.not.be.null");
            return result;
        }

        try {
            result.setResult(yzlCreditQualifyDao.create(yzlCreditQualify));
            return result;
        } catch (Exception e) {
            log.error("snz_credit_qualify create failed");
            result.setError("insert.failed");
            return result;
        }
    }

    @Override
    public Response<Integer> update(YzlCreditQualify yzlCreditQualify) {
        Response<Integer> result = new Response<Integer>();
        if (yzlCreditQualify == null) {
            log.error("yzlCreditQualify can not be null");
            result.setError("params.not.null");
            return result;
        }

        try {
            result.setResult(yzlCreditQualifyDao.update(yzlCreditQualify));
            return result;
        } catch (Exception e) {
            log.error("snz_credit_qualify update failed,cause={}", Throwables.getStackTraceAsString(e));
            result.setError("update.failed");
            return result;
        }
    }

    @Override
    public Response<Integer> deleteByIds(List<Integer> ids) {
        Response<Integer> result = new Response<Integer>();
        if (ids == null) {
            log.error("param can not be null when deleteByIds");
            result.setError("params.not.null");
            return result;
        }
        try {
            result.setResult(yzlCreditQualifyDao.deleteByIds(ids));
            return result;
        } catch (Exception e) {
            log.error("Failed to deleteByIds from `snz_supplier_credit_qualifies` with param:{}", ids, e);
            result.setError("delete.failed");
            return result;
        }
    }

    @Override
    public Response<Integer> creates(List<YzlCreditQualify> yzlCreditQualifies) {
        Response<Integer> result = new Response<Integer>();
        if (yzlCreditQualifies == null) {
            log.error("param can not be null when creates");
            result.setError("params.not.null");
            return result;
        }
        try {
            result.setResult(yzlCreditQualifyDao.creates(yzlCreditQualifies));
            return result;
        } catch (Exception e) {
            log.error("Failed to creates into `snz_supplier_credit_qualifies` with param:{}", yzlCreditQualifies, e);
            result.setError("insert.failed");
            return result;
        }
    }

    @Override
    public Response<Paging<YzlCreditQualify>> findByPage(Integer status, String message, Integer pageNo, Integer size) {
        Response<Paging<YzlCreditQualify>> result = new Response<Paging<YzlCreditQualify>>();
        if (status == null && message == null) {
            log.error("params can not be null");
            result.setError("no.select.params");
            return result;
        }
        Map<String, Object> params = Maps.newHashMap();
        try {
            PageInfo pageInfo = new PageInfo(pageNo, size);
            params.put("offset", pageInfo.getOffset());
            params.put("limit", pageInfo.getLimit());
            Paging<YzlCreditQualify> yzlCreditQualifyPaging = yzlCreditQualifyDao.pagingForWhat(status, message, params);
            if (yzlCreditQualifyPaging.getTotal() < 1) {
                log.warn("No records found from `snz_supplier_credit_qualifies` with param:{}", params);
                result.setError("params.is.null");
                return result;
            }
            result.setResult(yzlCreditQualifyPaging);
            return result;
        } catch (Exception e) {
            log.error("Failed to findByPage from `snz_supplier_credit_qualifies` with param:{}", params, e);
            result.setError("select.failed");
            return result;
        }
    }
}
