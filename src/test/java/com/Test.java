package com;

import java.util.LinkedList;
import java.util.List;

/**
 * @ClassName Test
 * @Description TODO
 * @Author wangruwei
 * @Date 2021/1/19 11:11
 * @Version 1.0
 */
public class Test {
    public static void main(String[] args) {
        try {
            int a = 1/0;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("1");
        }
        System.out.println("2");


        /*String allStr = "";
        String[] split = allStr.split(",");
        List<String> list = new LinkedList<>();
        for (int i = 0; i < split.length; i++) {
            list.add(split[i]);
        }
        String gonggao = "";
        List<String> yunxulist = new LinkedList<>();
        for (int i = 0; i <list.size() ; i++) {
            if(!gonggao.contains(list.get(i))){
                yunxulist.add(list.get(i));
            }
        }
        System.out.println(yunxulist);*/
    }
}
