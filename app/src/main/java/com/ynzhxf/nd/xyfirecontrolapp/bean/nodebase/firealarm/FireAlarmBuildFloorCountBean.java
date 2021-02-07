package com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.firealarm;

import com.ynzhxf.nd.xyfirecontrolapp.bean.BaseDataBean;

/**
 * 火灾报警楼层统计对象 (换用做统计信息)
 * Created by nd on 2018-07-25.
 */

public class FireAlarmBuildFloorCountBean extends BaseDataBean{

    /// <summary>
    /// 系统ID
    /// </summary>
    private String ProjectSystemID ;

    /// <summary>
    /// 火灾报警主机ID
    /// </summary>
    private String FireHostId ;

    //火灾报警主机名称
    private String FireHostName;

    //主机状态
    private int HostState;



    //主机状态翻译
    private String HostStateStr;

    //串口状态
    private String HostPortStateStr;

    /// <summary>
    /// 建筑物名称
    /// </summary>
    private String BuildName;
    /// <summary>
    /// 楼层
    /// </summary>
    private int floor;

    //当前楼层数量
    private int floorCount;

    /// <summary>
    /// 当前楼层设备总数
    /// </summary>
    private int EquipentCount;


    /// <summary>
    /// 当前楼层正常设备总数
    /// </summary>
    private int NormalCount ;

    /// <summary>
    /// 当前楼层动作总数
    /// </summary>
    private int ActionCount;
    /// <summary>
    /// 当前层设备故障数量
    /// </summary>
    private int FaultCount;

    /// <summary>
    /// 当前楼层设备屏蔽数量
    /// </summary>
    private int CloseCount;

    /// <summary>
    /// 当前楼层监管设备数量
    /// </summary>
    public int ManageCount;

    /// <summary>
    /// 当前楼层启动设备数量
    /// </summary>
    private int OpenCount;

    /// <summary>
    /// 当前楼层屏蔽设备数量
    /// </summary>
    private int FeedCount;

    /// <summary>
    /// 当前楼层火警设备数量
    /// </summary>
    private int FireCount;

    /// <summary>
    /// 当前楼层设备掉线数量
    /// </summary>
    private int OutLineCount;

    public String getProjectSystemID() {
        return ProjectSystemID;
    }

    public void setProjectSystemID(String projectSystemID) {
        ProjectSystemID = projectSystemID;
    }

    public String getFireHostId() {
        return FireHostId;
    }

    public void setFireHostId(String fireHostId) {
        FireHostId = fireHostId;
    }

    public String getBuildName() {
        return BuildName;
    }

    public void setBuildName(String buildName) {
        BuildName = buildName;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getEquipentCount() {
        return EquipentCount;
    }

    public void setEquipentCount(int equipentCount) {
        EquipentCount = equipentCount;
    }

    public int getNormalCount() {
        return NormalCount;
    }

    public void setNormalCount(int normalCount) {
        NormalCount = normalCount;
    }

    public int getActionCount() {
        return ActionCount;
    }

    public void setActionCount(int actionCount) {
        ActionCount = actionCount;
    }

    public int getFaultCount() {
        return FaultCount;
    }

    public void setFaultCount(int faultCount) {
        FaultCount = faultCount;
    }

    public int getCloseCount() {
        return CloseCount;
    }

    public void setCloseCount(int closeCount) {
        CloseCount = closeCount;
    }

    public int getManageCount() {
        return ManageCount;
    }

    public void setManageCount(int manageCount) {
        ManageCount = manageCount;
    }

    public int getOpenCount() {
        return OpenCount;
    }

    public void setOpenCount(int openCount) {
        OpenCount = openCount;
    }

    public int getFeedCount() {
        return FeedCount;
    }

    public void setFeedCount(int feedCount) {
        FeedCount = feedCount;
    }

    public String getFireHostName() {
        return FireHostName;
    }

    public void setFireHostName(String fireHostName) {
        FireHostName = fireHostName;
    }

    public int getFireCount() {
        return FireCount;
    }

    public void setFireCount(int fireCount) {
        FireCount = fireCount;
    }

    public int getOutLineCount() {
        return OutLineCount;
    }

    public void setOutLineCount(int outLineCount) {
        OutLineCount = outLineCount;
    }
    public int getHostState() {
        return HostState;
    }

    public void setHostState(int hostState) {
        HostState = hostState;
    }

    public String getHostStateStr() {
        return HostStateStr;
    }

    public void setHostStateStr(String hostStateStr) {
        HostStateStr = hostStateStr;
    }

    public String getHostPortStateStr() {
        return HostPortStateStr;
    }

    public void setHostPortStateStr(String hostPortStateStr) {
        HostPortStateStr = hostPortStateStr;
    }

    public int getFloorCount() {
        return floorCount;
    }

    public void setFloorCount(int floorCount) {
        this.floorCount = floorCount;
    }
}
