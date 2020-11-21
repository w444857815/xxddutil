package com.dxhy.order.controller;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName BaseController
 * @Description TODO
 * @Author wangruwei
 * @Date 2020-11-16 14:37
 * @Version 1.0
 */
public class BaseController {

    private static String sucCode = "0000";

    private static String failCode = "9999";


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
