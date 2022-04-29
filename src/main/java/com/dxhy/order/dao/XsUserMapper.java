package com.dxhy.order.dao;


import com.dxhy.order.model.XsUser;

import java.util.List;
import java.util.Map;



public interface XsUserMapper{


	int insertSelective(XsUser xsUser);

	List<XsUser> selectByCondition(XsUser xsUser);
}
