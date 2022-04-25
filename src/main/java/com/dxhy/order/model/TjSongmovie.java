package com.dxhy.order.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TjSongmovie  implements Serializable {
	
	private static final long serialVersionUID = 5454155825314635342L;
	
	

	//columns START
    /**
     * id  
     */ 	
	private Integer id;
    /**
     * 歌，电影名字啥的  
     */ 	
	private String songName;
    /**
     * 描述  
     */ 	
	private String jianjie;
    /**
     * 1已下载，0未下载  
     */ 	
	private String isDownload;
    /**
     * createTime  
     */ 	
	private Date createTime;

	//columns END


}

