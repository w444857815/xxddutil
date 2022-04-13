<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>直接建线程</title>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.10.2.min.js"></script>
    <script type="text/javascript">

        function zhuanJson(){
            $.ajax({
                type:"post",
                url:"${pageContext.request.contextPath}/thread/zhuanJson",
                success:function(data){
                    if(data.code=="0000"){
                        $('#result').html('');
                        $('#result').html("data:"+data.data+";msg:"+data.msg);
                    }
                },
                error:function(e){

                }
            })

        }




    </script>
</head>
<body>

<h1>目的:新建一个线程去执行不影响主业务但耗时较长的代码块</h1><br>
步骤:<br>
①主线程有一个实体转json字符串的方法。<br/>
②执行完后新建一个线程，里面有查询数据并打印出来的功能。<br>
<input value="转化json在本业务，查询sql在新线程,并打印主线程传来的数据" onclick="zhuanJson()" type="button"/><br>
    <pre>
        <code>
            <span style="color: red">//主线程-----</span>
            2022-04-12 17:22:06.691 [http-nio-8080-exec-4] INFO  c.d.o.controller.ThreadController [50]- 转化出来的数据：{"ddh":"订单号","ddrq":"2022-04-12 17:22:06","fpqqlsh":"流水号","sldMc":"开票点名称","xhfMc":"销货方名称","xhfNsrsbh":"销方识别号"}
            2022-04-12 17:22:06.691 [http-nio-8080-exec-4] INFO  c.d.o.controller.ThreadController [55]- 新建线程开始
            2022-04-12 17:22:06.696 [http-nio-8080-exec-4] INFO  c.d.o.controller.ThreadController [59]- 新建线程结束
            <span style="color: red">//主线程-----</span>

            <span style="color: red">//新建线程里的执行----</span>
            2022-04-12 17:22:06.698 [Thread-32] INFO  c.dxhy.order.thread.PrintDateThread [39]- 打印获取数据库线程开始
            Creating a new SqlSession
            SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@6856b755] was not registered for synchronization because synchronization is not active
            Cache Hit Ratio [SQL_CACHE]: 0.5
            JDBC Connection [HikariProxyConnection@280179908 wrapping com.mysql.cj.jdbc.ConnectionImpl@62e21517] will not be managed by Spring
            ==>  Preparing: SELECT count(0) FROM article WHERE gg_status = '0' AND title LIKE CONCAT(CONCAT('%', ?), '%')
            ==> Parameters: (String)
            <==    Columns: count(0)
            <==        Row: 1
            <==      Total: 1
            ==>  Preparing: select id, title,hot, content, is_pic, open_id,gg_status,visit_num, create_time, update_time from article where gg_status = '0' and title like CONCAT(CONCAT('%', ?), '%') order by create_time desc LIMIT ?
            ==> Parameters: (String), 5(Integer)
            <==    Columns: id, title, hot, content, is_pic, open_id, gg_status, visit_num, create_time, update_time
            <==        Row: 99, 在我要发布里发布信息可在此页面展示, null, <<BLOB>>, null, oCiV85TsfmKhz7wATQjFAComkAOQ, 0, 73, 2021-07-03 11:11:44, 2021-07-28 10:13:30
            <==      Total: 1
            Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@6856b755]
            2022-04-12 17:22:06.750 [Thread-32] INFO  c.d.o.service.impl.WankeServiceImpl [55]- 返回值com.dxhy.order.util.PageUtils@41ebea7c
            2022-04-12 17:22:06.751 [Thread-32] INFO  c.dxhy.order.thread.PrintDateThread [40]- 获取到的字符传参:{"ddh":"订单号","ddrq":"2022-04-12 17:22:06","fpqqlsh":"流水号","sldMc":"开票点名称","xhfMc":"销货方名称","xhfNsrsbh":"销方识别号"}。从wankeService获取到的数据:{"currPage":1,"list":[{"content":"<p><strong>万科城自己的小程序，自由发布信息</strong><br><br>此处展示用户发布的消息，想要发布可在：我的→我要发布 里面进行发表，可将一些公共的信息发到此处大家共享</p>","createTime":"2021-07-03 11:11:44","ggStatus":"0","id":99,"openId":"oCiV85TsfmKhz7wATQjFAComkAOQ","title":"在我要发布里发布信息可在此页面展示","updateTime":"2021-07-28 10:13:30","visitNum":73}],"pageSize":5,"totalCount":1,"totalPage":1}
            2022-04-12 17:22:06.751 [Thread-32] INFO  c.dxhy.order.thread.PrintDateThread [42]- 打印获取数据库线程结束
            <span style="color: red">//新建线程里的执行----</span>


        </code>
    </pre>


    <span id="result"></span>
</body>
</html>