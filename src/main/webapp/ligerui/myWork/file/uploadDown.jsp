<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>zSet操作排序</title>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.10.2.min.js"></script>
    <script type="text/javascript">

        function ajaxtSubmit(){
            var files = document.getElementById("uploadFile").files;
            //文件数量
            //alert(files.length);
            var formData = new FormData();
            for(var i=0;i<files.length;i++){
                formData.append("file",files[i]);
            }
            formData.append("username","zxm");
            formData.append("password","123456");
            $.ajax({
                type:"post",
                url:"${pageContext.request.contextPath}/file/uploadFile",
                data:formData,
                xhr:xhrOnProgress(function(e){
                    var percent=e.loaded / e.total;//计算百分比
                    $('#xxx').val(percent);
                }),
                processData : false,// 不处理数据
                contentType : false,// 不设置内容类型
                /*beforeSend: function(request) {
                    request.setRequestHeader("filePath", file_path);
                }, */
                success:function(data){

                },
                error:function(e){

                }
            })

        }

        var xhrOnProgress=function(fun) {
            xhrOnProgress.onprogress = fun; //绑定监听

            //使用闭包实现监听绑
            return function() {
                //通过$.ajaxSettings.xhr();获得XMLHttpRequest对象
                var xhr = $.ajaxSettings.xhr();

                //判断监听函数是否为函数
                if (typeof xhrOnProgress.onprogress !== 'function')
                    return xhr;
                //如果有监听函数并且xhr对象支持绑定时就把监听函数绑定上去
                if (xhrOnProgress.onprogress && xhr.upload) {
                    xhr.upload.onprogress = xhrOnProgress.onprogress;
                }
                return xhr;
            }
        }

        //下载文件
        function download(){
            var url = "${pageContext.request.contextPath}/file/downLoad";

            //本项目没用，可用此定义文件名
            var fileName = "testAjaxDownload.txt";

            var form = $("<form></form>").attr("action", url).attr("method", "post");

            form.append($("<input></input>").attr("type", "hidden").attr("name", "fileName").attr("value", fileName));

            //多个参数的话复制上面这一行，不能在上面继续加attr，不起作用.
            form.append($("<input></input>").attr("type", "hidden").attr("name", "id").attr("value", "567"));

            form.appendTo('body').submit().remove();
        }


    </script>
</head>
<body>
    <div id="uploadDiv">
        <input type="file" name="file" multiple id="uploadFile"/>
    </div>


    <button onclick="ajaxtSubmit()">ajax提交</button>
    <div style="border:black solid 1px; width: 800px;height: 10px;">
        <div id="result" style="height: 8px;background: green;position: relative; top: 1px;left: 1px;"></div>
    </div>
    <input id="xxx"/>


    <br><br><br><br><br><br><br>


    <button onclick="download()">下载文件</button>
</body>
</html>