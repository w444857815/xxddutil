package com.dxhy.order.util;

import java.io.*;
import java.util.Base64;

/**
 * @ClassName FileUtil
 * @Description TODO
 * @Author wangruwei
 * @Date 2021/11/22 14:13
 * @Version 1.0
 */
public class FileUtil {
    public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath+fileName);
        out.write(file);
        out.flush();
        out.close();
    }

    public static void main(String[] args) {
        //System.out.println(fileToBase64("D:\\a.ftl"));


        base64ToFile( fileToBase64("D:\\a.ftl"), "ceshide.ftl");
    }

    /**
     * 文件转base64字符
     * @param path
     * @return
     */

    public static  String fileToBase64(String path) {
        String base64 = null;
        InputStream in = null;
        try {
            File file = new File(path);
            in = new FileInputStream(file);
            byte[] bytes=new byte[(int)file.length()];
            in.read(bytes);
            base64 = Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return base64;
    }

    /**
    * @Description 获取resource文件夹地址
    * @param
    * @Return java.lang.String
    * @Author wangruwei
    * @Date 2022/3/14 10:49
    **/
    public static String getResourcePath(){
        return Thread.currentThread().getContextClassLoader().getResource("").getPath();
    }

    //BASE64转File文件
    public static void base64ToFile(String base64, String fileName) {

        File file = null;
        //�����ļ�Ŀ¼
        String filePath= getResourcePath()+"temp";
        File  dir=new File(filePath);
        if (!dir.exists() && !dir.isDirectory()) {
            dir.mkdirs();
        }
        BufferedOutputStream bos = null;
        java.io.FileOutputStream fos = null;
        try {
            //org.apache.commons.codec.binary.Base64

            //byte[] bytes = Base64.getDecoder().decode(base64);
            byte[] bytes = org.apache.tomcat.util.codec.binary.Base64.decodeBase64(base64);
            file=new File(filePath+"/"+fileName);
            fos = new java.io.FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    //BASE64转File文件
    public static void base64ToFile(String filePath,String base64, String fileName) {

        File file = null;
        //文件目录
        File  dir=new File(filePath);
        if (!dir.exists() && !dir.isDirectory()) {
            dir.mkdirs();
        }
        BufferedOutputStream bos = null;
        java.io.FileOutputStream fos = null;
        try {
            byte[] bytes = Base64.getDecoder().decode(base64);
            file=new File(filePath+"/"+fileName);
            fos = new java.io.FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
