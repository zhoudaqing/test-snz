package io.terminus.snz.user.tool;

import com.google.common.base.Objects;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;

/**
 * Author:Guo Chaopeng
 * Created on 14-6-6.
 */
@Slf4j
@Component
public class AnalyzeExcelUtil {

    private static final DateTimeFormatter YEAR_MONTH_FORMAT = DateTimeFormat.forPattern("yyyy-MM");

    /**
     * 通过url链接获取excel解析数据封装
     *
     * @param fileUrl  url链接数据地址
     * @param rowStart 从第几行开始解析
     * @param action   数据处理逻辑
     * @param <T>      封装数据的格式
     * @return List
     * 返回解析后封装的列表数据
     */
    public <T> List<T> analyzeURL(String fileUrl, int rowStart, AnalyzeAction<T> action) {
        List<T> analyzeObj = null;

        InputStream inputStream = null;
        try {
            URL website = new URL(fileUrl);
            URLConnection connection = website.openConnection();

            String type = fileUrl.substring(fileUrl.lastIndexOf('.') + 1);
            inputStream = connection.getInputStream();

            Long start = System.currentTimeMillis();
            //分析excel数据信息
            analyzeObj = analyzeFile(inputStream, EXCEL_TYPE.from(type), rowStart, action);

            log.debug("analyze excel file used {}ms", System.currentTimeMillis() - start);
        } catch (IOException e) {
            log.error("analyze excel file failed, fileUrl={}, rowStart={}, error code={}", fileUrl, rowStart, Throwables.getStackTraceAsString(e));
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
                log.error("close file failed, fileUrl={}, error code={}", fileUrl, Throwables.getStackTraceAsString(e));
            }
        }

        return analyzeObj;
    }

    /**
     * 通过文件路径获取excel解析数据封装
     *
     * @param filePath 文件路径
     * @param rowStart 从第几行开始解析
     * @param action   数据处理逻辑
     * @param <T>      封装数据的格式
     * @return List
     * 返回解析后封装的列表数据
     */
    public <T> List<T> analyzePath(String filePath, int rowStart, AnalyzeAction<T> action) {
        List<T> analyzeObj = null;

        InputStream inputStream = null;
        try {
            String type = filePath.substring(filePath.lastIndexOf('.') + 1);
            inputStream = new FileInputStream(filePath);

            Long start = System.currentTimeMillis();

            //分析excel数据信息
            analyzeObj = analyzeFile(inputStream, EXCEL_TYPE.from(type), rowStart, action);
            log.debug("analyze excel file used {}ms", System.currentTimeMillis() - start);
        } catch (IOException e) {
            log.error("analyze excel file failed, filePath={}, rowStart={}, error code={}", filePath, rowStart, Throwables.getStackTraceAsString(e));
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
                log.error("close file failed, filePath={}, error code={}", filePath, Throwables.getStackTraceAsString(e));
            }
        }

        return analyzeObj;
    }

    /**
     * 通过一个输入流&文件类型使用不同的POI对于excel的不同解析方式，然后调用action封装数据到类中
     *
     * @param inputStream 输入流
     * @param fileType    文件类型
     * @param rowStart    从第几行开始解析
     * @param action      数据处理逻辑
     * @param <T>         封装的结构
     * @return List
     * 返回解析后的列表数据
     * （后期可能考虑使用反射机制来自动注入数据）
     */
    public <T> List<T> analyzeFile(InputStream inputStream, EXCEL_TYPE fileType, int rowStart, AnalyzeAction<T> action) {
        List<T> objList = null;

        try {
            Workbook workbook;
            if (Objects.equal(EXCEL_TYPE.XLS, fileType)) {
                //解析excel2003
                workbook = new HSSFWorkbook(inputStream);
            } else if (Objects.equal(EXCEL_TYPE.XLSX, fileType)) {
                //解析excel2007
                workbook = new XSSFWorkbook(inputStream);
            } else {
                return null;
            }

            //解析excel中的那一个sheet
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                return null;
            }

            objList = Lists.newArrayList();
            for (int rowNum = rowStart; rowNum <= sheet.getLastRowNum(); rowNum++) {
                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    continue;
                }

                String[] cellInfo = new String[row.getLastCellNum() + 1];
                for (int cellNum = 0; cellNum <= row.getLastCellNum(); cellNum++) {
                    Cell cell = row.getCell(cellNum);
                    if (cell == null) {
                        continue;
                    }

                    if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
                        short format = cell.getCellStyle().getDataFormat();
                        if(format == 57){
                            //日期
                            double value = cell.getNumericCellValue();
                            Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
                            cellInfo[cellNum] = YEAR_MONTH_FORMAT.print(date.getTime());
                        }else {
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                            //将数据保存到一个String数组中然后具体的封装由action决定(格式是某一行某个列)
                            cellInfo[cellNum] = cell.getStringCellValue();
                        }

                    }else {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        //将数据保存到一个String数组中然后具体的封装由action决定(格式是某一行某个列)
                        cellInfo[cellNum] = cell.getStringCellValue();
                    }

//                    cell.setCellType(Cell.CELL_TYPE_STRING);
//                    cellInfo[cellNum] = cell.getStringCellValue();

                }

                T obj = action.transform(cellInfo);
                if (obj != null) {
                    objList.add(obj);
                }
            }
        } catch (Exception e) {
            log.error("analyze excel file failed, error code={}", Throwables.getStackTraceAsString(e));
        }

        return objList;
    }

    /**
     * 让外部调用的具体解析后的数据处理
     *
     * @param <T> 返回的数据类型
     */
    public interface AnalyzeAction<T> {
        /**
         * 执行后封装的数据类型信息
         *
         * @param analyzeInfo 返回某一行的解析数据
         * @return T
         * 返回一个解析完成的数据信息(存在数据类型转换问题)
         */
        T transform(String[] analyzeInfo) throws NumberFormatException;
    }

    public enum EXCEL_TYPE {
        XLS(1, "xls"), XLSX(2, "xlsx");

        private final Integer value;

        private final String description;

        private EXCEL_TYPE(Integer value, String description) {
            this.value = value;
            this.description = description;
        }

        public static EXCEL_TYPE from(String description) {
            for (EXCEL_TYPE excelType : EXCEL_TYPE.values()) {
                if (Objects.equal(excelType.description, description)) {
                    return excelType;
                }
            }

            return null;
        }

        public Integer getValue() {
            return value;
        }

        @Override
        public String toString() {
            return description;
        }
    }
}
