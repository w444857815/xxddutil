package com.dxhy.order.thread;

import com.dxhy.order.model.XsContent;
import com.dxhy.order.service.ApiWankeService;
import com.dxhy.order.service.XsContentService;
import com.dxhy.order.util.JsonUtils;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;

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




    public XsConGetInMysqlThread(String xsConId,
                                 String url,
                                 String contentId,
                                 int zjOrder,
                                 XsContentService xsContentService

    ) {
        this.xsConId = xsConId;
        this.url = url;
        this.contentId = contentId;
        this.zjOrder = zjOrder;
        this.xsContentService = xsContentService;
    }

    @Override
    public void run() {
        try {
            long i = new Random().nextInt(5)*1000;
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

    }


    private String getHtml(String webUrl) throws IOException {
        int waitTime = 15000;
        //构造一个webClient 模拟Chrome 浏览器
        WebClient webClient = new WebClient(BrowserVersion.FIREFOX_3);
        //屏蔽日志信息
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
        //支持JavaScript
        webClient.setJavaScriptEnabled(true);
        webClient.setCssEnabled(false);
        webClient.setActiveXNative(false);
        webClient.setCssEnabled(false);
        webClient.setThrowExceptionOnScriptError(false);
        webClient.setThrowExceptionOnFailingStatusCode(false);
        webClient.setTimeout(waitTime);
        HtmlPage rootPage = webClient.getPage(webUrl);
        //设置一个运行JavaScript的时间
        webClient.waitForBackgroundJavaScript(waitTime);
        webClient.closeAllWindows();
        return rootPage.asXml();
    }

}

