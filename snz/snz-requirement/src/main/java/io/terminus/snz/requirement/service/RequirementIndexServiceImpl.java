package io.terminus.snz.requirement.service;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Stopwatch;
import com.google.common.base.Throwables;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import io.terminus.pampas.common.Response;
import io.terminus.search.ESClient;
import io.terminus.search.endpoint.SearchExecutor;
import io.terminus.snz.requirement.dao.RequirementDao;
import io.terminus.snz.requirement.dto.RichRequirement;
import io.terminus.snz.requirement.manager.CountManager;
import io.terminus.snz.requirement.model.Requirement;
import io.terminus.snz.requirement.model.RequirementStatus;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by yangzefeng on 14-6-23
 */
@Service
@Slf4j
public class RequirementIndexServiceImpl implements RequirementIndexService {

    @Autowired
    private RequirementDao requirementDao;

    @Autowired
    private ESClient esClient;

    @Autowired
    private SearchExecutor searchExecutor;

    @Autowired
    private CountManager countManager;

    @Autowired
    private RichRequirements richRequirementsFactory;

    private static final Integer PAGE_SIZE = 200;

    private static final String REQUIREMENT_INDEX_NAME = "requirements";

    private static final String REQUIREMENT_INDEX_TYPE = "requirement";

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    private static final RichRequirement EMPTY = new RichRequirement();

    @Override
    public Response<Boolean> fullDump() {
        Response<Boolean> result = new Response<Boolean>();

        log.info("[REQUIREMENT-FULL-DUMP] full requirement dump start");
        Stopwatch stopwatch = Stopwatch.createStarted();

        try {
            Long lastId = requirementDao.maxId() + 1;
            int returnSize = PAGE_SIZE;
            int handled = 0;
            while (returnSize == PAGE_SIZE) {
                log.info("lastId = {}, returnSize={}",lastId, returnSize);

                List<Requirement> requirements = requirementDao.forDump(lastId, PAGE_SIZE);

                if (!requirements.isEmpty()) {
                    final List<Long> invalidateIds = Lists.newArrayList();

                    Iterable<RichRequirement> filterRequirements = filterRequirement(requirements, invalidateIds);
                    esClient.index(REQUIREMENT_INDEX_NAME, REQUIREMENT_INDEX_TYPE, filterRequirements);
                    esClient.delete(REQUIREMENT_INDEX_NAME, REQUIREMENT_INDEX_TYPE, invalidateIds);

                    lastId = requirements.get(requirements.size() - 1).getId();
                    returnSize = requirements.size();
                    handled += returnSize;
                    log.info("has index {} requirement, last handled id is {}", handled, lastId);
                } else {
                    break;
                }
            }

            stopwatch.stop();
            log.info("[REQUIREMENT-FULL-DUMP] full requirement dump end, token {} ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));

            result.setResult(Boolean.TRUE);
            return result;
        }catch (Exception e) {
            log.error("fail to full dump requirement cause:{}", e);
            result.setError("full.dump.requirement.fail");
            return result;
        }
    }

    @Override
    public Response<Boolean> deltaDump(int intervel) {
        Response<Boolean> result = new Response<Boolean>();

        log.info("[DELTA_REQUIREMENT_DUMP] delta dump requirement start");
        Stopwatch stopwatch = Stopwatch.createStarted();

        try {
            Long lastId = requirementDao.maxId() + 1;
            int returnSize = PAGE_SIZE;
            int handle = 0;
            String compared = DATE_TIME_FORMATTER.print(DateTime.now().minusMinutes(intervel));

            while (returnSize == PAGE_SIZE) {

                List<Requirement> requirements = requirementDao.forDeltaDump(lastId, compared, PAGE_SIZE);

                if (!requirements.isEmpty()) {
                    final List<Long> invalidateIds = Lists.newArrayList();

                    Iterable<RichRequirement> filterRequirements = filterRequirement(requirements, invalidateIds);

                    esClient.index(REQUIREMENT_INDEX_NAME, REQUIREMENT_INDEX_TYPE, filterRequirements);
                    esClient.delete(REQUIREMENT_INDEX_NAME, REQUIREMENT_INDEX_TYPE, invalidateIds);

                    returnSize = requirements.size();
                    lastId = requirements.get(returnSize - 1).getId();
                    handle += returnSize;

                    log.info("has index {} requirements, and last handled id is {}", handle, lastId);
                } else {
                    break;
                }

            }

            stopwatch.stop();
            log.info("[DELTA_REQUIREMENT_DUMP] delta dump requirement end, token {} ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));

            result.setResult(Boolean.TRUE);
            return result;
        }catch (Exception e) {
            log.error("fail to delta dump requirement cause:{}", e);
            result.setError("delta.dump.requirement.fail");
            return result;
        }
    }

    @Override
    public Response<Boolean> realTimeIndex(Long id, Requirement.SearchStatus status) {
        Response<Boolean> result = new Response<Boolean>();
        if(id == null) {
            log.error("id can not be null when find requirement");
            result.setError("illegal.params");
            return result;
        }
        try {
            switch (status) {
                case DELETE:
                    RichRequirement rd = new RichRequirement();
                    rd.setId(id);
                    searchExecutor.submit(REQUIREMENT_INDEX_NAME, REQUIREMENT_INDEX_TYPE, rd, SearchExecutor.OP_TYPE.DELETE);
                    break;
                case INDEX:
                    Requirement r = requirementDao.findById(id);
                    //写入模块的统计数据(当阶段还未到需求锁定则从redis获取统计数据)
                    if(r.getStatus() < RequirementStatus.SOL_INTERACTIVE.value()){
                        r.setModuleTotal(countManager.getModuleTotal(id));
                        r.setModuleNum(countManager.getModuleNum(id));
                    }
                    RichRequirement ri = richRequirementsFactory.make(r);
                    searchExecutor.submit(REQUIREMENT_INDEX_NAME, REQUIREMENT_INDEX_TYPE, ri, SearchExecutor.OP_TYPE.INDEX);
                    break;
                default:
                    break;
            }

            result.setResult(Boolean.TRUE);
            return result;
        }catch (Exception e) {
            log.error("fail to realTime index requirement id={}, cause:{}", id, Throwables.getStackTraceAsString(e));
            result.setError("requirement.realTime.index.fail");
            return result;
        }
    }

    private Iterable<RichRequirement> filterRequirement(List<Requirement> requirements, final List<Long> invalidateIds) {
        return FluentIterable.from(requirements).filter(new Predicate<Requirement>() {
            @Override
            public boolean apply(Requirement input) {
                return input.getStatus() != null && input.getStatus() != -1;  //过滤状态为删除的需求
            }
        }).transform(new Function<Requirement, RichRequirement>() {
            @Override
            public RichRequirement apply(Requirement input) {
                try {
                    return richRequirementsFactory.make(input);
                }catch (Exception e) {
                    log.error("fail to convert requirement {} to richRequirement, cause:{}, skip it",
                            input, Throwables.getStackTraceAsString(e));
                    invalidateIds.add(input.getId());
                    return EMPTY;
                }
            }
        }).filter(new Predicate<RichRequirement>() {
            @Override
            public boolean apply(RichRequirement input) {
                return input.getId() != null;
            }
        });
    }
}
