<%@ page language="java" import="java.util.*"  contentType="text/html;charset=UTF-8"%> 
<%@ page import="com.chengxusheji.po.CarInfo" %>
<%@ page import="com.chengxusheji.po.UserInfo" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    List<CarInfo> carInfoList = (List<CarInfo>)request.getAttribute("carInfoList");
    //获取所有的userObj信息
    List<UserInfo> userInfoList = (List<UserInfo>)request.getAttribute("userInfoList");
    int currentPage =  (Integer)request.getAttribute("currentPage"); //当前页
    int totalPage =   (Integer)request.getAttribute("totalPage");  //一共多少页
    int recordNumber =   (Integer)request.getAttribute("recordNumber");  //一共多少记录
    String carNo = (String)request.getAttribute("carNo"); //车牌号查询关键字
    String chexing = (String)request.getAttribute("chexing"); //车型查询关键字
    UserInfo userObj = (UserInfo)request.getAttribute("userObj");
    String shenHeState = (String)request.getAttribute("shenHeState"); //审核状态查询关键字
    String addTime = (String)request.getAttribute("addTime"); //提交时间查询关键字
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1 , user-scalable=no">
<title>小车查询</title>
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
  			<li><a href="<%=basePath %>CarInfo/frontlist">小车信息列表</a></li>
  			<li class="active">查询结果显示</li>
  			<a class="pull-right" href="<%=basePath %>CarInfo/carInfo_frontAdd.jsp" style="display:none;">添加小车</a>
		</ul>
		<div class="row">
			<%
				/*计算起始序号*/
				int startIndex = (currentPage -1) * 5;
				/*遍历记录*/
				for(int i=0;i<carInfoList.size();i++) {
            		int currentIndex = startIndex + i + 1; //当前记录的序号
            		CarInfo carInfo = carInfoList.get(i); //获取到小车对象
            		String clearLeft = "";
            		if(i%4 == 0) clearLeft = "style=\"clear:left;\"";
			%>
			<div class="col-md-3 bottom15" <%=clearLeft %>>
			  <a  href="<%=basePath  %>CarInfo/<%=carInfo.getCarNo() %>/frontshow"><img class="img-responsive" src="<%=basePath%><%=carInfo.getCarPhoto()%>" /></a>
			     <div class="showFields">
			     	<div class="field">
	            		车牌号:<%=carInfo.getCarNo() %>
			     	</div>
			     	<div class="field">
	            		车型:<%=carInfo.getChexing() %>
			     	</div>
			     	<div class="field">
	            		购买年份:<%=carInfo.getBuyYear() %>
			     	</div>
			     	<div class="field">
	            		所属学生:<%=carInfo.getUserObj().getName() %>
			     	</div>
			     	<div class="field">
	            		审核状态:<%=carInfo.getShenHeState() %>
			     	</div>
			     	<div class="field">
	            		提交时间:<%=carInfo.getAddTime() %>
			     	</div>
			        <a class="btn btn-primary top5" href="<%=basePath %>CarInfo/<%=carInfo.getCarNo() %>/frontshow">详情</a>
			        <a class="btn btn-primary top5" onclick="carInfoEdit('<%=carInfo.getCarNo() %>');" style="display:none;">修改</a>
			        <a class="btn btn-primary top5" onclick="carInfoDelete('<%=carInfo.getCarNo() %>');" style="display:none;">删除</a>
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
    		<h1>小车查询</h1>
		</div>
		<form name="carInfoQueryForm" id="carInfoQueryForm" action="<%=basePath %>CarInfo/frontlist" class="mar_t15">
			<div class="form-group">
				<label for="carNo">车牌号:</label>
				<input type="text" id="carNo" name="carNo" value="<%=carNo %>" class="form-control" placeholder="请输入车牌号">
			</div>
			<div class="form-group">
				<label for="chexing">车型:</label>
				<input type="text" id="chexing" name="chexing" value="<%=chexing %>" class="form-control" placeholder="请输入车型">
			</div>
            <div class="form-group">
            	<label for="userObj_user_name">所属学生：</label>
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
				<label for="shenHeState">审核状态:</label>
				<input type="text" id="shenHeState" name="shenHeState" value="<%=shenHeState %>" class="form-control" placeholder="请输入审核状态">
			</div>
			<div class="form-group">
				<label for="addTime">提交时间:</label>
				<input type="text" id="addTime" name="addTime" class="form-control"  placeholder="请选择提交时间" value="<%=addTime %>" onclick="SelectDate(this,'yyyy-MM-dd')" />
			</div>
            <input type=hidden name=currentPage value="<%=currentPage %>" />
            <button type="submit" class="btn btn-primary">查询</button>
        </form>
	</div>

		</div>
</div>
<div id="carInfoEditDialog" class="modal fade" tabindex="-1" role="dialog">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title"><i class="fa fa-edit"></i>&nbsp;小车信息编辑</h4>
      </div>
      <div class="modal-body" style="height:450px; overflow: scroll;">
      	<form class="form-horizontal" name="carInfoEditForm" id="carInfoEditForm" enctype="multipart/form-data" method="post"  class="mar_t15">
		  <div class="form-group">
			 <label for="carInfo_carNo_edit" class="col-md-3 text-right">车牌号:</label>
			 <div class="col-md-9"> 
			 	<input type="text" id="carInfo_carNo_edit" name="carInfo.carNo" class="form-control" placeholder="请输入车牌号" readOnly>
			 </div>
		  </div> 
		  <div class="form-group">
		  	 <label for="carInfo_chexing_edit" class="col-md-3 text-right">车型:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="carInfo_chexing_edit" name="carInfo.chexing" class="form-control" placeholder="请输入车型">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="carInfo_carPhoto_edit" class="col-md-3 text-right">车辆照片:</label>
		  	 <div class="col-md-9">
			    <img  class="img-responsive" id="carInfo_carPhotoImg" border="0px"/><br/>
			    <input type="hidden" id="carInfo_carPhoto" name="carInfo.carPhoto"/>
			    <input id="carPhotoFile" name="carPhotoFile" type="file" size="50" />
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="carInfo_buyYear_edit" class="col-md-3 text-right">购买年份:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="carInfo_buyYear_edit" name="carInfo.buyYear" class="form-control" placeholder="请输入购买年份">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="carInfo_jiaShiZheng_edit" class="col-md-3 text-right">驾驶证:</label>
		  	 <div class="col-md-9">
			    <img  class="img-responsive" id="carInfo_jiaShiZhengImg" border="0px"/><br/>
			    <input type="hidden" id="carInfo_jiaShiZheng" name="carInfo.jiaShiZheng"/>
			    <input id="jiaShiZhengFile" name="jiaShiZhengFile" type="file" size="50" />
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="carInfo_xueShenZheng_edit" class="col-md-3 text-right">学生证:</label>
		  	 <div class="col-md-9">
			    <img  class="img-responsive" id="carInfo_xueShenZhengImg" border="0px"/><br/>
			    <input type="hidden" id="carInfo_xueShenZheng" name="carInfo.xueShenZheng"/>
			    <input id="xueShenZhengFile" name="xueShenZhengFile" type="file" size="50" />
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="carInfo_carDesc_edit" class="col-md-3 text-right">车辆描述:</label>
		  	 <div class="col-md-9">
			    <textarea id="carInfo_carDesc_edit" name="carInfo.carDesc" rows="8" class="form-control" placeholder="请输入车辆描述"></textarea>
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="carInfo_userObj_user_name_edit" class="col-md-3 text-right">所属学生:</label>
		  	 <div class="col-md-9">
			    <select id="carInfo_userObj_user_name_edit" name="carInfo.userObj.user_name" class="form-control">
			    </select>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="carInfo_shenHeState_edit" class="col-md-3 text-right">审核状态:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="carInfo_shenHeState_edit" name="carInfo.shenHeState" class="form-control" placeholder="请输入审核状态">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="carInfo_addTime_edit" class="col-md-3 text-right">提交时间:</label>
		  	 <div class="col-md-9">
                <div class="input-group date carInfo_addTime_edit col-md-12" data-link-field="carInfo_addTime_edit">
                    <input class="form-control" id="carInfo_addTime_edit" name="carInfo.addTime" size="16" type="text" value="" placeholder="请选择提交时间" readonly>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                </div>
		  	 </div>
		  </div>
		</form> 
	    <style>#carInfoEditForm .form-group {margin-bottom:5px;}  </style>
      </div>
      <div class="modal-footer"> 
      	<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      	<button type="button" class="btn btn-primary" onclick="ajaxCarInfoModify();">提交</button>
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
    document.carInfoQueryForm.currentPage.value = currentPage;
    document.carInfoQueryForm.submit();
}

/*可以直接跳转到某页*/
function changepage(totalPage)
{
    var pageValue=document.carInfoQueryForm.pageValue.value;
    if(pageValue>totalPage) {
        alert('你输入的页码超出了总页数!');
        return ;
    }
    document.carInfoQueryForm.currentPage.value = pageValue;
    documentcarInfoQueryForm.submit();
}

/*弹出修改小车界面并初始化数据*/
function carInfoEdit(carNo) {
	$.ajax({
		url :  basePath + "CarInfo/" + carNo + "/update",
		type : "get",
		dataType: "json",
		success : function (carInfo, response, status) {
			if (carInfo) {
				$("#carInfo_carNo_edit").val(carInfo.carNo);
				$("#carInfo_chexing_edit").val(carInfo.chexing);
				$("#carInfo_carPhoto").val(carInfo.carPhoto);
				$("#carInfo_carPhotoImg").attr("src", basePath +　carInfo.carPhoto);
				$("#carInfo_buyYear_edit").val(carInfo.buyYear);
				$("#carInfo_jiaShiZheng").val(carInfo.jiaShiZheng);
				$("#carInfo_jiaShiZhengImg").attr("src", basePath +　carInfo.jiaShiZheng);
				$("#carInfo_xueShenZheng").val(carInfo.xueShenZheng);
				$("#carInfo_xueShenZhengImg").attr("src", basePath +　carInfo.xueShenZheng);
				$("#carInfo_carDesc_edit").val(carInfo.carDesc);
				$.ajax({
					url: basePath + "UserInfo/listAll",
					type: "get",
					success: function(userInfos,response,status) { 
						$("#carInfo_userObj_user_name_edit").empty();
						var html="";
		        		$(userInfos).each(function(i,userInfo){
		        			html += "<option value='" + userInfo.user_name + "'>" + userInfo.name + "</option>";
		        		});
		        		$("#carInfo_userObj_user_name_edit").html(html);
		        		$("#carInfo_userObj_user_name_edit").val(carInfo.userObjPri);
					}
				});
				$("#carInfo_shenHeState_edit").val(carInfo.shenHeState);
				$("#carInfo_addTime_edit").val(carInfo.addTime);
				$('#carInfoEditDialog').modal('show');
			} else {
				alert("获取信息失败！");
			}
		}
	});
}

/*删除小车信息*/
function carInfoDelete(carNo) {
	if(confirm("确认删除这个记录")) {
		$.ajax({
			type : "POST",
			url : basePath + "CarInfo/deletes",
			data : {
				carNos : carNo,
			},
			success : function (obj) {
				if (obj.success) {
					alert("删除成功");
					$("#carInfoQueryForm").submit();
					//location.href= basePath + "CarInfo/frontlist";
				}
				else 
					alert(data.message);
			},
		});
	}
}

/*ajax方式提交小车信息表单给服务器端修改*/
function ajaxCarInfoModify() {
	$.ajax({
		url :  basePath + "CarInfo/" + $("#carInfo_carNo_edit").val() + "/update",
		type : "post",
		dataType: "json",
		data: new FormData($("#carInfoEditForm")[0]),
		success : function (obj, response, status) {
            if(obj.success){
                alert("信息修改成功！");
                $("#carInfoQueryForm").submit();
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

    /*提交时间组件*/
    $('.carInfo_addTime_edit').datetimepicker({
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

