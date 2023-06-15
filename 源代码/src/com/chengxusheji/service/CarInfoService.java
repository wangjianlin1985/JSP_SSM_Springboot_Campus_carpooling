package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.UserInfo;
import com.chengxusheji.po.CarInfo;

import com.chengxusheji.mapper.CarInfoMapper;
@Service
public class CarInfoService {

	@Resource CarInfoMapper carInfoMapper;
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

    /*添加小车记录*/
    public void addCarInfo(CarInfo carInfo) throws Exception {
    	carInfoMapper.addCarInfo(carInfo);
    }

    /*按照查询条件分页查询小车记录*/
    public ArrayList<CarInfo> queryCarInfo(String carNo,String chexing,UserInfo userObj,String shenHeState,String addTime,int currentPage) throws Exception { 
     	String where = "where 1=1";
    	if(!carNo.equals("")) where = where + " and t_carInfo.carNo like '%" + carNo + "%'";
    	if(!chexing.equals("")) where = where + " and t_carInfo.chexing like '%" + chexing + "%'";
    	if(null != userObj &&  userObj.getUser_name() != null  && !userObj.getUser_name().equals(""))  where += " and t_carInfo.userObj='" + userObj.getUser_name() + "'";
    	if(!shenHeState.equals("")) where = where + " and t_carInfo.shenHeState like '%" + shenHeState + "%'";
    	if(!addTime.equals("")) where = where + " and t_carInfo.addTime like '%" + addTime + "%'";
    	int startIndex = (currentPage-1) * this.rows;
    	return carInfoMapper.queryCarInfo(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<CarInfo> queryCarInfo(String carNo,String chexing,UserInfo userObj,String shenHeState,String addTime) throws Exception  { 
     	String where = "where 1=1";
    	if(!carNo.equals("")) where = where + " and t_carInfo.carNo like '%" + carNo + "%'";
    	if(!chexing.equals("")) where = where + " and t_carInfo.chexing like '%" + chexing + "%'";
    	if(null != userObj &&  userObj.getUser_name() != null && !userObj.getUser_name().equals(""))  where += " and t_carInfo.userObj='" + userObj.getUser_name() + "'";
    	if(!shenHeState.equals("")) where = where + " and t_carInfo.shenHeState like '%" + shenHeState + "%'";
    	if(!addTime.equals("")) where = where + " and t_carInfo.addTime like '%" + addTime + "%'";
    	return carInfoMapper.queryCarInfoList(where);
    }

    /*查询所有小车记录*/
    public ArrayList<CarInfo> queryAllCarInfo()  throws Exception {
        return carInfoMapper.queryCarInfoList("where 1=1");
    }
    
    /*查询所有小车记录*/
    public ArrayList<CarInfo> queryUserAllCarInfo(String userName)  throws Exception {
        return carInfoMapper.queryCarInfoList("where 1=1 and t_carInfo.userObj='" + userName + "' and t_carInfo.shenHeState='已审核'");
    }
    

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(String carNo,String chexing,UserInfo userObj,String shenHeState,String addTime) throws Exception {
     	String where = "where 1=1";
    	if(!carNo.equals("")) where = where + " and t_carInfo.carNo like '%" + carNo + "%'";
    	if(!chexing.equals("")) where = where + " and t_carInfo.chexing like '%" + chexing + "%'";
    	if(null != userObj &&  userObj.getUser_name() != null && !userObj.getUser_name().equals(""))  where += " and t_carInfo.userObj='" + userObj.getUser_name() + "'";
    	if(!shenHeState.equals("")) where = where + " and t_carInfo.shenHeState like '%" + shenHeState + "%'";
    	if(!addTime.equals("")) where = where + " and t_carInfo.addTime like '%" + addTime + "%'";
        recordNumber = carInfoMapper.queryCarInfoCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取小车记录*/
    public CarInfo getCarInfo(String carNo) throws Exception  {
        CarInfo carInfo = carInfoMapper.getCarInfo(carNo);
        return carInfo;
    }

    /*更新小车记录*/
    public void updateCarInfo(CarInfo carInfo) throws Exception {
        carInfoMapper.updateCarInfo(carInfo);
    }

    /*删除一条小车记录*/
    public void deleteCarInfo (String carNo) throws Exception {
        carInfoMapper.deleteCarInfo(carNo);
    }

    /*删除多条小车信息*/
    public int deleteCarInfos (String carNos) throws Exception {
    	String _carNos[] = carNos.split(",");
    	for(String _carNo: _carNos) {
    		carInfoMapper.deleteCarInfo(_carNo);
    	}
    	return _carNos.length;
    }
}
