<%@ page language="java" import="java.util.*"  contentType="text/html;charset=UTF-8"%> 
<%@ page import="com.chengxusheji.po.PinChe" %>
<%@ page import="com.chengxusheji.po.CarInfo" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    //获取所有的carObj信息
    List<CarInfo> carInfoList = (List<CarInfo>)request.getAttribute("carInfoList");
    PinChe pinChe = (PinChe)request.getAttribute("pinChe");

%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1 , user-scalable=no">
  <TITLE>查看拼车详情</TITLE>
  <link href="<%=basePath %>plugins/bootstrap.css" rel="stylesheet">
  <link href="<%=basePath %>plugins/bootstrap-dashen.css" rel="stylesheet">
  <link href="<%=basePath %>plugins/font-awesome.css" rel="stylesheet">
  <link href="<%=basePath %>plugins/animate.css" rel="stylesheet"> 
</head>
<body style="margin-top:70px;"> 
<jsp:include page="../header.jsp"></jsp:include>
<div class="container">
	<ul class="breadcrumb">
  		<li><a href="<%=basePath %>index.jsp">首页</a></li>
  		<li><a href="<%=basePath %>PinChe/frontlist">拼车信息</a></li>
  		<li class="active">详情查看</li>
	</ul>
	<div class="row bottom15"> 
		<div class="col-md-2 col-xs-4 text-right bold">记录id:</div>
		<div class="col-md-10 col-xs-6"><%=pinChe.getPincheId()%></div>
	</div>
	<div class="row bottom15"> 
		<div class="col-md-2 col-xs-4 text-right bold">车辆信息:</div>
		<div class="col-md-10 col-xs-6"><%=pinChe.getCarObj().getCarNo() %></div>
	</div>
	<div class="row bottom15"> 
		<div class="col-md-2 col-xs-4 text-right bold">拼车图片:</div>
		<div class="col-md-10 col-xs-6"><img class="img-responsive" src="<%=basePath %><%=pinChe.getPinChePhoto() %>"  border="0px"/></div>
	</div>
	<div class="row bottom15"> 
		<div class="col-md-2 col-xs-4 text-right bold">出发时间:</div>
		<div class="col-md-10 col-xs-6"><%=pinChe.getStartTime()%></div>
	</div>
	<div class="row bottom15"> 
		<div class="col-md-2 col-xs-4 text-right bold">车出发位置:</div>
		<div class="col-md-10 col-xs-6"><%=pinChe.getStartPlace()%></div>
	</div>
	<div class="row bottom15"> 
		<div class="col-md-2 col-xs-4 text-right bold">目的地:</div>
		<div class="col-md-10 col-xs-6"><%=pinChe.getEndPlace()%></div>
	</div>
	<div class="row bottom15"> 
		<div class="col-md-2 col-xs-4 text-right bold">价格:</div>
		<div class="col-md-10 col-xs-6"><%=pinChe.getPrice()%></div>
	</div>
	<div class="row bottom15"> 
		<div class="col-md-2 col-xs-4 text-right bold">可拼人数:</div>
		<div class="col-md-10 col-xs-6"><%=pinChe.getTotalNum()%></div>
	</div>
	<div class="row bottom15"> 
		<div class="col-md-2 col-xs-4 text-right bold">已拼人数:</div>
		<div class="col-md-10 col-xs-6"><%=pinChe.getHaveNum()%></div>
	</div>
	<div class="row bottom15"> 
		<div class="col-md-2 col-xs-4 text-right bold">输入拼车人数:</div>
		<div class="col-md-10 col-xs-6">
			<input type="text" class="form-control" id="personNum"/>
		</div>
	</div>
	<div class="row bottom15">
		<div class="col-md-2 col-xs-4"></div>
		<div class="col-md-6 col-xs-6">
			<button onclick="pinChe();" class="btn btn-primary">我要拼车</button>
			<button onclick="history.back();" class="btn btn-info">返回</button>
		</div>
	</div>
</div> 
<jsp:include page="../footer.jsp"></jsp:include>
<script src="<%=basePath %>plugins/jquery.min.js"></script>
<script src="<%=basePath %>plugins/bootstrap.js"></script>
<script src="<%=basePath %>plugins/wow.min.js"></script>
<script>
var basePath = "<%=basePath%>";
$(function(){
        /*小屏幕导航点击关闭菜单*/
        $('.navbar-collapse a').click(function(){
            $('.navbar-collapse').collapse('hide');
        });
        new WOW().init();
 })
 
 
function pinChe() {
	var personNum = $("#personNum").val();
	if(personNum == "") {
		alert("请输入拼车人数!");
		return;
	}

	var re = /^(?:0|[1-9][0-9]?|10000000)$/; 
    if(!re.test(personNum)){
        alert("请输入正确的数值,只允许输入整数!");
        document.getElementById(input).value = "";
        return false;
    }

    if(personNum < 1 || personNum > 3) {
    	alert("每个用户最多输入3个人！");
    	return;
    }


  //初始化车辆信息下拉框值 
	$.ajax({
		url: basePath + "PinCheOrder/userAdd",
		type: "post",
		dataType: "json",
		data: {
			"pinCheOrder.pinCheObj.pincheId": <%=pinChe.getPincheId() %>,
			"pinCheOrder.personNum": personNum
		},
		success: function(obj,response,status) { 
			if(obj.success) {
				alert(obj.message);
				location.href = basePath + "PinCheOrder/userFrontlist";
			} else {
				alert(obj.message);
			}
			 
    	}
	});
 
    
	
}



 </script> 
</body>
</html>

