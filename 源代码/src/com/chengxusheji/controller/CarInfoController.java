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
import com.chengxusheji.service.CarInfoService;
import com.chengxusheji.po.CarInfo;
import com.chengxusheji.service.UserInfoService;
import com.chengxusheji.po.UserInfo;

//CarInfo管理控制层
@Controller
@RequestMapping("/CarInfo")
public class CarInfoController extends BaseController {

    /*业务层对象*/
    @Resource CarInfoService carInfoService;

    @Resource UserInfoService userInfoService;
	@InitBinder("userObj")
	public void initBinderuserObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("userObj.");
	}
	@InitBinder("carInfo")
	public void initBinderCarInfo(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("carInfo.");
	}
	/*跳转到添加CarInfo视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new CarInfo());
		/*查询所有的UserInfo信息*/
		List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
		request.setAttribute("userInfoList", userInfoList);
		return "CarInfo_add";
	}

	/*客户端ajax方式提交添加小车信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated CarInfo carInfo, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
		if(carInfoService.getCarInfo(carInfo.getCarNo()) != null) {
			message = "车牌号已经存在！";
			writeJsonResponse(response, success, message);
			return ;
		}
		try {
			carInfo.setCarPhoto(this.handlePhotoUpload(request, "carPhotoFile"));
		} catch(UserException ex) {
			message = "图片格式不正确！";
			writeJsonResponse(response, success, message);
			return ;
		}
		try {
			carInfo.setJiaShiZheng(this.handlePhotoUpload(request, "jiaShiZhengFile"));
		} catch(UserException ex) {
			message = "图片格式不正确！";
			writeJsonResponse(response, success, message);
			return ;
		}
		try {
			carInfo.setXueShenZheng(this.handlePhotoUpload(request, "xueShenZhengFile"));
		} catch(UserException ex) {
			message = "图片格式不正确！";
			writeJsonResponse(response, success, message);
			return ;
		}
        carInfoService.addCarInfo(carInfo);
        message = "小车添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	
	
	
	/*客户端ajax方式用户前台添加自己的小车信息*/
	@RequestMapping(value = "/userAdd", method = RequestMethod.POST)
	public void userAdd(CarInfo carInfo, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response,HttpSession session) throws Exception {
		String message = "";
		boolean success = false;
	 
		String userName = (String)session.getAttribute("user_name");
		
		if(userName == null) {
			message = "请先登录系统！";
			writeJsonResponse(response, success, message);
			return ;
		}
		
		if(carInfoService.getCarInfo(carInfo.getCarNo()) != null) {
			message = "车牌号已经存在！";
			writeJsonResponse(response, success, message);
			return ;
		}
		try {
			carInfo.setCarPhoto(this.handlePhotoUpload(request, "carPhotoFile"));
		} catch(UserException ex) {
			message = "图片格式不正确！";
			writeJsonResponse(response, success, message);
			return ;
		}
		try {
			carInfo.setJiaShiZheng(this.handlePhotoUpload(request, "jiaShiZhengFile"));
		} catch(UserException ex) {
			message = "图片格式不正确！";
			writeJsonResponse(response, success, message);
			return ;
		}
		try {
			carInfo.setXueShenZheng(this.handlePhotoUpload(request, "xueShenZhengFile"));
		} catch(UserException ex) {
			message = "图片格式不正确！";
			writeJsonResponse(response, success, message);
			return ;
		}
		
		UserInfo userObj = userInfoService.getUserInfo(userName);
		carInfo.setUserObj(userObj);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		carInfo.setAddTime(sdf.format(new java.util.Date()));
		carInfo.setShenHeState("待审核");
		
        carInfoService.addCarInfo(carInfo);
        message = "小车提交成功,等待管理员审核!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	
	
	
	
	
	/*ajax方式按照查询条件分页查询小车信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(String carNo,String chexing,@ModelAttribute("userObj") UserInfo userObj,String shenHeState,String addTime,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (carNo == null) carNo = "";
		if (chexing == null) chexing = "";
		if (shenHeState == null) shenHeState = "";
		if (addTime == null) addTime = "";
		if(rows != 0)carInfoService.setRows(rows);
		List<CarInfo> carInfoList = carInfoService.queryCarInfo(carNo, chexing, userObj, shenHeState, addTime, page);
	    /*计算总的页数和总的记录数*/
	    carInfoService.queryTotalPageAndRecordNumber(carNo, chexing, userObj, shenHeState, addTime);
	    /*获取到总的页码数目*/
	    int totalPage = carInfoService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = carInfoService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(CarInfo carInfo:carInfoList) {
			JSONObject jsonCarInfo = carInfo.getJsonObject();
			jsonArray.put(jsonCarInfo);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	/*ajax方式按照查询条件分页查询小车信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<CarInfo> carInfoList = carInfoService.queryAllCarInfo();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(CarInfo carInfo:carInfoList) {
			JSONObject jsonCarInfo = new JSONObject();
			jsonCarInfo.accumulate("carNo", carInfo.getCarNo()); 
			jsonArray.put(jsonCarInfo);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}
	
	
	/*ajax方式按照查询条件分页查询小车信息*/
	@RequestMapping(value = { "/userListAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void userListAll(HttpServletResponse response,HttpSession session) throws Exception {
		String userName = (String)session.getAttribute("user_name");
		List<CarInfo> carInfoList = carInfoService.queryUserAllCarInfo(userName);
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(CarInfo carInfo:carInfoList) {
			JSONObject jsonCarInfo = new JSONObject();
			jsonCarInfo.accumulate("carNo", carInfo.getCarNo()); 
			jsonArray.put(jsonCarInfo);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}
	

	/*前台按照查询条件分页查询小车信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(String carNo,String chexing,@ModelAttribute("userObj") UserInfo userObj,String shenHeState,String addTime,Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (carNo == null) carNo = "";
		if (chexing == null) chexing = "";
		if (shenHeState == null) shenHeState = "";
		if (addTime == null) addTime = "";
		List<CarInfo> carInfoList = carInfoService.queryCarInfo(carNo, chexing, userObj, shenHeState, addTime, currentPage);
	    /*计算总的页数和总的记录数*/
	    carInfoService.queryTotalPageAndRecordNumber(carNo, chexing, userObj, shenHeState, addTime);
	    /*获取到总的页码数目*/
	    int totalPage = carInfoService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = carInfoService.getRecordNumber();
	    request.setAttribute("carInfoList",  carInfoList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("carNo", carNo);
	    request.setAttribute("chexing", chexing);
	    request.setAttribute("userObj", userObj);
	    request.setAttribute("shenHeState", shenHeState);
	    request.setAttribute("addTime", addTime);
	    List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
	    request.setAttribute("userInfoList", userInfoList);
		return "CarInfo/carInfo_frontquery_result"; 
	}

	
	
	/*前台按照查询条件分页查询小车信息*/
	@RequestMapping(value = { "/userFrontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String userFrontlist(String carNo,String chexing,@ModelAttribute("userObj") UserInfo userObj,String shenHeState,String addTime,Integer currentPage, Model model, HttpServletRequest request,HttpSession session) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (carNo == null) carNo = "";
		if (chexing == null) chexing = "";
		if (shenHeState == null) shenHeState = "";
		if (addTime == null) addTime = "";
		userObj = new UserInfo();
		String userName = (String)session.getAttribute("user_name");
		userObj.setUser_name(userName);
		List<CarInfo> carInfoList = carInfoService.queryCarInfo(carNo, chexing, userObj, shenHeState, addTime, currentPage);
	    /*计算总的页数和总的记录数*/
	    carInfoService.queryTotalPageAndRecordNumber(carNo, chexing, userObj, shenHeState, addTime);
	    /*获取到总的页码数目*/
	    int totalPage = carInfoService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = carInfoService.getRecordNumber();
	    request.setAttribute("carInfoList",  carInfoList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("carNo", carNo);
	    request.setAttribute("chexing", chexing);
	    request.setAttribute("userObj", userObj);
	    request.setAttribute("shenHeState", shenHeState);
	    request.setAttribute("addTime", addTime);
	    List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
	    request.setAttribute("userInfoList", userInfoList);
		return "CarInfo/carInfo_userFrontquery_result"; 
	}
	
	
	
     /*前台查询CarInfo信息*/
	@RequestMapping(value="/{carNo}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable String carNo,Model model,HttpServletRequest request) throws Exception {
		/*根据主键carNo获取CarInfo对象*/
        CarInfo carInfo = carInfoService.getCarInfo(carNo);

        List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
        request.setAttribute("userInfoList", userInfoList);
        request.setAttribute("carInfo",  carInfo);
        return "CarInfo/carInfo_frontshow";
	}

	/*ajax方式显示小车修改jsp视图页*/
	@RequestMapping(value="/{carNo}/update",method=RequestMethod.GET)
	public void update(@PathVariable String carNo,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键carNo获取CarInfo对象*/
        CarInfo carInfo = carInfoService.getCarInfo(carNo);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonCarInfo = carInfo.getJsonObject();
		out.println(jsonCarInfo.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新小车信息*/
	@RequestMapping(value = "/{carNo}/update", method = RequestMethod.POST)
	public void update(@Validated CarInfo carInfo, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		String carPhotoFileName = this.handlePhotoUpload(request, "carPhotoFile");
		if(!carPhotoFileName.equals("upload/NoImage.jpg"))carInfo.setCarPhoto(carPhotoFileName); 


		String jiaShiZhengFileName = this.handlePhotoUpload(request, "jiaShiZhengFile");
		if(!jiaShiZhengFileName.equals("upload/NoImage.jpg"))carInfo.setJiaShiZheng(jiaShiZhengFileName); 


		String xueShenZhengFileName = this.handlePhotoUpload(request, "xueShenZhengFile");
		if(!xueShenZhengFileName.equals("upload/NoImage.jpg"))carInfo.setXueShenZheng(xueShenZhengFileName); 


		try {
			carInfoService.updateCarInfo(carInfo);
			message = "小车更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "小车更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除小车信息*/
	@RequestMapping(value="/{carNo}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable String carNo,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  carInfoService.deleteCarInfo(carNo);
	            request.setAttribute("message", "小车删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "小车删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条小车记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String carNos,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = carInfoService.deleteCarInfos(carNos);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出小车信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel(String carNo,String chexing,@ModelAttribute("userObj") UserInfo userObj,String shenHeState,String addTime, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        if(carNo == null) carNo = "";
        if(chexing == null) chexing = "";
        if(shenHeState == null) shenHeState = "";
        if(addTime == null) addTime = "";
        List<CarInfo> carInfoList = carInfoService.queryCarInfo(carNo,chexing,userObj,shenHeState,addTime);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "CarInfo信息记录"; 
        String[] headers = { "车牌号","车型","车辆照片","购买年份","驾驶证","学生证","所属学生","审核状态","提交时间"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<carInfoList.size();i++) {
        	CarInfo carInfo = carInfoList.get(i); 
        	dataset.add(new String[]{carInfo.getCarNo(),carInfo.getChexing(),carInfo.getCarPhoto(),carInfo.getBuyYear() + "",carInfo.getJiaShiZheng(),carInfo.getXueShenZheng(),carInfo.getUserObj().getName(),carInfo.getShenHeState(),carInfo.getAddTime()});
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
			response.setHeader("Content-disposition","attachment; filename="+"CarInfo.xls");//filename是下载的xls的名，建议最好用英文 
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
