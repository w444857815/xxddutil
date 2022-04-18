package com.dxhy.order.dao;

import com.dxhy.order.model.Article;
import com.dxhy.order.model.XsBook;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface XsBookMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Article record);

    int insertSelective(XsBook record);

    Article selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Article record);

    int updateByPrimaryKey(Article record);

    List<XsBook> selectByCondition(XsBook book);
}