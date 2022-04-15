package com.dxhy.order.model;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class XsBook implements Serializable {
	
	private static final long serialVersionUID = 5454155825314635342L;
	
	

	//columns START
    /**
     * id  
     */ 	
	private String id;
    /**
     * 书名  
     */ 	
	private String bookName;

	/**
	 * 书地址
	 */
	private String bookUrl;
    /**
     * 创建时间  
     */ 	
	private Date createTime;
	//columns END


}

