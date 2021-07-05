package com.dxhy.order.controller;

import com.dxhy.order.model.*;
import com.dxhy.order.service.*;
import com.dxhy.order.util.HttpUtils;
import com.dxhy.order.util.JsonUtils;
import com.dxhy.order.util.PageUtils;
import com.dxhy.order.util.sql;
import io.github.yedaxia.apidocs.Docs;
import io.github.yedaxia.apidocs.DocsConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import sun.net.www.http.HttpClient;

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
@RequestMapping("/wanke")
public class WankeController {

    private static String sucCode = "0000";

    private static String failCode = "9999";

    @Autowired
    private ApiWankeService wankeService;

    @Autowired
    private ApiWxUserService wxUserService;


    @Value("${databaseName.c48}")
    private String c48DbName;

    @Value("${databaseName.bw}")
    private String bwDbName;

    @Value("${databaseName.a9}")
    private String a9DbName;


    @RequestMapping(value="/indexList")
    @ResponseBody
    public Map<String, Object> indexList(String seaStr, String pageSize,String currPage){
        PageUtils page = wankeService.selectArtilesList(seaStr,pageSize,currPage);
        return getSussRtn(page, "查询成功");
    }

    @RequestMapping(value="/gongGaoindexList")
    @ResponseBody
    public Map<String, Object> gongGaoList(){
        List<Article> list = wankeService.selectGgArtilesList("1");
        return getSussRtn(list, "查询成功");
    }


    @RequestMapping(value="/wodeWzList")
    @ResponseBody
    public Map<String, Object> wodeWzList(String openid){
        if(StringUtils.isEmpty(openid)){
            return getFailRtn("用户未登陆");
        }
        List<Article> list = wankeService.selectArtilesListByOpenid(openid);
        return getSussRtn(list, "查询成功");
    }

    //查看文章
    @RequestMapping(value="/wzDetail")
    @ResponseBody
    public Map<String, Object> wzDetail(Integer id){
        Article article = wankeService.selectArtileById(id);
        Article upNum = new Article();
        upNum.setId(article.getId());
        upNum.setVisitNum(article.getVisitNum()+1);
        int j = wankeService.updateByPrimaryKeySelective(upNum);
        List<WxUser> wxUsers = wxUserService.selectByopenId(article.getOpenId());
        if(wxUsers.size()>0){
            article.setNickName(wxUsers.get(0).getNickname());
        }
        return getSussRtn(article, "查询成功");
    }

    //发布文章
    @RequestMapping(value="/wzCreate")
    @ResponseBody
    public Map<String, Object> wzCreate(String title,String content,String openid){
        if(StringUtils.isEmpty(openid)){
            return getFailRtn("请登陆后再操作");
        }

        log.info("获取到的openid是：{}", openid);
        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setOpenId(openid);
        article.setCreateTime(new Date());
        article.setUpdateTime(new Date());
        int i = wankeService.insertSelective(article);
        if(i>0){
            return getSussRtn(article, "查询成功");
        }else{
            return getFailRtn("发布错误，请联系管理员");
        }

    }


    //微信登陆接口
    @RequestMapping(value="/wxLogin")
    @ResponseBody
    public Map<String, Object> wxLogin(@RequestBody Map<String,Object> param){
        String code = param.get("code").toString();
        System.out.println(code);


        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=wxcc1de6d285009bb3&secret=ab6ad8515bc8885c962cba44a13fcc1f&js_code="+code+"&grant_type=authorization_code";
        String result = HttpUtils.doGet(url, "");
        log.info("获取到的结果是：{}", result);
        WxLoginInfo wxLoginInfo = JsonUtils.getInstance().fromJson(result, WxLoginInfo.class);
        //登陆失败
        if(StringUtils.isNotEmpty(wxLoginInfo.getErrcode())&&!"0".equals(wxLoginInfo.getErrcode())){
            return getFailRtn("登陆失败。联系管理员");
        }

        String userInfoStr = param.get("userInfo").toString();
        log.info("实体对象:{}", JsonUtils.getInstance().toJsonString(userInfoStr));
        WxUser wxuInfo = JsonUtils.getInstance().parseObject(userInfoStr, WxUser.class);

        List<WxUser> list = wxUserService.selectByopenId(wxLoginInfo.getOpenid());

        if(list.size()==0){
            wxuInfo.setOpenid(wxLoginInfo.getOpenid());
            wxuInfo.setCreateTime(new Date());
            int insertNum = wxUserService.insertSelective(wxuInfo);
            if(insertNum>0){
                log.info("保存新用户成功:{}", JsonUtils.getInstance().toJsonString(wxuInfo));
            }
        }else{
            log.info("找到用户了，不做插入");
        }

        return getSussRtn(wxLoginInfo.getOpenid(), "登陆后用户的唯一id");
    }


    //获取全局配置
    @RequestMapping(value="/globalCon")
    @ResponseBody
    public Map<String, Object> globalCon(String id){
        GlobalCon globalCon = wankeService.selectGlobalCon(id);
        return getSussRtn(globalCon, "查询成功");

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
