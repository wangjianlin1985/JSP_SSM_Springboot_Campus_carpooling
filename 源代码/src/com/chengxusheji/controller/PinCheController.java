package com.chengxusheji.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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
import com.chengxusheji.service.PinCheService;
import com.chengxusheji.po.PinChe;
import com.chengxusheji.service.CarInfoService;
import com.chengxusheji.po.CarInfo;

//PinChe管理控制层
@Controller
@RequestMapping("/PinChe")
public class PinCheController extends BaseController {

    /*业务层对象*/
    @Resource PinCheService pinCheService;

    @Resource CarInfoService carInfoService;
	@InitBinder("carObj")
	public void initBindercarObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("carObj.");
	}
	@InitBinder("pinChe")
	public void initBinderPinChe(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("pinChe.");
	}
	/*跳转到添加PinChe视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new PinChe());
		/*查询所有的CarInfo信息*/
		List<CarInfo> carInfoList = carInfoService.queryAllCarInfo();
		request.setAttribute("carInfoList", carInfoList);
		return "PinChe_add";
	}

	/*客户端ajax方式提交添加拼车信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated PinChe pinChe, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
		try {
			pinChe.setPinChePhoto(this.handlePhotoUpload(request, "pinChePhotoFile"));
		} catch(UserException ex) {
			message = "图片格式不正确！";
			writeJsonResponse(response, success, message);
			return ;
		}
        pinCheService.addPinChe(pinChe);
        message = "拼车添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	
	/*客户端ajax方式提交添加拼车信息*/
	@RequestMapping(value = "/userAdd", method = RequestMethod.POST)
	public void userAdd(PinChe pinChe, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		 
		try {
			pinChe.setPinChePhoto(this.handlePhotoUpload(request, "pinChePhotoFile"));
		} catch(UserException ex) {
			message = "图片格式不正确！";
			writeJsonResponse(response, success, message);
			return ;
		}
		
		pinChe.setHaveNum(0); 
		
        pinCheService.addPinChe(pinChe);
        message = "拼车发布成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	
	
	/*ajax方式按照查询条件分页查询拼车信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(@ModelAttribute("carObj") CarInfo carObj,String startTime,String startPlace,String endPlace,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (startTime == null) startTime = "";
		if (startPlace == null) startPlace = "";
		if (endPlace == null) endPlace = "";
		if(rows != 0)pinCheService.setRows(rows);
		List<PinChe> pinCheList = pinCheService.queryPinChe(carObj, startTime, startPlace, endPlace, page);
	    /*计算总的页数和总的记录数*/
	    pinCheService.queryTotalPageAndRecordNumber(carObj, startTime, startPlace, endPlace);
	    /*获取到总的页码数目*/
	    int totalPage = pinCheService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = pinCheService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(PinChe pinChe:pinCheList) {
			JSONObject jsonPinChe = pinChe.getJsonObject();
			jsonArray.put(jsonPinChe);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	/*ajax方式按照查询条件分页查询拼车信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<PinChe> pinCheList = pinCheService.queryAllPinChe();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(PinChe pinChe:pinCheList) {
			JSONObject jsonPinChe = new JSONObject(); 
			jsonPinChe.accumulate("pincheId", pinChe.getPincheId());
			jsonArray.put(jsonPinChe);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询拼车信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(@ModelAttribute("carObj") CarInfo carObj,String startTime,String startPlace,String endPlace,Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (startTime == null) startTime = "";
		if (startPlace == null) startPlace = "";
		if (endPlace == null) endPlace = "";
		List<PinChe> pinCheList = pinCheService.queryPinChe(carObj, startTime, startPlace, endPlace, currentPage);
	    /*计算总的页数和总的记录数*/
	    pinCheService.queryTotalPageAndRecordNumber(carObj, startTime, startPlace, endPlace);
	    /*获取到总的页码数目*/
	    int totalPage = pinCheService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = pinCheService.getRecordNumber();
	    request.setAttribute("pinCheList",  pinCheList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("carObj", carObj);
	    request.setAttribute("startTime", startTime);
	    request.setAttribute("startPlace", startPlace);
	    request.setAttribute("endPlace", endPlace);
	    List<CarInfo> carInfoList = carInfoService.queryAllCarInfo();
	    request.setAttribute("carInfoList", carInfoList);
		return "PinChe/pinChe_frontquery_result"; 
	}

	
	
	/*前台用户按照查询条件分页查询自己发布的拼车信息*/
	@RequestMapping(value = { "/userFrontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String userFrontlist(@ModelAttribute("carObj") CarInfo carObj,String startTime,String startPlace,String endPlace,Integer currentPage, Model model, HttpServletRequest request,HttpSession session) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (startTime == null) startTime = "";
		if (startPlace == null) startPlace = "";
		if (endPlace == null) endPlace = "";
		String userName = (String)session.getAttribute("user_name");
		
		List<PinChe> pinCheList = pinCheService.queryPinChe(userName, startTime, startPlace, endPlace, currentPage);
	    /*计算总的页数和总的记录数*/
	    pinCheService.queryTotalPageAndRecordNumber(userName, startTime, startPlace, endPlace);
	    /*获取到总的页码数目*/
	    int totalPage = pinCheService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = pinCheService.getRecordNumber();
	    request.setAttribute("pinCheList",  pinCheList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("carObj", carObj);
	    request.setAttribute("startTime", startTime);
	    request.setAttribute("startPlace", startPlace);
	    request.setAttribute("endPlace", endPlace);
	    List<CarInfo> carInfoList = carInfoService.queryAllCarInfo();
	    request.setAttribute("carInfoList", carInfoList);
		return "PinChe/pinChe_userFrontquery_result"; 
	}
	
	
	
     /*前台查询PinChe信息*/
	@RequestMapping(value="/{pincheId}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable Integer pincheId,Model model,HttpServletRequest request) throws Exception {
		/*根据主键pincheId获取PinChe对象*/
        PinChe pinChe = pinCheService.getPinChe(pincheId);

        List<CarInfo> carInfoList = carInfoService.queryAllCarInfo();
        request.setAttribute("carInfoList", carInfoList);
        request.setAttribute("pinChe",  pinChe);
        return "PinChe/pinChe_frontshow";
	}

	/*ajax方式显示拼车修改jsp视图页*/
	@RequestMapping(value="/{pincheId}/update",method=RequestMethod.GET)
	public void update(@PathVariable Integer pincheId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键pincheId获取PinChe对象*/
        PinChe pinChe = pinCheService.getPinChe(pincheId);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonPinChe = pinChe.getJsonObject();
		out.println(jsonPinChe.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新拼车信息*/
	@RequestMapping(value = "/{pincheId}/update", method = RequestMethod.POST)
	public void update(@Validated PinChe pinChe, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		String pinChePhotoFileName = this.handlePhotoUpload(request, "pinChePhotoFile");
		if(!pinChePhotoFileName.equals("upload/NoImage.jpg"))pinChe.setPinChePhoto(pinChePhotoFileName); 


		try {
			pinCheService.updatePinChe(pinChe);
			message = "拼车更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "拼车更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除拼车信息*/
	@RequestMapping(value="/{pincheId}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable Integer pincheId,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  pinCheService.deletePinChe(pincheId);
	            request.setAttribute("message", "拼车删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "拼车删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条拼车记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String pincheIds,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = pinCheService.deletePinChes(pincheIds);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出拼车信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel(@ModelAttribute("carObj") CarInfo carObj,String startTime,String startPlace,String endPlace, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        if(startTime == null) startTime = "";
        if(startPlace == null) startPlace = "";
        if(endPlace == null) endPlace = "";
        List<PinChe> pinCheList = pinCheService.queryPinChe(carObj,startTime,startPlace,endPlace);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "PinChe信息记录"; 
        String[] headers = { "车辆信息","拼车图片","出发时间","车出发位置","目的地","价格","可拼人数","已拼人数"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<pinCheList.size();i++) {
        	PinChe pinChe = pinCheList.get(i); 
        	dataset.add(new String[]{pinChe.getCarObj().getCarNo(),pinChe.getPinChePhoto(),pinChe.getStartTime(),pinChe.getStartPlace(),pinChe.getEndPlace(),pinChe.getPrice() + "",pinChe.getTotalNum() + "",pinChe.getHaveNum() + ""});
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
			response.setHeader("Content-disposition","attachment; filename="+"PinChe.xls");//filename是下载的xls的名，建议最好用英文 
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
