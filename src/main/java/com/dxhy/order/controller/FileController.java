package com.dxhy.order.controller;

import com.dxhy.order.model.Article;
import com.dxhy.order.model.GlobalCon;
import com.dxhy.order.model.WxLoginInfo;
import com.dxhy.order.model.WxUser;
import com.dxhy.order.service.ApiWankeService;
import com.dxhy.order.service.ApiWxUserService;
import com.dxhy.order.util.FileUtil;
import com.dxhy.order.util.HttpUtils;
import com.dxhy.order.util.JsonUtils;
import com.dxhy.order.util.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class FileController {

    @Resource
    HttpServletRequest request;

    @Value("${file.uploadPath}")
    private String uploadPath;

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

}
