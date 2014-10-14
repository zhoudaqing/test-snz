package io.terminus.snz.related.services;

import com.google.common.base.Throwables;
import com.google.common.html.HtmlEscapers;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.related.daos.CompensationDao;
import io.terminus.snz.related.daos.CompensationReplyDao;
import io.terminus.snz.related.models.CompensationReply;
import io.terminus.snz.user.model.Company;
import io.terminus.snz.user.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Author:wenhaoli
 * Date: 2014-08-12
 */
@Service
@Slf4j
public class CompensationReplyServiceImpl implements CompensationReplyService {

    @Autowired
    private CompensationReplyDao compensationReplyDao;

    @Autowired
    private CompensationDao compensationDao;

    @Autowired
    private CompanyService companyService;

    @Override
    public Response<CompensationReply> create(BaseUser user, CompensationReply re, Long listId){

        Response<CompensationReply> resp = new Response<CompensationReply>();
        try{
            //验证用户是否登陆
            if (user == null){
                log.error("user isn't login");
                resp.setError("user.not.login");
                return resp;
            }
            //验证用户是供应商(2)还是采购商(3)
            if (user.getType() == 2){
                Company cresult = companyService.findCompanyByUserId(user.getId()).getResult();
                if(cresult == null){
                    log.error("Get company failed");
                    resp.setError("company.get.failed");
                    return resp;
                }
                re.setCompanyName(cresult.getCorporation());
                re.setUserId(cresult.getId());
                if (compensationDao.load(listId).getStatus()== 3 || compensationDao.load(listId).getStatus()==4 ){
                    log.error("compensation has been handled.");
                    resp.setError("compensation.is.invalid");
                    return resp;
                }
                compensationDao.load(listId).setStatus(1);
            }
            if (user.getType() == 3){
                if (compensationDao.load(listId).getStatus()== 3 || compensationDao.load(listId).getStatus()==4 ){
                    log.error("compensation has been handled.");
                    resp.setError("compensation.is.invalid");
                    return resp;
                }
                re.setCompanyName(user.getName());
                re.setUserId(user.getId());
            }
            re.setListId(listId);
            re.setContent(HtmlEscapers.htmlEscaper().escape(re.getContent()));
            Long id = compensationReplyDao.create(re);
            CompensationReply result = compensationReplyDao.findById(id);
            resp.setResult(result);
        }catch (Exception e){
            log.error("failed to create compensationReply cause:{}", re, Throwables.getStackTraceAsString(e));
            resp.setError("compensationReply.create.failed");
        }
        return resp;
    }

}
