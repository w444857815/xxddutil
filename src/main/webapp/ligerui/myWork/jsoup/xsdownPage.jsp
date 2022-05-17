<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="description"  content="免费电子书下载txt,电子书下载txt,电子书下载网站">
    <meta name="keywords"  content="免费电子书下载txt,电子书下载txt,电子书下载网站">
    <title>免费电子书下载txt,电子书下载txt,电子书下载网站</title>
    <link href="${pageContext.request.contextPath}/ligerui/lib/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
    <link href="${pageContext.request.contextPath}/ligerui/lib/ligerUI/skins/ligerui-icons.css" rel="stylesheet" type="text/css" />
    <script src="${pageContext.request.contextPath}/ligerui/lib/jquery/jquery-1.9.0.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/ligerui/lib/ligerUI/js/core/base.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/ligerui/lib/ligerUI/js/plugins/ligerDrag.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/ligerui/lib/ligerUI/js/plugins/ligerDialog.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/ligerui/lib/ligerUI/js/plugins/ligerResizable.js" type="text/javascript"></script>
    <style type="text/css">
        .l-case-title
        {
            font-weight: bold;
            margin-top: 20px;
            margin-bottom: 20px;
        }
    </style>
    <%--<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.10.2.min.js"></script>--%>
    <script type="text/javascript">

        function zhuanJson() {
            var webUrl = $('#webUrl').val();
            var listXpath = $('#listXpath').val();
            var bookNameId = $('#bookName').val();
            var contentId = $('#contentId').val();
            var emailAddress = $('#emailAddress').val();
            // $('#anniu').attr("disabled",true);
            // $('#result').html("已请求，成功与否都会以邮件通知");
            $.ajax({
                type: "post",
                url: "${pageContext.request.contextPath}/xsdown/getThisBook",
                data: {
                    webUrl: webUrl,
                    listXpath: listXpath,
                    bookNameId: bookNameId,
                    contentId: contentId,
                    emailAddress: emailAddress
                },
                success: function (data) {
                    if (data.code == "0000") {
                        $('#result').html('');
                        $('#result').html("data:" + data.data + ";msg:" + data.msg);
                    } else {
                        alert(data.msg);
                        $('#result').html("msg:" + data.msg);
                    }
                },
                error: function (e) {

                }
            })

        }

        var tan;
        function xsUserAdd() {
            var tan = $.ligerDialog.open({
                url: '${pageContext.request.contextPath}/xsdown/xsUserAddPage',
                height: 200,
                width: null
                // buttons: [{
                //     text: '确定', onclick: function (item, dialog) {
                //         alert(item.text);
                //     }
                // }, {
                //     text: '取消', onclick: function (item, dialog) {
                //         dialog.close();
                //     }
                // }]
            });

        }

        function xsUserLogin(){
            $.ligerDialog.open({
                url: '${pageContext.request.contextPath}/xsdown/xsUserLoginPage',
                height: 200,
                width: null
            });

        }
    </script>
</head>
<body>
<input type="button" value="注册" onclick="xsUserAdd()">
<input type="button" value="登陆" onclick="xsUserLogin()">
<br><br>

<span style="font-size: 30px;">登陆状态</span><span style="font-size: 30px;color: green" id="loginstatus"></span>

<br><br>

<%--<h2 style="color: red">只输入网址和邮箱即可，其他勿动</h2>--%>
在此地址里找到书的列表页，可爬取此书全章节内容。<span>例: http://www.twxs8.com/5_5518/</span> <br>
输入网页: <input id="webUrl" style="width: 200px;" value=""> <br>
邮箱地址:<input id="emailAddress" value=""><br>

<input type="button" id="anniu" style="height: 30px;color: red;" value="开始下载并发送" onclick="zhuanJson()"> <br>
<span id="result" style="color: red;font-size: 25px;"></span>
<br>
操作步骤:<br>
第一步:点开http://www.twxs8.com 搜索要看的书<br>
第二步:找到自己要看的书，点进去列表页面<br><img src="${pageContext.request.contextPath}/ligerui/myWork/jsoup/step2.jpg"><br>
第三步:复制此链接到下载框，即可下载












<div style="display: none">
下面内容无须理会 <br>
此网站列表xpath路径(<span style="color: red">图1</span>):<input id="listXpath" value="/html/body/div[4]/div[2]/dl"></input> <br>
书名id(列表页<span style="color: red">图2</span>):<input id="bookName" value="info"><br>
详情文章id(文章详情页<span style="color: red">图3</span>):<input id="contentId" value = "content"><br>
</div>



</body>
</html>