package com.chengxusheji.mapper;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;
import com.chengxusheji.po.PinChe;

public interface PinCheMapper {
	/*添加拼车信息*/
	public void addPinChe(PinChe pinChe) throws Exception;

	/*按照查询条件分页查询拼车记录*/
	public ArrayList<PinChe> queryPinChe(@Param("where") String where,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize) throws Exception;

	/*按照查询条件查询所有拼车记录*/
	public ArrayList<PinChe> queryPinCheList(@Param("where") String where) throws Exception;

	/*按照查询条件的拼车记录数*/
	public int queryPinCheCount(@Param("where") String where) throws Exception; 

	/*根据主键查询某条拼车记录*/
	public PinChe getPinChe(int pincheId) throws Exception;

	/*更新拼车记录*/
	public void updatePinChe(PinChe pinChe) throws Exception;

	/*删除拼车记录*/
	public void deletePinChe(int pincheId) throws Exception;

}
