package com.dxhy.order.dao;

import com.dxhy.order.model.WxUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WxUserDao {
    int deleteByPrimaryKey(Integer id);

    int insert(WxUser record);

    int insertSelective(WxUser record);

    WxUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WxUser record);

    int updateByPrimaryKey(WxUser record);

    List<WxUser> selectByopenId(@Param("openid") String openid);
}