<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>线程池</title>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.10.2.min.js"></script>
    <script type="text/javascript">

        function zhuanJson(){
            $.ajax({
                type:"post",
                url:"${pageContext.request.contextPath}/thread/countDownThread",
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


        function zhuanJsonPool(){
            $.ajax({
                type:"post",
                url:"${pageContext.request.contextPath}/thread/dengdaiThreadPool",
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
    <h1>目的:for里循环太慢，放入线程，但又要用每一个执行完后返回的数据。例如开票for，后续用处理完的开票数据list</h1>
    <h2>就像开王者一样，所有人都准备(单人线程)了，主线程才开始游戏(也可专门放一个线程去等待，主线程继续往下走)</h2>
    <h3>CountDownLauth和CyclicBarrier区别是，前者会执行完线程里的，然后执行主线程。后者不执行线程里方法，直到准备一定数量后一起。前在主线程await，后在线程里await</h3>
    步骤:<br>
    ①List<CommonOrder> list = new LinkedList<CommonOrder>();<br>
        fori{<br>
            处理逻辑耗时，然后<br>
            list.add(CommonOrder);<br>
        }<br>
    ②可在for里新建线程，把list传参进去。每一个线程处理完后list里添加值。最后可获取多线程处理的总的list<br>

    <input value="开始执行，后台看日志" onclick="zhuanJson()" type="button"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <br/>
    <span style="color: red;">直接创建线程的执行日志</span>
    <pre>
        <code>
            <span style="color: red">2022-04-13 10:54:46.362 [http-nio-8080-exec-2] INFO  c.d.o.controller.ThreadController [164]- 进入准备界面</span>
            2022-04-13 10:54:46.366 [Thread-142] INFO  c.d.order.thread.WangzheReadyThread [40]- 玩家1正在准备
            2022-04-13 10:54:46.370 [Thread-143] INFO  c.d.order.thread.WangzheReadyThread [40]- 玩家2正在准备
            2022-04-13 10:54:46.371 [Thread-143] INFO  c.d.order.thread.WangzheReadyThread [44]- 玩家2准备完成，耗时0秒
            2022-04-13 10:54:46.378 [Thread-149] INFO  c.d.order.thread.WangzheReadyThread [40]- 玩家8正在准备
            2022-04-13 10:54:46.379 [Thread-148] INFO  c.d.order.thread.WangzheReadyThread [40]- 玩家7正在准备
            2022-04-13 10:54:46.379 [Thread-146] INFO  c.d.order.thread.WangzheReadyThread [40]- 玩家5正在准备
            2022-04-13 10:54:46.380 [Thread-144] INFO  c.d.order.thread.WangzheReadyThread [40]- 玩家3正在准备
            2022-04-13 10:54:46.380 [Thread-145] INFO  c.d.order.thread.WangzheReadyThread [40]- 玩家4正在准备
            2022-04-13 10:54:46.379 [Thread-150] INFO  c.d.order.thread.WangzheReadyThread [40]- 玩家9正在准备
            2022-04-13 10:54:46.383 [Thread-141] INFO  c.d.order.thread.WangzheReadyThread [40]- 玩家0正在准备
            2022-04-13 10:54:46.382 [Thread-144] INFO  c.d.order.thread.WangzheReadyThread [44]- 玩家3准备完成，耗时0秒
            2022-04-13 10:54:46.385 [Thread-147] INFO  c.d.order.thread.WangzheReadyThread [40]- 玩家6正在准备
            2022-04-13 10:54:46.387 [Thread-146] INFO  c.d.order.thread.WangzheReadyThread [44]- 玩家5准备完成，耗时0秒
            2022-04-13 10:54:46.385 [Thread-141] INFO  c.d.order.thread.WangzheReadyThread [44]- 玩家0准备完成，耗时0秒
            2022-04-13 10:54:47.368 [Thread-142] INFO  c.d.order.thread.WangzheReadyThread [44]- 玩家1准备完成，耗时1秒
            2022-04-13 10:54:47.380 [Thread-149] INFO  c.d.order.thread.WangzheReadyThread [44]- 玩家8准备完成，耗时1秒
            2022-04-13 10:54:48.384 [Thread-150] INFO  c.d.order.thread.WangzheReadyThread [44]- 玩家9准备完成，耗时2秒
            2022-04-13 10:54:49.382 [Thread-145] INFO  c.d.order.thread.WangzheReadyThread [44]- 玩家4准备完成，耗时3秒
            2022-04-13 10:54:50.380 [Thread-148] INFO  c.d.order.thread.WangzheReadyThread [44]- 玩家7准备完成，耗时4秒
            2022-04-13 10:54:50.387 [Thread-147] INFO  c.d.order.thread.WangzheReadyThread [44]- 玩家6准备完成，耗时4秒
            <span style="color: red">2022-04-13 10:54:50.388 [http-nio-8080-exec-2] INFO  c.d.o.controller.ThreadController [173]- 所有玩家准备成功，开始游戏。
                所有线程处理完返回的总数据:["玩家2完成","玩家1完成","玩家4完成","玩家3完成","玩家6完成","玩家5完成","玩家8完成","玩家7完成","玩家9完成","玩家0完成"]</span>
        </code>
    </pre>


    <span id="result"></span>
</body>
</html>