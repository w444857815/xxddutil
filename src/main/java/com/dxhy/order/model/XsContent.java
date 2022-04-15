package com.dxhy.order.model;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class XsContent implements Serializable {
	
	private static final long serialVersionUID = 5454155825314635342L;
	
	

	//columns START
    /**
     * id  
     */ 	
	private String id;
    /**
     * 书id  
     */ 	
	private String bookId;
    /**
     * 书名  
     */ 	
	private String bookName;
    /**
     * 章节排序  
     */ 	
	private Integer zjOrder;
    /**
     * 章节名称  
     */ 	
	private String zjTitle;
    /**
     * 获取此页的地址  
     */ 	
	private String zjUrl;
    /**
     * 内容  
     */ 	
	private String content;
    /**
     * 是否成功(1成功，0失败)  
     */ 	
	private String isSuc;
    /**
     * 创建时间  
     */ 	
	private Date createTime;
	//columns END


}

