<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>订单详情界面</title>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.10.2.min.js"></script>
<script type="text/javascript">
	var leaveTime = ${leaveTime};
	console.log(leaveTime);
	$(document).ready(function(){
		//var totalTime = parseFloat($('#minute').text()), totalSecond = totalTime * 60, minute = $('#minute'), second = $('#second');
		var  totalSecond = leaveTime, minute = $('#minute'), second = $('#second');
		function setTimeover() {
			window.setInterval(function() {
						if (totalSecond > 0) {
							totalSecond--;
						} else {
							minute.text(0);
							second.text(0);
							return false;
						}
						var minuteTime = Math.floor(totalSecond / 60), secondTime = totalSecond % 60;
						minute.text(minuteTime);
						second.text(secondTime);
					}, 1000);
		}
		setTimeover();

	});
</script>
</head>
<body>
	下单成功，请在10分钟内支付。
	<a href="#" class="dropdown-toggle" >
      剩余时间: <i id="minute"></i>分<i id="second"></i>秒
     <br>
     
     影名：${movieInfo.name } <br>
     订单号:${orderInfo.orderId } <br>
     
</a>
<br>
	
</body>
</html>