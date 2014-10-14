package io.terminus.snz.user.service;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.FluentIterable;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.dao.UserDao;
import io.terminus.snz.user.model.Company;
import io.terminus.snz.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static io.terminus.common.utils.Arguments.isNull;

/**
 * Author:Guo Chaopeng
 * Created on 14-7-28.
 */
@Slf4j
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private UserDao userDao;

    @Override
    public Response<Boolean> addTag(Long userId, User.SupplierTag tag) {

        Response<Boolean> result = new Response<Boolean>();

        if (userId == null) {
            log.error("user id can not be null");
            result.setError("user.id.not.null.fail");
            return result;
        }

        if (tag == null) {
            log.error("tag can not be null");
            result.setError("tag.not.null.fail");
            return result;
        }

        try {

            User user = userDao.findById(userId);
            if (user == null) {
                log.error("user(user id={}) not found", userId);
                result.setError("user.not.found");
                return result;
            }

            List<String> tags = user.buildTags();

            add(tags, tag.toString());
            user.setTagsFromList(tags);

            //如果是状态标签，则更新所处阶段
            if (isStatusTag(tag)) {
                User.Step step = convertToStep(tag);
                if (step != null) {
                    user.setStep(step.value());
                }
            }

            userDao.update(user);

            result.setResult(Boolean.TRUE);

        } catch (Exception e) {
            log.error("fail to add tag where user id={},tag={},cause:{}", userId, tag, Throwables.getStackTraceAsString(e));
            result.setError("add.tag.fail");
        }

        return result;
    }

    @Override
    public Response<Boolean> delTag(Long userId, User.SupplierTag tag) {
        Response<Boolean> result = new Response<Boolean>();

        if (userId == null) {
            log.error("user id can not be null");
            result.setError("user.id.not.null.fail");
            return result;
        }

        if (tag == null) {
            log.error("tag can not be null");
            result.setError("tag.not.null.fail");
            return result;
        }

        try {

            User user = userDao.findById(userId);
            if (user == null) {
                log.error("user(user id={}) not found", userId);
                result.setError("user.not.found");
                return result;
            }

            List<String> tags = user.buildTags();

            remove(tags, tag.toString());
            user.setTagsFromList(tags);
            userDao.update(user);

            result.setResult(Boolean.TRUE);

        } catch (Exception e) {
            log.error("fail to add tag where user id={},tag={},cause:{}", userId, tag, Throwables.getStackTraceAsString(e));
            result.setError("add.tag.fail");
        }

        return result;
    }

    @Override
    public Response<Boolean> addTQRDCTag(Long userId, Integer score) {

        Response<Boolean> result = new Response<Boolean>();

        if (userId == null) {
            log.error("user id can not be null");
            result.setError("user.id.not.null.fail");
            return result;
        }

        if (score == null) {
            log.error("score can not be null");
            result.setError("score.not.null.fail");
            return result;
        }

        try {

            User user = userDao.findById(userId);
            if (user == null) {
                log.error("user(user id={}) not found", userId);
                result.setError("user.not.found");
                return result;
            }

            List<String> tags = user.buildTags();

            //找出已有的tqrdc tag
            String old = null;
            for (String tag : tags) {
                if (tag.startsWith(User.SupplierTag.PERFORMANCE.toString())) {
                    old = tag;
                }
            }

            //删除已有的tqrdc tag
            if (old != null) {
                tags.remove(old);
            }

            tags.add(User.SupplierTag.PERFORMANCE.toString().concat(String.valueOf(score)));
            user.setTagsFromList(tags);
            userDao.update(user);

            result.setResult(Boolean.TRUE);

        } catch (Exception e) {
            log.error("fail to add tqrdc tag where user id={},score={},cause:{}", userId, score, Throwables.getStackTraceAsString(e));
            result.setError("add.tag.fail");
        }

        return result;
    }

    @Override
    public Response<Boolean> updateTagsByCompany(Long userId, Company company) {

        Response<Boolean> result = new Response<Boolean>();

        if (userId == null) {
            log.error("user id can not be null");
            result.setError("user.id.not.null.fail");
            return result;
        }

        if (company == null) {
            log.error("company can not be null");
            result.setError("company.not.null.fail");
            return result;
        }

        try {

            User user = userDao.findById(userId);
            if (user == null) {
                log.error("user(user id={}) not found", userId);
                result.setError("user.not.found");
                return result;
            }

            List<String> tags = user.buildTags();

            if (Objects.equal(company.getWorldTop(), Company.WorldTop.IS_WORLD_TOP_500.value())) {
                add(tags, User.SupplierTag.WORLD_TOP_SUPPLIER.toString());
            } else if (Objects.equal(company.getWorldTop(), Company.WorldTop.NO_WORLD_TOP_500.value())) {
                remove(tags, User.SupplierTag.WORLD_TOP_SUPPLIER.toString());
            }

            if (!Strings.isNullOrEmpty(company.getSupplierCode())) {
                add(tags, User.SupplierTag.IN_SUPPLIER.toString());
            }

            user.setTagsFromList(tags);
            userDao.update(user);

            result.setResult(Boolean.TRUE);

        } catch (Exception e) {
            log.error("fail to update tag where user id={},company={},cause:{}", userId, company, Throwables.getStackTraceAsString(e));
            result.setError("add.tag.fail");
        }

        return result;
    }

    @Override
    public Response<Boolean> addSupplierStatusTag(Long userId, User.SupplierTag tag) {
        Response<Boolean> result = new Response<Boolean>();
        try {
            List<String> tags = checkNotNull(userDao.findById(userId)).buildRoles();
            User.SupplierTag curStat = getCurrentStat(tags);
            if (canMove(curStat, tag)) {
                delTag(userId, curStat);
                addTag(userId, tag);
                result.setResult(Boolean.TRUE);
            } else {
                result.setResult(Boolean.FALSE);
            }
        } catch (Exception e) {
            log.error("addSupplierStatusTag(userId={}, tag={}) failed, cause:{}",
                    userId, tag, Throwables.getStackTraceAsString(e));
            result.setError("add.tag.fail");
        }
        return result;
    }

    private User.SupplierTag getCurrentStat(final List<String> tags) {
        return FluentIterable.from(
                Arrays.asList(
                        User.SupplierTag.ALTERNATIVE,       // 备选
                        User.SupplierTag.DIE_OUT,           // 淘汰
                        User.SupplierTag.IN_SUPPLIER,       // 合作
                        User.SupplierTag.STANDARD_SUPPLIER, // 入围
                        User.SupplierTag.COMPLETE_SUPPLIER, // 完善信息
                        User.SupplierTag.REGISTER_SUPPLIER  // 注册
                )
        ).firstMatch(new Predicate<User.SupplierTag>() {
            @Override
            public boolean apply(User.SupplierTag input) {
                return tags.contains(input.toString());
            }
        }).orNull();
    }

    private boolean isStatusTag(User.SupplierTag tag) {
        return Arrays.asList(
                User.SupplierTag.REGISTER_SUPPLIER.value(),
                User.SupplierTag.COMPLETE_SUPPLIER.value(),
                User.SupplierTag.STANDARD_SUPPLIER.value(),
                User.SupplierTag.IN_SUPPLIER.value(),
                User.SupplierTag.ALTERNATIVE.value(),
                User.SupplierTag.DIE_OUT.value()
        ).contains(tag.value());
    }

    private boolean canMove(User.SupplierTag from, User.SupplierTag to) {
        if (isNull(from)) {
            return Objects.equal(to.value(), User.SupplierTag.REGISTER_SUPPLIER.value());
        }
        if (Objects.equal(from.value(), User.SupplierTag.REGISTER_SUPPLIER.value())) {
            return Arrays.asList(
                    User.SupplierTag.COMPLETE_SUPPLIER.value(),
                    User.SupplierTag.STANDARD_SUPPLIER.value(),
                    User.SupplierTag.ALTERNATIVE.value(),
                    User.SupplierTag.IN_SUPPLIER.value(),
                    User.SupplierTag.DIE_OUT.value()
            ).contains(to.value());
        }
        if (Objects.equal(from.value(), User.SupplierTag.COMPLETE_SUPPLIER.value())) {
            return Arrays.asList(
                    User.SupplierTag.STANDARD_SUPPLIER.value(),
                    User.SupplierTag.ALTERNATIVE.value(),
                    User.SupplierTag.IN_SUPPLIER.value(),
                    User.SupplierTag.DIE_OUT.value()
            ).contains(to.value());
        }
        if (Objects.equal(from.value(), User.SupplierTag.STANDARD_SUPPLIER.value())) {
            return Arrays.asList(
                    User.SupplierTag.ALTERNATIVE.value(),
                    User.SupplierTag.IN_SUPPLIER.value(),
                    User.SupplierTag.DIE_OUT.value()
            ).contains(to.value());
        }
        if (Objects.equal(from.value(), User.SupplierTag.ALTERNATIVE.value())) {
            return Arrays.asList(
                    User.SupplierTag.IN_SUPPLIER.value(),
                    User.SupplierTag.DIE_OUT.value()
            ).contains(to.value());
        }
        if (Objects.equal(from.value(), User.SupplierTag.IN_SUPPLIER.value())) {
            return Objects.equal(to.value(), User.SupplierTag.DIE_OUT.value());
        }
        if (Objects.equal(from.value(), User.SupplierTag.DIE_OUT.value())) {
            return Objects.equal(to.value(), User.SupplierTag.ALTERNATIVE.value());
        }
        return false;
    }

    private void add(List<String> tags, String tag) {
        if (tags == null) {
            return;
        }
        if (!tags.contains(tag)) {
            tags.add(tag);
        }
    }

    private void remove(List<String> tags, String tag) {
        if (tags == null) {
            return;
        }
        if (tags.contains(tag)) {
            tags.remove(tag);
        }
    }

    private User.Step convertToStep(User.SupplierTag tag) {
        switch (tag) {
            case REGISTER_SUPPLIER:
                return User.Step.REGISTER_SUPPLIER;
            case COMPLETE_SUPPLIER:
                return User.Step.COMPLETE_SUPPLIER;
            case STANDARD_SUPPLIER:
                return User.Step.STANDARD_SUPPLIER;
            case ALTERNATIVE:
                return User.Step.ALTERNATIVE;
            case IN_SUPPLIER:
                return User.Step.IN_SUPPLIER;
            case DIE_OUT:
                return User.Step.DIE_OUT;
            default:
                return null;
        }
    }


}
