package com.utils;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel文件导入工具类
 */
public class PoiUtil {

    /**
     * 导入Excel文件
     *
     * @param url 文件路径
     * @return 二维列表，每个子列表为一行数据
     * @throws Exception 读取异常
     */
    public static List<List<String>> poiImport(String url) throws Exception {
        List<List<String>> list = new ArrayList<>();
        // 创建Excel工作簿，读取文件内容
        HSSFWorkbook workbook = new HSSFWorkbook(FileUtils.openInputStream(new File(url)));
        // 获取第一个工作表
        HSSFSheet sheet = workbook.getSheetAt(0);
        for (int i = 0; i < sheet.getLastRowNum() + 1; i++) {
            HSSFRow row = sheet.getRow(i);
            List<String> rowlist = new ArrayList<>();
            for (int j = 0; j < row.getLastCellNum(); j++) {
                HSSFCell cell = row.getCell(j);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                String value = cell.getStringCellValue();
                rowlist.add(value);
            }
            list.add(rowlist);
        }
        return list;
    }
}
