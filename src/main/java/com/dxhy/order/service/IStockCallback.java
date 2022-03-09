package com.dxhy.order.service;

/**
 * @ClassName IStockCallback
 * @Description TODO
 * @Author wangruwei
 * @Date 2022/2/25 16:12
 * @Version 1.0
 */
/**
 * 获取库存回调
 */
public interface IStockCallback {

    /**
     * 获取库存
     * @return
     */
    int getStock();
}