package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.PinChe;
import com.chengxusheji.po.UserInfo;
import com.chengxusheji.po.PinCheOrder;

import com.chengxusheji.mapper.PinCheMapper;
import com.chengxusheji.mapper.PinCheOrderMapper;
@Service
public class PinCheOrderService {

	@Resource PinCheOrderMapper pinCheOrderMapper;
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

    /*添加拼车订单记录*/
    public void addPinCheOrder(PinCheOrder pinCheOrder) throws Exception {
    	pinCheOrderMapper.addPinCheOrder(pinCheOrder);
    }

    /*按照查询条件分页查询拼车订单记录*/
    public ArrayList<PinCheOrder> queryPinCheOrder(PinChe pinCheObj,UserInfo userObj,String orderTime,int currentPage) throws Exception { 
     	String where = "where 1=1";
    	if(null != pinCheObj && pinCheObj.getPincheId()!= null && pinCheObj.getPincheId()!= 0)  where += " and t_pinCheOrder.pinCheObj=" + pinCheObj.getPincheId();
    	if(null != userObj &&  userObj.getUser_name() != null  && !userObj.getUser_name().equals(""))  where += " and t_pinCheOrder.userObj='" + userObj.getUser_name() + "'";
    	if(!orderTime.equals("")) where = where + " and t_pinCheOrder.orderTime like '%" + orderTime + "%'";
    	int startIndex = (currentPage-1) * this.rows;
    	return pinCheOrderMapper.queryPinCheOrder(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<PinCheOrder> queryPinCheOrder(PinChe pinCheObj,UserInfo userObj,String orderTime) throws Exception  { 
     	String where = "where 1=1";
    	if(null != pinCheObj && pinCheObj.getPincheId()!= null && pinCheObj.getPincheId()!= 0)  where += " and t_pinCheOrder.pinCheObj=" + pinCheObj.getPincheId();
    	if(null != userObj &&  userObj.getUser_name() != null && !userObj.getUser_name().equals(""))  where += " and t_pinCheOrder.userObj='" + userObj.getUser_name() + "'";
    	if(!orderTime.equals("")) where = where + " and t_pinCheOrder.orderTime like '%" + orderTime + "%'";
    	return pinCheOrderMapper.queryPinCheOrderList(where);
    }

    /*查询所有拼车订单记录*/
    public ArrayList<PinCheOrder> queryAllPinCheOrder()  throws Exception {
        return pinCheOrderMapper.queryPinCheOrderList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(PinChe pinCheObj,UserInfo userObj,String orderTime) throws Exception {
     	String where = "where 1=1";
    	if(null != pinCheObj && pinCheObj.getPincheId()!= null && pinCheObj.getPincheId()!= 0)  where += " and t_pinCheOrder.pinCheObj=" + pinCheObj.getPincheId();
    	if(null != userObj &&  userObj.getUser_name() != null && !userObj.getUser_name().equals(""))  where += " and t_pinCheOrder.userObj='" + userObj.getUser_name() + "'";
    	if(!orderTime.equals("")) where = where + " and t_pinCheOrder.orderTime like '%" + orderTime + "%'";
        recordNumber = pinCheOrderMapper.queryPinCheOrderCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取拼车订单记录*/
    public PinCheOrder getPinCheOrder(int orderId) throws Exception  {
        PinCheOrder pinCheOrder = pinCheOrderMapper.getPinCheOrder(orderId);
        return pinCheOrder;
    }

    /*更新拼车订单记录*/
    public void updatePinCheOrder(PinCheOrder pinCheOrder) throws Exception {
        pinCheOrderMapper.updatePinCheOrder(pinCheOrder);
    }

    /*删除一条拼车订单记录*/
    public void deletePinCheOrder (int orderId) throws Exception {
        pinCheOrderMapper.deletePinCheOrder(orderId);
    }

    /*删除多条拼车订单信息*/
    public int deletePinCheOrders (String orderIds) throws Exception {
    	String _orderIds[] = orderIds.split(",");
    	for(String _orderId: _orderIds) {
    		PinCheOrder pinCheOrder = pinCheOrderMapper.getPinCheOrder(Integer.parseInt(_orderId));
    		int personNum = pinCheOrder.getPersonNum();
    		
    		//删除订单之前需要更新已拼人数 
    		PinChe pinChe = pinCheMapper.getPinChe(pinCheOrder.getPinCheObj().getPincheId());
    		pinChe.setHaveNum(pinChe.getHaveNum() - personNum);
    		pinCheMapper.updatePinChe(pinChe);
    		
    		//然后删除拼车订单
    		pinCheOrderMapper.deletePinCheOrder(Integer.parseInt(_orderId));
    		
    		
    	}
    	return _orderIds.length;
    }
}
