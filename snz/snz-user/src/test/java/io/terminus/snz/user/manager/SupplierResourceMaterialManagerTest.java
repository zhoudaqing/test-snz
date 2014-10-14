package io.terminus.snz.user.manager;

import com.google.common.collect.Lists;
import io.terminus.common.utils.JsonMapper;
import io.terminus.snz.category.model.BackendCategory;
import io.terminus.snz.user.dao.SupplierResourceMaterialSubjectDao;
import io.terminus.snz.user.model.SupplierResourceMaterialSubject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 8/25/14
 */
public class SupplierResourceMaterialManagerTest {

    @InjectMocks
    private SupplierResourceMaterialManager supplierResourceMaterialManager;

    @Mock
    private SupplierResourceMaterialSubjectDao supplierResourceMaterialSubjectDao;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    private SupplierResourceMaterialSubject getSubject(String name, Integer type, String role, Integer status, Long version) {
        SupplierResourceMaterialSubject subject = new SupplierResourceMaterialSubject();
        subject.setName(name);
        subject.setType(type);
        subject.setRole(role);
        subject.setStatus(status);
        subject.setVersion(version);
        return subject;
    }

    @Test
    public void testtest() {
        JsonMapper mapper = JsonMapper.nonDefaultMapper();
        List<BackendCategory> bcs = Lists.newArrayList();
        BackendCategory bc = new BackendCategory();
        bc.setId(1L);
        bc.setName("bc1");
        bcs.add(bc);
        BackendCategory bc2 = new BackendCategory();
        bc2.setId(2L);
        bc.setName("bc3");
        bcs.add(bc2);
        String json = mapper.toJson(bcs);
        System.out.println(json);
    }
}