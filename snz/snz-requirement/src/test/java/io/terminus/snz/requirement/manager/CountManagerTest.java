package io.terminus.snz.requirement.manager;

import io.terminus.common.redis.utils.JedisTemplate;
import io.terminus.snz.requirement.model.Module;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

import static org.junit.Assert.assertNotNull;

public class CountManagerTest {

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }


    private Pool<Jedis> jedisPool = new Pool<Jedis>() {
        @Override
        public Jedis getResource() {
            return Mockito.mock(Jedis.class);
        }

        @Override
        public void returnResourceObject(Jedis resource) {
        }

        @Override
        public void returnBrokenResource(Jedis resource) {
        }

        @Override
        public void returnResource(Jedis resource) {
        }

        @Override
        public void destroy() {
        }

        @Override
        protected void returnBrokenResourceObject(Jedis resource) {
        }

        @Override
        protected void closeInternalPool() {
        }
    };

    @Spy
    private JedisTemplate jedisTemplate = new JedisTemplate(jedisPool);

    @InjectMocks
    private CountManager countManager;

    @Test
    public void testCreateMEvent() throws Exception {
        countManager.createMEvent(1l, mock());
    }

    @Test
    public void testUpdateMEvent() throws Exception {
        countManager.updateMEvent(1l , mock(), mock());
    }

    @Test
    public void testDeleteMEvent() throws Exception {
        countManager.deleteMEvent(1l , mock());
    }

    @Test
    public void testSignSecrecyEvent() throws Exception {
        countManager.signSecrecyEvent(1l , 1l);
    }

    @Test
    public void testGetModuleNum() throws Exception {
        assertNotNull(countManager.getModuleNum(1l));
    }

    @Test
    public void testGetModuleTotal() throws Exception {
        assertNotNull(countManager.getModuleTotal(1l));
    }

    @Test
    public void testExistSign() throws Exception {
        assertNotNull(countManager.existSign(1l , 1l));
    }

    @Test
    public void testRemoveSign() throws Exception {
        countManager.removeSign(1l , 1l);
    }

    @Test
    public void testDeleteKey() throws Exception {
        countManager.deleteKey("1");
    }

    private Module mock(){
        Module module = new Module();
        module.setRequirementId(1l);
        module.setType(1);
        module.setModuleName("spu");
        module.setTotal(3000);
        module.setQuality(100);
        module.setCost(98);
        module.setDelivery(100);
        module.setAttestations("[{id:10,name:'EU认证'}, {id:10,name:'EU认证'}]");
        module.setSupplyAt(DateTime.now().toDate());

        return module;
    }
}