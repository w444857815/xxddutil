package com.dxhy.order.util;

/**
 * @ClassName OfdConver
 * @Description TODO
 * @Author wangruwei
 * @Date 2022/1/11 17:05
 * @Version 1.0
 */

import org.ofdrw.converter.GeneralConvertException;
import org.ofdrw.converter.ImageMaker;
import org.ofdrw.reader.OFDReader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.ofdrw.converter.ConvertHelper.ofd2pdf;

/**
 * @program: electroniclicense_xinxiang
 * @description: ofd转jpg和pdf
 * @author: Mr.wangruwei
 * @create: 2021-04-17 15:53
 **/

public class OfdConver {
    public static void main(String[] args) throws IOException {
        toPng("E:/xx.ofd", "E:/","bb");
    }


    /**
     * @Description: ofd转jpg
     * @Param: * @param null:
     * @return: * @return: null
     * @Author: Mr.wangruwei
     * @Date: 2021年04月17日 0017
     */
    public static void toPng(String filename, String dirPath, String jpgName) throws IOException {
        Files.createDirectories(Paths.get(dirPath));
        //filename文件路径到文件名
        Path src = Paths.get(filename);

        File file = new File("");

        ImageMaker imageMaker = new ImageMaker(new OFDReader(src), 15);
        imageMaker.config.setDrawBoundary(false);
        for (int i = 0; i < imageMaker.pageSize(); i++) {
            BufferedImage image = imageMaker.makePage(i);
            //文件要存的路径 第二个是文件名
            Path dist = Paths.get(dirPath, jpgName + ".jpg");
            ImageIO.write(image, "PNG", dist.toFile());
        }
    }

    /**
     * 转PDF
     *
     * @param input  OFD输入文件
     * @param output PDF输出文件路径
     * @throws IllegalArgumentException 参数错误
     * @throws GeneralConvertException  文档转换过程中异常
     */
    public static void toPdf(Path input, Path output) {
        ofd2pdf(input, output);
    }
}