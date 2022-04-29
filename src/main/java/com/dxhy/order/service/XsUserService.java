package com.dxhy.order.service;


import com.dxhy.order.model.XsContent;
import com.dxhy.order.model.XsUser;

import java.util.List;

/**
 * @author ：王汝伟
 * @ClassName ：ApiOrderInfoService
 * @Description ：订单查询，插入，更新，删除操作
 * @date ：2018年7月21日 下午5:37:00
 */
public interface XsUserService {

    int insertSelective(XsUser xsUser);

    List<XsUser> selectByCondition(XsUser xsUser);
}
