package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class PinChe {
    /*记录id*/
    private Integer pincheId;
    public Integer getPincheId(){
        return pincheId;
    }
    public void setPincheId(Integer pincheId){
        this.pincheId = pincheId;
    }

    /*车辆信息*/
    private CarInfo carObj;
    public CarInfo getCarObj() {
        return carObj;
    }
    public void setCarObj(CarInfo carObj) {
        this.carObj = carObj;
    }

    /*拼车图片*/
    private String pinChePhoto;
    public String getPinChePhoto() {
        return pinChePhoto;
    }
    public void setPinChePhoto(String pinChePhoto) {
        this.pinChePhoto = pinChePhoto;
    }

    /*出发时间*/
    @NotEmpty(message="出发时间不能为空")
    private String startTime;
    public String getStartTime() {
        return startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /*车出发位置*/
    @NotEmpty(message="车出发位置不能为空")
    private String startPlace;
    public String getStartPlace() {
        return startPlace;
    }
    public void setStartPlace(String startPlace) {
        this.startPlace = startPlace;
    }

    /*目的地*/
    @NotEmpty(message="目的地不能为空")
    private String endPlace;
    public String getEndPlace() {
        return endPlace;
    }
    public void setEndPlace(String endPlace) {
        this.endPlace = endPlace;
    }

    /*价格*/
    @NotNull(message="必须输入价格")
    private Float price;
    public Float getPrice() {
        return price;
    }
    public void setPrice(Float price) {
        this.price = price;
    }

    /*可拼人数*/
    @NotNull(message="必须输入可拼人数")
    private Integer totalNum;
    public Integer getTotalNum() {
        return totalNum;
    }
    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    /*已拼人数*/
    @NotNull(message="必须输入已拼人数")
    private Integer haveNum;
    public Integer getHaveNum() {
        return haveNum;
    }
    public void setHaveNum(Integer haveNum) {
        this.haveNum = haveNum;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonPinChe=new JSONObject(); 
		jsonPinChe.accumulate("pincheId", this.getPincheId());
		jsonPinChe.accumulate("carObj", this.getCarObj().getCarNo());
		jsonPinChe.accumulate("carObjPri", this.getCarObj().getCarNo());
		jsonPinChe.accumulate("pinChePhoto", this.getPinChePhoto());
		jsonPinChe.accumulate("startTime", this.getStartTime().length()>19?this.getStartTime().substring(0,19):this.getStartTime());
		jsonPinChe.accumulate("startPlace", this.getStartPlace());
		jsonPinChe.accumulate("endPlace", this.getEndPlace());
		jsonPinChe.accumulate("price", this.getPrice());
		jsonPinChe.accumulate("totalNum", this.getTotalNum());
		jsonPinChe.accumulate("haveNum", this.getHaveNum());
		return jsonPinChe;
    }}