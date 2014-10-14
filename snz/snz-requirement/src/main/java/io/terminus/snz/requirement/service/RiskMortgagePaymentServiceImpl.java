package io.terminus.snz.requirement.service;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.dao.RiskMortgagePaymentDao;
import io.terminus.snz.requirement.model.RiskMortgagePayment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 7/21/14
 */
@Slf4j
@Service
public class RiskMortgagePaymentServiceImpl implements RiskMortgagePaymentService {

    @Autowired
    private RiskMortgagePaymentDao riskMortgagePaymentDao;

    @Override
    public Response<Long> create(RiskMortgagePayment payment) {
        Response<Long> result = new Response<Long>();
        try {
            result.setResult(riskMortgagePaymentDao.create(payment));
        } catch (Exception e) {
            log.error("create RiskMortgagePayment={} failed, error code={}", payment, Throwables.getStackTraceAsString(e));
            result.setError("risk.mortgage.payment.create.fail");
        }
        return result;
    }

    @Override
    public Response<Boolean> update(RiskMortgagePayment payment) {
        Response<Boolean> result = new Response<Boolean>();
        try {
            result.setResult(riskMortgagePaymentDao.update(payment));
        } catch (Exception e) {
            log.error("update RiskMortgagePayment={} failed, error code={}", payment, Throwables.getStackTraceAsString(e));
            result.setError("risk.mortgage.payment.update.fail");
        }
        return result;
    }

    @Override
    public Response<Boolean> delete(Long id) {
        Response<Boolean> result = new Response<Boolean>();
        try {
            result.setResult(riskMortgagePaymentDao.delete(id));
        } catch (Exception e) {
            log.error("delete RiskMortgagePayment(id={}) failed, error code={}", id, Throwables.getStackTraceAsString(e));
            result.setError("risk.mortgage.payment.delete.fail");
        }
        return result;
    }

    @Override
    public Response<RiskMortgagePayment> findById(Long id) {
        Response<RiskMortgagePayment> result = new Response<RiskMortgagePayment>();
        try {
            result.setResult(Objects.firstNonNull(riskMortgagePaymentDao.findById(id), new RiskMortgagePayment()));
        } catch (Exception e) {
            log.error("find RiskMortgagePayment by id={} failed, error code={}", id, Throwables.getStackTraceAsString(e));
            result.setError("risk.mortgage.payment.query.fail");
        }
        return result;
    }

    @Override
    public Response<RiskMortgagePayment> findBy(RiskMortgagePayment payment) {
        Response<RiskMortgagePayment> result = new Response<RiskMortgagePayment>();
        try {
            result.setResult(Objects.firstNonNull(riskMortgagePaymentDao.findBy(payment), new RiskMortgagePayment()));
        } catch (Exception e) {
            log.error("find RiskMortgagePayment by({}) failed, error code={}", payment, Throwables.getStackTraceAsString(e));
            result.setError("risk.mortgage.payment.query.fail");
        }
        return result;
    }

    @Override
    public Response<Long> getRiskMortgageAmountOfSupplier(String supplierCode) {
        Response<Long> result = new Response<Long>();
        try {
            if (Strings.isNullOrEmpty(supplierCode)) {
                result.setResult(0L);
                return result;
            }
            RiskMortgagePayment payment = new RiskMortgagePayment();
            payment.setSupplierCode(supplierCode);
            List<RiskMortgagePayment> payments = riskMortgagePaymentDao.findListBy(payment);
            if (payments == null) {
                payments = Collections.emptyList();
            }
            Long ret = 0L;
            for (RiskMortgagePayment riskMortgagePayment : payments) {
                Long midRisk = riskMortgagePayment.getAmount();
                if (midRisk < 0) {
                    log.error("minus exist");
                    result.setError("risk.mortgage.payment.minus.exist");
                    return result;
                }
                ret += midRisk;
            }
            result.setResult(ret);
        } catch (Exception e) {
            log.error("get RiskMortgagePayment amount by(supplierCode={}) failed, error code={}",
                    supplierCode, Throwables.getStackTraceAsString(e));
            result.setError("risk.mortgage.payment.query.fail");
        }
        return result;
    }
}
