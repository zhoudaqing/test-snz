package io.terminus.snz.requirement.service;

import com.google.common.collect.Lists;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.manager.NewProductStepManager;
import io.terminus.snz.requirement.model.NewProductStep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wanggen on 14-8-8.
 */
@Service
@Slf4j
public class NewProductStepServiceImpl implements NewProductStepService {

    @Autowired
    private NewProductStepManager newProductStepManager;

    @Override
    public Response<Boolean> post(NewProductStep step) {
        return doPostSteps(Lists.newArrayList(step));
    }

    @Override
    public Response<Boolean> batchPost(List<NewProductStep> steps) {
        return doPostSteps(steps);
    }

    private Response<Boolean> doPostSteps(List<NewProductStep> steps) {
        Response<Boolean> resp = new Response<Boolean>();
        try{
            newProductStepManager.post(steps);
            resp.setResult(Boolean.TRUE);
            return resp;
        }catch (Exception e){
            resp.setError("snz_new_product_steps.update.failed");
            log.error("Faild to update from `snz_new_product_steps` with param:{}", steps, e);
            return resp;
        }
    }
}
