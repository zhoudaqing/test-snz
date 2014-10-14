package io.terminus.snz.user.service;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Throwables;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.dao.PurchaserAuthorityDao;
import io.terminus.snz.user.model.PurchaserAuthority;
import io.terminus.snz.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static io.terminus.common.utils.Arguments.notNull;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 8/28/14
 */
@Slf4j
@Service
public class PurchaserAuthorityServiceImpl implements PurchaserAuthorityService {

    @Autowired
    private PurchaserAuthorityDao purchaserAuthorityDao;

    @Override
    public Response<Boolean> grantAuthInBcId(Long userId, Long bcId, Integer position, String role) {
        Response<Boolean> result = new Response<Boolean>();
        try {
            checkArgument(userId > 0);
            checkArgument(bcId > 0);
            checkArgument(position == 1 || position == 2); // 目前只支持1. 小微主, 2. 小微成员
            checkArgument(notNull(User.JobRole.from(role)));
            PurchaserAuthority params = getAuthority(userId, bcId, position, role);
            List<PurchaserAuthority> authorities = purchaserAuthorityDao.findBy(params);
            if (authorities.isEmpty()) {
                checkState(purchaserAuthorityDao.create(params) > 0);
            }
            result.setResult(Boolean.TRUE);
        } catch (Exception e) {
            log.error("grantAuthInBcId(userId={}, bcId={}, position={}, role={}) failed, cause:{}",
                    userId, bcId, position, role, Throwables.getStackTraceAsString(e));
            result.setError("purchaser.authority.grant.fail");
        }
        return result;
    }

    @Override
    public Response<Boolean> revokeAuthInBcId(Long userId, Long bcId, Integer position, String role) {
        Response<Boolean> result = new Response<Boolean>();
        try {
            checkArgument(userId > 0);
            checkArgument(bcId > 0);
            checkArgument(position == 1 || position == 2); // 目前只支持1. 小微主, 2. 小微成员
            checkArgument(notNull(User.JobRole.from(role)));
            List<PurchaserAuthority> authorities = purchaserAuthorityDao.findBy(getAuthority(userId, bcId, position, role));
            if (!authorities.isEmpty()) {
                for (PurchaserAuthority authority : authorities) {
                    // TODO: need Transactional?
                    purchaserAuthorityDao.delete(authority.getId());
                }
            }
            result.setResult(Boolean.TRUE);
        } catch (Exception e) {
            log.error("revokeAuthInBcId(userId={}, bcId={}, position={}, role={}) failed, cause:{}",
                    userId, bcId, position, role, Throwables.getStackTraceAsString(e));
            result.setError("purchaser.authority.revoke.fail");
        }
        return result;
    }

    @Override
    public Response<List<Long>> getAuthorizedBcIds(Long userId, Integer position, String role) {
        Response<List<Long>> result = new Response<List<Long>>();
        try {
            checkArgument(userId > 0);
            checkArgument(position == 1 || position == 2); // 目前只支持1. 小微主, 2. 小微成员
            checkArgument(notNull(User.JobRole.from(role)));
            PurchaserAuthority params = getAuthority(userId, null, position, role);
            List<Long> bcIds = Lists.transform(
                    purchaserAuthorityDao.findBy(params), new Function<PurchaserAuthority, Long>() {
                        @Override
                        public Long apply(PurchaserAuthority input) {
                            return Long.parseLong(input.getContent());
                        }
                    });
            result.setResult(bcIds);
        } catch (Exception e) {
            log.error("getAuthorizedBcIds(userId={}, position={}, role={}) failed, cause:{}",
                    userId, position, role, Throwables.getStackTraceAsString(e));
            result.setError("purchaser.authority.query.fail");
        }
        return result;
    }

    @Override
    public Response<List<Long>> getUserIdsHavingAuthInBcId(Long bcId, Integer position, String role) {
        Response<List<Long>> result = new Response<List<Long>>();
        try {
            checkArgument(bcId > 0);
            checkArgument(position == 1 || position == 2);
            checkArgument(notNull(User.JobRole.from(role)));
            result.setResult(Lists.transform(purchaserAuthorityDao.findBy(getAuthority(null, bcId, position, role)),
                    new Function<PurchaserAuthority, Long>() {
                        @Override
                        public Long apply(PurchaserAuthority input) {
                            return input.getUserId();
                        }
                    }));
        } catch (Exception e) {
            log.error("getUserIdsHavingAuthInBcId(bcId={}, position={}, role={}) failed, cause:{}",
                    bcId, position, role, Throwables.getStackTraceAsString(e));
            result.setError("purchaser.authority.query.fail");
        }
        return result;
    }

    @Override
    public Response<List<Long>> getUserIdsHavingAuthInBcIds(List<Long> bcIds, Integer position, String role) {
        Response<List<Long>> result = new Response<List<Long>>();
        try {
            checkArgument(!bcIds.isEmpty());
            checkArgument(position == 1 || position == 2);
            checkArgument(notNull(User.JobRole.from(role)));
            List<Long> userIds = Lists.newArrayList();
            for (Long bcId : bcIds) {
                userIds.addAll(Lists.transform(purchaserAuthorityDao.findBy(getAuthority(null, bcId, position, role)),
                        new Function<PurchaserAuthority, Long>() {
                            @Override
                            public Long apply(PurchaserAuthority input) {
                                return input.getUserId();
                            }
                        }));
            }
            result.setResult(userIds);
        } catch (Exception e) {
            log.error("getUserIdsHavingAuthInBcIds(bcIds={}, position={}, role={}) failed, cause:{}",
                    bcIds, position, role, Throwables.getStackTraceAsString(e));
            result.setError("purchaser.authority.query.fail");
        }
        return result;
    }

    @Override
    public Response<Boolean> checkAuthInBcId(Long userId, Long bcId, Integer position, String role) {
        Response<Boolean> result = new Response<Boolean>();
        try {
            checkArgument(userId > 0);
            checkArgument(bcId > 0);
            checkArgument(position == 1 || position == 2); // 目前只支持1. 小微主, 2. 小微成员
            checkArgument(notNull(User.JobRole.from(role)));
            result.setResult(!purchaserAuthorityDao.findBy(getAuthority(userId, bcId, position, role)).isEmpty());
        } catch (Exception e) {
            log.error("checkAuthInBcId(userId={}, bcId={}, position={}, role={}) failed, cause:{}",
                    userId, bcId, position, role, Throwables.getStackTraceAsString(e));
            result.setError("purchaser.authority.check.fail");
        }
        return result;
    }

    @Override
    public Response<Boolean> checkAuthInAnyBcIds(final Long userId, List<Long> bcIds, final Integer position, final String role) {
        Response<Boolean> result = new Response<Boolean>();
        try {
            checkArgument(userId > 0);
            checkArgument(!bcIds.isEmpty());
            checkArgument(position == 1 || position == 2); // 目前只支持1. 小微主, 2. 小微成员
            checkArgument(notNull(User.JobRole.from(role)));
            result.setResult(FluentIterable.from(bcIds).anyMatch(new Predicate<Long>() {
                @Override
                public boolean apply(Long input) {
                    return !purchaserAuthorityDao.findBy(getAuthority(userId, input, position, role)).isEmpty();
                }
            }));
        } catch (Exception e) {
            log.error("checkAuthInAnyBcIds(userId={}, bcIds={}, position={}, role={}) failed, cause:{}",
                    userId, bcIds, position, role, Throwables.getStackTraceAsString(e));
            result.setError("purchaser.authority.check.fail");
        }
        return result;
    }

    @Override
    public Response<Boolean> checkAuthInAllBcIds(final Long userId, List<Long> bcIds, final Integer position, final String role) {
        Response<Boolean> result = new Response<Boolean>();
        try {
            checkArgument(userId > 0);
            checkArgument(!bcIds.isEmpty());
            checkArgument(position == 1 || position == 2); // 目前只支持1. 小微主, 2. 小微成员
            checkArgument(notNull(User.JobRole.from(role)));
            result.setResult(FluentIterable.from(bcIds).allMatch(new Predicate<Long>() {
                @Override
                public boolean apply(Long input) {
                    return !purchaserAuthorityDao.findBy(getAuthority(userId, input, position, role)).isEmpty();
                }
            }));
        } catch (Exception e) {
            log.error("checkAuthInAllBcIds(userId={}, bcIds={}, position={}, role={}) failed, cause:{}",
                    userId, bcIds, position, role, Throwables.getStackTraceAsString(e));
            result.setError("purchaser.authority.check.fail");
        }
        return result;
    }

    private PurchaserAuthority getAuthority(@Nullable Long userId, @Nullable Long bcId, Integer position, String role) {
        PurchaserAuthority params = new PurchaserAuthority();
        params.setUserId(userId);
        if (notNull(bcId)) {
            params.setContent(bcId.toString());
        }
        params.setType(1); // 后台类目
        params.setPosition(position);
        params.setRole(role);
        return params;
    }
}
