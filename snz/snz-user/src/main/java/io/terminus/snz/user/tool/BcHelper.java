package io.terminus.snz.user.tool;

import com.google.common.base.Function;
import io.terminus.pampas.common.Response;
import io.terminus.snz.category.model.BackendCategory;
import io.terminus.snz.category.service.BackendCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.google.common.base.Preconditions.checkState;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 9/11/14
 */
@Slf4j
@Component
public class BcHelper {

    @Autowired
    private BackendCategoryService backendCategoryService;

    public Function<BackendCategory, Long> toIds() {
        return new Function<BackendCategory, Long>() {
            @Override
            public Long apply(BackendCategory bc) {
                return bc.getId();
            }
        };
    }

    public Function<Long, BackendCategory> toFull() {
        return new Function<Long, BackendCategory>() {
            @Override
            public BackendCategory apply(Long input) {
                Response<BackendCategory> backResp = backendCategoryService.findById(input);
                checkState(backResp.isSuccess());
                return backResp.getResult();
            }
        };
    }
}
