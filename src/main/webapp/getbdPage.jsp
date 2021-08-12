<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>跳转本地</title>
    <script
            src="${pageContext.request.contextPath}/views/js/jquery-1.7.1.min.js"></script>
    <script
            src="${pageContext.request.contextPath}/views/js/all.js"></script>
    
    <script type="text/javascript">
        function formate() {
            var sql = $('#sql').val();
            var params = $('#params').val();
			var isFh = $('#isFh').is(":checked");
            if(isFh){
            	isFh = "1";
            }else{
            	isFh = "0";
            }
            
            $.ajax({
                type: "post",
                url: "${pageContext.request.contextPath}/sqlformate",
                data: {
                    sql: sql,
                    params: params,
                    isFh:isFh
                },
                dataType: "json",
                success: function (data) {
                    if (data.code == '-1') {
                        alert(data.msg);
                    }
                    if (data.code == '0') {
                        $('#result').val(data.data);
                    }
                },
                error: function () {
                    alert("网络错误！");
                }
            });

        }


        function shengcheng() {
            var sessionid = $('#sessionid').val();
            var iplist = $('#iplist').val();
            console.log(document.cookie);

            $.ajax({
                type: "post",
                url: "${pageContext.request.contextPath}/getbendi",
                data: {
                    sessionid: sessionid,
                    iplist:iplist
                },
                dataType: "json",
                success: function (data) {
                    if (data.code == '-1') {
                        alert(data.msg);
                    }
                    if (data.code == '0') {

                        var obj='{"'+document.cookie.replace(/=/g,'":"').replace(/;\s+/g,'","')+'"}'
                        // var cookies=JSON.parse(obj) // 02 将json字符串转为 对象

                        // console.log(cookies);
                        var urlList = "";
                        $.each(data.data, function (n, value) {
                            urlList = urlList+" <span>"+value+"</span> <button onclick=\"window.open('"+value+"')\">跳转</button><br><br><br>";
                        });
                        $('#urlDz').html(urlList);
                        console.log(urlList);
                    }
                },
                error: function () {
                    alert("网络错误！");
                }
            });
        }
        
        function tiaozh(){

        }
        
    </script>

    <style type="">

    </style>
</head>

<body>
    输入sessionid:
    <div>
        <textarea style="width: 800px;height: 50px;" id="sessionid"></textarea>
    </div>
    ip集合:
    <div>
        <textarea style="width: 800px;height: 50px;" id="iplist">10.1.28.48,127.0.0.1</textarea>
    </div>
    <button onclick="shengcheng()">生成</button>

    <div id="urlDz">
        <span></span> <button onclick="tiaozh()">跳转</button>
    </div>



</body>


</html>
