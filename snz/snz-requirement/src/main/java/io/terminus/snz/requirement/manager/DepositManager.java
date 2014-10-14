package io.terminus.snz.requirement.manager;

import com.google.common.base.Objects;
import io.terminus.common.utils.JsonMapper;
import io.terminus.snz.requirement.dao.DepositDao;
import io.terminus.snz.requirement.dto.KjtTransDto;
import io.terminus.snz.requirement.model.Deposit;
import io.terminus.snz.requirement.tool.KjtpayKit;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static io.terminus.common.utils.Arguments.isNull;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 7/10/14
 */
@Slf4j
@Component
public class DepositManager {

    @Autowired
    private DepositDao depositDao;

    @Transactional
    public Deposit submit(Long requirementId, Long supplierId, Long amount, KjtTransDto kjtTransDto) {
        Deposit dp = new Deposit();

        // 查询标准，唯一确定一条支付信息
        dp.setRequirementId(requirementId);
        dp.setSupplierId(supplierId);
        dp.setType(Deposit.Type.PAY.value());

        Deposit exist = depositDao.findBy(dp);

        String bankInfo = JsonMapper.nonDefaultMapper().toJson(kjtTransDto);
        if (exist == null) {
            // 初始创建保证金
            dp.setStatus(Deposit.Status.INIT.value());
            dp.setAmount(amount);
            dp.setBankInfo(bankInfo);
            dp.setSyncStatus(0);
            depositDao.create(dp);
            checkState(dp.getId() > 0);
            exist = checkNotNull(depositDao.findById(dp.getId()));
        } else {
            // update current amount
            exist.setAmount(amount);
            exist.setBankInfo(bankInfo);
        }

        Map<String, String> params = KjtpayKit.buildPayRequest(
                "DEPOSIT4PAY" + exist.getId(), exist.getAmount(), exist.getCreatedAt(),
                "海尔迭代量对堵平台-保证金-支付"
        );

        String url = KjtpayKit.request(KjtpayKit.sign(params));

        exist.setDealUrl(url);
        exist.setStatus(Deposit.Status.SUBMIT_SUCCESS.value());

        depositDao.update(exist);

        return exist;
    }

    @Transactional
    public boolean revoke(Long id) {
        Deposit exist = depositDao.findById(id);

        if (exist == null || !Objects.equal(exist.getStatus(), Deposit.Status.TRANS_SUCCESS.value())) {
            throw new RuntimeException();
        }

        KjtTransDto kjtTransDto = JsonMapper.nonDefaultMapper().fromJson(exist.getBankInfo(), KjtTransDto.class);

        Map<String, String> params = KjtpayKit.buildTransRequest(
                "DEPOSIT4REFUND" + exist.getId(), exist.getAmount(), kjtTransDto
        );

        // 退款提交失败
        if (!KjtpayKit.requestTrans(KjtpayKit.sign(params))) {
            throw new RuntimeException();
        }

        Deposit refund = new Deposit();

        refund.setRequirementId(exist.getRequirementId());
        refund.setSupplierId(exist.getSupplierId());
        refund.setType(Deposit.Type.REFUND.value());

        // 已经存在此退款，不科学！
        if (depositDao.findBy(refund) != null) {
            throw new RuntimeException();
        }

        refund.setBankInfo(exist.getBankInfo());
        refund.setStatus(Deposit.Status.SUBMIT_SUCCESS.value());

        if (depositDao.create(refund) == null) {
            throw new RuntimeException();
        }

        return true;
    }

    @Transactional
    public boolean payResponse(Map<String, String> params) {
        if (!KjtpayKit.verify(params)) {
            throw new IllegalArgumentException("kjt failed to verify params");
        }

        Long orderId = Long.parseLong(params.get("orderId").substring("DEPOSIT4PAY".length()));
        String orderTime = params.get("orderTime");
        Long orderAmount = Long.parseLong(params.get("orderAmount"));
        Integer payType = Integer.parseInt(params.get("payType"));
        String payResult = params.get("payResult");
        String errCode = params.get("errCode");

        String dealId = params.get("dealId");
        String dealTime = params.get("dealTime");

        Deposit exist = depositDao.findById(orderId);

        // 已支付成功
        if (Objects.equal(exist.getStatus(), Deposit.Status.TRANS_SUCCESS.value())) {
            return true;
        }

        if (!Objects.equal(orderAmount, exist.getAmount())
                || !Objects.equal(orderTime, KjtpayKit.dateFormat.print(new DateTime(exist.getCreatedAt())))) {
            throw new RuntimeException();
        }

        exist.setDealId(dealId);
        exist.setDealTime(KjtpayKit.dateFormat.parseDateTime(dealTime).toDate());

        // TODO: check payType
        if (Objects.equal(payResult, "0000")
                || Objects.equal(errCode, KjtpayKit.ErrCode.ALREADY_PAY_SUCCESS.value())) {
            exist.setStatus(Deposit.Status.TRANS_SUCCESS.value());
        } else {
            exist.setStatus(Deposit.Status.TRANS_FAILED.value());
        }

        if (!depositDao.update(exist)) {
            throw new RuntimeException();
        }

        return true;
    }

    @Transactional
    public boolean transResponse(Map<String, String> params) {
        if (!KjtpayKit.verify(params)) {
            throw new IllegalArgumentException("kjt failed to verify params");
        }

        Long orderId = Long.parseLong(params.get("orderid").substring("DEPOSIT4REFUND".length()));
        Integer amount = Integer.parseInt(params.get("amount"));
        Integer payType = Integer.parseInt(params.get("paytype"));

        KjtTransDto kjtTransDto = KjtpayKit.buildTransResponse(params);

        Deposit exist = depositDao.findById(orderId);

        // 已退款成功
        if (Objects.equal(exist.getStatus(), Deposit.Status.TRANS_SUCCESS.value())) {
            return true;
        }

        // 与数据库中资料不一致
        if (!Objects.equal(exist.getBankInfo(), JsonMapper.nonDefaultMapper().toJson(kjtTransDto))
                || !Objects.equal(exist.getAmount(), amount.longValue())) {
            throw new RuntimeException();
        }

        if (payType == 1) {
            exist.setStatus(Deposit.Status.TRANS_SUCCESS.value());
        } else {
            exist.setStatus(Deposit.Status.TRANS_FAILED.value());
        }

        if (!depositDao.update(exist)) {
            throw new RuntimeException();
        }

        return true;
    }

    public void useBalance(Long requirementId, Long supplierId) {
        Deposit exist = depositDao.findBy(supplierId, requirementId, Deposit.Type.PAY);
        if (isNull(exist)) {
            exist = new Deposit();
            exist.setRequirementId(requirementId);
            exist.setSupplierId(supplierId);
            exist.setType(Deposit.Type.PAY.value());
            exist.setStatus(Deposit.Status.TRANS_SUCCESS.value());
            exist.setAmount(0L); // 无需缴纳
            depositDao.create(exist);
        } else {
            if (!Objects.equal(exist.getStatus(), Deposit.Status.TRANS_SUCCESS.value()) ||
                    Objects.equal(exist.getAmount(), 0L)) {
                log.error("exist deposit does not trans success nor need not paid, status={}, amount={}", exist.getStatus(), exist.getAmount());
                throw new RuntimeException();
            }
        }
    }
}
