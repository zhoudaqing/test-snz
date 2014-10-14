package io.terminus.snz.user.service;

import io.terminus.pampas.common.Response;
import io.terminus.snz.user.dao.ProductOwnerDao;
import io.terminus.snz.user.model.ProductOwner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 产品线负责人及相关工厂信息<P>
 *
 * @author wanggen 2014-08-18 10:50:18
 */
@Service
@Slf4j
public class ProductOwnerServiceImpl implements ProductOwnerService {

    @Autowired
    private ProductOwnerDao productOwnerDao;

    @Override
    public Response<Long> create(ProductOwner productOwner) {
        Response<Long> resp = new Response<Long>();
        if (productOwner == null) {
            log.error("param [productOwner] can not be null");
            resp.setError("params.not.null");
            return resp;
        }
        try {
            Long createdId = productOwnerDao.create(productOwner);
            resp.setResult(createdId);
            return resp;
        } catch (Exception e) {
            resp.setError("snz_product_owners.insert.failed");
            log.error("Failed to insert into `snz_product_owners` with param:{}", productOwner, e);
            return resp;
        }
    }

    @Override
    public Response<ProductOwner> findById(Long id) {
        Response<ProductOwner> resp = new Response<ProductOwner>();
        if (id == null) {
            log.error("param [id] can not be null when query");
            resp.setError("params.not.null");
            return resp;
        }
        try {
            ProductOwner productOwner = productOwnerDao.findById(id);
            if (productOwner == null) {
                log.warn("Failed to findById from `snz_product_owners` with param:{}", id);
                resp.setError("snz_product_owners.select.failed");
                return resp;
            }
            resp.setResult(productOwner);
            return resp;
        } catch (Exception e) {
            resp.setError("snz_product_owners.select.failed");
            log.error("Failed to findById from `snz_product_owners` with param:{}", id, e);
            return resp;
        }
    }

    @Override
    public Response<List<ProductOwner>> findByIds(List<Long> ids) {
        Response<List<ProductOwner>> resp = new Response<List<ProductOwner>>();
        if (ids == null) {
            log.error("param [ids] can not be null when findByIds");
            resp.setError("params.not.null");
            return resp;
        }
        try {
            List<ProductOwner> productOwners = productOwnerDao.findByIds(ids);
            if (productOwners == null || productOwners.size() < 1) {
                log.warn("Failed to findByIds from `snz_product_owners` with param:{}", ids);
                resp.setError("snz_product_owners.select.failed");
                return resp;
            }
            resp.setResult(productOwners);
            return resp;
        } catch (Exception e) {
            resp.setError("snz_product_owners.select.failed");
            log.error("Failed to findByIds from `snz_product_owners` with param:{}", ids, e);
            return resp;
        }
    }


    @Override
    public Response<List<ProductOwner>> findByProductLineId(Integer productLineId) {
        Response<List<ProductOwner>> resp = new Response<List<ProductOwner>>();
        try {
            List<ProductOwner> productOwners = productOwnerDao.findByProductLineId(productLineId);
            resp.setResult(productOwners==null ? Collections.EMPTY_LIST : productOwners);
            return resp;
        } catch (Exception e) {
            resp.setError("snz_product_owners.select.failed");
            log.error("Failed to findByProductLineId from `snz_product_owners` with productLineId:{}", productLineId, e);
            return resp;
        }
    }


    @Override
    public Response<List<ProductOwner>> findByProductLineName(String productLineName) {
        Response<List<ProductOwner>> resp = new Response<List<ProductOwner>>();
        try {
            List<ProductOwner> productOwners = productOwnerDao.findByProductLineName(productLineName);
            resp.setResult(productOwners==null ? Collections.EMPTY_LIST : productOwners);
            return resp;
        } catch (Exception e) {
            resp.setError("snz_product_owners.select.failed");
            log.error("Failed to findByProductLineName from `snz_product_owners` with productLineName:{}", productLineName, e);
            return resp;
        }
    }


    @Override
    public Response<Integer> update(ProductOwner productOwner) {
        Response<Integer> resp = new Response<Integer>();
        if (productOwner == null) {
            log.error("param productOwner can not be null when update");
            resp.setError("params.not.null");
            return resp;
        }
        try {
            resp.setResult(productOwnerDao.update(productOwner));
            return resp;
        } catch (Exception e) {
            resp.setError("snz_product_owners.update.failed");
            log.error("Failed to update table `snz_product_owners` with param:{}", productOwner, e);
            return resp;
        }
    }

    @Override
    public Response<Integer> deleteByIds(List<Long> ids) {
        Response<Integer> resp = new Response<Integer>();
        if (ids == null) {
            log.error("param can not be null when deleteByIds");
            resp.setError("params.not.null");
            return resp;
        }
        try {
            resp.setResult(productOwnerDao.deleteByIds(ids));
            return resp;
        } catch (Exception e) {
            resp.setError("snz_product_owners.delete.failed");
            log.error("Failed to deleteByIds from `snz_product_owners` with param:{}", ids, e);
            return resp;
        }
    }
}
