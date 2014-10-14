package io.terminus.snz.user.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import io.terminus.common.utils.Splitters;
import io.terminus.pampas.common.Response;
import io.terminus.snz.category.model.FrontendCategory;
import io.terminus.snz.category.service.FrontendCategoryService;
import io.terminus.snz.user.dao.CompanyDao;
import io.terminus.snz.user.dao.CompanyMainBusinessDao;
import io.terminus.snz.user.dto.RichSupplier;
import io.terminus.snz.user.model.Company;
import io.terminus.snz.user.model.CompanyMainBusiness;
import io.terminus.snz.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangzefeng on 14-6-24
 */
@Component
@Slf4j
public class RichSuppliers {

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private FrontendCategoryService frontendCategoryService;

    @Autowired
    private CompanyMainBusinessDao companyMainBusinessDao;

    public RichSupplier make(User supplier) {
        RichSupplier richSupplier = new RichSupplier();
        richSupplier.setId(supplier.getId());
        richSupplier.setMobile(supplier.getMobile());
        richSupplier.setCreatedAt(supplier.getCreatedAt());
        richSupplier.setType(supplier.getType());
        richSupplier.setApproveStatus(supplier.getApproveStatus());
        richSupplier.setStatus(supplier.getStatus());

        Iterable<String> splitterTags = null;
        if (!Strings.isNullOrEmpty(supplier.getTags())) {
            splitterTags = Splitters.COMMA.split(supplier.getTags());
        }

        richSupplier.setTags(splitterTags);

        //找到供应商对应公司
        Company company = companyDao.findByUserId(supplier.getId());
        richSupplier.setCompanyName(company.getCorporation());
        richSupplier.setCorpAddr(company.getCorpAddr());
        richSupplier.setParticipateCount(company.getParticipateCount());
        richSupplier.setRegCapital(company.getRegCapital());
        richSupplier.setRcCoinType(company.getRcCoinType());

        //找到所有主营业务
        List<CompanyMainBusiness> businesses = companyMainBusinessDao.findByCompanyId(company.getId());

        List<Long> businessIds = new ArrayList<Long>();
        for(CompanyMainBusiness cmb : businesses) {
            businessIds.add(cmb.getMainBusinessId());
        }

        Response<List<FrontendCategory>> frontendCategoryR = frontendCategoryService.findByIds(businessIds);
        if(!frontendCategoryR.isSuccess()) {
            log.error("fail to find frontendCategory by ids={}, error code:{}",
                    businessIds, frontendCategoryR.getError());
        }

        List<FrontendCategory> thirdLevels = frontendCategoryR.getResult();

        List<Long> secondLevelIds = Lists.newArrayListWithCapacity(businessIds.size());

        List<Long> firstLevelIds = Lists.newArrayListWithCapacity(businessIds.size());

        for(FrontendCategory fc : thirdLevels) {

            Response<List<FrontendCategory>> ancestorsR = frontendCategoryService.ancestorsOf(fc.getId());
            if(!ancestorsR.isSuccess()) {
                log.error("fail to find ancestor of frontendCategory id={}, error code={}",
                        fc.getId(), ancestorsR.getError());
            }
            List<FrontendCategory> ancestors = ancestorsR.getResult();
            secondLevelIds.add(ancestors.get(1).getId());

            firstLevelIds.add(ancestors.get(2).getId());
        }

        richSupplier.setMainBusinessIds(businessIds);

        richSupplier.setSecondIds(secondLevelIds);

        richSupplier.setFirstIds(firstLevelIds);

        return richSupplier;
    }
}
