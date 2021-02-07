package com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase;

/**
 * Created by nd on 2018-07-15.
 */

import com.ynzhxf.nd.xyfirecontrolapp.bean.dict.BaseDictBean;

/**
 * 标签节点
 */
public class LabelNodeBean extends  BaseNodeBean {

    /**
     * 标签类型
     */
    private BaseDictBean TagValueType;

    /**
     * 模拟量最大值
     */
    private Double MaxAnalogValue;

    /**
     * 数据单位
     */
    private BaseDictBean Unit;


    /**
     * 真实值
     */
    private Object RealValue;

    /**
     * 实时偏差率
     */
    private String AlalogOffset;

    /**
     * 标准值高限
     */
    private String StandardHight;

    /**
     * 标准值低限
     */
    private String StandarLower;

    /**
     * 实时数据翻译值
     */
    private String TranslateValue;

    /**
     * 是否处于报警状态
     */
    private boolean isAlarmState;

    /**
     * 报警的类型
     */
    private String nowAlarmType;

    /**
     * 产生报警时候的消息
     */
    private String AlarmMessage;

    /**
     * 标签节点所处位置描述（默认顶级名称加载到县）
     */
    private String AreaName;

    /**
     * 事件类型
     */
    private BaseDictBean EventType;

    /**
     * 报警时候的值
     */
    private String AlarmValue;

    /**
     * /报警时候的偏差值
     */
    private String AlarmOffset;

    /**
     * 报警类型
     */
    private String alarmTypeStr;

    private String OPCTagName;

    private String UnitID;

    private String BoolControlID;

    public String getBoolControlID() {
        return BoolControlID;
    }

    public void setBoolControlID(String boolControlID) {
        BoolControlID = boolControlID;
    }

    public String getUnitID() {
        return UnitID;
    }

    public void setUnitID(String unitID) {
        UnitID = unitID;
    }

    public String getOPCTagName() {
        return OPCTagName;
    }

    public void setOPCTagName(String OPCTagName) {
        this.OPCTagName = OPCTagName;
    }

    private int colorState;

    public int getColorState() {
        return colorState;
    }

    public void setColorState(int colorState) {
        this.colorState = colorState;
    }

    /**
     * 发生事件的时间
     */
    private String EventTimeStr;

    public BaseDictBean getTagValueType() {
        return TagValueType;
    }

    public void setTagValueType(BaseDictBean tagValueType) {
        TagValueType = tagValueType;
    }

    public BaseDictBean getUnit() {
        return Unit;
    }

    public void setUnit(BaseDictBean unit) {
        Unit = unit;
    }

    public Object getRealValue() {
        return RealValue;
    }

    public void setRealValue(Object realValue) {
        RealValue = realValue;
    }

    public String getAlalogOffset() {
        return AlalogOffset;
    }

    public void setAlalogOffset(String alalogOffset) {
        AlalogOffset = alalogOffset;
    }

    public String getStandardHight() {
        return StandardHight;
    }

    public void setStandardHight(String standardHight) {
        StandardHight = standardHight;
    }

    public String getStandarLower() {
        return StandarLower;
    }

    public void setStandarLower(String standarLower) {
        StandarLower = standarLower;
    }

    public String getTranslateValue() {
        return TranslateValue;
    }

    public void setTranslateValue(String translateValue) {
        TranslateValue = translateValue;
    }

    public boolean isAlarmState() {
        return isAlarmState;
    }

    public void setAlarmState(boolean alarmState) {
        isAlarmState = alarmState;
    }

    public String getNowAlarmType() {
        return nowAlarmType;
    }

    public void setNowAlarmType(String nowAlarmType) {
        this.nowAlarmType = nowAlarmType;
    }

    public String getAlarmMessage() {
        return AlarmMessage;
    }

    public void setAlarmMessage(String alarmMessage) {
        AlarmMessage = alarmMessage;
    }

    public String getAreaName() {
        return AreaName;
    }

    public void setAreaName(String areaName) {
        AreaName = areaName;
    }

    public BaseDictBean getEventType() {
        return EventType;
    }

    public void setEventType(BaseDictBean eventType) {
        EventType = eventType;
    }

    public String getAlarmValue() {
        return AlarmValue;
    }

    public void setAlarmValue(String alarmValue) {
        AlarmValue = alarmValue;
    }

    public String getAlarmOffset() {
        return AlarmOffset;
    }

    public void setAlarmOffset(String alarmOffset) {
        AlarmOffset = alarmOffset;
    }

    public String getAlarmTypeStr() {
        return alarmTypeStr;
    }

    public void setAlarmTypeStr(String alarmTypeStr) {
        this.alarmTypeStr = alarmTypeStr;
    }

    public String getEventTimeStr() {
        return EventTimeStr;
    }

    public void setEventTimeStr(String eventTimeStr) {
        EventTimeStr = eventTimeStr;
    }

    public Double getMaxAnalogValue() {
        return MaxAnalogValue;
    }

    public void setMaxAnalogValue(Double maxAnalogValue) {
        MaxAnalogValue = maxAnalogValue;
    }

    @Override
    public String toString() {
        return "LabelNodeBean{" +
                "TagValueType=" + TagValueType +
                ", MaxAnalogValue=" + MaxAnalogValue +
                ", Unit=" + Unit +
                ", RealValue=" + RealValue +
                ", AlalogOffset='" + AlalogOffset + '\'' +
                ", StandardHight='" + StandardHight + '\'' +
                ", StandarLower='" + StandarLower + '\'' +
                ", TranslateValue='" + TranslateValue + '\'' +
                ", isAlarmState=" + isAlarmState +
                ", nowAlarmType='" + nowAlarmType + '\'' +
                ", AlarmMessage='" + AlarmMessage + '\'' +
                ", AreaName='" + AreaName + '\'' +
                ", EventType=" + EventType +
                ", AlarmValue='" + AlarmValue + '\'' +
                ", AlarmOffset='" + AlarmOffset + '\'' +
                ", alarmTypeStr='" + alarmTypeStr + '\'' +
                ", colorState=" + colorState +
                ", EventTimeStr='" + EventTimeStr + '\'' +
                '}';
    }
}

