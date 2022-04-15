package com.dxhy.order.service.impl;


import com.dxhy.order.dao.XsBookMapper;
import com.dxhy.order.dao.XsContentMapper;
import com.dxhy.order.model.XsBook;
import com.dxhy.order.model.XsContent;
import com.dxhy.order.service.XsBookService;
import com.dxhy.order.service.XsContentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class XsContentServiceImpl implements XsContentService {

    @Autowired
    private XsContentMapper XsContentMapper;


    @Override
    public int insertSelective(XsContent content) {
        int i = XsContentMapper.insertSelective(content);
        return i;
    }

    @Override
    public int updateByPrimaryKeySelective(XsContent con) {
        return XsContentMapper.updateByPrimaryKeySelective(con);
    }
}
