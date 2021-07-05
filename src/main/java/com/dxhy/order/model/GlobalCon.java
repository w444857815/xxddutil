package com.dxhy.order.model;

import java.io.Serializable;
import lombok.Data;

/**
 * global_con
 * @author 
 */
@Data
public class GlobalCon implements Serializable {
    private Integer id;

    private String status;

    private String des;

    private static final long serialVersionUID = 1L;
}