package io.terminus.snz.user.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import io.terminus.common.model.Paging;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.dao.CompanyDao;
import io.terminus.snz.user.dao.UserComplaintDao;
import io.terminus.snz.user.model.Company;
import io.terminus.snz.user.model.UserComplaint;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.List;
import java.util.HashMap;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.when;

/**
 * @author wanggen 2014-08-13 15:23:32
 */
public class UserComplaintServiceImplTest {

    @InjectMocks
    private UserComplaintServiceImpl userComplaintService;

    @Mock
    private UserComplaintDao userComplaintDao;

    @Mock
    private CompanyDao companyDao;

    @Mock
    private UserComplaint userComplaint;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        userComplaint = new UserComplaint();
        userComplaint.setId(1l); //自增主键
        userComplaint.setUserId(1l); //抱怨人ID
        userComplaint.setUserName("抱怨人-1"); //抱怨人
        userComplaint.setProductLineId(1); //产品线ID
        userComplaint.setProductLineName("产品线名称:后台二级类目-1"); //产品线名称:后台二级类目
        userComplaint.setFactoryNum("1"); //生产工厂编号
        userComplaint.setFactoryName("工厂名称-1"); //工厂名称
        userComplaint.setProductOwnerId(1l); //产品负责人ID
        userComplaint.setProductOwnerName("产品负责人-1"); //产品负责人
        userComplaint.setComplaintTypes("抱怨类型(模块包装运输协议|(T-1,停产,欠产,拒单,延单))-1"); //抱怨类型(模块包装运输协议|(T-1,停产,欠产,拒单,延单))
        userComplaint.setSupplierCode("供应商CODE-1"); //供应商CODE
        userComplaint.setSupplierName("供应商名称-1"); //供应商名称
        userComplaint.setModuleNum("1l"); //模块号
        userComplaint.setModuleName("模块名称-1"); //模块名称
        userComplaint.setComplaintContent("抱怨回馈内容-1"); //抱怨回馈内容
        userComplaint.setComplaintReason("抱怨原因-1"); //抱怨原因
        userComplaint.setCreatedAt(DateTime.now().toDate()); //创建时间
        userComplaint.setUpdatedAt(DateTime.now().toDate()); //更新时间
    }

    @Test
    public void testCreate() throws Exception {

        // Should be successfully
        when(userComplaintDao.create(userComplaint)).thenReturn(1L);
        Response<Long> response = userComplaintService.create(userComplaint);
        Assert.assertTrue("The new created id must be:[1]", response.getResult()==1);

        // Should be interrupted
        when(userComplaintDao.create(Matchers.<UserComplaint>anyObject())).thenThrow(new RuntimeException("Interruption"));
        response = userComplaintService.create(userComplaint);
        Assert.assertTrue("This test must by error", response.getError()!=null);

        // Should be fast failed
        response = userComplaintService.create(null);
        Assert.assertTrue("This test must by error", response.getError()!=null);

    }


    @Test
    public void testFindById() throws Exception {

        // Should be successfully
        when(userComplaintDao.findById(1L)).thenReturn(userComplaint);
        Response<UserComplaint> findByIdResp = userComplaintService.findById(1L);
        Assert.assertTrue("The found object's id must be 1" ,
                          findByIdResp.getResult().getId()==1);

        // Should be interrupted
        when(userComplaintDao.findById(Matchers.<Long>anyObject())).thenThrow(new RuntimeException("Interruption"));
        findByIdResp = userComplaintService.findById(1L);
        Assert.assertTrue("This test must by error", findByIdResp.getError()!=null);

        // Should be fast failed
        findByIdResp = userComplaintService.findById(null);
        Assert.assertTrue("This test must by error", findByIdResp.getError()!=null);

    }


    @Test
    public void testFindByPaging() throws Exception {

        Paging<UserComplaint> paging = new Paging<UserComplaint>();
        paging.setTotal(1l);
        paging.setData(Lists.newArrayList(userComplaint));

        BaseUser baseUser = new BaseUser(2954l, "haier", 2);
        // Should be successfully
        when(userComplaintDao.findByPaging(anyMap())).thenReturn(paging);
        
//        Response<Paging<UserComplaint>> findByPagingResp = userComplaintService.findByPaging(
//                baseUser,
//                new HashMap<String, Object>(){{
//                    put("id", 1l);
//                }}, 1, 1);
//        Assert.assertTrue("The found records must be more than one", findByPagingResp.getResult().getData().size()>=1);
//
//        // Should be interrupted
//        when(companyDao.findByUserId(anyLong())).thenReturn(new Company());
//        when(userComplaintDao.findByPaging(anyMap())).thenThrow(new RuntimeException("Interruption"));
//        findByPagingResp=userComplaintService.findByPaging(
//                baseUser,
//                new HashMap<String, Object>(){{
//                put("id",1l);
//            }},1,1);
//        Assert.assertTrue("This test must by error", findByPagingResp.getError()!=null);
//
//        // Should be fast failed
//        findByPagingResp=userComplaintService.findByPaging(baseUser, null,1,1);
//        Assert.assertTrue("This test must by error", findByPagingResp.getError()!=null);

    }


    @Test
    public void testFindAllBy() throws Exception {

        List<UserComplaint> userComplaints = Lists.newArrayList(userComplaint);
        BaseUser baseUser = new BaseUser(2954l, "haier", 2);
        // Should be successfully
        when(companyDao.findByUserId(anyLong())).thenReturn(new Company());
        when(userComplaintDao.findAllBy(anyMap())).thenReturn(userComplaints);
        Response<List<UserComplaint>> allByResp = userComplaintService.findAllBy(
                baseUser,
                ImmutableMap.<String, Object>of("id", 1l));
        Assert.assertTrue("The found records must be more than one",
                allByResp.getResult().size()>=1);

        // Should be interrupted
        when(userComplaintDao.findAllBy(anyMap())).thenThrow(new RuntimeException("Interruption"));
        allByResp = userComplaintService.findAllBy(
                baseUser,
                ImmutableMap.<String, Object>of("id", 1l));
        Assert.assertTrue("This test must by error", allByResp.getError()!=null);

        // Should be fast failed
        allByResp = userComplaintService.findAllBy(baseUser, null);
        Assert.assertTrue("This test must by error", allByResp.getError()!=null);

    }

    @Test
    public void testFindByUserId(){

        Long userId = 1l; //抱怨人ID
        Response<List<UserComplaint>> resp;
        List<UserComplaint> userComplaints = Lists.newArrayList(userComplaint);

        // Should be successfully
        when(userComplaintDao.findByUserId(userId)).thenReturn(userComplaints);
        resp = userComplaintService.findByUserId(userId);
        Assert.assertTrue("At least one record must be found", resp.getResult().size()>=1);

        // Should be interrupted
        when(userComplaintDao.findByUserId(anyLong())).thenThrow(new RuntimeException("Interruption"));
        resp = userComplaintService.findByUserId(userId);
        Assert.assertTrue("Should be interrupted", resp.getError()!=null);

    }

    @Test
    public void testUpdate() throws Exception {

        // Should be successfully
        when(userComplaintDao.update(userComplaint)).thenReturn(1);
        userComplaint.setId(1l); //自增主键
        userComplaint.setUserId(1l); //抱怨人ID
        userComplaint.setUserName("抱怨人-1"); //抱怨人
        userComplaint.setProductLineId(1); //产品线ID
        userComplaint.setProductLineName("产品线名称:后台二级类目-1"); //产品线名称:后台二级类目
        userComplaint.setFactoryNum("1"); //生产工厂编号
        userComplaint.setFactoryName("工厂名称-1"); //工厂名称
        userComplaint.setProductOwnerId(1l); //产品负责人ID
        userComplaint.setProductOwnerName("产品负责人-1"); //产品负责人
        userComplaint.setComplaintTypes("抱怨类型(模块包装运输协议|(T-1,停产,欠产,拒单,延单))-1"); //抱怨类型(模块包装运输协议|(T-1,停产,欠产,拒单,延单))
        userComplaint.setSupplierCode("供应商CODE-1"); //供应商CODE
        userComplaint.setSupplierName("供应商名称-1"); //供应商名称
        userComplaint.setModuleNum("1l"); //模块号
        userComplaint.setModuleName("模块名称-1"); //模块名称
        userComplaint.setComplaintContent("抱怨回馈内容-1"); //抱怨回馈内容
        userComplaint.setComplaintReason("抱怨原因-1"); //抱怨原因
        userComplaint.setCreatedAt(DateTime.now().toDate()); //创建时间
        userComplaint.setUpdatedAt(DateTime.now().toDate()); //更新时间
        Response<Integer> updateResp = userComplaintService.update(userComplaint);
        Assert.assertTrue("At least one record must be updated", updateResp.getResult()==1);

        // Should be interrupted
        when(userComplaintDao.update(Matchers.<UserComplaint>anyObject())).thenThrow(new RuntimeException("Interruption"));
        updateResp = userComplaintService.update(userComplaint);
        Assert.assertTrue("This test must by error", updateResp.getError()!=null);

        // Should be fast failed
        updateResp = userComplaintService.update(null);
        Assert.assertTrue("This test must by error", updateResp.getError()!=null);

    }

    @Test
    public void testDeleteByIds() throws Exception {

        // Should be successfully
        when(userComplaintDao.deleteByIds(Lists.newArrayList(1l))).thenReturn(1);
        Response<Integer> deleteResponse = userComplaintService.deleteByIds(Lists.newArrayList(1l));
        Assert.assertTrue("At least one record must be deleted", deleteResponse.getResult()==1);

        // Should be interrupted
        when(userComplaintDao.deleteByIds(Matchers.<List>anyObject())).thenThrow(new RuntimeException("Interruption"));
        deleteResponse = userComplaintService.deleteByIds(Lists.newArrayList(1l));
        Assert.assertTrue("This test must by error", deleteResponse.getError()!=null);

        // Should be fast failed
        deleteResponse = userComplaintService.deleteByIds(null);
        Assert.assertTrue("This test must by error", deleteResponse.getError()!=null);

    }
}
