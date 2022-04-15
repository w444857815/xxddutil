package com.dxhy.order.controller;

import com.dxhy.order.model.EmailContent;
import com.dxhy.order.util.Base64Encoding;
import com.dxhy.order.util.DataHandleUtil;
import com.dxhy.order.util.JsonUtils;
import com.dxhy.order.util.SendMailUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class MailController {

    private String sucCode = "0000";

    private String failCode = "9999";

    @Value("${mail.senderName}")
    private String globalSenderName;

    /**
    * @Description 页面调发送邮件用此方法
    * @param subjects
    * @param contents
    * @param to
    * @param cc
    * @param senderName
    * @param showlogo
    * @Return java.util.Map<java.lang.String,java.lang.Object>
    * @Author wangruwei
    * @Date 2022/4/14 16:53
    **/
    @RequestMapping("/emailSendForm")
    @ResponseBody
    public Map<String,Object> emailSendForm(
            String subjects,
            String contents,
            String to,
            String cc,
            String senderName,
            List<String> fujianPath,
            boolean showlogo
    )  {
        EmailContent emailContent1 = new EmailContent();
        emailContent1.setSubjects(subjects);
        emailContent1.setContents(contents);
        if(StringUtils.isNotEmpty(to)){
            emailContent1.setTo(to.split(","));
        }
        if(StringUtils.isNotEmpty(cc)){
            emailContent1.setCC(cc.split(","));
        }

        if(StringUtils.isNotEmpty(senderName)){
            emailContent1.setSenderName(senderName);
        }else{
            emailContent1.setSenderName(globalSenderName);
        }
        emailContent1.setShowlogo(showlogo);

        //附件地址
        if(!CollectionUtils.isEmpty(fujianPath)){
            emailContent1.setFjFiles(fujianPath);
        }


        SendMailUtil sendMailUtil = new SendMailUtil();
        try {
            Map<String,String> map = sendMailUtil.sendMail(emailContent1);
            log.info("发送邮件over，结果:{}", JsonUtils.getInstance().toJsonString(map));
            return getReturn(map.get("code"),map.get("msg"));
        } catch (Exception e) {
            log.info("发送邮件异常:{}", e);
            return getReturn("9999","邮件发送异常");
        }
    }

    /**
     * @Description post直接请求发送  http://127.0.0.1:8080/order-api/emailSend
     * {
     *     "contents": "显示内容",
     *     "senderName": "显示",
     *     "subjects": "显示标题",
     *     "to": [
     *         "444857815@qq.com"
     *     ]
     * }
     * @Return com.dxhy.order.model.EmailSendResponse
     * @Author wangruwei
     * @Date 2022/3/14 15:50
     **/
    @RequestMapping("/emailSend")
    @ResponseBody
    public Map<String,Object> emailSend(@RequestBody String sendContent)  {
        EmailContent emailContent1 = JsonUtils.getInstance().parseObject(sendContent, EmailContent.class);
        emailContent1.setSenderName(globalSenderName);
        SendMailUtil sendMailUtil = new SendMailUtil();
        try {
            Map<String,String> map = sendMailUtil.sendMail(emailContent1);
            log.info("发送邮件over，结果:{}", JsonUtils.getInstance().toJsonString(map));
            return getReturn(map.get("code"),map.get("msg"));
        } catch (Exception e) {
            log.info("发送邮件异常:{}", e);
            return getReturn("9999","邮件发送异常");
        }
    }




    private Map<String,Object> getReturn(String code, String msg) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("code", code);
        params.put("msg", msg);
        return params;
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
