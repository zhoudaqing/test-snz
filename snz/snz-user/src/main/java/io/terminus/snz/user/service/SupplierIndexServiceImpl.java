package io.terminus.snz.user.service;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Stopwatch;
import com.google.common.base.Throwables;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import io.terminus.pampas.common.Response;
import io.terminus.search.ESClient;
import io.terminus.search.endpoint.SearchExecutor;
import io.terminus.snz.user.dao.UserDao;
import io.terminus.snz.user.dto.RichSupplier;
import io.terminus.snz.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by yangzefeng on 14-6-25
 */
@Component
@Slf4j
public class SupplierIndexServiceImpl implements SupplierIndexService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RichSuppliers richSupplierFactory;

    @Autowired
    private ESClient esClient;

    @Autowired
    private SearchExecutor searchExecutor;

    private static final Integer PAGE_SIZE = 200;

    private static final String SUPPLIER_INDEX_NAME = "suppliers";

    private static final String SUPPLIER_INDEX_TYPE = "supplier";

    private static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    private static final RichSupplier EMPTY = new RichSupplier();

    @Override
    public Response<Boolean> fullDump() {

        Response<Boolean> result = new Response<Boolean>();

        log.info("[SUPPLIER_FULL_DUMP] supplier full dump start");
        Stopwatch stopwatch = Stopwatch.createStarted();
        try {
            Long lastId = userDao.maxSupplierId() + 1;
            int returnSize = PAGE_SIZE;
            int handle = 0;
            while (returnSize == PAGE_SIZE) {
                List<User> users = userDao.forDump(lastId, PAGE_SIZE);

                if (!users.isEmpty()) {

                    final List<Long> invalidateIds = Lists.newArrayList();
                    Iterable<RichSupplier> richSuppliers = filterSupplier(users, invalidateIds);

                    esClient.index(SUPPLIER_INDEX_NAME, SUPPLIER_INDEX_TYPE, richSuppliers);
                    esClient.delete(SUPPLIER_INDEX_NAME, SUPPLIER_INDEX_TYPE, invalidateIds);

                    returnSize = users.size();
                    handle += returnSize;
                    lastId = users.get(users.size() - 1).getId();
                    log.info("has index {} supplier, last handled is {}", handle, lastId);
                } else {
                    break;
                }
            }

            stopwatch.stop();
            log.info("[SUPPLIER_FULL_DUMP] supplier full dump end, token {} ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));

            result.setResult(Boolean.TRUE);
            return result;

        } catch (Exception e) {
            log.error("fail to full dump supplier, cause:{]", e);
            result.setError("supplier.full.dump.fail");
            return result;
        }
    }

    @Override
    public Response<Boolean> deltaDump(int interval) {

        Response<Boolean> result = new Response<Boolean>();

        log.info("[SUPPLIER_DELTA_DUMP] supplier delta dump start");
        Stopwatch stopwatch = Stopwatch.createStarted();

        try {
            Long lastId = userDao.maxSupplierId() + 1;
            String compared = FORMATTER.print(DateTime.now().minusMinutes(interval));
            int handle = 0;
            int returnSize = PAGE_SIZE;

            while (returnSize == PAGE_SIZE) {
                List<User> users = userDao.forDeltaDump(lastId, compared, PAGE_SIZE);

                if (!users.isEmpty()) {
                    final List<Long> invalidateIds = Lists.newArrayList();
                    Iterable<RichSupplier> richSuppliers = filterSupplier(users, invalidateIds);

                    esClient.index(SUPPLIER_INDEX_NAME, SUPPLIER_INDEX_TYPE, richSuppliers);
                    esClient.delete(SUPPLIER_INDEX_NAME, SUPPLIER_INDEX_TYPE, invalidateIds);

                    returnSize = users.size();
                    lastId = users.get(returnSize - 1).getId();
                    handle += users.size();

                    log.info("has index {} suppliers, last handled is {}", handle, lastId);
                } else {
                    break;
                }
            }

            stopwatch.stop();
            log.info("[SUPPLIER_DELTA_DUMP] supplier delta dump end, token {} ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));

            result.setResult(Boolean.TRUE);
            return result;

        } catch (Exception e) {
            log.error("fail to delta dump supplier interval={}, currentTime={}, cause:{}",
                    interval, DateTime.now(), Throwables.getStackTraceAsString(e));
            result.setError("supplier.delta.dump.fail");
            return result;
        }
    }

    @Override
    public Response<Boolean> realTimeIndex(List<Long> ids, User.SearchStatus status) {
        Response<Boolean> result = new Response<Boolean>();

        if (ids == null || ids.isEmpty() || status == null) {
            log.error("ids and status both can not be null when realTime index supplier");
            result.setError("illegal.params");
            return result;
        }

        try {
            for (Long id : ids) {
                try {
                    switch (status) {
                        case DELETE:
                            RichSupplier supplier = new RichSupplier();
                            supplier.setId(id);
                            searchExecutor.submit(SUPPLIER_INDEX_NAME, SUPPLIER_INDEX_TYPE, supplier, SearchExecutor.OP_TYPE.DELETE);
                            break;
                        case INDEX:
                            User user = userDao.findById(id);
                            RichSupplier richSupplier = richSupplierFactory.make(user);
                            searchExecutor.submit(SUPPLIER_INDEX_NAME, SUPPLIER_INDEX_TYPE, richSupplier, SearchExecutor.OP_TYPE.INDEX);
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    log.error("fail to realTime index supplier id={}, status={}, cause:{}, skip it",
                            id, status, Throwables.getStackTraceAsString(e));
                }
            }

            result.setResult(Boolean.TRUE);
            return result;
        } catch (Exception e) {
            log.error("fail to realTime index supplier by ids={}, status={}, cause:{}",
                    ids, status, Throwables.getStackTraceAsString(e));
            result.setError("supplier.realTime.dump.fail");
            return result;
        }
    }

    //搜索中只应该出现类型为供应商，审核状态为审核通过的用户
    private Iterable<RichSupplier> filterSupplier(List<User> suppliers, final List<Long> invalidateIds) {
        return FluentIterable.from(suppliers).transform(new Function<User, RichSupplier>() {
            @Override
            public RichSupplier apply(User input) {
                try {
                    return richSupplierFactory.make(input);
                } catch (Exception e) {
                    log.error("fail to convert user {} to richSupplier, cause:{}, skip it",
                            input, Throwables.getStackTraceAsString(e));
                    invalidateIds.add(input.getId());
                    return EMPTY;
                }
            }
        }).filter(new Predicate<RichSupplier>() {
            @Override
            public boolean apply(RichSupplier input) {
                return input.getId() != null;
            }
        });
    }
}
