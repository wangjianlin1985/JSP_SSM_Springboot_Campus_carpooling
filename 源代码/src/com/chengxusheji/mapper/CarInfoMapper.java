package com.chengxusheji.mapper;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;
import com.chengxusheji.po.CarInfo;

public interface CarInfoMapper {
	/*添加小车信息*/
	public void addCarInfo(CarInfo carInfo) throws Exception;

	/*按照查询条件分页查询小车记录*/
	public ArrayList<CarInfo> queryCarInfo(@Param("where") String where,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize) throws Exception;

	/*按照查询条件查询所有小车记录*/
	public ArrayList<CarInfo> queryCarInfoList(@Param("where") String where) throws Exception;

	/*按照查询条件的小车记录数*/
	public int queryCarInfoCount(@Param("where") String where) throws Exception; 

	/*根据主键查询某条小车记录*/
	public CarInfo getCarInfo(String carNo) throws Exception;

	/*更新小车记录*/
	public void updateCarInfo(CarInfo carInfo) throws Exception;

	/*删除小车记录*/
	public void deleteCarInfo(String carNo) throws Exception;

}
