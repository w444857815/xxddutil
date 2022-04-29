package com.dxhy.order.model;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class XsUser implements Serializable {
	
	private static final long serialVersionUID = 5454155825314635342L;
	
	

	//columns START
    /**
     * id  
     */ 	
	private String id;
    /**
     * username  
     */ 	
	private String username;
    /**
     * password  
     */ 	
	private String password;
    /**
     * emailAddress  
     */ 	
	private String emailAddress;
    /**
     * createTime  
     */ 	
	private Date createTime;
	//columns END


}

