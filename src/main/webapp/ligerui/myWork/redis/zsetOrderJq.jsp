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
    <link href="${pageContext.request.contextPath}/ligerui/lib/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
    <script src="${pageContext.request.contextPath}/ligerui/lib//jquery/jquery-1.9.0.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/ligerui/lib//ligerUI/js/core/base.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/ligerui/lib//ligerUI/js/plugins/ligerDateEditor.js" type="text/javascript"></script>
    <script type="text/javascript">
        $(function ()
        {
            $("#startDate").ligerDateEditor({ showTime: true, label: '开始时间', labelWidth: 100, labelAlign: 'left',initValue:'2022-03-07' });
            $("#endDate").ligerDateEditor();
            $("#startDate").val('2022-03-07 00:00:00');
            $("#endDate").val('2022-03-27 23:59:59');
            checkTimeConstant();
        });


        //分数由大到小排
        function reverseRangeByScore(){
            var key = $('#hdkey').val();
            $.ajax({
                type : "post",
                url : "${pageContext.request.contextPath}/zsetJq/reverseRangeWithScores",
                data : {
                    key:key,
                    startIndex:$('#revRangestart').val(),
                    lastIndex:$('#revRangelast').val()
                },
                dataType : "json",
                success : function(data) {
                    if(data.code=='0000'){
                        var dataStr = "";
                        var jingdu = data.data.itemJingdu;
                        $.each(data.data.shuju, function (n, value) {
                            var score = value.score.toString();
                            dataStr += "<tr>\n" +
                                "            <td>唯一id:"+value.value+"</td>\n" +
                                "            <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>\n" +
                                "            <td>当前得分 :"+score.substring(0,(score.length-jingdu))+"<br></td>\n" +
                                "        </tr>";
                        });
                        $('#'+$('#hdkey').val()).html('');
                        $('#'+$('#hdkey').val()).html(dataStr);
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
                url : "${pageContext.request.contextPath}/zsetJq/rangeWithScores",
                data : {
                    key:key,
                    startIndex:$('#rangestart').val(),
                    lastIndex:$('#rangelast').val()
                },
                dataType : "json",
                success : function(data) {
                    if(data.code=='0000'){
                        var dataStr = "";
                        var jingdu = data.data.itemJingdu;
                        $.each(data.data.shuju, function (n, value) {
                            var score = value.score.toString();
                            dataStr += "<tr>\n" +
                                "            <td>唯一id:"+value.value+"</td>\n" +
                                "            <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>\n" +
                                "            <td>当前得分 :"+score.substring(0,(score.length-jingdu))+"<br></td>\n" +
                                "        </tr>";
                        });
                        $('#'+$('#hdkey').val()).html('');
                        $('#'+$('#hdkey').val()).html(dataStr);
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
                url : "${pageContext.request.contextPath}/zsetJq/zSetadd",
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
                url : "${pageContext.request.contextPath}/zsetJq/incrementScore",
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

        //校验时间范围常数
        function checkTimeConstant(){
            var key = $('#hdkey').val();
            $.ajax({
                type : "post",
                url : "${pageContext.request.contextPath}/zsetJq/checkTimeConstant",
                data : {
                    key:key,
                    startDate:$('#startDate').val(),
                    endDate:$('#endDate').val()
                },
                dataType : "json",
                success : function(data) {
                    if(data.code=='0000'){
                        var str = "开始时间戳:"+data.data.kaishi+"<br>"+"结束时间戳:"+data.data.jieshu+"<br>常数:"+data.data.changshu;
                        $('#changshu').html(str);
                        $('#changshuValue').val(data.data.changshu);
                    }else{
                        alert(data.msg);
                    }
                },
                error : function() {
                    alert("网络错误！");
                }
            });
        }

        /**
         * 设置精确度
         */
        function jingque(type) {
            var key = 'paimingJq';
            $.ajax({
                type : "post",
                url : "${pageContext.request.contextPath}/zsetJq/jingque",
                data : {
                    key:key,
                    type:type,
                    changshuValue:$('#changshuValue').val()
                },
                dataType : "json",
                success : function(data) {
                    if(data.code=='0000'){
                        $('#nowType').html("当前排序精度:"+data.data.nowType);
                        $('#nowOrderContants').html(data.data.changshuValue);
                        $('#hdkey').val(data.data.key);
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
    <span style="color: red;">精确排行，分数里面储存的值是 显示业务+时间戳相减。例如达到30级的人按时间排名，越早的排名越靠前 </span><br>

    活动key:<input id="hdkey" value="paimingJq" style="width: 300px;"><br><br>

    活动时效范围:<br>
    <input type="text" id="startDate" />
    <input type="text" id="endDate" />
    <br>
    <button onclick="checkTimeConstant()">检验时间范围常数</button><br>
    <span id="changshu"></span><br>
    <input id="changshuValue" readonly/><br>
    设置完毕后要使用的常数量：<span id="nowOrderContants"></span>
    <br><br><br><br>

    <span id="nowType" style="font-size: 40px;"></span><br>
    <button onclick="jingque(1)">精确到秒</button>
    <button onclick="jingque(2)">精确到百毫秒</button>
    <button onclick="jingque(3)">精确到十毫秒</button>
    <button onclick="jingque(4)">精确到个毫秒</button>

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


    <br><br><br>
    score是double，测试最大16位，超过会变科学计数法。
    排名顺序: <br>

    秒：
    <table id="paimingJq_seconds" style="border: 1px solid red;">
    </table>

    <br>

    百毫秒
    <table id="paimingJq_hundreds_ms" style="border: 1px solid red;">
    </table>

    <br>

    十毫秒
    <table id="paimingJq_ten_ms" style="border: 1px solid red;">
    </table>

    <br>

    个毫秒
    <table id="paimingJq_ms" style="border: 1px solid red;">
    </table>


</body>
</html>