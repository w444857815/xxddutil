package com.dxhy.order.service.impl;

import com.dxhy.order.dao.OrderItemInfoMapper;
import com.dxhy.order.model.OrderItemInfo;
import com.dxhy.order.service.ApiOrderItemInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemInfoServiceImpl implements ApiOrderItemInfoService {

    @Autowired
    private OrderItemInfoMapper orderItemInfoMapper;

    @Override
    public OrderItemInfo selectByPrimaryKey(String id) {
        return orderItemInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<OrderItemInfo> selectOrderItemInfoByOrderId(String orderId) {
        return orderItemInfoMapper.selectOrderItemInfoByOrderId(orderId);
    }
    
    @Override
    public int deleteByPrimaryKey(String id) {
        return orderItemInfoMapper.deleteByPrimaryKey(id);
    }
    
    @Override
    public int deleteOrderItemInfoByOrderId(String orderId) {
        return orderItemInfoMapper.deleteOrderItemInfoByOrderId(orderId);
    }

    @Override
    public int insertOrderItemInfo(OrderItemInfo orderItemInfo) {
        return orderItemInfoMapper.insertOrderItemInfo(orderItemInfo);
    }
    
    @Override
    public int insertOrderItemByList(List<OrderItemInfo> orderItemInfos) {
        return orderItemInfoMapper.insertOrderItemByList(orderItemInfos);
    }
    
}
