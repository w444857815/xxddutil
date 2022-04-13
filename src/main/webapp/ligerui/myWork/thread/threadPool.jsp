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
                url:"${pageContext.request.contextPath}/thread/simpleThreadPool",
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
<h1>目的:新建一个线程池，把线程放入池中，去执行不影响主业务但耗时较长的代码块</h1><br>
    步骤:<br>
    新建线程池，里有10条。<br>
    ①主线程只是单纯的打印放入线程池开始，完成。<br>
    ②新建40个线程放入线程池中执行，里面有主线程传来的值。<br>
    <input value="新建40个线程，放入线程池(10)中执行" onclick="zhuanJson()" type="button"/><br>
    <pre>
        <code>
            2022-04-12 17:40:17.002 [http-nio-8080-exec-5] INFO  c.d.o.controller.ThreadController [75]- 放入线程池执行
            2022-04-12 17:40:17.004 [http-nio-8080-exec-5] INFO  c.d.o.controller.ThreadController [80]- 放入线程池完成
            2022-04-12 17:40:17.054 [pool-6-thread-1] INFO  c.dxhy.order.thread.PrintDateThread [32]- 获取主线程传来的参数:主线程传参,线程的日期:2022-04-12 17:40:17 003
            2022-04-12 17:40:17.054 [pool-6-thread-2] INFO  c.dxhy.order.thread.PrintDateThread [32]- 获取主线程传来的参数:主线程传参,线程的日期:2022-04-12 17:40:17 003
            2022-04-12 17:40:17.054 [pool-6-thread-4] INFO  c.dxhy.order.thread.PrintDateThread [32]- 获取主线程传来的参数:主线程传参,线程的日期:2022-04-12 17:40:17 004
            2022-04-12 17:40:17.055 [pool-6-thread-1] INFO  c.dxhy.order.thread.PrintDateThread [32]- 获取主线程传来的参数:主线程传参,线程的日期:2022-04-12 17:40:17 054
            2022-04-12 17:40:17.054 [pool-6-thread-5] INFO  c.dxhy.order.thread.PrintDateThread [32]- 获取主线程传来的参数:主线程传参,线程的日期:2022-04-12 17:40:17 004
            2022-04-12 17:40:17.055 [pool-6-thread-2] INFO  c.dxhy.order.thread.PrintDateThread [32]- 获取主线程传来的参数:主线程传参,线程的日期:2022-04-12 17:40:17 055
            2022-04-12 17:40:17.055 [pool-6-thread-4] INFO  c.dxhy.order.thread.PrintDateThread [32]- 获取主线程传来的参数:主线程传参,线程的日期:2022-04-12 17:40:17 055
            2022-04-12 17:40:17.054 [pool-6-thread-7] INFO  c.dxhy.order.thread.PrintDateThread [32]- 获取主线程传来的参数:主线程传参,线程的日期:2022-04-12 17:40:17 004
            2022-04-12 17:40:17.054 [pool-6-thread-9] INFO  c.dxhy.order.thread.PrintDateThread [32]- 获取主线程传来的参数:主线程传参,线程的日期:2022-04-12 17:40:17 004
            2022-04-12 17:40:17.055 [pool-6-thread-1] INFO  c.dxhy.order.thread.PrintDateThread [32]- 获取主线程传来的参数:主线程传参,线程的日期:2022-04-12 17:40:17 055
            2022-04-12 17:40:17.054 [pool-6-thread-10] INFO  c.dxhy.order.thread.PrintDateThread [32]- 获取主线程传来的参数:主线程传参,线程的日期:2022-04-12 17:40:17 004
            2022-04-12 17:40:17.056 [pool-6-thread-9] INFO  c.dxhy.order.thread.PrintDateThread [32]- 获取主线程传来的参数:主线程传参,线程的日期:2022-04-12 17:40:17 056
            2022-04-12 17:40:17.054 [pool-6-thread-6] INFO  c.dxhy.order.thread.PrintDateThread [32]- 获取主线程传来的参数:主线程传参,线程的日期:2022-04-12 17:40:17 004
            2022-04-12 17:40:17.056 [pool-6-thread-7] INFO  c.dxhy.order.thread.PrintDateThread [32]- 获取主线程传来的参数:主线程传参,线程的日期:2022-04-12 17:40:17 056
            2022-04-12 17:40:17.055 [pool-6-thread-2] INFO  c.dxhy.order.thread.PrintDateThread [32]- 获取主线程传来的参数:主线程传参,线程的日期:2022-04-12 17:40:17 055
            2022-04-12 17:40:17.056 [pool-6-thread-4] INFO  c.dxhy.order.thread.PrintDateThread [32]- 获取主线程传来的参数:主线程传参,线程的日期:2022-04-12 17:40:17 055
            2022-04-12 17:40:17.056 [pool-6-thread-10] INFO  c.dxhy.order.thread.PrintDateThread [32]- 获取主线程传来的参数:主线程传参,线程的日期:2022-04-12 17:40:17 056
            2022-04-12 17:40:17.055 [pool-6-thread-5] INFO  c.dxhy.order.thread.PrintDateThread [32]- 获取主线程传来的参数:主线程传参,线程的日期:2022-04-12 17:40:17 055
            2022-04-12 17:40:17.057 [pool-6-thread-9] INFO  c.dxhy.order.thread.PrintDateThread [32]- 获取主线程传来的参数:主线程传参,线程的日期:2022-04-12 17:40:17 056
            2022-04-12 17:40:17.057 [pool-6-thread-4] INFO  c.dxhy.order.thread.PrintDateThread [32]- 获取主线程传来的参数:主线程传参,线程的日期:2022-04-12 17:40:17 057
            2022-04-12 17:40:17.057 [pool-6-thread-10] INFO  c.dxhy.order.thread.PrintDateThread [32]- 获取主线程传来的参数:主线程传参,线程的日期:2022-04-12 17:40:17 057
            2022-04-12 17:40:17.057 [pool-6-thread-5] INFO  c.dxhy.order.thread.PrintDateThread [32]- 获取主线程传来的参数:主线程传参,线程的日期:2022-04-12 17:40:17 057
            2022-04-12 17:40:17.057 [pool-6-thread-9] INFO  c.dxhy.order.thread.PrintDateThread [32]- 获取主线程传来的参数:主线程传参,线程的日期:2022-04-12 17:40:17 057
            2022-04-12 17:40:17.057 [pool-6-thread-4] INFO  c.dxhy.order.thread.PrintDateThread [32]- 获取主线程传来的参数:主线程传参,线程的日期:2022-04-12 17:40:17 057
            2022-04-12 17:40:17.058 [pool-6-thread-9] INFO  c.dxhy.order.thread.PrintDateThread [32]- 获取主线程传来的参数:主线程传参,线程的日期:2022-04-12 17:40:17 057
            2022-04-12 17:40:17.057 [pool-6-thread-5] INFO  c.dxhy.order.thread.PrintDateThread [32]- 获取主线程传来的参数:主线程传参,线程的日期:2022-04-12 17:40:17 057
            2022-04-12 17:40:17.058 [pool-6-thread-9] INFO  c.dxhy.order.thread.PrintDateThread [32]- 获取主线程传来的参数:主线程传参,线程的日期:2022-04-12 17:40:17 058
            2022-04-12 17:40:17.057 [pool-6-thread-10] INFO  c.dxhy.order.thread.PrintDateThread [32]- 获取主线程传来的参数:主线程传参,线程的日期:2022-04-12 17:40:17 057
            2022-04-12 17:40:17.058 [pool-6-thread-9] INFO  c.dxhy.order.thread.PrintDateThread [32]- 获取主线程传来的参数:主线程传参,线程的日期:2022-04-12 17:40:17 058
            2022-04-12 17:40:17.059 [pool-6-thread-10] INFO  c.dxhy.order.thread.PrintDateThread [32]- 获取主线程传来的参数:主线程传参,线程的日期:2022-04-12 17:40:17 059
            2022-04-12 17:40:17.057 [pool-6-thread-6] INFO  c.dxhy.order.thread.PrintDateThread [32]- 获取主线程传来的参数:主线程传参,线程的日期:2022-04-12 17:40:17 057
            2022-04-12 17:40:17.059 [pool-6-thread-9] INFO  c.dxhy.order.thread.PrintDateThread [32]- 获取主线程传来的参数:主线程传参,线程的日期:2022-04-12 17:40:17 059
            2022-04-12 17:40:17.060 [pool-6-thread-2] INFO  c.dxhy.order.thread.PrintDateThread [32]- 获取主线程传来的参数:主线程传参,线程的日期:2022-04-12 17:40:17 057
            2022-04-12 17:40:17.057 [pool-6-thread-7] INFO  c.dxhy.order.thread.PrintDateThread [32]- 获取主线程传来的参数:主线程传参,线程的日期:2022-04-12 17:40:17 057
            2022-04-12 17:40:17.060 [pool-6-thread-4] INFO  c.dxhy.order.thread.PrintDateThread [32]- 获取主线程传来的参数:主线程传参,线程的日期:2022-04-12 17:40:17 058
            2022-04-12 17:40:17.054 [pool-6-thread-8] INFO  c.dxhy.order.thread.PrintDateThread [32]- 获取主线程传来的参数:主线程传参,线程的日期:2022-04-12 17:40:17 004
            2022-04-12 17:40:17.056 [pool-6-thread-1] INFO  c.dxhy.order.thread.PrintDateThread [32]- 获取主线程传来的参数:主线程传参,线程的日期:2022-04-12 17:40:17 056
            2022-04-12 17:40:17.058 [pool-6-thread-5] INFO  c.dxhy.order.thread.PrintDateThread [32]- 获取主线程传来的参数:主线程传参,线程的日期:2022-04-12 17:40:17 058
            2022-04-12 17:40:17.060 [pool-6-thread-10] INFO  c.dxhy.order.thread.PrintDateThread [32]- 获取主线程传来的参数:主线程传参,线程的日期:2022-04-12 17:40:17 059
            2022-04-12 17:40:17.054 [pool-6-thread-3] INFO  c.dxhy.order.thread.PrintDateThread [32]- 获取主线程传来的参数:主线程传参,线程的日期:2022-04-12 17:40:17 004

        </code>
    </pre>
    <span id="result"></span>
</body>
</html>