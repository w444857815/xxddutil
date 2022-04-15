package com.dxhy.order.model;

import lombok.Data;

import java.util.List;

/**
 * @ClassName HtmlTree
 * @Description TODO
 * @Author wangruwei
 * @Date 2022/4/13 17:45
 * @Version 1.0
 */
@Data
public class HtmlTree {
    //html界面的标签
    private String tag;

    //真实要获取数据的位置
    private String trueTag;

    //当前标签
    private String nowHtml;

    //当前标签下面的子标签
    private List<HtmlTree> tagChild;
}
