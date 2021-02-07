package com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.firealarm;

import java.io.Serializable;

/**
 * 火灾报警探头点信息
 * Created by nd on 2018-07-26.
 */

public class FireAlarmPointBean implements Serializable , Cloneable{

    //唯一ID,自动生成
    private String Guid ;

    //设备位置描述
    private String FullPositionName;

    //探头状态
    private int State;

    //翻译后的状态
    private String TransformState;

    //建筑名称
    private String Building;

    //楼层名称
    private String FloorStr;

    private String UserID;

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getGuid() {
        return Guid;
    }

    public void setGuid(String guid) {
        Guid = guid;
    }

    public String getFullPositionName() {
        return FullPositionName;
    }

    public void setFullPositionName(String fullPositionName) {
        FullPositionName = fullPositionName;
    }

    public int getState() {
        return State;
    }

    public void setState(int state) {
        State = state;
    }

    public String getTransformState() {
        return TransformState;
    }

    public void setTransformState(String transformState) {
        TransformState = transformState;
    }

    public String getBuilding() {
        return Building;
    }

    public void setBuilding(String building) {
        Building = building;
    }

    public String getFloorStr() {
        return FloorStr;
    }

    public void setFloorStr(String floorStr) {
        FloorStr = floorStr;
    }
}
