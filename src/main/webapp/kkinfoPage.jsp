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
        function bu(seaDdh,seaFpqqlsh) {
            //先清空所有的数据
            $("tr").remove(".searchData");

            console.log("传进来的:ddh"+seaDdh+"ssssss:"+seaFpqqlsh)
            var ddh = '';
            if(seaDdh==''){
                ddh = $('#ddh').val();
            }else{
                ddh = seaDdh;
            }
            var fpqqlsh ='';
            if(seaFpqqlsh==''){
                fpqqlsh = $('#fpqqlsh').val();
            }else{
                fpqqlsh = seaFpqqlsh;
            }


            $.ajax({
                url: '${pageContext.request.contextPath}/order/getKkinfo',
                dataType: 'json',
                type: 'POST',
                data: {
                    //page:e
                    ddh:ddh,
                    fpqqlsh:fpqqlsh
                },
                success: function (data) {
                    // $('#result').html('');
                    if(data.code=='0000'){

                        $('#result').html("");
                        $('#invoiceType').html("税控所属:"+data.data.invoiceType);

                        setTitleDate(data.data.opi.title,"opititle");
                        setTitleDate(data.data.opi.data,"opidata",true);

                        setTitleDate(data.data.oii.title,"oiititle");
                        setTitleDate(data.data.oii.data,"oiidata",true);

                        setTitleDate(data.data.kjlog.title,"kj_logtitle");
                        setTitleDate(data.data.kjlog.data,"kj_logdata",true);

                        setTitleDate(data.data.kjxx.title,"kj_xxtitle");
                        setTitleDate(data.data.kjxx.data,"kj_xxdata",true);

                    }
                    //opi查到多条
                    else if(data.code=='1'){
                        $('#result').html("");
                        var fpqqlshNum = setTitleDate(data.data.opiData.title,"opititle");
                        console.log("号是:"+fpqqlshNum)
                        // $.each(data.data.opiData.data, function (n, value) {
                        setDateOpiList(data.data.opiData.data,"opidata",fpqqlshNum);
                        // });
                        //清空opi下面的那些数据
                        $("#oiititle").html('');
                        $("#oiidata").html('');
                        $("#kj_logtitle").html('');
                        $("#kj_logdata").html('');
                        $("#kj_xxtitle").html('');
                        $("#kj_xxdata").html('');
                    }

                    else{
                        $('#result').html(data.msg);
                    }


                },
                error: function (data) {
                    alert(data.msgCode);
                }
            });


        }
        
        function setTitleDate(data,id) {
            // console.log(id);
            $("#"+id).html('');
            var resultStr = '';
            var fpqqlshNum = 0;
            $.each(data, function (n, value) {
                if('fpqqlsh'==value){
                    fpqqlshNum = n;
                }
                value = "<th><span style='color:black'>"+value+"</span></th> "
                resultStr = resultStr+value+"";
            });
            console.log("saaaaaa"+fpqqlshNum);
            $("#"+id).html(resultStr);
            return fpqqlshNum;
        }





        //单独的方法，opi会查询到多个
        function setDateOpiList(data,id,fpqqlshNum) {
            // console.log(id);
            $("#"+id).html('');
            // $('#searchData').remove();
            $("tr").remove(".searchData");
            console.log("1")
            $.each(data, function (n, value) {
                var resultStr = '';
                // value = "<td><span style='color:black'>"+value+"</span></td> "
                var fpqqlsh = '';
                console.log("2")
                $.each(value, function (item_n, item_value) {
                    console.log("3")
                    if(item_n==fpqqlshNum){
                        fpqqlsh = item_value;
                    }
                    item_value = "<td><span style='color:black'>"+item_value+"</span></td> "
                    resultStr = resultStr+item_value+"";

                });
                console.log("4")
                resultStr = "<tr class='searchData' style='cursor: pointer;' title='选这个' onclick='bu(\"\",\""+fpqqlsh+"\")'>"+resultStr+"</tr>";
                $("#"+id).after(resultStr);

            });
        }
    </script>
</head>
<body>

    订单号: <input id="ddh" style="width: 300px" value="kanxhfmc"><br>
    fpqqlsh: <input id="fpqqlsh" style="width: 300px" value=""> <br>
    <input type="button" onclick="bu('','')" value="查询">
<br>
结果:
    <p id="result" style="color: red;font-size: 50px;"></p>
<br />
    <p id="invoiceType" style="color: red;font-size: 50px;"></p>
<br/>


order_process_info数据<br>
    <table border="1" style="border-collapse: collapse;border: 1px;border-color: red;">
        <tr id="opititle">
            <th>
                
            </th>
            <th>
                
            </th>
        </tr>

        <tr  id="opidata">
            <td>

            </td>
            <td>
                
            </td>
        </tr>

    </table>
<%--<p id="opiData"></p>--%>


order_invoice_info数据<br>
    <table border="1"  style="border-collapse: collapse;border: 1px;border-color: red;">
        <tr id="oiititle">
            <th>
                
            </th>
            <th>
                
            </th>
        </tr>
        <tr id="oiidata">
            <td>
                
            </td>
            <td>
                
            </td>
        </tr>

    </table>

税控表里：<br>
kj_log数据:<br>
    <table border="1"  style="border-collapse: collapse;border: 1px;border-color: red;">
        <tr id="kj_logtitle">
            <th>
                
            </th>
            <th>
                
            </th>
        </tr>
        <tr id="kj_logdata">
            <td>
                
            </td>
            <td>
                
            </td>
        </tr>

    </table>

<br>
kj_xx数据:<br>
    <table  border="1" style="border-collapse: collapse;border: 1px;border-color: red;">
        <tr id="kj_xxtitle">
            <th>
                
            </th>
            <th>
                
            </th>
        </tr>
        <tr id="kj_xxdata">
            <td>
                
            </td>
            <td>
                
            </td>
        </tr>

    </table>


</body>
</html>