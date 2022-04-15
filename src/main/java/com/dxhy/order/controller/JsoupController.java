package com.dxhy.order.controller;

import cn.hutool.core.util.ObjectUtil;
import com.dxhy.order.model.OrderInfo;
import com.dxhy.order.model.XsBook;
import com.dxhy.order.model.XsContent;
import com.dxhy.order.service.ApiWankeService;
import com.dxhy.order.service.XsBookService;
import com.dxhy.order.service.XsContentService;
import com.dxhy.order.thread.*;
import com.dxhy.order.util.ConnectAdslNet;
import com.dxhy.order.util.EmailUtils;
import com.dxhy.order.util.JsonUtils;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

/**
 * @ClassName RedisKucunController
 * @Description TODO
 * @Author wangruwei
 * @Date 2022/2/25 16:05
 * @Version 1.0
 */
@RestController
@RequestMapping("/jsoup")
@Slf4j
public class JsoupController extends BaseController{

    @Autowired
    private ApiWankeService wankeService;

    private static int waitTime = 5000;

    private static String basePath = "http://www.twxs8.com";

    @Value("${dzs.path}")
    private String dzsPath;

    @Autowired
    private XsBookService xsBookService;

    @Autowired
    private XsContentService xsContentService;



    /**
    * @Description 获取此本书的所有章节和内容生成txt
    * @Return java.util.Map<java.lang.String,java.lang.Object>
    * @Author wangruwei
    * @Date 2022/4/8 14:05
    **/
    @RequestMapping(value = "getThisBook" )
    public Map<String,Object> getThisBook(String webUrl,String listXpath,String bookNameId,String contentId,String emailAddress) {
        if(StringUtils.isEmpty(webUrl)){
            return getFailRtn("地址不能为空");
        }
        if(StringUtils.isEmpty(listXpath)){
            return getFailRtn("xpath不能为空");
        }
        if(StringUtils.isEmpty(bookNameId)){
            return getFailRtn("bookNameId不能为空");
        }
        if(StringUtils.isEmpty(contentId)){
            return getFailRtn("contentId不能为空");
        }
        if(StringUtils.isEmpty(emailAddress)){
            return getFailRtn("邮箱地址不能为空");
        }
        if(!EmailUtils.validateEmail(emailAddress)){
            return getFailRtn("邮箱格式不正确");
        }

        MailController mail = new MailController();
        String fileName = UUID.randomUUID().toString().replaceAll("-", "");
        try {
            String htmlStr = getHtml(webUrl);
            //String htmlStr = FileUtils.readFileToString(new File("E://xx.txt"), "UTF-8");
            log.info("获取html成功，开始读取章节");
            //log.info("此网站内容:{}",htmlStr);

            Document doc = Jsoup.parse(htmlStr);

            doc.setBaseUri(basePath);

            Elements childElements = doc.selectXpath(listXpath);

            Element bookNameEle = doc.getElementById(bookNameId);

            if(ObjectUtil.isNotNull(bookNameEle)){
                fileName = doc.getElementById(bookNameId).getElementsByTag("h1").text();
            }

            String savePath = dzsPath+fileName+".txt";
            File file = new File(savePath);
            //优化，判断是否需要删
            if(file.exists()){
                file.delete();
            }
            //入库 XsBook表
            XsBook book = new XsBook();
            String bookId = UUID.randomUUID().toString().replace("-", "");
            book.setId(bookId);
            book.setBookName(fileName);
            book.setBookUrl(webUrl);
            book.setCreateTime(new Date());
            xsBookService.insertSelective(book);

            ExecutorService executorService = Executors.newFixedThreadPool(5);
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
                    XsConGetInMysqlThread conCl = new XsConGetInMysqlThread(xsConid,conUrl,contentId,i,xsContentService);
                    executorService.submit(conCl);
                    long shijian = new Random().nextInt(5)*1000;
                    Thread.sleep(shijian);
                    //Thread conThread = new Thread(conCl);
                    //conThread.start();
                }



                /*FileUtils.writeStringToFile(new File(savePath), " "+title+"\n\t","UTF-8",true);
                //根据文章详情conUrl获取详情
                if(StringUtils.isNotEmpty(conUrl)){

                    //获取文章页的全部数据
                    String conHtmlStr = getHtml(conUrl);
                    Document contentDoc = Jsoup.parse(conHtmlStr);
                    //详情的内容
                    String content = contentDoc.getElementById(contentId).text();
                    log.info(content);
                    FileUtils.writeStringToFile(new File(savePath), "    "+content+"\n\t","UTF-8",true);
                }*/
                //if(i==3){
                //    break;
                //}

            }
            executorService.shutdown();
            log.info("走完");
            if(true){
                return getSussRtn("", "");
            }

            List<String> xiaoshuoFile = new LinkedList<String>();
            xiaoshuoFile.add(savePath);
            mail.emailSendForm(fileName+" 下载成功", "附件中可下载，欢迎后续使用，如有需要联系444857815@qq.com", emailAddress, "", "海洋小助手",xiaoshuoFile , false);
        } catch (IOException e) {
            e.printStackTrace();
            log.info("获取页面出错，请检查url地址");
            //mail.emailSendForm(fileName+" 下载失败", "下载失败，如有需要联系444857815@qq.com", emailAddress, "", "海洋小助手",null , false);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return getSussRtn("", "获取失败");
    }

    public static void main(String[] args) throws IOException {
        //String webUrl = "http://www.baidu.com";
        //String webUrl = "http://www.twxs8.com/5_5518/";
        String webUrl = "http://www.twxs8.com/5_5518/10243531.html";
        String linkUrl = "/5_5518/13174975.html";

        //设置http代理ip地址和端口
        InetSocketAddress add = new InetSocketAddress("101.200.90.74",8080);
        Proxy proxy = new Proxy(Proxy.Type.HTTP,add);

//访问api路径
        URL url = new URL(webUrl);
//建立连接
        /*HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection(proxy);
//post请求可以设请求参数类型(httpURLConnecction.setRequestProperty("key","value"))，get忽略
//设置请求方式
        httpURLConnection.setRequestMethod("GET");
//获取接口返回的数据
        */
        StringBuffer buffer = new StringBuffer();
        //InputStreamReader isr = new InputStreamReader(httpURLConnection.getInputStream());
        InputStreamReader isr = new InputStreamReader(url.openConnection().getInputStream());
        BufferedReader bufferedReader = new BufferedReader(isr);
        String str =null;
        while((str=bufferedReader.readLine())!=null){
            buffer.append(str);
        }
        bufferedReader.close();
        isr.close();
//a为最终的数据，需要什么类型可以转为什么类型
        String a= buffer.toString();
        log.info(a);


    }

    private String getHtml(String webUrl) throws IOException {
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
