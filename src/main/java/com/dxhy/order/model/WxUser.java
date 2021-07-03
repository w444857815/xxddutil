package com.dxhy.order.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * wx_user
 * @author 
 */
@Data
public class WxUser implements Serializable {
    private Integer id;

    private String openid;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatarurl;

    /**
     * 城市
     */
    private String city;

    /**
     * 省
     */
    private String province;

    /**
     * 国家
     */
    private String country;

    /**
     * 性别
     */
    private String gender;

    /**
     * 语言
     */
    private String language;

    private Date createTime;

    private static final long serialVersionUID = 1L;
}