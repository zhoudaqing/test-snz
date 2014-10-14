package io.terminus.snz.requirement.dao;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import io.terminus.snz.requirement.model.ModuleQuotation;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Desc:模块报价方案测试
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-06-10.
 */
public class ModuleQuotationDaoTest extends BasicTest {
    @Autowired
    private ModuleQuotationDao moduleQuotationDao;

    @Test
    public void createTest(){
        moduleQuotationDao.create(mock());
    }

    @Test
    public void createBatch(){
        List<ModuleQuotation> quotations = Lists.newArrayList();
        quotations.add(mock());
        quotations.add(mock());
        quotations.add(mock());
        quotations.add(mock());
        moduleQuotationDao.createBatch(quotations);
    }

    @Test
    public void updateTest(){
        ModuleQuotation quotation = mock();
        quotation.setId(1l);

        moduleQuotationDao.update(quotation);
    }

    @Test
    public void findByIdTest(){
        assertThat(moduleQuotationDao.findById(1l), notNullValue());
    }

    @Test
    public void testCountBySolutionId(){
        System.out.println(moduleQuotationDao.countBySolutionId(1l));
    }

    @Test
    public void testFindAllQuotations(){
        //排序方式
        Ordering<ModuleQuotation> byPrice = new Ordering<ModuleQuotation>() {
            @Override
            public int compare( ModuleQuotation left, ModuleQuotation right) {
                return left.getPrice() - right.getPrice();
            }
        };

        List<ModuleQuotation> quotationList = moduleQuotationDao.findAllQuotations(1l);
        List<ModuleQuotation> testQuotation = byPrice.immutableSortedCopy(quotationList);
        for(ModuleQuotation quotation : testQuotation){
            System.out.println(quotation.getPrice());
        }
    }

    @Test
    public void testDeleteBySolutionId(){
        moduleQuotationDao.deleteBySolutionId(1l);
    }

    @Test
    public void testFindExist(){
        moduleQuotationDao.findExist(1l , 1l);
    }

    @Test
    public void testFindByTransact(){
        Map<String , Object> params = Maps.newHashMap();
        params.put("offset" , 0);
        params.put("limit" , 20);
        params.put("queryType" , 2);
        moduleQuotationDao.findByTransact(Lists.newArrayList(1l , 2l) , params);
    }

    private ModuleQuotation mock(){
        ModuleQuotation quotation = new ModuleQuotation();
        quotation.setSolutionId(1l);
        quotation.setModuleId(1l);
        quotation.setModuleName("moduleName");
        quotation.setSupplierId(1l);
        quotation.setSupplierName("MichaelZhao");
        quotation.setTotal(1300);
        quotation.setPrice(350);
        quotation.setCoinType("CMN");
        quotation.setExchangeRate(70);

        return quotation;
    }
}
