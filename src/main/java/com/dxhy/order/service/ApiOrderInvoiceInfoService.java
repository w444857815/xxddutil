package com.dxhy.order.service;


import com.dxhy.order.model.OrderInvoiceInfo;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 订单发票表接口
 *
 * @author ZSC-DXHY
 */
public interface ApiOrderInvoiceInfoService {

    List<OrderInvoiceInfo> selectInvoiceListByOrderIds(String[] orderIds);

    /**
     * 根据list插入orderInvoiceInfo
     *
     * @param insertList
     * @return
     */
    int insertByList(List<OrderInvoiceInfo> insertList);

    List<LinkedHashMap> selectOrderProcessByFpqqlshDdh(String ddh, String fpqqlsh);
}

