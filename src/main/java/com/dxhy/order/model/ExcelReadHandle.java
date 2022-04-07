package com.dxhy.order.model;


import com.dxhy.order.exception.ExcelReadException;
import com.dxhy.order.model.excelHeads.ExcelImportErrorMessageEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 表格读取
 *
 * @author ZSC-DXHY
 * @date 创建时间: 2020-08-14 14:49
 */
@Slf4j
public class ExcelReadHandle {
    private static final String LOGGER_MSG = "(表格读取工具类)";
    
    private final String EXCEL_XLS = ".xls";
    
    private final String EXCEL_XLSX = ".xlsx";
    
    private final ExcelReadContext excelReadContext;
    
    public ExcelReadHandle(ExcelReadContext context){
        
        this.excelReadContext = context;
    }
    
    /**
     * @param inputStream
     * @return
     * @description excel通用读取类 exel数据读取封装到bean 目前支持简单对象 单sheet读取
     */
    public <T> List<T> readFromExcel(InputStream inputStream, Class<T> clazz) throws ExcelReadException, IOException {
    
    
        List<T> list = new ArrayList<>();
        Workbook wb = null;
        Sheet sheet = null;
    
        try {
        
            //根据 文件类型创建 workbook对象
            if (EXCEL_XLSX.equals(excelReadContext.getFilePrefix())) {
                try {
                    wb = new XSSFWorkbook(inputStream);
                } catch (Exception e) {
                    log.error("{}excel读取错误{}", LOGGER_MSG, e);
                    throw new ExcelReadException(ExcelImportErrorMessageEnum.ORDERINFO_EXCEL_READERROR_9101.getKey(),
                            ExcelImportErrorMessageEnum.ORDERINFO_EXCEL_READERROR_9101.getValue());
                }
            } else if (EXCEL_XLS.equals(excelReadContext.getFilePrefix())) {
                try {
                    wb = new HSSFWorkbook(inputStream);
                } catch (Exception e) {
                    log.error("{}excel读取错误{}", LOGGER_MSG, e);
                    throw new ExcelReadException(ExcelImportErrorMessageEnum.ORDERINFO_EXCEL_READERROR_9101.getKey(),
                            ExcelImportErrorMessageEnum.ORDERINFO_EXCEL_READERROR_9101.getValue());
                }
            } else {
                throw new ExcelReadException(ExcelImportErrorMessageEnum.ORDERINFO_FILEPREFIX_ERROR_9100.getKey(),
                        ExcelImportErrorMessageEnum.ORDERINFO_FILEPREFIX_ERROR_9100.getValue());
            }
            
            //读取指定idnex的sheet
            sheet = wb.getSheetAt(excelReadContext.getSheetIndex());
            FormulaEvaluator createFormulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();
            Iterator<Row> rows = sheet.rowIterator();
            
            int startRowIndex = excelReadContext.getHeadRow();
            Map<String, Integer> head = new HashMap<>(5);
            Map<Integer, String> cloumToProperties = new HashMap<>(5);
            int lastColumn = 0;
            
            //迭代excel数据
            while (rows.hasNext()) {
                
                Row currentRow = rows.next();
                //如果当前行小于模板头 跳过当前行
                if (currentRow.getRowNum() < startRowIndex) {
                    continue;
                } else if (currentRow.getRowNum() == startRowIndex) {
                    
                    for (Cell currentCell : currentRow) {
                        
                        if (currentCell.getColumnIndex() > 100) {
                            throw new ExcelReadException(ExcelImportErrorMessageEnum.ORDERINFO_HEAD_OVERLIMIT_1902.getKey(),
                                    ExcelImportErrorMessageEnum.ORDERINFO_HEAD_OVERLIMIT_1902.getValue());
                        }
                        String value = getStringCell(currentCell, createFormulaEvaluator);
                        
                        if (StringUtils.isBlank(value)) {
                            break;
                            /*throw new ExcelReadException(ExcelImportErrorMessageEnum.ORDERINFO_HEAD_OVERLIMIT_1903.getKey(),
                                    String.format(ExcelImportErrorMessageEnum.ORDERINFO_HEAD_OVERLIMIT_1903.getValue(), currentCell.getColumnIndex()));*/
                        }
                        head.put(value, currentCell.getColumnIndex());
                        lastColumn = currentCell.getColumnIndex();
                    }
                    Map<String, String> columToPropertyMap = excelReadContext.getHeadToPropertyMap();
                    
                    for (Map.Entry<String, Integer> entry : head.entrySet()) {
                        
                        if (StringUtils.isBlank(columToPropertyMap.get(removeSpace(entry.getKey())))) {
                            log.info(entry.getKey());
                            throw new ExcelReadException(ExcelImportErrorMessageEnum.ORDERINFO_TEMPLATE_ERROR_9104.getKey(),ExcelImportErrorMessageEnum.ORDERINFO_TEMPLATE_ERROR_9104.getValue());
                        }else{
                            //保存 cloum 到 javabean 属性之间的关系
                            cloumToProperties.put(entry.getValue(),columToPropertyMap.get(removeSpace(entry.getKey())));
                        }
                        
                    }
                    continue;
                }
                
                //读取到空行后结束
                boolean b = checkRowNull(currentRow);
                if(b){
                    break;
                }
                
                //将单元格的数据封装到实体类
                Iterator<Cell> cellIterator = currentRow.iterator();
                int currentRowIndex = currentRow.getRowNum();
                T t = clazz.newInstance();
                while (cellIterator.hasNext()) {
                    Cell currentCell = cellIterator.next();
                    String properity = cloumToProperties.get(currentCell.getColumnIndex());
                    if(properity != null){
                        String getMethodName = "set" + properity.substring(0, 1).toUpperCase() + properity.substring(1);
                        Method getMethod = clazz.getMethod(getMethodName, String.class);
                        getMethod.invoke(t, getStringCell(currentCell,createFormulaEvaluator));
                        if(currentCell.getColumnIndex() == lastColumn){
                            break;
                        }
                    }
                    
                }
                //需要行数实体设置行数
                if(excelReadContext.isNeedRowIndex()){
                    String getMethodName = "setRowIndex";
                    Method getMethod = clazz.getMethod(getMethodName, String.class);
                    getMethod.invoke(t, String.valueOf(currentRowIndex + 1));
                }
                
                list.add(t);
            }
            
            return list;
        }catch (Exception e){
            log.error("{}excel读取异常，异常原因:{}", LOGGER_MSG, e);
            throw new ExcelReadException(ExcelImportErrorMessageEnum.RECEIVE_DATA_FAILD.getKey(),
                    e.getMessage());
        }finally {
            
            if (wb != null) {
                try {
                    wb.close();
                } catch (IOException e) {
                    log.error("关闭输出流异常!");
                }
            }
            
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("关闭输出流异常!");
                }
            }
        
        }
        
    }
    
    public String getStringCell(Cell cell,FormulaEvaluator createFormulaEvaluator) {
        
        String temp = "";
        CellType cellTypeEnum = cell.getCellType();
        switch (cellTypeEnum) {
            case STRING:
                temp = cell.getStringCellValue().trim();
                temp = StringUtils.isEmpty(temp) ? "" : temp;
                break;
            case NUMERIC:
                temp = new DecimalFormat("#.########").format(cell.getNumericCellValue());
                break;
            case FORMULA:
                try {
                    double numericCellValue = cell.getNumericCellValue();
                    temp = new BigDecimal(numericCellValue).setScale(2, RoundingMode.HALF_UP).toString();
                } catch (IllegalStateException e) {
                    temp = String.valueOf(cell.getRichStringCellValue());
                }
                break;
            case BLANK:
                temp = "";
                break;
            case BOOLEAN:
                temp = String.valueOf(cell.getBooleanCellValue());
                break;
            case ERROR:
                temp = "";
                break;
            default:
                temp = "";
            
        }
        return temp;
    }
    
    
    private boolean checkRowNull(Row currentRow) {
        for (Cell c : currentRow) {
            if (CellType.BLANK != c.getCellType() || StringUtils.isNotBlank(c.getStringCellValue())) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 移除空格的方法
     *
     * @param str
     * @return
     */
    public String removeSpace(String str) {
        if (!StringUtils.isEmpty(str)) {
            str = str.replace(" ", "");
        }
        return str;
    }
    
}
