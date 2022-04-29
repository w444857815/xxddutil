package com.dxhy.order.service.impl;


import com.dxhy.order.dao.ArticleDao;
import com.dxhy.order.dao.WxUserDao;
import com.dxhy.order.model.Article;
import com.dxhy.order.model.WxUser;
import com.dxhy.order.service.ApiWankeService;
import com.dxhy.order.service.ApiWxUserService;
import com.dxhy.order.util.PageUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ：王汝伟
 * @ClassName ：OrderInfoServiceImpl
 * @Description ：订单信息实现类
 * @date ：2018年7月21日 下午5:53:27
 */
@Slf4j
@Service
public class WxUserServiceImpl implements ApiWxUserService {

    @Autowired
    private WxUserDao wxUserDao;


    @Override
    public List<WxUser> selectByopenId(String openid) {
        return wxUserDao.selectByopenId(openid);
    }

    @Override
    public int insertSelective(WxUser wxuInfo) {

        return wxUserDao.insertSelective(wxuInfo);
    }
}
