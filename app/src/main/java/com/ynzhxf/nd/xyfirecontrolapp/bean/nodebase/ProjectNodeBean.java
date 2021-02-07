package com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目信息实体
 * Created by nd on 2018-07-15.
 */

public class ProjectNodeBean extends BaseNodeBean {


    /**
     * 项目地址
     */
    private String Address;

    /**
     * 开始经度
     */
    private double StartLng;

    /**
     * 结束经度
     */
    private double EndLng;

    /**
     * 开始纬度
     */
    private double StartLat;

    /**
     * 结束纬度（百度地图坐标系）
     */
    private double EndLat;

    /**
     * 场所类型
     */
    private String PlaceTypeID;


    /**
     * 是否处于维保状态
     */
    private boolean IsRepair;
    /**
     * 项目图标
     */
    private String ProjectIcon;

    /**
     * 维保状态翻译
     */
    private String TrasentIsRepair;

    /**
     * 消防接管状态
     */
    private int FireControlTake;

    /**
     * 消防接管状态翻译
     */
    private String TrasentFireTake;

    /**
     * 通讯状态
     */
    public int ConnectionState;

    /**
     * 通讯状态翻译
     */
    private String TrasentConnection;

    /**
     * 额外要携带的数据
     */
    private List<String> extraData;

    /**
     * 是否显示实时视频和巡检自定义任务列表按钮
     */

    private boolean RealVideoShow;

    private int FireInspectUserType;

    public boolean isRealVideoShow() {
        return RealVideoShow;
    }

    public void setRealVideoShow(boolean realVideoShow) {
        RealVideoShow = realVideoShow;
    }

    public int getFireInspectUserType() {
        return FireInspectUserType;
    }

    public void setFireInspectUserType(int fireInspectUserType) {
        FireInspectUserType = fireInspectUserType;
    }

    public ProjectNodeBean() {
        extraData = new ArrayList<>();
        this.ConnectionState = -11111;
        this.FireControlTake = -11111;
        this.TrasentIsRepair = "获取中...";
        this.TrasentIsRepair = "获取中...";
        this.TrasentConnection = "获取中...";
    }

    public List<String> getExtraData() {
        return extraData;
    }

    public void setExtraData(List<String> extraData) {
        this.extraData = extraData;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public double getStartLng() {
        return StartLng;
    }

    public void setStartLng(double startLng) {
        StartLng = startLng;
    }

    public double getEndLng() {
        return EndLng;
    }

    public void setEndLng(double endLng) {
        EndLng = endLng;
    }

    public double getStartLat() {
        return StartLat;
    }

    public void setStartLat(double startLat) {
        StartLat = startLat;
    }

    public double getEndLat() {
        return EndLat;
    }

    public void setEndLat(double endLat) {
        EndLat = endLat;
    }

    public String getPlaceTypeID() {
        return PlaceTypeID;
    }

    public void setPlaceTypeID(String placeTypeID) {
        PlaceTypeID = placeTypeID;
    }

    public boolean isRepair() {
        return IsRepair;
    }

    public void setRepair(boolean repair) {
        IsRepair = repair;
    }

    public String getProjectIcon() {
        return ProjectIcon;
    }

    public void setProjectIcon(String projectIcon) {
        ProjectIcon = projectIcon;
    }

    public String getTrasentIsRepair() {
        return TrasentIsRepair;
    }

    public int getConnectionState() {
        return ConnectionState;
    }

    public void setConnectionState(int connectionState) {
        ConnectionState = connectionState;
    }

    public void setTrasentIsRepair(String trasentIsRepair) {
        TrasentIsRepair = trasentIsRepair;
    }

    public int getFireControlTake() {
        return FireControlTake;
    }

    public void setFireControlTake(int fireControlTake) {
        FireControlTake = fireControlTake;
    }

    public String getTrasentFireTake() {
        return TrasentFireTake;
    }

    public void setTrasentFireTake(String trasentFireTake) {
        TrasentFireTake = trasentFireTake;
    }

    public String getTrasentConnection() {
        return TrasentConnection;
    }

    public void setTrasentConnection(String trasentConnection) {
        TrasentConnection = trasentConnection;
    }
}
