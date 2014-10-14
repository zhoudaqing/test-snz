package io.terminus.snz.web.controllers;

import com.google.common.base.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.terminus.common.model.PageInfo;
import io.terminus.common.model.Paging;
import io.terminus.common.utils.BeanMapper;
import io.terminus.pampas.common.Response;
import io.terminus.pampas.engine.MessageSources;
import io.terminus.snz.category.model.FrontendCategory;
import io.terminus.snz.category.service.FrontendCategoryService;
import io.terminus.snz.related.models.AddressPark;
import io.terminus.snz.related.services.DeliveryService;
import io.terminus.snz.requirement.dto.RequirementBIZDto;
import io.terminus.snz.requirement.service.RequirementOutService;
import io.terminus.snz.user.dto.BizCreateSupplierDto;
import io.terminus.snz.user.dto.RichSupplierDto;
import io.terminus.snz.user.model.*;
import io.terminus.snz.user.service.AccountService;
import io.terminus.snz.user.service.AddressService;
import io.terminus.snz.user.service.CompanyService;
import io.terminus.snz.user.util.Signatures;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;


/**
 * 和百卓对接
 * Author:Guo Chaopeng
 * Created on 14-7-8.
 */
@Slf4j
@Controller
@RequestMapping("/api/biz")
public class BizController {

    @Autowired
    private MessageSources messageSources;

    @Autowired
    private AccountService<User> accountService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private DeliveryService deliveryService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private FrontendCategoryService frontendCategoryService;

    @Autowired
    private RequirementOutService requirementOutService;

    private final static Splitter splitter = Splitter.on(",").omitEmptyStrings().trimResults();

    @Value("#{app.restkey}")
    private String key;

    @RequestMapping(value = "/address/countries", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response<List<Address>> countries(@RequestParam("sign") String sign) {
        Response<List<Address>> result = new Response<List<Address>>();

        try {
            checkArgument(!Strings.isNullOrEmpty(sign), "sign.not.null.fail");
            Map<String, String> params = Maps.newHashMap();
            params.put("sign", sign);
            checkState(Signatures.verify(params, key), "sign.verify.fail");

            Response<List<Address>> addressRes = addressService.countries();
            checkState(addressRes.isSuccess(), addressRes.getError());
            result.setResult(addressRes.getResult());

        } catch (Exception e) {
            log.error("find countries failed, cause:{}", Throwables.getStackTraceAsString(e));
            result.setError(messageSources.get(e.getMessage()));
        }

        return result;
    }

    @RequestMapping(value = "/parks", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response<List<AddressPark>> parks(@RequestParam("sign") String sign) {
        Response<List<AddressPark>> result = new Response<List<AddressPark>>();

        try {
            checkArgument(!Strings.isNullOrEmpty(sign), "sign.not.null.fail");
            Map<String, String> params = Maps.newHashMap();
            params.put("sign", sign);
            checkState(Signatures.verify(params, key), "sign.verify.fail");

            Response<List<AddressPark>> addressParkRes = deliveryService.findAllPark();
            checkState(addressParkRes.isSuccess(), addressParkRes.getError());
            result.setResult(addressParkRes.getResult());

        } catch (Exception e) {
            log.error("find parks failed, cause:{}", Throwables.getStackTraceAsString(e));
            result.setError(messageSources.get(e.getMessage()));
        }

        return result;
    }

    @RequestMapping(value = "/product_lines", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response<List<ProductLine>> productLines(@RequestParam("sign") String sign) {
        Response<List<ProductLine>> result = new Response<List<ProductLine>>();

        try {
            checkArgument(!Strings.isNullOrEmpty(sign), "sign.not.null.fail");
            Map<String, String> params = Maps.newHashMap();
            params.put("sign", sign);
            checkState(Signatures.verify(params, key), "sign.verify.fail");

            Response<List<ProductLine>> productLineRes = companyService.findAllProductLine();
            checkState(productLineRes.isSuccess(), productLineRes.getError());
            result.setResult(productLineRes.getResult());

        } catch (Exception e) {
            log.error("find parks failed, cause:{}", Throwables.getStackTraceAsString(e));
            result.setError(messageSources.get(e.getMessage()));
        }

        return result;
    }

    @RequestMapping(value = "/frontend_categories", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response<List<FrontendCategory>> frontendCategories(@RequestParam("sign") String sign) {
        Response<List<FrontendCategory>> result = new Response<List<FrontendCategory>>();

        try {
            checkArgument(!Strings.isNullOrEmpty(sign), "sign.not.null.fail");
            Map<String, String> params = Maps.newHashMap();
            params.put("sign", sign);
            checkState(Signatures.verify(params, key), "sign.verify.fail");

            Response<List<FrontendCategory>> fcRes = frontendCategoryService.findAllTotally();
            checkState(fcRes.isSuccess(), fcRes.getError());
            result.setResult(fcRes.getResult());

        } catch (Exception e) {
            log.error("find parks failed, cause:{}", Throwables.getStackTraceAsString(e));
            result.setError(messageSources.get(e.getMessage()));
        }

        return result;
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response createSupplier(@DateTimeFormat(pattern = "yyyy-MM-dd") Date blDate,
                                   @DateTimeFormat(pattern = "yyyy-MM-dd") Date ocDate,
                                   @DateTimeFormat(pattern = "yyyy-MM-dd") Date tnDate,
                                   BizCreateSupplierDto bizCreateSupplierDto, HttpServletRequest request) {

        Response<Long> result = new Response<Long>();

        try {

            //TODO 检查参数

            boolean checkSign = Signatures.verify(transformToMap(request), key);
            checkState(checkSign, "sign.verify.fail");

            User user = BeanMapper.map(bizCreateSupplierDto, User.class);
            user.setMobile(bizCreateSupplierDto.getMobile());
            user.setEncryptedPassword(bizCreateSupplierDto.getPassword());

            ContactInfo contactInfo = BeanMapper.map(bizCreateSupplierDto, ContactInfo.class);

            Company company = BeanMapper.map(bizCreateSupplierDto, Company.class);
            company.setBlDate(blDate);
            company.setOcDate(ocDate);
            //company.setTnDate(tnDate);

            //可供货园区信息
            List<Long> productLineIds = convertToLong(splitter.splitToList(bizCreateSupplierDto.getProductLineIds()));
            Response<List<ProductLine>> productLineRes = companyService.findProductLineByIds(productLineIds);
            checkState(productLineRes.isSuccess(), productLineRes.getError());
            List<String> productLineNames = Lists.transform(productLineRes.getResult(), new Function<ProductLine, String>() {
                @Nullable
                @Override
                public String apply(@Nullable ProductLine productLine) {
                    return productLine.getName();
                }
            });
            company.setProductLine(Joiner.on(" ").join(productLineNames));

            //可供货园区信息
            List<Long> supplyParkIds = convertToLong(splitter.splitToList(bizCreateSupplierDto.getSupplyParkIds()));
            Response<List<AddressPark>> parkRes = deliveryService.findParksByIds(supplyParkIds);
            checkState(parkRes.isSuccess(), parkRes.getError());
            List<CompanySupplyPark> companySupplyParks = convertToCompanySupplierPark(parkRes.getResult());

            //主营业务信息
            List<Long> mainBusinessIds = convertToLong(splitter.splitToList(bizCreateSupplierDto.getMainBusinessIds()));
            Response<List<FrontendCategory>> fcRes = frontendCategoryService.findByIds(mainBusinessIds);
            checkState(fcRes.getResult() != null, "category.find.empty");
            List<CompanyMainBusiness> companyMainBusinesses = convertToCompanyMainBusiness(fcRes.getResult());

            RichSupplierDto richSupplierDto = new RichSupplierDto();
            richSupplierDto.setUser(user);
            richSupplierDto.setContactInfo(contactInfo);
            richSupplierDto.setCompany(company);
            richSupplierDto.setCompanySupplyParks(companySupplyParks);
            richSupplierDto.setCompanyMainBusinesses(companyMainBusinesses);
            Response<Long> userRes = accountService.createBizSupplier(richSupplierDto);
            checkState(userRes.isSuccess(), userRes.getError());
            result.setResult(userRes.getResult());


        } catch (IllegalStateException e) {
            log.error("fail to invoke method 'createSupplier' with data={}, error:{}", bizCreateSupplierDto, e.getMessage());
            result.setError(messageSources.get(e.getMessage()));
        } catch (Exception e) {
            log.error("fail to invoke method 'createSupplier' with data={}, error:{}", bizCreateSupplierDto, Throwables.getStackTraceAsString(e));
            result.setError(messageSources.get("create.supplier.fail"));
        }

        return result;
    }

    /**
     * 对百卓提供的获取需求信息的接口（添加更改时间的查询）
     * @param pageNo  当前页数（默认为0）
     * @param size    分页大小（默认为100）
     * @param startAt 查询开始时间（查询的时间段）
     * @param endAt   查询结束时间
     * @param sign    签名数据(用于判断数据是否有被更改过)
     * @return Paging
     * 返回需求的分页数据信息
     */
    @RequestMapping(value = "/requirements", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response<Paging<RequirementBIZDto>> findRequirement(@RequestParam("pageNo")String pageNo, @RequestParam("size")String size,
                                                               @RequestParam("startAt")String startAt, @RequestParam("endAt")String endAt, @RequestParam("sign")String sign){
        Response<Paging<RequirementBIZDto>> result = new Response<Paging<RequirementBIZDto>>();

        try{
            Map<String , String> singParams = Maps.newHashMap();
            singParams.put("pageNo" , pageNo);
            singParams.put("size" , size);
            singParams.put("startAt" , startAt);
            singParams.put("endAt" , endAt);
            singParams.put("sign", sign);

            //验证签名是否正确
            boolean checkSign = true;//Signatures.verify(singParams , key);
            checkState(checkSign, "sign.verify.fail");

            Response<Paging<RequirementBIZDto>> findRes = requirementOutService.sendToBizInfo(pageNo == null ? null : new Integer(pageNo) ,
                                                            size == null ? null : new Integer(size), startAt, endAt);
            if(!findRes.isSuccess()){
                log.error("find requirement info failed, error code={}", findRes.getError());
                result.setError(findRes.getError());
                return result;
            }

            result.setResult(findRes.getResult());
        }catch (IllegalStateException illegal){
            log.error("check sign failed, error code={}", Throwables.getStackTraceAsString(illegal));
            result.setError("sign.verify.fail");
        }catch(Exception e){
            log.error("find requirement info failed, error code={}", Throwables.getStackTraceAsString(e));
            result.setError("requirement.find.failed");
        }

        return result;
    }

    private Map<String, String> transformToMap(HttpServletRequest request) {
        Map<String, String> params = Maps.newHashMap();
        for (String key : request.getParameterMap().keySet()) {
            String value = request.getParameter(key);
            params.put(key, value);
        }
        return params;
    }

    private List<Long> convertToLong(List<String> ids) {
        return Lists.transform(ids, new Function<String, Long>() {
            @Nullable
            @Override
            public Long apply(String id) {
                return Long.parseLong(id);
            }
        });
    }

    private List<CompanySupplyPark> convertToCompanySupplierPark(List<AddressPark> addressParks) {
        return Lists.transform(addressParks, new Function<AddressPark, CompanySupplyPark>() {
            @Nullable
            @Override
            public CompanySupplyPark apply(AddressPark addressPark) {
                CompanySupplyPark companySupplyPark = new CompanySupplyPark();
                companySupplyPark.setSupplyParkId(addressPark.getId());
                companySupplyPark.setName(addressPark.getParkName());
                return companySupplyPark;
            }
        });
    }

    private List<CompanyMainBusiness> convertToCompanyMainBusiness(List<FrontendCategory> frontendCategories) {
        return Lists.transform(frontendCategories, new Function<FrontendCategory, CompanyMainBusiness>() {
            @Nullable
            @Override
            public CompanyMainBusiness apply(FrontendCategory frontendCategory) {
                CompanyMainBusiness companyMainBusiness = new CompanyMainBusiness();
                companyMainBusiness.setMainBusinessId(frontendCategory.getId());
                companyMainBusiness.setName(frontendCategory.getName());

                //冗余一级类目id
                Response<List<FrontendCategory>> ancestorsRes = frontendCategoryService.ancestorsOf(frontendCategory.getId());
                if (ancestorsRes.isSuccess()) {
                    Long firstLevelId = ancestorsRes.getResult().get(ancestorsRes.getResult().size() - 1).getId();
                    companyMainBusiness.setFirstLevelId(firstLevelId);
                }
                return companyMainBusiness;
            }
        });
    }

}
