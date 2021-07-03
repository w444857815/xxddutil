package com.dxhy.order.service.impl;


import com.dxhy.order.service.ApiOrderInfoService;
import com.dxhy.order.dao.OrderInfoMapper;
import com.dxhy.order.model.OrderInfo;
import com.dxhy.order.util.PageUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author ：杨士勇
 * @ClassName ：OrderInfoServiceImpl
 * @Description ：订单信息实现类
 * @date ：2018年7月21日 下午5:53:27
 */
@Slf4j
@Service
public class OrderInfoServiceImpl implements ApiOrderInfoService {

    @Autowired
    private OrderInfoMapper orderInfoMapper;


    @Override
    public OrderInfo selectOrderInfoByOrderId(String id) {
        return orderInfoMapper.selectOrderInfoByOrderId(id);
    }

    @Override
    public int insert(OrderInfo orderInfo) {
        return orderInfoMapper.insert(orderInfo);
    }

    @Override
    public List<LinkedHashMap<String, Object>> executeSelectSql(String sqlStr) {

        return orderInfoMapper.executeSelectSql(sqlStr);
    }

    @Override
    public int executeInsertSql(String sqlStr) {
        return orderInfoMapper.executeInsertSql(sqlStr);
    }

    @Override
    public int executeDeleteSql(String sqlStr) {
        return orderInfoMapper.executeDeleteSql(sqlStr);
    }

    @Override
    public int executeUpdateSql(String sqlStr) {
        return orderInfoMapper.executeUpdateSql(sqlStr);
    }

    @Override
    public PageUtils selectOrderInfo(String seaStr, String pageSize, String currPage) {

        return null;
    }
}
