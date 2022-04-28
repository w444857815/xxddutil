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

import java.util.List;

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

    @Override
    public List<XsContent> selectByCondition(XsContent xscon) {
        return XsContentMapper.selectByCondition(xscon);
    }

    @Override
    public int selectCountByCondition(XsContent xscon) {
        return XsContentMapper.selectCountByCondition(xscon);
    }

    @Override
    public List<XsContent> selectByConditionNoCon(XsContent conParams) {
        return XsContentMapper.selectByConditionNoCon(conParams);
    }

    @Override
    public List<XsContent> selectByUrls(List<String> jsoupUrlList) {
        return XsContentMapper.selectByUrls(jsoupUrlList);
    }

    @Override
    public XsContent selectLastZjByConditionNoCon(XsContent conParams) {
        return XsContentMapper.selectLastZjByConditionNoCon(conParams);
    }
}
