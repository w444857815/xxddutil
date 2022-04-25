package com.dxhy.order.controller;

import com.dxhy.order.model.TjSongmovie;
import com.dxhy.order.service.TjSongMovieService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * TjSongmovie
 * TjSongmovie
 * <p>Title:TjSongmovieController </p>
 * <p>Description:TjSongmovie </p>
 * <p>Company: </p> 
 * @author wangruwei
 * @date 2022-04-24 10:09:33
 */
@Controller
@RequestMapping(value = "/tjsong")
public class TjSongmovieController extends BaseController {
	
	@Autowired
	private TjSongMovieService tjSongMovieService;
	
	/**
	 * 跳转到列表页
	* @Title: index 
	* @return    设定文件 
	* @return modelAndView   返回类型 
	* @throws 
	* @author wangruwei
	* @date 2022-04-24 10:09:33
	 */
	@RequestMapping(value="/index")
	public ModelAndView index(){
		ModelAndView modelAndView = new ModelAndView("ligerui/myWork/tjsong/tjSong");
		return modelAndView;
	}


	@RequestMapping(value = "checkAdd")
	@ResponseBody
	public Map<String, Object> checkAdd(String songName,String jianjie) {
		if(StringUtils.isEmpty(songName)){
			return getFailRtn("名字必须输入");
		}
		TjSongmovie song = new TjSongmovie();
		song.setSongName(songName.trim());
		List<TjSongmovie> tjSongmovies = tjSongMovieService.selectByCondition(song);
		if(tjSongmovies.size()>0){
			return getFailRtn("已经存在此歌或电影");
		}
		song.setJianjie(jianjie);
		song.setCreateTime(new Date());
		int i = tjSongMovieService.insertSelective(song);
		if(i>0){
			return getSussRtn("记录成功", "记录成功");
		}else{
			return getFailRtn("入库失败");
		}


	}
}

