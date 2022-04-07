package com.dxhy.order.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Excel模板下载枚举类
 *
 * @author wrw
 */
public enum DownloadExcelEnum {

    EXCEL_ORDER_IMPORT("orderImportExcel.xlsx", "订单导入模板.xlsx"),

    LOGO("logo.png", "logo.png"),

    /**
     * Excel导出模板
     */
    EXCEL_EXPORT_INVOICE_QUERY("expProductExcel.xlsx", "发票统计报表导出模板"),
    EXCEL_EXPORT_INVOICE_DECLARE("exportDeclareExcel.xlsx", "发票销项统计导出模板"),
    
    /**
     * Excel模板下载数据
     */
    EXCEL_BUYER("BuyerExcel.xlsx", "购方信息导入模板"),
    EXCEL_COMMODITY_CODE("CommodityCodeExcel.xlsx", "商品税编模板"),
    EXCEL_GROUP_TAX_CLASS_CODE("GroupTaxClassCodeExcel.xlsx", "集团税编模板"),
    EXCEL_SPECIAL_INVOICE("SpecialInvoiceReversalExcel.xlsx", "批量导入红字信息表模板"),
    EXCEL_NCP("NcpImportExcel.xlsx", "农产品导入模板"),
    EXCEL_JDC("JdcImportExcel.xlsx", "机动车销售统一发票订单导入模板"),
    EXCEL_ESC("EscImportExcel.xlsx", "二手车销售统一发票订单导入模板"),
    EXCEL_DICTIONARY("DictionaryExcel.xlsx", "字典表导入模板"),
    EXCEL_ORDER("OrderExcel.xlsx", "订单导入模板"),
    EXCEL_INVOICE_COUNT("InvoiceCountExcel.xlsx", "批量下架模板"),
    EXCEL_VEHICLE_CODE("VehiclesCodeExcel.xlsx", "车辆税编模板"),
    EXCEL_HZXXB("SpecialInvoiceReversalsExcel.xlsx", "红字信息表模板");
    
    /**
     * key
     */
    private final String key;
    
    /**
     * 值
     */
    private final String value;
    
    public String getKey() {
        return key;
    }
    
    public String getValue() {
        return value;
    }
    
    DownloadExcelEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }
    
    public static DownloadExcelEnum getCodeValue(String key) {
        
        for (DownloadExcelEnum item : values()) {
            if (item.getKey().equals(key)) {
                return item;
            }
        }
        return null;
    }
    
    public static List<String> getValues() {
        
        List<String> resultList = new ArrayList<>();
        for (DownloadExcelEnum item : values()) {
            resultList.add(item.getValue());
        }
        return resultList;
    }
    
    
}
