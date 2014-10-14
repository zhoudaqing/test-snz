package io.terminus.snz.web.controllers;

import io.terminus.common.utils.JsonMapper;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.pampas.common.UserUtil;
import io.terminus.snz.user.model.UserComplaint;
import io.terminus.snz.user.service.UserComplaintService;
import io.terminus.snz.web.helpers.CellBuilder;
import io.terminus.snz.web.util.HttpHeaderUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 功能描述:导出用户抱怨信息
 *
 * @author wanggen on 14-9-19.
 */
@Controller
@RequestMapping("/api/usercomplaint")
@Slf4j
public class UserComplaintsExportController {

    @Autowired
    private UserComplaintService userComplaintService;

    @RequestMapping(value = "/export", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView export(HttpServletResponse response, HttpServletRequest request, @RequestParam("params") String paramsString) {

        try {

            BaseUser currentUser = UserUtil.getCurrentUser();
            if (currentUser == null) {
                return null;
            }
            Map<String, Object> params = JsonMapper.JSON_NON_EMPTY_MAPPER.fromJson(paramsString, Map.class);

            if(params!=null){
                params.put("limit", 2000);
            }
            Response<List<UserComplaint>> complaints = userComplaintService.findAllBy(currentUser, params);

            HttpHeaderUtil.setDowloadHeader(request, response, "采购商抱怨信息.xls");

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("采购商抱怨信息");
            Row header = sheet.createRow(0);
            int col=0;
            CellBuilder.start(header, col++).value("投诉人编号").width(12).alignHori(0x2);
            CellBuilder.start(header, col++).value("投诉人姓名").width(12).alignHori(0x2);
            CellBuilder.start(header, col++).value("产品线").width(20).alignHori(0x2);
            CellBuilder.start(header, col++).value("类目").width(10).alignHori(0x2);
            CellBuilder.start(header, col++).value("工厂").width(15).alignHori(0x2);
            CellBuilder.start(header, col++).value("工厂负责人").width(10).alignHori(0x2);
            CellBuilder.start(header, col++).value("供应商编码").width(14).alignHori(0x2);
            CellBuilder.start(header, col++).value("供应商名称").width(40).alignHori(0x2);
            CellBuilder.start(header, col++).value("物料号").width(15).alignHori(0x2);
            CellBuilder.start(header, col++).value("物料名称").width(35).alignHori(0x2);
            CellBuilder.start(header,col++).value("投诉内容").width(70).alignHori(0x2);
            CellBuilder.start(header,col++).value("投诉原因").width(8).alignHori(0x2);
            CellBuilder.start(header, col++).value("合计扣分").width(8).height(30).alignHori(0x2);

            if(complaints.getResult()!=null){
                for(int i=0;i<complaints.getResult().size();i++){
                    UserComplaint userComplaint = complaints.getResult().get(i);
                    Row row = sheet.createRow(i+1);
                    col = 0;
                    CellBuilder.start(row, col++).value(userComplaint.getUserId());
                    CellBuilder.start(row, col++).value(userComplaint.getUserName());
                    CellBuilder.start(row, col++).value(userComplaint.getProductLineName());
                    CellBuilder.start(row, col++).value(userComplaint.getFrontendCategoryName());
                    CellBuilder.start(row, col++).value(userComplaint.getFactoryName());
                    CellBuilder.start(row, col++).value(userComplaint.getProductOwnerName());
                    CellBuilder.start(row, col++).value(userComplaint.getSupplierCode());
                    CellBuilder.start(row, col++).value(userComplaint.getSupplierName());
                    CellBuilder.start(row, col++).value(userComplaint.getModuleNum());
                    CellBuilder.start(row, col++).value(userComplaint.getModuleName());
                    CellBuilder.start(row, col++).value(userComplaint.getComplaintContent());
                    CellBuilder.start(row, col++).value(userComplaint.getComplaintReason());
                    CellBuilder.start(row, col++).value(userComplaint.getScoreTotal()).height(20);
                }
            }
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            log.error("导出用户抱怨信息错误", e);
        }
        return null;
    }

}
