package com.dxhy.order.service.impl;


import com.dxhy.order.dao.TjSongmovieMapper;
import com.dxhy.order.dao.XsBookMapper;
import com.dxhy.order.model.TjSongmovie;
import com.dxhy.order.model.XsBook;
import com.dxhy.order.service.TjSongMovieService;
import com.dxhy.order.service.XsBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TjSongMovieServiceImpl implements TjSongMovieService {

    @Autowired
    private TjSongmovieMapper tjSongmovieMapper;


    @Override
    public int insertSelective(TjSongmovie tjSongmovie) {
        return tjSongmovieMapper.insertSelective(tjSongmovie);
    }

    @Override
    public List<TjSongmovie> selectByCondition(TjSongmovie tjSongmovie) {
        return tjSongmovieMapper.selectByCondition(tjSongmovie);
    }
}
