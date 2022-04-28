<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>歌曲电影统计</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.10.2.min.js"></script>
    <script type="text/javascript">

        function zhuanJson(){
            var songName = $('#songName').val();
            var jianjie = $('#jianjie').val();
            $.ajax({
                type:"post",
                url:"${pageContext.request.contextPath}/tjsong/checkAdd",
                data:{
                    songName:songName,
                    jianjie:jianjie
                },
                success:function(data){
                    if(data.code=="0000"){
                        $('#result').html('');
                        $('#result').html(data.msg);
                    }else{
                        $('#result').html('');
                        $('#result').html(data.msg);
                    }
                },
                error:function(e){

                }
            })

        }

        function qingkong(){
            $('#songName').val('');
            $('#jianjie').val('');
        }

    </script>
    <style>
        /* 小屏幕手机端 */
        @media (min-width: 0px) and (max-width:768px) {
            .div1{
                width: 100px;
                height: 100px;
                background-color: red;
            }
        }

        /* 中等屏幕（桌面显示器，大于等于 992px） */
        @media (min-width: 768px) and (max-width:992px){
            .div1{
                width: 300px;
                height: 300px;
                background-color: blue;
            }
        }

        /* 大屏幕（大桌面显示器，大于等于 1200px） */
        @media (min-width: 992px) {
            .div1{
                width: 500px;
                height: 500px;
                background-color: aqua;
            }
        }
    </style>
</head>
<body>

    输入名称:<input id="songName"/>  <input value="清空" onclick="qingkong()" type="button">
<br>
    输入描述:<textarea rows="5" cols="20" id="jianjie"></textarea><br>
    <input type="button" value="提交" style="width: 150px;height: 60px;" onclick="zhuanJson()"/><br>
    <span id="result"></span>

</body>
</html>