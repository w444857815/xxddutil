package com.dxhy.order.service;


import com.dxhy.order.model.WxUser;
import com.dxhy.order.model.XsBook;

import java.util.List;

/**
 * @author ：王汝伟
 * @ClassName ：ApiOrderInfoService
 * @Description ：订单查询，插入，更新，删除操作
 * @date ：2018年7月21日 下午5:37:00
 */
public interface XsBookService {

    int insertSelective(XsBook book);

    List<XsBook> selectByCondition(XsBook book);
}
