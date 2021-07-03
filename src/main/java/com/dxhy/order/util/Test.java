package com.dxhy.order.util;

import java.util.LinkedList;
import java.util.List;

/**
 * @ClassName Test
 * @Description TODO
 * @Author wangruwei
 * @Date 2021/6/7 16:56
 * @Version 1.0
 */
public class Test {
    public static void main(String[] args) {
//        test1();
        test2();
    }

    private static void test2() {

        List<Integer> yuan = new LinkedList<>();
        yuan.add(1);
        yuan.add(5);
        yuan.add(4);
        yuan.add(2);
        yuan.add(6);
        yuan.add(9);
        System.out.println(yuan);

        int yuanSize = yuan.size();
        for (int i = 0; i < yuanSize; i++) {

            int qian = yuan.get(i);
            int hou = yuan.get(i+1);
            if(qian>hou){
                yuan.set(i, hou);
                yuan.set(i+1, qian);
            }
            System.out.println(yuan);

//            for (int j = i+1; j < yuanSize-1; j++) {
//                if(yuan.get(i)>yuan.get(j)){
//
//                }
//            }


        }

    }

    private static void test1() {
        for (int i = 1; i < 10; i++) {
            for (int j = 1; j < i+1; j++) {
                System.out.print(j+" X "+i+" = "+i*j+"   ");
            }
            System.out.println();
        }
    }
}
