package com.dxhy.order.controller;

import com.dxhy.order.model.OrderInfo;
import com.dxhy.order.model.OrderInvoiceInfo;
import com.dxhy.order.model.OrderItemInfo;
import com.dxhy.order.model.OrderProcessInfo;
import com.dxhy.order.service.ApiOrderInfoService;
import com.dxhy.order.service.ApiOrderInvoiceInfoService;
import com.dxhy.order.service.ApiOrderItemInfoService;
import com.dxhy.order.service.ApiOrderProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * @ClassName OrderController
 * @Description TODO
 * @Author wangruwei
 * @Date 2020-10-13 11:36
 * @Version 1.0
 */
@Slf4j
@Controller
@RequestMapping("/order")
public class OrderController {

    private static String sucCode = "0000";

    private static String failCode = "9999";

    @Autowired
    private ApiOrderInfoService orderinfoService;

    @Autowired
    private ApiOrderItemInfoService orderIteminfoService;

    @Autowired
    private ApiOrderProcessService orderProcessService;

    @Autowired
    private ApiOrderInvoiceInfoService orderInvoiceInfoService;


    @GetMapping("/page")
    public ModelAndView page(ModelAndView model){
        model.setViewName("enterPage");
        return model;
    }

    @RequestMapping(value="/hehe")
    @ResponseBody
    public Map<String,Object> addYwlx(String orderId,String newkplsh){
        List<String> resultSb = new LinkedList<>();
        Map<String,Object> map = new HashMap<String,Object>();


        OrderInfo orderInfo = orderinfoService.selectOrderInfoByOrderId(orderId);
        if(null == orderInfo){
            return getFailRtn("没有找到此订单");
        }

        String oldDdh = orderInfo.getDdh();
        String newDdh = "";
        if(oldDdh.length()>=20){
            String oldDdhQ = oldDdh.substring(0, oldDdh.length()-1);
            String oldDdhH = oldDdh.substring(oldDdh.length()-1);
            if("0".equals(oldDdhH)){
                oldDdhH = "1";
            }else{
                oldDdhH = "O";
            }
            newDdh = oldDdhQ + oldDdhH;
        }else{
            newDdh = oldDdh + "1";
        }

        try {
            String newOiId = orderId+"1";
        /*
        OrderInfo开始
        **/
            //1
            orderInfo.setId(newOiId);
            //2
            orderInfo.setProcessId(orderInfo.getProcessId()+"1");
            //3
            orderInfo.setFpqqlsh(orderInfo.getFpqqlsh()+"1");


            //4
            orderInfo.setDdh(newDdh);

            int i = orderinfoService.insert(orderInfo);
            if(i==1){
                resultSb.add("orderInfo插入成功，id: "+newOiId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultSb.add("orderInfo插入失败");
        }
        /*
        OrderInfo结束
        **/

        /*
        OrderItemInfo开始
        **/

        try {
            List<OrderItemInfo> itemList = orderIteminfoService.selectOrderItemInfoByOrderId(orderId);
            int insertSize = 0;
            for (int j = 0; j < itemList.size(); j++) {
                OrderItemInfo item = itemList.get(j);
                item.setId(item.getId()+"1");
                item.setOrderInfoId(item.getOrderInfoId()+"1");
                int inser = orderIteminfoService.insertOrderItemInfo(item);
                insertSize = insertSize+inser;
            }
            if(insertSize==itemList.size()){
                resultSb.add("orderItemInfo插入成功，条数: "+insertSize);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultSb.add("orderItemInfo插入失败");
        }

        /*
        OrderItemInfo结束
        **/

        /*
        OrderProcessInfo开始
        **/

        try {
            OrderProcessInfo orderProcessInfo = orderProcessService.selectByOrderId(orderId);
            //1
            orderProcessInfo.setId(orderProcessInfo.getId()+"1");
            //2
            orderProcessInfo.setOrderInfoId(orderProcessInfo.getOrderInfoId()+"1");
            if(!StringUtils.isEmpty(orderProcessInfo.getDdqqpch())){
                //3
                orderProcessInfo.setDdqqpch(orderProcessInfo.getDdqqpch()+"1");
            }
            //4
            orderProcessInfo.setFpqqlsh(orderProcessInfo.getFpqqlsh()+"1");
            //5
            orderProcessInfo.setDdh(newDdh);
            int opiNum = orderProcessService.insert(orderProcessInfo);
            if(opiNum==1){
                resultSb.add("orderProcessInfo插入成功，opiId: "+orderProcessInfo.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultSb.add("orderProcessInfo插入失败");
        }

        /*
        OrderProcessInfo结束
        **/

        /*
        OrderInvoiceInfo开始
        **/
        try {
            String[] orderIds = new String[]{orderId};
            List<OrderInvoiceInfo> oiiList = orderInvoiceInfoService.selectInvoiceListByOrderIds(orderIds);
            if(oiiList.size()==1){
                OrderInvoiceInfo oii = oiiList.get(0);
                //1
                oii.setId(oii.getId()+"1");
                //2
                oii.setOrderInfoId(oii.getOrderInfoId()+"1");
                //3
                oii.setOrderProcessInfoId(oii.getOrderProcessInfoId()+"1");
                //4
                oii.setFpqqlsh(oii.getFpqqlsh()+"1");
                //5
                oii.setKplsh(newkplsh);
                //6
                oii.setDdh(newDdh);
                List<OrderInvoiceInfo> newList = new ArrayList<>();
                newList.add(oii);
                int i1 = orderInvoiceInfoService.insertByList(newList);
                if(i1==1){
                    resultSb.add("orderInvoiceInfo插入成功，oiiId: "+oii.getId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultSb.add("orderInvoiceInfo插入失败");
        }

        /*
        OrderInvoiceInfo结束
        **/


        return getSussRtn(resultSb, "成功");
    }


    protected Map<String, Object> getFailRtn(String msg) {
        Map<String, Object> rtn = new HashMap<String, Object>();
        rtn.put("code", failCode);
        rtn.put("msg", msg);
        rtn.put("data", null);
        return rtn;
    }

    /**
     * 获取成功的返回内容
     *
     * @param data
     * @author chenrui
     * @return
     */
    protected Map<String, Object> getSussRtn(Object data, String msg) {
        Map<String, Object> rtn = new HashMap<String, Object>();
        rtn.put("code", sucCode);
        rtn.put("msg", msg);
        rtn.put("data", data);
        return rtn;
    }

}
