package io.terminus.snz.web.helpers;

import com.google.common.base.Objects;
import lombok.Data;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

/**
 * 功能描述: 组织 XLS 文件行数据
 *
 * @author wanggen on 14-9-19.
 */
@Data
public class CellBuilder {

    private static final float ratePix = (float) 20;

    private Cell cell;

    private CellStyle cellStyle;

    private int index;

    public static CellBuilder start(Row row, int index, Boolean... bordered) {
        CellBuilder cellBuilder = new CellBuilder();
        cellBuilder.setCell(row.getCell(index));
        if (cellBuilder.getCell() == null) cellBuilder.setCell(row.createCell(index));
        cellBuilder.setIndex(index);
        if(bordered.length>0 && Objects.equal(Boolean.TRUE, bordered[0]))
            cellBuilder.borderThin();
        return cellBuilder;
    }

    public CellBuilder value(Object value, int... type) {
        if (type.length >= 1) {
            switch (type[0]) {
                case Cell.CELL_TYPE_NUMERIC:
                    if (value instanceof Number) cell.setCellValue(((Number) value).doubleValue());
                    else if (value != null) cell.setCellValue(Double.parseDouble((String) value));
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    if (value instanceof Boolean) cell.setCellValue((Boolean) value);
                    else if (value != null) cell.setCellValue(Boolean.parseBoolean((String) value));
                    break;
                case Cell.CELL_TYPE_BLANK:
                    cell.setCellValue("");
                    break;
                default:
                    cell.setCellValue(String.valueOf(value==null ? "" : value));
            }
        } else {
            cell.setCellValue(String.valueOf(value==null ? "" : value));
        }
        return this;
    }


    public CellBuilder alignHori(int align) {
        validateStyle();
        cell.getCellStyle().setAlignment((short) align);
        return this;
    }

    public CellBuilder alignVert(int align) {
        validateStyle();
        cellStyle.setVerticalAlignment((short) align);
        return this;
    }

    public CellBuilder width(int width) {
        cell.getSheet().setColumnWidth(this.getIndex(),(width*256));
        return this;
    }

    public CellBuilder height(int height) {
        cell.getRow().setHeight((short) (height*ratePix));
        return this;
    }

    public CellBuilder forefrontColor(int color) {
        validateStyle();
        cellStyle.setFillForegroundColor((short) color);
        cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        return this;
    }


    public CellBuilder borderThin(){
        border(CellStyle.BORDER_THIN); return this;
    }
    public CellBuilder border(short style) {
        validateStyle();
        cellStyle.setBorderTop(style);
        cellStyle.setBorderRight(style);
        cellStyle.setBorderBottom(style);
        cellStyle.setBorderLeft(style);
        return this;
    }

    private void validateStyle() {
        if(cellStyle==null){
            cellStyle = cell.getSheet().getWorkbook().createCellStyle();
            cell.setCellStyle(cellStyle);
        }
    }

}

