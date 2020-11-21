package com.dxhy.order.service.impl;


import com.dxhy.order.dao.OrderInvoiceInfoMapper;
import com.dxhy.order.dao.OrderItemInfoMapper;
import com.dxhy.order.dao.OrderProcessInfoMapper;
import com.dxhy.order.model.OrderInfo;
import com.dxhy.order.model.OrderInvoiceInfo;
import com.dxhy.order.model.OrderItemInfo;
import com.dxhy.order.service.ApiOrderInfoService;
import com.dxhy.order.service.ApiOrderInvoiceInfoService;
import com.dxhy.order.service.ApiOrderItemInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
@Slf4j
public class OrderInvoiceInfoServiceImpl implements ApiOrderInvoiceInfoService {
    @Autowired
    private OrderInvoiceInfoMapper orderInvoiceInfoMapper;
    @Autowired
    private OrderProcessInfoMapper orderProcessInfoMapper;


    @Override
    public List<OrderInvoiceInfo> selectInvoiceListByOrderIds(String[] orderIds) {
        List<OrderInvoiceInfo> orderInvoiceInfos = new ArrayList<>();
        for (String string : orderIds) {
            OrderInvoiceInfo orderInvoiceInfo1 = new OrderInvoiceInfo();
            orderInvoiceInfo1.setOrderInfoId(string);
            OrderInvoiceInfo selectInvoiceInfoByOrderId = orderInvoiceInfoMapper.selectOrderInvoiceInfoId(orderInvoiceInfo1);
            orderInvoiceInfos.add(selectInvoiceInfoByOrderId);
        }
        return orderInvoiceInfos;
    }

    @Override
    public int insertByList(List<OrderInvoiceInfo> insertList) {
        int i = 0;
        for (OrderInvoiceInfo orderInvoiceInfo : insertList) {
            int insertSelective = orderInvoiceInfoMapper.insert(orderInvoiceInfo);
            if (insertSelective <= 0) {
                return insertSelective;
            }
            i++;
        }
        return i;
    }
}

