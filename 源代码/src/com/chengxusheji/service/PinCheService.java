package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.CarInfo;
import com.chengxusheji.po.PinChe;

import com.chengxusheji.mapper.PinCheMapper;
@Service
public class PinCheService {

	@Resource PinCheMapper pinCheMapper;
    /*每页显示记录数目*/
    private int rows = 10;;
    public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}

    /*保存查询后总的页数*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    /*保存查询到的总记录数*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*添加拼车记录*/
    public void addPinChe(PinChe pinChe) throws Exception {
    	pinCheMapper.addPinChe(pinChe);
    }

    /*按照查询条件分页查询拼车记录*/
    public ArrayList<PinChe> queryPinChe(CarInfo carObj,String startTime,String startPlace,String endPlace,int currentPage) throws Exception { 
     	String where = "where 1=1";
    	if(null != carObj &&  carObj.getCarNo() != null  && !carObj.getCarNo().equals(""))  where += " and t_pinChe.carObj='" + carObj.getCarNo() + "'";
    	if(!startTime.equals("")) where = where + " and t_pinChe.startTime like '%" + startTime + "%'";
    	if(!startPlace.equals("")) where = where + " and t_pinChe.startPlace like '%" + startPlace + "%'";
    	if(!endPlace.equals("")) where = where + " and t_pinChe.endPlace like '%" + endPlace + "%'";
    	int startIndex = (currentPage-1) * this.rows;
    	return pinCheMapper.queryPinChe(where, startIndex, this.rows);
    }
    
    
    /*用户按照查询条件分页查询自己发布的拼车记录*/
    public ArrayList<PinChe> queryPinChe(String userName,String startTime,String startPlace,String endPlace,int currentPage) throws Exception { 
     	String where = "where 1=1";
    	if(!userName.equals("")) where = where + " and t_userInfo.user_name = '" + userName + "'";
    	if(!startTime.equals("")) where = where + " and t_pinChe.startTime like '%" + startTime + "%'";
    	if(!startPlace.equals("")) where = where + " and t_pinChe.startPlace like '%" + startPlace + "%'";
    	if(!endPlace.equals("")) where = where + " and t_pinChe.endPlace like '%" + endPlace + "%'";
    	int startIndex = (currentPage-1) * this.rows;
    	return pinCheMapper.queryPinChe(where, startIndex, this.rows);
    }
    

    /*按照查询条件查询所有记录*/
    public ArrayList<PinChe> queryPinChe(CarInfo carObj,String startTime,String startPlace,String endPlace) throws Exception  { 
     	String where = "where 1=1";
    	if(null != carObj &&  carObj.getCarNo() != null && !carObj.getCarNo().equals(""))  where += " and t_pinChe.carObj='" + carObj.getCarNo() + "'";
    	if(!startTime.equals("")) where = where + " and t_pinChe.startTime like '%" + startTime + "%'";
    	if(!startPlace.equals("")) where = where + " and t_pinChe.startPlace like '%" + startPlace + "%'";
    	if(!endPlace.equals("")) where = where + " and t_pinChe.endPlace like '%" + endPlace + "%'";
    	return pinCheMapper.queryPinCheList(where);
    }

    /*查询所有拼车记录*/
    public ArrayList<PinChe> queryAllPinChe()  throws Exception {
        return pinCheMapper.queryPinCheList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(CarInfo carObj,String startTime,String startPlace,String endPlace) throws Exception {
     	String where = "where 1=1";
    	if(null != carObj &&  carObj.getCarNo() != null && !carObj.getCarNo().equals(""))  where += " and t_pinChe.carObj='" + carObj.getCarNo() + "'";
    	if(!startTime.equals("")) where = where + " and t_pinChe.startTime like '%" + startTime + "%'";
    	if(!startPlace.equals("")) where = where + " and t_pinChe.startPlace like '%" + startPlace + "%'";
    	if(!endPlace.equals("")) where = where + " and t_pinChe.endPlace like '%" + endPlace + "%'";
        recordNumber = pinCheMapper.queryPinCheCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }
    
    
    
    /*用户自己的拼车信息当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(String userName,String startTime,String startPlace,String endPlace) throws Exception {
     	String where = "where 1=1";
     	if(!userName.equals("")) where = where + " and t_userInfo.user_name = '" + userName + "'";
    	if(!startTime.equals("")) where = where + " and t_pinChe.startTime like '%" + startTime + "%'";
    	if(!startPlace.equals("")) where = where + " and t_pinChe.startPlace like '%" + startPlace + "%'";
    	if(!endPlace.equals("")) where = where + " and t_pinChe.endPlace like '%" + endPlace + "%'";
        recordNumber = pinCheMapper.queryPinCheCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }
    
    

    /*根据主键获取拼车记录*/
    public PinChe getPinChe(int pincheId) throws Exception  {
        PinChe pinChe = pinCheMapper.getPinChe(pincheId);
        return pinChe;
    }

    /*更新拼车记录*/
    public void updatePinChe(PinChe pinChe) throws Exception {
        pinCheMapper.updatePinChe(pinChe);
    }

    /*删除一条拼车记录*/
    public void deletePinChe (int pincheId) throws Exception {
        pinCheMapper.deletePinChe(pincheId);
    }

    /*删除多条拼车信息*/
    public int deletePinChes (String pincheIds) throws Exception {
    	String _pincheIds[] = pincheIds.split(",");
    	for(String _pincheId: _pincheIds) {
    		pinCheMapper.deletePinChe(Integer.parseInt(_pincheId));
    	}
    	return _pincheIds.length;
    }
}
