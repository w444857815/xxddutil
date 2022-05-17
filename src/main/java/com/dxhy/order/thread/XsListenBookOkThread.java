package com.dxhy.order.thread;

import com.dxhy.order.controller.MailController;
import com.dxhy.order.model.XsContent;
import com.dxhy.order.service.RedisService;
import com.dxhy.order.service.XsContentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @ClassName 监听，当所有线程都跑完后，生成书，并发送。
 * @Description TODO
 * @Author wangruwei
 * @Date 2022/3/30 16:49
 * @Version 1.0
 */
@Slf4j
public class XsListenBookOkThread implements Runnable {

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

    private RedisService redisService;



    public XsListenBookOkThread(String bookId,
                                String emailAddress,
                                CountDownLatch downLatch,
                                String fileName,
                                XsContentService xsContentService,
                                String dzsPath,
                                String contentId,
                                String conFolderPath,
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
        this.redisService = redisService;
    }

    @Override
    public void run() {
        MailController mail = new MailController();
        try {
            downLatch.await();

            log.info("监听完成，所有线程都已跑完");

            String savePath = dzsPath+fileName+".txt";
            XsContent xscon = new XsContent();
            xscon.setBookId(bookId);

            //判断是否全部成功，
            // ①是的话，直接生成文件，发邮件。
            // ②有未成功的，直接起另外线程再跑一遍，无论成功与否都发送。
            xscon.setIsSuc("0");
            int failSize = xsContentService.selectCountByCondition(xscon);
            if(failSize==0){
                log.info("全部获取成功,生成并发邮件");
                //查获取成功的。

                createFileAndSendMail(xscon,savePath,mail);

            }else{
                //有获取失败的，新建一个线程再去爬取，只跑一次。成功与否都发邮件，不写线程了，直接循环。
                log.info("获取失败的再跑一次。也仅一次");
                xscon.setIsSuc("0");
                List<XsContent> failList = xsContentService.selectByCondition(xscon);
                CountDownLatch lastdownLatch = new CountDownLatch(failList.size());
                for (int i = 0; i < failList.size(); i++) {
                    //新开多线程去爬取失败的。
                    XsConLastGetThread last = new XsConLastGetThread(failList.get(i).getId(), failList.get(i).getZjUrl(), contentId, failList.get(i).getZjOrder(), xsContentService,lastdownLatch,conFolderPath);
                    Thread lastThread = new Thread(last);
                    lastThread.start();
                }
                lastdownLatch.await();
                log.info("获取失败的跑完，开始生成，并发送");
                createFileAndSendMail(xscon,savePath,mail);
            }



        } catch (InterruptedException e) {
            e.printStackTrace();
            log.info("监听出错");
            mail.emailSendForm(fileName+" 下载失败", "下载失败，如有需要联系444857815@qq.com", emailAddress, "", "海洋小助手",null , false);
        } catch (IOException e) {
            e.printStackTrace();
            log.info("生成书出错");
            mail.emailSendForm(fileName+" 生成书出错", "下载失败，如有需要联系444857815@qq.com", emailAddress, "", "海洋小助手",null , false);
        }

        redisService.del(bookId);
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


}

