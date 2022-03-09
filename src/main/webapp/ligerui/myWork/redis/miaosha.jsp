<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>请选择要看的--普通</title>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.10.2.min.js"></script>
    <script type="text/javascript">
        function goumai(mvid){
            $.ajax({
                type : "post",
                url : "${pageContext.request.contextPath}/order/createOrder",
                data : {
                    mvId:mvid
                },
                dataType : "json",
                success : function(data) {
                    if(data.code=='-1'){
                        alert(data.msg);
                    }
                    if(data.code=='0'){
                        window.location.href="${pageContext.request.contextPath}/order/orderDetailPage?orderId="+data.data;
                    }
                },
                error : function() {
                    alert("网络错误！");
                }
            });
        }
        //点赞
        function dianzan(mvid){
            $.ajax({
                type : "post",
                url : "${pageContext.request.contextPath}/order/dianZan",
                data : {
                    mvId:mvid
                },
                dataType : "json",
                success : function(data) {
                    if(data.code=='-1'){
                        alert(data.msg);
                    }
                    if(data.code=='0'){
                        <%--window.location.href="${pageContext.request.contextPath}/order/orderDetailPage?orderId="+data.data;--%>
                        $('#zan_'+mvid).html(data.data.zanOrder);
                        $('#toHid_'+mvid).html("");
                        var showStr = "";
                        $.each(data.data.orderData2, function (n, value) {
                            var name = "";
                            if(value.value==1){
                                name = "唐人街探案3";
                            }
                            if(value.value==2){
                                name = "西游降魔";
                            }
                            if(value.value==3){
                                name = "泰囧";
                            }
                            showStr+='<span style="width: 200px;float:left;">名称:'+name+',</span><span>当前赞数:'+value.score+'</span><br>';
                            <%--showStr+='<span style="width: 200px;float:left;">id：${pro.id } </span>'--%>
                        });
                        $('#order_Html').html(showStr);
                    }
                },
                error : function() {
                    alert("网络错误！");
                }
            });

        }


        //精确点赞
        function jian(){
            $.ajax({
                type : "post",
                url : "${pageContext.request.contextPath}/jian",
                data : {
                },
                dataType : "json",
                success : function(data) {
                    if(data.code=='0000'){
                        $('#shengyu').html(data.data);
                    }else{
                        $('#maiwan').html(data.msg);
                    }
                },
                error : function() {
                    alert("网络错误！");
                }
            });
        }

        //初始化库存
        function chushi(){
            $.ajax({
                type : "post",
                url : "${pageContext.request.contextPath}/chushi",
                data : {
                },
                dataType : "json",
                success : function(data) {
                    if(data.code=='0000'){
                        $('#chushinum').html(data.msg);
                        $('#shengyu').html("10");
                    }else{
                        $('#chushinum').html(data.msg);
                    }
                },
                error : function() {
                    alert("网络错误！");
                }
            });
        }

    </script>
</head>
<body>

    现有库存数量：<span id="shengyu">${kucun}</span>   <br>
    <span id="maiwan"></span>
    <br>
    <button  onclick="jian()">抢购，库存减一</button>

    <br>

    <button  onclick="chushi()">初始化库存为10</button>
<span id="chushinum"></span>

    <br>

    redis实现秒杀，减库存。并发情况可通过多线程测试。<br>
    链接: http://127.0.0.1:8080/order-api/jian
</body>
</html>