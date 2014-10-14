package io.terminus.snz.requirement.ignore;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Desc:使用POI实现对Excel的解析处理
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-08.
 */
public class POIExcelTest {

    public void loadFile(String fileURL){
        try {
            //通过url获取远程的一个excel文档
            URL website = new URL(fileURL);
            URLConnection connection = website.openConnection();

            InputStream is = connection.getInputStream();
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);



            // 循环工作表Sheet(得到所有的table数据)
            for(int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
                HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
                if (hssfSheet == null) {
                    continue;
                }

                // 循环行Row
                for (int rowNum = 0; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                    HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                    if (hssfRow == null) {
                        continue;
                    }

                    // 循环列Cell
                    for (int cellNum = 0; cellNum <= hssfRow.getLastCellNum(); cellNum++) {
                        HSSFCell hssfCell = hssfRow.getCell(cellNum);
                        if (hssfCell == null) {
                            continue;
                        }

                        System.out.println("cellNum:"+cellNum + "->" + getValue(hssfCell));
                    }
                    System.out.println();
                }
            }

            is.close();
        }catch(Exception e){
            System.out.print(e);
        }
    }

    //更改数据类型
    @SuppressWarnings("static-access")
    private String getValue(HSSFCell hssfCell){
        String value = null;

        switch(hssfCell.getCellType()){
            case Cell.CELL_TYPE_BOOLEAN:
                value = String.valueOf(hssfCell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_NUMERIC:
                value = String.valueOf(hssfCell.getNumericCellValue());
                break;
            default:
                value = hssfCell.getStringCellValue();
        }

        return value;
    }


    public static void main(String[] args){
        POIExcelTest poiExcelTest = new POIExcelTest();
        poiExcelTest.loadFile("http://qd.baidupcs.com/file/7c5e607527da205423a15aec7d018546?fid=68346277-250528-660238176040978&time=1399524851&sign=FDTAXER-DCb740ccc5511e5e8fedcff06b081203-o3hdFHDmAL7gTJmZmw1ki2DEvzY%3D&to=qb&fm=Q,B,U,nc&newver=1&expires=8h&rt=pr&r=393209371&logid=3263507485&vuk=68346277&fn=%E4%BA%8C%E7%BA%A7%E5%9F%9F%E5%90%8D%E8%A7%84%E5%88%92.xlsx");
    }
}
