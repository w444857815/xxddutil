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
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static com.sun.webkit.network.URLs.newURL;

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
            XsBook book = new XsBook();
            book.setBookUrl(webUrl);
            //如果库中有书，往后补，如果没有，创建。
            List<XsBook> bookList = xsBookService.selectByCondition(book);
            if(CollectionUtils.isEmpty(bookList)){
                log.info("没有书，直接创建，爬取，生成");
                //入库 XsBook表
                String bookId = UUID.randomUUID().toString().replace("-", "");
                book.setId(bookId);
                book.setBookName(fileName);

                book.setCreateTime(new Date());
                xsBookService.insertSelective(book);

                ExecutorService executorService = Executors.newFixedThreadPool(3);
                CountDownLatch downLatch = new CountDownLatch(childElements.get(0).children().size());

                //设置一个监听线程，当所有爬取动作都跑完后，生成书，发邮件
                XsListenBookOkThread listenThread = new XsListenBookOkThread(bookId, emailAddress,downLatch,fileName,xsContentService,dzsPath,contentId);
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
                        XsConGetInMysqlThread conCl = new XsConGetInMysqlThread(xsConid,conUrl,contentId,i,xsContentService,downLatch);
                        executorService.submit(conCl);
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
            }else{
                log.info("此书已经有，把没有爬取到的继续爬取");
                String bookId = bookList.get(0).getId();
                XsContent conParams = new XsContent();
                conParams.setBookId(bookId);
                List<XsContent> xsContentList = xsContentService.selectByConditionNoCon(conParams);
                //获取库中url集合
                List<String> sqlurlList = xsContentList.parallelStream().map(XsContent::getZjUrl).distinct().collect(Collectors.toList());
                //爬取的章节列表
                List<String> jsoupUrlList = new LinkedList<>();
                for (int i = 0; i <childElements.get(0).children().size() ; i++) {
                    String conUrl = "";
                    Element a = childElements.get(0).children().get(i).getAllElements().select("a").first();
                    if(ObjectUtil.isNotNull(a)){
                        conUrl = childElements.get(0).children().get(i).getElementsByTag("a").first().attr("abs:href");
                        jsoupUrlList.add(conUrl);
                    }
                }
                //获取库中没有的数据
                jsoupUrlList.removeAll(sqlurlList);
                //新更新的章节
                List<XsContent> newConList = xsContentService.selectByUrls(jsoupUrlList);
                CountDownLatch lastdownLatch = new CountDownLatch(newConList.size());
                for (int i = 0; i < newConList.size(); i++) {
                    //新开多线程去爬取失败的。
                    XsConLastGetThread last = new XsConLastGetThread(newConList.get(i).getId(), newConList.get(i).getZjUrl(), contentId, xsContentList.size()+newConList.get(i).getZjOrder(), xsContentService,lastdownLatch);
                    Thread lastThread = new Thread(last);
                    lastThread.start();
                }
                lastdownLatch.await();
                //新的爬取完，发邮件
                XsContent xscon = new XsContent();
                xscon.setBookId(bookId);
                createFileAndSendMail(xscon,savePath,new MailController(),fileName,emailAddress);
            }


            log.info("走完");

            return getSussRtn("爬取成功，耐心等待", "爬取成功，耐心等待");

        } catch (IOException e) {
            e.printStackTrace();
            log.info("获取页面出错，请检查url地址");
            return getFailRtn("获取页面出错，请检查url地址");
        } catch (Exception e) {
            e.printStackTrace();
            return getFailRtn("获取失败，联系管理员");
        }

    }


    private void createFileAndSendMail(XsContent xscon, String savePath, MailController mail, String fileName, String emailAddress) throws IOException {
        xscon.setIsSuc("1");
        List<XsContent> conList = xsContentService.selectByCondition(xscon);
        for (int i = 0; i < conList.size(); i++) {
            FileUtils.writeStringToFile(new File(savePath), " "+conList.get(i).getZjTitle()+"\n\t" + "    "+conList.get(i).getContent()+"\n\t","UTF-8",true);
        }

        //生成书以后，记录下现在最大的order是多少。下次查询就从这个书往后查,线程外面的for里的order需要设置下

        List<String> xiaoshuoFile = new LinkedList<String>();
        xiaoshuoFile.add(savePath);

        mail.emailSendForm(fileName+" 下载成功", "附件中可下载，欢迎后续使用，如有需要联系444857815@qq.com", emailAddress, "", "海洋小助手",xiaoshuoFile , false);
    }

    public static void main(String[] args) throws Exception {
        //String webUrl = "http://www.baidu.com";
        //String webUrl = "http://www.twxs8.com/5_5518/";
        String webUrl = "http://www.twxs8.com/5_5518/";
        String linkUrl = "/5_5518/13174975.html";

        //getHtml(webUrl);

        //getdata(webUrl);

        if(true){
            return;
        }

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

    /**
    * @Description 最早的方法，这个会打很多无用日志而且去不掉
    * @Return java.lang.String
    * @Author wangruwei
    * @Date 2022/4/18 9:37
    **/
    /*private static String getHtml(String webUrl) throws IOException {
        //构造一个webClient 模拟Chrome 浏览器
        WebClient webClient = new WebClient(BrowserVersion.FIREFOX_3);
        //屏蔽日志信息
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);

        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.http.client").setLevel(Level.OFF);

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
        System.out.println(rootPage.asXml());
        return rootPage.asXml();
    }*/




}
