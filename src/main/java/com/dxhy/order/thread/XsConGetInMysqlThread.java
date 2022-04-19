package com.dxhy.order.thread;

import com.dxhy.order.model.XsContent;
import com.dxhy.order.service.XsContentService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import static com.sun.webkit.network.URLs.newURL;

/**
 * @ClassName 详情页获取，并入库
 * @Description TODO
 * @Author wangruwei
 * @Date 2022/3/30 16:49
 * @Version 1.0
 */
@Slf4j
public class XsConGetInMysqlThread implements Runnable {

    //文章表的id
    private String xsConId;

    //orderNum.只是日志打一下
    private int zjOrder;

    //详情页的url
    private String url;

    //详情页面html里的内容所在id
    private String contentId;

    private XsContentService xsContentService;

    private CountDownLatch downLatch;




    public XsConGetInMysqlThread(String xsConId,
                                 String url,
                                 String contentId,
                                 int zjOrder,
                                 XsContentService xsContentService,
                                 CountDownLatch downLatch

    ) {
        this.xsConId = xsConId;
        this.url = url;
        this.contentId = contentId;
        this.zjOrder = zjOrder;
        this.xsContentService = xsContentService;
        this.downLatch = downLatch;
    }

    @Override
    public void run() {
        try {
            long i = new Random().nextInt(10)*1000;
            Thread.sleep(i);

            //获取文章页的全部数据
            String conHtmlStr = getHtml(url);
            Document contentDoc = Jsoup.parse(conHtmlStr);
            //详情的内容
            String content = contentDoc.getElementById(contentId).text();

            XsContent con = new XsContent();
            con.setId(xsConId);
            con.setContent(content);
            con.setIsSuc("1");
            xsContentService.updateByPrimaryKeySelective(con);
            
            log.info("文章第{}条获取成功",zjOrder);
        } catch (IOException e) {
            e.printStackTrace();
            XsContent con = new XsContent();
            con.setId(xsConId);
            con.setContent("获取错误");
            xsContentService.updateByPrimaryKeySelective(con);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        downLatch.countDown();

    }


    private static String getHtml(String url) throws IOException {

        StringBuffer buffer = new StringBuffer();
        URL urlObj= null;
        URLConnection uc= null;
        BufferedReader br=null;
        urlObj = newURL(url);//打开网络连接
        uc =urlObj.openConnection();//建立文件输入流
        uc.setConnectTimeout(10000);
        uc.setReadTimeout(10000);

        InputStreamReader isr = new InputStreamReader(uc.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(isr);
        String str =null;
        while((str=bufferedReader.readLine())!=null){
            buffer.append(str);
        }
        bufferedReader.close();
        isr.close();
        //a为最终的数据，需要什么类型可以转为什么类型
        return buffer.toString();
        /*String a= buffer.toString();
        System.out.println(a);*/
    }

}

