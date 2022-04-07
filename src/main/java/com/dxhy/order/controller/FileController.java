package com.dxhy.order.controller;

import com.dxhy.order.constant.DownloadExcelEnum;
import com.dxhy.order.exception.OrderReceiveException;
import com.dxhy.order.model.*;
import com.dxhy.order.model.excel.NewOrderExcel;
import com.dxhy.order.service.ExcelReadService;
import com.dxhy.order.util.FileUtil;
import com.dxhy.order.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @ClassName OrderController
 * @Description TODO
 * @Author wangruwei
 * @Date 2020-10-13 11:36
 * @Version 1.0
 */
@Slf4j
@Controller
@RequestMapping("/file")
public class FileController extends BaseController{

    @Resource
    HttpServletRequest request;

    @Value("${file.uploadPath}")
    private String uploadPath;

    private static final String LOGGER_MSG = "(文件处理)";

    @Resource
    private ExcelReadService excelReadService;

    //处理文件上传
    @RequestMapping(value="/upload", method = RequestMethod.POST)
    public @ResponseBody String uploadImg(@RequestParam("file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        //设置文件上传路径
//        String filePath = request.getSession().getServletContext().getRealPath(uploadPath);
        String filePath = uploadPath;
        try {
            FileUtil.uploadFile(file.getBytes(), filePath, fileName);
            return "上传成功";
        } catch (Exception e) {
            return "上传失败";
        }
    }



    /**
    * @Description 页面处理上传
    * @Return
    * @Author wangruwei
    * @Date 2022/3/15 9:53
    **/
    @RequestMapping(value="/uploadFile", method = RequestMethod.POST)
    public @ResponseBody String uploadFile(@RequestParam(value="file",required= false) MultipartFile[] files,HttpServletRequest request) throws IOException {

        long  startTime=System.currentTimeMillis();
        String fileSavePath = FileUtil.getResourcePath()+"upload/";
        File pathFile = new File(fileSavePath);

        if(!pathFile.exists()&&!pathFile.isDirectory()){
            pathFile.mkdirs();
        }

        if(files!=null&&files.length>0){
            //循环获取file数组中得文件
            for(int i = 0;i<files.length;i++){
                MultipartFile file = files[i];
                //这个方法最慢
                /*FileUtils.writeByteArrayToFile(new File("E:\\"+file.getOriginalFilename()), file.getBytes());*/
                log.info("文件类型:{},文件名称:{},文件大小:{}",file.getContentType(),file.getName(),file.getSize());


                //这个方法最快
                file.transferTo(new File(fileSavePath+file.getOriginalFilename()));

                //这个方法其次
                /*OutputStream os=new FileOutputStream("E:/"+file.getOriginalFilename());
                 //获取输入流 CommonsMultipartFile 中可以直接得到文件的流
                 InputStream is=file.getInputStream();
                 byte[] bts = new byte[2048];
                 //一个一个字节的读取并写入
                 while(is.read(bts)!=-1)
                 {
                     os.write(bts);
                 }
                os.flush();
                os.close();
                is.close();*/
            }
        }
        long  endTime=System.currentTimeMillis();
        System.out.println("方法四的运行时间："+String.valueOf(endTime-startTime)+"ms");
        return "success";
    }

    /**
     * 文件下载
     *
     */
    @RequestMapping("/downLoad")
    @ResponseBody
    public Map<String,Object> downLoadFtpFileMould(String fileName,String excelType, HttpServletResponse response) {
        try {

            //获取要下载的模板名称
            DownloadExcelEnum downloadExcelEnum = null;
            if (StringUtils.isNotBlank(excelType)) {
                switch (excelType) {
                    case "0":
                        downloadExcelEnum = DownloadExcelEnum.LOGO;
                        break;
                    case "1":
                        downloadExcelEnum = DownloadExcelEnum.EXCEL_ORDER_IMPORT;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + excelType);
                }
            }

            //----------------------下面无需关心


            File downLoadFile = new File(FileUtil.getResourcePath() + downloadExcelEnum.getKey());
            // 获取文件的长度
            long fileLength = downLoadFile.length();
            BufferedInputStream bis;
            BufferedOutputStream bos;

            // 设置文件输出类型
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition",
                    "attachment; filename=" + new String(downloadExcelEnum.getValue().getBytes(StandardCharsets.UTF_8), "ISO8859-1"));
            // 设置输出长度
            response.setHeader("Content-Length", String.valueOf(fileLength));
            // 获取输入流
            bis = new BufferedInputStream(new FileInputStream(downLoadFile));
            // 输出流
            bos = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
            bos.flush();
            // 关闭流
            bis.close();
            bos.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return getFailRtn("下载错误");
        }
    }



    /**
    * @Description 导入excel获取数据
    * @param file
    * @Return java.util.Map<java.lang.String,java.lang.Object>
    * @Author wangruwei
    * @Date 2022/3/16 11:42
    **/
    @PostMapping("/excel")
    public Map<String, Object> acceptByExcel(
            @RequestParam(value = "file") MultipartFile file
    ) {
        Map<String, List<NewOrderExcel>> readOrderInfoFromExcelxls;
        try {

            long time3 = System.currentTimeMillis();

            //从excel中读取数据
            long t1 = System.currentTimeMillis();
            log.debug("excel读取开始,当前时间{}",t1);
            List<NewOrderExcel> newOrderExcels = excelReadService.readOrderInfoFromExcelxls(file);
            long t2 = System.currentTimeMillis();
            log.debug("excel读取开始,当前时间{},耗时:{}",t2,t2-t1);
            log.info("获取到的实体数据:{}",JsonUtils.getInstance().toJsonString(newOrderExcels));

            //先读取数据，然后进行数据非空校验


            return getSussRtn(newOrderExcels.size(), "读取完成,条数:"+newOrderExcels.size());

        } catch (OrderReceiveException e) {
            log.error("{}excel导入异常:{}", LOGGER_MSG, e);
            return getFailRtn(e.getMessage());
        }  catch (Exception e) {
            log.error("{}excel导入处理异常:{}", LOGGER_MSG, e);
            return getFailRtn(e.getMessage());
        }

    }

}
