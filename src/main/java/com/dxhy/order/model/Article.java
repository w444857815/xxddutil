package com.dxhy.order.model;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * article
 * @author 
 */
@Data
public class Article implements Serializable {
    private Integer id;

    private String title;

    private String content;

    /**
     * 是否带有图片。1有，0没有
     */
    private String isPic;

    private String openId;

    private String ggStatus;

    private String nickName;

    private Date createTime;

    private Integer visitNum;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}