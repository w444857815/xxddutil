<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>sql替换？问号</title>
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


        function formateReplace() {
            var replaceStr = $('#replaceStr').val();
            
            
            var iskh = $('#iskh').is(":checked");
            if(iskh){
            	iskh = "1";
            }else{
            	iskh = "0";
            }

            $.ajax({
                type: "post",
                url: "${pageContext.request.contextPath}/replaceStrformate",
                data: {
                    replaceStr: replaceStr,
                    iskh:iskh
                },
                dataType: "json",
                success: function (data) {
                    if (data.code == '-1') {
                        alert(data.msg);
                    }
                    if (data.code == '0') {
                        $('#resultReplace').val(data.data);
                    }
                },
                error: function () {
                    alert("网络错误！");
                }
            });
        }
        
        
        
    </script>

    <style type="">

    </style>
</head>

<body>
<div style="display: inline-block;">
    输入sql:<br/>
    <textarea style="width: 800px;height: 200px;" id="sql"></textarea>
    <br/>
    输入参数:<br/>
    <textarea style="width: 800px;height: 50px;" id="params"></textarea>
    <br/>
    <br/>
    <button onclick="formate();">格式化</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" id="isFh" value="1"/>结果带;
    <br/>
    
    <br/>
    <textarea style="width: 800px;height: 200px;" id="result"></textarea>
</div>


<div style="display: inline-block;">
    输入内容:<br/>
    <textarea style="width: 800px;height: 277px;" id="replaceStr"></textarea>
    <br/>
    <br/>
    <button onclick="formateReplace();">格式化</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" id="iskh" value="1"/>结果带()
    <br/>
    <br/>
    <textarea style="width: 800px;height: 200px;" id="resultReplace"></textarea>
</div>

<button onclick="cs();">测试</button>

</body>


</html>
