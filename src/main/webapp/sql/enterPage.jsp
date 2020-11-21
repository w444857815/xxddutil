<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>输入</title>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.10.2.min.js"></script>
    <script type="text/javascript">
        function bu() {
            var sqlStr = $('#sqlStr').val();
            if(sqlStr==''||sqlStr==null){
                alert("输入语句");
                return;
            }

            $.ajax({
                url: '${pageContext.request.contextPath}/sqlExe/exeSql',
                dataType: 'json',
                type: 'POST',
                data: {
                    //page:e
                    sqlStr:sqlStr
                },
                success: function (data) {
                    $('#showtitle').html('')
                    $('#showData').html('');
                    $('#errmsg').html("");
                    if(data.code=='0000'){
                        //查
                        if(data.type=='select'){
                            var resultStr = '';
                            //显示标题
                            var titleList = [];
                            $.each(data.data.title, function (n, value) {
                                resultStr = resultStr+"<span  style='width: 200px;display: inline-block;'>"+value+"</span>|";
                                titleList.push(value);
                            });
                            $('#showtitle').html(resultStr);

                            console.log(titleList);

                            var dataStr = '';
                            //显示数据
                            $.each(data.data.data, function (n, value) {
                                $.each(titleList, function (n1, value1) {
                                    console.log(value1);
                                    dataStr = dataStr+"<span  style='width: 200px;display: inline-block;'>"+value[value1]+"</span>|";
                                });
                                dataStr = dataStr + "<br/>";
                                // dataStr = dataStr+"<span  style='width: 300px;display: inline-block;'>"+value[titleList[n]]+"</span>|";
                            });
                            console.log(dataStr);

                            $('#showData').html(dataStr);
                        }
                        //增
                        if(data.type=='insert'){
                            $('#showData').html(data.msg);
                        }
                        //删
                        if(data.type=='delete'){
                            $('#showData').html(data.msg);
                        }
                        //改
                        if(data.type=='update'){
                            $('#showData').html(data.msg);
                        }


                    }else{
                        $('#errmsg').html("执行错误:"+data.msg);
                    }

                },
                error: function (data) {
                    alert(data.msgCode);
                }
            });


        }
    </script>
</head>
<body>

<textarea rows="10" cols="100" id="sqlStr">

select * from t_sksb
</textarea>

    <input type="button" onclick="bu()" value="执行">
<br>
结果:<br>
<p id="showtitle" style="white-space:nowrap;">
<span style="width: 200px;"></span>
</p>
<br>
<p id="showData" style="white-space:nowrap;"></p>
<span></span>

<p id="errmsg"></p>

</body>
</html>