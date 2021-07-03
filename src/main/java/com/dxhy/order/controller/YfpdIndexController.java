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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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




    @RequestMapping(value = "/sqlreplace")
    public ModelAndView toyfpdShow() throws Exception {
        ModelAndView mav = new ModelAndView();
        String page = "sqlformate";
        mav.setViewName(page);

        return mav;
    }

    @RequestMapping(value = "/sqlformate")
    @ResponseBody
    public Map<String, Object> sqlFormate(String sql, String params,String isFh) throws Exception {
        if (StringUtils.isBlank(sql)) {
            return getFailRtn("sql必须传");
        }
        if (StringUtils.isBlank(params)) {
            return getFailRtn("参数必须传");
        }
        String replace = SqlReplaceUtil.replace(sql, params);
        if("1".equals(isFh)){
        	replace = replace + ";";
        }
        return getSuccessRtn(replace);
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

