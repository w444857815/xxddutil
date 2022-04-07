package com.dxhy.order.service;

import com.dxhy.order.exception.ExcelReadException;
import com.dxhy.order.exception.OrderReceiveException;
import com.dxhy.order.model.excel.NewOrderExcel;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * @author ：杨士勇
 * @ClassName ：ExcelReadService
 * @Description ：excel读取service
 * @date ：2018年9月11日 下午2:22:20
 */

public interface ExcelReadService {
    
    /**
     * 读取excel中的订单信息封装到invoiceexcel中
     *
     * @param file
     * @return
     * @throws OrderReceiveException
     * @throws ExcelReadException
     * @throws IOException
     */
    List<NewOrderExcel> readOrderInfoFromExcelxls(MultipartFile file) throws OrderReceiveException, ExcelReadException, IOException;
    
    /**
     * 把表格转换为数组对象
     *
     * @param file
     * @param tClass
     * @param <T>
     * @param headMap
     * @return
     * @throws ExcelReadException
     */
    <T> List<T> transExcelToList(MultipartFile file, Class<T> tClass, Map<String, String> headMap) throws ExcelReadException;
    
    /**
     * 校验订单信息
     *
     * @param readOrderInfoFromExcelxls
     * @param xhfNsrsbh
     * @return
     */
    Map<String, Object> examinByMap(Map<String, List<NewOrderExcel>> readOrderInfoFromExcelxls, String xhfNsrsbh);
    
    /**
     * 导出excel明细
     *
     * @param file
     * @param out
     * @param map
     * @param shList
     * @throws FileNotFoundException
     */
    void exportInvoiceDetailExcel(File file, OutputStream out, Map<String, Object> map, List<String> shList) throws FileNotFoundException;
    
    /**
     * 导出订单数据
     *
     * @param paramMap
     * @param outputStream
     * @param shList
     */
    void exportOrderInfo(Map<String, Object> paramMap, OutputStream outputStream, List<String> shList);
    
    /**
     * excel数据转换
     *
     * @param newOrderExcels
     * @param paramMap
     * @return
     * @throws UnsupportedEncodingException
     * @throws OrderReceiveException
     * @throws OrderSeparationException
     */
    //List<CommonOrderInfo> excelToOrderInfo(Map<String, List<NewOrderExcel>> newOrderExcels, Map<String, String> paramMap) throws UnsupportedEncodingException, OrderReceiveException, OrderSeparationException;


    /**
     * 导出发票主体信息
     *
     * @param file
     * @param out
     * @param newMap
     * @param shList
     * @throws IOException
     */
    void exportInvocie(FileInputStream file, OutputStream out, Map<String, Object> newMap, List<String> shList) throws IOException;
}
