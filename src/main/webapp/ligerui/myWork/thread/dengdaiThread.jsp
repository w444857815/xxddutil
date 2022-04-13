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
                url:"${pageContext.request.contextPath}/thread/dengdaiThread",
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
    <h1>目的:模拟并发请求。创建线程等待，然后同时请求。</h1>
    步骤:<br>

    ①模拟并发请求。新建的线程准备好后不执行，<span style="color: red">在等到设定的数量时，再一起执行。</span>关键字 CyclicBarrier cyclicBarrier = new CyclicBarrier(20); 定义要准备的数<br>
    ②线程池和上面直接起线程一样。只是<span style="color: red">用线程池的时候池里数量必须大于等于要准备的数量。</span>要是池10，要准备30，就会在放入10个后程序卡住宕机。<br>

    <input value="直接起线程。等待创建20个线程后，一起执行" onclick="zhuanJson()" type="button"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <input value="线程池。等待创建20个线程后，一起执行，池数量必须大于等于要等待的数量，不然池放不下会卡住" onclick="zhuanJsonPool()" type="button"/><br>
    <br>
    <span style="color: red;">直接创建线程的执行日志</span>
    <pre>
        <code>
            2022-04-12 19:33:32.314 [http-nio-8080-exec-10] INFO  c.d.o.controller.ThreadController [102]- 新建线程开始
            2022-04-12 19:33:32.322 [http-nio-8080-exec-10] INFO  c.d.o.controller.ThreadController [110]- 新建线程结束
            2022-04-12 19:33:32.318 [Thread-114] INFO  com.dxhy.order.thread.DengdaiThread [35]- 准备线程:3
            2022-04-12 19:33:32.339 [Thread-111] INFO  com.dxhy.order.thread.DengdaiThread [35]- 准备线程:0
            2022-04-12 19:33:32.341 [Thread-112] INFO  com.dxhy.order.thread.DengdaiThread [35]- 准备线程:1
            2022-04-12 19:33:32.342 [Thread-127] INFO  com.dxhy.order.thread.DengdaiThread [35]- 准备线程:16
            2022-04-12 19:33:32.349 [Thread-113] INFO  com.dxhy.order.thread.DengdaiThread [35]- 准备线程:2
            2022-04-12 19:33:32.377 [Thread-117] INFO  com.dxhy.order.thread.DengdaiThread [35]- 准备线程:6
            2022-04-12 19:33:32.387 [Thread-120] INFO  com.dxhy.order.thread.DengdaiThread [35]- 准备线程:9
            2022-04-12 19:33:32.406 [Thread-121] INFO  com.dxhy.order.thread.DengdaiThread [35]- 准备线程:10
            2022-04-12 19:33:32.409 [Thread-124] INFO  com.dxhy.order.thread.DengdaiThread [35]- 准备线程:13
            2022-04-12 19:33:32.411 [Thread-125] INFO  com.dxhy.order.thread.DengdaiThread [35]- 准备线程:14
            2022-04-12 19:33:32.415 [Thread-128] INFO  com.dxhy.order.thread.DengdaiThread [35]- 准备线程:17
            2022-04-12 19:33:32.418 [Thread-129] INFO  com.dxhy.order.thread.DengdaiThread [35]- 准备线程:18
            2022-04-12 19:33:32.420 [Thread-115] INFO  com.dxhy.order.thread.DengdaiThread [35]- 准备线程:4
            2022-04-12 19:33:32.420 [Thread-118] INFO  com.dxhy.order.thread.DengdaiThread [35]- 准备线程:7
            2022-04-12 19:33:32.423 [Thread-119] INFO  com.dxhy.order.thread.DengdaiThread [35]- 准备线程:8
            2022-04-12 19:33:32.426 [Thread-122] INFO  com.dxhy.order.thread.DengdaiThread [35]- 准备线程:11
            2022-04-12 19:33:32.428 [Thread-123] INFO  com.dxhy.order.thread.DengdaiThread [35]- 准备线程:12
            2022-04-12 19:33:32.430 [Thread-126] INFO  com.dxhy.order.thread.DengdaiThread [35]- 准备线程:15
            2022-04-12 19:33:32.433 [Thread-130] INFO  com.dxhy.order.thread.DengdaiThread [35]- 准备线程:19
            2022-04-12 19:33:32.434 [Thread-116] INFO  com.dxhy.order.thread.DengdaiThread [35]- 准备线程:5
            2022-04-12 19:33:32.436 [Thread-116] INFO  com.dxhy.order.thread.DengdaiThread [43]- 获取主线程传来的参数:5,线程的日期:2022-04-12 19:33:32 436
            2022-04-12 19:33:32.439 [Thread-114] INFO  com.dxhy.order.thread.DengdaiThread [43]- 获取主线程传来的参数:3,线程的日期:2022-04-12 19:33:32 439
            2022-04-12 19:33:32.441 [Thread-111] INFO  com.dxhy.order.thread.DengdaiThread [43]- 获取主线程传来的参数:0,线程的日期:2022-04-12 19:33:32 440
            2022-04-12 19:33:32.441 [Thread-127] INFO  com.dxhy.order.thread.DengdaiThread [43]- 获取主线程传来的参数:16,线程的日期:2022-04-12 19:33:32 441
            2022-04-12 19:33:32.442 [Thread-113] INFO  com.dxhy.order.thread.DengdaiThread [43]- 获取主线程传来的参数:2,线程的日期:2022-04-12 19:33:32 441
            2022-04-12 19:33:32.441 [Thread-112] INFO  com.dxhy.order.thread.DengdaiThread [43]- 获取主线程传来的参数:1,线程的日期:2022-04-12 19:33:32 441
            2022-04-12 19:33:32.442 [Thread-117] INFO  com.dxhy.order.thread.DengdaiThread [43]- 获取主线程传来的参数:6,线程的日期:2022-04-12 19:33:32 442
            2022-04-12 19:33:32.444 [Thread-121] INFO  com.dxhy.order.thread.DengdaiThread [43]- 获取主线程传来的参数:10,线程的日期:2022-04-12 19:33:32 444
            2022-04-12 19:33:32.447 [Thread-128] INFO  com.dxhy.order.thread.DengdaiThread [43]- 获取主线程传来的参数:17,线程的日期:2022-04-12 19:33:32 446
            2022-04-12 19:33:32.444 [Thread-120] INFO  com.dxhy.order.thread.DengdaiThread [43]- 获取主线程传来的参数:9,线程的日期:2022-04-12 19:33:32 443
            2022-04-12 19:33:32.453 [Thread-118] INFO  com.dxhy.order.thread.DengdaiThread [43]- 获取主线程传来的参数:7,线程的日期:2022-04-12 19:33:32 453
            2022-04-12 19:33:32.453 [Thread-129] INFO  com.dxhy.order.thread.DengdaiThread [43]- 获取主线程传来的参数:18,线程的日期:2022-04-12 19:33:32 452
            2022-04-12 19:33:32.455 [Thread-130] INFO  com.dxhy.order.thread.DengdaiThread [43]- 获取主线程传来的参数:19,线程的日期:2022-04-12 19:33:32 455
            2022-04-12 19:33:32.453 [Thread-125] INFO  com.dxhy.order.thread.DengdaiThread [43]- 获取主线程传来的参数:14,线程的日期:2022-04-12 19:33:32 445
            2022-04-12 19:33:32.457 [Thread-123] INFO  com.dxhy.order.thread.DengdaiThread [43]- 获取主线程传来的参数:12,线程的日期:2022-04-12 19:33:32 454
            2022-04-12 19:33:32.453 [Thread-115] INFO  com.dxhy.order.thread.DengdaiThread [43]- 获取主线程传来的参数:4,线程的日期:2022-04-12 19:33:32 453
            2022-04-12 19:33:32.452 [Thread-124] INFO  com.dxhy.order.thread.DengdaiThread [43]- 获取主线程传来的参数:13,线程的日期:2022-04-12 19:33:32 444
            2022-04-12 19:33:32.454 [Thread-126] INFO  com.dxhy.order.thread.DengdaiThread [43]- 获取主线程传来的参数:15,线程的日期:2022-04-12 19:33:32 454
            2022-04-12 19:33:32.455 [Thread-122] INFO  com.dxhy.order.thread.DengdaiThread [43]- 获取主线程传来的参数:11,线程的日期:2022-04-12 19:33:32 454
            2022-04-12 19:33:32.453 [Thread-119] INFO  com.dxhy.order.thread.DengdaiThread [43]- 获取主线程传来的参数:8,线程的日期:2022-04-12 19:33:32 453
        </code>
    </pre>

    <span style="color: red;">线程池方法，池数量必须大于等于要等待的数量，不然池放不下会卡住</span>
    <br>
    <pre>
        <code>
            2022-04-12 19:47:43.701 [http-nio-8080-exec-4] INFO  c.d.o.controller.ThreadController [133]- 放入线程池开始
            2022-04-12 19:47:43.706 [pool-6-thread-4] INFO  com.dxhy.order.thread.DengdaiThread [35]- 准备线程:3
            2022-04-12 19:47:43.706 [pool-6-thread-1] INFO  com.dxhy.order.thread.DengdaiThread [35]- 准备线程:0
            2022-04-12 19:47:43.706 [pool-6-thread-2] INFO  com.dxhy.order.thread.DengdaiThread [35]- 准备线程:1
            2022-04-12 19:47:43.706 [pool-6-thread-3] INFO  com.dxhy.order.thread.DengdaiThread [35]- 准备线程:2
            2022-04-12 19:47:43.707 [pool-6-thread-5] INFO  com.dxhy.order.thread.DengdaiThread [35]- 准备线程:4
            2022-04-12 19:47:43.707 [pool-6-thread-6] INFO  com.dxhy.order.thread.DengdaiThread [35]- 准备线程:5
            2022-04-12 19:47:43.707 [pool-6-thread-10] INFO  com.dxhy.order.thread.DengdaiThread [35]- 准备线程:9
            2022-04-12 19:47:43.707 [pool-6-thread-7] INFO  com.dxhy.order.thread.DengdaiThread [35]- 准备线程:6
            2022-04-12 19:47:43.708 [pool-6-thread-8] INFO  com.dxhy.order.thread.DengdaiThread [35]- 准备线程:7
            2022-04-12 19:47:43.708 [pool-6-thread-11] INFO  com.dxhy.order.thread.DengdaiThread [35]- 准备线程:10
            2022-04-12 19:47:43.707 [pool-6-thread-9] INFO  com.dxhy.order.thread.DengdaiThread [35]- 准备线程:8
            2022-04-12 19:47:43.708 [pool-6-thread-12] INFO  com.dxhy.order.thread.DengdaiThread [35]- 准备线程:11
            2022-04-12 19:47:43.708 [pool-6-thread-13] INFO  com.dxhy.order.thread.DengdaiThread [35]- 准备线程:12
            2022-04-12 19:47:43.708 [pool-6-thread-14] INFO  com.dxhy.order.thread.DengdaiThread [35]- 准备线程:13
            2022-04-12 19:47:43.708 [http-nio-8080-exec-4] INFO  c.d.o.controller.ThreadController [140]- 放入线程池结束
            2022-04-12 19:47:43.708 [pool-6-thread-15] INFO  com.dxhy.order.thread.DengdaiThread [35]- 准备线程:14
            2022-04-12 19:47:43.709 [pool-6-thread-18] INFO  com.dxhy.order.thread.DengdaiThread [35]- 准备线程:17
            2022-04-12 19:47:43.709 [pool-6-thread-17] INFO  com.dxhy.order.thread.DengdaiThread [35]- 准备线程:16
            2022-04-12 19:47:43.709 [pool-6-thread-16] INFO  com.dxhy.order.thread.DengdaiThread [35]- 准备线程:15
            2022-04-12 19:47:43.709 [pool-6-thread-19] INFO  com.dxhy.order.thread.DengdaiThread [35]- 准备线程:18
            2022-04-12 19:47:43.709 [pool-6-thread-20] INFO  com.dxhy.order.thread.DengdaiThread [35]- 准备线程:19
            2022-04-12 19:47:43.735 [pool-6-thread-4] INFO  com.dxhy.order.thread.DengdaiThread [43]- 获取主线程传来的参数:3,线程的日期:2022-04-12 19:47:43 710
            2022-04-12 19:47:43.735 [pool-6-thread-11] INFO  com.dxhy.order.thread.DengdaiThread [43]- 获取主线程传来的参数:10,线程的日期:2022-04-12 19:47:43 710
            2022-04-12 19:47:43.735 [pool-6-thread-13] INFO  com.dxhy.order.thread.DengdaiThread [43]- 获取主线程传来的参数:12,线程的日期:2022-04-12 19:47:43 710
            2022-04-12 19:47:43.736 [pool-6-thread-5] INFO  com.dxhy.order.thread.DengdaiThread [43]- 获取主线程传来的参数:4,线程的日期:2022-04-12 19:47:43 710
            2022-04-12 19:47:43.735 [pool-6-thread-12] INFO  com.dxhy.order.thread.DengdaiThread [43]- 获取主线程传来的参数:11,线程的日期:2022-04-12 19:47:43 710
            2022-04-12 19:47:43.737 [pool-6-thread-1] INFO  com.dxhy.order.thread.DengdaiThread [43]- 获取主线程传来的参数:0,线程的日期:2022-04-12 19:47:43 710
            2022-04-12 19:47:43.737 [pool-6-thread-7] INFO  com.dxhy.order.thread.DengdaiThread [43]- 获取主线程传来的参数:6,线程的日期:2022-04-12 19:47:43 710
            2022-04-12 19:47:43.739 [pool-6-thread-20] INFO  com.dxhy.order.thread.DengdaiThread [43]- 获取主线程传来的参数:19,线程的日期:2022-04-12 19:47:43 710
            2022-04-12 19:47:43.738 [pool-6-thread-2] INFO  com.dxhy.order.thread.DengdaiThread [43]- 获取主线程传来的参数:1,线程的日期:2022-04-12 19:47:43 710
            2022-04-12 19:47:43.739 [pool-6-thread-8] INFO  com.dxhy.order.thread.DengdaiThread [43]- 获取主线程传来的参数:7,线程的日期:2022-04-12 19:47:43 710
            2022-04-12 19:47:43.738 [pool-6-thread-19] INFO  com.dxhy.order.thread.DengdaiThread [43]- 获取主线程传来的参数:18,线程的日期:2022-04-12 19:47:43 710
            2022-04-12 19:47:43.738 [pool-6-thread-3] INFO  com.dxhy.order.thread.DengdaiThread [43]- 获取主线程传来的参数:2,线程的日期:2022-04-12 19:47:43 710
            2022-04-12 19:47:43.741 [pool-6-thread-15] INFO  com.dxhy.order.thread.DengdaiThread [43]- 获取主线程传来的参数:14,线程的日期:2022-04-12 19:47:43 710
            2022-04-12 19:47:43.742 [pool-6-thread-14] INFO  com.dxhy.order.thread.DengdaiThread [43]- 获取主线程传来的参数:13,线程的日期:2022-04-12 19:47:43 710
            2022-04-12 19:47:43.741 [pool-6-thread-18] INFO  com.dxhy.order.thread.DengdaiThread [43]- 获取主线程传来的参数:17,线程的日期:2022-04-12 19:47:43 710
            2022-04-12 19:47:43.742 [pool-6-thread-17] INFO  com.dxhy.order.thread.DengdaiThread [43]- 获取主线程传来的参数:16,线程的日期:2022-04-12 19:47:43 710
            2022-04-12 19:47:43.741 [pool-6-thread-10] INFO  com.dxhy.order.thread.DengdaiThread [43]- 获取主线程传来的参数:9,线程的日期:2022-04-12 19:47:43 710
            2022-04-12 19:47:43.742 [pool-6-thread-16] INFO  com.dxhy.order.thread.DengdaiThread [43]- 获取主线程传来的参数:15,线程的日期:2022-04-12 19:47:43 710
            2022-04-12 19:47:43.743 [pool-6-thread-9] INFO  com.dxhy.order.thread.DengdaiThread [43]- 获取主线程传来的参数:8,线程的日期:2022-04-12 19:47:43 710
            2022-04-12 19:47:43.744 [pool-6-thread-6] INFO  com.dxhy.order.thread.DengdaiThread [43]- 获取主线程传来的参数:5,线程的日期:2022-04-12 19:47:43 710
        </code>
    </pre>

    <span id="result"></span>
</body>
</html>