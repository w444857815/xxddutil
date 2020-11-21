package com.dxhy.order.service;

import com.dxhy.order.model.OrderItemInfo;

import java.util.List;

/**
 * 订单明细接口
 *
 * @author ZSC-DXHY
 */
public interface ApiOrderItemInfoService {
    /**
     * 根据id查询订单明细
     *
     * @param id
     * @return
     */
    OrderItemInfo selectByPrimaryKey(String id);
    
    
    /**
     * 根据订单id查询订单明细
     *
     * @param orderId
     * @return
     */
    List<OrderItemInfo> selectOrderItemInfoByOrderId(String orderId);

    /**
     * 根据主键删除订单明细
     *
     * @param id
     * @return
     */
    int deleteByPrimaryKey(String id);
    
    /**
     * 根据订单id删除订单明细
     *
     * @param orderId
     * @return
     */
    int deleteOrderItemInfoByOrderId(String orderId);

    /**
     * 插入订单明细
     *
     * @param orderItemInfo
     * @return
     */
    int insertOrderItemInfo(OrderItemInfo orderItemInfo);
    
    int insertOrderItemByList(List<OrderItemInfo> orderItemInfos);
    
}
