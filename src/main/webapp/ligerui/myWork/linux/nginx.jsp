<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>nginx配置</title>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.10.2.min.js"></script>
    <script type="text/javascript">

        function ajaxtSubmit(){
            // $('#result').attr("style","height: 8px;background: green;position: relative; top: 1px;left: 1px;width: 20%");
            // return;
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
                    $('#hhh').html(percent*100+"%");
                    $('#result').attr("style","height: 8px;background: green;position: relative; top: 1px;left: 1px;width: "+percent*100+"%");
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

            var excelType = "0";

            var form = $("<form></form>").attr("action", url).attr("method", "post");

            form.append($("<input></input>").attr("type", "hidden").attr("name", "fileName").attr("value", fileName));

            form.append($("<input></input>").attr("type", "hidden").attr("name", "excelType").attr("value", excelType));

            //多个参数的话复制上面这一行，不能在上面继续加attr，不起作用.
            form.append($("<input></input>").attr("type", "hidden").attr("name", "id").attr("value", "567"));

            form.appendTo('body').submit().remove();
        }


    </script>
    <style type="text/css">
        #group{
            display: flex;
            justify-content:space-around;
        }

        #prettyprint{
            tab-size: 4;
            -webkit-font-smoothing: antialiased;
            word-break: break-word!important;
            font-variant-ligatures: no-common-ligatures;
            outline: 0;
            font-weight: normal;
            position: relative;
            overflow-y: hidden;
            overflow-x: auto;
            box-sizing: border-box;
            font-family: Source Code Pro,DejaVu Sans Mono,Ubuntu Mono,Anonymous Pro,Droid Sans Mono,Menlo,Monaco,Consolas,Inconsolata,Courier,monospace,PingFang SC,Microsoft YaHei,sans-serif;
            font-size: 14px;
            line-height: 22px;
            color: #000;
            margin: 0 0 24px;
            padding: 8px 16px 6px 56px;
            border: none;
            background-color: #282c34;
        }

        #has-numbering{
            tab-size: 4;
            -webkit-font-smoothing: antialiased;
            font-variant-ligatures: no-common-ligatures;
            font-weight: normal;
            outline: 0;
            box-sizing: border-box;
            font-family: Source Code Pro,DejaVu Sans Mono,Ubuntu Mono,Anonymous Pro,Droid Sans Mono,Menlo,Monaco,Consolas,Inconsolata,Courier,monospace,PingFang SC,Microsoft YaHei,sans-serif;
            font-size: 14px;
            line-height: 22px;
            padding: 0!important;
            border-radius: 4px;
            min-width: 94%;
            display: block;
            overflow-x: auto;
            white-space: pre;
            background-color: #282c34;
            color: #abb2bf;
            word-break: break-all;
            position: unset;
        }
    </style>
</head>
<body>

    <pre id="prettyprint">
        <code id="has-numbering" style="position: unset;">

            #http转发开始
            http {
                include       mime.types;
                default_type  application/octet-stream;

             #负载地址
             upstream slAccept {#税控接口
                      server 10.0.0.24:9077; #invoice-accept所在位置
                      server 10.0.0.24:9177;
             }

            #ljfldq
                server
                {
                    listen 80;
                    server_name www.ljfldq.com; #可以配置多个server监听不同域名
                    #error_page   404   /404.html;
                    rewrite ^(.*)$ https://$host$1 permanent;
                    access_log  /usr/local/nginx/logs/access.log;

                    location /accept {
                          proxy_pass  http://slAccept; #负载均衡
                    }
                }
            }
            #http转发结束


            #tcp转发，和http一个级别。只能一个端口指向一个地址，1对1
            stream {

                server {
                           listen 2022;
                           proxy_pass 172.25.11.107:13600;
                       }
            }


        </code>
    </pre>


</body>
</html>