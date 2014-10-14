package io.terminus.snz.web.util;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;

/**
 * Desc: Excel文件导出
 * Mail: houly@terminus.io
 * author: Hou Luyao
 * Date: 14-8-26.
 */
public class ExportExcel {

    private ExportExcel(){}

    public static ExportExcel getInstance(){
        return new ExportExcel();
    }

    /**
     * 根据用户提供的数据，模板，转换方法，将数据以excel格式导出
     * @param excelSheets 每个Sheet格式和数据
     * @param excelFileName 导出的excel文件名称
     * @param exportAction 转换方法
     * @param outputStream 输出流
     * @param <T>
     * @throws IOException
     */
    public <T> void doExport(List<ExcelSheet<T>> excelSheets, String excelFileName, ExportAction<T> exportAction, OutputStream outputStream) throws IOException {
        SXSSFWorkbook workbook = new SXSSFWorkbook(200);
        workbook.setCompressTempFiles(true);

        for(ExcelSheet excelSheet: excelSheets){
            valvalidation(excelSheet, workbook);

            SXSSFSheet sheet = (SXSSFSheet) workbook.createSheet(Strings.isNullOrEmpty(excelSheet.getSheetName())?"":excelSheet.getSheetName());
            sheet.setRandomAccessWindowSize(200);

            //导出title
            int titleStartIndex = 0;
            if(excelSheet.getTitles()!=null){
                for(int i = 0; i < excelSheet.getTitles().length; i++){
                    Row rowTitle = sheet.createRow(titleStartIndex);
                    for(int j = 0; j < excelSheet.getTitles()[i].length; j++) {
                        sheet.setColumnWidth(i, excelSheet.getColumnWidth());
                        Cell cell = rowTitle.createCell(j);
                        cell.setCellValue(excelSheet.getTitles()[i][j]);
                        cell.setCellStyle(excelSheet.getTitleRowStyle());
                    }
                    titleStartIndex++;
                }
            }

            if(excelSheet.getData()!=null){
                //导出body
                int bodyStartIndex = titleStartIndex;
                for(int i = 0; i<excelSheet.getData().size(); i++){
                    Row rowBody = sheet.createRow(bodyStartIndex);
                    String[] exportData = exportAction.transform((T)excelSheet.getData().get(i));
                    for(int j = 0; j < exportData.length; j++){
                        sheet.setColumnWidth(i, excelSheet.getColumnWidth());
                        Cell cell = rowBody.createCell(j);
                        if(excelSheet.getColReachText()!=null && excelSheet.getColReachText().size()!=0){
                            if(excelSheet.getColReachText().contains(j)){
                                cell.setCellValue(new XSSFRichTextString(exportData[j]));
                                CellStyle cellStyle = crateBodyCellStyle(workbook);
                                cellStyle.setWrapText(true);
                                cell.setCellStyle(cellStyle);
                            }
                            else{
                                cell.setCellValue(exportData[j]);
                                cell.setCellStyle(excelSheet.getBodyRowStyle());
                            }
                        }
                        else{
                            cell.setCellValue(exportData[j]);
                            cell.setCellStyle(excelSheet.getBodyRowStyle());
                        }
                    }
                    bodyStartIndex++;
                }
            }

        }
        workbook.write(outputStream);
    }



    /**
     * 校验每个excel当中的sheet数据
     * @param excelSheet
     * @param workbook
     */
    private void valvalidation(ExcelSheet excelSheet, Workbook workbook){

        Preconditions.checkNotNull(excelSheet,"excelsheet cant be null");

        //这个地方给ExcelSheet设置默认style
        if(excelSheet.getTitleRowStyle() == null){
            excelSheet.setTitleRowStyle(crateTitleCellStyle(workbook));
        }

        if(excelSheet.getBodyRowStyle() == null){
            excelSheet.setBodyRowStyle(crateBodyCellStyle(workbook));
        }

    }

    /**
     * 生成默认的Title表格样式
     * @param workbook
     * @return
     */
    private CellStyle crateTitleCellStyle(Workbook workbook) {
        Font font = workbook.createFont();
        font.setColor(Font.COLOR_NORMAL);
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setWrapText(false);
        font.setFontHeight((short)250);
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(HSSFColor.BLUE_GREY.index);
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
     * 生成默认的Body的cell样式
     * @param workbook
     * @return
     */
    protected CellStyle crateBodyCellStyle(Workbook workbook) {
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
     * 将具体数据导出的格式
     * @param <T>
     */
    public interface ExportAction<T>{

        /**
         *
         * @param t 需要转换的数据
         * @return  导出数据的最终类型
         * @throws NumberFormatException
         */
        String[] transform(T t);
    }

    /**
     * Excel中每个Sheet格式数据描述
     * @param <T>
     */
    public static class ExcelSheet<T> {

        private static final int DEFAULT_COLUMN_WIDTH = 7000;

        @Getter
        @Setter
        private String sheetName;

        @Getter
        @Setter
        private String[][] titles;

        @Getter
        @Setter
        private CellStyle titleRowStyle;

        @Getter
        @Setter
        private CellStyle bodyRowStyle;

        @Getter
        @Setter
        private List<T> data;

        @Getter
        @Setter
        private int columnWidth = DEFAULT_COLUMN_WIDTH;

        @Getter
        @Setter
        private Set<Integer> colReachText = Sets.newHashSet();//需要使用richText的列

        public static <T> ExcelSheet<T> of(){
            return new ExcelSheet<T>();
        }

        public ExcelSheet<T> buildSheetName(String sheetName){
            this.sheetName = sheetName;
            return this;
        }

        public ExcelSheet<T> buildTitles(String[][] titles){
            this.titles = titles;
            return this;
        }

        public ExcelSheet<T> buildTitleRowStyle(CellStyle titleRowStyle){
            this.titleRowStyle = titleRowStyle;
            return this;
        }

        public ExcelSheet<T> buildBodyRowStyle(CellStyle bodyRowStyle){
            this.bodyRowStyle = bodyRowStyle;
            return this;
        }

        public ExcelSheet<T> buildData(List<T> data){
            this.data = data;
            return this;
        }

        public ExcelSheet<T> buildColReachText(Set<Integer> colReachText){
            this.colReachText = colReachText;
            return this;
        }

    }
}
