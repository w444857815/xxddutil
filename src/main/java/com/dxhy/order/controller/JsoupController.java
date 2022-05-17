package com.dxhy.order.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.dxhy.order.model.XsBook;
import com.dxhy.order.model.XsContent;
import com.dxhy.order.service.ApiWankeService;
import com.dxhy.order.service.RedisService;
import com.dxhy.order.service.XsBookService;
import com.dxhy.order.service.XsContentService;
import com.dxhy.order.thread.XsConGetInMysqlThread;
import com.dxhy.order.thread.XsConLastGetThread;
import com.dxhy.order.thread.XsListenBookOkThread;
import com.dxhy.order.util.EmailUtils;
import lombok.extern.slf4j.Slf4j;
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
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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


    @Value("${dzs.path}")
    private String dzsPath;

    //章节储存位置
    @Value("${dzs.conPath}")
    private String conPath;


    @Autowired
    private XsBookService xsBookService;

    @Autowired
    private XsContentService xsContentService;

    @Autowired
    private RedisService redisService;


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
            String basePath = "http://www.twxs8.com";

            String proxyUrl = "8.142.64.80:8090/wqx/";

            //替換代理ip
            //basePath = basePath.replace("www.twxs8.com", "8.142.64.80:8090/wqx/");
            webUrl = webUrl.replace("www.twxs8.com", "8.142.64.80:8090/wqx/");

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
            if(!file.exists()){
                file.createNewFile();
            }
            XsBook book = new XsBook();
            book.setBookUrl(webUrl);
            //如果库中有书，往后补，如果没有，创建。
            List<XsBook> bookList = xsBookService.selectByCondition(book);
            //入库 XsBook表
            String bookId = "";
            int lastOrder = -1;
            if(CollectionUtils.isEmpty(bookList)){
                log.info("没有书，直接创建，爬取，生成。创建章节目录");
                bookId = UUID.randomUUID().toString().replace("-", "");

                String conFolderPath = conPath+bookId;
                log.info("通过书创建的路径是:{}"+conFolderPath);
                File zjConFolder = new File(conFolderPath);
                if(!zjConFolder.exists()){
                    zjConFolder.mkdir();
                }

                book.setId(bookId);
                book.setBookName(fileName);

                book.setCreateTime(new Date());
                xsBookService.insertSelective(book);

                ExecutorService executorService = Executors.newFixedThreadPool(3);


                int countDownSize = 0;
                //获取要countdown的线程数
                for (int i = 0; i <childElements.get(0).children().size() ; i++) {
                    Element a = childElements.get(0).children().get(i).getAllElements().select("a").first();
                    String conUrl = "";
                    if(ObjectUtil.isNotNull(a)){
                        conUrl = childElements.get(0).children().get(i).getElementsByTag("a").first().attr("abs:href");
                    }
                    if(StringUtils.isNotEmpty(conUrl)){
                        countDownSize++;
                    }
                }

//                CountDownLatch downLatch = new CountDownLatch(childElements.get(0).children().size());
                CountDownLatch downLatch = new CountDownLatch(countDownSize);

                //设置一个监听线程，当所有爬取动作都跑完后，生成书，发邮件
                XsListenBookOkThread listenThread = new XsListenBookOkThread(bookId, emailAddress,downLatch,fileName,xsContentService,dzsPath,contentId,conPath,redisService);
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
                //downLatch.await();
            }else{
                log.info("此书已经有，把没有爬取到的继续爬取");
                bookId = bookList.get(0).getId();
                String conFolderPath = conPath+bookId;
                XsContent conParams = new XsContent();
                conParams.setBookId(bookId);
                //找到最后一个章节
                XsContent lastContent = xsContentService.selectLastZjByConditionNoCon(conParams);
                String lastUrl = lastContent.getZjUrl();
                lastOrder = lastContent.getZjOrder();
                //获取库中url集合
                //List<String> sqlurlList = xsContentList.parallelStream().map(XsContent::getZjUrl).distinct().collect(Collectors.toList());
                //爬取的章节列表
                List<String> jsoupUrlList = new LinkedList<>();
                Map<String,String> urlMap = new HashMap<String,String>();
                for (int i = 0; i <childElements.get(0).children().size() ; i++) {
                    String conUrl = "";
                    Element a = childElements.get(0).children().get(i).getAllElements().select("a").first();
                    if(ObjectUtil.isNotNull(a)){
                        conUrl = childElements.get(0).children().get(i).getElementsByTag("a").first().attr("abs:href");
                        if(!jsoupUrlList.contains(conUrl)){
                            conUrl = replaceTrueUrl(conUrl, proxyUrl);
                            jsoupUrlList.add(conUrl);
                            urlMap.put(conUrl, a.text());
                        }
                    }
                }
                log.info("倒叙爬取的章节，然后匹配库中最后一条");
                Collections.reverse(jsoupUrlList);
                List<String> newzjlist = new LinkedList<String>();
                for (int i = 0; i < jsoupUrlList.size(); i++) {
                    //如果匹配上了，跳出去
                    if(jsoupUrlList.get(i).equals(lastUrl)){
                        break;
                    }else{
                        //没有匹配上都是新章节
                        newzjlist.add(jsoupUrlList.get(i));
                    }
                }

                Collections.reverse(newzjlist);
                ExecutorService executorService = Executors.newFixedThreadPool(3);

                CountDownLatch downLatch = new CountDownLatch(newzjlist.size());
                for (int i = 0; i < newzjlist.size(); i++) {
                    log.info("入库操作");
                    XsContent con = new XsContent();
                    String xsConid = UUID.randomUUID().toString().replace("-", "");
                    con.setId(xsConid);
                    con.setBookId(bookId);
                    con.setBookName(fileName);
                    con.setZjOrder(lastOrder+i+1);
                    con.setZjTitle(urlMap.get(newzjlist.get(i)));
                    con.setZjUrl(newzjlist.get(i));
                    con.setIsSuc("0");
                    con.setCreateTime(new Date());
                    xsContentService.insertSelective(con);

                    log.info("线程爬取操作");
                    XsConGetInMysqlThread conCl = new XsConGetInMysqlThread(xsConid,newzjlist.get(i),contentId,i,xsContentService,downLatch,conFolderPath);
                    executorService.submit(conCl);
                }
                executorService.shutdown();

                downLatch.await();

                //新的章节爬取完，发邮件
                XsContent xscon = new XsContent();
                xscon.setBookId(bookId);
                xscon.setZjOrder(lastOrder);
                createFileAndSendMail(xscon,savePath,fileName,emailAddress);
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


    private void createFileAndSendMail(XsContent xscon, String savePath,  String fileName, String emailAddress) throws IOException {
        MailController mail = new MailController();
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

    public static void main(String[] args) throws Exception {



        String basePath = "http://www.twxs8.com";
        String webUrlaa = "http://www.twxs8.com/32_32205/";

        System.out.println(getHtml(webUrlaa));
        if(true){
            return ;
        }


        //替換代理ip
        basePath = basePath.replace("www.twxs8.com", "8.142.64.80:8090/wqx");
        webUrlaa = webUrlaa.replace("www.twxs8.com", "8.142.64.80:8090/wqx");
        System.out.println(basePath);
        System.out.println(webUrlaa);

        List<String> list = new LinkedList<String>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        String aaa = CollectionUtils.firstElement(Arrays.asList("c"));
        String hehe = CollectionUtils.lastElement(Arrays.asList("c"));
        Collections.reverse(list);
        System.out.println(list);
        System.out.println(aaa);


        //String webUrl = "http://www.baidu.com";
        //String webUrl = "http://www.twxs8.com/5_5518/";
        String webUrl = "http://www.twxs8.com/5_5518/";
        String linkUrl = "/5_5518/13174975.html";

        //getHtml(webUrl);

        //getdata(webUrl);
        File file = new File("E://aaa.txt");

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
