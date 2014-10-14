package io.terminus.snz.requirement.dao;

import com.google.common.collect.Lists;
import io.terminus.snz.requirement.model.RequirementRank;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Desc:供应商排名测试
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-03.
 */
public class RequirementRankDaoTest extends BasicTest {
    @Autowired
    private RequirementRankDao requirementRankDao;

    @Test
    public void createTest(){
        requirementRankDao.create(mock());
    }

    @Test
    public void testCreateBatch(){
        List<RequirementRank> rankList = Lists.newArrayList();
        rankList.add(mock());
        rankList.add(mock());
        requirementRankDao.createBatch(rankList);
    }

    @Test
    public void updateTest(){
        RequirementRank requirementRank = mock();
        requirementRank.setId(1l);
        requirementRank.setSupplierName("zero");

        requirementRankDao.update(requirementRank);
    }

    @Test
    public void findByIdTest(){
        assertThat(requirementRankDao.findById(1l), notNullValue());
    }

    @Test
    public void testFindAllRanks(){
        requirementRankDao.findAllRanks(1l , 1);
    }

    private RequirementRank mock(){
        RequirementRank requirementRank = new RequirementRank();
        requirementRank.setRequirementId(1l);
        requirementRank.setRank(1);
        requirementRank.setType(1);
        requirementRank.setSupplierId(1l);
        requirementRank.setSupplierName("Michael");
        requirementRank.setQuotaScale(50);

        return requirementRank;
    }
}
