package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class CarInfo {
    /*车牌号*/
    @NotEmpty(message="车牌号不能为空")
    private String carNo;
    public String getCarNo(){
        return carNo;
    }
    public void setCarNo(String carNo){
        this.carNo = carNo;
    }

    /*车型*/
    @NotEmpty(message="车型不能为空")
    private String chexing;
    public String getChexing() {
        return chexing;
    }
    public void setChexing(String chexing) {
        this.chexing = chexing;
    }

    /*车辆照片*/
    private String carPhoto;
    public String getCarPhoto() {
        return carPhoto;
    }
    public void setCarPhoto(String carPhoto) {
        this.carPhoto = carPhoto;
    }

    /*购买年份*/
    @NotNull(message="必须输入购买年份")
    private Integer buyYear;
    public Integer getBuyYear() {
        return buyYear;
    }
    public void setBuyYear(Integer buyYear) {
        this.buyYear = buyYear;
    }

    /*驾驶证*/
    private String jiaShiZheng;
    public String getJiaShiZheng() {
        return jiaShiZheng;
    }
    public void setJiaShiZheng(String jiaShiZheng) {
        this.jiaShiZheng = jiaShiZheng;
    }

    /*学生证*/
    private String xueShenZheng;
    public String getXueShenZheng() {
        return xueShenZheng;
    }
    public void setXueShenZheng(String xueShenZheng) {
        this.xueShenZheng = xueShenZheng;
    }

    /*车辆描述*/
    private String carDesc;
    public String getCarDesc() {
        return carDesc;
    }
    public void setCarDesc(String carDesc) {
        this.carDesc = carDesc;
    }

    /*所属学生*/
    private UserInfo userObj;
    public UserInfo getUserObj() {
        return userObj;
    }
    public void setUserObj(UserInfo userObj) {
        this.userObj = userObj;
    }

    /*审核状态*/
    @NotEmpty(message="审核状态不能为空")
    private String shenHeState;
    public String getShenHeState() {
        return shenHeState;
    }
    public void setShenHeState(String shenHeState) {
        this.shenHeState = shenHeState;
    }

    /*提交时间*/
    @NotEmpty(message="提交时间不能为空")
    private String addTime;
    public String getAddTime() {
        return addTime;
    }
    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonCarInfo=new JSONObject(); 
		jsonCarInfo.accumulate("carNo", this.getCarNo());
		jsonCarInfo.accumulate("chexing", this.getChexing());
		jsonCarInfo.accumulate("carPhoto", this.getCarPhoto());
		jsonCarInfo.accumulate("buyYear", this.getBuyYear());
		jsonCarInfo.accumulate("jiaShiZheng", this.getJiaShiZheng());
		jsonCarInfo.accumulate("xueShenZheng", this.getXueShenZheng());
		jsonCarInfo.accumulate("carDesc", this.getCarDesc());
		jsonCarInfo.accumulate("userObj", this.getUserObj().getName());
		jsonCarInfo.accumulate("userObjPri", this.getUserObj().getUser_name());
		jsonCarInfo.accumulate("shenHeState", this.getShenHeState());
		jsonCarInfo.accumulate("addTime", this.getAddTime().length()>19?this.getAddTime().substring(0,19):this.getAddTime());
		return jsonCarInfo;
    }}