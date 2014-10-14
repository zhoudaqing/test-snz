package io.terminus.snz.user.dao;

import com.google.common.collect.ImmutableList;
import io.terminus.common.model.Paging;
import io.terminus.snz.user.model.UserComplaint;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对 `snz_user_complaints` CRUD 测试<P>
 * created by wanggen 2014-08-13 14:34:57
 */
public class UserComplaintDaoTest extends TestBaseDao {

    @Autowired
    private UserComplaintDao userComplaintDao;

    private Long createdId = null;

    private UserComplaint userComplaint = null;

    @Before
    public void setUp() throws Exception {
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
        createdId = userComplaintDao.create(userComplaint);
    }

    @Test
    public void testCreate() throws Exception {
        createdId = userComplaintDao.create(userComplaint);
        Assert.assertTrue("One record must be created", createdId>=1);
    }

    @Test
    public void testFindById() throws Exception {
        UserComplaint byId = userComplaintDao.findById(createdId);
        Assert.assertTrue("The requery result's id must equals:"+createdId, byId!=null && byId.getId().equals(createdId));
    }


    @Test
    public void testFindByPaging() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>(){
            {
                put("id", 1l); //自增主键
                put("userId", 1l); //抱怨人ID
                put("userName", "抱怨人-1"); //抱怨人
                put("productLineId", 1); //产品线ID
                put("productLineName", "产品线名称:后台二级类目-1"); //产品线名称:后台二级类目
                put("factoryNum", 1); //生产工厂编号
                put("factoryName", "工厂名称-1"); //工厂名称
                put("productOwnerId", 1l); //产品负责人ID
                put("productOwnerName", "产品负责人-1"); //产品负责人
                put("complaintTypes", "抱怨类型(模块包装运输协议|(T-1,停产,欠产,拒单,延单))-1"); //抱怨类型(模块包装运输协议|(T-1,停产,欠产,拒单,延单))
                put("supplierCode", "供应商CODE-1"); //供应商CODE
                put("supplierName", "供应商名称-1"); //供应商名称
                put("moduleNum", "1l"); //模块号
                put("moduleName", "模块名称-1"); //模块名称
                put("complaintContent", "抱怨回馈内容-1"); //抱怨回馈内容
                put("complaintReason", "抱怨原因-1"); //抱怨原因
                put("createdAt", DateTime.now().toDate()); //创建时间
                put("updatedAt", DateTime.now().toDate()); //更新时间
                put("offset",0);
                put("limit",10000);
            }
        };
        Paging<UserComplaint> byPaging = userComplaintDao.findByPaging(param);
        Assert.assertTrue("The result must like:{} ",
                byPaging.getData()!=null && byPaging.getData().size()>=1);
    }

    @Test
    public void testFindAllBy() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>(){
            {
                put("id", 1l); //自增主键
                put("userId", 1l); //抱怨人ID
                put("userName", "抱怨人-1"); //抱怨人
                put("productLineId", 1); //产品线ID
                put("productLineName", "产品线名称:后台二级类目-1"); //产品线名称:后台二级类目
                put("factoryNum", 1); //生产工厂编号
                put("factoryName", "工厂名称-1"); //工厂名称
                put("productOwnerId", 1l); //产品负责人ID
                put("productOwnerName", "产品负责人-1"); //产品负责人
            }
        };
        List<UserComplaint> allBy = userComplaintDao.findAllBy(param);
        Assert.assertTrue("The result must like:{}", allBy.size()>=0);
    }

    @Test
    public void testFindByUserId(){

        Long userId = 1l; //抱怨人ID

        List<UserComplaint> userComplaintList = userComplaintDao.findByUserId(userId);

        Assert.assertTrue("Must ensure that one record be found", userComplaintList!=null && userComplaintList.size()==1);

    }


    @Test
    public void testUpdate() throws Exception {
        UserComplaint userComplaint = new UserComplaint();
        userComplaint.setId(createdId);
        userComplaint.setUserId(1001l); //抱怨人ID
        userComplaint.setUserName("抱怨人-1-new"); //抱怨人
        userComplaint.setProductLineId(1001); //产品线ID
        userComplaint.setProductLineName("产品线名称:后台二级类目-1-new"); //产品线名称:后台二级类目
        userComplaint.setFrontendCategoryId(1001l); //模块:前台一级类目
        userComplaint.setFrontendCategoryName("模块-前台一级类目-1-new"); //模块-前台一级类目
        userComplaint.setFactoryNum("1001"); //生产工厂编号
        userComplaint.setFactoryName("工厂名称-1-new"); //工厂名称
        userComplaint.setProductOwnerId(1001l); //产品负责人ID
        userComplaint.setProductOwnerName("产品负责人-1-new"); //产品负责人
        userComplaint.setComplaintTypes("抱怨类型(模块包装运输协议|(T-1,停产,欠产,拒单,延单))-1-new"); //抱怨类型(模块包装运输协议|(T-1,停产,欠产,拒单,延单))
        userComplaint.setSupplierCode("供应商CODE-1-new"); //供应商CODE
        userComplaint.setSupplierName("供应商名称-1-new"); //供应商名称
        userComplaint.setModuleNum("1001l"); //模块号
        userComplaint.setModuleName("模块名称-1-new"); //模块名称
        userComplaint.setComplaintContent("抱怨回馈内容-1-new"); //抱怨回馈内容
        userComplaint.setComplaintReason("抱怨原因-1-new"); //抱怨原因
        userComplaint.setCreatedAt(DateTime.now().toDate()); //创建时间
        userComplaint.setUpdatedAt(DateTime.now().toDate()); //更新时间
        int updated = userComplaintDao.update(userComplaint);
        Assert.assertTrue("One record must be updated", updated==1);
    }


    @Test
    public void testDeleteByIds() throws Exception {
        int deleted = userComplaintDao.deleteByIds(ImmutableList.of(createdId));
        Assert.assertTrue("One record must be deleted", deleted==1);
    }

}