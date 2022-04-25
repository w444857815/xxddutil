package com.dxhy.order.service;


import com.dxhy.order.model.TjSongmovie;

import java.util.List;

/**
 * @author ：杨士勇
 * @ClassName ：ApiOrderInfoService
 * @Description ：订单查询，插入，更新，删除操作
 * @date ：2018年7月21日 下午5:37:00
 */
public interface TjSongMovieService {

    int insertSelective(TjSongmovie song);

    List<TjSongmovie> selectByCondition(TjSongmovie song);
}
