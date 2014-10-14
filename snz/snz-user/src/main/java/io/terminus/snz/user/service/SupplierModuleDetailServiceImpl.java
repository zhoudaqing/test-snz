/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */
package io.terminus.snz.user.service;

import com.google.common.collect.Lists;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.dao.SupplierModuleDetailDao;
import io.terminus.snz.user.model.SupplierModuleDetail;
import lombok.extern.slf4j.Slf4j;
import org.example.transpricegoodsinfotohgvs.TransPriceGoodsInfoToHGVS;
import org.example.transpricegoodsinfotohgvs.TransPriceGoodsInfoToHGVSService;
import org.example.transpricegoodsinfotohgvs.ZDWHJHPT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 供应商物料明细 服务类<BR>
 *
 * @author wanggen 2014-09-22 22:23:37
 */
@Service
@Slf4j
public class SupplierModuleDetailServiceImpl implements SupplierModuleDetailService {

    @Autowired
    private SupplierModuleDetailDao supplierModuleDetailDao;


    private static final TransPriceGoodsInfoToHGVS sapService = new TransPriceGoodsInfoToHGVSService().getTransPriceGoodsInfoToHGVSSOAP();


    /**
     * 创建 供应商物料明细
     *
     * @param supplierModuleDetail 供应商物料明细
     * @return 新增实例ID
     */
    @Override
    public Response<Long> create(SupplierModuleDetail supplierModuleDetail) {
        Response<Long> resp = new Response<Long>();
        if (supplierModuleDetail == null) {
            log.error("param [supplierModuleDetail] can not be null");
            resp.setError("params.not.null");
            return resp;
        }
        try {
            Long createdId = supplierModuleDetailDao.create(supplierModuleDetail);
            resp.setResult(createdId);
            return resp;
        } catch (Exception e) {
            resp.setError("snz_supplier_module_details.insert.failed");
            log.error("Failed to insert into `snz_supplier_module_details` with param:{}", supplierModuleDetail, e);
            return resp;
        }
    }

    /**
     * 根据ID查找一条供应商物料明细
     *
     * @param id 实例ID
     * @return 返回供应商物料明细结果
     */
    @Override
    public Response<SupplierModuleDetail> findById(Long id) {
        Response<SupplierModuleDetail> resp = new Response<SupplierModuleDetail>();
        if (id == null) {
            log.error("param [id] can not be null when query");
            resp.setError("params.not.null");
            return resp;
        }
        try {
            SupplierModuleDetail supplierModuleDetail = supplierModuleDetailDao.findById(id);
            if (supplierModuleDetail == null) {
                log.warn("No records found from `snz_supplier_module_details` with param:{}", id);
                resp.setError("snz_supplier_module_details.select.failed");
                return resp;
            }
            resp.setResult(supplierModuleDetail);
            return resp;
        } catch (Exception e) {
            resp.setError("snz_supplier_module_details.select.failed");
            log.error("Failed to findById from `snz_supplier_module_details` with param:{}", id, e);
            return resp;
        }
    }


    @Override
    public Response<List<SupplierModuleDetail>> findBySupplierCode(String supplierCode) {
        Response<List<SupplierModuleDetail>> resp = new Response<List<SupplierModuleDetail>>();
        try {
            List<SupplierModuleDetail> supplierModuleDetails = supplierModuleDetailDao.findBySupplierCode(supplierCode);
            resp.setResult(supplierModuleDetails);
            return resp;
        } catch (Exception e) {
            resp.setError("snz_supplier_module_details.select.failed");
            log.error("Failed to findBySupplierCode from `snz_supplier_module_details` with supplierCode:{}", supplierCode, e);
            return resp;
        }
    }


    /**
     * 根据实例ID更新记录
     *
     * @param supplierModuleDetail 供应商物料明细
     * @return 更新影响行数
     */
    @Override
    public Response<Integer> update(SupplierModuleDetail supplierModuleDetail) {
        Response<Integer> resp = new Response<Integer>();
        if (supplierModuleDetail == null) {
            log.error("param supplierModuleDetail can not be null when update");
            resp.setError("params.not.null");
            return resp;
        }
        try {
            resp.setResult(supplierModuleDetailDao.update(supplierModuleDetail));
            return resp;
        } catch (Exception e) {
            resp.setError("snz_supplier_module_details.update.failed");
            log.error("Failed to update table `snz_supplier_module_details` with param:{}", supplierModuleDetail, e);
            return resp;
        }
    }

    /**
     * 更具ID列表删除供应商物料明细
     *
     * @param ids 供应商物料明细ID列表
     * @return 删除影响行数
     */
    @Override
    public Response<Integer> deleteByIds(List<Long> ids) {
        Response<Integer> resp = new Response<Integer>();
        if (ids == null) {
            log.error("param can not be null when deleteByIds");
            resp.setError("params.not.null");
            return resp;
        }
        try {
            resp.setResult(supplierModuleDetailDao.deleteByIds(ids));
            return resp;
        } catch (Exception e) {
            resp.setError("snz_supplier_module_details.delete.failed");
            log.error("Failed to deleteByIds from `snz_supplier_module_details` with param:{}", ids, e);
            return resp;
        }
    }

    /**
     * 推送供应商物料数据
     *
     * @param supplierCode 供应商编码
     * @return 实际推送数据量
     */
    @Override
    public Response<Integer> postToHGVS(String supplierCode) {
        List<ZDWHJHPT> datain = Lists.newArrayList();
        List<SupplierModuleDetail> moduleDetails = supplierModuleDetailDao.findBySupplierCode(supplierCode);
        for(SupplierModuleDetail supplierModuleDetail: moduleDetails){
            ZDWHJHPT data = new ZDWHJHPT();
            data.setIDNO(supplierModuleDetail.getId()+"");			            //数据ID: 可按规则生成: 如日期+id
            data.setMATNR(supplierModuleDetail.getModuleNum());			        //物料号
            data.setLIFNR(supplierModuleDetail.getSupplierCode());				//供应商账号
            data.setEKORG(supplierModuleDetail.getPurchOrg());					//采购组织
            data.setEKGRP(supplierModuleDetail.getPurchGroup());	            //采购组
            data.setTAX(supplierModuleDetail.getTaxCode());						//税码
            datain.add(data);
        }
        if(datain.size()>0)
        sapService.transPriceGoodsInfoToHGVS(datain);
        return null;
    }


}
