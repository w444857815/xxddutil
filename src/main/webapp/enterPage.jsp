<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>输入</title>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.10.2.min.js"></script>
    <script type="text/javascript">
        function bu() {
            var orderId = $('#orderId').val();
            var kplsh = $('#kplsh').val();
            if(orderId==''||orderId==null){
                alert("输入orderId");
                return;
            }
            if(kplsh==''||kplsh==null){
                alert("输入kplsh");
                return;
            }

            $.ajax({
                url: '${pageContext.request.contextPath}/order/hehe',
                dataType: 'json',
                type: 'POST',
                data: {
                    //page:e
                    orderId:orderId,
                    newkplsh:kplsh
                },
                success: function (data) {
                    $('#result').html('');
                    if(data.code=='0000'){
                        var resultStr = '';
                        $.each(data.data, function (n, value) {
                            if(value.indexOf("失败")>0){
                                value = "<span style='color:red'>"+value+"</span>"
                            }
                            resultStr = resultStr+value+"<br>";
                        });
                        $('#result').html(resultStr);
                    }else{
                        $('#result').html(data.msg);
                    }

                },
                error: function (data) {
                    alert(data.msgCode);
                }
            });


        }
    </script>
</head>
<body>

补完自己检查数据，出现问题开发概不负责<br><br>
补完自己检查数据，出现问题开发概不负责<br><br>
补完自己检查数据，出现问题开发概不负责<br><br>

    订单号: <input id="orderId" style="width: 300px"><br>
    kplsh: <input id="kplsh" style="width: 300px"> <br>
    <input type="button" onclick="bu()" value="补订单">
<br>
结果:
    <p id="result"></p>
</body>
</html>