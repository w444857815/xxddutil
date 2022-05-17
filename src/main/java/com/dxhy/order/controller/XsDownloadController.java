package com.dxhy.order.controller;

import cn.hutool.core.util.ObjectUtil;
import com.dxhy.order.model.TjSongmovie;
import com.dxhy.order.model.XsBook;
import com.dxhy.order.model.XsContent;
import com.dxhy.order.model.XsUser;
import com.dxhy.order.service.*;
import com.dxhy.order.thread.XsConGetInMysqlThread;
import com.dxhy.order.thread.XsListenBookOkThread;
import com.dxhy.order.thread.xsPageThread.XsNewBookThread;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
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

import static com.sun.webkit.network.URLs.newURL;

/**
 * @ClassName 下载小说
 * @Description TODO
 * @Author wangruwei
 * @Date 2022/2/25 16:05
 * @Version 1.0
 */
@RestController
@RequestMapping("/xsdown")
@Slf4j
public class XsDownloadController extends BaseController{

    @Autowired
    private ApiWankeService wankeService;

    private static int waitTime = 5000;


    @Value("${dzs.path}")
    private String dzsPath;

    //章节储存位置
    @Value("${dzs.conPath}")
    private String conPath;

    @Value("${serviceProject.ipadd}")
    private String ipadd;

    @Value("${project.name}")
    private String projectName;



    @Autowired
    private XsBookService xsBookService;

    @Autowired
    private XsContentService xsContentService;

    @Autowired
    private XsUserService xsUserService;

    @Autowired
    private RedisService redisService;

    /**
    * @Description 下载页面
    * @param
    * @Return org.springframework.web.servlet.ModelAndView
    * @Author wangruwei
    * @Date 2022/4/29 10:08
    **/
    @GetMapping("/xsdownPage")
    public ModelAndView newThread(){
        ModelAndView model = new ModelAndView();
        model.setViewName("ligerui/myWork/jsoup/xsdownPage");

        return model;
    }


    /**
    * @Description 当前进度查看
    * @Return java.util.Map<java.lang.String,java.lang.Object>
    * @Author wangruwei
    * @Date 2022/4/29 14:43
    **/
    @RequestMapping(value = "xsDownProgress")
    @ResponseBody
    public Map<String, Object> xsDownProgress(String bookId) {
        if(StringUtils.isEmpty(bookId)){
            return getFailRtn("id不能为空");
        }

        XsBook book = new XsBook();
        book.setId(bookId);
        List<XsBook> bookList = xsBookService.selectByCondition(book);
        //一共多少条
        int allSize = bookList.get(0).getAllSize();

        XsContent xsContent = new XsContent();
        xsContent.setBookId(bookId);
        xsContent.setIsSuc("1");
        //现在成功的数是:
        int num = xsContentService.selectCountByCondition(xsContent);

        return getSussRtn("", "一共"+allSize+"章，已获取"+num+"章");
    }


    /**
    * @Description 获取此本书的所有章节和内容生成txt
    * @Return java.util.Map<java.lang.String,java.lang.Object>
    * @Author wangruwei
    * @Date 2022/4/8 14:05
    **/
    @RequestMapping(value = "getThisBook" )
    public Map<String,Object> getThisBook(String webUrl,String listXpath,String bookNameId,String contentId,String emailAddress,HttpSession session) {
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

        Object xsUserloginName = session.getAttribute("xsUserloginName");
        if(ObjectUtil.isEmpty(xsUserloginName)){
            return getFailRtn("请先登陆");
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

            //如果没有获取到书名，说明其他也没获取到，直接返回地址错
            if(ObjectUtil.isNull(bookNameEle)){
                return getFailRtn("网址错误，请确认网址");
            }

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

                String isDownloading = redisService.get(bookId);
                log.info("判断此书是否在下载中:{}",isDownloading);
                if(StringUtils.isNotEmpty(isDownloading)){
                    log.info("此书正在下载中，不允许重复下载");
                    return getFailRtn("正在下载中，可点链接查看进度"+getDownLoadUrl(bookId));
                }
                redisService.set(bookId, "下载中");

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

                CountDownLatch downLatch = new CountDownLatch(countDownSize);
//                CountDownLatch downLatch = new CountDownLatch(childElements.get(0).children().size());

                XsNewBookThread newBookThread = new XsNewBookThread(bookId, emailAddress,downLatch,fileName,xsContentService,dzsPath,contentId,conFolderPath,childElements,proxyUrl,redisService);
                Thread nBt = new Thread(newBookThread);
                nBt.start();

            }else{
                log.info("此书已经有，把没有爬取到的继续爬取");
                bookId = bookList.get(0).getId();

                String isDownloading = redisService.get(bookId);
                log.info("判断此书是否在下载中:{}",isDownloading);
                if(StringUtils.isNotEmpty(isDownloading)){
                    log.info("此书正在下载中，不允许重复下载");
                    return getFailRtn("正在下载中，可点链接查看进度");
                }
                redisService.set(bookId, "下载中");

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

                redisService.del(bookId);

                //新的章节爬取完，发邮件
                XsContent xscon = new XsContent();
                xscon.setBookId(bookId);
                xscon.setZjOrder(lastOrder);
                createFileAndSendMail(xscon,savePath,fileName,emailAddress);
            }

            //记录用户和书的关系，谁下载的



            log.info("走完");

            return getSussRtn("爬取成功，耐心等待", "爬取成功，耐心等待。可点击"+getDownLoadUrl(bookId));

        } catch (IOException e) {
            e.printStackTrace();
            log.info("获取页面出错，请检查url地址");
            return getFailRtn("获取页面出错，请检查url地址");
        } catch (Exception e) {
            e.printStackTrace();
            return getFailRtn("获取失败，联系管理员");
        }

    }

    private String getDownLoadUrl(String bookId) {
        //String url = "http://localhost:8080/order-api/xsdown/downloadProgress?bookId=490165b0a0004725a9f86593b2785ac9";
        String url = "http://"+ipadd+":8080/"+projectName+"/xsdown/downloadProgress?bookId="+bookId;
        return url;
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
        String webUrlaa = "http://www.twxs8.com/31_31301/";

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

    @GetMapping("/xsUserAddPage")
    public ModelAndView xsUserAddPage(){
        ModelAndView model = new ModelAndView();
        model.setViewName("ligerui/myWork/jsoup/xsUserAdd");

        return model;
    }

    @GetMapping("/xsUserLoginPage")
    public ModelAndView xsUserLoginPage(){
        ModelAndView model = new ModelAndView();
        model.setViewName("ligerui/myWork/jsoup/xsUserLogin");

        return model;
    }


    @RequestMapping(value = "xsUserAdd")
    @ResponseBody
    public Map<String, Object> xsUserAdd(String username,String password,String emailAddress,HttpSession session) {
        if(StringUtils.isEmpty(username)){
            return getFailRtn("用户名不能为空");
        }
        if(StringUtils.isEmpty(password)){
            return getFailRtn("密码不能为空");
        }
        if (StringUtils.isBlank(emailAddress)) {
            return getFailRtn("邮箱不能为空");
        }
        if(StringUtils.isNotEmpty(emailAddress)){
            emailAddress = emailAddress.trim();
            if(!EmailUtils.validateEmail(emailAddress)){
                return getFailRtn("邮箱地址不正确");
            }
        }
        XsUser xsUser = new XsUser();
        xsUser.setUsername(username.trim());
        List<XsUser> list = xsUserService.selectByCondition(xsUser);
        if(list.size()>0){
            return getFailRtn("此用户名已存在");
        }

        xsUser.setPassword(password.trim());
        xsUser.setEmailAddress(emailAddress);
        xsUser.setCreateTime(new Date());
        xsUser.setId(UUID.randomUUID().toString().replace("-", ""));
        int i = xsUserService.insertSelective(xsUser);
        if(i>0){
            session.setAttribute("xsUserloginName", username.trim());
            return getSussRtn("", "注册成功");
        }
        return getFailRtn("注册失败");
    }



    @RequestMapping(value = "xsUserLogin")
    @ResponseBody
    public Map<String, Object> xsUserLogin(String username,String password,HttpSession session) {
        if(StringUtils.isEmpty(username)){
            return getFailRtn("用户名不能为空");
        }
        if(StringUtils.isEmpty(password)){
            return getFailRtn("密码不能为空");
        }
        XsUser xsUser = new XsUser();
        xsUser.setUsername(username.trim());
        xsUser.setPassword(password.trim());
        List<XsUser> list = xsUserService.selectByCondition(xsUser);
        if(list.size()==0){
            return getFailRtn("用户名或密码错，重新输入");
        }
        else{
            session.setAttribute("xsUserloginName", list.get(0).getUsername());
            log.info("设置session：xsUserloginName:{}",list.get(0).getUsername());
            return getSussRtn(list.get(0), "登陆成功，可下载");
        }
    }


    @RequestMapping(value = "downloadProgress")
    @ResponseBody
    public String downloadProgress(String bookId) {
        if (StringUtils.isBlank(bookId)) {
            return "已下载完";
        }
        XsBook book = new XsBook();
        book.setId(bookId);
        List<XsBook> xsBooks = xsBookService.selectByCondition(book);
        if(xsBooks.size()==1){
            XsContent xsAllSize = new XsContent();
            xsAllSize.setBookId(bookId);
            int allSize = xsContentService.selectCountByCondition(xsAllSize);
            xsAllSize.setIsSuc("1");
            int sucSize = xsContentService.selectCountByCondition(xsAllSize);
            return "《"+xsBooks.get(0).getBookName()+"》---下载进度:"+sucSize+"/"+allSize;
        }else{
            return "无此链接或已下载完，请检查";
        }
    }


}
