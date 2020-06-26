package com.example.run.model;

import cn.bmob.v3.BmobObject;

public class Take extends BmobObject {
    private String userId;

    //发货信息
    private String takeAddress;
    private String takeName;
    private String takePhone;
    private Double takeLatitude;
    private Double takeLongitude;

    //收货信息
    private String collectAddress;
    private String collectName;
    private String collectPhone;
    private Double collectLatitude;
    private Double collectLongitude;

    //备注信息
    private String remarks;

    //价格
    private Double money;

    //配送距离
    private Double dist;

    //物品重量
    private Integer weight;

    //1 = 刚下单 //2 = 已接单 3 = 正在配送 4 = 已完成
    private Integer runType;
    //骑手id
    private String riderId;

    public String getUserId() {
        return userId;
    }

    public String getTakeAddress() {
        return takeAddress;
    }

    public String getTakeName() {
        return takeName;
    }

    public String getTakePhone() {
        return takePhone;
    }

    public Double getTakeLatitude() {
        return takeLatitude;
    }

    public Double getTakeLongitude() {
        return takeLongitude;
    }

    public String getCollectAddress() {
        return collectAddress;
    }

    public String getCollectName() {
        return collectName;
    }

    public String getCollectPhone() {
        return collectPhone;
    }

    public Double getCollectLatitude() {
        return collectLatitude;
    }

    public Double getCollectLongitude() {
        return collectLongitude;
    }

    public String getRemarks() {
        return remarks;
    }

    public Double getMoney() {
        return money;
    }

    public Double getDist() {
        return dist;
    }

    public Integer getWeight() {
        return weight;
    }

    public Integer getRunType() {
        return runType;
    }

    public String getRiderId() {
        return riderId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setTakeAddress(String takeAddress) {
        this.takeAddress = takeAddress;
    }

    public void setTakeName(String takeName) {
        this.takeName = takeName;
    }

    public void setTakePhone(String takePhone) {
        this.takePhone = takePhone;
    }

    public void setTakeLatitude(Double takeLatitude) {
        this.takeLatitude = takeLatitude;
    }

    public void setTakeLongitude(Double takeLongitude) {
        this.takeLongitude = takeLongitude;
    }

    public void setCollectAddress(String collectAddress) {
        this.collectAddress = collectAddress;
    }

    public void setCollectName(String collectName) {
        this.collectName = collectName;
    }

    public void setCollectPhone(String collectPhone) {
        this.collectPhone = collectPhone;
    }

    public void setCollectLatitude(Double collectLatitude) {
        this.collectLatitude = collectLatitude;
    }

    public void setCollectLongitude(Double collectLongitude) {
        this.collectLongitude = collectLongitude;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public void setDist(Double dist) {
        this.dist = dist;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public void setRunType(Integer runType) {
        this.runType = runType;
    }

    public void setRiderId(String riderId) {
        this.riderId = riderId;
    }
}
