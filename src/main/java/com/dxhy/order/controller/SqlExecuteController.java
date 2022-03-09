package com.dxhy.order.controller;

import com.dxhy.order.service.ApiOrderInfoService;
import com.dxhy.order.util.OfdConver;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.*;

/**
 * @ClassName SqlExecuteController
 * @Description TODO
 * @Author wangruwei
 * @Date 2020-11-16 14:32
 * @Version 1.0
 */
@Slf4j
@Controller
@RequestMapping("/sqlExe")
public class SqlExecuteController extends BaseController{

    @Autowired
    private ApiOrderInfoService orderinfoService;


    @GetMapping("/page")
    public ModelAndView page(ModelAndView model){
        model.setViewName("sql/enterPage");
        return model;
    }

    @RequestMapping(value="/exeSql")
    @ResponseBody
    public Map<String,Object> addYwlx(String sqlStr){
        List<String> resultSb = new LinkedList<>();
        Map<String, Object> map = new HashMap<String, Object>();

        //增
        sqlStr = sqlStr.trim();
        sqlStr = sqlStr.replaceAll(";", "");
        if(StringUtils.isEmpty(sqlStr)){
            //sql为空
            return getFailRtn("sql为空");
        }
        String checkSql = sqlStr.toLowerCase();
        //增加
        if(checkSql.startsWith("insert")){
            try {
                int insertNum = orderinfoService.executeInsertSql(sqlStr);
                Map<String, Object> insert = getSussRtn("insert", "插入成功" + insertNum + "条");
                insert.put("type", "insert");
                return insert;
            } catch (Exception e) {
                e.printStackTrace();
                return getFailRtn(e.getMessage());
            }

        }
        //删
        if(checkSql.startsWith("delete")){
            try {
                int delNum = orderinfoService.executeDeleteSql(sqlStr);
                Map<String, Object> del = getSussRtn("del", "删除成功" + delNum + "条");
                del.put("type", "delete");
                return del;
            } catch (Exception e) {
                e.printStackTrace();
                return getFailRtn(e.getMessage());
            }
        }
        //改
        if(checkSql.startsWith("update")){
            try {
                int updateNum = orderinfoService.executeUpdateSql(sqlStr);
                Map<String, Object> update = getSussRtn("update", "修改成功" + updateNum + "条");
                update.put("type", "update");
                return update;
            } catch (Exception e) {
                e.printStackTrace();
                return getFailRtn(e.getMessage());
            }
        }
        //查
        if(checkSql.startsWith("select")){
            //验证是*还是用字段的
            //如果是*
            //好像不需要判断，直接可以执行sql
            List<LinkedHashMap<String,Object>> list = null;
            try {
                list = orderinfoService.executeSelectSql(sqlStr);
            } catch (Exception e) {
                e.printStackTrace();
                return getFailRtn(e.getMessage());
            }

            if(list.size()>0){
                List<String> titleList = new LinkedList<>();
                LinkedHashMap<String,Object> title = list.get(0);
                Iterator it = title.entrySet().iterator();
                //列表头
                while (it.hasNext()) {
                    Map.Entry entity = (Map.Entry) it.next();
                    System.out.println(entity);
                    titleList.add(entity.getKey().toString());

                }

                //数据体
                Map<String,Object> sucMap = new HashMap<String,Object>();
                sucMap.put("title", titleList);
                sucMap.put("data", list);

                Map<String, Object> selMap = getSussRtn(sucMap, "查询成功");
                selMap.put("type", "select");
                return selMap;
            }else{
                return getFailRtn("没有查到数据");
            }

        }


        return map;
    }


    @RequestMapping(value="/ofd")
    @ResponseBody
    public Map<String,Object> ofd() throws IOException {
        OfdConver.toPng("/root/uploadFiles/hh.ofd", "/root/uploadFiles/", "aa");
        return null;
    }

}
