package com.dxhy.order.controller;

import com.dxhy.order.model.HtmlTree;
import com.dxhy.order.util.JsonUtils;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class AiXiaWangTest {
    private static String basePath = "https://www.ixdzs.com/";
    private static int waitTime = 5000;



    private static void getBooks() throws FailingHttpStatusCodeException, MalformedURLException, IOException, SQLException {

        String url = "https://www.ixdzs.com/sort/1/index.html";
        //https://www.ixdzs.com/sort/1/index_0_2_0_1.html
        url = url.replace("index.html", "index_0_2_0_" + 1 + ".html");
        String html = initWebClient(url);
        //System.out.println(html);
        Document document = Jsoup.parse(html);
        document.setBaseUri(basePath);
        Element masthead = document.select("div.box_k").select(".mt15").first();
        //System.out.println(masthead);

        /**
         * 遍历获取目录
         */
        Elements links = masthead.getElementsByTag("li");
        for (int i = 0; i < links.size(); i++) {
            Element muluChild = links.get(i);
            //System.out.println(muluChild);
            //标题
            String title = muluChild.select("h2.b_name").first().getElementsByTag("a").text().trim();
            System.out.println(title);

            //地址
            String getUrl = muluChild.select("h2.b_name").first().getElementsByTag("a").attr("abs:href");
            System.out.println(getUrl);
            //类型，传过来吧
            String tag = "武侠";
            System.out.println(tag);
            //作者
            String author = muluChild.select("span.l1").first().getElementsByTag("a").text().trim();
            System.out.println(author);
            //字数
            String words_num = muluChild.select("span.l2").first().text().trim();
            System.out.println(words_num);
            //状态
            String status = muluChild.select("i.cp").first().text().trim();
            System.out.println(status);
            //简介
            String bookIntro = muluChild.select("p.b_intro").first().text().trim();
            System.out.println(bookIntro);
            //最新章节
            String lastNewChapter = muluChild.select("span.l5").first().getElementsByTag("a").first().text().trim();
            System.out.println(lastNewChapter);
            String lastNewChapterGetUrl = muluChild.select("span.l5").first().getElementsByTag("a").first().attr("abs:href");
            System.out.println(lastNewChapterGetUrl);
            //最新更新时间
            String lastNewChapterTime = muluChild.select("span.l5").first().getElementsByTag("i").first().text().trim();
            System.out.println(lastNewChapterTime);


            /*String sql = "INSERT INTO `xs_book` (`title`, `title_sub`, `get_url`, `title_tag`, `author`, `author_sub`, `words_num`, `status`, `book_intro`, `last_new_chapter`, `last_new_chapter_get_url`, `last_new_chapter_time`) VALUES "
                    + "('" + title + "', '', '" + url + "', '" + tag + "', '" + author + "', '', '" + words_num + "', '" + status + "', '" + bookIntro + "', '" + lastNewChapter + "', '" + lastNewChapterGetUrl + "', '" + lastNewChapterTime + "');";

            jdbcConnect.executeSql(sql);*/
			/*Thread.sleep(waitTime);
			//根据目录获取下面的内容
			getContent(muluChild.attr("abs:href"));
			*/
        }


    }

    private static void getTypes() throws FailingHttpStatusCodeException, MalformedURLException, IOException, SQLException {

        String url = "https://www.ixdzs.com";
        String html = initWebClient(url);
        //System.out.println(html);
        Document document = Jsoup.parse(html);
        document.setBaseUri(basePath);
        Element masthead = document.select("div.nav_l").select(".fdl").first();
        //System.out.println(masthead);

        /**
         * 遍历获取目录
         */
        Elements links = masthead.getElementsByTag("a");
        for (int i = 0; i < links.size(); i++) {
            Element muluChild = links.get(i);
            System.out.println(muluChild);
            System.out.println(muluChild.text());
            System.out.println(muluChild.attr("abs:href"));
            /*String sql = "INSERT INTO `xs_type` (`type_name`, `get_url`) VALUES ('" + muluChild.text() + "', '" + muluChild.attr("abs:href") + "');";
            jdbcConnect.executeSql(sql);*/
			/*Thread.sleep(waitTime);
			//根据目录获取下面的内容
			getContent(muluChild.attr("abs:href"));
			*/
        }


    }

    private static void getContent(String url) throws FailingHttpStatusCodeException, MalformedURLException, IOException {

        url = "http://www.chuixue.net/manhua/19750/200907.html";
        String html = initWebClient(url);
        //System.out.println(html);
        Document document = Jsoup.parse(html);
        Element title = document.select("#viewimg").first();
        System.out.println("开始");
        System.out.println(title);
        System.out.println("结束");
        //Element content = document.select("#content").first();
//		System.out.println(content);
        /**
         * 获取到里面的内容后，写入到本地文件
         * bookname
         */
        //ForFile.createFile(title.text(), content.html());


    }

    private static void getMulu() throws FailingHttpStatusCodeException, MalformedURLException, IOException, SQLException {
        String url = "https://www.ixdzs.com/d/171/171405/";
        url = url.replace("www", "read").replace("/d", "");
        System.out.println(url);
        //这是获取已完结的
//		String url = "https://www.ixdzs.com/sort/1/index_0_2_0_1.html";

        String html = initWebClient(url);
        Document document = Jsoup.parse(html);
        document.setBaseUri(url);
        Element masthead = document.select("div.catalog").first();
//		System.out.println(masthead);

        /**
         * 遍历获取目录
         */
        Elements links = masthead.getElementsByTag("li").select("li.chapter");
        for (int i = 0; i < links.size(); i++) {
            Element muluChild = links.get(i);
            String dicName = muluChild.select("li.chapter").first().getElementsByTag("a").text().trim();
            System.out.println(dicName);
            String getUrl = muluChild.select("li.chapter").first().getElementsByTag("a").attr("abs:href");
            System.out.println(getUrl);
            /*String sql = "INSERT INTO `xs_dic_content` (`book_id`, `dic_name`, `dic_geturl`, `is_get`, `vote_num`) VALUES "
                    + "('1', '" + dicName + "', '" + getUrl + "', '0', '0');";
            jdbcConnect.executeSql(sql);*/
			/*Thread.sleep(waitTime);
			//根据目录获取下面的内容
			getContent(muluChild.attr("abs:href"));
			*/
        }


    }



    public static String initWebClient(String url) throws FailingHttpStatusCodeException, MalformedURLException, IOException {

        // 用http代理
        WebClient webClient = new WebClient();

        HtmlPage rootPage = webClient.getPage(url);
        webClient.closeAllWindows();

        String html = rootPage.asXml();

        Document document = Jsoup.parse(html);

        //System.out.println(JsonUtils.getInstance().toJsonString(document));

        //Element masthead = document.select("div.box_k").select(".mt15").first();
        System.out.println("获取到的内容是:");
        System.out.println(html);
        return html;

        /*//设置代理
        ProxyConfig proxyConfig = webClient.getOptions().getProxyConfig();
        proxyConfig.setProxyHost(proxyHost);
        proxyConfig.setProxyPort(proxyPort);
        try {
            webClient.getBrowserVersion().setUserAgent(userAgent);
            webClient.setRefreshHandler(new ThreadedRefreshHandler());
            //禁用Css，可避免自动二次请求CSS进行渲染  
            webClient.getOptions().setCssEnabled(false);
            //启动js
            webClient.getOptions().setJavaScriptEnabled(false);
            //运行报错不抛出异常
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            webClient.getOptions().setRedirectEnabled(true);
            //时间
            webClient.getOptions().setTimeout(120000);
            //忽略ssl认证  
            webClient.getOptions().setUseInsecureSSL(true);
            webClient.setJavaScriptTimeout(300000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        
        //构造一个webClient 模拟Chrome 浏览器
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        //屏蔽日志信息
        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",
                "org.apache.commons.logging.impl.NoOpLog");
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
        //支持JavaScript
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setActiveXNative(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setTimeout(waitTime);
        HtmlPage rootPage = webClient.getPage(url);
        //设置一个运行JavaScript的时间
        webClient.waitForBackgroundJavaScript(waitTime);
        webClient.close();
        return rootPage.asXml();*/
    }





    public static void GUI(String htmlStr) throws IOException {
        // create window
        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //this.setTitle("JTree Example");
        //this.setMinimumSize(new Dimension(300, 400));
        //this.setExtendedState(3);
        System.out.println(File.separator);

        // create tree and root node
        JTree tree = new JTree();
        final DefaultMutableTreeNode ROOT = new DefaultMutableTreeNode("Html Document");

        // create model
        DefaultTreeModel treeModel = new DefaultTreeModel(ROOT);
        tree.setModel(treeModel);

        // add scrolling tree to window

        htmlStr = "<html>"
                +   "<head>"
                +       "<title><h3>First parse<h3></title>"

                +   "</head>"
                +   "<body>"
                +       "<a>a里面</a>"
                +       "<h3>h3里面</h3>"
                +       "<p>Parsed HTML into a doc.<span>p里的span</span></p>"
                +   "</body>"
                + "</html>";
        htmlStr = FileUtils.readFileToString(new File("E://xx.txt"), "UTF-8");

        System.out.println(htmlStr);
        // parse document (can be cleaned too)
        Document doc = Jsoup.parse(htmlStr);
        //System.out.println(doc.getAllElements());
        Elements body = doc.getElementsByTag("body");

        //String xPath = "/html/body";
        String xPath = "/html/body/div[4]/div[2]/dl";
        Elements childElements = doc.selectXpath(xPath);
        System.out.println("body下的数量"+childElements.size());
        System.out.println("tag获取的"+body.size());
        System.out.println("此tag获取的children"+childElements.get(0).children().size());

        for (int i = 0; i <childElements.get(0).children().size() ; i++) {
            System.out.println(childElements.get(0).children().get(i).tagName()+"???"+childElements.get(0).children().get(i).html());
        }





        System.out.println(body+"*********"+body.size());
        System.out.println(body.get(0).nodeName());

        System.out.println("******1*****"+doc.getElementsByAttribute("body"));
        System.out.println("*****2****"+doc.selectXpath("/html/body").get(0).children());



        HtmlTree htmlTree = new HtmlTree();
        Element rootBody = doc.selectXpath("/html/body").get(0);
        htmlTree.setTag("/html/body");
        htmlTree.setNowHtml(rootBody.html());


        //body下的孩子们
        System.out.println("分隔符开始-------------");

        buildHtmlTree(xPath,doc,htmlTree);

        System.out.println(JsonUtils.getInstance().toJsonString(htmlTree));

        System.out.println("分隔符结束-------------");



        List<HtmlTree> treeChild = new LinkedList<>();

        Elements bodyChild = doc.selectXpath(xPath).get(0).children();


        /*for (int i = 0; i < bodyChild.size(); i++) {
            String item = bodyChild.get(i).nodeName();
            System.out.println(bodyChild.get(i).nodeName());
            HtmlTree child = new HtmlTree();
            child.setTag("/html/body"+"/"+item);
            child.setNowHtml(bodyChild.get(i).html());
            treeChild.add(child);
            if(doc.selectXpath(xPath+"/"+item).get(0).children().size()!=0){
                Elements children = doc.selectXpath(xPath + "/" + item).get(0).children();
                for (int j = 0; j < children.size(); j++) {
                    System.out.println("字字字字字"+children.get(j).nodeName());
                }
            }
        }
        htmlTree.setTagChild(treeChild);*/

        System.out.println("body下的孩子们");
        System.out.println(JsonUtils.getInstance().toJsonString(htmlTree));


        System.out.println("33333333"+doc.getElementsByAttributeStarting("body"));


        //System.out.println(body.get(0).getAllElements());
        System.out.println("----------");
        //System.out.println(doc.getAllElements().first());
        // Cleaner cleaner = new Cleaner(Whitelist.simpleText());
        // doc = cleaner.clean(doc);

        // walk the document tree recursivly
        traverseRecursivly(doc.getAllElements().first(), ROOT);

        //expandAllNodes(tree);

        //System.out.println(JsonUtils.getInstance().toJsonString(tree));

    }

    private static void buildHtmlTree(String xPath, Document doc, HtmlTree htmlTree) {

        //获取到传过来的节点下一个级别的所有元素
        Elements childElements = doc.selectXpath(xPath);
        List<HtmlTree> treeList = new LinkedList<>();
        for (int i = 0; i < childElements.size(); i++) {
            Element childItem = childElements.get(i);
            System.out.println("nodeName:"+childItem.nodeName());
            System.out.println("html:"+childItem.html());

            HtmlTree tree = new HtmlTree();
            tree.setTag(childItem.nodeName());
            tree.setNowHtml(childItem.html());
            treeList.add(tree);

            /*Elements allElements = childElements.get(i).getAllElements();
            for (int j = 0; j < allElements.size(); j++) {

            }*/
        }
        htmlTree.setTagChild(treeList);

        /*Elements bodyChild = nextElements.get(next).children();
        List<HtmlTree> treeChild = new LinkedList<HtmlTree>();
        for (int i = 0; i < bodyChild.size(); i++) {
            String item = bodyChild.get(i).nodeName();
            System.out.println(bodyChild.get(i).nodeName());

            //htmlTree.setTag(doc.selectXpath(xPath+""));
            //htmlTree.setNowHtml(bodyChild.get(i).html());
            htmlTree.setTag(bodyChild.get(i).nodeName());
            htmlTree.setNowHtml(bodyChild.get(i).html());


            HtmlTree child = new HtmlTree();
            child.setTag(xPath+"/"+item);
            child.setNowHtml(bodyChild.get(i).html());
            treeChild.add(child);

            System.out.println("第"+i+"个数据是"+JsonUtils.getInstance().toJsonString(htmlTree));

            Elements diguiChild = doc.selectXpath(xPath + "/" + item);
            for (int j = 0; j <diguiChild.size() ; j++) {
                if(diguiChild.get(j).children().size()!=0) {
                    buildHtmlTree(xPath + "/" + item, doc, child);
                }
            }
            *//*if(doc.selectXpath(xPath+"/"+item).get(i).children().size()!=0){
                buildHtmlTree(xPath+"/"+item, doc, child);
            *//**//*Elements children = doc.selectXpath(xPath + "/" + item).get(0).children();
            for (int j = 0; j < children.size(); j++) {
                System.out.println("字字字字字"+children.get(j).nodeName());
            }*//**//*
            }*//*
        }
        htmlTree.setTagChild(treeChild);*/

    }

    private static void traverseRecursivly(Node docNode, DefaultMutableTreeNode treeNode) {
        // iterate child nodes:
        for (Node nextChildDocNode : docNode.childNodes()) {
            // create leaf:
            DefaultMutableTreeNode nextChildTreeNode = new DefaultMutableTreeNode(nextChildDocNode.nodeName());
            // add child to tree:
            treeNode.add(nextChildTreeNode);
            // do the same for this child's child nodes:
            traverseRecursivly(nextChildDocNode, nextChildTreeNode);
        }
    }

    // can be removed ...
    private static void expandAllNodes(JTree tree) {
        int j = tree.getRowCount();
        int i = 0;
        while (i < j) {
            tree.expandRow(i);
            i += 1;
            j = tree.getRowCount();
        }
    }


    public static void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException, SQLException {
        String url = "http://www.twxs8.com/5_5518/";

        //构造一个webClient 模拟Chrome 浏览器
        /*WebClient webClient = new WebClient(BrowserVersion.FIREFOX_3);
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
        HtmlPage rootPage = webClient.getPage(url);
        //设置一个运行JavaScript的时间
        webClient.waitForBackgroundJavaScript(waitTime);
        webClient.closeAllWindows();
        System.out.println("最后获取到的数据是"+rootPage.asXml());
*/

        //GUI(rootPage.asXml());
        GUI("");
        //initWebClient(url);

        //获取大类型    修真，情感，武侠   这类
//		getTypes();

        //获取具体的书    大主宰，斗破苍穹，龙蟠     这类
        //getBooks();

        //获取到某一个书下面的章节，    第一张。。。。第一百章
//		getMulu();

        //获取到某一张下面的内容
//		getContent("");
    }

}
