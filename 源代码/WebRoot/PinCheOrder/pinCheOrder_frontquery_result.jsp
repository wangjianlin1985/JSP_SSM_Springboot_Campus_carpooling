<%@ page language="java" import="java.util.*"  contentType="text/html;charset=UTF-8"%> 
<%@ page import="com.chengxusheji.po.PinCheOrder" %>
<%@ page import="com.chengxusheji.po.PinChe" %>
<%@ page import="com.chengxusheji.po.UserInfo" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    List<PinCheOrder> pinCheOrderList = (List<PinCheOrder>)request.getAttribute("pinCheOrderList");
    //获取所有的pinCheObj信息
    List<PinChe> pinCheList = (List<PinChe>)request.getAttribute("pinCheList");
    //获取所有的userObj信息
    List<UserInfo> userInfoList = (List<UserInfo>)request.getAttribute("userInfoList");
    int currentPage =  (Integer)request.getAttribute("currentPage"); //当前页
    int totalPage =   (Integer)request.getAttribute("totalPage");  //一共多少页
    int recordNumber =   (Integer)request.getAttribute("recordNumber");  //一共多少记录
    PinChe pinCheObj = (PinChe)request.getAttribute("pinCheObj");
    UserInfo userObj = (UserInfo)request.getAttribute("userObj");
    String orderTime = (String)request.getAttribute("orderTime"); //下单时间查询关键字
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1 , user-scalable=no">
<title>拼车订单查询</title>
<link href="<%=basePath %>plugins/bootstrap.css" rel="stylesheet">
<link href="<%=basePath %>plugins/bootstrap-dashen.css" rel="stylesheet">
<link href="<%=basePath %>plugins/font-awesome.css" rel="stylesheet">
<link href="<%=basePath %>plugins/animate.css" rel="stylesheet">
<link href="<%=basePath %>plugins/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">
</head>
<body style="margin-top:70px;">
<div class="container">
<jsp:include page="../header.jsp"></jsp:include>
	<div class="row"> 
		<div class="col-md-9 wow fadeInDown" data-wow-duration="0.5s">
			<div>
				<!-- Nav tabs -->
				<ul class="nav nav-tabs" role="tablist">
			    	<li><a href="<%=basePath %>index.jsp">首页</a></li>
			    	<li role="presentation" class="active"><a href="#pinCheOrderListPanel" aria-controls="pinCheOrderListPanel" role="tab" data-toggle="tab">拼车订单列表</a></li>
			    	<li role="presentation" ><a href="<%=basePath %>PinCheOrder/pinCheOrder_frontAdd.jsp" style="display:none;">添加拼车订单</a></li>
				</ul>
			  	<!-- Tab panes -->
			  	<div class="tab-content">
				    <div role="tabpanel" class="tab-pane active" id="pinCheOrderListPanel">
				    		<div class="row">
				    			<div class="col-md-12 top5">
				    				<div class="table-responsive">
				    				<table class="table table-condensed table-hover">
				    					<tr class="success bold"><td>序号</td><td>拼车信息</td><td>用户</td><td>拼车人数</td><td>下单时间</td><td>操作</td></tr>
				    					<% 
				    						/*计算起始序号*/
				    	            		int startIndex = (currentPage -1) * 5;
				    	            		/*遍历记录*/
				    	            		for(int i=0;i<pinCheOrderList.size();i++) {
					    	            		int currentIndex = startIndex + i + 1; //当前记录的序号
					    	            		PinCheOrder pinCheOrder = pinCheOrderList.get(i); //获取到拼车订单对象
 										%>
 										<tr>
 											<td><%=currentIndex %></td>
 											<td><%=pinCheOrder.getPinCheObj().getCarObj().getCarNo() %>&nbsp;&nbsp;<%=pinCheOrder.getPinCheObj().getStartTime() %>出发:<%=pinCheOrder.getPinCheObj().getStartPlace() %>-><%=pinCheOrder.getPinCheObj().getEndPlace() %></td>
 											<td><%=pinCheOrder.getUserObj().getName() %></td>
 											<td><%=pinCheOrder.getPersonNum() %></td>
 											<td><%=pinCheOrder.getOrderTime() %></td>
 											<td>
 												<a href="<%=basePath  %>PinCheOrder/<%=pinCheOrder.getOrderId() %>/frontshow"><i class="fa fa-info"></i>&nbsp;查看</a>&nbsp;
 												<a href="#" onclick="pinCheOrderEdit('<%=pinCheOrder.getOrderId() %>');" style="display:none;"><i class="fa fa-pencil fa-fw"></i>编辑</a>&nbsp;
 												<a href="#" onclick="pinCheOrderDelete('<%=pinCheOrder.getOrderId() %>');" style="display:none;"><i class="fa fa-trash-o fa-fw"></i>删除</a>
 											</td> 
 										</tr>
 										<%}%>
				    				</table>
				    				</div>
				    			</div>
				    		</div>

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
						            <div class="pull-right" style="line-height:75px;" >共有<%=recordNumber %>条记录，当前第 <%=currentPage %>/<%=totalPage %> 页&nbsp;&nbsp;&nbsp;<button class="btn btn-primary" onclick="history.back();">返回</button></div>
					            </div>
				            </div> 
				    </div>
				</div>
			</div>
		</div>
	<div class="col-md-3 wow fadeInRight" style="display:none;">
		<div class="page-header">
    		<h1>拼车订单查询</h1>
		</div>
		<form name="pinCheOrderQueryForm" id="pinCheOrderQueryForm" action="<%=basePath %>PinCheOrder/frontlist" class="mar_t15">
            <div class="form-group">
            	<label for="pinCheObj_pincheId">拼车id：</label>
                <select id="pinCheObj_pincheId" name="pinCheObj.pincheId" class="form-control">
                	<option value="0">不限制</option>
	 				<%
	 				for(PinChe pinCheTemp:pinCheList) {
	 					String selected = "";
 					if(pinCheObj!=null && pinCheObj.getPincheId()!=null && pinCheObj.getPincheId().intValue()==pinCheTemp.getPincheId().intValue())
 						selected = "selected";
	 				%>
 				 <option value="<%=pinCheTemp.getPincheId() %>" <%=selected %>><%=pinCheTemp.getPincheId() %></option>
	 				<%
	 				}
	 				%>
 			</select>
            </div>
            <div class="form-group">
            	<label for="userObj_user_name">用户：</label>
                <select id="userObj_user_name" name="userObj.user_name" class="form-control">
                	<option value="">不限制</option>
	 				<%
	 				for(UserInfo userInfoTemp:userInfoList) {
	 					String selected = "";
 					if(userObj!=null && userObj.getUser_name()!=null && userObj.getUser_name().equals(userInfoTemp.getUser_name()))
 						selected = "selected";
	 				%>
 				 <option value="<%=userInfoTemp.getUser_name() %>" <%=selected %>><%=userInfoTemp.getName() %></option>
	 				<%
	 				}
	 				%>
 			</select>
            </div>
			<div class="form-group">
				<label for="orderTime">下单时间:</label>
				<input type="text" id="orderTime" name="orderTime" class="form-control"  placeholder="请选择下单时间" value="<%=orderTime %>" onclick="SelectDate(this,'yyyy-MM-dd')" />
			</div>
            <input type=hidden name=currentPage value="<%=currentPage %>" />
            <button type="submit" class="btn btn-primary">查询</button>
        </form>
	</div>

		</div>
	</div> 
<div id="pinCheOrderEditDialog" class="modal fade" tabindex="-1" role="dialog">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title"><i class="fa fa-edit"></i>&nbsp;拼车订单信息编辑</h4>
      </div>
      <div class="modal-body" style="height:450px; overflow: scroll;">
      	<form class="form-horizontal" name="pinCheOrderEditForm" id="pinCheOrderEditForm" enctype="multipart/form-data" method="post"  class="mar_t15">
		  <div class="form-group">
			 <label for="pinCheOrder_orderId_edit" class="col-md-3 text-right">订单id:</label>
			 <div class="col-md-9"> 
			 	<input type="text" id="pinCheOrder_orderId_edit" name="pinCheOrder.orderId" class="form-control" placeholder="请输入订单id" readOnly>
			 </div>
		  </div> 
		  <div class="form-group">
		  	 <label for="pinCheOrder_pinCheObj_pincheId_edit" class="col-md-3 text-right">拼车id:</label>
		  	 <div class="col-md-9">
			    <select id="pinCheOrder_pinCheObj_pincheId_edit" name="pinCheOrder.pinCheObj.pincheId" class="form-control">
			    </select>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="pinCheOrder_userObj_user_name_edit" class="col-md-3 text-right">用户:</label>
		  	 <div class="col-md-9">
			    <select id="pinCheOrder_userObj_user_name_edit" name="pinCheOrder.userObj.user_name" class="form-control">
			    </select>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="pinCheOrder_personNum_edit" class="col-md-3 text-right">拼车人数:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="pinCheOrder_personNum_edit" name="pinCheOrder.personNum" class="form-control" placeholder="请输入拼车人数">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="pinCheOrder_orderTime_edit" class="col-md-3 text-right">下单时间:</label>
		  	 <div class="col-md-9">
                <div class="input-group date pinCheOrder_orderTime_edit col-md-12" data-link-field="pinCheOrder_orderTime_edit">
                    <input class="form-control" id="pinCheOrder_orderTime_edit" name="pinCheOrder.orderTime" size="16" type="text" value="" placeholder="请选择下单时间" readonly>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                </div>
		  	 </div>
		  </div>
		</form> 
	    <style>#pinCheOrderEditForm .form-group {margin-bottom:5px;}  </style>
      </div>
      <div class="modal-footer"> 
      	<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      	<button type="button" class="btn btn-primary" onclick="ajaxPinCheOrderModify();">提交</button>
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
    document.pinCheOrderQueryForm.currentPage.value = currentPage;
    document.pinCheOrderQueryForm.submit();
}

/*可以直接跳转到某页*/
function changepage(totalPage)
{
    var pageValue=document.pinCheOrderQueryForm.pageValue.value;
    if(pageValue>totalPage) {
        alert('你输入的页码超出了总页数!');
        return ;
    }
    document.pinCheOrderQueryForm.currentPage.value = pageValue;
    documentpinCheOrderQueryForm.submit();
}

/*弹出修改拼车订单界面并初始化数据*/
function pinCheOrderEdit(orderId) {
	$.ajax({
		url :  basePath + "PinCheOrder/" + orderId + "/update",
		type : "get",
		dataType: "json",
		success : function (pinCheOrder, response, status) {
			if (pinCheOrder) {
				$("#pinCheOrder_orderId_edit").val(pinCheOrder.orderId);
				$.ajax({
					url: basePath + "PinChe/listAll",
					type: "get",
					success: function(pinChes,response,status) { 
						$("#pinCheOrder_pinCheObj_pincheId_edit").empty();
						var html="";
		        		$(pinChes).each(function(i,pinChe){
		        			html += "<option value='" + pinChe.pincheId + "'>" + pinChe.pincheId + "</option>";
		        		});
		        		$("#pinCheOrder_pinCheObj_pincheId_edit").html(html);
		        		$("#pinCheOrder_pinCheObj_pincheId_edit").val(pinCheOrder.pinCheObjPri);
					}
				});
				$.ajax({
					url: basePath + "UserInfo/listAll",
					type: "get",
					success: function(userInfos,response,status) { 
						$("#pinCheOrder_userObj_user_name_edit").empty();
						var html="";
		        		$(userInfos).each(function(i,userInfo){
		        			html += "<option value='" + userInfo.user_name + "'>" + userInfo.name + "</option>";
		        		});
		        		$("#pinCheOrder_userObj_user_name_edit").html(html);
		        		$("#pinCheOrder_userObj_user_name_edit").val(pinCheOrder.userObjPri);
					}
				});
				$("#pinCheOrder_personNum_edit").val(pinCheOrder.personNum);
				$("#pinCheOrder_orderTime_edit").val(pinCheOrder.orderTime);
				$('#pinCheOrderEditDialog').modal('show');
			} else {
				alert("获取信息失败！");
			}
		}
	});
}

/*删除拼车订单信息*/
function pinCheOrderDelete(orderId) {
	if(confirm("确认删除这个记录")) {
		$.ajax({
			type : "POST",
			url : basePath + "PinCheOrder/deletes",
			data : {
				orderIds : orderId,
			},
			success : function (obj) {
				if (obj.success) {
					alert("删除成功");
					$("#pinCheOrderQueryForm").submit();
					//location.href= basePath + "PinCheOrder/frontlist";
				}
				else 
					alert(data.message);
			},
		});
	}
}

/*ajax方式提交拼车订单信息表单给服务器端修改*/
function ajaxPinCheOrderModify() {
	$.ajax({
		url :  basePath + "PinCheOrder/" + $("#pinCheOrder_orderId_edit").val() + "/update",
		type : "post",
		dataType: "json",
		data: new FormData($("#pinCheOrderEditForm")[0]),
		success : function (obj, response, status) {
            if(obj.success){
                alert("信息修改成功！");
                $("#pinCheOrderQueryForm").submit();
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

    /*下单时间组件*/
    $('.pinCheOrder_orderTime_edit').datetimepicker({
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

