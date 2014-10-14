package io.terminus.snz.requirement.tool;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.terminus.snz.requirement.model.Module;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * Desc:
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-06-24.
 */
public class HttpServiceUtilTest {
    @Test
    public void test(){
        Map<String , String> params = Maps.newHashMap();
        params.put("requirementId" , "1");
        params.put("creatorId", "1");

        System.out.println(HttpServiceUtil.buildParams(params, true, null));

        List<Module> moduleList = Lists.newArrayList();
        for(int i=0; i<1; i++){
            moduleList.add(mock());
        }
        //System.out.println(HttpServiceUtil.buildModuleRequest(moduleList));
        //des=null, signType=MD5, age=20, name=Zero, signValue=9f6cccdccc7087fab2e447ff02c67500

        params.put("signValue" , "9f6cccdccc7087fab2e447ff02c67500");
        HttpServiceUtil.checkSign(params , HttpServiceUtil.SING_WAY);
    }

    private Module mock(){
        Module module = new Module();
        module.setRequirementId(1l);
        module.setType(1);
        module.setModuleName("spu");
        module.setTotal(3000);
        module.setQuality(100);
        module.setCost(89);
        module.setDelivery(97);
        module.setAttestations("[{id:10,name:'EU认证'}, {id:10,name:'EU认证'}]");
        module.setSupplyAt(DateTime.now().toDate());

        return module;
    }
}
