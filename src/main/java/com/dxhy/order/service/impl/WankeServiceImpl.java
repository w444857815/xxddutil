package com.dxhy.order.service.impl;


import com.dxhy.order.dao.ArticleDao;
import com.dxhy.order.dao.GlobalConDao;
import com.dxhy.order.dao.OrderInfoMapper;
import com.dxhy.order.model.Article;
import com.dxhy.order.model.GlobalCon;
import com.dxhy.order.model.OrderInfo;
import com.dxhy.order.service.ApiOrderInfoService;
import com.dxhy.order.service.ApiWankeService;
import com.dxhy.order.util.PageUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author ：王汝伟
 * @ClassName ：OrderInfoServiceImpl
 * @Description ：订单信息实现类
 * @date ：2018年7月21日 下午5:53:27
 */
@Slf4j
@Service
public class WankeServiceImpl implements ApiWankeService {

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private GlobalConDao globalConDao;


    @Override
    public PageUtils selectArtilesList(String seaStr, String pageSize, String currPage) {
        if(StringUtils.isBlank(pageSize)){
            pageSize = "10";
        }
        if(StringUtils.isBlank(currPage)){
            currPage = "1";
        }
        PageHelper.startPage(Integer.parseInt(currPage), Integer.parseInt(pageSize));

        List<Article> list = articleDao.selectArtilesList(seaStr);
        PageInfo<Article> pageInfo = new PageInfo<>(list);
        PageUtils page = new PageUtils(pageInfo.getList(), (int) pageInfo.getTotal(), pageInfo.getPageSize(),
                pageInfo.getPageNum());

        log.info("返回值{} ", page);
        return page;
    }

    @Override
    public Article selectArtileById(Integer id) {
        return articleDao.selectByPrimaryKey(id);
    }

    @Override
    public int insertSelective(Article article) {
        return articleDao.insertSelective(article);
    }

    @Override
    public List<Article> selectArtilesListByOpenid(String openid) {
        return articleDao.selectArtilesListByOpenid(openid);
    }

    @Override
    public List<Article> selectGgArtilesList(String s) {
        return articleDao.selectGgArtilesList(s);
    }

    @Override
    public int updateByPrimaryKeySelective(Article upNum) {
        return articleDao.updateByPrimaryKeySelective(upNum);
    }

    @Override
    public GlobalCon selectGlobalCon(String id) {
        return globalConDao.selectByPrimaryKey(Integer.parseInt(id));
    }
}
