<%@ page language="java" import="java.util.*"  contentType="text/html;charset=UTF-8"%> 
<%@ page import="com.chengxusheji.po.PinChe" %>
<%@ page import="com.chengxusheji.po.CarInfo" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    List<PinChe> pinCheList = (List<PinChe>)request.getAttribute("pinCheList");
    //获取所有的carObj信息
    List<CarInfo> carInfoList = (List<CarInfo>)request.getAttribute("carInfoList");
    int currentPage =  (Integer)request.getAttribute("currentPage"); //当前页
    int totalPage =   (Integer)request.getAttribute("totalPage");  //一共多少页
    int recordNumber =   (Integer)request.getAttribute("recordNumber");  //一共多少记录
    CarInfo carObj = (CarInfo)request.getAttribute("carObj");
    String startTime = (String)request.getAttribute("startTime"); //出发时间查询关键字
    String startPlace = (String)request.getAttribute("startPlace"); //车出发位置查询关键字
    String endPlace = (String)request.getAttribute("endPlace"); //目的地查询关键字
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1 , user-scalable=no">
<title>拼车查询</title>
<link href="<%=basePath %>plugins/bootstrap.css" rel="stylesheet">
<link href="<%=basePath %>plugins/bootstrap-dashen.css" rel="stylesheet">
<link href="<%=basePath %>plugins/font-awesome.css" rel="stylesheet">
<link href="<%=basePath %>plugins/animate.css" rel="stylesheet">
<link href="<%=basePath %>plugins/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">
</head>
<body style="margin-top:70px;">
<div class="container">
<jsp:include page="../header.jsp"></jsp:include>
<body style="margin-top:70px;"> 
<div class="container">
<jsp:include page="../header.jsp"></jsp:include>
	<div class="col-md-9 wow fadeInLeft">
		<ul class="breadcrumb">
  			<li><a href="<%=basePath %>index.jsp">首页</a></li>
  			<li><a href="<%=basePath %>PinChe/frontlist">拼车信息列表</a></li>
  			<li class="active">查询结果显示</li>
  			<a class="pull-right" href="<%=basePath %>PinChe/pinChe_frontAdd.jsp" style="display:none;">添加拼车</a>
		</ul>
		<div class="row">
			<%
				/*计算起始序号*/
				int startIndex = (currentPage -1) * 5;
				/*遍历记录*/
				for(int i=0;i<pinCheList.size();i++) {
            		int currentIndex = startIndex + i + 1; //当前记录的序号
            		PinChe pinChe = pinCheList.get(i); //获取到拼车对象
            		String clearLeft = "";
            		if(i%4 == 0) clearLeft = "style=\"clear:left;\"";
			%>
			<div class="col-md-3 bottom15" <%=clearLeft %>>
			  <a  href="<%=basePath  %>PinChe/<%=pinChe.getPincheId() %>/frontshow"><img class="img-responsive" src="<%=basePath%><%=pinChe.getPinChePhoto()%>" /></a>
			     <div class="showFields">
			     	<div class="field">
	            		车辆信息:<%=pinChe.getCarObj().getCarNo() %>
			     	</div>
			     	<div class="field">
	            		出发时间:<%=pinChe.getStartTime() %>
			     	</div>
			     	<div class="field">
	            		车出发位置:<%=pinChe.getStartPlace() %>
			     	</div>
			     	<div class="field">
	            		目的地:<%=pinChe.getEndPlace() %>
			     	</div>
			     	<div class="field">
	            		价格:<%=pinChe.getPrice() %>
			     	</div>
			     	<div class="field">
	            		可拼人数:<%=pinChe.getTotalNum() %>
			     	</div>
			     	<div class="field">
	            		已拼人数:<%=pinChe.getHaveNum() %>
			     	</div>
			        <a class="btn btn-primary top5" href="<%=basePath %>PinChe/<%=pinChe.getPincheId() %>/frontshow">详情</a>
			        <a class="btn btn-primary top5" onclick="pinCheEdit('<%=pinChe.getPincheId() %>');" style="display:none;">修改</a>
			        <a class="btn btn-primary top5" onclick="pinCheDelete('<%=pinChe.getPincheId() %>');" style="display:none;">删除</a>
			     </div>
			</div>
			<%  } %>

			<div class="row">
				<div class="col-md-12">
					<nav class="pull-left">
						<ul class="pagination">
							<li><a href="#" onclick="GoToPage(<%=currentPage-1 %>,<%=totalPage %>);" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>
							<%
								int startPage = currentPage - 5;
								int endPage = currentPage + 5;
								if(startPage < 1) startPage=1;
								if(endPage > totalPage) endPage = totalPage;
								for(int i=startPage;i<=endPage;i++) {
							%>
							<li class="<%= currentPage==i?"active":"" %>"><a href="#"  onclick="GoToPage(<%=i %>,<%=totalPage %>);"><%=i %></a></li>
							<%  } %> 
							<li><a href="#" onclick="GoToPage(<%=currentPage+1 %>,<%=totalPage %>);"><span aria-hidden="true">&raquo;</span></a></li>
						</ul>
					</nav>
					<div class="pull-right" style="line-height:75px;" >共有<%=recordNumber %>条记录，当前第 <%=currentPage %>/<%=totalPage %> 页</div>
				</div>
			</div>
		</div>
	</div>

	<div class="col-md-3 wow fadeInRight">
		<div class="page-header">
    		<h1>拼车查询</h1>
		</div>
		<form name="pinCheQueryForm" id="pinCheQueryForm" action="<%=basePath %>PinChe/frontlist" class="mar_t15">
            <div class="form-group">
            	<label for="carObj_carNo">车辆信息：</label>
                <select id="carObj_carNo" name="carObj.carNo" class="form-control">
                	<option value="">不限制</option>
	 				<%
	 				for(CarInfo carInfoTemp:carInfoList) {
	 					String selected = "";
 					if(carObj!=null && carObj.getCarNo()!=null && carObj.getCarNo().equals(carInfoTemp.getCarNo()))
 						selected = "selected";
	 				%>
 				 <option value="<%=carInfoTemp.getCarNo() %>" <%=selected %>><%=carInfoTemp.getCarNo() %></option>
	 				<%
	 				}
	 				%>
 			</select>
            </div>
			<div class="form-group">
				<label for="startTime">出发时间:</label>
				<input type="text" id="startTime" name="startTime" class="form-control"  placeholder="请选择出发时间" value="<%=startTime %>" onclick="SelectDate(this,'yyyy-MM-dd')" />
			</div>
			<div class="form-group">
				<label for="startPlace">车出发位置:</label>
				<input type="text" id="startPlace" name="startPlace" value="<%=startPlace %>" class="form-control" placeholder="请输入车出发位置">
			</div>
			<div class="form-group">
				<label for="endPlace">目的地:</label>
				<input type="text" id="endPlace" name="endPlace" value="<%=endPlace %>" class="form-control" placeholder="请输入目的地">
			</div>
            <input type=hidden name=currentPage value="<%=currentPage %>" />
            <button type="submit" class="btn btn-primary">查询</button>
        </form>
	</div>

		</div>
</div>
<div id="pinCheEditDialog" class="modal fade" tabindex="-1" role="dialog">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title"><i class="fa fa-edit"></i>&nbsp;拼车信息编辑</h4>
      </div>
      <div class="modal-body" style="height:450px; overflow: scroll;">
      	<form class="form-horizontal" name="pinCheEditForm" id="pinCheEditForm" enctype="multipart/form-data" method="post"  class="mar_t15">
		  <div class="form-group">
			 <label for="pinChe_pincheId_edit" class="col-md-3 text-right">记录id:</label>
			 <div class="col-md-9"> 
			 	<input type="text" id="pinChe_pincheId_edit" name="pinChe.pincheId" class="form-control" placeholder="请输入记录id" readOnly>
			 </div>
		  </div> 
		  <div class="form-group">
		  	 <label for="pinChe_carObj_carNo_edit" class="col-md-3 text-right">车辆信息:</label>
		  	 <div class="col-md-9">
			    <select id="pinChe_carObj_carNo_edit" name="pinChe.carObj.carNo" class="form-control">
			    </select>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="pinChe_pinChePhoto_edit" class="col-md-3 text-right">拼车图片:</label>
		  	 <div class="col-md-9">
			    <img  class="img-responsive" id="pinChe_pinChePhotoImg" border="0px"/><br/>
			    <input type="hidden" id="pinChe_pinChePhoto" name="pinChe.pinChePhoto"/>
			    <input id="pinChePhotoFile" name="pinChePhotoFile" type="file" size="50" />
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="pinChe_startTime_edit" class="col-md-3 text-right">出发时间:</label>
		  	 <div class="col-md-9">
                <div class="input-group date pinChe_startTime_edit col-md-12" data-link-field="pinChe_startTime_edit">
                    <input class="form-control" id="pinChe_startTime_edit" name="pinChe.startTime" size="16" type="text" value="" placeholder="请选择出发时间" readonly>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                </div>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="pinChe_startPlace_edit" class="col-md-3 text-right">车出发位置:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="pinChe_startPlace_edit" name="pinChe.startPlace" class="form-control" placeholder="请输入车出发位置">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="pinChe_endPlace_edit" class="col-md-3 text-right">目的地:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="pinChe_endPlace_edit" name="pinChe.endPlace" class="form-control" placeholder="请输入目的地">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="pinChe_price_edit" class="col-md-3 text-right">价格:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="pinChe_price_edit" name="pinChe.price" class="form-control" placeholder="请输入价格">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="pinChe_totalNum_edit" class="col-md-3 text-right">可拼人数:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="pinChe_totalNum_edit" name="pinChe.totalNum" class="form-control" placeholder="请输入可拼人数">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="pinChe_haveNum_edit" class="col-md-3 text-right">已拼人数:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="pinChe_haveNum_edit" name="pinChe.haveNum" class="form-control" placeholder="请输入已拼人数">
			 </div>
		  </div>
		</form> 
	    <style>#pinCheEditForm .form-group {margin-bottom:5px;}  </style>
      </div>
      <div class="modal-footer"> 
      	<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      	<button type="button" class="btn btn-primary" onclick="ajaxPinCheModify();">提交</button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<jsp:include page="../footer.jsp"></jsp:include> 
<script src="<%=basePath %>plugins/jquery.min.js"></script>
<script src="<%=basePath %>plugins/bootstrap.js"></script>
<script src="<%=basePath %>plugins/wow.min.js"></script>
<script src="<%=basePath %>plugins/bootstrap-datetimepicker.min.js"></script>
<script src="<%=basePath %>plugins/locales/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="<%=basePath %>js/jsdate.js"></script>
<script>
var basePath = "<%=basePath%>";
/*跳转到查询结果的某页*/
function GoToPage(currentPage,totalPage) {
    if(currentPage==0) return;
    if(currentPage>totalPage) return;
    document.pinCheQueryForm.currentPage.value = currentPage;
    document.pinCheQueryForm.submit();
}

/*可以直接跳转到某页*/
function changepage(totalPage)
{
    var pageValue=document.pinCheQueryForm.pageValue.value;
    if(pageValue>totalPage) {
        alert('你输入的页码超出了总页数!');
        return ;
    }
    document.pinCheQueryForm.currentPage.value = pageValue;
    documentpinCheQueryForm.submit();
}

/*弹出修改拼车界面并初始化数据*/
function pinCheEdit(pincheId) {
	$.ajax({
		url :  basePath + "PinChe/" + pincheId + "/update",
		type : "get",
		dataType: "json",
		success : function (pinChe, response, status) {
			if (pinChe) {
				$("#pinChe_pincheId_edit").val(pinChe.pincheId);
				$.ajax({
					url: basePath + "CarInfo/listAll",
					type: "get",
					success: function(carInfos,response,status) { 
						$("#pinChe_carObj_carNo_edit").empty();
						var html="";
		        		$(carInfos).each(function(i,carInfo){
		        			html += "<option value='" + carInfo.carNo + "'>" + carInfo.carNo + "</option>";
		        		});
		        		$("#pinChe_carObj_carNo_edit").html(html);
		        		$("#pinChe_carObj_carNo_edit").val(pinChe.carObjPri);
					}
				});
				$("#pinChe_pinChePhoto").val(pinChe.pinChePhoto);
				$("#pinChe_pinChePhotoImg").attr("src", basePath +　pinChe.pinChePhoto);
				$("#pinChe_startTime_edit").val(pinChe.startTime);
				$("#pinChe_startPlace_edit").val(pinChe.startPlace);
				$("#pinChe_endPlace_edit").val(pinChe.endPlace);
				$("#pinChe_price_edit").val(pinChe.price);
				$("#pinChe_totalNum_edit").val(pinChe.totalNum);
				$("#pinChe_haveNum_edit").val(pinChe.haveNum);
				$('#pinCheEditDialog').modal('show');
			} else {
				alert("获取信息失败！");
			}
		}
	});
}

/*删除拼车信息*/
function pinCheDelete(pincheId) {
	if(confirm("确认删除这个记录")) {
		$.ajax({
			type : "POST",
			url : basePath + "PinChe/deletes",
			data : {
				pincheIds : pincheId,
			},
			success : function (obj) {
				if (obj.success) {
					alert("删除成功");
					$("#pinCheQueryForm").submit();
					//location.href= basePath + "PinChe/frontlist";
				}
				else 
					alert(data.message);
			},
		});
	}
}

/*ajax方式提交拼车信息表单给服务器端修改*/
function ajaxPinCheModify() {
	$.ajax({
		url :  basePath + "PinChe/" + $("#pinChe_pincheId_edit").val() + "/update",
		type : "post",
		dataType: "json",
		data: new FormData($("#pinCheEditForm")[0]),
		success : function (obj, response, status) {
            if(obj.success){
                alert("信息修改成功！");
                $("#pinCheQueryForm").submit();
            }else{
                alert(obj.message);
            } 
		},
		processData: false,
		contentType: false,
	});
}

$(function(){
	/*小屏幕导航点击关闭菜单*/
    $('.navbar-collapse a').click(function(){
        $('.navbar-collapse').collapse('hide');
    });
    new WOW().init();

    /*出发时间组件*/
    $('.pinChe_startTime_edit').datetimepicker({
    	language:  'zh-CN',  //语言
    	format: 'yyyy-mm-dd hh:ii:ss',
    	weekStart: 1,
    	todayBtn:  1,
    	autoclose: 1,
    	minuteStep: 1,
    	todayHighlight: 1,
    	startView: 2,
    	forceParse: 0
    });
})
</script>
</body>
</html>

