package com.dxhy.order.service.impl;


import com.dxhy.order.dao.XsContentMapper;
import com.dxhy.order.dao.XsUserMapper;
import com.dxhy.order.model.XsContent;
import com.dxhy.order.model.XsUser;
import com.dxhy.order.service.XsContentService;
import com.dxhy.order.service.XsUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class XsUserServiceImpl implements XsUserService {

    @Autowired
    private XsUserMapper xsUserMapper;


    @Override
    public int insertSelective(XsUser xsUser) {
        return xsUserMapper.insertSelective(xsUser);
    }

    @Override
    public List<XsUser> selectByCondition(XsUser xsUser) {
        return xsUserMapper.selectByCondition(xsUser);
    }
}
