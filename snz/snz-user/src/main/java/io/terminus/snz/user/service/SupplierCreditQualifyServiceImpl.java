package io.terminus.snz.user.service;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import io.terminus.common.model.PageInfo;
import io.terminus.common.model.Paging;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.message.models.Mail;
import io.terminus.snz.message.services.MailService;
import io.terminus.snz.message.services.MessageService;
import io.terminus.snz.user.dao.CompanyDao;
import io.terminus.snz.user.dao.SupplierCreditQualifyDao;
import io.terminus.snz.user.dto.SupplierCreditQualifyDto;
import io.terminus.snz.user.model.Company;
import io.terminus.snz.user.model.SupplierCreditQualify;
import io.terminus.snz.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static io.terminus.common.utils.Arguments.*;
import static io.terminus.snz.user.model.SupplierCreditQualify.STATUS;
import static io.terminus.snz.user.model.SupplierCreditQualify.TYPE;

/**
 * todo: test
 * Date: 7/31/14
 * Time: 11:05
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */

@Slf4j
@Service
public class SupplierCreditQualifyServiceImpl implements SupplierCreditQualifyService {

    private static final Days WEEK = Days.days(7);

    private static final Integer PAGE_SIZE = 500;

    @Autowired
    private SupplierCreditQualifyDao supplierCreditQualifyDao;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private TagService tagService;

    @Autowired
    private MailService mailService;

    @Deprecated
    private AccountService<User> accountService;

    @Override
    public Response<SupplierCreditQualify> findCreditQualifyByUserId(Long userId) {
        Response<SupplierCreditQualify> result = new Response<SupplierCreditQualify>();

        try {
            checkArgument(positive(userId), "illegal.argument");
            SupplierCreditQualify param = new SupplierCreditQualify();
            param.setUserId(userId);
            SupplierCreditQualify found = supplierCreditQualifyDao.findBy(param);
            // 未找到说明没有提交申请
            if (isNull(found)) {
                found = new SupplierCreditQualify();
                found.setUserId(userId);
                result.setResult(found);
                return result;
            }

            result.setResult(found);
        } catch (IllegalArgumentException e) {
            log.error("`findCreditQualifyByUserId` invoke with illegal userId:{}", userId);
            result.setError(e.getMessage());
            return result;
        } catch (Exception e) {
            log.error("`findCreditQualifyByUserId` invoke fail. with userId:{}, e:{}", userId, e);
            result.setError("supplier.credit.qualify.find.fail");
            return result;
        }

        return result;
    }

    @Override
    public Response<List<SupplierCreditQualify>> findCreditQualifyByUserIds(List<Long> userIds) {
        Response<List<SupplierCreditQualify>> result = new Response<List<SupplierCreditQualify>>();

        if(userIds == null || userIds.isEmpty()){
            log.error("find supplier credit qualify need user id, user id list is empty.");
            result.setError("supplier.id.empty");
            return result;
        }

        try {
            result.setResult(supplierCreditQualifyDao.findByUserIds(userIds));
        } catch (Exception e) {
            log.error("`findCreditQualifyByUserIds` invoke fail. with userIds:{}, e:{}", userIds, Throwables.getStackTraceAsString(e));
            result.setError("supplier.credit.qualify.find.fail");
            return result;
        }

        return result;
    }

    @Override
    public Response<Long> applyCreditQualify(BaseUser currentUser) {
        Response<Long> result = new Response<Long>();
        if (isNull(currentUser) || isNull(currentUser.getId())) {
            log.error("`applyCreditQualify` invoke without authorization, user:{}", currentUser);
            result.setError("user.not.login");
            return result;
        }

        try {
            // 已经完成的报错
            SupplierCreditQualify param = new SupplierCreditQualify();
            param.setUserId(currentUser.getId());
            SupplierCreditQualify found = supplierCreditQualifyDao.findBy(param);
            if (found != null && found.isCreditQualified()) {
                result.setError("supplier.credit.qualify.duplicate.apply");
                return result;
            }

            // 已存在的就更新
            Response<Company> companyGet = companyService.findCompanyByUserId(currentUser.getId());
            checkState(companyGet.isSuccess(), companyGet.getError());
            Company company = companyGet.getResult();
            if (found != null) {
                found.setStatusEnum(STATUS.APPLYING);
                found.setSupplierId(company.getId());
                supplierCreditQualifyDao.update(found);
                result.setResult(found.getId());
                return result;
            }

            // 不存在就创建
            found = new SupplierCreditQualify();
            found.setSupplierId(company.getId());
            found.setUserId(currentUser.getId());
            found.setStatusEnum(STATUS.APPLYING);
            supplierCreditQualifyDao.create(found);

            result.setResult(found.getId());
        } catch (IllegalStateException e) {
            log.error("`applyCreditQualify` invoke fail. with user:{}, e:{}", currentUser, e);
            result.setError(e.getMessage());
            return result;
        } catch (Exception e) {
            log.error("`applyCreditQualify` invoke fail. with user:{}, e:{}", currentUser, e);
            result.setError("supplier.credit.qualify.apply.fail");
            return result;
        }

        return result;
    }

    @Override
    public Response<Boolean> systemApplyCreditQualify(Long uid, String msg, Integer status) {
        Response<Boolean> result = new Response<Boolean>();

        try {
            checkArgument(positive(uid), "illegal.user.id");
            STATUS s = STATUS.fromValue(status);
            checkArgument(positive(status) && notNull(status), "illegal.credit.qualify.status");

            // 已经完成的报错
            SupplierCreditQualify param = new SupplierCreditQualify();
            param.setUserId(uid);
            SupplierCreditQualify found = supplierCreditQualifyDao.findBy(param);
            if (found != null && found.isCreditQualified()) {
                result.setError("supplier.credit.qualify.duplicate.apply");
                return result;
            }

            // 已存在的就更新
            Response<Company> companyGet = companyService.findCompanyByUserId(uid);
            checkState(companyGet.isSuccess(), companyGet.getError());
            Company company = companyGet.getResult();
            if (found != null) {
                found.setStatusEnum(s);
                found.setSupplierId(company.getId());
                if (!Strings.isNullOrEmpty(msg)) {
                    found.appendMessage(msg, 0l, "海尔");
                }
                supplierCreditQualifyDao.update(found);
                result.setResult(true);
                return result;
            }

            // 不存在就创建
            found = new SupplierCreditQualify();
            found.setSupplierId(company.getId());
            found.setUserId(uid);
            found.setStatusEnum(s);
            if (!Strings.isNullOrEmpty(msg)) {
                found.appendMessage(msg, 0l, "海尔");
            }
            supplierCreditQualifyDao.create(found);

            result.setResult(true);

            // 为供应商打上标签
            if (Objects.equal(found.whichType(), TYPE.APPLIED)) {
                Response<Boolean> tryAddTag = tagService.addTag(found.getUserId(), User.SupplierTag.CREDIT);
                if (!tryAddTag.isSuccess()) {
                    // just log an error and keep going
                    log.error("add supplier tag fail, with uid :{}. message:{}, status:{}, e:{}",
                            uid, msg, status, tryAddTag.getError());
                }
            } else { // 当做不成功处理，移除 tag
                Response<Boolean> tryDelTag = tagService.delTag(found.getUserId(), User.SupplierTag.CREDIT);
                if (!tryDelTag.isSuccess()) {
                    // just log an error and keep going
                    log.error("remove supplier tag fail, with uid :{}. message:{}, status:{}, e:{}",
                            uid, msg, status, tryDelTag.getError());
                }
            }
        } catch (IllegalArgumentException e) {
            log.error("`applyCreditQualify` invoke fail. with illegal uid:{}, msg:{}, status:{}, e:{}", uid, msg, status, e);
            result.setError(e.getMessage());
            return result;
        } catch (IllegalStateException e) {
            log.error("`applyCreditQualify` invoke fail. with uid:{}, msg:{}, status:{}, e:{}", uid, msg, status, e);
            result.setError(e.getMessage());
            return result;
        } catch (Exception e) {
            log.error("`applyCreditQualify` invoke fail. with uid:{}, msg:{}, status:{}, e:{}", uid, msg, status, e);
            result.setError("supplier.credit.qualify.apply.fail");
            return result;
        }

        return result;
    }

    @Override
    public Response<Long> appealCreditQualify(BaseUser currentUser, String message) {
        Response<Long> result = new Response<Long>();
        if (currentUser == null) {
            result.setError("user.not.login");
            return result;
        }

        if (Strings.isNullOrEmpty(message)) {
            result.setError("supplier.credit.qualify.message.not.present");
            return result;
        }

        try {
            SupplierCreditQualify found = supplierCreditQualifyDao.findByUserId(currentUser.getId());
            checkState(notNull(found), "supplier.credit.qualify.find.fail");
            checkState(Objects.equal(TYPE.REJECT, found.whichType()), "supplier.credit.qualify.not.status.reject");
            found.appendRepealMsg(message, currentUser.getId(), currentUser.getName());
            found.setStatusEnum(STATUS.APPEAL);
            supplierCreditQualifyDao.update(found);

            result.setResult(found.getId());
        } catch (Exception e) {
            log.error("`appealCreditQualify` invoke fail. ");
            result.setError("");
            return result;
        }

        return result;
    }

    @Override
    public Response<Boolean> updateCreditQualify(BaseUser user, Long id, String message, Integer status) {
        Response<Boolean> result = new Response<Boolean>();

        try {
            checkArgument(positive(id), "illegal.argument");
            checkArgument(notNull(status), "illegal.argument");
            if (Objects.equal(STATUS.OTHERS.toValue(), status)) {
                checkArgument(!Strings.isNullOrEmpty(message), "supplier.credit.qualify.message.not.present");
            }

            // 不存在就报错
            SupplierCreditQualify found = supplierCreditQualifyDao.findById(id);
            checkState(notNull(found), "supplier.credit.qualify.find.fail");
            found.setStatus(status);
            if (!Strings.isNullOrEmpty(message)) {
                found.appendMessage(message, user.getId(), user.getName());
            }
            found.setReviewer(user.getId());
            found.setReviewerName(user.getNickName());

            supplierCreditQualifyDao.update(found);
            // 为供应商打上标签
            if (Objects.equal(found.whichType(), TYPE.APPLIED)) {
                Response<Boolean> tryAddTag = tagService.addTag(found.getUserId(), User.SupplierTag.CREDIT);
                if (!tryAddTag.isSuccess()) {
                    // just log an error and keep going
                    log.error("add supplier tag fail, with loginUser:{}, credit qualify id :{}. message:{}, status:{}, e:{}",
                            user, id, message, status, tryAddTag.getError());
                }
            } else { // 当做不成功处理，移除 tag
                Response<Boolean> tryDelTag = tagService.delTag(found.getUserId(), User.SupplierTag.CREDIT);
                if (!tryDelTag.isSuccess()) {
                    // just log an error and keep going
                    log.error("remove supplier tag fail, with loginUser:{}, credit qualify id :{}. message:{}, status:{}, e:{}",
                            user, id, message, status, tryDelTag.getError());
                }
            }

            result.setResult(true);
        } catch (IllegalStateException e) {
            log.error("`findCreditQualifyByUserId` invoke fail, with loginUser:{}, credit qualify id :{}. message:{}, status:{}, e:{}",
                    user, id, message, status, e);
            result.setError(e.getMessage());
            return result;
        } catch (IllegalArgumentException e) {
            log.error("`findCreditQualifyByUserId` invoke with illegal with loginUser:{}, credit qualify id :{}. message:{}, status:{}, e:{}",
                    user, id, message, status, e);
            result.setError(e.getMessage());
            return result;
        } catch (Exception e) {
            log.error("`updateCreditQualify` invoke fail. with with loginUser:{}, credit qualify id :{}. message:{}, status:{}, e:{}",
                    user, id, message, status, e);
            result.setError("supplier.credit.qualify.update.fail");
            return result;
        }
        return result;
    }

    @Override
    public Response<Paging<SupplierCreditQualifyDto>> pagingCreditQualify(BaseUser currentUser, Integer type,
                                                                          Integer pageNo, Integer size) {
        PageInfo page = new PageInfo(pageNo, size);
        Response<Paging<SupplierCreditQualifyDto>> result = new Response<Paging<SupplierCreditQualifyDto>>();
        try {
            checkArgument(notNull(currentUser), "user.not.login");
            TYPE t = TYPE.fromValue(type);

            Paging<SupplierCreditQualify> creditQualifyPaging =
                    supplierCreditQualifyDao.pagingForQualify(t.getValue(), page.getOffset(), page.getLimit());
            if (Objects.equal(creditQualifyPaging.getTotal(), 0L)
                    || isNullOrEmpty(creditQualifyPaging.getData())) {

                result.setResult(
                        new Paging<SupplierCreditQualifyDto>(0l,
                                Collections.<SupplierCreditQualifyDto>emptyList())
                );
                return result;
            }

            // 完善公司信息
            List<SupplierCreditQualifyDto> dtos = Lists.newArrayList();
            for (SupplierCreditQualify cq : creditQualifyPaging.getData()) {
                Company company = companyDao.findById(cq.getSupplierId());
                if (company == null) {
                    continue;
                }
                SupplierCreditQualifyDto dto = new SupplierCreditQualifyDto();
                dto.setSupplierCreditQualify(cq);
                dto.setCompany(company);
                dtos.add(dto);
            }

            result.setResult(new Paging<SupplierCreditQualifyDto>(creditQualifyPaging.getTotal(), dtos));
        } catch (IllegalArgumentException e) {
            log.error("`pagingCreditQualify` invoke with illegal currentUser:{}", currentUser);
            result.setError(e.getMessage());
            return result;
        } catch (Exception e) {
            log.error("`pagingCreditQualify` invoke fail. with currentUser:{}", currentUser);
            result.setError("supplier.credit.qualify.find.fail");
            return result;
        }

        return result;
    }

    @Override
    public Response<Boolean> rejectQualify(User user, Long id) {
        Response<Boolean> result = new Response<Boolean>();

        try {
            if (!positive(id)) {
                result.setResult(false);
                return result;
            }
            // 登录用户须有二级资质验证权限
            checkArgument(notNull(user) && notNull(user.getId()), "user.not.login");
            // todo: 二级财务权限验证
            //List<String> roles = user.buildRoles();
            if(false) { //!roles.contains(User.JobRole.SHARING.role())) {
                result.setError("user.not.authorized");
                return result;
            }

            SupplierCreditQualify creditQualify = supplierCreditQualifyDao.findById(id);
            if (notNull(creditQualify)) {
                DateTime start = new DateTime(creditQualify.getUpdatedAt().getTime());
                if(Days.daysBetween(start, DateTime.now()).isGreaterThan(WEEK)) {
                    result.setError("supplier.credit.qualify.reject.greater.than.week");
                    return result;
                }
                creditQualify.setStatusEnum(STATUS.REJECT);
                supplierCreditQualifyDao.update(creditQualify);
                result.setResult(true);

                Response<Boolean> tryDelTag = tagService.delTag(creditQualify.getUserId(), User.SupplierTag.CREDIT);
                if (!tryDelTag.isSuccess()) {
                    // just log an error and keep going
                    log.error("add supplier tag fail, with loginUser:{}, credit qualify id :{}, e:{}",
                            user, id, tryDelTag.getError());
                }
            } else {
                result.setResult(false);
            }

        } catch (IllegalArgumentException e) {
            log.error("`rejectQualify` invoke with illegal arguments: users:{}, id:{}, e:{}", user, id, e);
            result.setError(e.getMessage());
            return result;
        } catch (Exception e) {
            log.error("`rejectQualify` invoke fail. with users:{}, id:{}, e:{}", user, id, e);
            result.setError("supplier.reject.credit.fail");
            return result;
        }

        return result;
    }

    @Override
    public Response<Boolean> notifyUpcomingIn(Integer days) {
        Response<Boolean> result = new Response<Boolean>();

        try {
            // find all sharings
            Response<List<User>> sharings = accountService.findUserByRoleStr(User.JobRole.SHARING.role());
            if (!sharings.isSuccess()) {
                log.error("find user with role string fail, e:{}", sharings.getError());
                result.setResult(false);
                return result;
            }
            if (sharings.getResult().isEmpty()) {
                result.setResult(true);
                return result;
            }

            batchNotify(days, sharings.getResult());
            result.setResult(true);
        } catch (Exception e) {
            log.error("`upComingIn` invoke fail. e:{}", e);
            result.setError("supplier.credit.notify.upcoming.fail");
            return result;
        }

        return result;
    }

    @Override
    public Response<Boolean> notifyDelayed(Integer days) {
        Response<Boolean> result = new Response<Boolean>();

        try {
            // find all sharings and his superior
            Response<List<User>> sharings = accountService.findUserByRoleStr(User.JobRole.SHARING.role());
            if (!sharings.isSuccess()) {
                log.error("find user with role string fail, e:{}", sharings.getError());
                result.setResult(false);
                return result;
            }
            Response<List<User>> superiors = accountService.findUserByRoleStr(User.JobRole.SHARING_SB.role());
            if (!superiors.isSuccess()) {
                log.error("find user with role string fail, e:{}", sharings.getError());
                result.setResult(false);
                return result;
            }

            List<User> users = Lists.newArrayList();
            users.addAll(sharings.getResult());
            users.addAll(superiors.getResult());
            if (sharings.getResult().isEmpty()) {
                result.setResult(true);
                return result;
            }

            batchNotify(days, users);
            result.setResult(true);
        } catch (Exception e) {
            log.error("`notifyDelayed` invoke fail. e:{}", e);
            result.setError("supplier.credit.notify.delayed.fail");
            return result;
        }
        return result;
    }

    private void batchNotify(Integer days, List<User> sharings) {
        DateTime now = DateTime.now().withTimeAtStartOfDay();
        Date startAt = now.minusDays(days).toDate();
        Date endAt = now.minusDays(days - 1).toDate();

        // get all supplier name whose qualifies near deadline
        Paging<SupplierCreditQualify> creditPage = supplierCreditQualifyDao
                .upCommingIn(startAt, endAt, 0, PAGE_SIZE);
        List<String> supplierNames = Lists.newArrayList();
        while (creditPage.getTotal() != 0 && !creditPage.getData().isEmpty()) {
            for (SupplierCreditQualify scq:creditPage.getData()) {
                Company company = companyDao.findById(scq.getSupplierId());
                if (company == null) {
                    log.error("find company by id fail, with id:{}", scq.getSupplierId());
                    continue;
                }
                supplierNames.add(company.getCorporation());
            }
        }
        if (supplierNames.isEmpty()) {
            return;
        }

        // send mail
        List<Mail<?>> mails = Lists.newArrayList();
        for (User user : sharings) {
            Mail<Map<String, Object>> mail = new Mail<Map<String, Object>>();
            mail.setTo(user.getEmail());
            mail.setType(Mail.Type.CREDIT_QUALIFY_UPCOMING);
            mail.setData(ImmutableMap.<String, Object>of("supplierNames", supplierNames));

            mails.add(mail);
        }
        mailService.batchSend(mails);
    }
}
