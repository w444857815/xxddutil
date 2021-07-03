package com.dxhy.order.model;

import lombok.Data;

/**
 * @ClassName WxLoginInfo
 * @Description TODO
 * @Author wangruwei
 * @Date 2021/6/19 14:25
 * @Version 1.0
 */
@Data
public class WxLoginInfo {
    //会话密钥
    private String session_key;
    //用户唯一标识
    private String openid;
//用户在开放平台的唯一标识符，若当前小程序已绑定到微信开放平台帐号下会返回，详见 UnionID 机制说明。
    private String unionid;
//    错误码
    private String errcode;
//    错误信息
    private String errmsg;

}
