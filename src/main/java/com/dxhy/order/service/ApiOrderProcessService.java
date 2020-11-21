package com.dxhy.order.service;

import com.dxhy.order.model.OrderInfo;
import com.dxhy.order.model.OrderProcessInfo;

import java.util.List;
import java.util.Map;

/**
 * 订单处理表接口
 *
 * @author ZSC-DXHY
 */
public interface ApiOrderProcessService {
    /**
     * 根据id修改订单状态
     * @param list
     * @param status
     * @return
     */
    int updateOrderStatusByIds(List<String> list, String status);

    /**
     * 根据id修改订单状态
     * @param ids
     * @return
     */
    List<OrderProcessInfo> selectByPrimaryKeys(List<String> ids);
    /**
     * 根据主键查询orderprocess信息
     *
     * @param id
     * @return
     */
    OrderProcessInfo selectOrderProcessInfoByProcessId(String id);
    
    /**
     * 根据发票请求流水号获取发票处理表数据
     *
     * @param fpqqlsh
     * @return
     */
    OrderProcessInfo queryOrderProcessInfoByFpqqlsh(String fpqqlsh);
    
    /**
     * 根据订单请求批次号获取发票处理表数据
     *
     * @param ddqqpch
     * @return
     */
    List<OrderProcessInfo> selectOrderProcessInfoByDdqqpch(String ddqqpch);
    

    /**
     * 更新orderprocessInfo表
     *
     * @param orderProcessInfo
     * @return
     */
    int updateOrderProcessInfoByProcessId(OrderProcessInfo orderProcessInfo);

    /**
     * 插入orderprocessInfo 数据
     *
     * @param record
     * @return
     */
    int insert(OrderProcessInfo record);

    /**
     * 根据list更新订单状态
     *
     * @param list
     * @param key
     * @return
     */
    int updateOrderDdztByList(List<OrderInfo> list, String key);

    /**
     * 根据销方税号,订单号,发票请求流水号进行查询orderprocess信息
     *
     * @param xsfNsrsbh
     * @param ddh
     * @param fpqqlsh
     * @return
     */
    List<OrderProcessInfo> selectOrderProcessByFpqqlshDdhNsrsbh(String xsfNsrsbh, String ddh, String fpqqlsh);

    /**
     * 根据orderProcessId查询最终的子订单
     */
    List<OrderProcessInfo> findChildList(String processId);
    
    /**
     * 编辑购方信息,更新
     *
     * @param orderInfo
     * @return
     */
    int updateOrderProcessInfo(OrderInfo orderInfo);
    


    /**
     * @param paramMap
     * @return 业务类型统计，统计各项，总金额，票数这些
     * IOrderInfoService.java
     * author wangruwei
     * 2019年7月16日
     */
    Map<String, Object> selectYwlxCountTotal(Map<String, Object> paramMap);

    OrderProcessInfo selectByOrderId(String orderId);
}
