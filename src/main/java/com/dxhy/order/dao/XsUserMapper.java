package com.dxhy.order.dao;


import com.dxhy.order.model.XsUser;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public interface XsUserMapper{


	int insertSelective(XsUser xsUser);

	List<XsUser> selectByCondition(XsUser xsUser);
}
