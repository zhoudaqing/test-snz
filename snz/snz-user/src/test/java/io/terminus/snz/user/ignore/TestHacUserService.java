package io.terminus.snz.user.ignore;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.haier.openplatform.hac.dto.HacUserDTO;
import com.haier.openplatform.hac.dto.UserIDSDTO;
import com.haier.openplatform.hac.dto.UserMergeDTO;
import com.haier.openplatform.hac.service.HacUserServiceCli;
import com.haier.openplatform.hac.service.agent.HacUserServiceClient;
import com.haier.openplatform.util.ExecuteResult;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

/**
 * testpsi/123456   18683443875
 * jiaoyuan/123456  15801384286
 * kaikai/123456    15167194126
 *
 * 测试地址 10.135.13.22:2181
 * 生产地址 10.135.12.123:2181,10.135.12.124:2181,10.135.12.125:2181
 *
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-7-4
 */
@Ignore
public class TestHacUserService {
    private static final String registryUrl = "10.135.12.123:2181,10.135.12.124:2181,10.135.12.125:2181";

    private static final String CO_SESSION_ID = "183";
    private static final String IDS_APP_NAME = "userCenter";
    private static final String PASSWD_KEY = ";TThxI4F84\\q";

    private static RegistryConfig registry;
    private static ApplicationConfig application;
    static {
        application = new ApplicationConfig();
        application.setName("HacDemo");
        registry = new RegistryConfig(registryUrl);
        registry.setProtocol("zookeeper");
    }

    /**
     * 用户服务
     */
    private HacUserServiceClient getHacUserServiceClient() {
        ReferenceConfig<HacUserServiceClient> huscR = new ReferenceConfig<HacUserServiceClient>();
        huscR.setApplication(application);
        huscR.setRegistry(registry);
        huscR.setVersion("2.0");
        huscR.setInterface(HacUserServiceClient.class);
        HacUserServiceClient husc = huscR.get();
        return husc;
    }

    private HacUserServiceCli getHacUserServiceCli(){
        ReferenceConfig<HacUserServiceCli> service = new ReferenceConfig<HacUserServiceCli>();
        service.setApplication(application);
        service.setRegistry(registry);
        service.setVersion("2.0");
        //service.setTimeout(1000000);
        service.setInterface(HacUserServiceCli.class);
        HacUserServiceCli husc = service.get();
        return husc;
    }

    @Test
    public void testCreateUser() {
        UserMergeDTO userMergeDTO = new UserMergeDTO();
        userMergeDTO.setUserName("zhangyuxin");
        userMergeDTO.setPassword("123456");
        userMergeDTO.setCoSessionId(CO_SESSION_ID);
        userMergeDTO.setAppName(IDS_APP_NAME);
        userMergeDTO.setPasswdKey(PASSWD_KEY);
        userMergeDTO.setRegistSrc("IDS");
        userMergeDTO.setMobile("18968111873");
        HacUserServiceCli service = getHacUserServiceCli();
        ExecuteResult<String> res = service.createMergeUser(userMergeDTO);
        printResult(res);
    }

    @Test
    public void testActiveUser(){
        HacUserServiceClient service = getHacUserServiceClient();
        UserIDSDTO userIDSDTO = new UserIDSDTO();
        userIDSDTO.setUserName("zhangyuxin");
        userIDSDTO.setCoSessionId(CO_SESSION_ID);
        userIDSDTO.setRegistSrc(IDS_APP_NAME);
        userIDSDTO.setPasswdKey(PASSWD_KEY);
        userIDSDTO.setAttributeName("mobile");
        userIDSDTO.setAttributeValue("18968111873");
        userIDSDTO.setActivationCode("151644");
        ExecuteResult<String> res = service.activeUser(userIDSDTO);
        printResult(res);
    }

    @Test
    public void testResendActiveCode(){
//        UserIDSDTO userIDSDTO = new UserIDSDTO();
//        userIDSDTO.setUserName("zhaoyu");
//        userIDSDTO.setCoSessionId(CO_SESSION_ID);
//        userIDSDTO.setRegistSrc(IDS_APP_NAME);
//        userIDSDTO.setPasswdKey(PASSWD_KEY);
//        userIDSDTO.setAttributeName("mobile");
//        userIDSDTO.setAttributeValue("18657327206");
//        HacUserServiceClient service = getHacUserServiceClient();
//        ExecuteResult<String> res = service.resendActiveCode(userIDSDTO);
//        printResult(res);
    }

    @Test
    public void testUserLoginIDS(){
        // "00575591", "Haier.201406"
        UserMergeDTO userMergeDTO = new UserMergeDTO();
        userMergeDTO.setUserName("zhangyuxin");
        userMergeDTO.setPassword("123456");
        userMergeDTO.setRegistSrc("IDS");
        userMergeDTO.setAppName(IDS_APP_NAME);
        userMergeDTO.setCoSessionId(CO_SESSION_ID);
        userMergeDTO.setPasswdKey(PASSWD_KEY);
        HacUserServiceCli service = getHacUserServiceCli();
        ExecuteResult<UserMergeDTO> res = service.mergeUserLogin(userMergeDTO);
        printResult(res);
    }

    /**
     * 徐峰: 00575591/Haier.201406
     * 其他: A0006399/303216
     */
    @Test
    public void testUserLoginIDM(){
        // "00575591", "Haier.201406"
        UserMergeDTO userMergeDTO = new UserMergeDTO();
        userMergeDTO.setUserName("00575591");
        userMergeDTO.setPassword("Haier.201407");
        userMergeDTO.setRegistSrc("IDM");

        HacUserServiceCli service = getHacUserServiceCli();
        ExecuteResult<UserMergeDTO> res = service.mergeUserLogin(userMergeDTO);//service.idsLoginUser(userIDSDTO);
        printResult(res);
        printUserMergeDto(res.getResult());
    }

    @Test
    public void testUserLogout(){
        HacUserServiceClient service = getHacUserServiceClient();
        UserIDSDTO userIDSDTO = new UserIDSDTO();
        userIDSDTO.setCoSessionId(CO_SESSION_ID);
        userIDSDTO.setRegistSrc(IDS_APP_NAME);
        userIDSDTO.setPasswdKey(PASSWD_KEY);
        userIDSDTO.setSsoSessionId("7D02CD5B2F0E75FDDDE6FCC5527768D6-10.135.106.115");
        userIDSDTO.setClientIp("10.135.106.115");
        ExecuteResult<String> res = service.userLogout(userIDSDTO);
        printResult(res);
    }

    @Test
    public void testSearchIDMUserByUserCode(){
        HacUserServiceCli service = getHacUserServiceCli();
        String userCode = "00575591";   //00575591's first line id
        String userName = "";
        ExecuteResult<List<HacUserDTO>> res = service.searchIDMUser(userCode, userName, "IN");
        printResult(res);
        printHacUserDTOs(res.getResult());
    }

    @Test
    public void testSearchIDMUserByUserName(){
        HacUserServiceCli service = getHacUserServiceCli();
        String userCode = "";
        String userName = "许峰";
        ExecuteResult<List<HacUserDTO>> res = service.searchIDMUser(userCode, userName, "IN");
        printResult(res);
        printHacUserDTOs(res.getResult());
    }

    private void printHacUserDTOs(List<HacUserDTO> userDTOs) {
        for (HacUserDTO userDTO : userDTOs){

            System.out.println("id: " + userDTO.getId());
            System.out.println("name: " + userDTO.getName());
            System.out.println("nickname: " +userDTO.getNickName());
            System.out.println("password: " + userDTO.getPassword());
            System.out.println("first line id: " + userDTO.getHaierUserFirstLineID());
            System.out.println("regist src: " + userDTO.getRegistSrc());
            System.out.println("phone: " + userDTO.getPhone());
            System.out.println("groups: " + userDTO.getHacGroups());
            System.out.println("userou: " + userDTO.getUserOU());
            System.out.println("email: " + userDTO.getEmail());
            System.out.println("status: " + userDTO.getStatus());
            System.out.println("hr position: " + userDTO.getHrPosition());
            System.out.println("hr position code: " + userDTO.getHrpositionCode());
            System.out.println("type: " + userDTO.getType());
            System.out.println("haier org code: " + userDTO.getHaierOrgCode());
            System.out.println("haier ziorg code: " +userDTO.getHaierOrgziCode());
            System.out.println("bussiness position: " + userDTO.getBusinessPosition());
            System.out.println("bussiness position code: " + userDTO.getBusinessPositionCode());
            System.out.println("haier persk: " + userDTO.getHaierPersk());
            System.out.println("---------------------------------------------");
        }
    }

    private void printUserMergeDto(UserMergeDTO hacUserDTO) {
        System.out.println("id: " + hacUserDTO.getId());
        System.out.println("position: " + hacUserDTO.getBusinessPosition());
        System.out.println("position code: " + hacUserDTO.getBusinessPositionCode());
        System.out.println("type: " + hacUserDTO.getType());
        System.out.println("current login ip: " + hacUserDTO.getCurrentLoginIp());
        System.out.println("email: " + hacUserDTO.getEmaile());
        System.out.println("mobile: " + hacUserDTO.getMobile());
        System.out.println("encode: " + hacUserDTO.getEncode());
        System.out.println("username: " + hacUserDTO.getUserName());
        System.out.println("nickname: " + hacUserDTO.getNickName());
        System.out.println("password: " + hacUserDTO.getPassword());
        System.out.println("dataPrivilegeId: " + hacUserDTO.getDataPrivilegeId());
        System.out.println("haierDataSrc: " + hacUserDTO.getHaierDataSrc());
        System.out.println("registSrc: " + hacUserDTO.getRegistSrc());
        System.out.println("UserOU: " + hacUserDTO.getUserOU());
        System.out.println("HacGroups: not supported!");
        System.out.println("status: " + hacUserDTO.getStatus());
        System.out.println("first line id: " + hacUserDTO.getHaierUserFirstLineID());

    }

    private void printResult(ExecuteResult<?> ers) {
        System.out.println("success: " + ers.isSuccess());
        Object res = ers.getResult();
        System.out.println("result: " + res);
        System.out.println("success msg: " + ers.getSuccessMessage());
        System.out.println("error msg: " + ers.getErrorMessages());
        System.out.println("field errors: " + ers.getFieldErrors());
        System.out.println("warning messages: " + ers.getWarningMessages());

    }
}
