package com.dxhy.order.dao;

import com.dxhy.order.model.Article;
import com.dxhy.order.model.XsBook;
import com.dxhy.order.model.XsContent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface XsContentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Article record);

    int insertSelective(XsContent record);

    Article selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(XsContent record);

    int updateByPrimaryKey(Article record);

    List<XsContent> selectByCondition(XsContent xscon);

    int selectCountByCondition(XsContent xscon);

    List<XsContent> selectByConditionNoCon(XsContent conParams);

    List<XsContent> selectByUrls(@Param("list") List<String> list);

    XsContent selectLastZjByConditionNoCon(XsContent conParams);
}