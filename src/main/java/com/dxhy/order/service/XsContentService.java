package com.dxhy.order.service;


import com.dxhy.order.model.XsBook;
import com.dxhy.order.model.XsContent;

/**
 * @author ：杨士勇
 * @ClassName ：ApiOrderInfoService
 * @Description ：订单查询，插入，更新，删除操作
 * @date ：2018年7月21日 下午5:37:00
 */
public interface XsContentService {

    int insertSelective(XsContent xsContent);

    int updateByPrimaryKeySelective(XsContent con);
}
