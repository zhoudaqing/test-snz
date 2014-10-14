package io.terminus.snz.web.util;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import io.terminus.common.utils.MapBuilder;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * Desc:
 * Mail: houly@terminus.io
 * author: Hou Luyao
 * Date: 14-8-27.
 */
public class ImportModulesWithExcel {
    private static final int DEFAULT_BODY_LINE = 100;
    private static final int DEFAULT_COL_WIDTH = 8000;

    private ImportModulesWithExcel(){}

    public static ImportModulesWithExcel getInstance(){
        return new ImportModulesWithExcel();
    }

    /**
     * 生成批量导入模块的模板
     * @param dynamicTitle 动态的列
     * @param moduleNameValue 模块属性数据
     * @param categoryProperty 产品分类
     * @param outputStream 输出流
     * @throws IOException
     */
    public void exportModuleTemplate(String[] dynamicTitle,String[] moduleNameValue, String[] categoryProperty, OutputStream outputStream) throws IOException {
        Preconditions.checkNotNull(moduleNameValue, "moduleNameValue cant be null");
        Preconditions.checkNotNull(categoryProperty, "categoryProperty cant be null");
        Preconditions.checkState(moduleNameValue.length > 0,"moduleNameValue length cant be 0");
        Preconditions.checkState(categoryProperty.length > 0,"categoryProperty length cant be 0");
        Preconditions.checkNotNull(outputStream, "outputStream cant be null");

        Map<String,String[]> map = MapBuilder.<String,String[]>of().put("模块属性",moduleNameValue).put("产品分类",categoryProperty).put("成本目标数量单位",ModuleStaticInfo.UNITQUANTITY).put("成本目标资源量单位",ModuleStaticInfo.UNITSALES)
                .put("高峰月产能资源量单位",ModuleStaticInfo.UNITSALES).map();
        String[] titles = null;
        //判断是否含有动态列
        if(dynamicTitle!=null && dynamicTitle.length>0){
            titles = new String[dynamicTitle.length*2 + ModuleStaticInfo.TITLE.length];
            for(int i = 0; i < dynamicTitle.length; i++){
                String intitle = dynamicTitle[i];
                String title = "资源量" + intitle;
                String titleUnit = title + "资源量单位";
                map.put(titleUnit, ModuleStaticInfo.UNITSALES);
                titles[ModuleStaticInfo.TITLE.length+2*i] = title;
                titles[ModuleStaticInfo.TITLE.length+2*i+1] = titleUnit;
            }
        }
        else{
            titles = new String[ModuleStaticInfo.TITLE.length];
        }

        System.arraycopy(ModuleStaticInfo.TITLE, 0, titles, 0, ModuleStaticInfo.TITLE.length);
        exportExcelTemplate(ModuleStaticInfo.DEFAULT_SHEET_NAME, map, titles, ModuleStaticInfo.HEAD_LINE, outputStream);
    }

    /**
     * 生成excel模板
     * @param sheetName sheet页名称
     * @param colSelect 需要生成下拉框 标题和数据的对应
     * @param title 标题
     * @param outputStream 输出流
     * @throws IOException
     */
    public void exportExcelTemplate(String sheetName, Map<String,String[]> colSelect, String[] title, String headLine, OutputStream outputStream) throws IOException {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet(sheetName);
        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
        int titleStartIndex = 0;

        //查看是否有标题行
        if(!Strings.isNullOrEmpty(headLine)){
            Row headerRow = sheet.createRow(0);
            headerRow.setHeight((short)1000);
            CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, 0, title.length-1);
            sheet.addMergedRegion(cellRangeAddress);
            Cell cell = headerRow.createCell(0);
            cell.setCellValue(new XSSFRichTextString(headLine));
            cell.setCellStyle(crateHeadLineCellStyle(wb));
            titleStartIndex = 1;
        }

        //遍历title
        for(int i = 0; i < title.length; i++){
            if(colSelect.get(title[i])!= null && colSelect.get(title[i]).length>0){
                String[] select = colSelect.get(title[i]);
                XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint)
                        dvHelper.createExplicitListConstraint(select);
                CellRangeAddressList addressList = new CellRangeAddressList(1, DEFAULT_BODY_LINE+1, i, i);
                XSSFDataValidation validation = (XSSFDataValidation)dvHelper.createValidation(
                        dvConstraint, addressList);
                validation.setShowErrorBox(true);
                sheet.addValidationData(validation);
            }
        }

        //导出title
        Row rowTitle = sheet.createRow(titleStartIndex);
        if(title!=null){
            for(int i = 0; i < title.length; i++){
                sheet.setColumnWidth(i, DEFAULT_COL_WIDTH);
                Cell cell = rowTitle.createCell(i);
                cell.setCellValue(title[i]);
                cell.setCellStyle(crateTitleCellStyle(wb));

//                XSSFDrawing drawing=sheet.createDrawingPatriarch();
//                XSSFComment comment=drawing.createCellComment(new XSSFClientAnchor(0, 0, 0, 0, (short) 3, 3, (short) 5, 6));
//                comment.setAuthor("test");
//                cell.setCellComment(comment);
            }
            titleStartIndex++;
        }
        //body默认是100行
        //导出body
        int bodyStartIndex = titleStartIndex;
        for(int i = 0; i < DEFAULT_BODY_LINE; i++){
            Row rowBody = sheet.createRow(bodyStartIndex);
            for(int j = 0; j < title.length ; j++){
                sheet.setColumnWidth(i, DEFAULT_COL_WIDTH);
                Cell cell = rowBody.createCell(j);
                cell.setCellStyle(crateBodyCellStyle(wb));
            }
            bodyStartIndex++;
        }
        wb.write(outputStream);
    }

    /**
     * 给sheet页指定的位置区域生成下拉选项
     * @param sheet sheet业
     * @param selectData 下拉框数据
     * @param firstRow 开始行
     * @param lastRow 结束行
     * @param firstCol 开始列
     * @param lastCol 结束列
     */
    private void createCellSelect(XSSFDataValidationHelper dvHelper,XSSFSheet sheet, String[] selectData, int firstRow, int lastRow, int firstCol, int lastCol){
        XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint)
                dvHelper.createExplicitListConstraint(selectData);
        CellRangeAddressList addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastRow);
        XSSFDataValidation validation = (XSSFDataValidation)dvHelper.createValidation(
                dvConstraint, addressList);
        validation.setShowErrorBox(true);
        sheet.addValidationData(validation);
    }

    /**
     * 获取excel抬头样式
     * @return
     */
    protected CellStyle crateHeadLineCellStyle(Workbook workbook) {
        Font font = workbook.createFont();
        font.setColor(Font.COLOR_RED);
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setWrapText(true);
        font.setFontHeight((short)350);
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
        cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
        return cellStyle;
    }
    /**
     * 生成默认的title样式
     * @param workbook
     * @return
     */
    private CellStyle crateTitleCellStyle(Workbook workbook) {
        Font font = workbook.createFont();
        font.setColor(Font.COLOR_NORMAL);
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setWrapText(false);
        font.setFontHeight((short) 350);
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
        cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        short border = 1;
        cellStyle.setBorderBottom(border);
        cellStyle.setBorderLeft(border);
        cellStyle.setBorderRight(border);
        cellStyle.setBorderTop(border);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        return cellStyle;
    }

    /**
     * 生成默认的body样式
     * @param workbook
     * @return
     */
    private CellStyle crateBodyCellStyle(Workbook workbook) {
        Font font = workbook.createFont();
        //font.setColor(HSSFColor.BLUE_GREY.index);
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setWrapText(false);
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(HSSFColor.WHITE.index);
        cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        short border = 1;
        cellStyle.setBorderBottom(border);
        cellStyle.setBorderLeft(border);
        cellStyle.setBorderRight(border);
        cellStyle.setBorderTop(border);
        cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
        return cellStyle;
    }

    /**
     * 模块导入模板基本信息
     */
    static class ModuleStaticInfo{
        final static String DEFAULT_SHEET_NAME = "模块批量导入";
        final static String[] UNITQUANTITY = {"1","10","100","1000"};
        final static String[] UNITSALES = {"EA","JUA","KG","BAG","BOT","G","L","M","M2","M3","ML","MM","PIA","TAO","TEN","TIA","TO","ZHA"};
        final static String[] TITLE = {"模块专用号","模块描述(*)","模块属性","产品分类","认证(* 多个以,区分)","质量目标(PPM)","成本目标","成本目标数量单位","成本目标资源量单位","高峰月产能","高峰月产能资源量单位","要求供货时间(*)"};
        final static String HEAD_LINE = "1.标注“*”是必填项，公示给供应商，其他项可在需求锁定环节锁定\r\n" +
                                        "2.请先检查配额分配规则是否制定，否则无法计算合作供应商数量和模块配额分配比例";
    }

}

