package com.dxhy.order.service.impl;


import com.dxhy.order.dao.OrderProcessInfoMapper;
import com.dxhy.order.dao.TaxEquipmentInfoMapper;
import com.dxhy.order.dao.XsBookMapper;
import com.dxhy.order.model.OrderInfo;
import com.dxhy.order.model.OrderProcessInfo;
import com.dxhy.order.model.TaxEquipmentInfo;
import com.dxhy.order.model.XsBook;
import com.dxhy.order.service.ApiOrderProcessService;
import com.dxhy.order.service.XsBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class XsBookServiceImpl implements XsBookService {

    @Autowired
    private XsBookMapper xsBookMapper;


    @Override
    public int insertSelective(XsBook book) {
        int i = xsBookMapper.insertSelective(book);
        return i;
    }

    @Override
    public List<XsBook> selectByCondition(XsBook book) {
        return xsBookMapper.selectByCondition(book);
    }
}
