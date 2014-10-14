package io.terminus.snz.related.services;

import com.google.common.base.Function;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.terminus.pampas.common.Response;
import io.terminus.snz.related.daos.AddressFactoryDao;
import io.terminus.snz.related.daos.AddressParkDao;
import io.terminus.snz.related.daos.CategoryFactoryDao;
import io.terminus.snz.related.dto.ParkFactoryDto;
import io.terminus.snz.related.models.AddressFactory;
import io.terminus.snz.related.models.AddressPark;
import io.terminus.snz.related.models.CategoryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Desc:配送服务处理
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-06-26.
 */
@Slf4j
@Service
public class DeliveryServiceImpl implements DeliveryService {
    @Autowired
    private AddressParkDao addressParkDao;

    @Autowired
    private AddressFactoryDao addressFactoryDao;

    @Autowired
    private CategoryFactoryDao categoryFactoryDao;

    //将园区进行cache一把
    private final LoadingCache<Long , List<AddressPark>> parkCache = CacheBuilder.newBuilder().expireAfterAccess(3 , TimeUnit.MINUTES).build(
            new CacheLoader<Long , List<AddressPark>>() {
                @Override
                public List<AddressPark> load(Long productId) throws Exception {
                    //查询园区详细信息
                    return addressParkDao.findParkByProductId(productId);
                }
            }
    );

    @Override
    public Response<List<AddressPark>> findAllPark() {
        Response<List<AddressPark>> result = new Response<List<AddressPark>>();

        try {
            result.setResult(addressParkDao.findAllPark());
        } catch (Exception e) {
            log.error("find all park failed, error code={}", Throwables.getStackTraceAsString(e));
            result.setError("find.park.failed");
        }

        return result;
    }

    @Override
    public Response<List<AddressPark>> findParkByProductId(Long productId) {
        Response<List<AddressPark>> result = new Response<List<AddressPark>>();

        if (productId == null) {
            log.error("find park must need productId.");
            result.setError("park.productId.null");
            return result;
        }

        try {
            result.setResult(parkCache.get(productId));
        } catch (Exception e) {
            log.error("find park failed, productId={}, error code={}", productId, Throwables.getStackTraceAsString(e));
            result.setError("find.park.failed");
        }

        return result;
    }

    @Override
    public Response<List<AddressFactory>> findFactories(Long productId, Long parkId) {
        Response<List<AddressFactory>> result = new Response<List<AddressFactory>>();

        if (productId == null) {
            log.error("find address factory need productId");
            result.setError("factory.productId.null");
            return result;
        }

        if (parkId == null) {
            log.error("find address factory need parkId");
            result.setError("factory.parkId.null");
            return result;
        }

        try {
            //获取类目与工厂关系
            List<Long> factoryIds = Lists.transform(categoryFactoryDao.findByProductId(productId , parkId) , new Function<CategoryFactory, Long>() {
                @Override
                public Long apply(CategoryFactory categoryFactory) {
                    return categoryFactory.getFactoryId();
                }
            });

            result.setResult(addressFactoryDao.findByFactoryIds(factoryIds));
        } catch (Exception e) {
            log.error("find address factory failed , productId={}, parkId={}, error code={}", productId, parkId, Throwables.getStackTraceAsString(e));
            result.setError("find.factory.failed");
        }

        return result;
    }

    @Override
    public Response<List<AddressFactory>> findFactoryByIds(List<Long> factoryIds) {
        Response<List<AddressFactory>> result = new Response<List<AddressFactory>>();

        if(factoryIds == null || factoryIds.isEmpty()){
            log.error("find park and factory info need factoryIds");
            result.setError("parkAddress.factoryIds.null");
            return result;
        }

        try {
            result.setResult(addressFactoryDao.findByFactoryIds(factoryIds));
        } catch (Exception e) {
            log.error("find address factory failed , factoryIds={}, error code={}", factoryIds, Throwables.getStackTraceAsString(e));
            result.setError("find.factory.failed");
        }

        return result;
    }

    @Override
    public Response<List<AddressPark>> findParksByIds(List<Long> ids) {
        Response<List<AddressPark>> result = new Response<List<AddressPark>>();

        try {
            if (ids == null || ids.isEmpty()) {
                log.error("ids can not be null");
                result.setError("park.ids.not.null.fail");
                return result;
            }

            List<AddressPark> addressParks = addressParkDao.findByIds(ids);
            if (addressParks == null || addressParks.isEmpty()) {
                log.error("parks not found where ids={}", ids);
                result.setError("parks.not.found");
                return result;
            }

            result.setResult(addressParks);
        } catch (Exception e) {
            log.error("find all park failed, error code={}", Throwables.getStackTraceAsString(e));
            result.setError("find.park.failed");
        }

        return result;
    }

    @Override
    public Response<List<ParkFactoryDto>> findDetailFactories(Long productId, List<Long> factoryIds) {
        Response<List<ParkFactoryDto>> result = new Response<List<ParkFactoryDto>>();

        if(productId == null){
            log.error("find park and factory info need productId");
            result.setError("parkAddress.productId.null");
            return result;
        }

        if(factoryIds == null || factoryIds.isEmpty()){
            log.error("find park and factory info need factoryIds");
            result.setError("parkAddress.factoryIds.null");
            return result;
        }

        try{
            List<CategoryFactory> categoryFactoryList = categoryFactoryDao.findByParams(productId , factoryIds);

            //园区信息查询结果列表
            List<AddressPark> parkQuery = addressParkDao.findByIds(Lists.transform(categoryFactoryList , new Function<CategoryFactory, Long>() {
                @Override
                public Long apply(CategoryFactory categoryFactory) {
                    return categoryFactory.getParkId();
                }
            }));
            Map<Long , AddressPark> parkMap = Maps.newHashMap();
            for(AddressPark addressPark : parkQuery){
                parkMap.put(addressPark.getId() , addressPark);
            }

            //工厂信息查询结果列表
            List<AddressFactory> factoryQuery = addressFactoryDao.findByFactoryIds(Lists.transform(categoryFactoryList , new Function<CategoryFactory, Long>() {
                @Override
                public Long apply(CategoryFactory categoryFactory) {
                    return categoryFactory.getFactoryId();
                }
            }));
            Map<Long , AddressFactory> factoryMap = Maps.newHashMap();
            for(AddressFactory factory : factoryQuery){
                factoryMap.put(factory.getId() , factory);
            }

            //组装园区工厂信息列表
            List<ParkFactoryDto> parkFactoryDtoList = Lists.newArrayList();
            ParkFactoryDto parkFactoryDto;
            AddressPark addressPark;
            AddressFactory addressFactory;
            for(CategoryFactory categoryFactory : categoryFactoryList){
                parkFactoryDto = new ParkFactoryDto();
                addressPark = parkMap.get(categoryFactory.getParkId());
                addressFactory = factoryMap.get(categoryFactory.getFactoryId());

                parkFactoryDto.setParkId(addressPark.getId());
                parkFactoryDto.setParkName(addressPark.getParkName());
                parkFactoryDto.setFactoryId(addressFactory.getId());
                parkFactoryDto.setFactoryNum(addressFactory.getFactoryNum());
                parkFactoryDto.setFactoryName(addressFactory.getFactoryName());

                parkFactoryDtoList.add(parkFactoryDto);
            }

            result.setResult(parkFactoryDtoList);
        }catch(Exception e){
            log.error("find park and factory failed , productId={}, factoryIds={}", productId, factoryIds);
            result.setError("find.factory.failed");
        }

        return result;
    }
}
