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
        function reverseRangeByScore(){
            var key = $('#hdkey').val();
            $.ajax({
                type : "post",
                url : "${pageContext.request.contextPath}/reverseRangeWithScores",
                data : {
                    key:key,
                    startIndex:$('#revRangestart').val(),
                    lastIndex:$('#revRangelast').val()
                },
                dataType : "json",
                success : function(data) {
                    if(data.code=='0000'){
                        var dataStr = "";
                        $.each(data.data, function (n, value) {
                            dataStr += "<tr>\n" +
                                "            <td>唯一id:"+value.value+"</td>\n" +
                                "            <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>\n" +
                                "            <td>当前得分 :"+value.score+"<br></td>\n" +
                                "        </tr>";
                        });
                        $('#pmData').html('');
                        $('#pmData').html(dataStr);
                    }else{
                        alert(data.msg);
                    }
                },
                error : function() {
                    alert("网络错误！");
                }
            });
        }


        //分数由小到大排
        function rangeByScore(){
            var key = $('#hdkey').val();
            $.ajax({
                type : "post",
                url : "${pageContext.request.contextPath}/rangeWithScores",
                data : {
                    key:key,
                    startIndex:$('#rangestart').val(),
                    lastIndex:$('#rangelast').val()
                },
                dataType : "json",
                success : function(data) {
                    if(data.code=='0000'){
                        var dataStr = "";
                        $.each(data.data, function (n, value) {
                            dataStr += "<tr>\n" +
                                "            <td>唯一id:"+value.value+"</td>\n" +
                                "            <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>\n" +
                                "            <td>当前得分 :"+value.score+"<br></td>\n" +
                                "        </tr>";
                        });
                        $('#pmData').html('');
                        $('#pmData').html(dataStr);
                    }else{
                        alert(data.msg);
                    }
                },
                error : function() {
                    alert("网络错误！");
                }
            });
        }


        //新增用户
        function addUser(){
            var key = $('#hdkey').val();
            $.ajax({
                type : "post",
                url : "${pageContext.request.contextPath}/zSetadd",
                data : {
                    key:key,
                    member:$('#newMemberId').val(),
                    score:$('#newMemScore').val()
                },
                dataType : "json",
                success : function(data) {
                    if(data.code=='0000'){
                        reverseRangeByScore();
                    }else{
                        alert(data.msg);
                    }
                },
                error : function() {
                    alert("网络错误！");
                }
            });
        }


        //新增用户
        function addScoreByUser(){
            var key = $('#hdkey').val();
            $.ajax({
                type : "post",
                url : "${pageContext.request.contextPath}/incrementScore",
                data : {
                    key:key,
                    member:$('#addMemberId').val(),
                    score:$('#addMemScore').val()
                },
                dataType : "json",
                success : function(data) {
                    if(data.code=='0000'){
                        reverseRangeByScore();
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
    活动key:<input id="hdkey" value="paiming" ><br>


    操作:<br>
    <div style="border-style:solid; border-width:1px; border-color:red;width: 400px;">
        位置下标是从0开始。默认前10名，0 -1 代表所有位置<br>
        起始位置:<input id="revRangestart" value="0"><br>
        终止位置:<input id="revRangelast" value="9"><br>
        <button onclick="reverseRangeByScore()">按分数由大到小排序</button>
    </div>

    <div style="border-style:solid; border-width:1px; border-color:red;width: 400px;">
        位置下标是从0开始。默认前10名，0 -1 代表所有位置<br>
        起始位置:<input id="rangestart" value="0"><br>
        终止位置:<input id="rangelast" value="9"><br>
        <button onclick="rangeByScore()">按分数由小到大排序</button>
    </div>

    <div style="border-style:solid; border-width:1px; border-color:red;width: 400px;">
        PS:一般不用这方法，当用户存在时候就会返回false<br>
        用下面的两个方法，有则根据原有分数加减，无则创建
        用户唯一id:<input id="newMemberId"><br>
        用户分数:<input id="newMemScore"><br>
        <button onclick="addUser()">增加新用户</button>
    </div>

    <div style="border-style:solid; border-width:1px; border-color:red;width: 400px;">
        用户唯一id:<input id="addMemberId"><br>
        用户分数:<input id="addMemScore"><br>
        <button onclick="addScoreByUser()">为(新)用户<span style="color: red">加减</span>分</button>
    </div>


    <br>
    score是double，测试最大16位，超过会变科学计数法。
    排名顺序: <br>
    <table id="pmData">
        <c:forEach var="item" items="${paiming}">
        <tr>
            <td>唯一id:${item.value}</td>
            <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
            <td>当前得分 :${item.score}<br></td>
        </tr>
        </c:forEach>
    </table>

</body>
</html>