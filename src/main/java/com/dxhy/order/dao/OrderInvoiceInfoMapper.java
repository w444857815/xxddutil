package com.dxhy.order.dao;

import com.dxhy.order.model.OrderInvoiceInfo;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface OrderInvoiceInfoMapper {
    /**
     * This METHOD was generated by MyBatis Generator.
     * This METHOD corresponds to the database table order_invoice_info
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(String id);
    
    /**
     * This METHOD was generated by MyBatis Generator.
     * This METHOD corresponds to the database table order_invoice_info
     *
     * @mbggenerated
     */
    int insert(OrderInvoiceInfo record);
    
    /**
     * This METHOD was generated by MyBatis Generator.
     * This METHOD corresponds to the database table order_invoice_info
     *
     * @mbggenerated
     */
    OrderInvoiceInfo selectByPrimaryKey(String id);
    
    /**
     * This METHOD was generated by MyBatis Generator.
     * This METHOD corresponds to the database table order_invoice_info
     *
     * @mbggenerated
     */
    int updateOrderInvoiceInfoByInvoiceId(OrderInvoiceInfo record);
    
    /**
     * selectByYfp
     *
     * @param fpdm
     * @param fphm
     * @return
     */
    OrderInvoiceInfo selectOrderInvoiceInfoByFpdmAndFphm(@Param("fpdm") String fpdm, @Param("fphm") String fphm);
    
    /**
     * 查询发票表id
     *
     * @param orderInvoiceInfo
     * @return
     */
    OrderInvoiceInfo selectOrderInvoiceInfoId(OrderInvoiceInfo orderInvoiceInfo);
    
    List<OrderInvoiceInfo> selectInvoiceByOrder(Map map);
    
    /**
     * selectByOrderIdArray
     *
     * @param orderIdArrays
     * @return
     */
    List<OrderInvoiceInfo> selectByOrderIdArray(@Param("orderIdArrays") String[] orderIdArrays);
    

    /**
     * updateInvoiceStatusByKplsh
     *
     * @return
     */
    int updateInvoiceStatusByKplsh(OrderInvoiceInfo orderInvoiceInfo);
    
    /**
     * selectInvoiceInfoByKplsh
     *
     * @param kplsh
     * @return
     */
    OrderInvoiceInfo selectInvoiceInfoByKplsh(String kplsh);
    
    /**
     * 根据发票请求流水号(订单请求流水号)查询订单发票数据,主要用于判断数据是否存在
     * 不需要加缓存
     *
     * @param fpqqlsh
     * @return
     */
    OrderInvoiceInfo selectOrderInvoiceInfoByFpqqlsh(String fpqqlsh);

    /**
     * 通过orderId查询发票信息
     *
     * @param orderId
     * @return
     */
    OrderInvoiceInfo selectInvoiceInfoByOrderId(String orderId);
    
    /**
     * @param @param  updateList
     * @param @return
     * @return int
     * @throws
     * @Title : updateByList(这里用一句话描述这个方法的作用)
     * @Description ：根据list更新orderInvoiceInfo
     */

    int updateByList(OrderInvoiceInfo updateList);
    
    /**
     * @param @param  map
     * @param @return
     * @return Map
     * @throws
     * @Title : queryCountByMap(这里用一句话描述这个方法的作用)
     * @Description ：根据条件统计总金额数量
     */
    Map<String, Object> queryCountByMap(Map<String, Object> map);
    
    /**
     * @param @param  kpzt
     * @param @param  pushStatus
     * @param @param  date
     * @param @return
     * @return List<OrderInvoiceInfo>
     * @throws
     * @Title : selectInvoiceInfoByPushStatus(这里用一句话描述这个方法的作用)
     * @Description ：根据开票状态 推送状态 和时间查询发票
     */
    List<OrderInvoiceInfo> selectInvoiceInfoByPushStatus(@Param("paramMap") Map paramMap);
    
    OrderInvoiceInfo selectOrderInvoiceInfoByFpdmFphmAndNsrsbh(@Param("fpdm") String fpdm, @Param("fphm") String fphm, @Param("nsrsbh") List<String> nsrsbh);
    
    List<OrderInvoiceInfo> selectInvoiceInfoByEmailPushStatus(@Param("kpztList") List<String> kpzt, @Param("emailPushStatus") List<String> pushStatus, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("fpzldm") String fpzldm);
    

    /**
     * 根据订单流水号修改
     *
     * @param orderInvoiceInfo
     * @return int
     * @author: 陈玉航
     * @date: Created on 2019年7月27日 下午4:28:36
     */
    int updateOrderInvoiceInfoByDdqqlsh(OrderInvoiceInfo orderInvoiceInfo);
    
    Map<String, String> getPrintTestInfo(@Param("xhfNsrsbh") String xhfNsrsbh, @Param("fpzldms") List<String> fpzldms);
    
    OrderInvoiceInfo selectByHzxxbbh(@Param("hzxxbbh") String hzxxbbh);

    /**
     * 查询发票表id
     *
     * @param orderInvoiceInfo
     * @return
     */
    OrderInvoiceInfo selectOrderInvoiceInfo(@Param("orderInvoiceInfo") OrderInvoiceInfo orderInvoiceInfo, @Param("shList") List<String> shList);

    List<LinkedHashMap> selectOrderProcessByFpqqlshDdh(@Param("ddh") String ddh, @Param("fpqqlsh")String fpqqlsh);
}
