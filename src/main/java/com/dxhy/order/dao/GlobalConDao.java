package com.dxhy.order.dao;


import com.dxhy.order.model.GlobalCon;

public interface GlobalConDao {
    int deleteByPrimaryKey(Integer id);

    int insert(GlobalCon record);

    int insertSelective(GlobalCon record);

    GlobalCon selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GlobalCon record);

    int updateByPrimaryKey(GlobalCon record);
}