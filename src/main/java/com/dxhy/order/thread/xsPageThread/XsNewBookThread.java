package com.dxhy.order.thread.xsPageThread;

import cn.hutool.core.util.ObjectUtil;
import com.dxhy.order.controller.MailController;
import com.dxhy.order.model.XsContent;
import com.dxhy.order.service.RedisService;
import com.dxhy.order.service.XsContentService;
import com.dxhy.order.thread.XsConGetInMysqlThread;
import com.dxhy.order.thread.XsConLastGetThread;
import com.dxhy.order.thread.XsListenBookOkThread;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName 监听，当所有线程都跑完后，生成书，并发送。
 * @Description TODO
 * @Author wangruwei
 * @Date 2022/3/30 16:49
 * @Version 1.0
 */
@Slf4j
public class XsNewBookThread implements Runnable {

    //书的id
    private String bookId;

    //书名
    private String fileName;

    //收件人邮箱
    private String emailAddress;

    private CountDownLatch downLatch;

    private XsContentService xsContentService;

    //文件储存路径
    private String dzsPath;

    private String contentId;

    private String conFolderPath;

    private Elements childElements;

    private String proxyUrl;

    private RedisService redisService;



    public XsNewBookThread(String bookId,
                           String emailAddress,
                           CountDownLatch downLatch,
                           String fileName,
                           XsContentService xsContentService,
                           String dzsPath,
                           String contentId,
                           String conFolderPath,
                           Elements childElements,
                           String proxyUrl,
                           RedisService redisService

    ) {
        this.bookId = bookId;
        this.emailAddress = emailAddress;
        this.downLatch = downLatch;
        this.fileName = fileName;
        this.xsContentService = xsContentService;
        this.dzsPath = dzsPath;
        this.contentId = contentId;
        this.conFolderPath = conFolderPath;
        this.childElements = childElements;
        this.proxyUrl = proxyUrl;
        this.redisService = redisService;
    }

    @SneakyThrows
    @Override
    public void run() {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        //设置一个监听线程，当所有爬取动作都跑完后，生成书，发邮件
        XsListenBookOkThread listenThread = new XsListenBookOkThread(bookId, emailAddress,downLatch,fileName,xsContentService,dzsPath,contentId,conFolderPath,redisService);
        Thread runThread = new Thread(listenThread);
        runThread.start();

        //章节列表
        for (int i = 0; i <childElements.get(0).children().size() ; i++) {
            //标题
            String title = childElements.get(0).children().get(i).getAllElements().select("a").text();
            Element a = childElements.get(0).children().get(i).getAllElements().select("a").first();
            String conUrl = "";
            if(ObjectUtil.isNotNull(a)){
                conUrl = childElements.get(0).children().get(i).getElementsByTag("a").first().attr("abs:href");
            }
            log.info(title+"*"+conUrl);
            if(StringUtils.isNotEmpty(conUrl)){
                conUrl = replaceTrueUrl(conUrl,proxyUrl);
                log.info("入库操作");
                XsContent con = new XsContent();
                String xsConid = UUID.randomUUID().toString().replace("-", "");
                con.setId(xsConid);
                con.setBookId(bookId);
                con.setBookName(fileName);
                con.setZjOrder(i);
                con.setZjTitle(title);
                con.setZjUrl(conUrl);
                con.setIsSuc("0");
                con.setCreateTime(new Date());
                xsContentService.insertSelective(con);

                log.info("线程爬取操作");
                XsConGetInMysqlThread conCl = new XsConGetInMysqlThread(xsConid,conUrl,contentId,i,xsContentService,downLatch,conFolderPath);
                executorService.submit(conCl);
                long sleep = new Random().nextInt(10)*1000;
                Thread.sleep(sleep);
                //Thread conThread = new Thread(conCl);
                //conThread.start();
            }
        }
        executorService.shutdown();
    }

    private void createFileAndSendMail(XsContent xscon, String savePath, MailController mail) throws IOException {
        xscon.setIsSuc("1");
        List<XsContent> conList = xsContentService.selectByCondition(xscon);
        for (int i = 0; i < conList.size(); i++) {
            String fileContent = "";
            try {
                fileContent = FileUtils.readFileToString(new File(conList.get(i).getContent()), "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileUtils.writeStringToFile(new File(savePath), " "+conList.get(i).getZjTitle()+"\n\t" + "    "+fileContent+"\n\t","UTF-8",true);
        }

        //生成书以后，记录下现在最大的order是多少。下次查询就从这个书往后查,线程外面的for里的order需要设置下

        List<String> xiaoshuoFile = new LinkedList<String>();
        xiaoshuoFile.add(savePath);

        mail.emailSendForm(fileName+" 下载成功", "附件中可下载，欢迎后续使用，如有需要联系444857815@qq.com", emailAddress, "", "海洋小助手",xiaoshuoFile , false);
    }

    public static String replaceTrueUrl(String getUrl,String proxyUrl){
        String basePath = "www.twxs8.com";
        return getUrl.replace(basePath, proxyUrl);
    }

}

