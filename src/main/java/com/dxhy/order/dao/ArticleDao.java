package com.dxhy.order.dao;

import com.dxhy.order.model.Article;
import com.dxhy.order.util.PageUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticleDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Article record);

    int insertSelective(Article record);

    Article selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Article record);

    int updateByPrimaryKey(Article record);

    List<Article> selectArtilesList(@Param("seaStr") String seaStr);

    List<Article> selectArtilesListByOpenid(@Param("openid")String openid);

    List<Article> selectGgArtilesList(String s);
}