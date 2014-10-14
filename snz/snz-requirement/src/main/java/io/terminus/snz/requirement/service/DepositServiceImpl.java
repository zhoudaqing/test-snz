package io.terminus.snz.requirement.service;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import io.terminus.common.model.Paging;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.eai.service.HGVSService;
import io.terminus.snz.requirement.dao.DepositDao;
import io.terminus.snz.requirement.dao.RequirementSolutionDao;
import io.terminus.snz.requirement.dto.DepositPayment;
import io.terminus.snz.requirement.dto.KjtTransDto;
import io.terminus.snz.requirement.dto.RequirementDto;
import io.terminus.snz.requirement.manager.DepositManager;
import io.terminus.snz.requirement.model.Deposit;
import io.terminus.snz.requirement.model.Requirement;
import io.terminus.snz.requirement.model.RequirementSolution;
import io.terminus.snz.user.model.Company;
import io.terminus.snz.user.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static io.terminus.common.utils.Arguments.isNull;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 6/16/14
 */
@Slf4j
@Service
public class DepositServiceImpl implements DepositService {

    @Autowired
    private DepositDao depositDao;

    @Autowired
    private DepositManager depositManager;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private RequirementService requirementService;

    @Autowired
    private RequirementSolutionService requirementSolutionService;

    @Autowired
    private RequirementSolutionDao requirementSolutionDao;

    @Autowired
    private RiskMortgagePaymentService riskMortgagePaymentService;

    @Autowired
    private HGVSService hgvsService;

    private static final long WAN = 1000000L;

    @Override
    public Response<List<Long>> findPaidListByRequirement(Long requirementId) {
        Response<List<Long>> result = new Response<List<Long>>();
        try {
            checkNotNull(requirementId, "requirementId must non null");

            List<Long> supplierIds = Lists.newArrayList();
            for (RequirementSolution solution : requirementSolutionDao.findAllSolution(requirementId)) {
                Response<Integer> paidResp = checkPaid(requirementId, solution.getSupplierId());
                if (!paidResp.isSuccess()) {
                    log.error("checkPaid requirementId={}, supplierId={} failed", requirementId, supplierIds);
                    continue;
                }
                // FIXME: to be .
                if (Objects.equal(paidResp.getResult(), 0) || Objects.equal(paidResp.getResult(), 2)) {
                    supplierIds.add(solution.getSupplierId());
                } else {
                    log.warn("requirementId={}, supplierId={}, not paid deposit, status={}, so skipped", requirementId, solution.getSupplierId(), paidResp.getResult());
                }
            }

            result.setResult(supplierIds);
        } catch (Exception e) {
            log.error("findPaidListByRequirement(requirementId={}) failed, cause:{}",
                    requirementId, Throwables.getStackTraceAsString(e));
            result.setError("deposit.query.fail");
        }
        return result;
    }

    @Override
    public Response<Integer> checkPaid(Long requirementId, Long supplierId) {
        Response<Integer> result = new Response<Integer>();
        try {
            checkNotNull(requirementId);
            checkNotNull(supplierId);

            Response<Company> companyResp = companyService.findCompanyById(supplierId);
            if (!companyResp.isSuccess()) {
                result.setError(companyResp.getError());
                return result;
            }

            Response<DepositPayment> paymentResp = getPayment(new BaseUser(companyResp.getResult().getUserId(), "", 2), requirementId);
            if (!paymentResp.isSuccess()) {
                result.setError(paymentResp.getError());
                return result;
            }

            result.setResult(paymentResp.getResult().getStatus());
        } catch (Exception e) {
            log.error("checkPaid(requirementId={}, supplierId={}) failed, cause:{}",
                    requirementId, supplierId, Throwables.getStackTraceAsString(e));
            result.setError("deposit.query.fail");
        }
        return result;
    }

    /**
     * 查询供应商已支付的保证金金额（当前时间点，其他需求中，已付款并且未退款）
     */
    private Response<Long> getAmountAlreadyPayingForDeposit(Long supplierId) {
        Response<Long> result = new Response<Long>();
        try {
            Long tot = 0L;

            Deposit criteria = new Deposit();
            criteria.setSupplierId(supplierId);
            criteria.setType(Deposit.Type.PAY.value());
            criteria.setStatus(Deposit.Status.TRANS_SUCCESS.value());

            for (Deposit deposit : depositDao.findListBy(criteria)) {
                tot += deposit.getAmount();
            }

            criteria.setType(Deposit.Type.REFUND.value());
            for (Deposit deposit : depositDao.findListBy(criteria)) {
                tot -= deposit.getAmount();
            }

            result.setResult(Math.max(0L, tot));

        } catch (Exception e) {
            log.error("getAmountAlreadyPayingForDeposit(supplierId={}) failed, cause:{}",
                    supplierId, Throwables.getStackTraceAsString(e));
            result.setError("deposit.query.fail");
        }

        return result;
    }


    private Response<DepositPayment> getPaymentRemain(Long supplierId, Long requirementId, Boolean isOldSupplier, Long balance, Long riskAmount) {
        Response<DepositPayment> result = new Response<DepositPayment>();
        try {
            checkArgument(supplierId > 0 && requirementId > 0 && riskAmount >= 0 && balance >= 0);

            Response<Company> companyR = companyService.findCompanyById(supplierId);
            if (!companyR.isSuccess()) {
                result.setError(companyR.getError());
                return result;
            }

            DepositPayment payment = new DepositPayment();

            // set company
            payment.setCompany(companyR.getResult());

            Response<RequirementDto> requirementR = requirementService.findById(requirementId);
            if (!requirementR.isSuccess()) {
                result.setError(requirementR.getError());
                return result;
            }
            // set requirement
            payment.setRequirement(requirementR.getResult().getRequirement());

            payment.setIsOldSupplier(isOldSupplier);

            // 质量风险抵押金、余额、累计缴纳保证金
            payment.setRiskMortgageAmount(riskAmount);
            payment.setHgvsBalance(balance);
            Response<Long> totRes = getAmountAlreadyPayingForDeposit(supplierId);
            if (!totRes.isSuccess()) {
                result.setError(totRes.getError());
                return result;
            }
            payment.setInPaying(totRes.getResult());

            Deposit dp = new Deposit();
            dp.setSupplierId(supplierId);
            dp.setRequirementId(requirementId);
            dp.setType(Deposit.Type.PAY.value());
            Deposit exist = depositDao.findBy(dp);
            if (exist != null) {
                // 若已提交成功，则无需再次提交
                if (Objects.equal(Deposit.Status.TRANS_SUCCESS.value(), exist.getStatus())) {
                    payment.setStatus(2); // 2表示已支付过
                    payment.setIsPaid(Boolean.TRUE);
                    payment.setIsEnough(Boolean.TRUE);
                    payment.setOrigMoney(exist.getAmount());
                    payment.setMoney(exist.getAmount());
                    result.setResult(payment);
                    return result;
                }
            }
            payment.setIsPaid(Boolean.FALSE);

            // 应付金额
            long moduleAmount = MoreObjects.firstNonNull(payment.getRequirement().getModuleAmount(), 0L);

            // 1. 保证金需要模块金额的百分之二
            long amount = moduleAmount / 50;

            // 2. 少于2W算2W
            amount = Math.max(2 * WAN, amount);

            // 3. 且不超过50W
            amount = Math.min(50 * WAN, amount);

            payment.setOrigMoney(amount); // 应付

            Long having;
            if (isOldSupplier) {
                having = Math.min(riskAmount, balance);
            } else {
                having = payment.getInPaying();
            }

            payment.setIsEnough(having >= amount);
            payment.setMoney(Math.max(0L, amount - having));

            payment.setStatus(1); // TODO: 默认都是需要支付（无需缴纳仍需要确认）

            result.setResult(payment);
        } catch (Exception e) {
            log.error("getPayment(requirementId={}) failed, error code={}", requirementId, Throwables.getStackTraceAsString(e));
            result.setError("deposit.get.payment.fail");
        }
        return result;
    }

    @Override
    public Response<DepositPayment> getPayment(BaseUser baseUser, Long requirementId) {
        Response<DepositPayment> result = new Response<DepositPayment>();
        if (isNull(baseUser)) {
            log.error("must login");
            result.setError("user.not.login");
            return result;
        }
        try {
            checkNotNull(requirementId, "must specify a requirement");

            Response<Company> companyR = companyService.findCompanyByUserId(baseUser.getId());
            if (!companyR.isSuccess()) {
                result.setError(companyR.getError());
                return result;
            }

            // 供应商V码，区分是否为老供应商
            String vCode = companyR.getResult().getSupplierCode();
            boolean isOldSupplier = !Strings.isNullOrEmpty(vCode);
            long riskAmount = 0L;
            long balance = 0L;

            // 只有老供应商有风险抵押金和HGVS余额
            if (isOldSupplier) {
                // 风险抵押金
                Response<Long> riskRes = riskMortgagePaymentService.getRiskMortgageAmountOfSupplier(vCode);
                if (!riskRes.isSuccess()) {
                    result.setError(riskRes.getError());
                    return result;
                }
                riskAmount = riskRes.getResult();
                // HGVS余额
                Response<Long> hgvsRes = hgvsService.getBalanceBySupplierCode(vCode);
                if (!hgvsRes.isSuccess()) {
                    result.setError(hgvsRes.getError());
                    return result;
                }
                balance = hgvsRes.getResult();
            }

            Response<DepositPayment> remainResp = getPaymentRemain(companyR.getResult().getId(), requirementId, isOldSupplier, balance, riskAmount);
            if (!remainResp.isSuccess()) {
                result.setError(remainResp.getError());
                return result;
            }

            result.setResult(remainResp.getResult());
        } catch (Exception e) {
            log.error("getPayment(requirementId={}) failed, error code={}", requirementId, Throwables.getStackTraceAsString(e));
            result.setError("deposit.get.payment.fail");
        }
        return result;
    }

    @Override
    public Response<Paging<DepositPayment>> pagingSolutionAndDeposit(BaseUser user, Integer qualifyStatus, Integer status, String reqName, String startAt, String endAt, Integer pageNo, Integer size) {
        Response<Paging<DepositPayment>> result = new Response<Paging<DepositPayment>>();
        if (isNull(user)) {
            log.error("must login");
            result.setError("user.not.login");
            return result;
        }
        Response<Paging<Requirement>> requirementPagingR = requirementSolutionService.findByParams(
                user, qualifyStatus, status, reqName, startAt, endAt, pageNo, size
        );
        if (!requirementPagingR.isSuccess()) {
            result.setError(requirementPagingR.getError());
            return result;
        }
        try {
            Response<Company> companyR = companyService.findCompanyByUserId(user.getId());
            if (!companyR.isSuccess()) {
                result.setError(companyR.getError());
                return result;
            }

            // 供应商V码，区分是否为老供应商
            String vCode = companyR.getResult().getSupplierCode();
            boolean isOldSupplier = !Strings.isNullOrEmpty(vCode);
            long riskAmount = 0L;
            long balance = 0L;

            // 只有老供应商有风险抵押金和HGVS余额
            if (isOldSupplier) {
                // 风险抵押金
                Response<Long> riskRes = riskMortgagePaymentService.getRiskMortgageAmountOfSupplier(vCode);
                if (!riskRes.isSuccess()) {
                    result.setError(riskRes.getError());
                    return result;
                }
                riskAmount = riskRes.getResult();
                // HGVS余额
                Response<Long> hgvsRes = hgvsService.getBalanceBySupplierCode(vCode);
                if (!hgvsRes.isSuccess()) {
                    result.setError(hgvsRes.getError());
                    return result;
                }
                balance = hgvsRes.getResult();
            }

            List<DepositPayment> payments = Lists.newArrayList();
            for (Requirement requirement : requirementPagingR.getResult().getData()) {
                Response<DepositPayment> remainResp = getPaymentRemain(companyR.getResult().getId(), requirement.getId(), isOldSupplier, balance, riskAmount);
                if (!remainResp.isSuccess()) {
                    result.setError(remainResp.getError());
                    return result;
                }
                DepositPayment payment = remainResp.getResult();
                payment.setRequirement(requirement);
                payments.add(payment);
            }

            result.setResult(new Paging<DepositPayment>(requirementPagingR.getResult().getTotal(), payments));
        } catch (Exception e) {
            log.error("pagingSolutionAndDeposit(user={}, qualifyStatus={}, status={}, reqName={}, startAt={}, endAt={}, pageNo={}, size={}) failed, error code={}",
                    user, qualifyStatus, status, reqName, startAt, endAt, pageNo, size, Throwables.getStackTraceAsString(e));
            result.setError("deposit.get.payment.fail");
        }
        return result;
    }

    @Override
    public Response<Boolean> agreeUseBalance(BaseUser baseUser, Long requirementId) {
        Response<Boolean> result = new Response<Boolean>();
        if (isNull(baseUser)) {
            result.setError("user.not.login");
            return result;
        }
        if (isNull(requirementId)) {
            result.setError("illegal.params");
            return result;
        }
        try {
            Response<DepositPayment> paymentResp = getPayment(baseUser, requirementId);
            if (!paymentResp.isSuccess()) {
                result.setError(paymentResp.getError());
                return result;
            }
            DepositPayment payment = paymentResp.getResult();
            if (payment.getIsPaid()) {
                log.warn("supplier(id={}) is already paid for requirement(id={})", payment.getCompany().getId(), requirementId);
            } else {
                if (payment.getIsEnough()) {
                    depositManager.useBalance(requirementId, payment.getCompany().getId());
                } else {
                    log.error("supplier(id={})'s balance does not enough for pay the deposit in requirement(id={})", payment.getCompany().getId(), requirementId);
                    result.setError("deposit.submit.fail");
                    return result;
                }
            }
            result.setResult(Boolean.TRUE);
        } catch (Exception e) {
            log.error("agree to use balance failed, baseUser={}, requirementId={}, cause:{}",
                    baseUser, requirementId, Throwables.getStackTraceAsString(e));
            result.setError("deposit.submit.fail");
        }
        return result;
    }

    @Override
    public Response<String> submit(BaseUser baseUser, Long requirementId, Long amount, KjtTransDto kjtTransDto) {
        log.debug("Deposit submit(requirementId={}, supplierId={}, amount={}, KjtTransDto={})",
                requirementId, amount, kjtTransDto);
        Response<String> result = new Response<String>();

        if (isNull(requirementId) || isNull(amount) || isNull(kjtTransDto)) {
            result.setError("illegal.params");
            return result;
        }

        if (isNull(baseUser)) {
            result.setError("user.not.login");
            return result;
        }
        Response<Company> companyR = companyService.findCompanyByUserId(baseUser.getId());
        if (!companyR.isSuccess()) {
            result.setError(companyR.getError());
            return result;
        }
        Long supplierId = companyR.getResult().getId();
        try {
            checkArgument(amount >= 0);
            Response<DepositPayment> paymentResp = getPayment(baseUser, requirementId);
            if (!paymentResp.isSuccess()) {
                result.setError(paymentResp.getError());
                return result;
            }
            checkState(Objects.equal(amount, paymentResp.getResult().getMoney()));
            Deposit deposit = depositManager.submit(requirementId, supplierId, amount, kjtTransDto);

            // 支付成功，不要重复提交
            if (Objects.equal(Deposit.Status.TRANS_SUCCESS.value(), deposit.getStatus())) {
                result.setError("deposit.already.trans");
                return result;
            }
            result.setResult(deposit.getDealUrl());
        } catch (Exception e) {
            log.error("submit(requirementId={}, supplierId={}, amount={}) failed, error code={}",
                    requirementId, supplierId, amount, Throwables.getStackTraceAsString(e));
            result.setError("deposit.submit.fail");
        }
        return result;
    }

    @Override
    public Response<Boolean> revoke(Long id) {
        Response<Boolean> result = new Response<Boolean>();

        if (id == null) {
            result.setError("illegal.params");
            return result;
        }

        try {
            result.setResult(depositManager.revoke(id));
        } catch (Exception e) {
            log.error("revoke Deposit(id={}) failed, error code={}", id, Throwables.getStackTraceAsString(e));
            result.setError("deposit.revoke.fail");
        }
        return result;
    }

    @Override
    public Response<Boolean> revokeFull(Long requirementId, Long supplierId) {
        Response<Boolean> result = new Response<Boolean>();
        try {
            checkNotNull(requirementId);
            checkNotNull(supplierId);

            Deposit criteria = new Deposit();
            criteria.setRequirementId(requirementId);
            criteria.setSupplierId(supplierId);
            criteria.setType(Deposit.Type.PAY.value());
            Deposit exist = checkNotNull(depositDao.findBy(criteria));
            result.setResult(depositManager.revoke(exist.getId()));
        } catch (Exception e) {
            log.error("revokeFull(requirementId={}, supplierId={}) failed, cause:{}",
                    requirementId, supplierId, Throwables.getStackTraceAsString(e));
            result.setError("deposit.revoke.fail");
        }
        return result;
    }

    @Override
    public Response<Boolean> dealResponse(Map<String, String> params) {
        Response<Boolean> result = new Response<Boolean>();
        try {
            log.warn("deal pay response(params={})", params);
            result.setResult(depositManager.payResponse(params));
        } catch (Exception e) {
            log.error("dealResponse(params={}) failed, error code={}",
                    params, Throwables.getStackTraceAsString(e));
            result.setError("deposit.response.fail");
        }
        return result;
    }

    @Override
    public Response<Boolean> dealTransResponse(Map<String, String> params) {
        Response<Boolean> result = new Response<Boolean>();
        try {
            log.warn("deal trans response(params={})", params);
            result.setResult(depositManager.transResponse(params));
        } catch (Exception e) {
            log.error("dealTransResponse(params={}) failed, error code={}",
                    params, Throwables.getStackTraceAsString(e));
            result.setError("deposit.response.fail");
        }
        return result;
    }
}
