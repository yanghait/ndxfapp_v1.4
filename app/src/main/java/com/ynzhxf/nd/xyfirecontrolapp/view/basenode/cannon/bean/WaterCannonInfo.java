package com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WaterCannonInfo implements Serializable {
    @SerializedName("ID")
    String Id = "";
    @SerializedName("Name")
    String name = "";
    @SerializedName("SkyLightStatus")
    int SkyLightStatus = 0;//天窗状态0.关闭 1.开启
    @SerializedName("WaterValveStatus")
    int WaterValveStatus = 0;//水阀状态
    @SerializedName("FlatAngle")
    String FlatAngle = "";//水平角度
    @SerializedName("ElevationAngle")
    String ElevationAngle = "";//仰角

    public String getId() {
        return Id;
    }

    public String getName() {
        return name;
    }

    public int getSkyLightStatus() {
        return SkyLightStatus;
    }

    public int getWaterValveStatus() {
        return WaterValveStatus;
    }

    public String getFlatAngle() {
        return FlatAngle;
    }

    public String getElevationAngle() {
        return ElevationAngle;
    }
}
