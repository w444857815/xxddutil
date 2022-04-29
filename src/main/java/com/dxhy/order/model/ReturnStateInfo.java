package com.dxhy.order.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 
 * @ClassName ：ReturnStateInfo
 * @Description ：
 * @author ：王汝伟
 * @date ：2020年4月16日 上午11:00:18
 * 
 * 
 */

@Setter
@Getter
public class ReturnStateInfo  implements Serializable {
	
	private String returnCode;
	
	private String returnMessage;

}
