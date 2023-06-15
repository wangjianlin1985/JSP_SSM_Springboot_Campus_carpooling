package com.chengxusheji.mapper;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;
import com.chengxusheji.po.PinCheOrder;

public interface PinCheOrderMapper {
	/*添加拼车订单信息*/
	public void addPinCheOrder(PinCheOrder pinCheOrder) throws Exception;

	/*按照查询条件分页查询拼车订单记录*/
	public ArrayList<PinCheOrder> queryPinCheOrder(@Param("where") String where,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize) throws Exception;

	/*按照查询条件查询所有拼车订单记录*/
	public ArrayList<PinCheOrder> queryPinCheOrderList(@Param("where") String where) throws Exception;

	/*按照查询条件的拼车订单记录数*/
	public int queryPinCheOrderCount(@Param("where") String where) throws Exception; 

	/*根据主键查询某条拼车订单记录*/
	public PinCheOrder getPinCheOrder(int orderId) throws Exception;

	/*更新拼车订单记录*/
	public void updatePinCheOrder(PinCheOrder pinCheOrder) throws Exception;

	/*删除拼车订单记录*/
	public void deletePinCheOrder(int orderId) throws Exception;

}
