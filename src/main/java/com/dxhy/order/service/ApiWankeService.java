package com.dxhy.order.service;


import com.dxhy.order.model.Article;
import com.dxhy.order.model.OrderInfo;
import com.dxhy.order.util.PageUtils;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author ：杨士勇
 * @ClassName ：ApiOrderInfoService
 * @Description ：订单查询，插入，更新，删除操作
 * @date ：2018年7月21日 下午5:37:00
 */
public interface ApiWankeService {


    PageUtils selectArtilesList(String seaStr, String pageSize, String currPage);

    Article selectArtileById(Integer id);

    int insertSelective(Article article);

    List<Article> selectArtilesListByOpenid(String openid);

    //获取公告信息
    List<Article> selectGgArtilesList(String s);
}
