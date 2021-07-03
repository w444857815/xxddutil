function cs(){
	$.ajax({
        type: "post",
        url: "http://localhost:8090/ljfl/cccsss",
        data: {
        },
        dataType: "json",
        success: function (data) {
        	
        },
        error: function () {
            alert("网络错误！");
        }
    });
}

function hah(){
	alert("123");
}