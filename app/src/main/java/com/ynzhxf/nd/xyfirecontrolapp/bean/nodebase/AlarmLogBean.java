package com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase;

import com.ynzhxf.nd.xyfirecontrolapp.bean.BaseDataBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.dict.BaseDictBean;

/**
 * 标签和火灾报警记录
 * Created by nd on 2018-07-27.
 */

public class AlarmLogBean extends BaseDataBean {
    /// <summary>
    /// 报警基础节点ID
    /// </summary>
    private String BaseNodeID;

    /// <summary>
    ///报警类型名称
    /// </summary>
    private String SubconditionName;

    /// <summary>
    /// 报警值
    /// </summary>
    private String AlarmValue ;


    /// <summary>
    /// 报警消息
    /// </summary>
    private String Message;

    /// <summary>
    /// 报警区域位置名称
    /// </summary>
    private String AreaName;

    /// <summary>
    /// 严重等级
    /// </summary>
    public Integer Serverity;

    /// <summary>
    /// 连接质量
    /// </summary>
    private String Quality ;

    /// <summary>
    /// 事件类型ID
    /// </summary>
    private String EventTypeID;

    private BaseDictBean EventType;

    /// <summary>
    /// 报警主机ID
    /// </summary>
    private String HostID;

    /// <summary>
    /// 报警主机编号
    /// </summary>
    public Integer HostNum;

    /// <summary>
    /// 报警住居描述
    /// </summary>
    private String VC_HostDec;

    /// <summary>
    /// 用户自定义ID
    /// </summary>
    private String UserID;

    /// <summary>
    /// 回路ID
    /// </summary>
    private Integer LoopID;

    /// <summary>
    /// 设备ID
    /// </summary>
    public Integer PointId;

    /// <summary>
    /// 设备名称
    /// </summary>
    private String pointType;

    /// <summary>
    /// 事件源类型
    /// </summary>
    private Integer EventSource;//产生记录的设备类型  1为主机 2为设备点 3 为普通标签点


    /// <summary>
    /// 记录是否已推送标识
    /// </summary>
    private boolean isPushCheck;

    /**
     * 报警时间字符串
     */
    private String EventTimeStr;
    //处理状态
    private Integer ReviewState;
    //处理状态字符
    private String ReviewStateStr;

    private String ProjectID;

    public String getProjectID() {
        return ProjectID;
    }

    public void setProjectID(String projectID) {
        ProjectID = projectID;
    }

    public String getReviewStateStr() {
        return ReviewStateStr;
    }

    public Integer getReviewState() {
        return ReviewState;
    }

    public void setReviewState(Integer reviewState) {
        ReviewState = reviewState;
    }

    public void setReviewStateStr(String reviewStateStr) {
        ReviewStateStr = reviewStateStr;
    }

    public String getBaseNodeID() {
        return BaseNodeID;
    }

    public void setBaseNodeID(String baseNodeID) {
        BaseNodeID = baseNodeID;
    }

    public String getSubconditionName() {
        return SubconditionName;
    }

    public void setSubconditionName(String subconditionName) {
        SubconditionName = subconditionName;
    }

    public String getAlarmValue() {
        return AlarmValue;
    }

    public void setAlarmValue(String alarmValue) {
        AlarmValue = alarmValue;
    }


    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getAreaName() {
        return AreaName;
    }

    public void setAreaName(String areaName) {
        AreaName = areaName;
    }

    public Integer getServerity() {
        return Serverity;
    }

    public void setServerity(Integer serverity) {
        Serverity = serverity;
    }

    public String getQuality() {
        return Quality;
    }

    public void setQuality(String quality) {
        Quality = quality;
    }

    public String getEventTypeID() {
        return EventTypeID;
    }

    public void setEventTypeID(String eventTypeID) {
        EventTypeID = eventTypeID;
    }

    public BaseDictBean getEventType() {
        return EventType;
    }

    public void setEventType(BaseDictBean eventType) {
        EventType = eventType;
    }

    public String getHostID() {
        return HostID;
    }

    public void setHostID(String hostID) {
        HostID = hostID;
    }

    public Integer getHostNum() {
        return HostNum;
    }

    public void setHostNum(Integer hostNum) {
        HostNum = hostNum;
    }

    public String getVC_HostDec() {
        return VC_HostDec;
    }

    public void setVC_HostDec(String VC_HostDec) {
        this.VC_HostDec = VC_HostDec;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public Integer getLoopID() {
        return LoopID;
    }

    public void setLoopID(Integer loopID) {
        LoopID = loopID;
    }

    public Integer getPointId() {
        return PointId;
    }

    public void setPointId(Integer pointId) {
        PointId = pointId;
    }

    public String getPointType() {
        return pointType;
    }

    public void setPointType(String pointType) {
        this.pointType = pointType;
    }

    public Integer getEventSource() {
        return EventSource;
    }

    public void setEventSource(Integer eventSource) {
        EventSource = eventSource;
    }

    public boolean isPushCheck() {
        return isPushCheck;
    }

    public void setPushCheck(boolean pushCheck) {
        isPushCheck = pushCheck;
    }

    public String getEventTimeStr() {
        return EventTimeStr;
    }

    public void setEventTimeStr(String eventTimeStr) {
        EventTimeStr = eventTimeStr;
    }

    @Override
    public String toString() {
        return "AlarmLogBean{" +
                "BaseNodeID='" + BaseNodeID + '\'' +
                ", SubconditionName='" + SubconditionName + '\'' +
                ", AlarmValue='" + AlarmValue + '\'' +
                ", Message='" + Message + '\'' +
                ", AreaName='" + AreaName + '\'' +
                ", Serverity=" + Serverity +
                ", Quality='" + Quality + '\'' +
                ", EventTypeID='" + EventTypeID + '\'' +
                ", EventType=" + EventType +
                ", HostID='" + HostID + '\'' +
                ", HostNum=" + HostNum +
                ", VC_HostDec='" + VC_HostDec + '\'' +
                ", UserID='" + UserID + '\'' +
                ", LoopID=" + LoopID +
                ", PointId=" + PointId +
                ", pointType='" + pointType + '\'' +
                ", EventSource=" + EventSource +
                ", isPushCheck=" + isPushCheck +
                ", EventTimeStr='" + EventTimeStr + '\'' +
                ", ReviewState=" + ReviewState +
                ", ReviewStateStr='" + ReviewStateStr + '\'' +
                ", ProjectID='" + ProjectID + '\'' +
                '}';
    }
}
