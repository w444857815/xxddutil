<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>批量执行命令</title>
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
    <div id="group">
        <div>
            <img src="${pageContext.request.contextPath}/ligerui/myWork/linux/mingling.png">
        </div>
        <div style="line-height: 20px;font-size: 17px;">
            <pre id="prettyprint">
                <code id="has-numbering" style="position: unset;">
                    pathA="/home/dxhy/application/order-management-consumer-18029-18109/a"
                    classPath="/home/dxhy/application/order-management-consumer-18029-18109/a/BOOT-INF/classes"
                    orderPath="/home/dxhy/application/order-management-consumer-18029-18109/"

                    echo --------创建a文件夹----------
                    ./del.sh
                    sleep 1s
                    cd ${pathA}

                    echo --解压文件--
                    ./jie.sh
                    sleep 8s
                    echo --解压完成--

                    cd ${classPath}
                    echo --进入${classPath}--

                    rm -rf com*
                    echo --删除com*--

                    cp /tmp/com.zip .
                    echo --拷贝com.zip到当前目录--
                    sleep 1s
                    unzip com.zip > /dev/null 2>&1
                    echo --解压com.zip

                    cd ${pathA}
                    echo --回到${pathA}--

                    ./yasuo.sh
                    echo -压缩生成新的order-api.jar-

                    cd ${orderPath}
                    echo -回到${orderPath}-

                    port=18109
                    tomcat_id=`netstat -tunlp|grep $port|awk -F " " '{print $7}'|awk -F "/" '{print $1}'`
                    echo --------进程id$tomcat_id-----------
                    if [ $tomcat_id > 0 ]; then
                    echo ------------kill tomcat-------------
                    kill -9 $tomcat_id
                    sleep 2
                    fi
                    sleep 3s
                    rm -rf order-api.jar
                    cp a/order-api.jar .
                    sleep 3s
                    nohup java -jar -Xms512M -Xmx2048M  order-api.jar > nohup.out 2>&1 &
                    tail -f nohup.out
                    </code>
            </pre>
        </div>
    </div>
</body>
</html>