package com.dxhy.order.controller;

import com.dxhy.order.util.SqlReplaceUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

/**
 * YfpdUserpay
 * YfpdUserpay
 * <p>Title:YfpdUserpayController </p>
 * <p>Description:YfpdUserpay </p>
 * <p>Company: </p>
 *
 * @author wangruwei
 * @date 2017-11-13 17:10:20
 */
@Controller
public class YfpdIndexController extends BaseController {




    @RequestMapping(value = "/getbdPage")
    public ModelAndView toyfpdShow() throws Exception {
        ModelAndView mav = new ModelAndView();
        String page = "getbdPage";
        mav.setViewName(page);

        return mav;
    }
    @RequestMapping(value = "/sqlreplace")
    public ModelAndView sqlreplace() throws Exception {
        ModelAndView mav = new ModelAndView();
        String page = "sqlformate";
        mav.setViewName(page);

        return mav;
    }

    @RequestMapping(value = "/getbendi")
    @ResponseBody
    public Map<String,Object> getbendi(String sessionid,String iplist) throws Exception {
//        String result = "http://10.1.28.48:9000/?dxhy_sso_sessionid="+sessionid;
        String[] split = iplist.split(",");
        List<String> list = new LinkedList<String>();
        for (int i = 0; i < split.length; i++) {
            String url = "http://"+split[i]+":9000/?dxhy_sso_sessionid="+sessionid;
            list.add(url);
        }
//        String result = "http://127.0.0.1:9000/?dxhy_sso_sessionid="+sessionid;
        return getSuccessRtn(list);
    }

    @RequestMapping(value = "/sqlformate")
    @ResponseBody
    public Map<String, Object> sqlFormate(String sql, String params,String isFh,String oneStep) throws Exception {
        if (StringUtils.isBlank(sql)) {
            return getFailRtn("sql必须传");
        }
        String useSql = "";
        if(StringUtils.isNotEmpty(oneStep)){
            useSql = sql;
            useSql = useSql.replaceAll("(\\d{4})-(\\d{1,2})-(\\d{1,2}) (\\d{2}):(\\d{2}):(\\d{2}).(\\d{3})", "换行开始$1年$2月$3日 $4是$5分$6秒$7结束");
            useSql = useSql.substring(0, useSql.indexOf("换行开始"));
            params = sql.substring(sql.indexOf("Parameters:")+11, sql.length());
        }else{
            useSql = sql;
        }
        if (StringUtils.isBlank(params)) {
            return getFailRtn("参数必须传");
        }
        String replace = SqlReplaceUtil.replace(useSql, params);
        if("1".equals(isFh)){
        	replace = replace + ";";
        }
        return getSuccessRtn(replace);
    }

    public static void main(String[] args) {
        String sql = " select SUM(CAST(hjbhsje AS DECIMAL(18,2))) hjje, SUM(CAST(hjse AS DECIMAL(18,2))) hjse from special_invoice_reversal WHERE data_status\n" +
                " = '0' and ( xhf_nsrsbh =? or ghf_nsrsbh = ? ) and create_time >= ? and ? >= create_time 2021-12-16 14:31:01.949 [DubboServerHandler-10.15.0.34:12348-thread-193] DEBUG c.d.o.d.S.selectSpecialInvoiceReversalsCount [143]- ==> Parameters: 913302056982010651(String), 913302056982010651(String), 2021-12-01(String), 2021-12-16 23:59:59(String)";
        sql = sql.substring(0,sql.indexOf("["));
        sql = sql.substring(0, sql.length()-24);
        System.out.println(sql);


    }

    @RequestMapping(value = "/replaceStrformate")
    @ResponseBody
    public Map<String, Object> replaceStrformate(String replaceStr,String iskh) throws Exception {
        if (StringUtils.isBlank(replaceStr)) {
            return getFailRtn("要替换的内容必须传");
        }
        String replace = SqlReplaceUtil.replaceStr(replaceStr);
        if("1".equals(iskh)){
        	replace = "("+replace + ")";
        }
        return getSuccessRtn(replace);
    }
    
    @RequestMapping(value = "/cccsss")
    @ResponseBody
    public Map<String, Object> cccsss(String replaceStr) throws Exception {
    	
    	ScriptEngineManager maneger = new ScriptEngineManager();
        ScriptEngine engine = maneger.getEngineByName("JavaScript");
 
        Reader scriptReader = new InputStreamReader(
                YfpdIndexController.class.getResourceAsStream("all.js"));
        if (engine != null) {
            try {
                // JS引擎解析文件
                engine.eval(scriptReader);
                if (engine instanceof Invocable) {
                    Invocable invocable = (Invocable) engine;
                    // JS引擎调用方法
                    Object result = invocable.invokeFunction("hah");
                    System.out.println("The result is: " + result);
                }
            } catch (ScriptException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } finally {
                scriptReader.close();
            }
        } else {
            System.out.println("ScriptEngine create error!");
        }
    	
//    	ScriptUtil.eval("print('Script test!');");
//    	ScriptUtil.eval("hah()");
    	return null;
    }


    @RequestMapping(value = "/jmeterPage")
    public ModelAndView jmeterPage() throws Exception {
        ModelAndView mav = new ModelAndView();
        String page = "/admin/jmeterPage";
        mav.setViewName(page);

        return mav;
    }


    @RequestMapping(value = "/getJsonPathResult")
    @ResponseBody
    public Map<String, Object> getJsonPathResult(String jsonStr,String pathStr,String szxj) throws Exception {
        if (StringUtils.isBlank(jsonStr)) {
            return getFailRtn("json数据必须传");
        }
        if (StringUtils.isBlank(pathStr)) {
            return getFailRtn("路径必须传");
        }

        String replace = SqlReplaceUtil.getJsonValueByPath(jsonStr,pathStr,szxj);
        return getSuccessRtn(replace);
    }


    /**
     * 获取成功返回
     *
     * @param data
     * @return
     * @author tianyunyun
     * @date 2016年11月4日下午12:15:48
     */
    protected Map<String, Object> getSuccessRtn(Object data) {
        Map<String, Object> rtn = new HashMap<String, Object>();
        rtn.put("code", "0");
        rtn.put("msg", "");
        rtn.put("data", data);
        return rtn;
    }


}

