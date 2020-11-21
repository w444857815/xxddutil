<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>请选择要看的--精确</title>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.10.2.min.js"></script>
<script type="text/javascript">
	function goumai(mvid){
		$.ajax({
			type : "post",
			url : "${pageContext.request.contextPath}/order/createOrder",
			data : {
				mvId:mvid
			},
			dataType : "json",
			success : function(data) {
				if(data.code=='-1'){
					alert(data.msg);
				}
				if(data.code=='0'){
					window.location.href="${pageContext.request.contextPath}/order/orderDetailPage?orderId="+data.data;
				}
			},
			error : function() {
				alert("网络错误！");
			}
		});
	}
	//点赞
	function dianzan(mvid){
		$.ajax({
			type : "post",
			url : "${pageContext.request.contextPath}/order/dianZan",
			data : {
				mvId:mvid
			},
			dataType : "json",
			success : function(data) {
				if(data.code=='-1'){
					alert(data.msg);
				}
				if(data.code=='0'){
					<%--window.location.href="${pageContext.request.contextPath}/order/orderDetailPage?orderId="+data.data;--%>
					$('#zan_'+mvid).html(data.data.zanOrder);
					$('#toHid_'+mvid).html("");
					var showStr = "";
					$.each(data.data.orderData2, function (n, value) {
						var name = "";
						if(value.value==1){
							name = "唐人街探案3";
						}
						if(value.value==2){
							name = "西游降魔";
						}
						if(value.value==3){
							name = "泰囧";
						}
						showStr+='<span style="width: 200px;float:left;">名称:'+name+',</span><span>当前赞数:'+value.score+'</span><br>';
						<%--showStr+='<span style="width: 200px;float:left;">id：${pro.id } </span>'--%>
					});
					$('#order_Html').html(showStr);
				}
			},
			error : function() {
				alert("网络错误！");
			}
		});

	}


	//精确点赞
	function jqdianzan(mvid){
		$.ajax({
			type : "post",
			url : "${pageContext.request.contextPath}/order/jqDianZan",
			data : {
				mvId:mvid
			},
			dataType : "json",
			success : function(data) {
				if(data.code=='-1'){
					alert(data.msg);
				}
				if(data.code=='0'){
					<%--window.location.href="${pageContext.request.contextPath}/order/orderDetailPage?orderId="+data.data;--%>
					$('#jqzan_'+mvid).html(data.data.zanOrder);
					$('#jqtoHid_'+mvid).html("");
					var showStr = "";
					$.each(data.data.orderData2, function (n, value) {
						var name = "";
						if(value.name==1){
							name = "唐人街探案3";
						}
						if(value.name==2){
							name = "西游降魔";
						}
						if(value.name==3){
							name = "泰囧";
						}
						showStr+='<span style="width: 200px;float:left;">名称:'+name+',</span><span>当前赞数:'+value.score+'</span><br>';
						<%--showStr+='<span style="width: 200px;float:left;">id：${pro.id } </span>'--%>
					});
					$('#order_Html').html(showStr);
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
	<c:forEach var="pro" items="${movieList}">
		<c:if test="${movieList != null }">
			<span style="width: 200px;float:left;">id：${pro.id } </span>&nbsp;&nbsp;&nbsp;
			<span style="width: 200px;float:left;">片名：${pro.name } </span>&nbsp;&nbsp;&nbsp;
			<span style="width: 100px;float:left;">票价：${pro.price }</span> &nbsp;&nbsp;&nbsp;
			<span style="width: 100px;float:left;">余票：${pro.leaveVotes }</span> &nbsp;&nbsp;&nbsp;
			<span style="width: 100px;float:left;"><a onclick="goumai(${pro.id })">购买</a> </span>


			<span style="width: 100px;float:left;"><a onclick="jqdianzan(${pro.id })">精确点赞</a> </span>
			<span style="width: 400px;float:left;"><a>赞数:
					<%--${dicmap[pro.id]}--%>

				<c:forEach items="${dicmap }" var="dicmap">
					<c:if test="${dicmap.key eq pro.id }"><span id="jqtoHid_${pro.id }" >${dicmap.value }</span></c:if>
				</c:forEach>

				<span id="jqzan_${pro.id }"></span></a> </span>

			<br>
		</c:if>
	</c:forEach>
    当前排行情况：<br>
<div id="order_Html">

	<c:forEach var="pro" items="${dicmap}">
		<c:if test="${dicmap != null }">
			<span style="width: 200px;float:left;">名称:${pro.key },</span><span>当前赞数:${pro.value }</span><br>
		</c:if>
	</c:forEach>

</div>


	<div>
		<br>
		<br>
		<br>
		<br>
		<a href="${pageContext.request.contextPath}/user/indexPage">到普通点赞排行</a>

	</div>
</body>
</html>