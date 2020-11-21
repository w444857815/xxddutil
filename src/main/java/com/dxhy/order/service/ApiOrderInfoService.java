package com.dxhy.order.service;


import com.dxhy.order.model.OrderInfo;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * @author ：杨士勇
 * @ClassName ：ApiOrderInfoService
 * @Description ：订单查询，插入，更新，删除操作
 * @date ：2018年7月21日 下午5:37:00
 */
public interface ApiOrderInfoService {

    /**
     * 根据orderID获取订单数据
     *
     * @param id
     * @return
     */
    OrderInfo selectOrderInfoByOrderId(String id);

    int insert(OrderInfo orderInfo);

    List<LinkedHashMap<String,Object>> executeSelectSql(String sqlStr);

    int executeInsertSql(String sqlStr);

    int executeDeleteSql(String sqlStr);

    int executeUpdateSql(String sqlStr);
}
