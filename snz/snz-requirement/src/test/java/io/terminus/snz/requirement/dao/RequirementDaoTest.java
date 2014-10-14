package io.terminus.snz.requirement.dao;

import com.google.common.collect.Maps;
import io.terminus.snz.requirement.manager.CountManager;
import io.terminus.snz.requirement.model.Requirement;
import io.terminus.snz.requirement.model.RequirementStatus;
import org.apache.ibatis.session.SqlSession;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.mockito.Mockito.when;

/**
 * Desc:需求测试对象
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-02.
 */
public class RequirementDaoTest {
    @Mock
    private SqlSession sqlSession;

    @Mock
    private CountManager countManager;

    @InjectMocks
    private RequirementDao requirementDao;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createTest(){
        requirementDao.create(mock());
    }

    @Test
    public void updateTest(){
        //模拟审核操作
        Requirement requirement = mock();
        requirement.setId(1l);
        requirement.setCheckResult(1);
        requirement.setCheckId(1l);
        requirement.setCheckName("Michael");
        requirement.setStatus(1);

        requirementDao.update(requirement);
    }

    @Test
    public void findByIdTest(){
        when(countManager.getModuleTotal(1l)).thenReturn(1);
        when(countManager.getModuleNum(1l)).thenReturn(1);

        when(sqlSession.selectOne("Requirement.findById", 1l)).thenReturn(mock());
        requirementDao.findById(1l);
    }

    @Test
    public void findByNameTest(){
        requirementDao.findByName(1l , "Michael");
    }

    @Test
    public void testFindByParams(){
        Map<String , Object> params = Maps.newHashMap();
        params.put("offset" , 0);
        params.put("size" , 20);
        params.put("sendTime", "desc");

        when(countManager.getModuleTotal(1l)).thenReturn(1);
        when(countManager.getModuleNum(1l)).thenReturn(1);

        when(sqlSession.selectOne("Requirement.findRequirementCount", params)).thenReturn(0l);
        requirementDao.findByParams(1l, params);

        when(sqlSession.selectOne("Requirement.findRequirementCount", params)).thenReturn(1l);
        when(sqlSession.selectOne("Requirement.findByParams", params)).thenReturn(mock());
        requirementDao.findByParams(1l, params);
    }

    @Test
    public void testFindReqByTeam(){
        Map<String , Object> params = Maps.newHashMap();
        params.put("offset" , 0);
        params.put("size" , 20);
        params.put("userId" , 1l);
        params.put("createTime", "desc");

        when(countManager.getModuleTotal(1l)).thenReturn(1);
        when(countManager.getModuleNum(1l)).thenReturn(1);
        when(sqlSession.selectOne("Requirement.findReqCountByTeam", params)).thenReturn(0l);
        requirementDao.findReqByTeam(1l , params);
    }

    @Test
    public void testFindBySupplier(){
        Map<String , Object> params = Maps.newHashMap();
        params.put("offset" , 0);
        params.put("size" , 20);
        when(sqlSession.selectOne("Requirement.findCountBySupplier", params)).thenReturn(0l);
        requirementDao.findBySupplier(1l, params);
    }

    @Test
    public void testFindAllExcellence(){
        requirementDao.findAllExcellence();
    }

    @Test
    public void testFindReqByStatus(){
        requirementDao.findReqByStatus(RequirementStatus.TENDER_END.value());
    }

    @Test
    public void testFindAllRequirements(){
        requirementDao.findAllRequirements(4 , null);
    }

    private Requirement mock(){
        Requirement requirement = new Requirement();
        requirement.setName("test");
        requirement.setPurchaserId(1l);
        requirement.setPurchaserName("purchaserName");
        requirement.setSeriesIds("{sids:[{id:1,name:冰箱把手},{id:21,name:冰箱门}]}");
        requirement.setCoinType(1);
        requirement.setMaterielType(1l);
        requirement.setModuleType(1);
        requirement.setDeliveryAddress("{ad:[{pa:101,fa:10},{pa:101,fa:20}]}");
        requirement.setDescription("describe");
        requirement.setAccessories("{file:[url1,url2]}");
        requirement.setSelectNum(3);
        requirement.setReplaceNum(2);
        requirement.setCompanyScope("[{id:10,name:AGH}]");
        requirement.setTacticsId(1);
        requirement.setHeadDrop("引领点");
        requirement.setModuleNum(10);
        requirement.setModuleTotal(10000);
        requirement.setCheckResult(0);
        requirement.setCreatorId(1l);
        requirement.setCreatorName("Michael");
        requirement.setCreatorPhone("18657327206");
        requirement.setCreatorEmail("MichaelZhaoZero@gmail.com");

        return requirement;
    }
}
