package io.terminus.snz.user.service;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import io.terminus.common.model.PageInfo;
import io.terminus.common.model.Paging;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.message.services.SmsService;
import io.terminus.snz.user.dao.CompanyDao;
import io.terminus.snz.user.dao.SupplierContactDao;
import io.terminus.snz.user.dao.UserComplaintDao;
import io.terminus.snz.user.model.Company;
import io.terminus.snz.user.model.SupplierContact;
import io.terminus.snz.user.model.User;
import io.terminus.snz.user.model.UserComplaint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户抱怨反馈信息<P>
 *
 * @author wanggen 2014-08-13 14:56:46
 */
@Service
@Slf4j
public class UserComplaintServiceImpl implements UserComplaintService {

    @Autowired
    private UserComplaintDao userComplaintDao;      //用户抱怨信息DAO

    @Autowired
    private CompanyDao companyDao;                  //公司信息DAO

    @Autowired
    private SupplierContactDao supplierContactDao;  //供应商联系方式DAO

    @Autowired
    private SmsService smsService;                  //短信服务Service

    @Override
    public Response<Long> create(UserComplaint userComplaint) {
        Response<Long> resp = new Response<Long>();
        if (userComplaint == null) {
            log.error("param [userComplaint] can not be null");
            resp.setError("params.not.null");
            return resp;
        }
        try{
            userComplaint.setComplaintTypes(UnicodeToString(userComplaint.getComplaintTypes()));
            userComplaint.setClaimDoc(UnicodeToString(userComplaint.getClaimDoc()));
            Long createdId = userComplaintDao.create(userComplaint);
            if(Objects.equal(Boolean.TRUE, userComplaint.getIsSendMessage())){
                SupplierContact contact = supplierContactDao.findBySupplierCodeOrName(userComplaint.getSupplierCode(), userComplaint.getSupplierName());
                if(contact!=null){
                    smsService.sendWithTemplate("000000", contact.getPhone(), "supplier_complained_notify", userComplaint);
                }
            }
            resp.setResult(createdId);
            return resp;
        }catch (Exception e){
            resp.setError("snz_user_complaints.insert.failed");
            log.error("Failed to insert into `snz_user_complaints` with param:{}", userComplaint , e);
            return resp;
        }
    }


    static Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
    private static String UnicodeToString(String str) {
        if(str==null)
            return null;
        StringBuffer sb = new StringBuffer();
        char ch;
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            if (matcher.groupCount() > 1) {
                ch = (char) Integer.parseInt(matcher.group(2), 16);
            } else {
                ch = (char) Integer.parseInt(matcher.group(1), 10);
            }
            matcher.appendReplacement(sb, String.valueOf(ch));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }


    @Override
    public Response<UserComplaint> findById(Long id) {
        Response<UserComplaint> resp = new Response<UserComplaint>();
        if (id == null) {
            log.error("param id can not be null when query");
            resp.setError("params.not.null");
            return resp;
        }
        try{
            UserComplaint userComplaint = userComplaintDao.findById(id);
            if(userComplaint == null){
                log.warn("Failed to findById from `snz_user_complaints` with param:{}", id);
                resp.setError("snz_user_complaints.select.failed");
                return resp;
            }
            resp.setResult(userComplaint);
            return resp;
        }catch (Exception e){
            resp.setError("snz_user_complaints.select.failed");
            log.error("Failed to findById from `snz_user_complaints` with param:{}", id , e);
            return resp;
        }
    }


    @Override
    public Response<Paging<UserComplaint>> findByPaging(BaseUser user, Map<String, Object> param, Integer pageNo, Integer pageSize) {
        Response<Paging<UserComplaint>> resp = new Response<Paging<UserComplaint>>();
        try{
            if(user==null){
                resp.setError("user.not.login");
                return resp;
            }
            if(param==null)
                param = Maps.newHashMap();
            PageInfo pageInfo = new PageInfo(pageNo, pageSize);
            param.put("offset", pageInfo.getOffset());
            param.put("limit", pageInfo.getLimit());
            if(Objects.equal(User.Type.SUPPLIER.value(),user.getType())){
                Company company = companyDao.findByUserId(user.getId());
                if(company!=null){
                    param.put("currUserSupplierName", company.getCorporation());
                }
            }
            Paging<UserComplaint> userComplaints = userComplaintDao.findByPaging(param);
            if(userComplaints.getTotal() < 1){
                log.warn("No records found from `snz_user_complaints` with param:{}", param);
                resp.setError("snz_user_complaints.select.failed");
                return resp;
            }
            resp.setResult(userComplaints);
            return resp;
        }catch (Exception e){
            resp.setError("snz_user_complaints.select.failed");
            log.error("Failed to findByPaging from `snz_user_complaints` with param:{}", param, e);
            return resp;
        }
    }

    @Override
    public Response<List<UserComplaint>> findAllBy(BaseUser user, Map<String, Object> param) {
        Response<List<UserComplaint>> resp = new Response<List<UserComplaint>>();
        if(user==null){
            resp.setError("user.not.login");
            return resp;
        }
        if(param==null)
            param = Maps.newHashMap();
        try{
            if(Objects.equal(User.Type.SUPPLIER.value(),user.getType())){
                Company company = companyDao.findByUserId(user.getId());
                if(company!=null){
                    param.put("currUserSupplierName", company.getCorporation());
                }
            }
            List<UserComplaint> userComplaints = userComplaintDao.findAllBy(param);
            if(userComplaints != null && userComplaints.size()<1){
                log.warn("Failed to findAllBy from `snz_user_complaints` with param:{}", param);
                resp.setError("snz_user_complaints.select.failed");
                return resp;
            }
            resp.setResult(userComplaints);
            return resp;
        }catch (Exception e){
            resp.setError("snz_user_complaints.select.failed");
            log.error("Failed to findAllBy from `snz_user_complaints` with param:{}", param, e);
            return resp;
        }
    }

    @Override
    public Response<List<UserComplaint>> findByUserId(Long userId){
        Response<List<UserComplaint>> resp = new Response<List<UserComplaint>>();
        try{
            List<UserComplaint> userComplaints = userComplaintDao.findByUserId(userId);
            if(userComplaints!=null && userComplaints.size()<1){
                log.warn("Failed to findByUserId from `snz_user_complaints` with userId:{}", userId);
                resp.setError("snz_user_complaints.select.failed");
                return resp;
            }
            resp.setResult(userComplaints);
            return resp;
        }catch (Exception e){
            resp.setError("snz_user_complaints.select.failed");
            log.error("Failed to findByUserId from `snz_user_complaints` with userId:{}", userId, e);
            return resp;
        }
    }


    @Override
    public Response<Integer> update(UserComplaint userComplaint) {
        Response<Integer> resp = new Response<Integer>();
        if (userComplaint == null) {
            log.error("param userComplaint can not be null when update");
            resp.setError("params.not.null");
            return resp;
        }
        try{
            resp.setResult(userComplaintDao.update(userComplaint));
            return resp;
        }catch (Exception e){
            resp.setError("snz_user_complaints.update.failed");
            log.error("Failed to update table `snz_user_complaints` with param:{}", userComplaint , e);
            return resp;
        }

    }


    @Override
    public Response<Integer> deleteByIds(List<Long> ids) {
        Response<Integer> resp = new Response<Integer>();
        if (ids == null) {
            log.error("param can not be null when deleteByIds");
            resp.setError("params.not.null");
            return resp;
        }
        try{
            resp.setResult(userComplaintDao.deleteByIds(ids));
            return resp;
        }catch (Exception e){
            resp.setError("snz_user_complaints.delete.failed");
            log.error("Failed to deleteByIds from `snz_user_complaints` with param:{}", ids , e);
            return resp;
        }
    }
}
