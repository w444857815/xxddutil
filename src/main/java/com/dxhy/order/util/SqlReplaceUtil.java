package com.dxhy.order.util;

import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.List;

public class SqlReplaceUtil {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
       /* String sql = "select oii.id, oii.ddh, opi.create_time, oii.fpzl_dm, opi.ghf_mc, opi.ghf_nsrsbh, oii.kphjje, oii.kpse, oii.kpzt, opi.sbyy, oii.push_sta\r\n" +
                "tus, opi.ywly, opi.ywsx, opi.ddly, oii.xhf_mc, oii.push_time, oii.push_result from order_invoice_info oii , order_process_info opi where oii.order_process_info_id=opi.id and opi.order_status = '0' and DATE_FORMAT(oii.create_time,'%Y-%m-%d') >=? and ?>=DATE_FORMAT(oii.create_time,'%Y-%m-%d') and oii.xhf_nsrsbh in ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? ) order by opi.create_time desc LIMIT ? ";

        String params = "2020-03-01(String), 2020-03-27(String), (String), (String), (String), 914403000775204185(String), 91360106091071401C(String), (String), \r\n" +
                "91350500749051256N(String), 91350583563399940T(String), 91350581054322435J(String), (String), 913101073014120416(String), 91411400317520725Q(String), 91330109MA27XXJU3A(String), (String), 91141000070499142N(String), 911310820922883032(String), 91120111MA0752436C(String), 91210311577217909K(String), 911302025869387753(String), (String), 91350200612048232N(String), 913502060728328505(String), 9135080206655775X1(String), 91350603070896658T(String), (String), (String), 91310000086179784P(String), (String), 10(Integer)";


        String replace = replace(sql, params);
        System.out.println(replace);*/
    	
    	String a = "1\r\n" + 
    			"\r\n" + 
    			"2\r\n" + 
    			"3";
    	checkListFromStr(a);
    	
    }

    public static String replace(String sql, String params) {
        sql = sql.replaceAll("[\\t\\n\\r]", "");
        String[] split = params.split(",");
        for (int i = 0; i < split.length; i++) {
            String item = split[i];
            item = item.replaceAll("[\\t\\n\\r]", "");
            if (item.contains("(String)")) {
                item = "'" + item.trim().replace("(String)", "") + "'";
            } else if (item.contains("(Timestamp)")) {
                item = "'" + item.trim().replace("(Timestamp)", "") + "'";
            } else if (item.contains("(Integer)")) {
                item = item.trim().replace("(Integer)", "");
            }

            sql = sql.replaceFirst("\\?", item);
        }


        return sql;
    }

    /**
     * 替换换行字符串为sql的in的格式
     *
     * @return TODO
     * SqlReplaceUtil.java
     * author wangruwei
     * 2019年11月19日
     */
    public static String replaceStr(String a) {

        System.out.println(a.indexOf("\n"));
        if (a.indexOf("\r\n,") > -1) {
            a = a.replaceAll("\r\n,", ",");
        }
        if (a.indexOf("\r\n") > -1) {
            a = a.replaceAll("\r\n", ",");
        }
        if (a.indexOf("\n,") > -1) {
            a = a.replaceAll("\n,", ",");
        }
        if (a.indexOf(",\n") > -1) {
            a = a.replaceAll(",\n", ",");
        }
        if (a.indexOf("\n") > -1) {
            a = a.replaceAll("\n", ",");
        }

        a = a.replace(",", "','");

        System.out.println(a);
        return "'" + a + "'";
    }
    
    /**
     * 把折行的字符串放到list里
     * @return
     * TODO
     * SqlReplaceUtil.java
     * author wangruwei
     * 2020年7月9日
     */
    public static List<String> checkListFromStr(String a){
    	System.out.println(a.indexOf("\n"));
        if (a.indexOf("\r\n,") > -1) {
            a = a.replaceAll("\r\n,", ",");
        }
        if (a.indexOf("\r\n") > -1) {
            a = a.replaceAll("\r\n", ",");
        }
        if (a.indexOf("\n,") > -1) {
            a = a.replaceAll("\n,", ",");
        }
        if (a.indexOf(",\n") > -1) {
            a = a.replaceAll(",\n", ",");
        }
        if (a.indexOf("\n") > -1) {
            a = a.replaceAll("\n", ",");
        }
        String[] split = a.split(",");
        List<String> list = new LinkedList<String>();
        for (int i = 0; i < split.length; i++) {
			if(StringUtils.isNotEmpty(split[i])){
				list.add(split[i]);
			}
		}
        System.out.println(a);
        return list;
    }

}