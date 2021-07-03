package com.dxhy.order.util;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 分页工具类
 *
 * @author ZSC-DXHY
 */
@Setter
@Getter
public class PageUtils implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 总记录数
     */
    private int totalCount;
    /**
     * 每页记录数
     */
    private int pageSize;
    /**
     * 总页数
     */
    private int totalPage;
    /**
     * 当前页数
     */
    private int currPage;
    /**
     * 列表数据
     */
    private List<?> list;

    /**
     * 分页
     *
     * @param list       列表数据
     * @param totalCount 总记录数
     * @param pageSize   每页记录数
     * @param currPage   当前页数
     */
    public PageUtils(List<?> list, int totalCount, int pageSize, int currPage) {
        this.list = list;
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.currPage = currPage;
        this.totalPage = (int) Math.ceil((double) totalCount / pageSize);
    }
    
    /**
     * 分页
     *
     * @param list       列表数据
     * @param totalCount 总记录数
     * @param pageSize   每页记录数
     * @param currPage   当前页数
     * @param split      是否手工分页
     */
    public PageUtils(List<?> list, int totalCount, int pageSize, int currPage, boolean split) {
        if (split) {
            List<?> newList;
            if (currPage * pageSize > totalCount) {
                newList = list.subList((currPage - 1) * pageSize, totalCount);
                
            } else {
                newList = list.subList((currPage - 1) * pageSize, (currPage - 1) * pageSize + pageSize);
                
            }
            this.list = newList;
        } else {
            this.list = list;
        }
        
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.currPage = currPage;
        this.totalPage = (int) Math.ceil((double) totalCount / pageSize);
    }
    
}