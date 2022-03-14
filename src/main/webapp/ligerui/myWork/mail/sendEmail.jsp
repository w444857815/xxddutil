<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>zSet操作排序</title>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.10.2.min.js"></script>
    <script type="text/javascript">

        //分数由大到小排
        function fasong(){
            var subjects = $('#subjects').val();
            var contents = $('#contents').val();
            var to = $('#to').val();
            var senderName = $('#senderName').val();
            var cc = $('#cc').val();
            //是否带logo，true带，false不带
            var islogo = $('#inlogo').is(":checked");
            if(''==subjects){
                alert("标题必填");
                return ;
            }
            if(''==contents){
                alert("内容必填");
                return ;
            }
            if(''==to){
                alert("收件人必填");
                return ;
            }
            $.ajax({
                type : "post",
                url : "${pageContext.request.contextPath}/emailSendForm",
                data : {
                    subjects:subjects,
                    contents:contents,
                    to:to,
                    cc:cc,
                    senderName:senderName,
                    showlogo:islogo
                },
                dataType : "json",
                success : function(data) {
                    if(data.code=='0000'){
                        alert(data.msg);
                    }else{
                        alert(data.msg);
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
    邮件标题:<input id="subjects"/> <br>
    发送人显示名称:<input id="senderName"/> <br>
    邮件内容:<textarea  id="contents"></textarea><br>
    收件人地址(,隔开):<input  id="to" value="444857815@qq.com"/><br>
    抄送人地址(,隔开):<input  id="cc"/><br>
    选中正文带logo <input id="inlogo" type="checkbox" checked="checked"/>
    附件:(以base64或者文件流上传，现base64)<br>
    图片附件和文本文件，代码里用file。<br>
    <button onclick="fasong()">发送</button>
</body>
</html>