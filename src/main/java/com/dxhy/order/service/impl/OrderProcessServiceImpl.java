package com.dxhy.order.service.impl;


import com.dxhy.order.dao.OrderProcessInfoMapper;
import com.dxhy.order.dao.TaxEquipmentInfoMapper;
import com.dxhy.order.model.OrderInfo;
import com.dxhy.order.model.OrderProcessInfo;
import com.dxhy.order.model.TaxEquipmentInfo;
import com.dxhy.order.service.ApiOrderProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OrderProcessServiceImpl implements ApiOrderProcessService {

    @Autowired
    private OrderProcessInfoMapper orderProcessInfoMapper;

    @Autowired
    private TaxEquipmentInfoMapper taxEquipmentInfoMapper;


    @Override
    public int updateOrderStatusByIds(List<String> list, String status) {
        return 0;
    }

    @Override
    public List<OrderProcessInfo> selectByPrimaryKeys(List<String> ids) {
        return orderProcessInfoMapper.selectByPrimaryKeys(ids);
    }

    @Override
    public OrderProcessInfo selectOrderProcessInfoByProcessId(String id) {
        return orderProcessInfoMapper.selectOrderProcessInfoByProcessId(id);
    }
    
    @Override
    public OrderProcessInfo queryOrderProcessInfoByFpqqlsh(String fpqqlsh) {
        return orderProcessInfoMapper.queryOrderProcessInfoByFpqqlsh(fpqqlsh);
    }
    
    @Override
    public List<OrderProcessInfo> selectOrderProcessInfoByDdqqpch(String ddqqpch) {
        return orderProcessInfoMapper.selectOrderProcessInfoByDdqqpch(ddqqpch);
    }
    
    @Override
    public int updateOrderProcessInfo(OrderInfo orderInfo) {
        return orderProcessInfoMapper.updateOrderProcessInfo(orderInfo);
    }


    @Override
    public int updateOrderProcessInfoByProcessId(OrderProcessInfo orderProcessInfo) {
        return orderProcessInfoMapper.updateOrderProcessInfoByProcessId(orderProcessInfo);
    }

    @Override
    public int insert(OrderProcessInfo record) {
        return orderProcessInfoMapper.insert(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateOrderDdztByList(List<OrderInfo> list, String key) {
        int i = 0;
        for (OrderInfo orderInfo : list) {
            OrderProcessInfo orderProcessInfo = new OrderProcessInfo();
            orderProcessInfo.setOrderInfoId(orderInfo.getId());
            String selectOrderProcessInfoId = orderProcessInfoMapper.selectOrderProcessInfoId(orderProcessInfo);
            OrderProcessInfo orderProcessInfo2 = new OrderProcessInfo();
            orderProcessInfo2.setId(selectOrderProcessInfoId);
            orderProcessInfo2.setDdzt(key);
            int updateByPrimaryKeySelective = orderProcessInfoMapper.updateOrderProcessInfoByProcessId(orderProcessInfo2);
            if (updateByPrimaryKeySelective <= 0) {
                return updateByPrimaryKeySelective;
            }
            i++;
        }
        return i;
        //return orderProcessInfoMapper.updateDdztByList(list, key);
    }

    /**
     * 根据销方税号,订单号,发票请求流水号进行查询orderprocess信息
     *
     * @param xsfNsrsbh
     * @param ddh
     * @param fpqqlsh
     * @return
     */
    @Override
    public List<LinkedHashMap> selectOrderProcessByFpqqlshDdhNsrsbh(String xsfNsrsbh, String ddh, String fpqqlsh) {
        return orderProcessInfoMapper.selectOrderProcessByFpqqlshDdhNsrsbh(xsfNsrsbh, ddh, fpqqlsh);
    }

    @Override
    public List<OrderProcessInfo> findChildList(String processId) {
        return null;
    }


    @Override
    public Map<String, Object> selectYwlxCountTotal(Map<String, Object> paramMap) {
        Map<String, Object> map = orderProcessInfoMapper.selectYwlxCountTotal(paramMap);
        return map;
    }

    @Override
    public OrderProcessInfo selectByOrderId(String orderId) {
        return orderProcessInfoMapper.selectByOrderId(orderId);
    }

    @Override
    public List<Map> selectHangXininfo(String s) {
        return orderProcessInfoMapper.selectHangXininfo(s);
    }

    @Override
    public TaxEquipmentInfo selectTaxByNsrsbh(String xhfNsrsbh) {
        return taxEquipmentInfoMapper.selectTaxByNsrsbh(xhfNsrsbh);
    }

    @Override
    public List<LinkedHashMap> selectC48FpkjLogList(String c48DbName, String kplsh) {
        return orderProcessInfoMapper.selectC48FpkjLogList(c48DbName,kplsh);
    }

    @Override
    public List<LinkedHashMap> selectC48FpkjXxList(String c48DbName, String kplsh) {
        return orderProcessInfoMapper.selectC48FpkjXxList(c48DbName,kplsh);
    }

    @Override
    public List<LinkedHashMap> selectbwFpkjLogList(String bwDbName, String kplsh) {
        return orderProcessInfoMapper.selectbwFpkjLogList(bwDbName,kplsh);
    }

    @Override
    public List<LinkedHashMap> selectbwFpkjXxList(String bwDbName, String kplsh) {
        return orderProcessInfoMapper.selectbwFpkjXxList(bwDbName,kplsh);
    }

    @Override
    public List<LinkedHashMap> selectA9FpkjLogList(String a9DbName, String kplsh) {
        return orderProcessInfoMapper.selectA9FpkjLogList(a9DbName,kplsh);
    }

    @Override
    public List<LinkedHashMap> selectA9FpkjXxList(String a9DbName, String kplsh) {
        return orderProcessInfoMapper.selectA9FpkjXxList(a9DbName,kplsh);
    }


}
