package com.dxhy.order.dao;

import com.dxhy.order.model.Article;
import com.dxhy.order.model.XsBook;
import com.dxhy.order.model.XsContent;

public interface XsContentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Article record);

    int insertSelective(XsContent record);

    Article selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(XsContent record);

    int updateByPrimaryKey(Article record);

}