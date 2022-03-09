package com.dxhy.order.controller;

import com.dxhy.order.model.*;
import com.dxhy.order.service.*;
import com.dxhy.order.util.JsonUtils;
import com.dxhy.order.util.sql;
import io.github.yedaxia.apidocs.Docs;
import io.github.yedaxia.apidocs.DocsConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
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
@RequestMapping("/ligerui")
public class UrlController {

    @Resource
    private RedisService redisService;

    @Resource
    private RedisZsetService redisZsetService;

    /**
    * @Description redis实现秒杀，减库存防超卖方法
    * @Author wangruwei
    * @Date 2022/3/1 17:32
    **/
    @GetMapping("/redis/miaosha")
    public ModelAndView miaosha(ModelAndView model){
        String kucun = redisService.get("kucun");
        log.info("库存数量剩余:{}",kucun);
        model.setViewName("ligerui/myWork/redis/miaosha");
        model.addObject("kucun", kucun);
        return model;
    }


    /**
     * @Description redis Zset排名
     * @Author wangruwei
     * @Date 2022/3/1 17:32
     **/
    @GetMapping("/redis/zsetOrder")
    public ModelAndView zsetOrder(ModelAndView model){
        Set<ZSetOperations.TypedTuple<String>> paiming = redisZsetService.reverseRangeWithScores("paiming", 0, -1);
        log.info("排名:{}", JsonUtils.getInstance().toJsonString(paiming));
        model.setViewName("ligerui/myWork/redis/zsetOrder");
        model.addObject("paiming", paiming);
        return model;
    }

    /**
     * @Description redis Zset 精确排名，看要以精确到 秒，百毫秒，十毫秒，个位毫秒
     * @Author wangruwei
     * @Date 2022/3/1 17:32
     **/
    @GetMapping("/redis/zsetOrderJq")
    public ModelAndView zsetOrderJq(ModelAndView model){
        Set<ZSetOperations.TypedTuple<String>> paimingJq_ms = redisZsetService.reverseRangeWithScores("paimingJq_ms", 0, -1);
        Set<ZSetOperations.TypedTuple<String>> paimingJq_ten_ms = redisZsetService.reverseRangeWithScores("paimingJq_ten_ms", 0, -1);
        Set<ZSetOperations.TypedTuple<String>> paimingJq_hundreds_ms = redisZsetService.reverseRangeWithScores("paimingJq_hundreds_ms", 0, -1);
        Set<ZSetOperations.TypedTuple<String>> paimingJq_seconds = redisZsetService.reverseRangeWithScores("paimingJq_seconds", 0, -1);
        model.setViewName("ligerui/myWork/redis/zsetOrderJq");

        model.addObject("paimingJq_ms", paimingJq_ms);
        model.addObject("paimingJq_ten_ms", paimingJq_ten_ms);
        model.addObject("paimingJq_hundreds_ms", paimingJq_hundreds_ms);
        model.addObject("paimingJq_seconds", paimingJq_seconds);
        return model;
    }


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

    @Value("${databaseName.c48}")
    private String c48DbName;

    @Value("${databaseName.bw}")
    private String bwDbName;

    @Value("${databaseName.a9}")
    private String a9DbName;

    @GetMapping("/hello")
    @ResponseBody
    public Map<String,Object> hello(String code){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("he", "一");
        map.put("ha", "二");
        map.put("hei", "三");
        map.put("hehe", "四");
        map.put("aaa", "44444");
        map.put("bbb", "55");
        System.out.println(code);
        return map;
    }

    @GetMapping("/yunxing")
    @ResponseBody
    public void yunxing(String code){
        new sql().setVisible(true);
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


    @GetMapping("/kkinfoPage")
    public ModelAndView kkinfoPage(ModelAndView model){
        model.setViewName("kkinfoPage");
        return model;
    }

    public static void main(String[] args) {
        DocsConfig config = new DocsConfig();
        config.setProjectPath("E:\\workspace\\xxddutil"); // 项目根目录
        config.setProjectName("xxddutil"); // 项目名称
        config.setApiVersion("V1.0");       // 声明该API的版本
        config.setDocsPath("E:\\"); // 生成API 文档所在目录
        config.setAutoGenerate(Boolean.TRUE);  // 配置自动生成
        Docs.buildHtmlDocs(config); // 执行生成文档
    }

    /**
    * @Description 获取跨库信息
    * @Return org.springframework.web.servlet.ModelAndView
    * @Author wangruwei
    * @Date 2021/2/5 11:17
    **/
    @PostMapping("/getKkinfo")
    @ResponseBody
    public Map<String, Object> getKkinfo(String ddh, String fpqqlsh){
        Map<String,Object> csmap = new HashMap<String,Object>();
        csmap.put("ddh", ddh.trim());
        csmap.put("fpqqlsh", fpqqlsh.trim());
        if(StringUtils.isEmpty(ddh)&&StringUtils.isEmpty(fpqqlsh)){
            return getFailRtn("ddh或fpqqlsh至少输入一个");
        }
        Map<String,Object> resultMap = new HashMap<String,Object>();
        List<LinkedHashMap> opiList = orderProcessService.selectOrderProcessByFpqqlshDdhNsrsbh(null, ddh, fpqqlsh);
        //返回的code  1 ，opi查到多条或0条数据，让确认
        if(opiList.size()==0){
            return getSussRtn(null, "没找到opi的数据","99");
        }
        if(opiList.size()>1){
            resultMap.put("opiData", getTitleDataList(opiList.get(0),opiList));
            return getSussRtn(resultMap, "opi的数据","1");
        }
        List<LinkedHashMap> oiiList =  orderInvoiceInfoService.selectOrderProcessByFpqqlshDdh(ddh, fpqqlsh);
        //返回的code  2 ，oii查到多条或0条数据，让确认
        if(oiiList.size()==0){
            return getSussRtn(null, "没找到oii的数据","99");
        }
        if(oiiList.size()>1){
            resultMap.put("oiiData", getTitleDataList(oiiList.get(0),oiiList));
            return getSussRtn(resultMap, "oii的数据","2");
        }


        String kplsh = oiiList.get(0).get("kplsh").toString();

        System.out.println("和hfhkasfh");

        TaxEquipmentInfo tSksb = orderProcessService.selectTaxByNsrsbh(opiList.get(0).get("xhf_nsrsbh").toString());


        /**
         * @Description 查税控库里的数据
         * @param ddh
         * @param fpqqlsh
         **/
        //C48
        List<LinkedHashMap> loglist = new ArrayList<>();
        List<LinkedHashMap> xxlist =  new ArrayList<>();
        if("001".equals(tSksb.getSksbCode())){
            loglist = orderProcessService.selectC48FpkjLogList(c48DbName,kplsh);
            xxlist = orderProcessService.selectC48FpkjXxList(c48DbName,kplsh);

//            resultMap.put("kjlogList", loglist);
//            resultMap.put("kjList", xxlist);
            resultMap.put("invoiceType", "C48");
        }else if("004".equals(tSksb.getSksbCode())){
            //百旺
            loglist = orderProcessService.selectbwFpkjLogList(bwDbName,kplsh);
            xxlist = orderProcessService.selectbwFpkjXxList(bwDbName,kplsh);

//            resultMap.put("kjlogList", loglist);
//            resultMap.put("kjList", xxlist);
            resultMap.put("invoiceType", "百旺盘阵");
        }
        //A9
        else if("002".equals(tSksb.getSksbCode())){
            loglist = orderProcessService.selectA9FpkjLogList(a9DbName,kplsh);
            xxlist = orderProcessService.selectA9FpkjXxList(a9DbName,kplsh);

//            resultMap.put("kjlogList", loglist);
//            resultMap.put("kjList", xxlist);
            resultMap.put("invoiceType", "A9");
        }




        resultMap.put("opi", getTitleDataList(opiList.get(0)));
        LinkedHashMap oii = oiiList.get(0);
        oii.remove("ewm");
        resultMap.put("oii", getTitleDataList(oii));

        resultMap.put("kjlog", getTitleDataList(loglist.get(0)));
        LinkedHashMap kjxx = xxlist.get(0);
        kjxx.remove("ewm");
        kjxx.remove("szqm");
        resultMap.put("kjxx", getTitleDataList(kjxx));


        return getSussRtn(resultMap, "获取成功");
    }



    private Map<String,Object> getTitleDataList(LinkedHashMap linkedHashMap) {
        Map<String,Object> resultMap = new HashMap<String,Object>();
        List<String> titleList = new LinkedList<>();
        List<String> dataList = new LinkedList<>();

        LinkedHashMap<String,Object> title = linkedHashMap;
        Iterator it = title.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entity = (Map.Entry) it.next();
            System.out.println(entity);
            titleList.add(entity.getKey().toString());
            dataList.add(linkedHashMap.get(entity.getKey().toString())+"");
        }
        resultMap.put("title", titleList);
        resultMap.put("data", dataList);
        return resultMap;
    }


    private Map<String,Object> getTitleDataList(LinkedHashMap linkedHashMap, List<LinkedHashMap> opiList) {
        Map<String,Object> resultMap = new HashMap<String,Object>();
        List<String> titleList = new LinkedList<>();


        LinkedHashMap<String,Object> title = linkedHashMap;
        Iterator it = title.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entity = (Map.Entry) it.next();
            System.out.println(entity);
            titleList.add(entity.getKey().toString());
//            dataList.add(linkedHashMap.get(entity.getKey().toString())+"");
        }
        List<List<String>> allDataList = new LinkedList<>();
        for (int j = 0; j < opiList.size() ; j++) {
            List<String> dataList = new LinkedList<>();
            for (int i = 0; i < titleList.size(); i++) {
                dataList.add(opiList.get(j).get(titleList.get(i).toString())+"");
            }
            allDataList.add(dataList);
        }
        resultMap.put("title", titleList);
        resultMap.put("data", allDataList);
        return resultMap;
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

    /**
     * 获取成功的返回内容
     *
     * @param data
     * @author chenrui
     * @return
     */
    protected Map<String, Object> getSussRtn(Object data, String msg,String code) {
        Map<String, Object> rtn = new HashMap<String, Object>();
        rtn.put("code", code);
        rtn.put("msg", msg);
        rtn.put("data", data);
        return rtn;
    }

}
