package com.dxhy.order.util;

import java.io.File;
import java.io.FileOutputStream;

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
}
