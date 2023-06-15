package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class PinCheOrder {
    /*订单id*/
    private Integer orderId;
    public Integer getOrderId(){
        return orderId;
    }
    public void setOrderId(Integer orderId){
        this.orderId = orderId;
    }

    /*拼车id*/
    private PinChe pinCheObj;
    public PinChe getPinCheObj() {
        return pinCheObj;
    }
    public void setPinCheObj(PinChe pinCheObj) {
        this.pinCheObj = pinCheObj;
    }

    /*用户*/
    private UserInfo userObj;
    public UserInfo getUserObj() {
        return userObj;
    }
    public void setUserObj(UserInfo userObj) {
        this.userObj = userObj;
    }

    /*拼车人数*/
    @NotNull(message="必须输入拼车人数")
    private Integer personNum;
    public Integer getPersonNum() {
        return personNum;
    }
    public void setPersonNum(Integer personNum) {
        this.personNum = personNum;
    }

    /*下单时间*/
    @NotEmpty(message="下单时间不能为空")
    private String orderTime;
    public String getOrderTime() {
        return orderTime;
    }
    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonPinCheOrder=new JSONObject(); 
		jsonPinCheOrder.accumulate("orderId", this.getOrderId());
		jsonPinCheOrder.accumulate("pinCheObj", this.getPinCheObj().getPincheId());
		jsonPinCheOrder.accumulate("pinCheObjPri", this.getPinCheObj().getPincheId());
		jsonPinCheOrder.accumulate("userObj", this.getUserObj().getName());
		jsonPinCheOrder.accumulate("userObjPri", this.getUserObj().getUser_name());
		jsonPinCheOrder.accumulate("personNum", this.getPersonNum());
		jsonPinCheOrder.accumulate("orderTime", this.getOrderTime().length()>19?this.getOrderTime().substring(0,19):this.getOrderTime());
		return jsonPinCheOrder;
    }}