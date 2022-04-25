package com.dxhy.order.dao;

import com.dxhy.order.model.Article;
import com.dxhy.order.model.TjSongmovie;

import java.util.List;

public interface TjSongmovieMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Article record);

    int insertSelective(TjSongmovie record);

    Article selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Article record);

    int updateByPrimaryKey(Article record);

    List<TjSongmovie> selectByCondition(TjSongmovie book);
}