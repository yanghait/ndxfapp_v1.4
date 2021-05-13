package com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SmartPowerInfo {
    @SerializedName("Id")
    String Id = "";
    @SerializedName("Name")
    String Name = "";//名称
    @SerializedName("Voltage")
    String Voltage = "";//电压
    @SerializedName("Current")
    String Current = "";//电流
    @SerializedName("Power")
    String Power = "";//功率
    @SerializedName("LeakageCurrent")
    String LeakageCurrent = "";//漏电电流
    @SerializedName("TotalElectricity")
    String TotalElectricity = "";//总电量
    @SerializedName("Temperature")
    String Temperature = "";//温度
    @SerializedName("SwitchStatus")
    String SwitchStatus = "";
    @SerializedName("IsSwitchStatus")
    boolean IsSwitchStatus = false;//分合状态
    @SerializedName("OnlineStatus")
    String OnlineStatus = "";
    @SerializedName("IsOnlineStatus")
    boolean IsOnlineStatus = false;//在线状态
    @SerializedName("FireAlarmStatus")
    String FireAlarmStatus = "";
    @SerializedName("IsFireAlarmStatus")
    boolean IsFireAlarmStatus = false;//打火报警状态
    @SerializedName("ShortAlarmStatus")
    String ShortAlarmStatus = "";
    @SerializedName("IsShortAlarmStatus")
    boolean IsShortAlarmStatus = false;//打火报警状态
    @SerializedName("OverCurrentAlarmStatus")
    String OverCurrentAlarmStatus = "";
    @SerializedName("IsOverCurrentAlarmStatus")
    boolean IsOverCurrentAlarmStatus = false;//过流报警状态
    @SerializedName("OverVoltageAlarmStatus")
    String OverVoltageAlarmStatus = "";
    @SerializedName("IsOverVoltageAlarmStatus")
    boolean IsOverVoltageAlarmStatus = false;//过压报警状态
    @SerializedName("OverloadAlarmStatus")
    String OverloadAlarmStatus = "";
    @SerializedName("IsOverloadAlarmStatus")
    boolean IsOverloadAlarmStatus = false;//过载报警状态
    @SerializedName("TemperatureAlarmStatus")
    String TemperatureAlarmStatus = "";
    @SerializedName("IsTemperatureAlarmStatus")
    boolean IsTemperatureAlarmStatus = false;//温度报警状态
    @SerializedName("UndervoltageAlarmStatus")
    String UndervoltageAlarmStatus = "";
    @SerializedName("IsUndervoltageAlarmStatus")
    boolean IsUndervoltageAlarmStatus = false;//欠压报警状态
    @SerializedName("VmSmartPowerChildren")
    List<SmartPowerInfo> VmSmartPowerChildren = null;

    public String getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public String getVoltage() {
        return Voltage;
    }

    public String getCurrent() {
        return Current;
    }

    public String getPower() {
        return Power;
    }

    public String getLeakageCurrent() {
        return LeakageCurrent;
    }

    public String getTotalElectricity() {
        return TotalElectricity;
    }

    public String getTemperature() {
        return Temperature;
    }

    public String getSwitchStatus() {
        return SwitchStatus;
    }

    public boolean isSwitchStatus() {
        return IsSwitchStatus;
    }

    public String getOnlineStatus() {
        return OnlineStatus;
    }

    public boolean isOnlineStatus() {
        return IsOnlineStatus;
    }

    public String getFireAlarmStatus() {
        return FireAlarmStatus;
    }

    public boolean isFireAlarmStatus() {
        return IsFireAlarmStatus;
    }

    public String getShortAlarmStatus() {
        return ShortAlarmStatus;
    }

    public boolean isShortAlarmStatus() {
        return IsShortAlarmStatus;
    }

    public String getOverCurrentAlarmStatus() {
        return OverCurrentAlarmStatus;
    }

    public boolean isOverCurrentAlarmStatus() {
        return IsOverCurrentAlarmStatus;
    }

    public String getOverVoltageAlarmStatus() {
        return OverVoltageAlarmStatus;
    }

    public boolean isOverVoltageAlarmStatus() {
        return IsOverVoltageAlarmStatus;
    }

    public String getOverloadAlarmStatus() {
        return OverloadAlarmStatus;
    }

    public boolean isOverloadAlarmStatus() {
        return IsOverloadAlarmStatus;
    }

    public String getTemperatureAlarmStatus() {
        return TemperatureAlarmStatus;
    }

    public boolean isTemperatureAlarmStatus() {
        return IsTemperatureAlarmStatus;
    }

    public String getUndervoltageAlarmStatus() {
        return UndervoltageAlarmStatus;
    }

    public boolean isUndervoltageAlarmStatus() {
        return IsUndervoltageAlarmStatus;
    }

    public List getVmSmartPowerChildren() {
        return VmSmartPowerChildren;
    }
}
