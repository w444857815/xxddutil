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
            var webUrl = $('#webUrl').val();
            var listXpath = $('#listXpath').val();
            var bookNameId = $('#bookName').val();
            var contentId = $('#contentId').val();
            var emailAddress = $('#emailAddress').val();
            // $('#anniu').attr("disabled",true);
            $('#result').html("已请求，成功与否都会以邮件通知");
            $.ajax({
                type:"post",
                url:"${pageContext.request.contextPath}/jsoup/getThisBook",
                data:{
                    webUrl:webUrl,
                    listXpath:listXpath,
                    bookNameId:bookNameId,
                    contentId:contentId,
                    emailAddress:emailAddress
                },
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
    <h2 style="color: red">只输入网址和邮箱即可，其他勿动</h2>
    在此地址里找到书的列表页，可爬取此书全章节内容。<span>http://www.twxs8.com/5_5518/</span> <br>
    输入网页: <input id="webUrl" style="width: 200px;" value="http://www.twxs8.com/5_5518/"> <br>
    邮箱地址:<input id="emailAddress" value="444857815@qq.com"><br>

    <input type="button" id="anniu" style="height: 30px" value="开始下载并发送" onclick="zhuanJson()"> <br>
    <span id="result"></span>
    <br><br><br><br>


    下面内容无须理会 <br>
    此网站列表xpath路径(<span style="color: red">图1</span>):<input id="listXpath" value="/html/body/div[4]/div[2]/dl"></input> <br>
    书名id(列表页<span style="color: red">图2</span>):<input id="bookName" value="info"><br>
    详情文章id(文章详情页<span style="color: red">图3</span>):<input id="contentId" value = "content"><br>

    <br>
    图1:<br>
    <img src="${pageContext.request.contextPath}/ligerui/myWork/jsoup/xpath.jpg"><br>
    图2:<br>
    <img src="${pageContext.request.contextPath}/ligerui/myWork/jsoup/FileName.jpg"><br>
    图3:<br>
    <img src="${pageContext.request.contextPath}/ligerui/myWork/jsoup/content.jpg">


</body>
</html>