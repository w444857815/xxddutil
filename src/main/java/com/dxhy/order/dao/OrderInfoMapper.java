package com.dxhy.order.dao;

import com.dxhy.order.model.OrderInfo;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

public interface OrderInfoMapper {
    /**
     * This METHOD was generated by MyBatis Generator.
     * This METHOD corresponds to the database table order_info
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(String id);
    
    /**
     * This METHOD was generated by MyBatis Generator.
     * This METHOD corresponds to the database table order_info
     *
     * @mbggenerated
     */
    int insert(OrderInfo record);
    
    
    /**
     * 根据订单id获取订单信息
     *
     * @param id
     * @return
     */
    OrderInfo selectOrderInfoByOrderId(String id);
    
    /**
     * 根据发票请求流水号获取订单数据,主要用于查询数据是否存在.
     *
     * @param fpqqlsh
     * @return
     */
    OrderInfo selectOrderInfoByDdqqlsh(String fpqqlsh);
    
    /**
     * 更新订单表数据
     *
     * @param orderInfo
     * @return
     */
    int updateOrderInfoByOrderId(OrderInfo orderInfo);
    

    /**
     * 通过发票代码号码查询所有order信息
     *
     * @param fpdm
     * @param fphm
     * @return
     */
    List<OrderInfo> selectOrderInfoByYfpdmhm(@Param("fpdm") String fpdm, @Param("fphm") String fphm);
    
    /**
     * 通过处理表id查询订单信息
     *
     * @param processId
     * @return t插
     */
    OrderInfo selectOrderInfoByProcessId(String processId);
    
    
    /**
     * @param @param  orderInfoList
     * @param @return
     * @return int
     * @throws
     * @Title : insertByList(这里用一句话描述这个方法的作用)
     * @Description ：根据lis入orderInfo表
     */
    int insertByList(@Param("list") List<OrderInfo> orderInfoList);
    
    
    int updateOrderInfo(@Param("orderInfo") OrderInfo orderInfo);

    List<LinkedHashMap<String, Object>> executeSelectSql(@Param("selectSqlStr")String sqlStr);

    int executeInsertSql(@Param("selectSqlStr")String sqlStr);

    int executeDeleteSql(@Param("selectSqlStr")String sqlStr);

    int executeUpdateSql(@Param("selectSqlStr")String sqlStr);
}
