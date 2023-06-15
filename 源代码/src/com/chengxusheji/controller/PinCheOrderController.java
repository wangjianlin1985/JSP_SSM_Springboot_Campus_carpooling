package com.chengxusheji.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.chengxusheji.utils.ExportExcelUtil;
import com.chengxusheji.utils.UserException;
import com.chengxusheji.service.PinCheOrderService;
import com.chengxusheji.po.PinCheOrder;
import com.chengxusheji.service.PinCheService;
import com.chengxusheji.po.PinChe;
import com.chengxusheji.service.UserInfoService;
import com.chengxusheji.po.UserInfo;

//PinCheOrder管理控制层
@Controller
@RequestMapping("/PinCheOrder")
public class PinCheOrderController extends BaseController {

    /*业务层对象*/
    @Resource PinCheOrderService pinCheOrderService;

    @Resource PinCheService pinCheService;
    @Resource UserInfoService userInfoService;
	@InitBinder("pinCheObj")
	public void initBinderpinCheObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("pinCheObj.");
	}
	@InitBinder("userObj")
	public void initBinderuserObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("userObj.");
	}
	@InitBinder("pinCheOrder")
	public void initBinderPinCheOrder(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("pinCheOrder.");
	}
	/*跳转到添加PinCheOrder视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new PinCheOrder());
		/*查询所有的PinChe信息*/
		List<PinChe> pinCheList = pinCheService.queryAllPinChe();
		request.setAttribute("pinCheList", pinCheList);
		/*查询所有的UserInfo信息*/
		List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
		request.setAttribute("userInfoList", userInfoList);
		return "PinCheOrder_add";
	}

	/*客户端ajax方式提交添加拼车订单信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated PinCheOrder pinCheOrder, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
        pinCheOrderService.addPinCheOrder(pinCheOrder);
        message = "拼车订单添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	
	
	/*客户端ajax方式用户提交添加拼车订单信息*/
	@RequestMapping(value = "/userAdd", method = RequestMethod.POST)
	public void userAdd(PinCheOrder pinCheOrder, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response,HttpSession session) throws Exception {
		String message = "";
		boolean success = false; 
		PinChe pinChe = pinCheService.getPinChe(pinCheOrder.getPinCheObj().getPincheId()); 
		String user_name = (String)session.getAttribute("user_name");
		if(user_name == null) {
			message = "请先登录网站！";
			writeJsonResponse(response, success, message);
			return;
		}
		
		UserInfo userObj = userInfoService.getUserInfo(user_name);
		
		if(pinCheOrderService.queryPinCheOrder(pinChe, userObj, "").size() > 0) {
			message = "你已经加入过这个拼车信息，不能重复下单！";
			writeJsonResponse(response, success, message);
			return;
		}
		
		int leftNum = pinChe.getTotalNum()-pinChe.getHaveNum();
		if(leftNum < pinCheOrder.getPersonNum()) {
			message = "不能满足你拼车人数需求，失败！";
			writeJsonResponse(response, success, message);
			return;
		}
		
		pinCheOrder.setUserObj(userObj);
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		pinCheOrder.setOrderTime(sdf.format(new java.util.Date()));
		
        pinCheOrderService.addPinCheOrder(pinCheOrder);
        
        pinChe.setHaveNum(pinChe.getHaveNum() + pinCheOrder.getPersonNum());
        pinCheService.updatePinChe(pinChe);
        
        
        message = "拼车订单添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	
	
	
	
	
	/*ajax方式按照查询条件分页查询拼车订单信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(@ModelAttribute("pinCheObj") PinChe pinCheObj,@ModelAttribute("userObj") UserInfo userObj,String orderTime,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (orderTime == null) orderTime = "";
		if(rows != 0)pinCheOrderService.setRows(rows);
		List<PinCheOrder> pinCheOrderList = pinCheOrderService.queryPinCheOrder(pinCheObj, userObj, orderTime, page);
	    /*计算总的页数和总的记录数*/
	    pinCheOrderService.queryTotalPageAndRecordNumber(pinCheObj, userObj, orderTime);
	    /*获取到总的页码数目*/
	    int totalPage = pinCheOrderService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = pinCheOrderService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(PinCheOrder pinCheOrder:pinCheOrderList) {
			JSONObject jsonPinCheOrder = pinCheOrder.getJsonObject();
			jsonArray.put(jsonPinCheOrder);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	/*ajax方式按照查询条件分页查询拼车订单信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<PinCheOrder> pinCheOrderList = pinCheOrderService.queryAllPinCheOrder();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(PinCheOrder pinCheOrder:pinCheOrderList) {
			JSONObject jsonPinCheOrder = new JSONObject();
			jsonPinCheOrder.accumulate("orderId", pinCheOrder.getOrderId());
			jsonPinCheOrder.accumulate("orderId", pinCheOrder.getOrderId());
			jsonArray.put(jsonPinCheOrder);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询拼车订单信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(@ModelAttribute("pinCheObj") PinChe pinCheObj,@ModelAttribute("userObj") UserInfo userObj,String orderTime,Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (orderTime == null) orderTime = "";
		List<PinCheOrder> pinCheOrderList = pinCheOrderService.queryPinCheOrder(pinCheObj, userObj, orderTime, currentPage);
	    /*计算总的页数和总的记录数*/
	    pinCheOrderService.queryTotalPageAndRecordNumber(pinCheObj, userObj, orderTime);
	    /*获取到总的页码数目*/
	    int totalPage = pinCheOrderService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = pinCheOrderService.getRecordNumber();
	    request.setAttribute("pinCheOrderList",  pinCheOrderList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("pinCheObj", pinCheObj);
	    request.setAttribute("userObj", userObj);
	    request.setAttribute("orderTime", orderTime);
	    List<PinChe> pinCheList = pinCheService.queryAllPinChe();
	    request.setAttribute("pinCheList", pinCheList);
	    List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
	    request.setAttribute("userInfoList", userInfoList);
		return "PinCheOrder/pinCheOrder_frontquery_result"; 
	}
	
	
	
	
	/*前台用户按照查询条件分页查询拼车订单信息*/
	@RequestMapping(value = { "/userFrontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String userFrontlist(@ModelAttribute("pinCheObj") PinChe pinCheObj,@ModelAttribute("userObj") UserInfo userObj,String orderTime,Integer currentPage, Model model, HttpServletRequest request,HttpSession session) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (orderTime == null) orderTime = "";
		String user_name = (String) session.getAttribute("user_name");
		userObj = new UserInfo();
		userObj.setUser_name(user_name);
		
		List<PinCheOrder> pinCheOrderList = pinCheOrderService.queryPinCheOrder(pinCheObj, userObj, orderTime, currentPage);
	    /*计算总的页数和总的记录数*/
	    pinCheOrderService.queryTotalPageAndRecordNumber(pinCheObj, userObj, orderTime);
	    /*获取到总的页码数目*/
	    int totalPage = pinCheOrderService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = pinCheOrderService.getRecordNumber();
	    request.setAttribute("pinCheOrderList",  pinCheOrderList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("pinCheObj", pinCheObj);
	    request.setAttribute("userObj", userObj);
	    request.setAttribute("orderTime", orderTime);
	    List<PinChe> pinCheList = pinCheService.queryAllPinChe();
	    request.setAttribute("pinCheList", pinCheList);
	    List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
	    request.setAttribute("userInfoList", userInfoList);
		return "PinCheOrder/pinCheOrder_userFrontquery_result"; 
	}
	
	
	

     /*前台查询PinCheOrder信息*/
	@RequestMapping(value="/{orderId}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable Integer orderId,Model model,HttpServletRequest request) throws Exception {
		/*根据主键orderId获取PinCheOrder对象*/
        PinCheOrder pinCheOrder = pinCheOrderService.getPinCheOrder(orderId);

        List<PinChe> pinCheList = pinCheService.queryAllPinChe();
        request.setAttribute("pinCheList", pinCheList);
        List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
        request.setAttribute("userInfoList", userInfoList);
        request.setAttribute("pinCheOrder",  pinCheOrder);
        return "PinCheOrder/pinCheOrder_frontshow";
	}

	/*ajax方式显示拼车订单修改jsp视图页*/
	@RequestMapping(value="/{orderId}/update",method=RequestMethod.GET)
	public void update(@PathVariable Integer orderId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键orderId获取PinCheOrder对象*/
        PinCheOrder pinCheOrder = pinCheOrderService.getPinCheOrder(orderId);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonPinCheOrder = pinCheOrder.getJsonObject();
		out.println(jsonPinCheOrder.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新拼车订单信息*/
	@RequestMapping(value = "/{orderId}/update", method = RequestMethod.POST)
	public void update(@Validated PinCheOrder pinCheOrder, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		try {
			pinCheOrderService.updatePinCheOrder(pinCheOrder);
			message = "拼车订单更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "拼车订单更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除拼车订单信息*/
	@RequestMapping(value="/{orderId}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable Integer orderId,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  pinCheOrderService.deletePinCheOrder(orderId);
	            request.setAttribute("message", "拼车订单删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "拼车订单删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条拼车订单记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String orderIds,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = pinCheOrderService.deletePinCheOrders(orderIds);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出拼车订单信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel(@ModelAttribute("pinCheObj") PinChe pinCheObj,@ModelAttribute("userObj") UserInfo userObj,String orderTime, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        if(orderTime == null) orderTime = "";
        List<PinCheOrder> pinCheOrderList = pinCheOrderService.queryPinCheOrder(pinCheObj,userObj,orderTime);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "PinCheOrder信息记录"; 
        String[] headers = { "拼车id","用户","拼车人数","下单时间"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<pinCheOrderList.size();i++) {
        	PinCheOrder pinCheOrder = pinCheOrderList.get(i); 
        	dataset.add(new String[]{pinCheOrder.getPinCheObj().getPincheId()+"",pinCheOrder.getUserObj().getName(),pinCheOrder.getPersonNum() + "",pinCheOrder.getOrderTime()});
        }
        /*
        OutputStream out = null;
		try {
			out = new FileOutputStream("C://output.xls");
			ex.exportExcel(title,headers, dataset, out);
		    out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		OutputStream out = null;//创建一个输出流对象 
		try { 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"PinCheOrder.xls");//filename是下载的xls的名，建议最好用英文 
			response.setContentType("application/msexcel;charset=UTF-8");//设置类型 
			response.setHeader("Pragma","No-cache");//设置头 
			response.setHeader("Cache-Control","no-cache");//设置头 
			response.setDateHeader("Expires", 0);//设置日期头  
			String rootPath = request.getSession().getServletContext().getRealPath("/");
			ex.exportExcel(rootPath,title,headers, dataset, out);
			out.flush();
		} catch (IOException e) { 
			e.printStackTrace(); 
		}finally{
			try{
				if(out!=null){ 
					out.close(); 
				}
			}catch(IOException e){ 
				e.printStackTrace(); 
			} 
		}
    }
}
